package com.alkemy.universidad.controllers;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alkemy.universidad.converters.ScheduleConverter;
import com.alkemy.universidad.entities.Schedule;
import com.alkemy.universidad.enums.Day;
import com.alkemy.universidad.enums.Turn;
import com.alkemy.universidad.errors.WebException;
import com.alkemy.universidad.models.ScheduleModel;
import com.alkemy.universidad.services.ScheduleService;

@Controller
@RequestMapping("/schedule")
public class ScheduleController extends OwnController{

	 @Autowired
	 private ScheduleService scheduleService;
	 
	 @Autowired
	 private ScheduleConverter scheduleConverter;
	
	@GetMapping("/list")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public String scheduleList(@RequestParam Map<String, Object> params, Model model) {
		int page = params.get("page") != null ? (Integer.valueOf(params.get("page").toString()) - 1) : 0;
		PageRequest pageRequest = PageRequest.of(page, 4, Sort.by("day").ascending());
		Page<Schedule> pageSchedule = scheduleService.listAllPageSchedule(pageRequest);
		int totalPage = pageSchedule.getTotalPages();
		if(totalPage > 0) {
			List<Integer> pages = IntStream.rangeClosed(1, totalPage).boxed().collect(Collectors.toList());
			model.addAttribute("pages", pages);
		}
		model.addAttribute("schedules", pageSchedule.getContent());
		model.addAttribute("current", page + 1);
		model.addAttribute("next", page + 2);
		model.addAttribute("prev", page);
		System.out.println(totalPage);
		model.addAttribute("last", totalPage);
		
		return "/front/dashboard/schedule-list";
	}
	
	@GetMapping( value = {"/form", "/form/{id}"})
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public String form(Model model, @PathVariable (required = false) String id, HttpSession session) {
		model.addAttribute("day", Arrays.asList(Day.values()));
		model.addAttribute("turn", Arrays.asList(Turn.values()));
		
		if(id != null) {
			session.setAttribute("id", id);
			ScheduleModel scheduleModel = scheduleConverter.entityToModel(scheduleService.findById(id));
			model.addAttribute("schedule", scheduleModel);
		}else {
			model.addAttribute("schedule", new ScheduleModel());
		}
		
		return "/front/dashboard/schedule-form";
	}
	
	@PostMapping("/form-success")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public String success(HttpSession session, RedirectAttributes attributes, @ModelAttribute("schedule") ScheduleModel schedule) {
			System.out.println(schedule.getDay());
		try {
			if(session.getAttribute("id") != null) {
				scheduleService.modify(schedule);
			}else {
				scheduleService.save(schedule);
			}
		}catch (WebException e) {
			attributes.addFlashAttribute("error", e.getMessage());
			return "redirect:/schedule/form";
		}
		
		return "redirect:/schedule/list";
	}
	
	
}
