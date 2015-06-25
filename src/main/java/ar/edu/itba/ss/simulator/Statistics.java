package ar.edu.itba.ss.simulator;

import java.util.Map;

import ar.edu.itba.ss.simulator.model.Data;
import ar.edu.itba.ss.simulator.model.Message;

import com.google.common.collect.Maps;

public class Statistics {

	private static int time = 0;

	private static final Map<Message, Long> messagesSent = Maps.newHashMap();

	private static int transferedBytes = 0;
	private static int transferedPackets = 0;
	private static double byterate = 0.0;
	private static double latency = 0.0;
	private static double usagePercentage = 0.0;

	public static void tick() {
		time++;
	}

	public static void logUsage(double percentage) {
		if (percentage > 100) {
			percentage = 100;
		}
		usagePercentage = ((usagePercentage * time) + percentage) / (time + 1);
	}

	public static void logMessage(Message message, long time) {
		messagesSent.put(message, time);
	}

	public static void logReceived(Data data, long time) {
		Message message = data.getMessage();
		if (!message.isTransferred())
			return;

		long elapsedTime = (time - messagesSent.get(message));
		double bytesPerTimeUnit = (double) message.getSize() / elapsedTime;

		byterate = ((byterate * transferedPackets) + bytesPerTimeUnit)
				/ (transferedPackets + 1);
		latency = ((latency * transferedPackets) + elapsedTime)
				/ (transferedPackets + 1);

		transferedBytes += message.getSize();
		transferedPackets++;

		// System.out.println("Received: " + message + "in " + elapsedTime
		// + " t.u. --> at " + bytesPerTimeUnit + " bytes/t.u.");

		messagesSent.remove(message);
	}

	public static int getUntransferredPackets() {
		return messagesSent.size();
	}

	public static int getTransferedBytes() {
		return transferedBytes;
	}

	public static int getTransferedPackets() {
		return transferedPackets;
	}

	public static double getByterate() {
		return byterate;
	}

	public static double getAverageLatency() {
		return latency;
	}

	public static double getNetworkUsageAverage() {
		return usagePercentage;
	}

	public static void reset() {
		messagesSent.clear();
		transferedBytes = 0;
		transferedPackets = 0;
		byterate = 0.0;
		latency = 0.0;
		usagePercentage = 0.0;
		time = 0;
	}

}
