package com.alkemy.universidad.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.alkemy.universidad.entities.Schedule;
import com.alkemy.universidad.enums.Day;
import com.alkemy.universidad.enums.Turn;

public interface ScheduleRepository extends JpaRepository<Schedule, String>, PagingAndSortingRepository<Schedule, String> {
	
	@Query("SELECT s FROM Schedule s WHERE s.active = true ORDER BY s.day, s.turn")
	public List<Schedule> listAllSchedule();
	
	@Query("SELECT s FROM Schedule s WHERE s.active = true AND s.day = :day")
	public List<Schedule> findByDay(@Param("day") Day day);
	
	@Query("SELECT s FROM Schedule s WHERE s.turn = :turn AND s.day = :day")
	public Schedule findByTurnDay(@Param("day") Day day, @Param("turn") Turn turn);
}
