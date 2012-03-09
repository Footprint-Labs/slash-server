package com.slashserver.json;

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
@RequestMapping("/json/snippetgroup")
public class SnippetGroupService {

	private SnippetStore snippetStore;
	private SnippetGroupStore snippetGroupStore;

	@RequestMapping(value="create", method=RequestMethod.GET)
	public @ResponseBody Map<String, ? extends Object> createSnippet(@RequestParam String name) {
		SnippetGroup group = new SnippetGroup();
		group.setName(name);
		snippetGroupStore.store(group);
		return Collections.singletonMap("id", group.getId());
	}
	
	@RequestMapping(value="update", method=RequestMethod.GET)
	public @ResponseBody Map<String, ? extends Object> updateGroup(@RequestParam int id,@RequestParam String name) {

		SnippetGroup groupToMerge = snippetGroupStore.get(id);
		groupToMerge.setName(name);
		snippetGroupStore.store(groupToMerge);
		return Collections.singletonMap("success", "yep!");
	}
	
	@RequestMapping(value="delete", method=RequestMethod.GET)
	public @ResponseBody Map<String, ? extends Object> deleteGroup(@RequestParam int id) {
		
		snippetGroupStore.delete(id);
		return Collections.singletonMap("success", "yep!");
	}

	@RequestMapping(value="list", method=RequestMethod.GET)
	public @ResponseBody Map<String, ? extends Object> loadGroup() {

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

		List<Map<String, ? extends Object>> snippetsToRet = new ArrayList<Map<String, ? extends Object>>();
		for(Snippet snippet:snippets){
			if(snippet.getSnippetGroup().getId()==groupId){
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
	
	@RequestMapping(value="allsnippets", method=RequestMethod.GET)
	public @ResponseBody Map<String, ? extends Object> loadSnippets() {

		List<Map<String, ? extends Object>> groupsToRet = new ArrayList<Map<String, ? extends Object>>();
		Collection <SnippetGroup> snippetGroups = snippetGroupStore.getAll();
		
		
		Collection <Snippet> snippets = snippetStore.getAll();
		
		for(SnippetGroup snippetGroup:snippetGroups){
			Map<String, Object> groupData = new HashMap<String, Object>();
			groupData.put("id", snippetGroup.getId());
			groupData.put("name", snippetGroup.getName());
			
			// get the snippets for this group
			List<Map<String, ? extends Object>> snippetsToRet = new ArrayList<Map<String, ? extends Object>>();
			for(Snippet snippet:snippets){
				if(snippet.getSnippetGroup()!=null && snippet.getSnippetGroup().getId()==snippetGroup.getId()){
					Map<String, Object> snippetData = new HashMap<String, Object>();
					snippetData.put("id", snippet.getId());
					snippetData.put("name", snippet.getName());
					snippetData.put("url", snippet.getUrl());
					//groupData.put("snippets", snippetGroup.getSnippets());
					snippetsToRet.add(snippetData);
				}
			}
			groupData.put("snippets", snippetsToRet);
			
			groupsToRet.add(groupData);
		}
		
		return Collections.singletonMap("groups",groupsToRet);
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
