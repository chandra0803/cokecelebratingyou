package com.biperf.core.service.client;

import java.util.Map;

import com.biperf.core.service.SAO;

/**
 * 
 * TccTempPwdExpiredProcessService.
 * @author Ramesh jaligama
 * @since Sep 5, 2016
 * @version 1.0
 */
public interface TccTempPwdExpiredProcessService extends SAO
{
  static final String BEAN_NAME = "tccTempPwdExpiredProcessService";
  
  public Map callTccTempPasswordExpireCalcSP();  
}
