package com.hexaphor.liveclass.service.impl;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.hexaphor.liveclass.exception.TeacherNotFoundException;
import com.hexaphor.liveclass.model.LoginUser;
import com.hexaphor.liveclass.model.School;
import com.hexaphor.liveclass.model.Subject;
import com.hexaphor.liveclass.model.Teacher;
import com.hexaphor.liveclass.repo.LoginRepository;
import com.hexaphor.liveclass.repo.TeacherRepository;
import com.hexaphor.liveclass.service.ITeacherService;
import com.hexaphor.liveclass.utils.MyCollectionUtil;

@Service
public class TeacherServiceImpl implements ITeacherService {

	@Autowired
	private TeacherRepository repo;
	@Autowired
	private LoginRepository loginRepo;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	@Override
	public String saveTeacher(Teacher teacher) {
		LoginUser loginUser=new LoginUser();
		loginUser.setPassword(passwordEncoder.encode(teacher.getPassword()));
		loginUser.setUsername(teacher.getMobileNumber().toString());
		loginUser.setRoles(Arrays.asList("TEACHER"));
		loginUser=loginRepo.save(loginUser);
		teacher.setLoginUser(loginUser);
		teacher.setCreatedOn(new Date());
		return repo.save(teacher).getTeacherId();
	}

	@Override
	public String updateTeacher(Teacher teacher) {
		Optional<LoginUser> opt=loginRepo.findById(teacher.getLoginUser().getLoginId());
		//loginUser.setPassword(passwordEncoder.encode(teacher.getPassword()));
		LoginUser loginUser=null;
		if(opt.isPresent()) {
			loginUser=opt.get();
		}
		else {
			loginUser=new LoginUser();
			loginUser.setPassword(passwordEncoder.encode(teacher.getPassword()));
		}
		loginUser.setUsername(teacher.getMobileNumber().toString());
		loginUser.setRoles(Arrays.asList("TEACHER"));
		loginUser=loginRepo.save(loginUser);
		teacher.setLoginUser(loginUser);
		teacher.setUpdatedOn(new Date());
		return repo.save(teacher).getTeacherId();
	}
	
	@Override
	public String passwordUpdateTeacher(Teacher teacher) {
		LoginUser loginUser=new LoginUser();
		loginUser.setLoginId(teacher.getLoginUser().getLoginId());
		loginUser.setPassword(passwordEncoder.encode(teacher.getPassword()));
		loginUser.setUsername(teacher.getMobileNumber());
		loginUser.setRoles(Arrays.asList(teacher.getRole()));
		loginUser=loginRepo.save(loginUser);
		teacher.setLoginUser(loginUser);
		teacher.setUpdatedOn(new Date());
		return repo.save(teacher).getTeacherId();
	}


	@Override
	public List<Teacher> allTeacher() {
	
		return repo.findAll();
	}

	@Override
	public Page<Teacher> allTeacherPage(Pageable pageable) {
		
		return repo.findAll(pageable);
	}

	@Override
	public Page<Teacher> allTeacherPage(Pageable pageable, String name) {
		
		return repo.findByNameContaining(name, pageable);
	}

	@Override
	public Teacher oneTeacherById(String id) {
	
		return repo.findById(id).orElseThrow(()->new TeacherNotFoundException("Teacher '"+id+"'not found"));
	}

	@Override
	public void remove(String id) {
		Teacher teacher=repo.findById(id).orElseThrow(()->new TeacherNotFoundException("Teacher '"+id+"'not found"));
		repo.delete(teacher);

	}

	@Override
	public Map<String, String> allTeacher(School school) {
		List<Object[]> listTeacher=repo.allTeacher(school);
		Map<String, String> map=new HashMap<String,String>();
		if(listTeacher.size()==0) {
			
		}
		else {
			map=MyCollectionUtil.convertListToMap(listTeacher);
		}
		return map;
	}

	@Override
	public Map<String, String> allTeachers() {
		List<Object[]> listTeacher=repo.allTeacher();
		Map<String, String> map=new HashMap<String,String>();
		if(listTeacher.size()==0) {
			
		}
		else {
			map=MyCollectionUtil.convertListToMap(listTeacher);
		}
		return map;
	}

	@Override
	public boolean isMobileExistForEdit(String mobileNumber, String id) {
		
		return repo.getmobileNumberCountForNotId(mobileNumber, id)>0;
	}

	@Override
	public boolean isMobileNameExist(String mobileNumber) {
		
		return repo.getMobileNumberCount(mobileNumber)>0;
	}

	@Override
	public Map<String, String> allTeachers(String subjectId) {

		List<Object[]> listTeacher=repo.allTeacher(subjectId);
		Map<String, String> map=new HashMap<String,String>();
		if(listTeacher.size()==0) {
			
		}
		else {
			map=MyCollectionUtil.convertListToMap(listTeacher);
		}
		return map;
	}

	@Override
	public List<Teacher> allTeacher(String subjectId) {
		Subject subject=new Subject();
		subject.setSubjectId(subjectId);;
		return repo.findBySubject(subject);
	}

	@Override
	public Teacher oneTeacherByLoginUser(LoginUser loginUser) {
		
		return repo.findByLoginUser(loginUser).orElseThrow(()->new TeacherNotFoundException("Teacher '"+loginUser.getUsername()+"'not found"));
	}

}
