/*
 * $Source$
 * (c) 2005 BI, Inc.  All rights reserved.
 */

package com.biperf.core.ui.promotion.stackrank;

import com.biperf.core.ui.BaseForm;

/**
 * StackRankPendingListForm.
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
 * <td>March 09, 2006</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class StackRankPendingListForm extends BaseForm
{
  private Long stackRankId;
  private Long promotionId;
  private String promotionName;
  private boolean approved;

  public boolean isApproved()
  {
    return approved;
  }

  public void setApproved( boolean approved )
  {
    this.approved = approved;
  }

  public String getPromotionName()
  {
    return promotionName;
  }

  public void setPromotionName( String promotionName )
  {
    this.promotionName = promotionName;
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
