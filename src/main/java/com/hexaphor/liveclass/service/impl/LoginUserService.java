package com.hexaphor.liveclass.service.impl;

import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.hexaphor.liveclass.exception.UserNotFoundException;
import com.hexaphor.liveclass.model.LoginUser;
import com.hexaphor.liveclass.repo.LoginRepository;
import com.hexaphor.liveclass.service.ILoginUserService;

@Service
public class LoginUserService implements ILoginUserService ,UserDetailsService{

	@Autowired
	private LoginRepository repo;
	@Override
	public Optional<LoginUser> getUserByName(String userName) {

		return repo.findByUsername(userName);
	}
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<LoginUser> opt = repo.findByUsername(username);
		if (opt.isPresent()) {
			return new org.springframework.security.core.userdetails.User(username, opt.get().getPassword(), opt.get()
					.getRoles().stream().map(role -> new SimpleGrantedAuthority(role)).collect(Collectors.toList()));

		} else {
			throw new UserNotFoundException("User '" + username + "' not exist!!!!");
		}
	}

}
