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
@Table
@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class Batch {

	@Id
	@GeneratedValue(generator = "BA")
	@GenericGenerator(name="BA",strategy = "com.hexaphor.liveclass.generator.BatchIdGenerator")
	@Column(name="batch_id")
	private String batchId;
	@Column(name="year")
	private String year;
	@Column(name="class_name")
	private String className;
	@Column(name="stream")
	private String stream;
	@Column(name="batch")
	private String batch;//year+classname+stream
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
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "sc_id_col")
	private School school;
	
	@Override
	public String toString() {
		return "Batch [batchId=" + batchId + ", year=" + year + ", className=" + className + ", stream=" + stream
				+ ", batch=" + batch + ", status=" + status + ", createdBy=" + createdBy + ", updatedBy=" + updatedBy
				+ ", createdOn=" + createdOn + ", updatedOn=" + updatedOn + ", school=" + school + "]";
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Batch other = (Batch) obj;
		if (batchId == null) {
			if (other.batchId != null)
				return false;
		} else if (!batchId.equals(other.batchId))
			return false;
		return true;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((batchId == null) ? 0 : batchId.hashCode());
		return result;
	}
}
