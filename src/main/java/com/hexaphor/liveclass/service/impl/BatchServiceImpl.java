package com.hexaphor.liveclass.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.hexaphor.liveclass.exception.BatchNotFoundException;
import com.hexaphor.liveclass.model.Batch;
import com.hexaphor.liveclass.model.School;
import com.hexaphor.liveclass.repo.BatchRepository;
import com.hexaphor.liveclass.service.IBatchService;
import com.hexaphor.liveclass.utils.MyCollectionUtil;

@Service
public class BatchServiceImpl implements IBatchService {

	@Autowired
	private BatchRepository repo;
	
	@Override
	public String saveBatch(Batch batch) {
		batch.setCreatedOn(new Date());
		return repo.save(batch).getBatchId();
	}

	@Override
	public String updateBatch(Batch batch) {
		batch.setUpdatedOn(new Date());
		return repo.save(batch).getBatchId();
	}

	@Override
	public List<Batch> allBatch() {
		
		return repo.findAll();
	}

	@Override
	public Page<Batch> allBatchPage(Pageable pageable) {
		
		return repo.findAll(pageable);
	}

	@Override
	public Page<Batch> allBatchPage(Pageable pageable, String name) {
		
		return repo.findByBatchContaining(name, pageable);
	}

	@Override
	public Batch oneBatchById(String id) {
		
		return repo.findById(id).orElseThrow(()->new BatchNotFoundException("Batch '"+id+"'not found."));
	}

	@Override
	public void remove(String id) {
		Batch batch= repo.findById(id).orElseThrow(()->new BatchNotFoundException("Batch '"+id+"'not found."));
		repo.delete(batch);

	}

	@Override
	public Map<String, String> allBatches(School school) {
		List<Object[]> listBatch=repo.allBatch(school);
		Map<String, String> map=new HashMap<String,String>();
		if(listBatch.size()==0) {
			
		}
		else {
			map=MyCollectionUtil.convertListToMap(listBatch);
		}
		return map;
	}

	@Override
	public Page<Batch> allBatch(Pageable pageable, School school) {
		
		return repo.findBySchoolContaining(school, pageable);
	}

	@Override
	public List<Batch> allBatch(School school) {
	
		return repo.findBySchool(school);
	}

	@Override
	public Map<String, String> allBatches() {
		List<Object[]> listBatch=repo.allBatch();
		Map<String, String> map=new HashMap<String,String>();
		if(listBatch.size()==0) {
			
		}
		else {
			map=MyCollectionUtil.convertListToMap(listBatch);
		}
		return map;
	}

	@Override
	public boolean isBatchNameExistForEdit(String batchName, String id) {
		return repo.getBatchCountForNotId(batchName, id)>0;
	}

	@Override
	public boolean isBatchNameExist(String batchName) {
		return repo.getBatchCount(batchName)>0;
	}

}
