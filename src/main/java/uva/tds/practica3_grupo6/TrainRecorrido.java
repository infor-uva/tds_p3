package uva.tds.practica3_grupo6;

import java.time.LocalDateTime;

public class TrainRecorrido extends Recorrido {

	public TrainRecorrido(String id, Connection connection, double price, LocalDateTime dateTime, int numSeats) {
		super(id, connection, Transport.TRAIN, price, dateTime, numSeats);
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
	@Override
	public void decreaseAvailableSeats(int numSeats) {
		if (numSeats > 250)
			throw new IllegalArgumentException(
					"numSeats is more than the limit of 250 for transport " + getTransport());
		super.decreaseAvailableSeats(numSeats);
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
	@Override
	public void increaseAvailableSeats(int numSeats) {
		if (numSeats > 250)
			throw new IllegalArgumentException(
					"numSeats is more than the limit of 250 for transport " + getTransport());
		super.increaseAvailableSeats(numSeats);
	}

	@Override
	public Recorrido clone(Recorrido r) {
		Recorrido clone = new TrainRecorrido(getID(), getConnection(), getPrice(), getDateTime(), getNumAvailableSeats());
		clone.decreaseAvailableSeats(getTotalSeats() - getNumAvailableSeats());
		return clone;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof TrainRecorrido)) {
			return false;
		}
		if (!super.equals(obj)) {
			return false;
		}
		return true;
	}

}
