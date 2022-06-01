/*
\ * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/promotion/PromotionOverviewForm.java,v $
 */

package com.biperf.core.ui.promotion;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.domain.enums.ManagerWebRulesAudienceType;
import com.biperf.core.domain.enums.PartnerWebRulesAudienceType;
import com.biperf.core.domain.enums.PrimaryAudienceType;
import com.biperf.core.domain.enums.PromotionAwardsType;
import com.biperf.core.domain.enums.PromotionIssuanceType;
import com.biperf.core.domain.enums.PromotionStatusType;
import com.biperf.core.domain.enums.PublicRecognitionAudienceType;
import com.biperf.core.domain.enums.SSIContestType;
import com.biperf.core.domain.enums.SecondaryAudienceType;
import com.biperf.core.domain.enums.SweepstakesWinnerEligibilityType;
import com.biperf.core.domain.enums.SweepstakesWinnersType;
import com.biperf.core.domain.enums.WebRulesAudienceType;
import com.biperf.core.domain.hierarchy.Hierarchy;
import com.biperf.core.domain.participant.Audience;
import com.biperf.core.domain.promotion.ChallengePointPromotion;
import com.biperf.core.domain.promotion.Division;
import com.biperf.core.domain.promotion.DivisionCompetitorsAudience;
import com.biperf.core.domain.promotion.EngagementPromotion;
import com.biperf.core.domain.promotion.EngagementPromotions;
import com.biperf.core.domain.promotion.GoalQuestPromotion;
import com.biperf.core.domain.promotion.HomePageItem;
import com.biperf.core.domain.promotion.NominationPromotion;
import com.biperf.core.domain.promotion.ProductClaimPromotion;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.PromotionPublicRecognitionAudience;
import com.biperf.core.domain.promotion.QuizPromotion;
import com.biperf.core.domain.promotion.RecognitionPromotion;
import com.biperf.core.domain.promotion.SurveyPromotion;
import com.biperf.core.domain.promotion.ThrowdownPromotion;
import com.biperf.core.domain.promotion.WellnessPromotion;
import com.biperf.core.domain.ssi.SSIPromotion;
import com.biperf.core.domain.ssi.SSIPromotionContestApprovalLevel1Audience;
import com.biperf.core.domain.ssi.SSIPromotionContestApprovalLevel2Audience;
import com.biperf.core.service.SAO;
import com.biperf.core.service.hierarchy.HierarchyService;
import com.biperf.core.service.participant.ListBuilderService;
import com.biperf.core.service.participant.UserCharacteristicService;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.service.ssi.SSIPromotionService;
import com.biperf.core.ui.BaseForm;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.NewServiceAnniversaryUtil;
import com.objectpartners.cms.util.CmsResourceBundle;

/**
 * Promotion Overview ActionForm transfer object.
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
 * <td>sedey</td>
 * <td>July 05, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 */
public class PromotionOverviewForm extends BaseForm
{
  private Long promotionId;
  private Long parentPromotionId;
  private String parentPromotionName;
  private String promotionName;
  private String promotionType;
  private String promotionTypeCode;
  private String promotionStatus;
  private String promotionStatusDesc;
  private String claimFormName;
  private String claimFormAsset;
  private Long claimFormId;
  private String claimSubmissionStartDate;
  private String claimSubmissionEndDate;
  private String individualOrTeamBased;
  private String evaluated;
  private String processingMode;
  private String attemptsAllowed;
  private boolean includeCertificate;
  private boolean copyRecipientManager;
  private boolean copyOthers;
  private boolean allPaxAsSubmitters;
  private boolean selfEnrollOnly;
  private boolean entireParentAudience;
  private boolean teamUsed;
  private boolean allPaxAsReceivers;
  private boolean teamSameAsSubmitters;
  private boolean teamFromSubmitterNode;
  private boolean teamFromSubmitterNodeBelow;
  private boolean teamSpecifyAudience;
  private boolean teamCollectedAsGroup;
  private boolean payoutManager;
  private boolean taxable;
  private boolean selfNomination;
  private int numberOfProducts;
  private int payoutManagerPercent;
  private String payoutManagerPeriod;
  private String payoutType;
  private String awardsActive;
  private String awardType;
  private String award;
  private String budgetName;
  private String sweepstakesActive;
  private String awardTypeText;
  private String numberOrPercentOfGivers;
  private String numberOrPercentOfReceivers;
  private String amountOfGiversAwards;
  private String amountOfReceiversAwards;
  private String sweepstakesPrimaryAwardLevel;
  private String sweepstakesSecondaryAwardLevel;
  private String behaviorsActive;
  private int numberOfBehaviors;
  private String eCardsActive;
  private String certificatesActive;
  private int numberOfeCards;
  private int numberOfCertificates;
  private String approvalType;
  private String approvalTypeCode;
  private String approverType;
  private String approverTypeCode;
  private String approvalAutoDelayDays;
  private String approvalConditionalClaimCount;
  private String approvalConditionalAmountField;
  private String approvalConditionalAmountOperator;
  private String approvalConditionalAmount;
  private String approvalSubmitterSize;
  private String approvalApproversSize;
  private String approvalStartDate;
  private String approvalEndDate;
  private int approvalOptions;
  private String method;
  private String webRulesStartDate;
  private String webRulesEndDate;
  private boolean webRulesSameAsPrimary;
  private boolean webRulesAllPax;
  private boolean webRulesSpecifyAudience;
  private boolean webRulesAllEligiblePrimary;
  private boolean webRulesAllEligiblePrimaryAndSecondary;
  private boolean webRulesAllEligibleSecondary;
  private String webRulesAudienceTypeName;
  private Long version;
  private boolean webRulesActive;
  private String sweepsWinnerEligibilityCode;
  private boolean includePurl;
  private boolean allowPublicRecognition;
  private boolean allowYourOwnCard;
  private boolean drawYourOwnCard;
  private String purlPromotionMediaType;
  private String purlMediaValue;
  private boolean allowRecognitionSendDate;
  private boolean whyNomination;

  // ssi promotion properties
  private String selectedContests;
  private String creatorNotificationsEmailCount;
  private String approverNotificationsEmailCount;
  private String awardThemNowContestEmailCount;
  private String doThisGetThatContestEmailCount;
  private String objectivesContestEmailCount;
  private String stackRankContestEmailCount;
  private String stepItUpContestEmailCount;
  private Integer maxContestsToDisplay;
  private Integer daysToArchive;
  private String contestGuideUrl;

  // ssi awards
  private boolean pointsAvailable;
  private boolean merchandiseAvailable;
  private boolean otherAvailable;
  private String badgeCount;
  private Boolean allowSpreadSheet;
  private Boolean allowClaimSubmission;
  private Boolean requireClaimApproval;
  private String claimApprovalAudienceTypeName;
  private List claimApprovalList;
  // ssi approvals
  private Boolean reqireApproval;
  private Integer numberOfDaysToApproveClaim;
  private Integer numberOfApproverLevels;
  private List level1ApproversList;
  private List level2ApproversList;

  private List submitterAudienceList;
  private List teamAudienceList;
  private List webRulesAudienceList;

  private Set childPromotions;

  private boolean notificationExists;
  private boolean submitterAudienceExists;
  private boolean teamAudienceExists;

  private String scoreBy;
  private String calculatorName;
  private Long calculatorId;

  private String surveySubmissionStartDate;
  private String surveySubmissionEndDate;

  // ChallengePoint Specific Fields
  private String challengePointAwardType;
  private boolean managerSelect = false;
  private String awardThresholdType;
  private String awardThresholdValue;
  private String awardIncrementType;
  private String awardIncrementValue;
  private String awardPerIncrement;

  private boolean managerAwardPromotion;
  private String managerAwardPromotionId;
  private String managerPromotionName;

  private boolean publicationDateActive;
  private String publicationDate = DateUtils.displayDateFormatMask;
  private String goalSelectionStartDate;
  private String goalSelectionEndDate;
  private String finalProcessDate;

  // GoalQuest Fields
  private String achievementRule;
  private String payoutStructure;
  private String achievementPrecision;
  private String roundingMethod;
  private boolean approveAwards;
  private String overrideStructure;
  private String programId;
  private boolean selfEnrollment;
  private String progressLoadTypeName;
  private String baseUnit;
  private String baseUnitPosition;

  private String awardStructure;
  private boolean featuredAwardsEnabled;
  private String whatsNewDescription;

  private boolean partnerAudienceExists = false;
  private List partnerAudienceList;
  private String partnerPayoutStructure;
  private String preSelectedPartner;
  private boolean autoCompletePartners = false;

  private String openEnrollmentEnabled;
  private String selfRecognitionEnabled;
  private String budgetSweepEnabled;
  private String budgetSweepDate;

  private boolean allowPublicRecognitionPoints;
  private String publicRecognitionAward;
  private String publicRecognitionBudget;
  private List publicRecognitionGiversList;
  private boolean publicRecognitionGiversExists;
  private boolean publicRecognitionGiverAllPax;

  private String billCodesActive;
  private String behaviorRequired;

  private String tileDisplayStartDate;
  private String tileDisplayEndDate;

  private List managerWebRulesAudienceList;
  private List partnerWebRulesAudienceList;
  private boolean managerWebRulesSpecifyAudience;
  private boolean partnerWebRulesSpecifyAudience;
  private String managerWebRulesAudienceTypeName;
  private String partnerWebRulesAudienceTypeName;
  private boolean managerWebRulesAllPax;
  private boolean partnerWebRulesAllPax;
  private boolean managerWebRulesAllEligible;
  private boolean partnerWebRulesAllEligible;
  public boolean partnerAvailable;

  // Throwdown Basic Fields
  private String promotionTheme;
  private String unevenPlaySelection;
  private boolean displayTeamProgress = true;
  private boolean smackTalkAvailable = true;
  private String daysPriorToRoundStartSchedule;

  // Throwdown Audience Fields
  private Map divisionAudienceMap;
  private boolean divisionAudienceExists;

  // Throwdown Payout Fields
  private int numberOfRounds;
  private int numberOfDayPerRound;
  private String startDateForFirstRound;

  private boolean includeCelebrations;
  private boolean serviceAnniversary;
  private String anniversaryInYears;
  private String celebrationDisplayPeriod;
  private boolean allowOwnerMessage;
  private boolean shareToMedia;

  private boolean corpAndMngr;

  // Recognition MobApp field
  private boolean mobAppEnabled;

  // PURL - Auto Contribution
  private boolean purlStandardMessageEnabled = false;

  // Engagement Promotion Fields
  private List<String> engagementEligiblePromotionList;
  private String scoreActive;
  private String companyGoal;
  private String approvalLevel;
  private boolean levelSelectionByApprover;// Client customization for WIP #56492  
  //Client customization for WIP #58122 starts
  private boolean levelPayoutByApproverAvailable;
  private Long capPerPax;
 // Client customization for WIP #58122 ends
  private boolean viewPastWinners = false;

  private boolean allowUnderArmour;
  /*Customization WIP#42198 start*/
  private String  cashEnabled;
  private Long  maxAwardInUSD;
  

	public Long getMaxAwardInUSD() {
		return maxAwardInUSD;
	}
	
	public void setMaxAwardInUSD(Long maxAwardInUSD) {
		this.maxAwardInUSD = maxAwardInUSD;
	}
  public String getCashEnabled() {
		return cashEnabled;
	}

	public void setCashEnabled(String cashEnabled) {
		this.cashEnabled = cashEnabled;
	}
	/*Customization WIP#42198 End*/
  public boolean isSelfEnrollment()
  {
    return selfEnrollment;
  }

  public void setSelfEnrollment( boolean selfEnrollment )
  {
    this.selfEnrollment = selfEnrollment;
  }

  public String getPublicationDate()
  {
    return publicationDate;
  }

  public void setPublicationDate( String publicationDate )
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

  public String getSurveySubmissionEndDate()
  {
    return surveySubmissionEndDate;
  }

  public void setSurveySubmissionEndDate( String surveySubmissionEndDate )
  {
    this.surveySubmissionEndDate = surveySubmissionEndDate;
  }

  public String getSurveySubmissionStartDate()
  {
    return surveySubmissionStartDate;
  }

  public void setSurveySubmissionStartDate( String surveySubmissionStartDate )
  {
    this.surveySubmissionStartDate = surveySubmissionStartDate;
  }

  public List<String> getEngagementEligiblePromotionList()
  {
    return engagementEligiblePromotionList;
  }

  public void setEngagementEligiblePromotionList( List<String> engagementEligiblePromotionList )
  {
    this.engagementEligiblePromotionList = engagementEligiblePromotionList;
  }

  /**
   * Reset all properties to their default values.
   *
   * @param mapping The mapping used to select this instance
   * @param request The servlet request we are processing
   */
  public void reset( ActionMapping mapping, HttpServletRequest request )
  {
    promotionId = null;
    parentPromotionId = null;
    parentPromotionName = null;
    promotionName = null;
    promotionType = null;
    promotionTypeCode = null;
    promotionStatus = null;
    claimFormName = null;
    claimFormAsset = null;
    claimFormId = null;
    claimSubmissionStartDate = null;
    claimSubmissionEndDate = null;
    individualOrTeamBased = null;
    evaluated = null;
    attemptsAllowed = null;
    processingMode = null;
    includeCertificate = false;
    copyRecipientManager = false;
    copyOthers = false;
    allPaxAsSubmitters = false;
    selfEnrollOnly = false;
    allPaxAsReceivers = false;
    teamUsed = false;
    teamSameAsSubmitters = false;
    teamFromSubmitterNode = false;
    teamFromSubmitterNodeBelow = false;
    teamSpecifyAudience = false;
    teamCollectedAsGroup = false;
    payoutManager = false;
    taxable = false;
    selfNomination = false;
    levelSelectionByApprover = false;//Client customization for WIP #56492
    levelPayoutByApproverAvailable = false;  // Client customization for WIP 58122
    allowPublicRecognition = false;
    numberOfProducts = 0;
    payoutManagerPercent = 0;
    payoutManagerPeriod = null;
    payoutType = null;
    awardsActive = null;
    awardType = null;
    award = null;
    budgetName = null;
    sweepstakesActive = null;
    awardTypeText = null;
    numberOrPercentOfGivers = null;
    numberOrPercentOfReceivers = null;
    amountOfGiversAwards = null;
    amountOfReceiversAwards = null;
    behaviorsActive = null;
    numberOfBehaviors = 0;
    eCardsActive = null;
    certificatesActive = null;
    numberOfeCards = 0;
    numberOfCertificates = 0;
    approvalType = null;
    approvalTypeCode = null;
    approverType = null;
    approverTypeCode = null;
    approvalAutoDelayDays = null;
    approvalConditionalClaimCount = null;
    approvalConditionalAmountField = null;
    approvalConditionalAmountOperator = null;
    approvalConditionalAmount = null;
    approvalSubmitterSize = null;
    approvalApproversSize = null;
    approvalStartDate = null;
    approvalEndDate = null;
    approvalOptions = 0;
    method = null;
    webRulesStartDate = null;
    webRulesEndDate = null;
    webRulesSameAsPrimary = false;
    webRulesAllPax = false;
    webRulesSpecifyAudience = false;
    webRulesAllEligiblePrimary = false;
    webRulesAllEligiblePrimaryAndSecondary = false;
    webRulesAllEligibleSecondary = false;
    version = null;
    webRulesActive = false;
    childPromotions = null;
    sweepsWinnerEligibilityCode = null;
    sweepstakesPrimaryAwardLevel = null;
    sweepstakesSecondaryAwardLevel = null;

    submitterAudienceList = new ArrayList();
    teamAudienceList = new ArrayList();
    webRulesAudienceList = new ArrayList();
    partnerAudienceList = new ArrayList();
    partnerAudienceExists = false;
    engagementEligiblePromotionList = new ArrayList<String>();

    scoreBy = null;
    calculatorId = null;
    calculatorName = null;

    surveySubmissionStartDate = null;
    surveySubmissionEndDate = null;
    corpAndMngr = false;

    managerAwardPromotion = false;
    managerPromotionName = null;
    managerAwardPromotionId = null;

    publicationDateActive = false;
    publicationDate = null;

    goalSelectionStartDate = null;
    goalSelectionEndDate = null;

    achievementRule = null;
    payoutStructure = null;
    achievementPrecision = null;
    roundingMethod = null;
    approveAwards = false;
    overrideStructure = null;
    programId = null;
    selfEnrollment = false;
    progressLoadTypeName = null;
    baseUnit = null;
    baseUnitPosition = null;

    // challengepoint fields
    awardThresholdType = null;
    awardThresholdValue = null;
    awardIncrementType = null;
    awardIncrementValue = null;
    challengePointAwardType = null;
    awardPerIncrement = null;
    challengePointAwardType = null;
    managerSelect = false;

    allowPublicRecognitionPoints = false;
    publicRecognitionAward = null;
    publicRecognitionBudget = null;
    publicRecognitionGiversList = new ArrayList();
    publicRecognitionGiversExists = false;
    publicRecognitionGiverAllPax = false;

    billCodesActive = null;

    preSelectedPartner = null;
    tileDisplayStartDate = null;
    tileDisplayEndDate = null;

    managerWebRulesAudienceList = new ArrayList();
    partnerWebRulesAudienceList = new ArrayList();
    managerWebRulesSpecifyAudience = false;
    partnerWebRulesSpecifyAudience = false;
    managerWebRulesAllEligible = false;
    partnerWebRulesAllEligible = false;
    partnerAvailable = false;

    divisionAudienceMap = new HashMap();
    numberOfRounds = 0;
    numberOfDayPerRound = 0;
    startDateForFirstRound = DateUtils.displayDateFormatMask;
    daysPriorToRoundStartSchedule = null;

    mobAppEnabled = false;
    purlStandardMessageEnabled = false;
    includeCelebrations = false;
    anniversaryInYears = null;
    celebrationDisplayPeriod = null;
    allowOwnerMessage = false;
    shareToMedia = false;
    approvalLevel = null;

    // ssi
    selectedContests = null;
    creatorNotificationsEmailCount = null;
    approverNotificationsEmailCount = null;
    awardThemNowContestEmailCount = null;
    doThisGetThatContestEmailCount = null;
    objectivesContestEmailCount = null;
    stackRankContestEmailCount = null;
    stepItUpContestEmailCount = null;
    maxContestsToDisplay = null;
    daysToArchive = null;
    contestGuideUrl = "";
    viewPastWinners = false;
    /*Customization WIP#42198 start*/
    cashEnabled= null;
    maxAwardInUSD= null;
    /*Customization WIP#42198 End*/
  }

  /**
   * Load the promotion to the form
   *
   * @param promotion
   */
  public void loadPromotion( Promotion promotion )
  {
    this.promotionId = promotion.getId();
    this.promotionName = promotion.getPromotionName();
    this.promotionType = promotion.getPromotionType().getName();
    this.promotionTypeCode = promotion.getPromotionType().getCode();
    this.promotionStatus = promotion.getPromotionStatus().getCode();
    this.promotionStatusDesc = promotion.getPromotionStatus().getName();
    this.claimSubmissionStartDate = DateUtils.toDisplayString( promotion.getSubmissionStartDate() );
    this.claimSubmissionEndDate = DateUtils.toDisplayString( promotion.getSubmissionEndDate() );

    if ( promotion.getAwardType() != null )
    {
      this.awardType = promotion.getAwardType().getCode();
      this.awardTypeText = promotion.getAwardType().getName();
    }

    this.taxable = promotion.isTaxable();

    if ( promotion.getApprovalType() != null )
    {
      this.approvalTypeCode = promotion.getApprovalType().getCode();
      this.approvalType = promotion.getApprovalType().getName();
    }
    if ( promotion.getApprovalAutoDelayDays() != null )
    {
      this.approvalAutoDelayDays = promotion.getApprovalAutoDelayDays().toString();
    }
    if ( promotion.getApprovalConditionalClaimCount() != null )
    {
      this.approvalConditionalClaimCount = promotion.getApprovalConditionalClaimCount().toString();
    }
    if ( promotion.getApprovalConditionalAmountField() != null )
    {
      this.approvalConditionalAmountField = promotion.getApprovalConditionalAmountField().getCmKeyForElementLabel();
    }
    if ( promotion.getApprovalConditionalAmountOperator() != null )
    {
      this.approvalConditionalAmountOperator = promotion.getApprovalConditionalAmountOperator().getName();
    }
    if ( promotion.getApprovalConditionalAmount() != null )
    {
      this.approvalConditionalAmount = promotion.getApprovalConditionalAmount().toString();
    }
    if ( promotion.getPromotionParticipantSubmitters() != null )
    {
      this.approvalSubmitterSize = String.valueOf( promotion.getPromotionParticipantSubmitters().size() );
    }
    if ( promotion.getPromotionParticipantApprovers() != null )
    {
      this.approvalApproversSize = String.valueOf( promotion.getPromotionParticipantApprovers().size() );
    }
    if ( promotion.getApproverType() != null )
    {
      this.approverTypeCode = promotion.getApproverType().getCode();
      this.approverType = promotion.getApproverType().getName();
    }
    if ( promotion.getApprovalStartDate() != null )
    {
      this.approvalStartDate = DateUtils.toDisplayString( promotion.getApprovalStartDate() );
    }
    if ( promotion.getApprovalEndDate() != null )
    {
      this.approvalEndDate = DateUtils.toDisplayString( promotion.getApprovalEndDate() );
    }
    if ( promotion.getPromotionApprovalOptions() != null )
    {
      this.approvalOptions = promotion.getPromotionApprovalOptions().size();
    }
    if ( promotion.getApprovalNodeLevels() != null )
    {
      this.approvalLevel = promotion.getApprovalNodeLevels().toString();
    }
    this.webRulesStartDate = DateUtils.toDisplayString( promotion.getWebRulesStartDate() );
    this.webRulesEndDate = DateUtils.toDisplayString( promotion.getWebRulesEndDate() );
    this.webRulesSpecifyAudience = promotion.getPromotionWebRulesAudiences().size() > 0;
    this.webRulesActive = promotion.isWebRulesActive();
    if ( promotion.getWebRulesAudienceType() != null )
    {
      this.webRulesAudienceTypeName = promotion.getWebRulesAudienceType().getName();
    }
    this.version = promotion.getVersion();

    Iterator webRulesAudiences = promotion.getPromotionWebRulesAudiences().iterator();

    while ( webRulesAudiences.hasNext() )
    {
      this.webRulesAudienceList.add( webRulesAudiences.next() );
    }

    this.billCodesActive = promotion.isBillCodesActive() ? "Yes" : "No";
    this.tileDisplayStartDate = DateUtils.toDisplayString( promotion.getTileDisplayStartDate() );
    this.tileDisplayEndDate = DateUtils.toDisplayString( promotion.getTileDisplayEndDate() );

    if ( promotion.isProductClaimPromotion() )
    {
      loadProductClaimPromotion( (ProductClaimPromotion)promotion );
    }
    else if ( promotion.isRecognitionPromotion() )
    {
      loadRecognitionPromotion( (RecognitionPromotion)promotion );
    }
    else if ( promotion.isQuizPromotion() || promotion.isDIYQuizPromotion() )
    {
      loadQuizPromotion( (QuizPromotion)promotion );
    }
    else if ( promotion.isNominationPromotion() )
    {
      loadNominationPromotion( (NominationPromotion)promotion );
    }
    else if ( promotion.isEngagementPromotion() )
    {
      loadEngagementPromotion( (EngagementPromotion)promotion );
    }
    else if ( promotion.isSurveyPromotion() )
    {
      loadSurveyPromotion( (SurveyPromotion)promotion );
    }
    else if ( promotion.isGoalQuestPromotion() )
    {
      loadGoalQuestPromotion( (GoalQuestPromotion)promotion );
    }
    else if ( promotion.isChallengePointPromotion() )
    {
      loadChallengePointPromotion( (ChallengePointPromotion)promotion );
    }
    else if ( promotion.isWellnessPromotion() )
    {
      loadWellnessPromotion( (WellnessPromotion)promotion );
    }
    else if ( promotion.isThrowdownPromotion() )
    {
      loadThrowdownPromotion( (ThrowdownPromotion)promotion );
    }
    else if ( promotion.isSSIPromotion() )
    {
      loadSSIPromotion( (SSIPromotion)promotion );
    }
  }

  /**
   * @return webRulesSpecifyAudience
   */
  public boolean isWebRulesSpecifyAudience()
  {
    return webRulesSpecifyAudience;
  }

  /**
   * @param webRulesSpecifyAudience
   */
  public void setWebRulesSpecifyAudience( boolean webRulesSpecifyAudience )
  {
    this.webRulesSpecifyAudience = webRulesSpecifyAudience;
  }

  /**
   * @return approvalType
   */
  public boolean isWebRulesActive()
  {
    return this.webRulesActive;
  }

  /**
   * @return approvalType
   */
  public String getApprovalType()
  {
    return approvalType;
  }

  /**
   * @param approvalType
   */
  public void setApprovalType( String approvalType )
  {
    this.approvalType = approvalType;
  }

  /**
   * @return method
   */
  public String getMethod()
  {
    return method;
  }

  /**
   * @param method
   */
  public void setMethod( String method )
  {
    this.method = method;
  }

  /**
   * @return payoutType
   */
  public String getPayoutType()
  {
    return payoutType;
  }

  /**
   * @param payoutType
   */
  public void setPayoutType( String payoutType )
  {
    this.payoutType = payoutType;
  }

  /**
   * @return processingMode
   */
  public String getProcessingMode()
  {
    return processingMode;
  }

  /**
   * @param processingMode
   */
  public void setProcessingMode( String processingMode )
  {
    this.processingMode = processingMode;
  }

  /**
   * @return promotionId
   */
  public Long getPromotionId()
  {
    return promotionId;
  }

  /**
   * @param promotionId
   */
  public void setPromotionId( Long promotionId )
  {
    this.promotionId = promotionId;
  }

  /**
   * @return promotionName
   */
  public String getPromotionName()
  {
    return promotionName;
  }

  /**
   * @param promotionName
   */
  public void setPromotionName( String promotionName )
  {
    this.promotionName = promotionName;
  }

  /**
   * @return promotionType
   */
  public String getPromotionType()
  {
    return promotionType;
  }

  /**
   * @param promotionType
   */
  public void setPromotionType( String promotionType )
  {
    this.promotionType = promotionType;
  }

  /**
   * @return claimFormName
   */
  public String getClaimFormName()
  {
    return claimFormName;
  }

  /**
   * @param activityForm
   */
  public void setClaimFormName( String activityForm )
  {
    this.claimFormName = activityForm;
  }

  /**
   * @return claimSubmissionEndDate
   */
  public String getClaimSubmissionEndDate()
  {
    return claimSubmissionEndDate;
  }

  /**
   * @param claimSubmissionEndDate
   */
  public void setClaimSubmissionEndDate( String claimSubmissionEndDate )
  {
    this.claimSubmissionEndDate = claimSubmissionEndDate;
  }

  /**
   * @return claimSubmissionStartDate
   */
  public String getClaimSubmissionStartDate()
  {
    return claimSubmissionStartDate;
  }

  /**
   * @param claimSubmissionStartDate
   */
  public void setClaimSubmissionStartDate( String claimSubmissionStartDate )
  {
    this.claimSubmissionStartDate = claimSubmissionStartDate;
  }

  /**
   * @return promotionStatus
   */
  public String getPromotionStatus()
  {
    return promotionStatus;
  }

  /**
   * @return true if promotion is Under Construction
   */
  public boolean isUnderConstruction()
  {
    return promotionStatus.equals( PromotionStatusType.UNDER_CONSTRUCTION );
  }

  /**
   * @return true if promotion is Expired
   */
  public boolean isExpired()
  {
    return promotionStatus.equals( PromotionStatusType.EXPIRED );
  }

  /**
   * @return true if promotion is Complete
   */
  public boolean isComplete()
  {
    return promotionStatus.equals( PromotionStatusType.COMPLETE );
  }

  /**
   * @return true if promotion is Live
   */
  public boolean isLive()
  {
    return promotionStatus.equals( PromotionStatusType.LIVE );
  }

  /**
   * @param promotionStatus
   */
  public void setPromotionStatus( String promotionStatus )
  {
    this.promotionStatus = promotionStatus;
  }

  /**
   * @return allPaxAsSubmitters
   */
  public boolean isAllPaxAsSubmitters()
  {
    return allPaxAsSubmitters;
  }

  /**
   * @param allPaxAsSubmitters
   */
  public void setAllPaxAsSubmitters( boolean allPaxAsSubmitters )
  {
    this.allPaxAsSubmitters = allPaxAsSubmitters;
  }

  /**
   * @return teamCollectedAsGroup
   */
  public boolean isTeamCollectedAsGroup()
  {
    return teamCollectedAsGroup;
  }

  /**
   * @param teamCollectedAsGroup
   */
  public void setTeamCollectedAsGroup( boolean teamCollectedAsGroup )
  {
    this.teamCollectedAsGroup = teamCollectedAsGroup;
  }

  /**
   * @return teamSpecifyAudience
   */
  public boolean isTeamFromSubmitterNode()
  {
    return teamFromSubmitterNode;
  }

  /**
   * @param teamFromSubmitterNode
   */
  public void setTeamFromSubmitterNode( boolean teamFromSubmitterNode )
  {
    this.teamFromSubmitterNode = teamFromSubmitterNode;
  }

  /**
   * @return teamSpecifyAudience
   */
  public boolean isTeamSpecifyAudience()
  {
    return teamSpecifyAudience;
  }

  /**
   * @param teamSpecifyAudience
   */
  public void setTeamSpecifyAudience( boolean teamSpecifyAudience )
  {
    this.teamSpecifyAudience = teamSpecifyAudience;
  }

  /**
   * @return teamSameAsSubmitters
   */
  public boolean isTeamSameAsSubmitters()
  {
    return teamSameAsSubmitters;
  }

  /**
   * @param teamSameAsSubmitters
   */
  public void setTeamSameAsSubmitters( boolean teamSameAsSubmitters )
  {
    this.teamSameAsSubmitters = teamSameAsSubmitters;
  }

  /**
   * @return teamUsed
   */
  public boolean isTeamUsed()
  {
    return teamUsed;
  }

  /**
   * @param teamUsed
   */
  public void setTeamUsed( boolean teamUsed )
  {
    this.teamUsed = teamUsed;
  }

  /**
   * @return taxable
   */
  public boolean isTaxable()
  {
    return taxable;
  }

  /**
   * @param taxable
   */
  public void setTaxable( boolean taxable )
  {
    this.taxable = taxable;
  }

  /**
   * @return webRulesEndDate
   */
  public String getWebRulesEndDate()
  {
    return webRulesEndDate;
  }

  /**
   * @param webRulesEndDate
   */
  public void setWebRulesEndDate( String webRulesEndDate )
  {
    this.webRulesEndDate = webRulesEndDate;
  }

  /**
   * @return webRulesStartDate
   */
  public String getWebRulesStartDate()
  {
    return webRulesStartDate;
  }

  /**
   * @param webRulesStartDate
   */
  public void setWebRulesStartDate( String webRulesStartDate )
  {
    this.webRulesStartDate = webRulesStartDate;
  }

  /**
   * @return webRulesAllPax
   */
  public boolean isWebRulesAllPax()
  {
    return webRulesAllPax;
  }

  /**
   * @param webRulesAllPax
   */
  public void setWebRulesAllPax( boolean webRulesAllPax )
  {
    this.webRulesAllPax = webRulesAllPax;
  }

  /**
   * @return webRulesSameAsPrimary
   */
  public boolean isWebRulesSameAsPrimary()
  {
    return webRulesSameAsPrimary;
  }

  /**
   * @param webRulesSameAsPrimary
   */
  public void setWebRulesSameAsPrimary( boolean webRulesSameAsPrimary )
  {
    this.webRulesSameAsPrimary = webRulesSameAsPrimary;
  }

  /**
   * @return claimFormId
   */
  public Long getClaimFormId()
  {
    return claimFormId;
  }

  /**
   * @param claimFormId
   */
  public void setClaimFormId( Long claimFormId )
  {
    this.claimFormId = claimFormId;
  }

  /**
   * @return version
   */
  public Long getVersion()
  {
    return version;
  }

  /**
   * @param version
   */
  public void setVersion( Long version )
  {
    this.version = version;
  }

  /**
   * @return approverType
   */
  public String getApproverType()
  {
    return approverType;
  }

  /**
   * @param approverType
   */
  public void setApproverType( String approverType )
  {
    this.approverType = approverType;
  }

  /**
   * @return approvalOptions
   */
  public int getApprovalOptions()
  {
    return approvalOptions;
  }

  /**
   * @param approvalOptions
   */
  public void setApprovalOptions( int approvalOptions )
  {
    this.approvalOptions = approvalOptions;
  }

  /**
   * @return approvalEndDate
   */
  public String getApprovalEndDate()
  {
    return approvalEndDate;
  }

  /**
   * @param approvalEndDate
   */
  public void setApprovalEndDate( String approvalEndDate )
  {
    this.approvalEndDate = approvalEndDate;
  }

  /**
   * @return approvalStartDate
   */
  public String getApprovalStartDate()
  {
    return approvalStartDate;
  }

  /**
   * @param approvalStartDate
   */
  public void setApprovalStartDate( String approvalStartDate )
  {
    this.approvalStartDate = approvalStartDate;
  }

  /**
   * @return payoutManager
   */
  public boolean isPayoutManager()
  {
    return payoutManager;
  }

  /**
   * @param payoutManager
   */
  public void setPayoutManager( boolean payoutManager )
  {
    this.payoutManager = payoutManager;
  }

  /**
   * @return payoutManagerPercent
   */
  public int getPayoutManagerPercent()
  {
    return payoutManagerPercent;
  }

  /**
   * @param payoutManagerPercent
   */
  public void setPayoutManagerPercent( int payoutManagerPercent )
  {
    this.payoutManagerPercent = payoutManagerPercent;
  }

  /**
   * @return payoutManagerPeriod
   */
  public String getPayoutManagerPeriod()
  {
    return payoutManagerPeriod;
  }

  /**
   * @param payoutManagerPeriod
   */
  public void setPayoutManagerPeriod( String payoutManagerPeriod )
  {
    this.payoutManagerPeriod = payoutManagerPeriod;
  }

  /**
   * @return numberOfProducts
   */
  public int getNumberOfProducts()
  {
    return numberOfProducts;
  }

  /**
   * @param numberOfProducts
   */
  public void setNumberOfProducts( int numberOfProducts )
  {
    this.numberOfProducts = numberOfProducts;
  }

  /**
   * @return claimFormAsset
   */
  public String getClaimFormAsset()
  {
    return claimFormAsset;
  }

  /**
   * @param claimFormAsset
   */
  public void setClaimFormAsset( String claimFormAsset )
  {
    this.claimFormAsset = claimFormAsset;
  }

  /**
   * @return parentPromotionId
   */
  public Long getParentPromotionId()
  {
    return parentPromotionId;
  }

  /**
   * @param parentPromotionId
   */
  public void setParentPromotionId( Long parentPromotionId )
  {
    this.parentPromotionId = parentPromotionId;
  }

  /**
   * @return parentPromotionName
   */
  public String getParentPromotionName()
  {
    return parentPromotionName;
  }

  /**
   * @param parentPromotionName
   */
  public void setParentPromotionName( String parentPromotionName )
  {
    this.parentPromotionName = parentPromotionName;
  }

  /**
   * @return childPromotions
   */
  public Set getChildPromotions()
  {
    return childPromotions;
  }

  /**
   * @param childPromotions
   */
  public void setChildPromotions( Set childPromotions )
  {
    this.childPromotions = childPromotions;
  }

  /**
   * @return submitterAudienceList
   */
  public List getSubmitterAudienceList()
  {
    return submitterAudienceList;
  }

  /**
   * @param submitterAudienceList
   */
  public void setSubmitterAudienceList( List submitterAudienceList )
  {
    this.submitterAudienceList = submitterAudienceList;
  }

  /**
   * @return teamAudienceList
   */
  public List getTeamAudienceList()
  {
    return teamAudienceList;
  }

  /**
   * @param teamAudienceList
   */
  public void setTeamAudienceList( List teamAudienceList )
  {
    this.teamAudienceList = teamAudienceList;
  }

  /**
   * @return webRulesAudienceList
   */
  public List getWebRulesAudienceList()
  {
    return webRulesAudienceList;
  }

  /**
   * @param webRulesAudienceList
   */
  public void setWebRulesAudienceList( List webRulesAudienceList )
  {
    this.webRulesAudienceList = webRulesAudienceList;
  }

  /**
   * @return approvalAutoDelayDays
   */
  public String getApprovalAutoDelayDays()
  {
    return approvalAutoDelayDays;
  }

  /**
   * @param approvalAutoDelayDays
   */
  public void setApprovalAutoDelayDays( String approvalAutoDelayDays )
  {
    this.approvalAutoDelayDays = approvalAutoDelayDays;
  }

  /**
   * @return approvalConditionalClaimCount
   */
  public String getApprovalConditionalClaimCount()
  {
    return approvalConditionalClaimCount;
  }

  /**
   * @param approvalConditionalClaimCount
   */
  public void setApprovalConditionalClaimCount( String approvalConditionalClaimCount )
  {
    this.approvalConditionalClaimCount = approvalConditionalClaimCount;
  }

  /**
   * @return approvalConditionalAmountField
   */
  public String getApprovalConditionalAmountField()
  {
    return approvalConditionalAmountField;
  }

  /**
   * @param approvalConditionalAmountField
   */
  public void setApprovalConditionalAmountField( String approvalConditionalAmountField )
  {
    this.approvalConditionalAmountField = approvalConditionalAmountField;
  }

  /**
   * @return approvalConditionalAmountOperator
   */
  public String getApprovalConditionalAmountOperator()
  {
    return approvalConditionalAmountOperator;
  }

  /**
   * @param approvalConditionalAmountOperator
   */
  public void setApprovalConditionalAmountOperator( String approvalConditionalAmountOperator )
  {
    this.approvalConditionalAmountOperator = approvalConditionalAmountOperator;
  }

  /**
   * @return approvalConditionalAmount
   */
  public String getApprovalConditionalAmount()
  {
    return approvalConditionalAmount;
  }

  /**
   * @param approvalConditionalAmount
   */
  public void setApprovalConditionalAmount( String approvalConditionalAmount )
  {
    this.approvalConditionalAmount = approvalConditionalAmount;
  }

  /**
   * @return approvalSubmitterSize
   */
  public String getApprovalSubmitterSize()
  {
    return approvalSubmitterSize;
  }

  /**
   * @param approvalSubmitterSize
   */
  public void setApprovalSubmitterSize( String approvalSubmitterSize )
  {
    this.approvalSubmitterSize = approvalSubmitterSize;
  }

  /**
   * @return approvalApproversSize
   */
  public String getApprovalApproversSize()
  {
    return approvalApproversSize;
  }

  /**
   * @param approvalApproversSize
   */
  public void setApprovalApproversSize( String approvalApproversSize )
  {
    this.approvalApproversSize = approvalApproversSize;
  }

  /**
   * @return approvalTypeCode
   */
  public String getApprovalTypeCode()
  {
    return approvalTypeCode;
  }

  /**
   * @param approvalTypeCode
   */
  public void setApprovalTypeCode( String approvalTypeCode )
  {
    this.approvalTypeCode = approvalTypeCode;
  }

  /**
   * @return approverTypeCode
   */
  public String getApproverTypeCode()
  {
    return approverTypeCode;
  }

  /**
   * @param approverTypeCode
   */
  public void setApproverTypeCode( String approverTypeCode )
  {
    this.approverTypeCode = approverTypeCode;
  }

  /**
   * @return true if this promotion has a parent promotion; return false if not
   */
  public boolean isHasParent()
  {
    if ( parentPromotionId == null )
    {
      return false;
    }

    return parentPromotionId.longValue() > 0;
  }

  private void loadProductClaimPromotion( ProductClaimPromotion promotion )
  {
    this.claimFormName = promotion.getClaimForm().getName();
    this.claimFormId = promotion.getClaimForm().getId();
    this.claimFormAsset = promotion.getClaimForm().getCmAssetCode();

    if ( promotion.getParentPromotion() != null )
    {
      this.parentPromotionId = promotion.getParentPromotion().getId();
      this.parentPromotionName = promotion.getParentPromotion().getName();
      this.childPromotions = promotion.getParentPromotion().getChildPromotions();
    }
    else
    {
      if ( promotion.getChildPromotionCount() > 0 )
      {
        this.childPromotions = promotion.getChildPromotions();
      }
    }

    this.processingMode = promotion.getPromotionProcessingMode().getName();
    if ( promotion.getPrimaryAudienceType() != null )
    {
      this.allPaxAsSubmitters = promotion.getPrimaryAudienceType().equals( PrimaryAudienceType.lookup( PrimaryAudienceType.ALL_ACTIVE_PAX_CODE ) );
      this.entireParentAudience = promotion.getPrimaryAudienceType().equals( PrimaryAudienceType.lookup( PrimaryAudienceType.ENTIRE_PARENT_AUDIENCE_CODE ) );
    }

    this.teamUsed = promotion.isTeamUsed();
    if ( promotion.getSecondaryAudienceType() != null )
    {
      this.teamSameAsSubmitters = promotion.getSecondaryAudienceType().equals( SecondaryAudienceType.lookup( SecondaryAudienceType.SAME_AS_PRIMARY_CODE ) );
    }
    else
    {
      this.teamSameAsSubmitters = false;
    }
    if ( promotion.getSecondaryAudienceType() != null )
    {
      this.teamFromSubmitterNode = promotion.getSecondaryAudienceType().equals( SecondaryAudienceType.lookup( SecondaryAudienceType.ACTIVE_PAX_PRIMARY_NODE_CODE ) );
    }
    else
    {
      this.teamFromSubmitterNode = false;
    }
    this.teamSpecifyAudience = promotion.getPromotionSecondaryAudiences().size() > 0;
    this.teamCollectedAsGroup = promotion.isTeamCollectedAsGroup();
    if ( promotion.getPayoutType() != null )
    {
      this.payoutType = promotion.getPayoutType().getName();
    }

    this.payoutManager = promotion.isPayoutManager();
    if ( promotion.getPayoutManagerPercent() != null )
    {
      this.payoutManagerPercent = promotion.getPayoutManagerPercent().intValue();
    }
    if ( promotion.getPayoutManagerPeriod() != null )
    {
      this.payoutManagerPeriod = promotion.getPayoutManagerPeriod().getName();
    }
    boolean result2 = false;
    if ( promotion.getWebRulesAudienceType() != null && promotion.getWebRulesAudienceType().equals( WebRulesAudienceType.lookup( WebRulesAudienceType.SAME_AS_PRIMARY_CODE ) ) )
    {
      result2 = true;
    }
    this.webRulesSameAsPrimary = result2;
    boolean result3 = false;
    if ( promotion.getWebRulesAudienceType() != null && promotion.getWebRulesAudienceType().equals( WebRulesAudienceType.lookup( WebRulesAudienceType.ALL_ACTIVE_PAX_CODE ) ) )
    {
      result3 = true;
    }
    this.webRulesAllPax = result3;

    Iterator submitterAudiences = promotion.getPromotionPrimaryAudiences().iterator();
    Iterator teamAudiences = promotion.getPromotionSecondaryAudiences().iterator();

    while ( submitterAudiences.hasNext() )
    {
      this.submitterAudienceList.add( submitterAudiences.next() );
    }
    if ( this.submitterAudienceList.size() == 0 )
    {
      this.submitterAudienceExists = false;
    }
    else
    {
      this.submitterAudienceExists = true;
    }

    while ( teamAudiences.hasNext() )
    {
      this.teamAudienceList.add( teamAudiences.next() );
    }
    if ( this.teamAudienceList.size() == 0 )
    {
      this.teamAudienceExists = false;
    }
    else
    {
      this.teamAudienceExists = true;
    }

    this.sweepstakesActive = promotion.isSweepstakesActive() ? "Yes" : "No";

    SweepstakesWinnerEligibilityType eligibilityType = promotion.getSweepstakesWinnerEligibilityType();
    if ( eligibilityType != null )
    {
      this.sweepsWinnerEligibilityCode = eligibilityType.getCode();
      if ( eligibilityType.equals( SweepstakesWinnerEligibilityType.lookup( SweepstakesWinnerEligibilityType.SUBMITTERS_ONLY_CODE ) ) )
      {
        this.numberOrPercentOfGivers = promotion.getSweepstakesPrimaryWinners().toString();
        if ( promotion.getSweepstakesPrimaryBasisType().equals( SweepstakesWinnersType.lookup( SweepstakesWinnersType.PERCENTAGE_CODE ) ) )
        {
          this.numberOrPercentOfGivers = this.numberOrPercentOfGivers + " %";
        }
        this.amountOfGiversAwards = promotion.getSweepstakesPrimaryAwardAmount().toString();
      }
      else if ( eligibilityType.equals( SweepstakesWinnerEligibilityType.lookup( SweepstakesWinnerEligibilityType.TEAM_MEMBERS_ONLY_CODE ) ) )
      {
        this.numberOrPercentOfReceivers = promotion.getSweepstakesSecondaryWinners().toString();
        if ( promotion.getSweepstakesSecondaryBasisType().equals( SweepstakesWinnersType.lookup( SweepstakesWinnersType.PERCENTAGE_CODE ) ) )
        {
          this.numberOrPercentOfReceivers = this.numberOrPercentOfReceivers + " %";
        }
        this.amountOfReceiversAwards = promotion.getSweepstakesSecondaryAwardAmount().toString();
      }
      else if ( promotion.getSweepstakesPrimaryWinners() != null )
      {

        if ( promotion.getSweepstakesPrimaryWinners() != null )
        {
          this.numberOrPercentOfGivers = promotion.getSweepstakesPrimaryWinners().toString();
        }

        if ( promotion.getSweepstakesPrimaryBasisType() != null && promotion.getSweepstakesPrimaryBasisType().equals( SweepstakesWinnersType.lookup( SweepstakesWinnersType.PERCENTAGE_CODE ) ) )
        {
          this.numberOrPercentOfGivers = this.numberOrPercentOfGivers + " %";
        }

        if ( promotion.getSweepstakesPrimaryAwardAmount() != null )
        {
          this.amountOfGiversAwards = promotion.getSweepstakesPrimaryAwardAmount().toString();
        }

        if ( promotion.getSweepstakesSecondaryWinners() != null )
        {
          this.numberOrPercentOfReceivers = promotion.getSweepstakesSecondaryWinners().toString();
        }

        if ( promotion.getSweepstakesSecondaryBasisType() != null && promotion.getSweepstakesSecondaryBasisType().equals( SweepstakesWinnersType.lookup( SweepstakesWinnersType.PERCENTAGE_CODE ) ) )
        {
          this.numberOrPercentOfReceivers = this.numberOrPercentOfReceivers + " %";
        }

        if ( promotion.getSweepstakesSecondaryAwardAmount() != null )
        {
          this.amountOfReceiversAwards = promotion.getSweepstakesSecondaryAwardAmount().toString();
        }

      }
    }

    this.budgetName = "";
    if ( promotion.getBudgetMaster() != null )
    {
      this.budgetName = promotion.getBudgetMaster().getBudgetMasterName();
    }
  }

  private void loadRecognitionPromotion( RecognitionPromotion promotion )
  {
    // ******** Remove the purl & celebration promotions. **********
    if ( NewServiceAnniversaryUtil.isNewServiceAnniversaryEnabled() && ( promotion.isIncludePurl() || promotion.isIncludeCelebrations() ) )
    {
      return;
    }

    this.claimFormName = promotion.getClaimForm().getName();
    this.claimFormId = promotion.getClaimForm().getId();
    this.claimFormAsset = promotion.getClaimForm().getCmAssetCode();
    this.sweepstakesPrimaryAwardLevel = promotion.getSweepstakesPrimaryAwardLevel() + "";
    this.sweepstakesSecondaryAwardLevel = promotion.getSweepstakesSecondaryAwardLevel() + "";

    // this.processingMode = promotion.getIssuanceType().getName();
    this.featuredAwardsEnabled = promotion.isFeaturedAwardsEnabled();
    if ( promotion.isFileLoadEntry() )
    {
      this.processingMode = PromotionIssuanceType.lookup( PromotionIssuanceType.FILE_LOAD ).getName();
    }
    if ( promotion.isOnlineEntry() )
    {
      this.processingMode = PromotionIssuanceType.lookup( PromotionIssuanceType.ONLINE ).getName();
    }

    this.setIncludePurl( promotion.isIncludePurl() );

    if ( promotion.isIncludePurl() )
    {
      if ( promotion.getPurlPromotionMediaType() != null )
      {
        this.setPurlPromotionMediaType( promotion.getPurlPromotionMediaType().getCode() );
      }
      if ( promotion.getPurlMediaValue() != null )
      {
        this.setPurlMediaValue( promotion.getPurlMediaValue().getCode() );
      }
    }
    this.setCopyRecipientManager( promotion.isCopyRecipientManager() );
    this.setIncludeCertificate( promotion.isIncludeCertificate() );
    this.setCopyOthers( promotion.isCopyOthers() );

    this.setAllowPublicRecognition( promotion.isAllowPublicRecognition() );
    this.setAllowYourOwnCard( promotion.isAllowYourOwnCard() );
    this.setDrawYourOwnCard( promotion.isDrawYourOwnCard() );
    this.setAllowRecognitionSendDate( promotion.isAllowRecognitionSendDate() );
    this.awardsActive = promotion.isAwardActive() ? "Yes" : "No";

    if ( promotion.getAwardAmountFixed() != null )
    {
      this.award = promotion.getAwardAmountFixed().toString();
    }
    else if ( promotion.getAwardAmountMin() != null && promotion.getAwardAmountMax() != null )
    {
      this.award = promotion.getAwardAmountMin().toString() + " - " + promotion.getAwardAmountMax().toString();
    }
    // Calculator object
    else
    {
      if ( promotion.getCalculator() != null && promotion.getScoreBy() != null && promotion.getScoreBy().getCode() != null )
      {
        this.award = "Calculator Name - " + promotion.getCalculator().getName() + " And Score By - " + promotion.getScoreBy().getCode();
      }
    }
    this.budgetName = "";
    if ( promotion.getBudgetMaster() != null )
    {
      this.budgetName = promotion.getBudgetMaster().getBudgetMasterName();
    }

    this.behaviorsActive = promotion.isBehaviorActive() ? "Yes" : "No";
    if ( promotion.getPromotionBehaviors() != null )
    {
      this.numberOfBehaviors = promotion.getPromotionBehaviors().size();
    }

    this.sweepstakesActive = promotion.isSweepstakesActive() ? "Yes" : "No";

    SweepstakesWinnerEligibilityType eligibilityType = promotion.getSweepstakesWinnerEligibilityType();
    if ( eligibilityType != null )
    {
      this.sweepsWinnerEligibilityCode = eligibilityType.getCode();

      if ( eligibilityType.equals( SweepstakesWinnerEligibilityType.lookup( SweepstakesWinnerEligibilityType.GIVERS_ONLY_CODE ) ) )
      {
        this.numberOrPercentOfGivers = promotion.getSweepstakesPrimaryWinners().toString();
        if ( promotion.getSweepstakesPrimaryBasisType().equals( SweepstakesWinnersType.lookup( SweepstakesWinnersType.PERCENTAGE_CODE ) ) )
        {
          this.numberOrPercentOfGivers = this.numberOrPercentOfGivers + " %";
        }
        if ( null != promotion.getSweepstakesPrimaryAwardAmount() )
        {
          this.amountOfGiversAwards = promotion.getSweepstakesPrimaryAwardAmount().toString();
        }
      }
      else if ( eligibilityType.equals( SweepstakesWinnerEligibilityType.lookup( SweepstakesWinnerEligibilityType.RECEIVERS_ONLY_CODE ) ) )
      {
        this.numberOrPercentOfReceivers = promotion.getSweepstakesSecondaryWinners().toString();
        if ( promotion.getSweepstakesSecondaryBasisType().equals( SweepstakesWinnersType.lookup( SweepstakesWinnersType.PERCENTAGE_CODE ) ) )
        {
          this.numberOrPercentOfReceivers = this.numberOrPercentOfReceivers + " %";
        }
        if ( promotion.getSweepstakesSecondaryAwardAmount() != null )
        {
          this.amountOfReceiversAwards = promotion.getSweepstakesSecondaryAwardAmount().toString();
        }
      }
      else if ( eligibilityType.equals( SweepstakesWinnerEligibilityType.lookup( SweepstakesWinnerEligibilityType.GIVERS_AND_RECEIVERS_SEPARATE_CODE ) ) )
      {
        if ( promotion.getSweepstakesPrimaryWinners() != null )
        {
          this.numberOrPercentOfGivers = promotion.getSweepstakesPrimaryWinners().toString();
        }
        if ( promotion.getSweepstakesPrimaryBasisType() != null && promotion.getSweepstakesPrimaryBasisType().equals( SweepstakesWinnersType.lookup( SweepstakesWinnersType.PERCENTAGE_CODE ) ) )
        {
          this.numberOrPercentOfGivers = this.numberOrPercentOfGivers + " %";
        }

        if ( promotion.getSweepstakesPrimaryAwardAmount() != null )
        {
          this.amountOfGiversAwards = promotion.getSweepstakesPrimaryAwardAmount().toString();
        }

        if ( promotion.getSweepstakesSecondaryWinners() != null )
        {
          this.numberOrPercentOfReceivers = promotion.getSweepstakesSecondaryWinners().toString();
        }
        if ( promotion.getSweepstakesSecondaryBasisType() != null && promotion.getSweepstakesSecondaryBasisType().equals( SweepstakesWinnersType.lookup( SweepstakesWinnersType.PERCENTAGE_CODE ) ) )
        {
          this.numberOrPercentOfReceivers = this.numberOrPercentOfReceivers + " %";
        }
        if ( promotion.getSweepstakesSecondaryAwardAmount() != null )
        {
          this.amountOfReceiversAwards = promotion.getSweepstakesSecondaryAwardAmount().toString();
        }

      } // To fix 18208 inorder to display givers and receivers combined
      else if ( eligibilityType.equals( SweepstakesWinnerEligibilityType.lookup( SweepstakesWinnerEligibilityType.GIVERS_AND_RECEIVERS_COMBINED_CODE ) ) )
      {
        this.numberOrPercentOfGivers = promotion.getSweepstakesPrimaryWinners().toString();
        if ( promotion.getSweepstakesPrimaryBasisType().equals( SweepstakesWinnersType.lookup( SweepstakesWinnersType.PERCENTAGE_CODE ) ) )
        {
          this.numberOrPercentOfGivers = this.numberOrPercentOfGivers + " %";
        }
        if ( null != promotion.getSweepstakesPrimaryAwardAmount() )
        {
          this.amountOfGiversAwards = promotion.getSweepstakesPrimaryAwardAmount().toString();
        }
      }
    }

    loadAudienceInfo( promotion );

    this.eCardsActive = promotion.isCardActive() ? "Yes" : "No";
    this.numberOfeCards = promotion.getPromotionECard().size();
    this.numberOfCertificates = promotion.getPromotionCertificates().size();

    this.behaviorRequired = promotion.isBehaviorRequired() ? "Yes" : "No";

    // Fix for Bug #11501 - display audience correctly
    boolean result = false;
    if ( promotion.getWebRulesAudienceType() != null && promotion.getWebRulesAudienceType().equals( WebRulesAudienceType.lookup( WebRulesAudienceType.ALL_ACTIVE_PAX_CODE ) ) )
    {
      result = true;
    }
    this.webRulesAllPax = result;

    boolean result2 = false;
    if ( promotion.getWebRulesAudienceType() != null && promotion.getWebRulesAudienceType().equals( WebRulesAudienceType.lookup( WebRulesAudienceType.ALL_ELIGIBLE_PRIMARY_CODE ) ) )
    {
      result2 = true;
    }
    this.webRulesAllEligiblePrimary = result2;

    boolean result3 = false;
    if ( promotion.getWebRulesAudienceType() != null && promotion.getWebRulesAudienceType().equals( WebRulesAudienceType.lookup( WebRulesAudienceType.ALL_ELIGIBLE_SECONDARY_CODE ) ) )
    {
      result3 = true;
    }
    this.webRulesAllEligibleSecondary = result3;

    boolean result4 = false;
    if ( promotion.getWebRulesAudienceType() != null && promotion.getWebRulesAudienceType().equals( WebRulesAudienceType.lookup( WebRulesAudienceType.ALL_ELIGIBLE_PRIMARY_AND_SECONDARY_CODE ) ) )
    {
      result4 = true;
    }
    this.webRulesAllEligiblePrimaryAndSecondary = result4;
    // End Fix for Bug #11501 - display audience correctly

    this.managerPromotionName = "";
    if ( promotion.isAllowManagerAward() )
    {
      this.managerAwardPromotion = true;
      RecognitionPromotion managerPromotion = (RecognitionPromotion)getPromotionService().getPromotionById( promotion.getMgrAwardPromotionId() );
      this.managerPromotionName = managerPromotion.getName();
      managerAwardPromotionId = String.valueOf( promotion.getMgrAwardPromotionId() );
    }

    if ( promotion.getAwardType().equals( PromotionAwardsType.lookup( PromotionAwardsType.MERCHANDISE ) ) )
    {
      Set items = promotion.getHomePageItems();
      if ( items != null && !items.isEmpty() )
      {
        this.whatsNewDescription = ( (HomePageItem)items.iterator().next() ).getDescription();
      }
      else
      {
        this.whatsNewDescription = null;
      }
      this.awardStructure = promotion.getAwardStructure();
    }

    this.openEnrollmentEnabled = promotion.isOpenEnrollmentEnabled() ? "Yes" : "No";
    this.selfRecognitionEnabled = promotion.isSelfRecognitionEnabled() ? "Yes" : "No";
    this.budgetSweepEnabled = promotion.isBudgetSweepEnabled() ? "Yes" : "No";

    this.allowPublicRecognitionPoints = promotion.isAllowPublicRecognitionPoints();
    this.publicRecognitionAward = "";
    if ( promotion.getPublicRecogAwardAmountFixed() != null )
    {
      this.publicRecognitionAward = promotion.getPublicRecogAwardAmountFixed().toString();
    }
    else if ( promotion.getPublicRecogAwardAmountMin() != null && promotion.getPublicRecogAwardAmountMax() != null )
    {
      this.publicRecognitionAward = promotion.getPublicRecogAwardAmountMin().toString() + " - " + promotion.getPublicRecogAwardAmountMax().toString();
    }

    this.publicRecognitionBudget = "";
    if ( promotion.getPublicRecogBudgetMaster() != null )
    {
      this.publicRecognitionBudget = promotion.getPublicRecogBudgetMaster().getBudgetMasterName();
    }

    if ( null != promotion.getPublicRecognitionAudienceType() )
    {
      this.publicRecognitionGiverAllPax = promotion.getPublicRecognitionAudienceType().equals( PublicRecognitionAudienceType.lookup( PublicRecognitionAudienceType.ALL_ACTIVE_PAX_CODE ) );
    }

    Iterator publicRecognitionGivers = promotion.getPromotionPublicRecognitionAudiences().iterator();

    while ( publicRecognitionGivers.hasNext() )
    {
      this.publicRecognitionGiversList.add( publicRecognitionGivers.next() );
    }
    if ( this.publicRecognitionGiversList.size() == 0 )
    {
      this.publicRecognitionGiversExists = false;
    }
    else
    {
      this.publicRecognitionGiversExists = true;
    }

    this.mobAppEnabled = promotion.isMobAppEnabled();
    this.purlStandardMessageEnabled = promotion.isPurlStandardMessageEnabled();
    this.includeCelebrations = promotion.isIncludeCelebrations();
    this.serviceAnniversary = promotion.isServiceAnniversary();
    if ( promotion.isServiceAnniversary() && promotion.getAnniversaryInYears() != null )
    {
      this.anniversaryInYears = promotion.getAnniversaryInYears().booleanValue() ? "Years" : "Days";
    }
    // this.celebrationDisplayPeriod = promotion.getCelebrationDisplayPeriod().toString();
    this.allowOwnerMessage = promotion.isAllowOwnerMessage();
    this.shareToMedia = promotion.isShareToMedia();

  }

  private void loadQuizPromotion( QuizPromotion promotion )
  {
    if ( promotion.getQuiz() != null )
    {
      this.claimFormName = promotion.getQuiz().getName();
      this.claimFormId = promotion.getQuiz().getId();
      // this.claimFormAsset = promotion.getQuiz().getCmAssetCode();
    }

    if ( promotion.isAllowUnlimitedAttempts() )
    {
      this.attemptsAllowed = CmsResourceBundle.getCmsBundle().getString( "report.quizzes.quizactivity.UNLIMITED" );
    }
    else
    {
      this.attemptsAllowed = String.valueOf( promotion.getMaximumAttempts() );
    }

    this.includeCertificate = promotion.isIncludePassingQuizCertificate();
    if ( promotion.getPrimaryAudienceType() != null )
    {
      this.allPaxAsSubmitters = promotion.getPrimaryAudienceType().equals( PrimaryAudienceType.lookup( PrimaryAudienceType.ALL_ACTIVE_PAX_CODE ) );
    }
    Iterator submitterAudiences = promotion.getPromotionPrimaryAudiences().iterator();

    while ( submitterAudiences.hasNext() )
    {
      this.submitterAudienceList.add( submitterAudiences.next() );
    }
    if ( this.submitterAudienceList.size() == 0 )
    {
      this.submitterAudienceExists = false;
    }
    else
    {
      this.submitterAudienceExists = true;
    }

    this.awardsActive = promotion.isAwardActive() ? "Yes" : "No";

    if ( promotion.getAwardAmount() != null )
    {
      this.award = promotion.getAwardAmount().toString();
    }

    this.sweepstakesActive = promotion.isSweepstakesActive() ? "Yes" : "No";
    if ( promotion.getSweepstakesPrimaryWinners() != null )
    {
      this.numberOrPercentOfGivers = promotion.getSweepstakesPrimaryWinners().toString();
    }
    if ( promotion.getSweepstakesPrimaryBasisType() != null )
    {
      if ( promotion.getSweepstakesPrimaryBasisType().equals( SweepstakesWinnersType.lookup( SweepstakesWinnersType.PERCENTAGE_CODE ) ) )
      {
        this.numberOrPercentOfGivers = this.numberOrPercentOfGivers + " %";
      }
    }
    if ( promotion.getSweepstakesPrimaryAwardAmount() != null )
    {
      this.amountOfGiversAwards = promotion.getSweepstakesPrimaryAwardAmount().toString();
    }

    // Fix for Bug #11501 - display audience correctly
    boolean result = false;
    if ( promotion.getWebRulesAudienceType() != null && promotion.getWebRulesAudienceType().equals( WebRulesAudienceType.lookup( WebRulesAudienceType.ALL_ACTIVE_PAX_CODE ) ) )
    {
      result = true;
    }
    this.webRulesAllPax = result;

    boolean result2 = false;
    if ( promotion.getWebRulesAudienceType() != null && promotion.getWebRulesAudienceType().equals( WebRulesAudienceType.lookup( WebRulesAudienceType.ALL_ELIGIBLE_PRIMARY_CODE ) ) )
    {
      result2 = true;
    }
    this.webRulesAllEligiblePrimary = result2;
    // Fix for Bug #11501 - display audience correctly

    this.budgetName = "";
    if ( promotion.getBudgetMaster() != null )
    {
      this.budgetName = promotion.getBudgetMaster().getBudgetMasterName();
    }
  }

  private void loadWellnessPromotion( WellnessPromotion promotion )
  {

    if ( promotion.isFileLoadEntry() )
    {
      this.processingMode = PromotionIssuanceType.lookup( PromotionIssuanceType.FILE_LOAD ).getName();
    }
    if ( promotion.isOnlineEntry() )
    {
      this.processingMode = PromotionIssuanceType.lookup( PromotionIssuanceType.ONLINE ).getName();
    }

    if ( promotion.getPrimaryAudienceType() != null )
    {
      this.allPaxAsSubmitters = promotion.getPrimaryAudienceType().equals( PrimaryAudienceType.lookup( PrimaryAudienceType.ALL_ACTIVE_PAX_CODE ) );
    }
    Iterator submitterAudiences = promotion.getPromotionPrimaryAudiences().iterator();

    while ( submitterAudiences.hasNext() )
    {
      this.submitterAudienceList.add( submitterAudiences.next() );
    }
    if ( this.submitterAudienceList.size() == 0 )
    {
      this.submitterAudienceExists = false;
    }
    else
    {
      this.submitterAudienceExists = true;
    }

    this.awardsActive = promotion.isAwardActive() ? "Yes" : "No";

    if ( promotion.getAwardAmountFixed() != null )
    {
      this.award = promotion.getAwardAmountFixed().toString();
    }
    else if ( promotion.getAwardAmountMin() != null && promotion.getAwardAmountMax() != null )
    {
      this.award = promotion.getAwardAmountMin().toString() + " - " + promotion.getAwardAmountMax().toString();
    }

  }

  private void loadEngagementPromotion( EngagementPromotion promotion )
  {
    if ( promotion.getPrimaryAudienceType() != null )
    {
      this.allPaxAsSubmitters = promotion.getPrimaryAudienceType().equals( PrimaryAudienceType.lookup( PrimaryAudienceType.ALL_ACTIVE_PAX_CODE ) );
    }
    Iterator submitterAudiences = promotion.getPromotionPrimaryAudiences().iterator();

    while ( submitterAudiences.hasNext() )
    {
      this.submitterAudienceList.add( submitterAudiences.next() );
    }
    if ( this.submitterAudienceList.size() == 0 )
    {
      this.submitterAudienceExists = false;
    }
    else
    {
      this.submitterAudienceExists = true;
    }
    if ( promotion.getEngagementPromotions() != null )
    {
      this.engagementEligiblePromotionList = new ArrayList<String>();
      for ( EngagementPromotions engagementPromotions : promotion.getEngagementPromotions() )
      {
        // ******** Remove the purl & celebration promotions. **********
        if ( NewServiceAnniversaryUtil.isNewServiceAnniversaryEnabled() )
        {
          if ( engagementPromotions.getEligiblePromotion() instanceof RecognitionPromotion )
          {
            RecognitionPromotion recPromotion = (RecognitionPromotion)engagementPromotions.getEligiblePromotion();
            if ( recPromotion.isIncludePurl() || recPromotion.isIncludeCelebrations() )
            {
              continue;
            }
          }
        }

        this.engagementEligiblePromotionList.add( engagementPromotions.getEligiblePromotion().getName() );
      }
    }
    this.scoreActive = promotion.isScoreActive() ? "Yes" : "No";
    this.companyGoal = promotion.getCompanyGoal() != null ? String.valueOf( promotion.getCompanyGoal().longValue() ) : " ";
  }

  private void loadNominationPromotion( NominationPromotion promotion )
  {
    this.claimFormName = promotion.getClaimForm().getName();
    this.claimFormId = promotion.getClaimForm().getId();
    this.claimFormAsset = promotion.getClaimForm().getCmAssetCode();

    this.publicationDateActive = promotion.isPublicationDateActive();
    if ( publicationDateActive )
    {
      this.publicationDate = DateUtils.toDisplayString( promotion.getPublicationDate() );
    }

    if ( promotion.getAwardGroupType() != null )
    {
      this.individualOrTeamBased = promotion.getAwardGroupType().getName();
    }
    if ( promotion.getEvaluationType() != null )
    {
      this.evaluated = promotion.getEvaluationType().getName();
    }
    this.selfNomination = promotion.isSelfNomination();
    this.levelSelectionByApprover = promotion.isLevelSelectionByApprover();//Client customization for WIP #56492
    // Client customization for WIP 58122
    this.levelPayoutByApproverAvailable = promotion.isLevelPayoutByApproverAvailable();
    this.allowPublicRecognition = promotion.isAllowPublicRecognition();
    this.whyNomination = promotion.isWhyNomination();
    this.awardsActive = promotion.isAwardActive() ? "Yes" : "No";

    if ( promotion.getAwardAmountFixed() != null )
    {
      this.award = promotion.getAwardAmountFixed().toString();
    }
    else if ( promotion.getAwardAmountMin() != null && promotion.getAwardAmountMax() != null )
    {
      this.award = promotion.getAwardAmountMin().toString() + " - " + promotion.getAwardAmountMax().toString();
    }
    this.budgetName = "";
    if ( promotion.getBudgetMaster() != null )
    {
      this.budgetName = promotion.getBudgetMaster().getBudgetMasterName();
    }

    loadAudienceInfo( promotion );

    this.sweepstakesActive = promotion.isSweepstakesActive() ? "Yes" : "No";

    SweepstakesWinnerEligibilityType eligibilityType = promotion.getSweepstakesWinnerEligibilityType();
    if ( eligibilityType != null )
    {
      if ( eligibilityType.equals( SweepstakesWinnerEligibilityType.lookup( SweepstakesWinnerEligibilityType.NOMINATORS_ONLY_CODE ) ) )
      {
        this.numberOrPercentOfGivers = promotion.getSweepstakesPrimaryWinners().toString();
        if ( promotion.getSweepstakesPrimaryBasisType().equals( SweepstakesWinnersType.lookup( SweepstakesWinnersType.PERCENTAGE_CODE ) ) )
        {
          this.numberOrPercentOfGivers = this.numberOrPercentOfGivers + " %";
        }
        this.amountOfGiversAwards = promotion.getSweepstakesPrimaryAwardAmount().toString();
      }
      else if ( eligibilityType.equals( SweepstakesWinnerEligibilityType.lookup( SweepstakesWinnerEligibilityType.NOMINEES_ONLY_CODE ) ) )
      {
        this.numberOrPercentOfReceivers = promotion.getSweepstakesSecondaryWinners().toString();
        if ( promotion.getSweepstakesSecondaryBasisType().equals( SweepstakesWinnersType.lookup( SweepstakesWinnersType.PERCENTAGE_CODE ) ) )
        {
          this.numberOrPercentOfReceivers = this.numberOrPercentOfReceivers + " %";
        }
        this.amountOfReceiversAwards = promotion.getSweepstakesSecondaryAwardAmount().toString();
      }
      else if ( promotion.getSweepstakesPrimaryWinners() != null )
      {

        if ( promotion.getSweepstakesPrimaryWinners() != null )
        {
          this.numberOrPercentOfGivers = promotion.getSweepstakesPrimaryWinners().toString();
        }

        if ( promotion.getSweepstakesPrimaryBasisType() != null && promotion.getSweepstakesPrimaryBasisType().equals( SweepstakesWinnersType.lookup( SweepstakesWinnersType.PERCENTAGE_CODE ) ) )
        {
          this.numberOrPercentOfGivers = this.numberOrPercentOfGivers + " %";
        }

        if ( promotion.getSweepstakesPrimaryAwardAmount() != null )
        {
          this.amountOfGiversAwards = promotion.getSweepstakesPrimaryAwardAmount().toString();
        }

        if ( promotion.getSweepstakesSecondaryWinners() != null )
        {
          this.numberOrPercentOfReceivers = promotion.getSweepstakesSecondaryWinners().toString();
        }

        if ( promotion.getSweepstakesSecondaryBasisType() != null && promotion.getSweepstakesSecondaryBasisType().equals( SweepstakesWinnersType.lookup( SweepstakesWinnersType.PERCENTAGE_CODE ) ) )
        {
          this.numberOrPercentOfReceivers = this.numberOrPercentOfReceivers + " %";
        }

        if ( promotion.getSweepstakesSecondaryAwardAmount() != null )
        {
          this.amountOfReceiversAwards = promotion.getSweepstakesSecondaryAwardAmount().toString();
        }

      }
    }

    this.behaviorsActive = promotion.isBehaviorActive() ? "Yes" : "No";
    if ( promotion.getPromotionBehaviors() != null )
    {
      this.numberOfBehaviors = promotion.getPromotionBehaviors().size();
    }

    this.eCardsActive = promotion.isCardActive() ? "Yes" : "No";
    this.certificatesActive = promotion.isCertificateActive() ? "Yes" : "No";
    this.numberOfeCards = promotion.getPromotionECard().size();
    this.numberOfCertificates = promotion.getPromotionCertificates().size();
    this.setAllowYourOwnCard( promotion.isAllowYourOwnCard() );
    this.setDrawYourOwnCard( promotion.isDrawYourOwnCard() );
    // Fix for Bug #11501 - display audience correctly
    boolean result = false;
    if ( promotion.getWebRulesAudienceType() != null && promotion.getWebRulesAudienceType().equals( WebRulesAudienceType.lookup( WebRulesAudienceType.ALL_ACTIVE_PAX_CODE ) ) )
    {
      result = true;
    }
    this.webRulesAllPax = result;

    boolean result2 = false;
    if ( promotion.getWebRulesAudienceType() != null && promotion.getWebRulesAudienceType().equals( WebRulesAudienceType.lookup( WebRulesAudienceType.ALL_ELIGIBLE_PRIMARY_CODE ) ) )
    {
      result2 = true;
    }
    this.webRulesAllEligiblePrimary = result2;

    boolean result3 = false;
    if ( promotion.getWebRulesAudienceType() != null && promotion.getWebRulesAudienceType().equals( WebRulesAudienceType.lookup( WebRulesAudienceType.ALL_ELIGIBLE_SECONDARY_CODE ) ) )
    {
      result3 = true;
    }
    this.webRulesAllEligibleSecondary = result3;

    boolean result4 = false;
    if ( promotion.getWebRulesAudienceType() != null && promotion.getWebRulesAudienceType().equals( WebRulesAudienceType.lookup( WebRulesAudienceType.ALL_ELIGIBLE_PRIMARY_AND_SECONDARY_CODE ) ) )
    {
      result4 = true;
    }
    this.webRulesAllEligiblePrimaryAndSecondary = result4;
    // End Fix for Bug #11501 - display audience correctly

    // Begin Public recognition fields
    this.allowPublicRecognitionPoints = promotion.isAllowPublicRecognitionPoints();

    this.publicRecognitionAward = "";
    if ( promotion.getPublicRecogAwardAmountFixed() != null )
    {
      this.publicRecognitionAward = promotion.getPublicRecogAwardAmountFixed().toString();
    }
    else if ( promotion.getPublicRecogAwardAmountMin() != null && promotion.getPublicRecogAwardAmountMax() != null )
    {
      this.publicRecognitionAward = promotion.getPublicRecogAwardAmountMin().toString() + " - " + promotion.getPublicRecogAwardAmountMax().toString();
    }

    this.publicRecognitionBudget = "";
    if ( promotion.getPublicRecogBudgetMaster() != null )
    {
      this.publicRecognitionBudget = promotion.getPublicRecogBudgetMaster().getBudgetMasterName();
    }

    if ( null != promotion.getPublicRecognitionAudienceType() )
    {
      this.publicRecognitionGiverAllPax = promotion.getPublicRecognitionAudienceType().equals( PublicRecognitionAudienceType.lookup( PublicRecognitionAudienceType.ALL_ACTIVE_PAX_CODE ) );
    }

    Iterator<PromotionPublicRecognitionAudience> publicRecognitionGivers = promotion.getPromotionPublicRecognitionAudiences().iterator();
    while ( publicRecognitionGivers.hasNext() )
    {
      this.publicRecognitionGiversList.add( publicRecognitionGivers.next() );
    }
    if ( this.publicRecognitionGiversList.size() == 0 )
    {
      this.publicRecognitionGiversExists = false;
    }
    else
    {
      this.publicRecognitionGiversExists = true;
    }
    // End Public recognition fields
    this.viewPastWinners = promotion.isViewPastWinners();
  }

  /*
   * LoadSurveyPromotion
   */

  private void loadSurveyPromotion( SurveyPromotion promotion )
  {
    this.surveySubmissionStartDate = DateUtils.toDisplayString( promotion.getSubmissionStartDate() );
    this.surveySubmissionEndDate = DateUtils.toDisplayString( promotion.getSubmissionEndDate() );
    this.corpAndMngr = promotion.isCorpAndMngr();

    if ( promotion.getSurvey() != null )
    {
      this.claimFormName = promotion.getSurvey().getName();
      this.claimFormId = promotion.getSurvey().getId();
      // this.claimFormAsset = promotion.getQuiz().getCmAssetCode();
    }

    if ( promotion.getPrimaryAudienceType() != null )
    {
      this.allPaxAsSubmitters = promotion.getPrimaryAudienceType().equals( PrimaryAudienceType.lookup( PrimaryAudienceType.ALL_ACTIVE_PAX_CODE ) );
    }
    Iterator submitterAudiences = promotion.getPromotionPrimaryAudiences().iterator();

    while ( submitterAudiences.hasNext() )
    {
      this.submitterAudienceList.add( submitterAudiences.next() );
    }
    if ( this.submitterAudienceList.size() == 0 )
    {
      this.submitterAudienceExists = false;
    }
    else
    {
      this.submitterAudienceExists = true;
    }

    this.sweepstakesActive = promotion.isSweepstakesActive() ? "Yes" : "No";
    if ( promotion.getSweepstakesPrimaryWinners() != null )
    {
      this.numberOrPercentOfGivers = promotion.getSweepstakesPrimaryWinners().toString();
    }
    if ( promotion.getSweepstakesPrimaryBasisType() != null )
    {
      if ( promotion.getSweepstakesPrimaryBasisType().equals( SweepstakesWinnersType.lookup( SweepstakesWinnersType.PERCENTAGE_CODE ) ) )
      {
        this.numberOrPercentOfGivers = this.numberOrPercentOfGivers + " %";
      }
    }
    if ( promotion.getSweepstakesPrimaryAwardAmount() != null )
    {
      this.amountOfGiversAwards = promotion.getSweepstakesPrimaryAwardAmount().toString();
    }

    // Fix for Bug #11501 - display audience correctly
    boolean result = false;
    if ( promotion.getWebRulesAudienceType() != null && promotion.getWebRulesAudienceType().equals( WebRulesAudienceType.lookup( WebRulesAudienceType.ALL_ACTIVE_PAX_CODE ) ) )
    {
      result = true;
    }
    this.webRulesAllPax = result;

    boolean result2 = false;
    if ( promotion.getWebRulesAudienceType() != null && promotion.getWebRulesAudienceType().equals( WebRulesAudienceType.lookup( WebRulesAudienceType.ALL_ELIGIBLE_PRIMARY_CODE ) ) )
    {
      result2 = true;
    }
    this.webRulesAllEligiblePrimary = result2;
    // Fix for Bug #11501 - display audience correctly

  }

  /**
   * Load Goalquest promotion on the form.
   *
   * @param promotion
   */
  private void loadGoalQuestPromotion( GoalQuestPromotion promotion )
  {
    this.goalSelectionStartDate = DateUtils.toDisplayString( promotion.getGoalCollectionStartDate() );
    this.goalSelectionEndDate = DateUtils.toDisplayString( promotion.getGoalCollectionEndDate() );
    this.finalProcessDate = DateUtils.toDisplayString( promotion.getFinalProcessDate() );
    this.programId = promotion.getProgramId();

    loadAudienceInfo( promotion );

    if ( promotion.getAchievementRule() != null )
    {
      this.achievementRule = promotion.getAchievementRule().getName();
    }
    if ( promotion.getPayoutStructure() != null )
    {
      this.payoutStructure = promotion.getPayoutStructure().getName();
    }
    if ( promotion.getAchievementPrecision() != null )
    {
      this.achievementPrecision = promotion.getAchievementPrecision().getName();
    }
    if ( promotion.getRoundingMethod() != null )
    {
      this.roundingMethod = promotion.getRoundingMethod().getName();
    }
    if ( promotion.getOverrideStructure() != null )
    {
      this.overrideStructure = promotion.getOverrideStructure().getName();
    }
    if ( promotion.getProgressLoadType() != null )
    {
      this.progressLoadTypeName = promotion.getProgressLoadType().getName();
    }
    if ( promotion.getBaseUnit() != null )
    {
      this.baseUnit = promotion.getBaseUnitText();
    }
    if ( promotion.getBaseUnitPosition() != null )
    {
      this.baseUnitPosition = promotion.getBaseUnitPosition().getCode();
    }
    // to fix 20250 inorder to display proper value for audience in overviewpage for goalquest
    boolean result = false;
    if ( promotion.getWebRulesAudienceType() != null && promotion.getWebRulesAudienceType().equals( WebRulesAudienceType.lookup( WebRulesAudienceType.ALL_ACTIVE_PAX_CODE ) ) )
    {
      result = true;
    }
    this.webRulesAllPax = result;

    boolean result4 = false;
    if ( promotion.getWebRulesAudienceType() != null && promotion.getWebRulesAudienceType().equals( WebRulesAudienceType.lookup( WebRulesAudienceType.ALL_ELIGIBLE_PRIMARY_AND_SECONDARY_CODE ) ) )
    {
      result4 = true;
    }
    this.webRulesAllEligiblePrimaryAndSecondary = result4;

    if ( promotion.isGoalQuestOrChallengePointPromotion() )
    {
      if ( promotion.getManagerWebRulesAudienceType() != null )
      {
        this.managerWebRulesAudienceTypeName = promotion.getManagerWebRulesAudienceType().getName();
      }

      if ( promotion.getPartnerWebRulesAudienceType() != null )
      {
        this.partnerWebRulesAudienceTypeName = promotion.getPartnerWebRulesAudienceType().getName();
      }

      boolean managerResult = false;
      if ( promotion.getManagerWebRulesAudienceType() != null
          && promotion.getManagerWebRulesAudienceType().equals( ManagerWebRulesAudienceType.lookup( ManagerWebRulesAudienceType.ALL_ACTIVE_PAX_CODE ) ) )
      {
        managerResult = true;
      }
      this.managerWebRulesAllPax = managerResult;

      boolean partnerResult = false;
      if ( promotion.getPartnerWebRulesAudienceType() != null
          && promotion.getPartnerWebRulesAudienceType().equals( PartnerWebRulesAudienceType.lookup( PartnerWebRulesAudienceType.ALL_ACTIVE_PAX_CODE ) ) )
      {
        partnerResult = true;
      }
      this.partnerWebRulesAllPax = partnerResult;

      this.managerWebRulesSpecifyAudience = promotion.getPromotionManagerWebRulesAudience().size() > 0;
      this.partnerWebRulesSpecifyAudience = promotion.getPromotionPartnerWebRulesAudience().size() > 0;

      Iterator managerWebRulesAudiences = promotion.getPromotionManagerWebRulesAudience().iterator();

      while ( managerWebRulesAudiences.hasNext() )
      {
        this.managerWebRulesAudienceList.add( managerWebRulesAudiences.next() );
      }

      Iterator partnerWebRulesAudiences = promotion.getPromotionPartnerWebRulesAudience().iterator();

      while ( partnerWebRulesAudiences.hasNext() )
      {
        this.partnerWebRulesAudienceList.add( partnerWebRulesAudiences.next() );
      }

      boolean result5 = false;
      if ( promotion.getManagerWebRulesAudienceType() != null
          && promotion.getManagerWebRulesAudienceType().equals( ManagerWebRulesAudienceType.lookup( ManagerWebRulesAudienceType.ALL_ELIGIBLE_PRIMARY_AND_SECONDARY_CODE ) ) )
      {
        result5 = true;
      }
      this.managerWebRulesAllEligible = result5;

      boolean result6 = false;
      if ( promotion.getPartnerWebRulesAudienceType() != null
          && promotion.getPartnerWebRulesAudienceType().equals( PartnerWebRulesAudienceType.lookup( PartnerWebRulesAudienceType.ALL_ELIGIBLE_PRIMARY_AND_SECONDARY_CODE ) ) )
      {
        result6 = true;
      }
      this.partnerWebRulesAllEligible = result6;
      this.allowUnderArmour = promotion.isAllowUnderArmour();
    }
  }

  /**
   * Load Goalquest promotion on the form.
   *
   * @param promotion
   */
  private void loadChallengePointPromotion( ChallengePointPromotion promotion )
  {
    loadGoalQuestPromotion( promotion );

    if ( this.challengePointAwardType != null )
    {
      this.challengePointAwardType = promotion.getChallengePointAwardType().getName();
    }
    if ( promotion.getManagerCanSelect() != null )
    {
      this.managerSelect = promotion.getManagerCanSelect().booleanValue();
    }
    if ( promotion.getAwardThresholdType() != null )
    {
      this.awardThresholdType = promotion.getAwardThresholdType();
    }
    if ( promotion.getAwardIncrementType() != null )
    {
      this.awardIncrementType = promotion.getAwardIncrementType();
    }
    if ( promotion.getChallengePointAwardType() != null )
    {
      this.challengePointAwardType = promotion.getChallengePointAwardType().getName();
    }
    if ( promotion.getAwardThresholdValue() != null )
    {
      this.awardThresholdValue = promotion.getAwardThresholdValue().toString();
    }
    if ( promotion.getAwardIncrementValue() != null )
    {
      this.awardIncrementValue = promotion.getAwardIncrementValue().toString();
    }
    if ( promotion.getAwardPerIncrement() != null )
    {
      this.awardPerIncrement = promotion.getAwardPerIncrement().toString();
    }
    if ( promotion.getAchievementRule() != null )
    {
      this.achievementRule = promotion.getAchievementRule().getName();
    }
  }

  private void loadAudienceInfo( Promotion promotion )
  {
    this.selfEnrollment = promotion.isAllowSelfEnroll();

    if ( promotion.getPrimaryAudienceType() != null )
    {
      this.allPaxAsSubmitters = promotion.getPrimaryAudienceType().equals( PrimaryAudienceType.lookup( PrimaryAudienceType.ALL_ACTIVE_PAX_CODE ) );
      this.selfEnrollOnly = promotion.getPrimaryAudienceType().equals( PrimaryAudienceType.lookup( PrimaryAudienceType.SELF_ENROLL_ONLY ) );
    }

    if ( promotion.getSecondaryAudienceType() != null )
    {
      this.allPaxAsReceivers = promotion.getSecondaryAudienceType().equals( SecondaryAudienceType.lookup( SecondaryAudienceType.ALL_ACTIVE_PAX_CODE ) );
      this.teamSameAsSubmitters = promotion.getSecondaryAudienceType().equals( SecondaryAudienceType.lookup( SecondaryAudienceType.SAME_AS_PRIMARY_CODE ) );
      this.teamFromSubmitterNode = promotion.getSecondaryAudienceType().equals( SecondaryAudienceType.lookup( SecondaryAudienceType.ACTIVE_PAX_PRIMARY_NODE_CODE ) );
      this.teamFromSubmitterNodeBelow = promotion.getSecondaryAudienceType().equals( SecondaryAudienceType.lookup( SecondaryAudienceType.ACTIVE_PAX_PRIMARY_NODE_BELOW_CODE ) );
    }
    else
    {
      this.teamSameAsSubmitters = false;
      this.teamFromSubmitterNode = false;
    }
    this.teamSpecifyAudience = promotion.getPromotionSecondaryAudiences().size() > 0;

    Iterator submitterAudiences = promotion.getPromotionPrimaryAudiences().iterator();
    while ( submitterAudiences.hasNext() )
    {
      this.submitterAudienceList.add( submitterAudiences.next() );
    }
    if ( this.submitterAudienceList.size() == 0 )
    {
      this.submitterAudienceExists = false;
    }
    else
    {
      this.submitterAudienceExists = true;
    }

    Iterator teamAudiences = promotion.getPromotionSecondaryAudiences().iterator();
    while ( teamAudiences.hasNext() )
    {
      this.teamAudienceList.add( teamAudiences.next() );
    }
    if ( this.teamAudienceList.size() == 0 )
    {
      this.teamAudienceExists = false;
    }
    else
    {
      this.teamAudienceExists = true;
    }

    if ( promotion.isGoalQuestOrChallengePointPromotion() )
    {
      boolean isPointsType = false;
      if ( promotion.isGoalQuestPromotion() )
      {
        isPointsType = promotion.getAwardType().getCode().equalsIgnoreCase( PromotionAwardsType.POINTS );
      }
      else if ( promotion.isChallengePointPromotion() )
      {
        isPointsType = ( (ChallengePointPromotion)promotion ).getChallengePointAwardType().getCode().equalsIgnoreCase( PromotionAwardsType.POINTS );
      }

      boolean isPartnerAudienceDefined = promotion.getPartnerAudienceType() != null ? true : false;
      if ( isPointsType && isPartnerAudienceDefined )
      {
        this.setPartnerAudienceExists( true );
      }
      else
      {
        this.setPartnerAudienceExists( false );
      }
      Iterator partnerAudiences = promotion.getPromotionPartnerAudiences().iterator();
      while ( partnerAudiences.hasNext() )
      {
        this.partnerAudienceList.add( partnerAudiences.next() );
      }
    }
    if ( promotion.isSSIPromotion() )
    {
      if ( promotion.getSecondaryAudienceType() != null )
      {
        this.teamFromSubmitterNode = promotion.getSecondaryAudienceType().equals( SecondaryAudienceType.lookup( SecondaryAudienceType.CREATOR_ORG_ONLY_CODE ) );
        this.teamFromSubmitterNodeBelow = promotion.getSecondaryAudienceType().equals( SecondaryAudienceType.lookup( SecondaryAudienceType.CREATOR_ORG_AND_BELOW_CODE ) );
      }
    }
  }

  private void loadThrowdownPromotion( ThrowdownPromotion promotion )
  {
    this.promotionTheme = promotion.getThrowdownPromotionType().getName();
    this.unevenPlaySelection = promotion.getTeamUnavailableResolverType().getName();
    this.displayTeamProgress = promotion.isDisplayTeamProgress();
    this.smackTalkAvailable = promotion.isSmackTalkAvailable();

    if ( promotion.getPrimaryAudienceType() != null )
    {
      this.allPaxAsSubmitters = promotion.getPrimaryAudienceType().equals( PrimaryAudienceType.lookup( PrimaryAudienceType.ALL_ACTIVE_PAX_CODE ) );
    }
    Iterator submitterAudiences = promotion.getPromotionPrimaryAudiences().iterator();

    while ( submitterAudiences.hasNext() )
    {
      this.submitterAudienceList.add( submitterAudiences.next() );
    }
    if ( this.submitterAudienceList.size() == 0 )
    {
      this.submitterAudienceExists = false;
    }
    else
    {
      this.submitterAudienceExists = true;
    }

    divisionAudienceMap = new HashMap();
    for ( Division div : promotion.getDivisions() )
    {
      List audiences = new ArrayList();
      for ( DivisionCompetitorsAudience divCompAud : div.getCompetitorsAudience() )
      {
        audiences.add( divCompAud );
      }
      divisionAudienceMap.put( div.getDivisionNameFromCM(), audiences );
    }
    if ( this.divisionAudienceMap.size() == 0 )
    {
      this.divisionAudienceExists = false;
    }
    else
    {
      this.divisionAudienceExists = true;
    }

    if ( promotion.getBaseUnit() != null )
    {
      this.baseUnit = promotion.getBaseUnitText();
    }
    if ( promotion.getBaseUnitPosition() != null )
    {
      this.baseUnitPosition = promotion.getBaseUnitPosition().getCode();
    }
    if ( promotion.getNumberOfRounds() > 0 )
    {
      this.numberOfRounds = promotion.getNumberOfRounds();
    }
    if ( promotion.getLengthOfRound() > 0 )
    {
      this.numberOfDayPerRound = promotion.getLengthOfRound();
    }
    if ( promotion.getHeadToHeadStartDate() != null )
    {
      this.startDateForFirstRound = DateUtils.toDisplayString( promotion.getHeadToHeadStartDate() );
    }
    if ( promotion.getAchievementPrecision() != null )
    {
      this.achievementPrecision = promotion.getAchievementPrecision().getName();
    }
    if ( promotion.getRoundingMethod() != null )
    {
      this.roundingMethod = promotion.getRoundingMethod().getName();
    }
    this.daysPriorToRoundStartSchedule = Integer.toString( promotion.getDaysPriorToRoundStartSchedule() );
  }

  private void loadSSIPromotion( SSIPromotion promotion )
  {
    // basics section
    List<String> contests = new ArrayList<String>();
    if ( promotion.isAwardThemNowSelected() )
    {
      contests.add( SSIContestType.lookup( SSIContestType.AWARD_THEM_NOW ).getName() );
    }
    if ( promotion.isDoThisGetThatSelected() )
    {
      contests.add( SSIContestType.lookup( SSIContestType.DO_THIS_GET_THAT ).getName() );
    }
    if ( promotion.isObjectivesSelected() )
    {
      contests.add( SSIContestType.lookup( SSIContestType.OBJECTIVES ).getName() );
    }
    if ( promotion.isStackRankSelected() )
    {
      contests.add( SSIContestType.lookup( SSIContestType.STACK_RANK ).getName() );
    }
    if ( promotion.isStepItUpSelected() )
    {
      contests.add( SSIContestType.lookup( SSIContestType.STEP_IT_UP ).getName() );
    }
    this.setSelectedContests( StringUtils.join( contests, ", " ) );
    this.setMaxContestsToDisplay( promotion.getMaxContestsToDisplay() );
    this.setDaysToArchive( promotion.getDaysToArchive() );

    if ( promotion.getContestGuideUrl() != null && !promotion.getContestGuideUrl().isEmpty() )
    {
      this.setContestGuideUrl( getSSIPromotionService().buildSSIContestGuideUrl( promotion.getContestGuideUrl() ) );
    }

    // audience section
    loadAudienceInfo( promotion );

    // awards section
    this.setPointsAvailable( promotion.getAllowAwardPoints() );
    // this.setMerchandiseAvailable( promotion.getAllowAwardMerchandise() );
    this.setOtherAvailable( promotion.getAllowAwardOther() );
    this.setBadgeCount( promotion.getBadge() != null && promotion.getBadge().getBadgeRules() != null && promotion.getBadge().getBadgeRules().size() > 0
        ? String.valueOf( promotion.getBadge().getBadgeRules().size() )
        : null );

    // activity submission section
    this.setAllowSpreadSheet( promotion.getAllowActivityUpload() );
    loadClaimInfo( promotion );

    // approval section
    this.setReqireApproval( promotion.getRequireContestApproval() );
    this.setNumberOfApproverLevels( promotion.getContestApprovalLevels() );
    if ( promotion.getRequireContestApproval() )
    {
      List level1ApprovalSetWithPaxSize = new ArrayList();
      if ( promotion.getContestApprovalLevel1Audiences() != null && promotion.getContestApprovalLevel1Audiences().size() > 0 )
      {
        Iterator contestApprovalLevel1Audiences = promotion.getContestApprovalLevel1Audiences().iterator();
        while ( contestApprovalLevel1Audiences.hasNext() )
        {
          SSIPromotionContestApprovalLevel1Audience level1ContestApproverAudience = (SSIPromotionContestApprovalLevel1Audience)contestApprovalLevel1Audiences.next();
          Audience audience = level1ContestApproverAudience.getAudience();
          audience.setSize( getNbrOfPaxsInCriteriaAudience( audience ) );
          level1ApprovalSetWithPaxSize.add( level1ContestApproverAudience );
        }
        this.setLevel1ApproversList( level1ApprovalSetWithPaxSize );
      }
      if ( promotion.getContestApprovalLevels() == 2 )
      {
        List level2ApprovalSetWithPaxSize = new ArrayList();
        if ( promotion.getContestApprovalLevel2Audiences() != null && promotion.getContestApprovalLevel2Audiences().size() > 0 )
        {
          Iterator contestApprovalLevel2Audiences = promotion.getContestApprovalLevel2Audiences().iterator();
          while ( contestApprovalLevel2Audiences.hasNext() )
          {
            SSIPromotionContestApprovalLevel2Audience level2ContestApproverAudience = (SSIPromotionContestApprovalLevel2Audience)contestApprovalLevel2Audiences.next();
            Audience audience = level2ContestApproverAudience.getAudience();
            audience.setSize( getNbrOfPaxsInCriteriaAudience( audience ) );
            level2ApprovalSetWithPaxSize.add( level2ContestApproverAudience );
          }
          this.setLevel2ApproversList( level2ApprovalSetWithPaxSize );
        }
      }
    }
  }

  /* Claim submission and approval has been moved to SSI_Phase_2 */
  private void loadClaimInfo( SSIPromotion promotion )
  {
    this.setAllowClaimSubmission( promotion.getAllowClaimSubmission() );
    /*
     * if ( promotion.getClaimApprovalRequired() != null && promotion.getClaimApprovalRequired() ) {
     * if ( promotion.getClaimApprovalAudienceType() != null ) {
     * this.setClaimApprovalAudienceTypeName( promotion.getClaimApprovalAudienceType().getCode() );
     * if ( promotion.getClaimApprovalAudienceType().getCode().equals(
     * SSIClaimApprovalAudienceType.SPECIFY_AUDIENCE_CODE ) ) { List
     * claimApprovalSecondarySetWithPaxSize = new ArrayList(); if (
     * promotion.getClaimApprovalAudiences() != null && promotion.getClaimApprovalAudiences().size()
     * > 0 ) { Iterator claimApproverAudienceIteraotr =
     * promotion.getClaimApprovalAudiences().iterator(); while (
     * claimApproverAudienceIteraotr.hasNext() ) { SSIPromotionClaimApprovalAudience
     * claimApproverAudience =
     * (SSIPromotionClaimApprovalAudience)claimApproverAudienceIteraotr.next(); Audience audience =
     * claimApproverAudience.getAudience(); audience.setSize( getNbrOfPaxsInCriteriaAudience(
     * audience ) ); claimApprovalSecondarySetWithPaxSize.add( claimApproverAudience ); }
     * this.setClaimApprovalList( claimApprovalSecondarySetWithPaxSize ); } } } else
     * this.setClaimApprovalAudienceTypeName( null ); } this.setNumberOfDaysToApproveClaim(
     * promotion.getDaysToApproveClaim() ); }
     */

  }

  private int getNbrOfPaxsInCriteriaAudience( Audience audience )
  {
    int nbrOfPaxInCriteriaAudience = 0;

    Hierarchy primaryHierarchy = getHierarchyService().getPrimaryHierarchy();
    Long primaryHierarchyId = primaryHierarchy.getId();

    Set audiences = new LinkedHashSet();
    audiences.add( audience );
    List paxFormattedValueList = getListBuilderService().searchParticipants( audiences, primaryHierarchyId, true, null, true );
    nbrOfPaxInCriteriaAudience = paxFormattedValueList.size();

    return nbrOfPaxInCriteriaAudience;
  }

  public String getPromotionTypeCode()
  {
    return promotionTypeCode;
  }

  public void setPromotionTypeCode( String promotionTypeCode )
  {
    this.promotionTypeCode = promotionTypeCode;
  }

  public String getAward()
  {
    return award;
  }

  public void setAward( String award )
  {
    this.award = award;
  }

  public String getAwardsActive()
  {
    return awardsActive;
  }

  public void setAwardsActive( String awardsActive )
  {
    this.awardsActive = awardsActive;
  }

  public String getAwardType()
  {
    return awardType;
  }

  public void setAwardType( String awardType )
  {
    this.awardType = awardType;
  }

  public String getBudgetName()
  {
    return budgetName;
  }

  public void setBudgetName( String budgetName )
  {
    this.budgetName = budgetName;
  }

  public String getSweepstakesActive()
  {
    return sweepstakesActive;
  }

  public void setSweepstakesActive( String sweepstakesActive )
  {
    this.sweepstakesActive = sweepstakesActive;
  }

  public String getBehaviorsActive()
  {
    return behaviorsActive;
  }

  public void setBehaviorsActive( String behaviorsActive )
  {
    this.behaviorsActive = behaviorsActive;
  }

  public int getNumberOfBehaviors()
  {
    return numberOfBehaviors;
  }

  public void setNumberOfBehaviors( int numberOfBehaviors )
  {
    this.numberOfBehaviors = numberOfBehaviors;
  }

  public int getNumberOfeCards()
  {
    return numberOfeCards;
  }

  public void setNumberOfeCards( int numberOfeCards )
  {
    this.numberOfeCards = numberOfeCards;
  }

  public String getECardsActive()
  {
    return eCardsActive;
  }

  public void setECardsActive( String cardsActive )
  {
    eCardsActive = cardsActive;
  }

  public String getCertificatesActive()
  {
    return certificatesActive;
  }

  public void setCertificatesActive( String certificatesActive )
  {
    this.certificatesActive = certificatesActive;
  }

  public int getNumberOfCertificates()
  {
    return numberOfCertificates;
  }

  public void setNumberOfCertificates( int numberOfCertificates )
  {
    this.numberOfCertificates = numberOfCertificates;
  }

  public String getAmountOfGiversAwards()
  {
    return amountOfGiversAwards;
  }

  public void setAmountOfGiversAwards( String amountOfGiversAwards )
  {
    this.amountOfGiversAwards = amountOfGiversAwards;
  }

  public String getAmountOfReceiversAwards()
  {
    return amountOfReceiversAwards;
  }

  public void setAmountOfReceiversAwards( String amountOfReceiversAwards )
  {
    this.amountOfReceiversAwards = amountOfReceiversAwards;
  }

  public String getNumberOrPercentOfGivers()
  {
    return numberOrPercentOfGivers;
  }

  public void setNumberOrPercentOfGivers( String numberOrPercentOfGivers )
  {
    this.numberOrPercentOfGivers = numberOrPercentOfGivers;
  }

  public String getNumberOrPercentOfReceivers()
  {
    return numberOrPercentOfReceivers;
  }

  public void setNumberOrPercentOfReceivers( String numberOrPercentOfReceivers )
  {
    this.numberOrPercentOfReceivers = numberOrPercentOfReceivers;
  }

  public String getAwardTypeText()
  {
    return awardTypeText;
  }

  public void setAwardTypeText( String awardTypeText )
  {
    this.awardTypeText = awardTypeText;
  }

  public boolean isAllPaxAsReceivers()
  {
    return allPaxAsReceivers;
  }

  public void setAllPaxAsReceivers( boolean allPaxAsReceivers )
  {
    this.allPaxAsReceivers = allPaxAsReceivers;
  }

  public boolean isTeamFromSubmitterNodeBelow()
  {
    return teamFromSubmitterNodeBelow;
  }

  public void setTeamFromSubmitterNodeBelow( boolean teamFromSubmitterNodeBelow )
  {
    this.teamFromSubmitterNodeBelow = teamFromSubmitterNodeBelow;
  }

  public boolean isCopyRecipientManager()
  {
    return copyRecipientManager;
  }

  public void setCopyRecipientManager( boolean copyRecipientManager )
  {
    this.copyRecipientManager = copyRecipientManager;
  }

  public boolean isIncludeCertificate()
  {
    return includeCertificate;
  }

  public void setIncludeCertificate( boolean includeCertificate )
  {
    this.includeCertificate = includeCertificate;
  }

  public String getAttemptsAllowed()
  {
    return attemptsAllowed;
  }

  public void setAttemptsAllowed( String attemptsAllowed )
  {
    this.attemptsAllowed = attemptsAllowed;
  }

  public String getWebRulesAudienceTypeName()
  {
    return webRulesAudienceTypeName;
  }

  public void setWebRulesAudienceTypeName( String webRulesAudienceTypeName )
  {
    this.webRulesAudienceTypeName = webRulesAudienceTypeName;
  }

  public boolean isEntireParentAudience()
  {
    return entireParentAudience;
  }

  public void setEntireParentAudience( boolean entireParentAudience )
  {
    this.entireParentAudience = entireParentAudience;
  }

  public boolean isNotificationExists()
  {
    return notificationExists;
  }

  public void setNotificationExists( boolean notificationExists )
  {
    this.notificationExists = notificationExists;
  }

  public boolean isSubmitterAudienceExists()
  {
    return submitterAudienceExists;
  }

  public void setSubmitterAudienceExists( boolean submitterAudienceExists )
  {
    this.submitterAudienceExists = submitterAudienceExists;
  }

  public boolean isTeamAudienceExists()
  {
    return teamAudienceExists;
  }

  public void setTeamAudienceExists( boolean teamAudienceExists )
  {
    this.teamAudienceExists = teamAudienceExists;
  }

  public boolean isWebRulesAllEligiblePrimary()
  {
    return webRulesAllEligiblePrimary;
  }

  public void setWebRulesAllEligiblePrimary( boolean webRulesAllEligiblePrimary )
  {
    this.webRulesAllEligiblePrimary = webRulesAllEligiblePrimary;
  }

  public boolean isWebRulesAllEligiblePrimaryAndSecondary()
  {
    return webRulesAllEligiblePrimaryAndSecondary;
  }

  public void setWebRulesAllEligiblePrimaryAndSecondary( boolean webRulesAllEligiblePrimaryAndSecondary )
  {
    this.webRulesAllEligiblePrimaryAndSecondary = webRulesAllEligiblePrimaryAndSecondary;
  }

  public boolean isWebRulesAllEligibleSecondary()
  {
    return webRulesAllEligibleSecondary;
  }

  public void setWebRulesAllEligibleSecondary( boolean webRulesAllEligibleSecondary )
  {
    this.webRulesAllEligibleSecondary = webRulesAllEligibleSecondary;
  }

  public String getSweepsWinnerEligibilityCode()
  {
    return sweepsWinnerEligibilityCode;
  }

  public void setSweepsWinnerEligibilityCode( String sweepsWinnerEligibilityCode )
  {
    this.sweepsWinnerEligibilityCode = sweepsWinnerEligibilityCode;
  }

  public String getEvaluated()
  {
    return evaluated;
  }

  public void setEvaluated( String evaluated )
  {
    this.evaluated = evaluated;
  }

  public String getIndividualOrTeamBased()
  {
    return individualOrTeamBased;
  }

  public void setIndividualOrTeamBased( String individualOrTeamBased )
  {
    this.individualOrTeamBased = individualOrTeamBased;
  }

  public boolean isSelfNomination()
  {
    return selfNomination;
  }

  public void setSelfNomination( boolean selfNomination )
  {
    this.selfNomination = selfNomination;
  }

  public String getScoreBy()
  {
    return scoreBy;
  }

  public void setScoreBy( String scoreBy )
  {
    this.scoreBy = scoreBy;
  }

  public Long getCalculatorId()
  {
    return calculatorId;
  }

  public void setCalculatorId( Long calculatorId )
  {
    this.calculatorId = calculatorId;
  }

  public String getCalculatorName()
  {
    return calculatorName;
  }

  public void setCalculatorName( String calculatorName )
  {
    this.calculatorName = calculatorName;
  }

  public String getManagerPromotionName()
  {
    return managerPromotionName;
  }

  public void setManagerPromotionName( String managerPromotionName )
  {
    this.managerPromotionName = managerPromotionName;
  }

  /**
   * Bean location through BeanLocator look-up.
   *
   * @param beanName
   * @return SAO
   */
  protected static SAO getService( String beanName )
  {
    return (SAO)BeanLocator.getBean( beanName );
  }

  /**
   * Get the promotionService from the beanFactory.
   *
   * @return PromotionService
   */
  private PromotionService getPromotionService()
  {
    return (PromotionService)getService( PromotionService.BEAN_NAME );
  }

  private SSIPromotionService getSSIPromotionService()
  {
    return (SSIPromotionService)getService( SSIPromotionService.BEAN_NAME );
  }

  /**
   * Gets an AudienceService
   * 
   * @return AudienceService
   */
  private ListBuilderService getListBuilderService()
  {
    return (ListBuilderService)getService( ListBuilderService.BEAN_NAME );
  }

  /**
   * Gets a HierarchyService
   * 
   * @return HierarchyService
   */
  private HierarchyService getHierarchyService()
  {
    return (HierarchyService)getService( HierarchyService.BEAN_NAME );
  }

  public boolean isManagerAwardPromotion()
  {
    return managerAwardPromotion;
  }

  public void setManagerAwardPromotion( boolean managerAwardPromotion )
  {
    this.managerAwardPromotion = managerAwardPromotion;
  }

  public String getManagerAwardPromotionId()
  {
    return managerAwardPromotionId;
  }

  public void setManagerAwardPromotionId( String managerAwardPromotionId )
  {
    this.managerAwardPromotionId = managerAwardPromotionId;
  }

  public String getGoalSelectionEndDate()
  {
    return goalSelectionEndDate;
  }

  public void setGoalSelectionEndDate( String goalSelectionEndDate )
  {
    this.goalSelectionEndDate = goalSelectionEndDate;
  }

  public String getGoalSelectionStartDate()
  {
    return goalSelectionStartDate;
  }

  public void setGoalSelectionStartDate( String goalSelectionStartDate )
  {
    this.goalSelectionStartDate = goalSelectionStartDate;
  }

  public String getAchievementPrecision()
  {
    return achievementPrecision;
  }

  public void setAchievementPrecision( String achievementPrecision )
  {
    this.achievementPrecision = achievementPrecision;
  }

  public boolean isApproveAwards()
  {
    return approveAwards;
  }

  public void setApproveAwards( boolean approveAwards )
  {
    this.approveAwards = approveAwards;
  }

  public String getOverrideStructure()
  {
    return overrideStructure;
  }

  public void setOverrideStructure( String overrideStructure )
  {
    this.overrideStructure = overrideStructure;
  }

  public String getPayoutStructure()
  {
    return payoutStructure;
  }

  public void setPayoutStructure( String payoutStructure )
  {
    this.payoutStructure = payoutStructure;
  }

  public String getRoundingMethod()
  {
    return roundingMethod;
  }

  public void setRoundingMethod( String roundingMethod )
  {
    this.roundingMethod = roundingMethod;
  }

  public void setWebRulesActive( boolean webRulesActive )
  {
    this.webRulesActive = webRulesActive;
  }

  public String getFinalProcessDate()
  {
    return finalProcessDate;
  }

  public void setFinalProcessDate( String finalProcessDate )
  {
    this.finalProcessDate = finalProcessDate;
  }

  public String getProgramId()
  {
    return programId;
  }

  public void setProgramId( String programId )
  {
    this.programId = programId;
  }

  public boolean isSelfEnrollOnly()
  {
    return selfEnrollOnly;
  }

  public void setSelfEnrollOnly( boolean selfEnrollOnly )
  {
    this.selfEnrollOnly = selfEnrollOnly;
  }

  public String getProgressLoadTypeName()
  {
    return progressLoadTypeName;
  }

  public void setProgressLoadTypeName( String progressLoadTypeName )
  {
    this.progressLoadTypeName = progressLoadTypeName;
  }

  public String getBaseUnit()
  {
    return baseUnit;
  }

  public void setBaseUnit( String baseUnit )
  {
    this.baseUnit = baseUnit;
  }

  public String getBaseUnitPosition()
  {
    return baseUnitPosition;
  }

  public void setBaseUnitPosition( String baseUnitPosition )
  {
    this.baseUnitPosition = baseUnitPosition;
  }

  public String getSweepstakesPrimaryAwardLevel()
  {
    return sweepstakesPrimaryAwardLevel;
  }

  public void setSweepstakesPrimaryAwardLevel( String sweepstakesPrimaryAwardLevel )
  {
    this.sweepstakesPrimaryAwardLevel = sweepstakesPrimaryAwardLevel;
  }

  public String getSweepstakesSecondaryAwardLevel()
  {
    return sweepstakesSecondaryAwardLevel;
  }

  public void setSweepstakesSecondaryAwardLevel( String sweepstakesSecondaryAwardLevel )
  {
    this.sweepstakesSecondaryAwardLevel = sweepstakesSecondaryAwardLevel;
  }

  public String getAwardStructure()
  {
    return awardStructure;
  }

  public void setAwardStructure( String awardStructure )
  {
    this.awardStructure = awardStructure;
  }

  public boolean isFeaturedAwardsEnabled()
  {
    return featuredAwardsEnabled;
  }

  public void setFeaturedAwardsEnabled( boolean featuredAwardsEnabled )
  {
    this.featuredAwardsEnabled = featuredAwardsEnabled;
  }

  public String getWhatsNewDescription()
  {
    return whatsNewDescription;
  }

  public void setWhatsNewDescription( String whatsNewDescription )
  {
    this.whatsNewDescription = whatsNewDescription;
  }

  public boolean isPartnerAudienceExists()
  {
    return partnerAudienceExists;
  }

  public void setPartnerAudienceExists( boolean partnerAudienceExists )
  {
    this.partnerAudienceExists = partnerAudienceExists;
  }

  public List getPartnerAudienceList()
  {
    return partnerAudienceList;
  }

  public void setPartnerAudienceList( List partnerAudienceList )
  {
    this.partnerAudienceList = partnerAudienceList;
  }

  public String getPartnerPayoutStructure()
  {
    return partnerPayoutStructure;
  }

  public void setPartnerPayoutStructure( String partnerPayoutStructure )
  {
    this.partnerPayoutStructure = partnerPayoutStructure;
  }

  public String getOpenEnrollmentEnabled()
  {
    return openEnrollmentEnabled;
  }

  public void setOpenEnrollmentEnabled( String openEnrollmentEnabled )
  {
    this.openEnrollmentEnabled = openEnrollmentEnabled;
  }

  public String getSelfRecognitionEnabled()
  {
    return selfRecognitionEnabled;
  }

  public void setSelfRecognitionEnabled( String selfRecognitionEnabled )
  {
    this.selfRecognitionEnabled = selfRecognitionEnabled;
  }

  public String getBudgetSweepDate()
  {
    return budgetSweepDate;
  }

  public void setBudgetSweepDate( String budgetSweepDate )
  {
    this.budgetSweepDate = budgetSweepDate;
  }

  public String getBudgetSweepEnabled()
  {
    return budgetSweepEnabled;
  }

  public void setBudgetSweepEnabled( String budgetSweepEnabled )
  {
    this.budgetSweepEnabled = budgetSweepEnabled;
  }

  public boolean isEnableEndPromoOption()
  {
    if ( null != this.budgetSweepEnabled && "Yes".equals( this.budgetSweepEnabled ) && null != this.budgetSweepDate )
    {
      Date sweepDate = DateUtils.toDate( this.budgetSweepDate );
      if ( sweepDate.after( new Date() ) )
      {
        return false;
      }
    }
    return true;
  }

  public String getChallengePointAwardType()
  {
    return challengePointAwardType;
  }

  public void setChallengePointAwardType( String challengePointAwardType )
  {
    this.challengePointAwardType = challengePointAwardType;
  }

  public boolean isManagerSelect()
  {
    return managerSelect;
  }

  public void setManagerSelect( boolean managerSelect )
  {
    this.managerSelect = managerSelect;
  }

  public String getAchievementRule()
  {
    return achievementRule;
  }

  public void setAchievementRule( String achievementRule )
  {
    this.achievementRule = achievementRule;
  }

  public String getAwardIncrementType()
  {
    return awardIncrementType;
  }

  public void setAwardIncrementType( String awardIncrementType )
  {
    this.awardIncrementType = awardIncrementType;
  }

  public String getAwardIncrementValue()
  {
    return awardIncrementValue;
  }

  public void setAwardIncrementValue( String awardIncrementValue )
  {
    this.awardIncrementValue = awardIncrementValue;
  }

  public String getAwardPerIncrement()
  {
    return awardPerIncrement;
  }

  public void setAwardPerIncrement( String awardPerIncrement )
  {
    this.awardPerIncrement = awardPerIncrement;
  }

  public String getAwardThresholdType()
  {
    return awardThresholdType;
  }

  public void setAwardThresholdType( String awardThresholdType )
  {
    this.awardThresholdType = awardThresholdType;
  }

  public String getAwardThresholdValue()
  {
    return awardThresholdValue;
  }

  public void setAwardThresholdValue( String awardThresholdValue )
  {
    this.awardThresholdValue = awardThresholdValue;
  }

  public boolean isIncludePurl()
  {
    return includePurl;
  }

  public void setIncludePurl( boolean includePurl )
  {
    this.includePurl = includePurl;
  }

  public String getPurlPromotionMediaType()
  {
    return purlPromotionMediaType;
  }

  public void setPurlPromotionMediaType( String purlPromotionMediaType )
  {
    this.purlPromotionMediaType = purlPromotionMediaType;
  }

  public String getPurlMediaValue()
  {
    return purlMediaValue;
  }

  public void setPurlMediaValue( String purlMediaValue )
  {
    this.purlMediaValue = purlMediaValue;
  }

  public void setAllowYourOwnCard( boolean allowYourOwnCard )
  {
    this.allowYourOwnCard = allowYourOwnCard;
  }

  public boolean isAllowYourOwnCard()
  {
    return allowYourOwnCard;
  }

  public void setCopyOthers( boolean copyOthers )
  {
    this.copyOthers = copyOthers;
  }

  public boolean isCopyOthers()
  {
    return copyOthers;
  }

  public boolean isAllowRecognitionSendDate()
  {
    return allowRecognitionSendDate;
  }

  public void setAllowRecognitionSendDate( boolean allowRecognitionSendDate )
  {
    this.allowRecognitionSendDate = allowRecognitionSendDate;
  }

  public String getSelectedContests()
  {
    return selectedContests;
  }

  public void setSelectedContests( String selectedContests )
  {
    this.selectedContests = selectedContests;
  }

  public String getAwardThemNowContestEmailCount()
  {
    return awardThemNowContestEmailCount;
  }

  public void setAwardThemNowContestEmailCount( String awardThemNowContestEmailCount )
  {
    this.awardThemNowContestEmailCount = awardThemNowContestEmailCount;
  }

  public String getDoThisGetThatContestEmailCount()
  {
    return doThisGetThatContestEmailCount;
  }

  public void setDoThisGetThatContestEmailCount( String doThisGetThatContestEmailCount )
  {
    this.doThisGetThatContestEmailCount = doThisGetThatContestEmailCount;
  }

  public String getCreatorNotificationsEmailCount()
  {
    return creatorNotificationsEmailCount;
  }

  public void setCreatorNotificationsEmailCount( String creatorNotificationsEmailCount )
  {
    this.creatorNotificationsEmailCount = creatorNotificationsEmailCount;
  }

  public String getApproverNotificationsEmailCount()
  {
    return approverNotificationsEmailCount;
  }

  public void setApproverNotificationsEmailCount( String approverNotificationsEmailCount )
  {
    this.approverNotificationsEmailCount = approverNotificationsEmailCount;
  }

  public String getObjectivesContestEmailCount()
  {
    return objectivesContestEmailCount;
  }

  public void setObjectivesContestEmailCount( String objectivesContestEmailCount )
  {
    this.objectivesContestEmailCount = objectivesContestEmailCount;
  }

  public String getStackRankContestEmailCount()
  {
    return stackRankContestEmailCount;
  }

  public void setStackRankContestEmailCount( String stackRankContestEmailCount )
  {
    this.stackRankContestEmailCount = stackRankContestEmailCount;
  }

  public String getStepItUpContestEmailCount()
  {
    return stepItUpContestEmailCount;
  }

  public void setStepItUpContestEmailCount( String stepItUpContestEmailCount )
  {
    this.stepItUpContestEmailCount = stepItUpContestEmailCount;
  }

  public boolean isPointsAvailable()
  {
    return pointsAvailable;
  }

  public void setPointsAvailable( boolean pointsAvailable )
  {
    this.pointsAvailable = pointsAvailable;
  }

  public boolean isMerchandiseAvailable()
  {
    return merchandiseAvailable;
  }

  public void setMerchandiseAvailable( boolean merchandiseAvailable )
  {
    this.merchandiseAvailable = merchandiseAvailable;
  }

  public boolean isOtherAvailable()
  {
    return otherAvailable;
  }

  public void setOtherAvailable( boolean otherAvailable )
  {
    this.otherAvailable = otherAvailable;
  }

  public String getBadgeCount()
  {
    return badgeCount;
  }

  public void setBadgeCount( String badgeCount )
  {
    this.badgeCount = badgeCount;
  }

  public Boolean getAllowSpreadSheet()
  {
    return allowSpreadSheet;
  }

  public void setAllowSpreadSheet( Boolean allowSpreadSheet )
  {
    this.allowSpreadSheet = allowSpreadSheet;
  }

  public Boolean getAllowClaimSubmission()
  {
    return allowClaimSubmission;
  }

  public void setAllowClaimSubmission( Boolean allowClaimSubmission )
  {
    this.allowClaimSubmission = allowClaimSubmission;
  }

  public Boolean getRequireClaimApproval()
  {
    return requireClaimApproval;
  }

  public void setRequireClaimApproval( Boolean requireClaimApproval )
  {
    this.requireClaimApproval = requireClaimApproval;
  }

  public List getClaimApprovalList()
  {
    return claimApprovalList;
  }

  public void setClaimApprovalList( List claimApprovalList )
  {
    this.claimApprovalList = claimApprovalList;
  }

  public Boolean getReqireApproval()
  {
    return reqireApproval;
  }

  public void setReqireApproval( Boolean reqireApproval )
  {
    this.reqireApproval = reqireApproval;
  }

  public Integer getNumberOfDaysToApproveClaim()
  {
    return numberOfDaysToApproveClaim;
  }

  public void setNumberOfDaysToApproveClaim( Integer numberOfDaysToApproveClaim )
  {
    this.numberOfDaysToApproveClaim = numberOfDaysToApproveClaim;
  }

  public Integer getNumberOfApproverLevels()
  {
    return numberOfApproverLevels;
  }

  public void setNumberOfApproverLevels( Integer numberOfApproverLevels )
  {
    this.numberOfApproverLevels = numberOfApproverLevels;
  }

  public List getLevel1ApproversList()
  {
    return level1ApproversList;
  }

  public void setLevel1ApproversList( List level1ApproversList )
  {
    this.level1ApproversList = level1ApproversList;
  }

  public List getLevel2ApproversList()
  {
    return level2ApproversList;
  }

  public void setLevel2ApproversList( List level2ApproversList )
  {
    this.level2ApproversList = level2ApproversList;
  }

  public String getClaimApprovalAudienceTypeName()
  {
    return claimApprovalAudienceTypeName;
  }

  public void setClaimApprovalAudienceTypeName( String claimApprovalAudienceTypeName )
  {
    this.claimApprovalAudienceTypeName = claimApprovalAudienceTypeName;
  }

  public void setDrawYourOwnCard( boolean drawYourOwnCard )
  {
    this.drawYourOwnCard = drawYourOwnCard;
  }

  public boolean isDrawYourOwnCard()
  {
    return drawYourOwnCard;
  }

  public boolean isAllowPublicRecognitionPoints()
  {
    return allowPublicRecognitionPoints;
  }

  public void setAllowPublicRecognitionPoints( boolean allowPublicRecognitionPoints )
  {
    this.allowPublicRecognitionPoints = allowPublicRecognitionPoints;
  }

  public String getPublicRecognitionAward()
  {
    return publicRecognitionAward;
  }

  public void setPublicRecognitionAward( String publicRecognitionAward )
  {
    this.publicRecognitionAward = publicRecognitionAward;
  }

  public String getPublicRecognitionBudget()
  {
    return publicRecognitionBudget;
  }

  public void setPublicRecognitionBudget( String publicRecognitionBudget )
  {
    this.publicRecognitionBudget = publicRecognitionBudget;
  }

  public List getPublicRecognitionGiversList()
  {
    return publicRecognitionGiversList;
  }

  public void setPublicRecognitionGiversList( List publicRecognitionGiversList )
  {
    this.publicRecognitionGiversList = publicRecognitionGiversList;
  }

  public boolean isPublicRecognitionGiversExists()
  {
    return publicRecognitionGiversExists;
  }

  public void setPublicRecognitionGiversExists( boolean publicRecognitionGiversExists )
  {
    this.publicRecognitionGiversExists = publicRecognitionGiversExists;
  }

  public boolean isPublicRecognitionGiverAllPax()
  {
    return publicRecognitionGiverAllPax;
  }

  public void setPublicRecognitionGiverAllPax( boolean publicRecognitionGiverAllPax )
  {
    this.publicRecognitionGiverAllPax = publicRecognitionGiverAllPax;
  }

  public void setBehaviorRequired( String behaviorRequired )
  {
    this.behaviorRequired = behaviorRequired;
  }

  public String getBehaviorRequired()
  {
    return behaviorRequired;
  }

  public boolean isAllowPublicRecognition()
  {
    return allowPublicRecognition;
  }

  public void setAllowPublicRecognition( boolean allowPublicRecognition )
  {
    this.allowPublicRecognition = allowPublicRecognition;
  }

  public String getBillCodesActive()
  {
    return billCodesActive;
  }

  public void setBillCodesActive( String billCodesActive )
  {
    this.billCodesActive = billCodesActive;
  }

  private UserCharacteristicService getUserCharacteristicService()
  {
    return (UserCharacteristicService)getService( UserCharacteristicService.BEAN_NAME );
  }

  public String getTileDisplayStartDate()
  {
    return tileDisplayStartDate;
  }

  public void setTileDisplayStartDate( String tileDisplayStartDate )
  {
    this.tileDisplayStartDate = tileDisplayStartDate;
  }

  public String getTileDisplayEndDate()
  {
    return tileDisplayEndDate;
  }

  public void setTileDisplayEndDate( String tileDisplayEndDate )
  {
    this.tileDisplayEndDate = tileDisplayEndDate;
  }

  public List getManagerWebRulesAudienceList()
  {
    return managerWebRulesAudienceList;
  }

  public void setManagerWebRulesAudienceList( List managerWebRulesAudienceList )
  {
    this.managerWebRulesAudienceList = managerWebRulesAudienceList;
  }

  public List getPartnerWebRulesAudienceList()
  {
    return partnerWebRulesAudienceList;
  }

  public void setPartnerWebRulesAudienceList( List partnerWebRulesAudienceList )
  {
    this.partnerWebRulesAudienceList = partnerWebRulesAudienceList;
  }

  public boolean isManagerWebRulesSpecifyAudience()
  {
    return managerWebRulesSpecifyAudience;
  }

  public void setManagerWebRulesSpecifyAudience( boolean managerWebRulesSpecifyAudience )
  {
    this.managerWebRulesSpecifyAudience = managerWebRulesSpecifyAudience;
  }

  public boolean isPartnerWebRulesSpecifyAudience()
  {
    return partnerWebRulesSpecifyAudience;
  }

  public void setPartnerWebRulesSpecifyAudience( boolean partnerWebRulesSpecifyAudience )
  {
    this.partnerWebRulesSpecifyAudience = partnerWebRulesSpecifyAudience;
  }

  public String getManagerWebRulesAudienceTypeName()
  {
    return managerWebRulesAudienceTypeName;
  }

  public void setManagerWebRulesAudienceTypeName( String managerWebRulesAudienceTypeName )
  {
    this.managerWebRulesAudienceTypeName = managerWebRulesAudienceTypeName;
  }

  public String getPartnerWebRulesAudienceTypeName()
  {
    return partnerWebRulesAudienceTypeName;
  }

  public void setPartnerWebRulesAudienceTypeName( String partnerWebRulesAudienceTypeName )
  {
    this.partnerWebRulesAudienceTypeName = partnerWebRulesAudienceTypeName;
  }

  public boolean isManagerWebRulesAllPax()
  {
    return managerWebRulesAllPax;
  }

  public void setManagerWebRulesAllPax( boolean managerWebRulesAllPax )
  {
    this.managerWebRulesAllPax = managerWebRulesAllPax;
  }

  public boolean isPartnerWebRulesAllPax()
  {
    return partnerWebRulesAllPax;
  }

  public void setPartnerWebRulesAllPax( boolean partnerWebRulesAllPax )
  {
    this.partnerWebRulesAllPax = partnerWebRulesAllPax;
  }

  public boolean isManagerWebRulesAllEligible()
  {
    return managerWebRulesAllEligible;
  }

  public void setManagerWebRulesAllEligible( boolean managerWebRulesAllEligible )
  {
    this.managerWebRulesAllEligible = managerWebRulesAllEligible;
  }

  public boolean isPartnerWebRulesAllEligible()
  {
    return partnerWebRulesAllEligible;
  }

  public void setPartnerWebRulesAllEligible( boolean partnerWebRulesAllEligible )
  {
    this.partnerWebRulesAllEligible = partnerWebRulesAllEligible;
  }

  public boolean isPartnerAvailable( Promotion promotion )
  {
    if ( promotion.getPartnerAudienceType() != null )
    {
      partnerAvailable = true;
    }
    else
    {
      partnerAvailable = false;
    }
    return partnerAvailable;
  }

  public boolean getPartnerAvailable()
  {
    return partnerAvailable;
  }

  public void setPartnerAvailable( boolean partnerAvailable )
  {
    this.partnerAvailable = partnerAvailable;
  }

  public String getPreSelectedPartner()
  {
    return preSelectedPartner;
  }

  public void setPreSelectedPartner( String preSelectedPartner )
  {
    this.preSelectedPartner = preSelectedPartner;
  }

  public void setAutoCompletePartners( boolean autoCompletePartners )
  {
    this.autoCompletePartners = autoCompletePartners;
  }

  public boolean isAutoCompletePartners()
  {
    return autoCompletePartners;
  }

  public String getPromotionStatusDesc()
  {
    return promotionStatusDesc;
  }

  public void setPromotionStatusDesc( String promotionStatusDesc )
  {
    this.promotionStatusDesc = promotionStatusDesc;
  }

  public String getPromotionTheme()
  {
    return promotionTheme;
  }

  public String getUnevenPlaySelection()
  {
    return unevenPlaySelection;
  }

  public void setPromotionTheme( String promotionTheme )
  {
    this.promotionTheme = promotionTheme;
  }

  public void setUnevenPlaySelection( String unevenPlaySelection )
  {
    this.unevenPlaySelection = unevenPlaySelection;
  }

  public boolean isDisplayTeamProgress()
  {
    return displayTeamProgress;
  }

  public void setDisplayTeamProgress( boolean displayTeamProgress )
  {
    this.displayTeamProgress = displayTeamProgress;
  }

  /**
   * @param numberOfRounds the numberOfRounds to set
   */
  public void setNumberOfRounds( int numberOfRounds )
  {
    this.numberOfRounds = numberOfRounds;
  }

  /**
   * @return the numberOfRounds
   */
  public int getNumberOfRounds()
  {
    return numberOfRounds;
  }

  /**
   * @param numberOfDayPerRound the numberOfDayPerRound to set
   */
  public void setNumberOfDayPerRound( int numberOfDayPerRound )
  {
    this.numberOfDayPerRound = numberOfDayPerRound;
  }

  /**
   * @return the numberOfDayPerRound
   */
  public int getNumberOfDayPerRound()
  {
    return numberOfDayPerRound;
  }

  /**
   * @param startDateForFirstRound the startDateForFirstRound to set
   */
  public void setStartDateForFirstRound( String startDateForFirstRound )
  {
    this.startDateForFirstRound = startDateForFirstRound;
  }

  /**
   * @return the startDateForFirstRound
   */
  public String getStartDateForFirstRound()
  {
    return startDateForFirstRound;
  }

  public String getDaysPriorToRoundStartSchedule()
  {
    return daysPriorToRoundStartSchedule;
  }

  public void setDaysPriorToRoundStartSchedule( String daysPriorToRoundStartSchedule )
  {
    this.daysPriorToRoundStartSchedule = daysPriorToRoundStartSchedule;
  }

  public Map getDivisionAudienceMap()
  {
    return divisionAudienceMap;
  }

  public void setDivisionAudienceMap( Map divisionAudienceMap )
  {
    this.divisionAudienceMap = divisionAudienceMap;
  }

  public boolean isDivisionAudienceExists()
  {
    return divisionAudienceExists;
  }

  public void setDivisionAudienceExists( boolean divisionAudienceExists )
  {
    this.divisionAudienceExists = divisionAudienceExists;
  }

  public boolean isSmackTalkAvailable()
  {
    return smackTalkAvailable;
  }

  public void setSmackTalkAvailable( boolean smackTalkAvailable )
  {
    this.smackTalkAvailable = smackTalkAvailable;
  }

  public boolean isCorpAndMngr()
  {
    return corpAndMngr;
  }

  public void setCorpAndMngr( boolean corpAndMngr )
  {
    this.corpAndMngr = corpAndMngr;
  }

  public boolean isMobAppEnabled()
  {
    return mobAppEnabled;
  }

  public void setMobAppEnabled( boolean mobAppEnabled )
  {
    this.mobAppEnabled = mobAppEnabled;
  }

  public boolean isPurlStandardMessageEnabled()
  {
    return purlStandardMessageEnabled;
  }

  public void setPurlStandardMessageEnabled( boolean purlStandardMessageEnabled )
  {
    this.purlStandardMessageEnabled = purlStandardMessageEnabled;
  }

  public String getScoreActive()
  {
    return scoreActive;
  }

  public void setScoreActive( String scoreActive )
  {
    this.scoreActive = scoreActive;
  }

  public String getCompanyGoal()
  {
    return companyGoal;
  }

  public void setCompanyGoal( String companyGoal )
  {
    this.companyGoal = companyGoal;
  }

  public boolean isIncludeCelebrations()
  {
    return includeCelebrations;
  }

  public void setIncludeCelebrations( boolean includeCelebrations )
  {
    this.includeCelebrations = includeCelebrations;
  }

  public String isAnniversaryInYears()
  {
    return anniversaryInYears;
  }

  public void setAnniversaryInYears( String anniversaryInYears )
  {
    this.anniversaryInYears = anniversaryInYears;
  }

  public String getCelebrationDisplayPeriod()
  {
    return celebrationDisplayPeriod;
  }

  public void setCelebrationDisplayPeriod( String celebrationDisplayPeriod )
  {
    this.celebrationDisplayPeriod = celebrationDisplayPeriod;
  }

  public boolean isAllowOwnerMessage()
  {
    return allowOwnerMessage;
  }

  public void setAllowOwnerMessage( boolean allowOwnerMessage )
  {
    this.allowOwnerMessage = allowOwnerMessage;
  }

  public boolean isShareToMedia()
  {
    return shareToMedia;
  }

  public void setShareToMedia( boolean shareToMedia )
  {
    this.shareToMedia = shareToMedia;
  }

  public boolean isServiceAnniversary()
  {
    return serviceAnniversary;
  }

  public void setServiceAnniversary( boolean serviceAnniversary )
  {
    this.serviceAnniversary = serviceAnniversary;
  }

  public String getApprovalLevel()
  {
    return approvalLevel;
  }

  public void setApprovalLevel( String approvalLevel )
  {
    this.approvalLevel = approvalLevel;
  }

  public Integer getMaxContestsToDisplay()
  {
    return maxContestsToDisplay;
  }

  public void setMaxContestsToDisplay( Integer maxContestsToDisplay )
  {
    this.maxContestsToDisplay = maxContestsToDisplay;
  }

  public Integer getDaysToArchive()
  {
    return daysToArchive;
  }

  public void setDaysToArchive( Integer daysToArchive )
  {
    this.daysToArchive = daysToArchive;
  }

  public String getContestGuideUrl()
  {
    return contestGuideUrl;
  }

  public void setContestGuideUrl( String contestGuideUrl )
  {
    this.contestGuideUrl = contestGuideUrl;
  }

  public boolean isWhyNomination()
  {
    return whyNomination;
  }

  public void setWhyNomination( boolean whyNomination )
  {
    this.whyNomination = whyNomination;
  }

  public boolean isViewPastWinners()
  {
    return viewPastWinners;
  }

  public void setViewPastWinners( boolean viewPastWinners )
  {
    this.viewPastWinners = viewPastWinners;
  }

  public boolean isAllowUnderArmour()
  {
    return allowUnderArmour;
  }

  public void setAllowUnderArmour( boolean includeUnderArmour )
  {
    this.allowUnderArmour = includeUnderArmour;
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
  //Client customization for WIP #58122 starts
  public boolean isLevelPayoutByApproverAvailable()
  {
    return levelPayoutByApproverAvailable;
  }

  public void setLevelPayoutByApproverAvailable( boolean levelPayoutByApproverAvailable )
  {
    this.levelPayoutByApproverAvailable = levelPayoutByApproverAvailable;
  }
 //Client customization for WIP #58122 ends

}
