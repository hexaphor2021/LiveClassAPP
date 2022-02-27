package com.hexaphor.liveclass.controller;

import java.security.Principal;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.hexaphor.liveclass.model.LoginUser;
import com.hexaphor.liveclass.service.ILoginUserService;

@Controller
public class CommonController {
	private static final Logger log = LoggerFactory.getLogger(CommonController.class);

	@Autowired
	private ILoginUserService service;

	@GetMapping("/")
	public String showLogPage(Model model) {
		log.info("Enter first in browser");
		return "Login";
	}

	// login setup
	@GetMapping("/setup")
	public String loginSetUp(Principal principal, HttpSession session) {
		Optional<LoginUser> opt = service.getUserByName(principal.getName());
		session.setAttribute("userOb", opt.get());
		log.info("First time login user {}", principal.getName());
		// User user = opt.get();
		return "home";
	}

	@GetMapping("/showLogin")
	public String showLogin(Model model) {
		log.info("show login page");
		return "Login";
	}

	@GetMapping("/accessdenied")
	public String accessdenied() {
		log.error("Access Denied page show");
		return "error";
	}

}
