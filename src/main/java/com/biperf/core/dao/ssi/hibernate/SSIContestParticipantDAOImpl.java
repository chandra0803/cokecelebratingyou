
package com.biperf.core.dao.ssi.hibernate;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.biperf.core.dao.BaseDAO;
import com.biperf.core.dao.BaseResultTransformer;
import com.biperf.core.dao.ssi.SSIContestParticipantDAO;
import com.biperf.core.domain.enums.SSIContestStatus;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.ssi.SSIContest;
import com.biperf.core.domain.ssi.SSIContestManager;
import com.biperf.core.domain.ssi.SSIContestParticipant;
import com.biperf.core.domain.ssi.SSIContestParticipantProgress;
import com.biperf.core.domain.ssi.SSIContestParticipantStackRank;
import com.biperf.core.domain.ssi.SSIContestPaxPayout;
import com.biperf.core.domain.ssi.SSIContestSuperViewer;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.HibernateSessionManager;
import com.biperf.core.utils.SSIContestUtil;
import com.biperf.core.utils.StringUtil;
import com.biperf.core.value.ssi.SSIContestListValueBean;
import com.biperf.core.value.ssi.SSIContestParticipantProgressValueBean;
import com.biperf.core.value.ssi.SSIContestStackRankPaxValueBean;
import com.biperf.core.value.ssi.SSIContestUniqueCheckValueBean;

public class SSIContestParticipantDAOImpl extends BaseDAO implements SSIContestParticipantDAO
{
  // TODO; move this to stored proc
  private static final String PAX_PROGRESS_DTGT_SQL = "SELECT progress.ssi_contest_activity_id AS activityId, participantName, activityName, "
      + "NVL(progress.activity_amt, 0.0) AS activityAmount, paxId FROM " + "(SELECT * FROM (SELECT ROWNUM rn, results.* FROM " + "(SELECT usr.last_name||', '||usr.first_name AS participantName, "
      + "pax.user_id AS paxId, nvl(pax.activity_description, '') AS activityName, " + "pax.ssi_contest_id AS paxContestId FROM "
      + "ssi_contest_participant pax INNER JOIN application_user usr ON (pax.user_id = usr.user_id) "
      + "WHERE pax.ssi_contest_id = :contestId AND usr.is_active = 1 ORDER BY :sortedOn :sortedBy ) results )" + "WHERE rn >= :startRow AND rn   <= :endRow)"
      + "LEFT OUTER JOIN ssi_contest_pax_progress progress ON " + "(paxId = progress.user_id  AND paxContestId  = progress.ssi_contest_id) ORDER BY :sortedOn :sortedBy";

  private static final String PAX_PROGRESS_SQL = "SELECT activityId, participantName, activityName, activityAmount, paxId FROM (SELECT * " + "FROM (SELECT ROWNUM rn, results.* FROM "
      + "(SELECT usr.last_name||', '||usr.first_name AS participantName, pax.user_id AS paxId, " + "NVL(pax.activity_description, '') AS activityName, "
      + "NVL(progress.activity_amt, 0.0) AS activityAmount, " + "progress.ssi_contest_activity_id AS activityId "
      + "FROM ssi_contest_participant pax INNER JOIN application_user usr ON (pax.user_id = usr.user_id) " + "LEFT OUTER JOIN ssi_contest_pax_progress progress ON "
      + "(pax.user_id  = progress.user_id AND pax.ssi_contest_id   = progress.ssi_contest_id) "
      + "WHERE pax.ssi_contest_id = :contestId AND usr.is_active = 1 ORDER BY :sortedOn :sortedBy ) results ) " + "WHERE rn >= :startRow AND rn   <= :endRow ) ";

  private DataSource dataSource;

  public void setDataSource( DataSource dataSource )
  {
    this.dataSource = dataSource;
  }

  @Override
  public SSIContestParticipant getContestParticipantById( Long contestParticipantId )
  {
    SSIContestParticipant ssiContestParticipant = (SSIContestParticipant)getSession().get( SSIContestParticipant.class, contestParticipantId );
    return ssiContestParticipant;
  }

  @Override
  public SSIContestParticipantProgress getContestParticipantProgressById( Long contestParticipantProgressId )
  {
    SSIContestParticipantProgress ssiContestParticipantProgress = (SSIContestParticipantProgress)getSession().get( SSIContestParticipantProgress.class, contestParticipantProgressId );
    return ssiContestParticipantProgress;
  }

  @Override
  public SSIContestParticipantProgress saveContestParticipantProgress( SSIContestParticipantProgress contestParticipantProgress )
  {
    getSession().save( contestParticipantProgress );
    return contestParticipantProgress;
  }

  @Override
  public List<SSIContestParticipantProgress> getContestParticipantsProgresses( Long contestId, List<Long> paxIds )
  {
    Criteria criteria = getSession().createCriteria( SSIContestParticipantProgress.class );
    criteria.add( Restrictions.eq( "contest.id", contestId ) );
    criteria.add( Restrictions.in( "participant.id", paxIds ) );
    return criteria.list();
  }

  @Override
  public Double getParticipantActivityAmoumt( Long contestId, Long participantId )
  {
    Criteria criteria = getSession().createCriteria( SSIContestParticipantProgress.class, "ssiContestParticipantProgress" );
    criteria.createAlias( "ssiContestParticipantProgress.contest", "contest" );
    criteria.createAlias( "ssiContestParticipantProgress.participant", "participant" );
    criteria.setProjection( Projections.sum( "activityAmount" ) );
    criteria.add( Restrictions.eq( "contest.id", contestId ) );
    criteria.add( Restrictions.eq( "participant.id", participantId ) );
    return (Double)criteria.uniqueResult();
  }

  @Override
  public Map<String, Object> getContestParticipantProgressDetail( Long contestId, Long participantId ) throws Exception
  {
    CallPrcSSIContestPaxProgress procedure = new CallPrcSSIContestPaxProgress( dataSource );
    Map<String, Object> inParams = new HashMap<String, Object>();
    inParams.put( "contestId", contestId );
    inParams.put( "userId", participantId );
    Map<String, Object> outParams = procedure.executeProcedure( inParams );

    Integer returnCode = (Integer)outParams.get( "p_out_return_code" );
    if ( returnCode != 0 )
    {
      throw new Exception( "Stored procedure returned error. Procedure returned: " + returnCode );
    }
    return outParams;
  }

  @Override
  public List<SSIContestParticipantProgressValueBean> getContestParticipantsProgresses( Long contestId, int currentPage, int resultsPerPage, String sortedOn, String sortedBy, boolean isDoThisGetThat )
  {
    String sql = isDoThisGetThat ? PAX_PROGRESS_DTGT_SQL : PAX_PROGRESS_SQL;
    Query query = getSession().createSQLQuery( sql.replaceAll( ":sortedOn", sortedOn ).replaceAll( ":sortedBy", sortedBy ) );
    query.setParameter( "contestId", contestId );
    int startRow = resultsPerPage * ( currentPage - 1 ) + 1;
    query.setParameter( "startRow", startRow );
    query.setParameter( "endRow", startRow + resultsPerPage - 1 );

    query.setResultTransformer( new SSIContestParticipantProgressResultTransformer() );

    return (List<SSIContestParticipantProgressValueBean>)query.list();
  }

  @Override
  public Participant getParticipant( Long paxIds )
  {
    Criteria criteria = getSession().createCriteria( Participant.class );
    criteria.add( Restrictions.eq( "id", paxIds ) );
    return (Participant)criteria.uniqueResult();
  }

  private class SSIContestParticipantProgressResultTransformer extends BaseResultTransformer
  {
    @Override
    public Object transformTuple( Object[] tuple, String[] aliases )
    {
      SSIContestParticipantProgressValueBean progress = new SSIContestParticipantProgressValueBean();
      progress.setActivityId( tuple[0] == null ? null : extractBigDecimal( tuple[0] ).longValue() );
      progress.setParticipantName( tuple[1].toString() );
      progress.setActivityName( tuple[2] == null ? "" : tuple[2].toString() );
      progress.setActivityAmount( extractBigDecimal( tuple[3] ).doubleValue() );
      progress.setPaxId( extractBigDecimal( tuple[4] ).longValue() );
      return progress;
    }
  }

  public List<SSIContestListValueBean> getParticipantLiveContestsValueBean( Long participantId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.contest.getParticipantLiveContestsValueBean" );
    query.setParameter( "participantId", participantId );
    query.setParameter( "liveStatus", SSIContestStatus.LIVE );
    query.setParameter( "pendingStatus", SSIContestStatus.PENDING );
    query.setParameter( "finalize_results", SSIContestStatus.FINALIZE_RESULTS );
    query.setParameter( "referenceDate", DateUtils.getCurrentDateTrimmed() );
    query.setResultTransformer( new SSIContestListValueBeannResultTransformer() );
    return query.list();
  }

  public List<SSIContest> getParticipantLiveContests( Long participantId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.contest.getParticipantLiveContests" );
    query.setParameter( "participantId", participantId );
    query.setParameter( "liveStatus", SSIContestStatus.LIVE );
    query.setParameter( "pendingStatus", SSIContestStatus.PENDING );
    query.setParameter( "finalize_results", SSIContestStatus.FINALIZE_RESULTS );
    query.setParameter( "referenceDate", DateUtils.getCurrentDateTrimmed() );
    return query.list();
  }

  public List<SSIContestListValueBean> getParticipantArchivedContests( Long participantId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.contest.getParticipantArchivedContests" );

    query.setParameter( "participantId", participantId );
    query.setParameter( "closedStatus", SSIContestStatus.CLOSED );

    query.setResultTransformer( new SSIContestListValueBeannResultTransformer() );
    return query.list();
  }

  private class SSIContestListValueBeannResultTransformer extends BaseResultTransformer
  {
    @Override
    public Object transformTuple( Object[] tuple, String[] aliases )
    {
      SSIContestListValueBean bean = new SSIContestListValueBean();
      bean.setId( tuple[0].toString() );
      bean.setStatus( extractString( tuple[1] ) );
      bean.setName( extractString( tuple[2] ) );
      bean.setStartDate( DateUtils.toDisplayString( (Date)tuple[3] ) );
      bean.setEndDate( DateUtils.toDisplayString( (Date)tuple[4] ) );
      return bean;
    }
  }

  @Override
  public int getContestParticipantsCount( Long contestId )
  {
    Criteria criteria = getSession().createCriteria( SSIContestParticipant.class, "ssiContestParticipant" );
    criteria.createAlias( "ssiContestParticipant.contest", "contest" );
    criteria.createAlias( "ssiContestParticipant.participant", "participant" );
    criteria.setProjection( Projections.rowCount() );
    criteria.add( Restrictions.eq( "contest.id", contestId ) );
    criteria.add( Restrictions.eq( "participant.active", Boolean.TRUE ) );
    return ( (Long)criteria.uniqueResult() ).intValue();
  }

  @SuppressWarnings( "unchecked" )
  @Override
  public List<SSIContestParticipant> getContestParticipants( Long contestId, Integer pageNumber, Integer pageSize, String sortColumnName, String sortOrder )
  {
    String originalSortColumnName = sortColumnName;
    Criteria criteria = getSession().createCriteria( SSIContestParticipant.class );
    criteria.setMaxResults( pageSize );
    if ( pageNumber > 1 )
    {
      criteria.setFirstResult( pageSize * ( pageNumber - 1 ) );
    }

    criteria.add( Restrictions.eq( "contest.id", contestId ) );
    if ( StringUtil.isNullOrEmpty( sortColumnName ) )
    {
      sortColumnName = SSIContestUtil.SORT_BY_LAST_NAME;
    }
    else if ( "baselineAmount".equals( sortColumnName ) )
    {
      sortColumnName = "stepItUpBaselineAmount";
    }
    if ( StringUtil.isNullOrEmpty( sortOrder ) )
    {
      sortOrder = SSIContestUtil.DEFAULT_SORT_BY;
    }
    if ( SSIContestUtil.SORT_BY_LAST_NAME.equals( sortColumnName ) )
    {
      criteria.createAlias( "participant", "participant" );
      sortColumnName = "participant." + sortColumnName;
    }
    criteria.addOrder( SSIContestUtil.DEFAULT_SORT_BY.equals( sortOrder ) ? Order.asc( sortColumnName ) : Order.desc( sortColumnName ) );
    if ( SSIContestUtil.SORT_BY_LAST_NAME.equals( originalSortColumnName ) )
    {
      criteria.addOrder( SSIContestUtil.DEFAULT_SORT_BY.equals( sortOrder ) ? Order.asc( "participant.firstName" ) : Order.desc( "participant.firstName" ) );
    }
    return criteria.list();
  }

  @Override
  public int getContestManagersCount( Long contestId )
  {
    Criteria criteria = getSession().createCriteria( SSIContestManager.class, "ssiContestManager" );
    criteria.createAlias( "ssiContestManager.contest", "contest" );
    criteria.setProjection( Projections.rowCount() );
    criteria.add( Restrictions.eq( "contest.id", contestId ) );
    return ( (Long)criteria.uniqueResult() ).intValue();
  }

  @Override
  public int getContestSuperViewersCount( Long contestId )
  {
    Criteria criteria = getSession().createCriteria( SSIContestSuperViewer.class, "ssiContestSuperViewer" );
    criteria.createAlias( "ssiContestSuperViewer.contest", "contest" );
    criteria.setProjection( Projections.rowCount() );
    criteria.add( Restrictions.eq( "contest.id", contestId ) );
    return ( (Long)criteria.uniqueResult() ).intValue();
  }

  @SuppressWarnings( "unchecked" )
  @Override
  public List<SSIContestManager> getContestManagers( Long contestId, Integer pageNumber, Integer pageSize, String sortedOn, String sortedBy )
  {
    Criteria criteria = getSession().createCriteria( SSIContestManager.class );
    criteria.setMaxResults( pageSize );
    if ( pageNumber > 1 )
    {
      criteria.setFirstResult( pageSize * ( pageNumber - 1 ) );
    }
    criteria.add( Restrictions.eq( "contest.id", contestId ) );

    if ( SSIContestUtil.SORT_BY_LAST_NAME.equals( sortedOn ) )
    {
      criteria.createAlias( "manager", "manager" ).addOrder( SSIContestUtil.DEFAULT_SORT_BY.equals( sortedBy ) ? Order.asc( "manager." + sortedOn ) : Order.desc( "manager." + sortedOn ) );
    }
    else
    {
      criteria.addOrder( SSIContestUtil.DEFAULT_SORT_BY.equals( sortedBy ) ? Order.asc( sortedOn ) : Order.desc( sortedOn ) );
    }
    return criteria.list();
  }

  @SuppressWarnings( "unchecked" )
  @Override
  public List<SSIContestSuperViewer> getContestSuperviewers( Long contestId, Integer pageNumber, Integer pageSize, String sortedOn, String sortedBy )
  {
    Criteria criteria = getSession().createCriteria( SSIContestSuperViewer.class );
    criteria.setMaxResults( pageSize );
    if ( pageNumber > 1 )
    {
      criteria.setFirstResult( pageSize * ( pageNumber - 1 ) );
    }
    criteria.add( Restrictions.eq( "contest.id", contestId ) );

    if ( SSIContestUtil.SORT_BY_LAST_NAME.equals( sortedOn ) )
    {
      criteria.createAlias( "superViewer", "superViewer" )
          .addOrder( SSIContestUtil.DEFAULT_SORT_BY.equals( sortedBy ) ? Order.asc( "superViewer." + sortedOn ) : Order.desc( "superViewer." + sortedOn ) );
    }
    else
    {
      criteria.addOrder( SSIContestUtil.DEFAULT_SORT_BY.equals( sortedBy ) ? Order.asc( sortedOn ) : Order.desc( sortedOn ) );
    }
    return criteria.list();
  }

  @Override
  public SSIContestUniqueCheckValueBean performUniqueCheck( Long contestId ) throws Exception
  {
    CallPrcSSIContestUniqueCheck procedure = new CallPrcSSIContestUniqueCheck( dataSource );
    Map<String, Object> outParams = procedure.executeProcedure( contestId );

    Integer returnCode = (Integer)outParams.get( "p_out_return_code" );
    if ( returnCode != 0 )
    {
      throw new Exception( "Stored procedure returned error. Procedure returned: " + returnCode );
    }
    SSIContestUniqueCheckValueBean contestUniqueCheckValueBean = getUniqueCheckValueBean( outParams );
    return contestUniqueCheckValueBean;
  }

  private SSIContestUniqueCheckValueBean getUniqueCheckValueBean( Map<String, Object> outParams )
  {
    SSIContestUniqueCheckValueBean contestUniqueCheckValueBean = new SSIContestUniqueCheckValueBean();
    contestUniqueCheckValueBean.setActivityDescSame( 1 == (int)outParams.get( "p_out_boolean_activity_desc" ) ? true : false );
    contestUniqueCheckValueBean.setAmountSame( 1 == (int)outParams.get( "p_out_boolean_obj_amount" ) ? true : false );
    contestUniqueCheckValueBean.setBonusSame( 1 == (int)outParams.get( "p_out_boolean_obj_bonus" ) ? true : false );
    contestUniqueCheckValueBean.setBonusCapSame( 1 == (int)outParams.get( "p_out_boolean_obj_bonus_cap" ) ? true : false );
    contestUniqueCheckValueBean.setPayoutDescSame( 1 == (int)outParams.get( "p_out_boolean_obj_payout_desc" ) ? true : false );
    contestUniqueCheckValueBean.setPayoutSame( 1 == (int)outParams.get( "p_out_boolean_obj_payout" ) ? true : false );
    contestUniqueCheckValueBean.setActivityDesc( (String)outParams.get( "p_out_activity_description" ) );
    contestUniqueCheckValueBean.setAmount( (Double)outParams.get( "p_out_obj_amount" ) );
    contestUniqueCheckValueBean.setBonusPayout( (Long)outParams.get( "p_out_obj_bonus_payout" ) );
    contestUniqueCheckValueBean.setBonusIncrement( (Long)outParams.get( "p_out_obj_bonus_increment" ) );
    contestUniqueCheckValueBean.setBonusCap( (Long)outParams.get( "p_out_obj_bonus_cap" ) );
    contestUniqueCheckValueBean.setPayoutDesc( (String)outParams.get( "p_out_obj_payout_desc" ) );
    contestUniqueCheckValueBean.setPayout( (Long)outParams.get( "p_out_obj_payout" ) );
    contestUniqueCheckValueBean.setTotalAmount( (Double)outParams.get( "p_out_total_obj_amount" ) );
    contestUniqueCheckValueBean.setTotalPayout( (Long)outParams.get( "p_out_total_obj_payout" ) );
    contestUniqueCheckValueBean.setTotalBonusCap( (Long)outParams.get( "p_out_total_obj_bonus_cap" ) );
    return contestUniqueCheckValueBean;
  }

  @Override
  public SSIContestUniqueCheckValueBean performUniqueCheck( Long contestId, Short issunanceNumber ) throws Exception
  {
    CallPrcSSIContestUniqueCheck procedure = new CallPrcSSIContestUniqueCheck( dataSource );
    Map<String, Object> outParams = procedure.executeProcedure( contestId, issunanceNumber );

    Integer returnCode = (Integer)outParams.get( "p_out_return_code" );
    if ( returnCode != 0 )
    {
      throw new Exception( "Stored procedure returned error. Procedure returned: " + returnCode );
    }
    SSIContestUniqueCheckValueBean contestUniqueCheckValueBean = getUniqueCheckValueBean( outParams );
    return contestUniqueCheckValueBean;
  }

  @Override
  public SSIContestParticipantStackRank getContestParticipantStackRankByContestIdAndPaxId( Long contestId, Long participantId )
  {
    Criteria criteria = getSession().createCriteria( SSIContestParticipantStackRank.class, "ssiContestParticipantStackRank" );

    criteria.createAlias( "ssiContestParticipantStackRank.contest", "contest" );
    criteria.createAlias( "ssiContestParticipantStackRank.participant", "participant" );

    criteria.add( Restrictions.eq( "contest.id", contestId ) );
    criteria.add( Restrictions.eq( "participant.id", participantId ) );

    return (SSIContestParticipantStackRank)criteria.uniqueResult();
  }

  @Override
  public List<SSIContestParticipantStackRank> getAllContestParticipantsStackRankByContestId( Long contestId, int pageNumber, int pageSize, String sortedOn, String sortedBy )
  {
    Criteria criteria = getSession().createCriteria( SSIContestParticipantStackRank.class );
    criteria.add( Restrictions.eq( "contest.id", contestId ) );
    criteria.setMaxResults( pageSize );
    if ( pageNumber > 1 )
    {
      criteria.setFirstResult( pageSize * ( pageNumber - 1 ) );
    }

    if ( SSIContestUtil.SORT_BY_LAST_NAME.equals( sortedOn ) )
    {
      criteria.createAlias( "participant", "participant" )
          .addOrder( SSIContestUtil.DEFAULT_SORT_BY.equals( sortedBy ) ? Order.asc( "participant." + sortedOn ) : Order.desc( "participant." + sortedOn ) );
    }
    else
    {
      criteria.addOrder( SSIContestUtil.DEFAULT_SORT_BY.equals( sortedBy ) ? Order.asc( sortedOn ) : Order.desc( sortedOn ) );
    }
    return criteria.list();
  }

  @Override
  public SSIContestParticipantStackRank saveContestParticipantsStackRank( SSIContestParticipantStackRank contestParticipantStackRank )
  {
    getSession().save( contestParticipantStackRank );
    return contestParticipantStackRank;
  }

  public boolean isPaxContestCreator( Long participantId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.contest.isPaxContestCreator" );
    query.setParameter( "participantId", participantId );
    query.setParameter( "liveStatus", SSIContestStatus.LIVE );
    query.setParameter( "pendingStatus", SSIContestStatus.PENDING );
    query.setParameter( "finalize_results", SSIContestStatus.FINALIZE_RESULTS );
    query.setParameter( "referenceDate", DateUtils.getCurrentDateTrimmed() );
    Integer count = (Integer)query.uniqueResult();
    return count.intValue() > 0;
  }

  public boolean isPaxContestSuperViewer( Long participantId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.contest.isPaxContestSuperViewer" );
    query.setParameter( "participantId", participantId );
    query.setParameter( "liveStatus", SSIContestStatus.LIVE );
    query.setParameter( "pendingStatus", SSIContestStatus.PENDING );
    query.setParameter( "finalize_results", SSIContestStatus.FINALIZE_RESULTS );
    query.setParameter( "referenceDate", DateUtils.getCurrentDateTrimmed() );
    Integer count = (Integer)query.uniqueResult();
    return count.intValue() > 0;
  }

  public boolean isPaxInContestPaxAudience( Long participantId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.contest.isPaxInContestPaxAudience" );
    query.setParameter( "participantId", participantId );
    query.setParameter( "liveStatus", SSIContestStatus.LIVE );
    query.setParameter( "pendingStatus", SSIContestStatus.PENDING );
    query.setParameter( "finalize_results", SSIContestStatus.FINALIZE_RESULTS );
    query.setParameter( "referenceDate", DateUtils.getCurrentDateTrimmed() );
    Integer count = (Integer)query.uniqueResult();
    return count.intValue() > 0;
  }

  public boolean isPaxInContestManagerAudience( Long participantId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.contest.isPaxInContestManagerAudience" );
    query.setParameter( "participantId", participantId );
    query.setParameter( "liveStatus", SSIContestStatus.LIVE );
    query.setParameter( "pendingStatus", SSIContestStatus.PENDING );
    query.setParameter( "finalize_results", SSIContestStatus.FINALIZE_RESULTS );
    query.setParameter( "referenceDate", DateUtils.getCurrentDateTrimmed() );
    Integer count = (Integer)query.uniqueResult();
    return count.intValue() > 0;
  }

  public List<SSIContestStackRankPaxValueBean> getContestStackRank( Long contestId, Long userId, Long activityId, int currentPage, int resultsPerPage, boolean isTeam, boolean isIncludeAll )
      throws Exception
  {
    CallPrcSSIContestStackRankList procedure = new CallPrcSSIContestStackRankList( dataSource );
    Map<String, Object> inParams = getInParams( contestId, userId, activityId, currentPage, resultsPerPage, isTeam, isIncludeAll );
    Map<String, Object> outParams = procedure.executeProcedure( inParams );
    Integer returnCode = (Integer)outParams.get( "p_out_return_code" );
    if ( returnCode != 0 )
    {
      throw new Exception( "Stored procedure returned error. Procedure returned: " + returnCode );
    }
    List<SSIContestStackRankPaxValueBean> stackRanks = (List<SSIContestStackRankPaxValueBean>)outParams.get( "p_out_ref_cursor" );
    if ( stackRanks != null && stackRanks.size() > 0 )
    {
      stackRanks.get( 0 ).setParticipantsCount( (Integer)outParams.get( "p_out_pax_count" ) );
    }
    return stackRanks;
  }

  private Map<String, Object> getInParams( Long contestId, Long userId, Long activityId, int currentPage, int resultsPerPage, boolean isTeam, boolean isIncludeAll )
  {
    Integer rowNumStart = ( currentPage - 1 ) * resultsPerPage + 1;
    Integer rowNumEnd = currentPage * resultsPerPage;
    Integer teamResults = isTeam ? 1 : 0;
    Map<String, Object> inParams = new HashMap<String, Object>();
    inParams.put( "contestId", contestId );
    inParams.put( "userId", userId );
    inParams.put( "isTeam", teamResults );
    inParams.put( "isIncludeAll", isIncludeAll );
    inParams.put( "activityId", activityId );
    inParams.put( "rowNumStart", rowNumStart );
    inParams.put( "rowNumEnd", rowNumEnd );
    return inParams;
  }

  public List<Long> getContestProgressLoadParticipantIdsByImportFileId( Long contestId, Long importFileId )
  {
    Session session = HibernateSessionManager.getSession();
    Query query = session.getNamedQuery( "com.biperf.core.domain.fileload.getContestProgressLoadPaxIdsByImportFileId" );
    query.setParameter( "contestId", contestId );
    query.setParameter( "importFileId", importFileId );
    List<Long> results = query.list();
    return results;
  }

  @Override
  public SSIContestParticipant getSSIContestParticipantByPaxId( Long contestId, Long userId )
  {
    Criteria criteria = getSession().createCriteria( SSIContestParticipant.class );
    criteria.add( Restrictions.eq( "contest.id", contestId ) );
    criteria.add( Restrictions.eq( "participant.id", userId ) );

    return (SSIContestParticipant)criteria.uniqueResult();
  }

  @Override
  public SSIContestPaxPayout saveContestPaxPayout( SSIContestPaxPayout contestPaxPayout )
  {
    getSession().saveOrUpdate( contestPaxPayout );
    return contestPaxPayout;
  }

}
