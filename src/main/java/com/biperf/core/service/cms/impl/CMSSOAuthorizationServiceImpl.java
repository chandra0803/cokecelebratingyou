/*
 * (c) 2006 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/cms/impl/CMSSOAuthorizationServiceImpl.java,v $
 */

package com.biperf.core.service.cms.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;

import com.biperf.cache.Cache;
import com.biperf.cache.CacheFactory;
import com.biperf.cache.jdk.JdkCacheFactoryImpl;
import com.biperf.core.dao.cm.CmsAuthenticationDao;
import com.objectpartners.cms.service.AuthorizationService;
import com.objectpartners.cms.util.CmsUtil;
import com.objectpartners.cms.util.UserUtil;

/**
 * CMAuthorizationServiceImpl.
 * <p>
 * <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>Todd</td>
 * <td>Oct 23, 2006</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class CMSSOAuthorizationServiceImpl implements AuthorizationService, InitializingBean
{
  private static final Log logger = LogFactory.getLog( CMSSOAuthorizationServiceImpl.class );
  private Cache cache;

  private CmsAuthenticationDao cmsAuthenticationDao;

  public void afterPropertiesSet()
  {
    CacheFactory cacheFactory = new JdkCacheFactoryImpl();
    Properties cmTermsCacheProps = new Properties();
    cmTermsCacheProps.put( JdkCacheFactoryImpl.CAPACITY_PROPERTY, "100" );
    Map cacheMap = new HashMap();
    cacheMap.put( "cm_terms", cmTermsCacheProps );
    cacheFactory.setRegionProperties( cacheMap );
    cache = cacheFactory.getCache( "cm_terms" );
  }

  /**
   * Overridden from
   * 
   * @see com.objectpartners.cms.service.AuthorizationService#getTermsAndConditionsAcceptance()
   * @return a boolean indicating if the user has accepted the CM Terms and Conditions
   */
  public boolean getTermsAndConditionsAcceptance()
  {
    String userName = CmsUtil.getUserName();
    if ( userName != null )
    {
      if ( cache.containsKey( userName.toLowerCase() ) )
      {
        return true;
      }
      return UserUtil.isUserAcceptedTermsAndConditions();
    }
    else
    {
      logger.error( "UserName not available" );
    }
    return false;
  }

  /**
   * Overridden from
   * 
   * @see com.objectpartners.cms.service.AuthorizationService#setTermsAndConditionsAcceptance(boolean)
   * @param acceptedTerms
   */
  public void setTermsAndConditionsAcceptance( boolean acceptedTerms )
  {
    Long userId = UserUtil.getUserId();
    if ( userId != null )
    {
      try
      {
        cmsAuthenticationDao.updateUserTermsAndConditions( acceptedTerms ? 1 : 0, userId );
      }
      catch( Exception ex )
      {
        logger.error( "Error updating cms terms acceptance in user", ex );
      }
      if ( acceptedTerms )
      {
        cache.put( UserUtil.getUsername().toLowerCase(), Boolean.TRUE );
      }
      else
      {
        cache.remove( UserUtil.getUsername().toLowerCase() );
      }
    }
    else
    {
      logger.error( "UserId not available" );
    }

  }

  public void setCmsAuthenticationDao( CmsAuthenticationDao cmsAuthenticationDao )
  {
    this.cmsAuthenticationDao = cmsAuthenticationDao;
  }

}
