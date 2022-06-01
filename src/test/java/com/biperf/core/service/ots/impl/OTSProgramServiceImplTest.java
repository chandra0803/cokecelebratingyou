/*
 * (c) 2017 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.service.ots.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;

import com.biperf.core.dao.ots.OTSProgramDAO;
import com.biperf.core.dao.participant.AudienceDAO;
import com.biperf.core.domain.characteristic.NodeTypeCharacteristicType;
import com.biperf.core.domain.enums.OTSProgramStatusType;
import com.biperf.core.domain.enums.PrimaryAudienceType;
import com.biperf.core.domain.ots.OTSBatch;
import com.biperf.core.domain.ots.OTSProgram;
import com.biperf.core.domain.ots.ProgramAudience;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.participant.PaxAudience;
import com.biperf.core.value.ots.v1.program.Batch;
import com.biperf.core.value.ots.v1.program.Program;
import com.biperf.core.vo.ots.OTSProgramVO;

/**
 * 
 * @author rajadura
 * @since Dec 5, 2017
 * 
 */
public class OTSProgramServiceImplTest extends MockObjectTestCase
{
  private OTSProgramServiceImpl otsProgramService = new OTSProgramServiceImpl();

  private Mock mockOTSProgramDAO = null;
  private Mock mockAudienceDAO = null;

  protected void setUp() throws Exception
  {
    super.setUp();
    mockOTSProgramDAO = new Mock( OTSProgramDAO.class );
    mockAudienceDAO = new Mock( AudienceDAO.class );
    otsProgramService.setOtsProgramDAO( (OTSProgramDAO)mockOTSProgramDAO.proxy() );
    otsProgramService.setAudienceDAO( (AudienceDAO)mockAudienceDAO.proxy() );
  }

  public void testCompletedProgramToDisplay()
  {
    List<OTSProgramVO> listOtsProgram = new ArrayList<>();

    OTSProgramVO otsProgram = new OTSProgramVO();
    otsProgram.setProgramNumber( "14356" );
    otsProgram.setProgramStatus( OTSProgramStatusType.COMPLETED );

    listOtsProgram.add( otsProgram );

    mockOTSProgramDAO.expects( once() ).method( "getOTSPrograms" ).will( returnValue( listOtsProgram ) );

    List<OTSProgramVO> otsProgramList = otsProgramService.getOTSProgram();
    List<OTSProgramVO> completedPrgmList = otsProgramList.stream().filter( p -> "completed".equals( p.getProgramStatus() ) ).collect( Collectors.toList() );
    List<OTSProgramVO> incompletePrgmList = otsProgramList.stream().filter( p -> "incompleted".equals( p.getProgramStatus() ) ).collect( Collectors.toList() );

    assertEquals( 1, completedPrgmList.size() );
    assertEquals( 0, incompletePrgmList.size() );
  }

  public void testInCompletedProgramToDisplay()
  {
    List<OTSProgramVO> listOtsProgram = new ArrayList<>();

    OTSProgramVO otsPrgm = new OTSProgramVO();
    otsPrgm.setProgramNumber( "15356" );
    otsPrgm.setProgramStatus( OTSProgramStatusType.INCOMPLETED );

    listOtsProgram.add( otsPrgm );

    mockOTSProgramDAO.expects( once() ).method( "getOTSPrograms" ).will( returnValue( listOtsProgram ) );

    List<OTSProgramVO> otsProgramList = otsProgramService.getOTSProgram();
    List<OTSProgramVO> completedPrgmList = otsProgramList.stream().filter( p -> "completed".equals( p.getProgramStatus() ) ).collect( Collectors.toList() );
    List<OTSProgramVO> incompletePrgmList = otsProgramList.stream().filter( p -> "incompleted".equals( p.getProgramStatus() ) ).collect( Collectors.toList() );
    assertEquals( 0, completedPrgmList.size() );
    assertEquals( 1, incompletePrgmList.size() );
  }

  public void testSave()
  {

    OTSProgram otsProgram = new OTSProgram();
    otsProgram.setId( new Long( 23 ) );
    otsProgram.setClientName( "Test" );
    otsProgram.setAudienceType( PrimaryAudienceType.lookup( PrimaryAudienceType.ALL_ACTIVE_PAX_CODE ) );
    otsProgram.setProgramStatus( OTSProgramStatusType.lookup( OTSProgramStatusType.COMPLETED ) );

    mockOTSProgramDAO.expects( once() ).method( "save" );

    otsProgramService.save( otsProgram );

    // Verify the mockDAO returns the same thing the service is attempting to save.
    mockOTSProgramDAO.verify();

  }

  public void testSaveBatch()
  {

    OTSBatch otsBatch = getOtsBatch();
    mockOTSProgramDAO.expects( once() ).method( "saveBatch" );

    otsProgramService.saveBatch( otsBatch );

    // Verify the mockDAO returns the same thing the service is attempting to save.
    mockOTSProgramDAO.verify();

  }

  private OTSBatch getOtsBatch()
  {
    OTSProgram otsProgram = new OTSProgram();
    otsProgram.setId( new Long( 23 ) );
    otsProgram.setClientName( "Test" );

    OTSBatch otsBatch = new OTSBatch();
    otsBatch.setId( 123L );
    otsBatch.setBatchNumber( 123456L );
    otsBatch.setOtsProgram( otsProgram );
    return otsBatch;
  }

  public void testGetOTSBatchByBatchNumber()
  {

    OTSBatch otsBatch = getOtsBatch();

    mockOTSProgramDAO.expects( once() ).method( "getOTSBatchByBatchNumber" ).will( returnValue( otsBatch ) );

    otsProgramService.getOTSBatchByBatchNumber( 123L );

    // Verify the mockDAO returns the same thing the service is attempting to save.
    mockOTSProgramDAO.verify();

  }

  public void testGetOTSProgramAudienceByProgramNumberAndAudienceId()
  {
    OTSProgram otsProgram = new OTSProgram();
    otsProgram.setProgramNumber( 1001L );
    otsProgram.setClientName( "Test" );
   
    PaxAudience paxAudience = new PaxAudience();
    Participant testPax = new Participant();
    paxAudience.addParticipant( testPax );
    paxAudience.setId( 10250L );

    ProgramAudience programAudience = new ProgramAudience();
    programAudience.setOtsProgram( otsProgram );
    programAudience.setAudience( paxAudience );

    mockOTSProgramDAO.expects( once() ).method( "getOTSProgramAudienceByProgramNumberAndAudienceId" ).with( same( 1001L ), same( paxAudience.getId() ) ).will( returnValue( programAudience ) );
    assertNotNull( otsProgramService.getOTSProgramAudienceByProgramNumberAndAudienceId( 1001L, paxAudience.getId() ) );
  }

  public void testGetBatchDetails()
  {
    Program pgm = new Program();
    Batch batch = new Batch();
    batch.setBatchNumber( "1234" );
    Batch batch1 = new Batch();
    batch1.setBatchNumber( "2345" );
    List<Batch> batches = new ArrayList<Batch>();
    batches.add( batch );
    batches.add( batch1 );
    pgm.setBatches( batches );
    // -ve Flow
    assertNull( otsProgramService.getBatchDetails( "5623", pgm ) );
    // +ve flow
    assertEquals( batch1, otsProgramService.getBatchDetails( "2345", pgm ) );
  }

  public void testIsUserInAudience()
  {
    NodeTypeCharacteristicType nodeTypeCharacteristicType = new NodeTypeCharacteristicType();
    nodeTypeCharacteristicType.setDescription( "description" );
    nodeTypeCharacteristicType.setCharacteristicName( "XChar2" );
    OTSProgram otsProgram = new OTSProgram();
    otsProgram.setProgramNumber( 1001L );
    otsProgram.setClientName( "Test" );
    otsProgram.setAudienceType( PrimaryAudienceType.lookup( PrimaryAudienceType.ALL_ACTIVE_PAX_CODE ) );
    otsProgram.setProgramStatus( OTSProgramStatusType.lookup( OTSProgramStatusType.COMPLETED ) );

    mockOTSProgramDAO.expects( once() ).method( "save" );

    otsProgramService.save( otsProgram );

    assertTrue( otsProgramService.isUserInAudience( 1000L, otsProgram ) );

    otsProgram.setAudienceType( PrimaryAudienceType.lookup( PrimaryAudienceType.SPECIFY_AUDIENCE_CODE ) );

    PaxAudience paxAudience = new PaxAudience();
    Participant testPax = new Participant();
    paxAudience.addParticipant( testPax );
    paxAudience.setId( 10250L );
    otsProgramService.save( otsProgram );

    testPax.setId( new Long( "32140" ) );

    mockAudienceDAO.expects( once() ).method( "checkPaxAudiencesByAudienceIdParticipantId" ).with( same( paxAudience.getId() ), same( testPax.getId() ) ).will( returnValue( new ArrayList() ) );

    ProgramAudience programAudience = new ProgramAudience();
    programAudience.setOtsProgram( otsProgram );
    programAudience.setAudience( paxAudience );

    assertFalse( otsProgramService.isUserInAudience( 1234L, otsProgram ) );
    assertTrue( otsProgramService.isUserInAudience( testPax.getId(), otsProgram ) );
  }

}
