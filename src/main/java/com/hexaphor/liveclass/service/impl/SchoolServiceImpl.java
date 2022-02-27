package com.hexaphor.liveclass.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.hexaphor.liveclass.exception.SchoolNotFoundException;
import com.hexaphor.liveclass.model.School;
import com.hexaphor.liveclass.repo.SchoolRepository;
import com.hexaphor.liveclass.service.ISchoolService;

@Service
public class SchoolServiceImpl implements ISchoolService {

	private Logger log=LoggerFactory.getLogger(SchoolServiceImpl.class);
	@Autowired
	private SchoolRepository repo;

	@Override
	public String saveSchool(School school) {
        log.info("Save School Service"+school.toString());
        return repo.save(school).getId().toString();
	}

	@Override
	public String updateSchool(School school) {
		 log.info("update School Service"+school.toString());
		return repo.save(school).getId().toString();
	}
	
	@Override
	public List<School> allSchool() {
		 log.info("All School Service");
		return repo.findAll();
	}
	@Override
	public Page<School> allSchoolPage(Pageable pageable) {
		return repo.findAll(pageable);
	}

	@Override
	public School oneSchoolById(String id) {
		 log.info("One School Service By ID {}",id);
		return repo.findById(id).orElseThrow(()->new SchoolNotFoundException("School '"+id+"'not found"));
	}

	@Override
	public void remove(String id) {
		 log.info("remove School Service By ID {}",id);
		School school=repo.findById(id).orElseThrow(()->new SchoolNotFoundException("School '"+id+"'not found"));
		repo.delete(school);
	}

	@Override
	public Page<School> allSchoolPage(Pageable pageable, String name) {
		return repo.findByNameContaining(name, pageable);
	}
	
	

}
