package com.alkemy.universidad;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.alkemy.universidad.entities.Schedule;
import com.alkemy.universidad.entities.Subject;
import com.alkemy.universidad.models.SubjectModel;
import com.alkemy.universidad.repositories.ScheduleRepository;
import com.alkemy.universidad.repositories.SubjectRepository;
import com.alkemy.universidad.services.ScheduleService;
import com.alkemy.universidad.services.SubjectService;

@SpringBootTest
class UniversidadAlkemyApplicationTests {

	@Autowired
	private ScheduleRepository scheduleRepository;
	
	@Autowired
	private ScheduleService scheduleService;
	
	@Autowired
	private SubjectRepository subjectRepository;
	
	@Autowired
	private SubjectService subjectService;
	
	@Test
	void contextLoads() {
		Schedule schedule = scheduleService.findByDayTurn("LUNES", "TARDE");
		System.out.println(schedule.toString());
		assertTrue(schedule != null);
	}
	
//	@Test
//	void findSubjectByStudentDni() {
//		List<Subject>list = subjectRepository.listByStudent("37413422");
//		System.out.println(list.toString());
//		assertTrue(!list.isEmpty());
//	}
//	@Test
//	void findSubjectByStudentDni2() {
//		List<SubjectModel>list = subjectService.listAllSubject();
//		System.out.println(list.toString());
//		assertTrue(!list.isEmpty());
//	}
	
	@Test
	void findSubjectByStudentDni3() {
		List<Subject>list = subjectRepository.findByStudents_dni("37413422");
		System.out.println(list.toString());
		assertTrue(!list.isEmpty());
	}
	
//	@Test
//	void findSubjectByStudentDni4() {
//		List<Subject>list;
//		SubjectModel subject = subjectService.searchById("877ee9d0-0837-4671-b0bd-708d1e40a7c2");
//		System.out.println(subject.toString());
//		assertTrue(subject != null);
//	}

}
