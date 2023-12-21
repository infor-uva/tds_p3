package uva.tds.practica3_grupo6;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;


class DatabaseManagerTest {
	
	private DatabaseManager databaseManager;
	private Usuario user;
	private Usuario user2;
	private Billete billete;
	private Billete billete2;
	private BusRecorrido recorrido;
	private BusRecorrido recorridoLI;
	private TrainRecorrido differentRecorrido;
	private TrainRecorrido differentFecha;
	
    @BeforeEach
    void setUp() {
    	String nif = "32698478E";
    	String nif2 = "32698847T";
		String nombre = "Geronimo";
		String nombre2 = "Paco";
		user = new Usuario(nif, nombre);
		user2 = new Usuario(nif2, nombre2);
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
		billete2 = new Billete("321c", recorrido, user, "comprado");
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
	void testAddRecorridoNull() {
        assertThrows(IllegalArgumentException.class, () -> {
			databaseManager.addRecorrido(null);
		});
     }
	
	@Test
	void testAddRecorridoYaExistente() {
        String ID = recorrido.getID();
        databaseManager.addRecorrido(recorrido);
        assertEquals(recorrido, databaseManager.getRecorrido(ID));
        assertThrows(IllegalStateException.class, () -> {
			databaseManager.addRecorrido(recorrido);
		});
     }

	@Test
	void testEliminarRecorrido() {
		String ID = recorrido.getID();
		databaseManager.addRecorrido(recorrido);
		databaseManager.eliminarRecorrido(ID);
		assertNull(databaseManager.getRecorrido(ID));
	}
	
	@Test
	void testEliminarRecorridoNull() {
		assertThrows(IllegalArgumentException.class, () -> {
			databaseManager.eliminarRecorrido(null);
		});
	}
	
	@Test
	void testEliminarRecorridoNoExiste() {
		String ID = "4321";
		databaseManager.addRecorrido(recorrido);
		assertThrows(IllegalStateException.class, () -> {
			databaseManager.eliminarRecorrido(ID);
		});
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
	void testActualizarRecorridoNull() {
		assertThrows(IllegalArgumentException.class, () -> {
			databaseManager.actualizarRecorrido(null);
		});
	}
	
	@Test
	void testActualizarRecorridoNoExiste() {
		assertThrows(IllegalStateException.class, () -> {
			databaseManager.actualizarRecorrido(recorrido);
		});
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
	void testAddUsuarioNull() {
		assertThrows(IllegalArgumentException.class, () -> {
			databaseManager.addUsuario(null);
		});
	}
	@Test
	void testAddUsuarioYaExiste() {
        String ID = user.getNif();
        databaseManager.addUsuario(user);
        assertEquals(user, databaseManager.getUsuario(ID));
        assertThrows(IllegalStateException.class, () -> {
			databaseManager.addUsuario(user);
		});
	}

	@Test
	void testEliminarUsuario() {
		 String ID = user.getNif();
	     databaseManager.addUsuario(user);
	     databaseManager.eliminarUsuario(ID);
	     assertNull(databaseManager.getUsuario(ID));
	}
	
	@Test
	void testEliminarUsuarioNull() {
		assertThrows(IllegalArgumentException.class, () -> {
			databaseManager.eliminarUsuario(null);
		});
	}
	
	@Test
	void testEliminarUsuarioNoExiste() {
		 String ID = user.getNif();
	     assertThrows(IllegalStateException.class, () -> {
				databaseManager.eliminarUsuario(ID);
			});
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
	void testActualizarUsuarioNull() {
		 assertThrows(IllegalArgumentException.class, () -> {
				databaseManager.actualizarUsuario(null);
			});
	}
	
	@Test
	void testActualizarUsuarioNoExiste() {
		 assertThrows(IllegalStateException.class, () -> {
				databaseManager.actualizarUsuario(user);
			});
	}

	@Test
	void testAddBillete() {
		String ID = billete.getLocalizador();
		databaseManager.addRecorrido(recorrido);
		databaseManager.addUsuario(user);
		databaseManager.addBillete(billete);
		databaseManager.addBillete(billete2);
		recorrido.decreaseAvailableSeats(2);
		ArrayList<Billete> billetes = new ArrayList<>();
		billetes.add(billete);
		assertEquals(billetes, databaseManager.getBilletes(ID));
	}
	
	@Test
	void testAddBilleteNull() {
		assertThrows(IllegalArgumentException.class, () -> {
			databaseManager.addBillete(null);
		});
	}

	@Test
	void testEliminarBilletes() {
		String ID = billete.getLocalizador();
		databaseManager.addRecorrido(recorrido);
		databaseManager.addUsuario(user);
		databaseManager.addBillete(billete);
		databaseManager.addBillete(billete2);
		recorrido.decreaseAvailableSeats(2);
		databaseManager.eliminarBilletes(ID);
		recorrido.increaseAvailableSeats(1);
		ArrayList<Billete> billetes = new ArrayList<>();
		billetes.add(billete2);
		assertEquals(true,databaseManager.getBilletes(ID).isEmpty());
		assertNotEquals(billete, databaseManager.getBilletes(billete2.getLocalizador()));
	}
	
	@Test
	void testEliminarBilletesNull() {
		assertThrows(IllegalArgumentException.class, () -> {
			databaseManager.eliminarBilletes(null);
		});
	}
	
	@Test
	void testEliminarBilletesNoExisten() {
		String ID = billete.getLocalizador();
		assertThrows(IllegalStateException.class, () -> {
			databaseManager.eliminarBilletes(ID);
		});
	}

	@Test
	void testActualizarBilletes() {
		String ID = billete.getLocalizador();
		databaseManager.addRecorrido(recorrido);
		databaseManager.addUsuario(user);
		databaseManager.addBillete(billete);
		
		recorrido.decreaseAvailableSeats(1);
		
		ArrayList<Billete> billetes = new ArrayList<>();
		billetes.add(billete);
		billete.setComprado();
		assertNotEquals(billetes,databaseManager.getBilletes(ID));
		
		databaseManager.actualizarBilletes(billete);
		assertEquals(billetes, databaseManager.getBilletes(ID));
	}
	
	@Test
	void testActualizarBilletesNull() {
		assertThrows(IllegalArgumentException.class, () -> {
			databaseManager.actualizarBilletes(null);
		});
	}
	
	@Test
	void testActualizarBilletesNoExisten() {
		assertThrows(IllegalStateException.class, () -> {
			databaseManager.actualizarBilletes(billete);
		});
	}


	@Test
	void testGetBilletesDeRecorrido() {
		String ID = recorrido.getID();
		databaseManager.addRecorrido(recorrido);
		databaseManager.addUsuario(user);
		databaseManager.addBillete(billete);
		databaseManager.addBillete(billete2);
		recorrido.decreaseAvailableSeats(2);
		ArrayList<Billete> billetes = new ArrayList<>();
		billetes.add(billete); billetes.add(billete2);
		assertEquals(billetes, databaseManager.getBilletesDeRecorrido(ID));
	}
	
	@Test
	@Tag("Cobertura")
	void testGetBilletesConMasBilletesDeDiferenteRecorrido() {
		String ID = recorrido.getID();
		databaseManager.addRecorrido(recorrido);
		databaseManager.addRecorrido(differentRecorrido);
		databaseManager.addUsuario(user);
		databaseManager.addBillete(billete);
		databaseManager.addBillete(new Billete(ID, differentRecorrido, user, Billete.ESTADO_COMPRADO));
		recorrido.decreaseAvailableSeats(1);
		ArrayList<Billete> billetes = new ArrayList<>();
		billetes.add(billete);
		assertEquals(billetes, databaseManager.getBilletesDeRecorrido(ID));
	}

	@Test
	void testGetBilletesDeUsuario() {
		String ID = user.getNif();
		databaseManager.addRecorrido(recorrido);
		databaseManager.addUsuario(user);
		databaseManager.addBillete(billete);
		databaseManager.addBillete(billete2);
		recorrido.decreaseAvailableSeats(2);
		ArrayList<Billete> billetes = new ArrayList<>();
		billetes.add(billete); billetes.add(billete2);
		assertEquals(billetes, databaseManager.getBilletesDeUsuario(ID));
	}
	
	@Test
	@Tag("Cobertura")
	void testGetBilletesConMasBilletesDeDiferenteUsuario() {
		String ID = user.getNif();
		databaseManager.addRecorrido(recorrido);
		databaseManager.addUsuario(user);
		databaseManager.addUsuario(user2);
		databaseManager.addBillete(billete);
		databaseManager.addBillete(new Billete("loc", recorrido, user2, Billete.ESTADO_RESERVADO));
		recorrido.decreaseAvailableSeats(2);
		ArrayList<Billete> billetes = new ArrayList<>();
		billetes.add(billete);
		assertEquals(billetes, databaseManager.getBilletesDeUsuario(ID));
	}
	
	@Test
	void testClearDatabase() {
		 String ID = user.getNif();
	     databaseManager.addUsuario(user);
	     databaseManager.clearDatabase();
	     assertNull(databaseManager.getUsuario(ID));
	}

}
