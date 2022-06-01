
package com.biperf.core.service.fileload.impl;

import java.util.ArrayList;

import org.easymock.EasyMock;
import org.jmock.Mock;

import com.biperf.core.dao.promotion.hibernate.PromotionDAOImplTest;
import com.biperf.core.domain.enums.ImportFileStatusType;
import com.biperf.core.domain.enums.ImportFileTypeType;
import com.biperf.core.domain.fileload.ImportFile;
import com.biperf.core.domain.fileload.PaxGoalImportRecord;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.GoalLevel;
import com.biperf.core.domain.promotion.GoalQuestPromotion;
import com.biperf.core.service.BaseServiceTest;
import com.biperf.core.service.goalquest.PaxGoalService;
import com.biperf.core.service.participant.AudienceService;
import com.biperf.core.service.participant.ParticipantService;

public class PaxGoalImportStrategyTest extends BaseServiceTest
{

  private ParticipantService mockParticipantService;
  private PaxGoalService mockPaxGoalService;
  private Mock mockAudienceService;

  private PaxGoalImportStrategy strategy;

  /**
   * Sets up the fixture, for example, open a network connection. This method is called before a
   * test is executed.
   * 
   * @throws Exception
   */
  protected void setUp() throws Exception
  {
    super.setUp();

    mockParticipantService = EasyMock.createMock( ParticipantService.class );

    mockPaxGoalService = EasyMock.createMock( PaxGoalService.class );
    mockAudienceService = new Mock( AudienceService.class );

    strategy = new PaxGoalImportStrategy();
    strategy.setParticipantService( mockParticipantService );
    strategy.setPaxGoalService( mockPaxGoalService );
    strategy.setAudienceService( (AudienceService)mockAudienceService.proxy() );
  }

  public void testVerifyImportFileInvalidState()
  {

    ImportFile file = new ImportFile();
    file.setStatus( ImportFileStatusType.lookup( ImportFileStatusType.VERIFIED ) );
    try
    {
      strategy.verifyImportFile( file, new ArrayList( file.getPaxGoalImportRecords() ), false );
      fail( "should have thrown an exception" );
    }
    catch( Exception e )
    {
      // success
    }

    file.setStatus( ImportFileStatusType.lookup( ImportFileStatusType.IMPORTED ) );
    try
    {
      strategy.verifyImportFile( file, new ArrayList( file.getPaxGoalImportRecords() ), false );
      fail( "should have thrown an exception" );
    }
    catch( Exception e )
    {
      // success
    }

  }

  public void testIMportImportFileInvalidState()
  {

    ImportFile file = new ImportFile();
    file.setStatus( ImportFileStatusType.lookup( ImportFileStatusType.STAGED ) );
    try
    {
      strategy.importImportFile( file, new ArrayList( file.getPaxGoalImportRecords() ) );
      fail( "should have thrown an exception" );
    }
    catch( Exception e )
    {
      // success
    }

    file.setStatus( ImportFileStatusType.lookup( ImportFileStatusType.IMPORTED ) );
    try
    {
      strategy.importImportFile( file, new ArrayList( file.getPaxGoalImportRecords() ) );
      fail( "should have thrown an exception" );
    }
    catch( Exception e )
    {
      // success
    }
  }

  public void testVerifyImport()
  {
    ImportFile file = new ImportFile();
    file.setId( new Long( 33 ) );
    file.setStatus( ImportFileStatusType.lookup( ImportFileStatusType.VERIFY_IN_PROCESS ) );
    file.setFileType( ImportFileTypeType.lookup( ImportFileTypeType.GQ_GOAL_DATA_LOAD ) );
    String uniqueString = getUniqueString();
    GoalQuestPromotion goalQuestPromotion = PromotionDAOImplTest.buildGoalQuestPromotionForFileLoad( "GOALQUESTPROMOTION_" + uniqueString );

    file.setPromotion( goalQuestPromotion );
    PaxGoalImportRecord record1 = new PaxGoalImportRecord();
    record1.setUserName( "bhd-001" );
    record1.setGoalLevelName( "Level One" );
    file.getPaxGoalImportRecords().add( record1 );
    TestGoalLevel lv = new TestGoalLevel( "Level One" );
    goalQuestPromotion.addGoalLevel( lv );
    Participant pax = new Participant();
    EasyMock.expect( mockParticipantService.getParticipantByUserName( "bhd-001" ) ).andReturn( pax );
    EasyMock.replay( mockParticipantService );
    mockAudienceService.expects( atLeastOnce() ).method( "isParticipantInPrimaryAudience" ).with( same( goalQuestPromotion ), same( pax ) ).will( returnValue( true ) );

    strategy.verifyImportFile( file, new ArrayList( file.getPaxGoalImportRecords() ) );

    assertEquals( "ImportRecordErrorCount", 0, file.getImportRecordErrorCount() );
    assertEquals( "Number of record errors", 0, file.getImportRecordErrors().size() );

  }

  public void testInvalidParticipant()
  {
    ImportFile file = new ImportFile();
    file.setId( new Long( 33 ) );
    file.setStatus( ImportFileStatusType.lookup( ImportFileStatusType.VERIFY_IN_PROCESS ) );
    file.setFileType( ImportFileTypeType.lookup( ImportFileTypeType.GQ_GOAL_DATA_LOAD ) );
    String uniqueString = getUniqueString();
    GoalQuestPromotion goalQuestPromotion = PromotionDAOImplTest.buildGoalQuestPromotionForFileLoad( "GOALQUESTPROMOTION_" + uniqueString );
    file.setPromotion( goalQuestPromotion );
    PaxGoalImportRecord record1 = new PaxGoalImportRecord();
    record1.setUserName( "bhd-001" );
    record1.setGoalLevelName( "Level One" );
    TestGoalLevel lv = new TestGoalLevel( "Level One" );
    goalQuestPromotion.addGoalLevel( lv );

    file.getPaxGoalImportRecords().add( record1 );

    EasyMock.expect( mockParticipantService.getParticipantByUserName( "bhd-001" ) ).andReturn( null );
    EasyMock.replay( mockParticipantService );

    strategy.verifyImportFile( file, new ArrayList( file.getPaxGoalImportRecords() ) );

    assertEquals( "ImportRecordErrorCount", 1, file.getImportRecordErrorCount() );
    assertEquals( "Number of record errors", 1, file.getImportRecordErrors().size() );

  }

  public void testMissingFields()
  {
    ImportFile file = new ImportFile();
    file.setId( new Long( 33 ) );
    file.setStatus( ImportFileStatusType.lookup( ImportFileStatusType.VERIFY_IN_PROCESS ) );
    file.setFileType( ImportFileTypeType.lookup( ImportFileTypeType.GQ_GOAL_DATA_LOAD ) );
    String uniqueString = getUniqueString();
    GoalQuestPromotion goalQuestPromotion = PromotionDAOImplTest.buildGoalQuestPromotionForFileLoad( "GOALQUESTPROMOTION_" + uniqueString );
    file.setPromotion( goalQuestPromotion );
    PaxGoalImportRecord record1 = new PaxGoalImportRecord();
    record1.setUserName( null );
    record1.setGoalLevelName( null );

    file.getPaxGoalImportRecords().add( record1 );

    strategy.verifyImportFile( file, new ArrayList( file.getPaxGoalImportRecords() ) );

    assertEquals( "ImportRecordErrorCount", 1, file.getImportRecordErrorCount() );
    assertEquals( "Number of record errors", 2, file.getImportRecordErrors().size() );

  }

  public void testDuplicateUser()
  {
    ImportFile file = new ImportFile();
    file.setId( new Long( 33 ) );
    file.setStatus( ImportFileStatusType.lookup( ImportFileStatusType.VERIFY_IN_PROCESS ) );
    file.setFileType( ImportFileTypeType.lookup( ImportFileTypeType.GQ_GOAL_DATA_LOAD ) );
    String uniqueString = getUniqueString();
    GoalQuestPromotion goalQuestPromotion = PromotionDAOImplTest.buildGoalQuestPromotionForFileLoad( "GOALQUESTPROMOTION_" + uniqueString );
    file.setPromotion( goalQuestPromotion );
    PaxGoalImportRecord record1 = new PaxGoalImportRecord();
    record1.setUserName( "bhd-001" );
    record1.setGoalLevelName( "Level One" );
    file.getPaxGoalImportRecords().add( record1 );
    PaxGoalImportRecord record2 = new PaxGoalImportRecord();
    record2.setUserName( "bhd-001" );
    record2.setGoalLevelName( "Level Two" );
    file.getPaxGoalImportRecords().add( record2 );
    TestGoalLevel level1 = new TestGoalLevel( "Level One" );
    TestGoalLevel level2 = new TestGoalLevel( "Level Two" );
    goalQuestPromotion.addGoalLevel( level2 );
    goalQuestPromotion.addGoalLevel( level1 );

    Participant pax = new Participant();
    EasyMock.expect( mockParticipantService.getParticipantByUserName( "bhd-001" ) ).andReturn( pax );
    EasyMock.expect( mockParticipantService.getParticipantByUserName( "bhd-001" ) ).andReturn( pax );
    EasyMock.replay( mockParticipantService );
    mockAudienceService.expects( atLeastOnce() ).method( "isParticipantInPrimaryAudience" ).with( same( goalQuestPromotion ), same( pax ) ).will( returnValue( true ) );

    strategy.verifyImportFile( file, new ArrayList( file.getPaxGoalImportRecords() ) );

    assertEquals( "ImportRecordErrorCount", 1, file.getImportRecordErrorCount() );
    assertEquals( "Number of record errors", 1, file.getImportRecordErrors().size() );

  }

  private class TestGoalLevel extends GoalLevel
  {
    private String name;

    public TestGoalLevel( String name )
    {
      this.name = name;
      this.setGoalLevelNameKey( name );
    }

    @Override
    public String getGoalLevelName()
    {
      return name;
    }

    @Override
    public boolean isManagerOverrideGoalLevel()
    {
      return false;
    }

    @Override
    public boolean isPromotionPartnerPayout()
    {
      return false;
    }

  }

}
