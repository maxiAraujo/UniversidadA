package com.alkemy.universidad.converters;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.alkemy.universidad.entities.Teacher;
import com.alkemy.universidad.models.SubjectModel;
import com.alkemy.universidad.models.TeacherModel;
import com.alkemy.universidad.repositories.TeacherRepository;

@Component("TeacherConverter")
public class TeacherConverter extends Converter<TeacherModel, Teacher> {

	@Autowired
	private TeacherRepository teacherRepository;

	public TeacherModel entityToModel(Teacher entity) {
		TeacherModel model = new TeacherModel();
		try {
			BeanUtils.copyProperties(entity, model);
		} catch (Exception e) {
			log.error("Error al convertir la entitad en el modelo del profesor", e);
		}

		return model;
	}

	public Teacher modelToEntity(TeacherModel model) {
		Teacher entity = new Teacher();
		
		try {
			BeanUtils.copyProperties(model, entity);
		} catch (Exception e) {
			log.error("Error al convertir el modelo del horario en entity", e);
		}

		return entity;
	}

	public List<TeacherModel> entitiesToModels(List<Teacher> entities) {
		List<TeacherModel> models = new ArrayList<>();
		for (Teacher s : entities) {
			models.add(entityToModel(s));
		}
		return models;
	}
	
	public Page<TeacherModel> entitiesToModelsPage(Page<Teacher> entities) {
		Page<TeacherModel> models = entities.map(this::entityToModel);
		return models;
	}

	@Override
	public List<Teacher> modelsToEntities(List<TeacherModel> m) {
		List<Teacher> entities = new ArrayList<>();
		for (TeacherModel model : m) {
			entities.add(modelToEntity(model));
		}
		return entities;
	}

}
