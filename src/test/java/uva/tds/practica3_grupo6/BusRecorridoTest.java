package uva.tds.practica3_grupo6;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * Class dedicated to execute the tests for the methods of the instances of
 * {@link Recorrido}.
 * 
 * @author diebomb
 * @author hugcubi
 * @author migudel
 * 
 * @version 11/12/2023
 */
class BusRecorridoTest {
	/**
	 * Error margin for the tests which implicates operations with float and/ or
	 * double values
	 */
	private static final double ERROR_MARGIN = 0.00001;

	private String id;
	private Connection connection;
	private double price;
	private LocalDateTime dateTime;
	private int numAvailableSeats;
	private BusRecorrido recorrido;
	private LocalDate newDate;
	private LocalTime newTime;
	private LocalDateTime newDateTime;

	@BeforeEach
	void setUp() {
		id = "c12345";
		price = 12.0;
		dateTime = LocalDateTime.of(2023, 10, 27, 19, 06, 50);
		numAvailableSeats = BusRecorrido.MAX_NUM_SEATS;
		connection = new Connection("Valladolid", "Palencia", 30);
		recorrido = new BusRecorrido(id, connection, price, dateTime, numAvailableSeats); 
		newDate = LocalDate.of(2025, 12, 30);
		newTime = LocalTime.of(16, 20, 37);
		newDateTime = LocalDateTime.of(2030, 5, 6, 19, 15, 13);
	}

	@Test
	void testConstructorValidoLimiteInferior() {
		String id = "c";
		double price = 0.0;
		int numAvailableSeats = 1;

		BusRecorrido recorrido = new BusRecorrido(id, connection, price, dateTime, numAvailableSeats);
		
		assertNotNull(recorrido);
		assertEquals(id, recorrido.getID());
		assertEquals(connection, recorrido.getConnection());
		assertEquals(connection.getOrigin(), recorrido.getOrigin());
		assertEquals(connection.getDestination(), recorrido.getDestination());
		assertEquals(connection.getDuration(), recorrido.getDuration());
		assertEquals(price, recorrido.getPrice(), ERROR_MARGIN);
		assertEquals(dateTime, recorrido.getDateTime());
		assertEquals(numAvailableSeats, recorrido.getNumAvailableSeats());
	}

	@Test
	void testConstructorValidoLimiteSuperior() {
		String id = "c123";
		double price = 25.50;
		int numAvailableSeats = 50;

		BusRecorrido recorrido = new BusRecorrido(id, connection, price, dateTime, numAvailableSeats);
		assertNotNull(recorrido);
		assertEquals(id, recorrido.getID());
		assertEquals(connection, recorrido.getConnection());
		assertEquals(connection.getOrigin(), recorrido.getOrigin());
		assertEquals(connection.getDestination(), recorrido.getDestination());
		assertEquals(connection.getDuration(), recorrido.getDuration());
		assertEquals(price, recorrido.getPrice(), ERROR_MARGIN);
		assertEquals(dateTime, recorrido.getDateTime());
		assertEquals(numAvailableSeats, recorrido.getNumAvailableSeats());
	}

	@Test
	void testConstructorNoValidoConIdNull() {
		assertThrows(IllegalArgumentException.class, () -> {
			new BusRecorrido(null, connection, price, dateTime, numAvailableSeats);
		});
	}

	@Test
	void testConstructorNoValidoConIdLimiteInferior() {
		assertThrows(IllegalArgumentException.class, () -> {
			new BusRecorrido("", connection, price, dateTime, numAvailableSeats);
		});
	}

	@Test
	void testConstructorNoValidoConConnectionNull() {
		assertThrows(IllegalArgumentException.class, () -> {
			new BusRecorrido(id, null, price, dateTime, numAvailableSeats);
		});
	}
	
	@Test
	void testConstructorNoValidoConPriceLimiteInferior() {
		assertThrows(IllegalArgumentException.class, () -> {
			new BusRecorrido(id, connection, -0.1, dateTime, numAvailableSeats);
		});
	}

	@Test
	void testConstructorNoValidoConDateTimeNull() {
		assertThrows(IllegalArgumentException.class, () -> {
			new BusRecorrido(id, connection, price, null, numAvailableSeats);
		});
	}

	@Test
	void testConstructorNoValidoConNumAvailableSeatsLimiteInferior() {
		assertThrows(IllegalArgumentException.class, () -> {
			new BusRecorrido(id, connection, price, dateTime, 0);
		});
	}

	@Test
	void testConstructorNoValidoConNumAvailableSeatsLimiteSuperior() {
		assertThrows(IllegalArgumentException.class, () -> {
			new BusRecorrido(id, connection, price, dateTime, 51);
		});
	}

	@Test
	void testConstructorCopiaValido() {
		BusRecorrido recorridoCopia = new BusRecorrido(recorrido);
		assertNotNull(recorridoCopia);
		assertEquals(recorrido.getID(), recorridoCopia.getID());
		assertEquals(recorrido.getConnection(), recorridoCopia.getConnection());
		assertEquals(recorrido.getOrigin(), recorridoCopia.getOrigin());
		assertEquals(recorrido.getDestination(), recorridoCopia.getDestination());
		assertEquals(recorrido.getDuration(), recorridoCopia.getDuration());
		assertEquals(recorrido.getPrice(), recorridoCopia.getPrice(), ERROR_MARGIN);
		assertEquals(recorrido.getDateTime(), recorridoCopia.getDateTime());
		assertEquals(recorrido.getNumAvailableSeats(), recorridoCopia.getNumAvailableSeats());
		assertEquals(recorrido.hashCode(), recorridoCopia.hashCode());
		assertEquals(recorrido, recorridoCopia);
		assertNotSame(recorrido, recorridoCopia);
	}

	@Test
	void testConstructorCopiaConNull() {
		assertThrows(IllegalArgumentException.class, () -> {
			new BusRecorrido(null);
		});
	}

	@Test
	void testUpdateDateValido() {
		assertNotEquals(newDate, recorrido.getDate());
		recorrido.updateDate(newDate);
		assertEquals(newDate, recorrido.getDate());
	}

	@Test
	void testUpdateDateConDateNull() {
		assertThrows(IllegalArgumentException.class, () -> {
			recorrido.updateDate(null);
		});
	}

	@Test
	void testUpdateDateConDateActual() {
		LocalDate date = recorrido.getDate();
		assertThrows(IllegalStateException.class, () -> {
			recorrido.updateDate(date);
		});
	}

	@Test
	void testUpdateTimeValido() {
		assertNotEquals(newTime, recorrido.getTime());
		recorrido.updateTime(newTime);
		assertEquals(newTime, recorrido.getTime());
	}

	@Test
	void testUpdateTimeConTimeNull() {
		assertThrows(IllegalArgumentException.class, () -> {
			recorrido.updateTime(null);
		});
	}

	@Test
	void testUpdateTimeConTimeActual() {
		LocalTime time = recorrido.getTime();
		assertThrows(IllegalStateException.class, () -> {
			recorrido.updateTime(time);
		});
	}

	@Test
	void testUpdateDateTimeValido() {
		assertNotEquals(newDateTime, recorrido.getDateTime());
		recorrido.updateDateTime(newDateTime);
		assertEquals(newDateTime, recorrido.getDateTime());
	}

	@Test
	void testUpdateDateTimeConDateNull() {
		assertThrows(IllegalArgumentException.class, () -> {
			recorrido.updateDateTime(null, newTime);
		});
	}

	@Test
	void testUpdateDateTimeConTimeNull() {
		assertThrows(IllegalArgumentException.class, () -> {
			recorrido.updateDateTime(newDate, null);
		});
	}

	@Test
	void testUpdateDateYTimeValido() {
		assertNotEquals(newDate, recorrido.getDate());
		assertNotEquals(newTime, recorrido.getTime());
		recorrido.updateDateTime(newDate, newTime);
		assertEquals(newDate, recorrido.getDate());
		assertEquals(newTime, recorrido.getTime());
	}

	@Test
	void testUpdateDateTimeConDateYTimeActual() {
		LocalDate date = recorrido.getDate();
		LocalTime time = recorrido.getTime();
		assertThrows(IllegalStateException.class, () -> {
			recorrido.updateDateTime(date, time);
		});
	}

	@Test
	void testUpdateDateTimeConDateTimeNull() {
		assertThrows(IllegalArgumentException.class, () -> {
			recorrido.updateDateTime(null);
		});
	}

	@Test
	void testUpdateDateTimeConDateTimeActual() {
		LocalDateTime dateTime = recorrido.getDateTime();
		assertThrows(IllegalStateException.class, () -> {
			recorrido.updateDateTime(dateTime);
		});
	}

	@Test
	void testDecreaseAvailableSeatsValidoLimiteInferior() {
		int numSeats = 1;
		assertEquals(recorrido.getTotalSeats(), recorrido.getNumAvailableSeats());
		recorrido.decreaseAvailableSeats(numSeats);
		assertEquals(recorrido.getTotalSeats() - numSeats, recorrido.getNumAvailableSeats());
	}
	
	@Test
	void testDecreaseAvailableSeatsValidoLimiteSuperior() {
		int numSeats = 50;
		assertEquals(recorrido.getTotalSeats(), recorrido.getNumAvailableSeats());
		recorrido.decreaseAvailableSeats(numSeats);
		assertEquals(recorrido.getTotalSeats() - numSeats, recorrido.getNumAvailableSeats());
	}
	
	@Test
	void testDecreaseAvailableSeatsConNumSeatsLimiteInferior() {
		assertThrows(IllegalArgumentException.class, () -> {
			recorrido.decreaseAvailableSeats(0);
		});
	}
	
	@Test
	void testDecreaseAvailableSeatsConNumSeatsLimiteSuperior() {
		assertThrows(IllegalArgumentException.class, () -> {
			recorrido.decreaseAvailableSeats(51);
		});
	}
	
	@Test
	void testDecreaseAvailableSeatsConNumSeatsMayorQueNumAvailableSeats() {
		recorrido.decreaseAvailableSeats(45);
		assertThrows(IllegalStateException.class, () -> {
			recorrido.decreaseAvailableSeats(6);
		});
	}
	
	@Test
	void testIncreaseAvailableSeatsValidoLimiteInferior() {
		int numSeats = 1;
		recorrido.decreaseAvailableSeats(numSeats);
		assertEquals(recorrido.getTotalSeats() - numSeats, recorrido.getNumAvailableSeats());
		recorrido.increaseAvailableSeats(numSeats);
		assertEquals(recorrido.getTotalSeats(), recorrido.getNumAvailableSeats());
	}

	@Test
	void testIncreaseAvailableSeatsValidoLimiteSuperior() {
		int numSeats = 50;
		recorrido.decreaseAvailableSeats(numSeats);
		assertEquals(recorrido.getTotalSeats() - numSeats, recorrido.getNumAvailableSeats());
		recorrido.increaseAvailableSeats(numSeats);
		assertEquals(recorrido.getTotalSeats(), recorrido.getNumAvailableSeats());
	}

	@Test
	void testIncreaseAvailableSeatsConNumSeatsLimiteInferior() {
		assertThrows(IllegalArgumentException.class, () -> {
			recorrido.increaseAvailableSeats(0);
		});
	}

	@Test
	void testIncreaseAvailableSeatsConNumSeatsLimiteSuperior() {
		assertThrows(IllegalArgumentException.class, () -> {
			recorrido.increaseAvailableSeats(51);
		});
	}
	
	@Test
	void testIncreaseAvailableSeatsConNumSeatsMayorQueNumAvailableSeats() {
		recorrido.decreaseAvailableSeats(5);
		assertThrows(IllegalStateException.class, () -> {
			recorrido.increaseAvailableSeats(6);
		});
	}

	@Test
	void testEquals() {
		BusRecorrido same = new BusRecorrido(recorrido);
		BusRecorrido different = new BusRecorrido("dif", connection, price, dateTime, numAvailableSeats);
		assertEquals(recorrido, recorrido);
		assertEquals(recorrido, same);
        assertNotEquals(recorrido, connection);
		assertNotEquals(recorrido, different);
	}

	@Test
	@Tag("Cobertura")
	void testEqualsConConnectionsDiferentes() {
		Connection c = new Connection("different", "different2", 12);
		BusRecorrido other = new BusRecorrido(id, c, price, dateTime, numAvailableSeats);
		assertNotEquals(recorrido, other);
	}

	@Test
	@Tag("Cobertura")
	void testEqualsConTransportsDiferentes() {
		TrainRecorrido other = new TrainRecorrido(id, connection, price, dateTime, TrainRecorrido.MAX_NUM_SEATS);
		assertNotEquals(recorrido, other);
	}

	@Test
	@Tag("Cobertura")
	void testEqualsConPricesDiferentes() {
		BusRecorrido other = new BusRecorrido(id, connection, 32.69, dateTime, numAvailableSeats);
		assertNotEquals(recorrido, other);
	}

	@Test
	@Tag("Cobertura")
	void testEqualsConDateTimeDiferentes() {
		BusRecorrido other = new BusRecorrido(id, connection, price, newDateTime, BusRecorrido.MAX_NUM_SEATS);
		assertNotEquals(recorrido, other);
	}

	@Test
	@Tag("Cobertura")
	void testEqualsConNumAvailableSeatsDifferentDiferentes() {
		BusRecorrido other = new BusRecorrido(recorrido);
		other.decreaseAvailableSeats(2);
		assertNotEquals(recorrido, other);
	}

	@Test
	@Tag("Cobertura")
	void testEqualsConTotalSeatsDiferentes() {
		BusRecorrido other = new BusRecorrido(id, connection, price, dateTime, 32);
		assertNotEquals(recorrido, other);
	}
	
	@Test
	void testHashCode() {
		assertEquals(new TrainRecorrido(recorrido).hashCode(), recorrido.hashCode());
	}
}
