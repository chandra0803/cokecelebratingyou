
package com.biw.hc.core.service.impl;

import static org.apache.commons.collections4.CollectionUtils.isEmpty;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.stream.Stream;

import org.apache.commons.lang.StringUtils;
import org.apache.http.client.utils.URIBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.biperf.core.exception.BeaconRuntimeException;
import com.biperf.core.exception.HoneycombException;
import com.biperf.core.security.AuthenticatedUser;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.utils.BICollectionUtils;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.EncryptionUtils;
import com.biperf.core.utils.Environment;
import com.biperf.core.utils.FileExtractUtils;
import com.biperf.core.utils.UserManager;
import com.biperf.core.utils.YamlFileUtils;
import com.biperf.core.utils.cms.ContentReaderUtils;
import com.biperf.core.value.hc.AccountSyncRequest;
import com.biperf.core.value.hc.GoalquestDetailsResponse;
import com.biperf.core.value.hc.GoalquestSSOResponse;
import com.biperf.core.value.hc.HCRequestDetails;
import com.biperf.core.value.hc.HoneycombInitializationData;
import com.biperf.core.value.hc.ParticipantSyncResponse;
import com.biperf.core.value.restresult.LongResult;
import com.biw.hc.core.service.HCRepositoryFactory;
import com.biw.hc.core.service.HCServices;
import com.objectpartners.cms.util.ContentReaderManager;

@Service( HCServices.BEAN_NAME )
public class HCServicesImpl implements HCServices
{
  private static final Logger log = LoggerFactory.getLogger( HCServicesImpl.class );

  private static volatile String version = StringUtils.EMPTY;
  private static volatile Map<String, Object> saltProperties = null;

  @Autowired
  private HCRepositoryFactory hcRepo;

  @Autowired
  private YamlFileUtils yamlFileUtils;
  
  @Override
  public Map<String, Map<String, Object>> readSecureProperties() throws FileNotFoundException
  {
    return yamlFileUtils.readAppSecureDir( SERVICE_FILENAME_SALT );
  }

  @Override
  public void addSecureProperties( String context, Map<String, Object> newProperties ) throws IOException
  {
    // Get existing properties, or create empty map if they don't exist, yet
    Map<String, Map<String, Object>> currentProperties;
    try
    {
      currentProperties = readSecureProperties();
    }
    catch( FileNotFoundException e )
    {
      currentProperties = new HashMap<>();
    }

    currentProperties.put( context, newProperties );

    yamlFileUtils.writeAppSecureDir( SERVICE_FILENAME_SALT, currentProperties );
  }
  
  @Override
  public String convertDateString( String honeycombDate )
  {
    Locale locale = getUserLocale();
    SimpleDateFormat honeycombFormat = new SimpleDateFormat( HONEYCOMB_DATE_FORMAT );
    DateFormat displayFormat = DateUtils.displayStringDateFormat( locale );
    return DateUtils.toConvertDateFormatString( honeycombFormat, displayFormat, honeycombDate );
  }

  @Override
  public GoalquestSSOResponse getGoalquestSSOParameters( String username ) throws BeaconRuntimeException
  {
    String endpoint = getSystemVariableService().getPropertyByName( SystemVariableService.HC_ENDPOINT_SSO_PARAMETERS ).getStringVal();
    endpoint = endpoint.replace( "${userName}", username );
    HCRequestDetails requestDetails = buildRequestDetails( endpoint, HttpMethod.GET );

    // Send request
    try
    {
      ResponseEntity<GoalquestSSOResponse> responseEntity = hcRepo.getRepo().execute( requestDetails, null, GoalquestSSOResponse.class );
      GoalquestSSOResponse response = responseEntity.getBody();
      return response;
    }
    catch( HoneycombException e )
    {
      log.error( "Get SSO Parameters Honeycomb Exception. Developer message: \r\n" + e.getDeveloperMessage() );
      throw new BeaconRuntimeException( "Exception getting sso parameters from honeycomb", e );
    }
  }

  @Override
  public String getGoalquestSSOPath( String username, Map<String, String> routerParameters ) throws URISyntaxException
  {
    GoalquestSSOResponse ssoResponse = getGoalquestSSOParameters( username );
    MultiValueMap<String, String> parameterMap = new LinkedMultiValueMap<>();
    parameterMap.add( "hashString", ssoResponse.getHashString() );
    parameterMap.add( "applicationId", ssoResponse.getApplicationId() );
    parameterMap.add( "userName", ssoResponse.getUserName() );
    parameterMap.add( "signature", ssoResponse.getSignature() );

    SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
    factory.setProxy( Environment.buildProxy() );
    RestTemplate restTemplate = new RestTemplate();
    restTemplate.setRequestFactory( factory );

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType( MediaType.APPLICATION_FORM_URLENCODED );
    HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>( parameterMap, headers );
    URI newLocation = restTemplate.postForLocation( ssoResponse.getProgramUrl() + "/client-sso", request );
    
    URIBuilder builder = new URIBuilder( ssoResponse.getProgramUrl() + newLocation.toASCIIString() );
    routerParameters.forEach( builder::addParameter );

    return builder.build().toASCIIString();
  }

  @Override
  // Async method
  public Future<ParticipantSyncResponse> syncAllParticipantDetails() throws BeaconRuntimeException
  {
    if ( !isHoneycombRegistered() )
    {
      return new AsyncResult<>( new ParticipantSyncResponse() );
    }
    
    List<Long> allPaxIds = getParticipantService().getAllPaxIds();
    if ( isEmpty( allPaxIds ) )
    {
      return new AsyncResult<>( new ParticipantSyncResponse() );
    }

    // Self-invocation. Will not spawn another thread, even though it's an async method. syncAll is
    // already async.
    return syncParticipantDetails( allPaxIds );
  }

  @Override
  // Async method
  public Future<ParticipantSyncResponse> syncParticipantDetails( List<Long> userIds ) throws BeaconRuntimeException
  {
    if ( !isHoneycombRegistered() )
    {
      return new AsyncResult<>( new ParticipantSyncResponse() );
    }
    
    boolean preparedContentReader = false;
    try
    {
      preparedContentReader = prepareContentReader();
      int batchSize = getSystemVariableService().getPropertyByName( SystemVariableService.HC_SYNC_BATCH_SIZE ).getIntVal();

      if ( batchSize <= 0 )
      {
        return new AsyncResult<>( new ParticipantSyncResponse() );
      }

      // Call the sync endpoint for each batch
      ParticipantSyncResponse mergedResponses = new ParticipantSyncResponse();
      Stream<List<Long>> batches = BICollectionUtils.batches( userIds, batchSize );
      Iterator<List<Long>> batchIterator = batches.iterator();
      while ( batchIterator.hasNext() )
      {
        List<Long> batchIds = batchIterator.next();
        ParticipantSyncResponse response = doSyncParticipantDetails( batchIds );
        mergedResponses = reduceSyncResponse( mergedResponses, response );
      }

      return new AsyncResult<>( mergedResponses );
    }
    catch( Throwable t )
    {
      log.error( "Caught async exception", t );
      throw t;
    }
    finally
    {
      // If we created a content reader for this thread, remove it
      if ( preparedContentReader )
      {
        removeContentReader();
      }
    }
  }

  /**
   * Actually do the work of sending participant details. Does not contain batching logic - will operate on entire list that is passed 
   */
  private ParticipantSyncResponse doSyncParticipantDetails( List<Long> userIds ) throws BeaconRuntimeException
  {
    String endpoint = getSystemVariableService().getPropertyByName( SystemVariableService.HC_ENDPOINT_ACCOUNT_SYNC ).getStringVal();
    HCRequestDetails requestDetails = buildRequestDetails( endpoint, HttpMethod.POST );

    // Get the participant data for everyone in this batch
    AccountSyncRequest request = getParticipantService().getGToHoneycombSyncPaxData( userIds );

    // Send request
    try
    {
      ResponseEntity<ParticipantSyncResponse> responseEntity = hcRepo.getRepo().execute( requestDetails, request, ParticipantSyncResponse.class );
      ParticipantSyncResponse response = responseEntity.getBody();

      // Save data from honeycomb for each successfully synced participant
      getParticipantService().saveAccountSyncResponse( response );

      if ( log.isDebugEnabled() )
      {
        log.debug( "Account sync successful for: " + String.join( ", ", response.getSuccessfulUsernames().keySet() ) );
        log.debug( "Account sync not done for: " + String.join( ", ", response.getMissingParticipants() ) );
      }

      return response;
    }
    catch( HoneycombException e )
    {
      log.error( "Participant sync Honeycomb Exception. Developer message: \r\n" + e.getDeveloperMessage() );
      throw new BeaconRuntimeException( "Exception sending honeycomb account sync request for participant IDs: " + userIds, e );
    }
  }

  /** 
   * Merge second response into the first, to 'unbatch' the results 
   */
  private ParticipantSyncResponse reduceSyncResponse( ParticipantSyncResponse first, ParticipantSyncResponse second )
  {
    first.getMissingParticipants().addAll( second.getMissingParticipants() );
    first.getSuccessfulUsernames().putAll( second.getSuccessfulUsernames() );
    return first;
  }

  @Override
  public GoalquestDetailsResponse getGoalquestProgramDetails( Long hcParticipantId ) throws BeaconRuntimeException
  {
    if ( !isHoneycombRegistered() )
    {
      return null;
    }
    
    String endpoint = getSystemVariableService().getPropertyByName( SystemVariableService.HC_ENDPOINT_GOALQUEST_DETAILS ).getStringVal();
    endpoint = endpoint.replace( "${participantId}", String.valueOf( hcParticipantId ) );

    // Send request
    try
    {
      HCRequestDetails requestDetails = buildRequestDetails( endpoint, HttpMethod.GET );
      ResponseEntity<GoalquestDetailsResponse> responseEntity = hcRepo.getRepo().execute( requestDetails, null, GoalquestDetailsResponse.class );
      GoalquestDetailsResponse response = responseEntity.getBody();
      return response;
    }
    catch( HoneycombException e )
    {
      log.error( "Get Goalquest Details Honeycomb Exception. Developer message: \r\n" + e.getDeveloperMessage() );
      throw new BeaconRuntimeException( "Exception getting goalquest details from honeycomb", e );
    }
  }
  
  @Override
  public GoalquestDetailsResponse getGoalquestManagerPrograms( Long hcParticipantId ) throws BeaconRuntimeException
  {
    if ( !isHoneycombRegistered() )
    {
      return null;
    }
    
    String endpoint = getSystemVariableService().getPropertyByName( SystemVariableService.HC_ENDPOINT_GOALQUEST_MANAGERPROGRAMS ).getStringVal();
    endpoint = endpoint.replace( "${participantId}", String.valueOf( hcParticipantId ) );

    // Send request
    try
    {
      HCRequestDetails requestDetails = buildRequestDetails( endpoint, HttpMethod.GET );
      ResponseEntity<GoalquestDetailsResponse> responseEntity = hcRepo.getRepo().execute( requestDetails, null, GoalquestDetailsResponse.class );
      GoalquestDetailsResponse response = responseEntity.getBody();
      return response;
    }
    catch( HoneycombException e )
    {
      log.error( "Get Goalquest Manager Programs Honeycomb Exception. Developer message: \r\n" + e.getDeveloperMessage() );
      throw new BeaconRuntimeException( "Exception getting goalquest manager programs from honeycomb", e );
    }
  }

  /** Builds request details - URL, headers, http method, etc */
  private HCRequestDetails buildRequestDetails( String endpoint, HttpMethod httpMethod ) throws BeaconRuntimeException
  {
    Map<String, Object> saltProperties = getSaltProperties();
    Long clientId = getHoneycombClientId( (String)saltProperties.get( PROPERTY_KEY_CLIENT_CODE ) );
    String clientIdString = clientId == null ? null : String.valueOf( clientId );
    return buildRequestDetails( endpoint, httpMethod, saltProperties, clientIdString );
  }

  // Split from above to avoid circular call on getHoneycombClientId
  private HCRequestDetails buildRequestDetails( String endpoint, HttpMethod httpMethod, Map<String, Object> saltProperties, String clientId ) throws BeaconRuntimeException
  {
    try
    {
      HCRequestDetails requestDetails = new HCRequestDetails();

      String farmProperty = (String)saltProperties.get( PROPERTY_KEY_FARM );
      String clientCodeProperty = (String)saltProperties.get( PROPERTY_KEY_CLIENT_CODE );

      requestDetails.setUrl( buildRequestUrl( endpoint, farmProperty ) );
      requestDetails.setHttpMethod( httpMethod );
      requestDetails.setClientCode( clientCodeProperty );
      requestDetails.setClientId( clientId );
      requestDetails.setEncyrptedSaltKey( EncryptionUtils.generateHoneyCombEncryptedSignature( (String)saltProperties.get( PROPERTY_KEY_SALT ), clientCodeProperty ) );
      requestDetails.setVersion( getVersion() );
      requestDetails.setProduct( getSystemVariableService().getPropertyByName( SystemVariableService.CLIENT_NAME ).getStringVal() );
      requestDetails.setLocale( getUserLocale().toString() );

      // The honeycomb user ID will only be set after account sync process. Some honeycomb calls do
      // require it in the header.
      AuthenticatedUser currentUser = getCurrentUser();
      if ( currentUser != null )
      {
        if ( currentUser.getHoneycombUserId() != null )
        {
          requestDetails.setUserId( String.valueOf( currentUser.getHoneycombUserId() ) );
        }
        requestDetails.setUsername( currentUser.getUsername() );
      }

      return requestDetails;
    }
    catch( IOException e )
    {
      log.error( "There was likely an error opening the honeycomb properties file", e );
      throw new BeaconRuntimeException( "There was an error with the honeycomb properties file.", e );
    }
  }

  private String buildRequestUrl( String endpoint, String farm )
  {
    String requestUrl = getSystemVariableService().getPropertyByNameAndEnvironment( SystemVariableService.HC_GATEWAY_URL ).getStringVal();
    requestUrl += endpoint;
    requestUrl = requestUrl.replace( "${farm}", farm ); // Request URL has a farm placeholder. Replace with configured farm.
    return requestUrl;
  }

  /** 
   * @return Honeycomb properties for the current context
   */
  private Map<String, Object> getSaltProperties() throws BeaconRuntimeException
  {
    try
    {
      if ( null == saltProperties )
      {
        Map<String, Object> properties = readSecureProperties().get( getSystemVariableService().getContextName() );
        if ( properties == null )
        {
          log.error( "Honeycomb properties file does not include properties for context " + getSystemVariableService().getContextName() );
          throw new BeaconRuntimeException( "There was an error with the honeycomb properties file." );
        }
        saltProperties = properties;
      }

      return saltProperties;
    }
    catch( FileNotFoundException e )
    {
      log.error( "Honeycomb properties file does not exist. Honeycomb services cannot be used", e );
      throw new BeaconRuntimeException( "There was an error with the honeycomb properties file.", e );
    }
  }

  /**
   * Checks the initialization data for the client ID. 
   * If not found, requests it from the honeycomb system using the client code.
   */
  public Long getHoneycombClientId( String clientCode )
  {
    if ( HoneycombInitializationData.getInstance().getClientId() != null )
    {
      return HoneycombInitializationData.getInstance().getClientId();
    }
    else
    {
      String endpoint = getSystemVariableService().getPropertyByName( SystemVariableService.HC_ENDPOINT_CLIENT_ID ).getStringVal();
      endpoint = endpoint.replace( "${clientCode}", clientCode );
      HCRequestDetails requestDetails = buildRequestDetails( endpoint, HttpMethod.GET, saltProperties, null );
      try
      {
        ResponseEntity<LongResult> responseEntity = hcRepo.getRepo().execute( requestDetails, null, LongResult.class );
        Long clientId = responseEntity.getBody().getResult();
        HoneycombInitializationData.getInstance().setClientId( clientId );
        return clientId;
      }
      catch( HoneycombException e )
      {
        log.error( "Get Honeycomb Client ID Honeycomb Exception. Developer message: \r\n" + e.getDeveloperMessage() );
        throw new BeaconRuntimeException( "Exception getting client ID from honeycomb", e );
      }
    }
  }

  /** 
   * @return
   * @throws IOException
   */
  public String getVersion() throws IOException
  {
    if ( StringUtils.isEmpty( version ) )
    {
      version = FileExtractUtils.getVersion();
      return version;
    }

    return version;
  }
  
  private boolean isHoneycombRegistered()
  {
    String clientCodeSysVar = getSystemVariableService().getPropertyByName( SystemVariableService.HC_CLIENT_CODE ).getStringVal();
    return StringUtils.isNotBlank( clientCodeSysVar ) && !"changeme".equalsIgnoreCase( clientCodeSysVar );
  }

  // This method exists so it can be overridden during unit testing
  Locale getUserLocale()
  {
    return UserManager.getLocale();
  }

  // Same story, want to be able to override this for unit testing - to avoid unneeded static call
  boolean prepareContentReader()
  {
    if ( ContentReaderManager.getContentReader() == null )
    {
      ContentReaderUtils.prepareContentReader();
      return true;
    }
    return false;
  }
  
  void removeContentReader()
  {
    ContentReaderManager.removeContentReader();
  }

  // Static state, man
  AuthenticatedUser getCurrentUser()
  {
    return UserManager.getUser();
  }

  SystemVariableService getSystemVariableService()
  {
    return (SystemVariableService)BeanLocator.getBean( SystemVariableService.BEAN_NAME );
  }

  ParticipantService getParticipantService()
  {
    return (ParticipantService)BeanLocator.getBean( ParticipantService.BEAN_NAME );
  }

}
