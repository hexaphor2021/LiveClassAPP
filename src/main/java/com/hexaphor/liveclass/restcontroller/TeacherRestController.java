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
import com.hexaphor.liveclass.model.Teacher;
import com.hexaphor.liveclass.service.ITeacherService;
import com.hexaphor.liveclass.utils.TeacherUtil;

@RestController
@RequestMapping("/rest/teacher")
public class TeacherRestController {

	private Logger LOGGER = LoggerFactory.getLogger(TeacherRestController.class);
	@Autowired
	private ITeacherService service;

	@Autowired
	private TeacherUtil util;

	// 1. all Teacher fetch
	@GetMapping("/all")
	public ResponseEntity<?> allTeacher() {
		ResponseEntity<?> response = null;
		LOGGER.info("All Teacher API");
		try {
			LOGGER.info("All Teacher service call");
			List<Teacher> list = service.allTeacher();
			response = new ResponseEntity<List<Teacher>>(list, HttpStatus.OK);
			LOGGER.info("All service call success");
		} catch (Exception e) {
			LOGGER.error("All Service exception : {}", e.getLocalizedMessage());
			response = new ResponseEntity<String>("Faild all Teacher fetch", HttpStatus.BAD_GATEWAY);
			e.printStackTrace();
		}
		return response;
	}

	// 2. Save Teacher
	@PostMapping("/save")
	public ResponseEntity<String> saveTeacher(@RequestBody Teacher teacher) {
		ResponseEntity<String> response = null;
		LOGGER.info("Save Teacher called ");
		try {
			LOGGER.info("save Teacher Service call");
			String id = service.saveTeacher(teacher);
			response = new ResponseEntity<String>("Teacher save '" + id + "' successfully", HttpStatus.CREATED);
			LOGGER.info("save Teacher success {}", id);
		} catch (Exception e) {
			LOGGER.error("save Teacher failed {}", e.getLocalizedMessage());
			response = new ResponseEntity<String>("save Teacher failed" + e.getLocalizedMessage(),
					HttpStatus.EXPECTATION_FAILED);
		}
		return response;
	}

	// 3. get Teacher by id
	@GetMapping("/TeacherById/{id}")
	public ResponseEntity<?> oneTeacherById(@PathVariable String id) {
		ResponseEntity<?> response = null;

		try {
			LOGGER.info("One Teacher by id method called");
			Teacher teacher = service.oneTeacherById(id);
			LOGGER.info("One Teacher by id service called success {}", id);
			response = new ResponseEntity<Teacher>(teacher, HttpStatus.OK);
		} catch (StudentNotFoundxception unfe) {
			throw unfe;
		} catch (Exception e) {
			LOGGER.error("Unable to get Teacher by id {}", e.getMessage());
			response = new ResponseEntity<String>("Unable to fetch Teacher by id", HttpStatus.INTERNAL_SERVER_ERROR);
			e.printStackTrace();
		}
		return response;
	}

	// 4. Remove Teacher By Id
	@DeleteMapping("/removeTeacherById/{id}")
	public ResponseEntity<String> removeOneTeacherById(@PathVariable String id) {
		ResponseEntity<String> response = null;
		try {
			LOGGER.info("remove Teacher by id method called");
			service.remove(id);
			LOGGER.info("remove Teacher successfully {}", id);
			response = ResponseEntity.ok("remove Teacher successfully" + id);
		} catch (StudentNotFoundxception unfe) {
			throw unfe;
		} catch (DataIntegrityViolationException cvef) {
			throw new DataIntegrityViolationException("We can not delete Teacher! used by Other Entity");
		} catch (Exception e) {
			LOGGER.error("Unable to delete Teacher {}", e.getMessage());
			response = new ResponseEntity<String>("Unable to delete Teacher", HttpStatus.INTERNAL_SERVER_ERROR);
			e.printStackTrace();
		}
		return response;
	}

	// 5. Update Teacher
	@PutMapping("/update/{id}")
	public ResponseEntity<String> updateTeacher(@RequestBody Teacher teacher, @PathVariable String id) {
		LOGGER.info("Teacher update called");
		ResponseEntity<String> response = null;
		try {
			Teacher dbTeacher = service.oneTeacherById(id);
			util.copyNonNullValues(dbTeacher, teacher);
			service.updateTeacher(dbTeacher);
			LOGGER.info("Teacher update  success {}", id);
			response = ResponseEntity.ok("Teacher update  success" + id);

		} catch (Exception e) {
			LOGGER.error("Student update failed");
			e.printStackTrace();
			response = new ResponseEntity<String>("Student updated failed ,please again",
					HttpStatus.EXPECTATION_FAILED);

		}

		return response;
	}

	// 6. all Teacher in the page
	@GetMapping("/all/Teacherpage")
	public ResponseEntity<?> allTeacherByPage(@PageableDefault(page = 0, size = 10) Pageable pageable,
			@RequestParam(required = false, value = "name", defaultValue = "") String name) {
		ResponseEntity<?> response = null;
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
			response = new ResponseEntity<Page<Teacher>>(page, HttpStatus.OK);
		} catch (Exception e) {
			LOGGER.error("Student page fetch failed");
			e.printStackTrace();
			response = new ResponseEntity<String>("Student fetch page failed ,please again", HttpStatus.BAD_REQUEST);
		}

		return response;
	}
}
