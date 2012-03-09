package com.slashserver.json;

import java.util.Collections;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.slashserver.dummydata.DummyDataHelper;


@Controller
@RequestMapping("/dummydata")
public class DummyDataService {

	
	private DummyDataHelper helper;
 
	@RequestMapping(value="clear",method = RequestMethod.GET)
	public @ResponseBody Map<String,String> clear() throws Exception {

		helper.clear();

		return Collections.singletonMap("success", "true");
	}

	@RequestMapping(value="refdata",method = RequestMethod.GET)
	public @ResponseBody Map<String,String> refdata() throws Exception {

		
		
		helper.refData();

		return Collections.singletonMap("success", "true");
	}



	@RequestMapping(value="pagedata",method = RequestMethod.GET)
	public @ResponseBody Map<String,String> pagedata() throws Exception {

		
		
		helper.pageData();

		return Collections.singletonMap("success", "true");
	}
	
	@RequestMapping(value="otherdummysnippet")
	public void otherdummysnippet(HttpServletRequest request,HttpServletResponse response,@RequestParam int uniqueId) throws Exception {
		
		response.getWriter().print("<h2>Oh you want the other dummy snippet? You got the other dummy snippet</h2><p>" +
				"You wanna play rough? Ok ok, you fucking with me you fucking with the best!</p><div style='background-color:green'>asdsad asd asd asdasd asd adas dasd</div>");
		
	}
	
	
	@RequestMapping(value="ajaxDest")
	public void ajaxDest(HttpServletRequest request,HttpServletResponse response,@RequestParam String value2) throws Exception {
		
		response.getWriter().print("This is the ajax response, "+value2+" !!!!");
		
	}
	
	
	@RequestMapping(value="dummysnippet")
	public String dummysnippet(HttpServletRequest request,HttpServletResponse response) throws Exception {//@RequestParam int uniqueId
		
		return "dummysnippet";
		
	}
	
	@Autowired
	public void setHelper(DummyDataHelper helper) {
		this.helper = helper;
	}
}
