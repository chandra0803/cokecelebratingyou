/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/domain/promotion/PromotionPayoutGroup.java,v $
 */

package com.biperf.core.domain.promotion;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.BooleanUtils;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.utils.GuidUtils;

/**
 * PromotionPayoutGroup.
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
 * <td>crosenquest</td>
 * <td>Jul 28, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class PromotionPayoutGroup extends BaseDomain implements Cloneable
{
  private String guid;
  private int quantity;
  private Integer teamMemberPayout;
  private int submitterPayout;
  private Integer minimumQualifier;
  private Boolean retroPayout;
  private Promotion promotion;
  private PromotionPayoutGroup parentPromotionPayoutGroup;
  private List promotionPayouts = new ArrayList();

  public PromotionPayoutGroup()
  {
    // empty constructor
  }

  public PromotionPayoutGroup( PromotionPayoutGroup promotionPayoutGroupToCopy )
  {
    guid = GuidUtils.generateGuid();

    this.quantity = promotionPayoutGroupToCopy.getQuantity();
    this.teamMemberPayout = promotionPayoutGroupToCopy.getTeamMemberPayout();
    this.submitterPayout = promotionPayoutGroupToCopy.getSubmitterPayout();
    this.promotion = promotionPayoutGroupToCopy.getPromotion();

    if ( promotionPayouts != null && promotionPayouts.size() > 0 )
    {
      Iterator promotionPayoutsIter = promotionPayouts.iterator();
      while ( promotionPayoutsIter.hasNext() )
      {

        PromotionPayout promotionPayout = (PromotionPayout)promotionPayoutsIter.next();

        this.promotionPayouts.add( new PromotionPayout( promotionPayout ) );
      }
    }
  }

  public String getGuid()
  {
    return guid;
  }

  public void setGuid( String guid )
  {
    this.guid = guid;
  }

  public int getQuantity()
  {
    return quantity;
  }

  public void setQuantity( int quantity )
  {
    this.quantity = quantity;
  }

  public int getSubmitterPayout()
  {
    return submitterPayout;
  }

  public void setSubmitterPayout( int submitterPayout )
  {
    this.submitterPayout = submitterPayout;
  }

  public Integer getTeamMemberPayout()
  {
    return teamMemberPayout;
  }

  public void setTeamMemberPayout( Integer teamMemberPayout )
  {
    this.teamMemberPayout = teamMemberPayout;
  }

  public Promotion getPromotion()
  {
    return promotion;
  }

  public void setPromotion( Promotion promotion )
  {
    this.promotion = promotion;
  }

  public List getPromotionPayouts()
  {
    return promotionPayouts;
  }

  public int getPromotionPayoutsCount()
  {
    if ( promotionPayouts == null )
    {
      return 0;
    }
    return promotionPayouts.size();
  }

  public void setPromotionPayouts( List promotionPayouts )
  {
    this.promotionPayouts = promotionPayouts;
  }

  /**
   * Add a PromotionPayout to promotionPayouts
   * 
   * @param promoPayout
   */
  public void addPromotionPayout( PromotionPayout promoPayout )
  {
    promoPayout.setPromotionPayoutGroup( this );
    this.promotionPayouts.add( promoPayout );
  }

  /**
   * Clones this, removes the auditInfo and Id and clones the promotionPayouts if applicable.
   * Overridden from
   * 
   * @see java.lang.Object#clone()
   * @return Object
   * @throws CloneNotSupportedException
   */
  public Object clone() throws CloneNotSupportedException
  {

    PromotionPayoutGroup clonedPromotionPayoutGroup = (PromotionPayoutGroup)super.clone();
    clonedPromotionPayoutGroup.resetBaseDomain();

    // copy the promotionPayoutGroup
    clonedPromotionPayoutGroup.setPromotionPayouts( new ArrayList() );
    for ( Iterator promotionPayoutsIter = this.getPromotionPayouts().iterator(); promotionPayoutsIter.hasNext(); )
    {
      PromotionPayout promotionPayout = (PromotionPayout)promotionPayoutsIter.next();
      clonedPromotionPayoutGroup.addPromotionPayout( (PromotionPayout)promotionPayout.clone() );
    }

    return clonedPromotionPayoutGroup;

  }

  public boolean equals( Object object )
  {
    boolean equals = true;

    if ( ! ( object instanceof PromotionPayoutGroup ) )
    {
      return false;
    }

    PromotionPayoutGroup promotionPayoutGroup = (PromotionPayoutGroup)object;

    if ( guid != null && !guid.equals( promotionPayoutGroup.getGuid() ) )
    {
      return false;
    }

    return equals;
  }

  public int hashCode()
  {
    return this.guid.hashCode();
  }

  public String toString()
  {

    StringBuffer sb = new StringBuffer();
    sb.append( "PromotionPayoutGroup [" );
    sb.append( "{id=" + super.getId() + "}, " );
    sb.append( "{guid=" + this.getGuid() + "}, " );
    sb.append( "{submitterPayout=" + this.getSubmitterPayout() + "}, " );
    sb.append( "{teamMemberPayout=" + this.getTeamMemberPayout() + "}, " );
    sb.append( "{promotion.id=" + this.getPromotion().getId() + "}, " );
    sb.append( "{promotionPayouts=" + this.getPromotionPayouts() + "}" );
    sb.append( "]" );

    return sb.toString();

  }

  public PromotionPayoutGroup getParentPromotionPayoutGroup()
  {
    return parentPromotionPayoutGroup;
  }

  public void setParentPromotionPayoutGroup( PromotionPayoutGroup parentPromotionPayoutGroup )
  {
    this.parentPromotionPayoutGroup = parentPromotionPayoutGroup;
  }

  public Integer getMinimumQualifier()
  {
    return minimumQualifier;
  }

  public void setMinimumQualifier( Integer minimumQualifier )
  {
    this.minimumQualifier = minimumQualifier;
  }

  public Boolean getRetroPayout()
  {
    return retroPayout;
  }

  public void setRetroPayout( Boolean retroPayout )
  {
    this.retroPayout = retroPayout;
  }

  public boolean isRetroPayout()
  {
    return BooleanUtils.isTrue( retroPayout );
  }

  /**
   * returns true is minimum qualifier is used.
   */
  public boolean isMinimumQualifierUsed()
  {

    return minimumQualifier != null;
  }

}
