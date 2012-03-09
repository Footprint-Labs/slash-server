package com.slashserver.persistence;


import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.slashserver.model.User;

@Repository
@Transactional
public class UserStore extends Dao<User>{

	public UserStore(){
		super(User.class);
	}

	public User getByName(String name){
		Query query = sessionFactory.getCurrentSession().createQuery("from User where name =:name");
		query.setParameter("name", name);
		return (User)query.list().get(0);
	}
}
