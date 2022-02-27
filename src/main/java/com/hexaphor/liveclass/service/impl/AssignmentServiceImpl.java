package com.hexaphor.liveclass.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.hexaphor.liveclass.exception.AssignmnetNotFoundException;
import com.hexaphor.liveclass.model.Assignment;
import com.hexaphor.liveclass.model.AssignmentStudentAssign;
import com.hexaphor.liveclass.model.Student;
import com.hexaphor.liveclass.model.Teacher;
import com.hexaphor.liveclass.repo.AssignmentRespository;
import com.hexaphor.liveclass.repo.AssignmentStudentAssignRepository;
import com.hexaphor.liveclass.repo.StudentRepository;
import com.hexaphor.liveclass.service.IAssignmentService;

@Service
public class AssignmentServiceImpl implements IAssignmentService {
   
	@Autowired
	private AssignmentRespository repo;
	@Autowired
	private AssignmentStudentAssignRepository ASRepo;
	@Autowired
	private StudentRepository studentRepo;
	
	@Override
	public Assignment saveAssignment(Assignment assignment) {
		List<Student> listStudent=studentRepo.findByBatch(assignment.getBatch());
		List<AssignmentStudentAssign> listAssign=new ArrayList<AssignmentStudentAssign>();
		assignment=repo.save(assignment);
		 Assignment as=assignment;
		listStudent.forEach(n->{
			AssignmentStudentAssign assign=new AssignmentStudentAssign();
			assign.setStudent(n);
			assign.setAssignment(as);
			assign.setStatus("Not Upload");
			listAssign.add(assign);
		});
		ASRepo.saveAll(listAssign);
		
		return assignment;
	}
	
	@Override
	public Assignment saveAssignmentForAssignmentUpdate(Assignment assignment) {
		return repo.save(assignment);
	}

	@Override
	public String updateAssignment(Assignment assignment) {
		List<Student> listStudent=studentRepo.findByBatch(assignment.getBatch());
		List<AssignmentStudentAssign> listAssign=new ArrayList<AssignmentStudentAssign>();
		assignment=repo.save(assignment);
		 Assignment as=assignment;
		listStudent.forEach(n->{
			AssignmentStudentAssign assign=new AssignmentStudentAssign();
			assign.setStudent(n);
			assign.setAssignment(as);
			listAssign.add(assign);
		});
		ASRepo.saveAll(listAssign);
		
		return assignment.getAssignmentId();
	}

	@Override
	public List<Assignment> allAssignment() {
		
		return repo.findAll();
	}

	@Override
	public Page<Assignment> allAssignment(Pageable pageable) {
		
		return repo.findAll(pageable);
	}

	@Override
	public Page<AssignmentStudentAssign> findByAssignment(Assignment assignment, Pageable pageable) {
		return ASRepo.findByAssignment(assignment, pageable);
	}

	@Override
	public List<AssignmentStudentAssign> findByAssignment(Assignment assignment) {
	
		return ASRepo.findByAssignment(assignment);
	}

	@Override
	public Assignment getOneByAssignmnetId(String assignmentId) {
		
		return repo.findById(assignmentId).orElseThrow(()->new AssignmnetNotFoundException("Conference Room '"+assignmentId+"' not found."));
	}

	@Override
	public List<Assignment> findByTeacher(Teacher teacher) {
		
		return repo.findByTeacher(teacher);
	}

	@Override
	public Page<Assignment> findByTeacher(Teacher teacher, Pageable pageable) {
		
		return repo.findByTeacher(teacher, pageable);
	}

	@Override
	public List<AssignmentStudentAssign> findByStudent(Student student) {
		
		return ASRepo.findByStudent(student);
	}

	@Override
	public Page<AssignmentStudentAssign> findByStudent(Student student, Pageable pageable) {
		
		return ASRepo.findByStudent(student, pageable);
	}

	@Override
	public Page<AssignmentStudentAssign> allCurrentAssignmentByStudent(Student student, Pageable pageable) {
		Date currentDate=new Date();
		java.time.format.DateTimeFormatter dtf = java.time.format.DateTimeFormatter.ofPattern("HH:mm"); 
		java.time.LocalDateTime now = java.time.LocalDateTime.now(); 
		 
		Date sTime=null;
		try {
			sTime=new SimpleDateFormat("HH:mm").parse(dtf.format(now));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return ASRepo.findByAssignmnetByStudent(student, currentDate, sTime, pageable);
	}

	@Override
	public AssignmentStudentAssign AssignmentStudentAssignById(String id) {
		
		return ASRepo.findById(id).orElseThrow(()->new AssignmnetNotFoundException("Assignment '"+id+"' not found."));
	}

	@Override
	public boolean updateStatusFilePath(String filePath, String assignmentstudentId, String status) {
		
		return ASRepo.updateStatusFilePath(status, filePath, assignmentstudentId)>0;
	}

}
