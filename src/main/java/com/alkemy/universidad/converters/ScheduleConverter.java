package com.alkemy.universidad.converters;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alkemy.universidad.entities.Schedule;
import com.alkemy.universidad.models.ScheduleModel;
import com.alkemy.universidad.repositories.ScheduleRepository;

@Component("ScheduleConverter")
public class ScheduleConverter extends Converter<ScheduleModel, Schedule> {

	@Autowired
	private ScheduleRepository scheduleRepository;

	public ScheduleModel entityToModel(Schedule entity) {
		ScheduleModel model = new ScheduleModel();
		try {
			BeanUtils.copyProperties(entity, model);
		} catch (Exception e) {
			log.error("Error al convertir la entitad en el modelo del horario", e);
		}

		return model;
	}

	public Schedule modelToEntity(ScheduleModel model) {
		Schedule entity;
		if (model.getId() != null && !model.getId().isEmpty()) {
			entity = scheduleRepository.getOne(model.getId());
		} else {
			entity = new Schedule();
		}
		try {
			BeanUtils.copyProperties(model, entity);
		} catch (Exception e) {
			log.error("Error al convertir el modelo del horario en entity", e);
		}

		return entity;
	}

	public List<ScheduleModel> entitiesToModels(List<Schedule> entities) {
		List<ScheduleModel> models = new ArrayList<>();
		for (Schedule s : entities) {
			models.add(entityToModel(s));
		}
		return models;
	}

	@Override
	public List<Schedule> modelsToEntities(List<ScheduleModel> m) {
		List<Schedule> entities = new ArrayList<>();
		for (ScheduleModel model : m) {
			entities.add(modelToEntity(model));
		}
		return entities;
	}

}