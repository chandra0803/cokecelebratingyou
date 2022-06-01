/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/domain/promotion/PromotionTeamPosition.java,v $
 */

package com.biperf.core.domain.promotion;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.enums.PromotionJobPositionType;

/**
 * PromotionTeamPosition.
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
 * <td>Aug 30, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class PromotionTeamPosition extends BaseDomain implements Cloneable
{

  private Promotion promotion;
  private boolean required;
  private PromotionJobPositionType promotionJobPositionType;

  public PromotionTeamPosition()
  {
    // empty default constructor
  }

  public PromotionTeamPosition( PromotionTeamPosition promotionTeamPositionsToCopy )
  {

    this.setPromotion( promotionTeamPositionsToCopy.getPromotion() );
    this.setRequired( promotionTeamPositionsToCopy.required );
    this.setPromotionJobPositionType( promotionTeamPositionsToCopy.getPromotionJobPositionType() );
  }

  /**
   * Clones this and removes the promotion which will be added. Overridden from
   * 
   * @see java.lang.Object#clone()
   * @return Object
   * @throws CloneNotSupportedException
   */
  public Object clone() throws CloneNotSupportedException
  {

    PromotionTeamPosition clonedPromotionTeamPosition = (PromotionTeamPosition)super.clone();

    clonedPromotionTeamPosition.setPromotion( null );
    clonedPromotionTeamPosition.resetBaseDomain();

    return clonedPromotionTeamPosition;
  }

  public Promotion getPromotion()
  {
    return promotion;
  }

  public void setPromotion( Promotion promotion )
  {
    this.promotion = promotion;
  }

  public PromotionJobPositionType getPromotionJobPositionType()
  {
    return promotionJobPositionType;
  }

  public void setPromotionJobPositionType( PromotionJobPositionType promotionJobPositionType )
  {
    this.promotionJobPositionType = promotionJobPositionType;
  }

  public boolean isRequired()
  {
    return required;
  }

  public void setRequired( boolean required )
  {
    this.required = required;
  }

  /**
   * Checks the equality of the object param to this. Overridden from
   * 
   * @see com.biperf.core.domain.BaseDomain#equals(java.lang.Object)
   * @param object
   * @return boolean
   */
  public boolean equals( Object object )
  {

    if ( object == this )
    {
      return true;
    }

    if ( ! ( object instanceof PromotionTeamPosition ) )
    {
      return false;
    }

    PromotionTeamPosition promotionTeamPosition = (PromotionTeamPosition)object;

    if ( !promotionTeamPosition.getPromotion().equals( this.getPromotion() ) )
    {
      return false;
    }

    if ( !promotionTeamPosition.getPromotionJobPositionType().equals( this.getPromotionJobPositionType() ) )
    {
      return false;
    }

    if ( promotionTeamPosition.isRequired() != this.isRequired() )
    {
      return false;
    }

    return true;
  }

  /**
   * Builds the hashCode for this. Overridden from
   * 
   * @see com.biperf.core.domain.BaseDomain#hashCode()
   * @return int
   */
  public int hashCode()
  {

    int hashCode = 0;

    hashCode += this.getPromotion() != null ? this.getPromotion().hashCode() : 13;
    hashCode += this.getPromotionJobPositionType() != null ? this.getPromotionJobPositionType().hashCode() : 17;

    return hashCode;

  }

  /**
   * Builds a String representation of this. Overridden from
   * 
   * @see java.lang.Object#toString()
   * @return String
   */
  public String toString()
  {

    StringBuffer sb = new StringBuffer();
    sb.append( "PromotionAudience [" );
    sb.append( "{id=" + super.getId() + "}, " );
    sb.append( "{promotion.id =" + this.getPromotion().getId() + "}, " );
    sb.append( "{isRequired = " + this.isRequired() + "}, " );
    sb.append( "{promotionJobPositionType = " + this.getPromotionJobPositionType().getName() + "}" );
    sb.append( "]" );

    return sb.toString();

  }

}
