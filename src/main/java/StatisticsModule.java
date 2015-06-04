
public class StatisticsModule {

	// Singleton
	private static StatisticsModule instance;
	
	public static StatisticsModule getInstance() {
		if (instance == null)
			instance = new StatisticsModule();
		return instance;		
	}
	
	
	public void logIdleRouter(int curTime, Router router) {
		// TODO: Implement...
	}
	
	public void logReceivedReq(int curTime, Device source, int delay, int size) {
		// TODO: Implement...
	}
	
	public void logLostReq(int curTime, Device source, int size) {
		// TODO: Implement...
	}
	
}
