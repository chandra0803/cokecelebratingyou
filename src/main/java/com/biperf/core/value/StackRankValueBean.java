/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.value;

import java.io.Serializable;

/**
 * StackRankValueBean.
 * <p>
 * <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>sedey</td>
 * <td>March 14, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class StackRankValueBean implements Serializable
{
  String nodeTypeName = "";
  String nodeTypeId;
  String totalNodes = "";
  String totalNodesWithRankings = "";

  public String getNodeTypeName()
  {
    return nodeTypeName;
  }

  public void setNodeTypeName( String nodeTypeName )
  {
    this.nodeTypeName = nodeTypeName;
  }

  public String getTotalNodes()
  {
    return totalNodes;
  }

  public void setTotalNodes( String totalNodes )
  {
    this.totalNodes = totalNodes;
  }

  public String getTotalNodesWithRankings()
  {
    return totalNodesWithRankings;
  }

  public void setTotalNodesWithRankings( String totalNodesWithRankings )
  {
    this.totalNodesWithRankings = totalNodesWithRankings;
  }

  public String getNodeTypeId()
  {
    return nodeTypeId;
  }

  public void setNodeTypeId( String nodeTypeId )
  {
    this.nodeTypeId = nodeTypeId;
  }

}
