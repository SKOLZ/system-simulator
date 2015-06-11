import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

public class Device {

	private static final Logger LOGGER = LoggerFactory.getLogger(Device.class);

	private String name;
	private List<AppProfile> profiles;
	private Router next;
	private int time;

	private List<Channel> channels; // hack..
	private Queue<Channel> queuedChannel;

	public Device(String name, List<AppProfile> profiles, Router next) {
		this.name = name;
		this.profiles = profiles;
		this.next = next;
		this.channels = Lists.newLinkedList();
		this.queuedChannel = Lists.newLinkedList();
		LOGGER.info("Created device with name :" + name);
	}

	public void update(int time) {
		this.time = time;

		while (!queuedChannel.isEmpty()) {
			Channel polledChannel = queuedChannel.poll();
			if (polledChannel == null || !sendRequest(polledChannel.reqSize))
				break;
		}

		for (AppProfile app : profiles)
			app.update(time);

		channels = channels.stream().filter(c -> {
			if (--c.ttl <= 0) {
				return false;
			}
			return true;
		}).collect(Collectors.toList());
	}

	public boolean sendRequest(int reqSize) {
		boolean ret = false;
		if (next.getClass() == PacketRouter.class)
			ret = sendPacketRequest(reqSize);
		else if (next.getClass() == CircuitRouter.class)
			ret = sendCircuitRequest(reqSize);
		return ret;
	}

	private boolean sendPacketRequest(int reqSize) {
		((PacketRouter) next).receivePacket(new Packet(this, reqSize, time), 0);
		return true;
	}

	private boolean sendCircuitRequest(int reqSize) {
		int delay = 0;
		Router cur = next;
		while (cur != null) {
			delay += cur.latency;
			cur = cur.next;
		}

		boolean queued = false;

		Channel chn = new Channel(this, reqSize, reqSize / next.concurCoef,
				delay);
		if (((CircuitRouter) next).setUpChannel(chn, time)) {
			channels.add(chn);
			StatisticsModule.getInstance().logStartedRequest(this);
		} else {
			queuedChannel.offer(chn);
			queued = true;
		}
		return !queued;
	}

	@Override
	public String toString() {
		return "Device [name=" + name + "]";
	}

}
