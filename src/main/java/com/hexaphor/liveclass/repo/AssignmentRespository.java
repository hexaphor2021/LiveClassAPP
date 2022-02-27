package com.hexaphor.liveclass.repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.hexaphor.liveclass.model.Assignment;
import com.hexaphor.liveclass.model.Teacher;

public interface AssignmentRespository extends JpaRepository<Assignment, String> {

	List<Assignment> findByTeacher(Teacher teacher);
	Page<Assignment> findByTeacher(Teacher teacher,Pageable pageable);
}
