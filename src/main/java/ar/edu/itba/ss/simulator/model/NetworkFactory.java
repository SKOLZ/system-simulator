package ar.edu.itba.ss.simulator.model;

import java.util.Arrays;

import ar.edu.itba.ss.simulator.model.device.Router;
import ar.edu.itba.ss.simulator.model.device.User;
import ar.edu.itba.ss.simulator.model.event.ExponentialDistribution;

public class NetworkFactory {

	public static Network getPacketNetwork(int simulationDelay, int maxTime,
			double lambda) {
		User receiver = new User(null, new ExponentialDistribution(0.5));
		Channel c4 = new PacketChannel(receiver, 100, 10, 20);
		Router r3 = new Router(c4);
		Channel c3 = new PacketChannel(r3, 100, 10, 20);
		Router r2 = new Router(c3);
		Channel c2 = new PacketChannel(r2, 100, 10, 20);
		Router r1 = new Router(c2);

		Channel cs1 = new CircuitChannel(r1, 100, 20);
		Channel cs2 = new CircuitChannel(r1, 100, 20);
		Channel cs3 = new CircuitChannel(r1, 100, 20);
		Channel cs4 = new CircuitChannel(r1, 100, 20);
		Channel cs5 = new CircuitChannel(r1, 100, 20);
		Channel cs6 = new CircuitChannel(r1, 100, 20);
		Channel cs7 = new CircuitChannel(r1, 100, 20);
		Channel cs8 = new CircuitChannel(r1, 100, 20);
		Channel cs9 = new CircuitChannel(r1, 100, 20);
		Channel cs10 = new CircuitChannel(r1, 100, 20);
		Channel cs11 = new CircuitChannel(r1, 100, 20);
		Channel cs12 = new CircuitChannel(r1, 100, 20);
		Channel cs13 = new CircuitChannel(r1, 100, 20);
		Channel cs14 = new CircuitChannel(r1, 100, 20);
		Channel cs15 = new CircuitChannel(r1, 100, 20);
		Channel cs16 = new CircuitChannel(r1, 100, 20);
		Channel cs17 = new CircuitChannel(r1, 100, 20);
		Channel cs18 = new CircuitChannel(r1, 100, 20);
		Channel cs19 = new CircuitChannel(r1, 100, 20);
		Channel cs20 = new CircuitChannel(r1, 100, 20);
		Channel cs21 = new CircuitChannel(r1, 100, 20);
		Channel cs22 = new CircuitChannel(r1, 100, 20);
		Channel cs23 = new CircuitChannel(r1, 100, 20);
		Channel cs24 = new CircuitChannel(r1, 100, 20);
		Channel cs25 = new CircuitChannel(r1, 100, 20);

		User sender1 = new User(cs1, new ExponentialDistribution(lambda));
		User sender2 = new User(cs2, new ExponentialDistribution(lambda));
		User sender3 = new User(cs3, new ExponentialDistribution(lambda));
		User sender4 = new User(cs4, new ExponentialDistribution(lambda));
		User sender5 = new User(cs5, new ExponentialDistribution(lambda));
		User sender6 = new User(cs6, new ExponentialDistribution(lambda));
		User sender7 = new User(cs7, new ExponentialDistribution(lambda));
		User sender8 = new User(cs8, new ExponentialDistribution(lambda));
		User sender9 = new User(cs9, new ExponentialDistribution(lambda));
		User sender10 = new User(cs10, new ExponentialDistribution(lambda));
		User sender11 = new User(cs11, new ExponentialDistribution(lambda));
		User sender12 = new User(cs12, new ExponentialDistribution(lambda));
		User sender13 = new User(cs13, new ExponentialDistribution(lambda));
		User sender14 = new User(cs14, new ExponentialDistribution(lambda));
		User sender15 = new User(cs15, new ExponentialDistribution(lambda));
		User sender16 = new User(cs16, new ExponentialDistribution(lambda));
		User sender17 = new User(cs17, new ExponentialDistribution(lambda));
		User sender18 = new User(cs18, new ExponentialDistribution(lambda));
		User sender19 = new User(cs19, new ExponentialDistribution(lambda));
		User sender20 = new User(cs20, new ExponentialDistribution(lambda));
		User sender21 = new User(cs21, new ExponentialDistribution(lambda));
		User sender22 = new User(cs22, new ExponentialDistribution(lambda));
		User sender23 = new User(cs23, new ExponentialDistribution(lambda));
		User sender24 = new User(cs24, new ExponentialDistribution(lambda));
		User sender25 = new User(cs25, new ExponentialDistribution(lambda));

		return new Network(simulationDelay, maxTime, Arrays.asList(sender1,
				sender2, sender3, sender4, sender5, sender6, sender7, sender8,
				sender9, sender10, sender11, sender12, sender13, sender14,
				sender15, sender16, sender17, sender18, sender19, sender20,
				sender21, sender22, sender23, sender24, sender25, cs1, cs2,
				cs3, r1, c2, r2, c3, r3, c4, receiver));
	}

	public static Network getCircuitNetwork(int simulationDelay, int maxTime,
			double lambda) {
		User receiver = new User(null, new ExponentialDistribution(0.5));
		Channel c4 = new CircuitChannel(receiver, 100, 20);
		Router r3 = new Router(c4);
		Channel c3 = new CircuitChannel(r3, 100, 20);
		Router r2 = new Router(c3);
		Channel c2 = new CircuitChannel(r2, 100, 20);
		Router r1 = new Router(c2);

		Channel cs1 = new CircuitChannel(r1, 100, 20);
		Channel cs2 = new CircuitChannel(r1, 100, 20);
		Channel cs3 = new CircuitChannel(r1, 100, 20);
		Channel cs4 = new CircuitChannel(r1, 100, 20);
		Channel cs5 = new CircuitChannel(r1, 100, 20);
		Channel cs6 = new CircuitChannel(r1, 100, 20);
		Channel cs7 = new CircuitChannel(r1, 100, 20);
		Channel cs8 = new CircuitChannel(r1, 100, 20);
		Channel cs9 = new CircuitChannel(r1, 100, 20);
		Channel cs10 = new CircuitChannel(r1, 100, 20);
		Channel cs11 = new CircuitChannel(r1, 100, 20);
		Channel cs12 = new CircuitChannel(r1, 100, 20);
		Channel cs13 = new CircuitChannel(r1, 100, 20);
		Channel cs14 = new CircuitChannel(r1, 100, 20);
		Channel cs15 = new CircuitChannel(r1, 100, 20);
		Channel cs16 = new CircuitChannel(r1, 100, 20);
		Channel cs17 = new CircuitChannel(r1, 100, 20);
		Channel cs18 = new CircuitChannel(r1, 100, 20);
		Channel cs19 = new CircuitChannel(r1, 100, 20);
		Channel cs20 = new CircuitChannel(r1, 100, 20);
		Channel cs21 = new CircuitChannel(r1, 100, 20);
		Channel cs22 = new CircuitChannel(r1, 100, 20);
		Channel cs23 = new CircuitChannel(r1, 100, 20);
		Channel cs24 = new CircuitChannel(r1, 100, 20);
		Channel cs25 = new CircuitChannel(r1, 100, 20);

		User sender1 = new User(cs1, new ExponentialDistribution(lambda));
		User sender2 = new User(cs2, new ExponentialDistribution(lambda));
		User sender3 = new User(cs3, new ExponentialDistribution(lambda));
		User sender4 = new User(cs4, new ExponentialDistribution(lambda));
		User sender5 = new User(cs5, new ExponentialDistribution(lambda));
		User sender6 = new User(cs6, new ExponentialDistribution(lambda));
		User sender7 = new User(cs7, new ExponentialDistribution(lambda));
		User sender8 = new User(cs8, new ExponentialDistribution(lambda));
		User sender9 = new User(cs9, new ExponentialDistribution(lambda));
		User sender10 = new User(cs10, new ExponentialDistribution(lambda));
		User sender11 = new User(cs11, new ExponentialDistribution(lambda));
		User sender12 = new User(cs12, new ExponentialDistribution(lambda));
		User sender13 = new User(cs13, new ExponentialDistribution(lambda));
		User sender14 = new User(cs14, new ExponentialDistribution(lambda));
		User sender15 = new User(cs15, new ExponentialDistribution(lambda));
		User sender16 = new User(cs16, new ExponentialDistribution(lambda));
		User sender17 = new User(cs17, new ExponentialDistribution(lambda));
		User sender18 = new User(cs18, new ExponentialDistribution(lambda));
		User sender19 = new User(cs19, new ExponentialDistribution(lambda));
		User sender20 = new User(cs20, new ExponentialDistribution(lambda));
		User sender21 = new User(cs21, new ExponentialDistribution(lambda));
		User sender22 = new User(cs22, new ExponentialDistribution(lambda));
		User sender23 = new User(cs23, new ExponentialDistribution(lambda));
		User sender24 = new User(cs24, new ExponentialDistribution(lambda));
		User sender25 = new User(cs25, new ExponentialDistribution(lambda));

		return new Network(simulationDelay, maxTime, Arrays.asList(sender1,
				sender2, sender3, sender4, sender5, sender6, sender7, sender8,
				sender9, sender10, sender11, sender12, sender13, sender14,
				sender15, sender16, sender17, sender18, sender19, sender20,
				sender21, sender22, sender23, sender24, sender25, cs1, cs2,
				cs3, r1, c2, r2, c3, r3, c4, receiver));
	}

}
