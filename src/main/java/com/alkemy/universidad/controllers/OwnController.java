package com.alkemy.universidad.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.alkemy.universidad.entities.Student;
import com.alkemy.universidad.errors.WebException;
import com.alkemy.universidad.services.StudentService;

public abstract class OwnController {
	
	@Autowired
	private StudentService studentService;
	
	public Student getUserEntity() throws WebException {
	    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    return studentService.searchByDni(auth.getName());
	  }
}
