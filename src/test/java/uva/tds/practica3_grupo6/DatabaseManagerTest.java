package uva.tds.practica3_grupo6;

import static org.junit.jupiter.api.Assertions.*;
import org.easymock.TestSubject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;


class DatabaseManagerTest {

	@Test
	void test() {
		fail("Not yet implemented");
	}
	
	import static org.junit.jupiter.api.Assertions.*;

	import java.time.LocalDate;
	import java.util.ArrayList;

	import org.junit.jupiter.api.AfterEach;
	import org.junit.jupiter.api.BeforeEach;
	import org.junit.jupiter.api.Test;

public class DatabaseManagerTest {

	    private DatabaseManager databaseManager;

	    @BeforeEach
	    void setUp() {
	        databaseManager = new DatabaseManager();
	    }

	    @AfterEach
	    void tearDown() {
	        databaseManager.clearDatabase();
	    }

	    @Test
	    void testAddRecorrido() {
	        Recorrido recorrido = new BusRecorrido();
	        recorrido.setID("123");

	        assertNull(databaseManager.getRecorrido("123"));

	        databaseManager.addRecorrido(recorrido);

	        assertNotNull(databaseManager.getRecorrido("123"));
	    }

	    @Test
	    void testEliminarRecorrido() {
	        Recorrido recorrido = new BusRecorrido();
	        recorrido.setID("123");

	        databaseManager.addRecorrido(recorrido);

	        assertNotNull(databaseManager.getRecorrido("123"));

	        databaseManager.eliminarRecorrido("123");

	        assertNull(databaseManager.getRecorrido("123"));
	    }

	    @Test
	    void testActualizarRecorrido() {
	        Recorrido recorrido = new BusRecorrido();
	        recorrido.setID("123");

	        databaseManager.addRecorrido(recorrido);

	        recorrido.setFecha(LocalDate.now());

	        databaseManager.actualizarRecorrido(recorrido);

	        Recorrido recorridoActualizado = databaseManager.getRecorrido("123");

	        assertEquals(LocalDate.now(), recorridoActualizado.getFecha());
	    }

	    @Test
	    void testGetRecorrido() {
	        Recorrido recorrido = new BusRecorrido();
	        recorrido.setID("123");

	        databaseManager.addRecorrido(recorrido);

	        Recorrido recorridoObtenido = databaseManager.getRecorrido("123");

	        assertEquals(recorrido, recorridoObtenido);
	    }

	    @Test
	    void testGetRecorridos() {
	        databaseManager.addRecorrido(new BusRecorrido("001", LocalDate.now()));
	        databaseManager.addRecorrido(new TrainRecorrido("002", LocalDate.now()));

	        ArrayList<Recorrido> recorridos = databaseManager.getRecorridos();

	        assertEquals(2, recorridos.size());
	    }

	    @Test
	    void testAddUsuario() {
	        Usuario usuario = new Usuario();
	        usuario.setNif("12345678A");

	        assertNull(databaseManager.getUsuario("12345678A"));

	        databaseManager.addUsuario(usuario);

	        assertNotNull(databaseManager.getUsuario("12345678A"));
	    }

	    @Test
	    void testEliminarUsuario() {
	        Usuario usuario = new Usuario();
	        usuario.setNif("12345678A");

	        databaseManager.addUsuario(usuario);

	        assertNotNull(databaseManager.getUsuario("12345678A"));

	        databaseManager.eliminarUsuario("12345678A");

	        assertNull(databaseManager.getUsuario("12345678A"));
	    }

	    @Test
	    void testActualizarUsuario() {
	        Usuario usuario = new Usuario();
	        usuario.setNif("12345678A");

	        databaseManager.addUsuario(usuario);

	        usuario.setNombre("NuevoNombre");

	        databaseManager.actualizarUsuario(usuario);

	        Usuario usuarioActualizado = databaseManager.getUsuario("12345678A");

	        assertEquals("NuevoNombre", usuarioActualizado.getNombre());
	    }

	    @Test
	    void testGetUsuario() {
	        Usuario usuario = new Usuario();
	        usuario.setNif("12345678A");

	        databaseManager.addUsuario(usuario);

	        Usuario usuarioObtenido = databaseManager.getUsuario("12345678A");

	        assertEquals(usuario, usuarioObtenido);
	    }

	    @Test
	    void testAddBillete() {
	        Billete billete = new Billete();
	        billete.setLocalizador("ABC123");

	        assertNull(databaseManager.getBilletes("ABC123"));

	        databaseManager.addBillete(billete);

	        assertNotNull(databaseManager.getBilletes("ABC123"));
	    }

	    @Test
	    void testEliminarBilletes() {
	        Billete billete = new Billete();
	        billete.setLocalizador("ABC123");

	        databaseManager.addBillete(billete);

	        assertNotNull(databaseManager.getBilletes("ABC123"));

	        databaseManager.eliminarBilletes("ABC123");

	        assertEquals(0, databaseManager.getBilletes("ABC123").size());
	    }

	    @Test
	    void testActualizarBilletes() {
	        Billete billete = new Billete();
	        billete.setLocalizador("ABC123");

	        databaseManager.addBillete(billete);

	        billete.setPrecio(50.0);

	        databaseManager.actualizarBilletes(billete);

	        Billete billeteActualizado = databaseManager.getBilletes("ABC123").get(0);

	        assertEquals(50.0, billeteActualizado.getPrecio());
	    }

	    @Test
	    void testGetBilletes() {
	        Billete billete1 = new Billete();
	        billete1.setLocalizador("ABC123");
	        Billete billete2 = new Billete();
	        billete2.setLocalizador("XYZ789");

	        databaseManager.addBillete(billete1);
	        databaseManager.addBillete(billete2);

	        ArrayList<Billete> billetes = databaseManager.getBilletes("ABC123");

	        assertEquals(1, billetes.size());
	        assertEquals(billete1, billetes.get(0));
	    }

	    @Test
	    void testGetBilletesDeRecorrido() {
	        Billete billete1 = new Billete();
	        billete1.setLocalizador("ABC123");
	        billete1.setRecorrido(new BusRecorrido("001", LocalDate.now()));

	        Billete billete2 = new Billete();
	        billete2.setLocalizador("XYZ789");
	        billete2.setRecorrido(new TrainRecorrido("002", LocalDate.now()));

	        databaseManager.addBillete(billete1);
	        databaseManager.addBillete(billete2);

	        ArrayList<Billete> billetes = databaseManager.getBilletesDeRecorrido("001");

	        assertEquals(1, billetes.size());
	        assertEquals(billete1, billetes.get(0));
	    }

	    @Test
	    void testGetBilletesDeUsuario() {
	        Billete billete1 = new Billete();
	        billete1.setLocalizador("ABC123");
	        billete1.setUsuario(new Usuario("12345678A"));

	        Billete billete2 = new Billete();
	        billete2.setLocalizador("XYZ789");
	        billete2.setUsuario(new Usuario("87654321B"));

	        databaseManager.addBillete(billete1);
	        databaseManager.addBillete(billete2);

	        //ArrayList<Billete> billetes =
	    }


}
