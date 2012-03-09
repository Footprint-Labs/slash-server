
package com.slashserver.json;

import java.util.Collections;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * You can try this out by hitting localhost:8080/slashserver/service_demo/test
 */
@Controller
@RequestMapping("/ajax")
public class AjaxTest {

	

	@RequestMapping(value="test", method=RequestMethod.GET)
	public @ResponseBody DummyRet doTheTest(@RequestParam String name) {
	    
	    DummyRet ret = new DummyRet();
	    ret.setName("Server did soemthing plus... "+name);
	    return ret;
	}
	
	
	@RequestMapping(value="maptest", method=RequestMethod.GET)
	public @ResponseBody Map<String, ? extends Object> returnConvertedMap(@RequestParam String param1) {
	    
	    return Collections.singletonMap("answer","Server did something plus... "+param1);
	    
	}


	
}
