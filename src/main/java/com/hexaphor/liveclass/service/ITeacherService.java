package com.hexaphor.liveclass.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.hexaphor.liveclass.model.LoginUser;
import com.hexaphor.liveclass.model.School;
import com.hexaphor.liveclass.model.Teacher;

public interface ITeacherService {
	
	public String saveTeacher(Teacher teacher);

	public String updateTeacher(Teacher teacher);

	public List<Teacher> allTeacher();

	public Page<Teacher> allTeacherPage(Pageable pageable);

	public Page<Teacher> allTeacherPage(Pageable pageable, String name);

	public Teacher oneTeacherById(String id);
	
	public Teacher oneTeacherByLoginUser(LoginUser loginUser);
	
	public String passwordUpdateTeacher(Teacher teacher);

	public void remove(String id);
	
	public Map<String,String> allTeacher(School school);
	public Map<String,String> allTeachers();
	
	public boolean isMobileExistForEdit(String mobileNumber,String id);
	public boolean isMobileNameExist(String mobileNumber);
	
	public Map<String,String> allTeachers(String subjectId);
	public List<Teacher> allTeacher(String subjectId);
	


}
