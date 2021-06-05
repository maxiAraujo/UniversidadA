package com.alkemy.universidad.controllers;

import java.io.IOException;

import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alkemy.universidad.entities.Student;
import com.alkemy.universidad.enums.Role;
import com.alkemy.universidad.errors.WebException;
import com.alkemy.universidad.models.StudentModel;
import com.alkemy.universidad.services.EmailService;
import com.alkemy.universidad.services.StudentService;

@Controller
@RequestMapping("/register")
public class RegisterController extends OwnController {
	
	@Autowired
	private StudentService studentService;
	
	@Autowired
	private EmailService emailService;
	
	@GetMapping("")
	public String register(Model model) {
		StudentModel studentModel = new StudentModel();
		model.addAttribute("user", studentModel);
		model.addAttribute("admin", Role.ADMIN);
		model.addAttribute("student", Role.USER);
		return "/front/register";
	}
	
	@PostMapping("/success")
	public String registerSuccess(Model model, RedirectAttributes attributes, 
			@ModelAttribute("user") StudentModel studentModel, HttpSession session) throws WebException {
		
		try {
			Student student = studentService.save(studentModel);
			model.addAttribute("user", studentModel);
			String file = studentService.generateFile();
			studentService.setFile(student.getDni(), file);
			model.addAttribute("file", file);
			emailService.sendEmail("registerEmail", "Bienvenido a Universidad Alkemy", student.getEmail(), student.getName(), "lastName", "text", "userEmail", file, student.getDni());
			attributes.addFlashAttribute("success", "Te hemos enviado un email para confirmar tu cuenta.");
		}catch(WebException e) {
			attributes.addFlashAttribute("error", e.getMessage());
			return "redirect:/register";
		} catch (MessagingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return "redirect:/";
	}
	
	@GetMapping("/completed")
	public String registerCompleted(RedirectAttributes attributes, Model model, @RequestParam(value = "confirmation", required = true) String confirmation) {
		
		if(confirmation != null) {
			Student student = studentService.searchByDni(confirmation);
			studentService.confirmProfile(confirmation);
			model.addAttribute("user", student);
			return "/front/registerSuccess";
		}else {
			attributes.addFlashAttribute("error", "Ha habido un error desconocido. Por favor, contacte con ");
			return "redirect:/";
		}
		
	}
}
