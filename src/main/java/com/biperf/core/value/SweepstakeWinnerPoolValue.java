
package com.biperf.core.value;

import java.io.Serializable;

/**
 * SweepstakeWinnerPoolValue.
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
 * <td></td>
 * <td></td>
 * <td></td>
 * <td>created</td>
 * </tr>
 * </table>
 */

public class SweepstakeWinnerPoolValue implements Serializable
{

  private Long activityId;
  private Long userId;
  private boolean isSubmitter;

  public Long getActivityId()
  {
    return activityId;
  }

  public void setActivityId( Long activityId )
  {
    this.activityId = activityId;
  }

  public Long getUserId()
  {
    return userId;
  }

  public void setUserId( Long userId )
  {
    this.userId = userId;
  }

  public boolean isSubmitter()
  {
    return isSubmitter;
  }

  public void setSubmitter( boolean isSubmitter )
  {
    this.isSubmitter = isSubmitter;
  }

}
