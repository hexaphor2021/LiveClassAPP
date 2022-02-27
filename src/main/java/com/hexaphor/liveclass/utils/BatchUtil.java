package com.hexaphor.liveclass.utils;

import java.util.Date;

import org.springframework.stereotype.Component;

import com.hexaphor.liveclass.model.Batch;
@Component
public class BatchUtil {

	public void copyNonNullValues(Batch dbBatch, Batch batch) {
		
		if(batch.getBatch() !=null) dbBatch.setBatch(batch.getBatch());
		if(batch.getClassName() !=null) dbBatch.setClassName(batch.getClassName());
		if(batch.getStream() !=null ) dbBatch.setStream(batch.getStream());
		if(batch.getYear() !=null) dbBatch.setYear(batch.getYear());
		batch.setUpdatedOn(new Date());
	}
}
