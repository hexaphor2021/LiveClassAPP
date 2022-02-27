package com.hexaphor.liveclass.service.impl;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.hexaphor.liveclass.exception.StudentNotFoundxception;
import com.hexaphor.liveclass.model.LoginUser;
import com.hexaphor.liveclass.model.School;
import com.hexaphor.liveclass.model.Student;
import com.hexaphor.liveclass.repo.LoginRepository;
import com.hexaphor.liveclass.repo.StudentRepository;
import com.hexaphor.liveclass.service.IStudentService;
import com.hexaphor.liveclass.utils.MyCollectionUtil;

@Service
public class StudentServiceImple implements IStudentService {

	@Autowired
	private StudentRepository repo;
	@Autowired
	private LoginRepository loginRepo;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Override
	public String saveStudent(Student student) {
		LoginUser loginUser=new LoginUser();
		loginUser.setPassword(passwordEncoder.encode(student.getPassword()));
		loginUser.setUsername(student.getMobile());
		loginUser.setRoles(Arrays.asList("STUDENT"));
		loginUser=loginRepo.save(loginUser);
		student.setLoginUser(loginUser);
		student.setCreatedOn(new Date());
		return repo.save(student).getStudentId();
	}

	@Override
	public String updateStudent(Student student) {
		LoginUser loginUser=new LoginUser();
		//loginUser.setPassword(student.getPassword());
		loginUser.setUsername(student.getMobile());
		loginUser.setRoles(Arrays.asList("STUDENT"));
		loginUser.setLoginId(student.getLoginUser().getLoginId());
		loginUser=loginRepo.save(loginUser);
		student.setLoginUser(loginUser);
		student.setUpdatedOn(new Date());
		return repo.save(student).getStudentId();
	}
	
	@Override
	public String passwordUpdateStudent(Student student) {
		LoginUser loginUser=new LoginUser();
        loginUser.setPassword(passwordEncoder.encode(student.getPassword()));
		loginUser.setUsername(student.getMobile());
		loginUser.setLoginId(student.getLoginUser().getLoginId());
		loginUser=loginRepo.save(loginUser);
		student.setLoginUser(loginUser);
		student.setUpdatedOn(new Date());
		return repo.save(student).getStudentId();
	}

	@Override
	public List<Student> allStudent() {
		return repo.findAll();
	}

	@Override
	public Page<Student> allStudentPage(Pageable pageable) {
		return repo.findAll(pageable);
	}

	@Override
	public Page<Student> allStudentPage(Pageable pageable, String name) {
		return repo.findByNameContaining(name, pageable);
	}

	@Override
	public Student oneStudentById(String id) {
	
		return repo.findById(id).orElseThrow(()->new StudentNotFoundxception("Student '"+id+"' not found!!"));
	}

	@Override
	public void remove(String id) {
		Student student =repo.findById(id).orElseThrow(()->new StudentNotFoundxception("Student '"+id+"' not found!!"));
        loginRepo.delete(student.getLoginUser());
        repo.delete(student);
	}

	@Override
	public Map<String, String> allStudent(School school) {
		List<Object[]> listStudent=repo.allStudent(school);
		Map<String, String> map=new HashMap<String,String>();
		if(listStudent.size()==0) {
			
		}
		else {
			map=MyCollectionUtil.convertListToMap(listStudent);
		}
		return map;
	}

	@Override
	public Map<String, String> allStudents() {
		List<Object[]> listStudent=repo.allStudent();
		Map<String, String> map=new HashMap<String,String>();
		if(listStudent.size()==0) {
			
		}
		else {
			map=MyCollectionUtil.convertListToMap(listStudent);
		}
		return map;
	}



	@Override
	public boolean isRegistrationNoExistForEdit(String registrationNo, String id) {
		
		return repo.getregistrationNoCountForNotId(registrationNo, id)>0;
	}

	@Override
	public boolean isRegistrationNoExist(String registrationNo) {
	
		return repo.getregistrationNoCount(registrationNo)>0;
	}

	@Override
	public Map<String, String> allStudents(String batchId) {
		List<Object[]> listStudent=repo.allStudentByBatch(batchId);
		Map<String, String> map=new HashMap<String,String>();
		if(listStudent.size()==0) {
			
		}
		else {
			map=MyCollectionUtil.convertListToMap(listStudent);
		}
		return map;
	}

	@Override
	public Student oneStudentByLoginUser(LoginUser loginUser) {

		return repo.findByLoginUser(loginUser).orElseThrow(()->new StudentNotFoundxception("Student '"+loginUser.getUsername()+"' not found!!"));
	}

	@Override
	public boolean isMobileNoExistForEdit(String mobile, String id) {

		return repo.getmobileCountForNotId(mobile, id)>0;
	}

	@Override
	public boolean ismobileNoExist(String mobile) {

		return repo.getmobileCount(mobile)>0;
	}

}
