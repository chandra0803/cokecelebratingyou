
package com.biperf.core.dao.client;

import java.util.List;

import com.biperf.core.dao.DAO;
import com.biperf.core.domain.client.TcccQcardBatch;
import com.biperf.core.exception.ServiceErrorException;

public interface TcccQcardBatchDAO extends DAO {
	static final String BEAN_NAME = "tcccQcardBatchDAO";

	public List getAllQcardBatchList() throws ServiceErrorException;

	public TcccQcardBatch getQcardBatchById(Long qcardBatchId);

	public void saveQcardBatch(TcccQcardBatch tcccQcardBatch)throws ServiceErrorException;
	
	public List getQCardBatchByBatchNbr(String batchNbr);

}
