package com.hexaphor.liveclass.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
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

import com.hexaphor.liveclass.exception.BatchNotFoundException;
import com.hexaphor.liveclass.model.Batch;
import com.hexaphor.liveclass.model.School;
import com.hexaphor.liveclass.service.IBatchService;
import com.hexaphor.liveclass.utils.BatchUtil;

@Controller
@RequestMapping("/batch")
public class BatchController {

	private Logger LOGGER = LoggerFactory.getLogger(SchoolController.class);
	@Autowired
	private IBatchService service;
	@Autowired
	private BatchUtil util;

	// 1. all Batcg fetch
	@GetMapping("/alls")
	public String allBatch(Model model) {
		LOGGER.info("All batch API");
		try {
			LOGGER.info("All batch service call");
			model.addAttribute("list", service.allBatch());
			LOGGER.info("All service call success");
		} catch (Exception e) {
			LOGGER.error("All Service exception : {}", e.getLocalizedMessage());
			e.printStackTrace();
			model.addAttribute("message", "batch data fetch problem ,please try again");
		}
		return "Batch/BatchData";
	}
	
	@GetMapping("/allbySchool")
	public String allBatch(Model model,String schoolId) {
		LOGGER.info("All batch API");
		try {
			School school=new School();
			school.setId(schoolId);
			LOGGER.info("All batch service call");
			model.addAttribute("list", service.allBatch(school));
			LOGGER.info("All service call success");
		} catch (Exception e) {
			LOGGER.error("All Service exception : {}", e.getLocalizedMessage());
			e.printStackTrace();
			model.addAttribute("message", "batch data fetch problem ,please try again");
		}
		return "Batch/BatchDataSchool";
	}

	// 2. Show register page
	@GetMapping("/register")
	public String showBatchReg(Model model) {
		LOGGER.info("school register page show");
		Batch batch=new Batch();
		model.addAttribute("batch", batch);
		return "Batch/BatchRegister";
	}

	// 3. Save batch
	@PostMapping("/save")
	public String savebatch(@ModelAttribute Batch batch, Model model) {
		LOGGER.info("Save batch called ");
		try {
			batch.setBatch(batch.getYear()+batch.getClassName()+batch.getStream());
			LOGGER.info("save batch Service call");
			String id = service.saveBatch(batch);
			model.addAttribute("message", "batch save '" + id + "' successfully");
			LOGGER.info("save batch success {}", id);
		} catch (Exception e) {
			LOGGER.error("save batch failed {}", e.getLocalizedMessage());
		}
		model.addAttribute("batch", new Batch());
		return "Batch/BatchRegister";
	}

	// 4. get batch by id
	@GetMapping("/batchById")
	public String onebatchById(Model model, @RequestParam String id) {
		LOGGER.info("One batch by id method called");
		model.addAttribute("batch", service.oneBatchById(id));
		LOGGER.info("One batch by id service called success {}", id);
		return "Batch/BatchEdit";
	}

	// 5. Remove batch By Id
	@GetMapping("/removeBatchById")
	public String removeOnebatchById(Model model, @RequestParam String id) {
		LOGGER.info("remove batch by id method called");
		try {
		service.remove(id);
		} catch (BatchNotFoundException unfe) {
			throw unfe;
		} catch (DataIntegrityViolationException cvef) {
			throw new DataIntegrityViolationException("We can not delete Batch! used by Other Entity");
		} catch (Exception e) {
			LOGGER.error("Unable to delete Batch {}", e.getMessage());
			e.printStackTrace();
		}
		LOGGER.info("remove batch successfully");
		return "redirect:all";
	}

	// 6. Update batch
	@PostMapping("/update")
	public String updatebatch(@ModelAttribute Batch batch, Model model) {
		LOGGER.info("batch update called");
		try {
			batch.setBatch(batch.getYear()+batch.getClassName()+batch.getStream());
			Batch dbBatch = service.oneBatchById(batch.getBatchId());
			util.copyNonNullValues(dbBatch, batch);
			LOGGER.info("Batch update service call");
			String id = service.updateBatch(dbBatch);
			LOGGER.info("Batch update  success {}", id);

		} catch (Exception e) {
			LOGGER.error("Batch update failed");
			e.printStackTrace();
			model.addAttribute("batch", batch);
			model.addAttribute("message", "Batch updated failed ,please again");
			return "Batch/BatchEdit";
		}

		return "redirect:all";
	}

	// 7. all Batch in the page
	@GetMapping("/all")
	public String allSchooByPage(Model model, @PageableDefault(page = 0, size = 10) Pageable pageable,
			@RequestParam(required = false, value = "batch", defaultValue = "") String batch) {
		LOGGER.info("Batch page service called");
		try {
			Page<Batch> page = null;
			if (batch.equals("")) {
				LOGGER.info("call Batch page service");
				page = service.allBatchPage(pageable);
				LOGGER.info("success all Batch page ");
			} else {
				LOGGER.info("call Batch page service by name filter");
				page = service.allBatchPage(pageable, batch);
				LOGGER.info("success Batch page service by name filter");
			}
			model.addAttribute("page", page);
			model.addAttribute("list", page.getContent());
		} catch (Exception e) {
			LOGGER.error("All Service exception : {}", e.getLocalizedMessage());
			e.printStackTrace();
			model.addAttribute("message", "Batch data fetch problem ,please try again");
		}

		return "Batch/BatchData";
	}
	
	// 7. all Batch in the page
		@GetMapping("/all/Batch")
		public String allBatchByPage(Model model, @PageableDefault(page = 0, size = 10) Pageable pageable,
				@RequestParam(required = false, value = "batch", defaultValue = "") String batch,String School) {
			LOGGER.info("Batch page service called");
			try {
				School school=new School();
				school.setId(School);
				Page<Batch> page = null;
				if (batch.equals("")) {
					LOGGER.info("call Batch page service");
					page = service.allBatchPage(pageable,School);
					LOGGER.info("success all Batch page ");
				} else {
					LOGGER.info("call Batch page service by name filter");
					page = service.allBatchPage(pageable, batch);
					LOGGER.info("success Batch page service by name filter");
				}
				model.addAttribute("page", page);
				model.addAttribute("list", page.getContent());
			} catch (Exception e) {
				LOGGER.error("All Service exception : {}", e.getLocalizedMessage());
				e.printStackTrace();
				model.addAttribute("message", "Batch data fetch problem ,please try again");
			}

			return "Batch/BatchData";
		}
		
		// AJAX VALIDATION
		@GetMapping("/validate")
		public @ResponseBody String validateModel(@RequestParam String batchName, @RequestParam String id) {
			String message = "";
			if (id != "" && service.isBatchNameExistForEdit(batchName, id)) {
				// if id exist then request came from edit page//isUserNameExistForEdit
				message = "Batch Name '" + batchName + "' already exist";
			}
			if (id == "" && service.isBatchNameExist(batchName)) {
				// if not id exist then request came from register page
				message = "Batch Name '" + batchName + "' already exist";
			}
			return message;
		}
}
