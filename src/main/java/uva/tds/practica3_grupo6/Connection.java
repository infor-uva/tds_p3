package uva.tds.practica3_grupo6;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="CONNECTION")
public class Connection {

	/**
	 * The id of the connection
	 */
	@Id
	@GeneratedValue
	private int id;
	/**
	 * The origin of the route (where the route start)
	 */
	@Column(name="ORIGIN")
	private String origin;
	/**
	 * The destination of the route (where the route ends)
	 */
	@Column(name="DESTINATION")
	private String destination;
	/**
	 * Duration of the connection in minutes
	 */
	@Column(name="DURATION")
	private int duration;
	
	@OneToMany(mappedBy = "connection", cascade = CascadeType.ALL)
	private List<Recorrido> recorridos;

	public Connection() {
		
	}
	/**
	 * Constructor for Connection
	 * 
	 * @param origin
	 * @param destination
	 * @param duration
	 * 
	 * @throws IllegalArgumentException if origin is null
	 * @throws IllegalArgumentException if origin have less than 1 character
	 * @throws IllegalArgumentException if destination is null
	 * @throws IllegalArgumentException if destination have less than 1 character
	 * @throws IllegalArgumentException if origin and destination are the same
	 * @throws IllegalArgumentException if duration is less or equal than 0
	 */
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
		recorridos = new ArrayList<>();
	}

	/**
	 * Consult the destination of the connection
	 * 
	 * @return the destination
	 */
	public String getDestination() {
		return destination;
	}

	/**
	 * Consult the duration of the connection
	 * 
	 * @return the duration
	 */
	public int getDuration() {
		return duration;
	}
	
	public int getId() {
		return this.id;
	}
	
	public void addRecorrido(Recorrido rec) {
		this.recorridos.add(rec);
	}
	
	public void deleteRecorrido(Recorrido rec) {
		this.recorridos.remove(rec);
	}

	/**
	 * Consult the origin of the connection
	 * 
	 * @return the origin
	 */
	public String getOrigin() {
		return origin;
	}

	/**
	 * Calculate the hascode of the connection
	 */
	@Override
	public int hashCode() {
		return Objects.hash(destination, duration, origin);
	}

	/**
	 * Compare if an object have the same values
	 * 
	 * @param obj object to compare
	 * 
	 * @return if have the same values or are the same object
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Connection)) {
			return false;
		}
		Connection other = (Connection) obj;
		return Objects.equals(origin, other.origin) && Objects.equals(destination, other.destination)
				&& duration == other.duration;
	}
}
