
package com.biperf.core.ui.engagement;

import java.util.List;

/**
 * 
 * EngagementTeamModelView.
 * 
 * @author kandhi
 * @since Jun 12, 2014
 * @version 1.0
 */
public class EngagementTeamModelView
{
  private List<EngagementTeamModelDataView> data;

  public EngagementTeamModelView( List<EngagementTeamModelDataView> data )
  {
    super();
    this.data = data;
  }

  public List<EngagementTeamModelDataView> getData()
  {
    return data;
  }

  public void setData( List<EngagementTeamModelDataView> data )
  {
    this.data = data;
  }

}
