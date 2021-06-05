package com.alkemy.universidad.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

import com.alkemy.universidad.enums.Day;
import com.alkemy.universidad.enums.Turn;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
@Entity
public class Schedule implements Serializable{
	
	private static final long serialVersionUID = 6522896498689132123L;
	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	private String id;
	@Enumerated(EnumType.STRING)
	private Turn turn;
	@Enumerated(EnumType.STRING)
	private Day day;
	@Temporal(TemporalType.DATE)
	private Date created;
	
	@Temporal(TemporalType.DATE)
	private Date edited;
	private Boolean active;
}
