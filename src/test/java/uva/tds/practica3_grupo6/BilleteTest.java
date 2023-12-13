package uva.tds.practica3_grupo6;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalTime;

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
	private String origin;
	private String destination;
	private String transport;
	private double price;
	private LocalDate date;
	private LocalTime time;
	private int numAvailableSeats;
	private int duration;
	private Recorrido recorrido;
	private String locator;
	private Billete ticket;

	@BeforeEach
	void setUp() {
		nif = "32698478E";
		nombre = "Geronimo";
		user = new Usuario(nif, nombre);
		id = "c12345";
		origin = "Valladolid";
		destination = "Palencia";
		transport = "bus";
		price = 0.0;
		date = LocalDate.of(2023, 10, 27);
		time = LocalTime.of(19, 06, 50);
		numAvailableSeats = 20;
		duration = 30;
		recorrido = new Recorrido(id, origin, destination, transport, price, date, time, numAvailableSeats, duration);
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

		assertNotEquals(ticket,false);
	}

	@Test
	void testEqualsNull() {
		assertThrows(IllegalArgumentException.class, () -> {
			ticket.equals(null);
		});
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
		Recorrido recorridoC =  new Recorrido("c12543", origin, destination, transport, price, date, time, numAvailableSeats, duration);
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
	
}
