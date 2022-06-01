
package com.biperf.core.service.throwdown.scheduler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.domain.enums.MatchStatusType;
import com.biperf.core.domain.enums.MatchTeamOutcomeType;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.Division;
import com.biperf.core.domain.promotion.Match;
import com.biperf.core.domain.promotion.MatchTeamOutcome;
import com.biperf.core.domain.promotion.Round;
import com.biperf.core.domain.promotion.Team;
import com.biperf.core.domain.promotion.ThrowdownPromotion;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.throwdown.ParticipantAudienceConflictResult;
import com.biperf.core.service.throwdown.TeamMatching;
import com.biperf.core.service.throwdown.TeamService;
import com.biperf.core.service.throwdown.ThrowdownService;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.HibernateSessionManager;

public class RandomThrowdownMatchScheduler implements ThrowdownMatchScheduler
{

  private Log log = LogFactory.getLog( RandomThrowdownMatchScheduler.class );

  private List<Long> teamIds = new ArrayList<Long>();
  private LinkedList<Long> top = new LinkedList<Long>();
  private LinkedList<Long> bottom = new LinkedList<Long>();

  @Override
  public Set<Match> scheduleHeadToHeadMatches( ThrowdownPromotion promotion )
  {
    Set<Match> matches = new HashSet<Match>();
    // for each division
    // for ( Division division : promotion.getDivisions() )
    // {
    // Set<Team> teams = getTeamsForDivision( promotion, division );
    // buildTeamIdList( teams ) ;
    // // each round
    // for ( Round round : division.getRounds() )
    // {
    // Set<Match> matchesForRound = schedule( round, teams, matches );
    // matches.addAll( matchesForRound );
    // shiftSchedules() ;
    // }
    // resetMatchAssignments() ;
    // }

    return matches;
  }

  @Override
  public Set<Match> scheduleIncrementalHeadToHeadMatches( Round round, List<Participant> potentialParticipantsForThisDivision, Set<ParticipantAudienceConflictResult> conflicts )
  {
    Set<Match> matches = new HashSet<Match>();
    List<Team> teams = getTeamsForDivision( round, potentialParticipantsForThisDivision, conflicts );
    if ( log.isInfoEnabled() )
    {
      log.info( "List of teams for division : " + teams );
    }
    // each Team
    for ( Team team : teams )
    {
      if ( log.isInfoEnabled() )
      {
        log.info( "Going to schedule match for team with id : " + team.getId() );
      }
      Match matchForRound = scheduleIncremental( round, team );
      if ( null != matchForRound )
      {
        getTeamService().saveMatch( matchForRound );
        matches.add( matchForRound );
        round.getMatches().add( matchForRound );
      }
    }
    return round.getMatches();
  }

  private Match scheduleIncremental( Round round, Team team )
  {
    // first check to make sure this team isn't already scheduled for this round
    if ( isScheduledForRound( round, team ) )
    {
      return null;
    }

    Match matchForRound = new Match();
    matchForRound.setRound( round );
    matchForRound.setStatus( MatchStatusType.lookup( MatchStatusType.NOT_PLAYED ) );
    // grab the first one...
    MatchTeamOutcome teamOutcome1 = buildMatchTeamOutcome( round, matchForRound );
    MatchTeamOutcome teamOutcome2 = buildMatchTeamOutcome( round, matchForRound );
    // get the next competitor for this team
    List<TeamMatching> teamMatching = getTeamService().getTeamMatchingForTeamInDivision( round.getDivision().getId(), team.getId() );

    if ( log.isInfoEnabled() )
    {
      log.info( "Team matching for team with id : " + team.getId() + " is " + teamMatching );
    }
    Team competitor = null;

    for ( int i = 0; i < teamMatching.size(); i++ )
    {
      TeamMatching teamMatchingInstance = teamMatching.get( i );
      // skip myself!
      if ( teamMatchingInstance.getCompetitor().getId().equals( team.getId() ) )
      {
        continue;
      }

      if ( !isScheduledForRound( round, teamMatchingInstance.getCompetitor() ) )
      {
        competitor = teamMatching.get( i ).getCompetitor();
        // Notes : Division is checked here too. Else may be we could rewrite query in
        // TeamDaoImpl(SCHEDULE_BASIS)
        // team is inactive OR
        // team is not shadow player and participant is inactive OR
        // team's division is different from the another user's team
        if ( !competitor.getActive() || !competitor.isShadowPlayer() && !competitor.getParticipant().isActive() || !competitor.getDivision().getId().equals( round.getDivision().getId() ) )
        {
          competitor = null;
          continue;
        }
        break;
      }
    }

    if ( log.isInfoEnabled() )
    {
      log.info( "Matched team for team with id : " + team.getId() + " is : " + competitor.getId() );
    }
    teamOutcome1.setTeam( team );
    teamOutcome2.setTeam( competitor );

    Set<MatchTeamOutcome> matchTeamOutcomes = new HashSet<MatchTeamOutcome>();
    matchTeamOutcomes.add( teamOutcome1 );
    matchTeamOutcomes.add( teamOutcome2 );
    matchForRound.setTeamOutcomes( matchTeamOutcomes );

    return matchForRound;
  }

  private boolean isScheduledForRound( Round round, Team team )
  {
    for ( Match match : round.getMatches() )
    {
      if ( match.getTeamOutcome( team.getId() ) != null )
      {
        return true;
      }
    }
    return false;
  }

  /*
   * http://en.wikipedia.org/wiki/Round-robin_tournament Here's the magic. Shifting the
   * assignments..see link above for appropriate explanation.
   */
  private void shiftSchedules()
  {
    // hold onto the first position
    Long topFirstPosition = top.removeFirst();
    // grab and remove the first position of the bottom array
    Long bottomFirstPosition = bottom.removeFirst();
    // add the first position of the bottom array to first of top array
    top.addFirst( bottomFirstPosition );
    // 'pin' the original first top position into the first spot again
    top.addFirst( topFirstPosition );
    // grab and remove the last spot on the top array
    Long topLastPosition = top.removeLast();
    bottom.addLast( topLastPosition );
  }

  private void buildTeamIdList( Set<Team> teams )
  {
    teamIds.clear();
    for ( Team team : teams )
    {
      teamIds.add( team.getId() );
    }
    Collections.shuffle( teamIds, new Random() );// randomize
    // now populate the top/bottom Deques
    int topSize = teamIds.size() / 2;
    for ( int i = 0; i < topSize; i++ )
    {
      top.add( teamIds.get( i ) );
      bottom.add( teamIds.get( i + topSize ) );
    }
  }

  private Team getTeam( Long teamId, Set<Team> teams )
  {
    for ( Team team : teams )
    {
      if ( team.getId().equals( teamId ) )
      {
        return team;
      }
    }
    return null;
  }

  private MatchTeamOutcome buildMatchTeamOutcome( Round round, Match match )
  {
    MatchTeamOutcome matchTeamOutcome = new MatchTeamOutcome();
    matchTeamOutcome.setMatch( match );
    matchTeamOutcome.setOutcome( MatchTeamOutcomeType.lookup( MatchTeamOutcomeType.NONE ) );
    matchTeamOutcome.setPromotion( round.getDivision().getPromotion() );
    matchTeamOutcome.setCurrentValue( null );
    return matchTeamOutcome;
  }

  private List<Team> getTeamsForDivision( Round round, List<Participant> potentialParticipantsForThisDivision, Set<ParticipantAudienceConflictResult> conflicts )
  {
    ThrowdownPromotion promotion = round.getDivision().getPromotion();
    Division division = round.getDivision();

    // if round 1, well we should be valid - skip this stuff
    if ( round.getRoundNumber() > 1 )
    {
      List<Team> existingTeamsForDivision = getTeamService().findAllActiveTeamsForPromotionAndDivision( promotion.getId(), division.getId() );
      List<Long> participantIdsInDivision = new ArrayList<Long>();
      // step 1 - get the audience(s) and generate a list of Participant IDs for that audience(s)

      for ( Participant pax : potentialParticipantsForThisDivision )
      {
        participantIdsInDivision.add( pax.getId() );
        Team team = getTeamService().getTeamByUserIdForPromotion( pax.getId(), promotion.getId() ); // TODO:
                                                                                                    // get
                                                                                                    // all
                                                                                                    // in
                                                                                                    // 1
                                                                                                    // call
                                                                                                    // for
                                                                                                    // better
                                                                                                    // performance

        boolean hasConflicts = false;
        boolean wasActiveInPreviousDivision = false;
        Division previousDivision = null;
        ParticipantAudienceConflictResult conflictPax = new ParticipantAudienceConflictResult();
        conflictPax.setParticipant( pax );
        if ( conflicts.contains( conflictPax ) )
        {
          hasConflicts = true;
        }

        // if the team is null, then we need to add it. This also means we can just default to the
        // fist division on the list
        if ( team == null )
        {
          team = new Team();
          Participant participant = getParticipantService().getParticipantById( pax.getId() );
          team.setParticipant( participant );
          team.setDivision( division );
          team.setActive( true );
          team = getTeamService().saveTeam( team );
        }
        // else determine which division this team should be in
        else
        {
          wasActiveInPreviousDivision = team.getActive();
          previousDivision = team.getDivision();
          Division assignedDivision = getAppropriateDivisionForParticipant( team, round, conflicts, hasConflicts );
          team.setDivision( assignedDivision );
          team.setActive( true );
          team = getTeamService().saveTeam( team );
        }

        if ( hasConflicts )
        {
          for ( ParticipantAudienceConflictResult existing : conflicts )
          {
            if ( existing.equals( conflictPax ) )
            {
              existing.setWasActiveInPreviousDivision( wasActiveInPreviousDivision );
              existing.setPreviousDivision( previousDivision );
              existing.setAssignedDivision( team.getDivision() );
            }
          }
        }

      }

      // make teams inactive if they were active earlier for same division and not matching for
      // current round.
      for ( Team team : existingTeamsForDivision )
      {
        if ( !participantIdsInDivision.contains( team.getParticipant().getId() ) )
        {
          team.setActive( false );
        }
      }

    }
    // Now that all the teams for this division should be setup, lets grab them from the database
    List<Team> teams = getTeamService().findAllActiveTeamsAndPaxForPromotionAndDivision( promotion.getId(), division.getId() );

    // determine if we have an un-even number of teams and provide an impl if so
    if ( teams.size() % 2 != 0 )
    {
      teams.add( getTeamService().createOrFindActiveShadowPlayerForPromotionAndDivision( division.getPromotion().getId(), division.getId() ) );
    }
    else
    {
      Team shadowPlayer = getTeamService().getShadowPlayerForPromotionAndDivision( division.getPromotion().getId(), division.getId() );
      if ( shadowPlayer != null )
      {
        shadowPlayer.setActive( false );
      }
    }

    // make team changes available for match creation
    HibernateSessionManager.getSession().flush();

    return teams;
  }

  private Division getAppropriateDivisionForParticipant( Team team, Round round, Set<ParticipantAudienceConflictResult> conflicts, boolean hasConflicts )
  {
    Division divisionToAssignToTeam = null;

    Division lastAssociateDivision = team.getDivision();
    // if there is no conflicts or last associated division is same as current processing division,
    // return currently processing division.
    if ( !hasConflicts || lastAssociateDivision.getId().equals( round.getDivision().getId() ) )
    {
      return round.getDivision();
    }

    // proceed further for conflicts.
    for ( ParticipantAudienceConflictResult conflict : conflicts )
    {
      // does this conflicting participant/audience mapping occur in this division?
      if ( team.getParticipant().getId().equals( conflict.getParticipant().getId() ) ) // ok lets
                                                                                       // determine
                                                                                       // which
                                                                                       // Division
                                                                                       // this Team
                                                                                       // should be
                                                                                       // in
      {
        Set<Division> potentialDivisions = conflict.getDivisions();
        // determine if the participant belongs to this specific division or should be in another
        for ( Division potentialDivision : potentialDivisions )
        {
          if ( potentialDivision.getId().equals( lastAssociateDivision.getId() ) ) // when conflict
                                                                                   // - the last
                                                                                   // division wins
          {
            divisionToAssignToTeam = lastAssociateDivision;
          }
        }
        // ok the user is no longer in the lastAssociatedDivision, set them to the first one on the
        // list
        if ( divisionToAssignToTeam == null )
        {
          divisionToAssignToTeam = potentialDivisions.iterator().next();
        }
      }
    }
    return divisionToAssignToTeam;
  }

  public TeamService getTeamService()
  {
    return (TeamService)BeanLocator.getBean( TeamService.BEAN_NAME );
  }

  public ThrowdownService getThrowdownService()
  {
    return (ThrowdownService)BeanLocator.getBean( ThrowdownService.BEAN_NAME );
  }

  public ParticipantService getParticipantService()
  {
    return (ParticipantService)BeanLocator.getBean( ParticipantService.BEAN_NAME );
  }
}
