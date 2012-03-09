package com.slashserver.persistence;


import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.slashserver.model.PageTemplate;

@Repository
@Transactional
public class PageTemplateStore extends Dao<PageTemplate>{

	public PageTemplateStore(){
		super(PageTemplate.class);
	}

	
}
