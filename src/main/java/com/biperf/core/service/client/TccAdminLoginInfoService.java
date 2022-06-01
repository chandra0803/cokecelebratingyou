package com.biperf.core.service.client;

import com.biperf.core.domain.client.TccAdminLoginInfo;
import com.biperf.core.service.SAO;

/**
 * 
 * TccAdminLoginInfoService.
 * @author Ramesh jaligama
 * @since Aug 30, 2016
 * @version 1.0
 */
public interface TccAdminLoginInfoService extends SAO
{
  static final String BEAN_NAME = "tccAdminLoginInfoService";  
  
  public TccAdminLoginInfo save( Long adminUserId, Long paxUserId ) ;
  
}
