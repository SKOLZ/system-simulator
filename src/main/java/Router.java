
public abstract class Router {

	protected Router next;
	protected int bandwidth;
	protected int latency;
	protected int concurCoef;
	
	public Router(Router next, int bandwidth, int latency, int concurCoef) {
		this.next = next;
		this.bandwidth = bandwidth;
		this.latency = latency;
		this.concurCoef = concurCoef;
	}
	
	public abstract void update(int time);
	
}
