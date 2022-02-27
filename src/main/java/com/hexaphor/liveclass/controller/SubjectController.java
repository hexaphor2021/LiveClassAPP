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
import org.springframework.web.bind.annotation.ResponseBody;

import com.hexaphor.liveclass.model.Subject;
import com.hexaphor.liveclass.service.ISubjectService;
import com.hexaphor.liveclass.utils.SubjectUtil;

@Controller
@RequestMapping("/subject")
public class SubjectController {

	private Logger log = LoggerFactory.getLogger(SubjectController.class);
	@Autowired
	private ISubjectService service;
	@Autowired
	private SubjectUtil util;

	// 1. all Subject fetch
	@GetMapping("/alls")
	public String allSubject(Model model) {
		log.info("All Subject API");
		try {
			log.info("All Subject service call");
			model.addAttribute("list", service.allSubject());
			log.info("All service call success");
		} catch (Exception e) {
			log.error("All Service exception : {}", e.getLocalizedMessage());
			e.printStackTrace();
			model.addAttribute("message", "Subject data fetch problem ,please try again");
		}
		return "Subject/SubjectData";
	}

	// 2. Show register page
	@GetMapping("/register")
	public String showSubjectReg(Model model) {
		log.info("Subject register page show");
		model.addAttribute("subject", new Subject());
		return "Subject/SubjectRegister";
	}

	// 3. Save Subject
	@PostMapping("/save")
	public String saveSubject(@ModelAttribute Subject subject, Model model) {
		log.info("Save Subject called ");
		try {
			log.info("save Subject Service call");
			String id = service.saveSubject(subject);
			model.addAttribute("message", "Subject save '" + id + "' successfully");
			log.info("save Subject success {}", id);
		} catch (Exception e) {
			log.error("save Subject failed {}", e.getLocalizedMessage());
		}
		model.addAttribute("subject", new Subject());
		return "Subject/SubjectRegister";
	}

	// 4. get Subject by id
	@GetMapping("/SubjectById")
	public String oneSubjectById(Model model, @RequestParam String id) {
		log.info("One Subject by id method called");
		model.addAttribute("subject", service.oneSubjectById(id));
		log.info("One Subject by id service called success {}", id);
		return "Subject/SubjectEdit";
	}

	// 5. Remove Subject By Id
	@GetMapping("/removeSubjectById")
	public String removeOneSubjectById(Model model, @RequestParam String id) {
		log.info("remove Subject by id method called");
		service.removeById(id);
		log.info("remove Subject successfully");
		return "redirect:all";
	}

	// 6. Update Subject
	@PostMapping("/update")
	public String updateSubject(@ModelAttribute Subject subject, Model model) {
		log.info("Subject update called");
		try {
			Subject dbSubject = service.oneSubjectById(subject.getSubjectId());
			util.copyNonNullValues(dbSubject, subject);
			log.info("Subject update service call");
			String id = service.updateSubject(dbSubject);
			log.info("Subject update  success {}", id);

		} catch (Exception e) {
			log.error("Subject update failed");
			e.printStackTrace();
			model.addAttribute("subject", subject);
			model.addAttribute("message", "Subject updated failed ,please again");
			return "Subject/SubjectEdit";
		}

		return "redirect:all";
	}

	// 7. all Subject in the page
	@GetMapping("/all")
	public String allSubjectByPage(Model model, @PageableDefault(page = 0, size = 10) Pageable pageable,
			@RequestParam(required = false, value = "name", defaultValue = "") String name) {
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
			model.addAttribute("page", page);
			model.addAttribute("list", page.getContent());
		} catch (Exception e) {
			log.error("All Service exception : {}", e.getLocalizedMessage());
			e.printStackTrace();
			model.addAttribute("message", "Subject data fetch problem ,please try again");
		}

		return "Subject/SubjectData";
	}
	
	// AJAX VALIDATION
			@GetMapping("/validate")
			public @ResponseBody String validateModel(@RequestParam String subjectName, @RequestParam String id) {
				String message = "";
				if (id != "" && service.isSubjectNameExistForEdit(subjectName, id)) {
					// if id exist then request came from edit page//isUserNameExistForEdit
					message = "Subject Name '" + subjectName + "' already exist";
				}
				if (id == "" && service.isSubjectNameExist(subjectName)) {
					// if not id exist then request came from register page
					message = "Subject Name '" + subjectName + "' already exist";
				}
				return message;
			}
}
