package uva.tds.practica3_grupo6;

import java.util.*;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Class dedicated for the representation of the User.
 * 
 * In this class you can check the user's name and ID number.
 * 
 * For the instantiation of this class, review
 * {@link Usuario#Usuario(String, String)}
 * 
 * @author diebomb
 * @author migudel
 * @author hugcubi
 * 
 * @version 21/12/23
 */
@Entity
@Table(name="USUARIO")
public class Usuario {
	
	private static final List<Character> letrasNif = new ArrayList<>(Arrays.asList('T', 'R', 'W', 'A', 'G', 'M', 'Y', 'F', 'P',
			'D', 'X', 'B', 'N', 'J', 'Z', 'S', 'Q', 'V', 'H', 'L', 'C', 'K', 'E'));
	
	@Id
	private String nif;
	@Column(name="NAME")
	private String nombre;
	
	@OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
	private List<Billete> billetes;
	
	/**
	 * Constructor sin parametros
	 * 
	 */
	public Usuario() {}
	
	/**
	 * Constructor
	 * 
	 * @param nif
	 * @param nombre
	 * 
	 * @throws IllegalArgumentException if the NIF is empty
	 * @throws IllegalArgumentException if the NIF is null
	 * @throws IllegalArgumentException if the name is empty
	 * @throws IllegalArgumentException if the name is null
	 * @throws IllegalArgumentException if the number of characters in the name
	 *                                  exceeds 15
	 * @throws IllegalArgumentException if the number of NIF digits exceeds 8
	 * @throws IllegalArgumentException if the number of NIF digits is less than 8
	 * @throws IllegalArgumentException if the NIF does not end with a letter,
	 *                                  except {I,Ñ,O,U}
	 * @throws IllegalArgumentException if the NIF value does not correspond to the
	 *                                  letter
	 */
	public Usuario(String nif, String nombre) {
		if (nombre == null) {
			throw new IllegalArgumentException("Nombre nulo\n");
		}
		if (nombre.isEmpty()) {
			throw new IllegalArgumentException("Nombre vacio\n");
		}
		if (nombre.length() > 15) {
			throw new IllegalArgumentException("Nombre demasiado largo\n");
		}
		checkNIF(nif);
		this.nif = nif;
		this.nombre = nombre;
		billetes = new ArrayList<>();
	}

	/**
	 * Check if the nif is ok
	 * 
	 * @param nif
	 * @param nombre
	 * 
	 * @throws IllegalArgumentException if the NIF is null
	 * @throws IllegalArgumentException if the number of characters in the name
	 *                                  exceeds 15
	 * @throws IllegalArgumentException if the number of NIF digits exceeds 8
	 * @throws IllegalArgumentException if the number of NIF digits is less than 8
	 * @throws IllegalArgumentException if the NIF does not end with a letter,
	 *                                  except {I,Ñ,O,U}
	 * @throws IllegalArgumentException if the NIF value does not correspond to the
	 *                                  letter
	 */
	public static void checkNIF(String nif) {
		if (nif == null) {
			throw new IllegalArgumentException("Nif nulo\n");
		}
		if (nif.isEmpty()) {
			throw new IllegalArgumentException("Nif vacio\n");
		}
		if (nif.length() <= 8) {
			throw new IllegalArgumentException("Nif demasiado corto\n");
		}
		if (nif.length() > 9) {
			throw new IllegalArgumentException("Nif demasiado largo\n");
		}
		if (!Character.isLetter(nif.charAt(8))) {
			throw new IllegalArgumentException("Nif no contiene la letra\n");
		}
		if (nif.charAt(8) == 'U' || nif.charAt(8) == 'I' || nif.charAt(8) == 'O' || nif.charAt(8) == 'Ñ') {
			throw new IllegalArgumentException("Nif contiene la letra erronea\n");
		}
		// sacar si el valor de la letra corresponde con la division del numero
		String cifras = nif.substring(0, nif.length() - 1);
		char letra = nif.charAt(8);
		int numero = Integer.parseInt(cifras);
		int resto = numero % 23;
		if (resto != letrasNif.indexOf(letra)) {
			throw new IllegalArgumentException("La letra del nif no corresponde con las cifras del nif\n");
		}
	}

	/**
	 * Consult the nif of the Usuario
	 * 
	 * @return nif
	 */
	public String getNif() {
		return this.nif;
	}

	/**
	 * Consult the name of Usuario
	 * 
	 * @return nombre
	 */
	public String getNombre() {
		return this.nombre;
	}

	public void addBilletes(Billete ticket) {
		this.billetes.add(ticket);
	}
	
	public void removeBilletes(Billete ticket) {
		this.billetes.remove(ticket);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(billetes, letrasNif, nif, nombre);
	}

	/**
	 * Compare if two Usuarios are the same
	 * 
	 * @param o Usuario to compare
	 * 
	 * @return if are the same
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Usuario)) {
			return false;
		}
		Usuario other = (Usuario) obj;
		return Objects.equals(nif, other.nif)
				&& Objects.equals(nombre, other.nombre);
	}
}
