package com.slashserver.persistence;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.slashserver.model.Page;
import com.slashserver.model.PageSnippet;

@Repository
@Transactional
public class PageSnippetStore extends Dao<PageSnippet>{

	public PageSnippetStore(){
		super(PageSnippet.class);
	}

	@SuppressWarnings("unchecked")
	public List<PageSnippet> getForPage(int pageId){
		Query query = sessionFactory.getCurrentSession().createQuery("from PageSnippet where page.id = :pageId");
		query.setParameter("pageId", pageId);
		return query.list();
	}

	public void mergePageSnippets(Page pageToMerge, List<PageSnippet> pageSnippets) {
		List<PageSnippet> dbSnips = getForPage(pageToMerge.getId());
		
		List<PageSnippet> toRemove = new ArrayList<PageSnippet>();
		
		outer:for(PageSnippet dbSnip:dbSnips){
			for(PageSnippet nowSnip:pageSnippets){
				if(nowSnip.getId()==(dbSnip.getId())){
					continue outer;
				}
			}
			toRemove.add(dbSnip);
		}
		
		for(PageSnippet delete:toRemove){
			delete(delete.getId());
		}
		
		for(PageSnippet nowSnip:pageSnippets){
			store(nowSnip);
		}
		
	}
	

	
}
