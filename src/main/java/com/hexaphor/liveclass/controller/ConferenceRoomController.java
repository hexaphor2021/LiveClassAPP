package com.hexaphor.liveclass.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hexaphor.liveclass.exception.ConferenceRoomNotFoundException;
import com.hexaphor.liveclass.model.ConferenceRoom;
import com.hexaphor.liveclass.model.ConferenceStudentAssign;
import com.hexaphor.liveclass.model.LoginUser;
import com.hexaphor.liveclass.model.Student;
import com.hexaphor.liveclass.model.Teacher;
import com.hexaphor.liveclass.service.IBatchService;
import com.hexaphor.liveclass.service.IConferenceRoomService;
import com.hexaphor.liveclass.service.IStudentService;
import com.hexaphor.liveclass.service.ISubjectService;
import com.hexaphor.liveclass.service.ITeacherService;
import com.hexaphor.liveclass.utils.ConferenceRoomUtil;

@Controller
@RequestMapping("/conference")
public class ConferenceRoomController {

	private Logger LOGGER = LoggerFactory.getLogger(ConferenceRoomController.class);
	@Autowired
	private IConferenceRoomService service;

	@Autowired
	private ConferenceRoomUtil util;

	@Autowired
	private ISubjectService subjectService;

	@Autowired
	private IBatchService batchService;

	@Autowired
	private IStudentService studentService;

	@Autowired
	private ITeacherService teacherService;

	private void commonui(Model model) {
		model.addAttribute("subjects", subjectService.allSubjects());
		model.addAttribute("batchs", batchService.allBatches());
		// model.addAttribute("students", studentService.allStudents());
		// model.addAttribute("teachers", teacherService.allTeachers());
	}

	private void commonuiForTecher(Model model) {
		model.addAttribute("batchs", batchService.allBatches());
	}

	// 1. all ConferenceRoom fetch
	@GetMapping("/all")
	public String allConferenceRoom(Model model) {
		LOGGER.info("All ConferenceRoom API");
		try {
			LOGGER.info("All Student service call");
			List<ConferenceRoom> list = service.allConferenceRoom();
			model.addAttribute("list", list);
			LOGGER.info("All service call success");
		} catch (Exception e) {
			LOGGER.error("All Service exception : {}", e.getLocalizedMessage());
			model.addAttribute("message", "Not found data");
			e.printStackTrace();
		}
		return "Conference/ConferenceData";
	}

	@GetMapping("/register")
	public String showConferenceRoomReg(Model model) {
		model.addAttribute("conference", new ConferenceRoom());
		commonui(model);
		return "Conference/ConferenceCreate";
	}

	@GetMapping("/current")
	public String currentCalender(Model model) {
		model.addAttribute("conference", new ConferenceRoom());
		commonui(model);
		return "Conference/ConferenceCurrentMeeting";
	}

	@GetMapping("/registerteacher")
	public String showConferenceRoomRegisterTeacher(Model model, HttpSession session) {
		try {
			LoginUser user = (LoginUser) session.getAttribute("userOb");
			Teacher teacher = teacherService.oneTeacherByLoginUser(user);
			ConferenceRoom conferenceRoom = new ConferenceRoom();
			conferenceRoom.setTeacher(teacher);
			conferenceRoom.setSubject(teacher.getSubject());
			model.addAttribute("conference", conferenceRoom);

			commonuiForTecher(model);
			return "Teacher/TeacherConferenceCreate";
		} catch (Exception e) {
			commonuiForTecher(model);
			model.addAttribute("conference", new ConferenceRoom());
			return "Teacher/TeacherConferenceCreate";
		}

	}

	@GetMapping("/teacherCurrentMeeting")
	public String showConferenceRoomRegisterTeacherForCurrent(Model model, HttpSession session) {
		try {
			LoginUser user = (LoginUser) session.getAttribute("userOb");
			Teacher teacher = teacherService.oneTeacherByLoginUser(user);
			ConferenceRoom conferenceRoom = new ConferenceRoom();
			conferenceRoom.setTeacher(teacher);
			conferenceRoom.setSubject(teacher.getSubject());
			model.addAttribute("conference", conferenceRoom);

			commonuiForTecher(model);
			return "Teacher/TeacherConferenceCreateCurrent";
		} catch (Exception e) {
			commonuiForTecher(model);
			model.addAttribute("conference", new ConferenceRoom());
			return "Teacher/TeacherConferenceCreate";
		}

	}

	// 2. Save ConferenceRoom
	@PostMapping("/save")
	public String saveConferenceRoom(@ModelAttribute ConferenceRoom conferenceRoom, Model model) {
		LOGGER.info("Save ConferenceRoom called ");
		try {
			List<ConferenceStudentAssign> list = new ArrayList<ConferenceStudentAssign>();
			for (String conString : conferenceRoom.getStudentList()) {
				Student student = new Student();
				ConferenceStudentAssign cof = new ConferenceStudentAssign();
				student.setStudentId(conString);
				cof.setStudent(student);
				list.add(cof);
			}
			conferenceRoom.setListAssignStudent(list);
			// SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd ");
			// DateTimeFormatter formatter = DateTimeFormatter. ofPattern("d/MM/yyyy");

			// SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
			// String dateInString = "2014-10-05T15:23:01Z";
			// Date date=formatter.parse("2021-08-02T16:36");
			conferenceRoom.setStartDatetime(new SimpleDateFormat("yyyy-MM-dd").parse(conferenceRoom.getStartDate()));
			conferenceRoom.setEndDatetime(new SimpleDateFormat("yyyy-MM-dd").parse(conferenceRoom.getEndDate()));
			conferenceRoom.setStartTime(new SimpleDateFormat("HH:mm").parse(conferenceRoom.getSTime()));
			conferenceRoom.setEndTime(new SimpleDateFormat("HH:mm").parse(conferenceRoom.getETime()));

			LOGGER.info("save ConferenceRoom Service call");
			String id = service.saveConference(conferenceRoom);
			model.addAttribute("message", "ConferenceRoom save '" + id + "' successfully");
			LOGGER.info("save ConferenceRoom success {}", id);
		} catch (Exception e) {
			LOGGER.error("save ConferenceRoom failed {}", e.getLocalizedMessage());
			model.addAttribute("message", "ConferenceRoom save failed");
			e.printStackTrace();
		}
		model.addAttribute("conference", new ConferenceRoom());
		commonui(model);
		return "Conference/ConferenceCreate";
	}

	// 2. Save ConferenceRoom
	@PostMapping("/currentMeetingForTeacher")
	public String currentMeetingForTeacher(@ModelAttribute ConferenceRoom conferenceRoom, Model model) {
		LOGGER.info("Save ConferenceRoom called ");
		try {

			List<ConferenceStudentAssign> list = new ArrayList<ConferenceStudentAssign>();
			for (String conString : conferenceRoom.getStudentList()) {
				Student student = new Student();
				ConferenceStudentAssign cof = new ConferenceStudentAssign();
				student.setStudentId(conString);
				cof.setStudent(student);
				list.add(cof);
			}
			conferenceRoom.setListAssignStudent(list);
			// SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm ");
			// DateTimeFormatter formatter = DateTimeFormatter. ofPattern("d/MM/yyyy
			// HH:mm");

			// SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
			// String dateInString = "2014-10-05T15:23:01Z";
			// Date date=formatter.parse("2021-08-02T16:36");
			// conferenceRoom.setStartDatetime(new
			// SimpleDateFormat("yyyy-MM-dd'T'HH:mm").parse(conferenceRoom.getStartDate()));
			// conferenceRoom.setEndDatetime(new
			// SimpleDateFormat("yyyy-MM-dd'T'HH:mm").parse(conferenceRoom.getEndDate()));

			LOGGER.info("save ConferenceRoom Service call");
			conferenceRoom.setStartDatetime(new Date());
			long hour = 3600 * 1000;

			java.time.format.DateTimeFormatter dtf = java.time.format.DateTimeFormatter.ofPattern("HH:mm");
			java.time.LocalDateTime now = java.time.LocalDateTime.now();

			Date startTime = null;
			Date endTime = null;
			try {
				startTime = new SimpleDateFormat("HH:mm").parse(dtf.format(now));
				endTime = new SimpleDateFormat("HH:mm").parse(dtf.format(now));
			} catch (ParseException e) {
				e.printStackTrace();
			}

			conferenceRoom.setEndDatetime(new Date(new Date().getTime() + hour));
			conferenceRoom.setStartTime(startTime);
			conferenceRoom.setEndTime(new Date(endTime.getTime() + hour));
			String id = service.saveConference(conferenceRoom);
			model.addAttribute("message", "ConferenceRoom save '" + id + "' successfully");
			LOGGER.info("save ConferenceRoom success {}", id);
		} catch (Exception e) {
			LOGGER.error("save ConferenceRoom failed {}", e.getLocalizedMessage());
			model.addAttribute("message", "ConferenceRoom save failed");
			e.printStackTrace();
		}
		model.addAttribute("conference", new ConferenceRoom());
		commonui(model);
		return "Conference/ConferenceCreate";
	}

	// 2. Save ConferenceRoom
	@PostMapping("/currentsave")
	public String currentmeetings(@ModelAttribute ConferenceRoom conferenceRoom, Model model) {
		LOGGER.info("Save ConferenceRoom called ");
		try {

			List<ConferenceStudentAssign> list = new ArrayList<ConferenceStudentAssign>();
			for (String conString : conferenceRoom.getStudentList()) {
				Student student = new Student();
				ConferenceStudentAssign cof = new ConferenceStudentAssign();
				student.setStudentId(conString);
				cof.setStudent(student);
				list.add(cof);
			}
			conferenceRoom.setListAssignStudent(list);
			// SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm ");
			// DateTimeFormatter formatter = DateTimeFormatter. ofPattern("d/MM/yyyy
			// HH:mm");

			// SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
			// String dateInString = "2014-10-05T15:23:01Z";
			// Date date=formatter.parse("2021-08-02T16:36");
			// conferenceRoom.setStartDatetime(new
			// SimpleDateFormat("yyyy-MM-dd'T'HH:mm").parse(conferenceRoom.getStartDate()));
			// conferenceRoom.setEndDatetime(new
			// SimpleDateFormat("yyyy-MM-dd'T'HH:mm").parse(conferenceRoom.getEndDate()));

			LOGGER.info("save ConferenceRoom Service call");
			conferenceRoom.setStartDatetime(new Date());
			long hour = 3600 * 1000;

			java.time.format.DateTimeFormatter dtf = java.time.format.DateTimeFormatter.ofPattern("HH:mm");
			java.time.LocalDateTime now = java.time.LocalDateTime.now();

			Date startTime = null;
			Date endTime = null;
			try {
				startTime = new SimpleDateFormat("HH:mm").parse(dtf.format(now));
				endTime = new SimpleDateFormat("HH:mm").parse(dtf.format(now));
			} catch (ParseException e) {
				e.printStackTrace();
			}

			conferenceRoom.setEndDatetime(new Date(new Date().getTime() + hour));
			conferenceRoom.setStartTime(startTime);
			conferenceRoom.setEndTime(new Date(endTime.getTime() + hour));
			String id = service.saveConference(conferenceRoom);
			model.addAttribute("message", "ConferenceRoom save '" + id + "' successfully");
			LOGGER.info("save ConferenceRoom success {}", id);
		} catch (Exception e) {
			LOGGER.error("save ConferenceRoom failed {}", e.getLocalizedMessage());
			model.addAttribute("message", "ConferenceRoom save failed");
			e.printStackTrace();
		}
		model.addAttribute("conference", new ConferenceRoom());
		commonui(model);
		return "Conference/ConferenceCreate";
	}

	@PostMapping("/saveMeetingForTeacher")
	public String saveConferenceRoomForTeacher(@ModelAttribute ConferenceRoom conferenceRoom, Model model) {
		LOGGER.info("Save ConferenceRoom called ");
		try {
			List<ConferenceStudentAssign> list = new ArrayList<ConferenceStudentAssign>();
			for (String conString : conferenceRoom.getStudentList()) {
				Student student = new Student();
				ConferenceStudentAssign cof = new ConferenceStudentAssign();
				student.setStudentId(conString);
				cof.setStudent(student);
				list.add(cof);
			}
			conferenceRoom.setListAssignStudent(list);
			// SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm ");
			// DateTimeFormatter formatter = DateTimeFormatter. ofPattern("d/MM/yyyy
			// HH:mm");

			// SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
			// String dateInString = "2014-10-05T15:23:01Z";
			// Date date=formatter.parse("2021-08-02T16:36");

			conferenceRoom.setStartDatetime(new SimpleDateFormat("yyyy-MM-dd").parse(conferenceRoom.getStartDate()));
			conferenceRoom.setEndDatetime(new SimpleDateFormat("yyyy-MM-dd").parse(conferenceRoom.getEndDate()));
			conferenceRoom.setStartTime(new SimpleDateFormat("HH:mm").parse(conferenceRoom.getSTime()));
			conferenceRoom.setEndTime(new SimpleDateFormat("HH:mm").parse(conferenceRoom.getETime()));

			LOGGER.info("save ConferenceRoom Service call");
			String id = service.saveConference(conferenceRoom);
			model.addAttribute("message", "ConferenceRoom save '" + id + "' successfully");
			LOGGER.info("save ConferenceRoom success {}", id);
		} catch (Exception e) {
			LOGGER.error("save ConferenceRoom failed {}", e.getLocalizedMessage());
			model.addAttribute("message", "ConferenceRoom save failed");
			e.printStackTrace();
		}
		ConferenceRoom conferenceRooms = new ConferenceRoom();
		conferenceRooms.setTeacher(conferenceRoom.getTeacher());
		conferenceRooms.setSubject(conferenceRoom.getSubject());
		model.addAttribute("conference", conferenceRooms);

		commonuiForTecher(model);
		return "Teacher/TeacherConferenceCreate";
	}

	// 3. get ConferenceRoom by id
	@GetMapping("/ConferenceRoomById")
	public String oneConferenceRoomById(@RequestParam String id, Model model) {

		try {
			LOGGER.info("One ConferenceRoom by id method called");
			ConferenceRoom conferenceRoom = service.getOneConferenceRoom(id);
			LOGGER.info("One batch by id service called success {}", id);
			model.addAttribute("ConferenceRoom", conferenceRoom);
		} catch (ConferenceRoomNotFoundException unfe) {
			throw unfe;
		} catch (Exception e) {
			LOGGER.error("Unable to get ConferenceRoom by id {}", e.getMessage());
			e.printStackTrace();
			model.addAttribute("message", "Not Found ConferenceRoom");
		}
		return "Conference/ConferenceRoomEdit";
	}

	// 4. Remove ConferenceRoom By Id
	@DeleteMapping("/removeConferenceRoomById")
	public String removeConferenceRoomById(@RequestParam String id, Model model) {
		try {
			LOGGER.info("remove ConferenceRoom by id method called");
			service.removeConferenceRoom(id);
			LOGGER.info("remove ConferenceRoom successfully {}", id);
		} catch (ConferenceRoomNotFoundException unfe) {
			throw unfe;
		} catch (DataIntegrityViolationException cvef) {
			throw new DataIntegrityViolationException("We can not delete ConferenceRoom! used by Other Entity");
		} catch (Exception e) {
			LOGGER.error("Unable to delete ConferenceRoom {}", e.getMessage());
			e.printStackTrace();
		}
		return "redirect:all";
	}

	// 5. Update dbConferenceRoom
	@PutMapping("/update")
	public String updatedbConferenceRoom(@ModelAttribute ConferenceRoom conferenceRoom, Model model) {
		LOGGER.info("Batch update called");
		try {
			ConferenceRoom dbConferenceRoom = service.getOneConferenceRoom(conferenceRoom.getConferenceId());
			util.copyNonNullValues(dbConferenceRoom, conferenceRoom);
			service.updateConference(dbConferenceRoom);
			LOGGER.info("dbConferenceRoom update  success {}", dbConferenceRoom.getConferenceId());

		} catch (Exception e) {
			LOGGER.error("dbConferenceRoom update failed");
			e.printStackTrace();
			model.addAttribute("message", "dbConferenceRoom updated failed ,please again");
		}

		return "redirect:all";
	}

	// 6. all ConferenceRoom in the page
	@GetMapping("/alls")
	public String allConferenceRoomByPage(@PageableDefault(page = 0, size = 10) Pageable pageable, Model model) {
		LOGGER.info("ConferenceRoom page service called");
		try {
			Page<ConferenceRoom> page = null;
			LOGGER.info("call ConferenceRoom page service");
			page = service.allConferenceRoom(pageable);
			LOGGER.info("success all ConferenceRoom page ");

			model.addAttribute("list", page.getContent());
			model.addAttribute("page", page);
		} catch (Exception e) {
			LOGGER.error("ConferenceRoom page fetch failed");
			e.printStackTrace();
			model.addAttribute("message", "ConferenceRoom fetch page failed ,please again");
		}

		return "Conference/ConferenceData";
	}

	// all view conference student list
	@GetMapping("/viewAllStudentById")
	public String allStudentInfoView(Model model, @RequestParam String id) {
		LOGGER.info("allStudentInfoView service called");
		try {
			LOGGER.info("call ConferenceRoom page service");
			ConferenceRoom conference = service.getOneConferenceRoom(id);
			LOGGER.info("success all ConferenceRoom page ");

			model.addAttribute("conference", conference);
		} catch (Exception e) {
			LOGGER.error("ConferenceRoom page fetch failed");
			e.printStackTrace();
			model.addAttribute("message", "ConferenceRoom fetch page failed ,please again");
		}

		return "Conference/ConferenceStudentData";
	}

	@GetMapping("/getTeacherBySubjectId")
	public @ResponseBody Map<String, String> validateModel(@RequestParam String subjectId) {
		Map<String, String> map = null;
		map = teacherService.allTeachers(subjectId);
		return map;
	}

	@GetMapping("/getStudentByBatchId")
	public @ResponseBody Map<String, String> getAllStudentByBatch(@RequestParam String batchId) {
		Map<String, String> map = null;
		map = studentService.allStudents(batchId);
		return map;
	}

	// join link
	@GetMapping("/join")
	public String joinMeeting(Model model, @RequestParam String conferenceStudentAssignId,
			@RequestParam String stream_url) {
		model.addAttribute("meeting", stream_url);
		service.updateStudentAttendance(conferenceStudentAssignId);
		return "Conference/MeetingJoin";
	}
}
