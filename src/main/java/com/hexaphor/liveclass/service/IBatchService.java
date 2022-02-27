package com.hexaphor.liveclass.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.hexaphor.liveclass.model.Batch;
import com.hexaphor.liveclass.model.School;

public interface IBatchService {

	public String saveBatch(Batch batch);

	public String updateBatch(Batch batch);

	public List<Batch> allBatch();

	public Page<Batch> allBatchPage(Pageable pageable);

	public Page<Batch> allBatchPage(Pageable pageable, String name);

	public Batch oneBatchById(String id);

	public void remove(String id);
	
	public Map<String,String> allBatches(School school);
	public Map<String,String> allBatches();
	
	public Page<Batch> allBatch(Pageable pageable,School school);
	
	public List<Batch> allBatch(School school);
	
	public boolean isBatchNameExistForEdit(String batchName,String id);
	public boolean isBatchNameExist(String batchName);
}
