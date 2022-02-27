package com.hexaphor.liveclass.utils;

import java.util.Date;

import org.springframework.stereotype.Component;

import com.hexaphor.liveclass.model.Teacher;

@Component
public class TeacherUtil {

	
	public void copyNonNullValues(Teacher dbTeacher, Teacher teacher) {

		if(teacher.getEmail() !=null)dbTeacher.setEmail(teacher.getEmail());
		if(teacher.getMobileNumber() !=null) dbTeacher.setMobileNumber(teacher.getMobileNumber());
		if(teacher.getName() !=null) dbTeacher.setName(teacher.getName());
		if(teacher.getSubject() !=null) dbTeacher.setSubject(teacher.getSubject());
		dbTeacher.setCreatedOn(new Date());
	}

}
