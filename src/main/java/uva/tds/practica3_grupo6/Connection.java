package uva.tds.practica3_grupo6;

public class Connection {
	/**
	 * The origin of the route (where the route start)
	 */
	private String origin;
	/**
	 * The destination of the route (where the route ends)
	 */
	private String destination;
	
	private int duration;
	
	public Connection(String origin, String destination, int duration) {
		if (origin == null)
			throw new IllegalArgumentException("origin is null");
		if (origin.isEmpty())
			throw new IllegalArgumentException("origin is empty");
		if (destination == null)
			throw new IllegalArgumentException("destination is null");
		if (destination.isEmpty())
			throw new IllegalArgumentException("destination is empty");
		if (origin.equals(destination))
			throw new IllegalArgumentException("origin and destination are the same");
		if (duration <= 0)
			throw new IllegalArgumentException("duration is equals or less to 0");
		this.origin = origin;
		this.destination = destination;
		this.duration = duration;
	}
	
	/**
	 * @return the origin
	 */
	public String getOrigin() {
		return origin;
	}
	/**
	 * @return the destination
	 */
	public String getDestination() {
		return destination;
	}
	/**
	 * @return the duration
	 */
	public int getDuration() {
		return duration;
	}
}
