package uva.tds.practica3_grupo6;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

class ConnectionTest {

	private String origin;
	private String destination;
	private int duration;
	private Connection connection;
	
	@BeforeEach
	void setUp() {
		origin = "Valladolid";
		destination = "Barcelona";
		duration = 95;
		connection = new Connection(origin, destination, duration);
	}
	
	@Test
	void testConstructorValidoLimiteInferior() {
		String origin = "o";
		String destination = "d";
		int duration = 1;
		Connection c = new Connection(origin, destination, duration);
		assertEquals(origin, c.getOrigin());
		assertEquals(destination, c.getDestination());
		assertEquals(duration, c.getDuration());
	}
	
	@Test
	void testConstructorInvalidoOriginNull() {
		assertThrows(IllegalArgumentException.class, () -> {
			new Connection(null, destination, duration);
		});
	}

	@Test
	void testConstructorInvalidoOriginVacio() {
		assertThrows(IllegalArgumentException.class, () -> {
			new Connection("", destination, duration);
		});
	}
	
	@Test
	void testConstructorInvalidoDestinationNull() {
		assertThrows(IllegalArgumentException.class, () -> {
			new Connection(origin, null, duration);
		});
	}

	@Test
	void testConstructorInvalidoDestinationVacio() {
		assertThrows(IllegalArgumentException.class, () -> {
			new Connection(origin, "", duration);
		});
	}
	
	@Test
	void testConstructorInvalidoOriginYDestinationIguales() {
		assertThrows(IllegalArgumentException.class, () -> {
			new Connection(origin, origin, duration);
		});
	}
	
	@Test
	void testConstructorInvalidoDurationLimiteInferior() {
		assertThrows(IllegalArgumentException.class, () -> {
			new Connection(origin, destination, 0);
		});
	}
	
	@Test
	void testEqualsValido() {
		Connection equal = new Connection(origin, destination, duration);
		Connection same = connection;
		Connection different = new Connection(origin, destination, 15);
		
		assertEquals(connection, same);
		assertEquals(connection, equal);
		assertNotEquals(connection, different);
        assertNotEquals(connection, new ArrayList<Connection>());
	}
	
	@Test
	@Tag("Cobertura")
	void testEqualsDiferenteOrigin() {
		assertNotEquals(connection, new Connection("dif", destination, duration));
	}
	
	@Test
	@Tag("Cobertura")
	void testEqualsDiferenteDestination() {
		assertNotEquals(connection, new Connection(origin, "dif", duration));
	}
	
	@Test
	void testHashCode() {
		Connection copy = new Connection(origin, destination, duration);
		assertEquals(copy.hashCode(), connection.hashCode());
	}
}
