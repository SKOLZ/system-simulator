import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


public class Device {

	private List<AppProfile> profiles;
	private Router next;
	private int time;
	
	private List<Channel> channels; // hack..

	public Device(List<AppProfile> profiles, Router next) {
		this.profiles = profiles;
		this.next = next;
		this.channels = new LinkedList<Channel>();
	}
	
	public void update(int time) {
		this.time = time;
		for (AppProfile app :profiles)
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
		((PacketRouter)next).receivePacket(new Packet(this, reqSize, time), 0);
	}
	
	private void sendCircuitRequest(int reqSize) {
		int delay = 0;
		Router cur = next;
		while (cur != null) {
			delay += cur.latency;
			cur = cur.next;
		}
		
		Channel chn = new Channel(this, reqSize, reqSize/next.concurCoef, delay);
		if (((CircuitRouter)next).setUpChannel(chn, time))
			channels.add(chn);
	}
	
}
