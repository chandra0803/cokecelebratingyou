
package com.biperf.core.domain.promotion;

import java.util.Comparator;

import com.biperf.core.domain.BaseDomain;

@SuppressWarnings( "serial" )
public class StackStandingPayout extends BaseDomain implements Cloneable
{

  private int startStanding;
  private int endStanding;
  private int payout;

  private StackStandingPayoutGroup stackStandingPayoutGroup;

  public StackStandingPayout()
  {
    super();
  }

  public StackStandingPayout( StackStandingPayout copy )
  {
    this.stackStandingPayoutGroup = copy.getStackStandingPayoutGroup();
    this.startStanding = copy.getStartStanding();
    this.endStanding = copy.getEndStanding();
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

    if ( ! ( object instanceof StackStandingPayout ) )
    {
      return false;
    }

    StackStandingPayout otherStackStandingPayout = (StackStandingPayout)object;

    if ( getStackStandingPayoutGroup() != null
        ? !getStackStandingPayoutGroup().equals( otherStackStandingPayout.getStackStandingPayoutGroup() )
        : otherStackStandingPayout.getStackStandingPayoutGroup() != null )
    {
      return false;
    }
    if ( getStartStanding() != 0 ? getStartStanding() != otherStackStandingPayout.getStartStanding() : otherStackStandingPayout.getStartStanding() != 0 )
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
    if ( getStackStandingPayoutGroup() != null )
    {
      return 29 * getStackStandingPayoutGroup().hashCode() + getStartStanding();
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

    StackStandingPayout promotionPayout = (StackStandingPayout)super.clone();
    promotionPayout.resetBaseDomain();

    return promotionPayout;

  }

  /**
   * Get StackStandingPayoutGroup
   * 
   * @return StackStandingPayoutGroup
   */

  public StackStandingPayoutGroup getStackStandingPayoutGroup()
  {
    return this.stackStandingPayoutGroup;
  }

  /**
   * Set StackStandingPayoutGroup
   * 
   * @param stackStandingPayoutGroup
   */
  public void setStackStandingPayoutGroup( StackStandingPayoutGroup stackStandingPayoutGroup )
  {
    this.stackStandingPayoutGroup = stackStandingPayoutGroup;
  }

  public int getEndStanding()
  {
    return endStanding;
  }

  public void setEndStanding( int endStanding )
  {
    this.endStanding = endStanding;
  }

  public int getPayout()
  {
    return payout;
  }

  public void setPayout( int payout )
  {
    this.payout = payout;
  }

  public int getStartStanding()
  {
    return startStanding;
  }

  public void setStartStanding( int startStanding )
  {
    this.startStanding = startStanding;
  }

  public static Comparator<StackStandingPayout> PayoutIdComparator = new Comparator<StackStandingPayout>()
  {
    @Override
    public int compare( StackStandingPayout stackStandingPayout1, StackStandingPayout stackStandingPayout2 )
    {
      return stackStandingPayout1.getId().compareTo( stackStandingPayout2.getId() );
    }
  };

}
