/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/dao/promotion/hibernate/GoalQuestPromotionQueryConstraint.java,v $
 */

package com.biperf.core.dao.promotion.hibernate;

import java.util.Date;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import com.biperf.core.domain.enums.ManagerOverrideStructure;
import com.biperf.core.domain.promotion.GoalQuestPromotion;
import com.biperf.core.utils.DateUtils;

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
 * <td>sedey</td>
 * <td>Dec 29, 2006</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 */
public class GoalQuestPromotionQueryConstraint extends PromotionQueryConstraint
{
  // ---------------------------------------------------------------------------
  // Fields
  // ---------------------------------------------------------------------------

  /**
   * If true, include only the goal quest promotions for which award process has been
   * run; if false, include only promotions that haven't run the award process.
   */
  private Boolean hasIssueAwardsRun;

  private Boolean hasAllowUnderArmour;

  /**
   * If used, only include goal quest promotions whose goalCollectionEndDate is less than
   * or equal to this date.
   */
  private Date endDate;

  /**
   * If used, only include goal quest promotions whose goalCollectionStartDate is less than
   * or equal to this date.
   */
  private Date startDate;

  private ManagerOverrideStructure[] overrideStructureExcluded;

  /**
   * Return the result object type - Should be overridden by subclasses.
   */
  public Class getResultClass()
  {
    return GoalQuestPromotion.class;
  }

  public Criteria buildCriteria()
  {
    Criteria criteria = super.buildCriteria();
    // additional criteria for goal quest promotion
    if ( hasIssueAwardsRun != null )
    {
      // include promotions that either have or haven't run the award process.
      criteria.add( Restrictions.eq( "promotion.issueAwardsRun", hasIssueAwardsRun ) );
    }
    if ( endDate != null )
    {
      // shift date to end of day
      Date endDateEndOfDay = DateUtils.toEndDate( endDate );
      criteria.add( Restrictions.le( "promotion.goalCollectionEndDate", endDateEndOfDay ) );
    }
    if ( startDate != null )
    {
      // shift date to beginning of day
      Date startDateBeginningOfDay = DateUtils.toStartDate( startDate );
      criteria.add( Restrictions.le( "promotion.goalCollectionStartDate", startDateBeginningOfDay ) );
    }
    if ( overrideStructureExcluded != null && overrideStructureExcluded.length > 0 )
    {
      criteria.add( Restrictions.not( Restrictions.in( "promotion.overrideStructure", overrideStructureExcluded ) ) );
    }
    // additional criteria for goal quest promotion
    if ( hasAllowUnderArmour != null )
    {
      // include promotions that either have or haven't include UA.
      criteria.add( Restrictions.eq( "promotion.allowUnderArmour", hasAllowUnderArmour ) );
    }
    return criteria;
  }

  public Boolean getHasAllowUnderArmour()
  {
    return hasAllowUnderArmour;
  }

  public void setHasAllowUnderArmour( Boolean hasAllowUnderArmour )
  {
    this.hasAllowUnderArmour = hasAllowUnderArmour;
  }

  public Boolean getHasIssueAwardsRun()
  {
    return hasIssueAwardsRun;
  }

  public void setHasIssueAwardsRun( Boolean hasIssueAwardsRun )
  {
    this.hasIssueAwardsRun = hasIssueAwardsRun;
  }

  public Date getEndDate()
  {
    return endDate;
  }

  public void setEndDate( Date endDate )
  {
    this.endDate = endDate;
  }

  public Date getStartDate()
  {
    return startDate;
  }

  public void setStartDate( Date startDate )
  {
    this.startDate = startDate;
  }

  public ManagerOverrideStructure[] getOverrideStructureExcluded()
  {
    return overrideStructureExcluded;
  }

  public void setOverrideStructureExcluded( ManagerOverrideStructure[] overrideStructureExcluded )
  {
    this.overrideStructureExcluded = overrideStructureExcluded;
  }
}
