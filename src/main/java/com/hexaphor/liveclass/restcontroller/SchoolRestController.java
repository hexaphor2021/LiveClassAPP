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
import com.hexaphor.liveclass.model.School;
import com.hexaphor.liveclass.service.ISchoolService;
import com.hexaphor.liveclass.utils.SchoolUtil;

@RestController
@RequestMapping("/rest/school")
public class SchoolRestController {
	private Logger LOGGER = LoggerFactory.getLogger(SchoolRestController.class);
	@Autowired
	private ISchoolService service;
	@Autowired
	private SchoolUtil util;

	// 1. all school fetch
	@GetMapping("/all")
	public ResponseEntity<?> allSchool() {
		ResponseEntity<?> response = null;
		LOGGER.info("All School API");
		try {
			LOGGER.info("All School service call");
			List<School> list = service.allSchool();
			response = new ResponseEntity<List<School>>(list, HttpStatus.OK);
			LOGGER.info("All service call success");
		} catch (Exception e) {
			LOGGER.error("All Service exception : {}", e.getLocalizedMessage());
			response = new ResponseEntity<String>("Faild all school fetch", HttpStatus.BAD_GATEWAY);
			e.printStackTrace();
		}
		return response;
	}

	// 2. Save School
	@PostMapping("/save")
	public ResponseEntity<String> saveSchool(@RequestBody School school) {
		ResponseEntity<String> response = null;
		LOGGER.info("Save School called ");
		try {
			LOGGER.info("save school Service call");
			String id = service.saveSchool(school);
			response = new ResponseEntity<String>("School save '" + id + "' successfully", HttpStatus.CREATED);
			LOGGER.info("save school success {}", id);
		} catch (Exception e) {
			LOGGER.error("save school failed {}", e.getLocalizedMessage());
			response = new ResponseEntity<String>("save school failed" + e.getLocalizedMessage(),
					HttpStatus.EXPECTATION_FAILED);
		}
		return response;
	}

	// 3. get school by id
	@GetMapping("/schoolById/{id}")
	public ResponseEntity<?> oneSchoolById(@PathVariable String id) {
		ResponseEntity<?> response = null;

		try {
			LOGGER.info("One school by id method called");
			School school = service.oneSchoolById(id);
			LOGGER.info("One school by id service called success {}", id);
			response = new ResponseEntity<School>(school, HttpStatus.OK);
		} catch (SchoolNotFoundException unfe) {
			throw unfe;
		} catch (Exception e) {
			LOGGER.error("Unable to get School by id {}", e.getMessage());
			response = new ResponseEntity<String>("Unable to fetch school by id", HttpStatus.INTERNAL_SERVER_ERROR);
			e.printStackTrace();
		}
		return response;
	}

	// 4. Remove school By Id
	@DeleteMapping("/removeShoolById/{id}")
	public ResponseEntity<String> removeOneSchoolById(@PathVariable String id) {
		ResponseEntity<String> response = null;
		try {
			LOGGER.info("remove school by id method called");
			service.remove(id);
			LOGGER.info("remove school successfully {}", id);
			response = ResponseEntity.ok("remove school successfully" + id);
		} catch (SchoolNotFoundException unfe) {
			throw unfe;
		} catch (DataIntegrityViolationException cvef) {
			throw new DataIntegrityViolationException("We can not delete School! used by Other Entity");
		} catch (Exception e) {
			LOGGER.error("Unable to delete School {}", e.getMessage());
			response = new ResponseEntity<String>("Unable to delete school", HttpStatus.INTERNAL_SERVER_ERROR);
			e.printStackTrace();
		}
		return response;
	}

	// 5. Update school
	@PutMapping("/update/{id}")
	public ResponseEntity<String> updateSchool(@RequestBody School school, @PathVariable String id) {
		LOGGER.info("school update called");
		ResponseEntity<String> response = null;
		try {
			School dbSchool = service.oneSchoolById(id);
			util.copyNonNullValues(dbSchool, school);
			LOGGER.info("School update service call");
			service.updateSchool(dbSchool);
			LOGGER.info("school update  success {}", id);
			response = ResponseEntity.ok("school update  success" + id);

		} catch (Exception e) {
			LOGGER.error("school update failed");
			e.printStackTrace();
			response = new ResponseEntity<String>("School updated failed ,please again", HttpStatus.EXPECTATION_FAILED);
		
		}

		return response;
	}
	
	//6. all school in the page
	@GetMapping("/all/schoolpage")
	public ResponseEntity<?> allSchooByPage(@PageableDefault(page = 0, size = 10) Pageable pageable,@RequestParam(required = false,value = "name",defaultValue = "") String name){
		ResponseEntity<?> response=null;
		LOGGER.info("School page service called");
		try {
			Page<School> page=null;
			if(name.equals("")) {
				LOGGER.info("call school page service");
				page=service.allSchoolPage(pageable);
				LOGGER.info("success all school page ");
			}else {
				LOGGER.info("call school page service by name filter");
				page=service.allSchoolPage(pageable, name);
				LOGGER.info("success school page service by name filter");
			}
			response = new ResponseEntity<Page<School>>(page, HttpStatus.OK);
		}catch (Exception e) {
			LOGGER.error("school page fetch failed");
			e.printStackTrace();
			response = new ResponseEntity<String>("School fetch page failed ,please again", HttpStatus.BAD_REQUEST);
		}
		
		return response;
	}
}
