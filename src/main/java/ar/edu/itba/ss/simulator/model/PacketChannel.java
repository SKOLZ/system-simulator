package ar.edu.itba.ss.simulator.model;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import ar.edu.itba.ss.simulator.Statistics;
import ar.edu.itba.ss.simulator.model.device.Device;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class PacketChannel extends Channel {

	private Device endpoint;
	private int channelSize;
	private int transferRate;
	private List<SubChannel> channels = Lists.newLinkedList();

	public PacketChannel(Device endpoint, int channelSize, int channels,
			int transferRate) {
		super();
		this.endpoint = endpoint;
		this.channelSize = channelSize;
		this.transferRate = transferRate;

		for (int i = 0; i < channels; i++) {
			this.channels.add(new SubChannel());
		}
	}

	@Override
	public boolean isAvailable() {
		return channels.stream().anyMatch(c -> c.isAvailable());
	}

	@Override
	public boolean transfer(Data data) {
		if (Math.random() > 0.5 || true) {
			endpoint.receive(data);
			return true;
		}
		SubChannel channel = channels
				.stream()
				.filter(c -> c.id != null
						&& c.id.equals(data.getMessage().getId())).findFirst()
				.orElse(null);
		if (channel != null) {
			channel.addData(data);
		} else if (isAvailable()) {
			SubChannel newChannel = channels.stream()
					.filter(c -> c.isAvailable()).findFirst().orElse(null);
			if (newChannel == null)
				return false;
			newChannel.takeChannel(data.getMessage().getId());
			newChannel.addData(data);
			return true;
		}
		return false;
	}

	@Override
	public void tick() {
		super.tick();
		channels.forEach(c -> c.tick());
		long usedChannels = channels.stream().filter(c -> c.isAvailable())
				.count();
		Statistics.logUsage(((double) usedChannels * 100.0) / channelSize);
	}

	@Override
	public int getChannelSize() {
		return channelSize / channels.size();
	}

	class SubChannel extends TimedObject {
		private UUID id;
		private Map<Data, Integer> sendingData;

		public SubChannel() {
			super();
			this.sendingData = Maps.newHashMap();
		}

		void addData(Data data) {
			if (this.id != null && this.id.equals(data.getMessage().getId())) {
				this.sendingData.put(data, 0);
			}
		}

		void takeChannel(UUID id) {
			this.id = id;
		}

		boolean isAvailable() {
			return this.id == null;
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
			if (auxMap.isEmpty()) {
				// Release channel
				this.id = null;
			}
			this.sendingData = auxMap;
		}
	}

}
