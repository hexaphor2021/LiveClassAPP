package com.hexaphor.liveclass.utils;

import org.springframework.stereotype.Component;

import com.hexaphor.liveclass.model.ConferenceRoom;
@Component
public class ConferenceRoomUtil {

	public void copyNonNullValues(ConferenceRoom dbConferenceRoom, ConferenceRoom conferenceRoom) {
		
		if(conferenceRoom.getBatch() !=null) dbConferenceRoom.setBatch(conferenceRoom.getBatch());
		
	}
}
