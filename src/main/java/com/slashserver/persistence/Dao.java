package com.slashserver.persistence;

import java.util.Collection;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.slashserver.model.HasId;


public class Dao<T extends HasId> {

	protected SessionFactory sessionFactory;
	protected Class<T> clazz;

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	public Dao(Class<T> clazz){
		this.clazz=clazz;
	}


	@Transactional(readOnly = true)
	@SuppressWarnings("unchecked")
	public Collection<T> getAll(String sortField) {
		return sessionFactory.getCurrentSession().createQuery("from "+clazz.getSimpleName()+" order by "+sortField).list();
	}
	
	@Transactional
	public Collection<T> getAll() {
		return getAll("id");
	}


	@Transactional
	public void store(T toPersist) {
		sessionFactory.getCurrentSession().merge(toPersist);
	}


	@SuppressWarnings("unchecked")
	@Transactional
	public T get(int id) {
		return (T) sessionFactory.getCurrentSession().get(clazz, id);
	}

	
	@Transactional
	public void delete(int id) {
//		HasId newInstance;
//		try {
//			newInstance = clazz.newInstance();
//		} catch (Exception e) {
//			throw new RuntimeException(e);
//		}
//		newInstance.setId(id);
//		sessionFactory.getCurrentSession().delete(newInstance);
		T toDelete = get(id);
		if(toDelete!=null){
			sessionFactory.getCurrentSession().delete(toDelete);
		}
		
	}


}
