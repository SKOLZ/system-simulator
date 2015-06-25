package ar.edu.itba.ss.simulator.model;

import java.util.Map;
import java.util.Map.Entry;

import ar.edu.itba.ss.simulator.Statistics;
import ar.edu.itba.ss.simulator.model.device.Device;

import com.google.common.collect.Maps;

public class CircuitChannel extends Channel {

	private Device endpoint;
	private int channelSize;
	private int transferRate;
	private Map<Data, Integer> sendingData;

	public CircuitChannel(Device endpoint, int channelSize, int transferRate) {
		super();
		this.endpoint = endpoint;
		this.channelSize = channelSize;
		this.transferRate = transferRate;
		this.sendingData = Maps.newHashMap();
	}

	@Override
	public boolean isAvailable() {
		return true;
	}

	@Override
	public boolean transfer(Data data) {
		if (isAvailable()) {
			sendingData.put(data, 0);
			return true;
		}
		return false;
	}

	@Override
	public void tick() {
		super.tick();
		Map<Data, Integer> auxMap = Maps.newHashMap();
		this.sendingData.forEach((data, elapsedTime) -> {
			elapsedTime++;
			if (data.getSize() <= elapsedTime * transferRate) {
				endpoint.receive(data);
			} else {
				auxMap.put(data, elapsedTime);
			}
		});
		int usedData = 0;
		for (Entry<Data, Integer> entry : this.sendingData.entrySet()) {
			usedData += entry.getKey().getSize();
		}
		Statistics.logUsage(((double) usedData * 100.0) / channelSize);
		this.sendingData = auxMap;
	}

	@Override
	public int getChannelSize() {
		return channelSize;
	}

}
