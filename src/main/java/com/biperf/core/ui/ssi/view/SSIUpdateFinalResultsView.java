
package com.biperf.core.ui.ssi.view;

/**
 * SSIUpdateFinalResultsView.
 * 
 * @author dudam
 * @since Apr 16, 2015
 * @version 1.0
 */
public class SSIUpdateFinalResultsView
{
  private String id;
  private boolean hasApprovePayout;
  private boolean allowFinalizeResults;
  private int daysToEnd;

  public SSIUpdateFinalResultsView()
  {

  }

  public String getId()
  {
    return id;
  }

  public void setId( String id )
  {
    this.id = id;
  }

  public boolean isHasApprovePayout()
  {
    return hasApprovePayout;
  }

  public void setHasApprovePayout( boolean hasApprovePayout )
  {
    this.hasApprovePayout = hasApprovePayout;
  }

  public boolean isAllowFinalizeResults()
  {
    return allowFinalizeResults;
  }

  public void setAllowFinalizeResults( boolean allowFinalizeResults )
  {
    this.allowFinalizeResults = allowFinalizeResults;
  }

  public int getDaysToEnd()
  {
    return daysToEnd;
  }

  public void setDaysToEnd( int daysToEnd )
  {
    this.daysToEnd = daysToEnd;
  }

}
