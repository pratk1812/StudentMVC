package com.ragnarson.StudentMVC.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ragnarson.StudentMVC.bean.StudentBean;
import com.ragnarson.StudentMVC.entity.StudentEntity;
import com.ragnarson.StudentMVC.repo.StudentRepository;

import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Valid;
import jakarta.validation.Validator;


@Service
@Transactional
public class StudentService {
	
	private static Logger log = LogManager.getLogger(StudentService.class);

	@Autowired
	private StudentRepository repository;
	
	@Autowired
	private Validator validator;

	public Long saveStudent(StudentBean studentBean) {
		Set<ConstraintViolation<StudentBean>> violations = validator.validate(studentBean);
		Long id = -1L;
		if (violations.isEmpty()) {
			StudentEntity entity = new StudentEntity();
			
			entity.setAge(studentBean.getAge());
			entity.setEmail(studentBean.getEmail());
			entity.setEnrollmentDate(studentBean.getEnrollmentDate());
			entity.setGpa(studentBean.getGpa());
			entity.setName(studentBean.getName());
			entity.setMajor(studentBean.getMajor());
			
			repository.save(entity);
			id = entity.getId();
		}else {
			violations.forEach(violation -> log.error(violation.getMessage()));
		}
		return id;
	}
	public List<StudentBean> readByParams(ReadStudentParams params) {
		log.info("Read method is called : " + params);
		
		List<StudentEntity> entities = repository.findByParams(
				params.getFromAge(), 
				params.getToAge(), 
				params.getFromGPA(), 
				params.getToGPA(),
				params.getFromDate(),
				params.getToDate(),
				params.getMajor());
		
		List<StudentBean> beans = entities.stream()
				.map(entity ->{
					StudentBean bean = new StudentBean();
					BeanUtils.copyProperties(entity, bean);
					return bean;
				})
				.collect(Collectors.toList());
		
		return beans;
	}
	public StudentBean findById(Long id) {
		StudentEntity entity = repository.findById(id).get();
		StudentBean studentBean = new StudentBean();
		BeanUtils.copyProperties(entity, studentBean);
		return studentBean;		
	}
	public Long update(@Valid StudentBean studentBean) {
		Set<ConstraintViolation<StudentBean>> violations = validator.validate(studentBean);
		Long id = -1L;
		if (violations.isEmpty()) {
			StudentEntity entity = repository.findById(studentBean.getId()).get();
			BeanUtils.copyProperties(studentBean, entity);
			repository.save(entity);
			id = entity.getId();
		}else {
			violations.forEach(violation -> log.error(violation.getMessage()));
		}
		return id;
	}
	public void deleteById(Long id) {
		repository.deleteById(id);
	}
}
