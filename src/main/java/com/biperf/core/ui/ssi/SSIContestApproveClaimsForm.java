
package com.biperf.core.ui.ssi;

import com.biperf.core.ui.BaseActionForm;

/**
 * SSIContestClaimSubmissionForm.
 * 
 * @author patel
 * @since May 22, 2015
 * @version 1.0
 */

public class SSIContestApproveClaimsForm extends BaseActionForm
{
  private static final long serialVersionUID = 1L;

  private String method;
  private String contestId;
  private String clientState;
  private String sortedOn;
  private String sortedBy;
  private int page;
  private String initializationJson;
  private String comment;
  private String filter;
  private String claimId;

  public String getClaimId()
  {
    return claimId;
  }

  public void setClaimId( String claimId )
  {
    this.claimId = claimId;
  }

  public String getMethod()
  {
    return method;
  }

  public void setMethod( String method )
  {
    this.method = method;
  }

  public String getContestId()
  {
    return contestId;
  }

  public void setContestId( String contestId )
  {
    this.contestId = contestId;
  }

  public String getClientState()
  {
    return clientState;
  }

  public void setClientState( String clientState )
  {
    this.clientState = clientState;
  }

  public String getSortedOn()
  {
    return sortedOn;
  }

  public void setSortedOn( String sortedOn )
  {
    this.sortedOn = sortedOn;
  }

  public String getSortedBy()
  {
    return sortedBy;
  }

  public void setSortedBy( String sortedBy )
  {
    this.sortedBy = sortedBy;
  }

  public int getPage()
  {
    return page;
  }

  public void setPage( int page )
  {
    this.page = page;
  }

  public String getInitializationJson()
  {
    return initializationJson;
  }

  public void setInitializationJson( String initializationJson )
  {
    this.initializationJson = initializationJson;
  }

  public String getComment()
  {
    return comment;
  }

  public void setComment( String comment )
  {
    this.comment = comment;
  }

  public static long getSerialversionuid()
  {
    return serialVersionUID;
  }

  public String getFilter()
  {
    return filter;
  }

  public void setFilter( String filter )
  {
    this.filter = filter;
  }
}
