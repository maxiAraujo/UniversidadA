package com.alkemy.universidad.converters;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.alkemy.universidad.entities.Subject;
import com.alkemy.universidad.models.SubjectModel;
import com.alkemy.universidad.repositories.SubjectRepository;
import com.alkemy.universidad.services.TeacherService;

@Component("SubjectConverter")
public class SubjectConverter extends Converter<SubjectModel, Subject> {

	@Autowired
	private SubjectRepository subjectRepository;
	
	@Autowired
	private TeacherConverter teacherConverter;
	
	@Autowired
	private TeacherService teacherService;
	
	@Autowired
	private StudentConverter studentConverter;
	
	@Autowired
	private ScheduleConverter scheduleConverter;

	public SubjectModel entityToModel(Subject entity) {
		SubjectModel model = new SubjectModel();
		try {
			BeanUtils.copyProperties(entity, model);
			if(entity.getTeacher() != null) {
				model.setTeacher(teacherConverter.entityToModel(entity.getTeacher()));				
			}
			if(entity.getStudents() != null) {
				model.setStudents(studentConverter.entitiesToModels(entity.getStudents()));				
			}
			if(entity.getSchedule() != null) {
				model.setSchedule(scheduleConverter.entityToModel(entity.getSchedule()));				
			}
		} catch (Exception e) {
			log.error("Error al convertir la entitad en el modelo a entidad asignatura", e);
		}

		return model;
	}

	public Subject modelToEntity(SubjectModel model) {
		Subject entity;
		if (model.getId() != null && !model.getId().isEmpty()) {
			entity = subjectRepository.getOne(model.getId());
		} else {
			entity = new Subject();
		}
		try {
			BeanUtils.copyProperties(model, entity);
			entity.setTeacher(teacherService.findById(model.getIdTeacher()));
			if(model.getStudents() != null ) {
				entity.setStudents(studentConverter.modelsToEntities(model.getStudents()));				
			}
			if(model.getSchedule() != null) {
				entity.setSchedule(scheduleConverter.modelToEntity(model.getSchedule()));
			}
		} catch (Exception e) {
			log.error("Error al convertir el modelo de la asignatura en entity", e);
		}

		return entity;
	}

	public List<SubjectModel> entitiesToModels(List<Subject> entities) {
		List<SubjectModel> models = new ArrayList<>();
		for (Subject s : entities) {
			models.add(entityToModel(s));
		}
		return models;
	}
	
	public Page<SubjectModel> entitiesToModelsPage(Page<Subject> entities) {
		Page<SubjectModel> models = entities.map(this::entityToModel);
		return models;
	}

	@Override
	public List<Subject> modelsToEntities(List<SubjectModel> m) {
		List<Subject> entities = new ArrayList<>();
		for (SubjectModel model : m) {
			entities.add(modelToEntity(model));
		}
		return entities;
	}

}

