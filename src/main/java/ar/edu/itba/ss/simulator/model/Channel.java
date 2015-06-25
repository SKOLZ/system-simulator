package ar.edu.itba.ss.simulator.model;

import java.util.Map;

import ar.edu.itba.ss.simulator.model.device.Device;

import com.google.common.collect.Maps;

public class Channel extends TimedObject {

	private Map<Data, Long> currentData = Maps.newHashMap();

	private Device endpoint;
	private int channelSize;
	private int transferRate;
	private int channels;

	public Channel(Device endpoint, int channelSize, int channels,
			int transferRate) {
		super();
		this.endpoint = endpoint;
		this.channelSize = channelSize;
		this.channels = channels;
		this.transferRate = transferRate;
	}

	public boolean isAvailable(int bytes) {
		return currentData.size() < channels && bytes <= channelSize;
	}

	public boolean transfer(Data data) {
		if (isAvailable(data.getSize())) {
			this.currentData.put(data, 0L);
			return true;
		}
		return false;
	}

	@Override
	public void tick() {
		super.tick();
		Map<Data, Long> auxMap = Maps.newHashMap();
		currentData.forEach((data, elapsedTime) -> {
			elapsedTime++;
			if (data.getSize() <= elapsedTime * transferRate) {
				endpoint.receive(data);
			} else {
				auxMap.put(data, elapsedTime);
			}
		});
		this.currentData = auxMap;
	}

	public int getChannelSize() {
		return channelSize;
	}

}
