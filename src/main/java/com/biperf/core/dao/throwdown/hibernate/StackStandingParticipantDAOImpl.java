
package com.biperf.core.dao.throwdown.hibernate;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.biperf.core.dao.BaseDAO;
import com.biperf.core.dao.throwdown.StackStandingParticipantDAO;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.StackStandingParticipant;
import com.biperf.core.utils.HibernateSessionManager;
import com.biperf.core.utils.hibernate.HibernateUtil;

public class StackStandingParticipantDAOImpl extends BaseDAO implements StackStandingParticipantDAO
{

  private static final String GET_ALL_RANKING_PARTICIPANTS = "select user_id, stackrank_factor, rank, payout, payouts_issued "
      + " from throwdown_stackrank_pax rankpax where stackrank_node_id = ? order by rank asc";

  private static final String GET_PAGE_RANKING_PARTICIPANTS = "select * from ( select rownum rn,ranker1.* from ( select user_id, stackrank_factor, rank, payout, payouts_issued "
      + " from throwdown_stackrank_pax rankpax where stackrank_node_id = ? order by rank asc  ) ranker1 ) ranker2 where ranker2.rn  >= ? and ranker2.rn <= ?";

  private static final String TOP_RANKING_PARTICIPANTS = "select * from ( select rownum rn,ranker1.* from ( select user_id, stackrank_factor, rank, payout, payouts_issued "
      + " from throwdown_stackrank_pax rankpax where stackrank_node_id = ? order by rank asc  ) ranker1 ) ranker2 where ranker2.rn  >= 1 and ranker2.rn <= 4";

  private static final String TOTAL_RANKING_PARTICIPANTS = "select count(*) from throwdown_stackrank_pax rankpax where stackrank_node_id = ?";

  private static final String PAX_POSITION_IN_RANKING_PARTICIPANTS = "select * from ( select rownum position,ranker1.* from ( select user_id, rank "
      + " from throwdown_stackrank_pax rankpax where stackrank_node_id = ? order by rank asc  ) ranker1 ) ranker2 where ranker2.user_id = ?";

  private static final String GET_HIERARCHY_RANK_DETAILS_FOR_PAX = "select pax.user_id, pax.stackrank_factor, pax.rank, pax.payout, pax.payouts_issued from throwdown_stackrank_pax pax, throwdown_stackrank_node ranknode, "
      + " throwdown_stackrank ranking where ranking.promotion_id = ? and ranking.round_number = ? and ranking.is_active = 1 and ranknode.node_id is null and pax.user_id = ? and "
      + " pax.stackrank_node_id = ranknode.stackrank_node_id and ranknode.stackrank_id = ranking.stackrank_id";

  private JdbcTemplate jdbcTemplate;

  public int getPaxPositionInRanking( Long rankingNodeId, Long userId )
  {
    List<Map<String, Object>> rows = jdbcTemplate.queryForList( PAX_POSITION_IN_RANKING_PARTICIPANTS, new Object[] { rankingNodeId, userId } );
    if ( !rows.isEmpty() )
    {
      return ( (BigDecimal)rows.iterator().next().get( "position" ) ).intValue();
    }
    return 0;
  }

  public int getTotalRankingParticipants( Long rankingNodeId )
  {
    return jdbcTemplate.queryForInt( TOTAL_RANKING_PARTICIPANTS, new Object[] { rankingNodeId } );
  }

  private StackStandingParticipant populateRankingParticipant( ResultSet rs ) throws SQLException
  {
    StackStandingParticipant rankingPax = new StackStandingParticipant();
    Participant pax = new Participant();
    pax.setId( rs.getLong( "USER_ID" ) );
    rankingPax.setParticipant( pax );
    rankingPax.setStackStandingFactor( rs.getBigDecimal( "STACKRANK_FACTOR" ) );
    rankingPax.setStanding( rs.getInt( "RANK" ) );
    rankingPax.setPayout( rs.getLong( "PAYOUT" ) );
    rankingPax.setPayoutsIssued( rs.getBoolean( "PAYOUTS_ISSUED" ) );
    return rankingPax;
  }

  public List<StackStandingParticipant> getTopRankingParticipants( Long rankingNodeId )
  {
    List<StackStandingParticipant> participants = jdbcTemplate.query( TOP_RANKING_PARTICIPANTS, new Object[] { rankingNodeId }, new RowMapper<StackStandingParticipant>()
    {
      public StackStandingParticipant mapRow( ResultSet rs, int rowNum ) throws SQLException
      {
        return populateRankingParticipant( rs );
      }
    } );
    return participants;
  }

  public List<StackStandingParticipant> getPageRankingParticipants( Long rankingNodeId, int fromIndex, int endIndex )
  {
    List<StackStandingParticipant> participants = jdbcTemplate.query( GET_PAGE_RANKING_PARTICIPANTS, new Object[] { rankingNodeId, fromIndex, endIndex }, new RowMapper<StackStandingParticipant>()
    {
      public StackStandingParticipant mapRow( ResultSet rs, int rowNum ) throws SQLException
      {
        return populateRankingParticipant( rs );
      }
    } );
    return participants;
  }

  public List<StackStandingParticipant> getAllRankingParticipants( Long rankingNodeId )
  {
    List<StackStandingParticipant> participants = jdbcTemplate.query( GET_ALL_RANKING_PARTICIPANTS, new Object[] { rankingNodeId }, new RowMapper<StackStandingParticipant>()
    {
      public StackStandingParticipant mapRow( ResultSet rs, int rowNum ) throws SQLException
      {
        return populateRankingParticipant( rs );
      }
    } );
    return participants;
  }

  public StackStandingParticipant getHierarchyRankDetailsForPax( Long promotionId, int roundNumber, Long userId )
  {
    Object[] objects = new Object[] { promotionId, roundNumber, userId };
    RowMapper<StackStandingParticipant> rowMapper = new RowMapper<StackStandingParticipant>()
    {
      public StackStandingParticipant mapRow( ResultSet rs, int rowNum ) throws SQLException
      {
        return populateRankingParticipant( rs );
      }
    };
    StackStandingParticipant participantRankDetail = jdbcTemplate.queryForObject( GET_HIERARCHY_RANK_DETAILS_FOR_PAX, objects, rowMapper );
    return participantRankDetail;
  }

  @Override
  public StackStandingParticipant getStackStandingParticipant( Long stackStandingParticipantId )
  {
    return (StackStandingParticipant)getSession().get( StackStandingParticipant.class, stackStandingParticipantId );
  }

  @Override
  public StackStandingParticipant saveStackStandingParticipant( StackStandingParticipant stackStandingParticipant )
  {
    return (StackStandingParticipant)HibernateUtil.saveOrUpdateOrShallowMerge( stackStandingParticipant );
  }

  @Override
  public StackStandingParticipant getStackStandingParticipant( Long stackStandingNodeId, Long userId )
  {
    Criteria criteria = HibernateSessionManager.getSession().createCriteria( StackStandingParticipant.class );
    criteria.createAlias( "stackStandingNode", "stackStandingNode" );
    criteria.add( Restrictions.eq( "stackStandingNode.id", stackStandingNodeId ) );
    criteria.createAlias( "participant", "participant" );
    criteria.add( Restrictions.eq( "participant.id", userId ) );
    return (StackStandingParticipant)criteria.uniqueResult();
  }

  public void setDataSource( DataSource dataSource )
  {
    this.jdbcTemplate = new JdbcTemplate( dataSource );
  }

}
