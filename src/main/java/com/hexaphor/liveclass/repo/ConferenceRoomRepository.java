package com.hexaphor.liveclass.repo;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.hexaphor.liveclass.model.ConferenceRoom;
import com.hexaphor.liveclass.model.School;
import com.hexaphor.liveclass.model.Teacher;

public interface ConferenceRoomRepository extends JpaRepository<ConferenceRoom, String> {

	Page<ConferenceRoom> findBySchoolContaining(School school, Pageable pageable);

	List<ConferenceRoom> findBySchool(School school);
	
	Page<ConferenceRoom>  findByTeacher(Teacher teacher,Pageable pageable);
	
	List<ConferenceRoom> findByTeacher(Teacher teacher);
	
	//postgresq
	/*
	 * @Query(value=
	 * "SELECT * FROM onlinelive_db.conefernce_room where start_date <= current_timestamp + (5 * interval '1 minute')  and end_date >= current_timestamp and teacher_id= :teacherId"
	 * , nativeQuery=true) List<ConferenceRoom> allConferenceByTeacher(String
	 * teacherId);
	 */
	
	
	//  mysql
	  
	/*
	 * @Query(value=
	 * "SELECT * FROM onlinelive_db.conefernce_room where start_date <= CURDATE() and end_date >= CURDATE() and starttime<=CURRENT_TIME() and endtime >=CURRENT_TIME() and  teacher_id= :teacherId"
	 * , nativeQuery=true) List<ConferenceRoom> allConferenceByTeacher(String
	 * teacherId);
	 */
	 
	
	@Query("SELECT cr FROM ConferenceRoom cr WHERE cr.teacher=:teacher and cr.startDatetime <=:currentDate and cr.endDatetime>=:currentDate and cr.startTime<=:currentTime and cr.endTime>=:currentTime ")
	List<ConferenceRoom> allConferenceByTeacher(Teacher teacher,Date currentTime,Date currentDate);
	
	@Query("SELECT cr FROM ConferenceRoom cr WHERE cr.teacher=:teacher and cr.startDatetime <=:currentDate and cr.endDatetime>=:currentDate and cr.startTime<=:currentTime and cr.endTime>=:currentTime ")
	Page<ConferenceRoom> allConferenceByTeacher(Teacher teacher,Date currentTime,Date currentDate,Pageable pageable);
}
