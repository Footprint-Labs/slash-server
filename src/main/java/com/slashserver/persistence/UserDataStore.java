package com.slashserver.persistence;


import java.util.HashSet;
import java.util.Set;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.slashserver.model.PageSnippet;
import com.slashserver.model.User;
import com.slashserver.model.UserData;

@Repository
@Transactional
public class UserDataStore extends Dao<UserData>{

	public UserDataStore(){
		super(UserData.class);
	}

	@SuppressWarnings("unchecked")
	public Set<UserData> getDataForPage(PageSnippet snippet,User user) {
		Query query = sessionFactory.getCurrentSession().createQuery(
				"from UserData where pageSnippet.id = :pageSnippetId and user.id = :userDataId");
		query.setInteger("pageSnippetId", snippet.getId());
		query.setInteger("userDataId", user.getId());
		
		
		return new HashSet<UserData>(query.list());
	}

}
