package com.biw.hc.core.service.impl;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.biperf.core.domain.system.PropertySetItem;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.participant.impl.ParticipantServiceImplTest;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.ui.UnitTest;
import com.biperf.core.utils.YamlFileUtils;
import com.biperf.core.value.hc.AccountSyncParticipantDetails;
import com.biperf.core.value.hc.AccountSyncRequest;
import com.biperf.core.value.hc.GoalquestDetailsResponse;
import com.biperf.core.value.hc.ParticipantSyncResponse;
import com.biperf.core.value.restresult.LongResult;
import com.biw.hc.core.service.HCRepository;
import com.biw.hc.core.service.HCRepositoryFactory;
import com.biw.hc.core.service.HCServices;

@RunWith( MockitoJUnitRunner.class )
public class HCServicesImplTest extends UnitTest
{
  @Mock
  HCRepositoryFactory hcRepo;
  
  @Mock
  HCRepository hcRepository;

  @Mock
  YamlFileUtils yamlFileUtils;
  
  @Mock
  SystemVariableService systemVariableService;
  
  @Mock
  ParticipantService participantService;

  @InjectMocks
  HCServicesImpl hcServices = new HCServicesImpl()
  {
    SystemVariableService getSystemVariableService()
    {
      return systemVariableService;
    }
    
    ParticipantService getParticipantService()
    {
      return participantService;
    }
    
    Locale getUserLocale()
    {
      return Locale.US;
    }
    
    boolean prepareContentReader()
    {
      return false; // Do nothing, we don't need the reader for the unit tests
    }
    
    void removeContentReader()
    {
      return; // Do nothing
    }
  };

  String EMPLOYER_SYNC_SERVICE_URI = "/employerSync";
  
  @Test
  public void testReadSecureProperties() throws Exception
  {
    Map<String, Map<String, Object>> properties = setupMockSaltProperties();

    Map<String, Map<String, Object>> result = hcServices.readSecureProperties();

    verify( yamlFileUtils ).readAppSecureDir( HCServices.SERVICE_FILENAME_SALT );
    assertTrue( properties.equals( result ) );
  }

  @SuppressWarnings( "unchecked" )
  @Test
  public void testAddSecureProperties() throws Exception
  {
    when( yamlFileUtils.readAppSecureDir( anyString() ) ).thenThrow( FileNotFoundException.class );

    Map<String, Map<String, Object>> properties = new HashMap<>();
    Map<String, Object> envProps = new HashMap<>();
    envProps.put( "salt", "pepper" );
    properties.put( "alpha", envProps );
    hcServices.addSecureProperties( "alpha", envProps );

    verify( yamlFileUtils ).writeAppSecureDir( eq( HCServices.SERVICE_FILENAME_SALT ), eq( properties ) );
  }
  
  @Test
  public void testSyncParticipantDetails() throws Exception
  {
    setupCommonRequestExpectations();
    setMockSystemVariable( SystemVariableService.HC_ENDPOINT_ACCOUNT_SYNC, "/fake/sync/endpoint" );
    PropertySetItem propertySetItem = new PropertySetItem();
    propertySetItem.setIntVal( 3 );
    when( systemVariableService.getPropertyByName( eq( SystemVariableService.HC_SYNC_BATCH_SIZE ) ) ).thenReturn( propertySetItem );

    ParticipantSyncResponse response = new ParticipantSyncResponse();
    when( hcRepository.execute( any(), any(), eq( ParticipantSyncResponse.class ) ) ).thenReturn( new ResponseEntity<ParticipantSyncResponse>( response, HttpStatus.OK ) );
    
    LongResult clientIdResponse = new LongResult( 50L );
    when( hcRepository.execute( any(), any(), eq( LongResult.class ) ) ).thenReturn( new ResponseEntity<LongResult>( clientIdResponse, HttpStatus.OK ) );
    
    AccountSyncRequest accountSyncRequest = buildAccountSyncRequest();
    when( participantService.getGToHoneycombSyncPaxData( any() ) ).thenReturn( accountSyncRequest );
    
    hcServices.syncParticipantDetails( Arrays.asList( 1234L ) );
    verify( hcRepository ).execute( any(), eq( accountSyncRequest ), eq( ParticipantSyncResponse.class ) );
  }
  
  @Test
  public void testSyncAllParticipantDetails() throws Exception
  {
    setupCommonRequestExpectations();
    setMockSystemVariable( SystemVariableService.HC_ENDPOINT_ACCOUNT_SYNC, "/fake/sync/endpoint" );
    PropertySetItem propertySetItem = new PropertySetItem();
    propertySetItem.setIntVal( 3 );
    when( systemVariableService.getPropertyByName( eq( SystemVariableService.HC_SYNC_BATCH_SIZE ) ) ).thenReturn( propertySetItem );
    
    ParticipantSyncResponse response = new ParticipantSyncResponse();
    when( hcRepository.execute( any(), any(), eq( ParticipantSyncResponse.class ) ) ).thenReturn( new ResponseEntity<ParticipantSyncResponse>( response, HttpStatus.OK ) );

    LongResult clientIdResponse = new LongResult( 50L );
    when( hcRepository.execute( any(), any(), eq( LongResult.class ) ) ).thenReturn( new ResponseEntity<LongResult>( clientIdResponse, HttpStatus.OK ) );
    
    AccountSyncRequest accountSyncRequest = buildAccountSyncRequest();
    when( participantService.getGToHoneycombSyncPaxData( any() ) ).thenReturn( accountSyncRequest );

    // 3 batches
    when( participantService.getAllPaxIds() ).thenReturn( Arrays.asList( 1L, 2L, 3L, 4L, 5L, 6L, 7L ) );
    hcServices.syncAllParticipantDetails();
    verify( hcRepository, times( 3 ) ).execute( any(), eq( accountSyncRequest ), eq( ParticipantSyncResponse.class ) );

    // 0 batch size, no additional calls - still 3 total executions
    propertySetItem.setIntVal( 0 );
    hcServices.syncAllParticipantDetails();
    verify( hcRepository, times( 3 ) ).execute( any(), eq( accountSyncRequest ), eq( ParticipantSyncResponse.class ) );

    // Prove point - add one more call
    propertySetItem.setIntVal( 7 );
    hcServices.syncAllParticipantDetails();
    verify( hcRepository, times( 4 ) ).execute( any(), eq( accountSyncRequest ), eq( ParticipantSyncResponse.class ) );

    // No additional calls, no pax IDs
    when( participantService.getAllPaxIds() ).thenReturn( new ArrayList<Long>() );
    hcServices.syncAllParticipantDetails();
    verify( hcRepository, times( 4 ) ).execute( any(), eq( accountSyncRequest ), eq( ParticipantSyncResponse.class ) );
  }
  
  @Test
  public void testGetGoalquestProgramDetails() throws Exception
  {
    setupCommonRequestExpectations();
    setMockSystemVariable( SystemVariableService.HC_ENDPOINT_GOALQUEST_DETAILS, "/fake/gqdetails/endpoint" );

    GoalquestDetailsResponse response = new GoalquestDetailsResponse();
    when( hcRepository.execute( any(), any(), eq( GoalquestDetailsResponse.class ) ) ).thenReturn( new ResponseEntity<GoalquestDetailsResponse>( response, HttpStatus.OK ) );
    
    LongResult clientIdResponse = new LongResult( 50L );
    when( hcRepository.execute( any(), any(), eq( LongResult.class ) ) ).thenReturn( new ResponseEntity<LongResult>( clientIdResponse, HttpStatus.OK ) );
    
    hcServices.getGoalquestProgramDetails( 100L );
    verify( hcRepository ).execute( any(), any(), eq( GoalquestDetailsResponse.class ) );
  }
  
  @Test
  public void testGetGoalquestManagerProgram() throws Exception
  {
    setupCommonRequestExpectations();
    setMockSystemVariable( SystemVariableService.HC_ENDPOINT_GOALQUEST_MANAGERPROGRAMS, "/fake/managerprogram/endpoint" );

    GoalquestDetailsResponse response = new GoalquestDetailsResponse();
    when( hcRepository.execute( any(), any(), eq( GoalquestDetailsResponse.class ) ) ).thenReturn( new ResponseEntity<GoalquestDetailsResponse>( response, HttpStatus.OK ) );
    
    LongResult clientIdResponse = new LongResult( 50L );
    when( hcRepository.execute( any(), any(), eq( LongResult.class ) ) ).thenReturn( new ResponseEntity<LongResult>( clientIdResponse, HttpStatus.OK ) );
    
    hcServices.getGoalquestManagerPrograms( 100L );
    verify( hcRepository ).execute( any(), any(), eq( GoalquestDetailsResponse.class ) );
  }
  
  @Test
  public void testConvertDateString() throws Exception
  {
    String emptyHCDate = "";
    String expectedResultEmpty = "";
    String actualResultEmpty = hcServices.convertDateString( emptyHCDate );
    assertEquals( expectedResultEmpty, actualResultEmpty );
    
    String normalHCDate = "2017-11-20T23:59:59-0600";
    String expectedResultNormal = "11/20/2017";
    String actualResultNormal = hcServices.convertDateString( normalHCDate );
    assertEquals( expectedResultNormal, actualResultNormal );
  }
  
  private void setupCommonRequestExpectations() throws Exception
  {
    setupMockSaltProperties();
    when( hcRepo.getRepo() ).thenReturn( hcRepository );

    setMockSystemVariable( SystemVariableService.HC_GATEWAY_URL, "http://gateway/" );
    setMockSystemVariable( SystemVariableService.HC_ENDPOINT_CLIENT_ID, "/fake/id/endpoint" );
    setMockSystemVariable( SystemVariableService.CLIENT_NAME, "alpha" );
    setMockSystemVariable( SystemVariableService.HC_CLIENT_CODE, "gdevgyoda" );
    when( systemVariableService.getContextName() ).thenReturn( "alpha" );
  }
  
  private Map<String, Map<String, Object>> setupMockSaltProperties() throws Exception
  {
    Map<String, Map<String, Object>> properties = new HashMap<>();
    Map<String, Object> envProps = new HashMap<>();
    envProps.put( HCServices.PROPERTY_KEY_SALT, "pepper" );
    envProps.put( HCServices.PROPERTY_KEY_CLIENT_CODE, "alpha" );
    envProps.put( HCServices.PROPERTY_KEY_FARM, "farm2" );
    properties.put( "alpha", envProps );
    when( yamlFileUtils.readAppSecureDir( anyString() ) ).thenReturn( properties );
    return properties;
  }
  
  private void setMockSystemVariable( String key, String stringValue )
  {
    PropertySetItem propertySetItem = new PropertySetItem();
    propertySetItem.setStringVal( stringValue );
    when( systemVariableService.getPropertyByName( eq( key ) ) ).thenReturn( propertySetItem );
    when( systemVariableService.getPropertyByNameAndEnvironment( eq( key ) ) ).thenReturn( propertySetItem );
  }

  public static AccountSyncRequest buildAccountSyncRequest()
  {
    AccountSyncRequest accountSyncRequest = new AccountSyncRequest();
    for ( int i = 0; i < 2; ++i )
    {
      AccountSyncParticipantDetails details = new AccountSyncParticipantDetails();
      details.setUserName( "username" );
      details.getAttributeMaps().put( "p_out_application_user", ParticipantServiceImplTest.buildAttributeMap( 123L, "username" ) );
      accountSyncRequest.getParticipantDetails().add( details );
    }
    return accountSyncRequest;
  }

}
