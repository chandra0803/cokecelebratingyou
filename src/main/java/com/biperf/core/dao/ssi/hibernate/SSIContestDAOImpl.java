
package com.biperf.core.dao.ssi.hibernate;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;

import com.biperf.core.dao.BaseDAO;
import com.biperf.core.dao.BaseResultTransformer;
import com.biperf.core.dao.ssi.SSIContestDAO;
import com.biperf.core.domain.currency.Currency;
import com.biperf.core.domain.enums.LanguageType;
import com.biperf.core.domain.enums.SSIApproverType;
import com.biperf.core.domain.enums.SSIContestIssuanceStatusType;
import com.biperf.core.domain.enums.SSIContestStatus;
import com.biperf.core.domain.enums.SSIContestType;
import com.biperf.core.domain.enums.SSIPayoutType;
import com.biperf.core.domain.participant.NameIdBean;
import com.biperf.core.domain.ssi.SSIAdminContestActions;
import com.biperf.core.domain.ssi.SSIContest;
import com.biperf.core.domain.ssi.SSIContestActivity;
import com.biperf.core.domain.ssi.SSIContestApprover;
import com.biperf.core.domain.ssi.SSIContestAwardThemNow;
import com.biperf.core.domain.ssi.SSIContestClaimField;
import com.biperf.core.domain.ssi.SSIContestLevel;
import com.biperf.core.domain.ssi.SSIContestManager;
import com.biperf.core.domain.ssi.SSIContestParticipant;
import com.biperf.core.domain.ssi.SSIContestPaxPayout;
import com.biperf.core.domain.ssi.SSIContestStackRankPayout;
import com.biperf.core.domain.ssi.SSIContestSuperViewer;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.SSIContestUtil;
import com.biperf.core.utils.StringUtil;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.DepositProcessBean;
import com.biperf.core.value.NameableBean;
import com.biperf.core.value.ssi.SSIContestBaseLineTotalsValueBean;
import com.biperf.core.value.ssi.SSIContestBillCodeBean;
import com.biperf.core.value.ssi.SSIContestDescriptionValueBean;
import com.biperf.core.value.ssi.SSIContestListValueBean;
import com.biperf.core.value.ssi.SSIContestMessageValueBean;
import com.biperf.core.value.ssi.SSIContestNameValueBean;
import com.biperf.core.value.ssi.SSIContestParticipantValueBean;
import com.biperf.core.value.ssi.SSIContestPayoutDtgtTotalsValueBean;
import com.biperf.core.value.ssi.SSIContestPayoutObjectivesTotalsValueBean;
import com.biperf.core.value.ssi.SSIContestPayoutStackRankTotalsValueBean;
import com.biperf.core.value.ssi.SSIContestPayoutStepItUpTotalsValueBean;
import com.biperf.core.value.ssi.SSIContestPayoutsValueBean;
import com.biperf.core.value.ssi.SSIContestSummaryTDColumnBean;
import com.biperf.core.value.ssi.SSIContestSummaryTDPaxResultBean;
import com.biperf.core.value.ssi.SSIContestSummaryTDResultActivityBean;
import com.biperf.core.value.ssi.SSIContestSummaryTDSubColumnBean;
import com.biperf.core.value.ssi.SSIContestSummaryValueBean;
import com.biperf.core.value.ssi.SSIContestTranslationsCountValueBean;
import com.objectpartners.cms.util.CmsResourceBundle;

/**
 * 
 * SSIContestDAOImpl.
 * 
 * @author kandhi
 * @since Oct 22, 2014
 * @version 1.0
 */
public class SSIContestDAOImpl extends BaseDAO implements SSIContestDAO
{

  protected static final Log log = LogFactory.getLog( SSIContestDAOImpl.class );

  private DataSource dataSource;
  private JdbcTemplate jdbcTemplate;

  private static String INSERT_CONTEST_MANAGER = "INSERT INTO SSI_CONTEST_MANAGER (SSI_CONTEST_MANAGER_ID, SSI_CONTEST_ID, USER_ID, CREATED_BY, DATE_CREATED, VERSION) VALUES ( SSI_CONTEST_MANAGER_PK_SQ.NEXTVAL, ?, ?, ?, sysdate, 1)";
  private static String INSERT_CONTEST_SUPERVIEWER = "INSERT INTO SSI_CONTEST_SUPERVIEWER (SSI_CONTEST_SUPERVIEWER_ID, SSI_CONTEST_ID, USER_ID, CREATED_BY, DATE_CREATED, VERSION) VALUES ( SSI_CONTEST_SUPERVIEWER_PK_SQ.NEXTVAL, ?, ?, ?, sysdate, 1)";
  private static String INSERT_CONTEST_PARTICIPANT = "INSERT INTO SSI_CONTEST_PARTICIPANT (SSI_CONTEST_PARTICIPANT_ID, SSI_CONTEST_ID, USER_ID, CREATED_BY, DATE_CREATED, VERSION) VALUES ( SSI_CONTEST_PARTICIPANT_PK_SQ.NEXTVAL, ?, ?, ?, sysdate, 1)";
  private static String DELETE_CONTEST_MANAGER = "DELETE FROM SSI_CONTEST_MANAGER WHERE SSI_CONTEST_ID=? AND USER_ID=?";
  private static String DELETE_CONTEST_SUPERVIEWER = "DELETE FROM SSI_CONTEST_SUPERVIEWER WHERE SSI_CONTEST_ID=? AND USER_ID=?";
  private static String DELETE_CONTEST_PARTICIPANT = "DELETE FROM SSI_CONTEST_PARTICIPANT WHERE SSI_CONTEST_ID=? AND USER_ID=?";
  private static String UPDATE_CONTEST_PARTICIPANT = "UPDATE SSI_CONTEST_PARTICIPANT SCP SET SCP.ACTIVITY_DESCRIPTION = ?, SCP.OBJECTIVE_AMOUNT = ?, SCP.OBJECTIVE_PAYOUT = ? , SCP.OBJECTIVE_BONUS_INCREMENT = ?, SCP.OBJECTIVE_BONUS_PAYOUT = ?, "
      + "SCP.OBJECTIVE_BONUS_CAP = ? WHERE SCP.SSI_CONTEST_PARTICIPANT_ID = ?";
  private static String RESET_PARTICIPANT_BASELINE = "UPDATE SSI_CONTEST_PARTICIPANT SET SIU_BASELINE_AMOUNT = NULL WHERE SSI_CONTEST_ID = ?";
  private static final Object OUTPUT_RETURN_CODE = "p_out_return_code";
  private static String DELETE_STACK_RANK_PAYOUT = "DELETE FROM SSI_CONTEST_SR_PAYOUT WHERE SSI_CONTEST_ID=? AND SSI_CONTEST_SR_PAYOUT_ID=?";

  private static String NON_ATN_CONTEST_ARCHIVAL_PROCESS = "UPDATE SSI_CONTEST SET STATUS ='closed' , DATE_MODIFIED = ? WHERE CONTEST_TYPE != '1' AND (PAYOUT_ISSUE_DATE IS NOT NULL OR PAYOUT_ISSUE_DATE != '') AND PAYOUT_ISSUE_DATE < ? AND STATUS = 'finalize_results'";
  private static String ATN_CONTEST_ARCHIVAL_PROCESS_WITHOUT_APPROVER = "UPDATE SSI_CONTEST SET STATUS = 'closed' , DATE_MODIFIED = ? WHERE CONTEST_END_DATE < ? AND CONTEST_TYPE ='1'";
  private static String ATN_CONTEST_ARCHIVAL_PROCESS_WITH_APPROVER = "UPDATE SSI_CONTEST SET STATUS = 'closed', DATE_MODIFIED = ? WHERE SSI_CONTEST_ID = ?";

  private static final String CONTEST_PROGRESS_SQL = "SELECT progress.ssi_contest_activity_id  AS activityId, sum(NVL(progress.activity_amt, 0.0)) AS activityAmount "
      + "FROM ssi_contest_participant pax INNER JOIN application_user usr ON (pax.user_id = usr.user_id) "
      + "LEFT OUTER JOIN ssi_contest_pax_progress progress ON (pax.user_id = progress.user_id AND pax.ssi_contest_id   = progress.ssi_contest_id) "
      + "WHERE pax.ssi_contest_id = :contestId group by progress.ssi_contest_activity_id";

  private static final BigDecimal BAD_OUTPUT = new BigDecimal( "99" );
  // these are FE style classes, used to align the text
  private static final String NUMBER = "number";
  private static final String STRING = "string";
  private static final String HEADER = "header";

  public void setDataSource( DataSource dataSource )
  {
    this.dataSource = dataSource;
    this.jdbcTemplate = new JdbcTemplate( dataSource );
  }

  @Override
  public SSIContest getContestById( Long contestId )
  {
    SSIContest ssiContest = (SSIContest)getSession().get( SSIContest.class, contestId );
    return ssiContest;
  }

  @Override
  public SSIContest saveContest( SSIContest contest )
  {
    getSession().saveOrUpdate( contest );
    return contest;
  }

  @Override
  public void deleteContest( SSIContest contest )
  {
    getSession().delete( contest );
  }

  @Override
  public SSIContest getContestByIdWithAssociations( Long id, AssociationRequestCollection associationRequestCollection )
  {
    SSIContest ssiContest = (SSIContest)getSession().get( SSIContest.class, id );

    if ( associationRequestCollection != null )
    {
      associationRequestCollection.process( ssiContest );
    }
    return ssiContest;
  }

  @Override
  public SSIContestParticipant getContestParticipantByContestIdAndPaxId( Long contestId, Long participantId )
  {
    Criteria criteria = getSession().createCriteria( SSIContestParticipant.class );
    criteria.add( Restrictions.eq( "contest.id", contestId ) );
    criteria.add( Restrictions.eq( "participant.id", participantId ) );
    return (SSIContestParticipant)criteria.uniqueResult();
  }

  @Override
  public List<SSIContestApprover> getContestApproversByIdAndApproverType( Long contestId, SSIApproverType ssiApproverType )
  {
    Criteria criteria = getSession().createCriteria( SSIContestApprover.class );
    criteria.add( Restrictions.eq( "contest.id", contestId ) );
    if ( ssiApproverType != null )
    {
      criteria.add( Restrictions.eq( "approverType", ssiApproverType ) );
    }
    return criteria.list();
  }

  @Override
  public List<SSIContestParticipant> getAllContestParticipantsByContestId( Long contestId )
  {
    Criteria criteria = getSession().createCriteria( SSIContestParticipant.class );
    criteria.add( Restrictions.eq( "contest.id", contestId ) );
    return criteria.list();
  }

  @Override
  public List<SSIContestManager> getAllContestManagersByContestId( Long contestId )
  {
    Criteria criteria = getSession().createCriteria( SSIContestManager.class );
    criteria.add( Restrictions.eq( "contest.id", contestId ) );
    return criteria.list();
  }

  public boolean isContestNameUnique( String contestName, Long currentContestId, String locale )
  {
    boolean isUnique = true;
    if ( currentContestId == null )
    {
      currentContestId = new Long( 0 );
    }
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.contest.ContestNameExistsCount" );
    query.setParameter( "contestId", currentContestId );
    query.setParameter( "contestName", contestName.trim() );
    query.setParameter( "locale", locale );
    Integer count = (Integer)query.uniqueResult();
    isUnique = count.intValue() == 0;
    return isUnique;
  }

  @Override
  public void saveContestParticipants( final List<SSIContestParticipant> ssiContestParticipants )
  {
    this.jdbcTemplate.batchUpdate( UPDATE_CONTEST_PARTICIPANT, new BatchPreparedStatementSetter()
    {
      @Override
      public void setValues( PreparedStatement ps, int i ) throws SQLException
      {
        SSIContestParticipant ssiContestParticipant = ssiContestParticipants.get( i );
        ps.setString( 1, ssiContestParticipant.getActivityDescription() );
        ps.setDouble( 2, ssiContestParticipant.getObjectiveAmount() );
        ps.setLong( 3, ssiContestParticipant.getObjectivePayout() );
        ps.setDouble( 4, ssiContestParticipant.getObjectiveBonusIncrement() );
        ps.setLong( 5, ssiContestParticipant.getObjectiveBonusPayout() );
        ps.setLong( 6, ssiContestParticipant.getObjectiveBonusCap() );
        ps.setLong( 7, ssiContestParticipant.getParticipant().getId() );

      }

      @Override
      public int getBatchSize()
      {
        return ssiContestParticipants.size();
      }
    } );
  }

  @Override
  public void resetAllParticipantsBaseLineAmount( final Long contestId )
  {
    this.jdbcTemplate.update( RESET_PARTICIPANT_BASELINE, new PreparedStatementSetter()
    {
      @Override
      public void setValues( PreparedStatement ps ) throws SQLException
      {
        ps.setLong( 1, contestId );
      }
    } );
  }

  @Override
  public void updateSameValueForAllPax( final Long contestId, String key, SSIContestParticipantValueBean participant )
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
          sql.append( "OBJECTIVE_BONUS_INCREMENT=" ).append( Double.parseDouble( participant.getBonusForEvery() ) );
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

      this.jdbcTemplate.update( sql.toString(), new PreparedStatementSetter()
      {
        @Override
        public void setValues( PreparedStatement ps ) throws SQLException
        {
          ps.setLong( 1, contestId );
        }
      } );
    }
  }

  /**
   * Doing a JDBC batch update for performance reasons. Hibernate would need the SSIContest objects loaded
   * {@inheritDoc}
   */
  @Override
  public void saveContestParticipants( final Long contestId, final Long[] participantIds )
  {
    this.jdbcTemplate.batchUpdate( INSERT_CONTEST_PARTICIPANT, new BatchPreparedStatementSetter()
    {
      @Override
      public void setValues( PreparedStatement ps, int i ) throws SQLException
      {
        ps.setLong( 1, contestId );
        ps.setLong( 2, participantIds[i] );
        ps.setLong( 3, getLoggedInUserId() );
      }

      @Override
      public int getBatchSize()
      {
        return participantIds.length;
      }
    } );
  }

  public int getRequireContentApproval( Long ssiContestId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.contest.getRequireContentApproval" );
    query.setParameter( "ssiContestId", ssiContestId );
    Integer count = (Integer)query.uniqueResult();
    return count.intValue();
  }

  /**
   * Doing a JDBC update for performance reasons. Hibernate would need the SSIContest objects loaded
   * {@inheritDoc}
   */
  @Override
  public void deleteContestParticipant( final Long contestId, final Long participantId )
  {
    this.jdbcTemplate.update( DELETE_CONTEST_PARTICIPANT, new PreparedStatementSetter()
    {
      @Override
      public void setValues( PreparedStatement ps ) throws SQLException
      {
        ps.setLong( 1, contestId );
        ps.setLong( 2, participantId );
      }
    } );
  }

  /**
   * Doing a JDBC batch update for performance reasons. Hibernate would need the SSIContest objects loaded
   * {@inheritDoc}
   */
  @Override
  public void saveContestManagers( final Long contestId, final Long[] managerIds )
  {
    this.jdbcTemplate.batchUpdate( INSERT_CONTEST_MANAGER, new BatchPreparedStatementSetter()
    {
      @Override
      public void setValues( PreparedStatement ps, int i ) throws SQLException
      {
        ps.setLong( 1, contestId );
        ps.setLong( 2, managerIds[i] );
        ps.setLong( 3, getLoggedInUserId() );
      }

      @Override
      public int getBatchSize()
      {
        return managerIds.length;
      }
    } );
  }

  /**
   * Doing a JDBC batch update for performance reasons. Hibernate would need the SSIContest objects loaded
   * {@inheritDoc}
   */
  @Override
  public void saveContestSuperViewers( final Long contestId, final Long[] superViewerIds )
  {
    this.jdbcTemplate.batchUpdate( INSERT_CONTEST_SUPERVIEWER, new BatchPreparedStatementSetter()
    {
      @Override
      public void setValues( PreparedStatement ps, int i ) throws SQLException
      {
        ps.setLong( 1, contestId );
        ps.setLong( 2, superViewerIds[i] );
        ps.setLong( 3, getLoggedInUserId() );
      }

      @Override
      public int getBatchSize()
      {
        return superViewerIds.length;
      }
    } );
  }

  /**
   * Doing a JDBC update for performance reasons. Hibernate would need the SSIContest objects loaded
   * {@inheritDoc}
   */
  @Override
  public void deleteContestManager( final Long contestId, final Long managerId )
  {
    this.jdbcTemplate.update( DELETE_CONTEST_MANAGER, new PreparedStatementSetter()
    {
      @Override
      public void setValues( PreparedStatement ps ) throws SQLException
      {
        ps.setLong( 1, contestId );
        ps.setLong( 2, managerId );
      }
    } );
  }

  /**
   * Doing a JDBC update for performance reasons. Hibernate would need the SSIContest objects loaded
   * {@inheritDoc}
   */
  @Override
  public void deleteContestSuperViewer( final Long contestId, final Long superViewerId )
  {
    this.jdbcTemplate.update( DELETE_CONTEST_SUPERVIEWER, new PreparedStatementSetter()
    {
      @Override
      public void setValues( PreparedStatement ps ) throws SQLException
      {
        ps.setLong( 1, contestId );
        ps.setLong( 2, superViewerId );
      }
    } );
  }

  @Override
  public List<SSIContestTranslationsCountValueBean> getContestTranslationsCount( String assetCode )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.contest.ContestTranslationsCounts" );
    query.setParameter( "assetCode", assetCode );
    query.setResultTransformer( Transformers.aliasToBean( SSIContestTranslationsCountValueBean.class ) );
    return query.list();
  }

  @Override
  public List<String> getAllContestNames()
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.contest.getAllContestNames" );
    return query.list();
  }

  @SuppressWarnings( "unchecked" )
  @Override
  public Map<String, Object> getSSIContestManagerList( Long ssiContestId, String locale, String sortedOn, String sortedBy ) throws ServiceErrorException
  {
    Map<String, Object> inParams = new HashMap<String, Object>();

    inParams.put( "p_in_ssi_contest_id", ssiContestId );
    inParams.put( "p_in_locale", locale );
    inParams.put( "p_in_sortedBy", sortedBy );
    inParams.put( "p_in_sortColName", sortedOn );

    CallPrcSSIContestManagerList procedure = new CallPrcSSIContestManagerList( dataSource );
    Map<String, Object> outParams = procedure.executeProcedure( inParams );

    Integer returnCode = (Integer)outParams.get( "p_out_return_code" );
    if ( returnCode != 0 )
    {
      log.error( "Stored procedure returned error. Procedure returned: " + returnCode );
      throw new ServiceErrorException( "ssi_contest.pax.manager.NO_MANAGERS_FOUND" );
    }
    Map<String, Object> ssiContestManagers = new HashMap<String, Object>();
    ssiContestManagers.put( "managerList", outParams.get( "p_out_ref_cursor" ) );
    ssiContestManagers.put( "managerCount", outParams.get( "p_out_count_mgr_owner" ) );
    return ssiContestManagers;
  }

  public List<SSIContestNameValueBean> getTranslatedContestNames( String assetCode, String key )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.contest.getTranslatedContestValues" );
    query.setParameter( "assetCode", assetCode );
    query.setParameter( "key", key );
    query.setResultTransformer( new SSIContestNameValueBeanResultTransformer() );
    return query.list();
  }

  private class SSIContestNameValueBeanResultTransformer extends BaseResultTransformer
  {
    @Override
    public Object transformTuple( Object[] tuple, String[] aliases )
    {
      SSIContestNameValueBean bean = new SSIContestNameValueBean();
      bean.setLanguage( LanguageType.lookup( extractString( tuple[0] ) ).getCode() );
      bean.setText( extractString( tuple[1] ) );
      return bean;
    }
  }

  public List<SSIContestDescriptionValueBean> getTranslatedContestDescriptions( String assetCode, String key )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.contest.getTranslatedContestValues" );
    query.setParameter( "assetCode", assetCode );
    query.setParameter( "key", key );
    query.setResultTransformer( new SSIContestDescriptionValueBeanResultTransformer() );
    return query.list();
  }

  private class SSIContestDescriptionValueBeanResultTransformer extends BaseResultTransformer
  {
    @Override
    public Object transformTuple( Object[] tuple, String[] aliases )
    {
      SSIContestDescriptionValueBean bean = new SSIContestDescriptionValueBean();
      bean.setLanguage( LanguageType.lookup( extractString( tuple[0] ) ).getCode() );
      bean.setText( extractString( tuple[1] ) );
      return bean;
    }
  }

  public List<SSIContestMessageValueBean> getTranslatedContestMessages( String assetCode, String key )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.contest.getTranslatedContestValues" );
    query.setParameter( "assetCode", assetCode );
    query.setParameter( "key", key );
    query.setResultTransformer( new SSIContestMessageValueBeanResultTransformer() );
    return query.list();
  }

  private class SSIContestMessageValueBeanResultTransformer extends BaseResultTransformer
  {
    @Override
    public Object transformTuple( Object[] tuple, String[] aliases )
    {
      SSIContestMessageValueBean bean = new SSIContestMessageValueBean();
      bean.setLanguage( LanguageType.lookup( extractString( tuple[0] ) ).getCode() );
      bean.setText( extractString( tuple[1] ) );
      return bean;
    }
  }

  public List<SSIContestListValueBean> getManagerArchivedContests( Long managerId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.contest.getManagerArchivedContests" );

    query.setParameter( "managerId", managerId );
    query.setParameter( "closedStatus", SSIContestStatus.CLOSED );

    query.setResultTransformer( new SSIContestListValueBeannResultTransformer() );
    return query.list();
  }

  public List<SSIContestListValueBean> getManagerLiveContests( Long managerId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.contest.getManagerLiveContests" );
    query.setParameter( "managerId", managerId );
    query.setParameter( "liveStatus", SSIContestStatus.LIVE );
    query.setParameter( "pendingStatus", SSIContestStatus.PENDING );
    query.setParameter( "finalize_results", SSIContestStatus.FINALIZE_RESULTS );
    query.setParameter( "referenceDate", DateUtils.getCurrentDateTrimmed() );
    query.setResultTransformer( new SSIContestListValueBeannResultTransformer() );
    return query.list();
  }

  public List<SSIContestListValueBean> getContestListByCreatorSuperViewer( Long creatorId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.contest.getContestListByCreator" );
    query.setParameter( "creatorId", creatorId );
    query.setParameter( "liveStatus", SSIContestStatus.LIVE );
    query.setParameter( "pendingStatus", SSIContestStatus.PENDING );
    query.setParameter( "pendingApprovalStatus", SSIContestStatus.WAITING_FOR_APPROVAL );
    query.setParameter( "incompleteStatus", SSIContestStatus.DRAFT );
    query.setParameter( "finalize_results", SSIContestStatus.FINALIZE_RESULTS );
    query.setParameter( "contestType", SSIContestType.AWARD_THEM_NOW );
    query.setResultTransformer( new SSIContestListValueBeannResultTransformer() );
    return query.list();
  }

  public List<SSIContestListValueBean> getAwardThemNowContestSuperViewer( Long userId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.contest.getAwardThemNowContest" );
    query.setParameter( "creatorId", userId );
    query.setParameter( "status", SSIContestStatus.LIVE );
    query.setParameter( "contestType", SSIContestType.AWARD_THEM_NOW );
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
      bean.setDateModified( (Date)tuple[5] );
      bean.setContestType( extractString( tuple[6] ) );
      bean.setCanShowActionLinks( true );
      return bean;
    }
  }

  private class DeniedValueBeannResultTransformer extends BaseResultTransformer
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
      bean.setDateModified( (Date)tuple[5] );
      bean.setContestType( extractString( tuple[6] ) );
      bean.setDeniedReason( extractString( tuple[7] ) );
      bean.setCanShowActionLinks( true );
      return bean;
    }
  }

  @Override
  public List<SSIContestParticipant> getContestParticipants( List<Long> paxIds )
  {
    Criteria criteria = getSession().createCriteria( SSIContestParticipant.class );
    criteria.add( Restrictions.in( "id", paxIds ) );
    return criteria.list();
  }

  @Override
  public SSIContestPayoutObjectivesTotalsValueBean calculatePayoutObjectivesTotals( Long contestId ) throws ServiceErrorException
  {
    CallPrcSSIContestTotals calcTotalsproc = new CallPrcSSIContestTotals( dataSource );
    Map<String, Object> results = calcTotalsproc.executeProcedure( contestId );
    return extractResultSets( results );
  }

  @Override
  public Long calculatePayoutDoThisGetThatTotals( Long contestId )
  {
    Criteria criteria = getSession().createCriteria( SSIContestActivity.class, "ssiContestActivity" );
    criteria.createAlias( "ssiContestActivity.contest", "contest" );
    criteria.add( Restrictions.eq( "contest.id", contestId ) );
    return (Long)criteria.setProjection( Projections.sum( "ssiContestActivity.payoutCapAmount" ) ).uniqueResult();
  }

  protected SSIContestPayoutObjectivesTotalsValueBean extractResultSets( Map<String, Object> results ) throws ServiceErrorException
  {
    SSIContestPayoutObjectivesTotalsValueBean contestPayoutObjectivesTotalsValueBean = new SSIContestPayoutObjectivesTotalsValueBean();
    if ( results != null )
    {
      if ( results.get( OUTPUT_RETURN_CODE ) != null && BAD_OUTPUT.equals( results.get( OUTPUT_RETURN_CODE ) ) )
      {
        log.error( "Stored procedure returned error. Procedure returned: " + results.get( OUTPUT_RETURN_CODE ) );
        throw new ServiceErrorException( "ssi_contest.pax.manager.BAD_PAYOUT_DATA" );
      }

      Double objectiveAmountTotal = (Double)results.get( "total_objective_amount" );
      Long objectivePayoutTotal = (Long)results.get( "total_objective_payout" );
      Long bonusPayoutTotal = (Long)results.get( "total_objective_bonus_payout" );
      Long bonusPayoutCapTotal = (Long)results.get( "total_objective_bonus_cap" );

      contestPayoutObjectivesTotalsValueBean.setObjectiveAmountTotal( objectiveAmountTotal );
      contestPayoutObjectivesTotalsValueBean.setObjectivePayoutTotal( objectivePayoutTotal );
      contestPayoutObjectivesTotalsValueBean.setBonusPayoutTotal( bonusPayoutTotal );
      contestPayoutObjectivesTotalsValueBean.setBonusPayoutCapTotal( bonusPayoutCapTotal );
      contestPayoutObjectivesTotalsValueBean.setMaxPayout( objectivePayoutTotal );
      contestPayoutObjectivesTotalsValueBean.setMaxPayoutWithBonus( objectivePayoutTotal + bonusPayoutCapTotal );
      contestPayoutObjectivesTotalsValueBean.setMaxPotential( objectiveAmountTotal );
    }
    return contestPayoutObjectivesTotalsValueBean;
  }

  @Override
  public SSIContestActivity getContestActivityById( Long contestActivityId )
  {
    SSIContestActivity contestActivity = (SSIContestActivity)getSession().get( SSIContestActivity.class, contestActivityId );
    return contestActivity;
  }

  @Override
  public List<SSIContestActivity> getContestActivitiesByContestId( Long contestId )
  {
    Criteria criteria = getSession().createCriteria( SSIContestActivity.class );
    criteria.add( Restrictions.eq( "contest.id", contestId ) );
    return criteria.list();
  }

  @Override
  public List<SSIContest> getAllContestsByStatus( List<SSIContestStatus> contestStatuses )
  {
    Criteria criteria = getSession().createCriteria( SSIContest.class );
    criteria.add( Restrictions.in( "status", contestStatuses ) );
    return criteria.list();
  }

  @Override
  public int getNextSequenceNum( Long contestId )
  {
    Criteria criteria = getSession().createCriteria( SSIContestActivity.class, "ssiContestActivity" );
    criteria.createAlias( "ssiContestActivity.contest", "contest" );
    criteria.setProjection( Projections.rowCount() );
    criteria.add( Restrictions.eq( "contest.id", contestId ) );
    return ( (Long)criteria.uniqueResult() ).intValue() + 1;
  }

  @Override
  public int getNextLevelSequenceNum( Long contestId )
  {
    Criteria criteria = getSession().createCriteria( SSIContestLevel.class, "ssiContestLevel" );
    criteria.createAlias( "ssiContestLevel.contest", "contest" );
    criteria.setProjection( Projections.rowCount() );
    criteria.add( Restrictions.eq( "contest.id", contestId ) );
    return ( (Long)criteria.uniqueResult() ).intValue() + 1;
  }

  @Override
  public void deleteContestActivity( Long contestId, Long contestActivityId, Long userId ) throws ServiceErrorException
  {
    CallPrcSSIContestDeleteActivity procedure = new CallPrcSSIContestDeleteActivity( dataSource );
    Map<String, Object> outParams = procedure.executeProcedure( contestId, contestActivityId, userId );

    Integer returnCode = (Integer)outParams.get( "p_out_return_code" );
    if ( returnCode != 0 )
    {
      log.error( "Stored procedure returned error. Procedure returned: " + returnCode );
      throw new ServiceErrorException( "ssi_contest.payout_dtgt.DELETE_ACTIVITY_ERR" );
    }
  }

  @Override
  public SSIContestActivity saveContestActivity( SSIContestActivity ssiContestActivity )
  {
    getSession().saveOrUpdate( ssiContestActivity );
    return ssiContestActivity;
  }

  public List<SSIContestListValueBean> getArchivedContestListByCreator( Long creatorId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.contest.getArchivedContestListByCreator" );
    query.setParameter( "creatorId", creatorId );
    query.setParameter( "closedStatus", SSIContestStatus.CLOSED );
    query.setResultTransformer( new SSIContestListValueBeannResultTransformer() );
    return query.list();
  }

  public List<SSIContestListValueBean> getArchivedContestListBySuperViewer( Long creatorId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.contest.getArchivedContestListBySuperViewer" );
    query.setParameter( "creatorId", creatorId );
    query.setParameter( "closedStatus", SSIContestStatus.CLOSED );
    query.setResultTransformer( new SSIContestListValueBeannResultTransformer() );
    return query.list();
  }

  public List<SSIContestListValueBean> getDeniedContestListByCreator( Long creatorId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.contest.getDeniedContestListByCreator" );
    query.setParameter( "creatorId", creatorId );
    query.setParameter( "closedStatus", SSIContestStatus.DENIED );
    query.setResultTransformer( new DeniedValueBeannResultTransformer() );
    return query.list();
  }

  public List<SSIContest> getCreatorLiveContests( Long creatorId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.contest.getCreatorLiveContests" );
    query.setParameter( "creatorId", creatorId );
    query.setParameter( "liveStatus", SSIContestStatus.LIVE );
    query.setParameter( "pendingStatus", SSIContestStatus.PENDING );
    query.setParameter( "finalize_results", SSIContestStatus.FINALIZE_RESULTS );
    query.setParameter( "referenceDate", DateUtils.getCurrentDateTrimmed() );
    return query.list();
  }

  public List<SSIContest> getSuperViewerLiveContests( Long creatorId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.contest.getSuperViewerLiveContests" );
    query.setParameter( "creatorId", creatorId );
    query.setParameter( "liveStatus", SSIContestStatus.LIVE );
    query.setParameter( "pendingStatus", SSIContestStatus.PENDING );
    query.setParameter( "finalize_results", SSIContestStatus.FINALIZE_RESULTS );
    query.setParameter( "referenceDate", DateUtils.getCurrentDateTrimmed() );
    return query.list();
  }

  public List<SSIContestListValueBean> getDeniedContestListBySuperViewer( Long creatorId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.contest.getDeniedContestListBySuperViewer" );
    query.setParameter( "creatorId", creatorId );
    query.setParameter( "closedStatus", SSIContestStatus.DENIED );
    query.setResultTransformer( new DeniedValueBeannResultTransformer() );
    return query.list();
  }

  @Override
  public void updatePayout( boolean resetObjectiveAmount, boolean resetObjectivePayoutDescription, boolean resetBonusFields, boolean resetActivityDescription, boolean resetPayout, Long contestId )
  {
    StringBuffer updateSSIContestParticiantSql = new StringBuffer( 500 );
    updateSSIContestParticiantSql.append( " UPDATE SSI_CONTEST_PARTICIPANT SCP SET " );
    if ( resetObjectiveAmount )
    {
      updateSSIContestParticiantSql.append( " SCP.OBJECTIVE_AMOUNT = NULL ," );
    }
    if ( resetObjectivePayoutDescription )
    {
      updateSSIContestParticiantSql.append( " SCP.OBJECTIVE_PAYOUT_DESCRIPTION = NULL ," );
    }
    if ( resetBonusFields )
    {
      updateSSIContestParticiantSql.append( " SCP.OBJECTIVE_BONUS_INCREMENT = NULL , SCP.OBJECTIVE_BONUS_PAYOUT = NULL , SCP.OBJECTIVE_BONUS_CAP = NULL ," );
    }
    if ( resetActivityDescription )
    {
      updateSSIContestParticiantSql.append( " SCP.ACTIVITY_DESCRIPTION = NULL ," );
    }
    if ( resetPayout )
    {
      updateSSIContestParticiantSql.append( " SCP.OBJECTIVE_PAYOUT = NULL ," );
    }
    updateSSIContestParticiantSql.deleteCharAt( updateSSIContestParticiantSql.length() - 1 );
    updateSSIContestParticiantSql.append( " WHERE SCP.SSI_CONTEST_ID = :contestId" );

    Query query = getSession().createSQLQuery( updateSSIContestParticiantSql.toString() );
    query.setLong( "contestId", contestId );
    query.executeUpdate();
  }

  @Override
  public SSIContestStackRankPayout getStackRankPayoutById( Long stackRankPayoutId )
  {
    SSIContestStackRankPayout stackRankPayout = (SSIContestStackRankPayout)getSession().get( SSIContestStackRankPayout.class, stackRankPayoutId );
    return stackRankPayout;
  }

  @Override
  public List<SSIContestStackRankPayout> getStackRankPayoutsByContestId( Long contestId )
  {
    Criteria criteria = getSession().createCriteria( SSIContestStackRankPayout.class );
    criteria.add( Restrictions.eq( "contest.id", contestId ) );
    criteria.addOrder( Order.asc( "rankPosition" ) );
    return criteria.list();
  }

  @Override
  public SSIContestLevel getContestLevelById( Long contestLevelId )
  {
    SSIContestLevel contestLevel = (SSIContestLevel)getSession().get( SSIContestLevel.class, contestLevelId );
    return contestLevel;
  }

  public List<SSIContestLevel> getContestLevelsByContestId( Long contestId )
  {
    Criteria criteria = getSession().createCriteria( SSIContestLevel.class );
    criteria.add( Restrictions.eq( "contest.id", contestId ) );
    return criteria.list();
  }

  /**
   * copy the source contest to a new contest named  destinationContestName
   * @param sourceContestId
   * @param destinationContestName
   * @param locale
   * @return new contest id
   * @throws Exception
   */
  @Override
  public Long copyContest( Long sourceContestId, String destinationContestName, String locale ) throws ServiceErrorException
  {
    CallPrcSSIContestCopy procedure = new CallPrcSSIContestCopy( dataSource );
    Map<String, Object> outParams = procedure.executeProcedure( sourceContestId, destinationContestName, locale, getLoggedInUserId() );

    int returnCode = ( (Integer)outParams.get( CallPrcSSIContestCopy.P_OUT_RETURN_CODE ) ).intValue();
    if ( returnCode != 0 )
    {
      log.error( "Stored procedure returned error. Procedure returned: " + returnCode );
      throw new ServiceErrorException( "ssi_contest.generalInfo.SSI_CONTEST_COPY_ERR" );
    }
    return ( (BigDecimal)outParams.get( CallPrcSSIContestCopy.P_OUT_DESTINATION_CONTEST_ID ) ).longValue();
  }

  protected Long getLoggedInUserId()
  {
    return UserManager.getUserId();
  }

  @Override
  public boolean checkUserBelongToContestApproversGroup( Long contestId, Long userId )
  {
    Criteria criteria = getSession().createCriteria( SSIContestApprover.class );
    criteria.add( Restrictions.eq( "contest.id", contestId ) );
    criteria.add( Restrictions.eq( "approver.id", userId ) );
    return criteria.list().size() > 0 ? true : false;
  }

  @Override
  public List<Long> getExistingContestParticipantIds( Long[] paxIds, Long contestId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.contest.getExistingContestParticipantIds" );
    query.setParameter( "contestId", contestId );
    query.setParameterList( "paxIds", Arrays.asList( paxIds ) );
    return (List<Long>)query.list();
  }

  @Override
  public List<Long> getExistingContestManagerIds( Long[] managerIds, Long contestId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.contest.getExistingContestManagerIds" );
    query.setParameter( "contestId", contestId );
    query.setParameterList( "managerIds", Arrays.asList( managerIds ) );
    return (List<Long>)query.list();
  }

  @Override
  public List<Long> getExistingContestSuperViewerIds( Long[] superViewerIds, Long contestId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.contest.getExistingContestSuperViewerIds" );
    query.setParameter( "contestId", contestId );
    query.setParameterList( "superViewerIds", Arrays.asList( superViewerIds ) );
    return (List<Long>)query.list();
  }

  public List<SSIContestListValueBean> getContestsWithTodayTileStartDate()
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.contest.getLiveContests" );
    query.setParameter( "liveStatus", SSIContestStatus.LIVE );
    query.setParameter( "pendingStatus", SSIContestStatus.PENDING );
    query.setParameter( "tileStartDate", DateUtils.getCurrentDateTrimmed() );
    query.setResultTransformer( new SSIContestListValueBeannResultTransformer() );
    return query.list();
  }

  public List<SSIContestListValueBean> getContestWaitingForApprovalByUserId( Long userId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.contest.getContestWaitingForApprovalByUserId" );
    query.setParameter( "userId", userId );
    query.setParameter( "status", SSIContestStatus.WAITING_FOR_APPROVAL );
    query.setParameter( "level1Approver", SSIApproverType.CONTEST_LEVEL1_APPROVER );
    query.setParameter( "level2Approver", SSIApproverType.CONTEST_LEVEL2_APPROVER );
    query.setResultTransformer( new SSIContestListValueBeannResultTransformer() );
    return query.list();
  }

  public List<NameIdBean> getContestNames( Set<Long> contestIds, String locale )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.contest.getContestNames" );
    query.setParameterList( "contestIds", contestIds );
    query.setParameter( "locale", locale );
    query.setResultTransformer( new SSIContestNameResultTransformer() );
    return query.list();
  }

  private class SSIContestNameResultTransformer extends BaseResultTransformer
  {
    @Override
    public Object transformTuple( Object[] tuple, String[] aliases )
    {
      NameIdBean bean = new NameIdBean();
      bean.setId( (Long)tuple[0] );
      bean.setName( extractString( tuple[1] ) );
      return bean;
    }
  }

  @Override
  public Map<String, Object> downloadContestData( Map<String, Object> inParameters ) throws ServiceErrorException
  {
    CallPrcSSIContestDownloadData procedure = new CallPrcSSIContestDownloadData( dataSource );
    Map<String, Object> outParams = procedure.executeProcedure( inParameters );
    Integer returnCode = (Integer)outParams.get( "p_out_return_code" );
    if ( returnCode != 0 )
    {
      log.error( "Stored procedure returned error. Procedure returned: " + returnCode );
      throw new ServiceErrorException( "ssi_contest.creator.DOWNLOAD_CONTEST_ERR" );
    }
    return outParams;
  }

  @Override
  public Map<String, Object> downloadContestPayoutData( Map<String, Object> inParameters ) throws ServiceErrorException
  {
    CallPrcSSIContestPayoutExtractData procedure = new CallPrcSSIContestPayoutExtractData( dataSource );
    Map<String, Object> outParams = procedure.executeProcedure( inParameters );
    Integer returnCode = (Integer)outParams.get( "p_out_return_code" );
    if ( returnCode != 0 )
    {
      log.error( "Stored procedure returned error. Procedure returned: " + returnCode );
      throw new ServiceErrorException( "Stored procedure returned error. Procedure returned: " + returnCode );
    }
    return outParams;
  }

  @Override
  public Map<String, Object> extractContestClaimData( Map<String, Object> inParameters ) throws ServiceErrorException
  {
    CallPrcSSIContestClaimExtractData procedure = new CallPrcSSIContestClaimExtractData( dataSource );
    Map<String, Object> outParams = procedure.executeProcedure( inParameters );
    Integer returnCode = (Integer)outParams.get( "p_out_return_code" );
    if ( returnCode != 0 )
    {
      log.error( "Stored procedure returned error. Procedure returned: " + returnCode );
      throw new ServiceErrorException( "ssi_contest.claims.DOWNLOAD_CLAIM_DATA_ERR" );
    }
    return outParams;
  }

  @Override
  public Map<String, Object> downloadContestCreatorExtractData( Map<String, Object> inParameters ) throws ServiceErrorException
  {
    CallPrcSSIContestCreatorExtractData procedure = new CallPrcSSIContestCreatorExtractData( dataSource );
    Map<String, Object> outParams = procedure.executeProcedure( inParameters );
    Integer returnCode = (Integer)outParams.get( "p_out_return_code" );
    if ( returnCode != 0 )
    {
      log.error( "Stored procedure returned error. Procedure returned: " + returnCode );
      throw new ServiceErrorException( "ssi_contest.creator.DOWNLOAD_CONTEST_ERR" );
    }
    return outParams;
  }

  @Override
  public SSIContestLevel saveContestLevel( SSIContestLevel ssiContestLevel )
  {
    getSession().saveOrUpdate( ssiContestLevel );
    return ssiContestLevel;
  }

  @Override
  public void deleteContestLevel( final Long contestId, final Long contestLevelId, final Long userId ) throws ServiceErrorException
  {
    CallPrcSSIContestDeleteLevel procedure = new CallPrcSSIContestDeleteLevel( dataSource );
    Map<String, Object> outParams = procedure.executeProcedure( contestId, contestLevelId, userId );

    Integer returnCode = (Integer)outParams.get( "p_out_return_code" );
    if ( returnCode != 0 )
    {
      log.error( "Stored procedure returned error. Procedure returned: " + returnCode );
      throw new ServiceErrorException( "ssi_contest.payout_stepitup.DELETE_LEVEL_ERROR" );
    }
  }

  @Override
  public SSIContestStackRankPayout saveStackRankPayout( SSIContestStackRankPayout ssiContestStackRankPayout )
  {
    getSession().saveOrUpdate( ssiContestStackRankPayout );
    return ssiContestStackRankPayout;
  }

  @Override
  public void deleteStackRankPayout( final Long contestId, final Long stackRankPayoutId )
  {
    this.jdbcTemplate.update( DELETE_STACK_RANK_PAYOUT, new PreparedStatementSetter()
    {
      @Override
      public void setValues( PreparedStatement ps ) throws SQLException
      {
        ps.setLong( 1, contestId );
        ps.setLong( 2, stackRankPayoutId );
      }
    } );

  }

  public SSIContestSummaryValueBean getParticipantsSummaryData( Long contestId, Long userId, String sortBy, String sortColumnName, int pageNumber, int perPage ) throws ServiceErrorException
  {
    int rowNumStart = ( pageNumber - 1 ) * perPage + 1;
    int rowNumEnd = pageNumber * perPage;

    Map<String, Object> output = getParticipantsSummaryDataMap( contestId, userId, sortColumnName, sortBy, rowNumStart, rowNumEnd );

    SSIContest contest = getContestById( contestId );
    SSIContestSummaryValueBean detailBean = null;
    if ( SSIContestType.OBJECTIVES.equals( contest.getContestType().getCode() ) )
    {
      detailBean = getParticipantsSummaryDataObjectives( contest, output, userId == null );
    }
    else if ( SSIContestType.DO_THIS_GET_THAT.equals( contest.getContestType().getCode() ) )
    {
      detailBean = getParticipantsSummaryDataDoThisGetThat( contest, output );
    }
    else if ( SSIContestType.STEP_IT_UP.equals( contest.getContestType().getCode() ) )
    {
      detailBean = getParticipantsSummaryDataStepItUp( contest, output, userId == null );
    }
    return detailBean;
  }

  private Map<String, Object> getParticipantsSummaryDataMap( Long contestId, Long userId, String sortColumnName, String sortBy, Integer rowNumStart, Integer rowNumEnd ) throws ServiceErrorException
  {
    Map<String, Object> inParams = setInParameters( contestId, userId, null, sortColumnName, sortBy, rowNumStart, rowNumEnd );
    CallPrcSSIContestPaxList procedure = new CallPrcSSIContestPaxList( dataSource );
    Map<String, Object> outParams = procedure.executeProcedure( inParams );

    Integer returnCode = (Integer)outParams.get( "p_out_return_code" );
    if ( returnCode != 0 )
    {
      log.error( "Stored procedure returned error. Procedure returned: " + returnCode );
      throw new ServiceErrorException( "ssi_contest.preview.PROGRESS_ERROR_MESG" );
    }
    return outParams;
  }

  public SSIContestPayoutsValueBean getContestPayouts( Long contestId, Long contestActivityId, String sortColumnName, String sortBy, Integer pageNumber, Integer perPage ) throws ServiceErrorException
  {
    CallPrcSSIContestApprovalsList procedure = new CallPrcSSIContestApprovalsList( dataSource );
    int rowNumStart = ( pageNumber - 1 ) * perPage + 1;
    int rowNumEnd = pageNumber * perPage;
    return procedure.executeProcedure( contestId, contestActivityId, sortColumnName, sortBy, rowNumStart, rowNumEnd );
  }

  public SSIContestPayoutsValueBean saveContestPaxPayouts( Long contestId,
                                                           String userIds,
                                                           String payoutAmounts,
                                                           String payoutDesc,
                                                           String sortColumnName,
                                                           String sortBy,
                                                           Integer pageNumber,
                                                           Integer perPage )
      throws ServiceErrorException
  {
    CallPrcSSIContestSavePaxPayouts procedure = new CallPrcSSIContestSavePaxPayouts( dataSource );
    int rowNumStart = ( pageNumber - 1 ) * perPage + 1;
    int rowNumEnd = pageNumber * perPage;
    return procedure.executeProcedure( contestId, userIds, payoutAmounts, payoutDesc, sortColumnName, sortBy, rowNumStart, rowNumEnd );
  }

  private Map<String, Object> setInParameters( Long contestId, Long userId, Long nodeId, String sortColumnName, String sortBy, Integer rowNumStart, Integer rowNumEnd )
  {
    Map<String, Object> inParams = new HashMap<String, Object>();
    inParams.put( "contestId", contestId );
    inParams.put( "userId", userId );
    if ( nodeId != null )
    {
      inParams.put( "nodeId", nodeId );
    }
    inParams.put( "sortColumnName", sortColumnName );
    inParams.put( "sortBy", sortBy );
    inParams.put( "rowNumStart", rowNumStart );
    inParams.put( "rowNumEnd", rowNumEnd );
    return inParams;
  }

  private SSIContestSummaryValueBean getParticipantsSummaryDataObjectives( SSIContest contest, Map<String, Object> output, boolean isCreator )
  {
    SSIContestSummaryValueBean detailBean = new SSIContestSummaryValueBean();
    List<SSIContestSummaryTDPaxResultBean> participantsSummary = (List<SSIContestSummaryTDPaxResultBean>)output.get( "p_out_obj_ref_cursor" );

    detailBean.setContestType( contest.getContestType().getCode() );
    detailBean.setPayoutType( contest.getPayoutType().getCode() );
    detailBean.setIncludeBonus( contest.isIncludeBonus() );
    detailBean.setContestId( contest.getId() );
    detailBean.setTotal( (Integer)output.get( "p_out_size_data" ) );

    populateParticipantSummaryTableColumnsForObjectives( contest, detailBean, output, isCreator );

    detailBean.addPaxResults( participantsSummary, contest.getActivityMeasureType().getCode() );
    return detailBean;
  }

  private void populateParticipantSummaryTableColumnsForObjectives( SSIContest contest, SSIContestSummaryValueBean detailBean, Map<String, Object> output, boolean isCreator )
  {
    String payoutType = contest.getPayoutType().getCode();
    boolean includeBonus = contest.isIncludeBonus();
    int precision = SSIContestUtil.getPrecision( contest.getActivityMeasureType().getCode() );
    String activityPrefix = getActivityPrefix( contest );
    String payoutPrefix = getPayoutPrefix( contest );
    String payoutSuffix = getPayoutSuffix( contest );

    SSIContestPayoutObjectivesTotalsValueBean participantsSummaryTotal = (SSIContestPayoutObjectivesTotalsValueBean)output.get( "p_out_obj_total_ref_cursor" );

    // proc data
    detailBean.setFooterActive( true );
    String objectives = activityPrefix + SSIContestUtil.getFormattedValue( participantsSummaryTotal.getObjectiveAmountTotal(), precision );
    String activities = activityPrefix + SSIContestUtil.getFormattedValue( participantsSummaryTotal.getActivityAmountTotal(), precision );
    Long percentage = participantsSummaryTotal.getPercentageAcheived(); // Bug fix #62054
    String objectivePayout = payoutPrefix + SSIContestUtil.getFormattedValue( participantsSummaryTotal.getObjectivePayoutTotal(), SSIContestUtil.PAYOUT_DECIMAL_PRECISION ) + payoutSuffix;
    String objectivePayoutDescription = participantsSummaryTotal.getPayoutDescription();
    String bonusPayout = payoutPrefix + SSIContestUtil.getFormattedValue( participantsSummaryTotal.getBonusPayoutTotal(), SSIContestUtil.PAYOUT_DECIMAL_PRECISION ) + payoutSuffix;
    Long totalPotentialPayout = participantsSummaryTotal.getObjectivePayoutTotal() + ( participantsSummaryTotal.getBonusPayoutTotal() != null ? participantsSummaryTotal.getBonusPayoutTotal() : 0L );
    String potentialPayout = payoutPrefix + SSIContestUtil.getFormattedValue( totalPotentialPayout, SSIContestUtil.PAYOUT_DECIMAL_PRECISION ) + payoutSuffix;

    // fetching lables for the summary table
    String participantsLabel = CmsResourceBundle.getCmsBundle().getString( "ssi_contest.participant.PAX" );
    String totalsLable = CmsResourceBundle.getCmsBundle().getString( "ssi_contest.participant.TOTALS" );
    String orgUnitLabel = CmsResourceBundle.getCmsBundle().getString( "ssi_contest.preview.ORG_UNIT" );
    String objectiveLabel = CmsResourceBundle.getCmsBundle().getString( "ssi_contest.payout_objectives.OBJECTIVE" );
    String currentActivityLabel = CmsResourceBundle.getCmsBundle().getString( "ssi_contest.participant.CURRENT_ACTIVITY" );
    String percentageToObjectiveLabel = CmsResourceBundle.getCmsBundle().getString( "ssi_contest.payout_objectives.PER_TO_OBJECTIVE" );
    String objectivePayoutLabel = CmsResourceBundle.getCmsBundle().getString( "ssi_contest.creator.POTENTIAL_POINTS" );
    String bonusPayoutLabel = CmsResourceBundle.getCmsBundle().getString( "ssi_contest.payout_objectives.BONUS_PAYOUT" );
    String totalPointsPayoutLabel = contest.getStatus().isFinalizeResults()
        ? CmsResourceBundle.getCmsBundle().getString( "ssi_contest.payout_objectives.TOTAL_PAYOUT" )
        : CmsResourceBundle.getCmsBundle().getString( "ssi_contest.participant.TOTAL_POTENTIAL_POINTS" );
    String payoutValueLabel = CmsResourceBundle.getCmsBundle().getString( "ssi_contest.participant.PAYOUT_VALUE" );
    String payoutDescriptionLabel = CmsResourceBundle.getCmsBundle().getString( "ssi_contest.payout_objectives.PAYOUT_DESCRIPTION" );

    List<SSIContestSummaryTDColumnBean> columns = new ArrayList<SSIContestSummaryTDColumnBean>();
    long columnIndex = 1;
    columns.add( new SSIContestSummaryTDColumnBean( columnIndex, "participants", STRING, participantsLabel, true, totalsLable, false ) );
    if ( SSIPayoutType.POINTS_CODE.equals( payoutType ) )
    {
      // columns, if payout points
      columns.add( new SSIContestSummaryTDColumnBean( ++columnIndex, "orgUnit", STRING, orgUnitLabel, true, "", false ) );
      columns.add( new SSIContestSummaryTDColumnBean( ++columnIndex, "objective", NUMBER, objectiveLabel, true, objectives, false ) );
      columns.add( new SSIContestSummaryTDColumnBean( ++columnIndex, "currentActivity", NUMBER, currentActivityLabel, true, activities, false ) );
      columns.add( new SSIContestSummaryTDColumnBean( ++columnIndex, "percentage", NUMBER, percentageToObjectiveLabel, true, percentage + "%", false ) );
      columns.add( new SSIContestSummaryTDColumnBean( ++columnIndex, "objectivePayout", NUMBER, objectivePayoutLabel, true, objectivePayout, false ) );
      if ( includeBonus )
      {
        columns.add( new SSIContestSummaryTDColumnBean( ++columnIndex, "bonusPayout", NUMBER, bonusPayoutLabel, true, bonusPayout, false ) );
        columns.add( new SSIContestSummaryTDColumnBean( ++columnIndex, "totalPotentialPayout", NUMBER, totalPointsPayoutLabel, true, potentialPayout, false ) );
      }

    }
    else if ( SSIPayoutType.OTHER_CODE.equals( payoutType ) )
    {
      // columns, if payout other
      columns.add( new SSIContestSummaryTDColumnBean( ++columnIndex, "objective", NUMBER, objectiveLabel, true, objectives, false ) );
      columns.add( new SSIContestSummaryTDColumnBean( ++columnIndex, "currentActivity", NUMBER, currentActivityLabel, true, activities, false ) );
      columns.add( new SSIContestSummaryTDColumnBean( ++columnIndex, "percentage", NUMBER, percentageToObjectiveLabel, true, percentage + "%", false ) );
      columns.add( new SSIContestSummaryTDColumnBean( ++columnIndex, "payoutDescription", NUMBER, payoutDescriptionLabel, true, objectivePayoutDescription, false ) );
      if ( isCreator )
      {
        columns.add( new SSIContestSummaryTDColumnBean( ++columnIndex, "payoutValue", NUMBER, payoutValueLabel, true, objectivePayout, false ) );
      }

    }
    detailBean.setColumns( columns );
  }

  // Do This Get That- pax
  private SSIContestSummaryValueBean getParticipantsSummaryDataDoThisGetThat( SSIContest contest, Map<String, Object> output )
  {
    Integer total = (Integer)output.get( "p_out_size_data" );
    List<SSIContestSummaryTDPaxResultBean> participants = (List<SSIContestSummaryTDPaxResultBean>)output.get( "p_out_dtgt_ref_cursor" );
    SSIContestPayoutDtgtTotalsValueBean participantssummaryTotal = (SSIContestPayoutDtgtTotalsValueBean)output.get( "p_out_total_dtgt_ref_cursor" );

    String contestType = contest.getContestType().getCode();
    String payoutType = contest.getPayoutType().getCode();
    boolean includeBonus = contest.isIncludeBonus();
    boolean isPoints = payoutType.equals( SSIPayoutType.POINTS_CODE );
    int precision = SSIContestUtil.getPrecision( contest.getActivityMeasureType().getCode() );

    SSIContestSummaryValueBean detailBean = new SSIContestSummaryValueBean();
    detailBean.setContestType( contestType );
    detailBean.setPayoutType( payoutType );
    detailBean.setIncludeBonus( includeBonus );
    detailBean.setTotal( total );
    detailBean.setFooterActive( true );

    // populate table column header
    populateParticipantSummaryTableColumnsForDtgt( contest, isPoints, detailBean, participants, participantssummaryTotal, precision );

    // results pax data list if points
    List<SSIContestSummaryTDPaxResultBean> participantsResult = new ArrayList<SSIContestSummaryTDPaxResultBean>();
    for ( SSIContestSummaryTDPaxResultBean participant : participants )
    {
      // populating activity description data
      List<SSIContestSummaryTDResultActivityBean> activities = new ArrayList<SSIContestSummaryTDResultActivityBean>();

      SSIContestSummaryTDResultActivityBean activity1 = new SSIContestSummaryTDResultActivityBean( participant.getActivity1Id(),
                                                                                                   SSIContestUtil.getFormattedValue( participant.getActivityAmount1(), precision ),
                                                                                                   SSIContestUtil.getFormattedValue( participant.getPayoutValue1(),
                                                                                                                                     SSIContestUtil.PAYOUT_DECIMAL_PRECISION ),
                                                                                                   SSIContestUtil.getFormattedValue( participant.getPayoutQuantity1(),
                                                                                                                                     SSIContestUtil.PAYOUT_DECIMAL_PRECISION ) );
      activities.add( activity1 );

      if ( participant.getActivity2Id() != null && !participant.getActivity2Id().equals( 0L ) )
      {
        SSIContestSummaryTDResultActivityBean activity2 = new SSIContestSummaryTDResultActivityBean( participant.getActivity2Id(),
                                                                                                     SSIContestUtil.getFormattedValue( participant.getActivityAmount2(), precision ),
                                                                                                     SSIContestUtil.getFormattedValue( participant.getPayoutValue2(),
                                                                                                                                       SSIContestUtil.PAYOUT_DECIMAL_PRECISION ),
                                                                                                     SSIContestUtil.getFormattedValue( participant.getPayoutQuantity2(),
                                                                                                                                       SSIContestUtil.PAYOUT_DECIMAL_PRECISION ) );
        activities.add( activity2 );
      }
      if ( participant.getActivity3Id() != null && !participant.getActivity3Id().equals( 0L ) )
      {
        SSIContestSummaryTDResultActivityBean activity3 = new SSIContestSummaryTDResultActivityBean( participant.getActivity3Id(),
                                                                                                     SSIContestUtil.getFormattedValue( participant.getActivityAmount3(), precision ),
                                                                                                     SSIContestUtil.getFormattedValue( participant.getPayoutValue3(),
                                                                                                                                       SSIContestUtil.PAYOUT_DECIMAL_PRECISION ),
                                                                                                     SSIContestUtil.getFormattedValue( participant.getPayoutQuantity3(),
                                                                                                                                       SSIContestUtil.PAYOUT_DECIMAL_PRECISION ) );
        activities.add( activity3 );
      }
      if ( participant.getActivity4Id() != null && !participant.getActivity4Id().equals( 0L ) )
      {
        SSIContestSummaryTDResultActivityBean activity4 = new SSIContestSummaryTDResultActivityBean( participant.getActivity4Id(),
                                                                                                     SSIContestUtil.getFormattedValue( participant.getActivityAmount4(), precision ),
                                                                                                     SSIContestUtil.getFormattedValue( participant.getPayoutValue4(),
                                                                                                                                       SSIContestUtil.PAYOUT_DECIMAL_PRECISION ),
                                                                                                     SSIContestUtil.getFormattedValue( participant.getPayoutQuantity4(),
                                                                                                                                       SSIContestUtil.PAYOUT_DECIMAL_PRECISION ) );
        activities.add( activity4 );
      }
      if ( participant.getActivity5Id() != null && !participant.getActivity5Id().equals( 0L ) )
      {
        SSIContestSummaryTDResultActivityBean activity5 = new SSIContestSummaryTDResultActivityBean( participant.getActivity5Id(),
                                                                                                     SSIContestUtil.getFormattedValue( participant.getActivityAmount5(), precision ),
                                                                                                     SSIContestUtil.getFormattedValue( participant.getPayoutValue5(),
                                                                                                                                       SSIContestUtil.PAYOUT_DECIMAL_PRECISION ),
                                                                                                     SSIContestUtil.getFormattedValue( participant.getPayoutQuantity5(),
                                                                                                                                       SSIContestUtil.PAYOUT_DECIMAL_PRECISION ) );
        activities.add( activity5 );
      }
      if ( participant.getActivity6Id() != null && !participant.getActivity6Id().equals( 0L ) )
      {
        SSIContestSummaryTDResultActivityBean activity6 = new SSIContestSummaryTDResultActivityBean( participant.getActivity6Id(),
                                                                                                     SSIContestUtil.getFormattedValue( participant.getActivityAmount6(), precision ),
                                                                                                     SSIContestUtil.getFormattedValue( participant.getPayoutValue6(),
                                                                                                                                       SSIContestUtil.PAYOUT_DECIMAL_PRECISION ),
                                                                                                     SSIContestUtil.getFormattedValue( participant.getPayoutQuantity6(),
                                                                                                                                       SSIContestUtil.PAYOUT_DECIMAL_PRECISION ) );
        activities.add( activity6 );
      }
      if ( participant.getActivity7Id() != null && !participant.getActivity7Id().equals( 0L ) )
      {
        SSIContestSummaryTDResultActivityBean activity7 = new SSIContestSummaryTDResultActivityBean( participant.getActivity7Id(),
                                                                                                     SSIContestUtil.getFormattedValue( participant.getActivityAmount7(), precision ),
                                                                                                     SSIContestUtil.getFormattedValue( participant.getPayoutValue7(),
                                                                                                                                       SSIContestUtil.PAYOUT_DECIMAL_PRECISION ),
                                                                                                     SSIContestUtil.getFormattedValue( participant.getPayoutQuantity7(),
                                                                                                                                       SSIContestUtil.PAYOUT_DECIMAL_PRECISION ) );
        activities.add( activity7 );
      }
      if ( participant.getActivity8Id() != null && !participant.getActivity8Id().equals( 0L ) )
      {
        SSIContestSummaryTDResultActivityBean activity8 = new SSIContestSummaryTDResultActivityBean( participant.getActivity8Id(),
                                                                                                     SSIContestUtil.getFormattedValue( participant.getActivityAmount8(), precision ),
                                                                                                     SSIContestUtil.getFormattedValue( participant.getPayoutValue8(),
                                                                                                                                       SSIContestUtil.PAYOUT_DECIMAL_PRECISION ),
                                                                                                     SSIContestUtil.getFormattedValue( participant.getPayoutQuantity8(),
                                                                                                                                       SSIContestUtil.PAYOUT_DECIMAL_PRECISION ) );
        activities.add( activity8 );
      }
      if ( participant.getActivity9Id() != null && !participant.getActivity9Id().equals( 0L ) )
      {
        SSIContestSummaryTDResultActivityBean activity9 = new SSIContestSummaryTDResultActivityBean( participant.getActivity9Id(),
                                                                                                     SSIContestUtil.getFormattedValue( participant.getActivityAmount9(), precision ),
                                                                                                     SSIContestUtil.getFormattedValue( participant.getPayoutValue9(),
                                                                                                                                       SSIContestUtil.PAYOUT_DECIMAL_PRECISION ),
                                                                                                     SSIContestUtil.getFormattedValue( participant.getPayoutQuantity9(),
                                                                                                                                       SSIContestUtil.PAYOUT_DECIMAL_PRECISION ) );
        activities.add( activity9 );
      }
      if ( participant.getActivity10Id() != null && !participant.getActivity10Id().equals( 0L ) )
      {
        SSIContestSummaryTDResultActivityBean activity10 = new SSIContestSummaryTDResultActivityBean( participant.getActivity10Id(),
                                                                                                      SSIContestUtil.getFormattedValue( participant.getActivityAmount10(), precision ),
                                                                                                      SSIContestUtil.getFormattedValue( participant.getPayoutValue10(),
                                                                                                                                        SSIContestUtil.PAYOUT_DECIMAL_PRECISION ),
                                                                                                      SSIContestUtil.getFormattedValue( participant.getPayoutQuantity10(),
                                                                                                                                        SSIContestUtil.PAYOUT_DECIMAL_PRECISION ) );
        activities.add( activity10 );
      }
      SSIContestSummaryTDPaxResultBean participantResult = new SSIContestSummaryTDPaxResultBean();
      participantResult.setUserId( participant.getUserId() );
      participantResult.setOrgUnit( participant.getOrgUnit() );
      participantResult.setLastName( participant.getLastName() );
      participantResult.setFirstName( participant.getFirstName() );
      participantResult.setPayoutAmount( participant.getTotalPayoutValue() );
      participantResult.setActivityDescription( activities );
      participantResult.setContestId( contest.getId() );
      participantsResult.add( participantResult );
    }
    detailBean.addPaxResults( participantsResult, contest.getActivityMeasureType().getCode() );
    return detailBean;
  }

  private void populateParticipantSummaryTableColumnsForDtgt( SSIContest contest,
                                                              boolean isPoints,
                                                              SSIContestSummaryValueBean detailBean,
                                                              List<SSIContestSummaryTDPaxResultBean> participants,
                                                              SSIContestPayoutDtgtTotalsValueBean participantsSummaryTotal,
                                                              int precision )
  {
    String activityPrefix = getActivityPrefix( contest );
    String payoutPrefix = getPayoutPrefix( contest );
    String payoutSuffix = getPayoutSuffix( contest );

    // populate column headers, 1st column
    List<SSIContestSummaryTDColumnBean> columns = new ArrayList<SSIContestSummaryTDColumnBean>();
    columns.add( new SSIContestSummaryTDColumnBean( 1L,
                                                    "participants",
                                                    STRING,
                                                    CmsResourceBundle.getCmsBundle().getString( "ssi_contest.participant.PAX" ),
                                                    true,
                                                    CmsResourceBundle.getCmsBundle().getString( "ssi_contest.participant.TOTALS" ),
                                                    false ) );

    // 2nd column
    columns.add( new SSIContestSummaryTDColumnBean( 2L, "orgUnit", STRING, CmsResourceBundle.getCmsBundle().getString( "ssi_contest.preview.ORG_UNIT" ), true, null, false ) );

    // 3rd columns
    List<SSIContestSummaryTDSubColumnBean> subColumns = new ArrayList<SSIContestSummaryTDSubColumnBean>();
    Long totalNumberOfActivities = 0L;
    for ( SSIContestSummaryTDPaxResultBean participant : participants )
    {
      if ( participant.getActivity1Id() != null && !participant.getActivity1Id().equals( 0L ) )
      {
        addSubColumn( 1, isPoints, subColumns, participantsSummaryTotal, precision, activityPrefix, payoutPrefix, payoutSuffix );
        totalNumberOfActivities++;
      }
      if ( participant.getActivity2Id() != null && !participant.getActivity2Id().equals( 0L ) )
      {
        addSubColumn( 2, isPoints, subColumns, participantsSummaryTotal, precision, activityPrefix, payoutPrefix, payoutSuffix );
        totalNumberOfActivities++;
      }
      if ( participant.getActivity3Id() != null && !participant.getActivity3Id().equals( 0L ) )
      {
        addSubColumn( 3, isPoints, subColumns, participantsSummaryTotal, precision, activityPrefix, payoutPrefix, payoutSuffix );
        totalNumberOfActivities++;
      }
      if ( participant.getActivity4Id() != null && !participant.getActivity4Id().equals( 0L ) )
      {
        addSubColumn( 4, isPoints, subColumns, participantsSummaryTotal, precision, activityPrefix, payoutPrefix, payoutSuffix );
        totalNumberOfActivities++;
      }
      if ( participant.getActivity5Id() != null && !participant.getActivity5Id().equals( 0L ) )
      {
        addSubColumn( 5, isPoints, subColumns, participantsSummaryTotal, precision, activityPrefix, payoutPrefix, payoutSuffix );
        totalNumberOfActivities++;
      }
      if ( participant.getActivity6Id() != null && !participant.getActivity6Id().equals( 0L ) )
      {
        addSubColumn( 6, isPoints, subColumns, participantsSummaryTotal, precision, activityPrefix, payoutPrefix, payoutSuffix );
        totalNumberOfActivities++;
      }
      if ( participant.getActivity7Id() != null && !participant.getActivity7Id().equals( 0L ) )
      {
        addSubColumn( 7, isPoints, subColumns, participantsSummaryTotal, precision, activityPrefix, payoutPrefix, payoutSuffix );
        totalNumberOfActivities++;
      }
      if ( participant.getActivity8Id() != null && !participant.getActivity8Id().equals( 0L ) )
      {
        addSubColumn( 8, isPoints, subColumns, participantsSummaryTotal, precision, activityPrefix, payoutPrefix, payoutSuffix );
        totalNumberOfActivities++;
      }
      if ( participant.getActivity9Id() != null && !participant.getActivity9Id().equals( 0L ) )
      {
        addSubColumn( 9, isPoints, subColumns, participantsSummaryTotal, precision, activityPrefix, payoutPrefix, payoutSuffix );
        totalNumberOfActivities++;
      }
      if ( participant.getActivity10Id() != null && !participant.getActivity10Id().equals( 0L ) )
      {
        addSubColumn( 10, isPoints, subColumns, participantsSummaryTotal, precision, activityPrefix, payoutPrefix, payoutSuffix );
        totalNumberOfActivities++;
      }
      break;
    }

    // populating 3rd main columns based on number of activites
    long columnIndex = 3;
    for ( int i = 1; i <= totalNumberOfActivities; i++ )
    {
      String activityDescriptionHeading = getActivityDescription( i, participants );
      SSIContestSummaryTDColumnBean column3_i = new SSIContestSummaryTDColumnBean( columnIndex, "activityDescriptionCol" + i, HEADER, activityDescriptionHeading, false, "", true );
      columns.add( column3_i );
      columnIndex++;
    }

    // now set the 3rd sub columns
    detailBean.setSubColumns( subColumns );

    // 4th column, if points
    String fourthColumnName = null;
    if ( isPoints )
    {
      fourthColumnName = contest.getStatus().isFinalizeResults()
          ? CmsResourceBundle.getCmsBundle().getString( "ssi_contest.participant.TOTAL_POINTS" )
          : CmsResourceBundle.getCmsBundle().getString( "ssi_contest.participant.TOTAL_POTENTIAL_POINTS" );
    }
    else
    {
      fourthColumnName = contest.getStatus().isFinalizeResults()
          ? CmsResourceBundle.getCmsBundle().getString( "ssi_contest.participant.TOTAL_VALUE" )
          : CmsResourceBundle.getCmsBundle().getString( "ssi_contest.participant.TOTAL_POTENTIAL_VALUE" );
    }
    SSIContestSummaryTDColumnBean column4 = new SSIContestSummaryTDColumnBean( 3 + totalNumberOfActivities,
                                                                               "totalPointsIssued",
                                                                               NUMBER,
                                                                               fourthColumnName,
                                                                               true,
                                                                               payoutPrefix + SSIContestUtil.getFormattedValue( participantsSummaryTotal.getPayoutValueTotal(),
                                                                                                                                SSIContestUtil.PAYOUT_DECIMAL_PRECISION )
                                                                                   + payoutSuffix,
                                                                               false );
    columns.add( column4 );
    detailBean.setColumns( columns );
  }

  private void addSubColumn( int index,
                             boolean isPoints,
                             List<SSIContestSummaryTDSubColumnBean> subColumns,
                             SSIContestPayoutDtgtTotalsValueBean participantsSummaryTotal,
                             int precision,
                             String activityPrefix,
                             String payoutPrefix,
                             String payoutSuffix )
  {
    String activityColumnName = "activityDescriptionCol" + index + "_" + StringUtils.lowerCase( "Activity" );
    String payoutColumnName = "activityDescriptionCol" + index + "_" + StringUtils.lowerCase( "PotentialPoints" );
    String activityAmount = activityPrefix + getActivityAmount( index, participantsSummaryTotal, precision );
    String payoutValue = payoutPrefix + getPayoutValue( index, participantsSummaryTotal ) + payoutSuffix;

    String payoutQuantity = getPayoutQuantity( index, participantsSummaryTotal );
    subColumns.add( new SSIContestSummaryTDSubColumnBean( 11L, activityColumnName, NUMBER, CmsResourceBundle.getCmsBundle().getString( "ssi_contest.participant.ACTIVITY" ), true, activityAmount ) );
    subColumns
        .add( new SSIContestSummaryTDSubColumnBean( 12L,
                                                    payoutColumnName,
                                                    NUMBER,
                                                    isPoints
                                                        ? CmsResourceBundle.getCmsBundle().getString( "ssi_contest.participant.POINTS_UPPERCASE" )
                                                        : CmsResourceBundle.getCmsBundle().getString( "ssi_contest.participant.PAYOUT_QUANTITY" ),
                                                    true,
                                                    isPoints ? payoutValue : payoutQuantity ) );
    if ( !isPoints )
    {
      String payoutValueColumnName = "activityDescriptionCol" + index + "_" + StringUtils.lowerCase( "PayoutValue" );
      subColumns
          .add( new SSIContestSummaryTDSubColumnBean( 13L, payoutValueColumnName, NUMBER, CmsResourceBundle.getCmsBundle().getString( "ssi_contest.participant.PAYOUT_VALUE" ), true, payoutValue ) );
    }
  }

  private String getActivityAmount( int index, SSIContestPayoutDtgtTotalsValueBean participantsSummaryTotal, int precision )
  {
    String activityAmount = null;
    switch ( index )
    {
      case 1:
        activityAmount = SSIContestUtil.getFormattedValue( participantsSummaryTotal.getActivityAmountTotal1(), precision );
        break;
      case 2:
        activityAmount = SSIContestUtil.getFormattedValue( participantsSummaryTotal.getActivityAmountTotal2(), precision );
        break;
      case 3:
        activityAmount = SSIContestUtil.getFormattedValue( participantsSummaryTotal.getActivityAmountTotal3(), precision );
        break;
      case 4:
        activityAmount = SSIContestUtil.getFormattedValue( participantsSummaryTotal.getActivityAmountTotal4(), precision );
        break;
      case 5:
        activityAmount = SSIContestUtil.getFormattedValue( participantsSummaryTotal.getActivityAmountTotal5(), precision );
        break;
      case 6:
        activityAmount = SSIContestUtil.getFormattedValue( participantsSummaryTotal.getActivityAmountTotal6(), precision );
        break;
      case 7:
        activityAmount = SSIContestUtil.getFormattedValue( participantsSummaryTotal.getActivityAmountTotal7(), precision );
        break;
      case 8:
        activityAmount = SSIContestUtil.getFormattedValue( participantsSummaryTotal.getActivityAmountTotal8(), precision );
        break;
      case 9:
        activityAmount = SSIContestUtil.getFormattedValue( participantsSummaryTotal.getActivityAmountTotal9(), precision );
        break;
      case 10:
        activityAmount = SSIContestUtil.getFormattedValue( participantsSummaryTotal.getActivityAmountTotal10(), precision );
        break;
      default:
        activityAmount = "";
        break;
    }
    return activityAmount;
  }

  private String getPayoutValue( int index, SSIContestPayoutDtgtTotalsValueBean participantsSummaryTotal )
  {
    String payoutValue = null;
    switch ( index )
    {
      case 1:
        payoutValue = SSIContestUtil.getFormattedValue( participantsSummaryTotal.getPayoutValueTotal1(), SSIContestUtil.PAYOUT_DECIMAL_PRECISION );
        break;
      case 2:
        payoutValue = SSIContestUtil.getFormattedValue( participantsSummaryTotal.getPayoutValueTotal2(), SSIContestUtil.PAYOUT_DECIMAL_PRECISION );
        break;
      case 3:
        payoutValue = SSIContestUtil.getFormattedValue( participantsSummaryTotal.getPayoutValueTotal3(), SSIContestUtil.PAYOUT_DECIMAL_PRECISION );
        break;
      case 4:
        payoutValue = SSIContestUtil.getFormattedValue( participantsSummaryTotal.getPayoutValueTotal4(), SSIContestUtil.PAYOUT_DECIMAL_PRECISION );
        break;
      case 5:
        payoutValue = SSIContestUtil.getFormattedValue( participantsSummaryTotal.getPayoutValueTotal5(), SSIContestUtil.PAYOUT_DECIMAL_PRECISION );
        break;
      case 6:
        payoutValue = SSIContestUtil.getFormattedValue( participantsSummaryTotal.getPayoutValueTotal6(), SSIContestUtil.PAYOUT_DECIMAL_PRECISION );
        break;
      case 7:
        payoutValue = SSIContestUtil.getFormattedValue( participantsSummaryTotal.getPayoutValueTotal7(), SSIContestUtil.PAYOUT_DECIMAL_PRECISION );
        break;
      case 8:
        payoutValue = SSIContestUtil.getFormattedValue( participantsSummaryTotal.getPayoutValueTotal8(), SSIContestUtil.PAYOUT_DECIMAL_PRECISION );
        break;
      case 9:
        payoutValue = SSIContestUtil.getFormattedValue( participantsSummaryTotal.getPayoutValueTotal9(), SSIContestUtil.PAYOUT_DECIMAL_PRECISION );
        break;
      case 10:
        payoutValue = SSIContestUtil.getFormattedValue( participantsSummaryTotal.getPayoutValueTotal10(), SSIContestUtil.PAYOUT_DECIMAL_PRECISION );
        break;
      default:
        payoutValue = "";
        break;
    }
    return payoutValue;
  }

  private String getPayoutQuantity( int index, SSIContestPayoutDtgtTotalsValueBean participantsSummaryTotal )
  {
    String payoutQuantity = null;
    switch ( index )
    {
      case 1:
        payoutQuantity = SSIContestUtil.getFormattedValue( participantsSummaryTotal.getPayoutQuantityTotal1(), SSIContestUtil.PAYOUT_DECIMAL_PRECISION );
        break;
      case 2:
        payoutQuantity = SSIContestUtil.getFormattedValue( participantsSummaryTotal.getPayoutQuantityTotal2(), SSIContestUtil.PAYOUT_DECIMAL_PRECISION );
        break;
      case 3:
        payoutQuantity = SSIContestUtil.getFormattedValue( participantsSummaryTotal.getPayoutQuantityTotal3(), SSIContestUtil.PAYOUT_DECIMAL_PRECISION );
        break;
      case 4:
        payoutQuantity = SSIContestUtil.getFormattedValue( participantsSummaryTotal.getPayoutQuantityTotal4(), SSIContestUtil.PAYOUT_DECIMAL_PRECISION );
        break;
      case 5:
        payoutQuantity = SSIContestUtil.getFormattedValue( participantsSummaryTotal.getPayoutQuantityTotal5(), SSIContestUtil.PAYOUT_DECIMAL_PRECISION );
        break;
      case 6:
        payoutQuantity = SSIContestUtil.getFormattedValue( participantsSummaryTotal.getPayoutQuantityTotal6(), SSIContestUtil.PAYOUT_DECIMAL_PRECISION );
        break;
      case 7:
        payoutQuantity = SSIContestUtil.getFormattedValue( participantsSummaryTotal.getPayoutQuantityTotal7(), SSIContestUtil.PAYOUT_DECIMAL_PRECISION );
        break;
      case 8:
        payoutQuantity = SSIContestUtil.getFormattedValue( participantsSummaryTotal.getPayoutQuantityTotal8(), SSIContestUtil.PAYOUT_DECIMAL_PRECISION );
        break;
      case 9:
        payoutQuantity = SSIContestUtil.getFormattedValue( participantsSummaryTotal.getPayoutQuantityTotal9(), SSIContestUtil.PAYOUT_DECIMAL_PRECISION );
        break;
      case 10:
        payoutQuantity = SSIContestUtil.getFormattedValue( participantsSummaryTotal.getPayoutQuantityTotal10(), SSIContestUtil.PAYOUT_DECIMAL_PRECISION );
        break;
      default:
        payoutQuantity = "";
        break;
    }
    return payoutQuantity;
  }

  private String getActivityDescription( int index, List<SSIContestSummaryTDPaxResultBean> participants )
  {
    String activityDescription = null;
    for ( SSIContestSummaryTDPaxResultBean participant : participants )
    {
      switch ( index )
      {
        case 1:
          activityDescription = participant.getActivityDescription1();
          break;
        case 2:
          activityDescription = participant.getActivityDescription2();
          break;
        case 3:
          activityDescription = participant.getActivityDescription3();
          break;
        case 4:
          activityDescription = participant.getActivityDescription4();
          break;
        case 5:
          activityDescription = participant.getActivityDescription5();
          break;
        case 6:
          activityDescription = participant.getActivityDescription6();
          break;
        case 7:
          activityDescription = participant.getActivityDescription7();
          break;
        case 8:
          activityDescription = participant.getActivityDescription8();
          break;
        case 9:
          activityDescription = participant.getActivityDescription9();
          break;
        case 10:
          activityDescription = participant.getActivityDescription10();
          break;
        default:
          activityDescription = "";
          break;
      }
      break; // break loop after first iteration
    }
    return activityDescription;
  }

  private SSIContestSummaryValueBean getParticipantsSummaryDataStepItUp( SSIContest contest, Map<String, Object> output, boolean isCreator )
  {
    List<SSIContestSummaryTDPaxResultBean> participantsSummary = (List<SSIContestSummaryTDPaxResultBean>)output.get( "p_out_siu_ref_cursor" );

    SSIContestSummaryValueBean detailBean = new SSIContestSummaryValueBean();
    detailBean.setContestType( contest.getContestType().getCode() );
    detailBean.setPayoutType( contest.getPayoutType().getCode() );
    detailBean.setIncludeBonus( contest.isIncludeBonus() );
    detailBean.setIncludeBaseline( !contest.getIndividualBaselineType().isNo() );
    detailBean.setContestId( contest.getId() );
    detailBean.setTotal( (Integer)output.get( "p_out_size_data" ) );
    detailBean.setFooterActive( true );

    populateParticipantSummaryTableColumnsForStepItUp( contest, detailBean, output, isCreator );

    detailBean.addPaxResults( participantsSummary, contest.getActivityMeasureType().getCode() );
    return detailBean;
  }

  private void populateParticipantSummaryTableColumnsForStepItUp( SSIContest contest, SSIContestSummaryValueBean detailBean, Map<String, Object> output, boolean isCreator )
  {
    SSIContestPayoutStepItUpTotalsValueBean participantsSummaryTotal = (SSIContestPayoutStepItUpTotalsValueBean)output.get( "p_out_siu_total_ref_cursor" );

    // labels
    String participantLabel = CmsResourceBundle.getCmsBundle().getString( "ssi_contest.participant.PAX" );
    String totalLabel = CmsResourceBundle.getCmsBundle().getString( "ssi_contest.participant.TOTALS" );
    String orgUnitLable = CmsResourceBundle.getCmsBundle().getString( "ssi_contest.preview.ORG_UNIT" );
    String levelLabel = contest.getStatus().isFinalizeResults()
        ? CmsResourceBundle.getCmsBundle().getString( "ssi_contest.payout_stepitup.LEVEL_ACHIEVED" )
        : CmsResourceBundle.getCmsBundle().getString( "ssi_contest.payout_stepitup.LEVEL_COMPLETED" );
    String baselineLabel = CmsResourceBundle.getCmsBundle().getString( "ssi_contest.payout_stepitup.BASELINE" );
    String activityLabel = CmsResourceBundle.getCmsBundle().getString( "ssi_contest.payout_stepitup.ACTIVITY" );
    String levelPayoutLabel = CmsResourceBundle.getCmsBundle().getString( "ssi_contest.payout_stepitup.LEVEL_PAYOUT" );
    String bonusPayoutLabel = CmsResourceBundle.getCmsBundle().getString( "ssi_contest.payout_objectives.BONUS_PAYOUT" );
    String totalPayoutLabel = CmsResourceBundle.getCmsBundle().getString( "ssi_contest.payout_stepitup.TOTAL_PAYOUT" );
    String payoutValueLabel = CmsResourceBundle.getCmsBundle().getString( "ssi_contest.payout_stepitup.PAYOUT_VALUE" );
    String payoutDescriptionLabel = CmsResourceBundle.getCmsBundle().getString( "ssi_contest.payout_objectives.PAYOUT_DESCRIPTION" );

    // variables
    String activityPrefix = getActivityPrefix( contest );
    String payoutPrefix = getPayoutPrefix( contest );
    String payoutSuffix = getPayoutSuffix( contest );
    long columnIndex = 1;
    int precision = SSIContestUtil.getPrecision( contest.getActivityMeasureType().getCode() );

    // totals
    String activityAmount = activityPrefix + SSIContestUtil.getFormattedValue( participantsSummaryTotal.getActivityAmount(), precision );
    String levelPayout = payoutPrefix + SSIContestUtil.getFormattedValue( participantsSummaryTotal.getLevelPayout(), SSIContestUtil.PAYOUT_DECIMAL_PRECISION ) + payoutSuffix;
    String bonusPayout = payoutPrefix + SSIContestUtil.getFormattedValue( participantsSummaryTotal.getBonusPayout(), SSIContestUtil.PAYOUT_DECIMAL_PRECISION ) + payoutSuffix;
    String totalPayout = payoutPrefix + SSIContestUtil.getFormattedValue( participantsSummaryTotal.getTotalPayout(), SSIContestUtil.PAYOUT_DECIMAL_PRECISION ) + payoutSuffix;
    String totalBaseline = activityPrefix + SSIContestUtil.getFormattedValue( participantsSummaryTotal.getBaselineTotal(), precision );

    // preparing table columns
    List<SSIContestSummaryTDColumnBean> columns = new ArrayList<SSIContestSummaryTDColumnBean>();
    columns.add( new SSIContestSummaryTDColumnBean( columnIndex, "participants", STRING, participantLabel, true, totalLabel, false ) );
    columns.add( new SSIContestSummaryTDColumnBean( ++columnIndex, "orgUnit", STRING, orgUnitLable, true, null, false ) );
    if ( !contest.getIndividualBaselineType().isNo() )
    {
      columns.add( new SSIContestSummaryTDColumnBean( ++columnIndex, "baseline", NUMBER, baselineLabel, true, totalBaseline, false ) );
    }
    columns.add( new SSIContestSummaryTDColumnBean( ++columnIndex, "currentActivity", NUMBER, activityLabel, true, activityAmount, false ) );
    columns.add( new SSIContestSummaryTDColumnBean( ++columnIndex, "levelCompleted", NUMBER, levelLabel, true, "", false ) );
    // columns, if points
    if ( contest.getPayoutType().isPoints() )
    {
      columns.add( new SSIContestSummaryTDColumnBean( ++columnIndex, "levelPayout", NUMBER, levelPayoutLabel, true, levelPayout, false ) );
      if ( contest.isIncludeBonus() )
      {
        columns.add( new SSIContestSummaryTDColumnBean( ++columnIndex, "bonusPayoutSiu", NUMBER, bonusPayoutLabel, true, bonusPayout, false ) );
      }
      columns.add( new SSIContestSummaryTDColumnBean( ++columnIndex, "totalPayoutSiu", NUMBER, totalPayoutLabel, true, totalPayout, false ) );
    }
    else if ( contest.getPayoutType().isOther() )
    {
      // columns for other payout type
      columns.add( new SSIContestSummaryTDColumnBean( ++columnIndex, "payoutDescriptionSiu", NUMBER, payoutDescriptionLabel, true, "", false ) );
      if ( isCreator )
      {
        columns.add( new SSIContestSummaryTDColumnBean( ++columnIndex, "payoutValueLabel", NUMBER, payoutValueLabel, true, totalPayout, false ) );
      }
    }
    detailBean.setColumns( columns );
  }

  private String getPayoutPrefix( SSIContest contest )
  {
    if ( contest.getPayoutType().isOther() )
    {
      Criteria criteria = getSession().createCriteria( Currency.class );
      criteria.add( Restrictions.eq( "currencyCode", contest.getPayoutOtherCurrencyCode().toUpperCase() ) );
      Currency currency = (Currency)criteria.uniqueResult();
      return currency.getCurrencySymbol();
    }
    else
    {
      return "";
    }
  }

  private String getPayoutSuffix( SSIContest contest )
  {
    if ( contest.getPayoutType().isPoints() )
    {
      return " " + CmsResourceBundle.getCmsBundle().getString( "ssi_contest.participant.POINTS_UPPERCASE" );
    }
    else
    {
      return "";
    }
  }

  private String getActivityPrefix( SSIContest contest )
  {
    if ( contest.getActivityMeasureType().isCurrency() )
    {
      Criteria criteria = getSession().createCriteria( Currency.class );
      criteria.add( Restrictions.eq( "currencyCode", contest.getActivityMeasureCurrencyCode().toUpperCase() ) );
      Currency currency = (Currency)criteria.uniqueResult();
      return currency.getCurrencySymbol();
    }
    else
    {
      return "";
    }
  }

  @Override
  public SSIContestBaseLineTotalsValueBean calculateBaseLineTotalsForStepItUp( Long contestId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.contest.calculateBaseLineTotalsForStepItUp" );
    query.setParameter( "contestId", contestId );
    query.setResultTransformer( Transformers.aliasToBean( SSIContestBaseLineTotalsValueBean.class ) );
    return (SSIContestBaseLineTotalsValueBean)query.uniqueResult();
  }

  public Map<String, Object> getContestProgress( Long contestId, Long userId ) throws ServiceErrorException
  {
    CallPrcSSIContestProgress procedure = new CallPrcSSIContestProgress( dataSource );
    Map<String, Object> inParams = new HashMap<String, Object>();
    inParams.put( "contestId", contestId );
    inParams.put( "userId", userId );
    Map<String, Object> outParams = procedure.executeProcedure( inParams );
    Integer returnCode = (Integer)outParams.get( "p_out_return_code" );
    if ( returnCode != 0 )
    {
      log.error( "Stored procedure returned error. Procedure returned: " + returnCode );
      throw new ServiceErrorException( "ssi_contest.generalInfo.CONTEST_PROGRESS_ERR" );
    }
    Map<String, Object> contestData = new HashMap<String, Object>();
    contestData.put( "contestType", outParams.get( "p_out_contest_type" ) );
    contestData.put( "contestProgressData", outParams.get( "p_out_ref_cursor" ) );
    contestData.put( "contestProgressSiuData", outParams.get( "p_out_siu_ref_cursor" ) );
    contestData.put( "contestProgressSiuLevelsData", outParams.get( "p_out_siu_level_ref_cursor" ) );
    contestData.put( "contestProgressSrData", outParams.get( "p_out_sr_ref_cursor" ) );
    contestData.put( "contestProgressSrPayoutsData", outParams.get( "p_out_sr_payout_ref_cursor" ) );
    contestData.put( "contestProgressStackRankData", outParams.get( "p_out_stackrank_cursor" ) );
    contestData.put( "contestProgressStackRankPaxData", outParams.get( "p_out_sr_pax_ref_cursor" ) );
    return contestData;
  }

  public Map<String, Object> getContestProgressForTile( Long contestId ) throws ServiceErrorException
  {
    CallPrcSSIContestProgressTile procedure = new CallPrcSSIContestProgressTile( dataSource );
    Map<String, Object> inParams = new HashMap<String, Object>();
    inParams.put( "contestId", contestId );
    Map<String, Object> outParams = procedure.executeProcedure( inParams );
    Integer returnCode = (Integer)outParams.get( "p_out_return_code" );
    if ( returnCode != 0 )
    {
      log.error( "Stored procedure returned error. Procedure returned: " + returnCode );
      throw new ServiceErrorException( "ssi_contest.generalInfo.CONTEST_PROGRESS_ERR" );
    }
    Map<String, Object> contestData = new HashMap<String, Object>();
    contestData.put( "contestType", outParams.get( "p_out_contest_type" ) );
    contestData.put( "contestProgressObjData", outParams.get( "p_out_obj_ref_cursor" ) );
    contestData.put( "contestProgressSiuData", outParams.get( "p_out_siu_ref_cursor" ) );
    contestData.put( "contestProgressSrData", outParams.get( "p_out_sr_ref_cursor" ) );
    return contestData;
  }

  @Override
  public void updateContestGoalPercentage( Long contestId ) throws ServiceErrorException
  {
    CallPrcSSIContestUpdateGoalPercentage procedure = new CallPrcSSIContestUpdateGoalPercentage( dataSource );
    Map<String, Object> outParams = procedure.executeProcedure( contestId );
    Integer returnCode = (Integer)outParams.get( "p_out_return_code" );
    if ( returnCode != 0 )
    {
      String returnErrorMessage = (String)outParams.get( "p_out_error_message" );
      log.error( "Stored procedure returned error. Procedure returned: " + returnCode + "\n" + returnErrorMessage );
      throw new ServiceErrorException( "ssi_contest.creator.CONTEST_GOAL_PERC_ERR" );
    }
  }

  @Override
  public void updateContestStackRank( Long contestId ) throws ServiceErrorException
  {
    CallPrcSSIContestUpdateStackRank procedure = new CallPrcSSIContestUpdateStackRank( dataSource );
    Map<String, Object> outParams = procedure.executeProcedure( contestId );

    Integer returnCode = (Integer)outParams.get( "p_out_return_code" );
    if ( returnCode != 0 )
    {
      String returnErrorMessage = (String)outParams.get( "p_out_error_message" );
      log.error( "Stored procedure returned error. Procedure returned: " + returnCode + "\n" + returnErrorMessage );
      throw new ServiceErrorException( "ssi_contest.participant.CONTEST_UPD_STACKRANK_ERR" );
    }
  }

  /**
   * 
   * @param contestId
   * @return Map with activity ids as keys and activity totals as values; for non DTGT contest one entry with null activity id
   */
  @Override
  public Map<String, Double> getContestActivityTotals( Long contestId )
  {
    final Map<String, Double> contestActivityTotals = new HashMap<String, Double>();
    Query query = getSession().createSQLQuery( CONTEST_PROGRESS_SQL );
    query.setParameter( "contestId", contestId );
    query.setResultTransformer( new BaseResultTransformer()
    {
      @Override
      public Object transformTuple( Object[] tuple, String[] aliases )
      {
        contestActivityTotals.put( tuple[0] == null ? null : tuple[0].toString(), extractDouble( tuple[1] ) );
        return null;
      }
    } );
    query.list();
    return contestActivityTotals;
  }

  /**
   * 
   * @param contestId
   * @return Map with user ids as keys and journal ids as values
   */
  @Override
  public List<DepositProcessBean> getContestUserJournalList( Long contestId, Short awardIssuanceNumber )
  {
    final List<DepositProcessBean> userJournalList = new ArrayList<DepositProcessBean>();
    StringBuffer selectContestUserJournalSql = new StringBuffer( 500 );

    selectContestUserJournalSql.append( "SELECT USER_ID, JOURNAL_ID FROM SSI_CONTEST_PAX_PAYOUT WHERE SSI_CONTEST_ID =  :contestId" );
    if ( awardIssuanceNumber != null )
    {
      selectContestUserJournalSql.append( " AND AWARD_ISSUANCE_NUMBER = :awardIssuanceNumber" );
    }
    selectContestUserJournalSql.append( " ORDER BY USER_ID" );
    Query query = getSession().createSQLQuery( selectContestUserJournalSql.toString() );
    query.setParameter( "contestId", contestId );
    if ( awardIssuanceNumber != null )
    {
      query.setParameter( "awardIssuanceNumber", awardIssuanceNumber );
    }
    query.setResultTransformer( new BaseResultTransformer()
    {
      @Override
      public Object transformTuple( Object[] tuple, String[] aliases )
      {
        DepositProcessBean depositProcessBean = new DepositProcessBean();
        depositProcessBean.setJournalId( tuple[1] == null ? null : extractLong( tuple[1] ) );
        depositProcessBean.setParticipantId( extractLong( tuple[0] ) );
        userJournalList.add( depositProcessBean );
        return null;
      }
    } );
    query.list();
    return userJournalList;
  }

  @Override
  public List<SSIContest> getAllContestsToLaunch( SSIContestStatus contestStatus )
  {
    Criteria criteria = getSession().createCriteria( SSIContest.class, "ssiContest" );
    criteria.add( Restrictions.eq( "status", contestStatus ) );
    criteria.add( Restrictions.le( "startDate", DateUtils.getCurrentDateTrimmed() ) );
    criteria.addOrder( Order.asc( "ssiContest.startDate" ) );
    return criteria.list();
  }

  public Integer getOpenContestCount( Long promotionId, String contestType )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.contest.OpenContestCount" );
    query.setParameter( "promotionId", promotionId );
    query.setParameter( "contestType", contestType );
    return (Integer)query.uniqueResult();
  }

  public SSIContestPayoutStackRankTotalsValueBean getStackRankTotals( Long contestId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.contest.calculateTotalsForStackRank" );
    query.setParameter( "contestId", contestId );
    query.setResultTransformer( Transformers.aliasToBean( SSIContestPayoutStackRankTotalsValueBean.class ) );
    return (SSIContestPayoutStackRankTotalsValueBean)query.uniqueResult();
  }

  public List<SSIContestManager> getContestManagers( Long contestId, Integer pageNumber, Integer pageSize, String sortColumnName, String sortOrder )
  {
    Criteria criteria = getSession().createCriteria( SSIContestManager.class );
    criteria.setMaxResults( pageSize );

    if ( pageNumber > 1 )
    {
      criteria.setFirstResult( pageSize * ( pageNumber - 1 ) );
    }

    criteria.add( Restrictions.eq( "contest.id", contestId ) );

    if ( StringUtil.isNullOrEmpty( sortColumnName ) )
    {
      sortColumnName = SSIContestManager.DEFAULT_SORTED_ON;
    }

    if ( StringUtil.isNullOrEmpty( sortOrder ) )
    {
      sortOrder = SSIContestManager.DEFAULT_SORTED_BY;
    }

    if ( SSIContestManager.DEFAULT_SORTED_ON.equals( sortColumnName ) )
    {
      criteria.createAlias( "manager", "manager" );
      sortColumnName = "manager." + sortColumnName;
    }

    criteria.addOrder( SSIContestManager.DEFAULT_SORTED_BY.equals( sortOrder ) ? Order.asc( sortColumnName ) : Order.desc( sortColumnName ) );

    return criteria.list();

  }

  public List<SSIContestSuperViewer> getContestSuperViewers( Long contestId, Integer pageNumber, Integer pageSize, String sortColumnName, String sortOrder )
  {
    Criteria criteria = getSession().createCriteria( SSIContestSuperViewer.class );
    criteria.setMaxResults( pageSize );

    if ( pageNumber > 1 )
    {
      criteria.setFirstResult( pageSize * ( pageNumber - 1 ) );
    }

    criteria.add( Restrictions.eq( "contest.id", contestId ) );

    if ( StringUtil.isNullOrEmpty( sortColumnName ) )
    {
      sortColumnName = SSIContestSuperViewer.DEFAULT_SORTED_ON;
    }

    if ( StringUtil.isNullOrEmpty( sortOrder ) )
    {
      sortOrder = SSIContestSuperViewer.DEFAULT_SORTED_BY;
    }

    if ( SSIContestSuperViewer.DEFAULT_SORTED_ON.equals( sortColumnName ) )
    {
      criteria.createAlias( "superViewer", "superViewer" );
      sortColumnName = "superViewer." + sortColumnName;
    }

    criteria.addOrder( SSIContestSuperViewer.DEFAULT_SORTED_BY.equals( sortOrder ) ? Order.asc( sortColumnName ) : Order.desc( sortColumnName ) );

    return criteria.list();

  }

  public boolean approveContestPayouts( SSIContest ssiContest, Long userId, Short awardIssuanceNumber, String csvUserIds, String csvPayoutAmounts ) throws ServiceErrorException
  {
    CallPrcSSIContestPaxPayout procedure = new CallPrcSSIContestPaxPayout( dataSource );
    Map<String, Object> outParams = procedure.executeProcedure( ssiContest.getId(), userId, awardIssuanceNumber, csvUserIds, csvPayoutAmounts );

    Integer returnCode = (Integer)outParams.get( "p_out_return_code" );
    if ( returnCode != 0 )
    {
      throw new ServiceErrorException( "Data Access Error, Stored procedure returned error: " + returnCode + "\n" );
    }
    return returnCode == 0 ? true : false;
  }

  public Long getHighestLevelPayout( Long contestId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.contest.HighestLevelPayout" );
    query.setParameter( "contestId", contestId );
    return (Long)query.uniqueResult();
  }

  public int getBadgeCountInSsiContest( Long promotionId, Long badgeRuleId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.contest.isBadgeInSsiContest" );
    query.setParameter( "promotionId", promotionId );
    query.setParameter( "badgeRuleId", badgeRuleId );
    Integer count = (Integer)query.uniqueResult();
    return count.intValue();
  }

  @SuppressWarnings( "unchecked" )
  @Override
  public Map<String, Object> getSSIContestUserManagerList( Long contestId, Long paxId ) throws ServiceErrorException
  {
    Map<String, Object> inParams = new HashMap<String, Object>();

    inParams.put( "p_in_ssi_contest_id", contestId );
    inParams.put( "p_in_user_id", paxId );

    CallPrcSSIContestUserManagerList procedure = new CallPrcSSIContestUserManagerList( dataSource );
    Map<String, Object> outParams = procedure.executeProcedure( inParams );

    Integer returnCode = (Integer)outParams.get( "p_out_return_code" );
    if ( returnCode != 0 )
    {
      log.error( "Stored procedure PKG_SSI_CONTEST.PRC_SSI_CONTEST_USER_MANAGERS returned error. with Contest ID:" + contestId + " & Pax ID: " + paxId + "Procedure returned: " + returnCode );
      throw new ServiceErrorException( "ssi_contest.pax.manager.NO_MANAGERS_FOUND" );
    }
    Map<String, Object> ssiContestManagers = new HashMap<String, Object>();
    ssiContestManagers.put( "managerList", outParams.get( "p_out_ref_cursor" ) );
    ssiContestManagers.put( "managerCount", outParams.get( "p_out_count_mgr_owner" ) );
    return ssiContestManagers;
  }

  public SSIContestClaimField getContestClaimFieldById( Long contestClaimFieldId )
  {
    SSIContestClaimField contestClaimField = (SSIContestClaimField)getSession().get( SSIContestClaimField.class, contestClaimFieldId );
    return contestClaimField;
  }

  @SuppressWarnings( "unchecked" )
  @Override
  public List<SSIContestClaimField> getContestClaimFieldsByContestId( Long contestId )
  {
    Criteria criteria = getSession().createCriteria( SSIContestClaimField.class );
    criteria.add( Restrictions.eq( "contest.id", contestId ) );
    return criteria.list();
  }

  @Override
  public int launchNonATNContestArchivalProcess( final java.sql.Date archivalDate ) throws SQLException
  {
    return this.jdbcTemplate.update( NON_ATN_CONTEST_ARCHIVAL_PROCESS, new PreparedStatementSetter()
    {
      @Override
      public void setValues( PreparedStatement ps ) throws SQLException
      {
        ps.setTimestamp( 1, new Timestamp( DateUtils.getCurrentDate().getTime() ) );
        ps.setDate( 2, archivalDate );
      }
    } );

  }

  @Override
  public int launchATNContestArchivalProcessWithoutApprover( final java.sql.Date archivalDate ) throws SQLException
  {
    return this.jdbcTemplate.update( ATN_CONTEST_ARCHIVAL_PROCESS_WITHOUT_APPROVER, new PreparedStatementSetter()
    {
      @Override
      public void setValues( PreparedStatement ps ) throws SQLException
      {
        ps.setTimestamp( 1, new Timestamp( DateUtils.getCurrentDate().getTime() ) );
        ps.setDate( 2, archivalDate );
      }
    } );
  }

  @Override
  public int launchATNContestArchivalProcessWithApprover( final Long ssiContestId ) throws SQLException
  {
    return this.jdbcTemplate.update( ATN_CONTEST_ARCHIVAL_PROCESS_WITH_APPROVER, new PreparedStatementSetter()
    {
      @Override
      public void setValues( PreparedStatement ps ) throws SQLException
      {
        ps.setTimestamp( 1, new Timestamp( DateUtils.getCurrentDate().getTime() ) );
        ps.setLong( 2, ssiContestId );
      }
    } );
  }

  @SuppressWarnings( "unchecked" )
  @Override
  public List<SSIContest> getExpiredAwardThemNowContest( java.sql.Date archivalDate )
  {
    Criteria criteria = getSession().createCriteria( SSIContest.class );
    criteria.add( Restrictions.le( "endDate", archivalDate ) );
    criteria.add( Restrictions.eq( "contestType", SSIContestType.lookup( SSIContestType.AWARD_THEM_NOW ) ) );
    return criteria.list();
  }

  @Override
  public int getWaitingforApprovalAwardThemNowIssuancesCount( Long ssiContestId )
  {
    Criteria criteria = getSession().createCriteria( SSIContestAwardThemNow.class );
    criteria.add( Restrictions.eq( "contest.id", ssiContestId ) );
    criteria.add( Restrictions.eq( "issuanceStatusType", SSIContestIssuanceStatusType.lookup( SSIContestIssuanceStatusType.WAITING_FOR_APPROVAL ) ) );
    return criteria.list().size();
  }

  public Map<String, Object> getContestSRPayoutList( Long contestId ) throws ServiceErrorException
  {
    CallPrcSSIContestSRPayoutList procedure = new CallPrcSSIContestSRPayoutList( dataSource );
    Map<String, Object> inParams = new HashMap<String, Object>();
    inParams.put( "contestId", contestId );
    Map<String, Object> outParams = procedure.executeProcedure( inParams );
    Integer returnCode = (Integer)outParams.get( "p_out_return_code" );
    if ( returnCode != 0 )
    {
      log.error( "Stored procedure returned error. Procedure returned: " + returnCode );
      throw new ServiceErrorException( "ssi_contest.generalInfo.CONTEST_PROGRESS_ERR" );
    }
    Map<String, Object> contestData = new HashMap<String, Object>();
    contestData.put( "contestSrPayoutsData", outParams.get( "p_out_sr_payout_ref_cursor" ) );
    return contestData;
  }

  @Override
  public SSIContestPaxPayout getPaxPayout( Long contestId, Long userId )
  {
    Criteria criteria = getSession().createCriteria( SSIContestPaxPayout.class );
    criteria.add( Restrictions.eq( "contest.id", contestId ) );
    criteria.add( Restrictions.eq( "participant.id", userId ) );
    return (SSIContestPaxPayout)criteria.uniqueResult();
  }

  @Override
  public List<SSIContest> getContestSearchByAdmin( List<String> assetCodes, List<Long> userIDs, String ssiContestStatus, Date startDate, Date endDate )
  {
    Criteria criteria = getSession().createCriteria( SSIContest.class );

    if ( assetCodes != null && assetCodes.size() > 0 )
    {

      criteria.add( Restrictions.in( "cmAssetCode", assetCodes ) );
    }

    if ( userIDs != null && userIDs.size() > 0 )
    {

      criteria.add( Restrictions.in( "contestOwnerId", userIDs ) );
    }

    if ( ssiContestStatus != null && !ssiContestStatus.equals( "ALL" ) )
    {
      criteria.add( Restrictions.eq( "status", SSIContestStatus.lookup( ssiContestStatus ) ) );
    }

    if ( startDate != null )
    {
      criteria.add( Restrictions.ge( "startDate", startDate ) );
    }

    if ( endDate != null )
    {
      criteria.add( Restrictions.le( "endDate", endDate ) );
    }
    criteria.add( Restrictions.ne( "contestType", SSIContestType.lookup( SSIContestType.AWARD_THEM_NOW ) ) );
    return criteria.list();
  }

  @Override
  public void saveAdminAction( SSIAdminContestActions ssiAdminContestActions )
  {
    getSession().saveOrUpdate( ssiAdminContestActions );
  }

  @Override
  public SSIAdminContestActions getAdminActionByEditCreator( Long contestID )
  {
    Criteria criteria = getSession().createCriteria( SSIAdminContestActions.class );
    criteria.add( Restrictions.eq( "contestID", contestID ) );
    criteria.addOrder( Order.desc( "id" ) );
    criteria.setMaxResults( 1 );
    return (SSIAdminContestActions)criteria.uniqueResult();
  }

  @Override
  public Map getContsetPaxManagerSVExtract( Map<String, Object> reportParameters, String ssiType )
  {
    if ( SSIContestType.OBJECTIVES.equals( ssiType ) )
    {
      CallPrcSSIAudienceObjExtract procedure = new CallPrcSSIAudienceObjExtract( dataSource );
      return procedure.executeProcedure( reportParameters );
    }
    else if ( SSIContestType.AWARD_THEM_NOW.equals( ssiType ) )
    {
      CallPrcSSIAudienceATNExtract procedure = new CallPrcSSIAudienceATNExtract( dataSource );
      return procedure.executeProcedure( reportParameters );
    }
    else if ( SSIContestType.DO_THIS_GET_THAT.equals( ssiType ) )
    {
      CallPrcSSIAudienceDTGTExtract procedure = new CallPrcSSIAudienceDTGTExtract( dataSource );
      return procedure.executeProcedure( reportParameters );
    }
    else if ( SSIContestType.STACK_RANK.equals( ssiType ) )
    {
      CallPrcSSIAudienceSRExtract procedure = new CallPrcSSIAudienceSRExtract( dataSource );
      return procedure.executeProcedure( reportParameters );
    }
    else if ( SSIContestType.STEP_IT_UP.equals( ssiType ) )
    {
      CallPrcSSIAudienceSITExtract procedure = new CallPrcSSIAudienceSITExtract( dataSource );
      return procedure.executeProcedure( reportParameters );
    }
    return null;
  }

  @Override
  public Map getContsetErrorExtract( Map<String, Object> reportParameters )
  {
    CallPrcSSIErrorExtract procedure = new CallPrcSSIErrorExtract( dataSource );
    return procedure.executeProcedure( reportParameters );
  }

  @Override
  public List<NameableBean> getSSIContestList( String contestType )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.contest.getContestListForSSIContestFileLoad" );
    query.setString( "contestType", contestType );
    query.setResultTransformer( new ContestListForSSIContestFileLoadResultTransformer() );
    return query.list();
  }

  @SuppressWarnings( "serial" )
  private static class ContestListForSSIContestFileLoadResultTransformer extends BaseResultTransformer
  {
    @Override
    public Object transformTuple( Object[] tuple, String[] aliases )
    {
      NameableBean bean = new NameableBean( (long)tuple[0], (String)tuple[1] );

      return bean;
    }
  }

  @Override
  public Map<String, Object> verifyImportFile( Long importFileId, String loadType, Long contestId, String contestType )
  {
    try
    {
      if ( StringUtils.isEmpty( contestType ) )
      {
        throw new Exception( "contestType cannot be null" );
      }
      String STORED_PROC_NAME = "";
      CallPrcSSIAllContestsVerifyImport prc = null;
      if ( SSIContestType.lookup( contestType ).isObjectives() )
      {
        STORED_PROC_NAME = "pkg_ssi_objective_load .p_ssi_objective_verify_import";
        prc = new CallPrcSSIAllContestsVerifyImport( dataSource, STORED_PROC_NAME );
        return prc.executeProcedure( importFileId, loadType, contestId );
      }
      else if ( SSIContestType.lookup( contestType ).isDoThisGetThat() )
      {
        STORED_PROC_NAME = "pkg_ssi_dtgt_load. p_ssi_dtgt_verify_import";
        prc = new CallPrcSSIAllContestsVerifyImport( dataSource, STORED_PROC_NAME );
        return prc.executeProcedure( importFileId, loadType, contestId );
      }
      else if ( SSIContestType.lookup( contestType ).isStackRank() )
      {
        STORED_PROC_NAME = "pkg_ssi_stack_rank_load .p_ssi_stack_rank_verify_import";
        prc = new CallPrcSSIAllContestsVerifyImport( dataSource, STORED_PROC_NAME );
        return prc.executeProcedure( importFileId, loadType, contestId );
      }
      else if ( SSIContestType.lookup( contestType ).isStepItUp() )
      {
        STORED_PROC_NAME = "pkg_ssi_step_it_up_load .p_ssi_step_it_up_verify_import";
        prc = new CallPrcSSIAllContestsVerifyImport( dataSource, STORED_PROC_NAME );
        return prc.executeProcedure( importFileId, loadType, contestId );
      }
      else if ( SSIContestType.lookup( contestType ).isAwardThemNow() )
      {
        STORED_PROC_NAME = "pkg_ssi_atn_load. p_ssi_atn_verify_import";
        prc = new CallPrcSSIAllContestsVerifyImport( dataSource, STORED_PROC_NAME );
      }
    }
    catch( Exception e )
    {
      e.printStackTrace();
    }
    return null;

  }

  @SuppressWarnings( "unchecked" )
  @Override
  public List<SSIContestBillCodeBean> getContestBillCodesByContestId( Long contestId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.ui.getConetstBillCodesByContestId" );
    query.setParameter( "contestId", contestId );
    query.setResultTransformer( new SSIContestBillCodeBeanMapper() );
    return query.list();
  }

  @SuppressWarnings( "unchecked" )
  @Override
  public List<SSIContestBillCodeBean> getBillCodesByPromoId( Long promoId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.ui.ssi.getBillCodesByPromoId" );
    query.setParameter( "promoId", promoId );
    query.setResultTransformer( new SSIContestBillCodeBeanMapper() );
    return query.list();
  }

  @SuppressWarnings( "serial" )
  private class SSIContestBillCodeBeanMapper extends BaseResultTransformer
  {
    @Override
    public SSIContestBillCodeBean transformTuple( Object[] tuple, String[] aliases )
    {
      SSIContestBillCodeBean billCodeBean = new SSIContestBillCodeBean();
      Integer sortOrder = (Integer)tuple[0];
      billCodeBean.setSortOrder( sortOrder.longValue() );
      billCodeBean.setBillCode( extractString( tuple[1] ) );
      billCodeBean.setTrackBillCodeBy( extractString( tuple[2] ) );
      billCodeBean.setCustomValue( extractString( tuple[3] ) );
      billCodeBean.setCmAsseCode( extractString( tuple[4] ) );
      billCodeBean.setCmAsseKey( extractString( tuple[5] ) );
      return billCodeBean;
    }
  }

  public boolean fetchContestCreatorCount( Long userId )
  {
    Criteria criteria = getSession().createCriteria( SSIContest.class ).setProjection( Projections.rowCount() ).add( Restrictions.eq( "contestOwnerId", userId ) );
    Long contestCreatorCount = (Long)criteria.uniqueResult();
    if ( contestCreatorCount == 0 )
    {
      return false;
    }
    return true;
  }

  @Override
  public void updateContestParticipants( SSIContestParticipant contestParticipant )
  {
    contestParticipant.setLaunchNotificationSent( true );
    getSession().update( contestParticipant );
  }

  @Override
  public void updateContestManagers( SSIContestManager contestManager )
  {
    contestManager.setLaunchNotificationSent( true );
    getSession().update( contestManager );
  }

  @SuppressWarnings( "unchecked" )
  public List<SSIContestParticipant> getSSIContestATNParticipants( Long ssiContestId, Short awardIssuanceNumber )
  {
    Criteria criteria = getSession().createCriteria( SSIContestParticipant.class );
    criteria.add( Restrictions.eq( "contest.id", ssiContestId ) );
    criteria.add( Restrictions.eq( "awardIssuanceNumber", awardIssuanceNumber ) );
    return criteria.list();
  }

  @SuppressWarnings( "unchecked" )
  public List<SSIContestParticipant> getSSIContestParticipants( Long ssiContestId )
  {
    Criteria criteria = getSession().createCriteria( SSIContestParticipant.class, "ssiContestParticipant" );
    criteria.createAlias( "ssiContestParticipant.contest", "contest" );
    criteria.add( Restrictions.eq( "contest.id", ssiContestId ) );
    criteria.add( Restrictions.ne( "contest.contestType", SSIContestType.lookup( SSIContestType.AWARD_THEM_NOW ) ) );
    return criteria.list();
  }

  public boolean isParticipantInSsiContest( Long contestId, Long userId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.contest.isParticipantInSsiContest" );

    query.setParameter( "contestId", contestId );
    query.setParameter( "userId", userId );

    Integer count = (Integer)query.uniqueResult();

    return count.intValue() > 0;
  }

}
