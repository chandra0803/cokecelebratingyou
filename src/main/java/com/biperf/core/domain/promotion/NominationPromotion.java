/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/domain/promotion/NominationPromotion.java,v $
 */

package com.biperf.core.domain.promotion;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.biperf.core.domain.budget.BudgetMaster;
import com.biperf.core.domain.enums.CustomApproverType;
import com.biperf.core.domain.enums.NominationAwardGroupSizeType;
import com.biperf.core.domain.enums.NominationAwardGroupType;
import com.biperf.core.domain.enums.NominationAwardSpecifierType;
import com.biperf.core.domain.enums.NominationEvaluationType;
import com.biperf.core.domain.multimedia.ECard;
import com.biperf.core.domain.participant.Audience;
import com.biperf.core.domain.participant.Participant;

/**
 * NominationPromotion.
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
 * <td>sathish</td>
 * <td>Oct 7, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class NominationPromotion extends AbstractRecognitionPromotion
{

  // ---------------------------------------------------------------------------
  // Fields
  // ---------------------------------------------------------------------------
  
  //Client customizations for WIP #59418 starts   
  /**
     * CM team Promotion prefix
     */
  public final static String CM_TEAM_ASSET_PREFIX = "coke.noms.team.copy.";
    
    public static final String TEAM_ASSET_TYPE_NAME = "_TeamData2";

    /**
     * CM team Promotion Data section
     */
    public final static String CM_TEAM_DATA_SECTION = "cokenomteamcopy";

    /**
     * CM Team Promotion Data key prefix
     */
    public final static String CM_TEAM_TEXT_KEY_PREFIX = "TEAM_NAME_";

    /**
     * CM Team Promotion Data key desc
     */
    public final static String CM_TEAM_TEXT_KEY_DESC = "Coke team nomination copy";
    
    //Client customizations for WIP #59418 ends 

  /**
   * If "individual," then only individuals can be nominated.  If "unlimited
   * size team," then teams whose size is unlimited can be nominated.  If
   * "limited size team," then teams whose size is limited can be nominated.
   */
  private NominationAwardGroupType awardGroupType;

  /**
   * When the awardGroupType is "limited size team," <code>maxGroupMembers</code>
   * is the maximum size of a team.  When the awardGroupType is not "limited
   * size team," <code>maxGroupMembers</code> is null.
   */
  private Integer maxGroupMembers;

  /**
   * If "cumulative," then the approver considers all nominations for a
   * nominee when deciding whether to approve a nominee's nomination.  If
   * "independent," then the approver considers each nomination for a nominee
   * separately.
   */
  private NominationEvaluationType evaluationType;

  /**
   * If true, participants can nominate themselves; if false, they cannot.
   */
  private boolean selfNomination;

  /**
   *    If true, the publication date is required and must be after submission
   *    start date, and after approval approval start and end dates. 
   */
  private boolean publicationDateActive;

  /**
   *    The publication date will allow administrators of a promotion the ability to
   *    control the visibility of promotion data in transaction history and reports.    
   */
  private Date publicationDate;

  private boolean allowYourOwnCard;

  private boolean drawYourOwnCard;

  private String payoutLevel;

  private NominationAwardGroupSizeType awardGroupSizeType;

  private boolean timePeriodActive;

  /**
   * True when more budget can be requested for an award
   */
  private boolean requestMoreBudget;

  /**
   * Recipient of budget increase requests
   */
  private Participant budgetApprover;

  /**
   * Fallback approver for nominations. Mandatory.
   */
  private Participant defaultApprover;

  /** The certificates available for selection in this promotion */
  private Set<PromotionCert> promotionCertificates = new LinkedHashSet<PromotionCert>();

  private Set<NominationPromotionTimePeriod> nominationTimePeriods = new HashSet<NominationPromotionTimePeriod>();

  private Set<NominationPromotionLevel> nominationLevels = new LinkedHashSet<NominationPromotionLevel>();

  private boolean nominatorRecommendedAward;

  private List<ApproverOption> customApproverOptions = new ArrayList<ApproverOption>();

  private Set<PromotionPublicRecognitionAudience> promotionPublicRecognitionAudiences = new LinkedHashSet<>();
  private boolean allowPublicRecognitionPoints;
  private BudgetMaster publicRecogBudgetMaster;
  private Long publicRecogAwardAmountMin;
  private Long publicRecogAwardAmountMax;
  private boolean publicRecogAwardAmountTypeFixed;
  private Long publicRecogAwardAmountFixed;

  private boolean certificateActive;
  private boolean oneCertPerPromotion;

  private boolean whyNomination;
  private boolean viewPastWinners;

  private Date lastPointBudgetRequestDate;
  private Date lastCashBudgetRequestDate;
  // Client customization for WIP #39189 ends
  private boolean levelSelectionByApprover; //Client customization for WIP #56492
//Client customization for WIP #58122 starts
 private boolean levelPayoutByApproverAvailable;
 private Integer capPerPax;
 // Client customization for WIP #58122 ends
 //Client customizations for WIP #59418 starts
 private String teamCmAsset;
 private String teamCmKey;
 private String teamCmAssetText;
 //Client customizations for WIP #59418 ends
 private boolean hideCommentsOnNomination; //Client customization for WIP #55713
 
 // Client customization for WIP #39189 starts
 private boolean enableFileUpload;
 private Integer fileMinNumber;
 private Integer fileMaxNumber;
 // Client customization for WIP #39189 ends
 // Client customization for WIP #59420 starts
 private String allowedFileTypes;
 // Client customization for WIP #59420 ends
 
 //Client customization start
 private boolean allowMeme;
 private boolean allowSticker;
 private boolean allowUploadOwnMeme;

 /**
  * Identifies who can specify the award amount: the approver, or the approver
  * and the nominator.
  */
 private NominationAwardSpecifierType awardSpecifierType;

 //Client customization end
  // ---------------------------------------------------------------------------
  // Copy Methods
  // ---------------------------------------------------------------------------

  /**
   * Returns a deep copy of this object.
   *
   * @param cloneWithChildren
   * @param newPromotionName   the promotion name to be assigned to the copy.
   * @param newChildPromotionNameHolders
   * @return a deep copy of this <code>AbstractRecognitionPromotion</code> object.
   * @throws CloneNotSupportedException if one of this objects component objects
   *         does not support cloning.
   */
  public Object deepCopy( boolean cloneWithChildren, String newPromotionName, List newChildPromotionNameHolders ) throws CloneNotSupportedException
  {
    NominationPromotion clonedPromotion = (NominationPromotion)super.deepCopy( cloneWithChildren, newPromotionName, newChildPromotionNameHolders );

    // These fields are in AbstractRecognitionPromotion, but copied here (matching
    // RecognitionPromotion, etc)
    clonedPromotion.setAllowYourOwnCard( this.isAllowYourOwnCard() );
    clonedPromotion.setDrawYourOwnCard( this.isDrawYourOwnCard() );

    clonedPromotion.setSelfNomination( this.isSelfNomination() );
    clonedPromotion.setEvaluationType( this.getEvaluationType() );
    clonedPromotion.setAwardGroupType( this.getAwardGroupType() );
    clonedPromotion.setMaxGroupMembers( this.getMaxGroupMembers() );
    clonedPromotion.setPublicationDateActive( this.isPublicationDateActive() );
    clonedPromotion.setPublicationDate( this.getPublicationDate() );
    clonedPromotion.setAllowYourOwnCard( this.isAllowYourOwnCard() );
    clonedPromotion.setDrawYourOwnCard( this.isDrawYourOwnCard() );
    clonedPromotion.setPayoutLevel( this.getPayoutLevel() );
    clonedPromotion.setAwardGroupSizeType( this.getAwardGroupSizeType() );
    clonedPromotion.setTimePeriodActive( this.isTimePeriodActive() );
    clonedPromotion.setWhyNomination( this.isWhyNomination() );
    // clonedPromotion.setPayoutDescription( this.getPayoutDescription() );
    // clonedPromotion.setPayoutValue( this.getPayoutValue() );
    // clonedPromotion.setPayoutCurrency( this.getPayoutCurrency() );
    clonedPromotion.setRequestMoreBudget( this.isRequestMoreBudget() );
    clonedPromotion.setBudgetApprover( this.getBudgetApprover() );
    clonedPromotion.setDefaultApprover( this.getDefaultApprover() );

    // Deep copy certificates
    clonedPromotion.setPromotionCertificates( new TreeSet<PromotionCert>() );
    if ( getPromotionCertificates() != null )
    {
      Iterator<PromotionCert> certIterator = getPromotionCertificates().iterator();
      while ( certIterator.hasNext() )
      {
        PromotionCert certificate = certIterator.next();
        clonedPromotion.addCertificate( certificate.deepCopy() );
      }
    }

    // Deep copy time periods
    clonedPromotion.setNominationTimePeriods( new HashSet<NominationPromotionTimePeriod>() );
    if ( getNominationTimePeriods() != null )
    {
      Iterator<NominationPromotionTimePeriod> timePeriodIterator = getNominationTimePeriods().iterator();
      while ( timePeriodIterator.hasNext() )
      {
        NominationPromotionTimePeriod timePeriod = timePeriodIterator.next();
        clonedPromotion.addNominationTimePeriods( timePeriod.deepCopy() );
      }
    }

    // Deep copy nomination levels
    clonedPromotion.setNominationLevels( new LinkedHashSet<NominationPromotionLevel>() );
    if ( getNominationLevels() != null )
    {
      Iterator<NominationPromotionLevel> nominationLevelIterator = getNominationLevels().iterator();
      while ( nominationLevelIterator.hasNext() )
      {
        NominationPromotionLevel nominationLevel = nominationLevelIterator.next();
        clonedPromotion.addNominationLevels( nominationLevel.deepCopy() );
      }
    }

    clonedPromotion.setNominatorRecommendedAward( this.isNominatorRecommendedAward() );

    // Deep copy custom approver options
    clonedPromotion.setCustomApproverOptions( new ArrayList<ApproverOption>() );
    if ( getCustomApproverOptions() != null )
    {
      Iterator<ApproverOption> customApproverIterator = getCustomApproverOptions().iterator();
      while ( customApproverIterator.hasNext() )
      {
        ApproverOption approverOption = customApproverIterator.next();
        clonedPromotion.addCustomApproverOptions( approverOption.deepCopy() );
      }
    }

    clonedPromotion.setPaxDisplayOrder( this.getPaxDisplayOrder() );

    // Deep copy public recognition
    clonedPromotion.setPromotionPublicRecognitionAudiences( new LinkedHashSet<PromotionPublicRecognitionAudience>() );
    for ( Iterator<PromotionPublicRecognitionAudience> audiencesIter = this.promotionPublicRecognitionAudiences.iterator(); audiencesIter.hasNext(); )
    {
      PromotionPublicRecognitionAudience promotionPublicRecognitionAudience = audiencesIter.next();
      clonedPromotion.addPromotionPublicRecognitionAudience( (PromotionPublicRecognitionAudience)promotionPublicRecognitionAudience.clone() );
    }
    clonedPromotion.setAllowPublicRecognitionPoints( allowPublicRecognitionPoints );
    clonedPromotion.setPublicRecogBudgetMaster( null );
    clonedPromotion.setPublicRecogAwardAmountMin( publicRecogAwardAmountMin );
    clonedPromotion.setPublicRecogAwardAmountMax( publicRecogAwardAmountMax );
    clonedPromotion.setPublicRecogAwardAmountTypeFixed( publicRecogAwardAmountTypeFixed );
    clonedPromotion.setPublicRecogAwardAmountFixed( publicRecogAwardAmountFixed );

    clonedPromotion.setCertificateActive( certificateActive );
    clonedPromotion.setOneCertPerPromotion( oneCertPerPromotion );
    //client customization start
    clonedPromotion.setAllowMeme( this.allowMeme );
    clonedPromotion.setAllowSticker( this.allowSticker );
    clonedPromotion.setAllowUploadOwnMeme(this.allowUploadOwnMeme);
    //client customization end

    return clonedPromotion;
  }

  public boolean isCumulative()
  {
    return NominationEvaluationType.CUMULATIVE.equals( evaluationType.getCode() );
  }

  public boolean isTeam()
  {
    return !NominationAwardGroupType.INDIVIDUAL.equals( awardGroupType.getCode() );
  }
  // ---------------------------------------------------------------------------
  // Getter and Setter Methods
  // ---------------------------------------------------------------------------

  public NominationAwardGroupType getAwardGroupType()
  {
    return awardGroupType;
  }

  public NominationEvaluationType getEvaluationType()
  {
    return evaluationType;
  }

  public Integer getMaxGroupMembers()
  {
    return maxGroupMembers;
  }

  public boolean isSelfNomination()
  {
    return selfNomination;
  }

  public void setAwardGroupType( NominationAwardGroupType awardGroupType )
  {
    this.awardGroupType = awardGroupType;
  }

  public void setEvaluationType( NominationEvaluationType evaluationType )
  {
    this.evaluationType = evaluationType;
  }

  public void setMaxGroupMembers( Integer maxGroupMembers )
  {
    this.maxGroupMembers = maxGroupMembers;
  }

  public void setSelfNomination( boolean selfNomination )
  {
    this.selfNomination = selfNomination;
  }

  public Date getPublicationDate()
  {
    return publicationDate;
  }

  public void setPublicationDate( Date publicationDate )
  {
    this.publicationDate = publicationDate;
  }

  public boolean isPublicationDateActive()
  {
    return publicationDateActive;
  }

  public void setPublicationDateActive( boolean publicationDateActive )
  {
    this.publicationDateActive = publicationDateActive;
  }

  public boolean isAllowYourOwnCard()
  {
    return allowYourOwnCard;
  }

  public void setAllowYourOwnCard( boolean allowYourOwnCard )
  {
    this.allowYourOwnCard = allowYourOwnCard;
  }

  public boolean isDrawYourOwnCard()
  {
    return drawYourOwnCard;
  }

  public void setDrawYourOwnCard( boolean drawYourOwnCard )
  {
    this.drawYourOwnCard = drawYourOwnCard;
  }

  public NominationAwardGroupSizeType getAwardGroupSizeType()
  {
    return awardGroupSizeType;
  }

  public void setAwardGroupSizeType( NominationAwardGroupSizeType awardGroupSizeType )
  {
    this.awardGroupSizeType = awardGroupSizeType;
  }

  public boolean isRequestMoreBudget()
  {
    return requestMoreBudget;
  }

  public void setRequestMoreBudget( boolean requestMoreBudget )
  {
    this.requestMoreBudget = requestMoreBudget;
  }

  public Participant getBudgetApprover()
  {
    return budgetApprover;
  }

  public void setBudgetApprover( Participant budgetApprover )
  {
    this.budgetApprover = budgetApprover;
  }

  public Participant getDefaultApprover()
  {
    return defaultApprover;
  }

  public void setDefaultApprover( Participant defaultApprover )
  {
    this.defaultApprover = defaultApprover;
  }

  public Set<PromotionCert> getPromotionCertificates()
  {
    return promotionCertificates;
  }

  public void setPromotionCertificates( Set<PromotionCert> promotionCertificates )
  {
    this.promotionCertificates = promotionCertificates;
  }

  public void addCertificate( PromotionCert promotionCertificate )
  {
    promotionCertificate.setPromotion( this );
    this.promotionCertificates.add( promotionCertificate );
  }

  public void addPromotionCertificate( String certificateId, String orderNumber )
  {
    PromotionCert promotionCert = new PromotionCert( this, certificateId, orderNumber );
    this.promotionCertificates.add( promotionCert );
  }

  public void addPromotionEcard( ECard eCard, String orderNumber )
  {
    PromotionECard promotionEcard = new PromotionECard( this, eCard, orderNumber );
    this.getPromotionECard().add( promotionEcard );
  }

  public boolean isTimePeriodActive()
  {
    return timePeriodActive;
  }

  public void setTimePeriodActive( boolean timePeriodActive )
  {
    this.timePeriodActive = timePeriodActive;
  }

  public boolean timePeriodExist()
  {
    return getNominationTimePeriods() != null && getNominationTimePeriods().size() > 0;
  }

  public List<ApproverOption> getCustomApproverOptions()
  {
    return customApproverOptions;
  }

  public Set<ApproverOption> getCustomApproverOptionsByLevel( Long level )
  {
    Set<ApproverOption> approversByLevel = new HashSet<ApproverOption>();

    for ( ApproverOption option : customApproverOptions )
    {
      if ( option.getApprovalLevel().equals( level ) )
      {
        approversByLevel.add( option );
      }

    }

    return approversByLevel;
  }

  public void setCustomApproverOptions( List<ApproverOption> customApproverOptions )
  {
    this.customApproverOptions = customApproverOptions;
  }

  public void addCustomApproverOptions( ApproverOption customApproverOptions )
  {
    customApproverOptions.setNominationPromotion( this );
    this.customApproverOptions.add( customApproverOptions );
  }

  public void addNominationTimePeriods( NominationPromotionTimePeriod timePeriod )
  {
    timePeriod.setNominationPromotion( this );
    this.nominationTimePeriods.add( timePeriod );
  }

  public Set<NominationPromotionTimePeriod> getNominationTimePeriods()
  {
    return nominationTimePeriods;
  }

  public void setNominationTimePeriods( Set<NominationPromotionTimePeriod> nominationTimePeriods )
  {
    this.nominationTimePeriods = nominationTimePeriods;
  }

  public Set<NominationPromotionLevel> getNominationLevels()
  {
    return nominationLevels;
  }

  public void setNominationLevels( Set<NominationPromotionLevel> nominationLevels )
  {
    this.nominationLevels = nominationLevels;
  }

  public void addNominationLevels( NominationPromotionLevel nominationPromotionLevel )
  {
    nominationPromotionLevel.setNominationPromotion( this );
    this.nominationLevels.add( nominationPromotionLevel );
  }

  public boolean isNominatorRecommendedAward()
  {
    return nominatorRecommendedAward;
  }

  public void setNominatorRecommendedAward( boolean nominatorRecommendedAward )
  {
    this.nominatorRecommendedAward = nominatorRecommendedAward;
  }

  public void addPromotionPublicRecognitionAudience( PromotionPublicRecognitionAudience promotionPublicRecognitionAudience )
  {
    promotionPublicRecognitionAudience.setPromotion( this );
    this.promotionPublicRecognitionAudiences.add( promotionPublicRecognitionAudience );
  }

  public Set<PromotionPublicRecognitionAudience> getPromotionPublicRecognitionAudiences()
  {
    return promotionPublicRecognitionAudiences;
  }

  public void setPromotionPublicRecognitionAudiences( Set<PromotionPublicRecognitionAudience> promotionPublicRecognitionAudiences )
  {
    this.promotionPublicRecognitionAudiences = promotionPublicRecognitionAudiences;
  }

//Client customization for WIP #58122 starts
  
  public boolean isLevelPayoutByApproverAvailable()
  {
    return levelPayoutByApproverAvailable;
  }


  public void setLevelPayoutByApproverAvailable( boolean levelPayoutByApproverAvailable )
  {
    this.levelPayoutByApproverAvailable = levelPayoutByApproverAvailable;
  }


  public Integer getCapPerPax()
  {
    return capPerPax;
  }


  public void setCapPerPax( Integer capPerPax )
  {
    this.capPerPax = capPerPax;
  }
//Client customization for WIP #58122 ends

  /**
   * @return the Audience objects from the Primary PromotionAudience objects
   */
  public Set<Audience> getPublicRecognitionAudiencesAsAudiences()
  {
    if ( getPromotionPublicRecognitionAudiences() == null )
    {
      return null;
    }

    Set<Audience> audiences = new LinkedHashSet<>( getPromotionPublicRecognitionAudiences().size() );
    for ( Iterator<PromotionPublicRecognitionAudience> iter = getPromotionPublicRecognitionAudiences().iterator(); iter.hasNext(); )
    {
      PromotionPublicRecognitionAudience promotionPublicRecognitionAudience = iter.next();
      audiences.add( promotionPublicRecognitionAudience.getAudience() );
    }

    return audiences;
  }

  public boolean isAllowPublicRecognitionPoints()
  {
    return allowPublicRecognitionPoints;
  }

  public void setAllowPublicRecognitionPoints( boolean allowPublicRecognitionPoints )
  {
    this.allowPublicRecognitionPoints = allowPublicRecognitionPoints;
  }

  public Long getPublicRecogAwardAmountMin()
  {
    return publicRecogAwardAmountMin;
  }

  public void setPublicRecogAwardAmountMin( Long publicRecogAwardAmountMin )
  {
    this.publicRecogAwardAmountMin = publicRecogAwardAmountMin;
  }

  public Long getPublicRecogAwardAmountMax()
  {
    return publicRecogAwardAmountMax;
  }

  public void setPublicRecogAwardAmountMax( Long publicRecogAwardAmountMax )
  {
    this.publicRecogAwardAmountMax = publicRecogAwardAmountMax;
  }

  public void setPublicRecogBudgetMaster( BudgetMaster publicRecogBudgetMaster )
  {
    this.publicRecogBudgetMaster = publicRecogBudgetMaster;
  }

  public BudgetMaster getPublicRecogBudgetMaster()
  {
    return publicRecogBudgetMaster;
  }

  public boolean isPublicRecogAwardAmountTypeFixed()
  {
    return publicRecogAwardAmountTypeFixed;
  }

  public void setPublicRecogAwardAmountTypeFixed( boolean publicRecogAwardAmountTypeFixed )
  {
    this.publicRecogAwardAmountTypeFixed = publicRecogAwardAmountTypeFixed;
  }

  public Long getPublicRecogAwardAmountFixed()
  {
    return publicRecogAwardAmountFixed;
  }

  public void setPublicRecogAwardAmountFixed( Long publicRecogAwardAmountFixed )
  {
    this.publicRecogAwardAmountFixed = publicRecogAwardAmountFixed;
  }

  public boolean isPublicRecBudgetUsed()
  {
    return getPublicRecogBudgetMaster() != null;
  }

  public boolean isCertificateActive()
  {
    return certificateActive;
  }

  public void setCertificateActive( boolean certificateActive )
  {
    this.certificateActive = certificateActive;
  }

  public boolean isOneCertPerPromotion()
  {
    return oneCertPerPromotion;
  }

  public void setOneCertPerPromotion( boolean oneCertPerPromotion )
  {
    this.oneCertPerPromotion = oneCertPerPromotion;
  }

  public String getPayoutLevel()
  {
    return payoutLevel;
  }

  public void setPayoutLevel( String payoutLevel )
  {
    this.payoutLevel = payoutLevel;
  }

  public boolean isWhyNomination()
  {
    return whyNomination;
  }

  public void setWhyNomination( boolean whyNomination )
  {
    this.whyNomination = whyNomination;
  }

  public boolean behaviourBasedApproverTypeExist()
  {
    List<ApproverOption> customApproverOptions = this.getCustomApproverOptions();

    for ( ApproverOption approverOption : customApproverOptions )
    {
      if ( CustomApproverType.lookup( CustomApproverType.BEHAVIOR ).equals( approverOption.getApproverType() ) )
      {
        return true;
      }

    }
    return false;
  }

  public boolean isViewPastWinners()
  {
    return viewPastWinners;
  }

  public void setViewPastWinners( boolean viewPastWinners )
  {
    this.viewPastWinners = viewPastWinners;
  }

  public Date getLastPointBudgetRequestDate()
  {
    return lastPointBudgetRequestDate;
  }

  public void setLastPointBudgetRequestDate( Date lastPointBudgetRequestDate )
  {
    this.lastPointBudgetRequestDate = lastPointBudgetRequestDate;
  }

  public Date getLastCashBudgetRequestDate()
  {
    return lastCashBudgetRequestDate;
  }

  public void setLastCashBudgetRequestDate( Date lastCashBudgetRequestDate )
  {
    this.lastCashBudgetRequestDate = lastCashBudgetRequestDate;
  }

  // Client customization for WIP #56492 starts
  public boolean isLevelSelectionByApprover()
  {
    return levelSelectionByApprover;
  }

  public void setLevelSelectionByApprover( boolean levelSelectionByApprover )
  {
    this.levelSelectionByApprover = levelSelectionByApprover;
  }
  // Client customization for WIP #56492 ends
  // Client customization for WIP #39189 starts
  public boolean isEnableFileUpload()
  {
    return enableFileUpload;
  }

  public void setEnableFileUpload( boolean enableFileUpload )
  {
    this.enableFileUpload = enableFileUpload;
  }

  public Integer getFileMinNumber()
  {
    return fileMinNumber;
  }

  public void setFileMinNumber( Integer fileMinNumber )
  {
    this.fileMinNumber = fileMinNumber;
  }

  public Integer getFileMaxNumber()
  {
    return fileMaxNumber;
  }

  public void setFileMaxNumber( Integer fileMaxNumber )
  {
    this.fileMaxNumber = fileMaxNumber;
  }  
  // Client customization for WIP #39189 ends
  //Client customization for WIP #55713 start
  public boolean isHideCommentsOnNomination() {
	  
	return hideCommentsOnNomination;
  }
  public void setHideCommentsOnNomination(boolean hideCommentsOnNomination) {
	  
	this.hideCommentsOnNomination = hideCommentsOnNomination;
  }
  //Client customization for WIP #55713 end
  // Client customization for WIP #59420 starts
  public String getAllowedFileTypes()
  {
    return allowedFileTypes;
  }

  public void setAllowedFileTypes( String allowedFileTypes )
  {
    this.allowedFileTypes = allowedFileTypes;
  }
  // Client customization for WIP #59420 ends
  
//Client customizations for WIP #59418 starts
 public String getTeamCmAsset()
 {
   return teamCmAsset;
 }

 public void setTeamCmAsset( String teamCmAsset )
 {
   this.teamCmAsset = teamCmAsset;
 }

 public String getTeamCmKey()
 {
   return teamCmKey;
 }

 public void setTeamCmKey( String teamCmKey )
 {
   this.teamCmKey = teamCmKey;
 }

 public String getTeamCmAssetText()
 {
   return teamCmAssetText;
 }

 public void setTeamCmAssetText( String teamCmAssetText )
 {
   this.teamCmAssetText = teamCmAssetText;
 }
 // Client customizations for WIP #59418 ends

public boolean isAllowMeme()
{
  return allowMeme;
}

public void setAllowMeme( boolean allowMeme )
{
  this.allowMeme = allowMeme;
}

public boolean isAllowSticker()
{
  return allowSticker;
}

public void setAllowSticker( boolean allowSticker )
{
  this.allowSticker = allowSticker;
}

public boolean isAllowUploadOwnMeme()
{
  return allowUploadOwnMeme;
}

public void setAllowUploadOwnMeme( boolean allowUploadOwnMeme )
{
  this.allowUploadOwnMeme = allowUploadOwnMeme;
}

/**
 * @return the awardSpecifierType
 */
public NominationAwardSpecifierType getAwardSpecifierType() {
	return awardSpecifierType;
}

/**
 * @param awardSpecifierType the awardSpecifierType to set
 */
public void setAwardSpecifierType(NominationAwardSpecifierType awardSpecifierType) {
	this.awardSpecifierType = awardSpecifierType;
}


}
