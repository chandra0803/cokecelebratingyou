package com.biperf.core.dao.client;

import java.util.Map;

import com.biperf.core.dao.DAO;

/**
 * 
 * TccTempPwdExpiredDao.
 * 
 * @author Ramesh jaligama
 * @since Sep 5, 2016
 * @version 1.0
 */
public interface TccTempPwdExpiredDao extends DAO
{
  static final String BEAN_NAME = "tccTempPwdExpiredDao";  
  
  
  public Map callTccTempPasswordExpireCalcSP();
  
}
