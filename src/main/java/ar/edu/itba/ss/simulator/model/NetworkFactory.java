package ar.edu.itba.ss.simulator.model;

import java.util.Arrays;
import java.util.List;

import ar.edu.itba.ss.simulator.model.device.Device;
import ar.edu.itba.ss.simulator.model.device.Router;
import ar.edu.itba.ss.simulator.model.device.User;
import ar.edu.itba.ss.simulator.model.event.ExponentialDistribution;

import com.google.common.collect.Lists;

public class NetworkFactory {

	public static Network getPacketNetwork(int bandwidth, int transferRate,
			double lambda, int users, int routers, int channels) {

		List<TimedObject> routerObjs = Lists.newLinkedList();
		Router next = getPacketRouters(routerObjs, routers - 1, bandwidth,
				transferRate, channels);
		Channel c2 = new PacketChannel(next, bandwidth, transferRate, channels);
		Router r1 = new Router(c2);

		List<TimedObject> objects = Lists.newLinkedList();

		for (int i = 0; i < users; i++) {
			objects.addAll(getPacketUser(r1, bandwidth, transferRate, lambda,
					channels));
		}

		objects.addAll(Arrays.asList(r1, c2));
		objects.addAll(routerObjs);

		return new Network(objects);
	}

	public static Router getPacketRouters(List<TimedObject> objs, int routers,
			int bandwidth, int transferRate, int channels) {
		User receiver = new User(null, new ExponentialDistribution(0.5));
		Device aux = receiver;
		for (int i = 0; i < routers; i++) {
			Channel c3 = new PacketChannel(aux, bandwidth, transferRate,
					channels);
			Router r2 = new Router(c3);
			objs.add(r2);
			objs.add(c3);
			aux = r2;
		}
		objs.add(receiver);
		return (Router) aux;
	}

	private static List<TimedObject> getPacketUser(Router r, int bandwidth,
			int transferRate, double lambda, int channels) {
		Channel cs1 = new PacketChannel(r, bandwidth, transferRate, channels);
		User sender1 = new User(cs1, new ExponentialDistribution(lambda));
		return Arrays.asList(sender1, cs1);
	}

	public static Network getCircuitNetwork(int bandwidth, int transferRate,
			double lambda, int users, int routers) {
		List<TimedObject> routerObjs = Lists.newLinkedList();
		Router next = getCircuitRouters(routerObjs, routers - 1, bandwidth,
				transferRate);
		Channel c2 = new CircuitChannel(next, bandwidth, transferRate);
		Router r1 = new Router(c2);

		List<TimedObject> objects = Lists.newLinkedList();

		for (int i = 0; i < users; i++) {
			objects.addAll(getCircuitUser(r1, bandwidth, transferRate, lambda));
		}

		objects.addAll(Arrays.asList(r1, c2));
		objects.addAll(routerObjs);

		return new Network(objects);
	}

	public static Router getCircuitRouters(List<TimedObject> objs, int routers,
			int bandwidth, int transferRate) {
		User receiver = new User(null, new ExponentialDistribution(0.5));
		Device aux = receiver;
		for (int i = 0; i < routers; i++) {
			Channel c3 = new CircuitChannel(aux, bandwidth, transferRate);
			Router r2 = new Router(c3);
			objs.add(r2);
			objs.add(c3);
			aux = r2;
		}
		objs.add(receiver);
		return (Router) aux;
	}

	private static List<TimedObject> getCircuitUser(Router r, int bandwidth,
			int transferRate, double lambda) {
		Channel cs1 = new CircuitChannel(r, bandwidth, transferRate);
		User sender1 = new User(cs1, new ExponentialDistribution(lambda));
		return Arrays.asList(sender1, cs1);
	}

}
