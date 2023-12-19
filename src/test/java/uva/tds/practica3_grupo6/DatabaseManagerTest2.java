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
	private Billete billete;
	private Billete billete2;
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
		billete = new Billete("c321", recorrido, user, "reservado");
		billete2 = new Billete("321", recorrido, user, "comprado");
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
        String ID = user.getNif();
        databaseManager.addUsuario(user);
        assertEquals(user, databaseManager.getUsuario(ID));
	}

	@Test
	void testEliminarUsuario() {
		 String ID = user.getNif();
	     databaseManager.addUsuario(user);
	     databaseManager.eliminarUsuario(user.getNif());
	     assertNull(databaseManager.getUsuario(user.getNif()));
	}

	@Test
	void testActualizarUsuario() {
		 String ID = user.getNif();
	     databaseManager.addUsuario(user);
	     databaseManager.addRecorrido(recorrido);
	     Usuario updateUser = new Usuario(ID, "Paco");
		 databaseManager.actualizarUsuario(updateUser);
	     assertEquals(updateUser,databaseManager.getUsuario(ID));
	}

	@Test
	void testAddBillete() {
		String ID = billete.getLocalizador();
		databaseManager.addBillete(billete);
		databaseManager.addBillete(billete2);
		ArrayList<Billete> billetes = new ArrayList<>();
		billetes.add(billete); billetes.add(billete2);
		assertEquals(billete, databaseManager.getBilletes(ID));
	}

	@Test
	void testEliminarBilletes() {
		String ID = billete.getLocalizador();
		databaseManager.addBillete(billete);
		databaseManager.addBillete(billete2);
		databaseManager.eliminarBilletes(ID);
		ArrayList<Billete> billetes = new ArrayList<>();
		billetes.add(billete); billetes.add(billete2);
		assertNull(databaseManager.getBilletes(ID));
		assertNotEquals(billete, databaseManager.getBilletes(ID));
	}

	@Test
	void testActualizarBilletes() {
		String ID = billete.getLocalizador();
		databaseManager.addRecorrido(recorrido);
		databaseManager.addUsuario(user);
		databaseManager.addBillete(billete);
		billete.setComprado();
		assertNotEquals(billete,databaseManager.getBilletes(ID));
		databaseManager.actualizarBilletes(billete);
		assertEquals(billete, databaseManager.getBilletes(ID));
	}


	@Test
	void testGetBilletesDeRecorrido() {
		String ID = recorrido.getID();
		databaseManager.addBillete(billete);
		databaseManager.addBillete(billete2);
		ArrayList<Billete> billetes = new ArrayList<>();
		billetes.add(billete); billetes.add(billete2);
		assertEquals(billete, databaseManager.getBilletesDeRecorrido(ID));
	}

	@Test
	void testGetBilletesDeUsuario() {
		String ID = user.getNif();
		databaseManager.addBillete(billete);
		databaseManager.addBillete(billete2);
		ArrayList<Billete> billetes = new ArrayList<>();
		billetes.add(billete); billetes.add(billete2);
		assertEquals(billete, databaseManager.getBilletesDeUsuario(ID));
	}

	@Test
	void testClearDatabase() {
		 String ID = user.getNif();
	     databaseManager.addUsuario(user);
	     databaseManager.clearDatabase();
	     assertNull(databaseManager.getUsuario(ID));
	}

}
