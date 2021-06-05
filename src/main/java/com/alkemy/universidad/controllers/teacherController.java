package com.alkemy.universidad.controllers;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.alkemy.universidad.converters.TeacherConverter;
import com.alkemy.universidad.errors.WebException;
import com.alkemy.universidad.models.TeacherModel;
import com.alkemy.universidad.services.TeacherService;

@Controller
@RequestMapping("/teacher")
public class teacherController extends OwnController{
	
	@Autowired
	private TeacherConverter teacherConverter;
	@Autowired
	private TeacherService teacherService;
	
	@GetMapping( value = {"/form", "/form/{id}"})
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public String form(Model model, @PathVariable (required = false) String id, HttpSession session, @RequestParam(required = false) String typeregister) {
		if(typeregister.equals("modify")) {
			System.out.println("en el form " +typeregister);
			TeacherModel teacherModel = teacherConverter.entityToModel(teacherService.findById(id));
			session.setAttribute("id", id);
			model.addAttribute("teacher", teacherModel);
		}else if(typeregister.equals("new")){
			System.out.println("en el form " + typeregister);
			model.addAttribute("teacher", new TeacherModel());			
		}
		model.addAttribute("typeregister", typeregister);
			
		return "/front/dashboard/teacher-form";
	}
	
	@PostMapping("/form-success")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public String formSuccess(HttpSession session, RedirectAttributes attributes, 
			@ModelAttribute("teacher") TeacherModel teacher, @RequestParam String typeregister) {
		try {
			if(typeregister.equals("modify")) {
				System.out.println("en el form-success " + typeregister);
				teacherService.modify(teacher);
			}else {
				System.out.println("save teacher");
				System.out.println("en el form-success " + typeregister);
				teacherService.save(teacher);
			}
		} catch (WebException e) {
			attributes.addFlashAttribute("error", e.getMessage());
			return "redirect:/teacher/form";
		}
		return "redirect:/admin/panelAdministrativo";
	}

}
