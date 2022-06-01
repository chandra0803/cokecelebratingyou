/*
 * (c) 2006 BI, Inc.  All rights reserved.
 * $Source$
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
import com.biperf.core.domain.user.User;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.utils.ServiceLocator;
import com.objectpartners.cms.service.AuthorizationService;
import com.objectpartners.cms.util.CmsUtil;

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
public class CMAuthorizationServiceImpl implements AuthorizationService, InitializingBean
{
  private static final Log logger = LogFactory.getLog( CMAuthorizationServiceImpl.class );
  private UserService userService;
  private Cache cache;

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
      User user = getUserService().getUserByUserName( userName );
      if ( user != null )
      {
        if ( user.getAcceptedCmsTerms() != null )
        {
          return user.getAcceptedCmsTerms().booleanValue();
        }
      }
      else
      {
        logger.error( "User not found for user : " + userName );
      }
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
    String userName = CmsUtil.getUserName();
    if ( userName != null )
    {
      User user = getUserService().getUserByUserName( userName );
      if ( user != null )
      {
        user.setAcceptedCmsTerms( new Boolean( acceptedTerms ) );
        try
        {
          getUserService().saveUser( user );
        }
        catch( Exception ex )
        {
          logger.error( "Error updating cms terms acceptance in user", ex );
        }
        if ( acceptedTerms )
        {
          cache.put( userName.toLowerCase(), Boolean.TRUE );
        }
        else
        {
          cache.remove( userName.toLowerCase() );
        }
      }
      else
      {
        logger.error( "User not found for user : " + userName );
      }
    }
    else
    {
      logger.error( "UserId not available" );
    }

  }

  protected UserService getUserService()
  {
    if ( userService == null )
    {
      userService = (UserService)ServiceLocator.getService( UserService.BEAN_NAME );
    }
    return userService;
  }

}
