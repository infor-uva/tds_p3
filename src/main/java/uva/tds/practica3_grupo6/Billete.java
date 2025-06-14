package uva.tds.practica3_grupo6;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

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
 * @version 22/12/23
 */
@Entity
@Table(name="BILLETE")
public class Billete {

	public static final String ESTADO_COMPRADO = "comprado";
	public static final String ESTADO_RESERVADO = "reservado";
	
	@Id
	@GeneratedValue
	private int id;
	@Column(name="LOCALIZADOR")
	private String localizador;
	@ManyToOne()
    @JoinColumn(name = "RECORRIDO_ID", referencedColumnName = "ID")
	private Recorrido recorrido;
	@ManyToOne()
    @JoinColumn(name = "USUARIO_ID", referencedColumnName = "NIF")
	private Usuario usuario;
	@Column(name="ESTADO")
	private String estado;

	public Billete() {
		
	}
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

	@Override
	public int hashCode() {
		return Objects.hash(estado, id, localizador, recorrido, usuario);
	}
	
	/**
	 * Compare if two tickets are the same
	 * 
	 * @param o ticket to compare
	 * 
	 * @return if are the same
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Billete)) {
			return false;
		}
		Billete other = (Billete) obj;
		return Objects.equals(estado, other.estado) 
				&& Objects.equals(localizador, other.localizador)
				&& Objects.equals(recorrido, other.recorrido) 
				&& Objects.equals(usuario, other.usuario);
	}
}