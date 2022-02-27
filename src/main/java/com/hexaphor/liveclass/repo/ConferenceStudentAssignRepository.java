package com.hexaphor.liveclass.repo;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.hexaphor.liveclass.model.ConferenceRoom;
import com.hexaphor.liveclass.model.ConferenceStudentAssign;
import com.hexaphor.liveclass.model.Student;

public interface ConferenceStudentAssignRepository extends JpaRepository<ConferenceStudentAssign, String> {

	List<ConferenceStudentAssign> findByStudent(Student student);
	
	Page<ConferenceStudentAssign> findByStudent(Student student,Pageable pageable);
	
	List<ConferenceStudentAssign> findByConferenceRoom(ConferenceRoom conferenceRoom);
	
	Page<ConferenceStudentAssign> findByConferenceRoom(ConferenceRoom conferenceRoom,Pageable pageable);
	
	//Postgresql
	/*
	 * @Query(
	 * value="SELECT csa.* FROM coneference_student_assign csa join conefernce_room cr on csa.conference_id=cr.conference_id where cr.start_date <= current_timestamp + (5 * interval '1 minute') and cr.end_date >= current_timestamp and csa.student_id=:studentId"
	 * ,nativeQuery=true) Page<ConferenceStudentAssign>
	 * findByConferenceMeeting(String studentId,Pageable page);
	 */
	/*
	 * //My Sql
	 * 
	 * @Query(
	 * value="SELECT csa.* FROM onlinelive_db.coneference_student_assign csa join onlinelive_db.conefernce_room cr on csa.conference_id=cr.conference_id where cr.start_date <= CURDATE() and cr.end_date >= CURDATE() and starttime<=CURRENT_TIME() and endtime >=CURRENT_TIME() and csa.student_id=:studentId"
	 * ,nativeQuery=true) Page<ConferenceStudentAssign>
	 * findByConferenceMeeting(String studentId,Pageable page);
	 */
	//cr.startDatetime <=: currentDate and cr.endDatetime >=: currentDates and cr.startTime<=:currentTime and cr.endTime >=:currentTimes and 
	//,Date currentTime,Date currentDate,Date currentTimes,Date currentDates
	@Query("SELECT csa FROM ConferenceStudentAssign csa join ConferenceRoom cr on csa.conferenceRoom=cr.conferenceId where cr.startDatetime <=: currentDate and  cr.endDatetime >= :currentDate and cr.startTime<=:currentTime and cr.endTime >=:currentTime and  csa.student=:studentId")
	Page<ConferenceStudentAssign> findByConferenceMeeting(Student studentId,Date currentDate,Date currentTime,  Pageable page);
	
	
	
	@Modifying
	@Transactional
	@Query("update ConferenceStudentAssign set status=:status where conferenceAssignId=:conference_assignId")
	Integer updateStatusStudentConference(String conference_assignId,String status);
	
	/*
	 * //MySql
	 * 
	 * @Query(
	 * value="SELECT csa.* FROM coneference_student_assign csa join conefernce_room cr on csa.conference_id=cr.conference_id where cr.start_date <= sysdate() + INTERVAL 5 MINUTE and cr.end_date >= sysdate() and csa.student_id= :studentId"
	 * ,nativeQuery=true) Page<ConferenceStudentAssign>
	 * findByConferenceMeeting(String studentId,Pageable page);
	 */
	
	/*
	 * @Query("SELECT csa.* FROM coneference_student_assign csa join conefernce_room cr on csa.conference_id=cr.conference_id where cr.start_date <= sysdate() + INTERVAL 5 MINUTE and cr.end_date >= sysdate() and csa.student_id= :studentId"
	 * ) Page<ConferenceStudentAssign> findByConferenceMeetingForStudent(Student
	 * student,Date currentDate,Date after5mDate,Pageable page);
	 */
}
