/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/domain/promotion/PromotionECard.java,v $
 */

package com.biperf.core.domain.promotion;

import java.sql.Timestamp;

import com.biperf.core.domain.AuditCreateInfo;
import com.biperf.core.domain.multimedia.ECard;
import com.biperf.core.utils.UserManager;

/**
 * PromotionECard.
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
 * <td>Oct 10, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class PromotionECard extends PromotionCard implements Comparable
{
  private static final long serialVersionUID = 8618059577580176768L;
  private AuditCreateInfo auditCreateInfo;
  private ECard eCard;

  public PromotionECard()
  {
    auditCreateInfo = new AuditCreateInfo();
    auditCreateInfo.setCreatedBy( UserManager.getUserId() );
    auditCreateInfo.setDateCreated( new Timestamp( System.currentTimeMillis() ) );
  }

  public PromotionECard( Promotion promotion, ECard eCard, String orderNumber )
  {
    this.eCard = eCard;
    this.setPromotion( promotion );
    this.setOrderNumber( orderNumber );
  }

  /**
   * Checks equality of the object parameter to this.
   * 
   * @param o
   * @return boolean
   */
  public boolean equals( Object o )
  {
    if ( this == o )
    {
      return true;
    }
    if ( ! ( o instanceof PromotionECard ) )
    {
      return false;
    }

    final PromotionECard promotionECard = (PromotionECard)o;

    if ( getPromotion() != null )
    {
      if ( !getPromotion().equals( promotionECard.getPromotion() ) )
      {
        return false;
      }
    }
    if ( geteCard() != null )
    {
      if ( !geteCard().equals( promotionECard.geteCard() ) )
      {
        return false;
      }
    }

    /*
     * if ( getOrderNumber() != null ) { if (
     * !getOrderNumber().equals(promotionECard.getOrderNumber()) ) { return false; } }
     */

    return true;
  }

  /**
   * @see com.biperf.core.domain.BaseDomain#hashCode()
   * @return int
   */
  public int hashCode()
  {
    int result;

    result = this.getPromotion() != null ? this.getPromotion().hashCode() * 13 : 0;
    result += this.geteCard() != null ? this.geteCard().hashCode() : 0;
    // result += ( this.getOrderNumber() != null ? this.getOrderNumber().hashCode() * 32 : 0 );

    return result;
  }

  /**
   * Overridden from @see java.lang.Comparable#compareTo(java.lang.Object)
   * @param object
   * @return int
   * @throws ClassCastException
   */
  public int compareTo( Object object ) throws ClassCastException
  {
    if ( ! ( object instanceof PromotionECard ) )
    {
      throw new ClassCastException( "A PromotionECard was expected." );
    }
    PromotionECard promoEcard = (PromotionECard)object;
    return this.geteCard().getName().compareTo( promoEcard.geteCard().getName() );

  }

  public String toString()
  {
    StringBuffer sb = new StringBuffer( super.toString() );
    sb.append( "PROMOTIONECARD (" );
    sb.append( "{eCard=" + eCard + "}" );
    return sb.toString();
  }

  public void seteCard( ECard eCard )
  {
    this.eCard = eCard;
  }

  public ECard geteCard()
  {
    return this.eCard;
  }

  public PromotionECard deepCopy()
  {
    PromotionECard clone = new PromotionECard();
    clone.seteCard( this.eCard );
    // clone.setOrderNumber(this.getOrderNumber());
    return clone;
  }

}
