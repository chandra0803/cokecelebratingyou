
package com.biperf.core.domain.promotion;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.hierarchy.NodeType;

@SuppressWarnings( "serial" )
public class StackStandingPayoutGroup extends BaseDomain implements Cloneable
{
  private NodeType nodeType;
  private Promotion promotion;
  private Set<StackStandingPayout> stackStandingPayouts = new LinkedHashSet<StackStandingPayout>();
  private int numOfPayouts;

  public StackStandingPayoutGroup()
  {
    // empty constructor
  }

  public StackStandingPayoutGroup( StackStandingPayoutGroup promotionStackStandingPayoutGroupToCopy )
  {

    this.nodeType = promotionStackStandingPayoutGroupToCopy.getNodeType();
    this.promotion = promotionStackStandingPayoutGroupToCopy.getPromotion();

    if ( stackStandingPayouts != null && stackStandingPayouts.size() > 0 )
    {
      Iterator<StackStandingPayout> stackStandingPayoutsIter = stackStandingPayouts.iterator();
      while ( stackStandingPayoutsIter.hasNext() )
      {
        StackStandingPayout stackStandingPayout = stackStandingPayoutsIter.next();

        this.stackStandingPayouts.add( new StackStandingPayout( stackStandingPayout ) );
      }
    }
  }

  public Promotion getPromotion()
  {
    return promotion;
  }

  public void setPromotion( Promotion promotion )
  {
    this.promotion = promotion;
  }

  public void setStackStandingPayouts( Set<StackStandingPayout> promotionPayouts )
  {
    this.stackStandingPayouts = promotionPayouts;
  }

  /**
   * Return the stack standing payouts associated with this group
   * 
   * @return the stack standing payouts associated with this group, as a <code>Set</code> of
   *         {@link com.biperf.core.domain.promotion.StackStandingPayout} objects.
   */
  public Set<StackStandingPayout> getStackStandingPayouts()
  {
    return stackStandingPayouts;
  }

  /**
   * Set the NodeType for this stack standing payout group
   * 
   * @param nodeType
   */
  public void setNodeType( NodeType nodeType )
  {
    this.nodeType = nodeType;
  }

  /**
   * Get the current NodeType for the stack standing payout group
   * 
   * @return nodeType
   */
  public NodeType getNodeType()
  {
    return nodeType;
  }

  /**
   * Add a StackStandingPayout to stackStandingPayouts
   * 
   * @param stackStandingPayout
   */
  public void addStackStandingPayout( StackStandingPayout stackStandingPayout )
  {
    stackStandingPayout.setStackStandingPayoutGroup( this );
    this.stackStandingPayouts.add( stackStandingPayout );
  }

  public Set<StackStandingPayout> getPromotionStackStandingPayout()
  {
    return stackStandingPayouts;
  }

  public int getPromotionStackStandingPayoutsCount()
  {
    if ( stackStandingPayouts == null )
    {
      return 0;
    }
    return stackStandingPayouts.size();
  }

  public boolean equals( Object object )
  {
    boolean equals = true;

    if ( ! ( object instanceof StackStandingPayoutGroup ) )
    {
      return false;
    }

    StackStandingPayoutGroup stackStandingPayoutGroup = (StackStandingPayoutGroup)object;

    if ( getPromotion() != null ? !getPromotion().equals( stackStandingPayoutGroup.getPromotion() ) : stackStandingPayoutGroup.getPromotion() != null )
    {
      return false;
    }
    if ( getNodeType() != null ? !getNodeType().equals( stackStandingPayoutGroup.getNodeType() ) : stackStandingPayoutGroup.getNodeType() != null )
    {
      return false;
    }

    return equals;
  }

  public int hashCode()
  {
    if ( getPromotion() != null && getNodeType() != null )
    {
      return 29 * getPromotion().hashCode() + getNodeType().hashCode();
    }

    return 0;
  }

  public String toString()
  {

    StringBuffer sb = new StringBuffer();
    sb.append( "StackStandingPayoutGroup [" );
    sb.append( "{id=" + super.getId() + "}, " );
    sb.append( "{nodeType=" + this.getNodeType() + "}, " );
    sb.append( "{promotion.id=" + this.getPromotion().getId() + "}, " );
    sb.append( "{stackStandingPayouts=" + this.getPromotionStackStandingPayout() + "}" );
    sb.append( "]" );

    return sb.toString();

  }

  /**
   * Clones this, removes the auditInfo and Id and clones the stackStandingPayouts if applicable.
   * Overridden from
   * 
   * @see java.lang.Object#clone()
   * @return Object
   * @throws CloneNotSupportedException
   */
  public Object clone() throws CloneNotSupportedException
  {

    StackStandingPayoutGroup clonedStackStandingPayoutGroup = (StackStandingPayoutGroup)super.clone();
    clonedStackStandingPayoutGroup.resetBaseDomain();

    // copy the promotionStackStandingPayoutGroup
    clonedStackStandingPayoutGroup.setStackStandingPayouts( new HashSet<StackStandingPayout>() );
    for ( Iterator<StackStandingPayout> promotionPayoutsIter = this.getPromotionStackStandingPayout().iterator(); promotionPayoutsIter.hasNext(); )
    {
      StackStandingPayout promotionPayout = promotionPayoutsIter.next();
      clonedStackStandingPayoutGroup.addStackStandingPayout( (StackStandingPayout)promotionPayout.clone() );
    }

    return clonedStackStandingPayoutGroup;

  }

  public void setNumOfPayouts( int numOfPayouts )
  {
    this.numOfPayouts = numOfPayouts;
  }

  public int getNumOfPayouts()
  {
    return numOfPayouts;
  }

  public boolean isHierarchyPayoutGroup()
  {
    return nodeType == null;
  }

  public boolean isRankingOnly()
  {
    return stackStandingPayouts == null || stackStandingPayouts.isEmpty();
  }

  public boolean hasPayout()
  {
    return stackStandingPayouts != null && !stackStandingPayouts.isEmpty();
  }

}
