/*
 * (c) 2006 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.ui.promotion.stackrank;

import com.biperf.core.ui.BaseForm;

/**
 * StackRankHistoryForm.
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
 * <td>Mar 14, 2006</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class StackRankHistoryForm extends BaseForm
{
  public static final String FORM_NAME = "stackRankHistoryForm";

  private String method;
  private Long promotionId;
  private Long stackRankId;
  private Long nodeTypeId;
  private Long nodeId;

  public String getMethod()
  {
    return method;
  }

  public void setMethod( String method )
  {
    this.method = method;
  }

  public Long getNodeId()
  {
    return nodeId;
  }

  public void setNodeId( Long nodeId )
  {
    this.nodeId = nodeId;
  }

  public Long getNodeTypeId()
  {
    return nodeTypeId;
  }

  public void setNodeTypeId( Long nodeTypeId )
  {
    this.nodeTypeId = nodeTypeId;
  }

  public Long getPromotionId()
  {
    return promotionId;
  }

  public void setPromotionId( Long promotionId )
  {
    this.promotionId = promotionId;
  }

  public Long getStackRankId()
  {
    return stackRankId;
  }

  public void setStackRankId( Long stackRankId )
  {
    this.stackRankId = stackRankId;
  }

}
