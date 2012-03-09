package com.slashserver.persistence;


import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.slashserver.model.Sitemap;
import com.slashserver.model.SitemapPage;

@Repository
@Transactional
public class SitemapPageStore extends Dao<SitemapPage>{

	public SitemapPageStore(){
		super(SitemapPage.class);
	}

	
	@Transactional
	public void store(SitemapPage toPersist, int index) {
		//Fetch all sitemappages with same sitemap and parent, ordered by index.
		Query query = sessionFactory.getCurrentSession().createQuery(
				"from SitemapPage sitepage " +
					"where sitepage.sitemap.id = :sitemapId " +
					(toPersist.getPageByParentId()!=null?" and sitepage.pageByParentId.id = :parentId ":" and sitepage.pageByParentId is null")+
					" order by sitepage.index ");
		if(toPersist.getPageByParentId()!=null){
			query.setInteger("parentId", toPersist.getPageByParentId().getId());
		}
		query.setInteger("sitemapId", toPersist.getSitemap().getId());
		@SuppressWarnings("unchecked")
		List<SitemapPage> list = query.list();
		
		//insert toPersist in appropriate location (or move if already there)
		index-=1;
		if(index>list.size()){
			index=list.size();
		}
		if(index<0){
			index=0;
		}
		list.add(index, toPersist);
		
		int indexCount=1;
		//set order index on each page.
		for(SitemapPage sitepage:list){
			if(sitepage!=toPersist&&toPersist.getId()==sitepage.getId()){
				//This is where we are updating existing record and so we ahve it twice in list..
				//	ignore second one.
				
			}else{
				sitepage.setIndex(indexCount);
				indexCount++;
			}
		}
		
		
		sessionFactory.getCurrentSession().merge(toPersist);
	}
	
	
	
}
