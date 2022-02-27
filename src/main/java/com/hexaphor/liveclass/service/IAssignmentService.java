package com.hexaphor.liveclass.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.hexaphor.liveclass.model.Assignment;
import com.hexaphor.liveclass.model.AssignmentStudentAssign;
import com.hexaphor.liveclass.model.Student;
import com.hexaphor.liveclass.model.Teacher;

public interface IAssignmentService {
	
	public Assignment saveAssignment(Assignment assignment);
	public Assignment saveAssignmentForAssignmentUpdate(Assignment assignment) ;
	public String updateAssignment(Assignment assignment);
	public List<Assignment> allAssignment();
	public Page<Assignment> allAssignment(Pageable pageable);
	
	Page<AssignmentStudentAssign> findByAssignment(Assignment assignment,Pageable pageable);
	List<AssignmentStudentAssign> findByAssignment(Assignment assignment);
	
	public Assignment getOneByAssignmnetId(String assignmentId);
	
	List<Assignment> findByTeacher(Teacher teacher);
	Page<Assignment> findByTeacher(Teacher teacher,Pageable pageable);
	
	List<AssignmentStudentAssign> findByStudent(Student student);
	Page<AssignmentStudentAssign> findByStudent(Student student,Pageable pageable);

	Page<AssignmentStudentAssign> allCurrentAssignmentByStudent(Student student,Pageable pageable);

	AssignmentStudentAssign AssignmentStudentAssignById(String id);
	
	boolean updateStatusFilePath(String filePath,String assignmentstudentId,String status);
}
