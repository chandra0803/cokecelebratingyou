/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/dao/promotion/hibernate/ChallengepointPromotionQueryConstraint.java,v $
 */

package com.biperf.core.dao.promotion.hibernate;

import java.util.Date;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import com.biperf.core.domain.enums.ManagerOverrideStructure;
import com.biperf.core.domain.promotion.ChallengePointPromotion;
import com.biperf.core.utils.DateUtils;

/**
 * <p>ChallengepointPromotionQueryConstraint
 * <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>babu</td>
 * <td>Jul 25, 2008</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 */
public class ChallengepointPromotionQueryConstraint extends PromotionQueryConstraint
{
  // ---------------------------------------------------------------------------
  // Fields
  // ---------------------------------------------------------------------------

  /**
   * If true, include only the goal quest promotions for which award process has been
   * run; if false, include only promotions that haven't run the award process.
   */
  private Boolean hasIssueAwardsRun;
  /**
   * If used, only include challengepoint promotions whose endSelectionDate is less than
   * or equal to this date.
   */
  private Date endSelectionDate;

  /**
   * If used, only include challengepoint promotions whose startDate is less than
   * or equal to this date.
   */
  private Date startDate;

  private ManagerOverrideStructure[] overrideStructureExcluded;

  /**
   * Return the result object type - Should be overridden by subclasses.
   */
  public Class getResultClass()
  {
    return ChallengePointPromotion.class;
  }

  public Criteria buildCriteria()
  {
    Criteria criteria = super.buildCriteria();
    if ( hasIssueAwardsRun != null )
    {
      // include promotions that either have or haven't run the award process.
      criteria.add( Restrictions.eq( "promotion.issueAwardsRun", hasIssueAwardsRun ) );
    }
    if ( endSelectionDate != null )
    {
      // shift date to end of day
      // Fix 21403 run calculation should display only after cp selection end date.
      Date endDateEndOfDay = DateUtils.toStartDate( endSelectionDate );
      criteria.add( Restrictions.lt( "promotion.goalCollectionEndDate", endDateEndOfDay ) );
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
    return criteria;
  }

  public Date getEndSelectionDate()
  {
    return endSelectionDate;
  }

  public void setEndSelectionDate( Date endSelectionDate )
  {
    this.endSelectionDate = endSelectionDate;
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

  public Boolean getHasIssueAwardsRun()
  {
    return hasIssueAwardsRun;
  }

  public void setHasIssueAwardsRun( Boolean hasIssueAwardsRun )
  {
    this.hasIssueAwardsRun = hasIssueAwardsRun;
  }
}
