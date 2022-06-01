/*
 * (c) 2014 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/dao/cm/CmsAuthenticationDao.java,v $
 */

package com.biperf.core.dao.cm;

import java.util.List;

import com.objectpartners.cms.domain.CmsUser;

/**
 * 
 * @author kothanda
 * @since Dec 8, 2014
 * @version 1.0
 */
public interface CmsAuthenticationDao
{
  public CmsUser getUser( String username );

  public List<String> getRoles( Long userId );

  public void updateUserTermsAndConditions( int acceptance, long userId );

}
