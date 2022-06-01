
package com.biperf.core.service.cmx.impl;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

import com.biperf.core.exception.DataException;
import com.biperf.core.service.cmx.CMXRepository;
import com.biperf.core.utils.MeshServicesUtil;
import com.biperf.core.value.cmx.v1.cmx.CMXTranslateResponse;

@Service( "cmxRepositoryImpl" )
public class CMXRepositoryImpl implements CMXRepository
{
  private static final Log log = LogFactory.getLog( CMXRepositoryImpl.class );

  @Override
  public <T> Map<String, Map<String, String>> getTranslation( T cmxTranslateRequest ) throws DataException
  {

    ResponseEntity<CMXTranslateResponse> cmxTranslateResponse;

    try
    {

      HttpEntity<T> httpEntity = new HttpEntity<T>( cmxTranslateRequest, (HttpHeaders)MeshServicesUtil.getAuthorizationHeadersWithCompanyIDAndJWTToken() );
      String url = MeshServicesUtil.getNackleMeshServicesBaseEndPoint() + "/cmx/bundle/service-awards";
      url = url.contains( "v1" ) ? url.replace( "v1", "v2" ) : url;
      cmxTranslateResponse = MeshServicesUtil.getRestWebClient().exchange( url, HttpMethod.POST, httpEntity, CMXTranslateResponse.class );
      CMXTranslateResponse response = cmxTranslateResponse.getBody();

      if ( response != null && response.getBundles() != null )
      {
        return response.getBundles();
      }
      else
      {
        if ( cmxTranslateRequest != null )
        {
          log.error( "CMX Response is null. Request keys are : " + cmxTranslateRequest.toString() );
        }
        return null;
      }

    }
    catch( RestClientException ex )
    {
      log.error( "RestClientException while getting translation content from CMX : " + ex.getMessage(), ex );
      throw new DataException( ex.getMessage() );
    }
    catch( Exception exception )
    {
      log.error( "Exception while getting translation content from CMX  : " + exception.getMessage(), exception );
      throw new DataException( exception.getMessage() );
    }
  }

}
