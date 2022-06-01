package com.biperf.core.dao.client.hibernate;

import com.biperf.core.dao.BaseDAO;
import com.biperf.core.dao.client.TccAdminLoginInfoDAO;
import com.biperf.core.domain.client.TccAdminLoginInfo;
import com.biperf.core.utils.hibernate.HibernateUtil;

/**
 * 
 * TccAdminLoginInfoDAOImpl.
 * 
 * @author Ramesh jaligama
 * @since Aug 30, 2016
 * @version 1.0
 */
public class TccAdminLoginInfoDAOImpl extends BaseDAO implements TccAdminLoginInfoDAO
{
  @Override
  public TccAdminLoginInfo save( TccAdminLoginInfo tccAdminLoginInfo )
  {
	  TccAdminLoginInfo tccAdminLoginInfoReturned = null;
	  tccAdminLoginInfoReturned = (TccAdminLoginInfo)HibernateUtil.saveOrUpdateOrShallowMerge( tccAdminLoginInfo );
    return tccAdminLoginInfoReturned;
  }

}
