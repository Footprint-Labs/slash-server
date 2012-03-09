package com.slashserver.json.sitemap;

import java.util.ArrayList;
import java.util.List;

import com.slashserver.model.Page;

public class SiteMapPageNode {

	public String name;
	public String title;
	public int pageId;
	public int key;
	public int sitemapPageId;
	public int index;
	
	public List<SiteMapPageNode> children=new ArrayList<SiteMapPageNode>();
	
	
}
