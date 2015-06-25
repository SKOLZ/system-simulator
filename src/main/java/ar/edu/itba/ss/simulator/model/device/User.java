package ar.edu.itba.ss.simulator.model.device;

import java.util.Queue;

import ar.edu.itba.ss.simulator.Statistics;
import ar.edu.itba.ss.simulator.model.Channel;
import ar.edu.itba.ss.simulator.model.Data;
import ar.edu.itba.ss.simulator.model.Message;
import ar.edu.itba.ss.simulator.model.event.ExponentialDistribution;

import com.google.common.collect.Lists;

public class User extends Device {

	private static final Integer MAX_MESSAGE_SIZE = 100;

	private Queue<Data> receivedData;
	private ExponentialDistribution eventDistribution;
	private int waitingTime = 0;

	public User(Channel channel, ExponentialDistribution eventDistribution) {
		super(channel);
		this.eventDistribution = eventDistribution;
		this.receivedData = Lists.newLinkedList();
	}

	@Override
	public void receive(Data data) {
		this.receivedData.add(data);
		super.tick();
	}

	@Override
	public void tick() {
		super.tick();
		if (this.getChannel() == null) {
			while (!receivedData.isEmpty()) {
				Data received = receivedData.remove();
				received.transferred();
				Statistics.logReceived(received, this.getTime());
			}
		} else {
			if (waitingTime == 0) {
				Message message = new Message((int) (Math.random()
						* MAX_MESSAGE_SIZE + 1), this.getChannel()
						.getChannelSize());
				Statistics.logMessage(message, this.getTime());
				for (Data data : message.getDatagrams()) {
					Statistics.logSent(data, this.getTime());
					this.getChannel().transfer(data);
				}
				waitingTime = this.eventDistribution
						.waitingTime(this.getTime());
			} else {
				waitingTime--;
			}
		}
	}
}
