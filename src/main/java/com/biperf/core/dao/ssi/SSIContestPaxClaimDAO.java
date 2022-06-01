
package com.biperf.core.dao.ssi;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.biperf.core.dao.DAO;
import com.biperf.core.domain.enums.SSIClaimStatus;
import com.biperf.core.domain.ssi.SSIContestPaxClaim;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.value.ssi.SSIContestListValueBean;
import com.biperf.core.value.ssi.SSIContestPaginationValueBean;
import com.biperf.core.value.ssi.SSIContestPaxClaimCountValueBean;

/**
 * 
 * SSIContestPaxClaimDAO.
 * 
 * @author kancherl
 * @since May 20, 2015
 * @version 1.0
 */

public interface SSIContestPaxClaimDAO extends DAO
{
  /** ApplicationContext beanName */
  public static final String BEAN_NAME = "ssiContestPaxClaimDAO";

  public SSIContestPaxClaim getPaxClaimById( Long paxClaimId );

  public SSIContestPaxClaim savePaxClaim( SSIContestPaxClaim paxClaim ) throws SQLException;

  public void approvePaxClaim( String status, Long approverId, Long paxClaimId ) throws SQLException;

  public Date approveAllPaxClaims( Long contestId, Long approverId ) throws SQLException;

  public void denyPaxClaim( String status, String deniedReason, Long approverId, Long paxClaimId ) throws SQLException;

  public List<SSIContestPaxClaim> getPaxClaimsByContestId( Long contestId );

  public List<SSIContestPaxClaim> getPaxClaimsBySubmitterId( Long contestId, Long submitterId, SSIContestPaginationValueBean paginationParams );

  public int getPaxClaimsCountBySubmitterId( Long contestId, Long submitterId );

  public Double getClaimsActivityAmount( Long contestId, Long submitterId );

  public Map<String, Object> getPaxClaimsByContestIdAndStatus( Long contestId, String claimStatuses, int pageNumber, int recordsPerpage, String sortedOn, String sortedBy )
      throws ServiceErrorException;

  public SSIContestPaxClaimCountValueBean getPaxClaimsCountByApproverId( Long contestId );

  public SSIContestPaxClaim getPaxClaimByClaimNumber( String claimNumber );

  public List<SSIContestPaxClaim> getPaxClaimsByContestIdAndStatus( Long contestId, List<SSIClaimStatus> claimStatuses );

  public List<SSIContestPaxClaim> getPaxClaimsByContestIdAndApproveDenyDate( Long contestId, Date approveDenyDate );

  public List<SSIContestPaxClaim> getPaxClaimsByApproverId( Long approverId );

  public List<SSIContestListValueBean> getPaxClaimsWaitingForApprovalByApproverId( Long approverId );

  public List<SSIContestListValueBean> getPaxClaimsViewAllByApproverId( Long approverId );

  public int updateContestClaims( Long contestId ) throws SQLException;

  public List<SSIContestPaxClaim> getPaxClaimsForApprovalByContestId( List<Long> paxcontestIds );

}
