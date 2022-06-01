/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javatest/com/biperf/core/dao/throwdown/hibernate/RoundDAOImplTest.java,v $
 */

package com.biperf.core.dao.throwdown.hibernate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.biperf.core.dao.hibernate.BaseDAOTest;
import com.biperf.core.dao.promotion.PromotionDAO;
import com.biperf.core.dao.promotion.hibernate.PromotionDAOImplTest;
import com.biperf.core.dao.throwdown.DivisionDAO;
import com.biperf.core.dao.throwdown.RoundDAO;
import com.biperf.core.domain.promotion.Division;
import com.biperf.core.domain.promotion.MatchTeamOutcome;
import com.biperf.core.domain.promotion.Round;
import com.biperf.core.domain.promotion.ThrowdownPromotion;
import com.biperf.core.utils.ApplicationContextFactory;
import com.biperf.core.utils.DateUtils;

public class RoundDAOImplTest extends BaseDAOTest
{

  public static int ROUND_NUM = 0;

  public List<Round> buildRoundWithAllDetails()
  {
    List<Round> listOfRounds = new ArrayList<Round>();

    DivisionDAO divisionDAO = getDivisionDAO();
    PromotionDAO promoDAO = getPromotionDAO();

    // building a promotion
    ThrowdownPromotion tdPromo1 = new ThrowdownPromotion();
    tdPromo1 = PromotionDAOImplTest.buildThrowdownPromotion( getUniqueString() );

    // For the above promotion creating a division
    Division division1 = new Division();
    division1 = DivisionDAOImplTest.buildDivision( getUniqueString() );
    division1.setPromotion( tdPromo1 );
    tdPromo1.addDivision( division1 );

    promoDAO.save( tdPromo1 );
    divisionDAO.save( division1 );

    int numberOfRounds = tdPromo1.getNumberOfRounds();
    int lengthOfRound = tdPromo1.getLengthOfRound();

    // build the round for each division in the promotion
    for ( Division division : tdPromo1.getDivisions() )
    {
      Calendar startDate = Calendar.getInstance();
      startDate.setTime( DateUtils.toStartDate( tdPromo1.getHeadToHeadStartDate() ) );
      Date previousRoundsEndDate = null;

      for ( int i = 0; i < numberOfRounds; i++ )
      {
        Calendar roundStartDate = null;
        if ( previousRoundsEndDate == null )
        {
          roundStartDate = startDate;
        }
        else
        {
          Calendar tempStart = Calendar.getInstance();
          tempStart.setTime( previousRoundsEndDate );
          // kick it over to the next day
          tempStart.add( Calendar.DATE, 1 );
          roundStartDate = tempStart;
        }
        Round round = new Round();
        round.setDivision( division );
        round.setPayoutsIssued( false );
        round.setRoundNumber( i + 1 );
        round.setStartDate( DateUtils.toStartDate( roundStartDate.getTime() ) );

        startDate.add( Calendar.DATE, i > 0 ? lengthOfRound : lengthOfRound - 1 );

        round.setEndDate( DateUtils.toEndDate( startDate.getTime() ) );
        previousRoundsEndDate = startDate.getTime();
        division.getRounds().add( round );
        listOfRounds.add( round );
      }
    }
    return listOfRounds;

  }

  public void testSave()
  {
    List<Round> actualRounds = buildRoundWithAllDetails();
    for ( Round round : actualRounds )
    {
      getRoundDAO().save( round );
      Round expectedRound = getRoundDAO().getRoundById( round.getId() );
      assertSame( "Round is  Same", expectedRound, round );
    }
  }

  public void testGetRoundsForPromotionByRoundNumber()
  {
    List<Round> actualRounds = buildRoundWithAllDetails();
    for ( Round round : actualRounds )
    {
      getRoundDAO().save( round );
    }
    Round round = actualRounds.get( 0 );
    List<Round> expectedRounds = getRoundDAO().getRoundsForPromotionByRoundNumber( round.getDivision().getPromotion().getId(), round.getRoundNumber() );
    assertTrue( "Round List is not empty", expectedRounds.size() > 0 );
    assertTrue( "Round List are  Same", actualRounds.containsAll( expectedRounds ) );

    flushAndClearSession();
  }

  public void testGetRoundsForPromotionByDivisionAndRoundNumber()
  {
    List<Round> actualRounds = buildRoundWithAllDetails();
    Round round = actualRounds.get( 0 );
    Round savedRound = getRoundDAO().save( round );
    Round expectedRound = getRoundDAO().getRoundsForPromotionByDivisionAndRoundNumber( savedRound.getDivision().getPromotion().getId(), savedRound.getDivision().getId(), savedRound.getRoundNumber() );
    assertSame( "Round is  Same", savedRound, expectedRound );

    flushAndClearSession();
  }

  public void testGetRoundsByDivision()
  {
    List<Round> actualRounds = buildRoundWithAllDetails();
    for ( Round round : actualRounds )
    {
      getRoundDAO().save( round );

    }
    Round round = actualRounds.get( 0 );
    List<Round> expectedRounds = getRoundDAO().getRoundsByDivision( round.getDivision().getId() );
    assertTrue( "Round List is not empty", expectedRounds.size() > 0 );
    assertTrue( "Round List are  Same", actualRounds.containsAll( expectedRounds ) );

    flushAndClearSession();
  }

  public void testgetCalculatedAverageForRound()
  {
    MatchTeamOutcome matchTeamOutcome = MatchTeamOutcomeDAOImplTest.buildMatchTeamOutcomeWithDetails();
    BigDecimal average = getRoundDAO().getCalculatedAverageForRound( matchTeamOutcome.getMatch().getRound().getId(), matchTeamOutcome.getTeam().getId(), RoundingMode.HALF_UP );
    assertTrue( "The calculated average is 0 ", average.doubleValue() == 0 );

    flushAndClearSession();
  }

  public void testGetCurrentRound()
  {
    List<Round> actualRounds = buildRoundWithAllDetails();
    Round round = actualRounds.get( 0 );
    round.setStartDate( new Date() );
    round.setEndDate( new Date() );
    Round savedRound = getRoundDAO().save( round );
    Round expectedRound = getRoundDAO().getCurrentRound( savedRound.getDivision().getPromotion().getId(), savedRound.getDivision().getId() );
    assertSame( "Rounds are  Same", savedRound, expectedRound );
    flushAndClearSession();
  }

  public void testIsRoundPaidForDivisionPayouts()
  {
    List<Round> actualRounds = buildRoundWithAllDetails();
    Round round = actualRounds.get( 0 );
    Round savedRound = getRoundDAO().save( round );
    boolean isPayoutIssued = getRoundDAO().isRoundPaidForDivisionPayouts( savedRound.getDivision().getPromotion().getId(), savedRound.getRoundNumber() );
    assertTrue( "Payout Not Issued", isPayoutIssued );
    flushAndClearSession();
  }

  public void testIsRoundCompleted()
  {
    List<Round> actualRounds = buildRoundWithAllDetails();
    Round round = actualRounds.get( 0 );
    round.setStartDate( new Date() );
    round.setEndDate( new Date() );
    Round savedRound = getRoundDAO().save( round );
    boolean roundCompleted = getRoundDAO().isRoundCompleted( savedRound.getDivision().getPromotion().getId(), savedRound.getRoundNumber() );
    assertFalse( "Round Completed", roundCompleted );
    flushAndClearSession();
  }

  public void testIsRoundStarted()
  {
    List<Round> actualRounds = buildRoundWithAllDetails();
    Round round = actualRounds.get( 0 );
    round.setStartDate( new Date() );
    round.setEndDate( new Date() );
    Round savedRound = getRoundDAO().save( round );
    boolean roundStarted = getRoundDAO().isRoundStarted( savedRound.getDivision().getPromotion().getId(), savedRound.getRoundNumber() );
    assertFalse( "Round Started", roundStarted );
    flushAndClearSession();
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

  /**
   * Get the DivisionDAO.
   * 
   * @return DivisionDAO
   */
  private static DivisionDAO getDivisionDAO()
  {
    return (DivisionDAO)ApplicationContextFactory.getApplicationContext().getBean( "divisionDAO" );
  }

  /**
   * Get the PromotionDAO.
   * 
   * @return PromotionDAO
   */
  private static PromotionDAO getPromotionDAO()
  {
    return (PromotionDAO)ApplicationContextFactory.getApplicationContext().getBean( "promotionDAO" );
  }
}
