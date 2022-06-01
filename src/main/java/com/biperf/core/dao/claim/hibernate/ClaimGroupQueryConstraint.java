/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.dao.claim.hibernate;

import java.util.Date;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import com.biperf.core.dao.BaseQueryConstraint;
import com.biperf.core.domain.claim.ClaimGroup;
import com.biperf.core.domain.enums.ApproverType;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.HibernateSessionManager;

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
public class ClaimGroupQueryConstraint extends BaseQueryConstraint
{

  /**
   * constrain by claims against the given promotion
   */
  private Long[] includedPromotionIds;

  /**
   * Include claim groups for the given participant.
   */
  private Long participantId;

  /**
   * only include claims whose promotions don't use the specified excludedPromotionApproverTypes.
   */
  private ApproverType[] excludedPromotionApproverTypes;

  /**
   * only include claims whose promotions use the specified excludedPromotionApproverTypes.
   */
  private ApproverType[] includedPromotionApproverTypes;

  /**
   * include only claim groups submitted on or after the beginning of day of startDate.
   */
  private Date startDate;

  /**
   * include only claim groups submitted on or before end of day of endDate.
   */
  private Date endDate;

  /**
   * true to include by claims that are open; closed if false.
   */
  private Boolean open;

  /**
   * If true, include claimGroups whose notification Date is non-null; if false, those
   * that are null.
   */
  private Boolean notificationDateExists;

  /**
   * include only claimGroups whose notification Date is past (or equal to ) the
   * notificationDatePastDate.
   */
  private Date notificationDatePastDate;

  /**
   * true to include claim groups which have one or more posted activity; if false, claim groups 
   * with no unposted activities.
   */
  private Boolean anyActivitityUnposted;

  /**
   * Return the result object type - Should be overridden by subclasses. Overridden from
   * 
   * @see com.biperf.core.dao.QueryConstraint#getResultClass()
   * @return Class
   */
  public Class getResultClass()
  {
    return ClaimGroup.class;
  }

  public Criteria buildCriteria()
  {
    Criteria criteria = HibernateSessionManager.getSession().createCriteria( getResultClass(), "claimGroup" );

    // Constrain by promotion
    if ( includedPromotionIds != null )
    {
      criteria.add( Restrictions.in( "claimGroup.promotion.id", includedPromotionIds ) );
    }

    // Constrain by participant
    if ( participantId != null )
    {
      criteria.add( Restrictions.eq( "claimGroup.participant.id", participantId ) );
    }

    // Constrain by promotion approver type
    if ( excludedPromotionApproverTypes != null )
    {
      criteria.createAlias( "claimGroup.promotion", "promotion" ).add( Restrictions.not( Restrictions.in( "promotion.approverType", excludedPromotionApproverTypes ) ) );
    }

    // Constrain by promotion approver type
    if ( includedPromotionApproverTypes != null )
    {
      criteria.createAlias( "claimGroup.promotion", "promotion" ).add( Restrictions.in( "promotion.approverType", includedPromotionApproverTypes ) );
    }

    // Constrain by Date
    if ( startDate != null )
    {
      // shift date to beginning of day - FUTURE: if exact time check is needed for start and end,
      // could add a boolean param to method to allow choosing between exact time check and "day of"
      // check.
      Date startDateBeginningOfDay = DateUtils.toStartDate( startDate );
      criteria.add( Restrictions.ge( "claimGroup.dateApproved", startDateBeginningOfDay ) );
    }

    if ( endDate != null )
    {
      // shift date to end of day
      Date endDateBeginningOfDay = DateUtils.toEndDate( endDate );
      criteria.add( Restrictions.le( "claimGroup.dateApproved", endDateBeginningOfDay ) );
    }

    // Constrain by open/close
    if ( open != null )
    {
      criteria.add( Restrictions.eq( "claimGroup.open", open ) );
    }

    if ( notificationDateExists != null )
    {
      if ( notificationDateExists.booleanValue() )
      {
        criteria.add( Restrictions.isNotNull( "claimGroup.notificationDate" ) );
      }
      else
      {
        criteria.add( Restrictions.isNull( "claimGroup.notificationDate" ) );
      }
    }

    if ( notificationDatePastDate != null )
    {
      criteria.add( Restrictions.le( "claimGroup.notificationDate", notificationDatePastDate ) );
    }

    // Constrain by anyActivitityUnposted
    if ( anyActivitityUnposted != null )
    {
      String postedCode = "1";
      if ( anyActivitityUnposted.booleanValue() )
      {
        postedCode = "0";
      }
      criteria
          .add( Restrictions.sqlRestriction( "" + "exists (select 1 from ACTIVITY activity " + "where {alias}.claim_group_id=activity.claim_group_id and activity.is_posted=" + postedCode + ")" ) );
    }

    return criteria;
  }

  /**
   * @return value of open property
   */
  public Boolean getOpen()
  {
    return open;
  }

  /**
   * @param open value for open property
   */
  public void setOpen( Boolean open )
  {
    this.open = open;
  }

  /**
   * @return value of includedPromotionIds property
   */
  public Long[] getIncludedPromotionIds()
  {
    return includedPromotionIds;
  }

  /**
   * @param includedPromotionIds value for includedPromotionIds property
   */
  public void setIncludedPromotionIds( Long[] includedPromotionIds )
  {
    this.includedPromotionIds = includedPromotionIds;
  }

  /**
   * @return value of excludedPromotionApproverTypes property
   */
  public ApproverType[] getExcludedPromotionApproverTypes()
  {
    return excludedPromotionApproverTypes;
  }

  /**
   * @param excludedPromotionApproverTypes value for excludedPromotionApproverTypes property
   */
  public void setExcludedPromotionApproverTypes( ApproverType[] excludedPromotionApproverTypes )
  {
    this.excludedPromotionApproverTypes = excludedPromotionApproverTypes;
  }

  /**
   * @return value of includedPromotionApproverTypes property
   */
  public ApproverType[] getIncludedPromotionApproverTypes()
  {
    return includedPromotionApproverTypes;
  }

  /**
   * @param includedPromotionApproverTypes value for includedPromotionApproverTypes property
   */
  public void setIncludedPromotionApproverTypes( ApproverType[] includedPromotionApproverTypes )
  {
    this.includedPromotionApproverTypes = includedPromotionApproverTypes;
  }

  public Long getParticipantId()
  {
    return participantId;
  }

  public void setParticipantId( Long participantId )
  {
    this.participantId = participantId;
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

  public Boolean getNotificationDateExists()
  {
    return notificationDateExists;
  }

  public void setNotificationDateExists( Boolean notificationDateExists )
  {
    this.notificationDateExists = notificationDateExists;
  }

  public Date getNotificationDatePastDate()
  {
    return notificationDatePastDate;
  }

  public void setNotificationDatePastDate( Date notificationDatePastDate )
  {
    this.notificationDatePastDate = notificationDatePastDate;
  }

  public Boolean getAnyActivitityUnposted()
  {
    return anyActivitityUnposted;
  }

  public void setAnyActivitityUnposted( Boolean anyActivitityUnposted )
  {
    this.anyActivitityUnposted = anyActivitityUnposted;
  }

}
