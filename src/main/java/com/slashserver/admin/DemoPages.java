package com.slashserver.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/service_demo")
public class DemoPages {

	@RequestMapping("dummy_data")
	public String dummyData() {
		return "/service_demo/dummy_data";
	}
	
	
	@RequestMapping("page")
	public String page() {
		return "/service_demo/page";
	}
	
	@RequestMapping("sitemap")
	public String sitemap() {
		return "/service_demo/sitemap";
	}
	
	@RequestMapping("test")
	public String test() {
		return "/service_demo/test";
	}
}
