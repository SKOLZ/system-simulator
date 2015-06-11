import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class PacketRouter extends Router {

	private List<PacketDelayPair> transmitting;
	private Queue<Packet> packetsQueue;
	private Packet current;
	private int transmittedBytes;

	public PacketRouter(String name, PacketRouter next, int bandwidth,
			int latency, int concurCoef) {
		super(name, next, bandwidth, latency, concurCoef);
		packetsQueue = new LinkedList<Packet>();
	}

	@Override
	public void update(int time) {

		transmissionUpdate(time);
		forwardingUpdate(time);

	}

	private void transmissionUpdate(int time) {
		Iterator<PacketDelayPair> it = transmitting.iterator();
		while (it.hasNext()) {
			PacketDelayPair pdp = it.next();
			pdp.delay--;

			if (pdp.delay <= 0) {
//				if (packetsQueue.size() < concurCoef)
//					packetsQueue.add(pdp.packet);
//				else
//					StatisticsModule.getInstance().logLostReq(time,
//							pdp.packet.source, pdp.packet.size);
				it.remove();
			}
		}
	}

	private void forwardingUpdate(int time) {

		if (current != null) {
			transmittedBytes += super.bandwidth;
			if (transmittedBytes >= current.size) {
//				if (next == null)
//					StatisticsModule.getInstance().logReceivedReq(
//							time + latency, current.source,
//							current.received(time + latency), current.size);
//				else
					forwardCurPacket();
			}
		} else if (!packetsQueue.isEmpty()) {
			current = packetsQueue.poll();
		} else {
//			StatisticsModule.getInstance().logIdleRouter(time, this);
		}
	}

	public void forwardCurPacket() {
		((PacketRouter) next).receivePacket(current, latency);
	}

	public void receivePacket(Packet packet, int delay) {
		transmitting.add(new PacketDelayPair(packet, delay));
	}

	public class PacketDelayPair {
		public Packet packet;
		public int delay;

		public PacketDelayPair(Packet packet, int delay) {
			this.packet = packet;
			this.delay = delay;
		}
	}

}
