package com.hexaphor.liveclass.utils;

import java.util.Date;

import org.springframework.stereotype.Component;

import com.hexaphor.liveclass.model.Subject;

@Component
public class SubjectUtil {
	
	public void copyNonNullValues(Subject dbSubject, Subject subject) {
		
		if(subject.getDescription()!=null) dbSubject.setDescription(subject.getDescription());
		dbSubject.setUpdatedOn(new Date());
		if(subject.getUpdatedBy()!=null)dbSubject.setUpdatedBy(subject.getUpdatedBy());
		if(subject.getStatus()!=null) dbSubject.setStatus(subject.getStatus());
		if(subject.getSubjectName()!=null)dbSubject.setSubjectName(subject.getSubjectName());
		if(subject.getType()!=null)dbSubject.setType(subject.getType());
	}
}
