/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/domain/promotion/PromotionApprovalOption.java,v $
 */

package com.biperf.core.domain.promotion;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.enums.PromotionApprovalOptionType;

/**
 * PromotionApprovalOption.
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
 * <td>Aug 3, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class PromotionApprovalOption extends BaseDomain implements Cloneable
{

  private Promotion promotion;
  private PromotionApprovalOptionType promotionApprovalOptionType;
  private Set promotionApprovalOptionReasons = new LinkedHashSet();

  /**
   * Empty constructor
   */
  public PromotionApprovalOption()
  {
    // empty constructor
  }

  /**
   * Used to copy the param to a new copy of this.
   * 
   * @param promotionApprovalOptionToCopy
   */
  public PromotionApprovalOption( PromotionApprovalOption promotionApprovalOptionToCopy )
  {
    this.setPromotion( promotionApprovalOptionToCopy.getPromotion() );

    // we dont want to have back reference for the obj, so create new and add
    // as oppose to set using setPromotionApprovalOptionReasons() method
    for ( Iterator iter = promotionApprovalOptionToCopy.getPromotionApprovalOptionReasons().iterator(); iter.hasNext(); )
    {
      PromotionApprovalOptionReason reason = (PromotionApprovalOptionReason)iter.next();
      this.addPromotionApprovalOptionReason( new PromotionApprovalOptionReason( reason ) );
    }
    this.setPromotionApprovalOptionType( promotionApprovalOptionToCopy.promotionApprovalOptionType );
  }

  /**
   * Clones this and the promotionApprovalOptionReasons if applicable. Overridden from
   * 
   * @see java.lang.Object#clone()
   * @return Object
   * @throws CloneNotSupportedException
   */
  public Object clone() throws CloneNotSupportedException
  {
    PromotionApprovalOption clonedPromotionApprovalOption = (PromotionApprovalOption)super.clone();
    clonedPromotionApprovalOption.setPromotion( null );

    clonedPromotionApprovalOption.resetBaseDomain();

    clonedPromotionApprovalOption.setPromotionApprovalOptionReasons( new LinkedHashSet() );
    for ( Iterator approvalOptionsIter = this.promotionApprovalOptionReasons.iterator(); approvalOptionsIter.hasNext(); )
    {
      PromotionApprovalOptionReason promotionApprovalOptionReason = (PromotionApprovalOptionReason)approvalOptionsIter.next();

      clonedPromotionApprovalOption.addPromotionApprovalOptionReason( (PromotionApprovalOptionReason)promotionApprovalOptionReason.clone() );
    }

    return clonedPromotionApprovalOption;
  }

  /**
   * Overridden from
   * 
   * @see java.lang.Object#equals(java.lang.Object)
   * @param object
   * @return true if equal
   */
  public boolean equals( Object object )
  {
    boolean equals = true;

    if ( ! ( object instanceof PromotionApprovalOption ) )
    {
      return false;
    }

    PromotionApprovalOption promotionApprovalOption = (PromotionApprovalOption)object;

    if ( promotionApprovalOption.getPromotion() != null && !promotionApprovalOption.getPromotion().equals( this.promotion ) )
    {
      equals = false;
    }
    if ( promotionApprovalOption.getPromotionApprovalOptionType() != null && !promotionApprovalOption.getPromotionApprovalOptionType().equals( this.promotionApprovalOptionType ) )
    {
      equals = false;
    }

    return equals;
  }

  /**
   * Overridden from
   * 
   * @see java.lang.Object#hashCode()
   * @return int the hashcode
   */
  public int hashCode()
  {

    int result;

    result = this.getPromotion() != null ? this.getPromotion().hashCode() : 0;
    result += 29 * ( this.getPromotionApprovalOptionType() != null ? this.getPromotionApprovalOptionType().hashCode() : 0 );

    return result;
  }

  /**
   * Overridden from
   * 
   * @see java.lang.Object#toString()
   * @return String representation of this object
   */
  public String toString()
  {
    StringBuffer sb = new StringBuffer();
    sb.append( "PromotionApprovalOption [" );
    sb.append( "{id=" + super.getId() + "}, " );
    sb.append( "{promotion.id=" + this.getPromotion().getId() + "}, " );
    sb.append( "{promotionApprovalOptionType=" + this.getPromotionApprovalOptionType().getName() + "}, " );
    sb.append( "{promotionApprovalOptionReasons=" + this.getPromotionApprovalOptionReasons() + "}" );
    sb.append( "]" );

    return sb.toString();
  }

  /**
   * @return promotion
   */
  public Promotion getPromotion()
  {
    return promotion;
  }

  /**
   * @param promotion
   */
  public void setPromotion( Promotion promotion )
  {
    this.promotion = promotion;
  }

  /**
   * @return promotionApprovalOptionType
   */
  public PromotionApprovalOptionType getPromotionApprovalOptionType()
  {
    return promotionApprovalOptionType;
  }

  /**
   * @param promotionApprovalOption
   */
  public void setPromotionApprovalOptionType( PromotionApprovalOptionType promotionApprovalOption )
  {
    this.promotionApprovalOptionType = promotionApprovalOption;
  }

  /**
   * @return promotionApprovalOptionReasons
   */
  public Set getPromotionApprovalOptionReasons()
  {
    return promotionApprovalOptionReasons;
  }

  /**
   * @param promotionApprovalOptionReasons
   */
  public void setPromotionApprovalOptionReasons( Set promotionApprovalOptionReasons )
  {
    this.promotionApprovalOptionReasons = promotionApprovalOptionReasons;
  }

  /**
   * Add a PromotionApprovalOptionReason to promotionApprovalOptionReasons
   * 
   * @param promoApprovalOptionReason
   */
  public void addPromotionApprovalOptionReason( PromotionApprovalOptionReason promoApprovalOptionReason )
  {
    promoApprovalOptionReason.setPromotionApprovalOption( this );
    this.promotionApprovalOptionReasons.add( promoApprovalOptionReason );
  }

}
