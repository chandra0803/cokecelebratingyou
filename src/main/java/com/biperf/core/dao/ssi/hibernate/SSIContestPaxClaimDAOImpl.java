
package com.biperf.core.dao.ssi.hibernate;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;

import com.biperf.core.dao.BaseDAO;
import com.biperf.core.dao.BaseResultTransformer;
import com.biperf.core.dao.ssi.SSIContestPaxClaimDAO;
import com.biperf.core.domain.enums.SSIClaimStatus;
import com.biperf.core.domain.enums.SSIContestStatus;
import com.biperf.core.domain.ssi.SSIContestPaxClaim;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.SSIContestUtil;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.ssi.SSIContestListValueBean;
import com.biperf.core.value.ssi.SSIContestPaginationValueBean;
import com.biperf.core.value.ssi.SSIContestPaxClaimCountValueBean;

/**
 * 
 * SSIContestPaxClaimDAOImpl.
 * 
 * @author kancherl
 * @since May 20, 2015
 * @version 1.0
 */

@SuppressWarnings( "unchecked" )
public class SSIContestPaxClaimDAOImpl extends BaseDAO implements SSIContestPaxClaimDAO
{

  protected static final Log log = LogFactory.getLog( SSIContestPaxClaimDAOImpl.class );

  private DataSource dataSource;
  private JdbcTemplate jdbcTemplate;

  private static String APPROVE_PAX_CLAIM_STATUS = "UPDATE SSI_CONTEST_PAX_CLAIM SET STATUS = ?, APPROVER_ID = ?, APPROVE_DENY_DATE = SYSDATE, VERSION=VERSION+1, DATE_MODIFIED = SYSDATE, MODIFIED_BY = ? WHERE SSI_CONTEST_PAX_CLAIM_ID = ?";
  private static String DENY_PAX_CLAIM_STATUS = "UPDATE SSI_CONTEST_PAX_CLAIM SET STATUS = ?, DENIED_REASON = ?, APPROVER_ID = ?, APPROVE_DENY_DATE = SYSDATE, VERSION=VERSION+1, DATE_MODIFIED = SYSDATE, MODIFIED_BY = ? WHERE SSI_CONTEST_PAX_CLAIM_ID = ?";
  private static String APPROVE_ALL_PAX_CLAIM_STATUS = "UPDATE SSI_CONTEST_PAX_CLAIM SET STATUS = ?, APPROVER_ID = ?, APPROVE_DENY_DATE = ?, VERSION=VERSION+1, DATE_MODIFIED = SYSDATE, MODIFIED_BY = ? WHERE SSI_CONTEST_ID = ? AND STATUS = 'waiting_for_approval'";

  public void setDataSource( DataSource dataSource )
  {
    this.dataSource = dataSource;
    this.jdbcTemplate = new JdbcTemplate( dataSource );
  }

  @Override
  public SSIContestPaxClaim getPaxClaimById( Long paxClaimId )
  {
    SSIContestPaxClaim paxClaim = (SSIContestPaxClaim)getSession().get( SSIContestPaxClaim.class, paxClaimId );
    return paxClaim;
  }

  @Override
  public SSIContestPaxClaim savePaxClaim( SSIContestPaxClaim paxClaim ) throws SQLException
  {
    getSession().saveOrUpdate( paxClaim );
    return paxClaim;
  }

  @Override
  public void approvePaxClaim( final String status, final Long approverId, final Long paxClaimId ) throws SQLException
  {
    this.jdbcTemplate.update( APPROVE_PAX_CLAIM_STATUS, new PreparedStatementSetter()
    {
      @Override
      public void setValues( PreparedStatement ps ) throws SQLException
      {
        ps.setString( 1, status );
        ps.setLong( 2, approverId );
        ps.setLong( 3, UserManager.getUserId() );
        ps.setLong( 4, paxClaimId );
      }
    } );
  }

  @Override
  public Date approveAllPaxClaims( final Long contestId, final Long approverId ) throws SQLException
  {
    final Date approveDenyDate = new Date();
    this.jdbcTemplate.update( APPROVE_ALL_PAX_CLAIM_STATUS, new PreparedStatementSetter()
    {
      @Override
      public void setValues( PreparedStatement ps ) throws SQLException
      {
        ps.setString( 1, SSIClaimStatus.APPROVED );
        ps.setLong( 2, approverId );
        ps.setTimestamp( 3, new java.sql.Timestamp( approveDenyDate.getTime() ) );
        ps.setLong( 4, UserManager.getUserId() );
        ps.setLong( 5, contestId );
      }
    } );
    return approveDenyDate;
  }

  @Override
  public void denyPaxClaim( final String status, final String deniedReason, final Long approverId, final Long paxClaimId ) throws SQLException
  {
    this.jdbcTemplate.update( DENY_PAX_CLAIM_STATUS, new PreparedStatementSetter()
    {
      @Override
      public void setValues( PreparedStatement ps ) throws SQLException
      {
        ps.setString( 1, status );
        ps.setString( 2, deniedReason );
        ps.setLong( 3, approverId );
        ps.setLong( 4, UserManager.getUserId() );
        ps.setLong( 5, paxClaimId );
      }
    } );
  }

  @Override
  public List<SSIContestPaxClaim> getPaxClaimsByContestId( Long contestId )
  {
    Criteria criteria = getSession().createCriteria( SSIContestPaxClaim.class );
    criteria.add( Restrictions.eq( "contest.id", contestId ) );
    return criteria.list();
  }

  @Override
  public List<SSIContestPaxClaim> getPaxClaimsBySubmitterId( Long contestId, Long submitterId, SSIContestPaginationValueBean paginationParams )
  {
    Criteria criteria = getSession().createCriteria( SSIContestPaxClaim.class, "paxClaim" );
    criteria.add( Restrictions.eq( "submitterId", submitterId ) );
    criteria.add( Restrictions.eq( "contestId", contestId ) );
    buildCriteriaForSort( criteria, paginationParams );
    criteria.setMaxResults( paginationParams.getPageSize() );
    if ( paginationParams.getCurrentPage() > 1 )
    {
      criteria.setFirstResult( paginationParams.getPageSize() * ( paginationParams.getCurrentPage() - 1 ) );
    }
    return criteria.list();
  }

  private void buildCriteriaForSort( Criteria criteria, SSIContestPaginationValueBean paginationParams )
  {
    if ( SSIContestUtil.ASC.equals( paginationParams.getSortedBy() ) )
    {
      criteria.addOrder( Order.asc( "paxClaim." + paginationParams.getSortedOn() ) );
    }
    else if ( SSIContestUtil.DESC.equals( paginationParams.getSortedBy() ) )
    {
      criteria.addOrder( Order.desc( "paxClaim." + paginationParams.getSortedOn() ) );
    }
  }

  @Override
  public int getPaxClaimsCountBySubmitterId( Long contestId, Long submitterId )
  {
    Criteria criteria = getSession().createCriteria( SSIContestPaxClaim.class, "paxClaim" );
    criteria.setProjection( Projections.rowCount() );
    criteria.add( Restrictions.eq( "submitterId", submitterId ) );
    criteria.add( Restrictions.eq( "contestId", contestId ) );
    return ( (Long)criteria.uniqueResult() ).intValue();
  }

  @Override
  public Double getClaimsActivityAmount( Long contestId, Long submitterId )
  {
    Criteria criteria = getSession().createCriteria( SSIContestPaxClaim.class );
    criteria.setProjection( Projections.sum( "claimAmountQuantity" ) );
    criteria.add( Restrictions.eq( "submitterId", submitterId ) );
    criteria.add( Restrictions.eq( "contestId", contestId ) );
    return (Double)criteria.uniqueResult();
  }

  @Override
  public Map<String, Object> getPaxClaimsByContestIdAndStatus( Long contestId, String claimStatuses, int pageNumber, int recordsPerpage, String sortColumn, String sortOrder )
      throws ServiceErrorException
  {

    CallPrcSSIContestClaimsSummary proc = new CallPrcSSIContestClaimsSummary( dataSource );

    pageNumber = pageNumber < 1 ? 1 : pageNumber;
    int rowStart = ( pageNumber - 1 ) * recordsPerpage + 1;
    int rowEnd = pageNumber * recordsPerpage;
    sortColumn = sortColumn != null ? sortColumn : SSIContestUtil.DEFAULT_SORT_ON;
    sortOrder = sortOrder != null ? sortOrder : SSIContestUtil.DEFAULT_SORT_BY;

    Map<String, Object> inParams = new HashMap<String, Object>();
    inParams.put( "p_in_ssi_contest_id", contestId );
    inParams.put( "p_in_locale", UserManager.getLocale() );
    inParams.put( "p_in_status", claimStatuses );
    inParams.put( "p_in_sortColName", sortColumn );
    inParams.put( "p_in_sortedBy", sortOrder );
    inParams.put( "p_in_rowNumStart", rowStart );
    inParams.put( "p_in_rowNumEnd", rowEnd );

    Map<String, Object> outParams = proc.executeProcedure( inParams );
    Integer returnCode = (Integer)outParams.get( "p_out_return_code" );
    if ( returnCode != 0 )
    {
      log.error( "Strored Procedure Returned Error: Error Code Returned = " + returnCode );
      throw new ServiceErrorException( "Pkg_ssi_contest_data.ssi_contest_claims_sort" );
    }
    Map<String, Object> ssiContestClaims = new HashMap<String, Object>();
    ssiContestClaims.put( "claimList", outParams.get( "p_out_ref_cursor" ) );
    ssiContestClaims.put( "claimsCount", outParams.get( "p_out_claims_count" ) );
    ssiContestClaims.put( "claimsSubmittedCount", outParams.get( "p_out_claims_submitted_count" ) );
    ssiContestClaims.put( "claimsPendingCount", outParams.get( "p_out_claims_pending_count" ) );
    ssiContestClaims.put( "claimsApprovedCount", outParams.get( "p_out_claims_approved_count" ) );
    ssiContestClaims.put( "claimsDeniedCount", outParams.get( "p_out_claims_denied_count" ) );
    return ssiContestClaims;
  }

  @Override
  public List<SSIContestPaxClaim> getPaxClaimsByApproverId( Long approverId )
  {
    Criteria criteria = getSession().createCriteria( SSIContestPaxClaim.class );
    criteria.add( Restrictions.eq( "auditCreateInfo.createdBy", approverId ) );
    return criteria.list();
  }

  @Override
  public SSIContestPaxClaimCountValueBean getPaxClaimsCountByApproverId( final Long contestId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.contest.getPaxClaimsCountByApproverId" );
    query.setParameter( "contestId", contestId );
    query.setResultTransformer( new SSIContestPaxClaimValueBeanResultTransformer() );
    return (SSIContestPaxClaimCountValueBean)query.uniqueResult();
  }

  @SuppressWarnings( "serial" )
  private class SSIContestPaxClaimValueBeanResultTransformer extends BaseResultTransformer
  {
    @Override
    public Object transformTuple( Object[] tuple, String[] aliases )
    {
      SSIContestPaxClaimCountValueBean bean = new SSIContestPaxClaimCountValueBean();
      bean.setClaimsSubmittedCount( extractIntFromObject( tuple[0] ) );
      bean.setClaimsApprovedCount( extractIntFromObject( tuple[1] ) );
      bean.setClaimsWaitingForApprovalCount( extractIntFromObject( tuple[2] ) );
      bean.setClaimsDeniedCount( extractIntFromObject( tuple[3] ) );
      return bean;
    }
  }

  @Override
  public SSIContestPaxClaim getPaxClaimByClaimNumber( String claimNumber )
  {
    Criteria criteria = getSession().createCriteria( SSIContestPaxClaim.class );
    criteria.add( Restrictions.eq( "claimNumber", claimNumber ) );
    return (SSIContestPaxClaim)criteria.uniqueResult();
  }

  @Override
  public List<SSIContestPaxClaim> getPaxClaimsByContestIdAndStatus( Long contestId, List<SSIClaimStatus> claimStatuses )
  {
    Criteria criteria = getSession().createCriteria( SSIContestPaxClaim.class );
    criteria.add( Restrictions.eq( "contestId", contestId ) );
    criteria.add( Restrictions.in( "status", claimStatuses ) );
    return criteria.list();
  }

  public List<SSIContestPaxClaim> getPaxClaimsByContestIdAndApproveDenyDate( Long contestId, Date approveDenyDate )
  {
    SimpleDateFormat sdf = new SimpleDateFormat( "MM/dd/yyyy HH:mm:ss" );
    String date = sdf.format( approveDenyDate );

    Query query = getSession().getNamedQuery( "com.biperf.core.domain.contest.getPaxClaimsByContestIdAndApproveDenyDate" );
    query.setParameter( "contestId", contestId );
    query.setParameter( "approveDenyDate", date );
    return query.list();
  }

  public List<SSIContestListValueBean> getPaxClaimsWaitingForApprovalByApproverId( Long approverId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.contest.getPaxClaimsWaitingForApproverByApproverId" );
    query.setParameter( "createdBy", approverId );
    query.setResultTransformer( new SSIContestListValueBeannResultTransformer() );
    return query.list();
  }

  public List<SSIContestListValueBean> getPaxClaimsViewAllByApproverId( Long approverId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.contest.getPaxClaimsViewAllByApproverId" );
    query.setParameter( "createdBy", approverId );
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
      bean.setClaimSubmissionLastDate( (Date)tuple[5] );
      bean.setContestType( extractString( tuple[6] ) );
      return bean;
    }
  }

  @Override
  public int updateContestClaims( Long contestId ) throws SQLException
  {
    CallPrcSSIContestClaimsUpdate procedure = new CallPrcSSIContestClaimsUpdate( dataSource );
    Map<String, Object> outParams = procedure.executeProcedure( contestId );
    Integer returnCode = (Integer)outParams.get( "p_out_return_code" );
    if ( returnCode != 0 )
    {
      String returnErrorMessage = (String)outParams.get( "p_out_error_message" );
      throw new SQLException( "Stored procedure returned error. Procedure returned: " + returnCode + "\n" + returnErrorMessage );
    }
    return (Integer)outParams.get( "p_out_count_claims" );
  }

  @Override
  public List<SSIContestPaxClaim> getPaxClaimsForApprovalByContestId( List<Long> paxcontestIds )
  {
    Criteria criteria = getSession().createCriteria( SSIContestPaxClaim.class );
    criteria.add( Restrictions.in( "contestId", paxcontestIds ) );
    criteria.add( Restrictions.eq( "status", SSIContestStatus.lookup( SSIContestStatus.WAITING_FOR_APPROVAL ) ) );
    return criteria.list();

  }
}
