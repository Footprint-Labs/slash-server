package com.slashserver.engine;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.CookiePolicy;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Set;
import java.util.zip.GZIPInputStream;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.entity.HttpEntityWrapper;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

import com.slashserver.engine.SpecialStringTokenizer.Tokens;
import com.slashserver.model.PageSnippet;
import com.slashserver.model.UserData;

@Component
public class SnippetHttpClient {

	SpecialStringTokenizer tokenizer = new SpecialStringTokenizer();

	public SnippetRenderingInfo fetchSnippetContent(PageSnippet snippet,Set<UserData> userData,HttpServletRequest request) throws Exception{

		HttpParams params = new BasicHttpParams();
		params.setBooleanParameter(ClientPNames.HANDLE_REDIRECTS, true);
		params.setIntParameter(ClientPNames.MAX_REDIRECTS,10);


		DefaultHttpClient client = new DefaultHttpClient(params);
		//AMLOW: prep the client properly, use as singleton.




		try {
			client.addRequestInterceptor(new HttpRequestInterceptor() {

				public void process(
						final HttpRequest request,
						final HttpContext context) throws HttpException, IOException {
					if (!request.containsHeader("Accept-Encoding")) {
						request.addHeader("Accept-Encoding", "gzip");
					}
				}

			});


			client.addResponseInterceptor(new HttpResponseInterceptor() {

				public void process(
						final HttpResponse response,
						final HttpContext context) throws HttpException, IOException {
					HttpEntity entity = response.getEntity();
					Header ceheader = entity.getContentEncoding();
					if (ceheader != null) {
						HeaderElement[] codecs = ceheader.getElements();
						for (int i = 0; i < codecs.length; i++) {
							if (codecs[i].getName().equalsIgnoreCase("gzip")) {
								response.setEntity(
										new GzipDecompressingEntity(response.getEntity()));
								return;
							}
						}
					}
				}

			});


//			HttpGet httpget = new HttpGet("http://www.apache.org/");
//
//			// Execute HTTP request
//			System.out.println("executing request " + httpget.getURI());
//			HttpResponse response = client.execute(httpget);
//
//			System.out.println("----------------------------------------");
//			System.out.println(response.getStatusLine());
//			System.out.println(response.getLastHeader("Content-Encoding"));
//			System.out.println(response.getLastHeader("Content-Length"));
//			System.out.println("----------------------------------------");
//
//			HttpEntity entity = response.getEntity();
//
//			if (entity != null) {
//				String content = EntityUtils.toString(entity);
//				System.out.println(content);
//				System.out.println("----------------------------------------");
//				System.out.println("Uncompressed size: "+content.length());
//			}






			HttpPost post = new HttpPost(
					snippet.getSnippet().getUrl());
			//Pass through headers..
			@SuppressWarnings("unchecked")
			Enumeration<String> headers=(Enumeration<String>)request.getHeaderNames();
			while(headers.hasMoreElements()){
				String headerName=(String)headers.nextElement();
				post.setHeader(headerName,request.getHeader(headerName));

			}


			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(userData.size());
			nameValuePairs.add(new BasicNameValuePair("uniqueId", ""+snippet.getId()));
			for(UserData data:userData){
				nameValuePairs.add(new BasicNameValuePair(data.getName(), data.getValue()));
			}

			//AM: note params override saved data.. is this correct, shall we namespace, or what?
			Enumeration parameterNames = request.getParameterNames();
			while(parameterNames.hasMoreElements()){
				String paramName =(String)parameterNames.nextElement();
				nameValuePairs.add(new BasicNameValuePair(paramName, request.getParameter(paramName)));
			}


			post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			HttpResponse response = client.execute(post);
			//BufferedReader rd = new BufferedReader(new InputStreamReader(
			//		response.getEntity().getContent()));
			String content = EntityUtils.toString(response.getEntity());

			StringBuilder output = new StringBuilder();
			String line=null;
			System.out.println("\n\n\n\nGetting for '"+snippet.getSnippet().getUrl()+"'\n");
//AMDO: temp			while ((line = rd.readLine()) != null) {
//				if(line.indexOf("jquery.min.js")>-1){
//					//we dont want people overriding jquery.. dirty way to skip it. //AMLOW improve.
//					continue;
//
//				}
//				System.out.println(line);
//				output.append(line).append("\n");
//			}
			
			System.out.println(content);
			output.append(content).append("\n");
			
			System.out.println("\nDone for '"+snippet.getSnippet().getUrl()+"'\n\n\n\n");

			//AMLOW bit convulted just to strip the html tags and possible cockup the page..
			Tokens pageMatch = tokenizer.match(output.toString(),"<html>","</html>",false);
			pageMatch.matches.addAll(pageMatch.nonMatches);
			String page = tokenizer.concatenate(pageMatch.matches);

			Tokens bottomMatch = tokenizer.match(page, "<!-- bottom content -->", "<!-- end bottom content -->", false);
			page = tokenizer.concatenate(bottomMatch.nonMatches);
			Tokens headMatch = tokenizer.match(page, "<head>", "</head>", false);
			page = tokenizer.concatenate(headMatch.nonMatches);

			SnippetRenderingInfo info = new SnippetRenderingInfo();
			info.setBodyContent(page);

			info.setBottomContent(tokenizer.concatenate(bottomMatch.matches));
			info.setHeadContent(tokenizer.concatenate(headMatch.matches));
			info.setPageSnippetId(snippet.getId());
			info.setUuid(snippet.getUuid());
			return info;

		} finally {
			// When HttpClient instance is no longer needed,
			// shut down the connection manager to ensure
			// immediate deallocation of all system resources
			client.getConnectionManager().shutdown();
		}
	}




	static class GzipDecompressingEntity extends HttpEntityWrapper {

		public GzipDecompressingEntity(final HttpEntity entity) {
			super(entity);
		}

		@Override
		public InputStream getContent()
				throws IOException, IllegalStateException {

			// the wrapped entity's getContent() decides about repeatability
			InputStream wrappedin = wrappedEntity.getContent();

			return new GZIPInputStream(wrappedin);
		}

		@Override
		public long getContentLength() {
			// length of ungzipped content is not known
			return -1;
		}

	}

}


