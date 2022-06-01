
package com.biperf.core.service.company.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.company.CompanySetupRepository;
import com.biperf.core.utils.MeshServicesUtil;
import com.biperf.core.value.companysetup.v1.company.CompanyView;

@Service( "CompanySetupRepositoryImpl" )
public class CompanySetupRepositoryImpl implements CompanySetupRepository
{
  private static final Log log = LogFactory.getLog( CompanySetupRepositoryImpl.class );

  @Override
  public CompanyView getCompany( String companyIdentifier ) throws Exception
  {
    Map<String, Object> param = new HashMap<>();
    param.put( "identifier", companyIdentifier );

    ResponseEntity<CompanyView> companyView = null;

    try
    {
      companyView = MeshServicesUtil.getRestWebClient().exchange( MeshServicesUtil.getNackleMeshServicesBaseEndPoint() + "/company-setup/company/{identifier}",
                                                                  HttpMethod.GET,
                                                                  new HttpEntity<String>( MeshServicesUtil.getAuthorizationHeadersForClientCredentials() ),
                                                                  CompanyView.class,
                                                                  param );
      if ( companyView != null )
      {
        log.info( companyView.toString() );
      }
    }
    catch( RestClientException ex )
    {
      log.error( "Exception while get company information : " + ex.getMessage() );
      throw new ServiceErrorException( ex.getMessage() );
    }
    catch( Exception exception )
    {
      log.error( "Exception while get company information : " + exception.getMessage() );
      throw new ServiceErrorException( exception.getMessage() );
    }

    if ( companyView != null && companyView.getBody() != null )
    {
      return (CompanyView)companyView.getBody();
    }
    else
    {
      return null;
    }
  }

}
