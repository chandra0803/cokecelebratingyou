/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.dao.activity.hibernate;

import java.util.Date;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import com.biperf.core.dao.BaseQueryConstraint;
import com.biperf.core.domain.activity.Activity;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.HibernateSessionManager;

/**
 * .
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
public class ActivityQueryConstraint extends BaseQueryConstraint
{

  /**
   * Constrain by posted status. Only posted if true, only unposted if false, don't constrain (i.e.,
   * both) if null.
   */
  private Boolean posted;

  /**
   * Constrain by participant id.
   */
  private Long participantId;

  /**
   * Constrain by promotion id.
   */
  private Long promotionId;

  /**
   * Constrain by claim id.
   */
  private Long claimId;

  /**
   * include only activities created on or after the beginning of day of startDate.
   */
  private Date startDate;

  /**
   * include only activities created on or before end of day of startDate.
   */
  private Date endDate;

  /**
   * Return the result object type - Should be overridden by subclasses.
   */
  public Class getResultClass()
  {
    return Activity.class;
  }

  public Criteria buildCriteria()
  {
    Criteria criteria = HibernateSessionManager.getSession().createCriteria( getResultClass(), "activity" );

    // Constrain by pax
    if ( participantId != null && participantId.longValue() > 0 )
    {
      criteria.add( Restrictions.eq( "activity.participant.id", participantId ) );
    }

    // Constrain by promo
    if ( promotionId != null )
    {
      criteria.add( Restrictions.eq( "activity.promotion.id", promotionId ) );
    }

    // Constrain by claim
    if ( claimId != null )
    {
      criteria.add( Restrictions.eq( "activity.claim.id", claimId ) );
    }

    // Constrain by Date
    if ( startDate != null )
    {
      // shift date to beginning of day - FUTURE: refactor if exact time check is needed for start
      // and end,
      // could add a boolean param to method to allow choosing between exact time check and "day of"
      // check.
      Date startDateBeginningOfDay = DateUtils.toStartDate( startDate );
      criteria.add( Restrictions.ge( "activity.submissionDate", startDateBeginningOfDay ) );
    }

    if ( endDate != null )
    {
      // shift date to end of day
      Date endDateBeginningOfDay = DateUtils.toEndDate( endDate );
      criteria.add( Restrictions.le( "activity.submissionDate", endDateBeginningOfDay ) );
    }

    // Constrain by posted/unposted
    if ( posted != null )
    {
      criteria.add( Restrictions.eq( "activity.posted", posted ) );
    }

    return criteria;
  }

  /**
   * @return value of claimId property
   */
  public Long getClaimId()
  {
    return claimId;
  }

  /**
   * @param claimId value for claimId property
   */
  public void setClaimId( Long claimId )
  {
    this.claimId = claimId;
  }

  /**
   * @return value of endDate property
   */
  public Date getEndDate()
  {
    return endDate;
  }

  /**
   * @param endDate value for endDate property
   */
  public void setEndDate( Date endDate )
  {
    this.endDate = endDate;
  }

  /**
   * @return value of participantId property
   */
  public Long getParticipantId()
  {
    return participantId;
  }

  /**
   * @param participantId value for participantId property
   */
  public void setParticipantId( Long participantId )
  {
    this.participantId = participantId;
  }

  /**
   * @return value of posted property
   */
  public Boolean getPosted()
  {
    return posted;
  }

  /**
   * @param posted value for posted property
   */
  public void setPosted( Boolean posted )
  {
    this.posted = posted;
  }

  /**
   * @return value of promotionId property
   */
  public Long getPromotionId()
  {
    return promotionId;
  }

  /**
   * @param promotionId value for promotionId property
   */
  public void setPromotionId( Long promotionId )
  {
    this.promotionId = promotionId;
  }

  /**
   * @return value of startDate property
   */
  public Date getStartDate()
  {
    return startDate;
  }

  /**
   * @param startDate value for startDate property
   */
  public void setStartDate( Date startDate )
  {
    this.startDate = startDate;
  }

}
