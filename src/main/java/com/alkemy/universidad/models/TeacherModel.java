package com.alkemy.universidad.models;

import java.io.Serializable;
import java.util.Date;


import com.alkemy.universidad.enums.Role;

import lombok.Data;

@Data
public class TeacherModel implements Serializable{
	private static final long serialVersionUID = 6522896498689132123L;
	
	private String dni;
	protected String name;
	protected String lastName;
	protected String email;
	protected String password;
	protected Boolean active;
	protected Date created;
	protected Date edited;
	protected Role rol;



	
}
