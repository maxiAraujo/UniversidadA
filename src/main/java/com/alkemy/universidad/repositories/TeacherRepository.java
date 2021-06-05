package com.alkemy.universidad.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.alkemy.universidad.entities.Teacher;

public interface TeacherRepository extends JpaRepository<Teacher, String>{
	
	@Query("SELECT t FROM Teacher t ")
	public List<Teacher>listAllTeachers();

	@Query("SELECT t FROM Teacher t WHERE active = true")
	public List<Teacher>listAllTeachersActives();
}
