
package com.biperf.core.service.ots.impl;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import com.biperf.core.domain.ots.OTSBillCode;
import com.biperf.core.domain.system.PropertySetItem;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.ots.OTSJWTAuthorization;
import com.biperf.core.resttemplateclientfactory.RestTemplateClientFactory;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.ui.UnitTest;
import com.biperf.core.utils.Environment;
import com.biperf.core.value.ots.v1.program.Batch;
import com.biperf.core.value.ots.v1.program.BatchDescription;
import com.biperf.core.value.ots.v1.program.Program;

@RunWith( MockitoJUnitRunner.class )
public class OTSRepositoryImplTest extends UnitTest
{
  @Mock
  private RestTemplateClientFactory restTemplateClientFactory;

  @Mock
  private OTSJWTAuthorization otsJWTAuthorization;

  @Mock
  private SystemVariableService systemVariableService;

  @InjectMocks
  private OTSRepositoryImpl otsRepositoryImpl = new OTSRepositoryImpl();

  private RestTemplate restTemplate;

  private HttpHeaders headers = new HttpHeaders();

  @Before
  public void setUp() throws Exception
  {
    SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
    factory.setProxy( Environment.buildProxy() );
    restTemplate = new RestTemplate();
    restTemplate.setRequestFactory( factory );

    headers.add( "content-type", MediaType.APPLICATION_JSON_VALUE );
    headers.add( "accept", MediaType.APPLICATION_JSON_VALUE );
    headers.add( "authorization", "Bearer " + otsJWTAuthorization.getOTSJWTAuthorization( "g-development", "72c80448fab64838a217eea00f1ece65" ) );

    // Initialize mocks created above
    MockitoAnnotations.initMocks( this );

  }

  @SuppressWarnings( { "unchecked" } )
  @Test
  public void testProgramInfo() throws HttpStatusCodeException, ServiceErrorException
  {

    // TODO : Need to replace the testing logic.
    /*
     * RestTemplate programOpValue = null; Program opValue = null; OTSRepositoryImpl
     * otsRepositorySpy = Mockito.spy( otsRepositoryImpl ); RestTemplate restTemplateSpy =
     * Mockito.spy( new RestTemplate() ); String url = "https://apipprd.biworldwide.com/v1/ots"; (
     * (OngoingStubbing<RestTemplate>) ( (RestTemplate)Mockito.when( restTemplateSpy ) ) .exchange(
     * ( (OTSRepositoryImpl)Mockito.doReturn( url ).when( otsRepositorySpy ) ).getOTSBaseEndPoint()
     * + "/programs/{id}", HttpMethod.GET, new HttpEntity<String>( (
     * (OTSRepositoryImpl)Mockito.doReturn( headers ).when( otsRepositorySpy )
     * ).getOTSAuthorizationHeaders() ), Program.class, "09017" ) ).thenReturn( programOpValue );
     * opValue = otsRepositoryImpl.getProgramInfo( "09017" ); assertEquals( "09017",
     * opValue.getProgramNumber() );
     */
  }

  @SuppressWarnings( { "unchecked", "null" } )
  @Test
  public void testupdateBatchDetails() throws HttpStatusCodeException, ServiceErrorException
  {

    BatchDescription batchDescription = new BatchDescription();

    batchDescription.setCmText( "OTS Testing Description" );
    batchDescription.setDisplayName( "English [U.S.]" );
    batchDescription.setLocale( "en_US" );

    List<BatchDescription> batchDescriptions = new ArrayList<>();
    batchDescriptions.add( batchDescription );

    OTSBillCode billCode = new OTSBillCode();

    billCode.setId( 1L );
    billCode.setSortOrder( 1L );
    billCode.setBillCode( "5889" );
    billCode.setCustomValue( "OTS Testing" );

    List<OTSBillCode> billCodes = new ArrayList<>();
    billCodes.add( billCode );

    Batch batch = new Batch();
    batch.setBatchNumber( "509078" );
    batch.setBillCodesActive( false );
    batch.setBatchDescription( batchDescriptions );
    batch.setOTSBillCodes( billCodes );

    List<Batch> batches = new ArrayList<>();
    batches.add( batch );

    Program inputProgramValue = new Program();

    inputProgramValue.setProgramNumber( "09017" );
    inputProgramValue.setProgramDescription( "OTS TESTING" );
    inputProgramValue.setClientName( "BIW" );
    inputProgramValue.setHasTransactions( true );
    inputProgramValue.setBatches( batches );

    String credential = new OTSJWTAuthorization().getOTSJWTAuthorization( "g-development", "72c80448fab64838a217eea00f1ece65" );

    PropertySetItem propertySetItem = new PropertySetItem();
    propertySetItem.setStringVal( "https://apipprd.biworldwide.com/v1/ots" );

    when( systemVariableService.getPropertyByNameAndEnvironment( any() ) ).thenReturn( propertySetItem );

    when( otsJWTAuthorization.getOTSJWTAuthorization( "g-development", "72c80448fab64838a217eea00f1ece65" ) ).thenReturn( credential );

    when( restTemplateClientFactory.getRestWebClient() ).thenReturn( restTemplate );

    boolean opVal = otsRepositoryImpl.updateBatchDetails( inputProgramValue );

    boolean checkVal = true;

    assertTrue( opVal == checkVal );

  }

}
