package com.slashserver.persistence;


import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.slashserver.model.SnippetGroup;

@Repository
@Transactional
public class SnippetGroupStore extends Dao<SnippetGroup>{

	
	
	public SnippetGroupStore(){
		super(SnippetGroup.class);
	}

}
