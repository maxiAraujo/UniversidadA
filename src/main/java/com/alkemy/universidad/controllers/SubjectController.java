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
import com.alkemy.universidad.converters.SubjectConverter;
import com.alkemy.universidad.converters.TeacherConverter;
import com.alkemy.universidad.entities.Student;
import com.alkemy.universidad.entities.Subject;
import com.alkemy.universidad.enums.Day;
import com.alkemy.universidad.errors.WebException;
import com.alkemy.universidad.models.ScheduleModel;
import com.alkemy.universidad.models.SubjectModel;
import com.alkemy.universidad.models.TeacherModel;
import com.alkemy.universidad.services.ScheduleService;
import com.alkemy.universidad.services.StudentService;
import com.alkemy.universidad.services.SubjectService;
import com.alkemy.universidad.services.TeacherService;

@Controller
@RequestMapping("/subject")
public class SubjectController extends OwnController{

	@Autowired
	private SubjectService subjectService;
	
	@Autowired
	private SubjectConverter subjectConverter;
	
	@Autowired
	private TeacherService teacherService;
	
	@Autowired
	private TeacherConverter teacherConverter;
	
	@Autowired
	private ScheduleService scheduleService;
	
	@Autowired
	private ScheduleConverter scheduleConverter;
	
	@Autowired
	private StudentService studentService;
	
	@GetMapping("/list")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public String listSubject(Model model, @RequestParam Map<String, Object> params) {
		int page = params.get("page") != null ? (Integer.valueOf(params.get("page").toString()) - 1) : 0;
		PageRequest pageRequest = PageRequest.of(page, 6, Sort.by("name").ascending());
		Page<SubjectModel> subjectsModel = subjectService.listAllPageSubject(pageRequest);
		int totalPage = subjectsModel.getTotalPages();
		if(totalPage > 0) {
			List<Integer> pages = IntStream.rangeClosed(1, totalPage).boxed().collect(Collectors.toList());
			model.addAttribute("pages", pages);
		}
		
		model.addAttribute("subjects", subjectsModel.getContent());
		model.addAttribute("current", page + 1);
		model.addAttribute("next", page + 2);
		model.addAttribute("prev", page);
		System.out.println(totalPage);
		model.addAttribute("last", totalPage);
		return "/front/dashboard/subject-list";
	}
	
	@GetMapping( value = {"/form", "/form/{id}"})
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public String form(Model model, @PathVariable (required = false) String id, HttpSession session, @RequestParam(required = false) String typeregister) {
		if(id != null) {
			session.setAttribute("id", id);
			SubjectModel subjectModel = subjectConverter.entityToModel(subjectService.findById(id));
			model.addAttribute("subject", subjectModel);
			System.out.println(teacherService.listAllTeachersActives());
			model.addAttribute("teachers", teacherConverter.entitiesToModels(teacherService.listAllTeachersActives()));
			model.addAttribute("day", Arrays.asList(Day.values()));
		}else {
			SubjectModel subjectModel = new SubjectModel();
			subjectModel.setMaximumNumberOfStudents(0);
			model.addAttribute("subject", subjectModel);
			System.out.println(teacherService.listAllTeachersActives());
			model.addAttribute("teachers", teacherConverter.entitiesToModels(teacherService.listAllTeachersActives()));
			model.addAttribute("day", Arrays.asList(Day.values()));
		}
		model.addAttribute("typeregister", typeregister);
		return "/front/dashboard/subject-form";
	}
	
	@PostMapping("/form-success")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public String formSuccess(HttpSession session, RedirectAttributes attributes, @RequestParam String typeregister,
			@ModelAttribute("subject") SubjectModel subject, @RequestParam String day, @RequestParam String turn) {
		try {
			if(typeregister.equals("modify")) {
				ScheduleModel scheduleModel = scheduleConverter.entityToModel(scheduleService.findByDayTurn(day, turn));
				subjectService.addScheduleModel(scheduleModel, subject);
				subjectService.modify(subject);
			}else {
				System.out.println(subject);
				
				ScheduleModel scheduleModel = scheduleConverter.entityToModel(scheduleService.findByDayTurn(day, turn));
				subjectService.addScheduleModel(scheduleModel, subject);
				subjectService.save(subject);
			}
		} catch (WebException e) {
				attributes.addFlashAttribute("error", e.getMessage());
				return "redirect:/subject/form";
				
			}
		return "redirect:/subject/list"; 
	}
	
	@PreAuthorize("hasRole('ROLE_USER')")
	@GetMapping("/subjects")
	public String subjects(Model model, @RequestParam Map<String, Object> params) {
		List<SubjectModel>listSubjectModel = subjectService.listAllSubject();
		int page = params.get("page") != null ? (Integer.valueOf(params.get("page").toString()) - 1) : 0;
		PageRequest pageRequest = PageRequest.of(page, 6, Sort.by("name").ascending());
		Page<SubjectModel> subjectsModel = subjectService.listAllPageSubject(pageRequest);
		int totalPage = subjectsModel.getTotalPages();
		if(totalPage > 0) {
			List<Integer> pages = IntStream.rangeClosed(1, totalPage).boxed().collect(Collectors.toList());
			model.addAttribute("pages", pages);
		}
		
		model.addAttribute("subjects", subjectsModel.getContent());
		model.addAttribute("current", page + 1);
		model.addAttribute("next", page + 2);
		model.addAttribute("prev", page);
		System.out.println(totalPage);
		model.addAttribute("last", totalPage);
		return "front/subjects";
	}
	
	@PreAuthorize("hasRole('ROLE_USER')")
	@GetMapping("/inscription/{idSubject}")
	public String inscription(Model model,@PathVariable (required = false) String idSubject, RedirectAttributes atributos) throws WebException {
		Student student = getUserEntity();
		Subject subject = subjectService.findById(idSubject);
		if(subjectService.validateInscription(student, subject)) {
			System.out.println("Ya te has inscripto a esta materia");
			model.addAttribute("enrollmenterror", "Ya te has inscripto a esta materia");
			atributos.addFlashAttribute("enrollmenterror", "Ya te has inscripto a esta materia");
			return "redirect:/subject/subjects";
		}else {
			if(subjectService.validateTurnDay(student, subject)) {
				System.out.println("Ya tienes una materia para este horario.");
				model.addAttribute("enrollmenterror", "Ya tienes una materia para este horario");
				atributos.addFlashAttribute("enrollmenterror", "Ya tienes una materia para este horario");
				return "redirect:/subject/subjects";
			}else {
				if((subject.getMaximumNumberOfStudents()-subject.getStudents().size()) == 0) {
					System.out.println("Esta materia tiene el cupo completo. Elige otra asignatura.");
					model.addAttribute("enrollmenterror", "Esta materia tiene el cupo completo. Elige otra asignatura.");
					atributos.addFlashAttribute("enrollmenterror", "Esta materia tiene el cupo completo. Elige otra asignatura.");
					return "redirect:/subject/subjects";
				}else {
					subjectService.saveStudent(idSubject, student);
					model.addAttribute("success", "Te has registrado a la materia con éxito");
					atributos.addFlashAttribute("success", "Te has registrado a la materia con éxito");
				}
				
			}
		}
		
		
		return "redirect:/subject/subjects";
	}
}
