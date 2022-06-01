
package com.biperf.core.service.client.impl;

import java.util.List;

import com.biperf.core.dao.client.TcccQcardBatchDAO;
import com.biperf.core.domain.client.TcccQcardBatch;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.client.TcccQcardBatchService;

public class TcccQcardBatchServiceImpl implements TcccQcardBatchService {
	private TcccQcardBatchDAO tcccQcardBatchDAO;

	public TcccQcardBatchDAO getTcccQcardBatchDAO() {
		return tcccQcardBatchDAO;
	}

	public void setTcccQcardBatchDAO(TcccQcardBatchDAO tcccQcardBatchDAO) {
		this.tcccQcardBatchDAO = tcccQcardBatchDAO;
	}

	public List getAllQcardBatchList() throws ServiceErrorException {
		List qcardBatchList = tcccQcardBatchDAO.getAllQcardBatchList();
		return qcardBatchList;

	}

	public TcccQcardBatch getQcardBatchById(Long qcardBatchId) {
		TcccQcardBatch tcccQcardBatch = tcccQcardBatchDAO.getQcardBatchById(qcardBatchId);
		return tcccQcardBatch;
	}

	public void saveQcardBatch(TcccQcardBatch tcccQcardBatch) throws ServiceErrorException {
		tcccQcardBatchDAO.saveQcardBatch(tcccQcardBatch);
	}
	public List getQCardBatchByBatchNbr(String batchNbr)
	  {
	    return tcccQcardBatchDAO.getQCardBatchByBatchNbr(batchNbr);
	    
	  }

}
