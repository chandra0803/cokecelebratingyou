/*
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/hierarchy/NodeSearchCriteria.java,v $
 * (c) 2005 BI, Inc.  All rights reserved.
 */

package com.biperf.core.service.hierarchy;

import java.io.Serializable;

/**
 * NodeSearchCriteria.
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
 * <td>meadows</td>
 * <td>Oct 26, 2007</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class NodeSearchCriteria implements Serializable
{
  private String nodeName;
  private Long nodeId;
  private boolean nodeIdAndBelow = false;
  private Long budgetSegmentId;

  public String getNodeName()
  {
    return nodeName;
  }

  public void setNodeName( String nodeName )
  {
    this.nodeName = nodeName;
  }

  public Long getNodeId()
  {
    return nodeId;
  }

  public void setNodeId( Long nodeId )
  {
    this.nodeId = nodeId;
  }

  public boolean isNodeIdAndBelow()
  {
    return nodeIdAndBelow;
  }

  public void setNodeIdAndBelow( boolean nodeIdAndBelow )
  {
    this.nodeIdAndBelow = nodeIdAndBelow;
  }

  public Long getBudgetSegmentId()
  {
    return budgetSegmentId;
  }

  public void setBudgetSegmentId( Long budgetSegmentId )
  {
    this.budgetSegmentId = budgetSegmentId;
  }
}
