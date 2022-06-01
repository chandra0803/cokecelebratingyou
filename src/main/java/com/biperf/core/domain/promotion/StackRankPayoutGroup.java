/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/domain/promotion/StackRankPayoutGroup.java,v $
 */

package com.biperf.core.domain.promotion;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.enums.SubmittersToRankType;
import com.biperf.core.domain.hierarchy.NodeType;

/**
 * StackRankPayoutGroup.
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
public class StackRankPayoutGroup extends BaseDomain implements Cloneable
{
  private NodeType nodeType;
  private Promotion promotion;
  private Set stackRankPayouts = new LinkedHashSet();
  private SubmittersToRankType submittersToRankType;

  public StackRankPayoutGroup()
  {
    // empty constructor
  }

  public StackRankPayoutGroup( StackRankPayoutGroup promotionStackRankPayoutGroupToCopy )
  {

    this.nodeType = promotionStackRankPayoutGroupToCopy.getNodeType();
    this.promotion = promotionStackRankPayoutGroupToCopy.getPromotion();
    this.submittersToRankType = promotionStackRankPayoutGroupToCopy.getSubmittersToRankType();

    if ( stackRankPayouts != null && stackRankPayouts.size() > 0 )
    {
      Iterator stackRankPayoutsIter = stackRankPayouts.iterator();
      while ( stackRankPayoutsIter.hasNext() )
      {
        StackRankPayout stackRankPayout = (StackRankPayout)stackRankPayoutsIter.next();

        this.stackRankPayouts.add( new StackRankPayout( stackRankPayout ) );
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

  public Set getPromotionStackRankPayout()
  {
    return stackRankPayouts;
  }

  public int getPromotionStackRankPayoutsCount()
  {
    if ( stackRankPayouts == null )
    {
      return 0;
    }
    return stackRankPayouts.size();
  }

  public void setStackRankPayouts( Set promotionPayouts )
  {
    this.stackRankPayouts = promotionPayouts;
  }

  /**
   * Add a StackRankPayout to stackRankPayouts
   * 
   * @param stackRankPayout
   */
  public void addStackRankPayout( StackRankPayout stackRankPayout )
  {
    stackRankPayout.setStackRankPayoutGroup( this );
    this.stackRankPayouts.add( stackRankPayout );
  }

  /**
   * Clones this, removes the auditInfo and Id and clones the stackRankPayouts if applicable.
   * Overridden from
   * 
   * @see java.lang.Object#clone()
   * @return Object
   * @throws CloneNotSupportedException
   */
  public Object clone() throws CloneNotSupportedException
  {

    StackRankPayoutGroup clonedStackRankPayoutGroup = (StackRankPayoutGroup)super.clone();
    clonedStackRankPayoutGroup.resetBaseDomain();

    // copy the promotionStackRankPayoutGroup
    clonedStackRankPayoutGroup.setStackRankPayouts( new HashSet() );
    for ( Iterator promotionPayoutsIter = this.getPromotionStackRankPayout().iterator(); promotionPayoutsIter.hasNext(); )
    {
      StackRankPayout promotionPayout = (StackRankPayout)promotionPayoutsIter.next();
      clonedStackRankPayoutGroup.addStackRankPayout( (StackRankPayout)promotionPayout.clone() );
    }

    return clonedStackRankPayoutGroup;

  }

  public boolean equals( Object object )
  {
    boolean equals = true;

    if ( ! ( object instanceof StackRankPayoutGroup ) )
    {
      return false;
    }

    StackRankPayoutGroup stackRankPayoutGroup = (StackRankPayoutGroup)object;

    if ( getPromotion() != null ? !getPromotion().equals( stackRankPayoutGroup.getPromotion() ) : stackRankPayoutGroup.getPromotion() != null )
    {
      return false;
    }
    if ( getNodeType() != null ? !getNodeType().equals( stackRankPayoutGroup.getNodeType() ) : stackRankPayoutGroup.getNodeType() != null )
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
    sb.append( "StackRankPayoutGroup [" );
    sb.append( "{id=" + super.getId() + "}, " );
    sb.append( "{nodeType=" + this.getNodeType() + "}, " );
    sb.append( "{promotion.id=" + this.getPromotion().getId() + "}, " );
    sb.append( "{stackRankPayouts=" + this.getPromotionStackRankPayout() + "}" );
    sb.append( "]" );

    return sb.toString();

  }

  /**
   * Return the associated SubmittersToRankType of the stack rank payout group
   * 
   * @return SubmittersToRankType
   */
  public SubmittersToRankType getSubmittersToRankType()
  {
    return submittersToRankType;
  }

  /**
   * set the SubmittersToRankType to the stack rank payout group
   * 
   * @param submittersToRankType
   */
  public void setSubmittersToRankType( SubmittersToRankType submittersToRankType )
  {
    this.submittersToRankType = submittersToRankType;
  }

  /**
   * Return the stack rank payouts associated with this group
   * 
   * @return the stack rank payouts associated with this group, as a <code>Set</code> of
   *         {@link com.biperf.core.domain.promotion.StackRankPayout} objects.
   */
  public Set getStackRankPayouts()
  {
    return stackRankPayouts;
  }

  /**
   * Set the NodeType for this stack rank payout group
   * 
   * @param nodeType
   */
  public void setNodeType( NodeType nodeType )
  {
    this.nodeType = nodeType;
  }

  /**
   * Get the current NodeType for the stack rank payout group
   * 
   * @return nodeType
   */
  public NodeType getNodeType()
  {
    return nodeType;
  }

}
