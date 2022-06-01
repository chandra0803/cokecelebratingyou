
package com.biperf.core.ui.engagement;

import java.util.List;

/**
 * 
 * EngagementTeamTeamsMembersView.
 * 
 * @author kandhi
 * @since May 29, 2014
 * @version 1.0
 */
public class EngagementTeamTeamsMembersView
{
  private Long nodeId;
  private String nodeName;
  private String nodeOwnerName;
  private List<EngagementTeamTeamsMembersDataView> data;

  public EngagementTeamTeamsMembersView( Long nodeId, String nodeName, String nodeOwnerName, List<EngagementTeamTeamsMembersDataView> data )
  {
    super();
    this.nodeId = nodeId;
    this.nodeName = nodeName;
    this.nodeOwnerName = nodeOwnerName;
    this.data = data;
  }

  public Long getNodeId()
  {
    return nodeId;
  }

  public void setNodeId( Long nodeId )
  {
    this.nodeId = nodeId;
  }

  public String getNodeName()
  {
    return nodeName;
  }

  public void setNodeName( String nodeName )
  {
    this.nodeName = nodeName;
  }

  public String getNodeOwnerName()
  {
    return nodeOwnerName;
  }

  public void setNodeOwnerName( String nodeOwnerName )
  {
    this.nodeOwnerName = nodeOwnerName;
  }

  public List<EngagementTeamTeamsMembersDataView> getData()
  {
    return data;
  }

  public void setData( List<EngagementTeamTeamsMembersDataView> data )
  {
    this.data = data;
  }

}
