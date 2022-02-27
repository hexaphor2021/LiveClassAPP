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
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name="teacher")
public class Teacher {

	@Id
	@GeneratedValue(generator = "SF")
	@GenericGenerator(name = "SF", strategy = "com.hexaphor.liveclass.generator.TeacherIdGenerator")
	@Column(name = "teacher_id")
	private String teacherId;
	@Column(name = "name")
	private String name;
	@Column(name = "email")
	private String email;
	@Column(name = "mobile_number", unique = true)
	private String mobileNumber;
	@Column(name="password")
	private String password;
	
	@Column(name = "status")
	private String status;

	@Column(name = "createdby")
	private String createdBy;

	@Column(name = "updateby")
	private String updatedBy;

	@Column(name = "createdon")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdOn;

	@Column(name = "updatedon")
	@Temporal(TemporalType.TIMESTAMP)
	private Date updatedOn;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "subjectid")
	private Subject subject;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "login_id")
	private LoginUser loginUser;
	
	@Transient
	private String role;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "sc_id_col")
	private School school;

	@Override
	public String toString() {
		return "Teacher [teacherId=" + teacherId + ", name=" + name + ", email=" + email + ", mobileNumber="
				+ mobileNumber + ", status=" + status + ", createdBy=" + createdBy + ", updatedBy=" + updatedBy
				+ ", createdOn=" + createdOn + ", updatedOn=" + updatedOn + ", subject=" + subject + ", loginUser="
				+ loginUser + ", school=" + school + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((teacherId == null) ? 0 : teacherId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Teacher other = (Teacher) obj;
		if (teacherId == null) {
			if (other.teacherId != null)
				return false;
		} else if (!teacherId.equals(other.teacherId))
			return false;
		return true;
	}
}
