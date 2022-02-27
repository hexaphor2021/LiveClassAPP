package com.hexaphor.liveclass.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.hexaphor.liveclass.model.Batch;
import com.hexaphor.liveclass.model.LoginUser;
import com.hexaphor.liveclass.model.School;
import com.hexaphor.liveclass.model.Student;

public interface StudentRepository extends JpaRepository<Student,String> {

	@Query("SELECT COUNT(registrationNo) FROM Student WHERE registrationNo=:registrationNo and studentId!=:id")
	Integer getregistrationNoCountForNotId(String registrationNo, String id);

	@Query("SELECT COUNT(registrationNo) FROM Student WHERE registrationNo=:registrationNo")
	Integer getregistrationNoCount(String registrationNo);
	
	@Query("SELECT COUNT(mobile) FROM Student WHERE mobile=:mobile and studentId!=:id")
	Integer getmobileCountForNotId(String mobile, String id);

	@Query("SELECT COUNT(username) FROM LoginUser WHERE username=:username")
	Integer getmobileCount(String username);
	
	Page<Student> findByNameContaining(String name,Pageable pageable);
	
	@Query("SELECT studentId,name FROM Student WHERE school=:school")
	List<Object[]> allStudent(School school);
	
	@Query("SELECT studentId,name FROM Student ")
	List<Object[]> allStudent();
	
	@Query(value="SELECT t.student_id,t.name FROM student t where t.batch_id= :batchId",nativeQuery=true)
	List<Object[]> allStudentByBatch(String  batchId);
	
	List<Student> findByBatch(Batch batch);
	
	Optional<Student> findByLoginUser(LoginUser loginUser);
}
