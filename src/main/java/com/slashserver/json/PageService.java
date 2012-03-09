package com.slashserver.json;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.slashserver.model.Page;
import com.slashserver.model.PageTemplate;
import com.slashserver.model.Sitemap;
import com.slashserver.model.SitemapPage;
import com.slashserver.persistence.PageStore;
import com.slashserver.persistence.SitemapPageStore;
import com.slashserver.persistence.SitemapStore;

@Controller
@RequestMapping("/json/page")
public class PageService {

	private PageStore pageStore;
	private SitemapStore sitemapStore;
	private SitemapPageStore sitemapPageStore;

	@RequestMapping(value="create", method=RequestMethod.GET)
	public @ResponseBody Map<String, ? extends Object> createPage(@RequestParam String url,@RequestParam String name,@RequestParam String sitemapId) {

		Page newPage = new Page();

		newPage.setUrl(url);
		newPage.setName(name);

		pageStore.store(newPage);
		if(url==null||url.equals("")){
			// default a unique value
			url="page_"+newPage.getId();
			newPage.setUrl(url);
			pageStore.store(newPage);
		}
		Sitemap currentSitemap = sitemapStore.get(Integer.parseInt(sitemapId));

		SitemapPage newSitemapPage = new SitemapPage();
		newSitemapPage.setPageByPageId(newPage);
		newSitemapPage.setSitemap(currentSitemap);

		Collection<SitemapPage> sitePages = sitemapPageStore.getAll();
		int maxIndex = 0 ;

		Iterator<SitemapPage> iterator = sitePages.iterator();
		// need to find the biggest index to ensure the new page goes on the bottom
		while (iterator.hasNext()) {
			SitemapPage sp = iterator.next();

			if (sp.getIndex()!=null 
					&& sp.getSitemap().getId()== Integer.parseInt(sitemapId) 
					&& sp.getIndex()>maxIndex){
				maxIndex=sp.getIndex();
			}
		}

		sitemapPageStore.store(newSitemapPage,maxIndex+1);
		return Collections.singletonMap("id", newPage.getId());
	}

	@Transactional
	@RequestMapping(value="movePage", method=RequestMethod.GET)
	public @ResponseBody Map<String, ? extends Object> movePage(@RequestParam int sitemapPageId,@RequestParam int sitemapPageIdNewLocation,@RequestParam String relationship) {

		SitemapPage sitemapPage = sitemapPageStore.get(sitemapPageId);
		SitemapPage newLocation = sitemapPageStore.get(sitemapPageIdNewLocation);

		Integer index = newLocation.getIndex();
		if(relationship.equalsIgnoreCase("before")){
			sitemapPage.setPageByParentId(newLocation.getPageByParentId());
			sitemapPage.setSitemap(newLocation.getSitemap());
			sitemapPageStore.store(sitemapPage, index);
		}else if (relationship.equalsIgnoreCase("after")){
			sitemapPage.setPageByParentId(newLocation.getPageByParentId());
			sitemapPage.setSitemap(newLocation.getSitemap());
			sitemapPageStore.store(sitemapPage, index+1);

		}else{
			//We've got an 'over'
			sitemapPage.setPageByParentId(newLocation.getPageByPageId());
			sitemapPage.setSitemap(newLocation.getSitemap());
			sitemapPageStore.store(sitemapPage, 1000);

		}
		//AMLOW: shift child pages too in case of sitemap shift.

		return Collections.singletonMap("success", "yeah");
	}



	//	@RequestMapping(value="createForSitemap", method=RequestMethod.GET)
	//	public @ResponseBody Map<String, ? extends Object> createPageForSiteMap(@RequestParam String name,@RequestParam int sitemapId,@RequestParam int orderIndex) {
	//		return createChildForPage(name, null, sitemapId,orderIndex);
	//	}
	//
	//
	//
	//	//AMDO: sort TX. (tag doesnt seem to work at controller level properly)
	//	@RequestMapping(value="createChildForPage", method=RequestMethod.GET)
	//	public @ResponseBody Map<String, ? extends Object> createChildForPage(@RequestParam String name,@RequestParam Integer pageId,@RequestParam int sitemapId,@RequestParam int orderIndex) {
	//
	//		Page newPage = new Page();
	//		newPage.setUrl("whocares"+new Date());
	//		newPage.setName(name);
	//		pageStore.store(newPage);
	//
	//		SitemapPage newSitemapPage = new SitemapPage();
	//		newSitemapPage.setSitemap(sitemapStore.get(sitemapId));
	//		newSitemapPage.setPageByPageId(newPage);
	//		if(pageId!=null){
	//			newSitemapPage.setPageByParentId(pageStore.get(pageId));
	//		}
	//
	//		sitemapPageStore.store(newSitemapPage,orderIndex);
	//
	//		Map<String, Integer> ret= new HashMap<String, Integer>();
	//		ret.put("sitemap_page_id",(Integer) newSitemapPage.getId());
	//		ret.put("page_id",(Integer)newPage.getId());
	//		return ret;
	//	}

	@RequestMapping(value="update", method=RequestMethod.GET)
	public @ResponseBody Map<String, ? extends Object> updatePage(@RequestParam int id,
			@RequestParam String url,@RequestParam String name,@RequestParam int templateId) {

		Page pageToMerge = pageStore.get(id);
		pageToMerge.setUrl(url);
		pageToMerge.setName(name);
		pageToMerge.setId(id);
		
		PageTemplate wrapper = new PageTemplate();
		wrapper.setId(templateId);
		pageToMerge.setPageTemplate(wrapper);

		pageStore.store(pageToMerge);
		return Collections.singletonMap("success", "yep!");
	}


	

	@RequestMapping(value="delete", method=RequestMethod.GET)
	public @ResponseBody Map<String, ? extends Object> deletePage(@RequestParam int id) {
		Collection<SitemapPage> sitemapPages = sitemapPageStore.getAll();

		// first remove all the pages from the sitemaps
		for(SitemapPage sitemapPage:sitemapPages){
			if(sitemapPage.getPageByPageId().getId()==id){
				sitemapPageStore.delete(sitemapPage.getId());
			}
		}
		// then delete the page
		pageStore.delete(id);
		return Collections.singletonMap("success", "yep!");
	}

	@RequestMapping(value="view", method=RequestMethod.GET)
	public @ResponseBody Page getPage(@RequestParam int id) {
		Page page = pageStore.get(id);
		//serialUtils.clipAll(page);
		return page;
	}

	@RequestMapping(value="viewcomplete", method=RequestMethod.GET)
	public @ResponseBody Map<String, ? extends Object> getPageComplete(@RequestParam int id) {
		Page page = pageStore.get(id);
		//serialUtils.clipAll(page);
		Map<String, Object> pageData = new HashMap<String, Object>();
		pageData.put("id", page.getId());
		pageData.put("url", page.getUrl());
		pageData.put("name", page.getName());
		pageData.put("markup", page.getMarkup());
		if(page.getPageTemplate()!=null){
			pageData.put("templateId", page.getPageTemplate().getId());
		}
		return pageData;
	}

	@RequestMapping(value="list", method=RequestMethod.GET)
	public @ResponseBody Map<String, ? extends Object> loadPages() {

		Collection<Page> pages = pageStore.getAll();
		List<Map<String, ? extends Object>> pagesToRet = new ArrayList<Map<String, ? extends Object>>();
		for(Page page:pages){
			Map<String, Object> pageData = new HashMap<String, Object>();
			pageData.put("id", page.getId());
			pageData.put("url", page.getUrl());
			pageData.put("name", page.getName());
			pagesToRet.add(pageData);
		}

		return Collections.singletonMap("pages",pagesToRet);
	}

	@Autowired
	public void setPageStore(PageStore pageStore) {
		this.pageStore = pageStore;
	}


	/*	@Autowired
	public void setSerialUtils(SerialUtils serialUtils) {
		this.serialUtils = serialUtils;
	}*/

	@Autowired
	public void setSitemapPageStore(SitemapPageStore sitemapPageStore) {
		this.sitemapPageStore = sitemapPageStore;
	}

	@Autowired
	public void setSitemapStore(SitemapStore sitemapStore) {
		this.sitemapStore = sitemapStore;
	}



}
