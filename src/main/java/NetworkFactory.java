
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
		// TODO: implement...
		return null;
	}
	
}
