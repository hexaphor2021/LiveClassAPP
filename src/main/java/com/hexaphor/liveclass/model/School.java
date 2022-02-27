package com.hexaphor.liveclass.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
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
@Table(name = "school")
@Entity
public class School {

	@Id
	    @GeneratedValue(generator = "id")
	    @GenericGenerator(
	        name = "id",
	        strategy = "com.hexaphor.liveclass.generator.SchoolIdGenerator"
	    )
	@Column(name = "sc_id_col")
	private String id;
	@Column(name = "name",unique = true)
	private String name;
	@Column(name = "address")
	private String address;
	@Column(name = "institute")
	private String institute;
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
	
	@Override
	public String toString() {
		return "School [id=" + id + ", name=" + name + ", address=" + address + ", institute=" + institute + ", status="
				+ status + ", createdBy=" + createdBy + ", updatedBy=" + updatedBy + ", createdOn=" + createdOn
				+ ", updatedOn=" + updatedOn + "]";
	}



}
