
package com.biperf.core.indexing.impl;

import static com.biperf.core.utils.Environment.ENV_DEV;
import static com.biperf.core.utils.Environment.getEnvironment;
import static java.util.Objects.isNull;
import static org.apache.commons.lang3.StringUtils.isEmpty;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Objects;

import javax.annotation.PreDestroy;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpHost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.biperf.core.domain.system.PropertySetItem;
import com.biperf.core.exception.BeaconRuntimeException;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.utils.AwsUtils;
import com.biperf.core.utils.Environment;
import com.biperf.core.utils.UrlReader;
import com.google.common.base.Supplier;

import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.config.HttpClientConfig;
import io.searchbox.client.config.HttpClientConfig.Builder;
import vc.inreach.aws.request.AWSSigner;
import vc.inreach.aws.request.AWSSigningRequestInterceptor;

@Component
public class ESClientFactory
{

  private static final Log log = LogFactory.getLog( ESClientFactory.class );

  private static final String SERVICE = "es";

  @Autowired
  private SystemVariableService systemVariableService;

  private static JestClient jestClient;

  public JestClient getInstance()
  {
    if ( jestClient == null )
    {
      init();
    }

    return jestClient;
  }

  public void reset()
  {
    init();
  }

  private void init()
  {
    String propertyMissing = "System property not configured for : ";

    JestClientFactory factory = null;

    try
    {
      if ( Objects.nonNull( jestClient ) )
      {
        jestClient.close();
      }
    }
    catch( IOException ioException )
    {
      log.error( "Error while closing jest client " + ioException );
    }

    PropertySetItem urlItem = systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.AUTOCOMPLETE_ES_URL );

    if ( isNull( urlItem ) || isEmpty( urlItem.getStringVal() ) )
    {
      throw new BeaconRuntimeException( propertyMissing + SystemVariableService.AUTOCOMPLETE_ES_URL + "." + Environment.getEnvironment() );
    }

    PropertySetItem readTimeOutItem = systemVariableService.getPropertyByName( SystemVariableService.AUTOCOMPLETE_ES_READTIMEOUT );
    if ( isNull( readTimeOutItem ) || isEmpty( readTimeOutItem.getIntVal() + "" ) )
    {
      throw new BeaconRuntimeException( propertyMissing + SystemVariableService.AUTOCOMPLETE_ES_READTIMEOUT );
    }

    // We can provide security credentials even if the service doesn't require them
    PropertySetItem esUsername = systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.ELASTICSEARCH_CREDENTIALS_USERNAME );
    if ( isNull( esUsername ) || isEmpty( esUsername.getStringVal() ) )
    {
      throw new BeaconRuntimeException( propertyMissing + SystemVariableService.ELASTICSEARCH_CREDENTIALS_USERNAME );
    }

    PropertySetItem esPassword = systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.ELASTICSEARCH_CREDENTIALS_PASSWORD );
    if ( isNull( esPassword ) || isEmpty( esPassword.getStringVal() ) )
    {
      throw new BeaconRuntimeException( propertyMissing + SystemVariableService.ELASTICSEARCH_CREDENTIALS_PASSWORD );
    }

    PropertySetItem esRegion = systemVariableService.getPropertyByName( SystemVariableService.AUTOCOMPLETE_ES_AWS_REGION );
    if ( isNull( esRegion ) || isEmpty( esRegion.getStringVal() ) )
    {
      throw new BeaconRuntimeException( propertyMissing + SystemVariableService.AUTOCOMPLETE_ES_AWS_REGION );
    }

    Builder builder = new HttpClientConfig.Builder( urlItem.getStringVal() ).multiThreaded( true );

    HttpHost proxy = getProxyHost( urlItem.getStringVal() );
    if ( proxy != null )
    {
      builder.proxy( proxy );
    }

    if ( readTimeOutItem.getIntVal() > 0 )
    {
      builder.readTimeout( readTimeOutItem.getIntVal() );
    }

    if ( AwsUtils.isAws() )
    {
      factory = new JestClientFactory();
    }
    else
    {
      Supplier<LocalDateTime> clock = () -> LocalDateTime.now( ZoneOffset.UTC );
      AWSSigner awsSigner = new AWSSigner( new AWSStaticCredentialsProvider( new BasicAWSCredentials( esUsername.getStringVal(), esPassword.getStringVal() ) ),
                                           esRegion.getStringVal(),
                                           SERVICE,
                                           clock );

      AWSSigningRequestInterceptor requestInterceptor = new AWSSigningRequestInterceptor( awsSigner );
      factory = new JestClientFactory()
      {
        @Override
        protected HttpClientBuilder configureHttpClient( HttpClientBuilder builder )
        {
          builder.addInterceptorLast( requestInterceptor );
          return builder;
        }

        @Override
        protected HttpAsyncClientBuilder configureHttpClient( HttpAsyncClientBuilder builder )
        {
          builder.addInterceptorLast( requestInterceptor );
          return builder;
        }
      };
    }

    factory.setHttpClientConfig( builder.build() );
    jestClient = factory.getObject();
  }

  private HttpHost getProxyHost( String endPoint )
  {
    if ( ( ENV_DEV.equalsIgnoreCase( getEnvironment() ) && !new UrlReader().useProxy( endPoint ) ) || AwsUtils.isAws() )
    {
      return null;
    }
    return new HttpHost( Environment.getProxy(), Environment.getProxyPort() );
  }

  @PreDestroy
  public void destory()
  {
    try
    {
      if ( Objects.nonNull( jestClient ) )
      {
        jestClient.close();
      }
    }
    catch( IOException ioException )
    {
      log.error( "Error while closing jest client " + ioException );
    }
  }
}
