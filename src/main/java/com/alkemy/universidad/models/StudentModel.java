package com.alkemy.universidad.models;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

import com.alkemy.universidad.enums.Role;

import lombok.Data;

@Data
public class StudentModel implements Serializable{
	
	private static final long serialVersionUID = 6522896498689132123L;

	private String dni;
	protected String name;
	protected String lastName;
	protected String email;
	protected String password;
	protected Boolean active;
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	protected Date created;
	
	@Temporal(TemporalType.DATE)
	protected Date edited;
	
	@Enumerated(EnumType.STRING)
	protected Role rol;
	private String file;
}
