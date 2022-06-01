
package com.biperf.core.service.awardbanq;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.awardslinqDataRetriever.util.AwardslinqDataRetrieverException;
import com.biperf.awardslinqDataRetriever.util.EnvironmentProperties;
import com.biperf.core.exception.BeaconRuntimeException;
import com.biperf.core.security.ws.rest.ConnectionFactory;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.EncryptionUtils;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.client.urlconnection.URLConnectionClientHandler;
import com.sun.jersey.core.util.MultivaluedMapImpl;

public class DataRetrieverDelegate
{

  private static final Log logger = LogFactory.getLog( DataRetrieverDelegate.class );

  private static DataRetrieverDelegate instance = new DataRetrieverDelegate();

  protected DataRetrieverDelegate()
  {
    // suppress constructor to make it singleton
  }

  public static DataRetrieverDelegate getInstance()
  {
    return instance;
  }

  /**
   * @return SystemVariableService
   */
  private SystemVariableService getSystemVariableService()
  {
    return (SystemVariableService)BeanLocator.getBean( SystemVariableService.BEAN_NAME );
  }

  public static void shutdown()
  {
    instance = null;
  }

  public AwardBanqMerchResponseValueObject getMerchlinqLevelDataWebService( String programId ) throws AwardslinqDataRetrieverException
  {
    return getMerchlinqLevelDataWebService( programId, false );
  }

  /**
   * Gets the merchlinq level data.
   * 
   * @param programId the program id
   * @param includeProducts the include products
   * @return the merchlinq level data
   * @throws AwardslinqDataRetrieverException the awardslinq data retriever exception
   */
  public AwardBanqMerchResponseValueObject getMerchlinqLevelDataWebService( String programId, boolean includeProducts ) throws AwardslinqDataRetrieverException
  {
    return getMerchlinqLevelDataWebService( programId, includeProducts, false );
  }

  /**
   * Gets the merchlinq level data.
   * 
   * @param programId the program id
   * @param includeProducts the include products
   * @param includeProductIds the include product Ids
   * @return the merchlinq level data
   * @throws AwardslinqDataRetrieverException the awardslinq data retriever exception
   */
  public AwardBanqMerchResponseValueObject getMerchlinqLevelDataWebService( String programId, boolean includeProducts, boolean includeProductIds ) throws AwardslinqDataRetrieverException
  {
    return getMerchlinqLevelDataWebService( programId, includeProducts, includeProductIds, System.getProperty( EnvironmentProperties.ENVIRONMENT_ID ) );
  }

  /**
   * Gets the merchlinq level data.
   * 
   * @param programId the program id
   * @param environmentId the environment id
   * @return the merchlinq level data
   * @throws AwardslinqDataRetrieverException the awardslinq data retriever exception
   */
  public AwardBanqMerchResponseValueObject getMerchlinqLevelDataWebService( String programId, String environmentId ) throws AwardslinqDataRetrieverException
  {
    return getMerchlinqLevelDataWebService( programId, false, environmentId );
  }

  public AwardBanqMerchResponseValueObject getMerchlinqLevelDataWebService( String programId, boolean includeProducts, String environmentId ) throws AwardslinqDataRetrieverException
  {
    return getMerchlinqLevelDataWebService( programId, includeProducts, false, environmentId );
  }

  public AwardBanqMerchResponseValueObject getMerchlinqLevelDataWebService( String programId, boolean includeProducts, boolean includeProductIds, String environmentId )
      throws AwardslinqDataRetrieverException
  {
    AwardBanqMerchResponseValueObject procedureResponseVO = new AwardBanqMerchResponseValueObject();
    ClientResponse resp = null;

    try
    {
      // ClientConfig config = new DefaultClientConfig();
      URLConnectionClientHandler ch = new URLConnectionClientHandler( new ConnectionFactory() );
      Client client = new Client( ch );
      ObjectMapper mapper = buildObjectMapper();

      MultivaluedMap<String, String> params = new MultivaluedMapImpl();
      params.add( "includeProductIds", String.valueOf( includeProductIds ) );
      params.add( "includeProducts", String.valueOf( includeProducts ) );
      params.add( "programId", programId );

      WebResource awardBanqServiceResource = client.resource( OMRemoteDelegate.getInstance().getCatalogBaseURI() );
      String encryptedSignatureValue = EncryptionUtils.getSharedServicesEncryptedSignature();
      String contextName = getSystemVariableService().getContextName();
      resp = awardBanqServiceResource.path( "getMerchlinqLevelData" ).queryParams( params ).type( MediaType.APPLICATION_JSON_TYPE ).header( "ContextName", contextName )
          .header( "Signature", encryptedSignatureValue ).get( ClientResponse.class );

      if ( resp == null )
      {
        logger.error( "An Error in getMerchlinqLevelDataWebService:" );
        throw new BeaconRuntimeException( "AwardBanq response is null" );
      }

      String output = resp.getEntity( String.class );
      if ( logger.isDebugEnabled() )
      {
        logger.debug( "output : " + output );
      }

      procedureResponseVO = mapper.readValue( output, AwardBanqMerchResponseValueObject.class );

      if ( procedureResponseVO.getErrCode() != 0 )
      {
        throw new AwardslinqDataRetrieverException( procedureResponseVO.getErrDescription() );
      }
    }
    catch( Exception e )
    {
      throw new AwardslinqDataRetrieverException( AwardslinqDataRetrieverException.ERROR_FETCH_MERCH_LEVELS, e );
    }
    finally
    {
      if ( resp != null )
      {
        resp.close();
      }
    }

    return procedureResponseVO;
  }

  public static ObjectMapper buildObjectMapper()
  {
    ObjectMapper mapper = new ObjectMapper();
    // this is required if the Collection/List only has one element. Otherwise, this will cause a
    // deserialization Exception
    mapper.configure( DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true );
    mapper.configure( DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false );
    return mapper;
  }
}
