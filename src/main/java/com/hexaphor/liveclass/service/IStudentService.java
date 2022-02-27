package com.hexaphor.liveclass.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.hexaphor.liveclass.model.LoginUser;
import com.hexaphor.liveclass.model.School;
import com.hexaphor.liveclass.model.Student;

public interface IStudentService {
	
	public String saveStudent(Student student);

	public String updateStudent(Student student);

	public List<Student> allStudent();
	public String passwordUpdateStudent(Student student);
	public Page<Student> allStudentPage(Pageable pageable);

	public Page<Student> allStudentPage(Pageable pageable, String name);

	public Student oneStudentById(String id);
	
	public Student oneStudentByLoginUser(LoginUser loginUser);

	public void remove(String id);
	
	public Map<String,String> allStudent(School school);
	
	public Map<String,String> allStudents();
	
	public boolean isRegistrationNoExistForEdit(String registrationNo,String id);
	public boolean isRegistrationNoExist(String registrationNo);
	
	public boolean isMobileNoExistForEdit(String mobile,String id);
	public boolean ismobileNoExist(String mobile);
	
	public Map<String,String> allStudents(String batchId);

}
