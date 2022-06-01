/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.dao.activity.hibernate;

import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.LogicalExpression;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.SimpleExpression;

import com.biperf.core.domain.activity.ManagerOverrideActivity;

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
public class ManagerOverrideActivityQueryConstraint extends ActivityQueryConstraint
{

  /**
   * only include activities whose minimumQualifierStatus's minimumQualifierMet matches the input
   * statusMinimumQualifierMet. Return all if the input pendingMinimumQualifier is null.
   */
  private Boolean statusMinimumQualifierMet;

  /**
   * only include activities whose minimumQualifierStatus's promotionPayoutGroup ID matches
   * statusPromotionPayoutGroupId.
   */
  private Long statusPromotionPayoutGroupId;

  /**
   * Return the result object type - Should be overridden by subclasses.
   */
  public Class getResultClass()
  {
    return ManagerOverrideActivity.class;
  }

  public Criteria buildCriteria()
  {
    Criteria criteria = super.buildCriteria();

    // Constrain by statusMinimumQualifierMet
    if ( statusMinimumQualifierMet != null )
    {
      Criteria criteriaWithStatus = createAliasIfNotAlreadyCreated( criteria, "activity.minimumQualifierStatus", "minimumQualifierStatus" );

      SimpleExpression equalRestriction = Restrictions.eq( "minimumQualifierStatus.minQualifierMet", statusMinimumQualifierMet );
      if ( !statusMinimumQualifierMet.booleanValue() )
      {
        criteriaWithStatus.add( equalRestriction );
      }
      else
      {
        // minimumQualifierMet also considered true if status is null
        Criterion nullRestriction = Restrictions.isNull( "activity.minimumQualifierStatus" );
        LogicalExpression logicalExpression = Restrictions.or( nullRestriction, equalRestriction );
        criteria.add( logicalExpression );
      }
    }

    // Constrain by statusPromotionPayoutGroupId
    if ( statusPromotionPayoutGroupId != null )
    {
      createAliasIfNotAlreadyCreated( criteria, "activity.minimumQualifierStatus", "minimumQualifierStatus" )
          .add( Restrictions.eq( "minimumQualifierStatus.promotionPayoutGroup.id", statusPromotionPayoutGroupId ) );
    }

    return criteria;
  }

  /**
   * @return value of statusMinimumQualifierMet property
   */
  public Boolean getStatusMinimumQualifierMet()
  {
    return statusMinimumQualifierMet;
  }

  /**
   * @param statusMinimumQualifierMet value for statusMinimumQualifierMet property
   */
  public void setStatusMinimumQualifierMet( Boolean statusMinimumQualifierMet )
  {
    this.statusMinimumQualifierMet = statusMinimumQualifierMet;
  }

  /**
   * @return value of statusPromotionPayoutGroupId property
   */
  public Long getStatusPromotionPayoutGroupId()
  {
    return statusPromotionPayoutGroupId;
  }

  /**
   * @param statusPromotionPayoutGroupId value for statusPromotionPayoutGroupId property
   */
  public void setStatusPromotionPayoutGroupId( Long statusPromotionPayoutGroupId )
  {
    this.statusPromotionPayoutGroupId = statusPromotionPayoutGroupId;
  }

}
