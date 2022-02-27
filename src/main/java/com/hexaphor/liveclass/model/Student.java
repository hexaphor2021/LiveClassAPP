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

@AllArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name="student")
@Data
public class Student {
	@Id
	@GeneratedValue(generator = "st")
	@GenericGenerator(name="st",strategy = "com.hexaphor.liveclass.generator.StudentIdGenerator")
	@Column(name="student_id")
	private String studentId;
	@Column(name="name")
	private String name;
	@Column(name="registration_no",unique = true)
	private String registrationNo;
	@Column(name="mobile")
	private String mobile;
	@Column(name="email")
	private String email;
	@Column(name="parent_name")
	private String parentName;
	@Column(name="password")
	private String password;
	
	@Column(name="address")
	private String address;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="batch_id")
	private Batch batch;

	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="login_id")
	private LoginUser loginUser;
	
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
	
	/*
	 * @OneToMany(mappedBy = "student") private Set<Assignment> assignment;
	 */
	

}
