package ar.edu.itba.ss.simulator.model.device;

import java.util.Queue;

import ar.edu.itba.ss.simulator.model.Channel;
import ar.edu.itba.ss.simulator.model.Data;

import com.google.common.collect.Lists;

public class Router extends Device {

	private Queue<Data> dataQueue = Lists.newLinkedList();

	public Router(Channel channel) {
		super(channel);
	}

	public void receive(Data data) {
		this.send(data);
	}

	@Override
	public void tick() {
		super.tick();
		Queue<Data> auxQueue = Lists.newLinkedList();
		while (!this.dataQueue.isEmpty()) {
			Data data = dataQueue.poll();
			if (!this.send(data)) {
				auxQueue.add(data);
			}
		}
		this.dataQueue.addAll(auxQueue);
	}

	private boolean send(Data data) {
		Channel channel = this.getChannel();
		if (!channel.isAvailable(data.getSize()) || !channel.transfer(data)) {
			return false;
		}
		return false;
	}

}
