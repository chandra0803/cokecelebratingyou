
package com.biperf.core.dao.throwdown.hibernate;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.biperf.core.dao.hibernate.BaseDAOTest;
import com.biperf.core.dao.participant.ParticipantDAO;
import com.biperf.core.dao.participant.hibernate.ParticipantDAOImplTest;
import com.biperf.core.dao.promotion.PromotionDAO;
import com.biperf.core.dao.promotion.hibernate.PromotionDAOImplTest;
import com.biperf.core.dao.throwdown.DivisionDAO;
import com.biperf.core.dao.throwdown.TeamDAO;
import com.biperf.core.domain.enums.ParticipantStatus;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.Division;
import com.biperf.core.domain.promotion.Match;
import com.biperf.core.domain.promotion.MatchTeamOutcome;
import com.biperf.core.domain.promotion.Team;
import com.biperf.core.domain.promotion.ThrowdownPromotion;
import com.biperf.core.utils.ApplicationContextFactory;

public class TeamDAOImplTest extends BaseDAOTest
{

  public void testTeamMethods()
  {
    MatchTeamOutcome matchTeamOutcome = MatchTeamOutcomeDAOImplTest.buildMatchTeamOutcomeWithDetails();

    Team savedTeam = matchTeamOutcome.getTeam();

    Team expectedTeam = getTeamDAO().getTeam( savedTeam.getId() );

    assertSame( "Expected team is same as actual team", expectedTeam, savedTeam );

    boolean teamUndefeatedInH2H = getTeamDAO().isTeamUndefeatedTillNow( savedTeam.getId(), savedTeam.getDivision().getPromotion().getId(), 1 );

    assertEquals( true, teamUndefeatedInH2H );

    Set<Long> userIds = new HashSet<Long>();

    Participant participant = savedTeam.getParticipant();

    Long userId = participant.getId();
    userIds.add( userId );
    List<Long> participantIds = new ArrayList<Long>();
    participantIds.addAll( userIds );

    List<Team> expectedTeamList = getTeamDAO().getTeamsByUserIdsForPromotion( userIds, savedTeam.getDivision().getPromotion().getId() );
    assertTrue( "Size of expected team list is", expectedTeamList.size() == 1 );

    Team expectedTeam1 = getTeamDAO().getTeamByUserIdForPromotion( participant.getId(), savedTeam.getDivision().getPromotion().getId() );
    assertSame( "Expected team is same as actual team", expectedTeam1, savedTeam );

    List<Team> expectedTeamList2 = getTeamDAO().findAllActiveTeamsForPromotionAndDivision( savedTeam.getDivision().getPromotion().getId(), savedTeam.getDivision().getId() );
    assertTrue( "Size of expected team list is", expectedTeamList2.size() == 1 );

    List<Match> teamShedule = getTeamDAO().getTeamSchedule( savedTeam.getDivision().getPromotion().getId(), savedTeam.getId() );
    assertTrue( "Size of expected Active Team list is", teamShedule.size() == 1 );

    Match actualMatch = matchTeamOutcome.getMatch();
    Match expectedMatch = getTeamDAO().getCurrentMatchForTeam( savedTeam.getId() );
    assertSame( "Expected match is same as actual match", expectedMatch, actualMatch );

  }

  public void testGetShadowPlayerForPromotionAndDivision()
  {
    Team team = buildTeam();
    team.setShadowPlayer( true );
    getTeamDAO().save( team );
    Team shadowPlayer = getTeamDAO().getShadowPlayerForPromotionAndDivision( team.getDivision().getPromotion().getId(), team.getDivision().getId() );
    assertSame( "Expected team is same as actual team", shadowPlayer, team );
  }

  public void testFindAllActiveTeamsAndPaxForPromotionAndDivision()
  {
    Team team = buildTeam();
    getTeamDAO().save( team );
    List<Team> activeTeams = getTeamDAO().findAllActiveTeamsAndPaxForPromotionAndDivision( team.getDivision().getPromotion().getId(), team.getDivision().getId() );
    assertTrue( "Size of expected Active Team list is", activeTeams.size() == 1 );
  }

  public void testFindAllActiveTeamsForPromotionAndDivision()
  {
    Team team = buildTeam();
    getTeamDAO().save( team );
    List<Team> activeTeams = getTeamDAO().findAllActiveTeamsForPromotionAndDivision( team.getDivision().getPromotion().getId(), team.getDivision().getId() );
    assertTrue( "Size of expected Active Team list is", activeTeams.size() == 1 );
  }

  public void testGetRandomTeamForPromotionAndDivision()
  {
    Team team = buildTeam();
    getTeamDAO().save( team );
    Team randomTeam = getTeamDAO().getRandomTeamForPromotionAndDivision( team.getDivision().getPromotion().getId(), team.getDivision().getId() );
    assertEquals( "Expected team is same as actual team", randomTeam, team );
  }

  public static Team buildTeam()
  {
    Team team = new Team();
    ThrowdownPromotion tdPromo = PromotionDAOImplTest.buildThrowdownPromotion( buildUniqueString() );
    Division division1 = DivisionDAOImplTest.buildDivision( buildUniqueString() );
    division1.setPromotion( tdPromo );
    tdPromo.addDivision( division1 );
    Participant participant = ParticipantDAOImplTest.buildUniqueParticipant( buildUniqueString() );
    participant.setStatus( ParticipantStatus.lookup( ParticipantStatus.ACTIVE ) );
    getPromotionDAO().save( tdPromo );
    getDivisionDAO().save( division1 );
    getParticipantDAO().saveParticipant( participant );
    team.setDivision( division1 );
    team.setParticipant( participant );
    team.setActive( true );
    return team;

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
