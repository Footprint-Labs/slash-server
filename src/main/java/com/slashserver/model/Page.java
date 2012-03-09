package com.slashserver.model;

// Generated 05-Dec-2011 23:49:32 by Hibernate Tools 3.4.0.CR1

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * Page generated by hbm2java
 */
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler","role","pageTemplate","sitemapPagesForParentId","sitemapPagesForPageId","pageSnippets"})
@Entity
@Table(name = "page", catalog = "slash_server", uniqueConstraints = @UniqueConstraint(columnNames = "url"))
public class Page implements HasId  {

	
	private Role role;
	private PageTemplate pageTemplate;
	private String url;
	private String name;
	private String markup;
	private Set<SitemapPage> sitemapPagesForParentId = new HashSet<SitemapPage>(0);
	private Set<SitemapPage> sitemapPagesForPageId = new HashSet<SitemapPage>(0);
	private Set<PageSnippet> pageSnippets = new HashSet<PageSnippet>(0);

	public Page() {
	}
	
	public Page(int id) {
		this.id=id;
	}


	private int id;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}


	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "role_id")
	public Role getRole() {
		return this.role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "page_template_id")
	public PageTemplate getPageTemplate() {
		return this.pageTemplate;
	}

	public void setPageTemplate(PageTemplate pageTemplate) {
		this.pageTemplate = pageTemplate;
	}

	@Column(name = "url", unique = true, nullable = false, length = 200)
	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Column(name = "name", nullable = false, length = 200)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	
	@Column(name = "markup")
	public String getMarkup() {
		return markup;
	}
	
	public void setMarkup(String markup) {
		this.markup = markup;
	}
	
	//AM: note no delete cascade as can null the ref.
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "pageByParentId")
	public Set<SitemapPage> getSitemapPagesForParentId() {
		return this.sitemapPagesForParentId;
	}

	public void setSitemapPagesForParentId(Set<SitemapPage> sitemapPagesForParentId) {
		this.sitemapPagesForParentId = sitemapPagesForParentId;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "pageByPageId",cascade={javax.persistence.CascadeType.REMOVE})
	public Set<SitemapPage> getSitemapPagesForPageId() {
		return this.sitemapPagesForPageId;
	}

	public void setSitemapPagesForPageId(Set<SitemapPage> sitemapPagesForPageId) {
		this.sitemapPagesForPageId = sitemapPagesForPageId;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "page")
	public Set<PageSnippet> getPageSnippets() {
		return this.pageSnippets;
	}

	public void setPageSnippets(Set<PageSnippet> pageSnippets) {
		this.pageSnippets = pageSnippets;
	}

}