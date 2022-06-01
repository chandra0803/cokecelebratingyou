/*
 * (c) 2006 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/claim/ProductClaimValueObject.java,v $
 */

package com.biperf.core.ui.claim;

import java.util.Date;

import com.biperf.core.domain.claim.Claim;
import com.biperf.core.domain.enums.PromotionAwardsType;
import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.user.User;
import com.biperf.core.utils.DateUtils;

public class ProductClaimValueObject
{
  // ---------------------------------------------------------------------------
  // Fields
  // ---------------------------------------------------------------------------

  private String timeZoneID;

  /**
   * True if this value object is associated with a claim; false if it is
   * associated with a non-claim-based award.
   */
  private boolean claimBased;

  /**
   * If this value object is associated with a claim, then this field contains
   * the claim ID.  If this value object is associated with a non-claim-based
   * award, then this field is null.
   */
  private Long claimId;

  /**
   * If this value object is associated with a claim, then this field contains
   * the claim number.  If this value object is associated with a non-claim-based
   * award, then this field contains a description of the award type.
   */
  private String claimNumber;

  /**
   * The name of the promotion associated with the claim or non-claim-based award.
   */
  private String promotionName;

  /**
   * If this value object is associated with a claim, then this field identifies
   * the node associated with the claim.  If this value object is associated
   * with a non-claim-based award, then this field is null.
   */
  private Node node;

  /**
   * If this value object is associated with a claim, then this field contains
   * the date on which the claim was submitted.  If this value object is associated
   * with a non-claim-based award, then this field contains the date on which the
   * award was paid out.
   */
  private Date dateSubmitted;

  /**
   * If this value object is associated with a claim, then this field identifies
   * the participant who submitted the claim.  If this value object is associated
   * with a non-claim-based award, then this field is null.
   */
  private Participant submitter;

  /**
   * If this value object is associated with a claim, then this field identifies
   * the user who submitted this claim on behalf of another user.  If this value
   * object is associated with a non-claim-based award, then this field is null.
   */
  private User proxyUser;

  /**
   * If this value object is associated with a claim, then this field is true if
   * the claim can be deleted and false if it cannot.  If this value object is
   * associated with a non-claim-based award, then this field is null.
   */
  private Boolean deletable;

  /**
   * If this value object is associated with a claim, then this field contains
   * the name of the company to which the participant sold the products associated
   * with this claim.  If this value object is associated with a non-claim-based
   * award, then this field is null.
   */
  private String companyName;

  /**
   * If this value object is associated with a claim, then this field contains
   * the number of the current approval round.  If this value object is associated
   * with a non-claim-based award, then this field is null.
   */
  private Long approvalRound;

  /**
   * The amount that the participant earned from this claim or non-claim-based
   * award.
   */
  private Long earnings;

  private Long journalId;

  private String regSymbol = "&#174;";

  public Long getJournalId()
  {
    return journalId;
  }

  public void setJournalId( Long journalId )
  {
    this.journalId = journalId;
  }

  /**
   * The award type. e.g. Points. Used when displaying recognitions received.
   */
  private String awardTypeName;

  /**
   * True if this value object represents a non-claim award that is a result
   * of a Discretionary Award; false otherwise.
   */
  private boolean isDiscretionary;

  private boolean isSweepstakes;

  private boolean isManagerOverride;

  private boolean isStackRank;

  private String reversalDescription = null;

  private Long promotionId;

  // ---------------------------------------------------------------------------
  // Constructors
  // ---------------------------------------------------------------------------

  public Long getPromotionId()
  {
    return promotionId;
  }

  public void setPromotionId( Long promotionId )
  {
    this.promotionId = promotionId;
  }

  /**
   * Constructs a <code>ProductClaimValueObject</code> object.
   */
  public ProductClaimValueObject()
  {
    // empty constructor.
  }

  /**
   * Constructs a <code>ProductClaimValueObject</code> object.
   *
   * @param claim  used to initialize the state of this value object.
   */
  public ProductClaimValueObject( Claim claim )
  {
    claimBased = true;

    claimId = claim.getId();
    claimNumber = claim.getClaimNumber();
    approvalRound = claim.getApprovalRound();
    companyName = claim.getCompanyName();
    dateSubmitted = claim.getAuditCreateInfo().getDateCreated();
    deletable = new Boolean( claim.isDeletable() );
    earnings = claim.getEarnings();
    awardTypeName = PromotionAwardsType.lookup( claim.getPromotion().getAwardType().getCode() ).getName();
    node = claim.getNode();
    promotionName = claim.getPromotion().getName();
    promotionId = claim.getPromotion().getId();
    submitter = claim.getSubmitter();
    timeZoneID = claim.getTimeZoneID();
    proxyUser = claim.getProxyUser();

  }

  // ---------------------------------------------------------------------------
  // Getter and Setter Methods
  // ---------------------------------------------------------------------------

  public Long getApprovalRound()
  {
    return approvalRound;
  }

  public void setApprovalRound( Long approvalRound )
  {
    this.approvalRound = approvalRound;
  }

  public boolean isClaimBased()
  {
    return claimBased;
  }

  public void setClaimBased( boolean claimBased )
  {
    this.claimBased = claimBased;
  }

  public Long getClaimId()
  {
    return claimId;
  }

  public void setClaimId( Long claimId )
  {
    this.claimId = claimId;
  }

  public String getClaimNumber()
  {
    return claimNumber;
  }

  public void setClaimNumber( String claimNumber )
  {
    this.claimNumber = claimNumber;
  }

  public String getCompanyName()
  {
    return companyName;
  }

  public void setCompanyName( String companyName )
  {
    this.companyName = companyName;
  }

  public Date getDateSubmitted()
  {
    return dateSubmitted;
  }

  public void setDateSubmitted( Date dateSubmitted )
  {
    this.dateSubmitted = dateSubmitted;
  }

  public String getDisplayDateSubmitted()
  {
    if ( this.dateSubmitted != null )
    {
      return DateUtils.toDisplayString( this.dateSubmitted );
    }
    else
    {
      return "";
    }
  }

  public Boolean getDeletable()
  {
    return deletable;
  }

  public void setDeletable( Boolean deletable )
  {
    this.deletable = deletable;
  }

  public Long getEarnings()
  {
    return earnings;
  }

  public void setEarnings( Long earnings )
  {
    this.earnings = earnings;
  }

  public Node getNode()
  {
    return node;
  }

  public void setNode( Node node )
  {
    this.node = node;
  }

  public String getPromotionName()
  {
    return promotionName;
  }

  public void setPromotionName( String promotionName )
  {
    this.promotionName = promotionName;
  }

  public User getProxyUser()
  {
    return proxyUser;
  }

  public void setProxyUser( User proxyUser )
  {
    this.proxyUser = proxyUser;
  }

  public Participant getSubmitter()
  {
    return submitter;
  }

  public void setSubmitterId( Participant submitter )
  {
    this.submitter = submitter;
  }

  public boolean isDiscretionary()
  {
    return isDiscretionary;
  }

  public void setDiscretionary( boolean isDiscretionary )
  {
    this.isDiscretionary = isDiscretionary;
  }

  public boolean isSweepstakes()
  {
    return isSweepstakes;
  }

  public void setSweepstakes( boolean isSweepstakes )
  {
    this.isSweepstakes = isSweepstakes;
  }

  public boolean isManagerOverride()
  {
    return isManagerOverride;
  }

  public void setManagerOverride( boolean isManagerOverride )
  {
    this.isManagerOverride = isManagerOverride;
  }

  public boolean isStackRank()
  {
    return isStackRank;
  }

  public void setStackRank( boolean isStackRank )
  {
    this.isStackRank = isStackRank;
  }

  public String getAwardTypeName()
  {
    return awardTypeName;
  }

  public void setAwardTypeName( String awardTypeName )
  {
    this.awardTypeName = awardTypeName;
  }

  public String getReversalDescription()
  {
    return reversalDescription;
  }

  public void setReversalDescription( String reversalDescription )
  {
    this.reversalDescription = reversalDescription;
  }

  public String getRegSymbol()
  {
    return regSymbol;
  }

  public void setRegSymbol( String regSymbol )
  {
    this.regSymbol = regSymbol;
  }

  public String getTimeZoneID()
  {
    return timeZoneID;
  }

  public void setTimeZoneID( String timeZoneID )
  {
    this.timeZoneID = timeZoneID;
  }

}
