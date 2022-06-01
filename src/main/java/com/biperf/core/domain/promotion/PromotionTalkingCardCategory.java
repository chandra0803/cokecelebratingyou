/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/domain/promotion/PromotionTalkingCardCategory.java,v $
 */

package com.biperf.core.domain.promotion;

import com.biperf.core.domain.multimedia.OddCastCategory;

/**
 * PromotionTalkingCardCategory.
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
public class PromotionTalkingCardCategory extends PromotionCard implements Comparable
{
  private OddCastCategory talkingCardCategory;

  /**
   * Overridden from @see java.lang.Comparable#compareTo(java.lang.Object)
   * @param object
   * @return int
   * @throws ClassCastException
   */
  public int compareTo( Object object ) throws ClassCastException
  {
    if ( ! ( object instanceof PromotionTalkingCardCategory ) )
    {
      throw new ClassCastException( "A talkingCardCategory was expected." );
    }
    PromotionTalkingCardCategory promoTalkingCardCategory = (PromotionTalkingCardCategory)object;
    return this.getTalkingCardCategory().getName().compareTo( promoTalkingCardCategory.getTalkingCardCategory().getName() );
  }

  public String toString()
  {
    StringBuffer sb = new StringBuffer( super.toString() );
    sb.append( "PROMOTIONTALKINGCARD (" );
    sb.append( "{talkingCardCategory=" + talkingCardCategory + "}" );
    return sb.toString();
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
    if ( ! ( o instanceof PromotionTalkingCardCategory ) )
    {
      return false;
    }

    final PromotionTalkingCardCategory promotionTalkingCardCategory = (PromotionTalkingCardCategory)o;

    if ( getPromotion() != null )
    {
      if ( !getPromotion().equals( promotionTalkingCardCategory.getPromotion() ) )
      {
        return false;
      }
    }
    if ( getTalkingCardCategory() != null )
    {
      if ( !getTalkingCardCategory().equals( promotionTalkingCardCategory.getTalkingCardCategory() ) )
      {
        return false;
      }
    }

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
    result += this.getTalkingCardCategory() != null ? this.getTalkingCardCategory().hashCode() : 0;

    return result;
  }

  public void setTalkingCardCategory( OddCastCategory talkingCardCategory )
  {
    this.talkingCardCategory = talkingCardCategory;
  }

  public OddCastCategory getTalkingCardCategory()
  {
    return this.talkingCardCategory;
  }

  public PromotionTalkingCardCategory deepCopy()
  {
    PromotionTalkingCardCategory clone = new PromotionTalkingCardCategory();
    clone.setTalkingCardCategory( this.getTalkingCardCategory() );
    return clone;
  }
}
