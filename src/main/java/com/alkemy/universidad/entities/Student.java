package com.alkemy.universidad.entities;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper=true)
@Entity
public class Student extends User implements Serializable{
	
	private static final long serialVersionUID = 6522896498689132123L;
	
	private String file;

}
