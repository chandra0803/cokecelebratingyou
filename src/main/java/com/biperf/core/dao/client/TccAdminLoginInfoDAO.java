package com.biperf.core.dao.client;

import com.biperf.core.dao.DAO;
import com.biperf.core.domain.client.TccAdminLoginInfo;

/**
 * 
 * TccAdminLoginInfoDAO.
 * 
 * @author Ramesh jaligama
 * @since Aug 30, 2016
 * @version 1.0
 */
public interface TccAdminLoginInfoDAO extends DAO
{
  static final String BEAN_NAME = "tccAdminLoginInfoDAO";  
  
  public TccAdminLoginInfo save( TccAdminLoginInfo tccAdminLoginInfo ) ;

}
