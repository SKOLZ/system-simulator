import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class CircuitRouter extends Router {

	private List<Channel> channels;
	
	public CircuitRouter(Router next, int bandwidth, int latency, int concurCoef) {
		super(next, bandwidth, latency, concurCoef);
		channels = new LinkedList<Channel>();
	}

	@Override
	public void update(int time) {
		Iterator<Channel> it = channels.iterator();
		
		while (it.hasNext()) {
			Channel chn = it.next();
			if (chn.ttl <= 0)
				it.remove();				
		}
		
	}
	
	public boolean setUpChannel(Channel chn, int time) {
		int sum = chn.channelSize;
		for (Channel each :channels)
			sum += each.channelSize;
		
		if (bandwidth >= sum) {
			if (next != null) {
				if (((CircuitRouter)next).setUpChannel(chn, time)) {
					channels.add(chn);
				} else {
					StatisticsModule.getInstance().logLostReq(time, chn.source, chn.reqSize);
					return false;
				}
			} else { 
				channels.add(chn);
			}
			return true;
		} else {
			return false;
		}
	}

}
