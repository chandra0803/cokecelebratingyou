
package com.biperf.core.dao.client.hibernate;

import java.util.List;

import org.hibernate.Query;

import com.biperf.core.dao.BaseDAO;
import com.biperf.core.dao.client.TcccQcardBatchDAO;
import com.biperf.core.domain.client.TcccQcardBatch;

import com.biperf.core.exception.ServiceErrorException;

public class TcccQcardBatchDAOImpl extends BaseDAO implements TcccQcardBatchDAO {

	private TcccQcardBatchDAO tcccQcardBatchDAO;

	public TcccQcardBatchDAO getTcccQcardBatchDAO() {
		return tcccQcardBatchDAO;
	}

	public void setTcccQcardBatchDAO(TcccQcardBatchDAO tcccQcardBatchDAO) {
		this.tcccQcardBatchDAO = tcccQcardBatchDAO;
	}

	@Override
	public List getAllQcardBatchList() throws ServiceErrorException {
		// TODO Auto-generated method stub
		Query query = getSession().getNamedQuery("com.biperf.core.domain.client.TccQcardBatch.TccQcardBatchList");

		return query.list();

	}

	public TcccQcardBatch getQcardBatchById(Long qcardBatchId) {
		return (TcccQcardBatch) getSession().get(TcccQcardBatch.class, qcardBatchId);
	}

	public void saveQcardBatch(TcccQcardBatch tcccQcardBatch) throws ServiceErrorException{
		if (tcccQcardBatch.getId() != null && tcccQcardBatch.getId() > 0) {
			getSession().update(tcccQcardBatch);
		} else {
			getSession().save(tcccQcardBatch);
		}
	}
	
	public List getQCardBatchByBatchNbr(String batchNbr)
	  {
	    Query query = getSession().getNamedQuery( "com.biperf.core.domain.client.TccQcardBatch.getQCardBatchByBatchNbr" );
	    query.setParameter( "batchNbr", batchNbr );
	    return query.list();
	  }

}
