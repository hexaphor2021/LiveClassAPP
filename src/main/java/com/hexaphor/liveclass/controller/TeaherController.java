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
import com.hexaphor.liveclass.exception.TeacherNotFoundException;
import com.hexaphor.liveclass.model.ConferenceRoom;
import com.hexaphor.liveclass.model.LoginUser;
import com.hexaphor.liveclass.model.Teacher;
import com.hexaphor.liveclass.restcontroller.TeacherRestController;
import com.hexaphor.liveclass.service.IConferenceRoomService;
import com.hexaphor.liveclass.service.ISubjectService;
import com.hexaphor.liveclass.service.ITeacherService;
import com.hexaphor.liveclass.utils.TeacherUtil;

@Controller
@RequestMapping("/teacher")
public class TeaherController {

	private Logger LOGGER = LoggerFactory.getLogger(TeacherRestController.class);
	@Autowired
	private ITeacherService service;

	@Autowired
	private IConferenceRoomService conferenceService;

	@Autowired
	private TeacherUtil util;

	@Autowired
	private ISubjectService subjectService;

	private void commonui(Model model) {
		// School school = new School();
		model.addAttribute("subjects", subjectService.allSubjects());
	}

	// 1. all Teacher fetch
	@GetMapping("/all")
	public String allTeacher(Model model) {
		LOGGER.info("All Teacher API");
		try {
			LOGGER.info("All Teacher service call");
			List<Teacher> list = service.allTeacher();
			model.addAttribute("list", list);
			LOGGER.info("All service call success");
		} catch (Exception e) {
			LOGGER.error("All Service exception : {}", e.getLocalizedMessage());
			model.addAttribute("message", "no data found");
			e.printStackTrace();
		}
		return "Teacher/TeacherData";
	}

	// show register page
	@GetMapping("/register")
	public String showTeacherRegistation(Model model) {
		model.addAttribute("teacher", new Teacher());
		commonui(model);
		return "Teacher/TeacherRegister";
	}

	// 2. Save Teacher
	@PostMapping("/save")
	public String saveTeacher(@ModelAttribute Teacher teacher, Model model) {
		LOGGER.info("Save Teacher called ");
		try {
			LOGGER.info("save Teacher Service call");
			String id = service.saveTeacher(teacher);
			model.addAttribute("message", "Teacher save '" + id + "' successfully");
			LOGGER.info("save Teacher success {}", id);
		} catch (Exception e) {
			LOGGER.error("save Teacher failed {}", e.getLocalizedMessage());
			model.addAttribute("message", "Teacher save failed");
		}
		model.addAttribute("teacher", new Teacher());
		commonui(model);
		return "Teacher/TeacherRegister";
	}

	// 3. get Teacher by id
	@GetMapping("/TeacherById")
	public String oneTeacherById(@RequestParam String id, Model model) {

		try {
			LOGGER.info("One Teacher by id method called");
			Teacher teacher = service.oneTeacherById(id);
			LOGGER.info("One Teacher by id service called success {}", id);
			model.addAttribute("teacher", teacher);
		} catch (TeacherNotFoundException unfe) {
			throw unfe;
		} catch (Exception e) {
			LOGGER.error("Unable to get Teacher by id {}", e.getMessage());
			model.addAttribute("message", "Teacher featch problem");
			e.printStackTrace();
		}
		commonui(model);
		return "Teacher/TeacherEdit";
	}

	// 4. Remove Teacher By Id
	@DeleteMapping("/removeTeacherById")
	public String removeOneTeacherById(@RequestParam String id, Model model) {
		try {
			LOGGER.info("remove Teacher by id method called");
			service.remove(id);
			LOGGER.info("remove Teacher successfully {}", id);
		} catch (StudentNotFoundxception unfe) {
			throw unfe;
		} catch (DataIntegrityViolationException cvef) {
			throw new DataIntegrityViolationException("We can not delete Teacher! used by Other Entity");
		} catch (Exception e) {
			LOGGER.error("Unable to delete Teacher {}", e.getMessage());
			e.printStackTrace();
		}
		return "redirect:allPage";
	}

	// 5. Update Teacher
	@PostMapping("/update")
	public String updateTeacher(@ModelAttribute Teacher teacher) {
		LOGGER.info("Teacher update called");
		try {
			Teacher dbTeacher = service.oneTeacherById(teacher.getTeacherId());
			util.copyNonNullValues(dbTeacher, teacher);
			service.updateTeacher(dbTeacher);
			LOGGER.info("Teacher update  success {}", teacher.getTeacherId());
			// response = ResponseEntity.ok("Teacher update success" +
			// teacher.getTeacherId());

		} catch (Exception e) {
			LOGGER.error("Student update failed");
			e.printStackTrace();
			// response = new ResponseEntity<String>("Student updated failed ,please again",
			// HttpStatus.EXPECTATION_FAILED);

		}

		return "redirect:allPage";
	}

	// 6. all Teacher in the page
	@GetMapping("/allPage")
	public String allTeacherByPage(@PageableDefault(page = 0, size = 10) Pageable pageable,
			@RequestParam(required = false, value = "name", defaultValue = "") String name, Model model) {
		LOGGER.info("Teacher page service called");
		try {
			Page<Teacher> page = null;
			if (name.equals("")) {
				LOGGER.info("call Teacher page service");
				page = service.allTeacherPage(pageable);
				LOGGER.info("success all Teacher page ");
			} else {
				LOGGER.info("call Teacher page service by name filter");
				page = service.allTeacherPage(pageable, name);
				LOGGER.info("success Batch page service by name filter");
			}
			model.addAttribute("list", page.getContent());
			model.addAttribute("page", page);

		} catch (Exception e) {
			LOGGER.error("Student page fetch failed");
			e.printStackTrace();
			model.addAttribute("message", "Student fetch page failed ,please again");
		}

		return "Teacher/TeacherData";
	}

	// all view conference student list
	@GetMapping("/viewAllStudentById")
	public String allStudentInfoView(Model model, @RequestParam String id) {
		LOGGER.info("allStudentInfoView service called");
		try {
			LOGGER.info("call ConferenceRoom page service");
			ConferenceRoom conference = conferenceService.getOneConferenceRoom(id);
			LOGGER.info("success all ConferenceRoom page ");

			model.addAttribute("conference", conference);
		} catch (Exception e) {
			LOGGER.error("ConferenceRoom page fetch failed");
			e.printStackTrace();
			model.addAttribute("message", "ConferenceRoom fetch page failed ,please again");
		}

		return "Teacher/ConferenceStudentData";
	}

	@GetMapping("/allMeeting")
	public String allConferenceRoomByPage(@PageableDefault(page = 0, size = 10) Pageable pageable, Model model,HttpSession session) {
		LOGGER.info("ConferenceRoom page service called");
		Page<ConferenceRoom> page = null;
		try {
			// Temporary add teacher for testing actually use session for this
			LoginUser loginUser=(LoginUser) session.getAttribute("userOb");
			
			Teacher teacher =service.oneTeacherByLoginUser(loginUser);

			
			LOGGER.info("call ConferenceRoom page service");
			page = conferenceService.allConferenceRoomByTeacherId(teacher, pageable);
			LOGGER.info("success all ConferenceRoom page ");

			model.addAttribute("list", page.getContent());
			model.addAttribute("page", page);
		} catch (Exception e) {
			LOGGER.error("ConferenceRoom page fetch failed");
			e.printStackTrace();
			model.addAttribute("message", "ConferenceRoom fetch page failed ,please again");
		}
		return "Teacher/ConferenceData";
	}

	@GetMapping("/allConferenceByTeacher")
	public String allConferenceRoom(@PageableDefault(page = 0, size = 10) Pageable pageable, Model model,HttpSession session) {
		LOGGER.info("ConferenceRoom page service called");
		try {
			// Temporary add teacher for testing actually use session for this
            LoginUser loginUser=(LoginUser) session.getAttribute("userOb");
			
			Teacher teacher =service.oneTeacherByLoginUser(loginUser);

			Page<ConferenceRoom> page = null;
			LOGGER.info("call ConferenceRoom page service");
			page = conferenceService.allConferenceRoomByTeacherId(pageable, teacher);
			LOGGER.info("success all ConferenceRoom page ");

			model.addAttribute("list", page.getContent());
			model.addAttribute("page", page);
		} catch (Exception e) {
			LOGGER.error("ConferenceRoom page fetch failed");
			e.printStackTrace();
			model.addAttribute("message", "ConferenceRoom fetch page failed ,please again");
		}
		return "Teacher/TeacherConferenceData";
	}
	
	@GetMapping("/allConference")
	public String allConferenceRoomByAdmin(@PageableDefault(page = 0, size = 10) Pageable pageable, Model model) {
		LOGGER.info("ConferenceRoom page service called");
		try {

			Page<ConferenceRoom> page = null;
			LOGGER.info("call ConferenceRoom page service");
			page = conferenceService.allConferenceRoom(pageable);
			LOGGER.info("success all ConferenceRoom page ");

			model.addAttribute("list", page.getContent());
			model.addAttribute("page", page);
		} catch (Exception e) {
			LOGGER.error("ConferenceRoom page fetch failed");
			e.printStackTrace();
			model.addAttribute("message", "ConferenceRoom fetch page failed ,please again");
		}
		return "Teacher/TeacherConferenceData";
	}

	// AJAX VALIDATION
	@GetMapping("/validate")
	public @ResponseBody String validateModel(@RequestParam String mobile, @RequestParam String id) {
		String message = "";
		if (id != "" && service.isMobileExistForEdit(mobile, id)) {
			// if id exist then request came from edit page//isUserNameExistForEdit
			message = "Mobile Number '" + mobile + "' already exist";
		}
		if (id == "" && service.isMobileNameExist(mobile)) {
			// if not id exist then request came from register page
			message = "Mobile Number '" + mobile + "' already exist";
		}
		return message;
	}
}
