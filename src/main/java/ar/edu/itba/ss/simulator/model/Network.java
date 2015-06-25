package ar.edu.itba.ss.simulator.model;

import java.util.Arrays;
import java.util.List;

import ar.edu.itba.ss.simulator.Statistics;
import ar.edu.itba.ss.simulator.model.device.Router;
import ar.edu.itba.ss.simulator.model.device.User;
import ar.edu.itba.ss.simulator.model.event.ExponentialDistribution;

import com.google.common.collect.Lists;

public class Network extends TimedObject {

	public void run() throws InterruptedException {
		int simulationDelay = 10;
		int maxTime = 1000;
		double lambda = 0.05;

		List<TimedObject> timedObjects = Lists.newLinkedList();

		User receiver = new User(null, new ExponentialDistribution(0.5));
		Channel c4 = new Channel(receiver, 100, 10, 20);
		Router r3 = new Router(c4);
		Channel c3 = new Channel(r3, 100, 10, 1);
		Router r2 = new Router(c3);
		Channel c2 = new Channel(r2, 100, 10, 20);
		Router r1 = new Router(c2);

		Channel cs1 = new Channel(r1, 100, 10, 20);
		Channel cs2 = new Channel(r1, 100, 10, 20);
		Channel cs3 = new Channel(r1, 100, 10, 20);

		User sender1 = new User(cs1, new ExponentialDistribution(lambda));
		User sender2 = new User(cs2, new ExponentialDistribution(lambda));
		User sender3 = new User(cs3, new ExponentialDistribution(lambda));

		timedObjects.addAll(Arrays.asList(sender1, sender2, sender3, cs1, cs2,
				cs3, r1, c2, r2, c3, r3, c4, receiver));

		for (long i = 0; i < maxTime; i++) {
			Thread.sleep(simulationDelay);
			timedObjects.forEach(to -> to.tick());
		}

		System.out.println("Transfered Bytes: "
				+ Statistics.getTransferedBytes());
		System.out.println("Transfered Packets: "
				+ Statistics.getTransferedPackets());
		System.out.println("Byterate: " + Statistics.getByterate()
				+ " bytes/t.u.");
		System.out.println("Latency: " + Statistics.getAverageLatency()
				+ " t.u.");
		System.out.println("Untransferred packets: "
				+ Statistics.getUntransferredPackets());
	}

}
