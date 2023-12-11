package uva.tds.practica3_grupo6;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

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
 * @version 11/12/23
 */
public abstract class Recorrido {

	/**
	 * Identification of the route
	 */
	private String id;
	/**
	 * Connection from the start of the route to the destination and the time that
	 * lasts
	 */
	private Connection connection;
	/**
	 * Type of transport train
	 */
	private Transport transport;
	/**
	 * The price of the route
	 */
	private double price;
	/**
	 * DateTime of the route
	 */
	private LocalDateTime dateTime;
	/**
	 * Number of seats on the route
	 */
	private int totalSeats;
	/**
	 * Number of available seats on the route
	 */
	private int numAvailableSeats;

	/**
	 * Constructor
	 * 
	 * @param id
	 * @param connection
	 * @param transport
	 * @param price
	 * @param dateTime
	 * @param numSeats
	 * 
	 * @throws IllegalArgumentException if id is null
	 * @throws IllegalArgumentException if id have less than 1 character
	 * @throws IllegalArgumentException if connection is null
	 * @throws IllegalArgumentException if transport is null
	 * @throws IllegalArgumentException if price is less than 0
	 * @throws IllegalArgumentException if dateTime is null
	 * @throws IllegalArgumentException if numSeats is less than 1
	 */
	public Recorrido(String id, Connection connection, Transport transport, double price, LocalDateTime dateTime,
			int numSeats) {
		setId(id);
		setConecction(connection);
		setTransport(transport);
		// TODO Preguntar si se puede usar esto, tengo que usar un setter o ambos
		updateDateTime(dateTime);
		setPrice(price);
		setTotalSeats(numSeats);
		setNumAvailableSeats(numSeats);
	}

	/**
	 * Set the id
	 * 
	 * @param id to set
	 * 
	 * @throws IllegalArgumentException if id is null
	 * @throws IllegalArgumentException if id have less than 1 character
	 */
	public void setId(String id) {
		if (id == null)
			throw new IllegalArgumentException("id is null");
		if (id.isEmpty())
			throw new IllegalArgumentException("id is empty");
		this.id = id;
	}
	
	/**
	 * Set the transport for this route
	 *  
	 * @param transport to set
	 * 
	 * @throws IllegalArgumentException if transport is null
	 */
	public void setTransport(Transport transport) {
		if (transport == null)
			throw new IllegalArgumentException("transport is null");
		this.transport = transport;
	}

	/**
	 * Set the connection for this route
	 * 
	 * @param connection to set
	 * 
	 * @throws IllegalArgumentException if id is null
	 */
	public void setConecction(Connection connection) {
		if (connection == null)
			throw new IllegalArgumentException("the connection is null");
		this.connection = connection;
	}

	/**
	 * Set the price for this route
	 * 
	 * @param price for this route
	 * 
	 * @throws IllegalArgumentException if price is less than 0
	 */
	public void setPrice(double price) {
		if (price < 0)
			throw new IllegalArgumentException("price is less than 0");
		this.price = price;
	}
	
	/**
	 * Check if the number of seats is more than 0
	 * 
	 * @param numSeats
	 * 
	 * @throws IllegalArgumentException if numSeats is less than 1
	 */
	private void checkNumSeats(int numSeats) {
		if (numSeats < 1)
			throw new IllegalArgumentException("the num of seats is less than 1");
	}
	
	/**
	 * Set the total seats for the route
	 * 
	 * @param numSeats
	 * 
	 * @throws IllegalArgumentException if numSeats is less than 1
	 */
	public void setTotalSeats(int numSeats) {
		checkNumSeats(numSeats);
		this.totalSeats = numSeats;
	}
	
	/**
	 * Set the number of available seats for the route
	 * 
	 * @param numSeats
	 * 
	 * @throws IllegalArgumentException if numSeats is less than 1
	 */
	public void setNumAvailableSeats(int numSeats) {
		checkNumSeats(numSeats);
		this.numAvailableSeats = numSeats;
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
		return connection.getOrigin();
	}

	/**
	 * Consult the destination of the Recorrido
	 * 
	 * @return destination
	 */
	public String getDestination() {
		return connection.getDestination();
	}

	/**
	 * Consult the duration of the Recorrido
	 * 
	 * @return duration
	 */
	public int getDuration() {
		return connection.getDuration();
	}

	/**
	 * Consult the connection of the recorrido
	 * 
	 * @return connection
	 */
	public Connection getConnection() {
		return connection;
	}

	/**
	 * Consult the transport of the Recorrido
	 * 
	 * @return transport
	 */
	public Transport getTransport() {
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
		return dateTime.toLocalDate();
	}

	/**
	 * Consult the time of the Recorrido
	 * 
	 * @return time
	 */
	public LocalTime getTime() {
		return dateTime.toLocalTime();
	}

	/**
	 * Consult the Date time of the recorrido
	 * 
	 * @return date time of recorrido
	 */
	public LocalDateTime getDateTime() {
		return dateTime;
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
		if (getDate().equals(newDate))
			throw new IllegalStateException("newDate is the already date");
		dateTime = LocalDateTime.of(newDate, getTime());
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
		if (newTime.equals(getTime()))
			throw new IllegalStateException("newTime is the already date");
		dateTime = LocalDateTime.of(getDate(), newTime);
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
		if (newDateTime.equals(getDateTime()))
			throw new IllegalStateException("newDateTime is the already date");
		dateTime = newDateTime;
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
		if (LocalDateTime.of(newDate, newTime).equals(getDateTime()))
			throw new IllegalStateException("the new Date Time is the already date");
		dateTime = LocalDateTime.of(newDate, newTime);
	}

	/**
	 * Decrease the number of available seats
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
		if (numSeats + numAvailableSeats > totalSeats)
			throw new IllegalStateException("this increase will exceed the total number of seats for this route");
		this.numAvailableSeats += numSeats;
	}

	/**
	 * Create a copy of this instance of Recorrido with the same values of the
	 * attributes but not are the same object.
	 * 
	 * @return clone of the instance
	 */
	public abstract Recorrido clone();

	
	@Override
	public int hashCode() {
		return Objects.hash(connection, dateTime, id, numAvailableSeats, price, totalSeats, transport);
	}

	/**
	 * Compare if two Recorridos are the same
	 * 
	 * @param obj Recorrido to compare
	 * 
	 * @return if are the same
	 */
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Recorrido))
			return false;
		Recorrido other = (Recorrido) obj;
		return Objects.equals(connection, other.connection) && Objects.equals(dateTime, other.dateTime)
				&& Objects.equals(id, other.id) && totalSeats == other.totalSeats
				&& numAvailableSeats == other.numAvailableSeats
				&& Double.doubleToLongBits(price) == Double.doubleToLongBits(other.price)
				&& transport == other.transport;
	}
}
