package com.slashserver.persistence;


import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.slashserver.model.Role;
import com.slashserver.model.Snippet;

@Repository
@Transactional
public class RoleStore extends Dao<Role>{

	public RoleStore(){
		super(Role.class);
	}
	
	public Role getByName(String name){
		Query query = sessionFactory.getCurrentSession().createQuery("from Role where name =:name");
		query.setParameter("name", name);
		return (Role)query.list().get(0);
	}

	
}
