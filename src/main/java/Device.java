import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Device {

	private static final Logger LOGGER = LoggerFactory.getLogger(Device.class);

	private String name;
	private List<AppProfile> profiles;
	private Router next;
	private int time;

	private List<Channel> channels; // hack..

	public Device(String name, List<AppProfile> profiles, Router next) {
		this.name = name;
		this.profiles = profiles;
		this.next = next;
		this.channels = new LinkedList<Channel>();
		LOGGER.info("Created device with name :" + name);
	}

	public void update(int time) {
		this.time = time;
		for (AppProfile app : profiles)
			app.update(time);

		Iterator<Channel> it = channels.iterator();
		while (it.hasNext()) {
			Channel chn = it.next();
			chn.ttl--;

			if (chn.ttl <= 0)
				it.remove();
		}
	}

	public void sendRequest(int reqSize) {
		if (next.getClass() == PacketRouter.class)
			sendPacketRequest(reqSize);
		else if (next.getClass() == CircuitRouter.class)
			sendCircuitRequest(reqSize);
	}

	private void sendPacketRequest(int reqSize) {
		((PacketRouter) next).receivePacket(new Packet(this, reqSize, time), 0);
	}

	private void sendCircuitRequest(int reqSize) {
		int delay = 0;
		Router cur = next;
		while (cur != null) {
			delay += cur.latency;
			cur = cur.next;
		}

		Channel chn = new Channel(this, reqSize, reqSize / next.concurCoef,
				delay);
		StatisticsModule.getInstance().logAttemptedRequest(this);
		((CircuitRouter) next).setUpChannel(chn, time);
		channels.add(chn);
		// if (((CircuitRouter) next).setUpChannel(chn, time)) {
		// // StatisticsModule.getInstance().logStartedRequest(this);
		// channels.add(chn);
		// }
	}

	@Override
	public String toString() {
		return "Device [name=" + name + "]";
	}

}
