package com.hexaphor.liveclass.service;

import java.util.Optional;

import com.hexaphor.liveclass.model.LoginUser;

public interface ILoginUserService {

	
	public Optional<LoginUser> getUserByName(String userName);
}
