import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class Router {

	protected static final Logger LOGGER = LoggerFactory
			.getLogger(Router.class);
	
	protected String name;
	protected Router next;
	protected int bandwidth;
	protected int latency;
	protected int concurCoef;

	public Router(String name, Router next, int bandwidth, int latency,
			int concurCoef) {
		this.name = name;
		this.next = next;
		this.bandwidth = bandwidth;
		this.latency = latency;
		this.concurCoef = concurCoef;
	}

	public abstract void update(int time);

}
