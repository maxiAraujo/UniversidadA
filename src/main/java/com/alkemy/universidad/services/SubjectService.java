package com.alkemy.universidad.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;

import com.alkemy.universidad.converters.ScheduleConverter;
import com.alkemy.universidad.converters.SubjectConverter;
import com.alkemy.universidad.converters.TeacherConverter;
import com.alkemy.universidad.entities.Student;
import com.alkemy.universidad.entities.Subject;
import com.alkemy.universidad.entities.Teacher;
import com.alkemy.universidad.errors.WebException;
import com.alkemy.universidad.models.ScheduleModel;
import com.alkemy.universidad.models.SubjectModel;
import com.alkemy.universidad.repositories.SubjectRepository;

@Service
public class SubjectService {
	
	@Autowired
	private SubjectConverter subjectConverter;
	
	@Autowired
	private SubjectRepository subjectRepository;
	
	@Autowired
	private ScheduleConverter scheduleConverter;
	
	@Autowired
	private TeacherConverter teacherConverter;
	
	@Autowired
	private TeacherService teacherService;
	
	@Transactional
	public Subject save(SubjectModel model) throws WebException{
		
		validator(model); 
		Teacher teacher = teacherService.findById(model.getIdTeacher());
		Subject entity = subjectConverter.modelToEntity(model);
		entity.setTeacher(teacher);
		entity.setActive(true);
		entity.setCreated(new Date());
		
		
		return subjectRepository.save(entity);
	}
	
	public Subject modify(SubjectModel model)throws WebException {
		
		validator(model);
		Subject subject = findById(model.getId());
		subject.setEdited(new Date());
		subject.setName(model.getName());
		subject.setMaximumNumberOfStudents(model.getMaximumNumberOfStudents());
		subject.setSchedule(scheduleConverter.modelToEntity(model.getSchedule()));
		subject.setTeacher(teacherService.findById(model.getIdTeacher()));
		return subjectRepository.save(subject);
	}
	
	public void validator(SubjectModel model)throws WebException {
		if(model.getName() == null || model.getName().isEmpty()) {
			throw new WebException("Debe ingresar un nombre valido");
		}
		if(model.getMaximumNumberOfStudents() <= 0 || model.getMaximumNumberOfStudents() == null) {
			throw new WebException("El numero maximo debe ser mayor a 5");
		}
		
		if(model.getSchedule() == null) {
			throw new WebException("La asignatura debe tener al menos un horario");
		}
		
		if(model.getIdTeacher() == null) {
			throw new WebException("La asignatura debe tener un profesor activo");
		}
	}
	
	public List<SubjectModel>listAllSubject(){
		return subjectConverter.entitiesToModels(subjectRepository.listAllSubject());
	}
	
	public Subject findById(String id) {
		return subjectRepository.findById(id).get();
	}
	
	public void addScheduleModel(ScheduleModel scheduleModel, SubjectModel subjectModel) {
		subjectModel.setSchedule(scheduleModel);
	}
	
	/*Valida si el estudiante se inscribio a la asignatura o no
	 * Devuele true si ya esta inscripto
	 * */
	public boolean validateInscription(Student student, Subject subject) {
		if(subject.getStudents() != null) {
			for (Student s : subject.getStudents()) {
				if(s.getDni().equals(student.getDni())) {
					return true;
				}
			
			}
		}
		
		return false;
	}
	
	/*Valida si el estudiante ya esta inscripto en ese dia y horario
	 * Devuele true si ya esta inscripto
	 * */
	public boolean validateTurnDay(Student student, Subject subject) {
		List<Subject>listSubject = findByStudent(student.getDni());
		if(listSubject != null) {
			for (Subject s : listSubject) {
				if(s.getSchedule().getId().equals(subject.getSchedule().getId())) {
					return true;
				}
			}
		}
		
		
		return false;
	}
	
	/*guarda un estudiante en la lista de la materia*/
	@Transactional
	public void saveStudent(String id, Student student) {
		List<Student>list;
		Subject subject = findById(id);
		if(subject.getStudents() != null) {
			list = subject.getStudents();
			list.add(student);
		}else {
			list = new ArrayList();
			list.add(student);
		}
		
		subject.setStudents(list);
		subjectRepository.save(subject);
	}
	
	public List<Subject> findByStudent(String dni) {
		return subjectRepository.findByStudents_dni(dni);
	}
	
	public Page<SubjectModel> findByStudentPage(String dni, Pageable pageable) {
		return subjectConverter.entitiesToModelsPage(subjectRepository.findByStudents_dni(dni, pageable));
	}
	
	public SubjectModel searchById(String id) {
		return subjectConverter.entityToModel(subjectRepository.searchById(id));
	}
	
	
	//da de baja una materia a un alumno
		@Transactional
		public Boolean unsubscribe(String idSubject, String dni) {
			Subject subject = findById(idSubject);
			List<Student> listStudents = subject.getStudents();
			
			for (Student student : listStudents) {
				if(student.getDni().equals(dni)) {
					listStudents.remove(student);
					subject.setStudents(listStudents);
					subjectRepository.save(subject);
					return true;
				}
			}
			return false;
		}
		
		public Page<SubjectModel> listAllPageSubject(Pageable pageable){
			return subjectConverter.entitiesToModelsPage(subjectRepository.findAll(pageable));
		}
}
