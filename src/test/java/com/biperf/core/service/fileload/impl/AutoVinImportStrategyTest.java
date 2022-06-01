
package com.biperf.core.service.fileload.impl;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import org.easymock.EasyMock;
import org.jmock.Mock;
import org.jmock.core.constraint.IsAnything;

import com.biperf.core.domain.enums.ImportFileStatusType;
import com.biperf.core.domain.enums.ImportFileTypeType;
import com.biperf.core.domain.enums.ProgressLoadType;
import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.domain.fileload.AutoVinImportRecord;
import com.biperf.core.domain.fileload.ImportFile;
import com.biperf.core.domain.goalquest.PaxGoal;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.GoalLevel;
import com.biperf.core.domain.promotion.GoalQuestPromotion;
import com.biperf.core.domain.system.PropertySetItem;
import com.biperf.core.security.AuthenticatedUser;
import com.biperf.core.service.BaseServiceTest;
import com.biperf.core.service.fileload.ImportService;
import com.biperf.core.service.goalquest.GoalQuestPaxActivityService;
import com.biperf.core.service.goalquest.PaxGoalService;
import com.biperf.core.service.participant.AudienceService;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.utils.UserManager;

public class AutoVinImportStrategyTest extends BaseServiceTest
{

  private ParticipantService mockParticipantService;
  private GoalQuestPaxActivityService mockGoalQuestPaxActivityService;
  private PaxGoalService mockPaxGoalService;
  private ImportService mockImportService;
  private AutoVinImportStrategy strategy;
  private Mock mockAudienceService;
  // private Mock mockSystemVariableService = null;

  private AuthenticatedUser user, prevUser;

  private DateFormat sdf = new SimpleDateFormat( "yyyy.MM.dd" );

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
    mockGoalQuestPaxActivityService = EasyMock.createMock( GoalQuestPaxActivityService.class );
    mockPaxGoalService = EasyMock.createMock( PaxGoalService.class );
    mockImportService = EasyMock.createMock( ImportService.class );
    mockAudienceService = new Mock( AudienceService.class );

    strategy = new AutoVinImportStrategy();
    strategy.setParticipantService( mockParticipantService );
    strategy.setGoalQuestPaxActivityService( mockGoalQuestPaxActivityService );
    strategy.setPaxGoalService( mockPaxGoalService );
    strategy.setImportService( mockImportService );
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
      strategy.verifyImportFile( file, new ArrayList( file.getVinImportRecords() ), false );
      fail( "should have thrown an exception" );
    }
    catch( Exception e )
    {
      // success
    }

    file.setStatus( ImportFileStatusType.lookup( ImportFileStatusType.IMPORTED ) );
    try
    {
      strategy.verifyImportFile( file, new ArrayList( file.getVinImportRecords() ), false );
      fail( "should have thrown an exception" );
    }
    catch( Exception e )
    {
      // success
    }

  }

  public void testImportImportFileInvalidState()
  {

    ImportFile file = new ImportFile();
    file.setStatus( ImportFileStatusType.lookup( ImportFileStatusType.STAGED ) );
    try
    {
      strategy.importImportFile( file, new ArrayList( file.getVinImportRecords() ) );
      fail( "should have thrown an exception" );
    }
    catch( Exception e )
    {
      // success
    }

    file.setStatus( ImportFileStatusType.lookup( ImportFileStatusType.IMPORTED ) );
    try
    {
      strategy.importImportFile( file, new ArrayList( file.getVinImportRecords() ) );
      fail( "should have thrown an exception" );
    }
    catch( Exception e )
    {
      // success
    }
  }

  public void testVerifyImport() throws ParseException
  {
    ImportFile file = new ImportFile();
    file.setId( new Long( 22 ) );
    file.setStatus( ImportFileStatusType.lookup( ImportFileStatusType.VERIFY_IN_PROCESS ) );
    file.setFileType( ImportFileTypeType.lookup( ImportFileTypeType.GQ_VIN_LOAD ) );
    file.setProgressEndDate( sdf.parse( "2007.10.19" ) );
    AutoVinImportRecord record1 = new AutoVinImportRecord();
    record1.setLoginId( "bhd-001" );
    record1.setTransactionType( "S" );
    record1.setModel( "Volkswagan Weekender" );
    record1.setVin( "1234567890" );

    GoalQuestPromotion promotion = new GoalQuestPromotion();
    promotion.setId( new Long( 25 ) );
    promotion.setPromotionType( PromotionType.lookup( "goalquest" ) );
    promotion.setProgressLoadType( ProgressLoadType.lookup( ProgressLoadType.AUTOMOTIVE ) );
    promotion.setGoalCollectionEndDate( sdf.parse( "2007.10.2" ) );
    file.setPromotion( promotion );
    file.getVinImportRecords().add( record1 );
    Participant pax = new Participant();
    pax.setId( new Long( 5583 ) );
    EasyMock.expect( mockParticipantService.getParticipantByUserName( record1.getLoginId() ) ).andReturn( pax );
    EasyMock.replay( mockParticipantService );
    PaxGoal paxGoal = new PaxGoal();
    paxGoal.setGoalLevel( new GoalLevel() );
    EasyMock.expect( mockPaxGoalService.getPaxGoalByPromotionIdAndUserId( promotion.getId(), pax.getId() ) ).andReturn( paxGoal );
    EasyMock.replay( mockPaxGoalService );

    List<ImportFile> importFiles = new ArrayList<>();
    ImportFile importFile1 = new ImportFile();
    importFile1.setPromotion( promotion );
    importFile1.setProgressEndDate( sdf.parse( "2007.10.18" ) );
    importFiles.add( importFile1 );
    ImportFileTypeType importFileType = ImportFileTypeType.lookup( ImportFileTypeType.GQ_VIN_LOAD );
    ImportFileStatusType importFileStatusType = ImportFileStatusType.lookup( ImportFileStatusType.IMPORTED );
    EasyMock.expect( mockImportService.getImportFiles( null, importFileType, importFileStatusType, null, null ) ).andReturn( importFiles );
    EasyMock.replay( mockImportService );

    mockAudienceService.expects( atLeastOnce() ).method( "isParticipantInPrimaryAudience" ).with( new IsAnything(), same( pax ) ).will( returnValue( true ) );

    strategy.verifyImportFile( file, new ArrayList( file.getVinImportRecords() ) );

    assertEquals( "ImportRecordErrorCount", 0, file.getImportRecordErrorCount() );
    assertEquals( "Number of record errors", 0, file.getImportRecordErrors().size() );

  }

  public void testInvalidGoalSelectionEndDate() throws ParseException
  {
    ImportFile file = new ImportFile();
    file.setId( new Long( 22 ) );
    file.setStatus( ImportFileStatusType.lookup( ImportFileStatusType.VERIFY_IN_PROCESS ) );
    file.setFileType( ImportFileTypeType.lookup( ImportFileTypeType.GQ_VIN_LOAD ) );
    file.setProgressEndDate( sdf.parse( "2007.10.19" ) );
    AutoVinImportRecord record1 = new AutoVinImportRecord();
    record1.setLoginId( "bhd-001" );
    record1.setTransactionType( "S" );
    record1.setModel( "Volkswagan Weekender" );
    record1.setVin( "1234567890" );

    GoalQuestPromotion promotion = new GoalQuestPromotion();
    promotion.setId( new Long( 25 ) );
    promotion.setPromotionType( PromotionType.lookup( "goalquest" ) );
    promotion.setProgressLoadType( ProgressLoadType.lookup( ProgressLoadType.AUTOMOTIVE ) );
    Calendar aFutureDate = Calendar.getInstance();
    aFutureDate.add( Calendar.DATE, 1 );
    promotion.setGoalCollectionEndDate( aFutureDate.getTime() );
    file.setPromotion( promotion );
    file.getProgressImportRecords().add( record1 );
    Participant pax = new Participant();
    pax.setId( new Long( 5583 ) );
    EasyMock.expect( mockParticipantService.getParticipantByUserName( record1.getLoginId() ) ).andReturn( pax );
    EasyMock.replay( mockParticipantService );

    PaxGoal paxGoal = new PaxGoal();
    paxGoal.setGoalLevel( new GoalLevel() );
    EasyMock.expect( mockPaxGoalService.getPaxGoalByPromotionIdAndUserId( promotion.getId(), pax.getId() ) ).andReturn( paxGoal );
    EasyMock.replay( mockPaxGoalService );

    List<ImportFile> importFiles = new ArrayList<>();
    ImportFile importFile1 = new ImportFile();
    importFile1.setPromotion( promotion );
    importFile1.setProgressEndDate( sdf.parse( "2007.10.18" ) );
    importFiles.add( importFile1 );
    ImportFileTypeType importFileType = ImportFileTypeType.lookup( ImportFileTypeType.GQ_VIN_LOAD );
    ImportFileStatusType importFileStatusType = ImportFileStatusType.lookup( ImportFileStatusType.IMPORTED );
    EasyMock.expect( mockImportService.getImportFiles( null, importFileType, importFileStatusType, null, null ) ).andReturn( importFiles );
    EasyMock.replay( mockImportService );

    strategy.verifyImportFile( file, new ArrayList( file.getProgressImportRecords() ) );

    assertEquals( "ImportRecordErrorCount", 1, file.getImportRecordErrorCount() );
    assertEquals( "Number of record errors", 1, file.getImportRecordErrors().size() );

  }

  public void testInvalidProgressSubmissionDate() throws ParseException
  {
    ImportFile file = new ImportFile();
    file.setId( new Long( 22 ) );
    file.setStatus( ImportFileStatusType.lookup( ImportFileStatusType.VERIFY_IN_PROCESS ) );
    file.setFileType( ImportFileTypeType.lookup( ImportFileTypeType.GQ_VIN_LOAD ) );
    file.setProgressEndDate( sdf.parse( "2007.10.19" ) );
    AutoVinImportRecord record1 = new AutoVinImportRecord();
    record1.setLoginId( "bhd-001" );
    record1.setTransactionType( "S" );
    record1.setModel( "Volkswagan Weekender" );
    record1.setVin( "1234567890" );

    GoalQuestPromotion promotion = new GoalQuestPromotion();
    promotion.setId( new Long( 25 ) );
    promotion.setPromotionType( PromotionType.lookup( "goalquest" ) );
    promotion.setProgressLoadType( ProgressLoadType.lookup( ProgressLoadType.AUTOMOTIVE ) );
    promotion.setGoalCollectionEndDate( sdf.parse( "2007.10.2" ) );
    file.setPromotion( promotion );
    file.getProgressImportRecords().add( record1 );
    Participant pax = new Participant();
    pax.setId( new Long( 5583 ) );
    EasyMock.expect( mockParticipantService.getParticipantByUserName( record1.getLoginId() ) ).andReturn( pax );
    EasyMock.replay( mockParticipantService );

    PaxGoal paxGoal = new PaxGoal();
    paxGoal.setGoalLevel( new GoalLevel() );
    EasyMock.expect( mockPaxGoalService.getPaxGoalByPromotionIdAndUserId( promotion.getId(), pax.getId() ) ).andReturn( paxGoal );
    EasyMock.replay( mockPaxGoalService );

    List importFiles = new ArrayList();
    ImportFile importFile1 = new ImportFile();
    importFile1.setPromotion( promotion );
    importFile1.setProgressEndDate( sdf.parse( "2007.10.20" ) );
    importFiles.add( importFile1 );
    ImportFileTypeType importFileType = ImportFileTypeType.lookup( ImportFileTypeType.GQ_VIN_LOAD );
    ImportFileStatusType importFileStatusType = ImportFileStatusType.lookup( ImportFileStatusType.IMPORTED );
    EasyMock.expect( mockImportService.getImportFiles( null, importFileType, importFileStatusType, null, null ) ).andReturn( importFiles );
    EasyMock.replay( mockImportService );

    strategy.verifyImportFile( file, new ArrayList( file.getProgressImportRecords() ) );

    assertEquals( "ImportRecordErrorCount", 1, file.getImportRecordErrorCount() );
    assertEquals( "Number of record errors", 1, file.getImportRecordErrors().size() );

  }

  public void testAwardIssueDate() throws ParseException
  {
    ImportFile file = new ImportFile();
    file.setId( new Long( 22 ) );
    file.setStatus( ImportFileStatusType.lookup( ImportFileStatusType.VERIFY_IN_PROCESS ) );
    file.setFileType( ImportFileTypeType.lookup( ImportFileTypeType.GQ_VIN_LOAD ) );
    file.setProgressEndDate( sdf.parse( "2007.10.19" ) );
    AutoVinImportRecord record1 = new AutoVinImportRecord();
    record1.setLoginId( "bhd-001" );
    record1.setTransactionType( "S" );
    record1.setModel( "Volkswagan Weekender" );
    record1.setVin( "1234567890" );

    GoalQuestPromotion promotion = new GoalQuestPromotion();
    promotion.setId( new Long( 25 ) );
    promotion.setPromotionType( PromotionType.lookup( "goalquest" ) );
    promotion.setProgressLoadType( ProgressLoadType.lookup( ProgressLoadType.AUTOMOTIVE ) );
    promotion.setIssueAwardsRun( true );
    promotion.setGoalCollectionEndDate( sdf.parse( "2007.10.2" ) );
    file.setPromotion( promotion );
    file.getProgressImportRecords().add( record1 );
    Participant pax = new Participant();
    pax.setId( new Long( 5583 ) );
    EasyMock.expect( mockParticipantService.getParticipantByUserName( record1.getLoginId() ) ).andReturn( pax );
    EasyMock.replay( mockParticipantService );

    PaxGoal paxGoal = new PaxGoal();
    paxGoal.setGoalLevel( new GoalLevel() );
    EasyMock.expect( mockPaxGoalService.getPaxGoalByPromotionIdAndUserId( promotion.getId(), pax.getId() ) ).andReturn( paxGoal );
    EasyMock.replay( mockPaxGoalService );

    List importFiles = new ArrayList();
    ImportFile importFile1 = new ImportFile();
    importFile1.setPromotion( promotion );
    importFile1.setProgressEndDate( sdf.parse( "2007.10.18" ) );
    importFiles.add( importFile1 );
    ImportFileTypeType importFileType = ImportFileTypeType.lookup( ImportFileTypeType.GQ_VIN_LOAD );
    ImportFileStatusType importFileStatusType = ImportFileStatusType.lookup( ImportFileStatusType.IMPORTED );

    PropertySetItem systemVariableDefaultLanguage = new PropertySetItem();
    systemVariableDefaultLanguage.setStringVal( "en_US" );
    systemVariableDefaultLanguage.setKey( SystemVariableService.DEFAULT_LANGUAGE );
    systemVariableDefaultLanguage.setEntityName( SystemVariableService.DEFAULT_LANGUAGE );

    // mockSystemVariableService.expects( once() ).method( "getPropertyByName" )
    // .with( same( SystemVariableService.DEFAULT_LANGUAGE ) )
    // .will( returnValue( systemVariableDefaultLanguage ) );

    EasyMock.expect( mockImportService.getImportFiles( null, importFileType, importFileStatusType, null, null ) ).andReturn( importFiles );
    EasyMock.replay( mockImportService );

    strategy.verifyImportFile( file, new ArrayList( file.getProgressImportRecords() ) );

    assertEquals( "ImportRecordErrorCount", 1, file.getImportRecordErrorCount() );
    assertEquals( "Number of record errors", 1, file.getImportRecordErrors().size() );

  }

  public void testPaxNoGoalSelected() throws ParseException
  {
    ImportFile file = new ImportFile();
    file.setId( new Long( 22 ) );
    file.setStatus( ImportFileStatusType.lookup( ImportFileStatusType.VERIFY_IN_PROCESS ) );
    file.setFileType( ImportFileTypeType.lookup( ImportFileTypeType.GQ_VIN_LOAD ) );
    file.setProgressEndDate( sdf.parse( "2007.10.19" ) );
    AutoVinImportRecord record1 = new AutoVinImportRecord();
    record1.setLoginId( "bhd-001" );
    record1.setTransactionType( "S" );
    record1.setModel( "Volkswagan Weekender" );
    record1.setVin( "1234567890" );

    GoalQuestPromotion promotion = new GoalQuestPromotion();
    promotion.setId( new Long( 25 ) );
    promotion.setPromotionType( PromotionType.lookup( "goalquest" ) );
    promotion.setProgressLoadType( ProgressLoadType.lookup( ProgressLoadType.AUTOMOTIVE ) );
    promotion.setGoalCollectionEndDate( sdf.parse( "2007.10.2" ) );
    file.setPromotion( promotion );
    file.getProgressImportRecords().add( record1 );
    Participant pax = new Participant();
    pax.setId( new Long( 5583 ) );
    EasyMock.expect( mockParticipantService.getParticipantByUserName( record1.getLoginId() ) ).andReturn( pax );
    EasyMock.replay( mockParticipantService );

    PaxGoal paxGoal = new PaxGoal();
    paxGoal.setGoalLevel( null );
    EasyMock.expect( mockPaxGoalService.getPaxGoalByPromotionIdAndUserId( promotion.getId(), pax.getId() ) ).andReturn( paxGoal );
    EasyMock.replay( mockPaxGoalService );

    List importFiles = new ArrayList();
    ImportFile importFile1 = new ImportFile();
    importFile1.setPromotion( promotion );
    importFile1.setProgressEndDate( sdf.parse( "2007.10.18" ) );
    importFiles.add( importFile1 );
    ImportFileTypeType importFileType = ImportFileTypeType.lookup( ImportFileTypeType.GQ_VIN_LOAD );
    ImportFileStatusType importFileStatusType = ImportFileStatusType.lookup( ImportFileStatusType.IMPORTED );
    EasyMock.expect( mockImportService.getImportFiles( null, importFileType, importFileStatusType, null, null ) ).andReturn( importFiles );
    EasyMock.replay( mockImportService );

    mockAudienceService.expects( atLeastOnce() ).method( "isParticipantInPrimaryAudience" ).with( new IsAnything(), same( pax ) ).will( returnValue( true ) );

    strategy.verifyImportFile( file, new ArrayList( file.getProgressImportRecords() ) );

    assertEquals( "ImportRecordErrorCount", 1, file.getImportRecordErrorCount() );
    assertEquals( "Number of record errors", 1, file.getImportRecordErrors().size() );

  }

  public void testPaxNoGoal() throws ParseException
  {
    ImportFile file = new ImportFile();
    file.setId( new Long( 22 ) );
    file.setStatus( ImportFileStatusType.lookup( ImportFileStatusType.VERIFY_IN_PROCESS ) );
    file.setFileType( ImportFileTypeType.lookup( ImportFileTypeType.GQ_VIN_LOAD ) );
    file.setProgressEndDate( sdf.parse( "2007.10.19" ) );
    AutoVinImportRecord record1 = new AutoVinImportRecord();
    record1.setLoginId( "bhd-001" );
    record1.setTransactionType( "S" );
    record1.setModel( "Volkswagan Weekender" );
    record1.setVin( "1234567890" );

    GoalQuestPromotion promotion = new GoalQuestPromotion();
    promotion.setId( new Long( 25 ) );
    promotion.setPromotionType( PromotionType.lookup( "goalquest" ) );
    promotion.setProgressLoadType( ProgressLoadType.lookup( ProgressLoadType.AUTOMOTIVE ) );
    promotion.setGoalCollectionEndDate( sdf.parse( "2007.10.2" ) );
    file.setPromotion( promotion );
    file.getProgressImportRecords().add( record1 );
    Participant pax = new Participant();
    pax.setId( new Long( 5583 ) );
    EasyMock.expect( mockParticipantService.getParticipantByUserName( record1.getLoginId() ) ).andReturn( pax );
    EasyMock.replay( mockParticipantService );

    EasyMock.expect( mockPaxGoalService.getPaxGoalByPromotionIdAndUserId( promotion.getId(), pax.getId() ) ).andReturn( null );
    EasyMock.replay( mockPaxGoalService );

    List importFiles = new ArrayList();
    ImportFile importFile1 = new ImportFile();
    importFile1.setPromotion( promotion );
    importFile1.setProgressEndDate( sdf.parse( "2007.10.18" ) );
    importFiles.add( importFile1 );
    ImportFileTypeType importFileType = ImportFileTypeType.lookup( ImportFileTypeType.GQ_VIN_LOAD );
    ImportFileStatusType importFileStatusType = ImportFileStatusType.lookup( ImportFileStatusType.IMPORTED );
    EasyMock.expect( mockImportService.getImportFiles( null, importFileType, importFileStatusType, null, null ) ).andReturn( importFiles );
    EasyMock.replay( mockImportService );

    mockAudienceService.expects( atLeastOnce() ).method( "isParticipantInPrimaryAudience" ).with( new IsAnything(), same( pax ) ).will( returnValue( true ) );

    strategy.verifyImportFile( file, new ArrayList( file.getProgressImportRecords() ) );

    assertEquals( "ImportRecordErrorCount", 1, file.getImportRecordErrorCount() );
    assertEquals( "Number of record errors", 1, file.getImportRecordErrors().size() );

  }

  public void testInvalidParticipant() throws ParseException
  {
    ImportFile file = new ImportFile();
    file.setId( new Long( 22 ) );
    file.setStatus( ImportFileStatusType.lookup( ImportFileStatusType.VERIFY_IN_PROCESS ) );
    file.setFileType( ImportFileTypeType.lookup( ImportFileTypeType.GQ_VIN_LOAD ) );
    file.setProgressEndDate( sdf.parse( "2007.10.19" ) );
    AutoVinImportRecord record1 = new AutoVinImportRecord();
    record1.setLoginId( "bhd-001" );
    record1.setTransactionType( "S" );
    record1.setModel( "Volkswagan Weekender" );
    record1.setVin( "1234567890" );

    GoalQuestPromotion promotion = new GoalQuestPromotion();
    promotion.setId( new Long( 25 ) );
    promotion.setPromotionType( PromotionType.lookup( "goalquest" ) );
    promotion.setProgressLoadType( ProgressLoadType.lookup( ProgressLoadType.AUTOMOTIVE ) );
    promotion.setGoalCollectionEndDate( sdf.parse( "2007.10.2" ) );
    file.setPromotion( promotion );
    file.getProgressImportRecords().add( record1 );

    EasyMock.expect( mockParticipantService.getParticipantByUserName( "bhd-001" ) ).andReturn( null );
    EasyMock.replay( mockParticipantService );

    List importFiles = new ArrayList();
    ImportFile importFile1 = new ImportFile();
    importFile1.setPromotion( promotion );
    importFile1.setProgressEndDate( sdf.parse( "2007.10.18" ) );
    importFiles.add( importFile1 );
    ImportFileTypeType importFileType = ImportFileTypeType.lookup( ImportFileTypeType.GQ_VIN_LOAD );
    ImportFileStatusType importFileStatusType = ImportFileStatusType.lookup( ImportFileStatusType.IMPORTED );
    EasyMock.expect( mockImportService.getImportFiles( null, importFileType, importFileStatusType, null, null ) ).andReturn( importFiles );
    EasyMock.replay( mockImportService );

    strategy.verifyImportFile( file, new ArrayList( file.getProgressImportRecords() ) );

    assertEquals( "ImportRecordErrorCount", 1, file.getImportRecordErrorCount() );
    assertEquals( "Number of record errors", 1, file.getImportRecordErrors().size() );
  }

  public void testMissingFields() throws ParseException
  {
    ImportFile file = new ImportFile();
    file.setId( new Long( 22 ) );
    file.setStatus( ImportFileStatusType.lookup( ImportFileStatusType.VERIFY_IN_PROCESS ) );
    file.setFileType( ImportFileTypeType.lookup( ImportFileTypeType.GQ_VIN_LOAD ) );
    file.setProgressEndDate( sdf.parse( "2007.10.19" ) );
    AutoVinImportRecord record1 = new AutoVinImportRecord();
    record1.setLoginId( "bhd-001" );
    record1.setTransactionType( "S" );
    record1.setModel( "Volkswagan Weekender" );
    record1.setVin( null );

    GoalQuestPromotion promotion = new GoalQuestPromotion();
    promotion.setId( new Long( 25 ) );
    promotion.setPromotionType( PromotionType.lookup( "goalquest" ) );
    promotion.setProgressLoadType( ProgressLoadType.lookup( ProgressLoadType.AUTOMOTIVE ) );
    promotion.setGoalCollectionEndDate( sdf.parse( "2007.10.2" ) );
    file.setPromotion( promotion );
    file.getProgressImportRecords().add( record1 );

    List importFiles = new ArrayList();
    ImportFile importFile1 = new ImportFile();
    importFile1.setPromotion( promotion );
    importFile1.setProgressEndDate( sdf.parse( "2007.10.18" ) );
    importFiles.add( importFile1 );
    ImportFileTypeType importFileType = ImportFileTypeType.lookup( ImportFileTypeType.GQ_VIN_LOAD );
    ImportFileStatusType importFileStatusType = ImportFileStatusType.lookup( ImportFileStatusType.IMPORTED );
    EasyMock.expect( mockImportService.getImportFiles( null, importFileType, importFileStatusType, null, null ) ).andReturn( importFiles );
    EasyMock.replay( mockImportService );

    strategy.verifyImportFile( file, new ArrayList( file.getProgressImportRecords() ) );

    assertEquals( "ImportRecordErrorCount", 1, file.getImportRecordErrorCount() );
    assertEquals( "Number of record errors", 2, file.getImportRecordErrors().size() );

  }

}
