/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.dao.activity.hibernate;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import com.biperf.core.domain.activity.SalesActivity;

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
public class SalesActivityQueryConstraint extends ActivityQueryConstraint
{

  private Boolean carryover;

  /**
   * Return the result object type - Should be overridden by subclasses.
   */
  public Class getResultClass()
  {
    return SalesActivity.class;
  }

  public Criteria buildCriteria()
  {
    Criteria criteria = super.buildCriteria();

    // Constrain by carryover
    if ( carryover != null )
    {
      criteria.add( Restrictions.eq( "activity.carryover", carryover ) );
    }

    return criteria;
  }

  /**
   * @return value of carryover property
   */
  public Boolean getCarryover()
  {
    return carryover;
  }

  /**
   * @param carryover value for carryover property
   */
  public void setCarryover( Boolean carryover )
  {
    this.carryover = carryover;
  }

}
