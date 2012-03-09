package com.slashserver.json;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.slashserver.model.PageTemplate;
import com.slashserver.persistence.PageTemplateStore;

@Controller
@RequestMapping("/json/pagetemplate")
public class PageTemplateService {

	private PageTemplateStore pageTemplateStore;

	// create
	@RequestMapping( method=RequestMethod.POST)
	public @ResponseBody Map<String, ? extends Object> createPage(@RequestBody String jsonParams) {
		
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> params = null;
		try {
			params = mapper.readValue(jsonParams, Map.class);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		PageTemplate newPageTemplate = new PageTemplate();
		newPageTemplate.setName((String)params.get("name"));
		newPageTemplate.setContent((String)params.get("content"));
		pageTemplateStore.store(newPageTemplate);
		
		return Collections.singletonMap("id", newPageTemplate.getId());
	}

	//update
	@RequestMapping(value="{id}", method=RequestMethod.PUT)
	public @ResponseBody Map<String, ? extends Object> updatePage(@PathVariable int id,@RequestParam String name,@RequestParam String content) {

		PageTemplate templateToMerge = pageTemplateStore.get(id);

		templateToMerge.setName(name);
		templateToMerge.setContent(content);
		pageTemplateStore.store(templateToMerge);
		
		return Collections.singletonMap("success", "yep!");
	}

	// delete
	@RequestMapping(value="{id}", method=RequestMethod.DELETE)
	public @ResponseBody Map<String, ? extends Object> deletePage(@PathVariable int id) {
		
		pageTemplateStore.delete(id);
		return Collections.singletonMap("success", "yep!");
	}

	// read
	@RequestMapping(value="{id}", method=RequestMethod.GET)
	public @ResponseBody PageTemplate getPage(@PathVariable int id) {
		PageTemplate pageTemplate = pageTemplateStore.get(id);
		// need to create a new object so as not to reference a lazy loader
		PageTemplate ret = new PageTemplate();
		ret.setId(pageTemplate.getId());
		ret.setName(pageTemplate.getName());
		ret.setContent(pageTemplate.getContent());
		return ret;
	}

	@RequestMapping(value="",method=RequestMethod.GET)
	public @ResponseBody List<Map<String, ? extends Object>> loadPages() {

		Collection<PageTemplate> pageTemplates = pageTemplateStore.getAll();
		List<Map<String, ? extends Object>> pageTemplatesToRet = new ArrayList<Map<String, ? extends Object>>();
		for(PageTemplate pageTemplate:pageTemplates){
			Map<String, Object> pageData = new HashMap<String, Object>();
			pageData.put("id", pageTemplate.getId());
			//pageData.put("content", pageTemplate.getContent());
			pageData.put("name", pageTemplate.getName());
			pageTemplatesToRet.add(pageData);
		}

		return pageTemplatesToRet;
	}

	@Autowired
	public void setPageTemplateStore(PageTemplateStore pageTemplateStore) {
		this.pageTemplateStore = pageTemplateStore;
	}

}
