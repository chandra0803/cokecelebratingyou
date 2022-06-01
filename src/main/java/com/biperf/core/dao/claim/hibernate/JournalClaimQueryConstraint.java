/*
 * (c) 2006 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/dao/claim/hibernate/JournalClaimQueryConstraint.java,v $
 */

package com.biperf.core.dao.claim.hibernate;

import java.util.Date;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import com.biperf.core.dao.BaseQueryConstraint;
import com.biperf.core.domain.claim.NominationClaim;
import com.biperf.core.domain.enums.NominationAwardGroupSizeType;
import com.biperf.core.domain.enums.NominationAwardGroupType;
import com.biperf.core.domain.enums.NominationEvaluationType;

public class JournalClaimQueryConstraint extends BaseQueryConstraint
{
  // ---------------------------------------------------------------------------
  // Fields Methods
  // ---------------------------------------------------------------------------

  /**
   * Return claims approved on or before this date.
   */
  private Date approvalEndDate;

  /**
   * Return claims approved on or after this date.
   */
  private Date approvalStartDate;

  /**
   * Return claims for promotions with this award group type.
   */
  private NominationAwardGroupType awardGroupType;

  /**
   * Return claims associated with journals with this journal status type.
   */
  private String journalStatusType;

  /**
   * Return claims where this participant is a recipient.
   */
  private Long recipientId;

  /**
   * Return claims associated with this promotion.
   */
  private Long promotionId;

  private NominationAwardGroupSizeType awardGroupSizeType;
//---------------------------------------------------------------------------
 // Constructors
 // ---------------------------------------------------------------------------

 /**
  * Constructs a <code>JournalClaimQueryConstraint</code> object.
  *
  * @param awardGroupType  return claims for promotions with this award group
  *                        type.  Must not be null.
  */
 public JournalClaimQueryConstraint( NominationAwardGroupType awardGroupType )
 {
   if ( awardGroupType == null )
   {
     throw new IllegalArgumentException( "Argument \"awardGroupType\" is null.");
   }

   this.awardGroupType = awardGroupType;
 }
  /**
   * Constructs a <code>JournalClaimQueryConstraint</code> object.
   *
   * @param awardGroupType  return claims for promotions with this award group
   *                        type.  Must not be null.
   *                        
   * @param awardGroupSizeType return claims for promotions with this size type. May be null for individual group type only.
   */
  public JournalClaimQueryConstraint( NominationAwardGroupType awardGroupType, NominationAwardGroupSizeType awardGroupSizeType )
  {
    if ( awardGroupType == null )
    {
      throw new IllegalArgumentException( "Argument \"awardGroupType\" is null." );
    }

    // Size type can only be null for the individual group type, because well, then the size is
    // kinda just 1 person
    if ( awardGroupSizeType == null && !awardGroupType.isIndividual() )
    {
      throw new IllegalArgumentException( "Argument \"awardGroupSizeType\" is null and group type is not individual." );
    }

    this.awardGroupType = awardGroupType;

    this.awardGroupSizeType = awardGroupSizeType;
  }

  // ---------------------------------------------------------------------------
  // Query Methods
  // ---------------------------------------------------------------------------

  public Criteria buildCriteria()
  {
    CriteriaWrapper criteria = new CriteriaWrapper( super.buildCriteria() );

    criteria.setCommonCriteria();

    criteria.setAwardGroupCriteria();

    return criteria.getCriteria();
  }

  public Class getResultClass()
  {
    return NominationClaim.class;
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

  public NominationAwardGroupType getAwardGroupType()
  {
    return awardGroupType;
  }

  public void setAwardGroupType( NominationAwardGroupType awardGroupType )
  {
    this.awardGroupType = awardGroupType;
  }

  public String getJournalStatusType()
  {
    return journalStatusType;
  }

  public void setJournalStatusType( String journalStatusType )
  {
    this.journalStatusType = journalStatusType;
  }

  public Long getRecipientId()
  {
    return recipientId;
  }

  public void setRecipientId( Long recipientId )
  {
    this.recipientId = recipientId;
  }

  public Long getPromotionId()
  {
    return promotionId;
  }

  public void setPromotionId( Long promotionId )
  {
    this.promotionId = promotionId;
  }

  // ---------------------------------------------------------------------------
  // Inner Classes
  // ---------------------------------------------------------------------------

  private class CriteriaWrapper
  {
    // ------------------------------------------------------------------------
    // Fields
    // ------------------------------------------------------------------------

    private Criteria criteria;

    // ------------------------------------------------------------------------
    // Constructors
    // ------------------------------------------------------------------------

    /**
     * Constructs a <code>CriteriaWrapper</code> object.
     *
     * @param criteria  the <code>Criteria</code> object to which this object
     *                  adds behavior.
     */
    public CriteriaWrapper( Criteria criteria )
    {
      this.criteria = criteria;
    }

    // ------------------------------------------------------------------------
    // Configuration Methods
    // ------------------------------------------------------------------------

    /**
     * Specifies common criteria.
     */
    public void setCommonCriteria()
    {
      // Return claims for promotions whose evaluation type is "independent."
      StringBuffer buffer1 = new StringBuffer().append( "{alias}.claim_id in (" ).append( "select c.claim_id from claim c where c.promotion_id in (" )
          .append( "select pn.promotion_id from promo_nomination pn where pn.evaluation_type = '" ).append( NominationEvaluationType.INDEPENDENT ).append( "'))" );

      criteria.add( Restrictions.sqlRestriction( buffer1.toString() ) );

      // Return claims for the given promotion.
      if ( promotionId != null )
      {
        criteria.add( Restrictions.eq( "promotion.id", promotionId ) );
      }

      // Return claims approved on or after the given date.
      if ( approvalStartDate != null )
      {
        createAliasIfNotAlreadyCreated( criteria, "claimRecipients", "claimRecipient" ).add( Restrictions.ge( "claimRecipient.dateApproved", approvalStartDate ) );
      }

      // Return claims approved on or before the given date.
      if ( approvalEndDate != null )
      {
        createAliasIfNotAlreadyCreated( criteria, "claimRecipients", "claimRecipient" ).add( Restrictions.le( "claimRecipient.dateApproved", approvalEndDate ) );
      }

      // Return claims associated with journals that have the given status.
      StringBuffer buffer2 = new StringBuffer().append( "{alias}.claim_id in (select distinct a.claim_id from " )
          .append( "(journal j inner join activity_journal aj on j.journal_id = aj.journal_id) " ).append( "inner join activity a on aj.activity_id = a.activity_id " )
          .append( "where (a.claim_id is not null) " );

      if ( journalStatusType != null )
      {
        buffer2.append( "and (j.status_type = '" ).append( journalStatusType ).append( "')" );
      }

      buffer2.append( ")" );

      criteria.add( Restrictions.sqlRestriction( buffer2.toString() ) );

      // Return claims where the given user is the recipient.
      if ( recipientId != null )
      {
        createAliasIfNotAlreadyCreated( criteria, "claimRecipients", "claimRecipient" ).add( Restrictions.eq( "claimRecipient.recipient.id", recipientId ) );
      }
    }

    /**
     * Adds criteria for award group type and award group size type
     */
    public void setAwardGroupCriteria()
    {
      StringBuilder queryString = new StringBuilder().append( "{alias}.claim_id in (" ).append( "select c.claim_id from claim c where c.promotion_id in (" )
          .append( "select pn.promotion_id from promo_nomination pn where pn.award_group_type = '" ).append( awardGroupType.getCode() ).append( "'" );

      // For teams, add the team size constraint as well
      if ( awardGroupSizeType != null )
      {
        queryString.append( " and pn.award_group_size = '" ).append( awardGroupSizeType.getCode() ).append( "'" );
      }

      queryString.append( "))" );

      criteria.add( Restrictions.sqlRestriction( queryString.toString() ) );
    }

    // ------------------------------------------------------------------------
    // Getter and Setter Methods
    // ------------------------------------------------------------------------

    /**
     * Returns the wrapped criteria.
     *
     * @return the wrapped criteria.
     */
    public Criteria getCriteria()
    {
      return criteria;
    }
  }
}
