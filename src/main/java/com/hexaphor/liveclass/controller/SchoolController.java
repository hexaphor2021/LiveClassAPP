package com.hexaphor.liveclass.controller;

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

import com.hexaphor.liveclass.model.School;
import com.hexaphor.liveclass.service.ISchoolService;
import com.hexaphor.liveclass.utils.SchoolUtil;

@Controller
@RequestMapping("/school")
public class SchoolController {

	private Logger log = LoggerFactory.getLogger(SchoolController.class);
	@Autowired
	private ISchoolService service;
	@Autowired
	private SchoolUtil util;

	// 1. all school fetch
	@GetMapping("/alls")
	public String allSchool(Model model) {
		log.info("All School API");
		try {
			log.info("All School service call");
			model.addAttribute("list", service.allSchool());
			log.info("All service call success");
		} catch (Exception e) {
			log.error("All Service exception : {}", e.getLocalizedMessage());
			e.printStackTrace();
			model.addAttribute("message", "School data fetch problem ,please try again");
		}
		return "School/SchoolData";
	}

	// 2. Show register page
	@GetMapping("/register")
	public String showSchoolReg(Model model) {
		log.info("school register page show");
		model.addAttribute("school", new School());
		return "School/SchoolRegister";
	}

	// 3. Save School
	@PostMapping("/save")
	public String saveSchool(@ModelAttribute School school, Model model) {
		log.info("Save School called ");
		try {
			log.info("save school Service call");
			String id = service.saveSchool(school);
			model.addAttribute("message", "School save '" + id + "' successfully");
			log.info("save school success {}", id);
		} catch (Exception e) {
			log.error("save school failed {}", e.getLocalizedMessage());
		}
		model.addAttribute("school", new School());
		return "School/SchoolRegister";
	}

	// 4. get school by id
	@GetMapping("/schoolById")
	public String oneSchoolById(Model model, @RequestParam String id) {
		log.info("One school by id method called");
		model.addAttribute("school", service.oneSchoolById(id));
		log.info("One school by id service called success {}", id);
		return "School/SchoolEdit";
	}

	// 5. Remove school By Id
	@GetMapping("/removeShoolById")
	public String removeOneSchoolById(Model model, @RequestParam String id) {
		log.info("remove school by id method called");
		service.remove(id);
		log.info("remove school successfully");
		return "redirect:all";
	}

	// 6. Update school
	@PostMapping("/update")
	public String updateSchool(@ModelAttribute School school, Model model) {
		log.info("school update called");
		try {
			School dbSchool = service.oneSchoolById(school.getId().toString());
			util.copyNonNullValues(dbSchool, school);
			log.info("School update service call");
			String id = service.updateSchool(dbSchool);
			log.info("school update  success {}", id);

		} catch (Exception e) {
			log.error("school update failed");
			e.printStackTrace();
			model.addAttribute("school", school);
			model.addAttribute("message", "School updated failed ,please again");
			return "School/SchoolEdit";
		}

		return "redirect:all";
	}

	// 7. all school in the page
	@GetMapping("/all")
	public String allSchooByPage(Model model, @PageableDefault(page = 0, size = 10) Pageable pageable,
			@RequestParam(required = false, value = "name", defaultValue = "") String name) {
		log.info("School page service called");
		try {
			Page<School> page = null;
			if (name.equals("")) {
				log.info("call school page service");
				page = service.allSchoolPage(pageable);
				log.info("success all school page ");
			} else {
				log.info("call school page service by name filter");
				page = service.allSchoolPage(pageable, name);
				log.info("success school page service by name filter");
			}
			model.addAttribute("page", page);
			model.addAttribute("list", page.getContent());
		} catch (Exception e) {
			log.error("All Service exception : {}", e.getLocalizedMessage());
			e.printStackTrace();
			model.addAttribute("message", "School data fetch problem ,please try again");
		}

		return "School/SchoolData";
	}
}
