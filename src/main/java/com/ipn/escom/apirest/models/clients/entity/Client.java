package com.ipn.escom.apirest.models.clients.entity;

import java.io.Serializable;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Entity
@Table(name="clients")
public class Client implements Serializable {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@NotEmpty(message = "can't be empty")
	@Size(min=4,max=12, message =" must have a length between 4 and 12")
	private String name;
	
	@NotEmpty(message = "can't be empty")
	private String lastName;
	
	@NotEmpty(message = "can't be empty")
	@Email(message="it's not a well-formed email address")
	private String email;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
