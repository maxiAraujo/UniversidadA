package com.alkemy.universidad.services;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.alkemy.universidad.converters.TeacherConverter;
import com.alkemy.universidad.entities.Teacher;
import com.alkemy.universidad.errors.WebException;
import com.alkemy.universidad.models.TeacherModel;
import com.alkemy.universidad.repositories.TeacherRepository;

@Service
public class TeacherService {
	
	@Autowired
	private TeacherConverter teacherConverter;
	
	@Autowired
	private TeacherRepository teacherRepository;
	
	public Teacher save(TeacherModel model) throws WebException{
		validator(model);
		Teacher entity = teacherConverter.modelToEntity(model);
		entity.setActive(true);
		entity.setCreated(new Date());
		
		return teacherRepository.save(entity);
	}
	
	public Teacher modify(TeacherModel model) throws WebException{
		validator(model);
		Teacher entity = teacherRepository.findById(model.getDni()).get();
		
		entity.setName(model.getName());
		entity.setLastName(model.getLastName());
		entity.setDni(model.getDni());
		return teacherRepository.save(entity);
	}
	
	private void validator(TeacherModel model) throws WebException{
		if(model.getName() == null || model.getName().isEmpty() || model.getName().equals("")) {
			throw new WebException("El nombre del profesor no puede ser nulo o estar vacio");
		}
		if(model.getLastName() == null || model.getLastName().isEmpty() || model.getLastName().equals("")) {
			throw new WebException("El apellido del profesor no puede ser nulo o estar vacio");
		}
		if(model.getDni().isEmpty() || model.getDni().equals("") || model.getDni() == null) {
			throw new WebException("El documento no puede ser 0 o estar vacio");
		}
		
	}
	
	public List<TeacherModel> listAllTeachers(){
		return teacherConverter.entitiesToModels(teacherRepository.listAllTeachers());
	}
	
	public Teacher findById(String id) {
		return teacherRepository.findById(id).get();
	}
	
	public List<Teacher> listAllTeachersActives(){
		return teacherRepository.listAllTeachersActives();
	}
	
	public Page<TeacherModel>findAllPage(Pageable pageable){
		return teacherConverter.entitiesToModelsPage(teacherRepository.findAll(pageable));
	}
}
