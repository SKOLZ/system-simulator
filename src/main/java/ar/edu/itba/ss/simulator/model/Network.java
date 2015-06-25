package ar.edu.itba.ss.simulator.model;

import java.util.List;

import ar.edu.itba.ss.simulator.Statistics;

import com.google.common.collect.Lists;

public class Network extends TimedObject {
	
	private int simulationDelay; 
	private int maxTime;
	private List<TimedObject> timedObjects = Lists.newLinkedList();
	
	public Network(int simulationDelay, int maxTime,
			List<TimedObject> timedObjects) {
		super();
		this.simulationDelay = simulationDelay;
		this.maxTime = maxTime;
		this.timedObjects = timedObjects;
	}

	public void run() throws InterruptedException {
		for (long i = 0; i < maxTime; i++) {
			Thread.sleep(simulationDelay);
			timedObjects.forEach(to -> to.tick());
			Statistics.tick();
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
		System.out.println("Network usage average: "
				+ Statistics.getNetworkUsageAverage() + "%");
	}

}
