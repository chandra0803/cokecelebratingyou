
package com.biperf.core.resttemplateclientfactory;

import static java.util.Objects.isNull;
import static org.apache.commons.lang3.StringUtils.isEmpty;

import java.util.Collections;
import java.util.Objects;

import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.biperf.core.domain.system.PropertySetItem;
import com.biperf.core.exception.BeaconRuntimeException;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.Environment;

/**
 * RestTemplateClientFactory.
 * <p>
 * <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>Saravanan Sivanandam</td>
 * <td>Nov 20, 2017</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */

@Component
public class RestTemplateClientFactory
{
  private RestTemplate restTemplate = null;

  public RestTemplate getRestWebClient()
  {
    if ( Objects.isNull( restTemplate ) )
    {
      return initClientObject();
    }

    return restTemplate;
  }

  private RestTemplate initClientObject()
  {
    SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();

    factory.setProxy( Environment.buildProxy() );

    // Setting Read timeout to avoid blocking thread
    PropertySetItem readTimeOutItem = getSystemVariableService().getPropertyByName( SystemVariableService.AUTOCOMPLETE_ES_READTIMEOUT );
    if ( isNull( readTimeOutItem ) || isEmpty( readTimeOutItem.getIntVal() + "" ) )
    {
      throw new BeaconRuntimeException( "System property not configured for :" + SystemVariableService.AUTOCOMPLETE_ES_READTIMEOUT );
    }
    else
    {
      if ( readTimeOutItem.getIntVal() > 0 )
      {
        factory.setReadTimeout( readTimeOutItem.getIntVal() );
      }
    }

    ClientHttpRequestFactory clientHttpRequestFactory = new BufferingClientHttpRequestFactory( factory );

    restTemplate = new RestTemplate();
    restTemplate.setRequestFactory( clientHttpRequestFactory );
    restTemplate.setInterceptors( Collections.singletonList( new RestReqAndResLoggingInterceptor() ) );

    return restTemplate;
  }

  private SystemVariableService getSystemVariableService()
  {
    return (SystemVariableService)BeanLocator.getBean( SystemVariableService.BEAN_NAME );
  }

}
