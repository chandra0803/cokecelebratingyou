
package com.biperf.core.service.client;

import java.util.List;

import com.biperf.core.domain.client.TcccQcardBatch;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.SAO;

public interface TcccQcardBatchService extends SAO {
	public final String BEAN_NAME = "qcardBatchService";

	public List getAllQcardBatchList() throws ServiceErrorException;

	public TcccQcardBatch getQcardBatchById(Long qcardBatchId);

	public void saveQcardBatch(TcccQcardBatch tcccQcardBatch)throws ServiceErrorException;
	
	public List getQCardBatchByBatchNbr(String batchNbr);

}
