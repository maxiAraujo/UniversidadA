/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alkemy.universidad.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.alkemy.universidad.entities.Student;
import com.alkemy.universidad.errors.WebException;
import com.alkemy.universidad.services.StudentService;

@Controller
public class ErrorsController implements ErrorController{
	
	@Autowired
	private StudentService studentService;
	
	public Student getUserEntity() throws WebException {
	    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    return studentService.searchByDni(auth.getName());
	  }

    @RequestMapping(value = "/error", method = { RequestMethod.GET, RequestMethod.POST })
    public ModelAndView renderErrorPage(HttpServletRequest httpRequest, Model model) throws WebException {
        ModelAndView errorPage = new ModelAndView("/front/error");
        String errorMsg = "";
        model.addAttribute("user", getUserEntity());
        int httpErrorCode = getErrorCode(httpRequest);

        switch (httpErrorCode) {
            case 400: {
                errorMsg = "El recurso solicitado no existe.";
                break;
            }
            case 403: {
                errorMsg = "No tiene permisos para acceder al recurso.";
                break;
            }
            case 401: {
                errorMsg = "No se encuentra autorizado.";
                break;
            }
            case 404: {
                errorMsg = "El recurso solicitado no fue encontrado.";
                break;
            }
            case 500: {
                errorMsg = "Ocurrió un error interno.";
                break;
            }
        }

        errorPage.addObject("codigo", httpErrorCode);
        errorPage.addObject("mensaje", errorMsg);
        return errorPage;
    }

    private int getErrorCode(HttpServletRequest httpRequest) {
            return (Integer) httpRequest.getAttribute("javax.servlet.error.status_code");
    }
    
    @Override
    public String getErrorPath() {
        return "/error";
    }
    
}
