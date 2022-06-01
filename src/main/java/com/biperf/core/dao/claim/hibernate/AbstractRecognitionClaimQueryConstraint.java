/*
 * (c) 2006 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/dao/claim/hibernate/AbstractRecognitionClaimQueryConstraint.java,v $
 */

package com.biperf.core.dao.claim.hibernate;

import java.util.Date;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

public class AbstractRecognitionClaimQueryConstraint extends ClaimQueryConstraint
{
  // ---------------------------------------------------------------------------
  // Fields Methods
  // ---------------------------------------------------------------------------

  /**
   * Identifies a nominee who is a claim recipient for the selected nomination
   * claims.
   */
  private Long recipientId;

  /**
   * If true, include claims whose claimRecipient's notification Date is non-null; if false, those
   * that are null.
   */
  private Boolean recipientNotificationDateExists;

  /**
   * include only claims whose claimRecipient's notification Date is past (or equal to ) the
   * notificationDatePastDate.
   */
  private Date recipientNotificationDatePastDate;

  /**
   * if populated, claims which were submitted for the given team id
   */
  private Long teamId;

  /**
   * will exclude claims with points
   */
  private Boolean excludeClaimsWithPoints;

  // ---------------------------------------------------------------------------
  // Query Methods
  // ---------------------------------------------------------------------------

  public Criteria buildCriteria()
  {
    Criteria criteria = super.buildCriteria();

    if ( recipientId != null )
    {
      criteria.createAlias( "claim.claimRecipients", "claimRecipient" ).add( Restrictions.eq( "claimRecipient.recipient.id", recipientId ) );
      if ( excludeClaimsWithPoints != null && excludeClaimsWithPoints.booleanValue() )
      {
        criteria.add( Restrictions.or( Restrictions.isNull( "claimRecipient.awardQuantity" ), Restrictions.eq( "claimRecipient.awardQuantity", new Long( 0 ) ) ) );
      }
    }

    if ( recipientNotificationDateExists != null )
    {
      if ( recipientNotificationDateExists.booleanValue() )
      {
        createAliasIfNotAlreadyCreated( criteria, "claim.claimRecipients", "claimRecipient" ).add( Restrictions.isNotNull( "claimRecipient.notificationDate" ) );
      }
      else
      {
        createAliasIfNotAlreadyCreated( criteria, "claim.claimRecipients", "claimRecipient" ).add( Restrictions.isNull( "claimRecipient.notificationDate" ) );
      }
    }

    if ( recipientNotificationDatePastDate != null )
    {
      createAliasIfNotAlreadyCreated( criteria, "claim.claimRecipients", "claimRecipient" ).add( Restrictions.le( "claimRecipient.notificationDate", recipientNotificationDatePastDate ) );
    }

    if ( teamId != null )
    {
      criteria.add( Restrictions.eq( "teamId", teamId ) );
    }

    return criteria;
  }

  // ---------------------------------------------------------------------------
  // Getter and Setter Methods
  // ---------------------------------------------------------------------------

  public Long getRecipientId()
  {
    return recipientId;
  }

  public Boolean getExcludeClaimsWithPoints()
  {
    return excludeClaimsWithPoints;
  }

  public void setExcludeClaimsWithPoints( Boolean excludeClaimsWithPoints )
  {
    this.excludeClaimsWithPoints = excludeClaimsWithPoints;
  }

  public void setRecipientId( Long recipientId )
  {
    this.recipientId = recipientId;
  }

  public Boolean getRecipientNotificationDateExists()
  {
    return recipientNotificationDateExists;
  }

  public void setRecipientNotificationDateExists( Boolean recipientNotificationDateExists )
  {
    this.recipientNotificationDateExists = recipientNotificationDateExists;
  }

  public Date getRecipientNotificationDatePastDate()
  {
    return recipientNotificationDatePastDate;
  }

  public void setRecipientNotificationDatePastDate( Date recipientNotificationDatePastDate )
  {
    this.recipientNotificationDatePastDate = recipientNotificationDatePastDate;
  }

  public Long getTeamId()
  {
    return teamId;
  }

  public void setTeamId( Long teamId )
  {
    this.teamId = teamId;
  }

}
