package uva.tds.practica3_grupo6;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * Class dedicated to execute the tests for the methods of the instances of
 * {@link Usuarios}. In this case the constructor, the getters and equals
 * methods
 * 
 * @author diebomb
 * 
 * @author migudel
 * 
 * @author hugcubi
 */

class UsuarioTest {

	private String nif;
	private String nombre;
	private Usuario user;
	private Usuario sameUser;
	private Usuario differentUser;

	@BeforeEach
	void setUp() {
		nif = "32698478E";
		nombre = "Geronimo";
		user = new Usuario(nif, nombre);
		sameUser = new Usuario(nif, nombre);
		differentUser = new Usuario("79105889B", nombre);
	}

	@Test
	void testConstructorUsuarioValido() {
		Usuario user = new Usuario("32698478E", "Geronimo");
		assertEquals("32698478E", user.getNif());
		assertEquals("Geronimo", user.getNombre());
	}

	@Test
	void testConstructorUsuarioNifVacio() {
		assertThrows(IllegalArgumentException.class, () -> {
			new Usuario("", "Geronimo");
		});
	}

	@Test
	void testConstructorUsuarioNifNull() {
		assertThrows(IllegalArgumentException.class, () -> {
			new Usuario(null, "Geronimo");
		});
	}

	@Test
	void testConstructorUsuarioNombreNull() {
		assertThrows(IllegalArgumentException.class, () -> {
			new Usuario("32698478E", null);
		});
	}

	@Test
	void testConstructorUsuarioNombreLimiteInferior() {
		assertThrows(IllegalArgumentException.class, () -> {
			new Usuario("32698478E", "");
		});
	}

	@Test
	void testConstructorUsuarioNombreLimiteSuperior() {
		assertThrows(IllegalArgumentException.class, () -> {
			new Usuario("32698478E", "GeronimoStiltons");
		});
	}

	@Test
	void testConstructorUsuarioNifLimiteInferiorDigitos() {
		assertThrows(IllegalArgumentException.class, () -> {
			new Usuario("3269847E", "Geronimo");
		});
	}

	@Test
	void testConstructorUsuarioNifLimiteSuperiorDigitos() {
		assertThrows(IllegalArgumentException.class, () -> {
			new Usuario("326984789E", "Geronimo");
		});
	}

	@Test
	void testConstructorUsuarioNifSinCaracter() {
		assertThrows(IllegalArgumentException.class, () -> {
			new Usuario("326984789", "Geronimo");
		});
	}

	@Test
	void testConstructorUsuarioNifInvalido() {
		assertThrows(IllegalArgumentException.class, () -> {
			new Usuario("32698478P", "Geronimo");
		});
	}

	@Test
	void testConstructorUsuarioNifInvalidoLetraInvalidaI() {
		assertThrows(IllegalArgumentException.class, () -> {
			new Usuario("32698478I", "Geronimo");
		});
	}

	@Test
	void testConstructorUsuarioNifInvalidoLetraInvalidaO() {
		assertThrows(IllegalArgumentException.class, () -> {
			new Usuario("32698478O", "Geronimo");
		});
	}

	@Test
	void testConstructorUsuarioNifInvalidoLetraInvalidaÑ() {
		assertThrows(IllegalArgumentException.class, () -> {
			new Usuario("32698478Ñ", "Geronimo");
		});
	}

	@Test
	void testConstructorUsuarioNifInvalidoLetraInvalidaU() {
		assertThrows(IllegalArgumentException.class, () -> {
			new Usuario("32698478U", "Geronimo");
		});
	}

	@Test
	void testEqualsValido() {
		assertEquals(user, user);
		assertEquals(user, sameUser);
		assertNotEquals(user, true);
		assertNotEquals(user, differentUser);
	}

	@Test
	void testEqualsNoValido() {
		assertThrows(IllegalArgumentException.class, () -> {
			user.equals(null);
		});
	}
	
	@Tag("Cobertura")
	@Test
	void testEqualNifIgual() {
		Usuario user2 = new Usuario(nif, "Gonzalo");
		assertNotEquals(user, user2);
	}
}
