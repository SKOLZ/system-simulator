import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;

public class StatisticsModule {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(StatisticsModule.class);

	private static StatisticsModule instance;

	public static StatisticsModule getInstance() {
		if (instance == null)
			instance = new StatisticsModule();
		return instance;
	}

	private Map<Device, Integer> attemptedRequests = Maps.newHashMap();
	private Map<Device, Integer> startedRequests = Maps.newHashMap();
	private Map<Device, Integer> completedRequests = Maps.newHashMap();
	private Map<Device, Integer> exceededBandwithRequests = Maps.newHashMap();

	public void logExceededBandwithRequest(Device device) {
		this.logRequestOnMap(exceededBandwithRequests, device);
	}

	public void logStartedRequest(Device device) {
		this.logRequestOnMap(startedRequests, device);
	}

	public void logCompletedRequest(Device device) {
		this.logRequestOnMap(completedRequests, device);
	}

	public void logAttemptedRequest(Device device) {
		this.logRequestOnMap(attemptedRequests, device);
	}

	public void printExceededBandwithRequestsPerDevice() {
		this.printRequestStatistics(exceededBandwithRequests,
				"exceeded bandwith");
	}

	public void printStartedRequestsPerDevice() {
		this.printRequestStatistics(startedRequests, "started");
	}

	public void printCompletedRequestsPerDevice() {
		this.printRequestStatistics(completedRequests, "completed");
	}

	public void printAttemptedRequestsPerDevice() {
		this.printRequestStatistics(attemptedRequests, "attempted");
	}

	public void logIdleRouter(int curTime, Router router) {
		// TODO: Implement...
	}

	public void logReceivedReq(int curTime, Device source, int delay, int size) {
		// TODO: Implement...
	}

	public void logLostReq(int curTime, Device source, int size) {
		// TODO: Implement...
	}

	private void logRequestOnMap(Map<Device, Integer> map, Device device) {
		map.putIfAbsent(device, 0);
		map.put(device, map.get(device) + 1);
	}

	private void printRequestStatistics(Map<Device, Integer> map, String message) {
		for (Entry<Device, Integer> entry : map.entrySet()) {
			LOGGER.info(entry.getKey() + " " + message + " requests: "
					+ entry.getValue());
		}
	}

}
