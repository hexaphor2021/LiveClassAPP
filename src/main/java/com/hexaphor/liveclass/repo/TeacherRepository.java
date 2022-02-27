package com.hexaphor.liveclass.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.hexaphor.liveclass.model.LoginUser;
import com.hexaphor.liveclass.model.School;
import com.hexaphor.liveclass.model.Subject;
import com.hexaphor.liveclass.model.Teacher;

public interface TeacherRepository extends JpaRepository<Teacher,String> {

	@Query("SELECT COUNT(mobileNumber) FROM Teacher WHERE mobileNumber=:mobileNumber and teacherId!=:id")
	Integer getmobileNumberCountForNotId(String mobileNumber, String id);

	@Query("SELECT COUNT(mobileNumber) FROM Teacher WHERE mobileNumber=:mobileNumber")
	Integer getMobileNumberCount(String mobileNumber);
	
	Page<Teacher> findByNameContaining(String name,Pageable pageable);
	
	Page<Teacher> findBySchoolContaining(School school,Pageable pageable);
	
	List<Teacher> findBySchool(School school);
	
	@Query("SELECT teacherId,name FROM Teacher WHERE school=:school")
	List<Object[]> allTeacher(School school);
	
	@Query("SELECT teacherId,name FROM Teacher")
	List<Object[]> allTeacher();
	
	@Query(value="SELECT t.teacher_id,t.name FROM teacher t where t.subjectid= :subjectId",nativeQuery=true)
	List<Object[]> allTeacher(String  subjectId);
	
	List<Teacher> findBySubject(Subject subject);
	
	Optional<Teacher> findByLoginUser(LoginUser loginUser);
	
	
	
}
