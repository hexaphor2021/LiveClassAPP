package com.hexaphor.liveclass.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import com.hexaphor.liveclass.model.Assignment;
import com.hexaphor.liveclass.model.AssignmentStudentAssign;
import com.hexaphor.liveclass.model.LoginUser;
import com.hexaphor.liveclass.model.Student;
import com.hexaphor.liveclass.model.Teacher;
import com.hexaphor.liveclass.service.IAssignmentService;
import com.hexaphor.liveclass.service.IBatchService;
import com.hexaphor.liveclass.service.IStudentService;
import com.hexaphor.liveclass.service.ISubjectService;
import com.hexaphor.liveclass.service.ITeacherService;
import com.hexaphor.liveclass.utils.UploadFileUtil;

@Controller
@RequestMapping("/assignment")
public class AssignmentController {
	private Logger LOGGER = LoggerFactory.getLogger(AssignmentController.class);

	@Autowired
	private IAssignmentService service;
	@Autowired
	private ISubjectService subjectService;

	@Autowired
	private IBatchService batchService;
	@Autowired
	private IStudentService studentService;

	@Autowired
	private ITeacherService teacherService;
	@Autowired
	private UploadFileUtil util;

	Path facultyTempletPath = Paths.get("src", "main", "resources", "static");

	public void commonui(Model model) {
		model.addAttribute("subjects", subjectService.allSubjects());
		model.addAttribute("batchs", batchService.allBatches());
	}

	@GetMapping("/uploadAssignmnet")
	public String showAdminUploadQuestionPaper(Model model) {
		model.addAttribute("assignment", new Assignment());
		commonui(model);
		return "Assignment/AssignmentUpload";
	}

	// Upload Assignment file
	@PostMapping("uploadfile")
	public String uploadFile(@ModelAttribute Assignment assignment, Model model) {
		try {
			assignment.setStartDatetime(new SimpleDateFormat("yyyy-MM-dd").parse(assignment.getStartDate()));
			assignment.setEndDatetime(new SimpleDateFormat("yyyy-MM-dd").parse(assignment.getEndDate()));
			assignment.setStartTime(new SimpleDateFormat("HH:mm").parse(assignment.getSTime()));
			assignment.setEndTime(new SimpleDateFormat("HH:mm").parse(assignment.getETime()));

			Assignment dbAssignment = service.saveAssignment(assignment);
			String filePath = util.assignmentFileSave(assignment.getUploadFile(), dbAssignment.getAssignmentId(),
					assignment.getBatch().getBatchId());
			dbAssignment.setUploadFilePath(filePath);
			service.saveAssignmentForAssignmentUpdate(dbAssignment);
			model.addAttribute("message", "File assignmnet  upload successfully ");
			model.addAttribute("assignment", new Assignment());
			commonui(model);
		} catch (Exception e) {
			LOGGER.error(e.getLocalizedMessage());
			model.addAttribute("message", "File assignmnet not upload ,Please try again !!!");
			model.addAttribute("assignment", assignment);
			commonui(model);
		}

		return "Assignment/AssignmentUpload";
	}

	// get all upload file
	@GetMapping("all_assignment")
	public String allUploadFile(Model model, @PageableDefault(page = 0, size = 10) Pageable pageable) {
		Page<Assignment> page = null;
		try {
			page = service.allAssignment(pageable);
		} catch (Exception e) {
			LOGGER.error(e.getLocalizedMessage());
		}
		model.addAttribute("list", page.getContent());
		model.addAttribute("page", page);
		return "Assignment/AssignmentData";
	}

	// Download question file
	@GetMapping("/downlaod")
	public StreamingResponseBody getSteamingFile(HttpServletResponse response, @RequestParam String asignmentId,
			@RequestParam String filePath) throws IOException {
		response.setContentType("application/pdf");
		LOGGER.info("Download Faculty Templet");
		response.setHeader("Content-Disposition", "attachment; filename=" + asignmentId + ".pdf");
		File file = new File(facultyTempletPath.toAbsolutePath() + File.separator + "/" + filePath);
		@SuppressWarnings("resource")
		InputStream inputStream = new FileInputStream(file);
		return outputStream -> {
			int nRead;
			byte[] data = new byte[1024];
			while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
				outputStream.write(data, 0, nRead);
			}
		};
	}

	// Student Answer file download
	@GetMapping("/student-answerfile")
	public StreamingResponseBody downloadStudentFile(HttpServletResponse response,
			@RequestParam String studentAssignmentId, @RequestParam String filePath) throws IOException {
		response.setContentType("application/pdf");
		LOGGER.info("Download Faculty Templet");
		response.setHeader("Content-Disposition", "attachment; filename=" + studentAssignmentId + ".pdf");
		File file = new File(facultyTempletPath.toAbsolutePath() + File.separator + "/" + filePath);
		@SuppressWarnings("resource")
		InputStream inputStream = new FileInputStream(file);
		return outputStream -> {
			int nRead;
			byte[] data = new byte[1024];
			while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
				// System.out.println("Writing some bytes..");
				outputStream.write(data, 0, nRead);
			}
		};
	}

	// Fetch all Student by AssignmentId
	@GetMapping("/viewAllStudentById")
	public String allStudentByAssignmentId(Model model, @RequestParam String assignmenetId) {
		Assignment assignmnet = null;
		try {

			assignmnet = service.getOneByAssignmnetId(assignmenetId);

		} catch (Exception e) {

		}
		model.addAttribute("assignment", assignmnet);
		return "Assignment/StudentAssignmnetData";
	}

	@GetMapping("/getTeacherBySubjectId")
	public @ResponseBody Map<String, String> validateModel(@RequestParam String subjectId) {
		Map<String, String> map = null;
		map = teacherService.allTeachers(subjectId);
		return map;
	}

	// Teacher Role
	@GetMapping("/uploadAssignmnetTeacher")
	public String showAdminUploadQuestionPaperForteacher(Model model,HttpSession session) {
		LoginUser user = (LoginUser) session.getAttribute("userOb");
		Teacher teacher = teacherService.oneTeacherByLoginUser(user);
		Assignment assignment=new Assignment();
		assignment.setTeacher(teacher);
		assignment.setSubject(teacher.getSubject());
		model.addAttribute("assignment", assignment);
		commonui(model);
		return "Teacher/AssignmentUpload";
	}

	// Upload Assignment file
	@PostMapping("uploadfileTeacher")
	public String uploadFileForTeacher(@ModelAttribute Assignment assignment, Model model) {
		try {
			assignment.setStartDatetime(new SimpleDateFormat("yyyy-MM-dd").parse(assignment.getStartDate()));
			assignment.setEndDatetime(new SimpleDateFormat("yyyy-MM-dd").parse(assignment.getEndDate()));
			assignment.setStartTime(new SimpleDateFormat("HH:mm").parse(assignment.getSTime()));
			assignment.setEndTime(new SimpleDateFormat("HH:mm").parse(assignment.getETime()));

			Assignment dbAssignment = service.saveAssignment(assignment);
			String filePath = util.assignmentFileSave(assignment.getUploadFile(), dbAssignment.getAssignmentId(),
					assignment.getBatch().getBatchId());
			dbAssignment.setUploadFilePath(filePath);
			service.saveAssignmentForAssignmentUpdate(dbAssignment);
			model.addAttribute("message", "File assignmnet  upload successfully ");
			model.addAttribute("assignment", new Assignment());
			commonui(model);
		} catch (Exception e) {
			LOGGER.error(e.getLocalizedMessage());
			model.addAttribute("message", "File assignmnet not upload ,Please try again !!!");
			model.addAttribute("assignment", assignment);
			commonui(model);
		}

		return "Teacher/AssignmentUpload";
	}

	// get all upload file
	@GetMapping("all_assignment-for-teacher")
	public String allUploadFileForTeacher(HttpSession session, Model model,
			@PageableDefault(page = 0, size = 10) Pageable pageable) {
		Page<Assignment> page = null;
		// Temporary add teacher for testing actually use session for this
		LoginUser loginUser = (LoginUser) session.getAttribute("userOb");

		Teacher teacher = teacherService.oneTeacherByLoginUser(loginUser);
		try {

			page = service.findByTeacher(teacher, pageable);
		} catch (Exception e) {
			LOGGER.error(e.getLocalizedMessage());
		}
		model.addAttribute("list", page.getContent());
		model.addAttribute("page", page);
		return "Teacher/AssignmentData";
	}

	//Student Role API
	@GetMapping("/allAssignmentByStudent")
	public String allConferenceData(@PageableDefault(page = 0, size = 10) Pageable pageable, Model model,HttpSession session) {
		LOGGER.info("ConferenceRoom page service called");
		try {
			//Temporary add teacher for testing actually use session for this 
            LoginUser user= (LoginUser) session.getAttribute("userOb");
			
			Student student=studentService.oneStudentByLoginUser(user);
			
			Page<AssignmentStudentAssign> page = null;
				LOGGER.info("call ConferenceRoom page service");
				page = service.findByStudent(student, pageable);
				LOGGER.info("success all ConferenceRoom page ");
			
			model.addAttribute("list", page.getContent());
			model.addAttribute("page", page);
		} catch (Exception e) {
			LOGGER.error("ConferenceRoom page fetch failed");
			e.printStackTrace();
			model.addAttribute("message", "ConferenceRoom fetch page failed ,please again");
		}
		return "Student/StudentAssignmentData";
	}
	
	//Student Current assignment 
		@GetMapping("/current-assignment-by-Student")
		public String currentAssignmentByStudent(@PageableDefault(page = 0, size = 10) Pageable pageable, Model model,HttpSession session) {
			LOGGER.info("ConferenceRoom page service called");
			try {
				//Temporary add teacher for testing actually use session for this 
	            LoginUser user= (LoginUser) session.getAttribute("userOb");
				
				Student student=studentService.oneStudentByLoginUser(user);
				
				Page<AssignmentStudentAssign> page = null;
					LOGGER.info("call ConferenceRoom page service");
					page = service.allCurrentAssignmentByStudent(student, pageable);
					LOGGER.info("success all ConferenceRoom page ");
				
				model.addAttribute("list", page.getContent());
				model.addAttribute("page", page);
			} catch (Exception e) {
				LOGGER.error("ConferenceRoom page fetch failed");
				e.printStackTrace();
				model.addAttribute("message", "ConferenceRoom fetch page failed ,please again");
			}
			return "Student/StudentCurrentAssignmentData";
		}
	
		@GetMapping("/answer-uploadfile")
		public String uploadAnswerFile(Model model,@RequestParam String studentAssignmentId) {
			AssignmentStudentAssign assignmentStudentAssign=service.AssignmentStudentAssignById(studentAssignmentId);
			model.addAttribute("assignmentStudentAssign", assignmentStudentAssign);
			return "Student/AnswerUpload";
		}
		@PostMapping("/answer-uploadfile")
		public String uploadAnswerFilePost(Model model,@ModelAttribute AssignmentStudentAssign assignmentStudentAssign,HttpSession session) throws IOException {
            try {
                LoginUser user= (LoginUser) session.getAttribute("userOb");
       			
       			Student student=studentService.oneStudentByLoginUser(user);
    			String filePath=util.saveAnswerFile(assignmentStudentAssign.getUploadFile(), assignmentStudentAssign.getAssignment().getAssignmentId(), student.getStudentId(), assignmentStudentAssign.getAssignmentstudentId());
                service.updateStatusFilePath(filePath, assignmentStudentAssign.getAssignmentstudentId(), "Upload");
            }catch (Exception e) {
            	LOGGER.error(e.getLocalizedMessage());
			}
			return "redirect:current-assignment-by-Student";
		}
	
}
