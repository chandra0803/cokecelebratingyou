
package com.biperf.core.service.fileload.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Locale;

import org.easymock.EasyMock;
import org.jmock.Mock;

import com.biperf.core.dao.promotion.hibernate.PromotionDAOImplTest;
import com.biperf.core.domain.enums.ImportFileStatusType;
import com.biperf.core.domain.enums.ImportFileTypeType;
import com.biperf.core.domain.fileload.ImportFile;
import com.biperf.core.domain.fileload.PaxBaseImportRecord;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.GoalQuestPromotion;
import com.biperf.core.security.AuthenticatedUser;
import com.biperf.core.service.BaseServiceTest;
import com.biperf.core.service.goalquest.PaxGoalService;
import com.biperf.core.service.participant.AudienceService;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.utils.UserManager;

public class PaxBaseImportStrategyTest extends BaseServiceTest
{

  private ParticipantService mockParticipantService;
  private Mock mockAudienceService;
  private PaxGoalService mockPaxGoalService;

  private AuthenticatedUser user, prevUser;
  private PaxBaseImportStrategy strategy;

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

    strategy = new PaxBaseImportStrategy();
    strategy.setParticipantService( mockParticipantService );
    strategy.setPaxGoalService( mockPaxGoalService );
    strategy.setAudienceService( (AudienceService)mockAudienceService.proxy() );

    user = new AuthenticatedUser();
    user.setLocale( Locale.US );
    user.setPrimaryCountryCode( "us" ); // US
    prevUser = UserManager.getUser();
    UserManager.setUser( user );
  }

  @Override
  protected void tearDown() throws Exception
  {
    super.tearDown();
    UserManager.setUser( prevUser );
  }

  public void testVerifyImportFileInvalidState()
  {

    ImportFile file = new ImportFile();
    file.setStatus( ImportFileStatusType.lookup( ImportFileStatusType.VERIFIED ) );
    try
    {
      strategy.verifyImportFile( file, new ArrayList( file.getPaxBaseImportRecords() ), false );
      fail( "should have thrown an exception" );
    }
    catch( Exception e )
    {
      // success
    }

    file.setStatus( ImportFileStatusType.lookup( ImportFileStatusType.IMPORTED ) );
    try
    {
      strategy.verifyImportFile( file, new ArrayList( file.getPaxBaseImportRecords() ), false );
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
      strategy.importImportFile( file, new ArrayList( file.getPaxBaseImportRecords() ) );
      fail( "should have thrown an exception" );
    }
    catch( Exception e )
    {
      // success
    }

    file.setStatus( ImportFileStatusType.lookup( ImportFileStatusType.IMPORTED ) );
    try
    {
      strategy.importImportFile( file, new ArrayList( file.getPaxBaseImportRecords() ) );
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
    file.setId( new Long( 44 ) );
    file.setStatus( ImportFileStatusType.lookup( ImportFileStatusType.VERIFY_IN_PROCESS ) );
    file.setFileType( ImportFileTypeType.lookup( ImportFileTypeType.GQ_BASE_DATA_LOAD ) );
    String uniqueString = getUniqueString();
    GoalQuestPromotion goalQuestPromotion = PromotionDAOImplTest.buildGoalQuestPromotionForFileLoad( "GOALQUESTPROMOTION_" + uniqueString );
    file.setPromotion( goalQuestPromotion );
    PaxBaseImportRecord record1 = new PaxBaseImportRecord();
    record1.setUserName( "bhd-001" );
    record1.setBaseQuantity( new BigDecimal( "4.5" ) );

    file.getPaxBaseImportRecords().add( record1 );
    Participant pax = new Participant();
    EasyMock.expect( mockParticipantService.getParticipantByUserName( "bhd-001" ) ).andReturn( pax );
    EasyMock.replay( mockParticipantService );

    mockAudienceService.expects( atLeastOnce() ).method( "isParticipantInPrimaryAudience" ).with( same( goalQuestPromotion ), same( pax ) ).will( returnValue( true ) );

    strategy.verifyImportFile( file, new ArrayList( file.getPaxBaseImportRecords() ) );

    assertEquals( "ImportRecordErrorCount", 0, file.getImportRecordErrorCount() );
    assertEquals( "Number of record errors", 0, file.getImportRecordErrors().size() );

  }

  public void testInvalidParticipant()
  {
    ImportFile file = new ImportFile();
    file.setId( new Long( 44 ) );
    file.setStatus( ImportFileStatusType.lookup( ImportFileStatusType.VERIFY_IN_PROCESS ) );
    file.setFileType( ImportFileTypeType.lookup( ImportFileTypeType.GQ_BASE_DATA_LOAD ) );
    String uniqueString = getUniqueString();
    GoalQuestPromotion goalQuestPromotion = PromotionDAOImplTest.buildGoalQuestPromotionForFileLoad( "GOALQUESTPROMOTION_" + uniqueString );
    file.setPromotion( goalQuestPromotion );
    PaxBaseImportRecord record1 = new PaxBaseImportRecord();
    record1.setUserName( "bhd-001" );
    record1.setBaseQuantity( new BigDecimal( "3.5" ) );

    file.getPaxBaseImportRecords().add( record1 );

    EasyMock.expect( mockParticipantService.getParticipantByUserName( "bhd-001" ) ).andReturn( null );
    EasyMock.replay( mockParticipantService );

    strategy.verifyImportFile( file, new ArrayList( file.getPaxBaseImportRecords() ) );

    assertEquals( "ImportRecordErrorCount", 1, file.getImportRecordErrorCount() );
    assertEquals( "Number of record errors", 1, file.getImportRecordErrors().size() );

  }

  public void testMissingFields()
  {
    ImportFile file = new ImportFile();
    file.setId( new Long( 44 ) );
    file.setStatus( ImportFileStatusType.lookup( ImportFileStatusType.VERIFY_IN_PROCESS ) );
    file.setFileType( ImportFileTypeType.lookup( ImportFileTypeType.GQ_BASE_DATA_LOAD ) );
    String uniqueString = getUniqueString();
    GoalQuestPromotion goalQuestPromotion = PromotionDAOImplTest.buildGoalQuestPromotionForFileLoad( "GOALQUESTPROMOTION_" + uniqueString );
    file.setPromotion( goalQuestPromotion );
    PaxBaseImportRecord record1 = new PaxBaseImportRecord();
    record1.setUserName( null );
    record1.setBaseQuantity( null );

    file.getPaxBaseImportRecords().add( record1 );

    strategy.verifyImportFile( file, new ArrayList( file.getPaxBaseImportRecords() ) );

    assertEquals( "ImportRecordErrorCount", 1, file.getImportRecordErrorCount() );
    assertEquals( "Number of record errors", 2, file.getImportRecordErrors().size() );

  }

  public void testDuplicateUser()
  {
    ImportFile file = new ImportFile();
    file.setId( new Long( 44 ) );
    file.setStatus( ImportFileStatusType.lookup( ImportFileStatusType.VERIFY_IN_PROCESS ) );
    file.setFileType( ImportFileTypeType.lookup( ImportFileTypeType.GQ_BASE_DATA_LOAD ) );
    String uniqueString = getUniqueString();
    GoalQuestPromotion goalQuestPromotion = PromotionDAOImplTest.buildGoalQuestPromotionForFileLoad( "GOALQUESTPROMOTION_" + uniqueString );
    file.setPromotion( goalQuestPromotion );
    PaxBaseImportRecord record1 = new PaxBaseImportRecord();
    record1.setUserName( "bhd-001" );
    record1.setBaseQuantity( new BigDecimal( "11.5" ) );
    file.getPaxBaseImportRecords().add( record1 );
    PaxBaseImportRecord record2 = new PaxBaseImportRecord();
    record2.setUserName( "bhd-001" );
    record2.setBaseQuantity( new BigDecimal( "11.5" ) );
    file.getPaxBaseImportRecords().add( record2 );
    Participant pax = new Participant();
    EasyMock.expect( mockParticipantService.getParticipantByUserName( "bhd-001" ) ).andReturn( pax );
    EasyMock.expect( mockParticipantService.getParticipantByUserName( "bhd-001" ) ).andReturn( pax );
    EasyMock.replay( mockParticipantService );

    mockAudienceService.expects( atLeastOnce() ).method( "isParticipantInPrimaryAudience" ).with( same( goalQuestPromotion ), same( pax ) ).will( returnValue( true ) );

    strategy.verifyImportFile( file, new ArrayList( file.getPaxBaseImportRecords() ) );

    assertEquals( "ImportRecordErrorCount", 1, file.getImportRecordErrorCount() );
    assertEquals( "Number of record errors", 1, file.getImportRecordErrors().size() );

  }

}
