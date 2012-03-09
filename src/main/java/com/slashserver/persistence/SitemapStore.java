package com.slashserver.persistence;


import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.slashserver.model.Sitemap;

@Repository
@Transactional
public class SitemapStore extends Dao<Sitemap>{

	public SitemapStore(){
		super(Sitemap.class);
	}

	
	@SuppressWarnings("unchecked")
	@Transactional
	public List<Sitemap> getAllFullyPopulated(){
		Query query = sessionFactory.getCurrentSession().createQuery( 
				"select distinct map from Sitemap map left outer join fetch map.sitemapPages pages left outer join fetch pages.pageByParentId left join fetch pages.pageByPageId order by map.name"
				);
		
		return query.list();		
		
	}
	
}
