package com.hexaphor.liveclass.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Entity
@Table(name="subject")
@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class Subject {
	@Id
	@GeneratedValue(generator = "sub")
	@GenericGenerator(name="sub",strategy = "com.hexaphor.liveclass.generator.SubjectIdGenerator")
	@Column(name="subjectid")
	private String subjectId;
	@Column(name="subjectname")
	private String subjectName;
	@Column(name="description")
	private String description;
	@Column(name="type")
	private String type;
	
	@Column(name="status")
	private String status;
	
	@Column(name = "createdby")
	private String createdBy;
	@Column(name = "updateby")
	private String updatedBy;
	@Column(name = "createdon")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdOn;
	@Temporal(TemporalType.TIMESTAMP)
	private Date updatedOn;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="sc_id_col")
	private School school;

}
