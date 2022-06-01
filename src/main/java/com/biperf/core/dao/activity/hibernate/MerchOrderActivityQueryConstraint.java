/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/dao/activity/hibernate/MerchOrderActivityQueryConstraint.java,v $
 */

package com.biperf.core.dao.activity.hibernate;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import com.biperf.core.domain.activity.MerchOrderActivity;

public class MerchOrderActivityQueryConstraint extends RecognitionActivityQueryConstraint
{
  private Long merchOrderId;

  /**
   * Return the result object type - Should be overridden by subclasses.
   */
  public Class getResultClass()
  {
    return MerchOrderActivity.class;
  }

  public Criteria buildCriteria()
  {
    Criteria criteria = super.buildCriteria();

    // Constrain by merchOrderId
    if ( merchOrderId != null )
    {
      criteria.add( Restrictions.eq( "activity.merchOrderId", merchOrderId ) );
    }
    return criteria;
  }

  /**
   * @return the merchOrderId
   */
  public Long getMerchOrderId()
  {
    return merchOrderId;
  }

  /**
   * @param merchOrderId
   *            the merchOrderId to set
   */
  public void setMerchOrderId( Long merchOrderId )
  {
    this.merchOrderId = merchOrderId;
  }

}
