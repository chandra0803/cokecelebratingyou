
package com.biperf.core.ui.ssi;

import com.biperf.core.ui.BaseActionForm;

/**
 * @author dudam
 * @since Nov 20, 2014
 * @version 1.0
 */
public class SSIContestParticipantForm extends BaseActionForm
{
  private String method;
  private Long contestId;
  private String sortedOn;
  private String sortedBy;
  private int currentPage;
  private Long[] paxIds;

  public String getMethod()
  {
    return method;
  }

  public void setMethod( String method )
  {
    this.method = method;
  }

  public Long[] getPaxIds()
  {
    return paxIds;
  }

  public void setPaxIds( Long[] paxIds )
  {
    this.paxIds = paxIds;
  }

  public Long getContestId()
  {
    return contestId;
  }

  public void setContestId( Long contestId )
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

  public int getCurrentPage()
  {
    return currentPage;
  }

  public void setCurrentPage( int currentPage )
  {
    this.currentPage = currentPage;
  }

}
