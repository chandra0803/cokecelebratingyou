
package com.biperf.core.value;

import java.io.Serializable;

/**
 * PurlAwardValue.
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
 * <td>bala</td>
 * <td>Feb 23, 2011</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 */

public class PurlAwardValue implements Serializable
{

  private boolean isPurlEmailSent;
  private boolean isStandardEmailSent;
  private int totalPointsDeposited;
  private boolean numberOfLevels;

  public boolean isNumberOfLevels()
  {
    return numberOfLevels;
  }

  public void setNumberOfLevels( boolean numberOfLevels )
  {
    this.numberOfLevels = numberOfLevels;
  }

  public boolean isPurlEmailSent()
  {
    return isPurlEmailSent;
  }

  public void setPurlEmailSent( boolean isPurlEmailSent )
  {
    this.isPurlEmailSent = isPurlEmailSent;
  }

  public boolean isStandardEmailSent()
  {
    return isStandardEmailSent;
  }

  public void setStandardEmailSent( boolean isStandardEmailSent )
  {
    this.isStandardEmailSent = isStandardEmailSent;
  }

  public int getTotalPointsDeposited()
  {
    return totalPointsDeposited;
  }

  public void setTotalPointsDeposited( int totalPointsDeposited )
  {
    this.totalPointsDeposited = totalPointsDeposited;
  }

}
