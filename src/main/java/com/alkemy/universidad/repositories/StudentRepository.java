package com.alkemy.universidad.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.alkemy.universidad.entities.Student;
import com.alkemy.universidad.enums.Role;

public interface StudentRepository extends JpaRepository<Student, String>{
	@Query("SELECT s FROM Student s WHERE s.dni = :dni")
	public Student searchByDni(@Param("dni") String dni); 

	@Query("SELECT s FROM Student s")
	public List<Student> allStudent(); 
	
	@Query("SELECT s FROM Student s WHERE s.email = :email")
	public Student searchByEmail(@Param("email") String email); 
	
	@Query("SELECT s FROM Student s WHERE s.rol = :role")
	public Page<Student>findAllPage(@Param("role") Role role,Pageable pageable);
}
