/**
 * 
 */

package com.biperf.core.ui.promotion.stackrank;

import java.io.Serializable;

/**
 * StackRankBean.
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
 * <td>Mar 16, 2006</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class StackRankFormBean implements Serializable
{
  private boolean displayCreate;
  private boolean displayApprove;
  private boolean displayHistory;
  private Long promotionId;
  private String promotionName;

  public boolean isDisplayApprove()
  {
    return displayApprove;
  }

  public void setDisplayApprove( boolean displayApprove )
  {
    this.displayApprove = displayApprove;
  }

  public boolean isDisplayCreate()
  {
    return displayCreate;
  }

  public void setDisplayCreate( boolean displayCreate )
  {
    this.displayCreate = displayCreate;
  }

  public boolean isDisplayHistory()
  {
    return displayHistory;
  }

  public void setDisplayHistory( boolean displayHistory )
  {
    this.displayHistory = displayHistory;
  }

  public Long getPromotionId()
  {
    return promotionId;
  }

  public void setPromotionId( Long promotionId )
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

}
