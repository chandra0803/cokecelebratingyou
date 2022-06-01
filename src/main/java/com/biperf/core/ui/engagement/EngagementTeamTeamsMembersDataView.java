
package com.biperf.core.ui.engagement;

/**
 * 
 * EngagementTeamTeamsMembersDataView.
 * 
 * @author kandhi
 * @since May 29, 2014
 * @version 1.0
 */
public class EngagementTeamTeamsMembersDataView
{
  private Integer target;
  private Integer actual;
  private String type;

  public EngagementTeamTeamsMembersDataView( Integer target, Integer actual, String type )
  {
    super();
    this.target = target;
    this.actual = actual;
    this.type = type;
  }

  public String getType()
  {
    return type;
  }

  public void setType( String type )
  {
    this.type = type;
  }

  public Integer getTarget()
  {
    return target;
  }

  public void setTarget( Integer target )
  {
    this.target = target;
  }

  public Integer getActual()
  {
    return actual;
  }

  public void setActual( Integer actual )
  {
    this.actual = actual;
  }

}
