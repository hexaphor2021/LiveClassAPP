package com.hexaphor.liveclass.utils;

import java.util.Date;

import org.springframework.stereotype.Component;

import com.hexaphor.liveclass.model.School;

@Component
public class SchoolUtil {
	
	public void copyNonNullValues(School dbSchool, School school) {
		if(school.getAddress()!=null) dbSchool.setAddress(school.getAddress());
		if(school.getInstitute()!=null)dbSchool.setInstitute(school.getInstitute());
		if(school.getName()!=null)dbSchool.setName(school.getName());
		if(school.getStatus()!=null)dbSchool.setStatus(school.getStatus());
		dbSchool.setUpdatedOn(new Date());
		if(school.getUpdatedBy()!=null)dbSchool.setUpdatedBy(school.getUpdatedBy());
	}

}
