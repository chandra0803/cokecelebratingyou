/*
 * (c) 2007 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/domain/activity/GoalQuestPayoutActivity.java,v $
 */

package com.biperf.core.domain.activity;

import java.util.Date;

import com.biperf.core.utils.GuidUtils;

/**
 * GoalQuestActivity.
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
 * <td>Satish</td>
 * <td>Jan 11, 2007</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 */
public class GoalQuestPayoutActivity extends Activity
{
  private Long amountAchieved;
  private Date giftCodeIssueDate;

  // ---------------------------------------------------------------------------
  // Constructors
  // ---------------------------------------------------------------------------
  public GoalQuestPayoutActivity()
  {
    // empty constructor
  }

  public GoalQuestPayoutActivity( String guid )
  {
    super( guid );
  }

  /**
   * Construct an activity copying properties of the input sourceActivity, creating a transient
   * SalesActivity with a new guid.
   * 
   * @param sourceActivity
   */
  public GoalQuestPayoutActivity( GoalQuestPayoutActivity sourceActivity )
  {
    super( GuidUtils.generateGuid() );
    participant = sourceActivity.getParticipant();
    promotion = sourceActivity.getPromotion();
  }

  public Long getAmountAchieved()
  {
    return amountAchieved;
  }

  public void setAmountAchieved( Long amountAchieved )
  {
    this.amountAchieved = amountAchieved;
  }

  public Date getGiftCodeIssueDate()
  {
    return giftCodeIssueDate;
  }

  public void setGiftCodeIssueDate( Date giftCodeIssueDate )
  {
    this.giftCodeIssueDate = giftCodeIssueDate;
  }

}
