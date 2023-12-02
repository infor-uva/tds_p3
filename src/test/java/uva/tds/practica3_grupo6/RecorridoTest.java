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
 * @version 28/10/2023
 */
class RecorridoTest {
	/**
	 * Error margin for the tests which implicates operations with float and/ or
	 * double values
	 */
	private static final double ERROR_MARGIN = 0.00001;
	/**
	 * Type of transport bus
	 */
	private static final String BUS = Recorrido.BUS;
	/**
	 * Type of transport train
	 */
	private static final String TRAIN = Recorrido.TRAIN;

	private String id;
	private String origin;
	private String destination;
	private String transport;
	private double price;
	private LocalDate date;
	private LocalTime time;
	private int numAvailableSeats;
	private int duration;
	private Recorrido recorrido;
	private Recorrido sameRecorrido;
	private Recorrido differentRecorrido;
	private Recorrido busRecorrido;
	private Recorrido trainRecorrido;
	private LocalDate newDate;
	private LocalTime newTime;
	private LocalDateTime newDateTime;

	@BeforeEach
	void setUp() {
		id = "c12345";
		origin = "Valladolid";
		destination = "Palencia";
		transport = BUS;
		price = 0.0;
		date = LocalDate.of(2023, 10, 27);
		time = LocalTime.of(19, 06, 50);
		numAvailableSeats = 20;
		duration = 30;
		recorrido = new Recorrido(id, origin, destination, transport, price, date, time, numAvailableSeats, duration);
		sameRecorrido = new Recorrido(id, origin, destination, transport, price, date, time, numAvailableSeats,
				duration);
		differentRecorrido = new Recorrido("dif", destination, origin, transport, price, date, time, numAvailableSeats,
				duration);
		newDate = LocalDate.of(2025, 12, 30);
		newTime = LocalTime.of(16, 20, 37);
		newDateTime = LocalDateTime.of(2030, 5, 6, 19, 15, 13);
		busRecorrido = new Recorrido("B123", origin, destination, BUS, price, date, time, 50, duration);
		trainRecorrido = new Recorrido("B123", origin, destination, TRAIN, price, date, time, 250, duration);
	}

	@Test
	void testConstructorBusValidoLimiteInferior() {
		String id = "c";
		String origin = "V";
		String destination = "P";
		String transport = BUS;
		double price = 0.0;
		int numAvailableSeats = 1;
		int duration = 1;

		Recorrido recorrido = new Recorrido(id, origin, destination, transport, price, date, time, numAvailableSeats,
				duration);
		assertNotNull(recorrido);
		assertEquals(id, recorrido.getID());
		assertEquals(origin, recorrido.getOrigin());
		assertEquals(destination, recorrido.getDestination());
		assertEquals(transport, recorrido.getTransport());
		assertEquals(price, recorrido.getPrice(), ERROR_MARGIN);
		assertEquals(date, recorrido.getDate());
		assertEquals(time, recorrido.getTime());
		assertEquals(numAvailableSeats, recorrido.getNumAvailableSeats());
		assertEquals(duration, recorrido.getDuration());
	}

	@Test
	void testConstructorTrainValidoLimiteInferior() {
		String id = "c";
		String origin = "V";
		String destination = "P";
		String transport = TRAIN;
		double price = 0.0;
		int numAvailableSeats = 1;
		int duration = 1;

		Recorrido recorrido = new Recorrido(id, origin, destination, transport, price, date, time, numAvailableSeats,
				duration);

		recorrido = new Recorrido(id, origin, destination, transport, price, date, time, numAvailableSeats, duration);
		assertNotNull(recorrido);
		assertEquals(id, recorrido.getID());
		assertEquals(origin, recorrido.getOrigin());
		assertEquals(destination, recorrido.getDestination());
		assertEquals(transport, recorrido.getTransport());
		assertEquals(price, recorrido.getPrice(), ERROR_MARGIN);
		assertEquals(date, recorrido.getDate());
		assertEquals(time, recorrido.getTime());
		assertEquals(numAvailableSeats, recorrido.getNumAvailableSeats());
		assertEquals(duration, recorrido.getDuration());
	}

	@Test
	void testConstructorValidoBusLimiteSuperior() {
		String id = "c123";
		String origin = "Valladolid";
		String destination = "Palencia";
		String transport = BUS;
		double price = 25.50;
		int numAvailableSeats = 50;
		int duration = 120;

		Recorrido recorrido = new Recorrido(id, origin, destination, transport, price, date, time, numAvailableSeats,
				duration);
		assertNotNull(recorrido);
		assertEquals(id, recorrido.getID());
		assertEquals(origin, recorrido.getOrigin());
		assertEquals(destination, recorrido.getDestination());
		assertEquals(transport, recorrido.getTransport());
		assertEquals(price, recorrido.getPrice(), ERROR_MARGIN);
		assertEquals(date, recorrido.getDate());
		assertEquals(time, recorrido.getTime());
		assertEquals(numAvailableSeats, recorrido.getNumAvailableSeats());
		assertEquals(duration, recorrido.getDuration());
	}

	@Test
	void testConstructorValidoTrainLimiteSuperior() {
		String id = "c123";
		String origin = "Valladolid";
		String destination = "Palencia";
		String transport = TRAIN;
		double price = 25.50;
		int numAvailableSeats = 250;
		int duration = 120;

		Recorrido recorrido = new Recorrido(id, origin, destination, transport, price, date, time, numAvailableSeats,
				duration);

		recorrido = new Recorrido(id, origin, destination, transport, price, date, time, numAvailableSeats, duration);
		assertNotNull(recorrido);
		assertEquals(id, recorrido.getID());
		assertEquals(origin, recorrido.getOrigin());
		assertEquals(destination, recorrido.getDestination());
		assertEquals(transport, recorrido.getTransport());
		assertEquals(price, recorrido.getPrice(), ERROR_MARGIN);
		assertEquals(date, recorrido.getDate());
		assertEquals(time, recorrido.getTime());
		assertEquals(numAvailableSeats, recorrido.getNumAvailableSeats());
		assertEquals(duration, recorrido.getDuration());
	}

	@Test
	void testConstructorNoValidoConIdNull() {
		assertThrows(IllegalArgumentException.class, () -> {
			new Recorrido(null, origin, destination, transport, price, date, time, numAvailableSeats, duration);
		});
	}

	@Test
	void testConstructorNoValidoConIdLimiteInferior() {
		assertThrows(IllegalArgumentException.class, () -> {
			new Recorrido("", origin, destination, transport, price, date, time, numAvailableSeats, duration);
		});
	}

	@Test
	void testConstructorNoValidoConOriginNull() {
		assertThrows(IllegalArgumentException.class, () -> {
			new Recorrido(id, null, destination, transport, price, date, time, numAvailableSeats, duration);
		});
	}

	@Test
	void testConstructorNoValidoConOriginLimiteInferior() {
		assertThrows(IllegalArgumentException.class, () -> {
			new Recorrido(id, "", destination, transport, price, date, time, numAvailableSeats, duration);
		});
	}

	@Test
	void testConstructorNoValidoConDestinationNull() {
		assertThrows(IllegalArgumentException.class, () -> {
			new Recorrido(id, origin, null, transport, price, date, time, numAvailableSeats, duration);
		});
	}

	@Test
	void testConstructorNoValidoConDestinationLimiteInferior() {
		assertThrows(IllegalArgumentException.class, () -> {
			new Recorrido(id, origin, "", transport, price, date, time, numAvailableSeats, duration);
		});
	}

	@Test
	void testConstructorNoValidoConOrigenYDestinoIguales() {
		assertThrows(IllegalArgumentException.class, () -> {
			new Recorrido(id, origin, origin, transport, price, date, time, numAvailableSeats, duration);
		});
	}

	@Test
	void testConstructorNoValidoConTransportNull() {
		assertThrows(IllegalArgumentException.class, () -> {
			new Recorrido(id, origin, destination, null, price, date, time, numAvailableSeats, duration);
		});
	}

	@Test
	void testConstructorNoValidoConTransportValorDiferente() {
		assertThrows(IllegalArgumentException.class, () -> {
			new Recorrido(id, origin, destination, "boat", price, date, time, numAvailableSeats, duration);
		});
	}

	@Test
	void testConstructorNoValidoConPriceLimiteInferior() {
		assertThrows(IllegalArgumentException.class, () -> {
			new Recorrido(id, origin, destination, transport, -0.1, date, time, numAvailableSeats, duration);
		});
	}

	@Test
	void testConstructorNoValidoConDateNull() {
		assertThrows(IllegalArgumentException.class, () -> {
			new Recorrido(id, origin, destination, transport, price, null, time, numAvailableSeats, duration);
		});
	}

	@Test
	void testConstructorNoValidoConTimeNull() {
		assertThrows(IllegalArgumentException.class, () -> {
			new Recorrido(id, origin, destination, transport, price, date, null, numAvailableSeats, duration);
		});
	}

	@Test
	void testConstructorNoValidoConNumAvailableSeatsLimiteInferiorTipoBus() {
		assertThrows(IllegalArgumentException.class, () -> {
			new Recorrido(id, origin, destination, BUS, price, date, time, 0, duration);
		});
	}

	@Test
	void testConstructorNoValidoConNumAvailableSeatsLimiteSuperiorTipoBus() {
		assertThrows(IllegalArgumentException.class, () -> {
			new Recorrido(id, origin, destination, BUS, price, date, time, 51, duration);
		});
	}

	@Test
	void testConstructorNoValidoConNumAvailableSeatsLimiteInferiorTipoTrain() {
		assertThrows(IllegalArgumentException.class, () -> {
			new Recorrido(id, origin, destination, TRAIN, price, date, time, 0, duration);
		});
	}

	@Test
	void testConstructorNoValidoConNumAvailableSeatsLimiteSuperiorTipoTrain() {
		assertThrows(IllegalArgumentException.class, () -> {
			new Recorrido(id, origin, destination, TRAIN, price, date, time, 251, duration);
		});
	}

	@Test
	void testConstructorNoValidoConDurationLimiteInferior() {
		assertThrows(IllegalArgumentException.class, () -> {
			new Recorrido(id, origin, destination, transport, price, date, time, numAvailableSeats, 0);
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
		assertThrows(IllegalStateException.class, () -> {
			recorrido.updateDate(recorrido.getDate());
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
		assertThrows(IllegalStateException.class, () -> {
			recorrido.updateTime(recorrido.getTime());
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
		assertThrows(IllegalStateException.class, () -> {
			recorrido.updateDateTime(recorrido.getDate(), recorrido.getTime());
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
		assertThrows(IllegalStateException.class, () -> {
			recorrido.updateDateTime(recorrido.getDateTime());
		});
	}

	@Test
	void testDecreaseAvailableSeatsBusValidoLimiteInferior() {
		int numSeats = 1;
		assertEquals(busRecorrido.getTotalSeats(), busRecorrido.getNumAvailableSeats());
		busRecorrido.decreaseAvailableSeats(numSeats);
		assertEquals(busRecorrido.getTotalSeats() - numSeats, busRecorrido.getNumAvailableSeats());
	}

	@Test
	void testDecreaseAvailableSeatsTrainValidoLimiteInferior() {
		int numSeats = 1;
		assertEquals(trainRecorrido.getTotalSeats(), trainRecorrido.getNumAvailableSeats());
		trainRecorrido.decreaseAvailableSeats(numSeats);
		assertEquals(trainRecorrido.getTotalSeats() - numSeats, trainRecorrido.getNumAvailableSeats());
	}

	@Test
	void testDecreaseAvailableSeatsBusValidoLimiteSuperior() {
		int numSeats = 50;
		assertEquals(busRecorrido.getTotalSeats(), busRecorrido.getNumAvailableSeats());
		busRecorrido.decreaseAvailableSeats(numSeats);
		assertEquals(busRecorrido.getTotalSeats() - numSeats, busRecorrido.getNumAvailableSeats());
	}

	@Test
	void testDecreaseAvailableSeatsTrainValidoLimiteSuperior() {
		int numSeats = 250;
		assertEquals(trainRecorrido.getTotalSeats(), trainRecorrido.getNumAvailableSeats());
		trainRecorrido.decreaseAvailableSeats(numSeats);
		assertEquals(trainRecorrido.getTotalSeats() - numSeats, trainRecorrido.getNumAvailableSeats());
	}

	@Test
	void testDecreaseAvailableSeatsConNumSeatsLimiteInferiorBus() {
		assertThrows(IllegalArgumentException.class, () -> {
			busRecorrido.decreaseAvailableSeats(0);
		});
	}

	@Test
	void testDecreaseAvailableSeatsConNumSeatsLimiteInferiorTrain() {
		assertThrows(IllegalArgumentException.class, () -> {
			trainRecorrido.decreaseAvailableSeats(0);
		});
	}

	@Test
	void testDecreaseAvailableSeatsConNumSeatsLimiteSuperiorBus() {
		assertThrows(IllegalArgumentException.class, () -> {
			busRecorrido.decreaseAvailableSeats(51);
		});
	}

	@Test
	void testDecreaseAvailableSeatsConNumSeatsLimiteSuperiorTrain() {
		assertThrows(IllegalArgumentException.class, () -> {
			trainRecorrido.decreaseAvailableSeats(251);
		});
	}

	@Test
	void testDecreaseAvailableSeatsConNumSeatsMayorQueNumAivailableSeatsBus() {
		busRecorrido.decreaseAvailableSeats(45);
		assertThrows(IllegalStateException.class, () -> {
			busRecorrido.decreaseAvailableSeats(6);
		});
	}

	@Test
	void testDecreaseAvailableSeatsConNumSeatsMayorQueNumAivailableSeatsTrain() {
		trainRecorrido.decreaseAvailableSeats(245);
		assertThrows(IllegalStateException.class, () -> {
			trainRecorrido.decreaseAvailableSeats(6);
		});
	}

	@Test
	void testIncreaseAvailableSeatsBusValidoLimiteInferior() {
		int numSeats = 1;
		busRecorrido.decreaseAvailableSeats(numSeats);
		assertEquals(busRecorrido.getTotalSeats() - numSeats, busRecorrido.getNumAvailableSeats());
		busRecorrido.increaseAvailableSeats(numSeats);
		assertEquals(busRecorrido.getTotalSeats(), busRecorrido.getNumAvailableSeats());
	}

	@Test
	void testIncreaseAvailableSeatsTrainValidoLimiteInferior() {
		int numSeats = 1;
		trainRecorrido.decreaseAvailableSeats(numSeats);
		assertEquals(trainRecorrido.getTotalSeats() - numSeats, trainRecorrido.getNumAvailableSeats());
		trainRecorrido.increaseAvailableSeats(numSeats);
		assertEquals(trainRecorrido.getTotalSeats(), trainRecorrido.getNumAvailableSeats());
	}

	@Test
	void testIncreaseAvailableSeatsBusValidoLimiteSuperior() {
		int numSeats = 50;
		busRecorrido.decreaseAvailableSeats(numSeats);
		assertEquals(busRecorrido.getTotalSeats() - numSeats, busRecorrido.getNumAvailableSeats());
		busRecorrido.increaseAvailableSeats(numSeats);
		assertEquals(busRecorrido.getTotalSeats(), busRecorrido.getNumAvailableSeats());
	}

	@Test
	void testIncreaseAvailableSeatsTrainValidoLimiteSuperior() {
		int numSeats = 250;
		trainRecorrido.decreaseAvailableSeats(numSeats);
		assertEquals(trainRecorrido.getTotalSeats() - numSeats, trainRecorrido.getNumAvailableSeats());
		trainRecorrido.increaseAvailableSeats(numSeats);
		assertEquals(trainRecorrido.getTotalSeats(), trainRecorrido.getNumAvailableSeats());
	}

	@Test
	void testIncreaseAvailableSeatsConNumSeatsLimiteInferiorBus() {
		assertThrows(IllegalArgumentException.class, () -> {
			busRecorrido.increaseAvailableSeats(0);
		});
	}

	@Test
	void testIncreaseAvailableSeatsConNumSeatsLimiteInferiorTrain() {
		assertThrows(IllegalArgumentException.class, () -> {
			trainRecorrido.increaseAvailableSeats(0);
		});
	}

	@Test
	void testIncreaseAvailableSeatsConNumSeatsLimiteSuperiorBus() {
		assertThrows(IllegalArgumentException.class, () -> {
			busRecorrido.increaseAvailableSeats(51);
		});
	}

	@Test
	void testIncreaseAvailableSeatsConNumSeatsLimiteSuperiorTrain() {
		assertThrows(IllegalArgumentException.class, () -> {
			trainRecorrido.increaseAvailableSeats(251);
		});
	}

	@Test
	void testIncreaseAvailableSeatsConNumSeatsMayorQueNumAivailableSeatsBus() {
		busRecorrido.decreaseAvailableSeats(5);
		assertThrows(IllegalStateException.class, () -> {
			busRecorrido.increaseAvailableSeats(6);
		});
	}

	@Test
	void testIncreaseAvailableSeatsConNumSeatsMayorQueNumAivailableSeatsTrain() {
		busRecorrido.decreaseAvailableSeats(5);
		assertThrows(IllegalStateException.class, () -> {
			trainRecorrido.increaseAvailableSeats(6);
		});
	}

	@Test
	void testEqualsValido() {
		assertEquals(recorrido, recorrido);
		assertEquals(recorrido, sameRecorrido);
		assertNotEquals(recorrido, true);
		assertNotEquals(recorrido, differentRecorrido);
	}

	@Test
	void testEqualsNoValidoConNull() {
		assertThrows(IllegalArgumentException.class, () -> {
			recorrido.equals(null);
		});
	}

	@Test
	@Tag("Cobertura")
	void testEqualsConIDsDiferentes() {
		Recorrido other = new Recorrido("other", origin, destination, transport, price, date, time, numAvailableSeats,
				duration);
		assertNotEquals(recorrido, other);
	}

	@Test
	@Tag("Cobertura")
	void testEqualsConOriginsDiferentes() {
		Recorrido other = new Recorrido(id, "Pamplona", destination, transport, price, date, time, numAvailableSeats,
				duration);
		assertNotEquals(recorrido, other);
	}

	@Test
	@Tag("Cobertura")
	void testEqualsConDestinationsDiferentes() {
		Recorrido other = new Recorrido(id, origin, "Pamplona", transport, price, date, time, numAvailableSeats,
				duration);
		assertNotEquals(recorrido, other);
	}

	@Test
	@Tag("Cobertura")
	void testEqualsConTransportsDiferentes() {
		Recorrido other = new Recorrido(id, origin, destination, TRAIN, price, date, time, numAvailableSeats, duration);
		assertNotEquals(recorrido, other);
	}

	@Test
	@Tag("Cobertura")
	void testEqualsConPricesDiferentes() {
		Recorrido other = new Recorrido(id, origin, destination, transport, 26.9, date, time, numAvailableSeats,
				duration);
		assertNotEquals(recorrido, other);
	}

	@Test
	@Tag("Cobertura")
	void testEqualsConDateDiferentes() {
		Recorrido other = new Recorrido(id, origin, destination, transport, price, newDate, time, numAvailableSeats,
				duration);
		assertNotEquals(recorrido, other);
	}

	@Test
	@Tag("Cobertura")
	void testEqualsConTimesDiferentes() {
		Recorrido other = new Recorrido(id, origin, destination, transport, price, date, newTime, numAvailableSeats,
				duration);
		assertNotEquals(recorrido, other);
	}

	@Test
	@Tag("Cobertura")
	void testEqualsConNumAvailableSeatsDiferentDiferentes() {
		Recorrido other = new Recorrido(id, origin, destination, transport, price, date, time, numAvailableSeats,
				duration);
		other.decreaseAvailableSeats(2);
		assertNotEquals(recorrido, other);
	}

	@Test
	@Tag("Cobertura")
	void testEqualsConTotalSeatsDiferentes() {
		Recorrido other = new Recorrido(id, origin, destination, transport, price, date, time, 12, duration);
		assertNotEquals(recorrido, other);
	}

	@Test
	@Tag("Cobertura")
	void testEqualsConDurationDiferentes() {
		Recorrido other = new Recorrido(id, origin, destination, transport, price, date, time, numAvailableSeats, 360);
		assertNotEquals(recorrido, other);
	}

	@Test
	void testCloneBus() {
		// Comprobar con numSeas y numAvailableSeats no iguales
		busRecorrido.decreaseAvailableSeats(5);

		Recorrido clone = busRecorrido.clone();

		assertEquals(busRecorrido, clone);
		assertNotSame(busRecorrido, clone);
	}

	@Test
	void testCloneTrain() {
		// Comprobar con numSeas y numAvailableSeats no iguales
		trainRecorrido.decreaseAvailableSeats(5);

		Recorrido clone = trainRecorrido.clone();

		assertEquals(trainRecorrido, clone);
		assertNotSame(trainRecorrido, clone);
	}
}
