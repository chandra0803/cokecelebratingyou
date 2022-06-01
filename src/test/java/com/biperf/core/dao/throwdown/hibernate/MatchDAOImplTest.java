/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javatest/com/biperf/core/dao/throwdown/hibernate/MatchDAOImplTest.java,v $
 */

package com.biperf.core.dao.throwdown.hibernate;

import java.util.ArrayList;
import java.util.List;

import com.biperf.core.dao.hibernate.BaseDAOTest;
import com.biperf.core.dao.throwdown.MatchDAO;
import com.biperf.core.dao.throwdown.RoundDAO;
import com.biperf.core.domain.enums.MatchStatusType;
import com.biperf.core.domain.promotion.Match;
import com.biperf.core.domain.promotion.Round;
import com.biperf.core.utils.ApplicationContextFactory;

public class MatchDAOImplTest extends BaseDAOTest
{

  public static int ROUND_NUM = 0;

  /**
   *public method to create a match that can be used in other test suite.
   */
  public static List<Match> buildMatchesWithAllDetails()
  {
    // Save the Round Object
    RoundDAO roundDAO = getRoundDAO();
    RoundDAOImplTest roundDAOImplTest = new RoundDAOImplTest();
    List<Round> rounds = roundDAOImplTest.buildRoundWithAllDetails();
    List<Match> matches = new ArrayList<Match>();
    for ( Round round : rounds )
    {
      roundDAO.save( round );
      Match match = new Match();
      match.setRound( round );
      MatchStatusType matchStatusType = MatchStatusType.lookup( MatchStatusType.NOT_PLAYED );
      match.setStatus( matchStatusType );
      getMatchDAO().save( match );
      matches.add( match );
    }

    return matches;
  }

  public void testGetMatch()
  {
    List<Match> matches = buildMatchesWithAllDetails();
    Match expectedMatch = getMatchDAO().getMatch( matches.get( 0 ).getId() );
    System.out.println( expectedMatch );
    assertSame( "Match is  Same", expectedMatch, matches.get( 0 ) );
    assertNotSame( "Match is not Same", expectedMatch, matches.get( 1 ) );
  }

  public void testGetMatchDetails()
  {
    List<Match> matches = buildMatchesWithAllDetails();
    Match expectedMatch = getMatchDAO().getMatchDetails( matches.get( 0 ).getId() );
    System.out.println( expectedMatch );
    assertSame( "Match is  Same", expectedMatch, matches.get( 0 ) );
    assertNotSame( "Match is not Same", expectedMatch, matches.get( 1 ) );
  }

  public void testGetUnplayedMatchesForPromotionAndRound()
  {
    List<Match> matches = buildMatchesWithAllDetails();
    Long roundId = matches.get( 0 ).getRound().getId();
    List<Match> expectedMatch = getMatchDAO().getUnplayedMatchesForPromotionAndRound( roundId );
    assertTrue( "Match contains status as not_played", expectedMatch.size() > 0 );
    assertFalse( "Match contans status as played", expectedMatch.size() == 0 );
  }

  public void testGetMatchesByPromotionAndTeam()
  {
    List<Match> matches = buildMatchesWithAllDetails();
    Long promoId = matches.get( 0 ).getRound().getDivision().getPromotion().getId();
    List<Match> expectedMatches = getMatchDAO().getMatchesByPromotionAndTeam( promoId, new Long( 100 ) );
    assertTrue( "Match List is empty", expectedMatches.size() == 0 );
  }

  public void testGetMatchesByRound()
  {
    List<Match> matches = buildMatchesWithAllDetails();
    Long roundId = matches.get( 0 ).getRound().getId();
    List<Match> expectedMatches = getMatchDAO().getMatchesByRound( roundId );
    assertTrue( "Match Lists are  Same", matches.containsAll( expectedMatches ) );
  }

  public void testGetMatchesByPromotionAndRoundNumber()
  {
    List<Match> matches = buildMatchesWithAllDetails();
    Long promoId = matches.get( 0 ).getRound().getDivision().getPromotion().getId();
    // check the hbm - create the match team outcome
    List<Match> expectedMatch = getMatchDAO().getMatchesByPromotionAndRoundNumber( promoId, 1 );
    assertTrue( "Expected Match is empty", expectedMatch.size() == 0 );
  }

  public void testGetMatchByPromotionAndRoundIdAndTeam()
  {
    List<Match> matches = buildMatchesWithAllDetails();
    Long promoId = matches.get( 0 ).getRound().getDivision().getPromotion().getId();
    // check the hbm - create the match team outcome
    Match expectedMatch = getMatchDAO().getMatchByPromotionAndRoundIdAndTeam( promoId, matches.get( 0 ).getRound().getId(), new Long( 100 ) );
    assertNull( "Expected Match is empty", expectedMatch );
  }

  public void testGetMatchByPromotionAndRoundNumberAndTeam()
  {
    List<Match> matches = buildMatchesWithAllDetails();
    Long promoId = matches.get( 0 ).getRound().getDivision().getPromotion().getId();
    Match expectedMatch = getMatchDAO().getMatchByPromotionAndRoundNumberAndTeam( promoId, matches.get( 0 ).getRound().getRoundNumber(), new Long( 100 ) );
    assertNull( "Expected Match is empty", expectedMatch );
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

  /**
   * Get the RoundDAO.
   * 
   * @return RoundDAO
   */
  private static RoundDAO getRoundDAO()
  {
    return (RoundDAO)ApplicationContextFactory.getApplicationContext().getBean( "roundDAO" );
  }
}
