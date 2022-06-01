/* Copyright 2008, 2009, StrongMail Systems, Inc.  All rights reserved. */

package com.biperf.core.service.strongmail;

import java.io.IOException;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;

import org.apache.ws.security.WSPasswordCallback;

import com.biperf.core.service.SAO;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.utils.BeanLocator;

/**
 * This file set call back password.
 */
public class ClientPasswordCallbackHandler implements CallbackHandler
{

  public ClientPasswordCallbackHandler()
  {
  }

  public void handle( Callback[] callbacks ) throws IOException, UnsupportedCallbackException
  {
    for ( int i = 0; i < callbacks.length; i++ )
    {
      WSPasswordCallback pc = (WSPasswordCallback)callbacks[i];
      String pass = getSystemVariableService().getPropertyByName( SystemVariableService.STRONGMAIL_PASSWORD ).getStringVal();
      if ( pass != null )
      {
        pc.setPassword( pass );
        return;
      }
    }
  }

  private SystemVariableService getSystemVariableService()
  {
    return (SystemVariableService)getService( SystemVariableService.BEAN_NAME );
  }

  protected static SAO getService( String beanName )
  {
    return (SAO)BeanLocator.getBean( beanName );
  }
}
