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
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * User generated by hbm2java
 */
@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "user", catalog = "slash_server")
public class User implements HasId {

	private int id;
	private String name;
	private Set<UserData> userDatas = new HashSet<UserData>(0);
	private Set<UserRole> userRoles = new HashSet<UserRole>(0);

	public User() {
	}

	public User(int id) {
		this.id = id;
	}

	public User(int id, Set<UserData> userDatas, Set<UserRole> userRoles) {
		this.id = id;
		this.userDatas = userDatas;
		this.userRoles = userRoles;
	}

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
	public Set<UserData> getUserDatas() {
		return this.userDatas;
	}

	public void setUserDatas(Set<UserData> userDatas) {
		this.userDatas = userDatas;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
	public Set<UserRole> getUserRoles() {
		return this.userRoles;
	}

	public void setUserRoles(Set<UserRole> userRoles) {
		this.userRoles = userRoles;
	}
	
	@Column(name="name")
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

}