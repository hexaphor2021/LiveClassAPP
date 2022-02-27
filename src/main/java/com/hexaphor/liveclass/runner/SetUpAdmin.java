package com.hexaphor.liveclass.runner;

import java.util.Arrays;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.hexaphor.liveclass.model.LoginUser;
import com.hexaphor.liveclass.model.Teacher;
import com.hexaphor.liveclass.repo.LoginRepository;
import com.hexaphor.liveclass.repo.TeacherRepository;

@Component
public class SetUpAdmin implements CommandLineRunner {

	@Autowired
	private TeacherRepository repo;
	@Autowired
	private LoginRepository loginRepo;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Override
	public void run(String... args) throws Exception {
		


		
		
		
		Teacher teacher=new Teacher();
		teacher.setEmail("info.hexaphorpvtltd@gmail.com");
		teacher.setName("Hexaphor");
		teacher.setMobileNumber("7008573915");
		teacher.setPassword("hexaphor");
		teacher.setCreatedOn(new Date());
		
		LoginUser loginUser=new LoginUser();
		loginUser.setPassword(passwordEncoder.encode(teacher.getPassword()));
		loginUser.setUsername(teacher.getMobileNumber().toString());
		loginUser.setRoles(Arrays.asList("ADMIN"));
		
		
		if(repo.getMobileNumberCount(teacher.getMobileNumber())==0) {
			loginUser=loginRepo.save(loginUser);
			teacher.setLoginUser(loginUser);
			repo.save(teacher);
		}
	}
	
	
}
