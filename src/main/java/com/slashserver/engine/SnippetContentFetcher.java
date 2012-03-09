package com.slashserver.engine;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.slashserver.model.PageSnippet;
import com.slashserver.persistence.UserDataStore;
import com.slashserver.persistence.UserStore;

@Component
public class SnippetContentFetcher {

	private static ExecutorService threadPool = Executors.newCachedThreadPool();
	
	@Autowired
	private SnippetHttpClient httpClient;
	
	@Autowired
	private UserDataStore userDataStore;
	
	
	@Autowired
	private UserStore userStore;
	
	private class IndividualSnippetFetcher implements Callable<SnippetRenderingInfo>{

		private PageSnippet snippet;
		private UserDetails user;
		private HttpServletRequest request;
		
		public IndividualSnippetFetcher(PageSnippet snippet,UserDetails user,HttpServletRequest request){
			this.snippet=snippet;
			this.user=user;
			this.request=request;
		}
		
		public SnippetRenderingInfo call() throws Exception {
			
			return httpClient.fetchSnippetContent(snippet,userDataStore.getDataForPage(snippet, userStore.getByName(user.getUsername())) , request);
		}
		
	}
	
	
	public Set<SnippetRenderingInfo> fetchContent(Set<PageSnippet> snippets,
										HttpServletRequest request,
										UserDetails user,
										boolean useTimeout) throws Exception{
		List<Callable<SnippetRenderingInfo>> fetchList = new ArrayList<Callable<SnippetRenderingInfo>>();
		for(PageSnippet snippet:snippets){
			IndividualSnippetFetcher fetcher = new IndividualSnippetFetcher(snippet, user, request);
			fetchList.add(fetcher);
		}
		
		
		List<Future<SnippetRenderingInfo>> results =null;
		if(useTimeout){
			results = threadPool.invokeAll(fetchList,2000l, TimeUnit.MILLISECONDS);
		}else{
			//Sixty seconds for slow timeout..
			results = threadPool.invokeAll(fetchList,60000l, TimeUnit.MILLISECONDS);
		}
		
		Set<SnippetRenderingInfo> ret = new HashSet<SnippetRenderingInfo>();
		for(Future<SnippetRenderingInfo> result:results){
			try{
				ret.add(result.get());
			}catch(Exception e){
				//Suppress.
				//AMDO: logging.
				System.out.println("No va!"+e);
				e.printStackTrace();
			}
		}
		
		return ret;
	}
	
	public void setHttpClient(SnippetHttpClient httpClient) {
		this.httpClient = httpClient;
	}
	
	public void setUserDataStore(UserDataStore store){
		this.userDataStore=store;
	}
	
	public void setUserDataStore(UserStore store){
		this.userStore=store;
	}
	
}
