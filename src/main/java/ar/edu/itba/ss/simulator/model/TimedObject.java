package ar.edu.itba.ss.simulator.model;

public abstract class TimedObject {

	private int time = 0;
	
	public void tick() {
		this.time++;
	}

	protected int getTime() {
		return this.time;
	}
	
}
