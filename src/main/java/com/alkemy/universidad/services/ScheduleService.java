package com.alkemy.universidad.services;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.alkemy.universidad.converters.ScheduleConverter;
import com.alkemy.universidad.entities.Schedule;
import com.alkemy.universidad.enums.Day;
import com.alkemy.universidad.enums.Turn;
import com.alkemy.universidad.errors.WebException;
import com.alkemy.universidad.models.ScheduleModel;
import com.alkemy.universidad.repositories.ScheduleRepository;

@Service
public class ScheduleService {
	
	@Autowired
	private ScheduleRepository scheduleRepository;
	
	@Autowired
	private ScheduleConverter scheduleConverter;
	
	@Transactional
	public Schedule save(ScheduleModel model) throws WebException{
		validator(model);
		
		Schedule schedule = scheduleConverter.modelToEntity(model);
		schedule.setActive(true);
		schedule.setCreated(new Date());
		
		return scheduleRepository.save(schedule);
	}
	
	public Schedule modify(ScheduleModel model) throws WebException {
		validator(model);
		Schedule schedule = findById(model.getId());
		schedule.setEdited(new Date());
		schedule.setDay(model.getDay());
		schedule.setTurn(model.getTurn());
		
		return scheduleRepository.save(schedule);
	}
	
	public void validator(ScheduleModel model) throws WebException{
		if(model.getDay() == null) {
			throw new WebException("Seleccione un dia de la semana");
		}
		
		if(model.getTurn() == null) {
			throw new WebException("Seleccione un turno");
		}
	}
	
	public List<ScheduleModel>listAllSchedule(){
		return scheduleConverter.entitiesToModels(scheduleRepository.listAllSchedule());
	}
	
	public Page<Schedule>listAllPageSchedule(Pageable pageable){
		return scheduleRepository.findAll(pageable);
	}
	
	public Schedule findById(String id) {
		return scheduleRepository.findById(id).get();
	}
	
	public List<Schedule> findByDay(Day day) {
		return scheduleRepository.findByDay(day);
	}
	
	public Schedule findByDayTurn(String day, String turn) {
		List<Day>days = Arrays.asList(Day.values());
		List<Turn>turns = Arrays.asList(Turn.values());
		Turn turnFinal = null;
		Day dayFinal = null;
		for (int i = 0; i < turns.size(); i++) {
			if(turns.get(i).toString().equals(turn)) {
				turnFinal = turns.get(i);
			}
		}
		for (int i = 0; i < days.size(); i++) {
			if(days.get(i).toString().equals(day)) {
				dayFinal = days.get(i);
			}
		}
		
		return scheduleRepository.findByTurnDay(dayFinal, turnFinal);
	}
}
