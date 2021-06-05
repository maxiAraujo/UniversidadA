package com.alkemy.universidad.controllers;

import java.io.IOException;
import java.lang.ProcessBuilder.Redirect;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alkemy.universidad.converters.StudentConverter;
import com.alkemy.universidad.converters.SubjectConverter;
import com.alkemy.universidad.entities.Student;
import com.alkemy.universidad.enums.Role;
import com.alkemy.universidad.errors.WebException;
import com.alkemy.universidad.models.StudentModel;
import com.alkemy.universidad.models.SubjectModel;
import com.alkemy.universidad.services.EmailService;
import com.alkemy.universidad.services.StudentService;
import com.alkemy.universidad.services.SubjectService;

@Controller
@RequestMapping("/")
public class MainController extends OwnController{
	
	@Autowired
	private SubjectService subjectService;
	
	@Autowired
	private SubjectConverter subjectConverter;
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private StudentConverter studentConverter;
	
	@Autowired StudentService studentService;
	
	@Value("${spring.mail.username}")
	private String mailFrom;
	
	@GetMapping({"", "index"})
	public String index(Model model, Principal principal, @RequestParam(value = "error", required = false) String error) throws WebException{
		try {
			if(principal == null) {
				if (error != null && !error.isEmpty()) {
			        error = "El documento o el legajo no son validos. Intente nuevamente";
			        model.addAttribute("error", error );
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
			
		return "front/index";
	}
	@PreAuthorize("hasRole('ROLE_USER') || hasRole('ROLE_ADMIN')")
	@GetMapping("/success")
	public String loginSuccess(Model model) throws WebException{
		if(getUserEntity() != null) {	
			if(getUserEntity().getRol() == Role.USER) {
				
				return "redirect:/home";				
			}else {
				return "redirect:/admin/panelAdministrativo";
			}
			
		}	
		return "/";
	}
	@PreAuthorize("hasRole('ROLE_USER')")
	@GetMapping("/home")
	public String home (Model model, @RequestParam Map<String, Object> params) throws WebException{
		Student student = getUserEntity();
		int page = params.get("page") != null ? (Integer.valueOf(params.get("page").toString()) - 1) : 0;
		PageRequest pageRequest = PageRequest.of(page, 6, Sort.by("name").ascending());
		Page<SubjectModel> subjectsModel = subjectService.findByStudentPage(student.getDni(), pageRequest);
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
		model.addAttribute("user", studentConverter.entityToModel(student));
		return "front/home";
	}
	
	@PreAuthorize("hasRole('ROLE_USER')")
	@PostMapping("/support")
	public String support (RedirectAttributes attributes, @RequestParam String name, @RequestParam String lastName,
			 @RequestParam String message, @RequestParam String email) {
			System.out.println("Entro al controlador");
			try {
				
				//supportEmail email a usuario
				emailService.sendEmail("supportEmail", "Mensaje de Universidad Alkemy", email, name, lastName, message,
						"userEmail", "file", "dni");
				
				
				//supportEmail email a admin
				emailService.sendEmail("supportAdmin", "Mensaje de usuario", mailFrom, name, lastName, message, email, "file", "dni");
			} catch (MessagingException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			attributes.addFlashAttribute("typehome", "contact");
			attributes.addFlashAttribute("supportActive", true);
		return "redirect:/home";
	}
	
	@GetMapping("/forgotpassword")
	public String forgotPassword() {
			
		return "front/forgotPassword";
	}
	
	@PostMapping("/sendmail")
	public String sendmail(Model model, @RequestParam String email, HttpSession session) {
		if(studentService.confirmEmail(email)) {
			try {
				emailService.sendEmail("recoverEmail", "Restablecer contraseña", email, "name", "lastName", "text", "userEmail", "code", "dni");
				session.setAttribute("verification", true);
			} catch (MessagingException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			model.addAttribute("success", "Te hemos enviado un email para restablecer tu contraseña");
		}else {
			model.addAttribute("error", "El mail ingresado es invalido. Intentalo nuevamente");
		}
		return "front/forgotPassword";
	}
	
	@GetMapping("/changepassword")
	public String changePassword(@RequestParam(value = "email", required = true) String email, Model model, HttpSession session, RedirectAttributes attributes) {
		if(session.getAttribute("verification") != null) {
			Student student = studentService.searchByEmail(email);
			if(student != null) {
				String file = studentService.generateFile();
				studentService.changePassword(file, email);
				model.addAttribute("file", file);
				model.addAttribute("name", student.getName());
				return "front/changePassword";
			}
		}
		attributes.addFlashAttribute("error", "No se ha podido restablecer el número de legajo. Inténtalo nuevamente mas tarde.");
		return "redirect:/";
	}

}
