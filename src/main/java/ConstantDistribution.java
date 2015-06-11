public class ConstantDistribution extends Distribution {

	private double min, max;

	public ConstantDistribution(double min, double max) {
		this.min = min;
		this.max = max;
	}

	@Override
	public double getValue(double x) {
		return (Math.random() * max) + min;
	}

}
