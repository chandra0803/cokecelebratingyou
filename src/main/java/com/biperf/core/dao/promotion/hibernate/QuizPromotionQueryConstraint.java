/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.dao.promotion.hibernate;

import org.hibernate.Criteria;

import com.biperf.core.domain.promotion.QuizPromotion;

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
public class QuizPromotionQueryConstraint extends PromotionQueryConstraint
{
  /**
   * Return the result object type - Should be overridden by subclasses.
   */
  public Class getResultClass()
  {
    return QuizPromotion.class;
  }

  public Criteria buildCriteria()
  {
    Criteria criteria = super.buildCriteria();

    // No additional criteria for this subclass

    return criteria;
  }
}
