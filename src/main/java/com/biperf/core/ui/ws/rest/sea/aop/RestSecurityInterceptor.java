/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/ws/rest/sea/aop/Attic/RestSecurityInterceptor.java,v $
 */

package com.biperf.core.ui.ws.rest.sea.aop;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.security.ws.rest.SecureToken;
import com.biperf.core.ui.ws.rest.sea.dto.BaseRestObject;
import com.biperf.core.utils.RestSecurityManager;

public class RestSecurityInterceptor implements MethodInterceptor
{

  private final static Log log = LogFactory.getLog( RestSecurityInterceptor.class );

  /**
   * Overridden from
   * 
   * @see org.aopalliance.intercept.MethodInterceptor#invoke(org.aopalliance.intercept.MethodInvocation)
   * @param methodInvocation
   * @return Object
   * @throws Throwable
   */
  public Object invoke( MethodInvocation methodInvocation ) throws Throwable
  {
    log.debug( ">>> invoke" );
    Object[] arguments = methodInvocation.getArguments();
    boolean valid = false;
    Object object = null;
    SecureToken argument = null;

    if ( arguments != null )
    {
      for ( int index = 0; index < arguments.length; index++ )
      {
        if ( arguments[index] instanceof SecureToken )
        {
          // for now, only one parameter is assumed to bring in secure token and not all parameters
          // are validated.
          argument = (SecureToken)arguments[index];
          break;
        }
      }
    }

    if ( isSecurityTokenAvailable( argument ) )
    {
      valid = RestSecurityManager.validate( argument );
      if ( valid )
      {
        object = methodInvocation.proceed();
      }
      else
      {
        return buildSecurityResponse( methodInvocation, "Security validation failed for method : " + methodInvocation.getMethod().getName() );
      }
    }
    else
    {
      log.error( "Request didnt contain security token parameter" );
      return buildSecurityResponse( methodInvocation, "Request didnt contain security token parameter" );
    }

    log.debug( "<<< invoke" );
    return object;
  }

  private boolean isSecurityTokenAvailable( SecureToken argument )
  {
    return argument != null;
  }

  @SuppressWarnings( { "rawtypes", "unchecked" } )
  private Object buildSecurityResponse( MethodInvocation methodInvocation, String message ) // TODO: this is specific to the method return type - will need to alter when other method 
  {
    Class clazz = methodInvocation.getMethod().getReturnType() ; 
    if( null!=clazz && clazz.isAssignableFrom( BaseRestObject.class ) )
    {
      try
      {
        Object response = clazz.newInstance() ;
        ( (BaseRestObject) response ).setCode( 99 ); 
        ( (BaseRestObject) response ).setErrorMessage( message );
        return response ;
      }
      catch( Exception e )
      {
        log.error( e.getMessage(), e );
      }
    }
    return null ;
  }
}
