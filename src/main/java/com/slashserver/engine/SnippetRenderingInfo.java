package com.slashserver.engine;

public class SnippetRenderingInfo {

	private String headContent;
	private String bodyContent;
	private String bottomContent;
	private int pageSnippetId;
	private String uuid;
	
	public void setPageSnippetId(int pageSnippetId) {
		this.pageSnippetId = pageSnippetId;
	}
	
	public int getPageSnippetId() {
		return pageSnippetId;
	}
	
	public String getHeadContent() {
		return headContent;
	}
	public void setHeadContent(final String headContent) {
		this.headContent = headContent;
	}
	public String getBodyContent() {
		return bodyContent;
	}
	public void setBodyContent(final String bodyContent) {
		this.bodyContent = bodyContent;
	}
	
	public String getBottomContent() {
		return bottomContent;
	}
	public void setBottomContent(final String bottomContent) {
		this.bottomContent = bottomContent;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	
}
