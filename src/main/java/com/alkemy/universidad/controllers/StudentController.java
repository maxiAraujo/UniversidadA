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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alkemy.universidad.entities.Student;
import com.alkemy.universidad.errors.WebException;
import com.alkemy.universidad.models.StudentModel;
import com.alkemy.universidad.models.SubjectModel;
import com.alkemy.universidad.services.StudentService;
import com.alkemy.universidad.services.SubjectService;

@Controller
@RequestMapping("/student")
public class StudentController extends OwnController {
	
	@Autowired
	private StudentService studentService;
	
	@Autowired
	private SubjectService subjectService;
	
	@PreAuthorize("hasRole('ROLE_USER')")
	@GetMapping("/unsubscribe/{idSubject}")
	public String unsubscribe(@PathVariable("idSubject") String idSubject, RedirectAttributes attributes ) throws WebException {
		Boolean band = subjectService.unsubscribe(idSubject, getUserEntity().getDni());
		if(band) {
			attributes.addFlashAttribute("success", "Te has dado de baja a la materia con éxito.");
		}else {
			attributes.addFlashAttribute("error", "Ha ocurrido un error inesperado. Por favor vuelve a intentarlo más tarde");
		}
		
		return "redirect:/home";
	}
	
	@PreAuthorize("hasRole('ROLE_USER')")
	@PostMapping("/edit")
	public String edit(Model model, RedirectAttributes attributes, @ModelAttribute("user") StudentModel student) {
		
		try {
			studentService.edit(student);
			attributes.addFlashAttribute("editOn", true);
		} catch (WebException e) {
			attributes.addFlashAttribute("error", e.getStackTrace());
			e.printStackTrace();
		}
		attributes.addFlashAttribute("typehome", "editProfile");
		System.out.println(student.toString());
		return "redirect:/home";
	}
	
	@GetMapping("/list")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public String list(Model model, @RequestParam Map<String, Object> params) {
		int page = params.get("page") != null ? (Integer.valueOf(params.get("page").toString()) - 1) : 0;
		PageRequest pageRequest = PageRequest.of(page, 6, Sort.by("name").ascending());
		Page<StudentModel> studentsModel = studentService.findAllPage(pageRequest);
		System.out.println(studentsModel.getContent().toString());
		int totalPage = studentsModel.getTotalPages();
		if(totalPage > 0) {
			List<Integer> pages = IntStream.rangeClosed(1, totalPage).boxed().collect(Collectors.toList());
			model.addAttribute("pages", pages);
		}
		
		model.addAttribute("students", studentsModel.getContent());
		model.addAttribute("current", page + 1);
		model.addAttribute("next", page + 2);
		model.addAttribute("prev", page);
		System.out.println(totalPage);
		model.addAttribute("last", totalPage);
		
		
		return "/front/dashboard/student-list";
	}
}
