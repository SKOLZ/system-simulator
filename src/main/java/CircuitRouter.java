import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;

public class CircuitRouter extends Router {

	private List<Channel> channels;

	public CircuitRouter(String name, Router next, int bandwidth, int latency,
			int concurCoef) {
		super(name, next, bandwidth, latency, concurCoef);
		channels = Lists.newLinkedList();
	}

	@Override
	public void update(int time) {
		channels = channels.stream().filter(c -> {
			if (c.ttl <= 0) {
				StatisticsModule.getInstance().logCompletedRequest(c.source);
				return false;
			}
			return true;
		}).collect(Collectors.toList());
	}

	public boolean setUpChannel(Channel chn, int time) {
		int sum = chn.channelSize;
		for (Channel each : channels)
			sum += each.channelSize;

		if (bandwidth >= sum) {
			if (next != null) {
				if (!((CircuitRouter) next).setUpChannel(chn, time)) {
					return false;
				}
			}
			channels.add(chn);
			return true;
		} else {
			StatisticsModule.getInstance().logExceededBandwithRequest(
					chn.source);
			return false;
		}
	}

}
