package com.hexaphor.liveclass.repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.hexaphor.liveclass.model.School;
import com.hexaphor.liveclass.model.Subject;

public interface SubjectRepository extends JpaRepository<Subject, String> {

	Page<Subject> findBySubjectNameContaining(String subjectName, Pageable pageable);

	@Query("SELECT COUNT(subjectName) FROM Subject WHERE subjectName=:subjectName and studentid!=:id")
	Integer getsubjectNameCountForNotId(String subjectName,  String id);

	@Query("SELECT COUNT(subjectName) FROM Subject WHERE  subjectName=:subjectName")
	Integer getsubjectNameCount(String subjectName);
	
	@Query("SELECT subjectId,subjectName FROM Subject WHERE school=:school")
	List<Object[]> allSubject(School school);
	
	@Query("SELECT subjectId,subjectName FROM Subject")
	List<Object[]> allSubject();
}
