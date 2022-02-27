package com.hexaphor.liveclass.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.hexaphor.liveclass.exception.ConferenceRoomNotFoundException;
import com.hexaphor.liveclass.model.ConferenceRoom;
import com.hexaphor.liveclass.model.ConferenceStudentAssign;
import com.hexaphor.liveclass.model.School;
import com.hexaphor.liveclass.model.Student;
import com.hexaphor.liveclass.model.Teacher;
import com.hexaphor.liveclass.repo.ConferenceRoomRepository;
import com.hexaphor.liveclass.repo.ConferenceStudentAssignRepository;
import com.hexaphor.liveclass.service.IConferenceRoomService;

@Service
public class ConferenceRoomServiceImpl implements IConferenceRoomService {

	@Autowired
	private ConferenceRoomRepository repo;
	@Autowired
	private ConferenceStudentAssignRepository assignRepo;
	
	@Override
	public String saveConference(ConferenceRoom conferenceRoom) {
		
		//ConferenceRoom con=new ConferenceRoom();
		//BeanUtils.copyProperties(conferenceRoom, con);
		conferenceRoom.setCreatedOn(new Date());
		String streamURL="https://meet.jit.si/";
		conferenceRoom.setStreamLink(streamURL);
		ConferenceRoom conference=repo.save(conferenceRoom);
		conference.setStreamLink(streamURL+conference.getConferenceId());
		ConferenceRoom conferences=repo.save(conference);
		List<ConferenceStudentAssign> list=new ArrayList<ConferenceStudentAssign>();
		
		conferenceRoom.getListAssignStudent().stream().forEach(s->{
			
			ConferenceStudentAssign conferenceStudentAssign=new ConferenceStudentAssign();
			conferenceStudentAssign.setStudent(s.getStudent());
			conferenceStudentAssign.setConferenceRoom(conferences);
			//conferenceStudentAssign.setSchool(s.getSchool());
			conferenceStudentAssign.setCreatedOn(new Date());
			conferenceStudentAssign.setStreamURL(conference.getStreamLink());
			list.add(conferenceStudentAssign);
			
		});
		
		
		assignRepo.saveAll(list);
		return conference.getConferenceId();
	}

	@Override
	public String updateConference(ConferenceRoom conferenceRoom) {
		conferenceRoom.setCreatedOn(new Date());
		String streamURL="https://meet.jit.si/";
		conferenceRoom.setStreamLink(streamURL);
		ConferenceRoom conference=repo.save(conferenceRoom);

		List<ConferenceStudentAssign> list=new ArrayList<ConferenceStudentAssign>();
		
		conferenceRoom.getListStudent().stream().forEach(s->{
			ConferenceStudentAssign conferenceStudentAssign=new ConferenceStudentAssign();
			conferenceStudentAssign.setStudent(s);
			conferenceStudentAssign.setConferenceRoom(conference);
			conferenceStudentAssign.setSchool(s.getSchool());
			conferenceStudentAssign.setCreatedOn(new Date());
			conferenceStudentAssign.setStreamURL(streamURL+conference.getConferenceId());
			list.add(conferenceStudentAssign);
			
		});
		assignRepo.saveAll(list);
		return conference.getConferenceId();
	}

	@Override
	public ConferenceRoom getOneConferenceRoom(String conferenceId) {
		
		return repo.findById(conferenceId).orElseThrow(()->new ConferenceRoomNotFoundException("Conference Room '"+conferenceId+"' not found."));
	}

	@Override
	public void removeConferenceRoom(String conferenceId) {
		ConferenceRoom conferenceRoom=	repo.findById(conferenceId).orElseThrow(()->new ConferenceRoomNotFoundException("Conference Room '"+conferenceId+"' not found."));
        repo.delete(conferenceRoom);
	}

	@Override
	public List<ConferenceRoom> allConferenceRoom() {
		
		return repo.findAll();
	}

	@Override
	public List<ConferenceRoom> allConferenceRoom(School school) {
		
		return repo.findBySchool(school);
	}

	@Override
	public Page<ConferenceRoom> allConferenceRoom(Pageable pageable) {
		
		return repo.findAll(pageable);
	}

	@Override
	public Page<ConferenceRoom> allConferenceRoom(Pageable pageable, School school) {
		
		return repo.findBySchoolContaining(school, pageable);
	}

	@Override
	public List<ConferenceStudentAssign> allConferenceByStudent(Student student) {
		
		return assignRepo.findByStudent(student);
	}

	@Override
	public List<ConferenceStudentAssign> allStudentByConference(String conferenceId) {
		ConferenceRoom conferenceRoom=new ConferenceRoom();
		conferenceRoom.setConferenceId(conferenceId);
		return assignRepo.findByConferenceRoom(conferenceRoom);
	}

	@Override
	public Page<ConferenceStudentAssign> findByConferenceRoom(String conferenceId, Pageable pageable) {
		ConferenceRoom conferenceRoom=new ConferenceRoom();
		conferenceRoom.setConferenceId(conferenceId);
		return assignRepo.findByConferenceRoom(conferenceRoom, pageable);
	}

	@Override
	public Page<ConferenceRoom> allConferenceRoomByTeacherId(Pageable pageable, Teacher teacher) {
		return repo.findByTeacher(teacher, pageable);
	}

	@Override
	public List<ConferenceRoom> allConferenceRoom(Teacher teacher) {
		return repo.findByTeacher(teacher);
	}

	@Override
	public Page<ConferenceStudentAssign> findByStudent(Student student, Pageable pageable) {
		
		return assignRepo.findByStudent(student, pageable);
	}

	@Override
	public Page<ConferenceStudentAssign> findByConferenceMeeting(String studentId, Pageable page) {
		Student student=new Student();
		student.setStudentId(studentId);
		Date currentDate=new Date();
		java.time.format.DateTimeFormatter dtf = java.time.format.DateTimeFormatter.ofPattern("HH:mm"); 
		java.time.LocalDateTime now = java.time.LocalDateTime.now(); 
		 
		Date sTime=null;
		try {
			sTime=new SimpleDateFormat("HH:mm").parse(dtf.format(now));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return assignRepo.findByConferenceMeeting(student,currentDate,sTime, page);
	}

	@Override
	public List<ConferenceRoom> allConferenceRoomByTeacherId(Teacher teacher) {
		// Adding 5 mins using Date constructor.
		/*
		 * Calendar date = Calendar.getInstance(); long timeInSecs =
		 * date.getTimeInMillis(); Date afterAdding5Mins = new Date(timeInSecs + (5 * 60
		 * * 1000)); Date currentDate=new Date();
		 */
		Date currentDate=new Date();
		java.time.format.DateTimeFormatter dtf = java.time.format.DateTimeFormatter.ofPattern("HH:mm"); 
		java.time.LocalDateTime now = java.time.LocalDateTime.now(); 
		 
		Date sTime=null;
		try {
			sTime=new SimpleDateFormat("HH:mm").parse(dtf.format(now));
		} catch (ParseException e) {
			e.printStackTrace();
		}
				
		return repo.allConferenceByTeacher(teacher, sTime, currentDate);
	}

	@Override
	public List<ConferenceRoom> allConferenceByTeacher(String teacherId) {
		/*
		 * Calendar date = Calendar.getInstance();
		 * System.out.println("Current Date and TIme : " + date.getTime()); long
		 * timeInSecs = date.getTimeInMillis(); Date afterAdding5Mins = new
		 * Date(timeInSecs + (5 * 60 * 1000)); Date currentDate=new Date(); Teacher
		 * teacher=new Teacher(); teacher.setTeacherId(teacherId); return
		 * repo.allConferenceByTeacher(teacher, afterAdding5Mins, currentDate);
		 */
		Date currentDate=new Date();
		java.time.format.DateTimeFormatter dtf = java.time.format.DateTimeFormatter.ofPattern("HH:mm"); 
		java.time.LocalDateTime now = java.time.LocalDateTime.now(); 
		 
		Date sTime=null;
		try {
			sTime=new SimpleDateFormat("HH:mm").parse(dtf.format(now));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		//return repo.allConferenceByTeacher(teacherId);
		Teacher teacher=new Teacher();
		teacher.setTeacherId(teacherId);
		return repo.allConferenceByTeacher(teacher, sTime, currentDate);
	}

	@Override
	public Page<ConferenceRoom> allConferenceRoomByTeacherId(Teacher teacher, Pageable pageable) {
		// Adding 5 mins using Date constructor.
		/*
		 * Calendar date = Calendar.getInstance();
		 * System.out.println("Current Date and TIme : " + date.getTime()); long
		 * timeInSecs = date.getTimeInMillis(); Date afterAdding5Mins = new
		 * Date(timeInSecs + (5 * 60 * 1000)); Date currentDate=new Date();
		 */
		Date currentDate=new Date();
		java.time.format.DateTimeFormatter dtf = java.time.format.DateTimeFormatter.ofPattern("HH:mm"); 
		java.time.LocalDateTime now = java.time.LocalDateTime.now(); 
		 
		Date sTime=null;
		try {
			sTime=new SimpleDateFormat("HH:mm").parse(dtf.format(now));
		} catch (ParseException e) {
			e.printStackTrace();
		}
				
		return repo.allConferenceByTeacher(teacher, sTime, currentDate,pageable);
	}

	@Override
	public boolean updateStudentAttendance(String conferenceAssignId) {
		
		return assignRepo.updateStatusStudentConference(conferenceAssignId, "Prasent")>0;
	}

}
