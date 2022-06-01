/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/utils/Attic/RestSecurityManager.java,v $
 */

package com.biperf.core.utils;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.domain.system.PropertySetItem;
import com.biperf.core.security.ws.rest.SecureToken;
import com.biperf.core.service.SAO;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.utils.crypto.SHA256Hash;

public class RestSecurityManager
{

  private final static Log log = LogFactory.getLog( RestSecurityManager.class );

  public static boolean validate( SecureToken token )
  {
    boolean isValidRequest = validateSecurityToken( token );
    return isValidRequest;
  }

  private static boolean validateSecurityToken( SecureToken token )
  {
    // no token
    if ( StringUtils.isEmpty( token.getSecurityToken() ) )
    {
      log.error( "Request didnt contain valid security token" );
      return false;
    }

    // no identifier passed on
    if ( StringUtils.isEmpty( token.getHashableAttribute() ) )
    {
      log.error( "WS call didnt have identifier being passed on" );
      return false;
    }

    PropertySetItem salt = getSystemVariableService().getPropertyByName( SystemVariableService.SEA_SECURITY_SALT ) ;

    // no salt
    if ( salt==null || StringUtils.isEmpty( salt.getStringVal() ) )
    {
      log.error( "Client doesnt have salt configured" );
      return false;
    }

    // token validation failed
    /*START MD5 to SHA256 conversion code: TO BE UPDATED LATER*/
    //if ( !token.getSecurityToken().equals( new MD5Hash().encryptWithSalt( token.getHashableAttribute(), salt.getStringVal() ) ) )
    if ( !token.getSecurityToken().equals( new SHA256Hash().encryptWithSalt( token.getHashableAttribute(), salt.getStringVal() ) ) )
    /*END MD5 to SHA256 conversion code: TO BE UPDATED LATER*/
    {
      log.error( "Security token validation failed. Identifier is : " + token.getHashableAttribute() + ". Token value is " + token.getSecurityToken() );
      return false;
    }

    return true;
  }

  private static SystemVariableService getSystemVariableService()
  {
    return (SystemVariableService)getService( SystemVariableService.BEAN_NAME );
  }

  private static SAO getService( String beanName )
  {
    return (SAO)BeanLocator.getBean( beanName );
  }

}