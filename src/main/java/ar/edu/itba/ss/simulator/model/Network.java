package ar.edu.itba.ss.simulator.model;

import java.util.List;

import ar.edu.itba.ss.simulator.Statistics;

import com.google.common.collect.Lists;

public class Network extends TimedObject {

	private List<TimedObject> timedObjects = Lists.newLinkedList();

	public Network(List<TimedObject> timedObjects) {
		super();
		this.timedObjects = timedObjects;
	}

	public void run() throws InterruptedException {

		timedObjects.forEach(to -> to.tick());
		Statistics.tick();

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
		System.out.println("Network usage average: "
				+ Statistics.getNetworkUsageAverage() + "%");
	}

	public List<TimedObject> getTimedObjects() {
		return timedObjects;
	}

}
