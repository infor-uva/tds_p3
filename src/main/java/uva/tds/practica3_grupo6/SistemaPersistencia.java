package uva.tds.practica3_grupo6;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Class dedicated for the management of the different instances of
 * {@link Recorrido}, {@link Billete} and {@link Usuario} based in external
 * database.
 * 
 * The management will be based on:
 * <ul>
 * <li>Add a Recorrido in to the system<br>
 * {@link SistemaPersistencia#addRecorrido(Recorrido)}</li>
 * <li>Remove a Recorrido of system<br>
 * {@link SistemaPersistencia#removeRecorrido(String)}</li>
 * <li>Consult the list of recorridos in system<br>
 * {@link SistemaPersistencia#getRecorridos()}</li> Consult the total price
 * accumulated by a user<br>
 * {@link SistemaPersistencia#getPrecioTotalBilletesUsuario(String)}</li>
 * <li>Consult the list of Recorridos that have a specific date<br>
 * {@link SistemaPersistencia#getRecorridosDisponiblesFecha(LocalDate)}</li>
 * <li>Consult the list of Billetes which are associated to the recorrido
 * route<br>
 * {@link SistemaPersistencia#getAssociatedBilletesToRoute(String)}</li>
 * <li>Consult the date of the route which have the id specified<br>
 * {@link SistemaPersistencia#getDateOfRecorrido(String)}</li>
 * <li>Consult the time of the route which have the id specified<br>
 * {@link SistemaPersistencia#getTimeOfRecorrido(String)}</li>
 * <li>Consult the date and time of the route which have the id specified<br>
 * {@link SistemaPersistencia#getDateTimeOfRecorrido(String)}</li>
 * <li>Update the date of a recorrido<br>
 * {@link SistemaPersistencia#updateRecorridoDate(String, LocalDate)}</li>
 * <li>Update the time of a recorrido<br>
 * {@link SistemaPersistencia#updateRecorridoTime(String, LocalTime)}</li>
 * <li>Update the time and date of a recorrido<br>
 * {@link SistemaPersistencia#updateRecorrido(String, LocalDate, LocalTime)}</li>
 * <li>Reserve tickets for a Recorrido and a Usuario<br>
 * {@link SistemaPersistencia#reservarBilletes(String, Usuario, Recorrido, int)}</li>
 * <li>Cancel a reservation<br>
 * {@link SistemaPersistencia#anularReserva(String, int)}</li>
 * <li>Return the tickets<br>
 * {@link SistemaPersistencia#devolverBilletes(String, int)}</li>
 * <li>Buy tickets for a Recorrido and a Usuario<br>
 * {@link SistemaPersistencia#comprarBilletes(String, Usuario, Recorrido, int)}</li>
 * <li>Buy reserved tickets (the previously reserved lot)<br>
 * {@link SistemaPersistencia#comprarBilletesReservados(String)}</li>
 * </ul>
 * 
 * @author hugcubi
 * @author diebomb
 * @author migudel
 * 
 * @version 28/11/23
 */
public class SistemaPersistencia {

	/**
	 * List of the character indexed by the rest resulted of the division of nif and 23
	 */
	private final List<Character> letrasNif=new ArrayList<>(Arrays.asList('T','R','W','A','G','M','Y','F','P','D','X','B','N','J','Z','S','Q','V','H','L','C','K','E'));
	/**
	 * {@link Recorrido#TRAIN}
	 */
	private static final TransportType TRAIN = TransportType.TRAIN;
	/**
	 * {@link Billete#ESTADO_RESERVADO}
	 */
	private static final String ESTADO_RESERVADO = Billete.ESTADO_RESERVADO;
	/**
	 * {@link Billete#ESTADO_COMPRADO}
	 */
	private static final String ESTADO_COMPRADO = Billete.ESTADO_COMPRADO;
	
	/**
	 * External dabatabe manager
	 */
	private IDatabaseManager database;
	
	/**
	 * Lista de Excepciones
	 */
	
	private static final String EXCEPTION_NOT_ROUTE_ID = "the id's route isn't in the system\n";
	private static final String LOCALIZADOR_NULL = "El localizador no puede ser null\n";
	private static final String LOCALIZADOR_VACIO = "El localizador no puede ser vacio\n";

	/**
	 * Instance the System
	 */
	public SistemaPersistencia(IDatabaseManager database) {
		this.database = database;
	}

	/**
	 * Consult the database manager assigned to the system
	 * 
	 * @return databasemanager
	 */
	public IDatabaseManager getDataBaseManager() {
		return database;
	}

	/**
	 * Add a route in to the system
	 * 
	 * @param route to add
	 * 
	 * @throws IllegalArgumentException if route is null
	 * @throws IllegalStateException    if route is already in the system
	 */
	public void addRecorrido(Recorrido route) {
		try {
			database.addRecorrido(route);
		} catch (IllegalArgumentException e1) {
			throw e1;
		} catch (IllegalStateException e2) {
			throw e2;
		}
	}

	/**
	 * Check of the id is not null and have at less one character different of
	 * spaces
	 * 
	 * @param id
	 * 
	 * @throws IllegalArgumentException if id is null
	 * @throws IllegalArgumentException if id is empty
	 */
	private void checkID(String id) {
		if (id == null)
			throw new IllegalArgumentException("is is null");
		if (id.isBlank())
			throw new IllegalArgumentException("is is empty");
	}

	/**
	 * Remove a route of system
	 * 
	 * @param id of the route
	 * 
	 * @throws IllegalArgumentException if the id is null
	 * @throws IllegalArgumentException if id is empty
	 * @throws IllegalStateException    if id's route isn't in the system
	 * @throws IllegalStateException    if route has associated tickets
	 */
	public void removeRecorrido(String id) {
		checkID(id);
		List<Billete> tmp;
		try {
			tmp = getAssociatedBilletesToRoute(id);
		} catch (IllegalStateException e2) {
			throw e2;
		}
		if (!tmp.isEmpty())
			throw new IllegalStateException("the route has associated tickets");
		database.eliminarRecorrido(id);
	}

	/**
	 * Consult the list of routes in system
	 * 
	 * @return list of routes in system
	 */
	public List<Recorrido> getRecorridos() {
		return database.getRecorridos();
	}

	/**
	 * Returns the price of all the tickets associated with a user, if the tickets
	 * are valid. In addition, it should be checked whether the journey is by train
	 * or bus. If it is by train, the price of each ticket should have a 10%
	 * discount.
	 * 
	 * @param nif
	 * 
	 * @return total price
	 *
	 * @throws IllegalArgumentException if the NIF is null.
	 * @throws IllegalArgumentException if the nif is empty
	 * 
	 * @throws IllegalArgumentException if the number of NIF digits exceeds 8
	 * @throws IllegalArgumentException if the number of NIF digits is less than 8
	 * @throws IllegalArgumentException if the NIF does not end with a letter,
	 *                                  except {I,Ñ,O,U}
	 * @throws IllegalArgumentException if the NIF value does not correspond to the
	 *                                  letter
	 * @throws IllegalStateException    if the nif holder isn't in the system.
	 * @throws IllegalStateException    if the nif holder not have associated
	 *                                  tickets.
	 */
	public double getPrecioTotalBilletesUsuario(String nif) {
		if (nif == null)
			throw new IllegalArgumentException("El nif es nulo\n");
		if (nif.isEmpty())
			throw new IllegalArgumentException("El nif esta vacio\n");
		if (nif.length()<9)
			throw new IllegalArgumentException("El nif es demasiado corto\n");
		if (nif.length()>9)
			throw new IllegalArgumentException("El nif es demasiado largo\n");
		if (nif.charAt(8) == 'I' || nif.charAt(8) == 'Ñ' || nif.charAt(8) == 'O' || nif.charAt(8) == 'U')
			throw new IllegalArgumentException("El nif contiene una letra incorrecta\n");
		String cifras=nif.substring(0, nif.length()-1);
		char letra=nif.charAt(8);
		int numero=Integer.parseInt(cifras);
		int resto=numero%23;
		if(resto != letrasNif.indexOf(letra))
			throw new IllegalArgumentException("La letra del nif no corresponde con las cifras del nif\n");
		
		if(database.getUsuario(nif)==null)
			throw new IllegalArgumentException("El usuario no esta en el sistema\n");
		ArrayList<Billete> tikets = database.getBilletesDeUsuario(nif);
		double salida=0;
		for (Billete tiket : tikets) {
			double price=tiket.getRecorrido().getPrice();
			if (tiket.getRecorrido() instanceof TrainRecorrido)
				salida+=(price*0.9);
			else
				salida+=price;
		}
		return salida;
	}

	/**
	 * Returns a list of available journeys for that date, both for buses and
	 * trains.
	 *
	 * @param fecha
	 * @return list of routes or an empty list if there are no routes for that date.
	 *
	 * @throws IllegalArgumentException if the date is null.
	 * @throws IllegalStateException    if the date does not have associated route.
	 */
	public List<Recorrido> getRecorridosDisponiblesFecha(LocalDate fecha) {
		if(fecha == null)
			throw new IllegalArgumentException("La fecha es nula\n");
		if (database.getRecorridos(fecha) == null)
			throw new IllegalStateException("Para la fecha no hay recorridos disponibles\n");
		return database.getRecorridos(fecha);
	}

	/**
	 * Consult the list of tickets which are associated to the route
	 * 
	 * @param id of the route
	 * 
	 * @return list of tickets
	 * 
	 * @throws IllegalArgumentException if the id is null
	 * @throws IllegalArgumentException if id is empty
	 * @throws IllegalStateException    if id's route isn't in the system
	 */
	public List<Billete> getAssociatedBilletesToRoute(String id) {
		checkID(id);
		if (database.getRecorrido(id) == null)
			throw new IllegalStateException("the route isn't in the system");
		return database.getBilletesDeRecorrido(id);
	}

	/**
	 * Consult the date of the route which have the id specified
	 * 
	 * @param id of the route
	 * 
	 * @return the date of the route
	 * 
	 * @throws IllegalArgumentException if id is null
	 * @throws IllegalArgumentException if id have less than 1 character
	 * @throws IllegalStateException    if there is no route in the system with that
	 *                                  id
	 */
	public LocalDate getDateOfRecorrido(String id) {
		checkID(id);
		Recorrido route;
		if ((route = database.getRecorrido(id)) == null)
			throw new IllegalStateException(EXCEPTION_NOT_ROUTE_ID);
		return route.getDate();
	}

	/**
	 * Consult the time of the route which have the id specified
	 * 
	 * @param id of the route
	 * 
	 * @return the time of the route
	 * 
	 * @throws IllegalArgumentException if id is null
	 * @throws IllegalArgumentException if id have less than 1 character
	 * @throws IllegalStateException    if there is no route in the system with that
	 *                                  id
	 */
	public LocalTime getTimeOfRecorrido(String id) {
		checkID(id);
		Recorrido route;
		if ((route = database.getRecorrido(id)) == null)
			throw new IllegalStateException(EXCEPTION_NOT_ROUTE_ID);
		return route.getTime();
	}

	/**
	 * Consult the date and time of the route which have the id specified
	 * 
	 * @param id of the route
	 * 
	 * @return the DateTime of the route
	 * 
	 * @throws IllegalArgumentException if id is null
	 * @throws IllegalArgumentException if id have less than 1 character
	 * @throws IllegalStateException    if there is no route in the system with that
	 *                                  id
	 */
	public LocalDateTime getDateTimeOfRecorrido(String id) {
		checkID(id);
		Recorrido route;
		if ((route = database.getRecorrido(id)) == null)
			throw new IllegalStateException(EXCEPTION_NOT_ROUTE_ID);
		return route.getDateTime();
	}

	/**
	 * Update the date of a route
	 * 
	 * @param id of the route
	 * @param newDate
	 * 
	 * @throws IllegalArgumentException if id is null
	 * @throws IllegalArgumentException if id is empty
	 * @throws IllegalStateException    if id's route isn't in the system
	 * @throws IllegalArgumentException if newDate is null
	 * @throws IllegalStateException    if the new date is the already the set
	 */
	public void updateRecorridoDate(String id, LocalDate newDate) {
		checkID(id);
		Recorrido route;
		if ((route = database.getRecorrido(id)) == null)
			throw new IllegalStateException(EXCEPTION_NOT_ROUTE_ID);
		try {
			route.updateDate(newDate);
		} catch (IllegalArgumentException e1) {
			throw e1;
		} catch (IllegalStateException e2) {
			throw e2;
		}
	}

	/**
	 * Update the time of a route
	 * 
	 * @param id of the route
	 * @param newTime
	 * 
	 * @throws IllegalArgumentException if id is null
	 * @throws IllegalArgumentException if id is empty
	 * @throws IllegalStateException    if id's route isn't in the system
	 * @throws IllegalArgumentException if newTime is null
	 * @throws IllegalStateException    if the new time is the already the set
	 */
	public void updateRecorridoTime(String id, LocalTime newTime) {
		checkID(id);
		Recorrido route;
		if ((route = database.getRecorrido(id)) == null)
			throw new IllegalStateException(EXCEPTION_NOT_ROUTE_ID);
		try {
			route.updateTime(newTime);
		} catch (IllegalArgumentException e1) {
			throw e1;
		} catch (IllegalStateException e2) {
			throw e2;
		}
	}

	/**
	 * Update the time and date of a route
	 * 
	 * @param id of the route
	 * @param newDateTime
	 * 
	 * @throws IllegalArgumentException if id is null
	 * @throws IllegalArgumentException if id is empty
	 * @throws IllegalStateException    if id's route isn't in the system
	 * @throws IllegalArgumentException if newDateTime is null
	 * @throws IllegalStateException    if the new Date time is the already the set
	 */
	public void updateRecorridoDateTime(String id, LocalDateTime newDateTime) {
		checkID(id);
		Recorrido route;
		if ((route = database.getRecorrido(id)) == null)
			throw new IllegalStateException(EXCEPTION_NOT_ROUTE_ID);
		try {
			route.updateDateTime(newDateTime);
		} catch (IllegalArgumentException e1) {
			throw e1;
		} catch (IllegalStateException e2) {
			throw e2;
		}
	}

	/**
	 * Update the time and date of a route
	 * 
	 * @param id of the route
	 * @param newDate
	 * @param newTime
	 * 
	 * @throws IllegalArgumentException if id is null
	 * @throws IllegalArgumentException if id is empty
	 * @throws IllegalStateException    if id's route isn't in the system
	 * @throws IllegalArgumentException if newDate is null
	 * @throws IllegalArgumentException if newTime is null
	 * @throws IllegalStateException    if the new Date time is the already the set
	 */
	public void updateRecorrido(String id, LocalDate newDate, LocalTime newTime) {
		checkID(id);
		Recorrido route;
		if ((route = database.getRecorrido(id)) == null)
			throw new IllegalStateException(EXCEPTION_NOT_ROUTE_ID);
		try {
			route.updateDateTime(newDate, newTime);
		} catch (IllegalArgumentException e1) {
			throw e1;
		} catch (IllegalStateException e2) {
			throw e2;
		}
	}

	/**
	 * Reserva un número de billetes para un recorrido
	 *
	 * @param localizador
	 * @param user
	 * @param recorrido
	 * @param numBilletesReservar
	 * 
	 * @return lot of reserved tickets
	 * 
	 * @throws IllegalArgumentException if the number of tickets exceeds the
	 *                                  available seat limit.
	 * @throws IllegalArgumentException if the number of available tickets is less
	 *                                  than half of the total.
	 * @throws IllegalArgumentException if the identifier is empty.
	 * @throws IllegalArgumentException if a locator that has been used previously
	 *                                  is passed.
	 * @throws IllegalArgumentException if user is null.
	 * @throws IllegalArgumentException if recorrido is null.
	 * @throws IllegalArgumentException if recorrido is null.
	 */
	public List<Billete> reservarBilletes(String localizador, Usuario user, Recorrido recorrido, int numBilletesReservar) {
		if(user == null)
			throw new IllegalArgumentException("El usuario no puede ser null");
		if(recorrido == null)
			throw new IllegalArgumentException("El recorrido no puede ser null");
		if(localizador == null)
			throw new IllegalArgumentException(LOCALIZADOR_NULL);
		if (numBilletesReservar > recorrido.getNumAvailableSeats())
			throw new IllegalStateException("No se puede reservar si el número de billetes es mayor a los asientos disponibles");
		if (recorrido.getNumAvailableSeats() < recorrido.getTotalSeats()/2)
			throw new IllegalStateException("No se puede reservar si el número de asientos disponibles es menor a la mitad del número total de asientos");
		if (localizador.equals(""))
			throw new IllegalArgumentException(LOCALIZADOR_VACIO);
		if (!database.getBilletes(localizador).isEmpty()) {
			throw new IllegalStateException("El localizador ya ha sido utilizado");
		}

		List<Billete> billetes = new ArrayList<>();
		for (int i = 0; i < numBilletesReservar; i++) {
			Billete ticket = new Billete(localizador, recorrido, user, ESTADO_RESERVADO);
			billetes.add(ticket);
			database.addBillete(ticket);
		}
		recorrido.decreaseAvailableSeats(numBilletesReservar);
		database.actualizarRecorrido(recorrido);
		return billetes;

	}

	/**
	 * Cancel the reservation of a specific number of reserved tickets identified by
	 * the locator
	 * 
	 * @param localizador
	 * @param numBilletesAnular
	 * 
	 * @throws IllegalArgumentException if the localizador is null
	 * @throws IllegalArgumentException if the localizador is empty
	 * @throws IllegalStateException    if the locator isn't in the system
	 * @throws IllegalStateException    if the locator belongs to tickets that are
	 *                                  not reserved.
	 * @throws IllegalArgumentException if the number of tickets ir less or equal to
	 *                                  0
	 * @throws IllegalStateException    if the number of tickets is more than the
	 *                                  number of reserved tickets with that locator
	 */
	public void anularReserva(String localizador, int numBilletesAnular) {
		if(localizador == null)
			throw new IllegalArgumentException(LOCALIZADOR_NULL);
		if (localizador.equals(""))
			throw new IllegalArgumentException(LOCALIZADOR_VACIO);
		if (numBilletesAnular < 1)
			throw new IllegalArgumentException("No se puede reservar si el número de billetes es menor que 1");	
		
		ArrayList<Billete> billetes = database.getBilletes(localizador);
		if(billetes.isEmpty() || 
		!billetes.get(0).getEstado().equals(ESTADO_RESERVADO)) {
			throw new IllegalStateException("No hay tickets reservados con ese localizador");
		}
		
		if(billetes.size() < numBilletesAnular) {
			throw new IllegalStateException("Hay menos tickets de los que se quieren anular con ese localizador");
		}
		
		Recorrido recorrido = billetes.get(0).getRecorrido();
		int billetesRestantes = billetes.size() - numBilletesAnular;
		database.eliminarBilletes(localizador);
		if(billetesRestantes > 0) {
			for (int i = 0; i < billetesRestantes; i++) {
				database.addBillete(billetes.get(0));
			}
		}
		recorrido.increaseAvailableSeats(numBilletesAnular);
		database.actualizarRecorrido(recorrido);

	}

	/**
	 * Returns a specific number of tickets belonging to a lot identified by the
	 * locator
	 * 
	 * @param localizador         of the lot or tickets (it's the same)
	 * @param numBilletesDevolver
	 * 
	 * @throws IllegalArgumentException if the localizador is null
	 * @throws IllegalArgumentException if the localizador is empty
	 * @throws IllegalStateException    if the locator isn't in the system
	 * @throws IllegalStateException    if the tickets to be returned have not been
	 *                                  purchased comprados
	 * @throws IllegalArgumentException if the number of tickets ir less or equal to
	 *                                  0
	 * @throws IllegalStateException    if the number of tickets is more than the
	 *                                  number of tickets with that locator
	 */
	public void devolverBilletes(String localizador, int numBilletesDevolver) {
		if(localizador == null)
			throw new IllegalArgumentException(LOCALIZADOR_NULL);
		if (localizador.equals(""))
			throw new IllegalArgumentException(LOCALIZADOR_VACIO);
		if (numBilletesDevolver < 1)
			throw new IllegalArgumentException("No se puede devolver si el número de billetes es menor que 1");	
		
		ArrayList<Billete> billetes = database.getBilletes(localizador);
		if(billetes.isEmpty() || !billetes.get(0).getEstado().equals(ESTADO_COMPRADO)) {
			throw new IllegalStateException("No hay tickets comprados con ese localizador");
		}
		
		if(billetes.size() < numBilletesDevolver) {
			throw new IllegalStateException("Hay menos tickets de los que se quieren devolver con ese localizador");
		}
		
		int billetesRestantes = billetes.size() - numBilletesDevolver;
		database.eliminarBilletes(localizador);

			Billete b = billetes.get(0);
			Recorrido r = b.getRecorrido();
			r.increaseAvailableSeats(numBilletesDevolver);
			database.actualizarRecorrido(r);
			if(billetesRestantes > 0) {
			for (int i = 0; i < billetesRestantes; i++) {
				database.addBillete(b);
			}
			

		}


	}

	/**
	 * Buy tickets for a trip.
	 *
	 * @param localizador the locator of the tickets
	 * @param usr         the user who is buying the tickets
	 * @param recorrido   the trip for which the tickets are being bought
	 * @param numBilletes the number of tickets to buy
	 * 
	 * @return lot of tickets bought
	 * 
	 * @throws IllegalArgumentException if the localizador is null
	 * @throws IllegalArgumentException if the localizador is empty
	 * @throws IllegalArgumentException if the locator has already been used in
	 *                                  another ticket, either on the same route or
	 *                                  another route
	 * @throws IllegalArgumentException if the usr is null
	 * @throws IllegalArgumentException if the recorrido is null
	 * @throws IllegalArgumentException if the number of tickets is greater or less
	 *                                  than the limit of available seats per
	 *                                  vehicle
	 * @throws IllegalStateException    if the number of tickets is greater or less
	 *                                  than the limit of free seats
	 * @throws IllegalArgumentException if a previously used locator is passed
	 */
	public List<Billete> comprarBilletes(String localizador, Usuario usr, Recorrido recorrido, int numBilletes) {
		if(localizador==null)
			throw new IllegalArgumentException(LOCALIZADOR_NULL);
		if (usr == null)
			throw new IllegalArgumentException("El usuario es null\n");
		if (recorrido == null)
			throw new IllegalArgumentException("El recorrido es null\n");
		if (numBilletes<1)
			throw new IllegalArgumentException("El numero de billetes es inferior al minimo\n");
		if (numBilletes > recorrido.getNumAvailableSeats())
			throw new IllegalStateException("El numero de billetes es superior a las plazas disponibles\n");
		if (localizador.isEmpty())
			throw new IllegalArgumentException("EL localizador esta vacio\n");
		if (!database.getBilletes(localizador).isEmpty())
			throw new IllegalArgumentException("El localizador ya ha sido usado\n");
		List<Billete> returned=new ArrayList<>();
		if(database.getUsuario(usr.getNif())==null) {
			database.addUsuario(usr);
		}
		for(int i=0;i<numBilletes;i++) {
			java.lang.System.out.println(i);
			Billete tiket=new Billete(localizador,recorrido, usr, ESTADO_COMPRADO);
			returned.add(tiket);
			database.addBillete(tiket);
		}
		//recorrido.decreaseAvailableSeats(numBilletes);
		database.actualizarRecorrido(recorrido);
		return returned;
	}

	/**
	 * Buy reserved tickets (the previously reserved lot)
	 * 
	 * @param locator of the reserved lot of tickets
	 *
	 * @return lot of tickets bought
	 * 
	 * @throws IllegalArgumentException if the locator is null
	 * @throws IllegalArgumentException if locator is less than 1 character or more
	 *                                  than 8 characters
	 * @throws IllegalStateException    if there are no tickets booked in the system
	 *                                  with that locator
	 */
	public List<Billete> comprarBilletesReservados(String locator) {
		if (locator == null)
			throw new IllegalArgumentException(LOCALIZADOR_NULL);
		if (locator.isBlank() || locator.length() > 8)
			throw new IllegalArgumentException("locator must be between 1 and 8 characters longs");

		List<Billete> tickets;
		tickets = database.getBilletes(locator);
		if (tickets.isEmpty())
			throw new IllegalStateException("the is no tickets for this locator: " + locator);
		for (Billete ticket : tickets) {
			try {
				ticket.setComprado();
			} catch (IllegalStateException e) {
				// Reescritura de mensaje de error
				throw new IllegalStateException("the are no tickets booked with that locator");
			}
		}
		database.actualizarBilletes(tickets.get(0));
		return tickets;
	}
}