package uva.tds.practica3_grupo6;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

public class DatabaseManager implements IDatabaseManager {

	@Override
	public void addRecorrido(Recorrido recorrido) {
		if (recorrido == null)
			throw new IllegalArgumentException();
		if (getRecorrido(recorrido.getID())!= null)
			throw new IllegalArgumentException("El recorrido con ese id ya existe");
		
		Session session = getSession();
		
		try {
			Transaction tx=session.beginTransaction();
			
			Connection connection = new Connection("Valladolid", "Palencia", 30);
		    session.save(connection);
		    
		    BusRecorrido recorrido2 = new BusRecorrido("ide", connection, 20, LocalDateTime.of(2023, 10, 27, 19, 06, 50), 40);
		    session.save(recorrido2);
		    
		    session.flush();
		    tx.commit();
			
		} catch(HibernateException e) {
			e.printStackTrace();
			session.getTransaction().rollback();
		} finally {
			
			session.close();
		}
	}

	@Override
	public void eliminarRecorrido(String idRecorrido) {
		if (idRecorrido == null)
			throw new IllegalArgumentException();
		if (getRecorrido(idRecorrido) == null)
			throw new IllegalArgumentException("El recorrido con ese id no existe");
		
		Session session = getSession();
		
		try {
			session.beginTransaction();
			session.delete(idRecorrido, getRecorrido(idRecorrido));
			
		} catch(HibernateException e) {
			e.printStackTrace();
			session.getTransaction().rollback();
		} finally {
			session.close();
		}
	}

	@Override
	public void actualizarRecorrido(Recorrido recorrido) {
		if (recorrido == null)
			throw new IllegalArgumentException();
		if (getRecorrido(recorrido.getID()) == null)
			throw new IllegalArgumentException("El recorrido con ese id no existe");
		
		Session session = getSession();
		
		try {
			session.beginTransaction();
			session.refresh(recorrido);
			
		} catch(HibernateException e) {
			e.printStackTrace();
			session.getTransaction().rollback();
		} finally {
			session.close();
		}

	}

	@Override
	public Recorrido getRecorrido(String idRecorrido) {
		Session session = getSession();
		
		try {
			session.beginTransaction();
			
			Recorrido recorrido = session.get(Recorrido.class, idRecorrido);
			return recorrido;
			
		} catch (Exception e) {
			e.printStackTrace();
			session.getTransaction().rollback();
		}finally {
			session.close();
		}
		return null;
	}

	@Override
	public ArrayList<Recorrido> getRecorridos() {
		Session session = getSession();
		ArrayList<Recorrido> lista = new ArrayList<>();
		
		try {
			session.beginTransaction();
			List<Recorrido> recorridosList = session.createQuery("FROM Recorrido", Recorrido.class).list();
			lista.addAll(recorridosList);
			return lista;
			
		} catch (Exception e) {
			e.printStackTrace();
			session.getTransaction().rollback();
		}finally {
			session.close();
		}
		return lista;
	}

	@Override
	public ArrayList<Recorrido> getRecorridos(LocalDate fecha) {
		Session session = getSession();
		ArrayList<Recorrido> lista = new ArrayList<>();
		
		try {
			session.beginTransaction();
			List<Recorrido> recorridosList = session.createQuery("FROM RECORRIDO WHERE DATE = :fecha", Recorrido.class).setParameter("fecha", fecha).list();
			lista.addAll(recorridosList);
			return lista;
			
		} catch (Exception e) {
			e.printStackTrace();
			session.getTransaction().rollback();
		}finally {
			session.close();
		}
		return lista;
	}

	@Override
	public void addUsuario(Usuario usuario) {
		if (usuario == null)
			throw new IllegalArgumentException();
		if (getUsuario(usuario.getNif()) != null)
			throw new IllegalArgumentException("El usuario con ese nif ya existe");
		
		Session session = getSession();
		
		try {
			session.beginTransaction();
			
			session.persist(usuario);
		} catch (HibernateException e) {
			e.printStackTrace();
			session.getTransaction().rollback();
		} finally {
			session.close();
		}
	}

	@Override
	public void eliminarUsuario(String idUsuario) {
		if (idUsuario == null)
			throw new IllegalArgumentException();
		if (getRecorrido(idUsuario) == null)
			throw new IllegalArgumentException("El usuario con ese id no existe");
		
		Session session = getSession();
		
		try {
			session.beginTransaction();
			session.delete(idUsuario, getUsuario(idUsuario));
			
		} catch(HibernateException e) {
			e.printStackTrace();
			session.getTransaction().rollback();
		} finally {
			session.close();
		}
	}

	@Override
	public void actualizarUsuario(Usuario usuario) {
		if (usuario == null)
			throw new IllegalArgumentException();
		if (getRecorrido(usuario.getNif()) == null)
			throw new IllegalArgumentException("El usuario con ese id no existe");
		
		Session session = getSession();
		
		try {
			session.beginTransaction();
			session.refresh(usuario);
			
		} catch(HibernateException e) {
			e.printStackTrace();
			session.getTransaction().rollback();
		} finally {
			session.close();
		}
	}

	@Override
	public Usuario getUsuario(String idUsuario) {
		Session session = getSession();
		
		try {
			session.beginTransaction();
			
			Usuario usuario = session.get(Usuario.class, idUsuario);
			return usuario;
		} catch (Exception e) {
			e.printStackTrace();
			session.getTransaction().rollback();
		} finally {
			session.close();
		}
		return null;
	}

	@Override
	public void addBillete(Billete billete) {
		if (billete == null)
			throw new IllegalArgumentException();
		
		Session session = getSession();
		
		try {
			session.beginTransaction();
			
			session.persist(billete);
		} catch (HibernateException e) {
			e.printStackTrace();
			session.getTransaction().rollback();
		} finally {
			session.close();
		}
	}

	@Override
	public void eliminarBilletes(String localizadorBillete) {
		if (localizadorBillete == null)
			throw new IllegalArgumentException();
		if (getRecorrido(localizadorBillete) == null)
			throw new IllegalArgumentException("El billete con ese id no existe");
		
		Session session = getSession();
		
		try {
			session.beginTransaction();
			session.delete(localizadorBillete, getRecorrido(localizadorBillete));
			
		} catch(HibernateException e) {
			e.printStackTrace();
			session.getTransaction().rollback();
		} finally {
			session.close();
		}

	}

	@Override
	public void actualizarBilletes(Billete billete) {
		if (billete == null)
			throw new IllegalArgumentException();
		
		Session session = getSession();
		
		try {
			session.beginTransaction();
			
			session.refresh(billete);
		} catch (HibernateException e) {
			e.printStackTrace();
			session.getTransaction().rollback();
		} finally {
			session.close();
		}

	}

	@Override
	public ArrayList<Billete> getBilletes(String localizadorBilletes) {
		Session session = getSession();
		ArrayList<Billete> lista = new ArrayList<>();
		
		try {
			session.beginTransaction();
			List<Billete> recorridosList = session.createQuery("FROM BILLETE", Billete.class).list();
			
			lista.addAll(recorridosList);
			return lista;
			
		} catch (Exception e) {
			e.printStackTrace();
			session.getTransaction().rollback();
		}finally {
			session.close();
		}
		return lista;
	}

	@Override
	public ArrayList<Billete> getBilletesDeRecorrido(String idRecorrido) {
		Session session = getSession();
		ArrayList<Billete> lista = new ArrayList<>();
		
		try {
			session.beginTransaction();
			List<Billete> recorridosList = session.createQuery("FROM BILLETE WHERE RECORRIDO_ID = :idRecorrido", Billete.class).setParameter("idRecorrido", idRecorrido).list();
			lista.addAll(recorridosList);
			return lista;
			
		} catch (Exception e) {
			e.printStackTrace();
			session.getTransaction().rollback();
		}finally {
			session.close();
		}
		return lista;
	}

	@Override
	public ArrayList<Billete> getBilletesDeUsuario(String idUsuario) {
		Session session = getSession();
		ArrayList<Billete> lista = new ArrayList<>();
		
		try {
			session.beginTransaction();
			List<Billete> recorridosList = session.createQuery("FROM BILLETE WHERE USUARIO_ID = :idUsuario", Billete.class).setParameter("idUsuario", idUsuario).list();
			lista.addAll(recorridosList);
			return lista;
			
		} catch (Exception e) {
			e.printStackTrace();
			session.getTransaction().rollback();
		}finally {
			session.close();
		}
		return lista;
	}
	
	private Session getSession() {
		SessionFactory factory = HibernateUtil.getSessionFactory();
		Session session;
		try {
			session = factory.getCurrentSession();
			return session;
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Elimina las tablas de la base de datos
	 */
	public void clearDatabase() {
		Session session = getSession();
		session.getTransaction().begin();
		Query query = session.createSQLQuery("Truncate table CARD");
		query.executeUpdate();
		query = session.createSQLQuery("Truncate table CARD");
		query.executeUpdate();
		session.close();

	}

}
