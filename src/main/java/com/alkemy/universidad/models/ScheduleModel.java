package com.alkemy.universidad.models;

import java.io.Serializable;

import com.alkemy.universidad.enums.Day;
import com.alkemy.universidad.enums.Turn;

import lombok.Data;

@Data
public class ScheduleModel implements Serializable{
	
	private static final long serialVersionUID = 6522896498689132123L;
	
	private String id;
	private Turn turn;
	private Day day;
}
