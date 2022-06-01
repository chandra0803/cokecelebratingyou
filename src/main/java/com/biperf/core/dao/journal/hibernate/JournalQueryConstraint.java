/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/dao/journal/hibernate/JournalQueryConstraint.java,v $
 */

package com.biperf.core.dao.journal.hibernate;

import java.util.Date;

import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.LogicalExpression;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.StandardBasicTypes;

import com.biperf.core.dao.BaseQueryConstraint;
import com.biperf.core.domain.enums.JournalStatusType;
import com.biperf.core.domain.enums.JournalTransactionType;
import com.biperf.core.domain.enums.PromotionAwardsType;
import com.biperf.core.domain.enums.PromotionPayoutType;
import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.domain.journal.Journal;
import com.biperf.core.utils.HibernateSessionManager;

/*
 * JournalQueryConstraint <p> <b>Change History:</b><br> <table border="1"> <tr> <th>Author</th>
 * <th>Date</th> <th>Version</th> <th>Comments</th> </tr> <tr> <td>Thomas Eaton</td> <td>Dec
 * 2, 2005</td> <td>1.0</td> <td>created</td> </tr> </table> </p>
 * 
 */

public class JournalQueryConstraint extends BaseQueryConstraint
{
  // ---------------------------------------------------------------------------
  // Fields
  // ---------------------------------------------------------------------------

  /**
   * The ID of the promotion whose journals will be fetched.
   */
  private Long promotionId;

  /**
   * The types of journals that will not be fetched.
   */
  private String promotionAwardType;

  private String promotionType;

  /**
   * The status types of journals that will be fetched.
   */
  private JournalStatusType[] journalStatusTypesIncluded;

  /**
   * The status types of journals that will not be fetched.
   */
  private JournalStatusType[] journalStatusTypesExcluded;

  /**
   * The types of journals that will be fetched.
   */
  private String[] journalTypesIncluded;

  /**
   * The types of transaction type that will be fetched.
   */
  private JournalTransactionType[] journalTransactionTypesIncluded;

  /**
   * The types of journals that will not be fetched.
   */
  private String[] journalTypesExcluded;

  private Long participantId;

  /**
   * The journals that do not have this string in their comments.
   */
  private String notInComments;

  /**
   * If true, get journals that are not associated with a claim or claim group.
   */
  private boolean notResultOfClaim;

  /**
   * If true, get journals that are not associated with an activity.
   */
  private boolean notResultOfActivity;

  /**
   * only include journals whose payoutCalculationAudit's promotionPayoutGroup ID matches
   * auditPromotionPayoutGroupID.
   */
  private Long auditPromotionPayoutGroupId;

  /**
   * Return journals for promotions with this payout type.
   */
  private PromotionPayoutType promotionPayoutType;

  /**
   * Return journals for promotions with transaction date after this start date.
   */
  private Date startDate;

  /**
   * Return journals for promotions with transaction date before this end date.
   */
  private Date endDate;

  private Boolean redeemed;

  /** 
   * Claim the activity is from
   */
  private Long claimId;

  /* Bug # 40160 start */
  private Boolean giftCode;

  public Boolean getGiftCode()
  {
    return giftCode;
  }

  public void setGiftCode( Boolean giftCode )
  {
    this.giftCode = giftCode;
  }
  /* Bug # 40160 end */

  // ---------------------------------------------------------------------------
  // Query Methods
  // ---------------------------------------------------------------------------

  /**
   * Builds a Hibernate {@link Criteria} object that represents the SQL query specified by this
   * <code>JournalQueryConstraint</code> object.
   *
   * @return a Hibernate {@link Criteria} object that represents the SQL query specified by this
   *         <code>JournalQueryConstraint</code> object.
   */
  public Criteria buildCriteria()
  {
    Criteria criteria = HibernateSessionManager.getSession().createCriteria( Journal.class, "journal" );

    // Promotion criterion
    if ( promotionId != null )
    {
      criteria.add( Restrictions.eq( "journal.promotion.id", promotionId ) );
    }

    if ( participantId != null )
    {
      criteria.add( Restrictions.eq( "journal.participant.id", participantId ) );
    }

    if ( promotionAwardType != null )
    {
      criteria.createAlias( "journal.promotion", "promotion" ).add( Restrictions.eq( "promotion.awardType", PromotionAwardsType.lookup( promotionAwardType ) ) );
    }

    if ( promotionType != null )
    {
      criteria.createAlias( "journal.promotion", "promotion" ).add( Restrictions.eq( "promotion.promotionType", PromotionType.lookup( promotionType ) ) );
    }

    // Returns journals not associated with a claim or claimGroup.
    if ( notResultOfClaim )
    {
      String sql = new StringBuffer().append( "{alias}.journal_id in " ).append( "(select journal2.journal_id " )
          .append( "from journal journal2 left outer join activity_journal on journal2.journal_id = activity_journal.journal_id " )
          .append( "left outer join activity on activity_journal.activity_id = activity.activity_id " ).append( "where activity.claim_id is null and activity.claim_group_id is null )" ).toString();
      criteria.add( Restrictions.sqlRestriction( sql ) );
    }

    // Returns journals not associated with an activity.
    if ( notResultOfActivity )
    {
      String sql = new StringBuffer().append( "{alias}.journal_id in " ).append( "(select aj.journal_id from activity_journal aj where aj.activity_id is null)" ).toString();
      criteria.add( Restrictions.sqlRestriction( sql ) );
    }

    // Journal status type criterion
    if ( journalStatusTypesIncluded != null && journalStatusTypesIncluded.length > 0 )
    {
      criteria.add( Restrictions.in( "journal.journalStatusType", journalStatusTypesIncluded ) );
    }

    if ( journalStatusTypesExcluded != null && journalStatusTypesExcluded.length > 0 )
    {
      Criterion criterion = Restrictions.not( Restrictions.in( "journal.journalStatusType", journalStatusTypesExcluded ) );
      criteria.add( criterion );
    }

    // Journal type criterion
    if ( journalTypesIncluded != null && journalTypesIncluded.length > 0 )
    {
      criteria.add( Restrictions.in( "journal.journalType", journalTypesIncluded ) );
    }

    // Transaction Type criterion
    if ( journalTransactionTypesIncluded != null && journalTransactionTypesIncluded.length > 0 )
    {
      criteria.add( Restrictions.in( "journal.transactionType", journalTransactionTypesIncluded ) );
    }

    if ( auditPromotionPayoutGroupId != null )
    {
      Criterion criterion = Restrictions.sqlRestriction( "{alias}.journal_id in " + "(select journal_id from PAYOUT_CALCULATION_AUDIT where PROMO_PAYOUT_GROUP_ID = ?) ",
                                                         auditPromotionPayoutGroupId,
                                                         StandardBasicTypes.LONG );
      criteria.add( criterion );
    }

    if ( journalTypesExcluded != null && journalTypesExcluded.length > 0 )
    {
      Criterion criterion = Restrictions.not( Restrictions.in( "journal.journalType", journalTypesExcluded ) );
      criteria.add( criterion );
    }

    if ( notInComments != null && notInComments.length() > 0 )
    {
      Criterion lhs = Restrictions.isNull( "journal.comments" );
      Criterion rhs = Restrictions.ne( "journal.comments", notInComments );
      LogicalExpression logicalExpression = Restrictions.or( lhs, rhs );
      criteria.add( logicalExpression );
    }

    if ( promotionPayoutType != null )
    {
      String sql = new StringBuffer().append( "{alias}.journal_id in " ).append( "(select j.journal_id " )
          .append( "from journal j inner join promo_product_claim ppc on j.promotion_id = ppc.promotion_id " ).append( "where ppc.payout_type = ?)" ).toString();
      Criterion criterion = Restrictions.sqlRestriction( sql, promotionPayoutType.getCode(), StandardBasicTypes.STRING );
      criteria.add( criterion );
    }

    if ( startDate != null )
    {
      criteria.add( Restrictions.ge( "journal.transactionDate", startDate ) );
    }

    if ( endDate != null )
    {
      criteria.add( Restrictions.le( "journal.transactionDate", endDate ) );
    }

    if ( redeemed != null )
    {
      criteria.add( Restrictions.eq( "journal.redeemed", redeemed ) );
    }

    if ( claimId != null )
    {
      String sql = new StringBuilder().append( "{alias}.journal_id in " ).append( "(select aj.journal_id " )
          .append( "from activity_journal aj inner join activity a on aj.activity_id = a.activity_id " ).append( "where a.claim_id = ?)" ).toString();
      Criterion criterion = Restrictions.sqlRestriction( sql, claimId, StandardBasicTypes.LONG );
      criteria.add( criterion );
    }

    /* Bug # 40160 start */
    if ( giftCode != null && giftCode.booleanValue() )
    {
      criteria.add( Restrictions.ne( "journal.giftCode", "{AES}" ) );
    }
    /* Bug # 40160 end */

    return criteria;
  }

  /**
   * Returns the class of the objects returned by the query specified by this query constraint.
   *
   * @return the class of the objects returned by the query specified by this query constraint.
   */
  public Class getResultClass()
  {
    return Journal.class;
  }

  // ---------------------------------------------------------------------------
  // Getter and Setter Methods
  // ---------------------------------------------------------------------------

  public Long getAuditPromotionPayoutGroupId()
  {
    return auditPromotionPayoutGroupId;
  }

  public void setAuditPromotionPayoutGroupId( Long auditPromotionPayoutGroupId )
  {
    this.auditPromotionPayoutGroupId = auditPromotionPayoutGroupId;
  }

  public JournalStatusType[] getJournalStatusTypesExcluded()
  {
    return journalStatusTypesExcluded;
  }

  public void setJournalStatusTypesExcluded( JournalStatusType[] journalStatusTypesExcluded )
  {
    this.journalStatusTypesExcluded = journalStatusTypesExcluded;
  }

  public JournalStatusType[] getJournalStatusTypesIncluded()
  {
    return journalStatusTypesIncluded;
  }

  public void setJournalStatusTypesIncluded( JournalStatusType[] journalStatusTypesIncluded )
  {
    this.journalStatusTypesIncluded = journalStatusTypesIncluded;
  }

  public JournalTransactionType[] getJournalTransactionTypesIncluded()
  {
    return journalTransactionTypesIncluded;
  }

  public void setJournalTransactionTypesIncluded( JournalTransactionType[] journalTransactionTypesIncluded )
  {
    this.journalTransactionTypesIncluded = journalTransactionTypesIncluded;
  }

  public String[] getJournalTypesExcluded()
  {
    return journalTypesExcluded;
  }

  public void setJournalTypesExcluded( String[] journalTypesExcluded )
  {
    this.journalTypesExcluded = journalTypesExcluded;
  }

  public String[] getJournalTypesIncluded()
  {
    return journalTypesIncluded;
  }

  public void setJournalTypesIncluded( String[] journalTypesIncluded )
  {
    this.journalTypesIncluded = journalTypesIncluded;
  }

  public String getNotInComments()
  {
    return notInComments;
  }

  public void setNotInComments( String notInComments )
  {
    this.notInComments = notInComments;
  }

  public boolean isNotResultOfActivity()
  {
    return notResultOfActivity;
  }

  public void setNotResultOfActivity( boolean notResultOfActivity )
  {
    this.notResultOfActivity = notResultOfActivity;
  }

  public boolean isNotResultOfClaim()
  {
    return notResultOfClaim;
  }

  public void setNotResultOfClaim( boolean notResultOfClaim )
  {
    this.notResultOfClaim = notResultOfClaim;
  }

  public Long getParticipantId()
  {
    return participantId;
  }

  public void setParticipantId( Long participantId )
  {
    this.participantId = participantId;
  }

  public String getPromotionAwardType()
  {
    return promotionAwardType;
  }

  public void setPromotionAwardType( String promotionAwardType )
  {
    this.promotionAwardType = promotionAwardType;
  }

  public Long getPromotionId()
  {
    return promotionId;
  }

  public void setPromotionId( Long promotionId )
  {
    this.promotionId = promotionId;
  }

  public PromotionPayoutType getPromotionPayoutType()
  {
    return promotionPayoutType;
  }

  public void setPromotionPayoutType( PromotionPayoutType promotionPayoutType )
  {
    this.promotionPayoutType = promotionPayoutType;
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

  public Boolean getRedeemed()
  {
    return redeemed;
  }

  public void setRedeemed( Boolean redeemed )
  {
    this.redeemed = redeemed;
  }

  public String getPromotionType()
  {
    return promotionType;
  }

  public void setPromotionType( String promotionType )
  {
    this.promotionType = promotionType;
  }

  public Long getClaimId()
  {
    return claimId;
  }

  public void setClaimId( Long claimId )
  {
    this.claimId = claimId;
  }

}
