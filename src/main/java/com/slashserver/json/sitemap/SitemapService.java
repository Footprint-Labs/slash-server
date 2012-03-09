package com.slashserver.json.sitemap;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.slashserver.model.Page;
import com.slashserver.model.Sitemap;
import com.slashserver.model.SitemapPage;
import com.slashserver.persistence.SitemapStore;

@Controller
@RequestMapping("/json/sitemap")
public class SitemapService {

	private SitemapStore store;

	Comparator<SiteMapPageNode> nodeCompare= new Comparator<SiteMapPageNode>() {
		
		public int compare(SiteMapPageNode a, SiteMapPageNode b) {
			return new Integer(a.index).compareTo(b.index);
		}
	};

	@RequestMapping(value="list", method=RequestMethod.GET)
	public @ResponseBody List<SitemapNode> getEmAll() {

		List<Sitemap> sitemaps = store.getAllFullyPopulated();

		List<SitemapNode> ret =new ArrayList<SitemapNode>();

		for(Sitemap sitemap:sitemaps){
			SitemapNode rootNode = prepareTree(sitemap);
			//serialUtils.clipAll(sitemap); 
			ret.add(rootNode);
		}

		return ret;
	}
	
	@RequestMapping(value="view", method=RequestMethod.GET)
	public @ResponseBody Sitemap getPage(@RequestParam int id) {
		Sitemap sitemap = store.get(id);
		return sitemap;
	}

	
	@RequestMapping(value="create", method=RequestMethod.GET)
	public @ResponseBody Map<String, ? extends Object> createSitemap(@RequestParam String name) {

		Sitemap sitemap = new Sitemap();
		sitemap.setName(name);
		store.store(sitemap);
		return Collections.singletonMap("id", sitemap.getId());
	}
	
	@RequestMapping(value="update", method=RequestMethod.GET)
	public @ResponseBody Map<String, ? extends Object> updateSitemap(@RequestParam int id,@RequestParam String name) {

		Sitemap sitemap = store.get(id);

		sitemap.setName(name);
		store.store(sitemap);
		
		return Collections.singletonMap("success", "yep!");
	}

	@RequestMapping(value="delete", method=RequestMethod.GET)
	public @ResponseBody Map<String, ? extends Object> deleteSitemap(@RequestParam int sitemapId) {

		store.delete(sitemapId);
		return Collections.singletonMap("success", "yeah");
	}
	
	private SitemapNode prepareTree(Sitemap sitemap) {
		SitemapNode rootNode = new SitemapNode();
		rootNode.sitemapId=sitemap.getId(); 
		rootNode.name=sitemap.getName(); 
		if(sitemap.getSitemapPages()!=null){
			//Root node has no parent.
			for(SitemapPage sitemapPage:sitemap.getSitemapPages()){
				if(sitemapPage.getPageByParentId()==null){
					//Rootnode!
					SiteMapPageNode child = makeChild(sitemap, sitemapPage);
					rootNode.children.add(child);
				}
			}
			Collections.sort(rootNode.children,nodeCompare);
		}
		return rootNode;
	}

	//TODO bit of a hack here to get round refactering the js tree component hence the duplication here
	private SiteMapPageNode makeChild(Sitemap sitemap, SitemapPage sitemapPage) {
		SiteMapPageNode child = new SiteMapPageNode();
		Page page=sitemapPage.getPageByPageId();
		child.name=page.getName();
		child.title=page.getName();
		child.pageId=page.getId();
		child.key=page.getId();
		child.sitemapPageId=sitemapPage.getId();
		child.index=sitemapPage.getIndex()!=null?sitemapPage.getIndex():1;
		child.children = getChildren(sitemap, page);
		return child;
	}

	private List<SiteMapPageNode> getChildren(Sitemap sitemap,  Page parent) {
		List<SiteMapPageNode> ret = new ArrayList<SiteMapPageNode>();
		for(SitemapPage sitemapPage:sitemap.getSitemapPages()){
			if(sitemapPage.getPageByParentId()==parent){
				SiteMapPageNode child = makeChild(sitemap, sitemapPage);
				ret.add(child);
			}
		}
		Collections.sort(ret, nodeCompare);
		return ret;
	}

	@Autowired
	public void setStore(SitemapStore sitemapStore) {
		this.store = sitemapStore;
	}

}
