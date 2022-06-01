
package com.biperf.core.ui.ssi;

import com.biperf.core.ui.BaseActionForm;

/**
 * SSIContestClaimDetailForm.
 * 
 * @author dudam
 * @since Jun 3, 2015
 * @version 1.0
 */
public class SSIContestClaimDetailForm extends BaseActionForm
{
  private static final long serialVersionUID = 1L;

  private String method;
  private Long contestId;
  private String claimNumber;
  private String sortedOn;
  private String sortedBy;
  private int page;
  private String initializationJson;
  private String backButtonUrl;
  private String comment;

  public String getMethod()
  {
    return method;
  }

  public void setMethod( String method )
  {
    this.method = method;
  }

  public Long getContestId()
  {
    return contestId;
  }

  public void setContestId( Long contestId )
  {
    this.contestId = contestId;
  }

  public String getClaimNumber()
  {
    return claimNumber;
  }

  public void setClaimNumber( String claimNumber )
  {
    this.claimNumber = claimNumber;
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

  public String getBackButtonUrl()
  {
    return backButtonUrl;
  }

  public void setBackButtonUrl( String backButtonUrl )
  {
    this.backButtonUrl = backButtonUrl;
  }

  public static long getSerialversionuid()
  {
    return serialVersionUID;
  }

  public String getComment()
  {
    return comment;
  }

  public void setComment( String comment )
  {
    this.comment = comment;
  }
}
