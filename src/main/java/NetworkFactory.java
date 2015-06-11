import java.util.Arrays;
import java.util.List;

import com.google.common.collect.Lists;

public class NetworkFactory {

	// Singelton
	public static NetworkFactory instance;

	public static NetworkFactory getInstance() {
		if (instance == null)
			instance = new NetworkFactory();
		return instance;
	}

	public Network createPacketDomiciliaryNetwork() {
		// TODO: implement...
		return null;
	}

	public Network createCircuitDomiciliaryNetwork() {
		Router fourthRouter = new CircuitRouter("Fourth", null, 100, 10, 10);
//		Router thirdRouter = new CircuitRouter("Third", fourthRouter, 100, 10,
//				10);
//		Router secondRouter = new CircuitRouter("Second", thirdRouter, 100, 10,
//				10);
//		Router firstRouter = new CircuitRouter("First", secondRouter, 100, 10,
//				10);

		List<AppProfile> appProfiles = Lists.newLinkedList();

		Device device1 = new Device("joti device 1", appProfiles, fourthRouter);
		// Device device2 = new Device("joti device 2", appProfiles,
		// firstRouter);
		// Device device3 = new Device("joti device 3", appProfiles,
		// firstRouter);
		AppProfile profile1 = new AppProfile(device1,
				new FrequencyDistribution(), new ConstantDistribution(10, 100));
		AppProfile profile2 = new AppProfile(device1,
				new FrequencyDistribution(), new ConstantDistribution(10, 100));
		AppProfile profile3 = new AppProfile(device1,
				new FrequencyDistribution(), new ConstantDistribution(10, 100));
		appProfiles.addAll(Arrays.asList(profile1, profile2, profile3));

		List<Device> devices = Lists.newArrayList(device1);
		List<Router> routers = Lists.newArrayList(fourthRouter);
		return new Network(devices, routers, 0);
	}

}
