package com.hexaphor.liveclass.repo;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.hexaphor.liveclass.model.Assignment;
import com.hexaphor.liveclass.model.AssignmentStudentAssign;
import com.hexaphor.liveclass.model.Student;

public interface AssignmentStudentAssignRepository extends JpaRepository<AssignmentStudentAssign, String> {

	List<AssignmentStudentAssign> findByAssignment(Assignment assignment);
	
	Page<AssignmentStudentAssign> findByAssignment(Assignment assignment,Pageable pageable);

	@Query("SELECT csa FROM AssignmentStudentAssign csa join Assignment cr on csa.assignment=cr.assignmentId where cr.startDatetime <=: currentDate and  cr.endDatetime >= :currentDate and cr.startTime<=:currentTime and cr.endTime >=:currentTime and  csa.student=:student")
	Page<AssignmentStudentAssign> findByAssignmnetByStudent(Student student,Date currentDate,Date currentTime,  Pageable page);
	
	
	List<AssignmentStudentAssign> findByStudent(Student student);
	
	Page<AssignmentStudentAssign> findByStudent(Student student,Pageable pageable);

	@Modifying
	@Transactional
	@Query("update AssignmentStudentAssign set filePath=:filePath , status=:status WHere assignmentstudentId=:assignmentstudentId")
	Integer updateStatusFilePath(String status,String filePath,String assignmentstudentId);
}
