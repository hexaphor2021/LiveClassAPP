package com.hexaphor.liveclass.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hexaphor.liveclass.exception.StudentNotFoundxception;
import com.hexaphor.liveclass.model.ConferenceStudentAssign;
import com.hexaphor.liveclass.model.LoginUser;
import com.hexaphor.liveclass.model.Student;
import com.hexaphor.liveclass.model.Teacher;
import com.hexaphor.liveclass.restcontroller.StudentRestController;
import com.hexaphor.liveclass.service.IBatchService;
import com.hexaphor.liveclass.service.IConferenceRoomService;
import com.hexaphor.liveclass.service.IStudentService;
import com.hexaphor.liveclass.service.ITeacherService;
import com.hexaphor.liveclass.utils.StudentUtil;
@Controller
@RequestMapping("/student")
public class StudentController {

	private Logger LOGGER = LoggerFactory.getLogger(StudentRestController.class);
	@Autowired
	private IStudentService service;
	
	@Autowired
	private StudentUtil util;
	@Autowired
	private IBatchService batchService;
	@Autowired
	private IConferenceRoomService conferenceService;
	@Autowired
	private ITeacherService teacherService;
	
	
	private void commonui(Model model ) {
		model.addAttribute("batchs", batchService.allBatches());
	}

	// 1. all Student fetch
	@GetMapping("/all")
	public String allStudent(Model model) {
		LOGGER.info("All Student API");
		try {
			LOGGER.info("All Student service call");
			List<Student> list = service.allStudent();
			model.addAttribute("list", list);
			LOGGER.info("All service call success");
		} catch (Exception e) {
			LOGGER.error("All Service exception : {}", e.getLocalizedMessage());
			model.addAttribute("message", "Not found data");
			e.printStackTrace();
		}
		return "Student/StudentData";
	}

	@GetMapping("/register")
	public String showStudentReg(Model model) {
		model.addAttribute("student", new Student());
		commonui(model);
		return "Student/StudentRegister";
	}
	// 2. Save Student
	@PostMapping("/save")
	public String saveStudent(@ModelAttribute Student student,Model model) {
		LOGGER.info("Save Student called ");
		try {
			LOGGER.info("save Student Service call");
			String id = service.saveStudent(student);
			model.addAttribute("message", "Student save '" + id + "' successfully");
			LOGGER.info("save Student success {}", id);
		} catch (Exception e) {
			LOGGER.error("save Student failed {}", e.getLocalizedMessage());
			model.addAttribute("message", "student save failed");
		}
		model.addAttribute("student", new Student());
		commonui(model);
		return "Student/StudentRegister";
	}

	// 3. get Student by id
	@GetMapping("/StudentById")
	public String oneStudentById(@RequestParam String id,Model model) {

		try {
			LOGGER.info("One Student by id method called");
			Student student = service.oneStudentById(id);
			LOGGER.info("One batch by id service called success {}", id);
			model.addAttribute("student", student);
		} catch (StudentNotFoundxception unfe) {
			throw unfe;
		} catch (Exception e) {
			LOGGER.error("Unable to get Student by id {}", e.getMessage());
			e.printStackTrace();
			model.addAttribute("message", "Not Found Student");
		}
		commonui(model);
		return "Student/StudentEdit";
	}

	// 4. Remove Student By Id
	@DeleteMapping("/removeStudentById")
	public String removeOneStudentById(@RequestParam String id,Model model) {
		try {
			LOGGER.info("remove Student by id method called");
			service.remove(id);
			LOGGER.info("remove Student successfully {}", id);
		} catch (StudentNotFoundxception unfe) {
			throw unfe;
		} catch (DataIntegrityViolationException cvef) {
			throw new DataIntegrityViolationException("We can not delete Student! used by Other Entity");
		} catch (Exception e) {
			LOGGER.error("Unable to delete Student {}", e.getMessage());
			e.printStackTrace();
		}
		return "redirect:alls";
	}

	// 5. Update Student
	@PostMapping("/update")
	public String updateStudent(@ModelAttribute Student student, Model model) {
		LOGGER.info("Batch update called");
		try {
			Student dbStudent = service.oneStudentById(student.getStudentId());
			util.copyNonNullValues(dbStudent, student);
			service.updateStudent(dbStudent);
			LOGGER.info("Student update  success {}", student.getStudentId());

		} catch (Exception e) {
			LOGGER.error("Student update failed");
			e.printStackTrace();
			model.addAttribute("message", "Student updated failed ,please again");
		}

		return "redirect:alls";
	}

	// 6. all Student in the page
	@GetMapping("/alls")
	public String allStudentByPage(@PageableDefault(page = 0, size = 10) Pageable pageable,
			@RequestParam(required = false, value = "name", defaultValue = "") String name, Model model) {
		LOGGER.info("Student page service called");
		try {
			Page<Student> page = null;
			if (name.equals("")) {
				LOGGER.info("call Student page service");
				page = service.allStudentPage(pageable);
				LOGGER.info("success all Student page ");
			} else {
				LOGGER.info("call Student page service by name filter");
				page = service.allStudentPage(pageable, name);
				LOGGER.info("success Batch page service by name filter");
			}
			model.addAttribute("list", page.getContent());
			model.addAttribute("page", page);
		} catch (Exception e) {
			LOGGER.error("Student page fetch failed");
			e.printStackTrace();
			model.addAttribute("message", "Student fetch page failed ,please again");
		}

		return "Student/StudentData";
	}
	
	@GetMapping("/allMeeting")
	public String allConferenceRoomByPage(@PageableDefault(page = 0, size = 10) Pageable pageable, Model model,HttpSession session) {
		LOGGER.info("ConferenceRoom page service called");
		try {
			//Temporary add teacher for testing actually use session for this 
            LoginUser user= (LoginUser) session.getAttribute("userOb");
			
			Student student=service.oneStudentByLoginUser(user);
			
			Page<ConferenceStudentAssign> page = null;
				LOGGER.info("call ConferenceRoom page service");
				page = conferenceService.findByConferenceMeeting(student.getStudentId(), pageable);
				LOGGER.info("success all ConferenceRoom page ");
			
			model.addAttribute("list", page.getContent());
			model.addAttribute("page", page);
		} catch (Exception e) {
			LOGGER.error("ConferenceRoom page fetch failed");
			e.printStackTrace();
			model.addAttribute("message", "ConferenceRoom fetch page failed ,please again");
		}
		return "Student/ConferenceData";
	}
	
	@GetMapping("/allConference")
	public String allConferenceData(@PageableDefault(page = 0, size = 10) Pageable pageable, Model model,HttpSession session) {
		LOGGER.info("ConferenceRoom page service called");
		try {
			//Temporary add teacher for testing actually use session for this 
            LoginUser user= (LoginUser) session.getAttribute("userOb");
			
			Student student=service.oneStudentByLoginUser(user);
			
			Page<ConferenceStudentAssign> page = null;
				LOGGER.info("call ConferenceRoom page service");
				page = conferenceService.findByStudent(student, pageable);
				LOGGER.info("success all ConferenceRoom page ");
			
			model.addAttribute("list", page.getContent());
			model.addAttribute("page", page);
		} catch (Exception e) {
			LOGGER.error("ConferenceRoom page fetch failed");
			e.printStackTrace();
			model.addAttribute("message", "ConferenceRoom fetch page failed ,please again");
		}
		return "Student/StudentConferenceData";
	}
	// AJAX VALIDATION
	@GetMapping("/validate")
	public @ResponseBody String validateModel(@RequestParam String registrationNo, @RequestParam String id) {
		String message = "";
		if (id != "" && service.isRegistrationNoExistForEdit(registrationNo, id)) {
			// if id exist then request came from edit page//isUserNameExistForEdit
			message = "Registration Number '" + registrationNo + "' already exist";
		}
		if (id == "" && service.isRegistrationNoExist(registrationNo)) {
			// if not id exist then request came from register page
			message = "Registration Number '" + registrationNo + "' already exist";
		}
		return message;
	}
	
	// AJAX VALIDATION
		@GetMapping("/validatemobile")
		public @ResponseBody String validateMobile(@RequestParam String mobile, @RequestParam String id) {
			String message = "";
			if (id != "" && service.isMobileNoExistForEdit(mobile, id)) {
				// if id exist then request came from edit page//isUserNameExistForEdit
				message = "Mobile Number '" + mobile + "' already exist";
			}
			if (id == "" && service.ismobileNoExist(mobile)) {
				// if not id exist then request came from register page
				message = "Mobile Number '" + mobile + "' already exist";
			}
			return message;
		}
		@GetMapping("/passwordModify")
		public String studentPasswordChange(Model model) {
			LOGGER.info("Enter password modify page");
			return "Admin/PasswordChange";
		}
		
		@PostMapping("/passwordUpdate")
		public String studentPasswordUpdate(Model model, @RequestParam String newPassword, HttpSession session) {
			LoginUser user = (LoginUser) session.getAttribute("userOb");
			LOGGER.info("Enter passwordUpdate Method");
			if (user.getRoles().contains("TEACHER")) {
				Teacher teacher = teacherService.oneTeacherByLoginUser(user);
				teacher.setRole("TEACHER");
				teacher.setPassword(newPassword);
				teacherService.passwordUpdateTeacher(teacher);
				LOGGER.info("Faculty password Update ");
			} else if (user.getRoles().contains("STUDENT")) {
				Student student = service.oneStudentByLoginUser(user);
				service.passwordUpdateStudent(student);
				LOGGER.info("Student password Update ");
			}
			else {
				Teacher teacher = teacherService.oneTeacherByLoginUser(user);
				teacher.setPassword(newPassword);
				teacher.setRole("ADMIN");
				teacherService.passwordUpdateTeacher(teacher);
				LOGGER.info("Faculty password Update ");
			}
			return "redirect:profile";
		}
		
		@GetMapping("/profile")
		public String viewProfile(Model model, HttpSession session) {
			String responcePage = "";
			LoginUser user = (LoginUser) session.getAttribute("userOb");
			if (user.getRoles().contains("TEACHER")) {
				Teacher teacher = teacherService.oneTeacherByLoginUser(user);
				model.addAttribute("teacher", teacher);
				LOGGER.info("Faculty login.");
				responcePage = "Teacher/Profile";
			} else if (user.getRoles().contains("STUDENT")) {
				LOGGER.info(" student login.");
				Student student = service.oneStudentByLoginUser(user);
				model.addAttribute("student", student);
				responcePage = "Student/Profile";
			} else {
				Teacher teacher = teacherService.oneTeacherByLoginUser(user);
				model.addAttribute("teacher", teacher);
				LOGGER.info(" user login.");
				responcePage = "Admin/Profile";
			}
			return responcePage;
		}

}
