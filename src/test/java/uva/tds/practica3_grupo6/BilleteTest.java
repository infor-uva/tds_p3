package uva.tds.practica3_grupo6;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

class BilleteTest {

	private static final String ESTADO_RESERVADO = "reservado";
	private static final String ESTADO_COMPRADO = "comprado";

	private String nif;
	private String nombre;
	private Usuario user;
	private String id;
	private Connection connection;
	private double price;
	private LocalDateTime dateTime;
	private int numAvailableSeats;
	private Recorrido recorrido;
	private String locator;
	private Billete ticket;

	@BeforeEach
	void setUp() {
		nif = "32698478E";
		nombre = "Geronimo";
		user = new Usuario(nif, nombre);
		id = "c12345";
		connection = new Connection("Valladolid", "Palencia", 30);
		price = 0.0;
		dateTime = LocalDateTime.of(2023, 10, 27, 19, 06, 50);
		numAvailableSeats = 20;
		recorrido = new BusRecorrido(id, connection, price, dateTime, numAvailableSeats);
		locator = "c123";
		ticket = new Billete(locator, recorrido, user, ESTADO_RESERVADO);
	}

	@Test
	void testLocalizadorValidoConEstadoComprado() {
		Billete billete = new Billete("ABC12345", recorrido, user, ESTADO_COMPRADO);
		assertEquals("ABC12345", billete.getLocalizador());
		assertEquals(user, billete.getUsuario());
		assertEquals(recorrido, billete.getRecorrido());
		assertEquals(ESTADO_COMPRADO, billete.getEstado());
	}

	@Test
	void testLocalizadorValidoConEstadoReservado() {
		Billete billete = new Billete("ABC12345", recorrido, user, ESTADO_RESERVADO);
		assertEquals("ABC12345", billete.getLocalizador());
		assertEquals(user, billete.getUsuario());
		assertEquals(recorrido, billete.getRecorrido());
		assertEquals(ESTADO_RESERVADO, billete.getEstado());
	}

	@Test
	void testLocalizadorInvalidoDemasiadoCorto() {
		assertThrows(IllegalArgumentException.class, () -> {
			new Billete("", recorrido, user, ESTADO_COMPRADO);
		});

	}

	@Test
	void testLocalizadorInvalidoDemasiadoLargo() {
		assertThrows(IllegalArgumentException.class, () -> {
			new Billete("123456789", recorrido, user, ESTADO_COMPRADO);
		});

	}

	@Test
	void testUsuarioNull() {
		assertThrows(IllegalArgumentException.class, () -> {
			new Billete("1234567890", recorrido, null, ESTADO_COMPRADO);
		});

	}

	@Test
	void testRecorridoNull() {
		assertThrows(IllegalArgumentException.class, () -> {
			new Billete("1234567890", null, user, ESTADO_COMPRADO);
		});
	}

	@Test
	void testEstadoNull() {
		assertThrows(IllegalArgumentException.class, () -> {
			new Billete("1234567", recorrido, user, null);
		});
	}

	@Test
	void testEstadoDiferenteDeCompradoYReservado() {
		assertThrows(IllegalArgumentException.class, () -> {
			new Billete("ABC12345", recorrido, user, "OtroEstado");
		});
	}

	@Test
	void testSetCompradoValido() {
		assertEquals(ESTADO_RESERVADO, ticket.getEstado());
		ticket.setComprado();
		assertEquals(ESTADO_COMPRADO, ticket.getEstado());
	}

	@Test
	void testSetCompradoConBilleteYaComprado() {
		Billete billete = new Billete("ABC12345", recorrido, user, ESTADO_COMPRADO);
		assertEquals(ESTADO_COMPRADO, billete.getEstado());
		assertThrows(IllegalStateException.class, () -> {
			billete.setComprado();
		});
	}

	@Test
	void testEqualsValido() {
		// Mismo paquete
		Billete b2 = new Billete(locator, recorrido, user, ESTADO_RESERVADO);
		assertEquals(ticket,b2);

		b2.setComprado();
		assertNotEquals(ticket,b2);

        assertFalse(ticket.equals(null));
	}
	
	@Test
	@Tag("Cobertura")
	void testEqualsLocalizadorBillete() {
		Billete billete = new Billete("ABC12345", recorrido, user, ESTADO_RESERVADO);
		Billete billete2 = new Billete("ABC1234", recorrido, user, ESTADO_RESERVADO);
		assertNotEquals(billete,billete2);
	}
	
	@Test
	@Tag("Cobertura")
	void testEqualsRecorridoBillete() {
		Recorrido recorridoC = new TrainRecorrido(id, connection, price, dateTime, numAvailableSeats);
		Billete billete = new Billete("ABC12345", recorrido, user, ESTADO_RESERVADO);
		Billete billete2 = new Billete("ABC12345", recorridoC, user, ESTADO_RESERVADO);
		assertNotEquals(billete,billete2);
	}
	
	@Test
	@Tag("Cobertura")
	void testEqualsUsuarioBillete() {
		Usuario userC = new Usuario("71174681P", nombre);
		Billete billete = new Billete("ABC12345", recorrido, user, ESTADO_RESERVADO);
		Billete billete2 = new Billete("ABC12345", recorrido, userC, ESTADO_RESERVADO);
		assertNotEquals(billete,billete2);
	}
	
	@Test
	@Tag("Cobertura")
	void testEqualsEstadoBillete() {
		Billete billete = new Billete("ABC12345", recorrido, user, ESTADO_RESERVADO);
		Billete billete2 = new Billete("ABC12345", recorrido, user, ESTADO_COMPRADO);
		assertNotEquals(billete,billete2);
	}

	@Test
	void testHashCode() {
		Billete copy = new Billete(ticket.getLocalizador(), ticket.getRecorrido(), ticket.getUsuario(), ticket.getEstado());
		assertEquals(copy.hashCode(), ticket.hashCode());
	}
}
