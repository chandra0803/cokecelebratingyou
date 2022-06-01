/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.dao.activity.hibernate;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import com.biperf.core.domain.activity.SweepstakesActivity;

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
 * <td>Nov 7, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 */
public class SweepstakesActivityQueryConstraint extends ActivityQueryConstraint
{

  /**
   * constrain by the drawing id
   */
  private Long promotionSweepstakeId;

  /**
   * Return the result object type - Should be overridden by subclasses.
   */
  public Class getResultClass()
  {
    return SweepstakesActivity.class;
  }

  public Criteria buildCriteria()
  {
    Criteria criteria = super.buildCriteria();

    // constrain by the drawing id
    if ( promotionSweepstakeId != null )
    {
      criteria.add( Restrictions.eq( "promotionSweepstake.id", promotionSweepstakeId ) );
    }

    return criteria;
  }

  /**
   * @return value of promotionSweepstakeId property
   */
  public Long getPromotionSweepstakeId()
  {
    return promotionSweepstakeId;
  }

  /**
   * @param promotionSweepstakeId value for promotionSweepstakeId property
   */
  public void setPromotionSweepstakeId( Long promotionSweepstakeId )
  {
    this.promotionSweepstakeId = promotionSweepstakeId;
  }

}
