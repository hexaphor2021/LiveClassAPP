package com.hexaphor.liveclass.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.hexaphor.liveclass.model.School;
import com.hexaphor.liveclass.model.Subject;

public interface ISubjectService {
	
	public String saveSubject(Subject subject);
	
	public String updateSubject(Subject subject);
	
	public List<Subject> allSubject();
	
	public Page<Subject> allSubject(Pageable pageable);
	
	public Page<Subject> allSubjectNameConstraints(Pageable pageable,String name);
	
	public void removeById(String id);
	
	public Subject oneSubjectById(String id);
	
	public Map<String,String> allSubject(School school);
	
	public Map<String,String> allSubjects();
	
	public boolean isSubjectNameExistForEdit(String subjecthName,String id);
	public boolean isSubjectNameExist(String subjectName);

}
