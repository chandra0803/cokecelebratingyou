package com.biperf.core.service.client.impl;

import java.sql.Timestamp;

import com.biperf.core.dao.client.TccAdminLoginInfoDAO;
import com.biperf.core.domain.client.TccAdminLoginInfo;
import com.biperf.core.service.client.TccAdminLoginInfoService;
/**
 * 
 * TccAdminLoginInfoService.
 * @author Ramesh jaligama
 * @since Aug 30, 2016
 * @version 1.0
 */
public class TccAdminLoginInfoServiceImpl implements TccAdminLoginInfoService
{

  private TccAdminLoginInfoDAO tccAdminLoginInfoDAO;
  
  
  /* methods */

  public TccAdminLoginInfoDAO getTccAdminLoginInfoDAO() {
	return tccAdminLoginInfoDAO;
}


public void setTccAdminLoginInfoDAO(TccAdminLoginInfoDAO tccAdminLoginInfoDAO) {
	this.tccAdminLoginInfoDAO = tccAdminLoginInfoDAO;
}


@Override
  public TccAdminLoginInfo save( Long adminUserId, Long paxUserId )
  {
    Timestamp timestamp=new Timestamp(System.currentTimeMillis());
    
    TccAdminLoginInfo adminloginInfo = new TccAdminLoginInfo();
   
    adminloginInfo.setAdminUserId( adminUserId );
    adminloginInfo.setPaxUserId( paxUserId );
    adminloginInfo.setLaunchAsDateAndTime( timestamp );

    return tccAdminLoginInfoDAO.save( adminloginInfo ) ;
  }

}
