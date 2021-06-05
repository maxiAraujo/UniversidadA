package com.alkemy.universidad.restController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alkemy.universidad.controllers.OwnController;
import com.alkemy.universidad.entities.Schedule;
import com.alkemy.universidad.enums.Day;
import com.alkemy.universidad.enums.Turn;
import com.alkemy.universidad.services.ScheduleService;

@RestController
public class ScheduleRestController extends OwnController{
	
	@Autowired
	private ScheduleService scheduleService;
	
	@RequestMapping("/pull-turn/{day}")
	public List<Turn> pullTurn(@PathVariable() String day){
		List<Day> days = Arrays.asList(Day.values());
		Day finalDay = null;
		for (int i = 0; i < days.size(); i++) {
			if(days.get(i).toString().equals(day)) {
				finalDay = days.get(i);
			}
		}
		List<Turn> turn = new ArrayList();
		List<Schedule> schedules = scheduleService.findByDay(finalDay);
		for (int i = 0; i < schedules.size(); i++) {
			turn.add(schedules.get(i).getTurn());
		}
		
		return turn;
	}
}
