/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javaui/com/biperf/core/ui/node/NodeParticipantReassignForm.java,v $
 */

package com.biperf.core.ui.node;

import com.biperf.core.ui.BaseForm;

/**
 * NodeParticipantReassignForm.
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
 * <td>zahler</td>
 * <td>May 16, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class NodeParticipantReassignForm extends BaseForm
{
  private String method;
  private String oldNodeId;
  private String newNodeId;
  private String newNodeName;

  public String getNewNodeId()
  {
    return newNodeId;
  }

  public void setNewNodeId( String newNodeId )
  {
    this.newNodeId = newNodeId;
  }

  public String getNewNodeName()
  {
    return newNodeName;
  }

  public void setNewNodeName( String newNodeName )
  {
    this.newNodeName = newNodeName;
  }

  public String getOldNodeId()
  {
    return oldNodeId;
  }

  public void setOldNodeId( String oldNodeId )
  {
    this.oldNodeId = oldNodeId;
  }

  public String getMethod()
  {
    return method;
  }

  public void setMethod( String method )
  {
    this.method = method;
  }

}
