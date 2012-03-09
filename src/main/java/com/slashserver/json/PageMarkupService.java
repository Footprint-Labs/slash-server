package com.slashserver.json;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.slashserver.model.Page;
import com.slashserver.model.PageSnippet;
import com.slashserver.model.Snippet;
import com.slashserver.persistence.PageSnippetStore;
import com.slashserver.persistence.PageStore;


@Controller
@RequestMapping("/json/pageMarkup")
@SessionAttributes({"pageSnippets"})
public class PageMarkupService {

	private PageStore pageStore;
	private PageSnippetStore pageSnippetStore;

	@ModelAttribute("pageSnippets")
	public Hashtable<Integer,List<PageSnippet>> populateForm() {
		return new Hashtable<Integer, List<PageSnippet>>();
	}

	@RequestMapping(value="load")
	public  @ResponseBody  Page loadMarkup(@RequestParam int id,@ModelAttribute("pageSnippets") Hashtable<Integer,List<PageSnippet>> pageSnippets){
		Page pageToMerge = pageStore.get(id);
		//Set up list of page snippets into session that we can manipulate and then commit later.
		List<PageSnippet> snippets = pageSnippetStore.getForPage(id);
		pageSnippets.put(id,snippets);
		return pageToMerge;
	}

	@RequestMapping(value="addSnippet")
	public  @ResponseBody  Map<String, ? extends Object> addPageSnippet(@RequestParam int pageId,@RequestParam int snippetId,@ModelAttribute("pageSnippets") Hashtable<Integer,List<PageSnippet>> pageSnippets){
		UUID randomUUID = UUID.randomUUID();
		PageSnippet pageSnippet = new PageSnippet();
		pageSnippet.setPage(new Page(pageId));
		pageSnippet.setSnippet(new Snippet(snippetId));
		pageSnippet.setUuid(randomUUID.toString());
		pageSnippets.get(pageId).add(pageSnippet);
		return Collections.singletonMap("uuid", randomUUID.toString());
	}

	@RequestMapping(value="removeSnippet")
	public @ResponseBody Map<String, ? extends Object> deletePageSnippet(@RequestParam int pageId,@RequestParam String uuid,@ModelAttribute("pageSnippets") Hashtable<Integer,List<PageSnippet>> pageSnippets){
		List<PageSnippet> snippets =pageSnippets.get(pageId); 
		for(int i=0;i<snippets.size();i++){
			PageSnippet snippet = snippets.get(i);
			if(snippet.getUuid().equals(uuid)){
				//we got a match
				snippets.remove(i);
				break;
			}
		}

		return Collections.singletonMap("success", "yep!");
	}


	@RequestMapping("commit")
	public @ResponseBody Map<String, ? extends Object> commit(@RequestParam int id,@RequestParam String markup,@ModelAttribute("pageSnippets") Hashtable<Integer,List<PageSnippet>> pageSnippets) {

		Page pageToMerge = pageStore.get(id);

		pageSnippetStore.mergePageSnippets(pageToMerge,pageSnippets.get(id));
		try {
			pageToMerge.setMarkup(URLDecoder.decode(markup, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			// think we're okay to swallow this one
			e.printStackTrace();
		}


		pageStore.store(pageToMerge);
		return Collections.singletonMap("success", "yep!");
	}

	@Autowired
	public void setPageStore(PageStore pageStore) {
		this.pageStore = pageStore;
	}

	@Autowired
	public void setPageSnippetStore(PageSnippetStore pageStore) {
		this.pageSnippetStore = pageStore;
	}


}
