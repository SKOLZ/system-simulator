package ar.edu.itba.ss.simulator.model.device;

import ar.edu.itba.ss.simulator.model.Channel;
import ar.edu.itba.ss.simulator.model.Data;
import ar.edu.itba.ss.simulator.model.TimedObject;

public abstract class Device extends TimedObject {

	private Channel channel;

	public Device(Channel channel) {
		super();
		this.channel = channel;
	}
	
	public abstract void receive(Data data);

	protected Channel getChannel() {
		return this.channel;
	}

}
