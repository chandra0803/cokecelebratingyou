
package com.biperf.core.ui.ssi.view;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.biperf.core.domain.WebErrorMessage;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.SSIContestUtil;
import com.biperf.core.value.ssi.SSIContestPaxClaimValueBean;

/**
 * 
 * SSIContestApproveClaimsSummaryView.
 * 
 * @author patelP
 * @since May 25, 2015
 * @version 1.0R
 */

public class SSIContestApproveClaimsSummaryView
{
  private List<WebErrorMessage> messages = new ArrayList<WebErrorMessage>();
  private int currentPage;
  private int recordsPerPage;
  private int recordsTotal;
  private int claimsSubmittedCount;
  private int claimsPendingCount;
  private int claimsApprovedCount;
  private int claimsDeniedCount;
  private List<SSIContestApproveClaimsRecordView> claims = new ArrayList<SSIContestApproveClaimsRecordView>();

  public SSIContestApproveClaimsSummaryView( List<SSIContestPaxClaimValueBean> ssiContestPaxClaimValueBeanList, int currentPage, int totalRecords, int recordsPerPage, HttpServletRequest request )
  {
    for ( SSIContestPaxClaimValueBean ssiContestPaxClaimValueBean : ssiContestPaxClaimValueBeanList )
    {
      SSIContestApproveClaimsRecordView ssiContestApproveClaimsRecordView = new SSIContestApproveClaimsRecordView();
      ssiContestApproveClaimsRecordView.setClaimNumber( ssiContestPaxClaimValueBean.getClaimNumber() );
      ssiContestApproveClaimsRecordView.setId( SSIContestUtil.createClaimClientState( request, getClientStateParameterMap( ssiContestPaxClaimValueBean ), true ) );
      ssiContestApproveClaimsRecordView.setDateSubmitted( DateUtils.toDisplayString( ssiContestPaxClaimValueBean.getSubmissionDate() ) );
      ssiContestApproveClaimsRecordView.setApprovedBy( ssiContestPaxClaimValueBean.getApprovedBy() );
      ssiContestApproveClaimsRecordView.setSubmittedBy( ssiContestPaxClaimValueBean.getSubmittedBy() );
      ssiContestApproveClaimsRecordView.setStatus( ssiContestPaxClaimValueBean.getStatus() );
      ssiContestApproveClaimsRecordView.setStatusDescription( ssiContestPaxClaimValueBean.getStatusDescription() );
      ssiContestApproveClaimsRecordView
          .setActivities( ssiContestPaxClaimValueBean.getActivityDescription() != null ? Arrays.asList( ssiContestPaxClaimValueBean.getActivityDescription().split( "\\s*,\\s*" ) ) : null );
      claims.add( ssiContestApproveClaimsRecordView );
    }
    this.currentPage = currentPage;
    this.recordsTotal = totalRecords;
    this.recordsPerPage = recordsPerPage;
  }

  public SSIContestApproveClaimsSummaryView( WebErrorMessage message )
  {
    this.messages.add( message );
  }

  private Map<String, Object> getClientStateParameterMap( SSIContestPaxClaimValueBean ssiContestPaxClaimValueBean )
  {
    Map<String, Object> clientStateParameterMap = new HashMap<String, Object>();
    clientStateParameterMap.put( SSIContestUtil.CONTEST_ID, ssiContestPaxClaimValueBean.getContestId() );
    clientStateParameterMap.put( SSIContestUtil.CLAIM_ID, ssiContestPaxClaimValueBean.getId() );
    clientStateParameterMap.put( SSIContestUtil.CLAIM_NUMBER, ssiContestPaxClaimValueBean.getClaimNumber() );
    return clientStateParameterMap;
  }

  class SSIContestApproveClaimsRecordView
  {
    private String id;
    private String claimNumber;
    private String dateSubmitted;
    private String submittedBy;
    private String approvedBy;
    private String statusDescription;
    private String status;
    private List<String> activities = new ArrayList<String>();

    public List<String> getActivities()
    {
      return activities;
    }

    public void setActivities( List<String> activities )
    {
      this.activities = activities;
    }

    public String getId()
    {
      return id;
    }

    public void setId( String id )
    {
      this.id = id;
    }

    public String getClaimNumber()
    {
      return claimNumber;
    }

    public void setClaimNumber( String claimNumber )
    {
      this.claimNumber = claimNumber;
    }

    public String getDateSubmitted()
    {
      return dateSubmitted;
    }

    public void setDateSubmitted( String dateSubmitted )
    {
      this.dateSubmitted = dateSubmitted;
    }

    public String getSubmittedBy()
    {
      return submittedBy;
    }

    public void setSubmittedBy( String submittedBy )
    {
      this.submittedBy = submittedBy;
    }

    public String getApprovedBy()
    {
      return approvedBy;
    }

    public void setApprovedBy( String approvedBy )
    {
      this.approvedBy = approvedBy;
    }

    public String getStatusDescription()
    {
      return statusDescription;
    }

    public void setStatusDescription( String statusDescription )
    {
      this.statusDescription = statusDescription;
    }

    public String getStatus()
    {
      return status;
    }

    public void setStatus( String status )
    {
      this.status = status;
    }

  }

  public List<WebErrorMessage> getMessages()
  {
    return messages;
  }

  public void setMessages( List<WebErrorMessage> messages )
  {
    this.messages = messages;
  }

  public int getCurrentPage()
  {
    return currentPage;
  }

  public void setCurrentPage( int currentPage )
  {
    this.currentPage = currentPage;
  }

  public int getRecordsPerPage()
  {
    return recordsPerPage;
  }

  public void setRecordsPerPage( int recordsPerPage )
  {
    this.recordsPerPage = recordsPerPage;
  }

  public List<SSIContestApproveClaimsRecordView> getClaims()
  {
    return claims;
  }

  public void setClaims( List<SSIContestApproveClaimsRecordView> claims )
  {
    this.claims = claims;
  }

  public int getRecordsTotal()
  {
    return recordsTotal;
  }

  public void setRecordsTotal( int recordsTotal )
  {
    this.recordsTotal = recordsTotal;
  }

  private ParticipantService getParticipantService()
  {
    return (ParticipantService)BeanLocator.getBean( ParticipantService.BEAN_NAME );
  }

  public int getClaimsSubmittedCount()
  {
    return claimsSubmittedCount;
  }

  public void setClaimsSubmittedCount( int claimsSubmittedCount )
  {
    this.claimsSubmittedCount = claimsSubmittedCount;
  }

  public int getClaimsPendingCount()
  {
    return claimsPendingCount;
  }

  public void setClaimsPendingCount( int claimsPendingCount )
  {
    this.claimsPendingCount = claimsPendingCount;
  }

  public int getClaimsApprovedCount()
  {
    return claimsApprovedCount;
  }

  public void setClaimsApprovedCount( int claimsApprovedCount )
  {
    this.claimsApprovedCount = claimsApprovedCount;
  }

  public int getClaimsDeniedCount()
  {
    return claimsDeniedCount;
  }

  public void setClaimsDeniedCount( int claimsDeniedCount )
  {
    this.claimsDeniedCount = claimsDeniedCount;
  }

}
