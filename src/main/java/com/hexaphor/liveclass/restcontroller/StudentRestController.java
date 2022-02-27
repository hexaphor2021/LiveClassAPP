package com.hexaphor.liveclass.restcontroller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hexaphor.liveclass.exception.StudentNotFoundxception;
import com.hexaphor.liveclass.model.Student;
import com.hexaphor.liveclass.service.IStudentService;
import com.hexaphor.liveclass.utils.StudentUtil;

@RestController
@RequestMapping("/rest/student")
public class StudentRestController {

	private Logger LOGGER = LoggerFactory.getLogger(StudentRestController.class);
	@Autowired
	private IStudentService service;
	
	@Autowired
	private StudentUtil util;

	// 1. all Student fetch
	@GetMapping("/all")
	public ResponseEntity<?> allSchool() {
		ResponseEntity<?> response = null;
		LOGGER.info("All Student API");
		try {
			LOGGER.info("All Student service call");
			List<Student> list = service.allStudent();
			response = new ResponseEntity<List<Student>>(list, HttpStatus.OK);
			LOGGER.info("All service call success");
		} catch (Exception e) {
			LOGGER.error("All Service exception : {}", e.getLocalizedMessage());
			response = new ResponseEntity<String>("Faild all Student fetch", HttpStatus.BAD_GATEWAY);
			e.printStackTrace();
		}
		return response;
	}

	// 2. Save Student
	@PostMapping("/save")
	public ResponseEntity<String> saveStudent(@RequestBody Student student) {
		ResponseEntity<String> response = null;
		LOGGER.info("Save Student called ");
		try {
			LOGGER.info("save Student Service call");
			String id = service.saveStudent(student);
			response = new ResponseEntity<String>("Student save '" + id + "' successfully", HttpStatus.CREATED);
			LOGGER.info("save Student success {}", id);
		} catch (Exception e) {
			LOGGER.error("save Student failed {}", e.getLocalizedMessage());
			response = new ResponseEntity<String>("save Student failed" + e.getLocalizedMessage(),
					HttpStatus.EXPECTATION_FAILED);
		}
		return response;
	}

	// 3. get Student by id
	@GetMapping("/StudentById/{id}")
	public ResponseEntity<?> oneStudentById(@PathVariable String id) {
		ResponseEntity<?> response = null;

		try {
			LOGGER.info("One Student by id method called");
			Student student = service.oneStudentById(id);
			LOGGER.info("One batch by id service called success {}", id);
			response = new ResponseEntity<Student>(student, HttpStatus.OK);
		} catch (StudentNotFoundxception unfe) {
			throw unfe;
		} catch (Exception e) {
			LOGGER.error("Unable to get Student by id {}", e.getMessage());
			response = new ResponseEntity<String>("Unable to fetch Student by id", HttpStatus.INTERNAL_SERVER_ERROR);
			e.printStackTrace();
		}
		return response;
	}

	// 4. Remove Student By Id
	@DeleteMapping("/removeStudentById/{id}")
	public ResponseEntity<String> removeOneStudentById(@PathVariable String id) {
		ResponseEntity<String> response = null;
		try {
			LOGGER.info("remove Student by id method called");
			service.remove(id);
			LOGGER.info("remove Student successfully {}", id);
			response = ResponseEntity.ok("remove Student successfully" + id);
		} catch (StudentNotFoundxception unfe) {
			throw unfe;
		} catch (DataIntegrityViolationException cvef) {
			throw new DataIntegrityViolationException("We can not delete Student! used by Other Entity");
		} catch (Exception e) {
			LOGGER.error("Unable to delete Student {}", e.getMessage());
			response = new ResponseEntity<String>("Unable to delete Student", HttpStatus.INTERNAL_SERVER_ERROR);
			e.printStackTrace();
		}
		return response;
	}

	// 5. Update Student
	@PutMapping("/update/{id}")
	public ResponseEntity<String> updateStudent(@RequestBody Student student, @PathVariable String id) {
		LOGGER.info("Batch update called");
		ResponseEntity<String> response = null;
		try {
			Student dbStudent = service.oneStudentById(id);
			util.copyNonNullValues(dbStudent, student);
			service.updateStudent(dbStudent);
			LOGGER.info("Student update  success {}", id);
			response = ResponseEntity.ok("Student update  success" + id);

		} catch (Exception e) {
			LOGGER.error("Student update failed");
			e.printStackTrace();
			response = new ResponseEntity<String>("Student updated failed ,please again",
					HttpStatus.EXPECTATION_FAILED);
		}

		return response;
	}

	// 6. all Student in the page
	@GetMapping("/all/Studentpage")
	public ResponseEntity<?> allStudentByPage(@PageableDefault(page = 0, size = 10) Pageable pageable,
			@RequestParam(required = false, value = "name", defaultValue = "") String name) {
		ResponseEntity<?> response = null;
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
			response = new ResponseEntity<Page<Student>>(page, HttpStatus.OK);
		} catch (Exception e) {
			LOGGER.error("Student page fetch failed");
			e.printStackTrace();
			response = new ResponseEntity<String>("Student fetch page failed ,please again", HttpStatus.BAD_REQUEST);
		}

		return response;
	}

}
