package uva.tds.practica3_grupo6;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.ArrayList;

import org.easymock.TestSubject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;


class DatabaseManagerTest2 {
	
	private DatabaseManager databaseManager;
	private Usuario user;
	private Usuario differentUser;
	private BusRecorrido recorrido;
	private BusRecorrido recorridoLI;
	private TrainRecorrido differentRecorrido;
	private TrainRecorrido differentFecha;
	
    @BeforeEach
    void setUp() {
    	String nif = "32698478E";
		String nombre = "Geronimo";
		user = new Usuario(nif, nombre);
		differentUser = new Usuario("79105889B", nombre);
		String id = "c12345";
		Connection connection = new Connection("Valladolid", "Palencia", 30);
		LocalDateTime dateTime = LocalDateTime.of(2023, 10, 27, 19, 06, 50);
		double price = 1.0;
		int numSeats = 50;
		recorrido = new BusRecorrido(id, connection, price, dateTime, numSeats);
		recorridoLI = new BusRecorrido("c12354", connection, price, dateTime, numSeats);
		differentFecha = new TrainRecorrido("train123", connection, price, LocalDateTime.of(2023, 9, 26, 13, 02, 50), numSeats);
		differentRecorrido = new TrainRecorrido("train", connection, price, dateTime, numSeats);
        databaseManager = new DatabaseManager();
    }

    @AfterEach
    void tearDown() {
        databaseManager.clearDatabase();
    }

	@Test
	void testAddRecorrido() {
        String ID = recorrido.getID();
        databaseManager.addRecorrido(recorrido);
        assertEquals(recorrido, databaseManager.getRecorrido(ID));
     }

	@Test
	void testEliminarRecorrido() {
		String ID = recorrido.getID();
		databaseManager.addRecorrido(recorrido);
		databaseManager.eliminarRecorrido(ID);
		assertNull(databaseManager.getRecorrido(ID));
	}

	@Test
	void testActualizarRecorrido() {
		String ID = recorrido.getID();
		databaseManager.addRecorrido(recorrido);
		recorrido.decreaseAvailableSeats(13);
		databaseManager.actualizarRecorrido(recorrido);
		assertEquals(recorrido, databaseManager.getRecorrido(ID));
	}

	@Test
	void testGetRecorridos() {
		databaseManager.addRecorrido(recorrido);
		databaseManager.addRecorrido(recorridoLI);
		databaseManager.addRecorrido(differentRecorrido);
		ArrayList<Recorrido> recorridos = new ArrayList<>();
		recorridos.add(recorrido); recorridos.add(recorridoLI); recorridos.add(differentRecorrido);
		assertEquals(recorridos,databaseManager.getRecorridos());
	}

	@Test
	void testGetRecorridosLocalDate() {
		databaseManager.addRecorrido(recorrido);
		databaseManager.addRecorrido(recorridoLI);
		databaseManager.addRecorrido(differentRecorrido);
		databaseManager.addRecorrido(differentFecha);
		ArrayList<Recorrido> recorridos = new ArrayList<>();
		recorridos.add(recorrido); recorridos.add(recorridoLI); recorridos.add(differentRecorrido);
		assertEquals(recorridos,databaseManager.getRecorridos(LocalDate.of(2023, 10, 27)));
	}

	@Test
	void testAddUsuario() {
        String ID = recorrido.getID();
        databaseManager.addUsuario(user);
        assertEquals(recorrido, databaseManager.getRecorrido(ID));
	}

	@Test
	void testEliminarUsuario() {
		fail("Not yet implemented");
	}

	@Test
	void testActualizarUsuario() {
		fail("Not yet implemented");
	}

	@Test
	void testGetUsuario() {
		fail("Not yet implemented");
	}

	@Test
	void testAddBillete() {
		fail("Not yet implemented");
	}

	@Test
	void testEliminarBilletes() {
		fail("Not yet implemented");
	}

	@Test
	void testActualizarBilletes() {
		fail("Not yet implemented");
	}

	@Test
	void testGetBilletes() {
		fail("Not yet implemented");
	}

	@Test
	void testGetBilletesDeRecorrido() {
		fail("Not yet implemented");
	}

	@Test
	void testGetBilletesDeUsuario() {
		fail("Not yet implemented");
	}

	@Test
	void testClearDatabase() {
		fail("Not yet implemented");
	}

}
