
package com.biperf.core.ui.engagement;

/**
 * 
 * EngagementTeamIndividualsMembersDataView.
 * 
 * @author kandhi
 * @since May 29, 2014
 * @version 1.0
 */
public class EngagementTeamIndividualsMembersDataView
{
  private String type;
  private int target;
  private int actual;

  public EngagementTeamIndividualsMembersDataView( int target, int actual, String type )
  {
    super();
    this.type = type;
    this.target = target;
    this.actual = actual;
  }

  public String getType()
  {
    return type;
  }

  public void setType( String type )
  {
    this.type = type;
  }

  public int getTarget()
  {
    return target;
  }

  public void setTarget( int target )
  {
    this.target = target;
  }

  public int getActual()
  {
    return actual;
  }

  public void setActual( int actual )
  {
    this.actual = actual;
  }

}
