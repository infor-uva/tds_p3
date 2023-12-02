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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * Class made for tests the methods of {@link System}
 * 
 * @author hugcubi
 * @author diebomb
 * @author migudel
 * 
 * @version 28/11/23
 */
class SystemTest {

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
	private System system;
	private int numSeats;
	private LocalDateTime newDateTime;
	private LocalDate newDate;
	private LocalTime newTime;

	@BeforeEach
	void setUp() {
		nif = "32698478E";
		nombre = "Geronimo";
		user = new Usuario(nif, nombre);
		differentUser = new Usuario("79105889B", nombre);
		id = "c12345";
		origin = "Valladolid";
		destination = "Galicia";
		transport = BUS;
		date = LocalDate.of(2023, 10, 27);
		time = LocalTime.of(19, 06, 50);
		price = 1.0;
		numSeats = 50;
		duration = 30;
		recorrido = new Recorrido(id, origin, destination, transport, price, date, time, numSeats, duration);
		transport = TRAIN;
		differentRecorrido = new Recorrido("dif", origin, destination, transport, price, date, time, numSeats,
				duration);
		idLI = "i";
		recorridoLI = new Recorrido(idLI, origin, destination, transport, price, date, time, numSeats, duration);

		newDateTime = LocalDateTime.of(2023, 5, 14, 22, 56, 20);
		newDate = LocalDate.of(2024, 2, 4);
		newTime = LocalTime.of(12, 2, 4);

		system = new System();
	}

	/**
	 * FINDME Tests for {@link System#system()}
	 */
	@Test
	void testConstructor() {
		System system = new System();
		assertNotNull(system);
		assertEquals(new ArrayList<>(), system.getRecorridos());
		assertEquals(new ArrayList<>(), system.getBilletes());
	}

	/**
	 * FINDME Tests for {@link System#addRecorrido(Recorrido)}
	 */
	@Test
	void testAddRecorridoValido() {
		system.addRecorrido(recorrido);
		List<Recorrido> recorridos = system.getRecorridos();
		List<Recorrido> recorridosCheck = new ArrayList<>();
		recorridosCheck.add(recorrido);
		assertEquals(recorridosCheck, recorridos);
	}

	@Test
	void testAddRecorridoConRecorridoNull() {
		assertThrows(IllegalArgumentException.class, () -> {
			system.addRecorrido(null);
		});
	}

	@Test
	void testAddRecorridoConRecorridoYaEnSystem() {
		system.addRecorrido(recorrido);
		assertThrows(IllegalStateException.class, () -> {
			system.addRecorrido(recorrido);
		});
	}

	/**
	 * FINDME Tests for {@link System#removeRecorrido(String))}
	 */
	@Test
	void testRemoveRecorridoValidoConIdLimiteInferior() {
		system.addRecorrido(recorridoLI);
		system.removeRecorrido(idLI);
		assertEquals(new ArrayList<>(), system.getRecorridos());
	}

	@Test
	void testRemoveRecorridoConIDNull() {
		assertThrows(IllegalArgumentException.class, () -> {
			system.removeRecorrido(null);
		});
	}

	@Test
	void testRemoveRecorridoConIDVacio() {
		assertThrows(IllegalArgumentException.class, () -> {
			system.removeRecorrido("");
		});
	}

	@Test
	void testRemoveRecorridoConIDFueraSystem() {
		assertThrows(IllegalStateException.class, () -> {
			system.removeRecorrido(id);
		});
	}

	@Test
	void testRemoveRecorridoConRecorridoEnSystemConBilletesAsociados() {
		Usuario usuario = new Usuario("32698478E", "Geronimo");
		system.addRecorrido(recorrido);
		system.comprarBilletes("T12345", usuario, recorrido, 1);

		assertThrows(IllegalStateException.class, () -> {
			system.removeRecorrido(id);
		});
	}

	/**
	 * FINDME Tests for {@link System#getPrecioTotalBilletesUsuario(String)}
	 */
	@Test
	void testGetPrecioTotalBilletesUsuarioBus() {
		system.comprarBilletes("32698478", user, recorrido, 5);
		assertEquals(5.0, system.getPrecioTotalBilletesUsuario("32698478E"), ERROR_MARGIN);
	}

	@Test
	void testGetPrecioTotalBilletesUsuarioPrecioRecorridoTren() {
		system.comprarBilletes("32698479", user, differentRecorrido, 5);
		assertEquals(4.5, system.getPrecioTotalBilletesUsuario("32698478E"), ERROR_MARGIN);
	}

	@Test
	void testGetPrecioTotalBilletesUsuarioNulo() {
		assertThrows(IllegalArgumentException.class, () -> {
			system.getPrecioTotalBilletesUsuario(null);
		});
	}

	@Test
	void testGetPrecioTotalBilletesUsuarioVacio() {
		assertThrows(IllegalArgumentException.class, () -> {
			system.getPrecioTotalBilletesUsuario("");
		});
	}

	@Test
	void testGetPrecioTotalBilletesUsuarioNoEnSystem() {
		assertThrows(IllegalArgumentException.class, () -> {
			system.getPrecioTotalBilletesUsuario("32698478E");
		});
	}

	@Test
	void testGetPrecioTotalBilletesUsuarioSinBilletes() {
		system.comprarBilletes("1234T", user, recorrido, 5);
		system.devolverBilletes("1234T", 5);
		assertThrows(IllegalStateException.class, () -> {
			system.getPrecioTotalBilletesUsuario(user.getNif());
		});
	}

	@Test
	void testGetPrecioTotalBilletesUsuarioConNifLimiteInferiorDigitos() {
		assertThrows(IllegalArgumentException.class, () -> {
			system.getPrecioTotalBilletesUsuario("3269847E");
		});
	}

	@Test
	void testGetPrecioTotalBilletesUsuarioConNifLimiteSuperiorDigitos() {
		assertThrows(IllegalArgumentException.class, () -> {
			system.getPrecioTotalBilletesUsuario("326984789E");
		});
	}

	@Test
	void testGetPrecioTotalBilletesUsuarioConNifSinCaracter() {
		assertThrows(IllegalArgumentException.class, () -> {
			system.getPrecioTotalBilletesUsuario("326984788");
		});
	}

	@Test
	void testGetPrecioTotalBilletesUsuarioConNifInvalido() {
		assertThrows(IllegalArgumentException.class, () -> {
			system.getPrecioTotalBilletesUsuario("32698478P");
		});
	}

	@Test
	void testGetPrecioTotalBilletesUsuarioConNifInvalidoLetraInvalidaI() {
		assertThrows(IllegalArgumentException.class, () -> {
			system.getPrecioTotalBilletesUsuario("32698478I");
		});
	}

	@Test
	void testGetPrecioTotalBilletesUsuarioConNifInvalidoLetraInvalidaO() {
		assertThrows(IllegalArgumentException.class, () -> {
			system.getPrecioTotalBilletesUsuario("32698478O");
		});
	}

	@Test
	void testGetPrecioTotalBilletesUsuarioConNifInvalidoLetraInvalidaÑ() {
		assertThrows(IllegalArgumentException.class, () -> {
			system.getPrecioTotalBilletesUsuario("32698478Ñ");
		});
	}

	@Test
	void testGetPrecioTotalBilletesUsuarioConNifInvalidoLetraInvalidaU() {
		assertThrows(IllegalArgumentException.class, () -> {
			system.getPrecioTotalBilletesUsuario("32698478U");
		});
	}
	
	@Tag("Coberage")
	@Test
	void testGetPrecioTotalBilletesConBilletesAsociados() {
		system.comprarBilletes("1234T", differentUser, recorrido, 5);
		system.comprarBilletes("1234R", user, recorrido, 1);
		assertEquals(1.0, system.getPrecioTotalBilletesUsuario(user.getNif()), ERROR_MARGIN);
	}

	/**
	 * FINDME Tests for {@link System#getRecorridosDisponiblesFecha(LocalDate)}
	 */
	@Test
	void testGetRecorridosDisponiblesFecha() {
		ArrayList<Recorrido> recorridos = new ArrayList<>();
		recorridos.add(recorrido);
		recorridos.add(differentRecorrido);
		system.addRecorrido(recorrido);
		system.addRecorrido(differentRecorrido);
		assertEquals(recorridos, system.getRecorridosDisponiblesFecha(date));
	}

	@Test
	void testGetRecorridosDisponiblesFechaSinFecha() {
		assertThrows(IllegalArgumentException.class, () -> {
			system.getRecorridosDisponiblesFecha(null);
		});
	}

	@Test
	void testGetRecorridosDisponiblesFechaSinRecorridos() {
		system.addRecorrido(recorrido);
		system.addRecorrido(differentRecorrido);
		LocalDate date1 = LocalDate.of(15, 10, 24);
		assertThrows(IllegalStateException.class, () -> {
			system.getRecorridosDisponiblesFecha(date1);
		});
	}

	/**
	 * FINDME Tests for {@link System#getAssociatedBilletesToRoute(String))}
	 */
	@Test
	void testGetAssociatedBilletesToRouteValidoConIDLimiteInferior() {
		String locator = "123";
		String locator2 = "321";
		system.addRecorrido(recorridoLI);
		system.reservarBilletes(locator, user, recorridoLI, 1);
		system.comprarBilletes(locator2, differentUser, recorridoLI, 2);

		List<Billete> expected = new ArrayList<>();
		expected.add(new Billete(locator, recorridoLI, user, ESTADO_RESERVADO));
		expected.add(new Billete(locator2, recorridoLI, differentUser, ESTADO_COMPRADO));
		expected.add(new Billete(locator2, recorridoLI, differentUser, ESTADO_COMPRADO));

		assertEquals(expected, system.getAssociatedBilletesToRoute(idLI));
	}

	@Test
	void testGetAssociatedBilletesToRouteConIDNull() {
		assertThrows(IllegalArgumentException.class, () -> {
			system.getAssociatedBilletesToRoute(null);
		});
	}

	@Test
	void testGetAssociatedBilletesToRouteConIDVacio() {
		assertThrows(IllegalArgumentException.class, () -> {
			system.getAssociatedBilletesToRoute("");
		});
	}

	@Test
	void testGetAssociatedBilletesToRouteConRouteFueraDeSystem() {
		assertThrows(IllegalStateException.class, () -> {
			system.getAssociatedBilletesToRoute(id);
		});
	}
	
	@Test
	@Tag("Cobertura")
	void testGetAssociatedBilletesToRouteConVariasBilletesCoRecorridosDiferentesEnSystem() {
		system.addRecorrido(recorrido);
		system.addRecorrido(differentRecorrido);
		String locator = "123";
		String locator2 = "321";
		system.comprarBilletes(locator, user, recorrido, 2);
		system.comprarBilletes(locator2, user, differentRecorrido, 2);
		
		List<Billete> expected = new ArrayList<>();
		for (int i = 0; i < 2; i++)
			expected.add(new Billete(locator, recorrido, user, ESTADO_COMPRADO));

		assertEquals(expected, system.getAssociatedBilletesToRoute(id));	
	}	

	/**
	 * FINDME Tests for {@link System#getDateOfRecorrido(String)}
	 */
	@Test
	void testGetDateOfRecorridoValidoConIDLimiteInferior() {
		system.addRecorrido(recorridoLI);
		assertEquals(recorridoLI.getDate(), system.getDateOfRecorrido(idLI));
	}

	@Test
	void testGetDateOfRecorridoConIDNull() {
		assertThrows(IllegalArgumentException.class, () -> {
			system.getDateOfRecorrido(null);
		});
	}

	@Test
	void testGetDateOfRecorridoNoValidoConIDVacio() {
		assertThrows(IllegalArgumentException.class, () -> {
			system.getDateOfRecorrido("");
		});
	}

	@Test
	void testGetDateOfRecorridoNoValidoConRecorridoFueraDeSystem() {
		assertThrows(IllegalStateException.class, () -> {
			system.getDateOfRecorrido(id);
		});
	}

	/**
	 * FINDME Tests for {@link System#getTimeOfRecorrido(String)}
	 */
	@Test
	void testGetTimeOfRecorridoValidoConIDLimiteInferior() {
		system.addRecorrido(recorridoLI);
		assertEquals(recorridoLI.getTime(), system.getTimeOfRecorrido(idLI));
	}

	@Test
	void testGetTimeOfRecorridoNoValidoConIDNull() {
		assertThrows(IllegalArgumentException.class, () -> {
			system.getTimeOfRecorrido(null);
		});
	}

	@Test
	void testGetTimeOfRecorridoNoValidoConIDVacio() {
		assertThrows(IllegalArgumentException.class, () -> {
			system.getTimeOfRecorrido("");
		});
	}

	@Test
	void testGetTimeOfRecorridoNoValidoConIDLimiteInferior() {
		assertThrows(IllegalArgumentException.class, () -> {
			system.getTimeOfRecorrido("");
		});
	}

	@Test
	void testGetTimeOfRecorridoNoValidoConRecorridoFueraDeSystem() {
		assertThrows(IllegalStateException.class, () -> {
			system.getTimeOfRecorrido(id);
		});
	}

	/**
	 * FINDME Tests for {@link System#getDateTimeOfRecorrido(String)}
	 */
	@Test
	void testGetDateTimeOfRecorridoValidoConIDLimiteInferior() {
		system.addRecorrido(recorridoLI);
		assertEquals(recorridoLI.getDateTime(), system.getDateTimeOfRecorrido(idLI));
	}

	@Test
	void testGetDateTimeOfRecorridoNoValidoConIDNull() {
		assertThrows(IllegalArgumentException.class, () -> {
			system.getDateTimeOfRecorrido(null);
		});
	}

	@Test
	void testGetDateTimeOfRecorridoNoValidoConIDLimiteInferior() {
		assertThrows(IllegalArgumentException.class, () -> {
			system.getDateTimeOfRecorrido("");
		});
	}

	@Test
	void testGetDateTimeOfRecorridoNoValidoConRecorridoFueraDeSystem() {
		assertThrows(IllegalStateException.class, () -> {
			system.getDateTimeOfRecorrido(id);
		});
	}

	/**
	 * FINDME Tests for {@link System#updateRecorridoDate(String, LocalDate)}
	 */
	@Test
	void testUpdateRecorridoDateValidoConIDLimiteInferior() {
		system.addRecorrido(recorridoLI);
		assertNotEquals(newDate, system.getDateOfRecorrido(idLI));
		system.updateRecorridoDate(idLI, newDate);
		assertEquals(newDate, system.getDateOfRecorrido(idLI));
	}

	@Test
	void testUpdateRecorridoDateConIDNull() {
		assertThrows(IllegalArgumentException.class, () -> {
			system.updateRecorridoDate(null, newDate);
		});
	}

	@Test
	void testUpdateRecorridoDateConIDVacio() {
		assertThrows(IllegalArgumentException.class, () -> {
			system.updateRecorridoDate("", newDate);
		});
	}

	@Test
	void testUpdateRecorridoDateConRecorridoFueraDelsystem() {
		assertThrows(IllegalStateException.class, () -> {
			system.updateRecorridoDate(id, newDate);
		});
	}

	@Test
	void testUpdateRecorridoDateConDateNull() {
		system.addRecorrido(recorrido);
		assertThrows(IllegalArgumentException.class, () -> {
			system.updateRecorridoDate(id, null);
		});
	}

	@Test
	void testUpdateRecorridoDateConValoresActuales() {
		system.addRecorrido(recorrido);
		assertThrows(IllegalStateException.class, () -> {
			system.updateRecorridoDate(id, recorrido.getDate());
		});
	}

	/**
	 * FINDME Tests for {@link System#updateRecorridoTime(String, LocalTime)}
	 */
	@Test
	void testUpdateRecorridoTimeValidoConIDLimiteInferior() {
		system.addRecorrido(recorridoLI);
		assertNotEquals(newTime, system.getTimeOfRecorrido(idLI));
		system.updateRecorridoTime(idLI, newTime);
		assertEquals(newTime, system.getTimeOfRecorrido(idLI));
	}

	@Test
	void testUpdateRecorridoTimeConIDNull() {
		assertThrows(IllegalArgumentException.class, () -> {
			system.updateRecorridoTime(null, newTime);
		});
	}

	@Test
	void testUpdateRecorridoTimeConIDVacio() {
		assertThrows(IllegalArgumentException.class, () -> {
			system.updateRecorridoTime("", newTime);
		});
	}

	@Test
	void testUpdateRecorridoTimeConRecorridoFueraDelsystem() {
		assertThrows(IllegalStateException.class, () -> {
			system.updateRecorridoTime(id, newTime);
		});
	}

	@Test
	void testUpdateRecorridoTimeConTimeNull() {
		system.addRecorrido(recorrido);
		assertThrows(IllegalArgumentException.class, () -> {
			system.updateRecorridoTime(id, null);
		});
	}

	@Test
	void testUpdateRecorridoTimeConValoresActuales() {
		system.addRecorrido(recorrido);
		assertThrows(IllegalStateException.class, () -> {
			system.updateRecorridoTime(id, recorrido.getTime());
		});
	}

	/**
	 * FINDME Tests for
	 * {@link System#updateRecorridoDateTime(String, LocalDateTime)}
	 */
	@Test
	void testUpdateRecorridoDateTimeValidoConIDLimiteInferior() {
		system.addRecorrido(recorridoLI);
		assertNotEquals(newDateTime, system.getDateTimeOfRecorrido(idLI));
		system.updateRecorridoDateTime(idLI, newDateTime);
		assertEquals(newDateTime, system.getDateTimeOfRecorrido(idLI));
	}

	@Test
	void testUpdateRecorridoDateTimeConIDNull() {
		assertThrows(IllegalArgumentException.class, () -> {
			system.updateRecorridoDateTime(null, newDateTime);
		});
	}

	@Test
	void testUpdateRecorridoDateTimeConIDVacio() {
		assertThrows(IllegalArgumentException.class, () -> {
			system.updateRecorridoDateTime("", newDateTime);
		});
	}

	@Test
	void testUpdateRecorridoDateTimeConRecorridoFueraDelsystem() {
		assertThrows(IllegalStateException.class, () -> {
			system.updateRecorridoDateTime(id, newDateTime);
		});
	}

	@Test
	void testUpdateRecorridoDateTimeConDateTimeNull() {
		system.addRecorrido(recorrido);
		assertThrows(IllegalArgumentException.class, () -> {
			system.updateRecorridoDateTime(id, null);
		});
	}

	@Test
	void testUpdateRecorridoDateTimeConValoresActuales() {
		system.addRecorrido(recorrido);
		assertThrows(IllegalStateException.class, () -> {
			system.updateRecorridoDateTime(id, recorrido.getDateTime());
		});
	}

	/**
	 * FINDME Tests for {@link System#updateRecorrido(String, LocalDate, LocalTime)}
	 */
	@Test
	void testUpdateRecorridoValidoConIDLimiteInferior() {
		system.addRecorrido(recorridoLI);
		assertNotEquals(newDateTime, system.getDateTimeOfRecorrido(idLI));
		system.updateRecorrido(idLI, newDateTime.toLocalDate(), newDateTime.toLocalTime());
		assertEquals(newDateTime, system.getDateTimeOfRecorrido(idLI));
	}

	@Test
	void testUpdateRecorridoConIDNull() {
		assertThrows(IllegalArgumentException.class, () -> {
			system.updateRecorrido(null, newDate, newTime);
		});
	}

	@Test
	void testUpdateRecorridoConIDVacio() {
		assertThrows(IllegalArgumentException.class, () -> {
			system.updateRecorrido("", newDate, newTime);
		});
	}

	@Test
	void testUpdateRecorridoConRecorridoFueraDelsystem() {
		assertThrows(IllegalStateException.class, () -> {
			system.updateRecorrido(id, newDate, newTime);
		});
	}

	@Test
	void testUpdateRecorridoConDateNull() {
		system.addRecorrido(recorrido);
		assertThrows(IllegalArgumentException.class, () -> {
			system.updateRecorrido(id, null, newTime);
		});
	}

	@Test
	void testUpdateRecorridoConTimeNull() {
		system.addRecorrido(recorrido);
		assertThrows(IllegalArgumentException.class, () -> {
			system.updateRecorrido(id, newDate, null);
		});
	}

	@Test
	void testUpdateRecorridoConValoresActuales() {
		system.addRecorrido(recorrido);
		assertThrows(IllegalStateException.class, () -> {
			system.updateRecorrido(id, recorrido.getDate(), recorrido.getTime());
		});
	}

	/**
	 * FINDME Tests for
	 * {@link System#comprarBilletes(String, Usuario, Recorrido, int)}
	 */
	@Test
	void testComprarBilletesValidoBusLimiteInferior() {
		List<Billete> listaBilletes = system.comprarBilletes("ABC12345", user, recorrido, 1);
		List<Billete> listaBilletesComprobacion = new ArrayList<>();
		Billete billeteComprobacion = new Billete("ABC12345", recorrido, user, ESTADO_COMPRADO);
		for (int i = 0; i < 1; i++) {
			listaBilletesComprobacion.add(billeteComprobacion);
		}
		assertEquals(listaBilletes, listaBilletesComprobacion);
	}

	@Test
	void testComprarBilletesValidosBusLimiteSuperior() {
		List<Billete> listaBilletes = system.comprarBilletes("ABC12345", user, recorrido, 50);
		List<Billete> listaBilletesComprobacion = new ArrayList<>();
		Billete billeteComprobacion = new Billete("ABC12345", recorrido, user, ESTADO_COMPRADO);
		for (int i = 0; i < 50; i++) {
			listaBilletesComprobacion.add(billeteComprobacion);
		}
		assertEquals(listaBilletes, listaBilletesComprobacion);
	}

	@Test
	void testComprarBilletesValidoTrenLimiteInferior() {
		List<Billete> listaBilletes = system.comprarBilletes("ABC12345", user, differentRecorrido, 1);
		List<Billete> listaBilletesComprobacion = new ArrayList<>();
		Billete billeteComprobacion = new Billete("ABC12345", differentRecorrido, user, ESTADO_COMPRADO);
		for (int i = 0; i < 1; i++) {
			listaBilletesComprobacion.add(billeteComprobacion);
		}
		assertEquals(listaBilletes, listaBilletesComprobacion);
	}

	@Test
	void testComprarBilletesValidosTrenLimiteSuperior() {
		differentRecorrido = new Recorrido("dif", origin, destination, transport, price, date, time, 250, duration);
		List<Billete> listaBilletes = system.comprarBilletes("ABC12345", user, differentRecorrido, 250);
		List<Billete> listaBilletesComprobacion = new ArrayList<>();
		Billete billeteComprobacion = new Billete("ABC12345", differentRecorrido, user, ESTADO_COMPRADO);
		for (int i = 0; i < 250; i++) {
			listaBilletesComprobacion.add(billeteComprobacion);
		}
		assertEquals(listaBilletes, listaBilletesComprobacion);
	}

	@Test
	void testComprarBilleteBusInvalidoLimiteInferior() {
		assertThrows(IllegalArgumentException.class, () -> {
			system.comprarBilletes("ABC12345", user, recorrido, 0);
		});
	}

	@Test
	void testComprarBilleteTrainInvalidoLimiteInferior() {
		assertThrows(IllegalArgumentException.class, () -> {
			system.comprarBilletes("ABC12345", user, differentRecorrido, 0);
		});
	}

	@Test
	void testComprarBilleteBusInvalidoLimiteSuperior() {
		assertThrows(IllegalArgumentException.class, () -> {
			system.comprarBilletes("ABC12345", user, recorrido, 151);
		});
	}

	@Test
	void testComprarBilleteTrainInvalidoLimiteSuperior() {
		assertThrows(IllegalArgumentException.class, () -> {
			system.comprarBilletes("ABC12345", user, differentRecorrido, 251);
		});
	}

	@Test
	void testComprarBilleteBusInvalidoLimiteSuperiorDemasiadaCompras() {
		recorrido = new Recorrido(id, origin, destination, transport, price, date, time, 50, duration);
		system.comprarBilletes("ABC12345", user, recorrido, 49);
		assertThrows(IllegalStateException.class, () -> {
			system.comprarBilletes("ABC12346", differentUser, recorrido, 2);
		});
	}

	@Test
	void testComprarBilleteTrainInvalidoLimiteSuperiorDemasiadaCompras() {
		differentRecorrido = new Recorrido("dif", origin, destination, transport, price, date, time, 250, duration);
		system.comprarBilletes("ABC12345", user, differentRecorrido, 249);
		assertThrows(IllegalStateException.class, () -> {
			system.comprarBilletes("ABC12346", differentUser, differentRecorrido, 2);
		});
	}

	@Test
	void testComprarBilleteLocalizadorInvalido() {
		assertThrows(IllegalArgumentException.class, () -> {
			system.comprarBilletes("", user, differentRecorrido, 1);
		});
	}

	@Test
	void testComprarBilleteLocalizadorNull() {
		assertThrows(IllegalArgumentException.class, () -> {
			system.comprarBilletes(null, user, differentRecorrido, 1);
		});
	}

	@Test
	void testComprarBilleteLocalizadorYaUsadoMismoRecorrido() {
		system.comprarBilletes("ABC12345", user, differentRecorrido, 2);
		assertThrows(IllegalArgumentException.class, () -> {
			system.comprarBilletes("ABC12345", differentUser, differentRecorrido, 1);
		});
	}

	@Test
	void testComprarBilleteLocalizadorYaUsadoDistinoRecorridoRecorrido() {
		system.comprarBilletes("ABC12345", user, differentRecorrido, 2);
		assertThrows(IllegalArgumentException.class, () -> {
			system.comprarBilletes("ABC12345", differentUser, recorrido, 1);
		});
	}

	@Test
	void testComprarBilleteUsuarioNull() {
		assertThrows(IllegalArgumentException.class, () -> {
			system.comprarBilletes("ABC12345", null, differentRecorrido, 1);
		});
	}

	@Test
	void testComprarBilleteRecorridoNull() {
		assertThrows(IllegalArgumentException.class, () -> {
			system.comprarBilletes("ABC12345", user, null, 1);
		});
	}

	@Tag("Cobertura")
	@Test
	void testComprarBilleteUsuarioYaEnSistema() {
		List<Billete> salidaEsperadaTren = new ArrayList<>();
		List<Billete> salidaEsperadaBus = new ArrayList<>();
		salidaEsperadaTren.add(new Billete("ABC12345", differentRecorrido, user, ESTADO_COMPRADO));
		salidaEsperadaTren.add(new Billete("ABC12345", differentRecorrido, user, ESTADO_COMPRADO));
		salidaEsperadaBus.add(new Billete("ABC12346", recorrido, user, ESTADO_COMPRADO));
		List<Billete> salidaTren = system.comprarBilletes("ABC12345", user, differentRecorrido, 2);
		List<Billete> salidaBus = system.comprarBilletes("ABC12346", user, recorrido, 1);

		assertEquals(salidaEsperadaTren, salidaTren);
		assertEquals(salidaEsperadaBus, salidaBus);
	}

	/**
	 * FINDME Tests for {@link System#comprarBilletesReservados(String)}
	 */
	@Test
	void testComprarBilletesReservadosValidoLimiteInferior() {
		String locator = "1";
		system.addRecorrido(recorrido);
		system.reservarBilletes(locator, user, recorrido, 3);

		List<Billete> buyedTicketsCheck = new ArrayList<>();
		buyedTicketsCheck.add(new Billete(locator, recorrido, user, ESTADO_COMPRADO));
		buyedTicketsCheck.add(new Billete(locator, recorrido, user, ESTADO_COMPRADO));
		buyedTicketsCheck.add(new Billete(locator, recorrido, user, ESTADO_COMPRADO));
		List<Billete> buyedTickets = system.comprarBilletesReservados(locator);
		assertEquals(buyedTicketsCheck, buyedTickets);
	}

	@Test
	void testComprarBilletesReservadosValidoLimiteSuperior() {
		String locator = "12345678";
		system.addRecorrido(recorrido);
		system.reservarBilletes(locator, user, recorrido, 3);

		List<Billete> buyedTicketsCheck = new ArrayList<>();
		buyedTicketsCheck.add(new Billete(locator, recorrido, user, ESTADO_COMPRADO));
		buyedTicketsCheck.add(new Billete(locator, recorrido, user, ESTADO_COMPRADO));
		buyedTicketsCheck.add(new Billete(locator, recorrido, user, ESTADO_COMPRADO));
		List<Billete> buyedTickets = system.comprarBilletesReservados(locator);
		assertEquals(buyedTicketsCheck, buyedTickets);
	}

	@Test
	void testComprarBilletesReservadosConLocatorNull() {
		assertThrows(IllegalArgumentException.class, () -> {
			system.comprarBilletesReservados(null);
		});
	}

	@Test
	void testComprarBilletesReservadosLimiteInferior() {
		assertThrows(IllegalArgumentException.class, () -> {
			system.comprarBilletesReservados("");
		});
	}

	@Test
	void testComprarBilletesReservadosLimiteSuperior() {
		assertThrows(IllegalArgumentException.class, () -> {
			system.comprarBilletesReservados("123456789");
		});
	}

	@Test
	void testComprarBilletesReservadosConLocalizatorIncorrecto() {
		String locator = "12345678";
		system.addRecorrido(recorrido);
		assertThrows(IllegalStateException.class, () -> {
			system.comprarBilletesReservados(locator);
		});
	}

	@Test
	void testComprarBilletesReservadosConLocatorDeBilletesComprados() {
		String locator = "12345678";
		system.addRecorrido(recorrido);
		system.comprarBilletes(locator, user, recorrido, 3);
		assertThrows(IllegalStateException.class, () -> {
			system.comprarBilletesReservados(locator);
		});
	}

	@Test
	@Tag("Cobertura")
	void testComprarBilletesReservadosConAlMenosUnBilleteReservadoYOtroComprado() {
		String locator = "12345678";
		String locator2 = "87654321";
		system.addRecorrido(recorrido);
		system.reservarBilletes(locator, user, recorrido, 1);
		system.comprarBilletes(locator2, user, recorrido, 3);
		List<Billete> purchasedTickets = system.comprarBilletesReservados(locator);
		List<Billete> purchasedTicketsCheck = new ArrayList<>();
		purchasedTicketsCheck.add(new Billete(locator, recorrido, user, ESTADO_COMPRADO));
		assertEquals(purchasedTicketsCheck, purchasedTickets);
	}

	/**
	 * FINDME Tests for
	 * {@link System#reservarBilletes(String, Usuario, Recorrido, int)}
	 */
	@Test
	void testReservarBilletesExitosamente() {
		int plazasDisponibles = recorrido.getNumAvailableSeats();
		int numBilletesReservar = 6;
		system.addRecorrido(recorrido);
		// Realiza la reserva de los billetes
		List<Billete> reservado = system.reservarBilletes("ABC12345", user, recorrido, numBilletesReservar);

		assertEquals(plazasDisponibles - numBilletesReservar, recorrido.getNumAvailableSeats());

		ArrayList<Billete> listaBilletesComprobacion = new ArrayList<>();
		Billete billeteComprobacion = new Billete("ABC12345", recorrido, user, ESTADO_RESERVADO);
		for (int i = 0; i < numBilletesReservar; i++) {
			listaBilletesComprobacion.add(billeteComprobacion);
		}
		assertEquals(reservado, listaBilletesComprobacion);
	}

	@Test
	void testReservarBilleteLocalizadorInvalido() {
		assertThrows(IllegalArgumentException.class, () -> {
			system.reservarBilletes("", user, recorrido, 1);
		});
	}

	@Test
	void testReservarBilleteLocalizadorNull() {
		assertThrows(IllegalArgumentException.class, () -> {
			system.reservarBilletes(null, user, recorrido, 1);
		});
	}

	@Test
	void testReservarBilleteUsuarioNull() {
		assertThrows(IllegalArgumentException.class, () -> {
			system.reservarBilletes("ABC12345", null, recorrido, 1);
		});
	}

	@Test
	void testReservarBilleteRecorridoNull() {
		assertThrows(IllegalArgumentException.class, () -> {
			system.reservarBilletes("ABC12345", user, null, 1);
		});
	}

	@Test
	void testNoSePuedeReservarSiPlazasInsuficientes() {
		int plazasDisponibles = recorrido.getNumAvailableSeats();
		int numBilletesReservar = plazasDisponibles + 1;

		assertThrows(IllegalStateException.class, () -> {
			system.reservarBilletes("ABC12345", user, recorrido, numBilletesReservar);
		});

	}

	@Test
	void testNoSePuedeReservarSiPlazasMenoresQueLaMitad() {
		int numBilletesComprar = (recorrido.getTotalSeats() / 2) + 1;
		system.addRecorrido(recorrido);

//		 Intenta reservar más billetes de los disponibles
		system.comprarBilletes("ABC12345", user, recorrido, numBilletesComprar);

		assertThrows(IllegalStateException.class, () -> {
			system.reservarBilletes("ABC12345", user, recorrido, 2);
		});

	}

	/**
	 * FINDME Tests for {@link System#anularReserva(String, int)}
	 */
	@Test
	void testAnularReservaAumentaPlazasDisponiblesLimiteInferior() {
		int numBilletesReservar = 6;

		system.addRecorrido(recorrido);

		// Realiza la reserva de los billetes
		system.reservarBilletes("ABC12345", user, recorrido, numBilletesReservar);

		int plazasDisponiblesAntes = recorrido.getNumAvailableSeats();
		int numBilletesAnular = 1;

		// Realiza la anulación de la reserva
		system.anularReserva("ABC12345", numBilletesAnular);

		int plazasDisponiblesDespues = recorrido.getNumAvailableSeats();

		// Verifica que las plazas disponibles aumenten en la cantidad correcta
		assertEquals(plazasDisponiblesAntes + numBilletesAnular, plazasDisponiblesDespues);
	}

	@Test
	void testNoSePuedeAnularReservaSiLocalizadorNull() {
		assertThrows(IllegalArgumentException.class, () -> {
			system.anularReserva(null, 1);
		});
	}

	@Test
	void testNoSePuedeAnularReservaSiLocalizadorVacío() {
		assertThrows(IllegalArgumentException.class, () -> {
			system.anularReserva("", 1);
		});
	}

	@Test
	void testNoSePuedeAnularReservaSiFueraSistema() {
		int numBilletesAnular = 2;
		// Intenta anular la reserva sin reservar previamente
		assertThrows(IllegalStateException.class, () -> {
			system.anularReserva("ABC12345", numBilletesAnular);
		});
	}

	@Test
	void testNoSePuedeAnularReservaSiLocalizadorLimiteInferior() {
		assertThrows(IllegalArgumentException.class, () -> {
			system.anularReserva("ABCD12345", 0);
		});
	}

	@Test
	void testNoSePuedeAnularReservaSiLocalizadorPerteneceABilletesComprados() {
		String locator = "ABC12345";
		system.comprarBilletes(locator, user, recorrido, 3);
		assertThrows(IllegalStateException.class, () -> {
			system.anularReserva(locator, 2);
		});
	}

	@Test
	void testNoSePuedeAnularReservaSiMayorNumeroDeAnulacionesQueBilletesReservados() {
		String locator = "ABC12345";
		int numBilletesAnular = 3;
		system.addRecorrido(recorrido);
		system.reservarBilletes(locator, user, recorrido, 1);
		assertThrows(IllegalStateException.class, () -> {
			system.anularReserva(locator, numBilletesAnular);
		});
	}

	/**
	 * FINDME Tests for {@link System#devolverBilletes(String, int)}
	 */
	@Test
	void testDevolverBilletesAumentaPlazasDisponiblesLimiteInferior() {
		int numBilletesComprar = 6;

		// Realiza la reserva de los billetes
		system.comprarBilletes("ABC12345", user, recorrido, numBilletesComprar);
		int plazasDisponiblesAntes = recorrido.getNumAvailableSeats();
		int numBilletesDevolver = 1;

		// Realiza la anulación de la reserva
		system.devolverBilletes("ABC12345", numBilletesDevolver);

		int plazasDisponiblesDespues = recorrido.getNumAvailableSeats();

		// Verifica que las plazas disponibles aumenten en la cantidad correcta
		assertEquals(plazasDisponiblesAntes + numBilletesDevolver, plazasDisponiblesDespues);
	}

	@Test
	void testNoSePuedeDevolverBilletesSiLocalizadorNull() {
		assertThrows(IllegalArgumentException.class, () -> {
			system.devolverBilletes(null, 1);
		});
	}

	@Test
	void testNoSePuedeDevolverBilletesSiLocalizadorVacío() {
		assertThrows(IllegalArgumentException.class, () -> {
			system.devolverBilletes("", 1);
		});
	}

	@Test
	void testNoSePuedeDevolverBilletesSiLocalizadorLimiteInferior() {
		assertThrows(IllegalArgumentException.class, () -> {
			system.devolverBilletes("ABCD12345", 0);
		});
	}

	@Test
	void testNoSePuedeDevolverBilleteFueraSistema() {
		int numBilletesDevolver = 2;
		// Intenta anular la reserva sin reservar previamente
		assertThrows(IllegalStateException.class, () -> {
			system.devolverBilletes("ABC12345", numBilletesDevolver);
		});
	}

	@Test
	void testNoSePuedeDevolverBilleteSiReservados() {
		String locator = "ABC12345";
		int numBilletesDevolver = 2;
		system.addRecorrido(recorrido);
		system.reservarBilletes(locator, user, recorrido, 5);

		assertThrows(IllegalStateException.class, () -> {
			system.devolverBilletes(locator, numBilletesDevolver);
		});
	}

	@Test
	void testNoSePuedeDevolverBilletesConNumLimiteInferior() {
		String locator = "ABC12345";
		system.comprarBilletes(locator, user, recorrido, 1);
		assertThrows(IllegalArgumentException.class, () -> {
			system.devolverBilletes(locator, 0);
		});
	}

	@Test
	void testNoSePuedeDevolverBilletesSiMayorNumeroDeDevolucionesQueBilletesComprados() {
		String locator = "ABC12345";
		int numBilletesDevolver = 3;
		system.comprarBilletes(locator, user, recorrido, 1);
		assertThrows(IllegalStateException.class, () -> {
			system.devolverBilletes(locator, numBilletesDevolver);
		});
	}

	@Test
	@Tag("Cobertura")
	void testLocalizadorYaUtilizadoReserva() {
		String localizador = "ABC12345";
		system.addRecorrido(recorrido);
		system.reservarBilletes(localizador, user, recorrido, 1);
		assertThrows(IllegalStateException.class, () -> {
			system.reservarBilletes(localizador, user, recorrido, 1);
		});
	}

	@Test
	@Tag("Cobertura")
	void testRecorridoNoExisteReserva() {
		String localizador = "ABC12345";
		assertThrows(IllegalStateException.class, () -> {
			system.reservarBilletes(localizador, user, recorrido, 1);
		});
	}

	@Test
	@Tag("Cobertura")
	void testComprobarRamasLocalizadorAnularReserva() {
		String localizadorA = "ABC12345";
		String localizadorB = "BCA12345";
		int numBilletesReservar = 3;
		int numBilletesAnular = 2;
		system.addRecorrido(recorrido);
		system.reservarBilletes(localizadorA, user, recorrido, numBilletesReservar);
		system.reservarBilletes(localizadorB, user, recorrido, numBilletesReservar);
		
		system.anularReserva(localizadorA, numBilletesAnular);
	}
	
	@Test
	@Tag("Cobertura")
	void testComprobarRamasLocalizadorDevolverBilletes() {
		String localizadorA = "ABC12345";
		String localizadorB = "BCA12345";
		int numBilletesComprar = 3;
		int numBilletesDevolver = 2;
		system.addRecorrido(recorrido);
		system.comprarBilletes(localizadorA, user, recorrido, numBilletesComprar);
		system.comprarBilletes(localizadorB, user, recorrido, numBilletesDevolver);
		
		system.devolverBilletes(localizadorA, numBilletesDevolver);
	}

}
