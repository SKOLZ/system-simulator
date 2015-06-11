public class AppProfile {

	private Device device;
	private Distribution reqFreq;
	private Distribution reqSize;

	public AppProfile(Device device, Distribution reqFreq, Distribution reqSize) {
		this.device = device;
		this.reqFreq = reqFreq;
		this.reqSize = reqSize;
	}

	public void update(int time) {
		double prob = reqFreq.getValue(time);

		if (Math.random() < prob) {
			StatisticsModule.getInstance().logAttemptedRequest(device);
			device.sendRequest((int) Math.ceil(reqSize.getValue(time)));
		}
	}

}
