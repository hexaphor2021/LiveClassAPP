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

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name="coneference_student_assign")
public class ConferenceStudentAssign {
	
	@Id
	@GeneratedValue(generator = "CT")
	@GenericGenerator(name="CT",strategy = "com.hexaphor.liveclass.generator.ConferenceStudentAssignIdGenerator")
	@Column(name="conference_assign_id")
	private String conferenceAssignId;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="Conference_id")
	private ConferenceRoom conferenceRoom;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="student_id")
	private Student student;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="sc_id_col")
	private School school;
	@Column(name = "stream_url")
	private String streamURL;
	
	//Attendance using status
	@Column(name="status" ,columnDefinition = "varchar(255) default 'Absence'")
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

}
