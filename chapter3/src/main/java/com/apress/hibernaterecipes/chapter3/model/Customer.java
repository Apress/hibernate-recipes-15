package com.apress.hibernaterecipes.chapter3.model;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

@Entity
@DynamicInsert
@DynamicUpdate
@Table(name="Customer")
public class Customer {

	@Id
	@GenericGenerator(name="customergen" , strategy="increment")
	@GeneratedValue(generator="customergen")
	@Column(name="id")
	private Long id;

	@Column
	private String name;
	@ElementCollection(targetClass=Address.class,fetch=FetchType.EAGER) 
	@JoinTable (name = "Address" , 
		joinColumns = @JoinColumn(name="Customer_ID"))
	private Set<Address> contacts;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public Set<Address> getContacts() {
		return contacts;
	}
	
	public void setContacts(Set<Address> contacts) {
		this.contacts = contacts;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}
