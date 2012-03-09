package com.slashserver.persistence;


import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.slashserver.model.Page;

@Repository
@Transactional
public class PageStore extends Dao<Page>{

	public PageStore(){
		super(Page.class);
	}

	public Page getFullyPopulated(String pageUrl) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Page.class);
		criteria.setFetchMode("pageSnippets", FetchMode.JOIN);
		criteria.setFetchMode("pageSnippets.snippet", FetchMode.JOIN);
		criteria.setFetchMode("pageTemplate", FetchMode.JOIN);
		criteria.setFetchMode("role", FetchMode.JOIN);
		criteria.add( Restrictions.eq("url", pageUrl) );
		
		Page page =(Page) criteria.list().get(0);
		
		return page;
	}

	

	
}
