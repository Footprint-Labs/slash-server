package com.slashserver.json;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.slashserver.model.Snippet;
import com.slashserver.model.SnippetGroup;
import com.slashserver.persistence.SnippetGroupStore;
import com.slashserver.persistence.SnippetStore;

@Controller
@RequestMapping("/json/snippet")
public class SnippetService {

	private SnippetStore snippetStore;
	private SnippetGroupStore snippetGroupStore;

	@RequestMapping(value="create", method=RequestMethod.GET)
	public @ResponseBody Map<String, ? extends Object> createSnippet(@RequestParam String url,
																@RequestParam String name,
																@RequestParam int snippetGroupId) {

		Snippet snippet = new Snippet();
		snippet.setName(name);
		try {
			snippet.setUrl(URLDecoder.decode(url, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			// think we're okay to swallow this one
			e.printStackTrace();
		}
		SnippetGroup wrapper = new SnippetGroup();
		wrapper.setId(snippetGroupId);
		snippet.setSnippetGroup(wrapper);
		
		snippetStore.store(snippet);
		
		return Collections.singletonMap("id", snippet.getId());
	}
	
	@RequestMapping(value="update", method=RequestMethod.GET)
	public @ResponseBody Map<String, ? extends Object> updateGroup(@RequestParam int id,@RequestParam String name,@RequestParam String url,@RequestParam int snippetGroupId) {

		Snippet snippetToMerge = snippetStore.get(id);
		snippetToMerge.setName(name);
		
		SnippetGroup wrapper = new SnippetGroup();
		wrapper.setId(snippetGroupId);
		snippetToMerge.setSnippetGroup(wrapper);
		
		try {
			snippetToMerge.setUrl(URLDecoder.decode(url, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			// think we're okay to swallow this one
			e.printStackTrace();
		}
		snippetStore.store(snippetToMerge);
		return Collections.singletonMap("success", "yep!");
	}
	
	@RequestMapping(value="delete", method=RequestMethod.GET)
	public @ResponseBody Map<String, ? extends Object> deleteSnippet(@RequestParam int id) {
		
		snippetStore.delete(id);
		return Collections.singletonMap("success", "yep!");
	}

	@RequestMapping(value="list", method=RequestMethod.GET)
	public @ResponseBody Map<String, ? extends Object> loadPages() {

		Collection <SnippetGroup> snippetGroups = snippetGroupStore.getAll();
		
		List<Map<String, ? extends Object>> groupsToRet = new ArrayList<Map<String, ? extends Object>>();
		for(SnippetGroup snippetGroup:snippetGroups){
			Map<String, Object> groupData = new HashMap<String, Object>();
			groupData.put("id", snippetGroup.getId());
			groupData.put("name", snippetGroup.getName());
			//groupData.put("snippets", snippetGroup.getSnippets());
			groupsToRet.add(groupData);
		}
		
		return Collections.singletonMap("groups",groupsToRet);
	}
	
	@RequestMapping(value="allingroup", method=RequestMethod.GET)
	public @ResponseBody Map<String, ? extends Object> loadSnippets(@RequestParam int groupId) {

		Collection <Snippet> snippets = snippetStore.getAll();
		snippetStore.getAll();
		
		List<Map<String, ? extends Object>> snippetsToRet = new ArrayList<Map<String, ? extends Object>>();
		for(Snippet snippet:snippets){
			if(snippet.getSnippetGroup()!=null && snippet.getSnippetGroup().getId()==groupId){
				Map<String, Object> snippetData = new HashMap<String, Object>();
				snippetData.put("id", snippet.getId());
				snippetData.put("name", snippet.getName());
				snippetData.put("url", snippet.getUrl());
				//groupData.put("snippets", snippetGroup.getSnippets());
				snippetsToRet.add(snippetData);
			}
		}	
		return Collections.singletonMap("snippets",snippetsToRet);
	}
	
	@Autowired
	public void setSnippetStore(SnippetStore snippetStore) {
		this.snippetStore = snippetStore;
	}
	
	@Autowired
	public void setSnippetGroupStore(SnippetGroupStore snippetGroupStore) {
		this.snippetGroupStore = snippetGroupStore;
	}

}
