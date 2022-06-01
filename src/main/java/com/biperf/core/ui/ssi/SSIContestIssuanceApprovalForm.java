
package com.biperf.core.ui.ssi;

import com.biperf.core.ui.BaseActionForm;

/**
 * 
 * SSIContestIssuanceApprovalForm.
 * 
 * @author patelp
 * @since Feb 24, 2014
 * @version 1.0
 */
public class SSIContestIssuanceApprovalForm extends BaseActionForm
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

  public String getClientState()
  {
    return clientState;
  }

  public void setClientState( String clientState )
  {
    this.clientState = clientState;
  }

  public int getPage()
  {
    return page;
  }

  public void setPage( int page )
  {
    this.page = page;
  }

  public String getContestId()
  {
    return contestId;
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

  public void setContestId( String contestId )
  {
    this.contestId = contestId;
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

  public String getMethod()
  {
    return method;
  }

  public void setMethod( String method )
  {
    this.method = method;
  }

}
