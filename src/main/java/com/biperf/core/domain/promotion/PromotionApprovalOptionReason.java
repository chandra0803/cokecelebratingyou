/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/domain/promotion/PromotionApprovalOptionReason.java,v $
 */

package com.biperf.core.domain.promotion;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.enums.PromotionApprovalOptionReasonType;

/**
 * PromotionApprovalOptionReason.
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
public class PromotionApprovalOptionReason extends BaseDomain implements Cloneable
{
  private PromotionApprovalOption promotionApprovalOption;
  private PromotionApprovalOptionReasonType promotionApprovalOptionReasonType;

  public PromotionApprovalOptionReason()
  {
    // empty construction
  }

  public PromotionApprovalOptionReason( PromotionApprovalOptionReason toCopy )
  {
    this.setPromotionApprovalOption( toCopy.promotionApprovalOption );
    this.setPromotionApprovalOptionReasonType( toCopy.promotionApprovalOptionReasonType );
  }

  /**
   * Clones this object. Overridden from
   * 
   * @see java.lang.Object#clone()
   * @return Object
   * @throws CloneNotSupportedException
   */
  public Object clone() throws CloneNotSupportedException
  {
    PromotionApprovalOptionReason clonedPromotionApprovalOptionReason = (PromotionApprovalOptionReason)super.clone();

    clonedPromotionApprovalOptionReason.setPromotionApprovalOption( null );

    clonedPromotionApprovalOptionReason.resetBaseDomain();

    return clonedPromotionApprovalOptionReason;
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

    if ( ! ( object instanceof PromotionApprovalOptionReason ) )
    {
      return false;
    }

    PromotionApprovalOptionReason promotionApprovalOptionReason = (PromotionApprovalOptionReason)object;

    if ( promotionApprovalOptionReason.getPromotionApprovalOption() != null && !promotionApprovalOptionReason.getPromotionApprovalOption().equals( this.promotionApprovalOption ) )
    {
      equals = false;
    }

    if ( promotionApprovalOptionReason.getPromotionApprovalOptionReasonType() != this.promotionApprovalOptionReasonType )
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
    result = this.getPromotionApprovalOption() != null ? this.getPromotionApprovalOption().hashCode() : 0;
    result = 29 * result + this.getPromotionApprovalOptionReasonType().hashCode();

    return result;
  }

  /**
   * Overridden from
   * 
   * @see java.lang.Object#toString()
   * @return String representation of the object
   */
  public String toString()
  {

    StringBuffer sb = new StringBuffer();
    sb.append( "PromotionApprovalOptionReason [" );
    sb.append( "{id=" + super.getId() + "}, " );
    sb.append( "{promotionApprovalOptionId=" + this.getPromotionApprovalOption().getId() + "}, " );
    sb.append( "{promotionApprovalOptionReason=" + this.getPromotionApprovalOptionReasonType().getName() + "}, " );
    sb.append( "]" );

    return sb.toString();

  }

  /**
   * @return promotionApprovalOption
   */
  public PromotionApprovalOption getPromotionApprovalOption()
  {
    return promotionApprovalOption;
  }

  /**
   * @param promotionApprovalOption
   */
  public void setPromotionApprovalOption( PromotionApprovalOption promotionApprovalOption )
  {
    this.promotionApprovalOption = promotionApprovalOption;
  }

  /**
   * @return promotionApprovalOptionReasonType
   */
  public PromotionApprovalOptionReasonType getPromotionApprovalOptionReasonType()
  {
    return promotionApprovalOptionReasonType;
  }

  /**
   * @param promotionApprovalOptionReasonType
   */
  public void setPromotionApprovalOptionReasonType( PromotionApprovalOptionReasonType promotionApprovalOptionReasonType )
  {
    this.promotionApprovalOptionReasonType = promotionApprovalOptionReasonType;
  }

}
