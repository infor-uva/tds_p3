package uva.tds.practica3_grupo6;

import java.time.LocalDateTime;

/**
 * A child of {@link Recorrido} who represent the routes in which the transport
 * is {@link Transport#BUS}. The limit of seats for this type of route is
 * {@link BusRecorrido#MAX_NUM_SEATS}
 * 
 * @author diebomb
 * @author hugcubi
 * @author migudel
 * 
 * @version 12/12/23
 */
public class BusRecorrido extends Recorrido {

	/**
	 * Maximum number of seats the route can have
	 */
	public static final int MAX_NUM_SEATS = 50;

	/**
	 * Constructor
	 * 
	 * @param id
	 * @param connection
	 * @param price
	 * @param dateTime
	 * @param numSeats
	 * 
	 * @throws IllegalArgumentException if id is null
	 * @throws IllegalArgumentException if id have less than 1 character
	 * @throws IllegalArgumentException if price is less than 0
	 * @throws IllegalArgumentException if dateTime is null
	 * @throws IllegalArgumentException if numSeats is less than 1 or more than
	 *                                  {@link BusRecorrido#MAX_NUM_SEATS}
	 */
	public BusRecorrido(String id, Connection connection, double price, LocalDateTime dateTime, int numSeats) {
		super(id, connection, Transport.BUS, price, dateTime, numSeats);
		checkNumSeats(numSeats);
	}

	/**
	 * Decrease the number of available seats
	 * 
	 * @param numSeats to decrease
	 * 
	 * @throws IllegalArgumentException if the number of seats is less than 1 or
	 *                                  more than {@link BusRecorrido#MAX_NUM_SEATS}
	 * @throws IllegalStateException    if the number of seats to decremented is
	 *                                  greater than the number of available sites
	 */
	@Override
	public void decreaseAvailableSeats(int numSeats) {
		checkNumSeats(numSeats);
		super.decreaseAvailableSeats(numSeats);
	}

	/**
	 * Check if the numbers of seats is over the limit
	 * 
	 * @param numSeats to check
	 * 
	 * @throws IllegalArgumentException if the number of seats is more than
	 *                                  {@link BusRecorrido#MAX_NUM_SEATS}
	 */
	private void checkNumSeats(int numSeats) {
		if (numSeats > MAX_NUM_SEATS)
			throw new IllegalArgumentException(
					"numSeats is more than the limit of " + MAX_NUM_SEATS + " for transport " + getTransport());
	}

	/**
	 * Increase the number of available seats
	 * 
	 * @param numSeats to increase
	 * 
	 * @throws IllegalArgumentException if the number of seats is less than 1 or
	 *                                  more than {@link BusRecorrido#MAX_NUM_SEATS}
	 * @throws IllegalStateException    if the number of seats exceeds the total
	 *                                  number of seats
	 */
	@Override
	public void increaseAvailableSeats(int numSeats) {
		checkNumSeats(numSeats);
		super.increaseAvailableSeats(numSeats);
	}

	/**
	 * Create a copy of this instance of BusRecorrido with the same values of the
	 * attributes but not are the same object.
	 * 
	 * @return clone of the instance
	 */
	@Override
	public BusRecorrido clone() {
		BusRecorrido clone = new BusRecorrido(getID(), getConnection(), getPrice(), getDateTime(), getTotalSeats());
		int decreased;
		if ((decreased = getTotalSeats() - getNumAvailableSeats()) != 0)
			clone.decreaseAvailableSeats(decreased);
		return clone;
	}

}
