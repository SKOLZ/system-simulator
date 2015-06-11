import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App {

	private static final Logger LOGGER = LoggerFactory.getLogger(App.class);

	public static void main(String[] args) throws InterruptedException {
		Network network = NetworkFactory.getInstance()
				.createCircuitDomiciliaryNetwork();

		LOGGER.info("Started");

		for (int i = 0; i < 100; i++) {
//			Thread.sleep(10);
			network.update();
		}

		StatisticsModule.getInstance().printAttemptedRequestsPerDevice();
		StatisticsModule.getInstance().printExceededBandwithRequestsPerDevice();
		StatisticsModule.getInstance().printStartedRequestsPerDevice();
		StatisticsModule.getInstance().printCompletedRequestsPerDevice();
	}

}
