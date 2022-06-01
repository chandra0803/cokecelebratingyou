/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.domain.promotion;

import com.biperf.core.domain.BaseDomain;

/**
 * Promotion.
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
 * <td>March 01, 2006</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class StackRankPayout extends BaseDomain implements Cloneable
{

  private int startRank;
  private int endRank;
  private int payout;

  private StackRankPayoutGroup stackRankPayoutGroup;

  public StackRankPayout()
  {
    super();
  }

  public StackRankPayout( StackRankPayout copy )
  {
    this.stackRankPayoutGroup = copy.getStackRankPayoutGroup();
    this.startRank = copy.getStartRank();
    this.endRank = copy.getEndRank();
    this.payout = copy.getPayout();
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.domain.BaseDomain#equals(java.lang.Object)
   * @param object
   * @return boolean
   */
  public boolean equals( Object object )
  {
    boolean equals = true;

    if ( ! ( object instanceof StackRankPayout ) )
    {
      return false;
    }

    StackRankPayout otherStackRankPayout = (StackRankPayout)object;

    if ( getStackRankPayoutGroup() != null ? !getStackRankPayoutGroup().equals( otherStackRankPayout.getStackRankPayoutGroup() ) : otherStackRankPayout.getStackRankPayoutGroup() != null )
    {
      return false;
    }
    if ( getStartRank() != 0 ? getStartRank() != otherStackRankPayout.getStartRank() : otherStackRankPayout.getStartRank() != 0 )
    {
      return false;
    }

    return equals;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.domain.BaseDomain#hashCode()
   * @return int
   */
  public int hashCode()
  {
    if ( getStackRankPayoutGroup() != null )
    {
      return 29 * getStackRankPayoutGroup().hashCode() + getStartRank();
    }

    return 0;
  }

  /**
   * Clones this, removes the auditInfo and id. Overridden from
   * 
   * @see java.lang.Object#clone()
   * @return Object
   * @throws CloneNotSupportedException
   */
  public Object clone() throws CloneNotSupportedException
  {

    StackRankPayout promotionPayout = (StackRankPayout)super.clone();
    promotionPayout.resetBaseDomain();

    return promotionPayout;

  }

  /**
   * Get StackRankPayoutGroup
   * 
   * @return StackRankPayoutGroup
   */

  public StackRankPayoutGroup getStackRankPayoutGroup()
  {
    return this.stackRankPayoutGroup;
  }

  /**
   * Set StackRankPayoutGroup
   * 
   * @param stackRankPayoutGroup
   */
  public void setStackRankPayoutGroup( StackRankPayoutGroup stackRankPayoutGroup )
  {
    this.stackRankPayoutGroup = stackRankPayoutGroup;
  }

  public int getEndRank()
  {
    return endRank;
  }

  public void setEndRank( int endRank )
  {
    this.endRank = endRank;
  }

  public int getPayout()
  {
    return payout;
  }

  public void setPayout( int payout )
  {
    this.payout = payout;
  }

  public int getStartRank()
  {
    return startRank;
  }

  public void setStartRank( int startRank )
  {
    this.startRank = startRank;
  }

}
