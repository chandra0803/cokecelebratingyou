
package com.biperf.core.service.ots.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import com.biperf.core.domain.ots.OTSBillCode;
import com.biperf.core.service.ots.OTSRepository;
import com.biperf.core.ui.UnitTest;
import com.biperf.core.value.ots.v1.program.Batch;
import com.biperf.core.value.ots.v1.program.BatchDescription;
import com.biperf.core.value.ots.v1.program.Program;

@RunWith( MockitoJUnitRunner.class )
public class OTSServiceImplTest extends UnitTest
{

  @Mock
  private OTSRepository otsRepository;

  @InjectMocks
  @Resource
  private OTSServiceImpl otsServiceImpl = new OTSServiceImpl();

  @Before
  public void setUp() throws Exception
  {
    // Initialize mocks created above
    MockitoAnnotations.initMocks( this );

  }

  @Test
  public void testOTSProgramInfo() throws Exception
  {
    Program program = otsServiceImpl.getOTSProgramInfo( "09017" );
    assertEquals( "16368", program.getProgramNumber().toString() );
  }

  @Test
  public void testUpdateOTSBatchDetails() throws Exception
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

    boolean opVal = otsServiceImpl.updateOTSBatchDetails( inputProgramValue );

    boolean checkVal = true;

    assertTrue( opVal == checkVal );

  }

}
