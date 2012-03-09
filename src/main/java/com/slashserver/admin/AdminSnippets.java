package com.slashserver.admin;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.slashserver.json.PageTemplateService;
import com.slashserver.json.SnippetGroupService;

@Controller
@RequestMapping("/snippets/admin")
public class AdminSnippets {
	
	private SnippetGroupService snippetGroupService;
	private PageTemplateService pageTemplateService;

	@RequestMapping("page_layout_manager")
	public String pageLayoutManager(HttpServletRequest request) {
		request.setAttribute("snippets", snippetGroupService.loadSnippets());
		return "/snippets/admin/page_layout_manager";
	}
	
	
	@RequestMapping("sitemap_admin")
	public String sitemapAdmin() {
		return "/snippets/admin/sitemap_admin";
	}
	
	@RequestMapping("sitemap")
	public String sitemap(HttpServletRequest request) {
		request.setAttribute("templates", pageTemplateService.loadPages());
		return "/snippets/admin/sitemap";
	}
	
	
	@RequestMapping("snippets")
	public String snippets() {
		return "/snippets/admin/snippets";
	}
	
	@RequestMapping("template")
	public String template() {
		return "/snippets/admin/template";
	}
	
	@RequestMapping("templatev2")
	public String templatev2() {
		return "/snippets/admin/templatev2";
	}
	
	@RequestMapping("templatev3")
	public String templatev3() {
		return "/snippets/admin/templatev3";
	}
	
	@RequestMapping("slashbar")
	public String slashbar() {
		return "/snippets/admin/slashbar";
	}
	
	@Autowired
	public void setSnippetGroupService(SnippetGroupService snippetGroupService) {
		this.snippetGroupService = snippetGroupService;
	}
	
	@Autowired
	public void setPageTemplateService(PageTemplateService pageTemplateService) {
		this.pageTemplateService = pageTemplateService;
	}
}
