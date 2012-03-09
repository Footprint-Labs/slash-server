package com.slashserver.persistence;


import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.slashserver.model.Snippet;

@Repository
@Transactional
public class SnippetStore extends Dao<Snippet>{

	public SnippetStore(){
		super(Snippet.class);
	}

	
}
