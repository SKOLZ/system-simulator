package ar.edu.itba.ss.simulator.model;

public abstract class Channel extends TimedObject {

	public abstract int getChannelSize();

	public abstract boolean isAvailable();

	public abstract boolean transfer(Data data);

}
