package com.alkemy.universidad.converters;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.alkemy.universidad.entities.Student;
import com.alkemy.universidad.models.StudentModel;
import com.alkemy.universidad.models.SubjectModel;
import com.alkemy.universidad.repositories.StudentRepository;

@Component("StudentConverter")
public class StudentConverter extends Converter<StudentModel, Student> {

	@Autowired
	private StudentRepository studentRepository;

	public StudentModel entityToModel(Student entity) {
		StudentModel model = new StudentModel();
		try {
			BeanUtils.copyProperties(entity, model);
			
		} catch (Exception e) {
			log.error("Error al convertir la entitad en el modelo del alumno", e);
		}

		return model;
	}

	public Student modelToEntity(StudentModel model) {
		Student entity= new Student();
		try {
			
			BeanUtils.copyProperties(model, entity);
			
		} catch (Exception e) {
			log.error("Error al convertir el modelo del alumno en entity", e);
		}

		return entity;
	}

	public List<StudentModel> entitiesToModels(List<Student> entities) {
		List<StudentModel> models = new ArrayList<>();
		for (Student s : entities) {
			models.add(entityToModel(s));
		}
		return models;
	}
	
	public Page<StudentModel> entitiesToModelsPage(Page<Student> entities) {
		Page<StudentModel> models = entities.map(this::entityToModel);
		
		return models;
	}

	@Override
	public List<Student> modelsToEntities(List<StudentModel> m) {
		List<Student> entities = new ArrayList<>();
		for (StudentModel model : m) {
			entities.add(modelToEntity(model));
		}
		return entities;
	}

}
