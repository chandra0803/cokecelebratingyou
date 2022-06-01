/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.dao.promotion.hibernate;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import com.biperf.core.domain.enums.PromotionPayoutType;
import com.biperf.core.domain.promotion.ProductClaimPromotion;

/**
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
 * <td>wadzinsk</td>
 * <td>Oct 20, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 */
public class ProductClaimPromotionQueryConstraint extends PromotionQueryConstraint
{

  /**
   * Only include promos matching the given promotionPayoutType.
   */
  private PromotionPayoutType[] promotionPayoutTypesIncluded;

  /**
   * Return the result object type - Should be overridden by subclasses.
   */
  public Class getResultClass()
  {
    return ProductClaimPromotion.class;
  }

  public Criteria buildCriteria()
  {
    Criteria criteria = super.buildCriteria();

    // additional criteria for this subclass
    if ( promotionPayoutTypesIncluded != null && promotionPayoutTypesIncluded.length > 0 )
    {
      criteria.add( Restrictions.in( "promotion.payoutType", promotionPayoutTypesIncluded ) );
    }

    return criteria;
  }

  /**
   * Returns the promotionPayoutType(s) of a promotion
   * 
   * @return value of promotionPayoutType
   */
  public PromotionPayoutType[] getPromotionPayoutTypesIncluded()
  {
    return promotionPayoutTypesIncluded;
  }

  /**
   * Set the promotionPayoutType of a promotion
   * 
   * @param promotionPayoutTypesIncluded
   */
  public void setPromotionPayoutTypesIncluded( PromotionPayoutType[] promotionPayoutTypesIncluded )
  {
    this.promotionPayoutTypesIncluded = promotionPayoutTypesIncluded;
  }

}
