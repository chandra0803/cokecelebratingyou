
package com.biperf.core.ui.ssi;

import com.biperf.core.ui.BaseActionForm;

/**
 * 
 * SSIContestSummaryAwardThemNowForm.
 * 
 * @author kandhi
 * @since Feb 11, 2015
 * @version 1.0
 */
public class SSIContestSummaryAwardThemNowForm extends BaseActionForm
{
  private static final long serialVersionUID = 1L;

  private String method;
  private String contestId;
  private String sortedOn;
  private String sortedBy;
  private int page;

  private String initializationJson;

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

}
