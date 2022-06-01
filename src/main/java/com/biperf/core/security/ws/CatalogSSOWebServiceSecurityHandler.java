
package com.biperf.core.security.ws;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.Bus;
import org.apache.cxf.BusFactory;
import org.apache.cxf.bus.spring.SpringBusFactory;
import org.apache.cxf.common.util.StringUtils;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.ws.security.wss4j.WSS4JInInterceptor;
import org.apache.cxf.ws.security.wss4j.WSS4JOutInterceptor;
import org.apache.ws.security.WSConstants;
import org.apache.ws.security.components.crypto.Merlin;
import org.apache.ws.security.handler.WSHandlerConstants;

import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.Environment;
import com.biperf.partnerservices.ws.catalog.v2.CatalogSSOWebService;

/**
 * Handles WS-Security implementation for the bank web service client
 *
 */
public class CatalogSSOWebServiceSecurityHandler
{
  private static final Log logger = LogFactory.getLog( CatalogSSOWebServiceSecurityHandler.class );
  private static final String KEYSTORE_PASSWORD_CALLBACK = "com.biperf.core.security.ws.KeyPasswordCallback";
  private static final String SERVER_APPLICATION = "partnerservices";
  private static final String MERLIN_KEYSTORE_PASSWORD_KEY = "org.apache.ws.security.crypto.merlin.keystore.password";

  public static void applySecurity( CatalogSSOWebService port, String app, String env ) throws Exception
  {
    SpringBusFactory bf = new SpringBusFactory();
    URL busFile = Thread.currentThread().getContextClassLoader().getResource( "wssecurity/wssec.xml" );
    Bus bus = bf.createBus( busFile.toString() );
    BusFactory.setDefaultBus( bus );

    // driving the set up based on system variable set up
    // prod should be there always for prod environment. qa, preprod, dev is optional
    // qa, preprod, dev can be replaced by generic 'nonprd' environment
    Properties serverProps = new Properties();
    if ( Objects.nonNull( getSystemVariableService().getPropertyByNameAndEnvironment( SystemVariableService.PARTNERSRVC_SERVER_KEYSTORE_PASSWORD ) ) )
    {
      serverProps = getProperties( SERVER_APPLICATION, env );
      if ( Objects.nonNull( serverProps ) )
      {
        serverProps.put( MERLIN_KEYSTORE_PASSWORD_KEY,
                         getSystemVariableService()
                             .getAESDecryptedValue( getSystemVariableService().getPropertyByNameAndEnvironment( SystemVariableService.PARTNERSRVC_SERVER_KEYSTORE_PASSWORD ).getStringVal(),
                                                    SystemVariableService.PARTNERSRVC_AES_KEY,
                                                    SystemVariableService.PARTNERSRVC_AES_INIT_VECTOR ) );
      }
    }
    else if ( !Environment.ENV_PROD.equals( Environment.getEnvironment() )
        && Objects.nonNull( getSystemVariableService().getPropertyByName( getNonProdEnvironmentVarName( SystemVariableService.PARTNERSRVC_SERVER_KEYSTORE_PASSWORD ) ) ) )
    {
      serverProps = getProperties( SERVER_APPLICATION, SystemVariableService.PARTNERSRVC_NON_PROD_ENV );
      if ( Objects.nonNull( serverProps ) )
      {
        serverProps.put( MERLIN_KEYSTORE_PASSWORD_KEY,
                         getSystemVariableService().getAESDecryptedValue( getSystemVariableService()
                             .getPropertyByName( getNonProdEnvironmentVarName( SystemVariableService.PARTNERSRVC_SERVER_KEYSTORE_PASSWORD ) ).getStringVal(),
                                                                          SystemVariableService.PARTNERSRVC_AES_KEY,
                                                                          SystemVariableService.PARTNERSRVC_AES_INIT_VECTOR ) );
      }

    }
    if ( Objects.isNull( serverProps ) )
    {
      return;
    }
    String serverAlias = getKeyAlias( serverProps );

    Properties clientProps = new Properties();
    if ( Objects.nonNull( getSystemVariableService().getPropertyByNameAndEnvironment( SystemVariableService.PARTNERSRVC_CLIENT_KEYSTORE_PASSWORD ) ) )
    {
      clientProps = getProperties( app, env );
      if ( Objects.nonNull( clientProps ) )
      {
        clientProps.put( MERLIN_KEYSTORE_PASSWORD_KEY,
                         getSystemVariableService()
                             .getAESDecryptedValue( getSystemVariableService().getPropertyByNameAndEnvironment( SystemVariableService.PARTNERSRVC_CLIENT_KEYSTORE_PASSWORD ).getStringVal(),
                                                    SystemVariableService.PARTNERSRVC_AES_KEY,
                                                    SystemVariableService.PARTNERSRVC_AES_INIT_VECTOR ) );
      }
    }
    else if ( !Environment.ENV_PROD.equals( Environment.getEnvironment() )
        && Objects.nonNull( getSystemVariableService().getPropertyByName( getNonProdEnvironmentVarName( SystemVariableService.PARTNERSRVC_CLIENT_KEYSTORE_PASSWORD ) ) ) )
    {
      clientProps = getProperties( app, SystemVariableService.PARTNERSRVC_NON_PROD_ENV );
      if ( Objects.nonNull( clientProps ) )
      {
        clientProps.put( MERLIN_KEYSTORE_PASSWORD_KEY,
                         getSystemVariableService().getAESDecryptedValue( getSystemVariableService()
                             .getPropertyByName( getNonProdEnvironmentVarName( SystemVariableService.PARTNERSRVC_CLIENT_KEYSTORE_PASSWORD ) ).getStringVal(),
                                                                          SystemVariableService.PARTNERSRVC_AES_KEY,
                                                                          SystemVariableService.PARTNERSRVC_AES_INIT_VECTOR ) );
      }

    }
    if ( Objects.isNull( clientProps ) )
    {
      return;
    }
    String clientAlias = getKeyAlias( clientProps );

    if ( Objects.isNull( serverAlias ) || Objects.isNull( clientAlias ) )
    {
      return;
    }

    Map<String, Object> outProps = new HashMap<String, Object>();
    outProps.put( WSHandlerConstants.ACTION, WSHandlerConstants.TIMESTAMP + " " + WSHandlerConstants.SIGNATURE + " " + WSHandlerConstants.ENCRYPT );
    outProps.put( WSHandlerConstants.USER, clientAlias );
    outProps.put( WSHandlerConstants.PW_CALLBACK_CLASS, KEYSTORE_PASSWORD_CALLBACK );
    outProps.put( WSHandlerConstants.SIGNATURE_USER, clientAlias );
    outProps.put( "clientProps", clientProps );
    outProps.put( WSHandlerConstants.SIG_PROP_REF_ID, "clientProps" );
    outProps.put( WSHandlerConstants.SIGNATURE_PARTS, "{Element}{" + WSConstants.WSU_NS + "}Timestamp;{Element}{" + WSConstants.URI_SOAP11_ENV + "}Body" );
    outProps.put( WSHandlerConstants.SIG_ALGO, WSConstants.RSA );
    outProps.put( WSHandlerConstants.ENCRYPTION_USER, serverAlias );
    outProps.put( "serverProps", serverProps );
    outProps.put( WSHandlerConstants.ENC_PROP_REF_ID, "serverProps" );
    outProps.put( WSHandlerConstants.ENCRYPTION_PARTS, "{Element}{" + WSConstants.SIG_NS + "}Signature;{Content}{" + WSConstants.URI_SOAP11_ENV + "}Body" );
    outProps.put( WSHandlerConstants.ENC_SYM_ALGO, WSConstants.AES_256 );

    Map<String, Object> inProps = new HashMap<String, Object>();
    inProps.put( WSHandlerConstants.ACTION, WSHandlerConstants.TIMESTAMP + " " + WSHandlerConstants.SIGNATURE + " " + WSHandlerConstants.ENCRYPT );
    inProps.put( WSHandlerConstants.PW_CALLBACK_CLASS, KEYSTORE_PASSWORD_CALLBACK );
    inProps.put( "serverProps", serverProps );
    inProps.put( WSHandlerConstants.SIG_PROP_REF_ID, "serverProps" );
    inProps.put( "clientProps", clientProps );
    inProps.put( WSHandlerConstants.DEC_PROP_REF_ID, "clientProps" );

    Client client = ClientProxy.getClient( port );
    HTTPConduit http = (HTTPConduit)client.getConduit();
    http.getClient().setProxyServer( Environment.getProxy() );
    http.getClient().setProxyServerPort( Environment.getProxyPort() );
    client.getInInterceptors().add( new WSS4JInInterceptor( inProps ) );
    client.getOutInterceptors().add( new WSS4JOutInterceptor( outProps ) );
  }

  private static Properties getProperties( String app, String env )
  {

    if ( StringUtils.isEmpty( app ) || StringUtils.isEmpty( env ) )
    {
      throw new RuntimeException( "Cannot get WS-Security properties file because app or env is empty" );
    }

    StringBuilder sb = new StringBuilder();
    sb.append( "wssecurity/props/" );
    sb.append( app );
    sb.append( "-" );
    sb.append( env );
    sb.append( ".properties" );

    Properties props = new Properties();
    InputStream propsStream = Thread.currentThread().getContextClassLoader().getResourceAsStream( sb.toString() );
    try
    {
      props.load( propsStream );
    }
    catch( Exception e )
    {
      throw new RuntimeException( "Failed to get private key alias: " + sb.toString(), e );
    }
    finally
    {
      if ( Objects.nonNull( propsStream ) )
      {
        try
        {
          propsStream.close();
        }
        catch( IOException e )
        {
          logger.error( "Failed to close private key alias: " + e.toString() );
        }
      }
    }
    return props;
  }

  private static String getKeyAlias( Properties props )
  {
    return (String)props.get( Merlin.KEYSTORE_ALIAS );
  }

  public static String getNonProdEnvironmentVarName( String prefix )
  {
    return prefix + "." + SystemVariableService.PARTNERSRVC_NON_PROD_ENV;
  }

  private static SystemVariableService getSystemVariableService()
  {
    return (SystemVariableService)BeanLocator.getBean( SystemVariableService.BEAN_NAME );
  }

}
