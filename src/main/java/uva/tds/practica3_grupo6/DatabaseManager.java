package uva.tds.practica3_grupo6;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

/**
 * Class dedicated for the implementation of the BD.
 * 
 * In this class you will can consult the routes, users or the state of
 * the tickets.
 * 
 * @author hugcubi
 * @author diebomb
 * @author migudel
 * 
 * @version 22/12/23
 */

public class DatabaseManager implements IDatabaseManager {

	@Override
	public void addRecorrido(Recorrido rec) {
		if (rec == null) {
			throw new IllegalArgumentException();
		}

		if (getRecorrido(rec.getID()) != null) {
			throw new IllegalStateException("El recorrido con ese numero ya existe");
		}

		Session session = getSession();

		try {
			session.beginTransaction();
			if (rec instanceof BusRecorrido bus) {
				session.persist(bus);
			}
			if (rec instanceof TrainRecorrido train) {
				session.persist(train);
			}
			if (session.get(Connection.class, rec.getConnection().getId()) == null) {
				session.persist(rec.getConnection());
			} else {
				Connection conexion = session.get(Connection.class, rec.getConnection().getId());
				conexion.addRecorrido(rec);
				session.save(conexion);
			}
			session.flush();

		} catch (HibernateException e) {
			session.getTransaction().rollback();
		} finally {
			session.close();
		}
	}

	@Override
	public void eliminarRecorrido(String idRecorrido) {
		if (idRecorrido == null) {
			throw new IllegalArgumentException();
		}

		if (getRecorrido(idRecorrido) == null) {
			throw new IllegalStateException("El recorrido con ese numero no existe");
		}

		Session session = getSession();

		try {
			session.beginTransaction();
			Recorrido rec = session.get(Recorrido.class, idRecorrido);
			Connection conexion = session.get(Connection.class, rec.getConnection().getId());
			conexion.deleteRecorrido(rec);
			session.save(conexion);
			session.delete(rec);

			session.flush();

		} catch (HibernateException e) {
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
			throw new IllegalStateException("El recorrido con ese id no existe");

		Session session = getSession();

		try {
			session.beginTransaction();
			session.update(recorrido);
			session.flush();

		} catch (HibernateException e) {
			session.getTransaction().rollback();
		} finally {
			session.close();
		}

	}

	@Override
	public Recorrido getRecorrido(String idRecorrido) {
		Session session = getSession();
		Recorrido rec = null;
		try {
			session.beginTransaction();

			rec = session.get(Recorrido.class, idRecorrido);

		} catch (Exception e) {
			session.getTransaction().rollback();
		} finally {
			session.close();
		}
		return rec;
	}

	@Override
	public ArrayList<Recorrido> getRecorridos() {
		Session session = getSession();
		ArrayList<Recorrido> lista = new ArrayList<>();

		try {
			session.beginTransaction();
			lista.addAll(session.createQuery("FROM Recorrido", Recorrido.class).getResultList());

		} catch (Exception e) {
			session.getTransaction().rollback();
		} finally {
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
			ArrayList<Recorrido> recorridosList = new ArrayList<>(session.createQuery("FROM Recorrido", Recorrido.class).getResultList());
			for (Recorrido s : recorridosList) {
				if (fecha.equals(s.getDate()))
					lista.add(s);
			}

		} catch (Exception e) {
			session.getTransaction().rollback();
		} finally {
			session.close();
		}
		return lista;
	}

	@Override
	public void addUsuario(Usuario usuario) {
		if (usuario == null)
			throw new IllegalArgumentException();
		if (getUsuario(usuario.getNif()) != null)
			throw new IllegalStateException("El usuario con ese nif ya existe");

		Session session = getSession();

		try {
			session.beginTransaction();

			session.persist(usuario);
			session.flush();
		} catch (HibernateException e) {
			session.getTransaction().rollback();
		} finally {
			session.close();
		}
	}

	@Override
	public void eliminarUsuario(String idUsuario) {
		if (idUsuario == null)
			throw new IllegalArgumentException();
		if (getUsuario(idUsuario) == null)
			throw new IllegalStateException("El usuario con ese id no existe");

		Session session = getSession();

		try {
			session.beginTransaction();
			Usuario user = session.get(Usuario.class, idUsuario);
			session.delete(idUsuario, user);
			session.flush();

		} catch (HibernateException e) {
			session.getTransaction().rollback();
		} finally {
			session.close();
		}
	}

	@Override
	public void actualizarUsuario(Usuario usuario) {
		if (usuario == null)
			throw new IllegalArgumentException();
		if (getUsuario(usuario.getNif()) == null)
			throw new IllegalStateException("El usuario con ese id no existe");

		Session session = getSession();

		try {
			session.beginTransaction();
			session.update(usuario);
			session.flush();

		} catch (HibernateException e) {
			session.getTransaction().rollback();
		} finally {
			session.close();
		}
	}

	@Override
	public Usuario getUsuario(String idUsuario) {
		Session session = getSession();

		Usuario user = null;
		
		try {
			session.beginTransaction();

			user = session.get(Usuario.class, idUsuario);
		} catch (Exception e) {
			session.getTransaction().rollback();
		} finally {
			session.close();
		}
		return user;
	}

	@Override
	public void addBillete(Billete billete) {
		if (billete == null)
			throw new IllegalArgumentException();

		Session session = getSession();

		try {
			
			session.beginTransaction();

			session.save(billete);
			Usuario user = session.get(Usuario.class, billete.getUsuario().getNif());
			user.addBilletes(billete);
			session.save(user);

			Recorrido rec = session.get(Recorrido.class, billete.getRecorrido().getID());
			rec.addBilletes(billete);
			rec.decreaseAvailableSeats(1);
			session.save(rec);

			session.flush();
		} catch (HibernateException e) {
			session.getTransaction().rollback();
		} finally {
			session.close();
		}
	}

	@Override
	public void eliminarBilletes(String localizadorBillete) {
		if (localizadorBillete == null)
			throw new IllegalArgumentException();
		if (getBilletes(localizadorBillete).equals(new ArrayList<>()))
			throw new IllegalStateException("El billete con ese id no existe");

		Session session = getSession();

		try {
			session.beginTransaction();
			List<Billete> listaBilletes = getBilletes(session);
			for (Billete b : listaBilletes) {
				session.delete(b);
				Usuario user = session.get(Usuario.class, b.getUsuario().getNif());
				user.removeBilletes(b);
				session.save(user);
				Recorrido rec = session.get(Recorrido.class, b.getRecorrido().getID());
				rec.removeBilletes(b);
				rec.increaseAvailableSeats(1);
				session.save(rec);
			}
			session.flush();

		} catch (HibernateException e) {
			session.getTransaction().rollback();
		} finally {
			session.close();
		}

	}

	@Override
	public void actualizarBilletes(Billete billete) {
		if (billete == null)
			throw new IllegalArgumentException();
		if (getBilletes(billete.getLocalizador()).equals(new ArrayList<>()))
			throw new IllegalStateException("El billete con ese id no existe");

		Session session = getSession();

		try {
			session.beginTransaction();

			session.update(billete);
			session.flush();
		} catch (HibernateException e) {
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
			List<Billete> recorridosList = getBilletes(session);
			for (Billete b : recorridosList) {
				if (b.getLocalizador().equals(localizadorBilletes))
					lista.add(b);
			}

		} catch (Exception e) {
			session.getTransaction().rollback();
		} finally {
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
			List<Billete> recorridosList = getBilletes(session);
			for (Billete s : recorridosList) {
				if (idRecorrido.equals(s.getRecorrido().getID()))
					lista.add(s);
			}

		} catch (Exception e) {
			session.getTransaction().rollback();
		} finally {
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
			List<Billete> recorridosList = getBilletes(session);
			for (Billete s : recorridosList) {
				if (idUsuario.equals(s.getUsuario().getNif()))
					lista.add(s);
			}

		} catch (Exception e) {
			session.getTransaction().rollback();
		} finally {
			session.close();
		}
		return lista;
	}

	private List<Billete> getBilletes(Session session) {
		return session.createQuery("FROM Billete", Billete.class).getResultList();
	}

	private Session getSession() {
		SessionFactory factory = HibernateUtil.getSessionFactory();
		Session session;
		try {
			session = factory.getCurrentSession();
			return session;
		} catch (HibernateException e) {
			throw new IllegalStateException(e);
		}

	}

	/**
	 * Elimina las tablas de la base de datos
	 */
	public void clearDatabase() {
		Session session = getSession();
		session.getTransaction().begin();
		Query query = session.createSQLQuery("Truncate table BILLETE");
		query.executeUpdate();
		query = session.createSQLQuery("Truncate table USUARIO_LETRASNIF");
		query.executeUpdate();
		query = session.createSQLQuery("Truncate table USUARIO");
		query.executeUpdate();
		query = session.createSQLQuery("Truncate table RECORRIDO");
		query.executeUpdate();
		query = session.createSQLQuery("Truncate table CONNECTION");
		query.executeUpdate();

		session.getTransaction().commit();
		session.close();
	}

}
