package uva.tds.practica3_grupo6;

import java.time.LocalDateTime;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * A child of {@link Recorrido} who represent the routes in which the transport
 * is {@link TransportType#TRAIN}. The limit of seats for this type of route is
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
@Entity
@DiscriminatorValue("TRAIN")
public class TrainRecorrido extends Recorrido {

	/**
	 * Maximum number of seats the route can have
	 */
	public static final int MAX_NUM_SEATS = 250;
	/**
	 * Percent of discount for this type of {@link Recorrido}
	 */
	public static final double DISCOUNT = 0.1;

	public TrainRecorrido() {
		super();
	}
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
	 *                                  {@link TrainRecorrido#MAX_NUM_SEATS}
	 */
	public TrainRecorrido(String id, Connection connection, double price, LocalDateTime dateTime, int numSeats) {
		super(id, connection, price, dateTime, numSeats);
		checkNumSeats(numSeats);
	}

	/**
	 * Create a copy of an existing TrainRecorrido or transform a other type of
	 * recorrido in TrainRecorrido with the same attribute values
	 * 
	 * @param r Recorrido to copy or convert
	 * 
	 * @throws IllegalArgumentException if r is null
	 */
	public TrainRecorrido(Recorrido r) {
		super(r);
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
					"numSeats is more than the limit of " + MAX_NUM_SEATS + " for transport TRAIN");
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
