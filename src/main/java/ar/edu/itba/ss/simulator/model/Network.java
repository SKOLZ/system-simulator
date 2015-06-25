package ar.edu.itba.ss.simulator.model;

import java.util.List;

import ar.edu.itba.ss.simulator.model.device.Device;

public class Network extends TimedObject {

	private List<Device> devices;
	
	public Network(List<Device> devices) {
		super();
		this.devices = devices;
	}

	@Override
	public void tick() {
		super.tick();
		devices.forEach(device -> {
			device.tick();
		});
	}

}
