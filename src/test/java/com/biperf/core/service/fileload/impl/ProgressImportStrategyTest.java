/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javatest/com/biperf/core/service/fileload/impl/ProgressImportStrategyTest.java,v $
 */

package com.biperf.core.service.fileload.impl;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.springframework.test.context.ContextConfiguration;

import com.biperf.core.domain.enums.ImportFileStatusType;
import com.biperf.core.domain.enums.ImportFileTypeType;
import com.biperf.core.domain.enums.ProgressLoadType;
import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.domain.fileload.GoalQuestProgressImportRecord;
import com.biperf.core.domain.fileload.ImportFile;
import com.biperf.core.domain.goalquest.PaxGoal;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.GoalLevel;
import com.biperf.core.domain.promotion.GoalQuestPromotion;
import com.biperf.core.security.AuthenticatedUser;
import com.biperf.core.service.BaseServiceTest;
import com.biperf.core.service.fileload.ImportService;
import com.biperf.core.service.goalquest.GoalQuestPaxActivityService;
import com.biperf.core.service.goalquest.PaxGoalService;
import com.biperf.core.service.participant.AudienceService;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.utils.UserManager;

/**
 * ProgressImportStrategyTest.
 * <p>
 * <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>tennant</td>
 * <td>Sep 23, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
@ContextConfiguration
public class ProgressImportStrategyTest extends BaseServiceTest
{

  private IMocksControl control;
  private ParticipantService mockParticipantService;
  private GoalQuestPaxActivityService mockGoalQuestPaxActivityService;
  private PaxGoalService mockPaxGoalService;
  private ImportService mockImportService;
  private ProgressImportStrategy strategy;
  private AudienceService mockAudienceService;

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
    control = EasyMock.createControl();
    mockParticipantService = control.createMock( ParticipantService.class );
    mockGoalQuestPaxActivityService = control.createMock( GoalQuestPaxActivityService.class );
    mockPaxGoalService = control.createMock( PaxGoalService.class );
    mockImportService = control.createMock( ImportService.class );
    mockAudienceService = control.createMock( AudienceService.class );

    strategy = new ProgressImportStrategy();
    strategy.setParticipantService( mockParticipantService );
    strategy.setGoalQuestPaxActivityService( mockGoalQuestPaxActivityService );
    strategy.setPaxGoalService( mockPaxGoalService );
    strategy.setImportService( mockImportService );
    strategy.setAudienceService( mockAudienceService );

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
      strategy.verifyImportFile( file, new ArrayList( file.getProgressImportRecords() ), false );
      fail( "should have thrown an exception" );
    }
    catch( Exception e )
    {
      // success
    }

    file.setStatus( ImportFileStatusType.lookup( ImportFileStatusType.IMPORTED ) );
    try
    {
      strategy.verifyImportFile( file, new ArrayList( file.getProgressImportRecords() ), false );
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
      strategy.importImportFile( file, new ArrayList( file.getProgressImportRecords() ) );
      fail( "should have thrown an exception" );
    }
    catch( Exception e )
    {
      // success
    }

    file.setStatus( ImportFileStatusType.lookup( ImportFileStatusType.IMPORTED ) );
    try
    {
      strategy.importImportFile( file, new ArrayList( file.getProgressImportRecords() ) );
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
    file.setFileType( ImportFileTypeType.lookup( ImportFileTypeType.GQ_PROGRESS_DATA_LOAD ) );
    file.setReplaceValues( Boolean.TRUE );
    file.setProgressEndDate( sdf.parse( "2007.10.19" ) );
    GoalQuestProgressImportRecord record1 = new GoalQuestProgressImportRecord();
    record1.setLoginId( "bhd-001" );
    record1.setTotalPerformanceToDate( new BigDecimal( "22.5" ) );

    GoalQuestPromotion promotion = new GoalQuestPromotion();
    promotion.setId( new Long( 25 ) );
    promotion.setPromotionType( PromotionType.lookup( "goalquest" ) );
    promotion.setProgressLoadType( ProgressLoadType.lookup( ProgressLoadType.SALES ) );
    promotion.setGoalCollectionEndDate( sdf.parse( "2007.10.2" ) );
    file.setPromotion( promotion );
    file.getProgressImportRecords().add( record1 );
    Participant pax = new Participant();
    pax.setId( new Long( 5583 ) );

    PaxGoal paxGoal = new PaxGoal();
    paxGoal.setGoalLevel( new GoalLevel() );

    List importFiles = new ArrayList();
    ImportFile importFile1 = new ImportFile();
    importFile1.setPromotion( promotion );
    importFile1.setProgressEndDate( sdf.parse( "2007.10.18" ) );
    importFiles.add( importFile1 );
    ImportFileTypeType importFileType = ImportFileTypeType.lookup( ImportFileTypeType.GQ_PROGRESS_DATA_LOAD );
    ImportFileStatusType importFileStatusType = ImportFileStatusType.lookup( ImportFileStatusType.IMPORTED );

    EasyMock.expect( mockParticipantService.getParticipantByUserName( record1.getLoginId() ) ).andReturn( pax );
    EasyMock.expect( mockPaxGoalService.getPaxGoalByPromotionIdAndUserId( promotion.getId(), pax.getId() ) ).andReturn( paxGoal );
    EasyMock.expect( mockImportService.getImportFiles( null, importFileType, importFileStatusType, null, null ) ).andReturn( importFiles );
    EasyMock.expect( mockAudienceService.isParticipantInPrimaryAudience( promotion, pax ) ).andReturn( true );

    control.replay();
    strategy.verifyImportFile( file, new ArrayList( file.getProgressImportRecords() ) );

    assertEquals( "ImportRecordErrorCount", 0, file.getImportRecordErrorCount() );
    assertEquals( "Number of record errors", 0, file.getImportRecordErrors().size() );

  }

  public void testInvalidGoalSelectionEndDate() throws ParseException
  {
    ImportFile file = new ImportFile();
    file.setId( new Long( 22 ) );
    file.setStatus( ImportFileStatusType.lookup( ImportFileStatusType.VERIFY_IN_PROCESS ) );
    file.setFileType( ImportFileTypeType.lookup( ImportFileTypeType.GQ_PROGRESS_DATA_LOAD ) );
    file.setReplaceValues( Boolean.TRUE );
    file.setProgressEndDate( sdf.parse( "2007.10.19" ) );
    GoalQuestProgressImportRecord record1 = new GoalQuestProgressImportRecord();
    record1.setLoginId( "bhd-001" );
    record1.setTotalPerformanceToDate( new BigDecimal( "22.5" ) );

    GoalQuestPromotion promotion = new GoalQuestPromotion();
    promotion.setId( new Long( 25 ) );
    promotion.setPromotionType( PromotionType.lookup( "goalquest" ) );
    promotion.setProgressLoadType( ProgressLoadType.lookup( ProgressLoadType.SALES ) );
    Calendar aFutureDate = Calendar.getInstance();
    aFutureDate.add( Calendar.DATE, 1 );
    promotion.setGoalCollectionEndDate( aFutureDate.getTime() );
    file.setPromotion( promotion );
    file.getProgressImportRecords().add( record1 );
    Participant pax = new Participant();
    pax.setId( new Long( 5583 ) );

    PaxGoal paxGoal = new PaxGoal();
    paxGoal.setGoalLevel( new GoalLevel() );

    List importFiles = new ArrayList();
    ImportFile importFile1 = new ImportFile();
    importFile1.setPromotion( promotion );
    importFile1.setProgressEndDate( sdf.parse( "2007.10.18" ) );
    importFiles.add( importFile1 );
    ImportFileTypeType importFileType = ImportFileTypeType.lookup( ImportFileTypeType.GQ_PROGRESS_DATA_LOAD );
    ImportFileStatusType importFileStatusType = ImportFileStatusType.lookup( ImportFileStatusType.IMPORTED );

    EasyMock.expect( mockParticipantService.getParticipantByUserName( record1.getLoginId() ) ).andReturn( pax );
    EasyMock.expect( mockPaxGoalService.getPaxGoalByPromotionIdAndUserId( promotion.getId(), pax.getId() ) ).andReturn( paxGoal );
    EasyMock.expect( mockImportService.getImportFiles( null, importFileType, importFileStatusType, null, null ) ).andReturn( importFiles );

    control.replay();
    strategy.verifyImportFile( file, new ArrayList( file.getProgressImportRecords() ) );

    assertEquals( "ImportRecordErrorCount", 1, file.getImportRecordErrorCount() );
    assertEquals( "Number of record errors", 1, file.getImportRecordErrors().size() );

  }

  public void testInvalidProgressSubmissionDate() throws ParseException
  {
    ImportFile file = new ImportFile();
    file.setId( new Long( 22 ) );
    file.setStatus( ImportFileStatusType.lookup( ImportFileStatusType.VERIFY_IN_PROCESS ) );
    file.setFileType( ImportFileTypeType.lookup( ImportFileTypeType.GQ_PROGRESS_DATA_LOAD ) );
    file.setReplaceValues( Boolean.TRUE );
    file.setProgressEndDate( sdf.parse( "2007.10.19" ) );
    GoalQuestProgressImportRecord record1 = new GoalQuestProgressImportRecord();
    record1.setLoginId( "bhd-001" );
    record1.setTotalPerformanceToDate( new BigDecimal( "22.5" ) );

    GoalQuestPromotion promotion = new GoalQuestPromotion();
    promotion.setId( new Long( 25 ) );
    promotion.setPromotionType( PromotionType.lookup( "goalquest" ) );
    promotion.setProgressLoadType( ProgressLoadType.lookup( ProgressLoadType.SALES ) );
    promotion.setGoalCollectionEndDate( sdf.parse( "2007.10.2" ) );
    file.setPromotion( promotion );
    file.getProgressImportRecords().add( record1 );
    Participant pax = new Participant();
    pax.setId( new Long( 5583 ) );

    PaxGoal paxGoal = new PaxGoal();
    paxGoal.setGoalLevel( new GoalLevel() );

    List importFiles = new ArrayList();
    ImportFile importFile1 = new ImportFile();
    importFile1.setPromotion( promotion );
    importFile1.setProgressEndDate( sdf.parse( "2007.10.20" ) );
    importFiles.add( importFile1 );
    ImportFileTypeType importFileType = ImportFileTypeType.lookup( ImportFileTypeType.GQ_PROGRESS_DATA_LOAD );
    ImportFileStatusType importFileStatusType = ImportFileStatusType.lookup( ImportFileStatusType.IMPORTED );

    EasyMock.expect( mockParticipantService.getParticipantByUserName( record1.getLoginId() ) ).andReturn( pax );
    EasyMock.expect( mockPaxGoalService.getPaxGoalByPromotionIdAndUserId( promotion.getId(), pax.getId() ) ).andReturn( paxGoal );
    EasyMock.expect( mockImportService.getImportFiles( null, importFileType, importFileStatusType, null, null ) ).andReturn( importFiles );

    control.replay();

    strategy.verifyImportFile( file, new ArrayList( file.getProgressImportRecords() ) );

    assertEquals( "ImportRecordErrorCount", 1, file.getImportRecordErrorCount() );
    assertEquals( "Number of record errors", 1, file.getImportRecordErrors().size() );

  }

  public void testAwardIssueDate() throws ParseException
  {
    ImportFile file = new ImportFile();
    file.setId( new Long( 22 ) );
    file.setStatus( ImportFileStatusType.lookup( ImportFileStatusType.VERIFY_IN_PROCESS ) );
    file.setFileType( ImportFileTypeType.lookup( ImportFileTypeType.GQ_PROGRESS_DATA_LOAD ) );
    file.setReplaceValues( Boolean.TRUE );
    file.setProgressEndDate( sdf.parse( "2007.10.19" ) );
    GoalQuestProgressImportRecord record1 = new GoalQuestProgressImportRecord();
    record1.setLoginId( "bhd-001" );
    record1.setTotalPerformanceToDate( new BigDecimal( "22.5" ) );

    GoalQuestPromotion promotion = new GoalQuestPromotion();
    promotion.setId( new Long( 25 ) );
    promotion.setPromotionType( PromotionType.lookup( "goalquest" ) );
    promotion.setProgressLoadType( ProgressLoadType.lookup( ProgressLoadType.SALES ) );
    promotion.setIssueAwardsRun( true );
    promotion.setGoalCollectionEndDate( sdf.parse( "2007.10.2" ) );
    file.setPromotion( promotion );
    file.getProgressImportRecords().add( record1 );
    Participant pax = new Participant();
    pax.setId( new Long( 5583 ) );

    PaxGoal paxGoal = new PaxGoal();
    paxGoal.setGoalLevel( new GoalLevel() );

    List importFiles = new ArrayList();
    ImportFile importFile1 = new ImportFile();
    importFile1.setPromotion( promotion );
    importFile1.setProgressEndDate( sdf.parse( "2007.10.18" ) );
    importFiles.add( importFile1 );
    ImportFileTypeType importFileType = ImportFileTypeType.lookup( ImportFileTypeType.GQ_PROGRESS_DATA_LOAD );
    ImportFileStatusType importFileStatusType = ImportFileStatusType.lookup( ImportFileStatusType.IMPORTED );

    EasyMock.expect( mockParticipantService.getParticipantByUserName( record1.getLoginId() ) ).andReturn( pax );
    EasyMock.expect( mockPaxGoalService.getPaxGoalByPromotionIdAndUserId( promotion.getId(), pax.getId() ) ).andReturn( paxGoal );
    EasyMock.expect( mockImportService.getImportFiles( null, importFileType, importFileStatusType, null, null ) ).andReturn( importFiles );
    control.replay();

    strategy.verifyImportFile( file, new ArrayList( file.getProgressImportRecords() ) );

    assertEquals( "ImportRecordErrorCount", 1, file.getImportRecordErrorCount() );
    assertEquals( "Number of record errors", 1, file.getImportRecordErrors().size() );

  }

  public void testPaxNoGoalSelected() throws ParseException
  {
    ImportFile file = new ImportFile();
    file.setId( new Long( 22 ) );
    file.setStatus( ImportFileStatusType.lookup( ImportFileStatusType.VERIFY_IN_PROCESS ) );
    file.setFileType( ImportFileTypeType.lookup( ImportFileTypeType.GQ_PROGRESS_DATA_LOAD ) );
    file.setReplaceValues( Boolean.TRUE );
    file.setProgressEndDate( sdf.parse( "2007.10.19" ) );
    GoalQuestProgressImportRecord record1 = new GoalQuestProgressImportRecord();
    record1.setLoginId( "bhd-001" );
    record1.setTotalPerformanceToDate( new BigDecimal( "22.5" ) );

    GoalQuestPromotion promotion = new GoalQuestPromotion();
    promotion.setId( new Long( 25 ) );
    promotion.setPromotionType( PromotionType.lookup( "goalquest" ) );
    promotion.setProgressLoadType( ProgressLoadType.lookup( ProgressLoadType.SALES ) );
    promotion.setGoalCollectionEndDate( sdf.parse( "2007.10.2" ) );
    file.setPromotion( promotion );
    file.getProgressImportRecords().add( record1 );
    Participant pax = new Participant();
    pax.setId( new Long( 5583 ) );

    PaxGoal paxGoal = new PaxGoal();
    paxGoal.setGoalLevel( null );

    List importFiles = new ArrayList();
    ImportFile importFile1 = new ImportFile();
    importFile1.setPromotion( promotion );
    importFile1.setProgressEndDate( sdf.parse( "2007.10.18" ) );
    importFiles.add( importFile1 );
    ImportFileTypeType importFileType = ImportFileTypeType.lookup( ImportFileTypeType.GQ_PROGRESS_DATA_LOAD );
    ImportFileStatusType importFileStatusType = ImportFileStatusType.lookup( ImportFileStatusType.IMPORTED );

    EasyMock.expect( mockParticipantService.getParticipantByUserName( record1.getLoginId() ) ).andReturn( pax );
    EasyMock.expect( mockPaxGoalService.getPaxGoalByPromotionIdAndUserId( promotion.getId(), pax.getId() ) ).andReturn( paxGoal );
    EasyMock.expect( mockImportService.getImportFiles( null, importFileType, importFileStatusType, null, null ) ).andReturn( importFiles );
    EasyMock.expect( mockAudienceService.isParticipantInPrimaryAudience( promotion, pax ) ).andReturn( true );

    control.replay();

    strategy.verifyImportFile( file, new ArrayList( file.getProgressImportRecords() ) );

    assertEquals( "ImportRecordErrorCount", 1, file.getImportRecordErrorCount() );
    assertEquals( "Number of record errors", 1, file.getImportRecordErrors().size() );

  }

  public void testPaxNoGoal() throws ParseException
  {
    ImportFile file = new ImportFile();
    file.setId( new Long( 22 ) );
    file.setStatus( ImportFileStatusType.lookup( ImportFileStatusType.VERIFY_IN_PROCESS ) );
    file.setFileType( ImportFileTypeType.lookup( ImportFileTypeType.GQ_PROGRESS_DATA_LOAD ) );
    file.setReplaceValues( Boolean.TRUE );
    file.setProgressEndDate( sdf.parse( "2007.10.19" ) );
    GoalQuestProgressImportRecord record1 = new GoalQuestProgressImportRecord();
    record1.setLoginId( "bhd-001" );
    record1.setTotalPerformanceToDate( new BigDecimal( "22.5" ) );

    GoalQuestPromotion promotion = new GoalQuestPromotion();
    promotion.setId( new Long( 25 ) );
    promotion.setPromotionType( PromotionType.lookup( "goalquest" ) );
    promotion.setProgressLoadType( ProgressLoadType.lookup( ProgressLoadType.SALES ) );
    promotion.setGoalCollectionEndDate( sdf.parse( "2007.10.2" ) );
    file.setPromotion( promotion );
    file.getProgressImportRecords().add( record1 );
    Participant pax = new Participant();
    pax.setId( new Long( 5583 ) );

    List importFiles = new ArrayList();
    ImportFile importFile1 = new ImportFile();
    importFile1.setPromotion( promotion );
    importFile1.setProgressEndDate( sdf.parse( "2007.10.18" ) );
    importFiles.add( importFile1 );
    ImportFileTypeType importFileType = ImportFileTypeType.lookup( ImportFileTypeType.GQ_PROGRESS_DATA_LOAD );
    ImportFileStatusType importFileStatusType = ImportFileStatusType.lookup( ImportFileStatusType.IMPORTED );

    EasyMock.expect( mockParticipantService.getParticipantByUserName( record1.getLoginId() ) ).andReturn( pax );
    EasyMock.expect( mockPaxGoalService.getPaxGoalByPromotionIdAndUserId( promotion.getId(), pax.getId() ) ).andReturn( null );
    EasyMock.expect( mockImportService.getImportFiles( null, importFileType, importFileStatusType, null, null ) ).andReturn( importFiles );
    EasyMock.expect( mockAudienceService.isParticipantInPrimaryAudience( promotion, pax ) ).andReturn( true );
    control.replay();

    strategy.verifyImportFile( file, new ArrayList( file.getProgressImportRecords() ) );

    assertEquals( "ImportRecordErrorCount", 1, file.getImportRecordErrorCount() );
    assertEquals( "Number of record errors", 1, file.getImportRecordErrors().size() );

  }

  public void testInvalidParticipant() throws ParseException
  {
    ImportFile file = new ImportFile();
    file.setId( new Long( 22 ) );
    file.setStatus( ImportFileStatusType.lookup( ImportFileStatusType.VERIFY_IN_PROCESS ) );
    file.setFileType( ImportFileTypeType.lookup( ImportFileTypeType.GQ_PROGRESS_DATA_LOAD ) );
    file.setReplaceValues( Boolean.TRUE );
    file.setProgressEndDate( sdf.parse( "2007.10.19" ) );
    GoalQuestProgressImportRecord record1 = new GoalQuestProgressImportRecord();
    record1.setLoginId( "bhd-001" );
    record1.setTotalPerformanceToDate( new BigDecimal( "22.5" ) );

    GoalQuestPromotion promotion = new GoalQuestPromotion();
    promotion.setId( new Long( 25 ) );
    promotion.setPromotionType( PromotionType.lookup( "goalquest" ) );
    promotion.setProgressLoadType( ProgressLoadType.lookup( ProgressLoadType.SALES ) );
    promotion.setGoalCollectionEndDate( sdf.parse( "2007.10.2" ) );
    file.setPromotion( promotion );
    file.getProgressImportRecords().add( record1 );

    List importFiles = new ArrayList();
    ImportFile importFile1 = new ImportFile();
    importFile1.setPromotion( promotion );
    importFile1.setProgressEndDate( sdf.parse( "2007.10.18" ) );
    importFiles.add( importFile1 );
    ImportFileTypeType importFileType = ImportFileTypeType.lookup( ImportFileTypeType.GQ_PROGRESS_DATA_LOAD );
    ImportFileStatusType importFileStatusType = ImportFileStatusType.lookup( ImportFileStatusType.IMPORTED );

    EasyMock.expect( mockParticipantService.getParticipantByUserName( "bhd-001" ) ).andReturn( null );
    EasyMock.expect( mockImportService.getImportFiles( null, importFileType, importFileStatusType, null, null ) ).andReturn( importFiles );

    control.replay();

    strategy.verifyImportFile( file, new ArrayList( file.getProgressImportRecords() ) );

    assertEquals( "ImportRecordErrorCount", 1, file.getImportRecordErrorCount() );
    assertEquals( "Number of record errors", 1, file.getImportRecordErrors().size() );

  }

  public void testMissingFields() throws ParseException
  {
    ImportFile file = new ImportFile();
    file.setId( new Long( 22 ) );
    file.setStatus( ImportFileStatusType.lookup( ImportFileStatusType.VERIFY_IN_PROCESS ) );
    file.setFileType( ImportFileTypeType.lookup( ImportFileTypeType.GQ_PROGRESS_DATA_LOAD ) );
    file.setReplaceValues( Boolean.TRUE );
    file.setProgressEndDate( sdf.parse( "2007.10.19" ) );
    GoalQuestProgressImportRecord record1 = new GoalQuestProgressImportRecord();
    record1.setLoginId( null );
    record1.setTotalPerformanceToDate( null );

    GoalQuestPromotion promotion = new GoalQuestPromotion();
    promotion.setId( new Long( 25 ) );
    promotion.setPromotionType( PromotionType.lookup( "goalquest" ) );
    promotion.setProgressLoadType( ProgressLoadType.lookup( ProgressLoadType.SALES ) );
    promotion.setGoalCollectionEndDate( sdf.parse( "2007.10.2" ) );
    file.setPromotion( promotion );
    file.getProgressImportRecords().add( record1 );

    List importFiles = new ArrayList();
    ImportFile importFile1 = new ImportFile();
    importFile1.setPromotion( promotion );
    importFile1.setProgressEndDate( sdf.parse( "2007.10.18" ) );
    importFiles.add( importFile1 );
    ImportFileTypeType importFileType = ImportFileTypeType.lookup( ImportFileTypeType.GQ_PROGRESS_DATA_LOAD );
    ImportFileStatusType importFileStatusType = ImportFileStatusType.lookup( ImportFileStatusType.IMPORTED );

    EasyMock.expect( mockImportService.getImportFiles( null, importFileType, importFileStatusType, null, null ) ).andReturn( importFiles );
    control.replay();

    strategy.verifyImportFile( file, new ArrayList( file.getProgressImportRecords() ) );

    assertEquals( "ImportRecordErrorCount", 1, file.getImportRecordErrorCount() );
    assertEquals( "Number of record errors", 2, file.getImportRecordErrors().size() );

  }

  public void testDuplicateUser() throws ParseException
  {
    ImportFile file = new ImportFile();
    file.setId( new Long( 22 ) );
    file.setStatus( ImportFileStatusType.lookup( ImportFileStatusType.VERIFY_IN_PROCESS ) );
    file.setFileType( ImportFileTypeType.lookup( ImportFileTypeType.GQ_PROGRESS_DATA_LOAD ) );
    file.setReplaceValues( Boolean.TRUE );
    file.setProgressEndDate( sdf.parse( "2007.10.19" ) );
    GoalQuestProgressImportRecord record1 = new GoalQuestProgressImportRecord();
    record1.setLoginId( "bhd-001" );
    record1.setTotalPerformanceToDate( new BigDecimal( "22.5" ) );
    file.getProgressImportRecords().add( record1 );
    GoalQuestProgressImportRecord record2 = new GoalQuestProgressImportRecord();
    record2.setLoginId( "bhd-001" );
    record2.setTotalPerformanceToDate( new BigDecimal( "25.5" ) );
    file.getProgressImportRecords().add( record2 );

    GoalQuestPromotion promotion = new GoalQuestPromotion();
    promotion.setId( new Long( 25 ) );
    promotion.setGoalCollectionEndDate( sdf.parse( "2007.10.2" ) );
    promotion.setPromotionType( PromotionType.lookup( "goalquest" ) );
    promotion.setProgressLoadType( ProgressLoadType.lookup( ProgressLoadType.SALES ) );
    file.setPromotion( promotion );
    Participant pax = new Participant();
    pax.setId( new Long( 5583 ) );

    PaxGoal paxGoal = new PaxGoal();
    paxGoal.setGoalLevel( new GoalLevel() );

    List importFiles = new ArrayList();
    ImportFile importFile1 = new ImportFile();
    importFile1.setPromotion( promotion );
    importFile1.setProgressEndDate( sdf.parse( "2007.10.18" ) );
    importFiles.add( importFile1 );
    ImportFileTypeType importFileType = ImportFileTypeType.lookup( ImportFileTypeType.GQ_PROGRESS_DATA_LOAD );
    ImportFileStatusType importFileStatusType = ImportFileStatusType.lookup( ImportFileStatusType.IMPORTED );

    EasyMock.expect( mockParticipantService.getParticipantByUserName( "bhd-001" ) ).andReturn( pax );
    EasyMock.expect( mockAudienceService.isParticipantInPrimaryAudience( promotion, pax ) ).andReturn( true );
    EasyMock.expect( mockParticipantService.getParticipantByUserName( "bhd-001" ) ).andReturn( pax );
    EasyMock.expect( mockPaxGoalService.getPaxGoalByPromotionIdAndUserId( promotion.getId(), pax.getId() ) ).andReturn( paxGoal );
    EasyMock.expect( mockPaxGoalService.getPaxGoalByPromotionIdAndUserId( promotion.getId(), pax.getId() ) ).andReturn( paxGoal );
    EasyMock.expect( mockImportService.getImportFiles( null, importFileType, importFileStatusType, null, null ) ).andReturn( importFiles );
    EasyMock.expect( mockImportService.getImportFiles( null, importFileType, importFileStatusType, null, null ) ).andReturn( importFiles );
    EasyMock.expect( mockAudienceService.isParticipantInPrimaryAudience( promotion, pax ) ).andReturn( true );

    control.replay();

    strategy.verifyImportFile( file, new ArrayList( file.getProgressImportRecords() ) );

    assertEquals( "ImportRecordErrorCount", 1, file.getImportRecordErrorCount() );
    assertEquals( "Number of record errors", 1, file.getImportRecordErrors().size() );

  }
}
