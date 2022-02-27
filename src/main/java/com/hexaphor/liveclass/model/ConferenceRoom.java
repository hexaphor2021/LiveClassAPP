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

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name="conefernce_room")
public class ConferenceRoom {

	@Id
	@GeneratedValue(generator = "CR")
	@GenericGenerator(name="CR",strategy = "com.hexaphor.liveclass.generator.ConferenceRoomIdGenerator")
	@Column(name="conference_id")
	private String conferenceId;
	
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
	
	/*
	 * @Temporal(TemporalType.TIME)
	 * 
	 * @Column(name="start_time") private Date startTime;
	 * 
	 * @Temporal(TemporalType.TIME)
	 * 
	 * @Column(name="end_time") private Date endTime;
	 */
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="batch_id")
	private Batch Batch;
	
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="teacher_id")
	private Teacher teacher;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="subject_id")
	private Subject subject;
	
	@OneToMany(mappedBy = "conferenceRoom", fetch = FetchType.LAZY
	           )
	private List<ConferenceStudentAssign> listAssignStudent=new ArrayList<ConferenceStudentAssign>();
	
	@Transient
	private List<String> studentList;
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
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="sc_id_col")
	private School school;
	
	@Column(name="streamlink")
	private String streamLink;
	
	@Transient
	private List<Student> listStudent;
}
