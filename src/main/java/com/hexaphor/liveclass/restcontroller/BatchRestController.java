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

import com.hexaphor.liveclass.exception.BatchNotFoundException;
import com.hexaphor.liveclass.exception.SchoolNotFoundException;
import com.hexaphor.liveclass.model.Batch;
import com.hexaphor.liveclass.service.IBatchService;
import com.hexaphor.liveclass.utils.BatchUtil;
@RestController
@RequestMapping("/rest/batch")
public class BatchRestController {

	private Logger log = LoggerFactory.getLogger(BatchRestController.class);
	@Autowired
	private IBatchService service;
	@Autowired
	private BatchUtil util;

	// 1. all Batch fetch
	@GetMapping("/all")
	public ResponseEntity<?> allSchool() {
		ResponseEntity<?> response = null;
		log.info("All Batch API");
		try {
			log.info("All Batch service call");
			List<Batch> list = service.allBatch();
			response = new ResponseEntity<List<Batch>>(list, HttpStatus.OK);
			log.info("All service call success");
		} catch (Exception e) {
			log.error("All Service exception : {}", e.getLocalizedMessage());
			response = new ResponseEntity<String>("Faild all Batch fetch", HttpStatus.BAD_GATEWAY);
			e.printStackTrace();
		}
		return response;
	}

	// 2. Save Batch
	@PostMapping("/save")
	public ResponseEntity<String> saveSchool(@RequestBody Batch batch) {
		ResponseEntity<String> response = null;
		log.info("Save Batch called ");
		try {
			log.info("save batch Service call");
			String id = service.saveBatch(batch);
			response = new ResponseEntity<String>("batch save '" + id + "' successfully", HttpStatus.CREATED);
			log.info("save batch success {}", id);
		} catch (Exception e) {
			log.error("save batch failed {}", e.getLocalizedMessage());
			response = new ResponseEntity<String>("save batch failed" + e.getLocalizedMessage(),
					HttpStatus.EXPECTATION_FAILED);
		}
		return response;
	}

	// 3. get batch by id
	@GetMapping("/BatchById/{id}")
	public ResponseEntity<?> oneSchoolById(@PathVariable String id) {
		ResponseEntity<?> response = null;

		try {
			log.info("One batch by id method called");
			Batch batch = service.oneBatchById(id);
			log.info("One batch by id service called success {}", id);
			response = new ResponseEntity<Batch>(batch, HttpStatus.OK);
		} catch (SchoolNotFoundException unfe) {
			throw unfe;
		} catch (Exception e) {
			log.error("Unable to get Batch by id {}", e.getMessage());
			response = new ResponseEntity<String>("Unable to fetch Batch by id", HttpStatus.INTERNAL_SERVER_ERROR);
			e.printStackTrace();
		}
		return response;
	}

	// 4. Remove Batch By Id
	@DeleteMapping("/removeBatchById/{id}")
	public ResponseEntity<String> removeOneBatchById(@PathVariable String id) {
		ResponseEntity<String> response = null;
		try {
			log.info("remove Batch by id method called");
			service.remove(id);
			log.info("remove Batch successfully {}", id);
			response = ResponseEntity.ok("remove Batch successfully" + id);
		} catch (BatchNotFoundException unfe) {
			throw unfe;
		} catch (DataIntegrityViolationException cvef) {
			throw new DataIntegrityViolationException("We can not delete Batch! used by Other Entity");
		} catch (Exception e) {
			log.error("Unable to delete Batch {}", e.getMessage());
			response = new ResponseEntity<String>("Unable to delete Batch", HttpStatus.INTERNAL_SERVER_ERROR);
			e.printStackTrace();
		}
		return response;
	}

	// 5. Update Batch
	@PutMapping("/update/{id}")
	public ResponseEntity<String> updateBatch(@RequestBody Batch batch, @PathVariable String id) {
		log.info("Batch update called");
		ResponseEntity<String> response = null;
		try {
			Batch dbBatch = service.oneBatchById(id);
			util.copyNonNullValues(dbBatch, batch);
			log.info("Batch update service call");
			service.updateBatch(dbBatch);
			log.info("Batch update  success {}", id);
			response = ResponseEntity.ok("Batch update  success" + id);

		} catch (Exception e) {
			log.error("school update failed");
			e.printStackTrace();
			response = new ResponseEntity<String>("Batch updated failed ,please again", HttpStatus.EXPECTATION_FAILED);

		}

		return response;
	}

	// 6. all Batch in the page
	@GetMapping("/all/schoolpage")
	public ResponseEntity<?> allBatchByPage(@PageableDefault(page = 0, size = 10) Pageable pageable,
			@RequestParam(required = false, value = "name", defaultValue = "") String name) {
		ResponseEntity<?> response = null;
		log.info("Batch page service called");
		try {
			Page<Batch> page = null;
			if (name.equals("")) {
				log.info("call Batch page service");
				page = service.allBatchPage(pageable);
				log.info("success all Batch page ");
			} else {
				log.info("call Batch page service by name filter");
				page = service.allBatchPage(pageable, name);
				log.info("success Batch page service by name filter");
			}
			response = new ResponseEntity<Page<Batch>>(page, HttpStatus.OK);
		} catch (Exception e) {
			log.error("Batch page fetch failed");
			e.printStackTrace();
			response = new ResponseEntity<String>("Batch fetch page failed ,please again", HttpStatus.BAD_REQUEST);
		}

		return response;
	}

}
