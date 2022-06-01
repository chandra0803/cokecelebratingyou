
package com.biperf.core.ui.ssi;

import com.biperf.core.ui.BaseActionForm;

/**
 * 
 * SSIContestPayoutHistoryAwardThemNowForm.
 * 
 * @author patelp
 * @since Feb 17, 2015
 * @version 1.0
 */
public class SSIContestPayoutHistoryAwardThemNowForm extends BaseActionForm
{
  private static final long serialVersionUID = 1L;
  private String id;
  private String method;
  private String contestId;
  private String sortedOn;
  private String sortedBy;
  private String name;
  private String status;
  private String totalActivity;
  private String totalPayoutAmount;
  private int currentPage;
  private String initializationJson;
  private String backButtonUrl;

  public String getId()
  {
    return id;
  }

  public void setId( String id )
  {
    this.id = id;
  }

  public String getName()
  {
    return name;
  }

  public void setName( String name )
  {
    this.name = name;
  }

  public String getStatus()
  {
    return status;
  }

  public void setStatus( String status )
  {
    this.status = status;
  }

  public String getTotalActivity()
  {
    return totalActivity;
  }

  public void setTotalActivity( String totalActivity )
  {
    this.totalActivity = totalActivity;
  }

  public String getTotalPayoutAmount()
  {
    return totalPayoutAmount;
  }

  public void setTotalPayoutAmount( String totalPayoutAmount )
  {
    this.totalPayoutAmount = totalPayoutAmount;
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

  public int getCurrentPage()
  {
    return currentPage;
  }

  public void setCurrentPage( int currentPage )
  {
    this.currentPage = currentPage;
  }

}
