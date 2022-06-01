
package com.biperf.core.dao.promotion.hibernate;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.dao.claim.ClaimDAO;
import com.biperf.core.dao.claim.hibernate.ClaimDAOImplTest;
import com.biperf.core.dao.hibernate.BaseDAOTest;
import com.biperf.core.dao.hierarchy.NodeTypeDAO;
import com.biperf.core.dao.participant.ParticipantDAO;
import com.biperf.core.dao.participant.UserDAO;
import com.biperf.core.dao.promotion.PromotionDAO;
import com.biperf.core.dao.promotion.PublicRecognitionDAO;
import com.biperf.core.domain.claim.RecognitionClaim;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.ParticipantFollowers;
import com.biperf.core.domain.promotion.PublicRecognitionComment;
import com.biperf.core.domain.promotion.PublicRecognitionLike;
import com.biperf.core.domain.user.User;
import com.biperf.core.utils.ApplicationContextFactory;
import com.biperf.core.value.PublicRecognitionFormattedValueBean;

public class PublicRecognitionDAOImplTest extends BaseDAOTest
{

  private static final Log logger = LogFactory.getLog( PublicRecognitionDAOImplTest.class );
  private PublicRecognitionDAO publicRecognitionDAO;

  public void setUp() throws Exception
  {
    super.setUp();

    publicRecognitionDAO = getPublicRecognitionDAO();

  }

  private UserDAO getUserDAO()
  {
    return (UserDAO)ApplicationContextFactory.getApplicationContext().getBean( UserDAO.BEAN_NAME );
  }

  private ParticipantDAO getParticipantDAO()
  {
    return (ParticipantDAO)ApplicationContextFactory.getApplicationContext().getBean( ParticipantDAO.BEAN_NAME );
  }

  /**
   * Returns a reference to the claim DAO.
   * 
   * @return a reference to the claim DAO.
   */
  private ClaimDAO getClaimDAO()
  {
    return (ClaimDAO)ApplicationContextFactory.getApplicationContext().getBean( ClaimDAO.BEAN_NAME );
  }

  /**
   * Returns a reference to the PublicRecognition DAO.
   * 
   * @return a reference to the PublicRecognition DAO.
   */
  private PublicRecognitionDAO getPublicRecognitionDAO()
  {
    return (PublicRecognitionDAO)ApplicationContextFactory.getApplicationContext().getBean( PublicRecognitionDAO.BEAN_NAME );
  }

  /**
   * Returns a reference to the Promotion DAO.
   * 
   * @return a reference to the Promotion DAO.
   */
  private static PromotionDAO getPromotionDAO()
  {
    return (PromotionDAO)getDAO( PromotionDAO.BEAN_NAME );
  }

  /**
   * Returns a reference to the NodeType DAO.
   * 
   * @return a reference to the NodeType DAO.
   */
  private static NodeTypeDAO getNodeTypeDAO()
  {
    return (NodeTypeDAO)getDAO( "nodeTypeDAO" );
  }

  /*
   * Sample Test case for savePublicRecognitionComment
   */
  public void testSavePublicRecognitionComment()
  {

    String uniqueString = String.valueOf( System.currentTimeMillis() % 5503032 );
    RecognitionClaim claim = ClaimDAOImplTest.buildRecognitionClaim( uniqueString );
    getClaimDAO().saveClaim( claim );
    RecognitionClaim savedClaim = (RecognitionClaim)getClaimDAO().getClaimById( claim.getId() );

    PublicRecognitionComment regComment = new PublicRecognitionComment();
    regComment.setClaim( savedClaim );
    Participant user = new Participant();
    ParticipantDAO participantDAO = getParticipantDAO();
    user = participantDAO.getParticipantById( 5583L );
    regComment.setUser( user );
    PublicRecognitionComment savedComment = publicRecognitionDAO.savePublicRecognitionComment( regComment );

    assertEquals( savedClaim.getId(), savedComment.getClaim().getId() );
  }

  /*
   * Sample Test case for savePublicRecognitionLike
   */
  public void testSavePublicRecognitionLike()
  {

    String uniqueString = String.valueOf( System.currentTimeMillis() % 5503032 );
    RecognitionClaim claim = ClaimDAOImplTest.buildRecognitionClaim( uniqueString );
    getClaimDAO().saveClaim( claim );
    RecognitionClaim savedClaim = (RecognitionClaim)getClaimDAO().getClaimById( claim.getId() );

    PublicRecognitionLike paxLikeClaim = new PublicRecognitionLike();
    paxLikeClaim.setClaim( savedClaim );
    Participant user = new Participant();
    UserDAO userDAO = getUserDAO();
    ParticipantDAO participantDAO = getParticipantDAO();
    user = participantDAO.getParticipantById( 5583L );
    paxLikeClaim.setUser( user );
    PublicRecognitionLike savedComment = publicRecognitionDAO.savePublicRecognitionLike( paxLikeClaim );

    assertEquals( savedClaim.getId(), savedComment.getClaim().getId() );

  }

  /*
   * Sample Test case for Get Users Likes for Claim
   */
  public void testGetUserLikesByClaim()
  {
    String uniqueString = String.valueOf( System.currentTimeMillis() % 5503032 );
    RecognitionClaim claim = ClaimDAOImplTest.buildRecognitionClaim( uniqueString );
    getClaimDAO().saveClaim( claim );
    RecognitionClaim savedClaim = (RecognitionClaim)getClaimDAO().getClaimById( claim.getId() );

    PublicRecognitionLike paxLikeClaim = new PublicRecognitionLike();
    paxLikeClaim.setClaim( savedClaim );
    Participant user = new Participant();
    ParticipantDAO participantDAO = getParticipantDAO();
    user = participantDAO.getParticipantById( 5583L );
    paxLikeClaim.setUser( user );
    publicRecognitionDAO.savePublicRecognitionLike( paxLikeClaim );
    List<PublicRecognitionLike> savedLike = publicRecognitionDAO.getUserLikesByClaim( savedClaim.getId() );

    assertEquals( "Number of Users Liked this Claim", 1, savedLike.size() );
  }

  /*
   * Sample Test case for addParticipantFollowee
   */
  public void testAddParticipantFollowee()
  {

    UserDAO userDAO = getUserDAO();
    User user = new User();
    user = userDAO.getUserById( 5584L );
    User userFollower = new User();
    userFollower = userDAO.getUserById( 60107L );
    ParticipantFollowers followerObj = new ParticipantFollowers();
    followerObj.setParticipant( user );
    followerObj.setFollower( userFollower );
    ParticipantFollowers expectedObj = new ParticipantFollowers();
    try
    {
      getParticipantDAO().addParticipantFollowee( followerObj );
      expectedObj = getParticipantDAO().getById( 5584L, 60107L );
    }
    catch( Exception e )
    {
      logger.debug( "Exception in Adding Followee - PublicRecognitionDAOImpl Test" + e );
    }

    assertEquals( followerObj, expectedObj );

  }

  /*
   * Sample Test case for removeParticipantFollowee
   */
  public void testRemoveParticipantFollowee()
  {
    UserDAO userDAO = getUserDAO();
    User user = new User();
    user = userDAO.getUserById( 5584L );
    User userFollower = new User();
    userFollower = userDAO.getUserById( 60108L );
    ParticipantFollowers followerObj = new ParticipantFollowers();
    followerObj.setParticipant( user );
    followerObj.setFollower( userFollower );

    ParticipantFollowers expectedObj = new ParticipantFollowers();
    try
    {
      getParticipantDAO().addParticipantFollowee( followerObj );
      getParticipantDAO().removeParticipantFollowee( followerObj );
      expectedObj = getParticipantDAO().getById( 5584L, 60108L );
    }
    catch( Exception e )
    {
      logger.debug( "Exception in Removing Followee - PublicRecognitionDAOImpl Test" + e );
    }
    assertNotSame( followerObj, expectedObj );
  }

  /*
   * Sample Test case for GetBy Participant Id, Follower Id
   */
  public void testGetById()
  {
    UserDAO userDAO = getUserDAO();
    User user = new User();
    user = userDAO.getUserById( 5584L );
    User userFollower = new User();
    userFollower = userDAO.getUserById( 60108L );
    ParticipantFollowers followerObj = new ParticipantFollowers();
    followerObj.setParticipant( user );
    followerObj.setFollower( userFollower );

    ParticipantFollowers expectedObj = new ParticipantFollowers();
    try
    {
      getParticipantDAO().addParticipantFollowee( followerObj );
      expectedObj = getParticipantDAO().getById( 5584L, 60108L );
    }
    catch( Exception e )
    {
      logger.debug( "Exception in Removing Followee - PublicRecognitionDAOImpl Test" + e );
    }
    assertNotNull( "UserID is not null", expectedObj.getParticipant().getId() );
  }

  /**
   * No sample data so it will return null, but at least we will see if the mapping
   * is correct.
   */
  public void testGetPublicRecognitionCommentBy()
  {
    PublicRecognitionComment prc = publicRecognitionDAO.getPublicRecognitionCommentBy( new Long( 5555 ) );
    assertNull( prc );
  }

  public void testGetPublicRecognitionClaimsByClaimId()
  {
    Long claimId = 10002L;
    Long userId = 5584L;
    List<PublicRecognitionFormattedValueBean> claims = publicRecognitionDAO.getPublicRecognitionClaimsByClaimId( claimId, userId );

    assertNotNull( claims );
  }

}
