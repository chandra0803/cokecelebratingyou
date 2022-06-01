/*
 * (c) 2006 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javaui/com/biperf/core/ui/budget/NodeTypeFormBean.java,v $
 */

package com.biperf.core.ui.budget;

import com.biperf.core.domain.hierarchy.NodeType;
import com.biperf.core.ui.BaseFormBean;

/**
 * NodeTypeFormBean.
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
 * <td>Aug 16, 2006</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class NodeTypeFormBean extends BaseFormBean
{
  private NodeType nodeType;
  private Long nodeTypeId;
  /* This node type should be returned in report if true */
  private boolean isSelected;
  /* All budgets if true - only budgets with characteristics if false */
  private boolean allBudgets;

  public NodeTypeFormBean()
  {
    super();
    setAllBudgets( true );

  }

  public boolean isSelected()
  {
    return isSelected;
  }

  public void setSelected( boolean isSelected )
  {
    this.isSelected = isSelected;
  }

  public Long getNodeTypeId()
  {
    return nodeTypeId;
  }

  public void setNodeTypeId( Long nodeTypeId )
  {
    this.nodeTypeId = nodeTypeId;
  }

  public boolean isAllBudgets()
  {
    return allBudgets;
  }

  public void setAllBudgets( boolean allBudgets )
  {
    this.allBudgets = allBudgets;
  }

  public NodeType getNodeType()
  {
    return nodeType;
  }

  public void setNodeType( NodeType nodeType )
  {
    this.nodeType = nodeType;
  }

}
