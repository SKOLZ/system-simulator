import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;

public class CircuitRouter extends Router {

	private List<Channel> channels;
	private Queue<Channel> queuedChannels;

	public CircuitRouter(String name, Router next, int bandwidth, int latency,
			int concurCoef) {
		super(name, next, bandwidth, latency, concurCoef);
		channels = new LinkedList<Channel>();
		queuedChannels = Lists.newLinkedList();
	}

	@Override
	public void update(int time) {
		final AtomicInteger releasedBandwith = new AtomicInteger(0);
		channels = channels.stream().filter(c -> {
			if (c.ttl <= 0) {
				releasedBandwith.addAndGet(c.channelSize);
				StatisticsModule.getInstance().logCompletedRequest(c.source);
			}
			return c.ttl > 0;
		}).collect(Collectors.toList());
		
		int bandwith = releasedBandwith.get();
		while (bandwith > 0 && !queuedChannels.isEmpty()) {
			Channel polled = queuedChannels.poll();
			if (polled != null && polled.channelSize <= bandwith) {
				this.setUpChannel(polled, time);
				bandwith -= polled.channelSize;
			}
		}
	}

	public void setUpChannel(Channel chn, int time) {
		if (chn == null)
			return;
		int sum = chn.channelSize;
		for (Channel each : channels)
			sum += each.channelSize;

		if (bandwidth >= sum) {
			if (next != null) {
				((CircuitRouter) next).setUpChannel(chn, time);
			}
			channels.add(chn);
		} else {
			StatisticsModule.getInstance().logExceededBandwithRequest(
					chn.source);
			if (!this.queuedChannels.offer(chn)) {
				StatisticsModule.getInstance()
						.logLostReq(time, chn.source, sum);
			}
		}
	}

}
