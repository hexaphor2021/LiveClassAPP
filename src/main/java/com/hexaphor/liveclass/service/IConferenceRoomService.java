package com.hexaphor.liveclass.service;

import java.text.ParseException;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.hexaphor.liveclass.model.ConferenceRoom;
import com.hexaphor.liveclass.model.ConferenceStudentAssign;
import com.hexaphor.liveclass.model.School;
import com.hexaphor.liveclass.model.Student;
import com.hexaphor.liveclass.model.Teacher;

public interface IConferenceRoomService {
	
   public String saveConference(ConferenceRoom conferenceRoom);
   
   public String updateConference(ConferenceRoom conferenceRoom);
   
   public ConferenceRoom getOneConferenceRoom(String conferenceId);
   
   public void removeConferenceRoom(String conferenceId);
   
   public List<ConferenceRoom> allConferenceRoom();
   
   public List<ConferenceRoom> allConferenceRoom(School school);
   
   public Page<ConferenceRoom> allConferenceRoom(Pageable pageable);
   
   public Page<ConferenceRoom> allConferenceRoom(Pageable pageable,School school);
   
   public List<ConferenceStudentAssign> allConferenceByStudent(Student student);
   
   Page<ConferenceStudentAssign> findByStudent(Student student,Pageable pageable);
   
   public List<ConferenceStudentAssign> allStudentByConference(String conferenceId);
   
   Page<ConferenceStudentAssign> findByConferenceRoom(String conferenceId,Pageable pageable);
   
   public Page<ConferenceRoom> allConferenceRoomByTeacherId(Pageable pageable,Teacher teacher);
   
   public List<ConferenceRoom> allConferenceRoomByTeacherId(Teacher teacher) throws ParseException;
   
   public Page<ConferenceRoom> allConferenceRoomByTeacherId(Teacher teacher,Pageable pageable);
   
   public List<ConferenceRoom> allConferenceRoom(Teacher teacher);
   
   Page<ConferenceStudentAssign> findByConferenceMeeting(String studentId,Pageable page);
   
   List<ConferenceRoom> allConferenceByTeacher(String teacherId);
   
   public boolean updateStudentAttendance(String conferenceAssignId);

}
