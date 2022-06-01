
package com.biperf.core.dao.ssi.hibernate;

import java.util.ArrayList;
import java.util.List;

import com.biperf.core.dao.ssi.SSIContestDAO;
import com.biperf.core.dao.ssi.SSIContestParticipantDAO;
import com.biperf.core.domain.enums.SSIContestStatus;
import com.biperf.core.domain.enums.SSIContestType;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.ssi.SSIContest;
import com.biperf.core.domain.ssi.SSIContestActivity;
import com.biperf.core.domain.ssi.SSIContestParticipant;
import com.biperf.core.domain.ssi.SSIContestParticipantProgress;
import com.biperf.core.domain.ssi.SSIContestParticipantStackRank;
import com.biperf.core.domain.ssi.SSIPromotion;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.value.ssi.SSIContestListValueBean;
import com.biperf.core.value.ssi.SSIContestParticipantProgressValueBean;

public class SSIContestParticipantDAOImplTest extends SSIBaseDAOTest
{

  public void testAllContestParticipantsStackRankByContestId()
  {

    // TODO Save feature of participant stack rank is unknown for now
    SSIPromotion promotion = buildSSIPromotionInstance();
    SSIContest contest = buildSSIContestInstance( promotion );
    SSIContestParticipant pax = buildSSIContestParticipantInstance( contest );
    saveContest( contest );

    saveContestParticipantStackRank( buildSSIContestParticipantStackRankInstance( contest, pax, null ) );

    List<SSIContestParticipantStackRank> actualContestParticipantStackRank = getSSIContestParticipantProgressDAO().getAllContestParticipantsStackRankByContestId( contest.getId(),
                                                                                                                                                                  1,
                                                                                                                                                                  20,
                                                                                                                                                                  "lastName",
                                                                                                                                                                  "asc" );
    assertNotNull( actualContestParticipantStackRank );
    assertTrue( actualContestParticipantStackRank.size() > 0 );
  }

  public void testGetContestParticipantStackRankByContestIdAndPaxId()
  {

    // TODO Save feature of participant stack rank is unknown for now
    SSIPromotion promotion = buildSSIPromotionInstance();
    SSIContest contest = buildSSIContestInstance( promotion );
    SSIContestParticipant pax = buildSSIContestParticipantInstance( contest );
    saveContest( contest );

    saveContestParticipantStackRank( buildSSIContestParticipantStackRankInstance( contest, pax, null ) );

    SSIContestParticipantStackRank actualContestParticipantStackRank = getSSIContestParticipantProgressDAO().getContestParticipantStackRankByContestIdAndPaxId( contest.getId(),
                                                                                                                                                                pax.getParticipant().getId() );
    assertNotNull( actualContestParticipantStackRank );

  }

  public void testGetContestParticipantProgressById()
  {
    SSIPromotion promotion = buildSSIPromotionInstance();
    SSIContest contest = buildSSIContestInstance( promotion );
    SSIContestParticipant pax = buildSSIContestParticipantInstance( contest );
    saveContest( contest );

    SSIContestParticipantProgress expected = buildSSIContestParticipantProgressInstance( contest, pax, null );
    saveContestParticipantProgress( expected );

    SSIContestParticipantProgress actual = getSSIContestParticipantProgressDAO().getContestParticipantProgressById( expected.getId() );
    assertEquals( expected.getId(), actual.getId() );
  }

  public void testGetContestParticipantsProgresses()
  {
    SSIPromotion promotion = buildSSIPromotionInstance();
    SSIContest contest = buildSSIContestInstance( promotion );
    SSIContestParticipant pax1 = buildSSIContestParticipantInstance( contest );
    SSIContestParticipant pax2 = buildSSIContestParticipantInstance( contest );
    saveContest( contest );

    saveContestParticipantProgress( buildSSIContestParticipantProgressInstance( contest, pax1, null ) );
    saveContestParticipantProgress( buildSSIContestParticipantProgressInstance( contest, pax2, null ) );
    List<Long> participantIds = new ArrayList<Long>();
    participantIds.add( pax1.getParticipant().getId() );
    participantIds.add( pax2.getParticipant().getId() );
    List<SSIContestParticipantProgress> progresslist = getSSIContestParticipantProgressDAO().getContestParticipantsProgresses( contest.getId(), participantIds );
    assertNotNull( progresslist );
    assertTrue( progresslist.size() == 2 );

  }

  public void testFetchContestParticipantsProgresses()
  {
    SSIPromotion promotion = buildSSIPromotionInstance();
    SSIContest contest = buildSSIContestInstance( promotion );
    contest.setContestType( SSIContestType.lookup( SSIContestType.OBJECTIVES ) );
    saveContest( contest );

    saveContestParticipantProgress( buildSSIContestParticipantProgressInstance( contest, contest.getContestParticipants().iterator().next(), null ) );

    List<SSIContestParticipantProgressValueBean> progresslist = getSSIContestParticipantProgressDAO().getContestParticipantsProgresses( contest.getId(), 1, 1, "participantName", "asc", false );

    assertNotNull( progresslist );
    assertTrue( progresslist.size() == 1 );
    SSIContestParticipantProgressValueBean progress = progresslist.get( 0 );
    assertNull( progress.getActivityId() );
    assertNotNull( progress.getActivityName() );
    assertNotNull( progress.getActivityAmount() );
    assertEquals( contest.getContestParticipants().iterator().next().getParticipant().getId(), progress.getPaxId() );

  }

  public void testFetchDTGTContestParticipantsProgresses()
  {
    SSIPromotion promotion = buildSSIPromotionInstance();
    SSIContest contest = buildSSIContestInstance( promotion );
    contest.setContestType( SSIContestType.lookup( SSIContestType.DO_THIS_GET_THAT ) );

    saveContest( contest );

    saveContestParticipantProgress( buildSSIContestParticipantProgressInstance( contest, contest.getContestParticipants().iterator().next(), contest.getContestActivities().iterator().next() ) );

    List<SSIContestParticipantProgressValueBean> progresslist = getSSIContestParticipantProgressDAO().getContestParticipantsProgresses( contest.getId(), 1, 1, "participantName", "asc", true );

    assertNotNull( progresslist );
    assertTrue( progresslist.size() == 1 );
    SSIContestParticipantProgressValueBean progress = progresslist.get( 0 );
    assertEquals( contest.getContestActivities().iterator().next().getId(), progress.getActivityId() );
    assertNotNull( progress.getActivityName() );
    assertNotNull( progress.getActivityAmount() );
    assertEquals( contest.getContestParticipants().iterator().next().getParticipant().getId(), progress.getPaxId() );

  }

  private void saveContestParticipantProgress( SSIContestParticipantProgress contestParticipantProgress )
  {
    getSSIContestParticipantProgressDAO().saveContestParticipantProgress( contestParticipantProgress );
    flushAndClearSession();

  }

  private void saveContestParticipantStackRank( SSIContestParticipantStackRank contestParticipantStackRank )
  {
    getSSIContestParticipantProgressDAO().saveContestParticipantsStackRank( contestParticipantStackRank );
    flushAndClearSession();

  }

  private void saveContest( SSIContest contest )
  {
    getSSIContestDAO().saveContest( contest );
    flushAndClearSession();

  }

  private SSIContestParticipant buildSSIContestParticipantInstance( SSIContest contest )
  {
    return createContestParticipant( contest );
  }

  private SSIContest buildSSIContestInstance( SSIPromotion promotion )
  {
    return createContest( promotion, buildUniqueString() );
  }

  private SSIPromotion buildSSIPromotionInstance()
  {
    SSIPromotion promotion = buildSSIPromotion();

    getPromotionDAO().save( promotion );
    flushAndClearSession();
    return promotion;
  }

  private SSIContestParticipantStackRank buildSSIContestParticipantStackRankInstance( SSIContest contest, SSIContestParticipant ssiContestParticipant, SSIContestActivity ssiContestActivity )
  {
    SSIContestParticipantStackRank ssiContestParticipantStackRank = new SSIContestParticipantStackRank();

    ssiContestParticipantStackRank.setAuditCreateInfo( contest.getAuditCreateInfo() );
    ssiContestParticipantStackRank.setAuditUpdateInfo( contest.getAuditUpdateInfo() );
    ssiContestParticipantStackRank.setContest( contest );
    ssiContestParticipantStackRank.setParticipant( ssiContestParticipant.getParticipant() );
    ssiContestParticipantStackRank.setStackRankPosition( 1L );
    ssiContestParticipantStackRank.setActivity( ssiContestActivity );
    ssiContestParticipantStackRank.setVersion( 1L );
    ssiContestParticipantStackRank.setId( 111L );

    return ssiContestParticipantStackRank;
  }

  private SSIContestParticipantProgress buildSSIContestParticipantProgressInstance( SSIContest contest, SSIContestParticipant ssiContestParticipant, SSIContestActivity contestActivity )
  {
    SSIContestParticipantProgress ssiContestParticipantProgress = new SSIContestParticipantProgress();
    ssiContestParticipantProgress.setActivityAmount( 1000.00 );
    ssiContestParticipantProgress.setActivityDate( DateUtils.getLastDayOfPreviousMonth() );
    ssiContestParticipantProgress.setAuditCreateInfo( contest.getAuditCreateInfo() );
    ssiContestParticipantProgress.setAuditUpdateInfo( contest.getAuditUpdateInfo() );
    ssiContestParticipantProgress.setContest( contest );
    ssiContestParticipantProgress.setParticipant( ssiContestParticipant.getParticipant() );
    ssiContestParticipantProgress.setContestActivity( contestActivity );

    return ssiContestParticipantProgress;
  }

  private SSIContestParticipantDAO getSSIContestParticipantProgressDAO()
  {
    return (SSIContestParticipantDAO)getDAO( SSIContestParticipantDAO.BEAN_NAME );
  }

  private SSIContestDAO getSSIContestDAO()
  {
    return (SSIContestDAO)getDAO( SSIContestDAO.BEAN_NAME );
  }

  public void testGetParticipantLiveContestsValueBean()
  {
    SSIPromotion promotion = buildSSIPromotion();
    getPromotionDAO().save( promotion );
    flushAndClearSession();

    String uniqueString = buildUniqueString();
    SSIContest liveContest = createContest( promotion, uniqueString );
    SSIContest closedContest = createContest( promotion, uniqueString );
    closedContest.setStatus( SSIContestStatus.lookup( SSIContestStatus.CLOSED ) );
    SSIContest pendingContest = createContest( promotion, uniqueString );
    pendingContest.setStatus( SSIContestStatus.lookup( SSIContestStatus.PENDING ) );

    Participant participant = createParticipant( uniqueString );

    addContestParticipant( liveContest, participant );
    addContestParticipant( closedContest, participant );
    addContestParticipant( pendingContest, participant );

    getSSIContestDAO().saveContest( liveContest );
    getSSIContestDAO().saveContest( closedContest );
    getSSIContestDAO().saveContest( pendingContest );
    flushAndClearSession();

    List<SSIContestListValueBean> contests = getSSIContestParticipantProgressDAO().getParticipantLiveContestsValueBean( participant.getId() );

    assertNotNull( contests );
    assertTrue( contests.size() == 1 );
    assertEquals( contests.get( 0 ).getStatus(), SSIContestStatus.LIVE );

  }

  public void testGetParticipantLiveContests()
  {
    SSIPromotion promotion = buildSSIPromotion();
    getPromotionDAO().save( promotion );
    flushAndClearSession();

    String uniqueString = buildUniqueString();
    SSIContest liveContest = createContest( promotion, uniqueString );
    SSIContest closedContest = createContest( promotion, uniqueString );
    closedContest.setStatus( SSIContestStatus.lookup( SSIContestStatus.CLOSED ) );
    SSIContest pendingContest = createContest( promotion, uniqueString );
    pendingContest.setStatus( SSIContestStatus.lookup( SSIContestStatus.PENDING ) );

    Participant participant = createParticipant( uniqueString );

    addContestParticipant( liveContest, participant );
    addContestParticipant( closedContest, participant );
    addContestParticipant( pendingContest, participant );

    getSSIContestDAO().saveContest( liveContest );
    getSSIContestDAO().saveContest( closedContest );
    getSSIContestDAO().saveContest( pendingContest );
    flushAndClearSession();

    List<SSIContest> contests = getSSIContestParticipantProgressDAO().getParticipantLiveContests( participant.getId() );

    assertNotNull( contests );
    assertTrue( contests.size() == 1 );
    assertEquals( contests.get( 0 ).getStatus().getCode(), SSIContestStatus.LIVE );

  }

  public void testGetArchievedContests()
  {
    SSIPromotion promotion = buildSSIPromotion();
    getPromotionDAO().save( promotion );
    flushAndClearSession();

    String uniqueString = buildUniqueString();
    SSIContest liveContest = createContest( promotion, uniqueString );
    SSIContest closedContest = createContest( promotion, uniqueString );
    closedContest.setStatus( SSIContestStatus.lookup( SSIContestStatus.CLOSED ) );
    SSIContest pendingContest = createContest( promotion, uniqueString );
    pendingContest.setStatus( SSIContestStatus.lookup( SSIContestStatus.PENDING ) );

    Participant participant = createParticipant( uniqueString );
    addContestParticipant( liveContest, participant );
    addContestParticipant( closedContest, participant );
    addContestParticipant( pendingContest, participant );

    getSSIContestDAO().saveContest( liveContest );
    getSSIContestDAO().saveContest( closedContest );
    getSSIContestDAO().saveContest( pendingContest );
    flushAndClearSession();

    List<SSIContestListValueBean> contests = getSSIContestParticipantProgressDAO().getParticipantArchivedContests( participant.getId() );

    assertNotNull( contests );
    assertTrue( contests.size() == 1 );
    assertEquals( contests.get( 0 ).getStatus(), SSIContestStatus.CLOSED );
  }

  public void testIsPaxContestCreator()
  {
    SSIPromotion promotion = buildSSIPromotion();
    getPromotionDAO().save( promotion );
    flushAndClearSession();

    String uniqueString = buildUniqueString();
    SSIContest expectedContest = createContest( promotion, uniqueString );
    getSSIContestDAO().saveContest( expectedContest );
    flushAndClearSession();

    assertTrue( getSSIContestParticipantProgressDAO().isPaxContestCreator( CONTEST_CREATOR_ID ) );
  }

  public void testIsPaxInContestPaxAudience()
  {
    SSIPromotion promotion = buildSSIPromotion();
    getPromotionDAO().save( promotion );
    flushAndClearSession();

    String uniqueString = buildUniqueString();
    SSIContest expectedContest = createContest( promotion, uniqueString );
    getSSIContestDAO().saveContest( expectedContest );
    flushAndClearSession();

    Participant participant = createParticipant( uniqueString );
    addContestParticipant( expectedContest, participant );
    getSSIContestDAO().saveContest( expectedContest );
    flushAndClearSession();

    boolean isPaxInContestPaxAudience = getSSIContestParticipantProgressDAO().isPaxInContestPaxAudience( participant.getId() );

    assertNotNull( expectedContest );
    assertTrue( expectedContest.getContestParticipants().size() > 0 );
    assertTrue( isPaxInContestPaxAudience );

  }

  public void testIsPaxInContestManagerAudience()
  {
    SSIPromotion promotion = buildSSIPromotion();
    getPromotionDAO().save( promotion );
    flushAndClearSession();

    String uniqueString = buildUniqueString();
    SSIContest expectedContest = createContest( promotion, uniqueString );
    getSSIContestDAO().saveContest( expectedContest );
    flushAndClearSession();

    Participant participant = createParticipant( uniqueString );
    addContestManager( expectedContest, participant );
    getSSIContestDAO().saveContest( expectedContest );
    flushAndClearSession();

    boolean isPaxInContestManagerAudience = getSSIContestParticipantProgressDAO().isPaxInContestManagerAudience( participant.getId() );

    assertNotNull( expectedContest );
    assertTrue( expectedContest.getContestManagers().size() > 0 );
    assertTrue( isPaxInContestManagerAudience );

  }

}
