package com.hexaphor.liveclass.repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.hexaphor.liveclass.model.Batch;
import com.hexaphor.liveclass.model.School;

public interface BatchRepository extends JpaRepository<Batch,String> {

	Page<Batch> findByBatchContaining(String batch,Pageable pageable);
	
	@Query("SELECT COUNT(batch) FROM Batch WHERE batch=:batch and batchId!=:id")
	Integer getBatchCountForNotId(String batch, String id);

	@Query("SELECT COUNT(batch) FROM Batch WHERE batch=:batch")
	Integer getBatchCount(String batch);
	
	@Query("SELECT studentId,name FROM Student WHERE school=:school")
	List<Object[]> allBatch(School school);
	@Query("SELECT batchId,batch FROM Batch")
	List<Object[]> allBatch();
	
	Page<Batch> findBySchoolContaining(School school,Pageable pageable);
	
	List<Batch> findBySchool(School school);
	
}
