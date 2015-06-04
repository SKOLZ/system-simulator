
public class Packet {

	public Device source;
	public int size;
	private int sentTime;
	
	public Packet(Device source, int size, int sentTime) {
		this.source = source;
		this.size = size;
		this.sentTime = sentTime;
	}
	
	public int received(int receivedTime) {
		return receivedTime - sentTime;
	}
	
}
