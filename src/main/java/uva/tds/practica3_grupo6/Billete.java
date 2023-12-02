package uva.tds.practica3_grupo6;

/**
 * Class dedicated for the representation of the ticket.
 * 
 * In this class you will can consult the locator, route, user and the state of
 * the ticket.
 * 
 * For compare two packs of tickets will be used the locator, for compare two
 * tickets will be used the locator and state
 * 
 * @author hugcubi
 * @author diebomb
 * @author migudel
 * 
 * @version 09/11/23
 */
public class Billete {

	public static final String ESTADO_COMPRADO = "comprado";
	public static final String ESTADO_RESERVADO = "reservado";

	private String localizador;
	private Recorrido recorrido;
	private Usuario usuario;
	private String estado;

	/**
	 * @param localizador
	 * @param recorrido
	 * @param usuario
	 * @param estado
	 * @throws IllegalArgumentException si localizador tiene menos de 1 y mas de 8
	 *                                  caracteres
	 * @throws IllegalArgumentException si recorrido, usuario o estado son null
	 * @throws IllegalArgumentException si estado es diferente de "comprado" y
	 *                                  "reservado"
	 */
	public Billete(String localizador, Recorrido recorrido, Usuario usuario, String estado) {
		if (recorrido == null)
			throw new IllegalArgumentException("Recorrido no puede ser null");
		if (usuario == null)
			throw new IllegalArgumentException("Usuario no puede ser null");
		if (estado == null)
			throw new IllegalArgumentException("Estado no puede ser null");
		if (localizador.length() < 1 || localizador.length() > 8)
			throw new IllegalArgumentException("Localizador tiene que tener entre 1 y 8 caracteres");
		if (!estado.equals(ESTADO_COMPRADO) && !estado.equals(ESTADO_RESERVADO))
			throw new IllegalArgumentException("Estado tiene que ser 'comprado' o 'reservado' ");
		this.localizador = localizador;
		this.recorrido = recorrido;
		this.usuario = usuario;
		this.estado = estado;

	}

	/**
	 * Consults the locator of the ticket
	 * 
	 * @return localizador
	 */
	public String getLocalizador() {
		return localizador;
	}

	/**
	 * Consults the route of the ticket
	 * 
	 * @return recorrido
	 */
	public Recorrido getRecorrido() {
		return recorrido;
	}

	/**
	 * Consults the user of the ticket
	 * 
	 * @return usuario
	 */
	public Usuario getUsuario() {
		return usuario;
	}

	/**
	 * Consult the state of the ticket
	 * <ul>
	 * <li>Comprado</li>
	 * <li>Reservado</li>
	 * </ul>
	 * 
	 * @return estado
	 */
	public String getEstado() {
		return estado;
	}

	/**
	 * Set ticket as after booking
	 * 
	 * @throws IllegalStateException if the state of the ticket is not booked
	 */
	public void setComprado() {
		if (!estado.equals(ESTADO_RESERVADO))
			throw new IllegalStateException("Ticket tiene que estar reservado");
		this.estado = ESTADO_COMPRADO;

	}

	/**
	 * Compare if two tickets are the same
	 * 
	 * @param o ticket to compare
	 * 
	 * @return if are the same
	 * 
	 * @throws IllegalArgumentException if o is null
	 */
	@Override
	public boolean equals(Object o) {
		if (o == null)
			throw new IllegalArgumentException("El objeto no puede ser null");
		// Compruebas que es un objeto billete
		if (!(o instanceof Billete))
			return false;
		Billete tmp = (Billete) o;
		// Comparamos los objetos
		return (this.localizador.equals(tmp.getLocalizador()) 
				&& this.recorrido.equals(tmp.getRecorrido())
				&& this.usuario.equals(tmp.getUsuario()) 
				&& this.estado.equals(tmp.getEstado()));
		}
	}

