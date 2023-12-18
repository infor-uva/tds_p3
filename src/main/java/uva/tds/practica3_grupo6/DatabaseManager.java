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
			if(session.get(Connection.class, rec.getConnection().getId())==null) {
				session.persist(rec.getConnection());
			}
			else {
				Connection conexion = session.get(Connection.class, rec.getConnection().getId());
				conexion.addRecorrido(rec);
				session.save(conexion);
			}
			session.flush();

		} catch (HibernateException e) {
			e.printStackTrace();
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
			session.update(recorrido);
			session.flush();
			
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

			Recorrido rec = session.get(Recorrido.class, idRecorrido);
			return rec;

		} catch (Exception e) {
			e.printStackTrace();
			session.getTransaction().rollback();
		} finally {
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
			List<Recorrido> recorridosList = session.createQuery("FROM Recorrido", Recorrido.class).getResultList();
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
			List<Recorrido> recorridosList = session.createQuery("FROM Recorrido", Recorrido.class).getResultList();
			for(Recorrido s: recorridosList) {
				if(fecha.equals(s.getDate()))lista.add(s);
			}
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
			session.flush();
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
			session.flush();
			
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
			session.flush();
			
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
			
			Usuario user = session.get(Usuario.class, billete.getUsuario().getNif());
			user.addBilletes(billete);
			session.update(user);
			
			Recorrido rec= session.get(Recorrido.class, billete.getRecorrido().getID());
			rec.addBilletes(billete);
			rec.decreaseAvailableSeats(1);
			session.update(rec);
			
			session.flush();
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
		if (getBilletes(localizadorBillete).equals(new ArrayList<>()))
			throw new IllegalArgumentException("El billete con ese id no existe");
		
		Session session = getSession();
		
		try {
			session.beginTransaction();
			List<Billete> listaBilletes = session.createQuery("FROM Billete", Billete.class).getResultList();
			for(Billete b:listaBilletes) {
				session.delete(b);
				Usuario user = session.get(Usuario.class, b.getUsuario().getNif());
				user.removeBilletes(b);
				session.update(user);
				Recorrido rec = session.get(Recorrido.class, b.getRecorrido().getID());
				rec.removeBilletes(b);
				rec.increaseAvailableSeats(1);
				session.update(rec);
			}
			session.flush();
			
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
			
			session.update(billete);
			session.flush();
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
			List<Billete> recorridosList = session.createQuery("FROM Billete", Billete.class).getResultList();
			for(Billete b: recorridosList) {
				if(b.getLocalizador().equals(localizadorBilletes))lista.add(b);
			}
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
			List<Billete> recorridosList = session.createQuery("FROM Billete", Billete.class).getResultList();
			for(Billete s: recorridosList) {
				if(idRecorrido.equals(s.getRecorrido().getID()))lista.add(s);
			}
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
			List<Billete> recorridosList = session.createQuery("FROM Billete", Billete.class).getResultList();
			for(Billete s: recorridosList) {
				if(idUsuario.equals(s.getUsuario().getNif()))lista.add(s);
			}
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
