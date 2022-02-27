package com.hexaphor.liveclass.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.hexaphor.liveclass.model.School;

public interface SchoolRepository extends JpaRepository<School,String> {

	Page<School> findByNameContaining(String name,Pageable pageable);
	
	
	@Query("SELECT COUNT(name) FROM School WHERE name=:name and id!=:id")
	Integer getSchoolNameCountForNotId(String name, String id);

	@Query("SELECT COUNT(name) FROM School WHERE name=:name")
	Integer getSchoolNameCount(String name);
	
}
