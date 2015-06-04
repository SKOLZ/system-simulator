
public class Channel {

	public Device source;
	public int reqSize;
	public int channelSize;
	public int ttl;
	
	public Channel(Device source, int reqSize, int channelSize, int extraDelay) {
		this.source = source;
		this.reqSize = reqSize;
		this.channelSize = channelSize;
		this.ttl = (int)Math.ceil(reqSize/(double)channelSize) + extraDelay;
	}
	
}
