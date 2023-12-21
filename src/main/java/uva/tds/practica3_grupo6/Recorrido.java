package uva.tds.practica3_grupo6;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;


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
 * @version 21/12/23
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TRANSPORT", discriminatorType = DiscriminatorType.STRING)
public abstract class Recorrido {

	/**
	 * Identification of the route
	 */
	@Id
	private String id;
	/**
	 * Connection from the start of the route to the destination and the time that
	 * lasts
	 */
	@ManyToOne
	@JoinColumn(name="CONNECTION_ID", referencedColumnName = "ID")
	private Connection connection;
	/**
	 * The price of the route
	 */
	@Column(name="PRICE")
	private double price;
	/**
	 * DateTime of the route
	 */
	@Column(name="DATETIME")
	private LocalDateTime dateTime;
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
	
	@OneToMany(mappedBy = "recorrido", cascade = CascadeType.ALL)
	private List<Billete> billetes;
	
	protected Recorrido() {
		
	}
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
	protected Recorrido(String id, Connection connection, double price, LocalDateTime dateTime, int numSeats) {
		setId(id);
		setConnection(connection);
		updateDateTime(dateTime);
		setPrice(price);
		setTotalSeats(numSeats);
		setNumAvailableSeats(numSeats);
		billetes = new ArrayList<>();
	}

	/**
	 * Create a copy of an existing Recorrido with the same attribute values
	 * 
	 * @param r Recorrido to copy
	 * 
	 * @throws IllegalArgumentException if r is null
	 */
	protected Recorrido(Recorrido r) {
		if (r == null)
			throw new IllegalArgumentException("r is null");
		setId(r.getID());
		setConnection(r.getConnection());
		updateDateTime(r.getDateTime());
		setPrice(r.getPrice());
		setTotalSeats(r.getTotalSeats());
		setNumAvailableSeats(r.getNumAvailableSeats());
		billetes = new ArrayList<>();
	}

	/**
	 * Set the id
	 * 
	 * @param id to set
	 * 
	 * @throws IllegalArgumentException if id is null
	 * @throws IllegalArgumentException if id have less than 1 character
	 */
	protected void setId(String id) {
		if (id == null)
			throw new IllegalArgumentException("id is null");
		if (id.isEmpty())
			throw new IllegalArgumentException("id is empty");
		this.id = id;
	}
	
	/**
	 * Set the connection for this route
	 * 
	 * @param connection to set
	 * 
	 * @throws IllegalArgumentException if id is null
	 */
	protected void setConnection(Connection connection) {
		if (connection == null)
			throw new IllegalArgumentException("the connection is null");
		this.connection = connection;
	}
	
	public void addBilletes(Billete ticket) {
		this.billetes.add(ticket);
	}
	
	public void removeBilletes(Billete ticket) {
		this.billetes.remove(ticket);
	}

	/**
	 * Set the price for this route
	 * 
	 * @param price for this route
	 * 
	 * @throws IllegalArgumentException if price is less than 0
	 */
	protected void setPrice(double price) {
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
	private void checkNumSeatsIfPositive(int numSeats) {
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
		checkNumSeatsIfPositive(numSeats);
		this.totalSeats = numSeats;
	}

	/**
	 * Set the number of available seats for the route
	 * 
	 * @param numSeats
	 * 
	 * @throws IllegalArgumentException if numSeats is less than 1
	 */
	protected void setNumAvailableSeats(int numSeats) {
		checkNumSeatsIfPositive(numSeats);
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

	@Override
	public int hashCode() {
		return Objects.hash(connection, dateTime, id, numAvailableSeats, price, totalSeats);
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
				&& getClass() == other.getClass();
	}
}
