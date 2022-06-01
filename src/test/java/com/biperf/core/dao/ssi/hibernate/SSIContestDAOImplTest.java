
package com.biperf.core.dao.ssi.hibernate;

import static org.apache.commons.collections.CollectionUtils.isNotEmpty;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.biperf.core.dao.gamification.GamificationDAO;
import com.biperf.core.dao.ssi.SSIContestDAO;
import com.biperf.core.domain.claim.ClaimFormStepElement;
import com.biperf.core.domain.enums.SSIApproverType;
import com.biperf.core.domain.enums.SSIBillPayoutCodeType;
import com.biperf.core.domain.enums.SSIContestStatus;
import com.biperf.core.domain.enums.SSIContestType;
import com.biperf.core.domain.gamification.BadgeRule;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.PromotionBillCode;
import com.biperf.core.domain.ssi.SSIContest;
import com.biperf.core.domain.ssi.SSIContestActivity;
import com.biperf.core.domain.ssi.SSIContestApprover;
import com.biperf.core.domain.ssi.SSIContestBillCode;
import com.biperf.core.domain.ssi.SSIContestClaimField;
import com.biperf.core.domain.ssi.SSIContestLevel;
import com.biperf.core.domain.ssi.SSIContestManager;
import com.biperf.core.domain.ssi.SSIContestParticipant;
import com.biperf.core.domain.ssi.SSIContestStackRankPayout;
import com.biperf.core.domain.ssi.SSIPromotion;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.ssi.SSIContestAssociationRequest;
import com.biperf.core.utils.ApplicationContextFactory;
import com.biperf.core.value.ssi.SSIContestBillCodeBean;
import com.biperf.core.value.ssi.SSIContestListValueBean;
import com.biperf.core.value.ssi.SSIContestParticipantValueBean;

/**
 * 
 * SSIDAOImplTest.
 * 
 * @author kandhi
 * @since Oct 22, 2014
 * @version 1.0
 */
public class SSIContestDAOImplTest extends SSIBaseDAOTest
{

  private static SSIContestDAO getSSIContestDAO()
  {
    return (SSIContestDAO)getDAO( SSIContestDAO.BEAN_NAME );
  }

  public void testSaveContest()
  {
    SSIPromotion promotion = buildSSIPromotion();
    getPromotionDAO().save( promotion );
    flushAndClearSession();

    String uniqueString = buildUniqueString();
    SSIContest expectedContest = createContest( promotion, uniqueString );
    getSSIContestDAO().saveContest( expectedContest );
    flushAndClearSession();

    SSIContest contestActual = getSSIContestDAO().getContestById( expectedContest.getId() );
    assertEquals( expectedContest.getId(), contestActual.getId() );
  }

  public void testGetContestById()
  {
    SSIPromotion promotion = buildSSIPromotion();
    getPromotionDAO().save( promotion );
    flushAndClearSession();

    String uniqueString = buildUniqueString();
    SSIContest expectedContest = createContest( promotion, uniqueString );
    getSSIContestDAO().saveContest( expectedContest );
    flushAndClearSession();

    SSIContest contestActual = getSSIContestDAO().getContestById( expectedContest.getId() );
    assertEquals( expectedContest.getId(), contestActual.getId() );
  }

  public void testGetContestByIdWithAssociations()
  {
    SSIPromotion promotion = buildSSIPromotion();
    getPromotionDAO().save( promotion );
    flushAndClearSession();

    String uniqueString = buildUniqueString();
    SSIContest expectedContest = createContest( promotion, uniqueString );
    getSSIContestDAO().saveContest( expectedContest );
    flushAndClearSession();

    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( new SSIContestAssociationRequest( SSIContestAssociationRequest.ALL ) );
    SSIContest contestActual = getSSIContestDAO().getContestByIdWithAssociations( expectedContest.getId(), associationRequestCollection );

    assertNotNull( contestActual.getContestLevel1Approvers() );
    assertNotNull( contestActual.getContestLevel2Approvers() );
    assertNotNull( contestActual.getClaimApprovers() );
    assertNotNull( contestActual.getContestManagers() );
    assertNotNull( contestActual.getContestParticipants() );
    assertNotNull( contestActual.getContestActivities() );
    assertNotNull( contestActual.getContestLevels() );

    assertTrue( contestActual.getContestLevel1Approvers().size() > 0 );
    assertTrue( contestActual.getContestLevel2Approvers().size() > 0 );
    assertTrue( contestActual.getClaimApprovers().size() > 0 );
    assertTrue( contestActual.getContestManagers().size() > 0 );
    assertTrue( contestActual.getContestParticipants().size() > 0 );
    assertTrue( contestActual.getContestActivities().size() > 0 );
    assertTrue( contestActual.getContestLevels().size() > 0 );
  }

  public void testGetContestApproversByIdAndApproverType()
  {
    SSIPromotion promotion = buildSSIPromotion();
    getPromotionDAO().save( promotion );
    flushAndClearSession();

    String uniqueString = buildUniqueString();
    SSIContest expectedContest = createContest( promotion, uniqueString );
    getSSIContestDAO().saveContest( expectedContest );
    flushAndClearSession();

    List<SSIContestApprover> lvl1Approvers = getSSIContestDAO().getContestApproversByIdAndApproverType( expectedContest.getId(), SSIApproverType.lookup( SSIApproverType.CONTEST_LEVEL1_APPROVER ) );

    assertNotNull( lvl1Approvers );
    assertTrue( lvl1Approvers.size() > 0 );

    List<SSIContestApprover> lvl2Approvers = getSSIContestDAO().getContestApproversByIdAndApproverType( expectedContest.getId(), SSIApproverType.lookup( SSIApproverType.CONTEST_LEVEL2_APPROVER ) );

    assertNotNull( lvl2Approvers );
    assertTrue( lvl2Approvers.size() > 0 );

    List<SSIContestApprover> claimApprovers = getSSIContestDAO().getContestApproversByIdAndApproverType( expectedContest.getId(), SSIApproverType.lookup( SSIApproverType.CLAIM_APPROVER ) );

    assertNotNull( claimApprovers );
    assertTrue( claimApprovers.size() > 0 );
  }

  public void testGetAllContestParticipantsByContestId()
  {
    SSIPromotion promotion = buildSSIPromotion();
    getPromotionDAO().save( promotion );
    flushAndClearSession();

    String uniqueString = buildUniqueString();
    SSIContest expectedContest = createContest( promotion, uniqueString );
    getSSIContestDAO().saveContest( expectedContest );
    flushAndClearSession();

    List<SSIContestParticipant> allContestParticipants = getSSIContestDAO().getAllContestParticipantsByContestId( expectedContest.getId() );

    assertNotNull( allContestParticipants );
    assertTrue( allContestParticipants.size() > 0 );
  }

  public void testGetAllContestManagersByContestId()
  {
    SSIPromotion promotion = buildSSIPromotion();
    getPromotionDAO().save( promotion );
    flushAndClearSession();

    String uniqueString = buildUniqueString();
    SSIContest expectedContest = createContest( promotion, uniqueString );
    getSSIContestDAO().saveContest( expectedContest );
    flushAndClearSession();

    List<SSIContestManager> allContestManagers = getSSIContestDAO().getAllContestManagersByContestId( expectedContest.getId() );

    assertNotNull( allContestManagers );
    assertTrue( allContestManagers.size() > 0 );
  }

  public void testIsContestNameUnique()
  {
    SSIPromotion promotion = buildSSIPromotion();
    getPromotionDAO().save( promotion );
    flushAndClearSession();

    String uniqueString = "Testing-12345";
    SSIContest expectedContest = createContest( promotion, uniqueString );
    getSSIContestDAO().saveContest( expectedContest );
    flushAndClearSession();

    assertTrue( getSSIContestDAO().isContestNameUnique( "Some--Unique--N-a-m-e", expectedContest.getId(), "en_US" ) );
    assertTrue( getSSIContestDAO().isContestNameUnique( uniqueString, expectedContest.getId(), "en_US" ) );

    assertTrue( getSSIContestDAO().isContestNameUnique( uniqueString, null, "en_US" ) );
    assertTrue( getSSIContestDAO().isContestNameUnique( uniqueString, new Long( 2 ), "en_US" ) );
  }

  public void testGetAllContestNames()
  {
    SSIPromotion promotion = buildSSIPromotion();
    getPromotionDAO().save( promotion );
    flushAndClearSession();

    String uniqueString = buildUniqueString();
    SSIContest expectedContest = createContest( promotion, uniqueString );
    getSSIContestDAO().saveContest( expectedContest );
    flushAndClearSession();

    List<String> contestNames = getSSIContestDAO().getAllContestNames();

    String contestName = contestNames.get( 0 );
    assertNotNull( contestNames );
    assertTrue( contestNames.contains( expectedContest.getCmAssetCode() ) );
  }

  public void testGetSSIContestManagerList()
  {
    SSIPromotion promotion = buildSSIPromotion();
    getPromotionDAO().save( promotion );
    flushAndClearSession();

    String uniqueString = buildUniqueString();
    SSIContest expectedContest = createContest( promotion, uniqueString );

    Participant baseDataParticpant = getParticipantDAO().getParticipantByUserName( "bhd-001" );
    addContestParticipant( expectedContest, baseDataParticpant );

    getSSIContestDAO().saveContest( expectedContest );
    flushAndClearSession();

    Map<String, Object> allContestManagers = null;
    try
    {
      allContestManagers = getSSIContestDAO().getSSIContestManagerList( expectedContest.getId(), "en_US", "last_name", "desc" );
    }
    catch( Exception e )
    {
      assertFalse( "Error While Calling getSSIContestDAO().getSSIContestManagerList: " + e, true );
    }

    assertNotNull( allContestManagers.get( "managerList" ) );
    assertTrue( ( (List<SSIContestParticipantValueBean>)allContestManagers.get( "managerList" ) ).size() > 0 );
    assertTrue( ( (Integer)allContestManagers.get( "managerCount" ) ) > 0 );
  }

  public void testDownloadContestData()
  {
    SSIPromotion promotion = buildSSIPromotion();
    getPromotionDAO().save( promotion );
    flushAndClearSession();

    String uniqueString = buildUniqueString();
    SSIContest expectedContest = createContest( promotion, uniqueString );

    Participant baseDataParticpant = getParticipantDAO().getParticipantByUserName( "bhd-001" );
    addContestParticipant( expectedContest, baseDataParticpant );

    getSSIContestDAO().saveContest( expectedContest );
    flushAndClearSession();
    Map<String, Object> output = null;
    try
    {
      Map<String, Object> inParameters = new HashMap<String, Object>();
      inParameters.put( "contestId", expectedContest.getId() );
      output = getSSIContestDAO().downloadContestData( inParameters );
    }
    catch( Exception e )
    {
      assertFalse( "Error While Calling getSSIContestDAO().downloadContestData: " + e, true );
    }
    assertTrue( ( (List<SSIContestParticipantValueBean>)output.get( "p_out_result_set" ) ).size() > 0 );
  }

  public void testUpdateContestStackRank()
  {
    SSIPromotion promotion = buildSSIPromotion();
    getPromotionDAO().save( promotion );
    flushAndClearSession();

    String uniqueString = buildUniqueString();
    SSIContest expectedContest = createContest( promotion, uniqueString );

    Participant baseDataParticpant = getParticipantDAO().getParticipantByUserName( "bhd-001" );
    addContestParticipant( expectedContest, baseDataParticpant );

    getSSIContestDAO().saveContest( expectedContest );
    flushAndClearSession();
    try
    {
      getSSIContestDAO().updateContestStackRank( expectedContest.getId() );
    }
    catch( Exception e )
    {
      assertFalse( "Error While Calling getSSIContestDAO().updateContestStackRank: " + e, true );
    }
  }

  public void testGetManagerLiveContests()
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

    Participant manager = createParticipant( uniqueString );

    addContestManager( liveContest, manager );
    addContestManager( closedContest, manager );
    addContestManager( pendingContest, manager );

    getSSIContestDAO().saveContest( liveContest );
    getSSIContestDAO().saveContest( closedContest );
    getSSIContestDAO().saveContest( pendingContest );
    flushAndClearSession();

    List<SSIContestListValueBean> contests = getSSIContestDAO().getManagerLiveContests( manager.getId() );

    assertNotNull( contests );
    assertTrue( contests.size() == 1 );
    assertEquals( contests.get( 0 ).getStatus(), SSIContestStatus.LIVE );

  }

  public void testGetManagerArchievedContests()
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

    Participant manager = createParticipant( uniqueString );

    addContestManager( liveContest, manager );
    addContestManager( closedContest, manager );
    addContestManager( pendingContest, manager );

    getSSIContestDAO().saveContest( liveContest );
    getSSIContestDAO().saveContest( closedContest );
    getSSIContestDAO().saveContest( pendingContest );
    flushAndClearSession();

    List<SSIContestListValueBean> contests = getSSIContestDAO().getManagerArchivedContests( manager.getId() );

    assertNotNull( contests );
    assertTrue( contests.size() == 1 );
    assertEquals( contests.get( 0 ).getStatus(), SSIContestStatus.CLOSED );

  }

  public void testSaveContestParticipants()
  {
    List<SSIContestParticipant> expectedSSIContestParticipants = new ArrayList<SSIContestParticipant>();
    expectedSSIContestParticipants.add( buidlSSIContestParticipant() );

    getSSIContestDAO().saveContestParticipants( expectedSSIContestParticipants );
    flushAndClearSession();

  }

  public void testgetBillCodesByPromoId()
  {

    SSIPromotion promotion = buildSSIPromotion();
    Promotion abstractomotion = (Promotion)promotion;
    abstractomotion.setBillCodesActive( true );
    List<PromotionBillCode> promoBillCodes = buildPromotionBillCodes();
    promoBillCodes.stream().forEach( b -> b.setPromotion( promotion ) );

    abstractomotion.setPromotionBillCodes( promoBillCodes );
    getPromotionDAO().save( promotion );
    flushAndClearSession();

    List<SSIContestBillCodeBean> billCodesByPromoId = getSSIContestDAO().getBillCodesByPromoId( promotion.getId() );
    assertTrue( isNotEmpty( billCodesByPromoId ) );
    flushAndClearSession();

  }

  public void testgetContestBillCodesByContestId()
  {

    SSIPromotion promotion = buildSSIPromotion();
    Promotion abstractomotion = (Promotion)promotion;
    abstractomotion.setBillCodesActive( true );
    List<PromotionBillCode> promoBillCodes = buildPromotionBillCodes();
    promoBillCodes.stream().forEach( b -> b.setPromotion( promotion ) );
    abstractomotion.setPromotionBillCodes( promoBillCodes );
    getPromotionDAO().save( promotion );

    flushAndClearSession();

    String uniqueString = buildUniqueString();
    SSIContest expectedContest = createContest( promotion, uniqueString );
    List<SSIContestBillCode> contestBillCodes = buildContestBillCodes();
    contestBillCodes.stream().forEach( b -> b.setSsiContest( expectedContest ) );
    expectedContest.setContestBillCodes( contestBillCodes );
    getSSIContestDAO().saveContest( expectedContest );

    flushAndClearSession();

    List<SSIContestBillCodeBean> billCodesByPromoId = getSSIContestDAO().getContestBillCodesByContestId( expectedContest.getId() );
    assertTrue( isNotEmpty( billCodesByPromoId ) );

    flushAndClearSession();

  }

  private List<PromotionBillCode> buildPromotionBillCodes()
  {
    PromotionBillCode billCode1 = new PromotionBillCode();
    billCode1.setTrackBillCodeBy( SSIBillPayoutCodeType.CREATOR );
    billCode1.setBillCode( "orgUnitName" );
    billCode1.setSortOrder( 0L );
    return Arrays.asList( billCode1 );
  }

  private List<SSIContestBillCode> buildContestBillCodes()
  {
    SSIContestBillCode billCode1 = new SSIContestBillCode();
    billCode1.setTrackBillCodeBy( SSIBillPayoutCodeType.CREATOR );
    billCode1.setBillCode( "orgUnitName" );
    billCode1.setSortOrder( 0L );
    return Arrays.asList( billCode1 );
  }

  private SSIContestParticipant buidlSSIContestParticipant()
  {
    SSIContestParticipant ssiContestParticipant = new SSIContestParticipant();
    Participant participantOne = createParticipant( buildUniqueString() );
    ssiContestParticipant.setParticipant( participantOne );
    ssiContestParticipant.setActivityDescription( "Activity in progress" );
    ssiContestParticipant.setObjectiveAmount( 1000d );
    ssiContestParticipant.setObjectiveBonusCap( 5000L );
    ssiContestParticipant.setObjectiveBonusIncrement( 3000d );
    ssiContestParticipant.setObjectiveBonusPayout( 7000L );
    ssiContestParticipant.setObjectivePayoutDescription( "Increment payout" );
    ssiContestParticipant.setObjectivePayout( 2100L );
    ssiContestParticipant.setId( 5000L );
    return ssiContestParticipant;

  }

  /*
   * public void testContests() { Integer paxs =
   * getSSIContestParticipantDAO().getContestParticipantsCount( 5000l ); assertTrue( paxs == 9 ); }
   */

  public void testGetContestListByCreator()
  {

    SSIPromotion promotion = buildSSIPromotion();
    getPromotionDAO().save( promotion );
    flushAndClearSession();

    String uniqueString = buildUniqueString();
    SSIContest expectedContest = createContest( promotion, uniqueString );
    getSSIContestDAO().saveContest( expectedContest );
    flushAndClearSession();

    List<SSIContestListValueBean> contests = getSSIContestDAO().getContestListByCreatorSuperViewer( expectedContest.getAuditCreateInfo().getCreatedBy() );
    assertNotNull( contests );
    assertTrue( contests.size() > 0 );
  }

  public static SSIContestActivity buildStaticContestActivity( Integer sequenceNumber )
  {
    SSIContestActivity ssiContestActivity = new SSIContestActivity();
    ssiContestActivity.setDescription( "activity 1 desc" );
    ssiContestActivity.setIncrementAmount( 50D );
    ssiContestActivity.setPayoutAmount( 5L );
    ssiContestActivity.setMinQualifier( 10D );
    ssiContestActivity.setPayoutCapAmount( 100L );
    ssiContestActivity.setGoalAmount( 100D );
    ssiContestActivity.setSequenceNumber( sequenceNumber );
    return ssiContestActivity;
  }

  public static SSIContestLevel buildStaticContestLevel( Integer sequenceNumber )
  {
    SSIContestLevel ssiContestLevel = new SSIContestLevel();
    ssiContestLevel.setGoalAmount( 100D );
    ssiContestLevel.setPayoutAmount( 50L );
    ssiContestLevel.setPayoutDesc( "payout desc" );
    ssiContestLevel.setBadgeRule( null );
    ssiContestLevel.setSequenceNumber( sequenceNumber );
    return ssiContestLevel;
  }

  public static SSIContestClaimField buildStaticClaimField( Integer sequenceNumber )
  {
    SSIContestClaimField claimField = new SSIContestClaimField();
    ClaimFormStepElement formElement = new ClaimFormStepElement();
    Long id = sequenceNumber.longValue() + 999999;
    formElement.setId( id );
    formElement.setDescription( "Test" + sequenceNumber );
    formElement.setCmKeyFragment( "234234234" + sequenceNumber );
    formElement.setRequired( true );
    formElement.setMaxSize( 10 );
    claimField.setId( id );
    claimField.setFormElement( formElement );
    claimField.setRequired( true );
    claimField.setSequenceNumber( sequenceNumber );
    return claimField;
  }

  public void testGetAllContestActivitiesById()
  {
    SSIPromotion promotion = buildSSIPromotion();
    getPromotionDAO().save( promotion );
    flushAndClearSession();

    String uniqueString = buildUniqueString();
    SSIContest expectedContest = createContest( promotion, uniqueString );
    getSSIContestDAO().saveContest( expectedContest );
    flushAndClearSession();

    List<SSIContestActivity> actual = getSSIContestDAO().getContestActivitiesByContestId( expectedContest.getId() );
    assertTrue( actual.size() > 0 );
  }

  public void testGetAllContestLevelsById()
  {
    SSIPromotion promotion = buildSSIPromotion();
    getPromotionDAO().save( promotion );
    flushAndClearSession();

    String uniqueString = buildUniqueString();
    SSIContest expectedContest = createContest( promotion, uniqueString );
    getSSIContestDAO().saveContest( expectedContest );
    flushAndClearSession();

    List<SSIContestLevel> actual = getSSIContestDAO().getContestLevelsByContestId( expectedContest.getId() );
    assertTrue( actual.size() > 0 );
  }

  public void testGettArchievedContestListByCreator()
  {

    SSIPromotion promotion = buildSSIPromotion();
    getPromotionDAO().save( promotion );
    flushAndClearSession();

    String uniqueString = buildUniqueString();
    SSIContest expectedContest = createContest( promotion, uniqueString );
    expectedContest.setStatus( SSIContestStatus.lookup( SSIContestStatus.CLOSED ) );
    getSSIContestDAO().saveContest( expectedContest );
    flushAndClearSession();

    List<SSIContestListValueBean> contests = getSSIContestDAO().getArchivedContestListByCreator( expectedContest.getAuditCreateInfo().getCreatedBy() );
    assertNotNull( contests );
    assertTrue( contests.size() > 0 );
  }

  public void testGettLiveContestListByCreator()
  {

    SSIPromotion promotion = buildSSIPromotion();
    getPromotionDAO().save( promotion );
    flushAndClearSession();

    String uniqueString = buildUniqueString();
    SSIContest expectedContest = createContest( promotion, uniqueString );
    getSSIContestDAO().saveContest( expectedContest );
    flushAndClearSession();

    List<SSIContest> contests = getSSIContestDAO().getCreatorLiveContests( expectedContest.getAuditCreateInfo().getCreatedBy() );
    assertNotNull( contests );
    assertTrue( contests.size() > 0 );
  }

  public void testCopyContest()
  {
    SSIPromotion promotion = buildSSIPromotion();
    getPromotionDAO().save( promotion );
    flushAndClearSession();

    String uniqueString = buildUniqueString();
    SSIContest originalContest = createContest( promotion, uniqueString );
    getSSIContestDAO().saveContest( originalContest );
    flushAndClearSession();
    try
    {
      Long copiedContestId = getSSIContestDAO().copyContest( originalContest.getId(), "copiedContest", "en_US" );
      assertNotNull( getSSIContestDAO().getContestById( copiedContestId ) );
    }
    catch( Exception e )
    {
      fail();
    }
  }

  public void testDeleteContest()
  {
    SSIPromotion promotion = buildSSIPromotion();
    getPromotionDAO().save( promotion );
    flushAndClearSession();

    String uniqueString = buildUniqueString();
    SSIContest expectedContest = createContest( promotion, uniqueString );
    getSSIContestDAO().saveContest( expectedContest );
    flushAndClearSession();

    SSIContest contestActual = getSSIContestDAO().getContestById( expectedContest.getId() );
    assertEquals( expectedContest.getId(), contestActual.getId() );

    getSSIContestDAO().deleteContest( contestActual );

    assertNull( getSSIContestDAO().getContestById( contestActual.getId() ) );

  }

  public void testCheckUserBelongToContestApproversGroup()
  {

    SSIPromotion promotion = buildSSIPromotion();
    getPromotionDAO().save( promotion );
    flushAndClearSession();

    String uniqueString = buildUniqueString();
    SSIContest expectedContest = createContest( promotion, uniqueString );
    getSSIContestDAO().saveContest( expectedContest );
    flushAndClearSession();

    Set<SSIContestApprover> contestLevel1Approvers = expectedContest.getContestLevel1Approvers();
    for ( SSIContestApprover ssiContestApprover : contestLevel1Approvers )
    {
      Long userId = ssiContestApprover.getApprover().getId();
      // assertTrue(getSSIContestDAO().checkUserBelongToContestApproversGroup(
      // expectedContest.getId(), userId ));
    }
  }

  public void testSaveStackRankPayout()
  {

    BadgeRule badgeRule = createBadgeRule();
    SSIContestStackRankPayout savedSSIContestStackRankPayout = saveStackRankPayout( badgeRule );
    SSIContestStackRankPayout loadedStackRankPayout = getSSIContestDAO().getStackRankPayoutById( savedSSIContestStackRankPayout.getId() );

    assertNotNull( loadedStackRankPayout );
    assertEquals( savedSSIContestStackRankPayout.getId(), loadedStackRankPayout.getId() );
    assertEquals( savedSSIContestStackRankPayout.getContest().getId(), loadedStackRankPayout.getContest().getId() );
    assertEquals( savedSSIContestStackRankPayout.getPayoutAmount(), loadedStackRankPayout.getPayoutAmount() );
    assertEquals( savedSSIContestStackRankPayout.getPayoutDescription(), loadedStackRankPayout.getPayoutDescription() );
    assertEquals( savedSSIContestStackRankPayout.getRankPosition(), loadedStackRankPayout.getRankPosition() );
    assertEquals( savedSSIContestStackRankPayout.getBadgeRule().getBadgeName(), loadedStackRankPayout.getBadgeRule().getBadgeName() );

    assertEquals( 1, getSSIContestDAO().getStackRankPayoutsByContestId( loadedStackRankPayout.getContest().getId() ).size() );

  }

  private BadgeRule createBadgeRule()
  {
    GamificationDAO gamificationDAO = getGamificationDAO();
    BadgeRule badgeRule = new BadgeRule();
    badgeRule.setBadgeName( "Test Badge1" );
    badgeRule.setBadgeLibraryCMKey( buildUniqueString() );
    badgeRule = gamificationDAO.saveBadgeRule( badgeRule );
    flushAndClearSession();
    return badgeRule;
  }

  public void testDeleteStackRankPayout()
  {
    /*
     * SSIContestStackRankPayout savedSSIContestStackRankPayout = saveStackRankPayout();
     * getSSIContestDAO().deleteStackRankPayout(
     * savedSSIContestStackRankPayout.getContest().getId(), savedSSIContestStackRankPayout.getId()
     * ); flushAndClearSession(); assertNull( getSSIContestDAO().getStackRankPayoutById(
     * savedSSIContestStackRankPayout.getId() ) );
     */

  }

  protected SSIContestStackRankPayout saveStackRankPayout( BadgeRule badgeRule )
  {
    SSIPromotion promotion = buildSSIPromotion();
    getPromotionDAO().save( promotion );
    flushAndClearSession();

    String uniqueString = buildUniqueString();
    SSIContest expectedContest = createContest( promotion, uniqueString );
    getSSIContestDAO().saveContest( expectedContest );
    flushAndClearSession();

    SSIContestStackRankPayout ssiContestStackRankPayout = buildSSIContestStackRankPayout( expectedContest, 1L, badgeRule );
    return getSSIContestDAO().saveStackRankPayout( ssiContestStackRankPayout );
  }

  protected SSIContestStackRankPayout buildSSIContestStackRankPayout( SSIContest contest, Long rankPosition, BadgeRule badgeRule )
  {
    SSIContestStackRankPayout ssiContestStackRankPayout = new SSIContestStackRankPayout();
    ssiContestStackRankPayout.setContest( contest );
    ssiContestStackRankPayout.setPayoutAmount( 1L );
    ssiContestStackRankPayout.setPayoutDescription( "payoutDescription" );
    ssiContestStackRankPayout.setRankPosition( rankPosition );
    ssiContestStackRankPayout.setBadgeRule( badgeRule );
    ssiContestStackRankPayout.setAuditCreateInfo( contest.getAuditCreateInfo() );
    ssiContestStackRankPayout.setAuditUpdateInfo( contest.getAuditUpdateInfo() );

    return ssiContestStackRankPayout;
  }

  public void testContestCount()
  {
    SSIPromotion promotion = buildSSIPromotion();
    getPromotionDAO().save( promotion );
    flushAndClearSession();

    String uniqueString = buildUniqueString();
    SSIContest expectedContest = createContest( promotion, uniqueString );

    Participant baseDataParticpant = getParticipantDAO().getParticipantByUserName( "bhd-001" );
    addContestParticipant( expectedContest, baseDataParticpant );

    getSSIContestDAO().saveContest( expectedContest );
    flushAndClearSession();
    int count = 0;
    try
    {
      count = getSSIContestDAO().getOpenContestCount( promotion.getId(), SSIContestType.OBJECTIVES );
    }
    catch( Exception e )
    {
      assertFalse( "Error While Calling getSSIContestDAO().getOpenContestCount: " + e, true );
    }
    assertTrue( count > 0 );
  }

  public void testGetAwardThemNowContest()
  {
    SSIPromotion promotion = buildSSIPromotion();
    getPromotionDAO().save( promotion );
    flushAndClearSession();

    String uniqueString = buildUniqueString();
    SSIContest expectedContest = createAwardThemNowContest( promotion, uniqueString );

    Participant baseDataParticpant = getParticipantDAO().getParticipantByUserName( "bhd-001" );
    addContestParticipant( expectedContest, baseDataParticpant );

    getSSIContestDAO().saveContest( expectedContest );
    flushAndClearSession();
    List<SSIContestListValueBean> contests = null;
    try
    {
      contests = getSSIContestDAO().getAwardThemNowContestSuperViewer( expectedContest.getAuditCreateInfo().getCreatedBy() );
    }
    catch( Exception e )
    {
      assertFalse( "Error While Calling getSSIContestDAO().getOpenContestCount: " + e, true );
    }
    assertTrue( contests != null && contests.size() > 0 );
  }

  private static GamificationDAO getGamificationDAO()
  {
    return (GamificationDAO)ApplicationContextFactory.getApplicationContext().getBean( "gamificationDAO" );
  }

  public void testFetchContestCreatorCount()
  {

    SSIPromotion promotion = buildSSIPromotion();
    getPromotionDAO().save( promotion );
    flushAndClearSession();

    String uniqueString = buildUniqueString();
    SSIContest expectedContest = createContest( promotion, uniqueString );
    getSSIContestDAO().saveContest( expectedContest );
    flushAndClearSession();

    boolean isContestCreatorCount = getSSIContestDAO().fetchContestCreatorCount( 5582L );
    assertTrue( isContestCreatorCount );

    boolean isContCreatorCount = getSSIContestDAO().fetchContestCreatorCount( 2234556L );
    assertFalse( isContCreatorCount );
  }
}
