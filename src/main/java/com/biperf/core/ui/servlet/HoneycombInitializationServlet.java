
package com.biperf.core.ui.servlet;

import java.io.FileNotFoundException;
import java.util.Base64;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.GuidUtils;
import com.biperf.core.value.hc.HoneycombInitializationData;
import com.biw.hc.core.service.HCServices;
import com.objectpartners.cms.util.ContentReaderManager;

@SuppressWarnings( "serial" )
public class HoneycombInitializationServlet extends CMSAwareBaseServlet
{

  private static Log logger = LogFactory.getLog( ContextListener.class );
  private static final int REQUEST_TIMEOUT_MILLIS = 5000;

  @Override
  public void init()
  {
    try
    {
      super.init();
      
      // Only try if registration is done. Otherwise there's nothing to grab.
      if ( isHoneycombRegistered() )
      {
        initHoneycombSalt();
      }
    }
    catch( Exception e )
    {
      logger.error( e.getMessage(), e );
    }
    finally
    {
      ContentReaderManager.removeContentReader();
    }
  }

  private void initHoneycombSalt()
  {
    // Check to see if we already have the salt password
    boolean haveProperties = false;
    try
    {
      Map<String, Map<String, Object>> hcProperties = getHCServices().readSecureProperties();
      haveProperties = hcProperties != null && hasAllRequiredProperties( hcProperties.get( getSystemVariableService().getContextName() ) );
    }
    catch( FileNotFoundException e )
    {
      // This is fine - just means we don't have it, yet.
    }

    // Send request if we do not yet have the salt password
    if ( !haveProperties )
    {
      // A GUID seems well and acceptable and lazy for a token
      String token = Base64.getUrlEncoder().encodeToString( GuidUtils.generateGuid().getBytes() );
      HoneycombInitializationData.getInstance().setToken( token );

      logger.warn( "Sending honeycomb salt initialization request" );
      sendHoneycombRequest( token );
    }
  }
  
  private boolean hasAllRequiredProperties( Map<String, Object> properties )
  {
    return properties != null 
        && properties.containsKey( HCServices.PROPERTY_KEY_CLIENT_CODE )
        && properties.containsKey( HCServices.PROPERTY_KEY_FARM )
        && properties.containsKey( HCServices.PROPERTY_KEY_SALT );
  }

  /** Send request to HC Config asking for the secure information to be sent back to us */
  private void sendHoneycombRequest( String token )
  {
    String hcconfigUrl = getSystemVariableService().getPropertyByNameAndEnvironment( SystemVariableService.HC_CONFIG_URL ).getStringVal();
    String honeycombClientCode = getSystemVariableService().getPropertyByName( SystemVariableService.HC_CLIENT_CODE ).getStringVal();
    String requestUrl = hcconfigUrl + "services/v1.0/public/asyncClient/" + honeycombClientCode + "/" + token;

    HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
    requestFactory.setConnectTimeout( REQUEST_TIMEOUT_MILLIS );
    requestFactory.setReadTimeout( REQUEST_TIMEOUT_MILLIS );
    RestTemplate restTemplate = new RestTemplate( requestFactory );
    try
    {
      ResponseEntity<Void> response = restTemplate.getForEntity( requestUrl, Void.class );

      if ( !HttpStatus.OK.equals( response.getStatusCode() ) )
      {
        logger.error( "Honeycomb Salt Initialization - Got " + response.getStatusCode() + " from HC Config call" );
      }
    }
    catch( ResourceAccessException e )
    {
      logger.error( "Exception getting properties from Honeycomb Config. Make sure HC Config and tomcat are up and running before deploying G", e );
      throw e;
    }
  }
  
  private boolean isHoneycombRegistered()
  {
    String clientCodeSysVar = getSystemVariableService().getPropertyByName( SystemVariableService.HC_CLIENT_CODE ).getStringVal();
    return StringUtils.isNotBlank( clientCodeSysVar ) && !"changeme".equalsIgnoreCase( clientCodeSysVar );
  }

  private HCServices getHCServices()
  {
    return (HCServices)BeanLocator.getBean( HCServices.BEAN_NAME );
  }

  private SystemVariableService getSystemVariableService()
  {
    return (SystemVariableService)BeanLocator.getBean( SystemVariableService.BEAN_NAME );
  }

}
