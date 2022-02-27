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
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hexaphor.liveclass.exception.ConferenceRoomNotFoundException;
import com.hexaphor.liveclass.model.ConferenceRoom;
import com.hexaphor.liveclass.service.IConferenceRoomService;
import com.hexaphor.liveclass.utils.ConferenceRoomUtil;

@RestController
@RequestMapping("/rest/conference")
public class ConferenceRestController {
	private Logger LOGGER = LoggerFactory.getLogger(ConferenceRestController.class);
	@Autowired
	private IConferenceRoomService service;

	@Autowired
	private ConferenceRoomUtil util;

	// 1. all ConferenceRoom fetch
	@GetMapping("/all")
	public ResponseEntity<?> allConferenceRoom() {
		LOGGER.info("All ConferenceRoom API");
		ResponseEntity<?> response = null;
		try {
			LOGGER.info("All Student service call");
			List<ConferenceRoom> list = service.allConferenceRoom();
			response = new ResponseEntity<List<ConferenceRoom>>(list, HttpStatus.OK);
			LOGGER.info("All service call success");
		} catch (Exception e) {
			LOGGER.error("All Service exception : {}", e.getLocalizedMessage());
			response = new ResponseEntity<String>("Faild all ConferenceRoom fetch", HttpStatus.BAD_GATEWAY);
			e.printStackTrace();
		}
		return response;
	}

	// 2. Save ConferenceRoom
	@PostMapping("/save")
	public ResponseEntity<String> saveConferenceRoom(@RequestBody ConferenceRoom conferenceRoom) {
		LOGGER.info("Save ConferenceRoom called ");
		ResponseEntity<String> response = null;
		try {
			LOGGER.info("save ConferenceRoom Service call");
			String id = service.saveConference(conferenceRoom);
			response = new ResponseEntity<String>("ConferenceRoom save '" + id + "' successfully", HttpStatus.CREATED);
			LOGGER.info("save ConferenceRoom success {}", id);
		} catch (Exception e) {
			LOGGER.error("save ConferenceRoom failed {}", e.getLocalizedMessage());
			response = new ResponseEntity<String>("ConferenceRoom save failed", HttpStatus.EXPECTATION_FAILED);
		}
		return response;
	}

	// 3. get ConferenceRoom by id
	@GetMapping("/ConferenceRoomById")
	public ResponseEntity<?> oneConferenceRoomById(@RequestParam String id) {
		ResponseEntity<?> response = null;
		try {
			LOGGER.info("One ConferenceRoom by id method called");
			ConferenceRoom conferenceRoom = service.getOneConferenceRoom(id);
			LOGGER.info("One batch by id service called success {}", id);
			response = new ResponseEntity<ConferenceRoom>(conferenceRoom, HttpStatus.OK);
		} catch (ConferenceRoomNotFoundException unfe) {
			throw unfe;
		} catch (Exception e) {
			LOGGER.error("Unable to get ConferenceRoom by id {}", e.getMessage());
			e.printStackTrace();
			response = new ResponseEntity<String>("Unable to fetch ConferenceRoom by id",
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return response;
	}

	// 4. Remove ConferenceRoom By Id
	@DeleteMapping("/removeConferenceRoomById")
	public ResponseEntity<String> removeConferenceRoomById(@RequestParam String id, Model model) {
		ResponseEntity<String> response = null;
		try {
			LOGGER.info("remove ConferenceRoom by id method called");
			service.removeConferenceRoom(id);
			LOGGER.info("remove ConferenceRoom successfully {}", id);
			response = new ResponseEntity<String>("remove ConferenceRoom successfully {}" + id, HttpStatus.OK);
		} catch (ConferenceRoomNotFoundException unfe) {
			throw unfe;
		} catch (DataIntegrityViolationException cvef) {
			throw new DataIntegrityViolationException("We can not delete ConferenceRoom! used by Other Entity");
		} catch (Exception e) {
			LOGGER.error("Unable to delete ConferenceRoom {}", e.getMessage());
			e.printStackTrace();
			response = new ResponseEntity<String>("Unable to delete ConferenceRoom {}" + e.getLocalizedMessage(),
					HttpStatus.EXPECTATION_FAILED);
		}
		return response;
	}

	// 5. Update dbConferenceRoom
	@PutMapping("/update")
	public ResponseEntity<String> updatedbConferenceRoom(@RequestBody ConferenceRoom conferenceRoom) {
		LOGGER.info("Batch update called");
		ResponseEntity<String> response = null;
		try {
			ConferenceRoom dbConferenceRoom = service.getOneConferenceRoom(conferenceRoom.getConferenceId());
			util.copyNonNullValues(dbConferenceRoom, conferenceRoom);
			service.updateConference(dbConferenceRoom);
			LOGGER.info("dbConferenceRoom update  success {}", dbConferenceRoom.getConferenceId());
			response = new ResponseEntity<String>(
					"ConferenceRoom update '" + dbConferenceRoom.getConferenceId() + "' successfully",
					HttpStatus.CREATED);

		} catch (Exception e) {
			LOGGER.error("dbConferenceRoom update failed");
			e.printStackTrace();
			response = new ResponseEntity<String>("ConferenceRoom updated failed ,please again",
					HttpStatus.EXPECTATION_FAILED);
		}

		return response;
	}

	// 6. all ConferenceRoom in the page
	@GetMapping("/all/dbConferenceRoompage")
	public ResponseEntity<?> allConferenceRoomByPage(@PageableDefault(page = 0, size = 10) Pageable pageable) {
		LOGGER.info("ConferenceRoom page service called");
		ResponseEntity<?> response = null;
		try {
			Page<ConferenceRoom> page = null;
			LOGGER.info("call ConferenceRoom page service");
			page = service.allConferenceRoom(pageable);
			LOGGER.info("success all ConferenceRoom page ");
			response = new ResponseEntity<Page<ConferenceRoom>>(page, HttpStatus.OK);
		} catch (Exception e) {
			LOGGER.error("ConferenceRoom page fetch failed");
			e.printStackTrace();
			response = new ResponseEntity<String>(
					"ConferenceRoom fetch page failed ,please again {}" + e.getLocalizedMessage(),
					HttpStatus.EXPECTATION_FAILED);
		}

		return response;
	}

}
