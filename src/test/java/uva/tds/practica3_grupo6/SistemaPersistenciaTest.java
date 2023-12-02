package uva.tds.practica3_grupo6;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.easymock.EasyMock;
import org.easymock.Mock;
import org.easymock.TestSubject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * Class made for tests the methods of {@link SistemaPersistencia}
 * 
 * @author hugcubi
 * @author diebomb
 * @author migudel
 * 
 * @version 28/11/23
 */
class SistemaPersistenciaTest {

	private static final double ERROR_MARGIN = 0.00001;
	private static final String BUS = Recorrido.BUS;
	private static final String TRAIN = Recorrido.TRAIN;
	private static final String ESTADO_RESERVADO = Billete.ESTADO_RESERVADO;
	private static final String ESTADO_COMPRADO = Billete.ESTADO_COMPRADO;

	private String nif;
	private String nombre;
	private Usuario user;
	private Usuario differentUser;
	private String id;
	private String idLI;
	private String origin;
	private String destination;
	private String transport;
	private double price;
	private LocalDate date;
	private LocalTime time;
	private int duration;
	private Recorrido recorrido;
	private Recorrido recorridoLI;
	private Recorrido differentRecorrido;
	private int numSeats;
	private LocalDateTime newDateTime;
	private LocalDate newDate;
	private LocalTime newTime;

	@Mock
	private IDatabaseManager database;

	@TestSubject
	private SistemaPersistencia sistema;

	@BeforeEach
	void setUp() {
		nif = "32698478E";
		nombre = "Geronimo";
		user = new Usuario(nif, nombre);
		differentUser = new Usuario("79105889B", nombre);
		id = "c12345";
		idLI = "c";
		origin = "Valladolid";
		destination = "Galicia";
		transport = BUS;
		date = LocalDate.of(2023, 10, 27);
		time = LocalTime.of(19, 06, 50);
		price = 1.0;
		numSeats = 50;
		duration = 250;
		recorrido = new Recorrido(id, origin, destination, transport, price, date, time, numSeats, duration);
		recorridoLI = new Recorrido(idLI, origin, destination, transport, price, date, time, numSeats, duration);
		transport = TRAIN;
		differentRecorrido = new Recorrido("dif", origin, destination, transport, price, date, time, numSeats,
				duration);
		newDateTime = LocalDateTime.of(2023, 5, 14, 22, 56, 20);
		newDate = LocalDate.of(2024, 2, 4);
		newTime = LocalTime.of(12, 2, 4);

		database = EasyMock.mock(IDatabaseManager.class);

		sistema = new SistemaPersistencia(database);
	}

	/**
	 * FINDME Tests for
	 * {@link SistemaPersistencia#SistemaPersistencia(IDatabaseManager)}
	 */
	@Test
	void testConstructor() {
		SistemaPersistencia sistema = new SistemaPersistencia(database);
		assertNotNull(sistema);
		assertEquals(database, sistema.getDataBaseManager());
	}

	/**
	 * FINDME Tests for {@link SistemaPersistencia#addRecorrido(Recorrido)}
	 */
	@Test
	void testAddRecorridoValido() {
		ArrayList<Recorrido> returned = new ArrayList<>();
		returned.add(recorrido);

		database.addRecorrido(recorrido);
		EasyMock.expectLastCall();
		EasyMock.expect(database.getRecorridos()).andReturn(returned).times(1);
		EasyMock.replay(database);

		sistema.addRecorrido(recorrido);
		List<Recorrido> recorridos = sistema.getRecorridos();
		List<Recorrido> recorridosCheck = new ArrayList<>();
		recorridosCheck.add(recorrido);
		assertEquals(recorridosCheck, recorridos);

		EasyMock.verify(database);
	}

	@Test
	void testAddRecorridoConRecorridoNull() {
		database.addRecorrido(null);
		EasyMock.expectLastCall().andThrow(new IllegalArgumentException());
		EasyMock.replay(database);

		assertThrows(IllegalArgumentException.class, () -> {
			sistema.addRecorrido(null);
		});

		EasyMock.verify(database);
	}

	@Test
	void testAddRecorridoConRecorridoYaEnSystem() {
		database.addRecorrido(recorrido);
		EasyMock.expectLastCall();
		database.addRecorrido(recorrido);
		EasyMock.expectLastCall().andThrow(new IllegalStateException());
		EasyMock.replay(database);

		sistema.addRecorrido(recorrido);
		assertEquals(recorrido, recorrido);
		assertThrows(IllegalStateException.class, () -> {
			sistema.addRecorrido(recorrido);
		});

		EasyMock.verify(database);
	}

	/**
	 * FINDME Tests for {@link SistemaPersistencia#removeRecorrido(String))}
	 */
	@Test
	void testRemoveRecorridoValidoConIDLimiteInferior() {
		ArrayList<Recorrido> returned = new ArrayList<>();
		returned.add(recorridoLI);

		// addRecorrido
		database.addRecorrido(recorridoLI);
		EasyMock.expectLastCall();
		// removeRecorrido
		EasyMock.expect(database.getBilletesDeRecorrido(idLI)).andReturn(new ArrayList<>()).times(1);
		EasyMock.expect(database.getRecorrido(idLI)).andReturn(recorridoLI).times(1);
		database.eliminarRecorrido(idLI);
		EasyMock.expectLastCall().times(1);
		// getRecorridos
		EasyMock.expect(database.getRecorridos()).andReturn(new ArrayList<>()).times(1);
		EasyMock.replay(database);

		sistema.addRecorrido(recorridoLI);
		sistema.removeRecorrido(idLI);
		assertEquals(new ArrayList<>(), sistema.getRecorridos());

		EasyMock.verify(database);
	}

	@Test
	void testRemoveRecorridoConIDNull() {
		assertThrows(IllegalArgumentException.class, () -> {
			sistema.removeRecorrido(null);
		});
	}

	@Test
	void testRemoveRecorridoConIDVacio() {
		assertThrows(IllegalArgumentException.class, () -> {
			sistema.removeRecorrido("");
		});
	}

	@Test
	void testRemoveRecorridoConRecorridoFueraSystem() {
		EasyMock.expect(database.getRecorrido(id)).andReturn(null).times(1);
		EasyMock.replay(database);

		assertThrows(IllegalStateException.class, () -> {
			sistema.removeRecorrido(id);
		});

		EasyMock.verify(database);
	}

	@Test
	void testRemoveRecorridoConRecorridoEnSystemConBilletesAsociados() {
		String locator = "T12345";
		ArrayList<Billete> returned = new ArrayList<Billete>();
		List<Billete> billetes = new ArrayList<>();
		Usuario usuario = new Usuario("32698478E", "Geronimo");
		Billete ticket = new Billete(locator, recorrido, usuario, ESTADO_COMPRADO);
		returned.add(ticket);

		// addReocorrido
		database.addRecorrido(recorrido);
		EasyMock.expectLastCall();
		// comprarBilletes
		EasyMock.expect(database.getBilletes(locator)).andReturn(new ArrayList<>());
		database.addBillete(ticket);
		EasyMock.expectLastCall();
		Recorrido modificado = recorrido.clone();
		modificado.decreaseAvailableSeats(1);
		database.actualizarRecorrido(modificado);
		EasyMock.expectLastCall();
		EasyMock.expect(database.getUsuario(usuario.getNif())).andReturn(null);
		database.addUsuario(usuario);
		// getAssociatedBilletesToRoute
		EasyMock.expect(database.getRecorrido(id)).andReturn(recorrido);
		EasyMock.expect(database.getBilletesDeRecorrido(id)).andReturn(returned);
		// removeRecorrido
		EasyMock.expect(database.getRecorrido(id)).andReturn(recorrido);
		EasyMock.expect(database.getBilletesDeRecorrido(id)).andReturn(returned);
		EasyMock.replay(database);

		sistema.addRecorrido(recorrido);
		sistema.comprarBilletes("T12345", usuario, recorrido, 1);
		billetes.add(ticket);
		assertEquals(billetes, sistema.getAssociatedBilletesToRoute(id));

		assertThrows(IllegalStateException.class, () -> {
			sistema.removeRecorrido(id);
		});

		EasyMock.verify(database);
	}

	/**
	 * FINDME Tests for
	 * {@link SistemaPersistencia#getPrecioTotalBilletesUsuario(String)}
	 */
	@Tag("Cobertura")
	@Test
	void testGetPrecioTotalBilletesUsuarioBus() {
		ArrayList<Billete> returned = new ArrayList<>();
		String localizador = "T12345";
		int numBilletes = 5;
		for (int i = 0; i < numBilletes; i++)
			returned.add(new Billete(localizador, recorrido, user, ESTADO_COMPRADO));

		// mock comprarBilletes
		EasyMock.expect(database.getBilletes(localizador)).andReturn(new ArrayList<>());

		database.addBillete(new Billete(localizador, recorrido, user, ESTADO_COMPRADO));
		EasyMock.expectLastCall().times(numBilletes);
		
		Recorrido clonRecorrido= recorrido.clone();
		clonRecorrido.decreaseAvailableSeats(numBilletes);

		database.actualizarRecorrido(clonRecorrido);
		EasyMock.expectLastCall();

		EasyMock.expect(database.getUsuario(user.getNif())).andReturn(null);
		database.addUsuario(user);
		EasyMock.expectLastCall();

		// mock getPrecioTotal
		EasyMock.expect(database.getUsuario(user.getNif())).andReturn(user);
		EasyMock.expect(database.getBilletesDeUsuario(user.getNif())).andReturn(returned);
		EasyMock.replay(database);

		sistema.comprarBilletes(localizador, user, recorrido, numBilletes);
		assertEquals(5.0, sistema.getPrecioTotalBilletesUsuario(user.getNif()), ERROR_MARGIN);

		EasyMock.verify(database);
	}

	@Tag("Cobertura")
	@Test
	void testGetPrecioTotalBilletesUsuarioPrecioRecorridoTren() {
		ArrayList<Billete> returned = new ArrayList<>();
		String localizador = "T12345";
		int numBilletes = 5;
		for (int i = 0; i < numBilletes; i++)
			returned.add(new Billete(localizador, differentRecorrido, user, ESTADO_COMPRADO));

		// mock comprarBilletes
		EasyMock.expect(database.getBilletes(localizador)).andReturn(new ArrayList<>());

		database.addBillete(new Billete(localizador, differentRecorrido, user, ESTADO_COMPRADO));
		EasyMock.expectLastCall().times(numBilletes);

		Recorrido clonRecorrido = new Recorrido("dif", origin, destination, TRAIN, price, date, time, numSeats,
				duration);
		clonRecorrido.decreaseAvailableSeats(numBilletes);

		database.actualizarRecorrido(clonRecorrido);
		EasyMock.expectLastCall();

		EasyMock.expect(database.getUsuario(user.getNif())).andReturn(null);
		database.addUsuario(user);
		EasyMock.expectLastCall();

		// mock getPrecioTotal
		EasyMock.expect(database.getUsuario(user.getNif())).andReturn(user);
		EasyMock.expect(database.getBilletesDeUsuario(user.getNif())).andReturn(returned);
		EasyMock.replay(database);

		sistema.comprarBilletes(localizador, user, differentRecorrido, numBilletes);
		assertEquals(4.5, sistema.getPrecioTotalBilletesUsuario(user.getNif()), ERROR_MARGIN);

		EasyMock.verify(database);
	}

	@Test
	void testGetPrecioTotalBilletesUsuarioNull() {
		assertThrows(IllegalArgumentException.class, () -> {
			sistema.getPrecioTotalBilletesUsuario(null);
		});
	}

	@Test
	void testGetPrecioTotalBilletesUsuarioVacio() {
		assertThrows(IllegalArgumentException.class, () -> {
			sistema.getPrecioTotalBilletesUsuario("");
		});
	}

	@Test
	void testGetPrecioTotalBilletesUsuarioSinBilletes() {
		assertThrows(IllegalArgumentException.class, () -> {
			sistema.getPrecioTotalBilletesUsuario("79105889B");
		});
	}

	@Test
	void testGetPrecioTotalBilletesUsuarioConNifLimiteInferiorDigitos() {
		assertThrows(IllegalArgumentException.class, () -> {
			sistema.getPrecioTotalBilletesUsuario("3269847E");
		});
	}

	@Test
	void testGetPrecioTotalBilletesUsuarioConNifLimiteSuperiorDigitos() {
		assertThrows(IllegalArgumentException.class, () -> {
			sistema.getPrecioTotalBilletesUsuario("326984789E");
		});
	}

	@Test
	void testGetPrecioTotalBilletesUsuarioConNifSinCaracter() {
		assertThrows(IllegalArgumentException.class, () -> {
			sistema.getPrecioTotalBilletesUsuario("3269847");
		});
	}

	@Test
	void testGetPrecioTotalBilletesUsuarioConNifInvalido() {
		assertThrows(IllegalArgumentException.class, () -> {
			sistema.getPrecioTotalBilletesUsuario("32698478P");
		});
	}

	@Test
	void testGetPrecioTotalBilletesUsuarioConNifInvalidoLetraInvalidaI() {
		assertThrows(IllegalArgumentException.class, () -> {
			sistema.getPrecioTotalBilletesUsuario("32698478I");
		});
	}

	@Test
	void testGetPrecioTotalBilletesUsuarioConNifInvalidoLetraInvalidaO() {
		assertThrows(IllegalArgumentException.class, () -> {
			sistema.getPrecioTotalBilletesUsuario("32698478O");
		});
	}

	@Test
	void testGetPrecioTotalBilletesUsuarioConNifInvalidoLetraInvalidaÑ() {
		assertThrows(IllegalArgumentException.class, () -> {
			sistema.getPrecioTotalBilletesUsuario("32698478Ñ");
		});
	}

	@Test
	void testGetPrecioTotalBilletesUsuarioConNifInvalidoLetraInvalidaU() {
		assertThrows(IllegalArgumentException.class, () -> {
			sistema.getPrecioTotalBilletesUsuario("32698478U");
		});
	}

	/**
	 * FINDME Tests for
	 * {@link SistemaPersistencia#getRecorridosDisponiblesFecha(LocalDate)}
	 */
	@Test
	void testGetRecorridosDisponiblesFecha() {
		ArrayList<Recorrido> returned = new ArrayList<>();
		returned.add(recorrido);
		returned.add(differentRecorrido);
		// mock sistema.addRecorrido
		database.addRecorrido(recorrido);
		database.addRecorrido(differentRecorrido);
		EasyMock.expectLastCall();
		// getRecorridosDisponiblesFecha
		EasyMock.expect(database.getRecorridos(date)).andReturn(returned).times(2);

		EasyMock.replay(database);

		ArrayList<Recorrido> recorridos = new ArrayList<>();
		recorridos.add(recorrido);
		recorridos.add(differentRecorrido);
		sistema.addRecorrido(recorrido);
		sistema.addRecorrido(differentRecorrido);
		assertEquals(recorridos, sistema.getRecorridosDisponiblesFecha(date));

		EasyMock.verify(database);
	}

	@Test
	void testGetRecorridosDisponiblesFechaSinFecha() {
		assertThrows(IllegalArgumentException.class, () -> {
			sistema.getRecorridosDisponiblesFecha(null);
		});
	}

	@Tag("Cobertura")
	@Test
	void testGetRecorridosDisponiblesFechaSinRecorridos() {
		EasyMock.expect(database.getRecorridos(date)).andReturn(null);
		EasyMock.replay(database);
		assertThrows(IllegalStateException.class, () -> {
			sistema.getRecorridosDisponiblesFecha(date);
		});
		EasyMock.verify(database);
	}

	/**
	 * FINDME Tests for {@link System#getAssociatedBilletesToRoute(String))}
	 */
	@Test
	void testGetAssociatedBilletesToRouteValidoConIDLimiteInferior() {
		String locator = "c";
		ArrayList<Billete> returned = new ArrayList<>();
		returned.add(new Billete(locator, recorrido, user, ESTADO_RESERVADO));
		returned.add(new Billete(locator, recorrido, differentUser, ESTADO_COMPRADO));
		returned.add(new Billete(locator, recorrido, differentUser, ESTADO_COMPRADO));
		// addRecorrido
		database.addRecorrido(recorrido);
		EasyMock.expectLastCall();
		// reservarBilletes
		EasyMock.expect(database.getBilletes(locator)).andReturn(new ArrayList<>());
		database.addBillete(new Billete(locator, recorrido, user, ESTADO_RESERVADO));
		EasyMock.expectLastCall().times(1);
		Recorrido modificado = recorrido.clone();
		modificado.decreaseAvailableSeats(1);
		database.actualizarRecorrido(modificado);
		EasyMock.expectLastCall().times(1);
		// comprarBilletes
		EasyMock.expect(database.getBilletes(locator)).andReturn(new ArrayList<>());
		database.addBillete(new Billete(locator, recorrido, differentUser, ESTADO_COMPRADO));
		EasyMock.expectLastCall().times(2);
		Recorrido modificado2 = modificado.clone();
		modificado2.decreaseAvailableSeats(2);
		database.actualizarRecorrido(modificado2);
		EasyMock.expectLastCall().times(1);
		EasyMock.expect(database.getUsuario(differentUser.getNif())).andReturn(user);
		// getAssociatedBilletesToRoute
		EasyMock.expect(database.getRecorrido(idLI)).andReturn(recorrido).times(1);
		EasyMock.expect(database.getBilletesDeRecorrido(idLI)).andReturn(returned).times(1);
		EasyMock.replay(database);

		sistema.addRecorrido(recorrido);
		sistema.reservarBilletes(locator, user, recorrido, 1);
		sistema.comprarBilletes(locator, differentUser, recorrido, 2);

		List<Billete> expected = new ArrayList<>();
		expected.add(new Billete(locator, recorrido, user, ESTADO_RESERVADO));
		expected.add(new Billete(locator, recorrido, differentUser, ESTADO_COMPRADO));
		expected.add(new Billete(locator, recorrido, differentUser, ESTADO_COMPRADO));

		assertEquals(expected, sistema.getAssociatedBilletesToRoute(idLI));

		EasyMock.verify(database);
	}

	@Test
	void testGetAssociatedBilletesToRouteConIDNull() {
		assertThrows(IllegalArgumentException.class, () -> {
			sistema.getAssociatedBilletesToRoute(null);
		});
	}

	@Test
	void testGetAssociatedBilletesToRouteConIDVacio() {
		assertThrows(IllegalArgumentException.class, () -> {
			sistema.getAssociatedBilletesToRoute("");
		});
	}

	@Test
	void testGetAssociatedBilletesToRouteConRouteFueraDeSystem() {
		EasyMock.expect(database.getRecorrido(id)).andReturn(null);
		EasyMock.replay(database);

		assertThrows(IllegalStateException.class, () -> {
			sistema.getAssociatedBilletesToRoute(id);
		});

		EasyMock.verify(database);
	}

	/**
	 * FINDME Tests for {@link SistemaPersistencia#getDateOfRecorrido(String)}
	 */
	@Test
	void testGetDateOfRecorridoValidoConIDLimiteInferior() {
		database.addRecorrido(recorridoLI);
		EasyMock.expectLastCall();
		EasyMock.expect(database.getRecorrido(idLI)).andReturn(recorridoLI).times(1);
		EasyMock.replay(database);

		sistema.addRecorrido(recorridoLI);
		assertEquals(recorridoLI.getDate(), sistema.getDateOfRecorrido(idLI));

		EasyMock.verify(database);
	}

	@Test
	void testGetDateOfRecorridoConIDNull() {
		assertThrows(IllegalArgumentException.class, () -> {
			sistema.getDateOfRecorrido(null);
		});
	}

	@Test
	void testGetDateOfRecorridoNoValidoConIDLimiteInferior() {
		assertThrows(IllegalArgumentException.class, () -> {
			sistema.getDateOfRecorrido("");
		});
	}

	@Test
	void testGetDateOfRecorridoNoValidoConRecorridoFueraDeSystem() {
		EasyMock.expect(database.getRecorrido(id)).andReturn(null);
		EasyMock.replay(database);

		assertThrows(IllegalStateException.class, () -> {
			sistema.getDateOfRecorrido(id);
		});

		EasyMock.verify(database);
	}

	/**
	 * FINDME Tests for {@link SistemaPersistencia#getTimeOfRecorrido(String)}
	 */
	@Test
	void testGetTimeOfRecorridoValidoConIDLimiteInferior() {
		database.addRecorrido(recorridoLI);
		EasyMock.expectLastCall();
		EasyMock.expect(database.getRecorrido(idLI)).andReturn(recorridoLI);
		EasyMock.replay(database);

		sistema.addRecorrido(recorridoLI);
		assertEquals(recorridoLI.getTime(), sistema.getTimeOfRecorrido(idLI));

		EasyMock.verify(database);
	}

	@Test
	void testGetTimeOfRecorridoNoValidoConIDNull() {
		assertThrows(IllegalArgumentException.class, () -> {
			sistema.getTimeOfRecorrido(null);
		});
	}

	@Test
	void testGetTimeOfRecorridoNoValidoConIDLimiteInferior() {
		assertThrows(IllegalArgumentException.class, () -> {
			sistema.getTimeOfRecorrido("");
		});
	}

	@Test
	void testGetTimeOfRecorridoNoValidoConRecorridoFueraDeSystem() {
		assertThrows(IllegalStateException.class, () -> {
			sistema.getTimeOfRecorrido(id);
		});
	}

	/**
	 * FINDME Tests for {@link SistemaPersistencia#getDateTimeOfRecorrido(String)}
	 */
	@Test
	void testGetDateTimeOfRecorridoValidoConIDLimiteInferior() {
		database.addRecorrido(recorridoLI);
		EasyMock.expectLastCall();
		EasyMock.expect(database.getRecorrido(idLI)).andReturn(recorridoLI);
		EasyMock.replay(database);

		sistema.addRecorrido(recorridoLI);
		assertEquals(recorridoLI.getDateTime(), sistema.getDateTimeOfRecorrido(idLI));

		EasyMock.verify(database);
	}

	@Test
	void testGetDateTimeOfRecorridoNoValidoConIDNull() {
		assertThrows(IllegalArgumentException.class, () -> {
			sistema.getDateTimeOfRecorrido(null);
		});
	}

	@Test
	void testGetDateTimeOfRecorridoNoValidoConIDLimiteInferior() {
		assertThrows(IllegalArgumentException.class, () -> {
			sistema.getDateTimeOfRecorrido("");
		});
	}

	@Test
	void testGetDateTimeOfRecorridoNoValidoConRecorridoFueraDeSystem() {
		EasyMock.expect(database.getRecorrido(id)).andReturn(null);
		EasyMock.replay(database);

		assertThrows(IllegalStateException.class, () -> {
			sistema.getDateTimeOfRecorrido(id);
		});

		EasyMock.verify(database);
	}

	/**
	 * FINDME Tests for
	 * {@link SistemaPersistencia#updateRecorridoDate(String, LocalDate)}
	 */
	@Test
	void testUpdateRecorridoDateValidoConIDLimiteInferior() {
		database.addRecorrido(recorridoLI);
		EasyMock.expectLastCall();
		EasyMock.expect(database.getRecorrido(idLI)).andReturn(recorridoLI).times(3);
		EasyMock.replay(database);

		sistema.addRecorrido(recorridoLI);
		assertNotEquals(newDate, sistema.getDateOfRecorrido(recorridoLI.getID()));
		sistema.updateRecorridoDate(idLI, newDate);
		assertEquals(newDate, sistema.getDateOfRecorrido(recorridoLI.getID()));

		EasyMock.verify(database);
	}

	@Test
	void testUpdateRecorridoDateConIDNull() {
		assertThrows(IllegalArgumentException.class, () -> {
			sistema.updateRecorridoDate(null, newDate);
		});
	}

	@Test
	void testUpdateRecorridoDateConIDVacio() {
		assertThrows(IllegalArgumentException.class, () -> {
			sistema.updateRecorridoDate("", newDate);
		});
	}

	@Test
	void testUpdateRecorridoDateConRecorridoFueraDelsystem() {
		EasyMock.expect(database.getRecorrido(id)).andReturn(null);
		EasyMock.replay(database);

		assertThrows(IllegalStateException.class, () -> {
			sistema.updateRecorridoDate(id, newDate);
		});

		EasyMock.verify(database);
	}

	@Test
	void testUpdateRecorridoDateConDateNull() {
		database.addRecorrido(recorrido);
		EasyMock.expectLastCall();
		EasyMock.expect(database.getRecorrido(id)).andReturn(recorrido);
		EasyMock.replay(database);

		sistema.addRecorrido(recorrido);
		assertThrows(IllegalArgumentException.class, () -> {
			sistema.updateRecorridoDate(id, null);
		});

		EasyMock.verify(database);
	}

	@Test
	void testUpdateRecorridoDateConDateActual() {
		database.addRecorrido(recorrido);
		EasyMock.expectLastCall();
		EasyMock.expect(database.getRecorrido(id)).andReturn(recorrido);
		EasyMock.replay(database);

		sistema.addRecorrido(recorrido);
		assertThrows(IllegalStateException.class, () -> {
			sistema.updateRecorridoDate(id, recorrido.getDate());
		});

		EasyMock.verify(database);
	}

	/**
	 * FINDME Tests for
	 * {@link SistemaPersistencia#updateRecorridoTime(String, LocalTime)}
	 */
	@Test
	void testUpdateRecorridoTimeValidoConIDLimiteInferior() {
		database.addRecorrido(recorridoLI);
		EasyMock.expectLastCall();
		EasyMock.expect(database.getRecorrido(idLI)).andReturn(recorridoLI).times(3);
		EasyMock.replay(database);

		sistema.addRecorrido(recorridoLI);
		assertNotEquals(newTime, sistema.getTimeOfRecorrido(recorridoLI.getID()));
		sistema.updateRecorridoTime(idLI, newTime);
		assertEquals(newTime, sistema.getTimeOfRecorrido(recorridoLI.getID()));

		EasyMock.verify(database);
	}

	@Test
	void testUpdateRecorridoTimeConIDNull() {
		assertThrows(IllegalArgumentException.class, () -> {
			sistema.updateRecorridoTime(null, newTime);
		});
	}

	@Test
	void testUpdateRecorridoTimeConIDVacio() {
		assertThrows(IllegalArgumentException.class, () -> {
			sistema.updateRecorridoTime("", newTime);
		});
	}

	@Test
	void testUpdateRecorridoTimeConRecorridoFueraDelsystem() {
		EasyMock.expect(database.getRecorrido(id)).andReturn(null);
		EasyMock.replay(database);

		assertThrows(IllegalStateException.class, () -> {
			sistema.updateRecorridoTime(id, newTime);
		});

		EasyMock.verify(database);
	}

	@Test
	void testUpdateRecorridoTimeConTimeNull() {
		database.addRecorrido(recorrido);
		EasyMock.expectLastCall();
		EasyMock.expect(database.getRecorrido(id)).andReturn(recorrido);
		EasyMock.replay(database);

		sistema.addRecorrido(recorrido);
		assertThrows(IllegalArgumentException.class, () -> {
			sistema.updateRecorridoTime(id, null);
		});

		EasyMock.verify(database);
	}

	@Test
	void testUpdateRecorridoTimeConTimeActual() {
		database.addRecorrido(recorrido);
		EasyMock.expectLastCall();
		EasyMock.expect(database.getRecorrido(id)).andReturn(recorrido);
		EasyMock.replay(database);

		sistema.addRecorrido(recorrido);
		assertThrows(IllegalStateException.class, () -> {
			sistema.updateRecorridoTime(id, recorrido.getTime());
		});

		EasyMock.verify(database);
	}

	/**
	 * FINDME Tests for
	 * {@link SistemaPersistencia#updateRecorridoDateTime(String, LocalDateTime)}
	 */
	@Test
	void testUpdateRecorridoDateTimeValidoConIDLimiteInferior() {
		database.addRecorrido(recorridoLI);
		EasyMock.expectLastCall();
		EasyMock.expect(database.getRecorrido(idLI)).andReturn(recorridoLI).times(3);
		EasyMock.replay(database);

		sistema.addRecorrido(recorridoLI);
		assertNotEquals(newDateTime, sistema.getDateTimeOfRecorrido(idLI));
		sistema.updateRecorridoDateTime(idLI, newDateTime);
		assertEquals(newDateTime, sistema.getDateTimeOfRecorrido(idLI));

		EasyMock.verify(database);
	}

	@Test
	void testUpdateRecorridoDateTimeConIDNull() {
		assertThrows(IllegalArgumentException.class, () -> {
			sistema.updateRecorridoDateTime(null, newDateTime);
		});
	}

	@Test
	void testUpdateRecorridoDateTimeConIDVacio() {
		assertThrows(IllegalArgumentException.class, () -> {
			sistema.updateRecorridoDateTime("", newDateTime);
		});
	}

	@Test
	void testUpdateRecorridoDateTimeConRecorridoFueraDelsystem() {
		EasyMock.expect(database.getRecorrido(id)).andReturn(null);
		EasyMock.replay(database);

		assertThrows(IllegalStateException.class, () -> {
			sistema.updateRecorridoDateTime(id, newDateTime);
		});

		EasyMock.verify(database);
	}

	@Test
	void testUpdateRecorridoDateTimeConDateTimeNull() {
		database.addRecorrido(recorrido);
		EasyMock.expectLastCall();
		EasyMock.expect(database.getRecorrido(id)).andReturn(recorrido);
		EasyMock.replay(database);

		sistema.addRecorrido(recorrido);
		assertThrows(IllegalArgumentException.class, () -> {
			sistema.updateRecorridoDateTime(id, null);
		});

		EasyMock.verify(database);
	}

	@Test
	void testUpdateRecorridoDateTimeConDateTimeActual() {
		database.addRecorrido(recorrido);
		EasyMock.expectLastCall();
		EasyMock.expect(database.getRecorrido(id)).andReturn(recorrido);
		EasyMock.replay(database);

		sistema.addRecorrido(recorrido);
		assertThrows(IllegalStateException.class, () -> {
			sistema.updateRecorridoDateTime(id, recorrido.getDateTime());
		});

		EasyMock.verify(database);
	}

	/**
	 * FINDME Tests for
	 * {@link SistemaPersistencia#updateRecorrido(String, LocalDate, LocalTime)}
	 */
	@Test
	void testUpdateRecorridoValidoConIDLimiteInferior() {
		database.addRecorrido(recorridoLI);
		EasyMock.expectLastCall();
		EasyMock.expect(database.getRecorrido(idLI)).andReturn(recorridoLI).times(3);
		EasyMock.replay(database);

		sistema.addRecorrido(recorridoLI);
		assertNotEquals(newDateTime, sistema.getDateTimeOfRecorrido(idLI));
		sistema.updateRecorrido(idLI, newDateTime.toLocalDate(), newDateTime.toLocalTime());
		assertEquals(newDateTime, sistema.getDateTimeOfRecorrido(idLI));

		EasyMock.verify(database);
	}

	@Test
	void testUpdateRecorridoConIDNull() {
		assertThrows(IllegalArgumentException.class, () -> {
			sistema.updateRecorrido(null, newDate, newTime);
		});
	}

	@Test
	void testUpdateRecorridoConRecorridoFueraDelsystem() {
		EasyMock.expect(database.getRecorrido(id)).andReturn(null);
		EasyMock.replay(database);

		assertThrows(IllegalStateException.class, () -> {
			sistema.updateRecorrido(id, newDate, newTime);
		});

		EasyMock.verify(database);
	}

	@Test
	void testUpdateRecorridoConDateNull() {
		database.addRecorrido(recorrido);
		EasyMock.expectLastCall();
		EasyMock.expect(database.getRecorrido(id)).andReturn(recorrido);
		EasyMock.replay(database);

		sistema.addRecorrido(recorrido);
		assertThrows(IllegalArgumentException.class, () -> {
			sistema.updateRecorrido(id, null, newTime);
		});
		EasyMock.verify(database);
	}

	@Test
	void testUpdateRecorridoConTimeNull() {
		database.addRecorrido(recorrido);
		EasyMock.expectLastCall();
		EasyMock.expect(database.getRecorrido(id)).andReturn(recorrido);
		EasyMock.replay(database);

		sistema.addRecorrido(recorrido);
		assertThrows(IllegalArgumentException.class, () -> {
			sistema.updateRecorrido(id, newDate, null);
		});

		EasyMock.verify(database);
	}

	@Test
	void testUpdateRecorridoConDateYTimeAnterior() {
		database.addRecorrido(recorrido);
		EasyMock.expectLastCall();
		EasyMock.expect(database.getRecorrido(id)).andReturn(recorrido);
		EasyMock.replay(database);

		sistema.addRecorrido(recorrido);
		assertThrows(IllegalStateException.class, () -> {
			sistema.updateRecorrido(id, recorrido.getDate(), recorrido.getTime());
		});

		EasyMock.verify(database);
	}

	/**
	 * FINDME Tests for
	 * {@link SistemaPersistencia#comprarBilletes(String, Usuario, Recorrido, int)}
	 */
	@Test
	void testComprarBilletesValidoBusLimiteInferior() {
		String localizador = "ABC12345";

		EasyMock.expect(database.getBilletes(localizador)).andReturn(new ArrayList<>());

		database.addBillete(new Billete("ABC12345", recorrido, user, ESTADO_COMPRADO));
		EasyMock.expectLastCall();

		Recorrido clonRecorrido = recorrido.clone();
		clonRecorrido.decreaseAvailableSeats(1);

		database.actualizarRecorrido(clonRecorrido);
		EasyMock.expectLastCall();

		EasyMock.expect(database.getUsuario(user.getNif())).andReturn(null);
		database.addUsuario(user);
		EasyMock.expectLastCall();
		EasyMock.replay(database);

		List<Billete> listaBilletes = sistema.comprarBilletes(localizador, user, recorrido, 1);
		List<Billete> listaBilletesComprobacion = new ArrayList<>();
		Billete billeteComprobacion = new Billete(localizador, recorrido, user, ESTADO_COMPRADO);
		listaBilletesComprobacion.add(billeteComprobacion);
		assertEquals(listaBilletes, listaBilletesComprobacion);

		EasyMock.verify(database);
	}

	@Test
	void testComprarBilletesValidosBusLimiteSuperior() {
		String localizador = "ABC12345";

		EasyMock.expect(database.getBilletes(localizador)).andReturn(new ArrayList<>());

		database.addBillete(new Billete("ABC12345", recorrido, user, ESTADO_COMPRADO));
		EasyMock.expectLastCall().times(50);

		Recorrido clonRecorrido = recorrido.clone();
		clonRecorrido.decreaseAvailableSeats(50);

		database.actualizarRecorrido(clonRecorrido);
		EasyMock.expectLastCall();

		EasyMock.expect(database.getUsuario(user.getNif())).andReturn(null);
		database.addUsuario(user);
		EasyMock.expectLastCall();
		EasyMock.replay(database);

		List<Billete> listaBilletes = sistema.comprarBilletes(localizador, user, recorrido, 50);
		List<Billete> listaBilletesComprobacion = new ArrayList<>();
		for (int i = 0; i < 50; i++) {
			Billete billeteComprobacion = new Billete(localizador, recorrido, user, ESTADO_COMPRADO);
			listaBilletesComprobacion.add(billeteComprobacion);
		}
		assertEquals(listaBilletes, listaBilletesComprobacion);

		EasyMock.verify(database);

	}

	@Test
	void testComprarBilletesValidoTrenLimiteInferior() {
		String localizador = "ABC12345";

		EasyMock.expect(database.getBilletes(localizador)).andReturn(new ArrayList<>());

		database.addBillete(new Billete(localizador, differentRecorrido, user, ESTADO_COMPRADO));
		EasyMock.expectLastCall();

		Recorrido clonRecorrido = new Recorrido("dif", origin, destination, TRAIN, price, date, time, numSeats,
				duration);
		clonRecorrido.decreaseAvailableSeats(1);

		database.actualizarRecorrido(clonRecorrido);
		EasyMock.expectLastCall();

		EasyMock.expect(database.getUsuario(user.getNif())).andReturn(null);
		database.addUsuario(user);
		EasyMock.expectLastCall();
		EasyMock.replay(database);

		List<Billete> listaBilletes = sistema.comprarBilletes(localizador, user, differentRecorrido, 1);
		List<Billete> listaBilletesComprobacion = new ArrayList<>();
		Billete billeteComprobacion = new Billete(localizador, differentRecorrido, user, ESTADO_COMPRADO);
		listaBilletesComprobacion.add(billeteComprobacion);

		assertEquals(listaBilletes, listaBilletesComprobacion);

		EasyMock.verify(database);
	}

	@Test
	void testComprarBilletesValidosTrenLimiteSuperior() {
		String localizador = "ABC12345";

		EasyMock.expect(database.getBilletes(localizador)).andReturn(new ArrayList<>());

		Recorrido rec = new Recorrido("dif", origin, destination, TRAIN, price, date, time, 250, duration);
		database.addBillete(new Billete(localizador, rec, user, ESTADO_COMPRADO));
		EasyMock.expectLastCall().times(250);

		Recorrido clonRecorrido = new Recorrido("dif", origin, destination, TRAIN, price, date, time, 250, duration);
		clonRecorrido.decreaseAvailableSeats(250);

		database.actualizarRecorrido(clonRecorrido);
		EasyMock.expectLastCall();

		EasyMock.expect(database.getUsuario(user.getNif())).andReturn(null);
		database.addUsuario(user);
		EasyMock.expectLastCall();
		EasyMock.replay(database);
		List<Billete> listaBilletes = sistema.comprarBilletes(localizador, user, rec, 250);
		List<Billete> listaBilletesComprobacion = new ArrayList<>();
		for (int i = 0; i < 250; i++) {
			Billete billeteComprobacion = new Billete(localizador, rec, user, ESTADO_COMPRADO);
			listaBilletesComprobacion.add(billeteComprobacion);
		}
		assertEquals(listaBilletes, listaBilletesComprobacion);

		EasyMock.verify(database);
	}

	@Test
	void testComprarBilleteBusInvalidoLimiteInferior() {
		assertThrows(IllegalArgumentException.class, () -> {
			sistema.comprarBilletes("ABC12345", user, recorrido, 0);
		});
	}

	@Test
	void testComprarBilleteTrainInvalidoLimiteInferior() {
		assertThrows(IllegalArgumentException.class, () -> {
			sistema.comprarBilletes("ABC12345", user, differentRecorrido, 0);
		});
	}

	@Test
	void testComprarBilleteBusInvalidoLimiteSuperior() {
		assertThrows(IllegalStateException.class, () -> {
			sistema.comprarBilletes("ABC12345", user, recorrido, 51);
		});
	}

	@Test
	void testComprarBilleteTrainInvalidoLimiteSuperior() {
		assertThrows(IllegalStateException.class, () -> {
			sistema.comprarBilletes("ABC12345", user, differentRecorrido, 251);
		});
	}

	@Test
	void testComprarBilleteBusInvalidoLimiteSuperiorDemasiadaCompras() {
		EasyMock.expect(database.getBilletes("ABC12345")).andReturn(new ArrayList<>());
		database.addBillete(new Billete("ABC12345", recorrido, user, ESTADO_COMPRADO));
		EasyMock.expectLastCall().times(49);

		Recorrido clonRecorrido = recorrido.clone();
		clonRecorrido.decreaseAvailableSeats(49);

		database.actualizarRecorrido(clonRecorrido);
		EasyMock.expectLastCall();

		EasyMock.expect(database.getUsuario(user.getNif())).andReturn(null);
		database.addUsuario(user);
		EasyMock.expectLastCall();
		EasyMock.replay(database);

		sistema.comprarBilletes("ABC12345", user, recorrido, 49);
		assertThrows(IllegalStateException.class, () -> {
			sistema.comprarBilletes("ABC12345", differentUser, recorrido, 2);
		});
		EasyMock.verify(database);
	}

	@Test
	void testComprarBilleteTrainInvalidoLimiteSuperiorDemasiadaCompras() {
		EasyMock.expect(database.getBilletes("ABC12345")).andReturn(new ArrayList<>());
		Recorrido rec = new Recorrido("dif", origin, destination, TRAIN, price, date, time, 250, duration);
		database.addBillete(new Billete("ABC12345", rec, user, ESTADO_COMPRADO));
		EasyMock.expectLastCall().times(249);

		Recorrido clonRecorrido = new Recorrido("dif", origin, destination, TRAIN, price, date, time, 250, duration);
		clonRecorrido.decreaseAvailableSeats(249);

		database.actualizarRecorrido(clonRecorrido);
		EasyMock.expectLastCall();

		EasyMock.expect(database.getUsuario(user.getNif())).andReturn(null);
		database.addUsuario(user);
		EasyMock.expectLastCall();
		EasyMock.replay(database);
		sistema.comprarBilletes("ABC12345", user, rec, 249);
		assertThrows(IllegalStateException.class, () -> {
			sistema.comprarBilletes("ABC12345", differentUser, rec, 2);
		});
		EasyMock.verify(database);
	}

	@Test
	void testComprarBilleteLocalizadorInvalido() {
		assertThrows(IllegalArgumentException.class, () -> {
			sistema.comprarBilletes("", user, differentRecorrido, 1);
		});
	}

	@Test
	void testComprarBilleteLocalizadorNull() {
		assertThrows(IllegalArgumentException.class, () -> {
			sistema.comprarBilletes(null, user, differentRecorrido, 1);
		});
	}

	@Test
	void testComprarBilleteLocalizadorYaUsadoMismoRecorrido() {
		Recorrido rec = new Recorrido("dif", origin, destination, TRAIN, price, date, time, 250, duration);
		Billete tiket = new Billete("ABC12345", rec, user, ESTADO_COMPRADO);
		ArrayList<Billete> returned = new ArrayList<>();
		returned.add(tiket);
		EasyMock.expect(database.getBilletes("ABC12345")).andReturn(new ArrayList<>());
		EasyMock.expect(database.getBilletes("ABC12345")).andReturn(returned);
		database.addBillete(tiket);
		EasyMock.expectLastCall().times(2);

		Recorrido clonRecorrido = rec.clone();
		clonRecorrido.decreaseAvailableSeats(2);

		database.actualizarRecorrido(clonRecorrido);
		EasyMock.expectLastCall();

		EasyMock.expect(database.getUsuario(user.getNif())).andReturn(null);
		database.addUsuario(user);
		EasyMock.expectLastCall();
		EasyMock.replay(database);
		sistema.comprarBilletes("ABC12345", user, rec, 2);
		assertThrows(IllegalArgumentException.class, () -> {
			sistema.comprarBilletes("ABC12345", differentUser, differentRecorrido, 1);
		});
		EasyMock.verify(database);
	}

	@Test
	void testComprarBilleteLocalizadorYaUsadoDistinoRecorridoRecorrido() {
		Recorrido rec = new Recorrido("dif", origin, destination, TRAIN, price, date, time, 250, duration);
		Billete tiket = new Billete("ABC12345", rec, user, ESTADO_COMPRADO);
		ArrayList<Billete> returned = new ArrayList<>();
		returned.add(tiket);
		EasyMock.expect(database.getBilletes("ABC12345")).andReturn(new ArrayList<>());
		EasyMock.expect(database.getBilletes("ABC12345")).andReturn(returned);
		database.addBillete(tiket);
		EasyMock.expectLastCall().times(2);

		Recorrido clonRecorrido = rec.clone();
		clonRecorrido.decreaseAvailableSeats(2);

		database.actualizarRecorrido(clonRecorrido);
		EasyMock.expectLastCall();

		EasyMock.expect(database.getUsuario(user.getNif())).andReturn(null);
		database.addUsuario(user);
		EasyMock.expectLastCall();
		EasyMock.replay(database);
		
		sistema.comprarBilletes("ABC12345", user, rec, 2);
		assertThrows(IllegalArgumentException.class, () -> {
			sistema.comprarBilletes("ABC12345", differentUser, recorrido, 1);
		});
		EasyMock.verify(database);
	}

	@Test
	void testComprarBilleteUsuarioNull() {
		assertThrows(IllegalArgumentException.class, () -> {
			sistema.comprarBilletes("ABC12345", null, differentRecorrido, 1);
		});
	}

	@Test
	void testComprarBilleteRecorridoNull() {
		assertThrows(IllegalArgumentException.class, () -> {
			sistema.comprarBilletes("ABC12345", user, null, 1);
		});
	}

	@Tag("Cobertura")
	@Test
	void testComprarBilleteUsuarioYaGuardado() {
		String localizador = "ABC12345";

		EasyMock.expect(database.getBilletes("ABC12346")).andReturn(new ArrayList<>());
		EasyMock.expect(database.getBilletes(localizador)).andReturn(new ArrayList<>());

		database.addBillete(new Billete("ABC12346", recorrido, user, ESTADO_COMPRADO));
		database.addBillete(new Billete(localizador, differentRecorrido, user, ESTADO_COMPRADO));
		EasyMock.expectLastCall();

		Recorrido clonRecorridoPrimero = recorrido.clone();
		Recorrido clonRecorrido = differentRecorrido.clone();
		clonRecorridoPrimero.decreaseAvailableSeats(1);
		clonRecorrido.decreaseAvailableSeats(1);

		database.actualizarRecorrido(clonRecorridoPrimero);
		EasyMock.expectLastCall();
		database.actualizarRecorrido(clonRecorrido);
		EasyMock.expectLastCall();

		EasyMock.expect(database.getUsuario(user.getNif())).andReturn(null);
		EasyMock.expect(database.getUsuario(user.getNif())).andReturn(user);
		database.addUsuario(user);
		EasyMock.expectLastCall();
		EasyMock.replay(database);

		sistema.comprarBilletes("ABC12346", user, recorrido, 1);
		List<Billete> listaBilletes = sistema.comprarBilletes(localizador, user, differentRecorrido, 1);
		List<Billete> listaBilletesComprobacion = new ArrayList<>();
		Billete billeteComprobacion = new Billete(localizador, differentRecorrido, user, ESTADO_COMPRADO);
		listaBilletesComprobacion.add(billeteComprobacion);

		assertEquals(listaBilletes, listaBilletesComprobacion);

		EasyMock.verify(database);
	}

	/**
	 * FINDME Tests for
	 * {@link SistemaPersistencia#comprarBilletesReservados(String)}
	 */
	@Test
	void testComprarBilletesReservadosValidoLimiteInferior() {
		String locator = "1";
		database.addRecorrido(recorrido);
		EasyMock.expectLastCall();
		EasyMock.expect(database.getBilletes(locator)).andReturn(new ArrayList<>()).times(1);
		database.addBillete(new Billete(locator, recorrido, user, ESTADO_RESERVADO));
		EasyMock.expectLastCall().times(3);
		Recorrido routeMD = recorrido.clone();
		routeMD.decreaseAvailableSeats(3);
		database.actualizarRecorrido(routeMD);
		EasyMock.expectLastCall().times(1);
		ArrayList<Billete> bookedTickets = new ArrayList<>();
		bookedTickets.add(new Billete(locator, routeMD, user, ESTADO_RESERVADO));
		bookedTickets.add(new Billete(locator, routeMD, user, ESTADO_RESERVADO));
		bookedTickets.add(new Billete(locator, routeMD, user, ESTADO_RESERVADO));
		EasyMock.expect(database.getBilletes(locator)).andReturn(bookedTickets);
		Billete bookedTicket = new Billete(locator, routeMD, user, ESTADO_RESERVADO);
		bookedTicket.setComprado();
		database.actualizarBilletes(bookedTicket);
		EasyMock.expectLastCall();
		EasyMock.replay(database);

		sistema.addRecorrido(recorrido);
		sistema.reservarBilletes(locator, user, recorrido, 3);

		List<Billete> purchasedTicketsCheck = new ArrayList<>();
		purchasedTicketsCheck.add(new Billete(locator, recorrido, user, ESTADO_COMPRADO));
		purchasedTicketsCheck.add(new Billete(locator, recorrido, user, ESTADO_COMPRADO));
		purchasedTicketsCheck.add(new Billete(locator, recorrido, user, ESTADO_COMPRADO));
		List<Billete> purchasedTickets = sistema.comprarBilletesReservados(locator);
		assertEquals(purchasedTicketsCheck, purchasedTickets);

		EasyMock.verify(database);
	}

	@Test
	void testComprarBilletesReservadosValidoLimiteSuperior() {
		String locator = "12345678";
		database.addRecorrido(recorrido);
		EasyMock.expectLastCall();
		EasyMock.expect(database.getBilletes(locator)).andReturn(new ArrayList<>()).times(1);
		database.addBillete(new Billete(locator, recorrido, user, ESTADO_RESERVADO));
		EasyMock.expectLastCall().times(3);
		Recorrido routeMD = recorrido.clone();
		routeMD.decreaseAvailableSeats(3);
		database.actualizarRecorrido(routeMD);
		EasyMock.expectLastCall().times(1);
		ArrayList<Billete> bookedTickets = new ArrayList<>();
		bookedTickets.add(new Billete(locator, routeMD, user, ESTADO_RESERVADO));
		bookedTickets.add(new Billete(locator, routeMD, user, ESTADO_RESERVADO));
		bookedTickets.add(new Billete(locator, routeMD, user, ESTADO_RESERVADO));
		EasyMock.expect(database.getBilletes(locator)).andReturn(bookedTickets);
		Billete bookedTicket = new Billete(locator, routeMD, user, ESTADO_RESERVADO);
		bookedTicket.setComprado();
		database.actualizarBilletes(bookedTicket);
		EasyMock.expectLastCall();
		EasyMock.replay(database);

		sistema.addRecorrido(recorrido);
		sistema.reservarBilletes(locator, user, recorrido, 3);

		List<Billete> buyedTicketsCheck = new ArrayList<>();
		buyedTicketsCheck.add(new Billete(locator, recorrido, user, ESTADO_COMPRADO));
		buyedTicketsCheck.add(new Billete(locator, recorrido, user, ESTADO_COMPRADO));
		buyedTicketsCheck.add(new Billete(locator, recorrido, user, ESTADO_COMPRADO));
		List<Billete> buyedTickets = sistema.comprarBilletesReservados(locator);
		assertEquals(buyedTicketsCheck, buyedTickets);

		EasyMock.verify(database);
	}

	@Test
	void testComprarBilletesReservadosConLocatorNull() {
		assertThrows(IllegalArgumentException.class, () -> {
			sistema.comprarBilletesReservados(null);
		});
	}

	@Test
	void testComprarBilletesReservadosLimiteInferior() {
		assertThrows(IllegalArgumentException.class, () -> {
			sistema.comprarBilletesReservados("");
		});
	}

	@Test
	void testComprarBilletesReservadosLimiteSuperior() {
		assertThrows(IllegalArgumentException.class, () -> {
			sistema.comprarBilletesReservados("123456789");
		});
	}

	@Test
	void testComprarBilletesReservadosSinBilletesReservadosEnSystem() {
		String locator = "12345678";
		database.addRecorrido(recorrido);
		EasyMock.expectLastCall();
		EasyMock.expect(database.getBilletes(locator)).andReturn(new ArrayList<>());
		EasyMock.replay(database);

		sistema.addRecorrido(recorrido);
		assertThrows(IllegalStateException.class, () -> {
			sistema.comprarBilletesReservados(locator);
		});

		EasyMock.verify(database);
	}

	@Test
	void testComprarBilletesReservadosConLocalizadorDeBilletesComprados() {
		String locator = "12345678";
		// addRecorrido
		database.addRecorrido(recorrido);
		// reservarBillete
		EasyMock.expectLastCall();
		EasyMock.expect(database.getBilletes(locator)).andReturn(new ArrayList<>()).times(1);
		database.addBillete(new Billete(locator, recorrido, user, ESTADO_RESERVADO));
		EasyMock.expectLastCall().times(2);
		Recorrido routeMD = recorrido.clone();
		routeMD.decreaseAvailableSeats(2);
		database.actualizarRecorrido(routeMD);
		EasyMock.expectLastCall().times(1);
		// comprarBilletesReservados
		ArrayList<Billete> returned = new ArrayList<>();
		for (int i = 0; i < 2; i++)
			returned.add(new Billete(locator, routeMD, user, ESTADO_COMPRADO));
		EasyMock.expect(database.getBilletes(locator)).andReturn(returned);
		EasyMock.replay(database);

		sistema.addRecorrido(recorrido);
		sistema.reservarBilletes(locator, user, recorrido, 2);
		assertThrows(IllegalStateException.class, () -> {
			sistema.comprarBilletesReservados(locator);
		});

		EasyMock.verify(database);
	}

	/**
	 * FINDME Tests for
	 * {@link SistemaPersistencia#reservarBilletes(String, Usuario, Recorrido, int)}
	 */
	@Test
	void testReservarBilletesExitosamente() {
		String localizador = "ABC12345";
		int plazasDisponibles = recorrido.getNumAvailableSeats();
		int numBilletesReservar = 6;

		EasyMock.expect(database.getBilletes("ABC12345")).andReturn(new ArrayList<>()).times(1);
		database.addBillete(new Billete(localizador, recorrido, user, ESTADO_RESERVADO));
		EasyMock.expectLastCall().times(numBilletesReservar);

		Recorrido recorridoCopia = recorrido.clone();
		recorridoCopia.decreaseAvailableSeats(numBilletesReservar);
		database.actualizarRecorrido(recorridoCopia);
		EasyMock.expectLastCall();

		EasyMock.replay(database);
		// Realiza la reserva de los billetes
		List<Billete> reservado = sistema.reservarBilletes("ABC12345", user, recorrido, numBilletesReservar);

		assertEquals(plazasDisponibles - numBilletesReservar, recorrido.getNumAvailableSeats());

		ArrayList<Billete> listaBilletesComprobacion = new ArrayList<>();
		for (int i = 0; i < numBilletesReservar; i++) {
			Billete billeteComprobacion = new Billete("ABC12345", recorrido, user, ESTADO_RESERVADO);
			listaBilletesComprobacion.add(billeteComprobacion);
		}
		assertEquals(reservado, listaBilletesComprobacion);

		EasyMock.verify(database);
	}

	@Test
	void testReservarBilleteLocalizadorInvalido() {
		assertThrows(IllegalArgumentException.class, () -> {
			sistema.reservarBilletes("", user, recorrido, 1);
		});
	}

	@Test
	void testReservarBilleteLocalizadorNull() {
		assertThrows(IllegalArgumentException.class, () -> {
			sistema.reservarBilletes(null, user, recorrido, 1);
		});
	}

	@Test
	void testReservarBilleteUsuarioNull() {
		assertThrows(IllegalArgumentException.class, () -> {
			sistema.reservarBilletes("ABC12345", null, recorrido, 1);
		});
	}

	@Test
	void testReservarBilleteRecorridoNull() {
		assertThrows(IllegalArgumentException.class, () -> {
			sistema.reservarBilletes("ABC12345", user, null, 1);
		});
	}

	@Test
	void testNoSePuedeReservarSiPlazasInsuficientes() {
		int plazasDisponibles = recorrido.getNumAvailableSeats();
		int numBilletesReservar = plazasDisponibles + 1;

		assertThrows(IllegalStateException.class, () -> {
			sistema.reservarBilletes("ABC12345", user, recorrido, numBilletesReservar);
		});

	}

	@Test
	void testNoSePuedeReservarSiQuedanPlazasMenoresQueLaMitad() {
		String localizador = "ABC12345";
		String localizador2 = "ABC12346";
		int numBilletesComprar = (recorrido.getTotalSeats() / 2) + 1;

		EasyMock.expect(database.getBilletes(localizador)).andReturn(new ArrayList<>());

		database.addBillete(new Billete("ABC12345", recorrido, user, ESTADO_COMPRADO));
		EasyMock.expectLastCall().times(numBilletesComprar);

		Recorrido clonRecorrido = recorrido.clone();
		clonRecorrido.decreaseAvailableSeats(numBilletesComprar);

		database.actualizarRecorrido(clonRecorrido);
		EasyMock.expectLastCall();

		EasyMock.expect(database.getUsuario(user.getNif())).andReturn(null);
		database.addUsuario(user);
		EasyMock.expectLastCall();

		EasyMock.replay(database);

		sistema.comprarBilletes("ABC12345", user, recorrido, numBilletesComprar);
		assertThrows(IllegalStateException.class, () -> {
			sistema.reservarBilletes(localizador2, user, recorrido, 5);
		});
		EasyMock.verify(database);

	}

	/**
	 * FINDME Tests for {@link SistemaPersistencia#anularReserva(String, int)}
	 */
	@Test
	void testAnularReservaAumentaPlazasDisponiblesLimiteInferior() {
		int numBilletesReservar = 6;
		int numBilletesAnular = 1;
		String localizador = "ABC12345";

		EasyMock.expect(database.getBilletes(localizador)).andReturn(new ArrayList<>()).times(1);
		database.addBillete(new Billete(localizador, recorrido, user, ESTADO_RESERVADO));
		EasyMock.expectLastCall().times(numBilletesReservar);

		Recorrido recorridoCopia = recorrido.clone();
		recorridoCopia.decreaseAvailableSeats(numBilletesReservar);
		database.actualizarRecorrido(recorridoCopia);
		EasyMock.expectLastCall();

		ArrayList<Billete> returned = new ArrayList<Billete>();
		for (int i = 0; i < numBilletesReservar; i++) {
			returned.add(new Billete(localizador, recorrido, user, ESTADO_RESERVADO));
		}
		EasyMock.expect(database.getBilletes(localizador)).andReturn(returned).times(1);
		database.eliminarBilletes(localizador);
		EasyMock.expectLastCall();
		database.addBillete(new Billete(localizador, recorridoCopia, user, ESTADO_RESERVADO));
		EasyMock.expectLastCall().times(numBilletesReservar - numBilletesAnular);
		Recorrido recorridoCopiaCopia = recorridoCopia.clone();
		recorridoCopiaCopia.increaseAvailableSeats(numBilletesAnular);
		database.actualizarRecorrido(recorridoCopiaCopia);
		EasyMock.expectLastCall();

		EasyMock.replay(database);

		// Realiza la reserva de los billetes
		sistema.reservarBilletes(localizador, user, recorrido, numBilletesReservar);
		int plazasDisponiblesAntes = recorrido.getNumAvailableSeats();

		// Realiza la anulación de la reserva
		sistema.anularReserva(localizador, numBilletesAnular);

		int plazasDisponiblesDespues = recorrido.getNumAvailableSeats();

		// Verifica que las plazas disponibles aumenten en la cantidad correcta
		assertEquals(plazasDisponiblesAntes + numBilletesAnular, plazasDisponiblesDespues);

		EasyMock.verify(database);
	}

	@Test
	void testNoSePuedeAnularReservaSiLocalizadorNull() {
		assertThrows(IllegalArgumentException.class, () -> {
			sistema.anularReserva(null, 1);
		});
	}

	@Test
	void testNoSePuedeAnularReservaSiLocalizadorVacío() {
		assertThrows(IllegalArgumentException.class, () -> {
			sistema.anularReserva("", 1);
		});
	}

	@Test
	void testNoSePuedeAnularReservaSiFueraSistema() {
		String localizador = "ABC12345";
		int numBilletesAnular = 2;

		EasyMock.expect(database.getBilletes(localizador)).andReturn(new ArrayList<>());
		EasyMock.expectLastCall();

		EasyMock.replay(database);
		// Intenta anular la reserva sin reservar previamente
		assertThrows(IllegalStateException.class, () -> {
			sistema.anularReserva("ABC12345", numBilletesAnular);
		});
		EasyMock.verify(database);
	}

	@Test
	void testNoSePuedeAnularReservaSiLocalizadorLimiteInferior() {
		assertThrows(IllegalArgumentException.class, () -> {
			sistema.anularReserva("ABCD12345", 0);
		});
	}

	@Test
	void testNoSePuedeAnularReservaSiLocalizadorPerteneceABilletesComprados() {
		String localizador = "ABC12345";
		int numBilletesComprar = 5;
		int numBilletesAnular = 2;

		EasyMock.expect(database.getBilletes(localizador)).andReturn(new ArrayList<>());
		database.addBillete(new Billete(localizador, recorrido, user, ESTADO_COMPRADO));
		EasyMock.expectLastCall().times(numBilletesComprar);

		Recorrido clonRecorrido = recorrido.clone();
		clonRecorrido.decreaseAvailableSeats(numBilletesComprar);

		database.actualizarRecorrido(clonRecorrido);
		EasyMock.expectLastCall();

		EasyMock.expect(database.getUsuario(user.getNif())).andReturn(null);
		database.addUsuario(user);
		EasyMock.expectLastCall();
		
		ArrayList<Billete> returned = new ArrayList<Billete>();
		for (int i = 0; i < numBilletesComprar; i++) {
			returned.add(new Billete(localizador, clonRecorrido, user, ESTADO_COMPRADO));
		}
		EasyMock.expect(database.getBilletes(localizador)).andReturn(returned);
		EasyMock.expectLastCall();

		EasyMock.replay(database);

		sistema.comprarBilletes(localizador, user, recorrido, numBilletesComprar);
		assertThrows(IllegalStateException.class, () -> {
			sistema.anularReserva(localizador, numBilletesAnular);
		});

		EasyMock.verify(database);
	}

	@Test
	void testNoSePuedeAnularReservaSiMayorNumeroDeAnulacionesQueBilletesReservados() {
		String localizador = "ABC12345";
		int numBilletesReservar = 2;
		int numBilletesAnular = 3;

		EasyMock.expect(database.getBilletes("ABC12345")).andReturn(new ArrayList<>()).times(1);
		database.addBillete(new Billete(localizador, recorrido, user, ESTADO_RESERVADO));
		EasyMock.expectLastCall().times(numBilletesReservar);

		Recorrido recorridoCopia = recorrido.clone();
		recorridoCopia.decreaseAvailableSeats(numBilletesReservar);
		database.actualizarRecorrido(recorridoCopia);
		EasyMock.expectLastCall();

		ArrayList<Billete> returned = new ArrayList<Billete>();
		for (int i = 0; i < numBilletesReservar; i++) {
			returned.add(new Billete(localizador, recorridoCopia, user, ESTADO_RESERVADO));
		}

		EasyMock.expect(database.getBilletes("ABC12345")).andReturn(returned).times(1);
		EasyMock.expectLastCall();

		EasyMock.replay(database);
		sistema.reservarBilletes(localizador, user, recorrido, numBilletesReservar);
		assertThrows(IllegalStateException.class, () -> {
			sistema.anularReserva(localizador, numBilletesAnular);
		});
		EasyMock.verify(database);
	}

	/**
	 * FINDME Tests for {@link SistemaPersistencia#devolverBilletes(String, int)}
	 */
	@Test
	void testDevolverBilletesAumentaPlazasDisponiblesLimiteInferior() {
		int numBilletesComprar = 6;
		int numBilletesDevolver = 1;
		String localizador = "ABC12345";

		EasyMock.expect(database.getBilletes("ABC12345")).andReturn(new ArrayList<>()).times(1);
		database.addBillete(new Billete(localizador, recorrido, user, ESTADO_COMPRADO));
		EasyMock.expectLastCall().times(numBilletesComprar);

		Recorrido recorridoCopia = recorrido.clone();
		recorridoCopia.decreaseAvailableSeats(numBilletesComprar);
		database.actualizarRecorrido(recorridoCopia);
		EasyMock.expectLastCall();
		EasyMock.expect(database.getUsuario(user.getNif())).andReturn(null);
		database.addUsuario(user);
		EasyMock.expectLastCall();

		ArrayList<Billete> returned = new ArrayList<Billete>();
		for (int i = 0; i < numBilletesComprar; i++) {
			returned.add(new Billete(localizador, recorridoCopia, user, ESTADO_COMPRADO));
		}
		EasyMock.expect(database.getBilletes(localizador)).andReturn(returned).times(1);
		database.eliminarBilletes(localizador);
		EasyMock.expectLastCall();
		database.addBillete(new Billete(localizador, recorridoCopia, user, ESTADO_COMPRADO));
		EasyMock.expectLastCall().times(numBilletesComprar - numBilletesDevolver);
		Recorrido recorridoCopiaCopia = recorridoCopia.clone();
		recorridoCopiaCopia.increaseAvailableSeats(numBilletesDevolver);
		database.actualizarRecorrido(recorridoCopiaCopia);
		EasyMock.expect(database.getRecorrido(recorrido.getID())).andReturn(recorridoCopiaCopia);
		EasyMock.expectLastCall();

		EasyMock.replay(database);

		// Realiza la reserva de los billetes
		sistema.comprarBilletes(localizador, user, recorrido, numBilletesComprar);
		int plazasDisponiblesAntes = recorrido.getNumAvailableSeats();

		// Realiza la anulación de la reserva
		sistema.devolverBilletes(localizador, numBilletesDevolver);

		// TODO Revisar
		int plazasDisponiblesDespues = database.getRecorrido(recorrido.getID()).getNumAvailableSeats();

		// Verifica que las plazas disponibles aumenten en la cantidad correcta
		assertEquals(plazasDisponiblesAntes + numBilletesDevolver, plazasDisponiblesDespues);

		EasyMock.verify(database);
	}

	@Test
	void testNoSePuedeDevolverBilletesSiLocalizadorNull() {
		assertThrows(IllegalArgumentException.class, () -> {
			sistema.devolverBilletes(null, 1);
		});
	}

	@Test
	void testNoSePuedeDevolverBilletesSiLocalizadorVacío() {
		assertThrows(IllegalArgumentException.class, () -> {
			sistema.devolverBilletes("", 1);
		});
	}

	@Test
	void testNoSePuedeDevolverBilletesSiLocalizadorLimiteInferior() {
		assertThrows(IllegalArgumentException.class, () -> {
			sistema.devolverBilletes("ABCD12345", 0);
		});
	}

	@Test
	void testNoSePuedeDevolverBilleteFueraSistema() {
		int numBilletesDevolver = 2;
		String localizador = "ABC12345";

		ArrayList<Billete> returned = new ArrayList<Billete>();
		EasyMock.expect(database.getBilletes(localizador)).andReturn(returned).times(1);
		EasyMock.expectLastCall();

		EasyMock.replay(database);

		// Intenta anular la compra sin comprar previamente
		assertThrows(IllegalStateException.class, () -> {
			sistema.devolverBilletes("ABC12345", numBilletesDevolver);
		});

		EasyMock.verify(database);

	}

	@Test
	void testNoSePuedeDevolverBilleteSiReservados() {
		int numBilletesReservar = 6;
		int numBilletesDevolver = 1;
		String localizador = "ABC12345";

		EasyMock.expect(database.getBilletes("ABC12345")).andReturn(new ArrayList<>());
		database.addBillete(new Billete(localizador, recorrido, user, ESTADO_RESERVADO));
		EasyMock.expectLastCall().times(numBilletesReservar);

		Recorrido recorridoCopia = recorrido.clone();
		recorridoCopia.decreaseAvailableSeats(numBilletesReservar);
		database.actualizarRecorrido(recorridoCopia);
		EasyMock.expectLastCall();

		ArrayList<Billete> returned = new ArrayList<Billete>();
		for (int i = 0; i < numBilletesReservar; i++) {
			returned.add(new Billete(localizador, recorridoCopia, user, ESTADO_RESERVADO));
		}
		EasyMock.expect(database.getBilletes(localizador)).andReturn(returned).times(1);

		EasyMock.expectLastCall();

		EasyMock.replay(database);

		sistema.reservarBilletes(localizador, user, recorrido, numBilletesReservar);

		assertThrows(IllegalStateException.class, () -> {
			sistema.devolverBilletes(localizador, numBilletesDevolver);
		});

		EasyMock.verify(database);

	}

	@Test
	void testNoSePuedeDevolverBilletesConNumLimiteInferior() {
		String locator = "ABC12345";
		int numBilletesComprar = 1;
		int numBilletesDevolver = 0;

		EasyMock.expect(database.getBilletes("ABC12345")).andReturn(new ArrayList<>()).times(1);
		database.addBillete(new Billete(locator, recorrido, user, ESTADO_COMPRADO));
		EasyMock.expectLastCall().times(numBilletesComprar);
		Recorrido clon = recorrido.clone();
		clon.decreaseAvailableSeats(numBilletesComprar);
		database.actualizarRecorrido(clon);
		EasyMock.expectLastCall();
		EasyMock.expect(database.getUsuario(user.getNif())).andReturn(null);
		database.addUsuario(user);
		EasyMock.expectLastCall();

		EasyMock.replay(database);

		sistema.comprarBilletes(locator, user, recorrido, 1);
		assertThrows(IllegalArgumentException.class, () -> {
			sistema.devolverBilletes(locator, numBilletesDevolver);
		});

		EasyMock.verify(database);
	}

	@Test
	void testNoSePuedeDevolverBilletesSiMayorNumeroDeDevolucionesQueBilletesComprados() {
		String locator = "ABC12345";
		int numBilletesDevolver = 3;
		int numBilletesComprar = 1;

		EasyMock.expect(database.getBilletes("ABC12345")).andReturn(new ArrayList<>()).times(1);
		database.addBillete(new Billete(locator, recorrido, user, ESTADO_COMPRADO));
		EasyMock.expectLastCall().times(numBilletesComprar);
		EasyMock.expect(database.getUsuario(user.getNif())).andReturn(null);
		database.addUsuario(user);
		EasyMock.expectLastCall();

		Recorrido recorridoCopia = recorrido.clone();
		recorridoCopia.decreaseAvailableSeats(numBilletesComprar);
		database.actualizarRecorrido(recorridoCopia);
		EasyMock.expectLastCall();

		ArrayList<Billete> returned = new ArrayList<Billete>();
		for (int i = 0; i < numBilletesComprar; i++) {
			returned.add(new Billete(locator, recorridoCopia, user, ESTADO_COMPRADO));
		}
		EasyMock.expect(database.getBilletes(locator)).andReturn(returned).times(1);

		EasyMock.expectLastCall();

		EasyMock.replay(database);
		sistema.comprarBilletes(locator, user, recorrido, 1);
		assertThrows(IllegalStateException.class, () -> {
			sistema.devolverBilletes(locator, numBilletesDevolver);
		});
		EasyMock.verify(database);
	}

	@Test
	@Tag("Cobertura")
	void testLocalizadorYaUtilizadoReserva() {
		String localizador = "ABC12345";
		int numBilletesReservar = 1;

		EasyMock.expect(database.getBilletes("ABC12345")).andReturn(new ArrayList<>());
		database.addBillete(new Billete(localizador, recorrido, user, ESTADO_RESERVADO));
		EasyMock.expectLastCall().times(numBilletesReservar);

		Recorrido recorridoCopia = recorrido.clone();
		recorridoCopia.decreaseAvailableSeats(numBilletesReservar);
		database.actualizarRecorrido(recorridoCopia);
		EasyMock.expectLastCall();

		ArrayList<Billete> returned = new ArrayList<Billete>();
		for (int i = 0; i < numBilletesReservar; i++) {
			returned.add(new Billete(localizador, recorridoCopia, user, ESTADO_RESERVADO));
		}
		EasyMock.expect(database.getBilletes(localizador)).andReturn(returned).times(1);

		EasyMock.expectLastCall();

		EasyMock.replay(database);

		sistema.reservarBilletes(localizador, user, recorrido, 1);

		assertThrows(IllegalStateException.class, () -> {
			sistema.reservarBilletes(localizador, user, recorrido, 1);
		});

		EasyMock.verify(database);
	}

	@Test
	@Tag("Cobertura")
	void testAnularReservaMismoNumeroDeAnulacionesQueBilletesReservados() {
		String localizador = "ABC12345";
		int numBilletesReservar = 3;
		int numBilletesAnular = 3;

		EasyMock.expect(database.getBilletes("ABC12345")).andReturn(new ArrayList<>()).times(1);
		database.addBillete(new Billete(localizador, recorrido, user, ESTADO_RESERVADO));
		EasyMock.expectLastCall().times(numBilletesReservar);

		Recorrido recorridoCopia = recorrido.clone();
		recorridoCopia.decreaseAvailableSeats(numBilletesReservar);
		database.actualizarRecorrido(recorridoCopia);
		EasyMock.expectLastCall();

		ArrayList<Billete> returned = new ArrayList<Billete>();
		for (int i = 0; i < numBilletesReservar; i++) {
			returned.add(new Billete(localizador, recorridoCopia, user, ESTADO_RESERVADO));
		}

		EasyMock.expect(database.getBilletes(localizador)).andReturn(returned).times(1);
		database.eliminarBilletes(localizador);
		EasyMock.expectLastCall();
		Recorrido recorridoCopiaCopia = recorridoCopia.clone();
		recorridoCopiaCopia.increaseAvailableSeats(numBilletesAnular);
		database.actualizarRecorrido(recorridoCopiaCopia);
		EasyMock.expectLastCall();

		EasyMock.replay(database);
		
		sistema.reservarBilletes(localizador, user, recorrido, numBilletesReservar);
		sistema.anularReserva(localizador, numBilletesAnular);
		
		EasyMock.verify(database);
	}
	
	@Test
	@Tag("Cobertura")
	void testDevolverBilletesIgualNumeroDeDevolucionesQueBilletesComprados() {
		String locator = "ABC12345";
		int numBilletesDevolver = 3;
		int numBilletesComprar = 3;

		EasyMock.expect(database.getBilletes("ABC12345")).andReturn(new ArrayList<>()).times(1);
		database.addBillete(new Billete(locator, recorrido, user, ESTADO_COMPRADO));
		EasyMock.expectLastCall().times(numBilletesComprar);
		EasyMock.expect(database.getUsuario(user.getNif())).andReturn(null);
		database.addUsuario(user);
		EasyMock.expectLastCall();

		Recorrido recorridoCopia = recorrido.clone();
		recorridoCopia.decreaseAvailableSeats(numBilletesComprar);
		database.actualizarRecorrido(recorridoCopia);
		EasyMock.expectLastCall();

		ArrayList<Billete> returned = new ArrayList<Billete>();
		for (int i = 0; i < numBilletesComprar; i++) {
			returned.add(new Billete(locator, recorridoCopia, user, ESTADO_COMPRADO));
		}
		EasyMock.expect(database.getBilletes(locator)).andReturn(returned).times(1);
		database.eliminarBilletes(locator);
		EasyMock.expectLastCall();
		Recorrido recorridoCopiaCopia = recorridoCopia.clone();
		recorridoCopiaCopia.increaseAvailableSeats(numBilletesDevolver);
		database.actualizarRecorrido(recorridoCopiaCopia);
		EasyMock.expectLastCall();
		
		EasyMock.replay(database);
		sistema.comprarBilletes(locator, user, recorrido, numBilletesComprar);
		sistema.devolverBilletes(locator, numBilletesDevolver);
		EasyMock.verify(database);
	}
}
