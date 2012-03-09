package com.slashserver.engine;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.CookiePolicy;
import java.net.URI;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class AjaxProxy {


	private static List<String> skipHeaders = new Vector<String>();

	static{
		skipHeaders.add("Content-Length");
		skipHeaders.add("content-length");
	}

	@RequestMapping("/ajax")
	public void serve(@RequestParam("pipeTo") String pageUrl, HttpServletRequest request,HttpServletResponse response) throws Exception {

		//Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		//UserDetails details = ((UserDetails)principal); 

		//AMDO: check a whitelist of accesible servers?

		//CookieStore cookieStore = new BasicCookieStore();
		//HttpContext localContext = new BasicHttpContext();

		//AMDO: set up connection manager too?
		
		HttpParams params = new BasicHttpParams();
		params.setBooleanParameter(ClientPNames.HANDLE_REDIRECTS, true);
		params.setIntParameter(ClientPNames.MAX_REDIRECTS,10);
		
		
		DefaultHttpClient client = new DefaultHttpClient(params);
		//client.getParams().setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true); 
		//localContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);

		//AMLOW: prep the client properly, use as singleton.

		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

		@SuppressWarnings("rawtypes")
		Enumeration parameterNames = request.getParameterNames();
		while(parameterNames.hasMoreElements()){
			String paramName = (String)parameterNames.nextElement();
			//System.out.println("Setting request param "+paramName+" to "+request.getParameter(paramName));
			nameValuePairs.add(new BasicNameValuePair(paramName, request.getParameter(paramName)));

		}
		
		HttpRequestBase message = null;
		if(request.getMethod().equals("GET")){
			
			String uri = addQueryString(pageUrl, nameValuePairs);
			message=new HttpGet(uri);
			
		}else if(request.getMethod().equals("DELETE")){
			String uri = addQueryString(pageUrl, nameValuePairs);
			message = new HttpDelete(uri);
			
		}else if(request.getMethod().equals("PUT")){
			message=new HttpPut(pageUrl);
			((HttpPut)message).setEntity(new UrlEncodedFormEntity(nameValuePairs));
		}else{
			message=new HttpPost(pageUrl);
			((HttpPost)message).setEntity(new UrlEncodedFormEntity(nameValuePairs));
		}

		//Pass through headers..
		@SuppressWarnings("unchecked")
		Enumeration<String> headers=(Enumeration<String>)request.getHeaderNames();
		while(headers.hasMoreElements()){
			String headerName=(String)headers.nextElement();
			if(skipHeaders.contains(headerName)){
				continue;
			}
			//System.out.println("Setting header: '"+headerName+"' to "+request.getHeader(headerName));
			message.setHeader(headerName,request.getHeader(headerName));

		}


		HttpResponse ajaxResponse = client.execute(message);//,localContext);
		BufferedReader rd = new BufferedReader(new InputStreamReader(
				ajaxResponse.getEntity().getContent()));

		StringBuilder output = new StringBuilder();
		String line=null;
		while ((line = rd.readLine()) != null) {
			output.append(line).append("\n");
		}

		response.getWriter().print(output.toString());
		response.getWriter().flush();

	}

	private String addQueryString(String pageUrl, List<NameValuePair> nameValuePairs) {
		String queryString = URLEncodedUtils.format(nameValuePairs, "UTF-8");
		String uriNow = pageUrl;
		if(uriNow.indexOf("?")==-1){
			uriNow+="?";
		}else{
			uriNow+="&";
		}
		String uri = uriNow+queryString;
		return uri;
	}




}
