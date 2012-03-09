package com.slashserver.engine;

import java.util.List;
import java.util.Set;

import com.slashserver.engine.SpecialStringTokenizer.Tokens;

public class SnippetInjector {

	private SpecialStringTokenizer tokenizer= new SpecialStringTokenizer();
	
	public String popSnippetsIntoPage(String pageMarkup, Set<SnippetRenderingInfo> snippets){


		String matchStartString = "<snippet";
		String matchEndString = "</snippet>";
		
		Tokens tokens = tokenizer.match(pageMarkup, matchStartString, matchEndString,true);
		
		StringBuilder ret = new StringBuilder();
		
		for(int i=0;i<tokens.nonMatches.size();i++){
			String nonMatch = tokens.nonMatches.get(i);
			ret.append(nonMatch);
			
			if(tokens.matches.size()>i){
				String match = tokens.matches.get(i);
				//AM: convert match to xml and fish out the uuid.
				// hmm.. yeah could do but probably quicker just to split again.
				Tokens uuidMatch = tokenizer.match(match, "uuid=\"", "\"",false);
				String uuid = uuidMatch.matches.get(0);
				inner:for(SnippetRenderingInfo snip:snippets){
					if(snip.getUuid().equals(uuid)){
						ret.append(snip.getBodyContent());
						break inner;
					}
				}
			}
		}
		
		return ret.toString();
	}
	
}
