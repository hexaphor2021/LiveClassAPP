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

import com.hexaphor.liveclass.exception.SchoolNotFoundException;
import com.hexaphor.liveclass.model.Subject;
import com.hexaphor.liveclass.service.ISubjectService;
import com.hexaphor.liveclass.utils.SubjectUtil;

@RestController
@RequestMapping("/rest/subject")
public class SubjectRestController {

	private Logger log = LoggerFactory.getLogger(SubjectRestController.class);
	@Autowired
	private ISubjectService service;
	@Autowired
	private SubjectUtil util;

	// 1. all Subject fetch
	@GetMapping("/all")
	public ResponseEntity<?> allSubject() {
		ResponseEntity<?> response = null;
		log.info("All Subject API");
		try {
			log.info("All Subject service call");
			List<Subject> list = service.allSubject();
			response = new ResponseEntity<List<Subject>>(list, HttpStatus.OK);
			log.info("All service call success");
		} catch (Exception e) {
			log.error("All Service exception : {}", e.getLocalizedMessage());
			response = new ResponseEntity<String>("Faild all Subject fetch", HttpStatus.BAD_GATEWAY);
			e.printStackTrace();
		}
		return response;
	}

	// 2. Save Subject
	@PostMapping("/save")
	public ResponseEntity<String> saveSubject(@RequestBody Subject subject) {
		ResponseEntity<String> response = null;
		log.info("Save Subject called ");
		try {
			log.info("save Subject Service call");
			String id = service.saveSubject(subject);
			response = new ResponseEntity<String>("Subject save '" + id + "' successfully", HttpStatus.CREATED);
			log.info("save Subject success {}", id);
		} catch (Exception e) {
			log.error("save Subject failed {}", e.getLocalizedMessage());
			response = new ResponseEntity<String>("save Subject failed" + e.getLocalizedMessage(),
					HttpStatus.EXPECTATION_FAILED);
		}
		return response;
	}

	// 3. get Subject by id
	@GetMapping("/SubjectById/{id}")
	public ResponseEntity<?> oneSubjectById(@PathVariable String id) {
		ResponseEntity<?> response = null;

		try {
			log.info("One Subject by id method called");
			Subject subject = service.oneSubjectById(id);
			log.info("One Subject by id service called success {}", id);
			response = new ResponseEntity<Subject>(subject, HttpStatus.OK);
		} catch (SchoolNotFoundException unfe) {
			throw unfe;
		} catch (Exception e) {
			log.error("Unable to get Subject by id {}", e.getMessage());
			response = new ResponseEntity<String>("Unable to fetch Subject by id", HttpStatus.INTERNAL_SERVER_ERROR);
			e.printStackTrace();
		}
		return response;
	}

	// 4. Remove Subject By Id
	@DeleteMapping("/removeSubjectById/{id}")
	public ResponseEntity<String> removeOneSubjectById(@PathVariable String id) {
		ResponseEntity<String> response = null;
		try {
			log.info("remove Subject by id method called");
			service.removeById(id);
			log.info("remove Subject successfully {}", id);
			response = ResponseEntity.ok("remove Subject successfully" + id);
		} catch (SchoolNotFoundException unfe) {
			throw unfe;
		} catch (DataIntegrityViolationException cvef) {
			throw new DataIntegrityViolationException("We can not delete Subject! used by Other Entity");
		} catch (Exception e) {
			log.error("Unable to delete Subject {}", e.getMessage());
			response = new ResponseEntity<String>("Unable to delete Subject", HttpStatus.INTERNAL_SERVER_ERROR);
			e.printStackTrace();
		}
		return response;
	}

	// 5. Update Subject
	@PutMapping("/update/{id}")
	public ResponseEntity<String> updateSubject(@RequestBody Subject subject, @PathVariable String id) {
		log.info("Subject update called");
		ResponseEntity<String> response = null;
		try {
			Subject dbSubject = service.oneSubjectById(id);
			util.copyNonNullValues(dbSubject, subject);
			log.info("Subject update service call");
			service.updateSubject(dbSubject);
			log.info("Subject update  success {}", id);
			response = ResponseEntity.ok("Subject update  success" + id);

		} catch (Exception e) {
			log.error("Subject update failed");
			e.printStackTrace();
			response = new ResponseEntity<String>("Subject updated failed ,please again",
					HttpStatus.EXPECTATION_FAILED);

		}

		return response;
	}

	// 6. all Subject in the page
	@GetMapping("/all/schoolpage")
	public ResponseEntity<?> allSchooByPage(@PageableDefault(page = 0, size = 10) Pageable pageable,
			@RequestParam(required = false, value = "name", defaultValue = "") String name) {
		ResponseEntity<?> response = null;
		log.info("Subject page service called");
		try {
			Page<Subject> page = null;
			if (name.equals("")) {
				log.info("call Subject page service");
				page = service.allSubject(pageable);
				log.info("success all Subject page ");
			} else {
				log.info("call Subject page service by name filter");
				page = service.allSubjectNameConstraints(pageable, name);
				log.info("success Subject page service by name filter");
			}
			response = new ResponseEntity<Page<Subject>>(page, HttpStatus.OK);
		} catch (Exception e) {
			log.error("Subject page fetch failed");
			e.printStackTrace();
			response = new ResponseEntity<String>("Subject fetch page failed ,please again", HttpStatus.BAD_REQUEST);
		}

		return response;
	}
}
