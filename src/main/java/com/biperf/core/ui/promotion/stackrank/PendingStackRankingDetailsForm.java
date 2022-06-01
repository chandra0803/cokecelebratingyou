/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.ui.promotion.stackrank;

import com.biperf.core.ui.BaseActionForm;

/**
 * PendingStackRankingDetailsForm.
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
 * <td>gaddam</td>
 * <td>Mar 10, 2006</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class PendingStackRankingDetailsForm extends BaseActionForm
{
  private String method;
  private String nodeId;
  private String nodeType;
  private String nodeTypeId;
  private String nodeName;
  private String promotionName;
  private String rankingPeriodFrom;
  private String rankingPeriodTo;
  private String stackRankId;
  private String promotionId;

  private String preNodeName;

  public String getMethod()
  {
    return method;
  }

  public void setMethod( String method )
  {
    this.method = method;
  }

  public String getNodeId()
  {
    return nodeId;
  }

  public void setNodeId( String nodeId )
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

  public String getNodeType()
  {
    return nodeType;
  }

  public void setNodeType( String nodeType )
  {
    this.nodeType = nodeType;
  }

  public String getPromotionId()
  {
    return promotionId;
  }

  public void setPromotionId( String promotionId )
  {
    this.promotionId = promotionId;
  }

  public String getPromotionName()
  {
    return promotionName;
  }

  public void setPromotionName( String promotionName )
  {
    this.promotionName = promotionName;
  }

  public String getRankingPeriodFrom()
  {
    return rankingPeriodFrom;
  }

  public void setRankingPeriodFrom( String rankingPeriodFrom )
  {
    this.rankingPeriodFrom = rankingPeriodFrom;
  }

  public String getRankingPeriodTo()
  {
    return rankingPeriodTo;
  }

  public void setRankingPeriodTo( String rankingPeriodTo )
  {
    this.rankingPeriodTo = rankingPeriodTo;
  }

  public String getStackRankId()
  {
    return stackRankId;
  }

  public void setStackRankId( String stackRankId )
  {
    this.stackRankId = stackRankId;
  }

  public String getPreNodeName()
  {
    return preNodeName;
  }

  public void setPreNodeName( String preNodeName )
  {
    this.preNodeName = preNodeName;
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
