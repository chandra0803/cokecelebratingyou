
package com.biperf.core.ui.engagement;

/**
 * 
 * EngagementTeamModelDataView.
 * 
 * @author kandhi
 * @since Jun 12, 2014
 * @version 1.0
 */
public class EngagementTeamModelDataView
{
  private String type;
  private Integer target;
  private Integer actual;

  public EngagementTeamModelDataView( Integer target, Integer actual, String type )
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
