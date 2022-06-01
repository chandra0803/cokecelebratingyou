/*
 * (c) 2006 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/dao/claim/hibernate/JournalClaimGroupQueryConstraint.java,v $
 */

package com.biperf.core.dao.claim.hibernate;

import java.util.Date;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import com.biperf.core.dao.BaseQueryConstraint;
import com.biperf.core.domain.claim.ClaimGroup;

public class JournalClaimGroupQueryConstraint extends BaseQueryConstraint
{
  // ---------------------------------------------------------------------------
  // Fields Methods
  // ---------------------------------------------------------------------------

  /**
   * Return claim groups approved on or before this date.
   */
  private Date approvalEndDate;

  /**
   * Return claim groups approved on or after this date.
   */
  private Date approvalStartDate;

  /**
   * Return claim groups associated with journals with this journal status type.
   */
  private String journalStatusType;

  /**
   * Return claim groups where this participant is a participantId.
   */
  private Long participantId;

  /**
   * Return claim groups associated with this promotion.
   */
  private Long promotionId;

  private Boolean isAwardActive;

  // ---------------------------------------------------------------------------
  // Query Methods
  // ---------------------------------------------------------------------------

  public Criteria buildCriteria()
  {
    Criteria criteria = super.buildCriteria();

    /*
     * SELECT cg.* FROM claim_group cg WHERE (cg.promotion_id = :promotionId) AND (cg.participant_id
     * = :participantId) AND (cg.date_approved >= :approvalStartDate) AND (cg.date_approved <=
     * :approvalEndDate) AND cg.claim_group_id IN ( SELECT DISTINCT a.claim_group_id FROM (journal j
     * INNER JOIN activity_journal aj ON j.journal_id = aj.journal_id) INNER JOIN activity a ON
     * aj.activity_id = a.activity_id WHERE (j.status_type = :journalStatusType) AND
     * (a.claim_group_id IS NOT NULL) )
     */

    if ( promotionId != null )
    {
      criteria.add( Restrictions.eq( "promotion.id", promotionId ) );
    }

    if ( participantId != null )
    {
      criteria.add( Restrictions.eq( "participant.id", participantId ) );
    }

    if ( approvalStartDate != null )
    {
      criteria.add( Restrictions.ge( "dateApproved", approvalStartDate ) );
    }

    if ( approvalEndDate != null )
    {
      criteria.add( Restrictions.le( "dateApproved", approvalEndDate ) );
    }

    if ( isAwardActive != null && isAwardActive )
    {
      StringBuffer subqueryString = new StringBuffer().append( "{alias}.claim_group_id in (select distinct a.claim_group_id from " )
          .append( "(journal j inner join activity_journal aj on j.journal_id = aj.journal_id) " ).append( "inner join activity a on aj.activity_id = a.activity_id " )
          .append( "where (a.claim_group_id is not null) " );

      if ( journalStatusType != null )
      {
        subqueryString.append( "and (j.status_type = '" ).append( journalStatusType ).append( "')" );
      }

      subqueryString.append( ")" );

      criteria.add( Restrictions.sqlRestriction( subqueryString.toString() ) );
    }

    return criteria;
  }

  public Class getResultClass()
  {
    return ClaimGroup.class;
  }

  // ---------------------------------------------------------------------------
  // Getter and Setter Methods
  // ---------------------------------------------------------------------------

  public Date getApprovalEndDate()
  {
    return approvalEndDate;
  }

  public void setApprovalEndDate( Date approvalEndDate )
  {
    this.approvalEndDate = approvalEndDate;
  }

  public Date getApprovalStartDate()
  {
    return approvalStartDate;
  }

  public void setApprovalStartDate( Date approvalStartDate )
  {
    this.approvalStartDate = approvalStartDate;
  }

  public String getJournalStatusType()
  {
    return journalStatusType;
  }

  public void setJournalStatusType( String journalStatusType )
  {
    this.journalStatusType = journalStatusType;
  }

  public Long getParticipantId()
  {
    return participantId;
  }

  public void setParticipantId( Long participantId )
  {
    this.participantId = participantId;
  }

  public Long getPromotionId()
  {
    return promotionId;
  }

  public void setPromotionId( Long promotionId )
  {
    this.promotionId = promotionId;
  }

  public void setAwardActive( Boolean isAwardActive )
  {
    this.isAwardActive = isAwardActive;
  }
}
