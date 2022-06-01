
package com.biperf.core.utils.jms;

import java.io.IOException;
import java.util.Map;

import javax.jms.JMSException;

import org.apache.commons.collections4.map.HashedMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.value.hc.HoneycombInitializationData;
import com.biw.hc.core.service.HCServices;
import com.fasterxml.jackson.annotation.JsonIgnore;

@SuppressWarnings( "serial" )
public class HoneycombInitializationMessage implements JsonJmsMessage
{

  private static final Log logger = LogFactory.getLog( HoneycombInitializationMessage.class );

  private String token;
  private String clientCode;
  private String salt;
  private String farm;

  @Override
  public void processMessage() throws JMSException
  {
    if ( logger.isDebugEnabled() )
    {
      logger.debug( "Processing honeycomb initialization message. salt=" + getSalt() + ", token=" + getToken() );
    }

    // Check token to see if current instance is intended recipient
    String storedToken = HoneycombInitializationData.getInstance().getToken();
    if ( token != null && token.equals( storedToken ) )
    {
      // Save the information to a secure file
      Map<String, Object> properties = new HashedMap<>();
      properties.put( HCServices.PROPERTY_KEY_CLIENT_CODE, clientCode );
      properties.put( HCServices.PROPERTY_KEY_SALT, salt );
      properties.put( HCServices.PROPERTY_KEY_FARM, farm );
      try
      {
        getHCServices().addSecureProperties( getSystemVariableService().getContextName(), properties );
      }
      catch( IOException e )
      {
        logger.error( "Unable to write to honeycomb properties file", e );
      }
    }
  }

  public String getToken()
  {
    return token;
  }

  public void setToken( String token )
  {
    this.token = token;
  }

  public String getClientCode()
  {
    return clientCode;
  }

  public void setClientCode( String clientCode )
  {
    this.clientCode = clientCode;
  }
  
  public String getSalt()
  {
    return salt;
  }

  public void setSalt( String salt )
  {
    this.salt = salt;
  }

  public String getFarm()
  {
    return farm;
  }

  public void setFarm( String farm )
  {
    this.farm = farm;
  }

  @JsonIgnore
  public HCServices getHCServices()
  {
    return (HCServices)BeanLocator.getBean( HCServices.BEAN_NAME );
  }

  @JsonIgnore
  public SystemVariableService getSystemVariableService()
  {
    return (SystemVariableService)BeanLocator.getBean( SystemVariableService.BEAN_NAME );
  }

}
