package uva.tds.practica3_grupo6;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Interfaz que define los métodos necesarios para gestionar usuarios,
 * recorridos y billetes en una base de datos
 */
public interface IDatabaseManager {

	/**
	 * Añade un recorrido al sistema
	 * 
	 * @param recorrido El recorrido a añadir
	 * @throws IllegalArgumentException si recorrido es nulo
	 * @throws IllegalStateException    si ya existe un recorrido con el mismo
	 *                                  identificador
	 */
	public void addRecorrido(Recorrido recorrido);

	/**
	 * Elimina un recorrido del sistema. Si no existe el recorrido, no se produce
	 * ningún cambio en el sistema
	 * 
	 * @param idRecorrido El identificador del recorrido
	 * @throws IllegalArgumentException si idRecorrido es nulo
	 */
	public void eliminarRecorrido(String idRecorrido);

	/**
	 * Actualiza el recorrido cuyo identificador coincida con el proporcionado.
	 * 
	 * @param recorrido El recorrido con los datos a actualizar
	 * @throws IllegalArgumentException si recorrido es nulo
	 * @throws IllegalStateException    si no existe un recorrido con el
	 *                                  identificador indicado en recorrido
	 */
	public void actualizarRecorrido(Recorrido recorrido);

	/**
	 * Devuelve el recorrido con el id idRecorrido. Si no existe ningún recorrido,
	 * se devuelve null
	 * 
	 * @param idRecorrido El id del recorrido
	 * @return El recorrido asociado a idRecorrido
	 * @throws IllegalArgumentException si idRecorrido es nulo
	 */
	public Recorrido getRecorrido(String idRecorrido);

	/**
	 * Devuelve todos los recorridos del sistema
	 * 
	 * @return Una lista con todos los recorridos del sistema. Si no hay ninguno, se
	 *         devuelve una lista vacia
	 */
	public ArrayList<Recorrido> getRecorridos();

	/**
	 * Devuelve los recorridos en una fecha del sistema
	 * 
	 * @return Una lista con los recorridos en la fecha indicada del. Si no hay
	 *         ninguno, se devuelve una lista vacia
	 */
	public ArrayList<Recorrido> getRecorridos(LocalDate fecha);

	/**
	 * Añade un usuario al sistema
	 * 
	 * @param usuario El usuario a añadir
	 * @throws IllegalArgumentException si usuario es nulo
	 * @throws IllegalStateException    si ya existe un usuario con el mismo
	 *                                  identificador
	 */
	public void addUsuario(Usuario usuario);

	/**
	 * Elimina un usuario del sistema. Si no existe el usuario, no se produce ningún
	 * cambio en el sistema
	 * 
	 * @param idUsuario El identificador del usuario
	 * @throws IllegalArgumentException si idUsuario es nulo
	 */
	public void eliminarUsuario(String idUsuario);

	/**
	 * Actualiza el usuario cuyo identificador coincida con el proporcionado.
	 * 
	 * @param usuario El usuario con los datos a actualizar
	 * @throws IllegalArgumentException si usuario es nulo
	 * @throws IllegalStateException    si no existe un usuario con el identificador
	 *                                  indicado en recorrido
	 */
	public void actualizarUsuario(Usuario usuario);

	/**
	 * Devuelve el usuario con el id idUsuario. Si no existe ningún usuario, se
	 * devuelve null
	 * 
	 * @param idUsuario El id del usuario
	 * @return El usuario asociado a idUsuario
	 * @throws IllegalArgumentException si idUsuario es nulo
	 */
	public Usuario getUsuario(String idUsuario);

	/**
	 * Añade un billete al sistema
	 * 
	 * @param billete El billete a añadir throws IllegalArgumentException si billete
	 *                es nulo
	 * 
	 */
	public void addBillete(Billete billete);

	/**
	 * Elimina los billetes asociados a localizadorBillete. Si no existen billetes
	 * con el localizador indicado no se produce ningún cambio en el sistema
	 * 
	 * @param localizadorBillete El localizador de los billetes a eliminar
	 * @throws IllegalArgumentException si localizadorBillete es nulo
	 */
	public void eliminarBilletes(String localizadorBillete);

	/**
	 * Actualiza los billetes con el localizador de billete
	 * 
	 * @param billete El billete con la información para actualizar los billetes
	 *                almacenados en el sistema con el mismo localizador
	 * @throws IllegalArgumentException si billete es nulo
	 * @throws IllegalStateException    si no existen billetes con el localizador
	 *                                  indicado en billete
	 */
	public void actualizarBilletes(Billete billete);

	/**
	 * Devuelve los billetes con el localizador indicado por localizadorBilletes
	 * 
	 * @param localizadorBilletes El localizador de los billetes a obtener.
	 * @return La lista de billetes con el localizador indicado por
	 *         localizadorBilletes. Si no existe ninguno, se devuelve una lista
	 *         vacía
	 */
	public ArrayList<Billete> getBilletes(String localizadorBilletes);

	/**
	 * Devuelve los billetes asociados a un recorrido
	 * 
	 * @param idRecorrido El identificador del recorrido
	 * @return La lista de billetes asociados al recorrido o lista vacia si no
	 *         existe ninguno
	 */
	public ArrayList<Billete> getBilletesDeRecorrido(String idRecorrido);

	/**
	 * Devuelve los billetes asociados a un usuario
	 * 
	 * @param idUsuario El identificador del usuario
	 * @return La lista de billetes asociados al usuario o lista vacia si no existe
	 *         ninguno
	 */
	public ArrayList<Billete> getBilletesDeUsuario(String idUsuario);
	
	public void clearDatabase();

}
