package uva.tds.practica3_grupo6;

import java.time.LocalDateTime;

public class TrainRecorrido extends Recorrido {

	/**
	 * Maximum number of seats the route can have 
	 */
	public static final int MAX_NUM_SEATS = 250;
	/**
	 * Percent of discount for this type of {@link Recorrido}
	 */
	public static final double DISCOUNT = 0.1;

	public TrainRecorrido(String id, Connection connection, double price, LocalDateTime dateTime, int numSeats) {
		super(id, connection, Transport.TRAIN, price, dateTime, numSeats);
	}

	/**
	 * Consult the price of a train route which have a discount of
	 * {@link TrainRecorrido#DISCOUNT}
	 * 
	 * @return price with the discount
	 */
	@Override
	public double getPrice() {
		return DISCOUNT * super.getPrice();
	}

	/**
	 * Decrease the number of available seats
	 * 
	 * @param numSeats to decrease
	 * 
	 * @throws IllegalArgumentException if the number of seats is less than 1 or
	 *                                  more than {@link TrainRecorrido#MAX_NUM_SEATS}
	 * @throws IllegalStateException    if the number of seats to decremented is
	 *                                  greater than the number of available sites
	 */
	@Override
	public void decreaseAvailableSeats(int numSeats) {
		if (numSeats > MAX_NUM_SEATS)
			throw new IllegalArgumentException(
					"numSeats is more than the limit of " + MAX_NUM_SEATS + " for transport " + getTransport());
		super.decreaseAvailableSeats(numSeats);
	}

	/**
	 * Increase the number of available seats
	 * 
	 * @param numSeats to increase
	 * 
	 * @throws IllegalArgumentException if the number of seats is less than 1 or
	 *                                  more than {@link TrainRecorrido#MAX_NUM_SEATS}
	 * @throws IllegalStateException    if the number of seats exceeds the total
	 *                                  number of seats
	 */
	@Override
	public void increaseAvailableSeats(int numSeats) {
		if (numSeats > MAX_NUM_SEATS)
			throw new IllegalArgumentException(
					"numSeats is more than the limit of " + MAX_NUM_SEATS + " for transport " + getTransport());
		super.increaseAvailableSeats(numSeats);
	}

	@Override
	public TrainRecorrido clone() {
		TrainRecorrido clone = new TrainRecorrido(getID(), getConnection(), getPrice(), getDateTime(), getTotalSeats());
		int decreased;
		if ((decreased = getTotalSeats() - getNumAvailableSeats()) != 0)
			clone.decreaseAvailableSeats(decreased);			
		return clone;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (!(obj instanceof TrainRecorrido)) {
			return false;
		}
		return true;
	}

}
