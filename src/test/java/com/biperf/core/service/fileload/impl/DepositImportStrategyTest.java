
package com.biperf.core.service.fileload.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.easymock.EasyMock;

import com.biperf.core.domain.enums.ImportFileStatusType;
import com.biperf.core.domain.enums.ImportFileTypeType;
import com.biperf.core.domain.enums.MockPickListFactory;
import com.biperf.core.domain.enums.ParticipantStatus;
import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.domain.enums.SecondaryAudienceType;
import com.biperf.core.domain.fileload.DepositImportRecord;
import com.biperf.core.domain.fileload.ImportFile;
import com.biperf.core.domain.fileload.ImportRecordError;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.RecognitionPromotion;
import com.biperf.core.service.BaseServiceTest;
import com.biperf.core.service.journal.JournalService;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.promotion.PromotionService;

/**
 * DepositImportStrategyTest.
 * <p>
 * <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Can't test importImportFile method with Mocks due to GUID issues.</th>
 * </tr>
 * <tr>
 * <td>Travis Jenniges</td>
 * <td>Sep 27, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class DepositImportStrategyTest extends BaseServiceTest
{
  private JournalService journalServiceMock;
  private ParticipantService participantServiceMock;
  private PromotionService promotionServiceMock;

  private DepositImportStrategy importStrategyUnderTest;

  public void setUp() throws Exception
  {
    super.setUp();
    importStrategyUnderTest = new DepositImportStrategy();

    journalServiceMock = EasyMock.createMock( JournalService.class );
    importStrategyUnderTest.setJournalService( journalServiceMock );

    participantServiceMock = EasyMock.createMock( ParticipantService.class );
    importStrategyUnderTest.setParticipantService( participantServiceMock );

    promotionServiceMock = EasyMock.createMock( PromotionService.class );
    importStrategyUnderTest.setPromotionService( promotionServiceMock );
  }

  public void testVerifyCleanImportFile()
  {
    ImportFile file = buildCleanDepositImportFile();
    file.setStatus( (ImportFileStatusType)MockPickListFactory.getMockPickListItem( ImportFileStatusType.class, ImportFileStatusType.VERIFY_IN_PROCESS ) );
    assertTrue( file.getImportRecordErrors().isEmpty() );
    Participant pax = new Participant();
    Set<Participant> paxs = new HashSet<>( 1 );
    pax.setId( new Long( 500 ) );
    pax.setStatus( (ParticipantStatus)MockPickListFactory.getMockPickListItem( ParticipantStatus.class, "active" ) );
    paxs.add( pax );

    // participantServiceControl.expectAndReturn( participantServiceMock
    // .getAllEligiblePaxForPromotion(file.getPromotion().getId(),false), paxs );

    EasyMock.expect( participantServiceMock.isAllActivePax( file.getPromotion(), false ) ).andReturn( true );

    for ( Iterator iter = file.getDepositImportRecords().iterator(); iter.hasNext(); )
    {
      DepositImportRecord record = (DepositImportRecord)iter.next();
      EasyMock.expect( participantServiceMock.getParticipantByUserName( record.getUserName() ) ).andReturn( pax );
    }
    EasyMock.replay( participantServiceMock );
    EasyMock.replay( promotionServiceMock );
    importStrategyUnderTest.verifyImportFile( file, new ArrayList( file.getDepositImportRecords() ) );
    EasyMock.verify( participantServiceMock );
    EasyMock.verify( promotionServiceMock );

    assertTrue( file.getImportRecordErrors().isEmpty() );
  }

  // public void testVerifyDirtyImportFile()
  // {
  // ImportFile file = buildBadDepositImportFile();
  // file.setStatus( (ImportFileStatusType)MockPickListFactory
  // .getMockPickListItem( ImportFileStatusType.class, ImportFileStatusType.VERIFY_IN_PROCESS ) );
  // assertTrue( file.getImportRecordErrors().isEmpty() );
  //
  // int i = 0;
  // for ( Iterator iter = file.getDepositImportRecords().iterator(); iter.hasNext(); )
  // {
  // DepositImportRecord record = (DepositImportRecord)iter.next();
  // if ( i == 0 )
  // {
  // Participant pax = new Participant();
  // Set paxs = new HashSet( 1 );
  // pax.setStatus( (ParticipantStatus)MockPickListFactory
  // .getMockPickListItem( ParticipantStatus.class, "active" ) );
  // paxs.add( pax );
  // participantServiceControl.expectAndReturn( participantServiceMock
  // .getParticipantByUserName( record.getUserName() ), pax );
  //// participantServiceControl.expectAndReturn( participantServiceMock
  //// .getAllEligiblePaxForPromotion( file.getPromotion().getId(), false ), paxs );
  // promotionServiceControl.expectAndReturn( promotionServiceMock
  // .isParticipantMemberOfPromotionAudience(pax,file.getPromotion(),false,null), true );
  // }
  // else if ( i == 1 )
  // {
  // Participant pax = new Participant();
  // Set paxs = new HashSet( 1 );
  // pax.setStatus( (ParticipantStatus)MockPickListFactory
  // .getMockPickListItem( ParticipantStatus.class, "active" ) );
  // paxs.add( pax );
  // participantServiceControl.expectAndReturn( participantServiceMock
  // .getParticipantByUserName( record.getUserName() ), null );
  //// participantServiceControl.expectAndReturn( participantServiceMock
  //// .getAllEligiblePaxForPromotion( file.getPromotion().getId(), false ), paxs );
  // promotionServiceControl.expectAndReturn( promotionServiceMock
  // .isParticipantMemberOfPromotionAudience(pax,file.getPromotion(),false,null), true );
  // }
  // else if ( i == 2 )
  // {
  // Set paxs = new HashSet( 1 );
  // Participant pax1 = new Participant();
  // pax1.setStatus( (ParticipantStatus)MockPickListFactory
  // .getMockPickListItem( ParticipantStatus.class, "inactive" ) );
  // paxs.add( pax1 );
  // participantServiceControl.expectAndReturn( participantServiceMock
  // .getParticipantByUserName( record.getUserName() ), pax1 );
  //// participantServiceControl.expectAndReturn( participantServiceMock
  //// .getAllEligiblePaxForPromotion( file.getPromotion().getId(), false ), paxs );
  // promotionServiceControl.expectAndReturn( promotionServiceMock
  // .isParticipantMemberOfPromotionAudience(pax1,file.getPromotion(),false,null), false );
  // }
  // else
  // {
  // Set paxs = new HashSet( 1 );
  // Participant pax2 = new Participant();
  // pax2.setStatus( (ParticipantStatus)MockPickListFactory
  // .getMockPickListItem( ParticipantStatus.class, "active" ) );
  // pax2.setSuspensionStatus( (ParticipantSuspensionStatus)MockPickListFactory
  // .getMockPickListItem( ParticipantSuspensionStatus.class, "susdep" ) );
  // paxs.add( pax2 );
  // participantServiceControl.expectAndReturn( participantServiceMock
  // .getParticipantByUserName( record.getUserName() ), pax2 );
  //// participantServiceControl.expectAndReturn( participantServiceMock
  //// .getAllEligiblePaxForPromotion( file.getPromotion().getId(), false ), paxs );
  // promotionServiceControl.expectAndReturn( promotionServiceMock
  // .isParticipantMemberOfPromotionAudience(pax2,file.getPromotion(),false,null), false );
  // }
  // i++;
  // }
  // participantServiceControl.replay();
  // promotionServiceControl.replay();
  // importStrategyUnderTest
  // .verifyImportFile( file, new ArrayList( file.getDepositImportRecords() ) );
  // participantServiceControl.verify();
  // promotionServiceControl.verify();
  //
  // assertFalse( file.getImportRecordErrors().isEmpty() );
  // assertEquals( 4, file.getImportRecordErrorCount() );
  // }

  public static ImportFile buildCleanDepositImportFile()
  {
    ImportFile file = new ImportFile();
    file.setFileType( (ImportFileTypeType)MockPickListFactory.getMockPickListItem( ImportFileTypeType.class, ImportFileTypeType.DEPOSIT ) );
    Set<DepositImportRecord> records = new HashSet<>( 4 );
    file.setDepositImportRecords( records );

    DepositImportRecord record = new DepositImportRecord();
    record.setActionType( "add" );
    record.setAwardAmount( new BigDecimal( 10 ) );
    record.setUserName( "Z" );
    records.add( record );

    DepositImportRecord record1 = new DepositImportRecord();
    record1.setActionType( "add" );
    record1.setAwardAmount( new BigDecimal( 10 ) );
    record1.setUserName( "Z" );
    records.add( record1 );

    DepositImportRecord record2 = new DepositImportRecord();
    record2.setActionType( "add" );
    record2.setAwardAmount( new BigDecimal( 10 ) );
    record2.setUserName( "Z" );
    records.add( record2 );

    DepositImportRecord record3 = new DepositImportRecord();
    record3.setActionType( "add" );
    record3.setAwardAmount( new BigDecimal( 10 ) );
    record3.setUserName( "Z" );
    records.add( record3 );

    PromotionType promotionType = PromotionType.lookup( PromotionType.RECOGNITION );
    RecognitionPromotion promotion = new RecognitionPromotion();
    promotion.setAwardActive( true );
    promotion.setPromotionType( promotionType );
    promotion.setAwardActive( true );
    promotion.setAwardAmountTypeFixed( true );
    promotion.setAwardAmountFixed( new Long( 10 ) );
    promotion.setSecondaryAudienceType( SecondaryAudienceType.lookup( SecondaryAudienceType.ALL_ACTIVE_PAX_CODE ) );
    file.setPromotion( promotion );

    return file;
  }

  public static ImportFile buildBadDepositImportFile()
  {
    ImportFile file = new ImportFile();
    file.setFileType( (ImportFileTypeType)MockPickListFactory.getMockPickListItem( ImportFileTypeType.class, ImportFileTypeType.DEPOSIT ) );
    Set<DepositImportRecord> records = new HashSet<>( 4 );
    file.setDepositImportRecords( records );

    DepositImportRecord record = new DepositImportRecord();
    record.setActionType( "add" );
    record.setAwardAmount( new BigDecimal( 10 ) );
    record.setUserName( "Z" );
    records.add( record );

    DepositImportRecord record1 = new DepositImportRecord();
    record1.setActionType( "add" );
    record1.setAwardAmount( new BigDecimal( 10 ) );
    record1.setUserName( "Travis" ); // unknown user name
    records.add( record1 );

    DepositImportRecord record2 = new DepositImportRecord();
    record2.setActionType( "add" );
    record2.setAwardAmount( new BigDecimal( 10 ) );
    record2.setUserName( "Y" ); // inactive user
    records.add( record2 );

    DepositImportRecord record3 = new DepositImportRecord();
    record3.setActionType( "add" );
    record3.setAwardAmount( new BigDecimal( 10 ) );
    record3.setUserName( "X" ); // user on whom deposits are suspended
    records.add( record3 );
    // fixed amount is different
    PromotionType promotionType = PromotionType.lookup( PromotionType.RECOGNITION );
    RecognitionPromotion promotion = new RecognitionPromotion();
    promotion.setAwardActive( true );
    promotion.setPromotionType( promotionType );
    promotion.setAwardActive( true );
    promotion.setAwardAmountTypeFixed( true );
    promotion.setAwardAmountFixed( new Long( 12 ) );
    promotion.setSecondaryAudienceType( SecondaryAudienceType.lookup( SecondaryAudienceType.ALL_ACTIVE_PAX_CODE ) );
    file.setPromotion( promotion );

    return file;
  }

  public static void addErrors( ImportFile file )
  {
    Set<ImportRecordError> errors = new HashSet<>( 1 );
    errors.add( new ImportRecordError() );
    for ( Iterator iter = file.getDepositImportRecords().iterator(); iter.hasNext(); )
    {
      DepositImportRecord record = (DepositImportRecord)iter.next();
      record.setImportRecordErrors( errors );
    }
  }

  public static ImportFile buildCleanDepositImportFileWithVarAmountPromotionInRange()
  {
    ImportFile file = new ImportFile();
    file.setFileType( (ImportFileTypeType)MockPickListFactory.getMockPickListItem( ImportFileTypeType.class, ImportFileTypeType.DEPOSIT ) );
    Set<DepositImportRecord> records = new HashSet<>( 2 );
    file.setDepositImportRecords( records );

    DepositImportRecord record = new DepositImportRecord();
    record.setActionType( "add" );
    record.setAwardAmount( new BigDecimal( 10 ) );
    record.setUserName( "Z" );
    records.add( record );

    DepositImportRecord record1 = new DepositImportRecord();
    record1.setActionType( "add" );
    record1.setAwardAmount( new BigDecimal( 10 ) );
    record1.setUserName( "Z" );
    records.add( record1 );

    PromotionType promotionType = PromotionType.lookup( PromotionType.RECOGNITION );
    RecognitionPromotion promotion = new RecognitionPromotion();
    promotion.setAwardActive( true );
    promotion.setPromotionType( promotionType );
    promotion.setAwardActive( true );
    promotion.setAwardAmountTypeFixed( false );
    promotion.setAwardAmountMin( new Long( 5 ) );
    promotion.setAwardAmountMax( new Long( 15 ) );
    promotion.setSecondaryAudienceType( SecondaryAudienceType.lookup( SecondaryAudienceType.ALL_ACTIVE_PAX_CODE ) );
    file.setPromotion( promotion );

    return file;
  }

  public static ImportFile buildBadDepositImportFileWithVarAmountPromotionNotInRage()
  {
    ImportFile file = new ImportFile();
    file.setFileType( (ImportFileTypeType)MockPickListFactory.getMockPickListItem( ImportFileTypeType.class, ImportFileTypeType.DEPOSIT ) );
    Set<DepositImportRecord> records = new HashSet<>( 2 );
    file.setDepositImportRecords( records );

    DepositImportRecord record = new DepositImportRecord();
    record.setActionType( "add" );
    record.setAwardAmount( new BigDecimal( 10 ) );
    record.setUserName( "Z" );
    records.add( record );

    DepositImportRecord record1 = new DepositImportRecord();
    record1.setActionType( "add" );
    record1.setAwardAmount( new BigDecimal( 10 ) );
    record1.setUserName( "Travis" ); // unknown user name
    records.add( record1 );

    // fixed amount is different
    PromotionType promotionType = PromotionType.lookup( PromotionType.RECOGNITION );
    RecognitionPromotion promotion = new RecognitionPromotion();
    promotion.setAwardActive( true );
    promotion.setPromotionType( promotionType );
    promotion.setAwardActive( true );
    promotion.setAwardAmountTypeFixed( false );
    promotion.setAwardAmountMin( new Long( 5 ) );
    promotion.setAwardAmountMax( new Long( 9 ) );
    promotion.setSecondaryAudienceType( SecondaryAudienceType.lookup( SecondaryAudienceType.ALL_ACTIVE_PAX_CODE ) );
    file.setPromotion( promotion );

    return file;
  }

  public void testVerifyCleanImportFileWithVarAmountPromotionInRange()
  {
    ImportFile file = buildCleanDepositImportFileWithVarAmountPromotionInRange();
    file.setStatus( (ImportFileStatusType)MockPickListFactory.getMockPickListItem( ImportFileStatusType.class, ImportFileStatusType.VERIFY_IN_PROCESS ) );
    assertTrue( file.getImportRecordErrors().isEmpty() );

    Set<Participant> paxs = new HashSet<>( 1 );
    Participant pax = new Participant();
    pax.setId( new Long( 500 ) );
    pax.setStatus( (ParticipantStatus)MockPickListFactory.getMockPickListItem( ParticipantStatus.class, "active" ) );
    paxs.add( pax );

    // participantServiceControl.expectAndReturn( participantServiceMock
    // .getAllEligiblePaxForPromotion(file.getPromotion().getId(), false), paxs );

    EasyMock.expect( participantServiceMock.isAllActivePax( file.getPromotion(), false ) ).andReturn( true );

    for ( Iterator iter = file.getDepositImportRecords().iterator(); iter.hasNext(); )
    {
      DepositImportRecord record = (DepositImportRecord)iter.next();
      EasyMock.expect( participantServiceMock.getParticipantByUserName( record.getUserName() ) ).andReturn( pax );
    }
    EasyMock.replay( participantServiceMock );
    EasyMock.replay( promotionServiceMock );
    importStrategyUnderTest.verifyImportFile( file, new ArrayList( file.getDepositImportRecords() ) );
    EasyMock.verify( participantServiceMock );
    EasyMock.verify( promotionServiceMock );

    assertTrue( file.getImportRecordErrors().isEmpty() );
  }

  // public void testVerifyDirtyImportFileWithVarAmountPromotionNotInRage()
  // {
  // ImportFile file = buildBadDepositImportFileWithVarAmountPromotionNotInRage();
  // file.setStatus( (ImportFileStatusType)MockPickListFactory
  // .getMockPickListItem( ImportFileStatusType.class, ImportFileStatusType.VERIFY_IN_PROCESS ) );
  // assertTrue( file.getImportRecordErrors().isEmpty() );
  //
  // int i = 0;
  // for ( Iterator iter = file.getDepositImportRecords().iterator(); iter.hasNext(); )
  // {
  // DepositImportRecord record = (DepositImportRecord)iter.next();
  // if ( i == 0 )
  // {
  // Set paxs = new HashSet( 1 );
  // Participant pax = new Participant();
  // pax.setStatus( (ParticipantStatus)MockPickListFactory
  // .getMockPickListItem( ParticipantStatus.class, "active" ) );
  // paxs.add( pax );
  // participantServiceControl.expectAndReturn( participantServiceMock
  // .getParticipantByUserName( record.getUserName() ), pax );
  //// participantServiceControl.expectAndReturn( participantServiceMock
  //// .getAllEligiblePaxForPromotion( file.getPromotion().getId(), false ), paxs );
  // promotionServiceControl.expectAndReturn( promotionServiceMock
  // .isParticipantMemberOfPromotionAudience(pax,file.getPromotion(),false,null), true );
  // }
  // else if ( i == 1 )
  // {
  // Set paxs = new HashSet( 1 );
  // Participant pax = new Participant();
  // pax.setStatus( (ParticipantStatus)MockPickListFactory
  // .getMockPickListItem( ParticipantStatus.class, "active" ) );
  // paxs.add( pax );
  // participantServiceControl.expectAndReturn( participantServiceMock
  // .getParticipantByUserName( record.getUserName() ), null );
  //// participantServiceControl.expectAndReturn( participantServiceMock
  //// .getAllEligiblePaxForPromotion( file.getPromotion().getId(), false ), paxs );
  // promotionServiceControl.expectAndReturn( promotionServiceMock
  // .isParticipantMemberOfPromotionAudience(pax,file.getPromotion(),false,null), true );
  // }
  // else if ( i == 2 )
  // {
  // Set paxs = new HashSet( 1 );
  // Participant pax1 = new Participant();
  // pax1.setStatus( (ParticipantStatus)MockPickListFactory
  // .getMockPickListItem( ParticipantStatus.class, "inactive" ) );
  // paxs.add( pax1 );
  // participantServiceControl.expectAndReturn( participantServiceMock
  // .getParticipantByUserName( record.getUserName() ), pax1 );
  //// participantServiceControl.expectAndReturn( participantServiceMock
  //// .getAllEligiblePaxForPromotion( file.getPromotion().getId(), false ), paxs );
  // promotionServiceControl.expectAndReturn( promotionServiceMock
  // .isParticipantMemberOfPromotionAudience(pax1,file.getPromotion(),false,null), false );
  // }
  // else
  // {
  // Set paxs = new HashSet( 1 );
  // Participant pax2 = new Participant();
  // pax2.setStatus( (ParticipantStatus)MockPickListFactory
  // .getMockPickListItem( ParticipantStatus.class, "active" ) );
  // pax2.setSuspensionStatus( (ParticipantSuspensionStatus)MockPickListFactory
  // .getMockPickListItem( ParticipantSuspensionStatus.class, "susdep" ) );
  // paxs.add( pax2 );
  // participantServiceControl.expectAndReturn( participantServiceMock
  // .getParticipantByUserName( record.getUserName() ), pax2 );
  //// participantServiceControl.expectAndReturn( participantServiceMock
  //// .getAllEligiblePaxForPromotion( file.getPromotion().getId(), false ), paxs );
  // promotionServiceControl.expectAndReturn( promotionServiceMock
  // .isParticipantMemberOfPromotionAudience(pax2,file.getPromotion(),false,null), false );
  // }
  // i++;
  // }
  // participantServiceControl.replay();
  // promotionServiceControl.replay();
  // importStrategyUnderTest
  // .verifyImportFile( file, new ArrayList( file.getDepositImportRecords() ) );
  // participantServiceControl.verify();
  // promotionServiceControl.verify();
  //
  // assertFalse( file.getImportRecordErrors().isEmpty() );
  // assertEquals( 2, file.getImportRecordErrorCount() );
  // }
}