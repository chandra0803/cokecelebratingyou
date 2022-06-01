
package com.biperf.core.dao.ssi.hibernate;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;

import com.biperf.core.dao.ssi.SSIContestAwardThemNowDAO;
import com.biperf.core.domain.ssi.SSIContestAwardThemNow;
import com.biperf.core.domain.ssi.SSIContestParticipant;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.utils.SSIContestUtil;
import com.biperf.core.utils.StringUtil;
import com.biperf.core.value.ssi.SSIContestAwardHistoryTotalsValueBean;
import com.biperf.core.value.ssi.SSIContestParticipantValueBean;
import com.biperf.core.value.ssi.SSIContestPayoutObjectivesTotalsValueBean;

/**
 * 
 * SSIContestAwardThemNowDAOImpl.
 * 
 * @author kandhi
 * @since Feb 6, 2015
 * @version 1.0
 */
public class SSIContestAwardThemNowDAOImpl extends SSIContestDAOImpl implements SSIContestAwardThemNowDAO
{

  protected static final Log log = LogFactory.getLog( SSIContestAwardThemNowDAOImpl.class );

  private DataSource dataSource;
  private JdbcTemplate jdbcTemplate;

  private static String INSERT_CONTEST_PARTICIPANT = "INSERT INTO SSI_CONTEST_PARTICIPANT (SSI_CONTEST_PARTICIPANT_ID, SSI_CONTEST_ID, AWARD_ISSUANCE_NUMBER, USER_ID, CREATED_BY, DATE_CREATED, VERSION) VALUES ( SSI_CONTEST_PARTICIPANT_PK_SQ.NEXTVAL, ?, ?, ?, ?, sysdate, 1)";
  private static String DELETE_CONTEST_PARTICIPANT = "DELETE FROM SSI_CONTEST_PARTICIPANT WHERE SSI_CONTEST_ID=? AND SSI_CONTEST_PARTICIPANT_ID=? AND AWARD_ISSUANCE_NUMBER=?";
  private static String UPDATE_ATN_CONTEST_STATUS = "UPDATE SSI_CONTEST_ATN SET ISSUANCE_STATUS=? WHERE SSI_CONTEST_ID=? AND ISSUANCE_NUMBER=?";
  private static String UPDATE_ATN_CONTEST_ISSUANCE_NUMBER = "UPDATE SSI_CONTEST SET AWARD_ISSUANCE_NUMBER=? WHERE SSI_CONTEST_ID=?";
  private static String UPDATE_ATN_CONTEST = "UPDATE SSI_CONTEST_ATN SET NOTIFICATION_MESSAGE_TEXT = ? , ISSUANCE_DATE= sysdate WHERE SSI_CONTEST_ID=? AND ISSUANCE_NUMBER=?";

  public void setDataSource( DataSource dataSource )
  {
    this.dataSource = dataSource;
    this.jdbcTemplate = new JdbcTemplate( dataSource );
  }

  @Override
  public SSIContestAwardThemNow getContestAwardThemNow( Long contestId, Short awardIssuanceNumber )
  {
    Criteria criteria = getSession().createCriteria( SSIContestAwardThemNow.class );
    criteria.add( Restrictions.eq( "contest.id", contestId ) );
    criteria.add( Restrictions.eq( "issuanceNumber", awardIssuanceNumber ) );
    return (SSIContestAwardThemNow)criteria.uniqueResult();
  }

  @Override
  public SSIContestAwardThemNow saveContestAtn( SSIContestAwardThemNow contestAtn )
  {
    getSession().saveOrUpdate( contestAtn );
    return contestAtn;
  }

  @Override
  public void updateContestWithAwardIssuanceNumber( final Long contestId, final Short awardIssuanceNumber )
  {
    this.jdbcTemplate.update( UPDATE_ATN_CONTEST_ISSUANCE_NUMBER, new PreparedStatementSetter()
    {
      @Override
      public void setValues( PreparedStatement ps ) throws SQLException
      {
        ps.setShort( 1, awardIssuanceNumber );
        ps.setLong( 2, contestId );
      }
    } );
  }

  @Override
  public void updateAwardThemNowContestStatus( final Long contestId, final Short awardIssuanceNumber, final String status )
  {
    this.jdbcTemplate.update( UPDATE_ATN_CONTEST_STATUS, new PreparedStatementSetter()
    {
      @Override
      public void setValues( PreparedStatement ps ) throws SQLException
      {
        ps.setString( 1, status );
        ps.setLong( 2, contestId );
        ps.setShort( 3, awardIssuanceNumber );
      }
    } );
  }

  @Override
  public List<SSIContestParticipant> getContestParticipants( Long contestId, Short awardIssuanceNumber, Integer pageNumber, int pageSize, String sortColumnName, String sortOrder )
  {
    Criteria criteria = getSession().createCriteria( SSIContestParticipant.class, "ssiContestParticipant" );
    criteria.setMaxResults( pageSize );

    if ( pageNumber > 1 )
    {
      criteria.setFirstResult( pageSize * ( pageNumber - 1 ) );
    }

    criteria.add( Restrictions.eq( "contest.id", contestId ) );
    criteria.add( Restrictions.eq( "awardIssuanceNumber", awardIssuanceNumber ) );

    if ( StringUtil.isNullOrEmpty( sortColumnName ) )
    {
      sortColumnName = SSIContestUtil.SORT_BY_LAST_NAME;
    }

    if ( StringUtil.isNullOrEmpty( sortOrder ) )
    {
      sortOrder = SSIContestUtil.DEFAULT_SORT_BY;
    }

    if ( SSIContestUtil.SORT_BY_LAST_NAME.equals( sortColumnName ) )
    {
      criteria.createAlias( "ssiContestParticipant.participant", "participant" );
      sortColumnName = "participant." + sortColumnName;
    }
    criteria.addOrder( SSIContestUtil.DEFAULT_SORT_BY.equals( sortOrder ) ? Order.asc( sortColumnName ) : Order.desc( sortColumnName ) );
    return criteria.list();
  }

  @Override
  public int getContestParticipantsCount( Long contestId, Short awardIssuanceNumber )
  {
    Criteria criteria = getSession().createCriteria( SSIContestParticipant.class );
    criteria.setProjection( Projections.rowCount() );
    criteria.add( Restrictions.eq( "contest.id", contestId ) );
    criteria.add( Restrictions.eq( "awardIssuanceNumber", awardIssuanceNumber ) );
    return ( (Long)criteria.uniqueResult() ).intValue();
  }

  /**
   * Doing a JDBC batch update for performance reasons. Hibernate would need the SSIContest objects loaded
   * {@inheritDoc}
   */
  @Override
  public void saveContestParticipants( final Long contestId, final Long[] participantIds, final Short awardIssuanceNumber )
  {
    this.jdbcTemplate.batchUpdate( INSERT_CONTEST_PARTICIPANT, new BatchPreparedStatementSetter()
    {
      @Override
      public void setValues( PreparedStatement ps, int i ) throws SQLException
      {
        ps.setLong( 1, contestId );
        ps.setShort( 2, awardIssuanceNumber );
        ps.setLong( 3, participantIds[i] );
        ps.setLong( 4, getLoggedInUserId() );
      }

      @Override
      public int getBatchSize()
      {
        return participantIds.length;
      }
    } );
  }

  @Override
  public Short getMaxAwardIssuanceNumber( Long contestId )
  {
    Short returnValue = null;
    Criteria crit = getSession().createCriteria( SSIContestParticipant.class );
    ProjectionList projList = Projections.projectionList();
    projList.add( Projections.max( "awardIssuanceNumber" ) );
    crit.setProjection( projList );
    Object maxNum = crit.uniqueResult();
    if ( maxNum != null )
    {
      returnValue = (short)maxNum;
    }
    return returnValue;
  }

  /**
   * Doing a JDBC update for performance reasons. Hibernate would need the SSIContest objects loaded
   * {@inheritDoc}
   */
  @Override
  public void deleteContestParticipant( final Long contestId, final Long participantId, final Short awardIssuanceNumber )
  {
    this.jdbcTemplate.update( DELETE_CONTEST_PARTICIPANT, new PreparedStatementSetter()
    {
      @Override
      public void setValues( PreparedStatement ps ) throws SQLException
      {
        ps.setLong( 1, contestId );
        ps.setLong( 2, participantId );
        ps.setShort( 3, awardIssuanceNumber );
      }
    } );
  }

  @Override
  public void updateAwadThemNowMessage( final String message, final Long contestId, final short issuanceNumber )
  {
    this.jdbcTemplate.update( UPDATE_ATN_CONTEST, new PreparedStatementSetter()
    {
      @Override
      public void setValues( PreparedStatement ps ) throws SQLException
      {
        ps.setString( 1, message );
        ps.setLong( 2, contestId );
        ps.setShort( 3, issuanceNumber );
      }
    } );
  }

  @Override
  public SSIContestAwardThemNow getContestAwardThemNowByIdAndIssunace( Long contestId, short issuanceNumber )
  {
    Criteria criteria = getSession().createCriteria( SSIContestAwardThemNow.class );
    criteria.add( Restrictions.eq( "contest.id", contestId ) );
    criteria.add( Restrictions.eq( "issuanceNumber", issuanceNumber ) );
    return (SSIContestAwardThemNow)criteria.uniqueResult();
  }

  @Override
  public SSIContestAwardHistoryTotalsValueBean getContestAwardTotals( Long contestId )
  {
    Criteria criteria = getSession().createCriteria( SSIContestParticipant.class, "ssiContestParticipant" );
    criteria.createAlias( "ssiContestParticipant.contest", "contest" );
    criteria.add( Restrictions.eq( "contest.id", contestId ) );
    ProjectionList projectionList = Projections.projectionList();
    projectionList.add( Projections.sum( "objectiveAmount" ).as( "objectiveAmount" ) );
    projectionList.add( Projections.sum( "objectivePayout" ).as( "objectivePayout" ) );
    criteria.setProjection( projectionList );
    criteria.setResultTransformer( Transformers.aliasToBean( SSIContestAwardHistoryTotalsValueBean.class ) );
    return (SSIContestAwardHistoryTotalsValueBean)criteria.uniqueResult();
  }

  @Override
  public Map<String, Object> getAllIssuancesForContest( Long contestId, int pageNumber, int recordsPerpage, String sortColumn, String sortOrder ) throws ServiceErrorException
  {
    CallPrcSSIContestHistorySummaryAwardThemNow proc = new CallPrcSSIContestHistorySummaryAwardThemNow( dataSource );

    pageNumber = pageNumber < 1 ? 1 : pageNumber;
    int rowStart = ( pageNumber - 1 ) * recordsPerpage + 1;
    int rowEnd = pageNumber * recordsPerpage;
    sortColumn = "dateCreated"; // temporary
    sortOrder = sortOrder != null ? sortOrder : "desc";

    Map<String, Object> inParams = new HashMap<String, Object>();
    inParams.put( "p_in_ssi_contest_id", contestId );
    inParams.put( "p_in_sortColName", sortColumn );
    inParams.put( "p_in_sortedBy", sortOrder );
    inParams.put( "p_in_rowNumStart", rowStart );
    inParams.put( "p_in_rowNumEnd", rowEnd );

    Map<String, Object> outParams = proc.execute( inParams );
    Integer returnCode = (Integer)outParams.get( "p_out_return_code" );
    if ( returnCode != 0 )
    {
      log.error( "Strored Procedure Returned Error: Error Code Returned = " + returnCode );
      throw new ServiceErrorException( "ssi_contest.atn.summary.POPULATE_ISSUANCE_ERR" );
    }
    return outParams;
  }

  @Override
  public List<SSIContestParticipant> getContestParticipants( List<Long> paxIds, short issuanceNumber )
  {
    Criteria criteria = getSession().createCriteria( SSIContestParticipant.class );
    criteria.add( Restrictions.in( "id", paxIds ) );
    criteria.add( Restrictions.eq( "awardIssuanceNumber", issuanceNumber ) );
    return criteria.list();
  }

  @Override
  public SSIContestPayoutObjectivesTotalsValueBean calculatePayoutObjectivesTotals( Long contestId, Short issuanceNumber ) throws ServiceErrorException
  {
    CallPrcSSIContestTotals calcTotalsproc = new CallPrcSSIContestTotals( dataSource );
    Map<String, Object> results = calcTotalsproc.executeProcedure( contestId, issuanceNumber );
    return extractResultSets( results );
  }

  @Override
  public void updateSameValueForAllPax( final Long contestId, final short issuanceNumber, String key, SSIContestParticipantValueBean participant )
  {
    StringBuffer sql = new StringBuffer();
    if ( !StringUtil.isNullOrEmpty( key ) )
    {
      sql.append( "UPDATE SSI_CONTEST_PARTICIPANT SET " );

      switch ( key )
      {
        case "objectiveAmount":
          sql.append( "OBJECTIVE_AMOUNT=" ).append( Double.parseDouble( participant.getObjectiveAmount() ) );
          break;
        case "objectivePayout":
          sql.append( "OBJECTIVE_PAYOUT=" ).append( Long.parseLong( participant.getObjectivePayout() ) );
          break;
        case "payoutDescription":
        case "objectivePayoutDescription":
          sql.append( "OBJECTIVE_PAYOUT_DESCRIPTION='" ).append( participant.getObjectivePayoutDescription() ).append( "'" );
          break;
        case "bonusForEvery":
          sql.append( "OBJECTIVE_BONUS_INCREMENT=" ).append( Long.parseLong( participant.getBonusForEvery() ) );
          break;
        case "bonusPayout":
          sql.append( "OBJECTIVE_BONUS_PAYOUT=" ).append( Long.parseLong( participant.getBonusPayout() ) );
          break;
        case "bonusPayoutCap":
          sql.append( "OBJECTIVE_BONUS_CAP=" ).append( Long.parseLong( participant.getBonusPayoutCap() ) );
          break;
        case "activityDescription":
          sql.append( "ACTIVITY_DESCRIPTION='" ).append( participant.getActivityDescription() ).append( "'" );
          break;
        case "baselineAmount":
          sql.append( "SIU_BASELINE_AMOUNT=" ).append( Double.parseDouble( participant.getBaselineAmount() ) );
          break;
        default:
          break;
      }
      sql.append( " WHERE SSI_CONTEST_ID = ?" );
      sql.append( " AND AWARD_ISSUANCE_NUMBER = ?" );

      this.jdbcTemplate.update( sql.toString(), new PreparedStatementSetter()
      {
        @Override
        public void setValues( PreparedStatement ps ) throws SQLException
        {
          ps.setLong( 1, contestId );
          ps.setLong( 2, issuanceNumber );
        }
      } );
    }
  }

  @Override
  public List<Long> getExistingContestParticipantIds( Long[] participantIds, Long contestId, Short awardIssuanceNumber )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.contest.getExistingContestParticipantIdsWithIssuanceNumber" );
    query.setParameter( "contestId", contestId );
    query.setParameter( "awardIssuanceNumber", awardIssuanceNumber );
    query.setParameterList( "paxIds", Arrays.asList( participantIds ) );
    return (List<Long>)query.list();

  }

}
