package com.alkemy.universidad.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alkemy.universidad.converters.StudentConverter;
import com.alkemy.universidad.entities.Student;
import com.alkemy.universidad.entities.Subject;
import com.alkemy.universidad.enums.Role;
import com.alkemy.universidad.errors.WebException;
import com.alkemy.universidad.models.StudentModel;
import com.alkemy.universidad.repositories.StudentRepository;


@Service
public class StudentService implements UserDetailsService {

	@Autowired
	private StudentConverter studentConverter;
	
	@Autowired
	private StudentRepository studentRepository;
	
	@Transactional
	public Student save(StudentModel model) throws WebException{
		validator(model);
		Student entity = studentConverter.modelToEntity(model);

		entity.setCreated(new Date());
		return studentRepository.save(entity);
	}
	
	public Student edit(StudentModel studentModel) throws WebException{
		validatorEdit(studentModel);
		Student student = studentConverter.modelToEntity(studentModel);
		Student student2 = searchByDni(student.getDni());
		student.setEdited(new Date());
		student.setCreated(student2.getCreated());
		return studentRepository.save(student);
	}
	
	@Transactional
	public void confirmProfile(String dni) {
		Student student = searchByDni(dni);
		student.setActive(true);
		studentRepository.save(student);
	}
	
	@Transactional
	public void changePassword(String file, String email) {
		Student student = searchByEmail(email);
		student.setEdited(new Date());
		setFile(student.getDni(), file);
	}
	
	public void validator(StudentModel model)throws WebException {
		if(model.getName() == null || model.getName().isEmpty()) {
			throw new WebException("Debe ingresar un nombre valido");
		}
		
		if(model.getDni().isEmpty() || model.getDni().equals("") || model.getDni() == null) {
			throw new WebException("El dni debe ser mayor a 0");
		}
		
		if(model.getEmail() == null || model.getEmail().isEmpty()) {
			throw new WebException("Debe ingregar un email valido");
		}
		
		if(model.getLastName() == null || model.getLastName().isEmpty()) {
			throw new WebException("Debe ingresar un apellido");
		}
		Student student = searchByDni(model.getDni());
		if(student != null) {
			throw new WebException("Este documento ya esta registrado a una cuenta. Intente nuevamente con otro documento");
		}
	}
	
	public void validatorEdit(StudentModel model)throws WebException {
		if(model.getName() == null || model.getName().isEmpty()) {
			throw new WebException("Debe ingresar un nombre valido");
		}
		
		if(model.getDni().isEmpty() || model.getDni().equals("") || model.getDni() == null) {
			throw new WebException("El dni debe ser mayor a 0");
		}
		
		if(model.getEmail() == null || model.getEmail().isEmpty()) {
			throw new WebException("Debe ingregar un email valido");
		}
		
		if(model.getLastName() == null || model.getLastName().isEmpty()) {
			throw new WebException("Debe ingresar un apellido");
		}
		Student student = searchByDni(model.getDni());
		Student student2 = searchByEmail(model.getEmail());
		if(student2 != null) {
			if(student.getDni() != student2.getDni()) {
				throw new WebException("El documento no coincide con el documento asociado al correo electrónico");
			}
		}
		
	}

	public UserDetails loadUserByUsername(String dni) throws UsernameNotFoundException {
		try {
		      Student user = studentRepository.findById(dni).get();

		      if (user != null && user.getActive()) {
		        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		        HttpSession session = attr.getRequest().getSession(true);
		        session.setAttribute("user", user);

		        ArrayList<GrantedAuthority> permits = new ArrayList<>();
		        GrantedAuthority p1 = new SimpleGrantedAuthority("ROLE_" + user.getRol().toString());
		        permits.add(p1);

		        return new org.springframework.security.core.userdetails.User(user.getDni(), user.getFile(), permits);
		      }
		}catch (Exception e) {
		      e.printStackTrace();
		      e.getMessage();
	    }
	    return null;
	}
	
	public Student searchByDni(String dni) {
		return studentRepository.searchByDni(dni);
	}
	
	public Student searchByEmail(String email) {
		return studentRepository.searchByEmail(email);
	}
	
	@Transactional
	public void setFile(String dni, String file) {
		Student student = searchByDni(dni);
		String encrypted = new BCryptPasswordEncoder().encode(file);
		student.setFile(encrypted);
		studentRepository.save(student);
	}
	
	//crear legajo y determinar que no sea igual a otro legajo
	public String generateFile() {
		Random rnd = new Random();
        List<Student>students = studentRepository.allStudent();
        String numberstr = "";
        int number = rnd.nextInt(999999);
        for (int i = 0; i < students.size(); i++) {
            	
            	numberstr = String.format("%06d", number);
				if(numberstr.equals(students.get(i).getFile())) {
					number = rnd.nextInt(999999);
					i = 0;
				}
			}
        return numberstr;
	}
	
	public Page<StudentModel>findAllPage(Pageable pageable){
		return studentConverter.entitiesToModelsPage(studentRepository.findAllPage(Role.USER, pageable));
	}
	
	public boolean confirmEmail(String email) {
		Student student = searchByEmail(email);
		if(student != null) {
			return true;
		}else {
			return false;
		}
	}
}
