package com.biperf.core.dao.client.hibernate;

import java.util.Map;

import javax.sql.DataSource;

import com.biperf.core.dao.BaseDAO;
import com.biperf.core.dao.client.TccTempPwdExpiredDao;

/**
 * 
 * TccTempPwdExpiredDaoImpl.
 * 
 * @author Ramesh jaligama
 * @since Sep 5, 2016
 * @version 1.0
 */
public class TccTempPwdExpiredDaoImpl extends BaseDAO implements TccTempPwdExpiredDao
{
  private TccTempPwdExpiredDao tccTempPwdExpiredDao;
	
  public TccTempPwdExpiredDao getTccTempPwdExpiredDao() {
	return tccTempPwdExpiredDao;
}


public void setTccTempPwdExpiredDao(TccTempPwdExpiredDao tccTempPwdExpiredDao) {
	this.tccTempPwdExpiredDao = tccTempPwdExpiredDao;
}


private DataSource dataSource;

  /**
   * set the data source.
   * 
   * @param dataSource
   */
  public void setDataSource( DataSource dataSource )
  {
    this.dataSource = dataSource;
  }

  
  public Map callTccTempPasswordExpireCalcSP()
  {
	  CallPrcAdihExpireTempPwCalcSP proc = new CallPrcAdihExpireTempPwCalcSP( dataSource );
	  
    return proc.executeProcedure();
  }

}
