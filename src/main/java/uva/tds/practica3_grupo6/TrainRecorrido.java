package uva.tds.practica3_grupo6;

import java.time.LocalDateTime;

/**
 * A child of {@link Recorrido} who represent the routes in which the transport
 * is {@link Transport#TRAIN}. The limit of seats for this type of route is
 * {@link BusRecorrido#MAX_NUM_SEATS}. This type of routes have a discount of
 * {@link TrainRecorrido#DISCOUNT} of the original price, can consult both with
 * the next getters:
 * <ul>
 * <li><code>{@link TrainRecorrido#getPrice()}</code> - The price of the route
 * </li>
 * <li><code>{@link TrainRecorrido#getPriceWithDiscount()}</code> - The
 * price with the discount</li>
 * </ul>
 * 
 * @author diebomb
 * @author hugcubi
 * @author migudel
 * 
 * @version 13/12/23
 */
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
		checkNumSeats(numSeats);
	}

	/**
	 * Check if the numbers of seats is over the limit
	 * 
	 * @param numSeats to check
	 * 
	 * @throws IllegalArgumentException if the number of seats is more than
	 *                                  {@link TrainRecorrido#MAX_NUM_SEATS}
	 */
	private void checkNumSeats(int numSeats) {
		if (numSeats > MAX_NUM_SEATS)
			throw new IllegalArgumentException(
					"numSeats is more than the limit of " + MAX_NUM_SEATS + " for transport " + getTransport());
	}

	/**
	 * Consult the price of a train route which have a discount of
	 * {@link TrainRecorrido#DISCOUNT}
	 * 
	 * @return price with the discount
	 */
	public double getPriceWithDiscount() {
		return (1 - DISCOUNT) * super.getPrice();
	}

	/**
	 * Decrease the number of available seats
	 * 
	 * @param numSeats to decrease
	 * 
	 * @throws IllegalArgumentException if the number of seats is less than 1 or
	 *                                  more than
	 *                                  {@link TrainRecorrido#MAX_NUM_SEATS}
	 * @throws IllegalStateException    if the number of seats to decremented is
	 *                                  greater than the number of available sites
	 */
	@Override
	public void decreaseAvailableSeats(int numSeats) {
		checkNumSeats(numSeats);
		super.decreaseAvailableSeats(numSeats);
	}

	/**
	 * Increase the number of available seats
	 * 
	 * @param numSeats to increase
	 * 
	 * @throws IllegalArgumentException if the number of seats is less than 1 or
	 *                                  more than
	 *                                  {@link TrainRecorrido#MAX_NUM_SEATS}
	 * @throws IllegalStateException    if the number of seats exceeds the total
	 *                                  number of seats
	 */
	@Override
	public void increaseAvailableSeats(int numSeats) {
		checkNumSeats(numSeats);
		super.increaseAvailableSeats(numSeats);
	}
}
