package com.hexaphor.liveclass.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.hexaphor.liveclass.model.School;

public interface ISchoolService {

	public String saveSchool(School school);
	
	public String updateSchool(School school);
	
	public List<School> allSchool(); 
	
	public Page<School> allSchoolPage(Pageable pageable);
	
	public Page<School> allSchoolPage(Pageable pageable,String name);
	
	public School oneSchoolById(String id);
	
	public void remove(String id);
	
	
}
