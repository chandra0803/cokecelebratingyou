
package com.biperf.core.service.fileload.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.easymock.EasyMock;

import com.biperf.core.domain.enums.ImportFileStatusType;
import com.biperf.core.domain.enums.ImportFileTypeType;
import com.biperf.core.domain.enums.MockPickListFactory;
import com.biperf.core.domain.fileload.ImportFile;
import com.biperf.core.domain.fileload.ImportRecordError;
import com.biperf.core.domain.fileload.LeaderBoardImportRecord;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.user.User;
import com.biperf.core.service.BaseServiceTest;
import com.biperf.core.service.leaderboard.LeaderBoardService;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.participant.UserService;

public class LeaderBoardImportStrategyTest extends BaseServiceTest
{
  private LeaderBoardImportStrategy importStrategyUnderTest;
  private LeaderBoardService LeaderBoardServiceMock;
  private ParticipantService participantServiceMock;

  private UserService userServiceMock;

  public void setUp() throws Exception
  {
    super.setUp();
    importStrategyUnderTest = new LeaderBoardImportStrategy();

    LeaderBoardServiceMock = EasyMock.createMock( LeaderBoardService.class );
    importStrategyUnderTest.setLeaderBoardService( LeaderBoardServiceMock );

    userServiceMock = EasyMock.createMock( UserService.class );
    importStrategyUnderTest.setUserService( userServiceMock );

    participantServiceMock = EasyMock.createMock( ParticipantService.class );
    importStrategyUnderTest.setParticipantService( participantServiceMock );
  }

  public static ImportFile buildCleanLeaderBoardImportFile()
  {
    ImportFile file = new ImportFile();
    file.setFileType( (ImportFileTypeType)MockPickListFactory.getMockPickListItem( ImportFileTypeType.class, ImportFileTypeType.LEADERBOARD ) );
    Set<LeaderBoardImportRecord> records = new HashSet<>( 4 );
    file.setLeaderBoardImportRecords( records );

    LeaderBoardImportRecord record = new LeaderBoardImportRecord();
    // record.setUserId( new LeaderBoard() );
    record.setUserName( "BI-ADMIN" );
    record.setLeaderBoardName( "Sales" );
    record.setScore( new Long( 10 ) );
    records.add( record );

    LeaderBoardImportRecord record1 = new LeaderBoardImportRecord();
    // record.setUserId( new LeaderBoard()) ;
    record1.setUserName( "BHD-070" );
    record1.setLeaderBoardName( "X" );
    record1.setScore( new Long( 15 ) );
    records.add( record1 );

    LeaderBoardImportRecord record2 = new LeaderBoardImportRecord();
    // record.setUserId( new LeaderBoard()) ;
    record2.setUserName( "BHD-001" );
    record2.setLeaderBoardName( "Sales" );
    record2.setScore( new Long( 20 ) );
    records.add( record2 );

    LeaderBoardImportRecord record3 = new LeaderBoardImportRecord();
    // record3.setUserId( new LeaderBoard());
    record3.setUserName( "BHD-003" );
    record3.setLeaderBoardName( "X" );
    record3.setScore( new Long( 13 ) );
    records.add( record3 );

    return file;
  }

  public static ImportFile buildBadLeaderBoardImportFile()
  {
    ImportFile file = new ImportFile();
    file.setFileType( (ImportFileTypeType)MockPickListFactory.getMockPickListItem( ImportFileTypeType.class, ImportFileTypeType.LEADERBOARD ) );
    Set<LeaderBoardImportRecord> records = new HashSet<>( 4 );
    file.setLeaderBoardImportRecords( records );

    LeaderBoardImportRecord record = new LeaderBoardImportRecord();
    record.setAsOfDate( new Date() );
    record.setAction( new Long( 1 ) );
    record.setLeaderBoardName( "Sales" );
    record.setScore( new Long( 10 ) );
    records.add( record );

    LeaderBoardImportRecord record1 = new LeaderBoardImportRecord();
    record1.setAsOfDate( new Date() );
    record1.setAction( new Long( 1 ) );
    record1.setLeaderBoardName( "Sales" );
    record1.setScore( new Long( 15 ) );
    records.add( record1 );

    LeaderBoardImportRecord record2 = new LeaderBoardImportRecord();
    record2.setAsOfDate( new Date() );
    record2.setAction( new Long( 1 ) );
    record2.setLeaderBoardName( "X" );
    record2.setScore( new Long( 110 ) );
    records.add( record2 );

    LeaderBoardImportRecord record3 = new LeaderBoardImportRecord();
    record3.setAsOfDate( new Date() );
    record3.setAction( new Long( 1 ) );
    record3.setLeaderBoardName( "Y" );
    record3.setScore( new Long( 111 ) );
    records.add( record3 );

    return file;
  }

  public static void addErrors( ImportFile file )
  {
    Set<ImportRecordError> errors = new HashSet<>( 1 );
    errors.add( new ImportRecordError() );
    for ( Iterator iter = file.getLeaderBoardImportRecords().iterator(); iter.hasNext(); )
    {
      LeaderBoardImportRecord record = (LeaderBoardImportRecord)iter.next();
      record.setImportRecordErrors( errors );
    }
  }

  public void testVerifyCleanImportFile()
  {
    ImportFile file = buildCleanLeaderBoardImportFile();
    file.setStatus( (ImportFileStatusType)MockPickListFactory.getMockPickListItem( ImportFileStatusType.class, ImportFileStatusType.VERIFY_IN_PROCESS ) );
    assertTrue( file.getImportRecordErrors().isEmpty() );
    User user = new User();

    Participant pax = new Participant();
    Set<Participant> paxs = new HashSet<>( 1 );
    paxs.add( pax );

    for ( Iterator iter = file.getLeaderBoardImportRecords().iterator(); iter.hasNext(); )
    {
      LeaderBoardImportRecord record = (LeaderBoardImportRecord)iter.next();
      if ( record.getUserName() != null )
      {
        EasyMock.expect( participantServiceMock.getParticipantByUserName( record.getUserName() ) ).andReturn( pax );
        EasyMock.expect( userServiceMock.getUserByUserName( record.getUserName() ) ).andReturn( user );
      }

    }
    EasyMock.replay( participantServiceMock );
    EasyMock.replay( userServiceMock );

    importStrategyUnderTest.verifyImportFile( file, new ArrayList( file.getLeaderBoardImportRecords() ) );
    // participantServiceControl.verify();

    assertTrue( file.getImportRecordErrors().size() > 0 );
  }

  public void testVerifyBadImportFile()
  {
    ImportFile file = buildBadLeaderBoardImportFile();
    file.setStatus( (ImportFileStatusType)MockPickListFactory.getMockPickListItem( ImportFileStatusType.class, ImportFileStatusType.VERIFY_IN_PROCESS ) );
    assertTrue( file.getImportRecordErrors().isEmpty() );
    Participant pax = new Participant();
    Set<Participant> paxs = new HashSet<>( 1 );
    paxs.add( pax );
    for ( Iterator iter = file.getLeaderBoardImportRecords().iterator(); iter.hasNext(); )
    {

      LeaderBoardImportRecord record = (LeaderBoardImportRecord)iter.next();

      if ( record.getAsOfDate() != null )
      {
        if ( record.getAsOfDate() == new Date() )
        {
          EasyMock.expect( participantServiceMock.getParticipantByUserName( record.getLeaderBoardName() ) ).andReturn( pax );
        }
      }
    }

    EasyMock.replay( participantServiceMock );

    importStrategyUnderTest.verifyImportFile( file, new ArrayList( file.getLeaderBoardImportRecords() ) );

    EasyMock.verify( participantServiceMock );

    assertFalse( file.getImportRecordErrors().isEmpty() );
    assertEquals( 4, file.getImportRecordErrorCount() );
  }

}
