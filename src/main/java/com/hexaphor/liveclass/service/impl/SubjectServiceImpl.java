package com.hexaphor.liveclass.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.hexaphor.liveclass.exception.SubjectNotFoundException;
import com.hexaphor.liveclass.model.School;
import com.hexaphor.liveclass.model.Subject;
import com.hexaphor.liveclass.repo.SubjectRepository;
import com.hexaphor.liveclass.service.ISubjectService;
import com.hexaphor.liveclass.utils.MyCollectionUtil;

@Service
public class SubjectServiceImpl implements ISubjectService {
   
	@Autowired
	private SubjectRepository repo;
	
	@Override
	public String saveSubject(Subject subject) {
		return repo.save(subject).getSubjectId();
	}

	@Override
	public String updateSubject(Subject subject) {
		return repo.save(subject).getSubjectId();
	}

	@Override
	public List<Subject> allSubject() {
		return repo.findAll();
	}

	@Override
	public Page<Subject> allSubject(Pageable pageable) {
		return repo.findAll(pageable);
	}

	@Override
	public Page<Subject> allSubjectNameConstraints(Pageable pageable, String name) {
		return repo.findBySubjectNameContaining(name, pageable);
	}

	@Override
	public void removeById(String id) {
       Subject subject=repo.findById(id).orElseThrow(()->new SubjectNotFoundException("Subject '"+id+"' not found!!"));
       repo.delete(subject);

	}

	@Override
	public Subject oneSubjectById(String id) {
		
		return repo.findById(id).orElseThrow(()->new SubjectNotFoundException("Subject '"+id+"' not found!!"));
	}

	@Override
	public Map<String, String> allSubject(School school) {
		List<Object[]> listSubject=repo.allSubject(school);
		Map<String, String> map=new HashMap<String,String>();
		if(listSubject.size()==0) {
			
		}
		else {
			map=MyCollectionUtil.convertListToMap(listSubject);
		}
		return map;
	}

	@Override
	public Map<String, String> allSubjects() {
		List<Object[]> listSubject=repo.allSubject();
		Map<String, String> map=new HashMap<String,String>();
		if(listSubject.size()==0) {
			
		}
		else {
			map=MyCollectionUtil.convertListToMap(listSubject);
		}
		return map;
	}

	@Override
	public boolean isSubjectNameExistForEdit(String subjecthName, String id) {
		
		return repo.getsubjectNameCountForNotId(subjecthName, id)>0;
	}

	@Override
	public boolean isSubjectNameExist(String subjectName) {
		
		return repo.getsubjectNameCount(subjectName)>0;
	}

}
