package com.hexaphor.liveclass.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name="assignment_student_assign")
public class AssignmentStudentAssign {

	@Id
	@GeneratedValue(generator = "AA")
	@GenericGenerator(name="AA",strategy = "com.hexaphor.liveclass.generator.ConferenceStudentAssignIdGenerator")
	@Column(name="assignment_student_id")
	private String assignmentstudentId;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="assignmentId")
	private Assignment assignment;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="student_id")
	private Student student;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="sc_id_col")
	private School school;
	
	@Column(name="file_path")
	private String filePath;
	
	@Column(name="status" ,columnDefinition = "varchar(255) default 'Absence'" )
	private String status;
	
	@Transient
	private MultipartFile uploadFile;

	
}
