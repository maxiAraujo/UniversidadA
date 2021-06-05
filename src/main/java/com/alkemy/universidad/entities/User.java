package com.alkemy.universidad.entities;

import java.util.Date;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.alkemy.universidad.enums.Role;

import lombok.Data;

@Data
@MappedSuperclass
public class User {
	
	@Id
	private String dni;
	protected String name;
	protected String lastName;
	protected String email;
	protected String password;
	protected Boolean active;
	@Temporal(TemporalType.DATE)
	protected Date created;
	
	@Temporal(TemporalType.DATE)
	protected Date edited;
	
	@Enumerated(EnumType.STRING)
	protected Role rol;

}
