package com.alkemy.universidad.controllers;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.alkemy.universidad.models.SubjectModel;
import com.alkemy.universidad.models.TeacherModel;
import com.alkemy.universidad.services.TeacherService;

@Controller
@RequestMapping("/admin")
public class Administration extends OwnController{
	
	@Autowired
	private TeacherService teacherService;
	
	@GetMapping("/panelAdministrativo")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public String administration(Model model, @RequestParam Map<String, Object> params) {
		int page = params.get("page") != null ? (Integer.valueOf(params.get("page").toString()) - 1) : 0;
		
		PageRequest pageRequest = PageRequest.of(page, 6, Sort.by("name").ascending());
		Page<TeacherModel> teachersModel = teacherService.findAllPage(pageRequest);
		int totalPage = teachersModel.getTotalPages();
		if(totalPage > 0) {
			List<Integer> pages = IntStream.rangeClosed(1, totalPage).boxed().collect(Collectors.toList());
			model.addAttribute("pages", pages);
		}
		System.out.println(teachersModel.getContent().toString());
		model.addAttribute("teachers", teachersModel.getContent());
		model.addAttribute("current", page + 1);
		model.addAttribute("next", page + 2);
		model.addAttribute("prev", page);
		System.out.println(totalPage);
		model.addAttribute("last", totalPage);
		
		
		return "front/dashboard/teacher-list";
	}
	
}
