package com.alkemy.universidad.entities;

import java.io.Serializable;
import javax.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
public class Teacher extends User implements Serializable{
	
	private static final long serialVersionUID = 6522896498689132123L;
	
}
