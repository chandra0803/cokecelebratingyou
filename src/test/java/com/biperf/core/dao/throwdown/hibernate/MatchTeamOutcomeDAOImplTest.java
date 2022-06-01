/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javatest/com/biperf/core/dao/throwdown/hibernate/MatchTeamOutcomeDAOImplTest.java,v $
 */

package com.biperf.core.dao.throwdown.hibernate;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import com.biperf.core.dao.hibernate.BaseDAOTest;
import com.biperf.core.dao.participant.ParticipantDAO;
import com.biperf.core.dao.participant.hibernate.ParticipantDAOImplTest;
import com.biperf.core.dao.throwdown.MatchDAO;
import com.biperf.core.dao.throwdown.MatchTeamOutcomeDAO;
import com.biperf.core.dao.throwdown.TeamDAO;
import com.biperf.core.domain.AuditCreateInfo;
import com.biperf.core.domain.enums.MatchTeamOutcomeType;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.Match;
import com.biperf.core.domain.promotion.MatchTeamOutcome;
import com.biperf.core.domain.promotion.Team;
import com.biperf.core.utils.ApplicationContextFactory;

public class MatchTeamOutcomeDAOImplTest extends BaseDAOTest
{
  public static Participant pax;

  public static MatchTeamOutcome buildMatchTeamOutcomeWithDetails()
  {
    MatchTeamOutcome matchTeamOutcome = new MatchTeamOutcome();
    Team team = new Team();
    pax = ParticipantDAOImplTest.buildUniqueParticipant( buildUniqueString() );
    getParticipantDAO().saveParticipant( pax );
    team.setParticipant( pax );
    team.setActive( true );
    List<Match> matches = MatchDAOImplTest.buildMatchesWithAllDetails();
    for ( Match match : matches )
    {
      getMatchDAO().save( match );
    }
    team.setDivision( matches.get( 0 ).getRound().getDivision() );
    getTeamDAO().save( team );
    matchTeamOutcome.setMatch( matches.get( 0 ) );
    matchTeamOutcome.setPromotion( matches.get( 0 ).getRound().getDivision().getPromotion() );
    matchTeamOutcome.setTeam( team );
    matchTeamOutcome.setOutcome( MatchTeamOutcomeType.lookup( MatchTeamOutcomeType.WIN ) );
    AuditCreateInfo auditCreateInfo = new AuditCreateInfo();
    auditCreateInfo.setCreatedBy( new Long( 5662 ) );
    auditCreateInfo.setDateCreated( new Timestamp( new Date().getTime() ) );
    matchTeamOutcome.setAuditCreateInfo( auditCreateInfo );
    matchTeamOutcome.setVersion( new Long( 1 ) );
    getMatchTeamOutcomeDAO().save( matchTeamOutcome );
    return matchTeamOutcome;
  }

  public void testGetMatchTeamOutcome()
  {
    MatchTeamOutcome actualMatchTeamOutcome = buildMatchTeamOutcomeWithDetails();
    MatchTeamOutcome expectedMatchTeamOutcome = getMatchTeamOutcomeDAO().getMatchTeamOutcome( actualMatchTeamOutcome.getId() );
    assertEquals( "Actual matchTeamOutcome is equal to what was expected", actualMatchTeamOutcome, expectedMatchTeamOutcome );
  }

  public void testGetOutcomeForMatch()
  {
    MatchTeamOutcome actualMatchTeamOutcome = buildMatchTeamOutcomeWithDetails();
    MatchTeamOutcome expectedMatchTeamOutcome = getMatchTeamOutcomeDAO()
        .getOutcomeForMatch( new Long( 100 ), actualMatchTeamOutcome.getPromotion().getId(), actualMatchTeamOutcome.getMatch().getRound().getRoundNumber() );
    assertNotSame( "Actual matchTeamOutcome is not equal to what was expected", actualMatchTeamOutcome, expectedMatchTeamOutcome );
  }

  public void testGetOutcomeForTeamInSpecificRound()
  {
    MatchTeamOutcome actualMatchTeamOutcome = buildMatchTeamOutcomeWithDetails();
    MatchTeamOutcome expectedMatchTeamOutcome1 = getMatchTeamOutcomeDAO().getOutcomeForTeamInSpecificRound( new Long( 100 ), actualMatchTeamOutcome.getMatch().getRound().getId() );
    assertNotSame( "Actual matchTeamOutcome is not equal to what was expected", actualMatchTeamOutcome, expectedMatchTeamOutcome1 );
    MatchTeamOutcome expectedMatchTeamOutcome2 = getMatchTeamOutcomeDAO().getOutcomeForTeamInSpecificRound( actualMatchTeamOutcome.getTeam().getParticipant().getId(),
                                                                                                            actualMatchTeamOutcome.getMatch().getRound().getRoundNumber(),
                                                                                                            actualMatchTeamOutcome.getPromotion().getId() );
    assertSame( "Actual matchTeamOutcome is equal to what was expected", actualMatchTeamOutcome, expectedMatchTeamOutcome2 );

  }

  /**
   * Get the MatchTeamOutcomeDAO.
   * 
   * @return MatchTeamOutcomeDAO
   */
  private static MatchTeamOutcomeDAO getMatchTeamOutcomeDAO()
  {
    return (MatchTeamOutcomeDAO)ApplicationContextFactory.getApplicationContext().getBean( "matchTeamOutcomeDAO" );
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
   * Get the TeamDAO.
   * 
   * @return TeamDAO
   */
  private static TeamDAO getTeamDAO()
  {
    return (TeamDAO)ApplicationContextFactory.getApplicationContext().getBean( "teamDAO" );
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

}
