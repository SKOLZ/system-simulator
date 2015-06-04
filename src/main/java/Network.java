import java.util.List;


public class Network {

	private List<Device> devices;
	private List<Router> routers;
	private int timer;
	
	public Network(List<Device> devices, List<Router> routers, int timerStart) {
		this.devices = devices;
		this.routers = routers;
		this.timer = timerStart;
	}
	
	public void update() {
		timer++;
		
		// TODO: add perpendicular network requests...
		
		for (Device dev :devices)
			dev.update(timer);
		for (Router router :routers)
			router.update(timer);
	}
	
	
}
