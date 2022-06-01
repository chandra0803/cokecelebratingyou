/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javatest/com/biperf/core/dao/throwdown/hibernate/SmackTalkDAOImplTest.java,v $
 */

package com.biperf.core.dao.throwdown.hibernate;

import java.util.List;

import com.biperf.core.dao.hibernate.BaseDAOTest;
import com.biperf.core.dao.participant.ParticipantDAO;
import com.biperf.core.dao.participant.hibernate.ParticipantDAOImplTest;
import com.biperf.core.dao.throwdown.MatchDAO;
import com.biperf.core.dao.throwdown.SmackTalkDAO;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.Match;
import com.biperf.core.domain.promotion.SmackTalkComment;
import com.biperf.core.domain.promotion.SmackTalkLike;
import com.biperf.core.utils.ApplicationContextFactory;

public class SmackTalkDAOImplTest extends BaseDAOTest
{

  /***  J
   ***/
  public void testSmackTalkCommentMethods()
  {
    SmackTalkDAO smackTalkDAO = getSmackTalkDAO();
    ParticipantDAO participantDAO = getParticipantDAO();
    MatchDAO matchDAO = getMatchDAO();

    Participant participant1 = ParticipantDAOImplTest.buildUniqueParticipant( getUniqueString() );
    participant1.setId( new Long( 5001 ) );
    participantDAO.saveParticipant( participant1 );

    List<Match> matches = MatchDAOImplTest.buildMatchesWithAllDetails();
    Long[] matchIds = new Long[matches.size()];
    for ( Match match : matches )
    {
      matchDAO.save( match );
      matchIds[matches.indexOf( match )] = match.getId();
    }

    SmackTalkComment smackTalkPost1 = new SmackTalkComment();
    smackTalkPost1.setMatch( matchDAO.getMatch( matches.get( 0 ).getId() ) );
    smackTalkPost1.setUser( participant1 );
    smackTalkPost1.setComments( "this is a first comment by participant1 for match" + matches.get( 0 ).getId() );
    smackTalkPost1.setParent( null );
    SmackTalkComment savedPost1 = smackTalkDAO.saveSmackTalkComment( smackTalkPost1 ); // smackTalkPost
                                                                                       // Save is
                                                                                       // done here

    SmackTalkComment smackTalkPost2 = new SmackTalkComment();
    smackTalkPost2.setMatch( matchDAO.getMatch( matches.get( 0 ).getId() ) );
    smackTalkPost2.setUser( participant1 );
    smackTalkPost2.setComments( "this is a first comment by participant1 for match" + matches.get( 0 ).getId() );
    smackTalkPost2.setParent( null );
    SmackTalkComment savedPost2 = smackTalkDAO.saveSmackTalkComment( smackTalkPost2 ); // smackTalkPost
                                                                                       // Save is
                                                                                       // done here

    SmackTalkComment smackTalkComment = new SmackTalkComment();
    smackTalkComment.setMatch( matchDAO.getMatch( matches.get( 0 ).getId() ) );
    smackTalkComment.setUser( participant1 );
    smackTalkComment.setComments( "this is a first comment by participant1 for post" + savedPost1.getId() );
    smackTalkComment.setParent( savedPost1 );
    SmackTalkComment savedComment = smackTalkDAO.saveSmackTalkComment( smackTalkComment );

    List<SmackTalkComment> expectedListSmackTalkComment = smackTalkDAO.getUserCommentsBySmackTalkPost( savedPost1.getId() );
    System.out.println( expectedListSmackTalkComment.size() );
    // below test cases written to verify the method getUserCommentsByMatch and also
    // saveSmackTalkComment
    assertFalse( "Size of SmackTalkComment Should Be One", expectedListSmackTalkComment.size() <= 0 && expectedListSmackTalkComment.size() > 1 );
    assertTrue( "Size of SmackTalkComment is one", expectedListSmackTalkComment.size() == 1 );

    List<SmackTalkComment> expectedSmackTalkPosts = smackTalkDAO.getSmackTalkPostsByMatch( matches.get( 0 ).getId() );
    System.out.println( "expectedSmackTalkPosts" + expectedSmackTalkPosts.size() );

    assertFalse( "Size of SmackTalkPost Should Be Two", expectedSmackTalkPosts.size() <= 0 && expectedSmackTalkPosts.size() > 2 );
    assertTrue( "Size of SmackTalkPost is Two", expectedSmackTalkPosts.size() == 2 );

    List<SmackTalkComment> expectedSmackTalkPostsForMatches = smackTalkDAO.getSmackTalkPostsForMatch( matchIds );
    System.out.println( "expectedSmackTalkPostsForMatches " + expectedSmackTalkPostsForMatches.size() );

    assertFalse( "Size of SmackTalkPosts Should Be Two", expectedSmackTalkPostsForMatches.size() <= 0 && expectedSmackTalkPostsForMatches.size() > 2 );
    assertTrue( "Size of SmackTalkPosts is two", expectedSmackTalkPostsForMatches.size() == 2 );

    List<SmackTalkComment> expectedSmackTalkPostsForPromotion = smackTalkDAO.getSmackTalkByPromotionAndRoundNumber( matches.get( 0 ).getRound().getDivision().getPromotion().getId(),
                                                                                                                    matches.get( 0 ).getRound().getRoundNumber() );
    System.out.println( "expectedSmackTalkPostsForPromotion " + expectedSmackTalkPostsForPromotion.size() );

    assertFalse( "Size of SmackTalkPosts Should Be 0", expectedSmackTalkPostsForPromotion.size() > 0 );
    assertTrue( "Size of SmackTalkPosts is 0", expectedSmackTalkPostsForPromotion.size() == 0 );

    /*
     * List<SmackTalkComment> expectedSmackTalkPostsForRound = smackTalkDAO.getSmackTalkByTeam(
     * matches.get( 0 ).getRound().getId() );
     * System.out.println("expectedSmackTalkPostsForRound "+expectedSmackTalkPostsForRound.size());
     * assertFalse( "Size of SmackTalkPosts Should Be two", expectedSmackTalkPostsForRound.size() <=
     * 0 && expectedSmackTalkPostsForRound.size() > 2 ); assertTrue(
     * "Size of SmackTalkPosts is two", expectedSmackTalkPostsForRound.size() == 2 );
     */

    List<SmackTalkComment> expectedSmackTalkPostsForRoundandTeam1 = smackTalkDAO.getSmackTalkByPromotionAndRoundIdAndTeam( matches.get( 0 ).getRound().getDivision().getPromotion().getId(),
                                                                                                                           matches.get( 0 ).getRound().getId(),
                                                                                                                           new Long( 100 ) );
    System.out.println( "expectedSmackTalkPostsForRoundandTeam1 " + expectedSmackTalkPostsForRoundandTeam1.size() );

    assertFalse( "Size of SmackTalkPosts Should Be zero", expectedSmackTalkPostsForRoundandTeam1.size() > 0 );
    assertTrue( "Size of SmackTalkPosts is zero", expectedSmackTalkPostsForRoundandTeam1.size() == 0 );

    Long[] smackTalkIds = new Long[2];

    smackTalkIds[0] = savedPost1.getId();
    smackTalkIds[1] = savedPost2.getId();

    List<SmackTalkComment> expectedSmackTalkComments2 = smackTalkDAO.getUserCommentsForSmackTalkPosts( smackTalkIds );

    assertFalse( "Size of SmackTalkComments Should Be One", expectedSmackTalkComments2.size() > 1 );
    assertTrue( "Size of SmackTalkComments is one", expectedSmackTalkComments2.size() == 1 );

  }

  public void testSmackTalkLikeMethods()
  {
    SmackTalkDAO smackTalkDAO = getSmackTalkDAO();
    ParticipantDAO participantDAO = getParticipantDAO();
    MatchDAO matchDAO = getMatchDAO();

    Participant participant1 = ParticipantDAOImplTest.buildUniqueParticipant( getUniqueString() );
    participantDAO.saveParticipant( participant1 );

    Participant participant2 = ParticipantDAOImplTest.buildUniqueParticipant( getUniqueString() );
    participantDAO.saveParticipant( participant2 );

    Participant participant3 = ParticipantDAOImplTest.buildUniqueParticipant( getUniqueString() );
    participantDAO.saveParticipant( participant3 );

    Participant participant4 = ParticipantDAOImplTest.buildUniqueParticipant( getUniqueString() );
    participantDAO.saveParticipant( participant4 );

    Participant participant5 = ParticipantDAOImplTest.buildUniqueParticipant( getUniqueString() );
    participantDAO.saveParticipant( participant5 );

    Participant current = ParticipantDAOImplTest.buildUniqueParticipant( getUniqueString() );
    participantDAO.saveParticipant( current );

    List<Match> matches = MatchDAOImplTest.buildMatchesWithAllDetails();
    Long[] matchIds = new Long[matches.size()];
    for ( Match match : matches )
    {
      matchDAO.save( match );
      matchIds[matches.indexOf( match )] = match.getId();
    }
    SmackTalkComment smackTalkPost = new SmackTalkComment();
    smackTalkPost.setMatch( matchDAO.getMatch( matches.get( 0 ).getId() ) );
    smackTalkPost.setUser( participant1 );
    smackTalkPost.setComments( "this is a first comment by participant1 for match" + matches.get( 0 ).getId() );
    smackTalkPost.setParent( null );
    SmackTalkComment savedPost1 = smackTalkDAO.saveSmackTalkComment( smackTalkPost ); // smackTalkPost
                                                                                      // Save is
                                                                                      // done here

    SmackTalkLike smackTalkLike1 = new SmackTalkLike();
    smackTalkLike1.setSmackTalkComment( savedPost1 );
    smackTalkLike1.setUser( participant1 );
    smackTalkLike1.setLiked( true );
    SmackTalkLike savedLike1 = smackTalkDAO.saveSmackTalkLike( smackTalkLike1 ); // smackTalkLike
                                                                                 // Save is done
                                                                                 // here

    SmackTalkLike smackTalkLike2 = new SmackTalkLike();
    smackTalkLike2.setSmackTalkComment( smackTalkPost );
    smackTalkLike2.setUser( current );
    smackTalkLike2.setLiked( true );
    SmackTalkLike savedLike2 = smackTalkDAO.saveSmackTalkLike( smackTalkLike2 ); // smackTalkLike
                                                                                 // Save is done
                                                                                 // here

    Long[] smackTalkIds = new Long[1];

    smackTalkIds[0] = savedPost1.getId();

    List<SmackTalkLike> listSmackTalkLike1 = smackTalkDAO.getUserLikesByComments( savedPost1.getId() );

    // below test cases written to verify the method getUserLikesByMatch and also saveSmackTalkLike
    assertFalse( "Size of getUserLikesByMatch Should Be Two.", listSmackTalkLike1.size() < 2 );
    assertTrue( "Size of getUserLikesByMatch is two", listSmackTalkLike1.size() == 2 );

    boolean currentUserLike = smackTalkDAO.isCurrentUserLikedSmackTalk( savedPost1.getId(), current.getId() );

    assertTrue( "Current User likes the post", currentUserLike );
    assertEquals( true, currentUserLike );
    List<SmackTalkLike> listSmackTalkLike2 = smackTalkDAO.getUserLikesForComments( smackTalkIds );

    // below test cases written to verify the method getUserLikesByMatch and also saveSmackTalkLike
    assertFalse( "Size of getUserLikesByPost Should Be Two.", listSmackTalkLike1.size() < 2 );
    assertTrue( "Size of getUserLikesByPost is two", listSmackTalkLike1.size() == 2 );

    long likeCountBySmackTalk = smackTalkDAO.getLikeCountBySmackTalk( savedPost1.getId() );

    int pageNumber1 = 1;
    List<SmackTalkLike> likedPaxListBySmackTalkPost = smackTalkDAO.getLikedPaxListBySmackTalkId( savedPost1.getId(), pageNumber1 );
    assertTrue( "Size of likedPaxListBySmackTalkPost is 2", likedPaxListBySmackTalkPost.size() == 2 );

  }

  /**
   * Get the SmackTalkDAO.
   * 
   * @return SmackTalkDAO
   */
  private static SmackTalkDAO getSmackTalkDAO()
  {
    return (SmackTalkDAO)ApplicationContextFactory.getApplicationContext().getBean( "smackTalkDAO" );
  }

  /**
   * Get the ParticipantDAO.
   * 
   * @return ParticipantDAO
   */
  private static ParticipantDAO getParticipantDAO()
  {
    return (ParticipantDAO)ApplicationContextFactory.getApplicationContext().getBean( "participantDAO" );
  }

  /**
   * Get the MatchDAO.
   * 
   * @return MatchDAO
   */
  private static MatchDAO getMatchDAO()
  {
    return (MatchDAO)ApplicationContextFactory.getApplicationContext().getBean( "matchDAO" );
  }
}
