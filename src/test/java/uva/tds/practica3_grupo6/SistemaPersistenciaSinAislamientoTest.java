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

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * Class made for tests the methods of {@link SistemaPersistenciaSinAislamiento}
 * 
 * @author hugcubi
 * @author diebomb
 * @author migudel
 * 
 * @version 21/12/23
 */
class SistemaPersistenciaSinAislamientoTest {

	private static final double ERROR_MARGIN = 0.00001;
	private static final String ESTADO_RESERVADO = Billete.ESTADO_RESERVADO;
	private static final String ESTADO_COMPRADO = Billete.ESTADO_COMPRADO;

	private String nif;
	private String nombre;
	private Usuario user;
	private Usuario differentUser;
	private String id;
	private String idLI;
	private Connection connection;
	private double price;
	private LocalDateTime dateTime;
	private BusRecorrido recorrido;
	private BusRecorrido recorridoLI;
	private TrainRecorrido differentRecorrido;
	private int numSeats;
	private LocalDateTime newDateTime;
	private LocalDate newDate;
	private LocalTime newTime;

	private IDatabaseManager database;

	private SistemaPersistenciaSinAislamiento sistema;

	@BeforeEach
	void setUp() {
		nif = "32698478E";
		nombre = "Geronimo";
		user = new Usuario(nif, nombre);
		differentUser = new Usuario("79105889B", nombre);
		id = "c12345";
		connection = new Connection("Valladolid", "Palencia", 30);
		dateTime = LocalDateTime.of(2023, 10, 27, 19, 06, 50);
		price = 1.0;
		numSeats = 50;
		recorrido = new BusRecorrido(id, connection, price, dateTime, numSeats);
		differentRecorrido = new TrainRecorrido("train", connection, price, dateTime, numSeats);
		idLI = "i";
		recorridoLI = new BusRecorrido(idLI, connection, price, dateTime, numSeats);

		newDateTime = LocalDateTime.of(2023, 5, 14, 22, 56, 20);
		newDate = LocalDate.of(2024, 2, 4);
		newTime = LocalTime.of(12, 2, 4);

		database = new DatabaseManager(); 

		sistema = new SistemaPersistenciaSinAislamiento(database);
	}

	/**
	 * FINDME Tests for
	 * {@link SistemaPersistencia#SistemaPersistencia(IDatabaseManager)}
	 */
	@Test
	void testConstructor() {
		SistemaPersistenciaSinAislamiento sistema = new SistemaPersistenciaSinAislamiento(database);
		assertNotNull(sistema);
		assertEquals(database, sistema.getDataBaseManager());
	}

	/**
	 * FINDME Tests for {@link SistemaPersistenciaSinAislamiento#addRecorrido(Recorrido)}
	 */
	@Test
	void testAddRecorridoValido() {
		sistema.addRecorrido(recorrido);
		sistema.addRecorrido(recorridoLI);
		sistema.addRecorrido(differentRecorrido);
		List<Recorrido> recorridos = sistema.getRecorridos();
		List<Recorrido> recorridosCheck = new ArrayList<>();
		recorridosCheck.add(recorrido);
		recorridosCheck.add(recorridoLI);
		recorridosCheck.add(differentRecorrido);
		assertEquals(recorridosCheck, recorridos);
	}

	@Test
	void testAddRecorridoConRecorridoNull() {
		assertThrows(IllegalArgumentException.class, () -> {
			sistema.addRecorrido(null);
		});
	}

	@Test
	void testAddRecorridoConRecorridoYaEnSystem() {
		sistema.addRecorrido(recorrido);
		Recorrido recorridoCopia = new BusRecorrido(recorrido);
		assertEquals(recorridoCopia, recorrido);
		assertThrows(IllegalStateException.class, () -> {
			sistema.addRecorrido(recorridoCopia);
		});
	}

	/**
	 * FINDME Tests for {@link SistemaPersistencia#removeRecorrido(String))}
	 */
	@Test
	void testRemoveRecorridoValidoConIDLimiteInferior() {
		sistema.addRecorrido(recorridoLI);
		sistema.removeRecorrido(idLI);
		assertEquals(new ArrayList<>(), sistema.getRecorridos());
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
		assertThrows(IllegalStateException.class, () -> {
			sistema.removeRecorrido(id);
		});
	}

	@Test
	void testRemoveRecorridoConRecorridoEnSystemConBilletesAsociados() {
		String locator = "T12345";
		List<Billete> billetes = new ArrayList<>();
		Usuario usuario = new Usuario("32698478E", "Geronimo");
		sistema.addRecorrido(recorrido);
		sistema.comprarBilletes("T12345", usuario, recorrido, 1);
		Recorrido rec=sistema.getRecorrido(recorrido.getID());
		Billete ticket = new Billete(locator, rec, usuario, ESTADO_COMPRADO);
		billetes.add(ticket);
		assertEquals(billetes, sistema.getAssociatedBilletesToRoute(id));

		assertThrows(IllegalStateException.class, () -> {
			sistema.removeRecorrido(id);
		});
	}

	/**
	 * FINDME Tests for
	 * {@link SistemaPersistenciaSinAislamiento#getPrecioTotalBilletesUsuario(String)}
	 */
	@Tag("Cobertura")
	@Test
	void testGetPrecioTotalBilletesUsuarioBus() {
		String localizador = "T12345";
		int numBilletes = 5;
		sistema.addRecorrido(recorrido);
		sistema.comprarBilletes(localizador, user, recorrido, numBilletes);
		assertEquals(5.0, sistema.getPrecioTotalBilletesUsuario(user.getNif()), ERROR_MARGIN);
	}

	@Tag("Cobertura")
	@Test
	void testGetPrecioTotalBilletesUsuarioPrecioRecorridoTren() {
		String localizador = "T12345";
		int numBilletes = 5;
		sistema.addRecorrido(differentRecorrido);
		sistema.comprarBilletes(localizador, user, differentRecorrido, numBilletes);
		assertEquals(4.5, sistema.getPrecioTotalBilletesUsuario(user.getNif()), ERROR_MARGIN);
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
	 * {@link SistemaPersistenciaSinAislamiento#getRecorridosDisponiblesFecha(LocalDate)}
	 */
	@Test
	void testGetRecorridosDisponiblesFecha() {
		LocalDate date = dateTime.toLocalDate();
		ArrayList<Recorrido> recorridos = new ArrayList<>();
		recorridos.add(recorrido);
		recorridos.add(differentRecorrido);
		sistema.addRecorrido(differentRecorrido);
		sistema.addRecorrido(recorrido);
		assertEquals(recorridos, sistema.getRecorridosDisponiblesFecha(date));
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
		LocalDate date = dateTime.toLocalDate();
		assertThrows(IllegalStateException.class, () -> {
			sistema.getRecorridosDisponiblesFecha(date);
		});
	}

	/**
	 * FINDME Tests for {@link System#getAssociatedBilletesToRoute(String))}
	 */
	@Test
	void testGetAssociatedBilletesToRouteValidoConIDLimiteInferior() {
		String locator = "c";
		sistema.addRecorrido(recorrido);
		sistema.reservarBilletes(locator, user, recorrido, 1);
		List<Billete> expected = new ArrayList<>();
		
		Recorrido rec=sistema.getRecorrido(recorrido.getID());
		sistema.comprarBilletes(locator, differentUser, rec, 2);
		rec=sistema.getRecorrido(rec.getID());		
		expected.add(new Billete(locator, rec, user, ESTADO_RESERVADO));
		expected.add(new Billete(locator, rec, differentUser, ESTADO_COMPRADO));
		expected.add(new Billete(locator, rec, differentUser, ESTADO_COMPRADO));

		
		assertEquals(expected, sistema.getAssociatedBilletesToRoute(id));

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
		assertThrows(IllegalStateException.class, () -> {
			sistema.getAssociatedBilletesToRoute(id);
		});

	}

	/**
	 * FINDME Tests for {@link SistemaPersistenciaSinAislamiento#getDateOfRecorrido(String)}
	 */
	@Test
	void testGetDateOfRecorridoValidoConIDLimiteInferior() {
		sistema.addRecorrido(recorridoLI);
		assertEquals(recorridoLI.getDate(), sistema.getDateOfRecorrido(idLI));

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
		assertThrows(IllegalStateException.class, () -> {
			sistema.getDateOfRecorrido(id);
		});
	}

	/**
	 * FINDME Tests for {@link SistemaPersistenciaSinAislamiento#getTimeOfRecorrido(String)}
	 */
	@Test
	void testGetTimeOfRecorridoValidoConIDLimiteInferior() {
		sistema.addRecorrido(recorridoLI);
		assertEquals(recorridoLI.getTime(), sistema.getTimeOfRecorrido(idLI));
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
	 * FINDME Tests for {@link SistemaPersistenciaSinAislamiento#getDateTimeOfRecorrido(String)}
	 */
	@Test
	void testGetDateTimeOfRecorridoValidoConIDLimiteInferior() {
		sistema.addRecorrido(recorridoLI);
		assertEquals(recorridoLI.getDateTime(), sistema.getDateTimeOfRecorrido(idLI));
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
		assertThrows(IllegalStateException.class, () -> {
			sistema.getDateTimeOfRecorrido(id);
		});
	}

	/**
	 * FINDME Tests for
	 * {@link SistemaPersistenciaSinAislamiento#updateRecorridoDate(String, LocalDate)}
	 */
	@Test
	void testUpdateRecorridoDateValidoConIDLimiteInferior() {
		sistema.addRecorrido(recorridoLI);
		assertNotEquals(newDate, sistema.getDateOfRecorrido(recorridoLI.getID()));
		sistema.updateRecorridoDate(idLI, newDate);
		assertEquals(newDate, sistema.getDateOfRecorrido(recorridoLI.getID()));
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
		assertThrows(IllegalStateException.class, () -> {
			sistema.updateRecorridoDate(id, newDate);
		});
	}

	@Test
	void testUpdateRecorridoDateConDateNull() {
		sistema.addRecorrido(recorrido);
		assertThrows(IllegalArgumentException.class, () -> {
			sistema.updateRecorridoDate(id, null);
		});

		
	}

	@Test
	void testUpdateRecorridoDateConDateActual() {
		sistema.addRecorrido(recorrido);
		LocalDate date = recorrido.getDate();
		assertThrows(IllegalStateException.class, () -> {
			sistema.updateRecorridoDate(id, date);
		});

		
	}

	/**
	 * FINDME Tests for
	 * {@link SistemaPersistenciaSinAislamiento#updateRecorridoTime(String, LocalTime)}
	 */
	@Test
	void testUpdateRecorridoTimeValidoConIDLimiteInferior() {
		sistema.addRecorrido(recorridoLI);
		assertNotEquals(newTime, sistema.getTimeOfRecorrido(recorridoLI.getID()));
		sistema.updateRecorridoTime(idLI, newTime);
		assertEquals(newTime, sistema.getTimeOfRecorrido(recorridoLI.getID()));

		
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
	void testUpdateRecorridoTimeConRecorridoFueraDelSystem() {
		assertThrows(IllegalStateException.class, () -> {
			sistema.updateRecorridoTime(id, newTime);
		});

		
	}

	@Test
	void testUpdateRecorridoTimeConTimeNull() {
		sistema.addRecorrido(recorrido);
		assertThrows(IllegalArgumentException.class, () -> {
			sistema.updateRecorridoTime(id, null);
		});

		
	}

	@Test
	void testUpdateRecorridoTimeConTimeActual() {
		sistema.addRecorrido(recorrido);
		LocalTime time = recorrido.getTime();
		assertThrows(IllegalStateException.class, () -> {
			sistema.updateRecorridoTime(id, time);
		});

		
	}

	/**
	 * FINDME Tests for
	 * {@link SistemaPersistenciaSinAislamiento#updateRecorridoDateTime(String, LocalDateTime)}
	 */
	@Test
	void testUpdateRecorridoDateTimeValidoConIDLimiteInferior() {
		sistema.addRecorrido(recorridoLI);
		assertNotEquals(newDateTime, sistema.getDateTimeOfRecorrido(idLI));
		sistema.updateRecorridoDateTime(idLI, newDateTime);
		assertEquals(newDateTime, sistema.getDateTimeOfRecorrido(idLI));

		
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
		assertThrows(IllegalStateException.class, () -> {
			sistema.updateRecorridoDateTime(id, newDateTime);
		});

		
	}

	@Test
	void testUpdateRecorridoDateTimeConDateTimeNull() {
		sistema.addRecorrido(recorrido);
		assertThrows(IllegalArgumentException.class, () -> {
			sistema.updateRecorridoDateTime(id, null);
		});

		
	}

	@Test
	void testUpdateRecorridoDateTimeConDateTimeActual() {
		sistema.addRecorrido(recorrido);
		LocalDateTime dateTime = recorrido.getDateTime();
		assertThrows(IllegalStateException.class, () -> {
			sistema.updateRecorridoDateTime(id, dateTime);
		});

		
	}

	/**
	 * FINDME Tests for
	 * {@link SistemaPersistenciaSinAislamiento#updateRecorrido(String, LocalDate, LocalTime)}
	 */
	@Test
	void testUpdateRecorridoValidoConIDLimiteInferior() {
		sistema.addRecorrido(recorridoLI);
		assertNotEquals(newDateTime, sistema.getDateTimeOfRecorrido(idLI));
		sistema.updateRecorrido(idLI, newDateTime.toLocalDate(), newDateTime.toLocalTime());
		assertEquals(newDateTime, sistema.getDateTimeOfRecorrido(idLI));

		
	}

	@Test
	void testUpdateRecorridoConIDNull() {
		assertThrows(IllegalArgumentException.class, () -> {
			sistema.updateRecorrido(null, newDate, newTime);
		});
	}

	@Test
	void testUpdateRecorridoConRecorridoFueraDelSystem() {
		assertThrows(IllegalStateException.class, () -> {
			sistema.updateRecorrido(id, newDate, newTime);
		});

		
	}

	@Test
	void testUpdateRecorridoConDateNull() {
		sistema.addRecorrido(recorrido);
		assertThrows(IllegalArgumentException.class, () -> {
			sistema.updateRecorrido(id, null, newTime);
		});
		
	}

	@Test
	void testUpdateRecorridoConTimeNull() {
		sistema.addRecorrido(recorrido);
		assertThrows(IllegalArgumentException.class, () -> {
			sistema.updateRecorrido(id, newDate, null);
		});

		
	}

	@Test
	void testUpdateRecorridoConDateYTimeAnterior() {
		LocalTime time = recorrido.getTime();
		LocalDate date = recorrido.getDate();
		sistema.addRecorrido(recorrido);
		assertThrows(IllegalStateException.class, () -> {
			sistema.updateRecorrido(id, date, time);
		});

		
	}

	/**
	 * FINDME Tests for
	 * {@link SistemaPersistenciaSinAislamiento#comprarBilletes(String, Usuario, Recorrido, int)}
	 */
	@Test
	void testComprarBilletesValidoBusLimiteInferior() {
		String localizador = "ABC12345";
		sistema.addRecorrido(recorrido);
		List<Billete> listaBilletes = sistema.comprarBilletes(localizador, user, recorrido, 1);
		List<Billete> listaBilletesComprobacion = new ArrayList<>();
		Billete billeteComprobacion = new Billete(localizador, recorrido, user, ESTADO_COMPRADO);
		listaBilletesComprobacion.add(billeteComprobacion);
		assertEquals(listaBilletes, listaBilletesComprobacion);
	}

	@Test
	void testComprarBilletesValidosBusLimiteSuperior() {
		String localizador = "ABC12345";
		sistema.addRecorrido(recorrido);
		List<Billete> listaBilletes = sistema.comprarBilletes(localizador, user, recorrido, 50);
		List<Billete> listaBilletesComprobacion = new ArrayList<>();
		for (int i = 0; i < 50; i++) {
			Billete billeteComprobacion = new Billete(localizador, recorrido, user, ESTADO_COMPRADO);
			listaBilletesComprobacion.add(billeteComprobacion);
		}
		assertEquals(listaBilletes, listaBilletesComprobacion);
	}

	@Test
	void testComprarBilletesValidoTrenLimiteInferior() {
		String localizador = "ABC12345";
		sistema.addRecorrido(differentRecorrido);
		List<Billete> listaBilletes = sistema.comprarBilletes(localizador, user, differentRecorrido, 1);
		List<Billete> listaBilletesComprobacion = new ArrayList<>();
		Billete billeteComprobacion = new Billete(localizador, differentRecorrido, user, ESTADO_COMPRADO);
		listaBilletesComprobacion.add(billeteComprobacion);
		assertEquals(listaBilletes, listaBilletesComprobacion);

	}

	@Test
	void testComprarBilletesValidosTrenLimiteSuperior() {
		String localizador = "ABC12345";
		Recorrido rec = new TrainRecorrido("dif", connection, price, dateTime, 250);
		sistema.addRecorrido(rec);
		List<Billete> listaBilletes = sistema.comprarBilletes(localizador, user, rec, 250);
		List<Billete> listaBilletesComprobacion = new ArrayList<>();
		for (int i = 0; i < 250; i++) {
			Billete billeteComprobacion = new Billete(localizador, rec, user, ESTADO_COMPRADO);
			listaBilletesComprobacion.add(billeteComprobacion);
		}
		assertEquals(listaBilletes, listaBilletesComprobacion);
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
		assertThrows(IllegalArgumentException.class, () -> {
			sistema.comprarBilletes("ABC12345", user, recorrido, 51);
		});
	}

	@Test
	void testComprarBilleteTrainInvalidoLimiteSuperior() {
		assertThrows(IllegalArgumentException.class, () -> {
			sistema.comprarBilletes("ABC12345", user, differentRecorrido, 251);
		});
	}

	@Test
	void testComprarBilleteBusInvalidoLimiteSuperiorDemasiadaCompras() {
		sistema.addRecorrido(recorrido);
		sistema.comprarBilletes("ABC12345", user, recorrido, 49);
		assertThrows(IllegalArgumentException.class, () -> {
			sistema.comprarBilletes("ABC12345", differentUser, recorrido, 2);
		});
	}

	@Test
	void testComprarBilleteTrainInvalidoLimiteSuperiorDemasiadaCompras() {
		Recorrido rec = new TrainRecorrido("dif", connection, price, dateTime, 250);
		sistema.addRecorrido(rec);
		sistema.comprarBilletes("ABC12345", user, rec, 249);
		assertThrows(IllegalArgumentException.class, () -> {
			sistema.comprarBilletes("ABC12345", differentUser, rec, 2);
		});
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
		Recorrido rec = new TrainRecorrido("dif", connection, price, dateTime, 250);
		sistema.addRecorrido(rec);
		sistema.addRecorrido(differentRecorrido);
		sistema.comprarBilletes("ABC12345", user, rec, 2);
		assertThrows(IllegalArgumentException.class, () -> {
			sistema.comprarBilletes("ABC12345", differentUser, differentRecorrido, 1);
		});
	}

	@Test
	void testComprarBilleteLocalizadorYaUsadoDistinoRecorridoRecorrido() {
		Recorrido rec = new TrainRecorrido("dif", connection, price, dateTime, 250);
		sistema.addRecorrido(rec);
		sistema.addRecorrido(recorrido);
		sistema.comprarBilletes("ABC12345", user, rec, 2);
		assertThrows(IllegalArgumentException.class, () -> {
			sistema.comprarBilletes("ABC12345", differentUser, recorrido, 1);
		});
		
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
		sistema.addRecorrido(differentRecorrido);
		sistema.addRecorrido(recorrido);
		sistema.comprarBilletes("ABC12346", user, recorrido, 1);
		List<Billete> listaBilletes = sistema.comprarBilletes(localizador, user, differentRecorrido, 1);
		List<Billete> listaBilletesComprobacion = new ArrayList<>();
		Billete billeteComprobacion = new Billete(localizador, differentRecorrido, user, ESTADO_COMPRADO);
		listaBilletesComprobacion.add(billeteComprobacion);

		assertEquals(listaBilletes, listaBilletesComprobacion);

	}

	/**
	 * FINDME Tests for
	 * {@link SistemaPersistenciaSinAislamiento#comprarBilletesReservados(String)}
	 */
	@Test
	void testComprarBilletesReservadosValidoLimiteInferior() {
		String locator = "1";
		sistema.addRecorrido(recorrido);
		sistema.reservarBilletes(locator, user, recorrido, 3);
		Recorrido rec=sistema.getRecorrido(recorrido.getID());
		List<Billete> purchasedTicketsCheck = new ArrayList<>();
		purchasedTicketsCheck.add(new Billete(locator, rec, user, ESTADO_COMPRADO));
		purchasedTicketsCheck.add(new Billete(locator, rec, user, ESTADO_COMPRADO));
		purchasedTicketsCheck.add(new Billete(locator, rec, user, ESTADO_COMPRADO));
		List<Billete> purchasedTickets = sistema.comprarBilletesReservados(locator);
		assertEquals(purchasedTicketsCheck, purchasedTickets);

		
	}

	@Test
	void testComprarBilletesReservadosValidoLimiteSuperior() {
		String locator = "12345678";
		sistema.addRecorrido(recorrido);
		sistema.reservarBilletes(locator, user, recorrido, 3);
		Recorrido rec=sistema.getRecorrido(recorrido.getID());
		List<Billete> buyedTicketsCheck = new ArrayList<>();
		buyedTicketsCheck.add(new Billete(locator, rec, user, ESTADO_COMPRADO));
		buyedTicketsCheck.add(new Billete(locator, rec, user, ESTADO_COMPRADO));
		buyedTicketsCheck.add(new Billete(locator, rec, user, ESTADO_COMPRADO));
		List<Billete> buyedTickets = sistema.comprarBilletesReservados(locator);
		assertEquals(buyedTicketsCheck, buyedTickets);

		
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
		sistema.addRecorrido(recorrido);
		assertThrows(IllegalStateException.class, () -> {
			sistema.comprarBilletesReservados(locator);
		});

		
	}

	@Test
	void testComprarBilletesReservadosConLocalizadorDeBilletesComprados() {
		String locator = "12345678";
		sistema.addRecorrido(recorrido);
		sistema.comprarBilletes(locator, user, recorrido, 2);
		assertThrows(IllegalStateException.class, () -> {
			sistema.comprarBilletesReservados(locator);
		});

		
	}

	/**
	 * FINDME Tests for
	 * {@link SistemaPersistenciaSinAislamiento#reservarBilletes(String, Usuario, Recorrido, int)}
	 */
	@Test
	void testReservarBilletesExitosamente() {
		int plazasDisponibles = recorrido.getNumAvailableSeats();
		int numBilletesReservar = 6;
		sistema.addRecorrido(recorrido);
		List<Billete> reservado = sistema.reservarBilletes("ABC12345", user, recorrido, numBilletesReservar);

		assertEquals(plazasDisponibles - numBilletesReservar, sistema.getRecorrido(recorrido.getID()).getNumAvailableSeats());

		ArrayList<Billete> listaBilletesComprobacion = new ArrayList<>();
		for (int i = 0; i < numBilletesReservar; i++) {
			Billete billeteComprobacion = new Billete("ABC12345", recorrido, user, ESTADO_RESERVADO);
			listaBilletesComprobacion.add(billeteComprobacion);
		}
		assertEquals(reservado, listaBilletesComprobacion);

		
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
		sistema.addRecorrido(recorrido);
		sistema.comprarBilletes(localizador, user, recorrido, numBilletesComprar);
		Recorrido rec=sistema.getRecorrido(recorrido.getID());
		assertThrows(IllegalStateException.class, () -> {
			sistema.reservarBilletes(localizador2, user, rec, 5);
		});
		

	}

	/**
	 * FINDME Tests for {@link SistemaPersistenciaSinAislamiento#anularReserva(String, int)}
	 */
	@Test
	void testAnularReservaAumentaPlazasDisponiblesLimiteInferior() {
		int numBilletesReservar = 6;
		int numBilletesAnular = 1;
		String localizador = "ABC12345";
		sistema.addRecorrido(recorrido);
		sistema.reservarBilletes(localizador, user, recorrido, numBilletesReservar);
		int plazasDisponiblesAntes = sistema.getRecorrido(recorrido.getID()).getNumAvailableSeats();

		// Realiza la anulación de la reserva
		sistema.anularReserva(localizador, numBilletesAnular);

		int plazasDisponiblesDespues = sistema.getRecorrido(recorrido.getID()).getNumAvailableSeats();

		// Verifica que las plazas disponibles aumenten en la cantidad correcta
		assertEquals(plazasDisponiblesAntes + numBilletesAnular, plazasDisponiblesDespues);
	}

	@Test
	void testNoSePuedeAnularReservaSiLocalizadorNull() {
		assertThrows(IllegalArgumentException.class, () -> {
			sistema.anularReserva(null, 1);
		});
	}

	@Test
	void testNoSePuedeAnularReservaSiLocalizadorVacio() {
		assertThrows(IllegalArgumentException.class, () -> {
			sistema.anularReserva("", 1);
		});
	}

	@Test
	void testNoSePuedeAnularReservaSiFueraSistema() {
		String localizador = "ABC12345";
		int numBilletesAnular = 2;

		assertThrows(IllegalStateException.class, () -> {
			sistema.anularReserva(localizador, numBilletesAnular);
		});
		
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
		sistema.addRecorrido(recorrido);
		sistema.comprarBilletes(localizador, user, recorrido, numBilletesComprar);
		assertThrows(IllegalStateException.class, () -> {
			sistema.anularReserva(localizador, numBilletesAnular);
		});

		
	}

	@Test
	void testNoSePuedeAnularReservaSiMayorNumeroDeAnulacionesQueBilletesReservados() {
		String localizador = "ABC12345";
		int numBilletesReservar = 2;
		int numBilletesAnular = 3;
		sistema.addRecorrido(recorrido);
		sistema.reservarBilletes(localizador, user, recorrido, numBilletesReservar);
		assertThrows(IllegalStateException.class, () -> {
			sistema.anularReserva(localizador, numBilletesAnular);
		});
		
	}

	/**
	 * FINDME Tests for {@link SistemaPersistenciaSinAislamiento#devolverBilletes(String, int)}
	 */
	@Test
	void testDevolverBilletesAumentaPlazasDisponiblesLimiteInferior() {
		int numBilletesComprar = 6;
		int numBilletesDevolver = 1;
		String localizador = "ABC12345";
		sistema.addRecorrido(recorrido);
		sistema.comprarBilletes(localizador, user, recorrido, numBilletesComprar);
		
		int plazasDisponiblesAntes = database.getRecorrido(recorrido.getID()).getNumAvailableSeats();

		// Realiza la anulación de la reserva
		sistema.devolverBilletes(localizador, numBilletesDevolver);

		int plazasDisponiblesDespues = database.getRecorrido(recorrido.getID()).getNumAvailableSeats();

		// Verifica que las plazas disponibles aumenten en la cantidad correcta
		assertEquals(plazasDisponiblesAntes + numBilletesDevolver, plazasDisponiblesDespues);

		
	}

	@Test
	void testNoSePuedeDevolverBilletesSiLocalizadorNull() {
		assertThrows(IllegalArgumentException.class, () -> {
			sistema.devolverBilletes(null, 1);
		});
	}

	@Test
	void testNoSePuedeDevolverBilletesSiLocalizadorVacio() {
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
		assertThrows(IllegalStateException.class, () -> {
			sistema.devolverBilletes("ABC12345", numBilletesDevolver);
		});

		

	}

	@Test
	void testNoSePuedeDevolverBilleteSiReservados() {
		int numBilletesReservar = 6;
		int numBilletesDevolver = 1;
		String localizador = "ABC12345";
		sistema.addRecorrido(recorrido);
		sistema.reservarBilletes(localizador, user, recorrido, numBilletesReservar);

		assertThrows(IllegalStateException.class, () -> {
			sistema.devolverBilletes(localizador, numBilletesDevolver);
		});

		

	}

	@Test
	void testNoSePuedeDevolverBilletesConNumLimiteInferior() {
		String locator = "ABC12345";
		int numBilletesComprar = 1;
		int numBilletesDevolver = 0;
		sistema.addRecorrido(recorrido);
		sistema.comprarBilletes(locator, user, recorrido, numBilletesComprar);
		assertThrows(IllegalArgumentException.class, () -> {
			sistema.devolverBilletes(locator, numBilletesDevolver);
		});

		
	}

	@Test
	void testNoSePuedeDevolverBilletesSiMayorNumeroDeDevolucionesQueBilletesComprados() {
		String locator = "ABC12345";
		int numBilletesDevolver = 3;
		int numBilletesComprar = 1;
		sistema.addRecorrido(recorrido);
		sistema.comprarBilletes(locator, user, recorrido, numBilletesComprar);
		assertThrows(IllegalStateException.class, () -> {
			sistema.devolverBilletes(locator, numBilletesDevolver);
		});
		
	}

	@Test
	@Tag("Cobertura")
	void testLocalizadorYaUtilizadoReserva() {
		String localizador = "ABC12345";
		int numBilletesReservar = 1;
		sistema.addRecorrido(recorrido);
		sistema.reservarBilletes(localizador, user, recorrido, numBilletesReservar);

		assertThrows(IllegalStateException.class, () -> {
			sistema.reservarBilletes(localizador, user, recorrido, 1);
		});

		
	}

	@Test
	@Tag("Cobertura")
	void testAnularReservaMismoNumeroDeAnulacionesQueBilletesReservados() {
		String localizador = "ABC12345";
		int numBilletesReservar = 3;
		int numBilletesAnular = 3;
		sistema.addRecorrido(recorrido);
		sistema.reservarBilletes(localizador, user, recorrido, numBilletesReservar);
		sistema.anularReserva(localizador, numBilletesAnular);
		assertEquals(sistema.getAssociatedBilletesToRoute(recorrido.getID()), new ArrayList<>());
	}
	
	@Test
	@Tag("Cobertura")
	void testDevolverBilletesIgualNumeroDeDevolucionesQueBilletesComprados() {
		String locator = "ABC12345";
		int numBilletesDevolver = 3;
		int numBilletesComprar = 3;
		sistema.addRecorrido(recorrido);
		sistema.comprarBilletes(locator, user, recorrido, numBilletesComprar);
		sistema.devolverBilletes(locator, numBilletesDevolver);
		
		assertEquals(sistema.getAssociatedBilletesToRoute(recorrido.getID()), new ArrayList<>());
	}
	
	@Test
	@Tag("Cobertura")
	void testreservarBilletesConUsuarioYaRegistrado() {
		String locator = "ABC12345";
		int numBilletesReservar = 3;
		sistema.addRecorrido(recorrido);
		sistema.reservarBilletes(locator, user, recorrido, numBilletesReservar);
		sistema.reservarBilletes("ABC54321", user, recorrido, numBilletesReservar);
		Recorrido recorrido = sistema.getRecorrido(this.recorrido.getID());
		ArrayList<Billete> b = new ArrayList<>();
		for (int i = 0; i < numBilletesReservar; i++)
			b.add(new Billete(locator, recorrido, user, ESTADO_RESERVADO));
		for (int i = 0; i < numBilletesReservar; i++) 
			b.add(new Billete("ABC54321", recorrido, user, ESTADO_RESERVADO));
		assertEquals(sistema.getAssociatedBilletesToRoute(recorrido.getID()), b);
	}
	
	@AfterEach
	void tearDown() {
		this.database.clearDatabase();
	}
}
