package uva.tds.practica3_grupo6;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Class dedicated for the management of the different instances of
 * {@link Recorrido}, {@link Billete} and {@link Usuario}.
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
public class System {

	/**
	 * List of the character indexed by the rest resulted of the division of nif and
	 * 23
	 */
	private final List<Character> letrasNif = new ArrayList<>(Arrays.asList('T', 'R', 'W', 'A', 'G', 'M', 'Y', 'F', 'P',
			'D', 'X', 'B', 'N', 'J', 'Z', 'S', 'Q', 'V', 'H', 'L', 'C', 'K', 'E'));
	/**
	 * {@link Recorrido#TRAIN}
	 */
	private static final Transport TRAIN = Transport.TRAIN;
	/**
	 * {@link Billete#ESTADO_RESERVADO}
	 */
	private static final String ESTADO_RESERVADO = Billete.ESTADO_RESERVADO;
	/**
	 * {@link Billete#ESTADO_COMPRADO}
	 */
	private static final String ESTADO_COMPRADO = Billete.ESTADO_COMPRADO;

	/**
	 * List of tickets registred in the system
	 */
	private List<Billete> tickets;
	/**
	 * List of nifs of users registered in the system
	 */
	private List<String> users;

	/**
	 * List of routes registered in the system
	 */
	private List<Recorrido> routes;

	/**
	 * Instance the System
	 */
	public System() {
		tickets = new ArrayList<>();
		users = new ArrayList<>();
		routes = new ArrayList<>();
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
		if (route == null)
			throw new IllegalArgumentException("route is null");
		if (getRecorrido(route.getID()) != null)
			throw new IllegalStateException("route is already in the system");
		routes.add(route);
	}

	/**
	 * Remove a route of system
	 * 
	 * @param id of the route
	 * 
	 * @throws IllegalArgumentException if the id is null
	 * @throws IllegalArgumentException if the id is empty
	 * @throws IllegalStateException    if id's route isn't in the system
	 * @throws IllegalStateException    if route has associated tickets
	 */
	public void removeRecorrido(String id) {
		Recorrido route;
		if ((route = getRecorrido(id)) == null)
			throw new IllegalStateException("the route isn't in the system");
		if (getAssociatedBilletesToRoute(id).size() != 0)
			throw new IllegalStateException("the route has associated tickets");
		routes.remove(route);

	}

	/**
	 * Consult the id's route
	 * 
	 * @param id of the route
	 * 
	 * @return id's route or null if the route isn't in the system
	 * 
	 * @throws IllegalArgumentException if the id is null
	 * @throws IllegalArgumentException if the id is empty
	 */
	private Recorrido getRecorrido(String id) {
		if (id == null)
			throw new IllegalArgumentException("id is null");
		if (id.isEmpty())
			throw new IllegalArgumentException("id is empty");
		for (Recorrido route : routes) {
			if (route.getID().equals(id))
				return route;
		}
		return null;
	}

	/**
	 * Consult the list of routes in system
	 * 
	 * @return list of routes in system
	 */
	public List<Recorrido> getRecorridos() {
		return routes;
	}

	/**
	 * Consult the list of tickets in system
	 * 
	 * @return list of tickets in system
	 */
	public List<Billete> getBilletes() {
		return tickets;
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
		if (nif.length() > 9)
			throw new IllegalArgumentException("Nif demasiado largo\n");
		if (nif.length() <= 8)
			throw new IllegalArgumentException("Nif demasiado corto\n");
		if (!Character.isLetter(nif.charAt(8)))
			throw new IllegalArgumentException("Nif no contiene la letra\n");
		if (nif.charAt(8) == 'U' || nif.charAt(8) == 'I' || nif.charAt(8) == 'O' || nif.charAt(8) == 'Ñ')
			throw new IllegalArgumentException("Nif contiene la letra erronea\n");
		String cifras = nif.substring(0, nif.length() - 1);
		char letra = nif.charAt(8);
		int numero = Integer.parseInt(cifras);
		int resto = numero % 23;
		if (resto != letrasNif.indexOf(letra))
			throw new IllegalArgumentException("La letra del nif no corresponde con las cifras del nif\n");
		if (!users.contains(nif))
			throw new IllegalArgumentException("El nif no concuerda con ninguno del sistema\n");
		boolean encuentraTiket = false;
		for (Billete tiket : tickets) {
			if (tiket.getUsuario().getNif().equals(nif))
				encuentraTiket = true;
		}
		if (!encuentraTiket)
			throw new IllegalStateException("El nif no tiene ningun billete asociado\n");
		double precioTotal = 0;
		for (Billete tiket : tickets) {
			if (tiket.getUsuario().getNif().equals(nif)) {
				if (tiket.getRecorrido().getTransport().equals(TRAIN)) {
					double precioDescuento = tiket.getRecorrido().getPrice() * 0.9;
					precioTotal += precioDescuento;
				} else
					precioTotal += tiket.getRecorrido().getPrice();
			}
		}

		return precioTotal;
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
		if (fecha == null)
			throw new IllegalArgumentException("La fecha es nula\n");
		boolean asociado = false;
		List<Recorrido> salida = new ArrayList<>();
		for (Recorrido route : routes) {
			if (route.getDate().equals(fecha)) {
				asociado = true;
				salida.add(route);
			}
		}
		if (!asociado)
			throw new IllegalStateException("La fecha no corresponde con ningun recorrido\n");

		return salida;
	}

	/**
	 * Consult the list of tickets which are associated to the route
	 * 
	 * @param id of the route
	 * 
	 * @return list of tickets
	 * 
	 * @throws IllegalArgumentException if the id is null
	 * @throws IllegalStateException    if id's route isn't in the system
	 */
	public List<Billete> getAssociatedBilletesToRoute(String id) {
		Recorrido route;
		List<Billete> list = new ArrayList<>();
		if ((route = getRecorrido(id)) == null)
			throw new IllegalStateException("the route isn't in the system");
		for (Billete ticket : tickets) {
			if (ticket.getRecorrido().equals(route))
				list.add(ticket);
		}
		return list;
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
		Recorrido r;
		if ((r = getRecorrido(id)) == null)
			throw new IllegalStateException("no route in the system with this id");
		return r.getDate();
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
		Recorrido r;
		if ((r = getRecorrido(id)) == null)
			throw new IllegalStateException("no route in the system with this id");
		return r.getTime();
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
		Recorrido r;
		if ((r = getRecorrido(id)) == null)
			throw new IllegalStateException("no route in the system with this id");
		return r.getDateTime();
	}

	/**
	 * Update the date of a route
	 * 
	 * @param id      of the route
	 * @param newDate
	 * 
	 * @throws IllegalArgumentException if id is null
	 * @throws IllegalStateException    if id's route isn't in the system
	 * @throws IllegalArgumentException if newDate is null
	 * @throws IllegalStateException    if the new date is the already the set
	 */
	public void updateRecorridoDate(String id, LocalDate newDate) {
		Recorrido route;
		if ((route = getRecorrido(id)) == null)
			throw new IllegalStateException("the route isn't in the system");
		if (newDate == null)
			throw new IllegalArgumentException("newDate is null");
		if (route.getDate().equals(newDate))
			throw new IllegalStateException("newDate is already set");
		route.updateDate(newDate);
	}

	/**
	 * Update the time of a route
	 * 
	 * @param id      of the route
	 * @param newTime
	 * 
	 * @throws IllegalArgumentException if id is null
	 * @throws IllegalStateException    if id's route isn't in the system
	 * @throws IllegalArgumentException if newTime is null
	 * @throws IllegalStateException    if the new time is the already the set
	 */
	public void updateRecorridoTime(String id, LocalTime newTime) {
		Recorrido route;
		if ((route = getRecorrido(id)) == null)
			throw new IllegalStateException("the route isn't in the system");
		if (newTime == null)
			throw new IllegalArgumentException("newTime is null");
		if (route.getTime().equals(newTime))
			throw new IllegalStateException("newTime is already set");
		route.updateTime(newTime);
	}

	/**
	 * Update the time and date of a route
	 * 
	 * @param id          of the route
	 * @param newDateTime
	 * 
	 * @throws IllegalArgumentException if id is null
	 * @throws IllegalStateException    if id's route isn't in the system
	 * @throws IllegalArgumentException if newDateTime is null
	 * @throws IllegalStateException    if the new Date time is the already the set
	 */
	public void updateRecorridoDateTime(String id, LocalDateTime newDateTime) {
		Recorrido route;
		if ((route = getRecorrido(id)) == null)
			throw new IllegalStateException("the route isn't in the system");
		if (newDateTime == null)
			throw new IllegalArgumentException("newDateTime is null");
		if (route.getDateTime().equals(newDateTime))
			throw new IllegalStateException("newDateTime is already set");
		route.updateDateTime(newDateTime);
	}

	/**
	 * Update the time and date of a route
	 * 
	 * @param id      of the route
	 * @param newDate
	 * @param newTime
	 * 
	 * @throws IllegalArgumentException if id is null
	 * @throws IllegalStateException    if id's route isn't in the system
	 * @throws IllegalArgumentException if newDate is null
	 * @throws IllegalArgumentException if newTime is null
	 * @throws IllegalStateException    if the new Date time is the already the set
	 */
	public void updateRecorrido(String id, LocalDate newDate, LocalTime newTime) {
		Recorrido route;
		if ((route = getRecorrido(id)) == null)
			throw new IllegalStateException("the route isn't in the system");
		if (newDate == null)
			throw new IllegalArgumentException("newDateTime is null");
		if (newTime == null)
			throw new IllegalArgumentException("newTime is null");
		if (route.getDateTime().equals(LocalDateTime.of(newDate, newTime)))
			throw new IllegalStateException("newDateTime is already set");
		route.updateDateTime(newDate, newTime);
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
	 * @throws IllegalStateException    if the route doesn't exist
	 * @throws IllegalArgumentException if user is null.
	 * @throws IllegalArgumentException if recorrido is null.
	 * @throws IllegalArgumentException if recorrido is null.
	 */
	public List<Billete> reservarBilletes(String localizador, Usuario user, Recorrido recorrido,
			int numBilletesReservar) {
		if (user == null)
			throw new IllegalArgumentException("El usuario no puede ser null");
		if (recorrido == null)
			throw new IllegalArgumentException("El recorrido no puede ser null");
		if (localizador == null)
			throw new IllegalArgumentException("El localizador no puede ser null");
		if (numBilletesReservar > recorrido.getNumAvailableSeats())
			throw new IllegalStateException(
					"No se puede reservar si el número de billetes es mayor a los asientos disponibles");
		if (recorrido.getNumAvailableSeats() < recorrido.getTotalSeats() / 2)
			throw new IllegalStateException(
					"No se puede reservar si el número de asientos disponibles es menor a la mitad del número total de asientos");
		if (localizador.equals(""))
			throw new IllegalArgumentException("El localizador no puede ser vacio");
		for (Billete billetes : this.tickets) {
			if (billetes.getLocalizador().equals(localizador))
				throw new IllegalStateException("Ese localizador ya ha sido utilizado");
		}
		if (!routes.contains(recorrido))
			throw new IllegalStateException("El recorrido no existe en el sistema");

		List<Billete> billetesReservados = new ArrayList<Billete>();
		for (int i = 0; i < numBilletesReservar; i++) {
			Billete billete = new Billete(localizador, recorrido, user, ESTADO_RESERVADO);
			billetesReservados.add(billete);
		}
		recorrido.decreaseAvailableSeats(numBilletesReservar);
		tickets.addAll(billetesReservados);
		return billetesReservados;

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
	 * @throws IllegalArgumentException if the number of tickets is less or equal to
	 *                                  0
	 * @throws IllegalStateException    if the number of tickets is more than the
	 *                                  number of reserved tickets with that locator
	 */
	public void anularReserva(String localizador, int numBilletesAnular) {
		if (localizador == null)
			throw new IllegalArgumentException("El localizador no puede ser null");
		if (localizador.equals(""))
			throw new IllegalArgumentException("El localizador no puede ser vacio");
		if (numBilletesAnular < 1)
			throw new IllegalArgumentException("No se puede reservar si el número de billetes es menor que 1");
		List<Billete> packBilletes = new ArrayList<>();
		for (Billete billete : this.tickets) {
			if (billete.getLocalizador().equals(localizador)) {
				packBilletes.add(billete);
				if (!billete.getEstado().equals(ESTADO_RESERVADO))
					throw new IllegalStateException("El localizador no corresponde con tickets reservados");
			}
		}
		if (packBilletes.isEmpty())
			throw new IllegalStateException("El localizador no corresponde con tickets reservados");
		if (packBilletes.size() < numBilletesAnular)
			throw new IllegalStateException("Hay menos tickets de los que se quieren anular con ese localizador");

		
		this.tickets.removeAll(packBilletes);
		packBilletes.get(0).getRecorrido().increaseAvailableSeats(numBilletesAnular);

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
		if (localizador == null)
			throw new IllegalArgumentException("El localizador no puede ser null");
		if (localizador.equals(""))
			throw new IllegalArgumentException("El localizador no puede ser vacio");
		if (numBilletesDevolver < 1)
			throw new IllegalArgumentException("No se puede reservar si el número de billetes es menor que 1");
		List<Billete> packBilletes = new ArrayList<>();
		for (Billete billete : this.tickets) {
			if (billete.getLocalizador().equals(localizador)) {
				packBilletes.add(billete);
				if (!billete.getEstado().equals(ESTADO_COMPRADO))
					throw new IllegalStateException("El localizador no corresponde con tickets comprados");
			}
		}
		if (packBilletes.isEmpty())
			throw new IllegalStateException("El localizador no corresponde con tickets comprados");

		if (packBilletes.size() < numBilletesDevolver)
			throw new IllegalStateException("Hay menos tickets de los que se quieren anular con ese localizador");

		Recorrido recorrido = this.tickets.get(0).getRecorrido();
		this.tickets.removeAll(packBilletes);
				
		recorrido.increaseAvailableSeats(numBilletesDevolver);

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
		if (localizador == null)
			throw new IllegalArgumentException("El localizador es nulo\n");
		if (localizador.isEmpty())
			throw new IllegalArgumentException("El localizador esta vacio\n");
		for (Billete tiket : tickets) {
			if (tiket.getLocalizador().equals(localizador)) {
				throw new IllegalArgumentException("El localizador ya a sido usado\n");
			}
		}
		if (usr == null)
			throw new IllegalArgumentException("El usuario es nulo\n");
		if (recorrido == null)
			throw new IllegalArgumentException("El recorrido es nulo\n");
		if (numBilletes < 1)
			throw new IllegalArgumentException("El numero de billetes ha de ser superior a 0\n");
		if (numBilletes > recorrido.getTotalSeats())
			throw new IllegalArgumentException("El numero es superior a los asientos del vehiculo\n");
		if (numBilletes > recorrido.getNumAvailableSeats())
			throw new IllegalStateException("El numero es superior a los asientos disponibles\n");
		List<Billete> salida = new ArrayList<>();
		for (int i = 0; i < numBilletes; i++) {
			Billete aux = new Billete(localizador, recorrido, usr, ESTADO_COMPRADO);
			salida.add(aux);
			tickets.add(aux);

		}
		recorrido.decreaseAvailableSeats(numBilletes);
		if (!users.contains(usr.getNif()))
			users.add(usr.getNif());
		return salida;

	}

	/**
	 * Consult and return the list with the ticket which has the same locator. If
	 * there is no tickets with that locator will be returned a empty list
	 * 
	 * @param locator of the ticket(s)
	 * 
	 * @return list of tickets (it could be empty)
	 * 
	 * @throws IllegalArgumentException if locator is null
	 * @throws IllegalArgumentException if locator have less than 1 or more than 8
	 *                                  characters
	 */
	private List<Billete> getBilletes(String locator) {
		if (locator == null)
			throw new IllegalArgumentException("locator is null");
		if (locator.isBlank() || locator.length() > 8)
			throw new IllegalArgumentException("locator must be between 1 and 8 characters longs");
		List<Billete> tickets = new ArrayList<>();
		for (Billete ticket : this.tickets) {
			if (ticket.getLocalizador().equals(locator))
				tickets.add(ticket);
		}
		return tickets;
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
		List<Billete> tickets;
		if ((tickets = getBilletes(locator)).isEmpty())
			throw new IllegalStateException("the is no tickets for this locator: " + locator);
		for (Billete ticket : tickets) {
			try {
				ticket.setComprado();
			} catch (IllegalStateException e) {
				// Reescritura de mensaje de error
				throw new IllegalStateException("the are no tickets booked with that locator");
			}
		}
		return tickets;

	}
}