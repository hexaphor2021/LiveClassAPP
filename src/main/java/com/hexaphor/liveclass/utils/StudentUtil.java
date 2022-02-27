package com.hexaphor.liveclass.utils;

import java.util.Date;

import org.springframework.stereotype.Component;

import com.hexaphor.liveclass.model.Student;

@Component
public class StudentUtil {

	public void copyNonNullValues(Student dbStudent, Student student) {
		if(student.getName() !=null) dbStudent.setName(student.getName());
		if(student.getMobile()!=null)dbStudent.setMobile(student.getMobile());
		if(student.getRegistrationNo() !=null) dbStudent.setRegistrationNo(student.getRegistrationNo());
		if(student.getEmail()!=null) dbStudent.setEmail(student.getEmail());
		if(student.getAddress() !=null)dbStudent.setAddress(student.getAddress());
		if(student.getBatch()!=null) dbStudent.setBatch(student.getBatch());
		if(student.getParentName() !=null) dbStudent.setParentName(student.getParentName());
        if(student.getPassword() !=null) dbStudent.setPassword(student.getPassword());
        dbStudent.setUpdatedOn(new Date());
	}
}
