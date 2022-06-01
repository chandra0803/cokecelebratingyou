
package com.biperf.core.ui.shopping;

import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.ws.WebServiceException;

import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;

import com.biperf.core.exception.DataException;
import com.biperf.core.exception.ServiceNotAvailableException;
import com.biperf.core.security.ws.CatalogSSOWebServiceSecurityHandler;
import com.biperf.partnerservices.ws.catalog.v2.CatalogSSOWSRequest;
import com.biperf.partnerservices.ws.catalog.v2.CatalogSSOWSResponse;
import com.biperf.partnerservices.ws.catalog.v2.CatalogSSOWebService;

/**
 * This class attempts to hide the WebServices nature of the implementation.  This
 * class wraps all errors/exceptions and converts them to a java equivalent.
 * The construction of this object takes a String - which should point towards
 * the implementation's end point.  All subsequent actions have no WebServices
 * related artifacts.
 * 
 */
public class CatalogWebServiceClient
{
  private static final QName SERVICE_NAME = new QName( "http://v2.catalog.ws.partnerservices.biperf.com/", "CatalogSSOWebServiceImplService" );
  private String application;
  private String environment;
  private URL url = null;

  @SuppressWarnings( "unused" )
  private CatalogWebServiceClient()
  {
  }

  public CatalogWebServiceClient( String endpoint, String app, String env )
  {
    application = app;
    environment = env;
    // work-around for "Cannot create a secure XMLInputFactory" run-time exception in theCatalog
    // System.setProperty( "org.apache.cxf.stax.allowInsecureParser", "true" );
    try
    {
      url = new URL( endpoint );
    }
    catch( MalformedURLException e )
    {
      e.printStackTrace();
    }
  }

  public CatalogSSOWSResponse doSSO( String customerId, String countryCode, String catalogType, String bankAccountNumber, CatalogSSOWSRequest catalogRequest )
      throws ServiceNotAvailableException, DataException
  {
    try
    {
      CatalogSSOWSResponse response = getCatalogWebService().sso( customerId, countryCode, catalogType, bankAccountNumber, catalogRequest );

      return response;
    }
    catch( WebServiceException e )
    {
      throw new ServiceNotAvailableException( e.getMessage(), e );
    }
  }

  public CatalogSSOWSResponse doSSOBrowseOnly( String customerId, String countryCode, String catalogType ) throws ServiceNotAvailableException, DataException
  {
    try
    {
      CatalogSSOWSResponse response = getCatalogWebService().ssoBrowseOnly( customerId, countryCode, catalogType );
      return response;
    }
    catch( WebServiceException e )
    {
      throw new ServiceNotAvailableException( e.getMessage(), e );
    }
  }

  public CatalogSSOWebService getCatalogWebService() throws ServiceNotAvailableException
  {

    CatalogSSOWebService client = null;
    try
    {
      JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
      factory.setServiceClass( CatalogSSOWebService.class );
      factory.setAddress( url.toString() );
      factory.setServiceName( SERVICE_NAME );
      client = (CatalogSSOWebService)factory.create();
      // adds WS-Security
      CatalogSSOWebServiceSecurityHandler.applySecurity( client, application, environment );
    }
    catch( Exception e )
    {
      throw new ServiceNotAvailableException( e.getMessage(), e );
    }
    return client;
  }

  public String getApplication()
  {
    return application;
  }

  public void setApplication( String application )
  {
    this.application = application;
  }

  public String getEnvironment()
  {
    return environment;
  }

  public void setEnvironment( String environment )
  {
    this.environment = environment;
  }

}
