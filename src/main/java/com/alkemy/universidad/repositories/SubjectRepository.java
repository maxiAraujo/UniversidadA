package com.alkemy.universidad.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.alkemy.universidad.entities.Subject;

public interface SubjectRepository extends JpaRepository<Subject, String>{

	@Query("SELECT s FROM Subject s")
	public List<Subject>listAllSubject();
	
	@Query("SELECT s FROM Subject s WHERE s.id = :id")
	public Subject searchById(@Param("id") String id);
	
//	@Query("SELECT s FROM Subject s JOIN s.students t WHERE t.dni = :dni")
//	public List<Subject>listByStudent(@Param("dni") String dni);
//	
	List<Subject>findByStudents_dni(String dni);
	Page<Subject>findByStudents_dni(String dni, Pageable pageable);
//	
//	@Query("SELECT s FROM Subject s WHERE :dni MEMBER OF s.students.dni")
//	public List<Subject>list(@Param("dni") String dni);
	
}
