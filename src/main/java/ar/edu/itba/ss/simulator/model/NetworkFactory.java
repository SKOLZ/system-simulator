package ar.edu.itba.ss.simulator.model;

import java.util.Arrays;
import java.util.List;

import ar.edu.itba.ss.simulator.model.device.Router;
import ar.edu.itba.ss.simulator.model.device.User;
import ar.edu.itba.ss.simulator.model.event.ExponentialDistribution;

import com.google.common.collect.Lists;

public class NetworkFactory {

	public static Network getPacketNetwork(int bandwidth, int transferRate,
			double lambda, int users, int routers, int channels) {
		User receiver = new User(null, new ExponentialDistribution(0.5));
		Channel c4 = new PacketChannel(receiver, bandwidth, transferRate, channels);
		Router r3 = new Router(c4);
		Channel c3 = new PacketChannel(r3, bandwidth, transferRate, channels);
		Router r2 = new Router(c3);
		Channel c2 = new PacketChannel(r2, bandwidth, transferRate, channels);
		Router r1 = new Router(c2);

		List<TimedObject> objects = Lists.newLinkedList();

		for (int i = 0; i < 12; i++) {
			objects.addAll(getPacketUser(r1, bandwidth, transferRate, lambda, channels));
		}

		objects.addAll(Arrays.asList(r1, c2, r2, c3, r3, c4, receiver));

		return new Network(objects);
	}

	public static Network getCircuitNetwork(int bandwidth, int transferRate,
			double lambda, int users, int routers) {
		User receiver = new User(null, new ExponentialDistribution(0.5));
		Channel c4 = new CircuitChannel(receiver, 1000, 200);
		Router r3 = new Router(c4);
		Channel c3 = new CircuitChannel(r3, 1000, 200);
		Router r2 = new Router(c3);
		Channel c2 = new CircuitChannel(r2, 1000, 200);
		Router r1 = new Router(c2);

		List<TimedObject> objects = Lists.newLinkedList();

		for (int i = 0; i < users; i++) {
			objects.addAll(getCircuitUser(r1, bandwidth, transferRate, lambda));
		}

		objects.addAll(Arrays.asList(r1, c2, r2, c3, r3, c4, receiver));

		return new Network(objects);
	}

	private static List<TimedObject> getCircuitUser(Router r, int bandwidth,
			int transferRate, double lambda) {
		Channel cs1 = new CircuitChannel(r, bandwidth, transferRate);
		User sender1 = new User(cs1, new ExponentialDistribution(lambda));
		return Arrays.asList(sender1, cs1);
	}

	private static List<TimedObject> getPacketUser(Router r, int bandwidth,
			int transferRate, double lambda, int channels) {
		Channel cs1 = new PacketChannel(r, bandwidth, transferRate, channels);
		User sender1 = new User(cs1, new ExponentialDistribution(lambda));
		return Arrays.asList(sender1, cs1);
	}

}
