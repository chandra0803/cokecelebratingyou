
package com.biperf.core.security.ws;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;

import org.apache.ws.security.WSPasswordCallback;

import com.biperf.core.domain.system.PropertySetItem;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.Environment;

/**
 *
 */
public class KeyPasswordCallback implements CallbackHandler
{

  private Map<String, String> passwords = new HashMap<String, String>();

  public KeyPasswordCallback()
  {
    PropertySetItem encryptedPropForEnv = getSystemVariableService().getPropertyByNameAndEnvironment( SystemVariableService.PARTNERSRVC_CLIENT_KEY_PASSWORD );
    PropertySetItem encryptedPropForNonProd = getSystemVariableService().getPropertyByName( getNonProdEnvironmentVarName( SystemVariableService.PARTNERSRVC_CLIENT_KEY_PASSWORD ) );

    if ( Objects.nonNull( encryptedPropForEnv ) )
    {
      passwords
          .put( SystemVariableService.APP_PREFIX + "-" + Environment.getEnvironment(),
                getSystemVariableService().getAESDecryptedValue( encryptedPropForEnv.getStringVal(), SystemVariableService.PARTNERSRVC_AES_KEY, SystemVariableService.PARTNERSRVC_AES_INIT_VECTOR ) );
    }
    else if ( !Environment.ENV_PROD.equals( Environment.getEnvironment() ) && Objects.nonNull( encryptedPropForNonProd ) )
    {
      passwords.put( SystemVariableService.APP_PREFIX + "-" + SystemVariableService.PARTNERSRVC_NON_PROD_ENV,
                     getSystemVariableService().getAESDecryptedValue( encryptedPropForNonProd.getStringVal(),
                                                                      SystemVariableService.PARTNERSRVC_AES_KEY,
                                                                      SystemVariableService.PARTNERSRVC_AES_INIT_VECTOR ) );

    }
  }

  /**
   * Here, we attempt to get the password from the private alias/passwords map.
   */
  public void handle( Callback[] callbacks ) throws IOException, UnsupportedCallbackException
  {
    for ( int i = 0; i < callbacks.length; i++ )
    {
      WSPasswordCallback pc = (WSPasswordCallback)callbacks[i];
      String pass = passwords.get( pc.getIdentifier() );
      if ( Objects.nonNull( pass ) )
      {
        pc.setPassword( pass );
        return;
      }
    }
  }

  public String getNonProdEnvironmentVarName( String prefix )
  {
    return prefix + "." + SystemVariableService.PARTNERSRVC_NON_PROD_ENV;
  }

  /**
   * Add an alias/password pair to the callback mechanism.
   */
  public void setAliasPassword( String alias, String password )
  {
    passwords.put( alias, password );
  }

  private SystemVariableService getSystemVariableService()
  {
    return (SystemVariableService)BeanLocator.getBean( SystemVariableService.BEAN_NAME );
  }
}
