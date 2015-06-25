package ar.edu.itba.ss.simulator.model.event;

public class ExponentialDistribution {

	private double lambda;

	public ExponentialDistribution(double lambda) {
		super();
		this.lambda = lambda;
	}

	public int waitingTime(int time) {
		return (int) (-Math.log(Math.random()) / lambda);
	}

}
