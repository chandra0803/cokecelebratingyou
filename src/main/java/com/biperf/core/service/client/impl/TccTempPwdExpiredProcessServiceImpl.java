package com.biperf.core.service.client.impl;

import java.util.Map;

import com.biperf.core.dao.client.TccTempPwdExpiredDao;
import com.biperf.core.service.client.TccTempPwdExpiredProcessService;
/**
 * 
 * TccTempPwdExpiredProcessServiceImpl.
 * @author Ramesh jaligama
 * @since Sep 5th, 2016
 * @version 1.0
 */
public class TccTempPwdExpiredProcessServiceImpl implements TccTempPwdExpiredProcessService
{

  private TccTempPwdExpiredDao  tccTempPwdExpiredDao; 


public TccTempPwdExpiredDao getTccTempPwdExpiredDao() {
	return tccTempPwdExpiredDao;
}

public void setTccTempPwdExpiredDao(TccTempPwdExpiredDao tccTempPwdExpiredDao) {
	this.tccTempPwdExpiredDao = tccTempPwdExpiredDao;
}



public Map callTccTempPasswordExpireCalcSP(){
	  
	  return tccTempPwdExpiredDao.callTccTempPasswordExpireCalcSP();
}

}
