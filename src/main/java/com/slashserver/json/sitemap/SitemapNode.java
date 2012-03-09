package com.slashserver.json.sitemap;

import java.util.ArrayList;
import java.util.List;

import com.slashserver.model.Sitemap;

public class SitemapNode {

	public String name;
	public int sitemapId;
	
	public List<SiteMapPageNode> children=new ArrayList<SiteMapPageNode>();
}
