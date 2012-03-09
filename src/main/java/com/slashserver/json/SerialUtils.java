package com.slashserver.json;

import java.lang.reflect.Method;

import org.springframework.stereotype.Component;

import com.slashserver.model.Page;
import com.slashserver.model.Sitemap;

@Component
public class SerialUtils {
//AM: change of plans.. have permanently clipped relationship 
//		fields on entities as it will be rare that we want to serialise them.
//	public Sitemap clipAll(Sitemap sitemap){
//		clipFields(sitemap, "sitemapPages");
//		return sitemap;
//	}
//	
//	
//	public Page clipAll(Page page){
//		clipFields(page, "pageSnippets","role","pageTemplate","sitemapPagesForPageId","sitemapPagesForParentId");
//		return page;
//	}
//	
//	
//	public void clipFields(Object bean,String... fields){
//		Method[] methods = bean.getClass().getMethods();
//		outer:for(String field:fields){
//			for(Method method:methods){
//				if(method.getName().equalsIgnoreCase("set"+field)){
//					try {
//						method.invoke(bean, new Object[]{null});
//					} catch (Exception e) {
//						System.out.println("Failed to clip field: "+field+" using method "+method.getName()+"   "+e);
//					}
//					continue outer;
//				}
//			}
//			System.out.println("Failed to clip field: "+field);
//		}
//	}
	
}
