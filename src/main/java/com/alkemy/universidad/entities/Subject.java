package com.alkemy.universidad.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
@Entity
public class Subject implements Serializable {

	private static final long serialVersionUID = 6522896498689132123L;
	
	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	private String id;
	private String name;
	@OneToOne
	private Schedule schedule;
	@OneToOne
	private Teacher teacher;
	private Integer maximumNumberOfStudents;
	
	@Temporal(TemporalType.DATE)
	private Date created;
	
	@Temporal(TemporalType.DATE)
	private Date edited;
	private Boolean active;
	
	@ManyToMany(fetch = FetchType.LAZY)
	private List<Student>students;
}
