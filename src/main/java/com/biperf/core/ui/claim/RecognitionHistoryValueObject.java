/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/claim/RecognitionHistoryValueObject.java,v $
 */

package com.biperf.core.ui.claim;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.biperf.core.domain.activity.NominationActivity;
import com.biperf.core.domain.claim.ApprovableItemApprover;
import com.biperf.core.domain.claim.Claim;
import com.biperf.core.domain.claim.ClaimGroup;
import com.biperf.core.domain.claim.ClaimRecipient;
import com.biperf.core.domain.claim.NominationClaim;
import com.biperf.core.domain.claim.ProductClaimParticipant;
import com.biperf.core.domain.claim.RecognitionClaim;
import com.biperf.core.domain.enums.ApprovalStatusType;
import com.biperf.core.domain.enums.JournalTransactionType;
import com.biperf.core.domain.enums.NominationAwardGroupSizeType;
import com.biperf.core.domain.enums.NominationAwardGroupType;
import com.biperf.core.domain.enums.PromotionAwardsType;
import com.biperf.core.domain.journal.Journal;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.NominationPromotion;
import com.biperf.core.domain.promotion.NominationPromotionLevel;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.user.User;
import com.biperf.core.service.activity.ActivityService;
import com.biperf.core.service.cashcurrency.CashCurrencyService;
import com.biperf.core.service.claim.ClaimGroupService;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.UserManager;

/**
 * RecognitionHistoryValueObject.
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
 * <td>zahler</td>
 * <td>Jan 20, 2006</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 *
 *
 */
public class RecognitionHistoryValueObject implements Serializable
{
  // ---------------------------------------------------------------------------
  // Fields
  // ---------------------------------------------------------------------------

  // Properties used when displaying nomination claims

  /**
   * The nomination award group type: individual or team.
   */
  private NominationAwardGroupType awardGroupType;

  /**
   * The claims grouped by a claim group.  Used when displaying nominations won
   * for a promotion whose evaluation type is "cumulative."  A <code>Set</code>
   * of {@link NominationClaim} objects.
   */
  private Set claims;

  /**
   * The members of a team that won a nomination promotion.  Used when displaying
   * nominations submitted.  A <code>Set</code> of {@link ClaimRecipient}
   * objects.
   */
  private Set teamMembers;

  // Properties used when displaying recognition claims

  /**
   * The award amount.  Used when displaying recognitions received.
   */
  private Long awardQuantity;

  /**
   * The cash award amount.  Used when displaying nominations received.
   */
  private BigDecimal cashAwardQuantity;

  /**
   * The payout description.  Used when displaying nominations received.
   */
  private String payoutDescription;

  /**
   * The award type. e.g. points. Used when displaying recognitions received.
   */
  private String awardTypeName;

  private String awardTypeCode;

  private Long approvalRound;

  /**
   * The approval status.  Used when displaying recognitions sent.
   */
  private ApprovalStatusType approvalStatusType;

  // Properties used when displaying nomination claims or recognition claims

  private Promotion promotion;
  private Claim claim;
  private ClaimGroup claimGroup;
  private ClaimRecipient claimRecipient;
  private Timestamp submissionDate;
  private Participant submitter;
  private User proxyUser;
  private String currencyCode;

  private Participant recipient; // TODO: Remove this property? Is it redundant? (See
                                 // claimRecipient.)
  private Date approvalDate;
  private Long journalId;
  private boolean isDiscretionary;
  private boolean isSweepstakes;
  private String reversalDescription = null;

  private List merchGiftCodeActivityList = null;

  private boolean isReversal = false;

  private String regSymbol = "&#174;";

  private String submitterComments = "";

  private String formatSubmissionDate;

  private String levelName;

  private Boolean isBadgePromotion = false;

  // ---------------------------------------------------------------------------
  // Constructors
  // ---------------------------------------------------------------------------

  public RecognitionHistoryValueObject()
  {
    // default constructor
  }

  /**
   * Constructs a <code>RecognitionHistoryValueObject</code> object from a
   * {@link ClaimGroup} object.
   *
   * @param claimGroup  the {@link ClaimGroup} object from which this object is
   *                    constructed.
   */
  public RecognitionHistoryValueObject( ClaimGroup claimGroup )
  {
    NominationPromotion nominationPromotion = (NominationPromotion)claimGroup.getPromotion();
    setPromotion( nominationPromotion );
    setApprovalDate( claimGroup.getDateApproved() );
    setClaimGroup( claimGroup );
    setClaims( claimGroup.getClaims() );
    for ( NominationPromotionLevel nominationPromotionLevel : nominationPromotion.getNominationLevels() )
    {
      if ( claimGroup.getApprovalRound() != null && claimGroup.getApprovalRound().equals( nominationPromotionLevel.getLevelIndex() ) )
      {
        if ( nominationPromotionLevel.getAwardPayoutType().getCode().equals( PromotionAwardsType.POINTS ) )
        {
          setAwardQuantity( claimGroup.getAwardQuantity() );
          Journal journal = getClaimGroupService().getJournalForClaimGroup( claimGroup.getId() );
          if ( journal != null )
          {
            setJournalId( journal.getId() );
          }
        }
        else if ( nominationPromotionLevel.getAwardPayoutType().getCode().equals( PromotionAwardsType.CASH ) )
        {
          BigDecimal convertedPaxCurrency = getCashCurrencyService().convertCurrency( "USD", UserManager.getUserPrimaryCountryCurrencyCode(), claimGroup.getCashAwardQuantity(), null );
          setCurrencyCode( UserManager.getUserPrimaryCountryCurrencyCode() );
          setCashAwardQuantity( convertedPaxCurrency );
        }
        else if ( nominationPromotionLevel.getAwardPayoutType().getCode().equals( PromotionAwardsType.OTHER ) )
        {
          setPayoutDescription( nominationPromotionLevel.getPayoutDescription() );
        }
        setAwardTypeCode( nominationPromotionLevel.getAwardPayoutType().getCode() );
        setAwardTypeName( PromotionAwardsType.lookup( nominationPromotionLevel.getAwardPayoutType().getCode() ).getName() );
        break;
      }
    }
    setRecipient( claimGroup.getParticipant() );
  }

  /**
   * Constructs a <code>RecognitionHistoryValueObject</code> object from a
   * {@link ClaimRecipient} object.
   *
   * @param claimRecipient  the {@link ClaimRecipient} object from which this
   *                        object is constructed.
   */
  public RecognitionHistoryValueObject( ClaimRecipient claimRecipient )
  {
	    setPromotion( claimRecipient.getClaim().getPromotion() );
	    setClaim( claimRecipient.getClaim() );

	    setSubmitter( claimRecipient.getClaim().getSubmitter() );
	    setProxyUser( claimRecipient.getClaim().getProxyUser() );
	    setSubmissionDate( new Timestamp( claimRecipient.getClaim().getSubmissionDate().getTime() ) );

	    setClaimRecipient( claimRecipient );
	    setRecipient( claimRecipient.getRecipient() );
	    setApprovalStatusType( claimRecipient.getApprovalStatusType() );
	    setAwardQuantity( claimRecipient.getAwardQuantity() );
	    setAwardTypeName( PromotionAwardsType.lookup( claimRecipient.getClaim().getPromotion().getAwardType().getCode() ).getName() );
	    setSubmitterComments( ( (RecognitionClaim)claimRecipient.getClaim() ).getSubmitterComments() );
	    // TODO: for spotlight, add level name if merch level, or add item name if merch item
	  }

  public RecognitionHistoryValueObject( NominationClaim claim )
  {
    setClaim( claim );

    setPromotion( claim.getPromotion() );

    if ( claim.getPromotion().getAwardType() != null )
    {
      setAwardTypeName( PromotionAwardsType.lookup( claim.getPromotion().getAwardType().getCode() ).getName() );
    }

    setSubmitter( claim.getSubmitter() );
    setProxyUser( claim.getProxyUser() );
    setSubmissionDate( new Timestamp( claim.getSubmissionDate().getTime() ) );
    setReversal( claim.isReversal() );

    // Nomination claims have one and only one claim recipient.
    Set claimRecipients = claim.getClaimRecipients();
    if ( claimRecipients != null )
    {
      Iterator iter = claimRecipients.iterator();
      if ( iter.hasNext() )
      {
        ClaimRecipient claimRecipient = (ClaimRecipient)iter.next();
        setClaimRecipient( claimRecipient );
        setAwardQuantity( claimRecipient.getAwardQuantity() );
        setApprovalDate( claimRecipient.getDateApproved() );
        setApprovalStatusType( claimRecipient.getApprovalStatusType() );
        setRecipient( claimRecipient.getRecipient() );
      }
    }

    NominationAwardGroupType awardGroupType = ( (NominationPromotion)promotion ).getAwardGroupType();
    setAwardGroupType( awardGroupType );
    setTeamMembers( claim.getClaimRecipients() );
  }

  /**
   * Constructs a <code>RecognitionHistoryValueObject</code> object from a
   * {@link NominationClaim} object.
   *
   * @param claim  the {@link NominationClaim} object from which this object
   *               is constructed.
   */
  public RecognitionHistoryValueObject( NominationClaim claim, ApprovableItemApprover approvableItemApprover, ClaimRecipient claimRecipient )
  {
    Promotion promotion = claim.getPromotion();
    NominationActivity activity = getActivityService().getNominationActivity( claimRecipient.getRecipient().getId(), promotion.getId(), claim.getId(), approvableItemApprover.getApprovalRound() );
    if ( activity != null )
    {
      setClaim( claim );

      setPromotion( promotion );

      NominationPromotion nominationPromotion = (NominationPromotion)promotion;
      for ( NominationPromotionLevel nominationPromotionLevel : nominationPromotion.getNominationLevels() )
      {
        if ( approvableItemApprover.getApprovalRound() != null && approvableItemApprover.getApprovalRound().equals( nominationPromotionLevel.getLevelIndex() ) )
        {
          if ( nominationPromotionLevel.getAwardPayoutType() != null && nominationPromotionLevel.getAwardPayoutType().getCode() != null )
          {
            setAwardTypeName( PromotionAwardsType.lookup( nominationPromotionLevel.getAwardPayoutType().getCode() ).getName() );
            setAwardTypeCode( nominationPromotionLevel.getAwardPayoutType().getCode() );
            if ( nominationPromotionLevel.getAwardPayoutType().getCode().equals( PromotionAwardsType.OTHER ) )
            {
              setPayoutDescription( nominationPromotionLevel.getPayoutDescription() );
            }
          }
          break;
        }
      }
      setApprovalRound( approvableItemApprover.getApprovalRound() );
      setSubmitter( claim.getSubmitter() );
      setProxyUser( claim.getProxyUser() );
      setSubmissionDate( new Timestamp( claim.getSubmissionDate().getTime() ) );
      setReversal( claim.isReversal() );
      setClaimRecipient( claimRecipient );
      if ( getAwardTypeCode() != null && getAwardTypeCode().equalsIgnoreCase( PromotionAwardsType.POINTS ) )
      {
        setAwardQuantity( activity.getAwardQuantity() );
        setCashAwardQuantity( null );
      }
      if ( getAwardTypeCode() != null && getAwardTypeCode().equalsIgnoreCase( PromotionAwardsType.CASH ) )
      {
        setAwardQuantity( null );
        BigDecimal convertedPaxCurrency = getCashCurrencyService().convertCurrency( "USD", UserManager.getUserPrimaryCountryCurrencyCode(), activity.getCashAwardQuantity(), null );
        setCashAwardQuantity( convertedPaxCurrency != null ? convertedPaxCurrency.setScale( 2, BigDecimal.ROUND_FLOOR ) : null );
        setCurrencyCode( UserManager.getUserPrimaryCountryCurrencyCode() );
      }
      setApprovalDate( approvableItemApprover.getDateApproved() );
      setApprovalStatusType( approvableItemApprover.getApprovalStatusType() );
      setRecipient( claimRecipient.getRecipient() );

      NominationAwardGroupType awardGroupType = ( (NominationPromotion)promotion ).getAwardGroupType();
      NominationAwardGroupSizeType awardGroupSizeType = ( (NominationPromotion)promotion ).getAwardGroupSizeType();
      setAwardGroupType( awardGroupType );
      if ( ( awardGroupType.isTeam() || awardGroupType.isIndividualOrTeam() ) && ( awardGroupSizeType.isLimited() || awardGroupSizeType.isUnlimited() ) )
      {
        setTeamMembers( claim.getClaimRecipients() );
      }
    }
  }

  /**
   * Constructs a <code>RecognitionHistoryValueObject</code> object from a
   * {@link NominationClaim} object.
   *
   * @param claim  the {@link NominationClaim} object from which this object
   *               is constructed.
   */
  public RecognitionHistoryValueObject( NominationClaim claim, Long recipientId )  /* coke customization */
  {
    setClaim( claim );

    setPromotion( claim.getPromotion() );
    if(null != claim && null != claim.getPromotion()  && null != claim.getPromotion().getAwardType()  && null != claim.getPromotion().getAwardType().getCode() )
    {
    	setAwardTypeName( PromotionAwardsType.lookup( claim.getPromotion().getAwardType().getCode() ).getName() );
    }
    setSubmitter( claim.getSubmitter() );
    setProxyUser( claim.getProxyUser() );
    setSubmissionDate( new Timestamp( claim.getSubmissionDate().getTime() ) );

    // Nomination claims have one and only one claim recipient.
    Set claimRecipients = claim.getClaimRecipients();
    if ( claimRecipients != null )
    {
      Iterator iter = claimRecipients.iterator();
      if ( iter.hasNext() )
      {
        ClaimRecipient claimRecipient = (ClaimRecipient)iter.next();
        setClaimRecipient( claimRecipient );
        setAwardQuantity( claimRecipient.getAwardQuantity() );
        setApprovalDate( claimRecipient.getDateApproved() );
        setApprovalStatusType( claimRecipient.getApprovalStatusType() );
      }
    }
    
    /* coke customization start 
     * for team - need to get award quantity from claim_participant because it could be null for opt out pax */
    if ( recipientId != null && claim.isTeam())  //recipientId will only be populated when retrieving nominations received
    {
      for ( ProductClaimParticipant participant : claim.getTeamMembers() )
      {
        if (recipientId.equals( participant.getParticipant().getId() ))
        {
          setAwardQuantity( participant.getAwardQuantity() );
        }
      }
    }
    /* coke customization end */ 

    NominationAwardGroupType awardGroupType = ( (NominationPromotion)promotion ).getAwardGroupType();
    NominationAwardGroupSizeType awardGroupSizeType = ( (NominationPromotion)promotion ).getAwardGroupSizeType();
    setAwardGroupType( awardGroupType );
    if ( ( awardGroupType.isTeam() || awardGroupType.isIndividualOrTeam() ) && ( awardGroupSizeType.isLimited() || awardGroupSizeType.isUnlimited() ) )
    {
      setTeamMembers( claim.getClaimRecipients() );
    }
  }

  /**
   * Constructs a <code>RecognitionHistoryValueObject</code> object from a
   * {@link Journal} object.
   *
   * @param journal  the {@link Journal} object from which this object
   *               is constructed.
   */
  public RecognitionHistoryValueObject( Journal journal )
  {
    setSubmissionDate( new Timestamp( journal.getTransactionDate().getTime() ) );
    setPromotion( journal.getPromotion() );
    setRecipient( journal.getParticipant() );
    setJournalId( journal.getId() );
    setApprovalDate( new Timestamp( journal.getTransactionDate().getTime() ) );

    if ( journal.getTransactionAmount() != null )
    {
      if ( journal.getPromotion().isRecognitionPromotion() )
      {
        setAwardQuantity( journal.getTransactionAmount() );
        setAwardTypeName( getJournalAwardTypeName( journal ) );
      }
      else if ( journal.getPromotion().isNominationPromotion() )
      {
        if ( journal.getAwardPayoutType() != null && journal.getAwardPayoutType().getCode().equals( PromotionAwardsType.POINTS ) )
        {
          setAwardQuantity( journal.getTransactionAmount() );
        }
        else if ( journal.getAwardPayoutType() != null && journal.getAwardPayoutType().getCode().equals( PromotionAwardsType.CASH ) )
        {
          BigDecimal convertedPaxCurrency = getCashCurrencyService().convertCurrency( "USD", UserManager.getUserPrimaryCountryCurrencyCode(), journal.getTransactionCashAmount(), null );
          setCurrencyCode( UserManager.getUserPrimaryCountryCurrencyCode() );
          setCashAwardQuantity( convertedPaxCurrency );
        }
        if ( journal.getAwardPayoutType() != null )
        {
          setAwardTypeName( journal.getAwardPayoutType().getName() );
        }
      }
    }
    setDiscretionary( Journal.DISCRETIONARY.equals( journal.getJournalType() ) );
    setSweepstakes( Journal.SWEEPSTAKES.equals( journal.getJournalType() ) );
    if ( JournalTransactionType.REVERSE.equals( journal.getTransactionType().getCode() ) )
    {
      setReversalDescription( journal.getTransactionDescription() );
    }
  }

  /** Helper method to get award type from multiple sources */
  private String getJournalAwardTypeName( Journal journal )
  {
    if ( journal.getPromotion().getAwardType() != null )
    {
      return journal.getPromotion().getAwardTypeNameFromCM();
    }
    else if ( journal.getAwardPayoutType() != null )
    {
      return journal.getAwardPayoutType().getName();
    }
    else
    {
      return "";
    }
  }

  // ---------------------------------------------------------------------------
  // Getter and Setter Methods
  // ---------------------------------------------------------------------------

  public Date getApprovalDate()
  {
    return approvalDate;
  }

  public void setApprovalDate( Date approvalDate )
  {
    this.approvalDate = approvalDate;
  }

  public ApprovalStatusType getApprovalStatusType()
  {
    return approvalStatusType;
  }

  public void setApprovalStatusType( ApprovalStatusType approvalStatusType )
  {
    this.approvalStatusType = approvalStatusType;
  }

  public NominationAwardGroupType getAwardGroupType()
  {
    return awardGroupType;
  }

  public void setAwardGroupType( NominationAwardGroupType awardGroupType )
  {
    this.awardGroupType = awardGroupType;
  }

  public Long getAwardQuantity()
  {
    return awardQuantity;
  }

  public void setAwardQuantity( Long awardQuantity )
  {
    this.awardQuantity = awardQuantity;
  }

  public BigDecimal getCashAwardQuantity()
  {
    return cashAwardQuantity;
  }

  public void setCashAwardQuantity( BigDecimal cashAwardQuantity )
  {
    this.cashAwardQuantity = cashAwardQuantity;
  }

  public Claim getClaim()
  {
    return claim;
  }

  public void setClaim( Claim claim )
  {
    this.claim = claim;
  }

  public ClaimGroup getClaimGroup()
  {
    return claimGroup;
  }

  public void setClaimGroup( ClaimGroup claimGroup )
  {
    this.claimGroup = claimGroup;
  }

  public ClaimRecipient getClaimRecipient()
  {
    return claimRecipient;
  }

  public void setClaimRecipient( ClaimRecipient claimRecipient )
  {
    this.claimRecipient = claimRecipient;
  }

  public Set getClaims()
  {
    return claims;
  }

  public void setClaims( Set claims )
  {
    this.claims = claims;
  }

  public Long getJournalId()
  {
    return journalId;
  }

  public void setJournalId( Long journalId )
  {
    this.journalId = journalId;
  }

  public Promotion getPromotion()
  {
    return promotion;
  }

  public void setPromotion( Promotion promotion )
  {
    this.promotion = promotion;
  }

  public User getProxyUser()
  {
    return proxyUser;
  }

  public void setProxyUser( User proxyUser )
  {
    this.proxyUser = proxyUser;
  }

  public Participant getRecipient()
  {
    return recipient;
  }

  public void setRecipient( Participant recipient )
  {
    this.recipient = recipient;
  }

  public Timestamp getSubmissionDate()
  {
    return submissionDate;
  }

  public void setSubmissionDate( Timestamp submissionDate )
  {
    this.submissionDate = submissionDate;
  }

  public Participant getSubmitter()
  {
    return submitter;
  }

  public void setSubmitter( Participant submitter )
  {
    this.submitter = submitter;
  }

  public Set getTeamMembers()
  {
    return teamMembers;
  }

  public void setTeamMembers( Set teamMembers )
  {
    this.teamMembers = teamMembers;
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

  public String getAwardTypeName()
  {
    if ( awardTypeName != null )
    {
      return awardTypeName;
    }
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

  public List getMerchGiftCodeActivityList()
  {
    return merchGiftCodeActivityList;
  }

  public void setMerchGiftCodeActivityList( List merchGiftCodeActivityList )
  {
    this.merchGiftCodeActivityList = merchGiftCodeActivityList;
  }

  public boolean isReversal()
  {
    return isReversal;
  }

  public void setReversal( boolean isReversal )
  {
    this.isReversal = isReversal;
  }

  public String getRegSymbol()
  {
    return regSymbol;
  }

  public void setRegSymbol( String regSymbol )
  {
    this.regSymbol = regSymbol;
  }

  public String getSubmitterComments()
  {
    return submitterComments;
  }

  public void setSubmitterComments( String submitterComments )
  {
    this.submitterComments = submitterComments;
  }

  public String getFormatSubmissionDate()
  {
    return formatSubmissionDate;
  }

  public void setFormatSubmissionDate( String formatSubmissionDate )
  {
    this.formatSubmissionDate = formatSubmissionDate;
  }

  public String getDisplaySubmissionDate()
  {
    if ( this.submissionDate != null )
    {
      return DateUtils.toDisplayString( this.submissionDate );
    }
    else
    {
      return "";
    }
  }

  public String getDisplayApprovalDate()
  {
    if ( this.approvalDate != null )
    {
      return DateUtils.toDisplayString( this.approvalDate );
    }
    else
    {
      return "";
    }
  }

  public String getLevelName()
  {
    return levelName;
  }

  public void setLevelName( String levelName )
  {
    this.levelName = levelName;
  }

  public Boolean getIsBadgePromotion()
  {
    return isBadgePromotion;
  }

  public void setIsBadgePromotion( Boolean isBadgePromotion )
  {
    this.isBadgePromotion = isBadgePromotion;
  }

  public String getCurrencyCode()
  {
    return currencyCode;
  }

  public void setCurrencyCode( String currencyCode )
  {
    this.currencyCode = currencyCode;
  }

  public String getAwardTypeCode()
  {
    return awardTypeCode;
  }

  public void setAwardTypeCode( String awardTypeCode )
  {
    this.awardTypeCode = awardTypeCode;
  }

  private CashCurrencyService getCashCurrencyService()
  {
    return (CashCurrencyService)BeanLocator.getBean( CashCurrencyService.BEAN_NAME );
  }

  public String getPayoutDescription()
  {
    return payoutDescription;
  }

  public void setPayoutDescription( String payoutDescription )
  {
    this.payoutDescription = payoutDescription;
  }

  public Long getApprovalRound()
  {
    return approvalRound;
  }

  public void setApprovalRound( Long approvalRound )
  {
    this.approvalRound = approvalRound;
  }

  private ActivityService getActivityService()
  {
    return (ActivityService)BeanLocator.getBean( ActivityService.BEAN_NAME );
  }

  private ClaimGroupService getClaimGroupService()
  {
    return (ClaimGroupService)BeanLocator.getBean( ClaimGroupService.BEAN_NAME );
  }

}
