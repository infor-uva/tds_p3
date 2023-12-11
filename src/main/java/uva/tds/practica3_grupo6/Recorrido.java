package uva.tds.practica3_grupo6;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Class dedicated for the representation of the route.
 * 
 * In this class you will can consult the identification of the route, the
 * origin and destination of the travel, in which method of transport it will
 * be, the price of the travel, the date and time of the travel, the number of
 * seats which are available and the duration of the travel.
 * 
 * The ID of the route will be used to compare the routes
 * 
 * @author diebomb
 * @author hugcubi
 * @author migudel
 * 
 * @version 28/11/23
 */
@Entity
@Table(name="RECORRIDO")
public class Recorrido implements Cloneable {

	/**
	 * Type of transport train
	 */
	public static final String TRAIN = "train";
	/**
	 * Type of transport bus
	 */
	public static final String BUS = "bus";

	/**
	 * Identification of the route
	 */
	@Id
	
	@Column(name="ID")
	private String id;
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
	 * The transport will be used in the route
	 */
	@Column(name="TRANSPORT")
	private String transport;
	/**
	 * The price of the route
	 */
	@Column(name="PRICE")
	private double price;
	/**
	 * Date of the route
	 */
	@Column(name="DATE")
	private LocalDate date;
	/**
	 * Time of the route
	 */
	@Column(name="TIME")
	private LocalTime time;
	/**
	 * Number of seats on the route
	 */
	@Column(name="TOTALSEATS")
	private int totalSeats;
	/**
	 * Number of available seats on the route
	 */
	@Column(name="NUMAVAILABLESEATS")
	private int numAvailableSeats;
	/**
	 * Duration in minutes of the route
	 */
	@Column(name="DURATION")
	private int duration;
	/**
	 * Constructor
	 * 
	 * @param id
	 * @param origin
	 * @param destination
	 * @param transport
	 * @param price
	 * @param date
	 * @param time
	 * @param numSeats
	 * @param duration    in minutes
	 * 
	 * @throws IllegalArgumentException if id is null
	 * @throws IllegalArgumentException if id have less than 1 character
	 * @throws IllegalArgumentException if origin is null
	 * @throws IllegalArgumentException if origin have less than 1 character
	 * @throws IllegalArgumentException if destination is null
	 * @throws IllegalArgumentException if destination have less than 1 character
	 * @throws IllegalArgumentException if origin and destination are the same
	 * @throws IllegalArgumentException if transport is null
	 * @throws IllegalArgumentException if transport is not a bus or train
	 * @throws IllegalArgumentException if price is less than 0
	 * @throws IllegalArgumentException if date is null
	 * @throws IllegalArgumentException if time is null
	 * @throws IllegalArgumentException if numSeats is less than 1 or more than 50
	 *                                  in the case of bus or more than 250 in the
	 *                                  case of train
	 * @throws IllegalArgumentException if duration is less or equal than 0
	 */
	public Recorrido(String id, String origin, String destination, String transport, double price, LocalDate date,
			LocalTime time, int numSeats, int duration) {
		if (id == null)
			throw new IllegalArgumentException("id is null");
		if (id.isEmpty())
			throw new IllegalArgumentException("id is empty");
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
		if (transport == null)
			throw new IllegalArgumentException("transport is null");
		if (!transport.equals(Recorrido.BUS) && !transport.equals(Recorrido.TRAIN))
			throw new IllegalArgumentException("transport isn't " + Recorrido.BUS + " or " + Recorrido.TRAIN);
		if (price < 0)
			throw new IllegalArgumentException("price is less than 0");
		if (date == null)
			throw new IllegalArgumentException("date is null");
		if (time == null)
			throw new IllegalArgumentException("time is null");
		if (numSeats < 1)
			throw new IllegalArgumentException("numSeats is less than 1");
		if (numSeats > 50 && transport.equals(Recorrido.BUS))
			throw new IllegalArgumentException("numSeats is more than the limit of 50 for transport " + Recorrido.BUS);
		if (numSeats > 250)
			throw new IllegalArgumentException(
					"numSeats is more than the limit of 250 for transport " + Recorrido.TRAIN);
		if (duration <= 0)
			throw new IllegalArgumentException("duration is equals or less to 0");
		this.id = id;
		this.origin = origin;
		this.destination = destination;
		this.transport = transport;
		this.price = price;
		this.date = date;
		this.time = time;
		this.totalSeats = this.numAvailableSeats = numSeats;
		this.duration = duration;
	}

	/**
	 * Consult the id of the Recorrido
	 * 
	 * @return id
	 */
	public String getID() {
		return id;
	}

	/**
	 * Consult the origin of the Recorrido
	 * 
	 * @return origin
	 */
	public String getOrigin() {
		return origin;
	}

	/**
	 * Consult the destination of the Recorrido
	 * 
	 * @return destination
	 */
	public String getDestination() {
		return destination;
	}

	/**
	 * Consult the transport of the Recorrido
	 * 
	 * @return transport
	 */
	public String getTransport() {
		return transport;
	}

	/**
	 * Consult the price of the Recorrido
	 * 
	 * @return price
	 */
	public double getPrice() {
		return price;
	}

	/**
	 * Consult the date of the Recorrido
	 * 
	 * @return date
	 */
	public LocalDate getDate() {
		return date;
	}

	/**
	 * Consult the time of the Recorrido
	 * 
	 * @return time
	 */
	public LocalTime getTime() {
		return time;
	}

	/**
	 * Consult the Date time of the recorrido
	 * 
	 * @return date time of recorrido
	 */
	public LocalDateTime getDateTime() {
		return LocalDateTime.of(date, time);
	}

	/**
	 * Consult the number of available seats
	 * 
	 * @return numAvailableSeats
	 */
	public int getNumAvailableSeats() {
		return numAvailableSeats;
	}

	/**
	 * Consult the number of the whole transport seats
	 * 
	 * @return the
	 */
	public int getTotalSeats() {
		return totalSeats;
	}

	/**
	 * Consult the duration of the Recorrido
	 * 
	 * @return duration
	 */
	public int getDuration() {
		return duration;
	}

	/**
	 * Update the date of a recorrido
	 * 
	 * @param newDate
	 * 
	 * @throws IllegalArgumentException if newDate is null
	 * @throws IllegalStateException    if the new date is the already the set
	 */
	public void updateDate(LocalDate newDate) {
		if (newDate == null)
			throw new IllegalArgumentException("newDate is null");
		if (date.equals(newDate))
			throw new IllegalStateException("newDate is the already date");
		date = newDate;
	}

	/**
	 * Update the time of the recorrido
	 * 
	 * @param newTime
	 * 
	 * @throws IllegalArgumentException if newTime is null
	 * @throws IllegalStateException    if the new time is the already the set
	 */
	public void updateTime(LocalTime newTime) {
		if (newTime == null)
			throw new IllegalArgumentException("newTime is null");
		if (time.equals(newTime))
			throw new IllegalStateException("newTime is the already date");
		time = newTime;
	}

	/**
	 * Update the date and time of the recorrido
	 * 
	 * @param newDateTime
	 * 
	 * @throws IllegalArgumentException if newDateTime is null
	 * @throws IllegalStateException    if the new date and time is the already the
	 *                                  set
	 */
	public void updateDateTime(LocalDateTime newDateTime) {
		if (newDateTime == null)
			throw new IllegalArgumentException("newDateTime is null");
		if (getDateTime().equals(newDateTime))
			throw new IllegalStateException("newDateTime is the already date");
		date = newDateTime.toLocalDate();
		time = newDateTime.toLocalTime();
	}

	/**
	 * Update the date and time of the recorrido
	 * 
	 * @param newDate
	 * @param newTime
	 * 
	 * @throws IllegalArgumentException if newDate is null
	 * @throws IllegalArgumentException if newTime is null
	 * @throws IllegalStateException    if the new date and time is the already the
	 *                                  set
	 */
	public void updateDateTime(LocalDate newDate, LocalTime newTime) {
		if (newDate == null)
			throw new IllegalArgumentException("newDate is null");
		if (newTime == null)
			throw new IllegalArgumentException("newTime is null");
		if (getDateTime().equals(LocalDateTime.of(newDate, newTime)))
			throw new IllegalStateException("the new Date Time is the already date");
		date = newDate;
		time = newTime;
	}

	/**
	 * Decrease the number of available seats
	 * 
	 * TODO Marcarlo como coverage
	 * 
	 * @param numSeats to decrease
	 * 
	 * @throws IllegalArgumentException if the number of seats is less than 1 or
	 *                                  more than 50 if the transport is bus or 250
	 *                                  if the transport is train
	 * @throws IllegalStateException    if the number of seats to decremented is
	 *                                  greater than the number of available sites
	 */
	public void decreaseAvailableSeats(int numSeats) {
		if (numSeats < 1)
			throw new IllegalArgumentException("numSeats is less than 1");
		if (numSeats > 50 && transport.equals(Recorrido.BUS))
			throw new IllegalArgumentException("numSeats is more than the limit of 50 for transport " + Recorrido.BUS);
		if (numSeats > 250)
			throw new IllegalArgumentException(
					"numSeats is more than the limit of 250 for transport " + Recorrido.TRAIN);
		if (numSeats > numAvailableSeats)
			throw new IllegalStateException("this decrease is greater than the number of available seats");
		this.numAvailableSeats -= numSeats;
	}

	/**
	 * Increase the number of available seats
	 * 
	 * @param numSeats to increase
	 * 
	 * @throws IllegalArgumentException if the number of seats is less than 1 or
	 *                                  more than 50 if the transport is bus or 250
	 *                                  if the transport is train
	 * @throws IllegalStateException    if the number of seats exceeds the total
	 *                                  number of seats
	 */
	public void increaseAvailableSeats(int numSeats) {
		if (numSeats < 1)
			throw new IllegalArgumentException("numSeats is less than 1");
		if (numSeats > 50 && transport.equals(Recorrido.BUS))
			throw new IllegalArgumentException("numSeats is more than the limit of 50 for transport " + Recorrido.BUS);
		if (numSeats > 250)
			throw new IllegalArgumentException(
					"numSeats is more than the limit of 250 for transport " + Recorrido.TRAIN);
		if (numSeats + numAvailableSeats > totalSeats)
			throw new IllegalStateException("this increase will exceed the total number of seats for this route");
		this.numAvailableSeats += numSeats;
	}

	/**
	 * Compare if two Recorridos are the same
	 * 
	 * @param obj Recorrido to compare
	 * 
	 * @return if are the same
	 * 
	 * @throws IllegalArgumentException if obj is null
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			throw new IllegalArgumentException("obj is null");
		if (this == obj)
			return true;
		if (!(obj instanceof Recorrido))
			return false;
		Recorrido other = (Recorrido) obj;
		return Objects.equals(id, other.id) && Objects.equals(origin, other.origin)
				&& Objects.equals(destination, other.destination) && Objects.equals(transport, other.transport)
				&& Double.doubleToLongBits(price) == Double.doubleToLongBits(other.price)
				&& Objects.equals(date, other.date) && Objects.equals(time, other.time)
				&& totalSeats == other.totalSeats && numAvailableSeats == other.numAvailableSeats
				&& duration == other.duration;
	}

	/**
	 * Create a copy of this instance of Recorrido with the same values of the
	 * attributes but not are the same object. In case if the clone is not supported
	 * a null will be returned
	 * 
	 * @return null or clone of the instance
	 */
	@Override
	public Recorrido clone() {
		// En caso de querer tener el 100% de cobertura en este método usar esta otra
		// posible implementación que no requiere la interfaz clonable
//		Recorrido clone = new Recorrido(id, origin, destination, transport, price, date, time, totalSeats, duration);
//		clone.numAvailableSeats = numAvailableSeats;
//		return clone;
		try {
			return (Recorrido) super.clone();
		} catch (CloneNotSupportedException e) {
			return null;
		}
	}
}
