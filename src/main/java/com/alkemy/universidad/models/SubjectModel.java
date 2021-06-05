package com.alkemy.universidad.models;

import java.io.Serializable;
import java.util.List;

import com.alkemy.universidad.entities.Teacher;

import lombok.Data;

@Data
public class SubjectModel implements Serializable{
	
	private static final long serialVersionUID = 6522896498689132123L;
	
	private String id;
	private String name;
	private ScheduleModel schedule;
	private TeacherModel teacher;
	private Integer maximumNumberOfStudents;
	private String idTeacher;
	private List<StudentModel> students;
}
