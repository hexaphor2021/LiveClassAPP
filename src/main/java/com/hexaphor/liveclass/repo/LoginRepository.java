package com.hexaphor.liveclass.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.hexaphor.liveclass.model.LoginUser;

public interface LoginRepository extends JpaRepository<LoginUser,String> {

	@Query("SELECT COUNT(username) FROM LoginUser WHERE username=:username  and loginId!=:id")
	Integer getLoginUserCountForNotId(String username, String id);

	@Query("SELECT COUNT(username) FROM LoginUser WHERE username=:username")
	Integer getusernameCount(String username);
	
	Optional<LoginUser> findByUsername(String username);
}
