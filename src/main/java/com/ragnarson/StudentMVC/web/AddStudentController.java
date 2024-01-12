package com.ragnarson.StudentMVC.web;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.ragnarson.StudentMVC.bean.StudentBean;
import com.ragnarson.StudentMVC.service.StudentService;

import jakarta.validation.Valid;

@Controller
public class AddStudentController {
	
	private static Logger log = LogManager.getLogger(AddStudentController.class);
	
	@Autowired
	private StudentService service;
	
	@GetMapping("/addStudent")
	public ModelAndView getAddStudent() {
		ModelAndView modelAndView = new ModelAndView("addStudent");
		modelAndView.addObject("studentBean", new StudentBean());
		return modelAndView;
	}
	
	@PostMapping("/addStudent")
	public ModelAndView addStudent(@Valid StudentBean studentBean, BindingResult bindingResult) {
		ModelAndView modelAndView = new ModelAndView();
		if(bindingResult.hasErrors())
			modelAndView.setViewName("addStudent");
		else {
			log.info(studentBean);
			Long id = service.saveStudent(studentBean);
			String message = "Save method called with return value(id) : " + id;
			modelAndView.setViewName("addStudent");
			modelAndView.addObject("studentBean", new StudentBean());
			modelAndView.addObject("message", message);
		}
		
		return modelAndView;
	}
	
}
