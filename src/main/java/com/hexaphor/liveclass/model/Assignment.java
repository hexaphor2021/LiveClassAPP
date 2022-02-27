package com.hexaphor.liveclass.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name="assignment")
public class Assignment {

	@Id
	@GeneratedValue(generator = "AS")
	@GenericGenerator(name="AS",strategy = "com.hexaphor.liveclass.generator.AssignmentIdGenerator")
	@Column(name="assignment_id")
	private String assignmentId;
	
	@Column(name="name")
	private String name;
	
	@Temporal(TemporalType.DATE)
	@Column(name="start_date")
	private Date startDatetime;
	
	@Temporal(TemporalType.DATE)
	@Column(name="end_date")
	private Date endDatetime;
	
	@Temporal(TemporalType.TIME)
	@Column(name="starttime")
	@DateTimeFormat(pattern = "HH:mm")
	private Date startTime;
	
	@Temporal(TemporalType.TIME)
	@DateTimeFormat(pattern = "HH:mm")
	@Column(name="endtime")
	private Date endTime;
	@Transient
	private String eTime;
	@Transient
	private String sTime;
	@Transient
	private String startDate;
	@Transient
	private String endDate;
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="batch_id")
	private Batch Batch;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="teacher_id")
	private Teacher teacher;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="subject_id")
	private Subject subject;
	
	@Column(name="file_path")
	private String uploadFilePath;

	@OneToMany(mappedBy = "assignment", fetch = FetchType.LAZY)
	private List<AssignmentStudentAssign> listAssignStudent=new ArrayList<AssignmentStudentAssign>();

	@Transient
	private MultipartFile uploadFile;
}
