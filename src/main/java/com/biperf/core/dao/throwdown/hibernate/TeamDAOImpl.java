
package com.biperf.core.dao.throwdown.hibernate;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.biperf.core.dao.throwdown.Standing;
import com.biperf.core.dao.throwdown.TeamDAO;
import com.biperf.core.dao.throwdown.TeamStats;
import com.biperf.core.domain.enums.ParticipantStatus;
import com.biperf.core.domain.enums.SortByType;
import com.biperf.core.domain.enums.SortOnType;
import com.biperf.core.domain.promotion.Match;
import com.biperf.core.domain.promotion.Team;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.throwdown.TeamMatching;
import com.biperf.core.service.throwdown.TeamProgress;
import com.biperf.core.utils.HibernateSessionManager;

public class TeamDAOImpl implements TeamDAO
{
  private static final String TEAM_STATS_SELECT = "SELECT OUTCOME_TYPE, COUNT(*) AS COUNT FROM THROWDOWN_MATCH_TEAM_OUTCOME WHERE TEAM_ID=? AND PROMOTION_ID=? GROUP BY OUTCOME_TYPE";

  private static final String STANDINGS_FOR_PROMOTION = "SELECT tt.team_id, tt.user_id, appuser.first_name AS firstName, appuser.last_name  AS lastName, pax.avatar_small, SUM (DECODE (tmto.outcome_type, 'win', 1, 0)) AS wins,"
      + "SUM (DECODE (tmto.outcome_type, 'loss', 1, 0)) AS losses," + "SUM (DECODE (tmto.outcome_type, 'tie', 1, 0)) AS ties," + "SUM (DECODE (tmto.outcome_type, 'none', 1, 0)) AS none"
      + " FROM throwdown_division td,throwdown_round tr,throwdown_match tm,throwdown_match_team_outcome tmto, throwdown_team tt, application_user appuser, participant pax "
      + "WHERE tmto.promotion_id = ? AND td.division_id = tr.division_id AND tr.round_id = tm.round_id AND tm.match_id = tmto.match_id "
      + "AND tmto.team_id = tt.team_id AND tt.user_id = appuser.user_id AND pax.user_id = appuser.user_id GROUP BY tt.team_id, tt.user_id,appuser.first_name, appuser.last_name, pax.avatar_small";

  private static final String STANDINGS_FOR_DIVISION = "SELECT tt.team_id, tt.user_id, appuser.first_name, appuser.last_name, pax.avatar_small, SUM (DECODE (tmto.outcome_type, 'win', 1, 0)) Wins,"
      + "SUM (DECODE (tmto.outcome_type, 'loss', 1, 0)) losses," + "SUM (DECODE (tmto.outcome_type, 'tie', 1, 0)) ties," + "SUM (DECODE (tmto.outcome_type, 'none', 1, 0)) none"
      + " FROM throwdown_division td,throwdown_round tr,throwdown_match tm,throwdown_match_team_outcome tmto, throwdown_team tt, application_user appuser, participant pax "
      + "WHERE td.division_id = ? AND td.division_id = tr.division_id AND tr.round_id = tm.round_id AND tm.match_id = tmto.match_id "
      + "AND tmto.team_id = tt.team_id AND tt.user_id = appuser.user_id AND pax.user_id = appuser.user_id GROUP BY tt.team_id, tt.user_id,appuser.first_name, appuser.last_name, pax.avatar_small ORDER BY wins DESC, ties DESC, losses ASC";

  private static final String STANDINGS_FOR_TEAM = "SELECT tt.team_id, tt.user_id, appuser.first_name AS firstName, appuser.last_name AS lastName, pax.avatar_small, SUM (DECODE (tmto.outcome_type, 'win', 1, 0)) AS wins,"
      + "SUM (DECODE (tmto.outcome_type, 'loss', 1, 0)) AS losses," + "SUM (DECODE (tmto.outcome_type, 'tie', 1, 0)) AS ties," + "SUM (DECODE (tmto.outcome_type, 'none', 1, 0)) AS none"
      + " FROM throwdown_division td,throwdown_round tr,throwdown_match tm,throwdown_match_team_outcome tmto, throwdown_team tt, application_user appuser, participant pax "
      + "WHERE tt.team_id = ? AND td.division_id = tr.division_id AND tr.round_id = tm.round_id AND tm.match_id = tmto.match_id "
      + "AND tmto.team_id = tt.team_id AND tt.user_id = appuser.user_id AND pax.user_id = appuser.user_id GROUP BY tt.team_id, tt.user_id,appuser.first_name, appuser.last_name, pax.avatar_small";

  private static final String SCHEDULE_BASIS = "SELECT team_id, count FROM (SELECT tmto2.team_id,COUNT(*) COUNT FROM throwdown_match_team_outcome tmto, "
      + "throwdown_match_team_outcome tmto2, throwdown_team tt WHERE tt.division_id = ? AND tt.team_id = ? AND tmto.team_id = tt.team_id "
      + "AND tmto.match_id = tmto2.match_id AND tmto.team_id<> tmto2.team_id GROUP BY tmto2.team_id UNION select team_id,0 count from throwdown_team "
      + "where division_id = ? AND team_id NOT IN (select team_id from throwdown_match_team_outcome where match_id in (select match_id from throwdown_match_team_outcome where team_id = ?) "
      + ") ) ORDER BY count,DBMS_RANDOM.VALUE";

  private static final String PAX_PLAYING_FOR_PROMO_IN_ROUND = "select team.user_id  from throwdown_match_team_outcome outcome, throwdown_match match, throwdown_round round, "
      + "throwdown_team team where outcome.promotion_id = ? and round.round_number = ? and team.is_shadow_player = 0 and "
      + "outcome.match_id = match.match_id and match.round_id = round.round_id and outcome.team_id = team.team_id";

  private static final String PAX_CUMULATIVE_PROGRESS_UPTO_ROUND = "select team.user_id, sum(nvl(outcome.current_value,0)) progress from throwdown_match_team_outcome outcome, throwdown_match match, throwdown_round round, "
      + "throwdown_team team where outcome.promotion_id = ? and round.round_number <= ? and team.is_shadow_player = 0 and "
      + "outcome.match_id = match.match_id and match.round_id = round.round_id and outcome.team_id = team.team_id " + "group by team.user_id order by to_number(progress) desc";

  private JdbcTemplate jdbcTemplate;

  public List<Long> getPaxPlayingPromotionInRound( Long promotionId, Integer roundNumber )
  {
    List<Long> allPax = jdbcTemplate.query( PAX_PLAYING_FOR_PROMO_IN_ROUND, new Object[] { promotionId, roundNumber }, new RowMapper<Long>()
    {
      public Long mapRow( ResultSet rs, int rowNum ) throws SQLException
      {
        return rs.getLong( "USER_ID" );
      }
    } );
    return allPax;
  }

  public List<TeamProgress> getAllPaxsCumulativeProgressUptoRound( Long promotionId, Integer roundNumber )
  {
    List<TeamProgress> allTeamProgress = jdbcTemplate.query( PAX_CUMULATIVE_PROGRESS_UPTO_ROUND, new Object[] { promotionId, roundNumber }, new RowMapper<TeamProgress>()
    {
      public TeamProgress mapRow( ResultSet rs, int rowNum ) throws SQLException
      {
        TeamProgress teamProgress = new TeamProgress();
        teamProgress.setUserId( rs.getLong( "USER_ID" ) );
        teamProgress.setProgress( rs.getBigDecimal( "PROGRESS" ) );
        return teamProgress;
      }
    } );
    return allTeamProgress;
  }

  private static String getStandingsForTeam( SortByType sortedByType, SortOnType sortOnType )
  {
    StringBuilder sql = new StringBuilder();
    sql.append( STANDINGS_FOR_TEAM );
    sql.append( buildSort( sortedByType, sortOnType ) );
    return sql.toString();
  }

  private static String getStandingsForPromotion( SortByType sortedByType, SortOnType sortOnType )
  {
    StringBuilder sql = new StringBuilder();
    sql.append( STANDINGS_FOR_PROMOTION );
    sql.append( buildSort( sortedByType, sortOnType ) );
    return sql.toString();
  }

  private static String buildSort( SortByType sortedByType, SortOnType sortOnType )
  {
    String sortedOn = sortOnType.getValue();
    String sortedBy = sortedByType.getSortBy();
    StringBuilder sb = new StringBuilder();
    if ( sortOnType == SortOnType.LAST_NAME )
    {
      sortedOn = "upper(lastName)";
    }
    sb.append( " ORDER BY " + sortedOn + " " + sortedBy + " " );
    if ( sortOnType != SortOnType.LAST_NAME )
    {
      sb.append( ", upper(lastName) ASC, upper(firstName) ASC" );
    }
    else
    {
      sb.append( ", upper(firstName)" + " " + sortedBy + " " );
    }
    return sb.toString();
  }

  @Override
  public TeamStats getTeamStatsForPromotion( Long teamId, Long promotionId )
  {
    TeamStats stats = new TeamStats();
    List<Map<String, Object>> rows = jdbcTemplate.queryForList( TEAM_STATS_SELECT, new Object[] { teamId, promotionId } );
    for ( Map<String, Object> row : rows )
    {
      stats.setTypeProperty( (String)row.get( "OUTCOME_TYPE" ), ( (BigDecimal)row.get( "COUNT" ) ).intValue() );
    }
    return stats;
  }

  @Override
  public List<Standing> getStandingsForPromotion( Long promotionId, SortByType sortedByType, SortOnType sortOnType )
  {
    return getStandings( getStandingsForPromotion( sortedByType, sortOnType ), promotionId );
  }

  @Override
  public List<Standing> getStandingsForDivision( Long divisionId )
  {
    return getStandings( STANDINGS_FOR_DIVISION, divisionId );
  }

  @Override
  public List<Standing> getMyStandings( Long teamId, SortByType sortedByType, SortOnType sortedOnType )
  {
    return getStandings( getStandingsForTeam( sortedByType, sortedOnType ), teamId );
  }

  public List<Standing> getStandings( String query, Long primer )
  {
    List<Standing> standings = jdbcTemplate.query( query, new Object[] { primer }, new RowMapper<Standing>()
    {
      public Standing mapRow( ResultSet rs, int rowNum ) throws SQLException
      {
        Standing standing = new Standing();
        standing.setTeamId( rs.getLong( "TEAM_ID" ) );
        standing.setUserId( rs.getLong( "USER_ID" ) );
        standing.setFirstName( rs.getString( "FIRSTNAME" ) );
        standing.setLastName( rs.getString( "LASTNAME" ) );
        standing.setAvatarUrl( rs.getString( "AVATAR_SMALL" ) );
        TeamStats stats = new TeamStats();
        stats.setWins( rs.getInt( "WINS" ) );
        stats.setLosses( rs.getInt( "LOSSES" ) );
        stats.setTies( rs.getInt( "TIES" ) );
        stats.setNone( rs.getInt( "NONE" ) );
        standing.setStats( stats );
        return standing;
      }
    } );

    return standings;
  }

  public List<TeamMatching> getTeamMatchingForTeamInDivision( Long divisionId, Long teamId )
  {
    List<TeamMatching> teamMatches = jdbcTemplate.query( SCHEDULE_BASIS, new Object[] { divisionId, teamId, divisionId, teamId }, new RowMapper<TeamMatching>()
    {
      public TeamMatching mapRow( ResultSet rs, int rowNum ) throws SQLException
      {
        TeamMatching teamMatching = new TeamMatching();
        teamMatching.setCompetitor( getTeam( new Long( rs.getLong( "TEAM_ID" ) ) ) );
        teamMatching.setNumberOfMatches( rs.getInt( "COUNT" ) );
        return teamMatching;
      }
    } );
    return teamMatches;
  }

  @Override
  public Team getRandomTeamForPromotionAndDivision( Long promotionId, Long divisionId )
  {
    Query query = HibernateSessionManager.getSession().getNamedQuery( "com.biperf.core.domain.throwdown.Team.randomForPromotionAndDivision" );
    query.setParameter( "divisionId", divisionId );
    query.setParameter( "promotionId", promotionId );
    return (Team)query.uniqueResult();
  }

  @Override
  public Match getCurrentMatchForTeam( Long teamId )
  {
    Date now = new Date();
    Session session = HibernateSessionManager.getSession();
    Criteria criteria = session.createCriteria( Match.class );
    criteria.createAlias( "teamOutcomes", "outcome" );
    criteria.add( Restrictions.eq( "outcome.team.id", teamId ) );
    criteria.createAlias( "round", "round" );
    criteria.add( Restrictions.lt( "round.startDate", now ) );
    criteria.add( Restrictions.gt( "round.endDate", now ) );
    return (Match)criteria.uniqueResult();
  }

  @SuppressWarnings( "unchecked" )
  @Override
  public List<Match> getTeamSchedule( Long promotionId, Long teamId )
  {
    Session session = HibernateSessionManager.getSession();
    Criteria criteria = session.createCriteria( Match.class );
    criteria.createAlias( "teamOutcomes", "outcome" );
    criteria.createAlias( "round", "round" );
    criteria.add( Restrictions.eq( "outcome.team.id", teamId ) );
    criteria.add( Restrictions.eq( "outcome.promotion.id", promotionId ) );
    criteria.addOrder( Order.asc( "round.startDate" ) );
    return criteria.list();
  }

  @Override
  public List<Match> getTeamSchedule( Long promotionId, Long teamId, AssociationRequestCollection associationRequestCollection )
  {
    List<Match> matches = getTeamSchedule( promotionId, teamId );
    for ( Match match : matches )
    {
      if ( associationRequestCollection != null )
      {
        associationRequestCollection.process( match );
      }
    }
    return matches;
  }

  @Override
  public Team getShadowPlayerForPromotionAndDivision( Long promotionId, Long divisionId )
  {
    Session session = HibernateSessionManager.getSession();
    Criteria criteria = session.createCriteria( Team.class );
    criteria.add( Restrictions.eq( "shadowPlayer", true ) );
    criteria.createAlias( "division", "division" );
    criteria.add( Restrictions.eq( "division.id", divisionId ) );
    criteria.createAlias( "division.promotion", "promotion" );
    criteria.add( Restrictions.eq( "division.promotion.id", promotionId ) );
    return (Team)criteria.uniqueResult();
  }

  @SuppressWarnings( "unchecked" )
  @Override
  public List<Team> findAllActiveTeamsAndPaxForPromotionAndDivision( Long promotionId, Long divisionId )
  {
    Session session = HibernateSessionManager.getSession();
    Criteria criteria = session.createCriteria( Team.class );
    criteria.add( Restrictions.eq( "active", true ) );
    criteria.add( Restrictions.eq( "shadowPlayer", false ) );
    criteria.createAlias( "division", "division" );
    criteria.add( Restrictions.eq( "division.id", divisionId ) );
    criteria.createAlias( "division.promotion", "promotion" );
    criteria.add( Restrictions.eq( "division.promotion.id", promotionId ) );
    criteria.createAlias( "participant", "participant" );
    criteria.add( Restrictions.eq( "participant.status", ParticipantStatus.lookup( ParticipantStatus.ACTIVE ) ) );
    return criteria.list();
  }

  @SuppressWarnings( "unchecked" )
  @Override
  public List<Team> findAllActiveTeamsForPromotionAndDivision( Long promotionId, Long divisionId )
  {
    Session session = HibernateSessionManager.getSession();
    Criteria criteria = session.createCriteria( Team.class );
    criteria.add( Restrictions.eq( "active", true ) );
    criteria.add( Restrictions.eq( "shadowPlayer", false ) );
    criteria.createAlias( "division", "division" );
    criteria.add( Restrictions.eq( "division.id", divisionId ) );
    criteria.createAlias( "division.promotion", "promotion" );
    criteria.add( Restrictions.eq( "division.promotion.id", promotionId ) );
    return criteria.list();
  }

  @SuppressWarnings( "unchecked" )
  @Override
  public List<Team> findAllActiveTeamsAndPaxForPromotionAndParticipants( Long promotionId, List<Long> participantIds )
  {
    Session session = HibernateSessionManager.getSession();
    Criteria criteria = session.createCriteria( Team.class );
    criteria.add( Restrictions.eq( "shadowPlayer", false ) );
    criteria.createAlias( "division", "division" );
    criteria.createAlias( "division.promotion", "promotion" );
    criteria.add( Restrictions.eq( "division.promotion.id", promotionId ) );
    criteria.createAlias( "participant", "participant" );
    criteria.add( Restrictions.in( "participant.id", participantIds ) );
    criteria.add( Restrictions.eq( "participant.status", ParticipantStatus.lookup( ParticipantStatus.ACTIVE ) ) );
    return criteria.list();
  }

  public List<Team> findAllActiveTeamsAndPaxForPromotionAndDivision( Long promotionId, Long divisionId, AssociationRequestCollection associationRequestCollection )
  {
    List<Team> teams = findAllActiveTeamsAndPaxForPromotionAndDivision( promotionId, divisionId );
    for ( Team team : teams )
    {
      if ( associationRequestCollection != null )
      {
        associationRequestCollection.process( team );
      }
    }
    return teams;
  }

  public Team getTeamByUserIdForPromotionWithAssociations( Long id, Long promotionId, AssociationRequestCollection associationRequestCollection )
  {
    Team team = getTeamByUserIdForPromotion( id, promotionId );
    if ( associationRequestCollection != null )
    {
      associationRequestCollection.process( team );
    }
    return team;
  }

  public Team getTeamByUserIdForPromotion( Long userId, Long promotionId )
  {
    // Session session = HibernateSessionManager.getSession();
    // Criteria criteria = session.createCriteria( Team.class );
    // criteria.createAlias( "participant", "participant" );
    // criteria.add( Restrictions.eq( "participant.id", userId ) );
    // criteria.createAlias( "matchTeamOutcomes", "matchTeamOutcomes" );
    // criteria.createAlias( "matchTeamOutcomes.promotion", "promotion" );
    // criteria.add( Restrictions.eq( "promotion.id", promotionId ) );
    // return (Team)criteria.list().get( 0 ) ;
    Query query = HibernateSessionManager.getSession().getNamedQuery( "com.biperf.core.domain.throwdown.Team.getTeamByUserIdForPromotion" );
    query.setParameter( "promotionId", promotionId );
    query.setParameter( "userId", userId );
    Team team = (Team)query.uniqueResult();
    if ( team != null )
    {
      Hibernate.initialize( team.getDivision().getPromotion() );
    }
    return team;
  }

  @SuppressWarnings( "unchecked" )
  public List<Team> getTeamsByUserIdsForPromotion( Set<Long> userIds, Long promotionId )
  {
    Query query = HibernateSessionManager.getSession().getNamedQuery( "com.biperf.core.domain.throwdown.Team.getTeamsByUserIdsForPromotion" );
    query.setParameter( "promotionId", promotionId );
    query.setParameterList( "userIds", userIds );
    return query.list();
  }

  public boolean isTeamUndefeatedTillNow( Long teamId, Long promotionId, Integer currentRound )
  {
    Query query = HibernateSessionManager.getSession().getNamedQuery( "com.biperf.core.domain.throwdown.Team.isTeamUndefeatedTillNow" );
    query.setParameter( "teamId", teamId );
    query.setParameter( "promotionId", promotionId );
    query.setParameter( "currentRound", currentRound );
    return ( (Long)query.uniqueResult() ).longValue() == 0;
  }

  @Override
  public Team getTeam( Long id )
  {
    Session session = HibernateSessionManager.getSession();
    return (Team)session.get( Team.class, id );
  }

  @Override
  public Team getTeam( Long id, AssociationRequestCollection associationRequestCollection )
  {
    Session session = HibernateSessionManager.getSession();
    Team team = (Team)session.get( Team.class, id );
    if ( associationRequestCollection != null )
    {
      associationRequestCollection.process( team );
    }
    return team;
  }

  @Override
  public Team save( Team team )
  {
    Session session = HibernateSessionManager.getSession();
    session.save( team );
    return team;
  }

  public void setDataSource( DataSource dataSource )
  {
    this.jdbcTemplate = new JdbcTemplate( dataSource );
  }
}
