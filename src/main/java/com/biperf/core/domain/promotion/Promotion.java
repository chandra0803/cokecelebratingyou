/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/domain/promotion/Promotion.java,v $
 */

package com.biperf.core.domain.promotion;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringEscapeUtils;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.budget.BudgetMaster;
import com.biperf.core.domain.calculator.Calculator;
import com.biperf.core.domain.claim.ClaimForm;
import com.biperf.core.domain.claim.ClaimFormStepElement;
import com.biperf.core.domain.enums.ApprovalConditionalAmmountOperatorType;
import com.biperf.core.domain.enums.ApprovalStatusType;
import com.biperf.core.domain.enums.ApprovalType;
import com.biperf.core.domain.enums.ApproverType;
import com.biperf.core.domain.enums.PartnerAudienceType;
import com.biperf.core.domain.enums.PickListItemSortOrderComparator;
import com.biperf.core.domain.enums.PrimaryAudienceType;
import com.biperf.core.domain.enums.PromotionApprovalOptionType;
import com.biperf.core.domain.enums.PromotionAwardsType;
import com.biperf.core.domain.enums.PromotionStatusType;
import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.domain.enums.PublicRecognitionAudienceType;
import com.biperf.core.domain.enums.ScoreBy;
import com.biperf.core.domain.enums.SecondaryAudienceType;
import com.biperf.core.domain.enums.SweepstakesClaimEligibilityType;
import com.biperf.core.domain.enums.SweepstakesMultipleAwardsType;
import com.biperf.core.domain.enums.SweepstakesWinnerEligibilityType;
import com.biperf.core.domain.enums.SweepstakesWinnersType;
import com.biperf.core.domain.enums.WebRulesAudienceType;
import com.biperf.core.domain.gamification.Badge;
import com.biperf.core.domain.hierarchy.Hierarchy;
import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.domain.hierarchy.NodeType;
import com.biperf.core.domain.journal.Journal;
import com.biperf.core.domain.participant.Audience;
import com.biperf.core.domain.proxy.ProxyModulePromotion;
import com.biperf.core.service.BaseAssociationRequest;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.UserManager;
import com.biperf.util.StringUtils;
import com.objectpartners.cms.util.CmsResourceBundle;

/**
 * Promotion.
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
 * <td>crosenquest</td>
 * <td>Jun 27, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 *
 *
 */
public abstract class Promotion extends BaseDomain implements Cloneable
{

  /**
   * CM Promotion Data webrules asset prefix
   */
  public static final String CM_PROMOTION_DATA_WEBRULES_ASSET_PREFIX = "promotion_data.";

  /**
   * CM Promotion Data section
   */
  public static final String CM_PROMOTION_DATA_SECTION = "promotion_data";
  
  
//Client customizations for WIP #59418 
  public static final String PROMOTION_ASSET_TYPE_NAME = "_PromotionData2";


  /**
   * CM Promotion Data webrules key prefix
   */
  public static final String CM_PROMOTION_DATA_WEBRULES_TEXT_KEY_PREFIX = "WEBRULES_NAME_";

  /**
   * CM Promotion Data webrules key desc
   */
  public static final String CM_PROMOTION_DATA_WEBRULES_TEXT_KEY_DESC = "Promotion Web Rules Text";

  public static final String GIVER_ACTIVITY_TEXT_KEY_PREFIX = "DISPLAY_GIVER_TEXT_";
  public static final String RECEIVER_ACTIVITY_TEXT_KEY_PREFIX = "DISPLAY_RECEIVER_TEXT_";
  public static final String ACTIVITY_TRACKER_DATA_ASSET_PREFIX = "activity_trackers_data.";
  public static final String ACTIVITY_TRACKER_DATA_SECTION_CODE = "activity_trackers_data";
  public static final String ACTIVITY_TRACKER_ASSET_TYPE_NAME = "_ACTIVITY_TRACKERS_DATA";
  public static final String ACTIVITY_TRACKER_DATA_TEXT_KEY_DESC = "Activity Tracker Text";

  // SAVE PROMOTION NAME IN CM
  public static final String PROMOTION_NAME_ASSET_PREFIX = "promotion_name.";
  public static final String PROMOTION_NAME_SECTION_CODE = "promotion_name";
  public static final String PROMOTION_NAME_ASSET_TYPE_NAME = "_PROMOTION_NAME_DATA";
  public static final String PROMOTION_NAME_KEY_DESC = "Promotion Name";
  public static final String PROMOTION_NAME_KEY_PREFIX = "PROMOTION_NAME_";

  // SAVE QUIZ PROMOTION DETAILS IN CM
  public static final String QUIZ_PROMOTION_DETAILS_CM_ASSET_PREFIX = "quiz_page_detail.";
  public static final String QUIZ_PROMOTION_DETAILS_CM_ASSET_TYPE_KEY = "HTML_KEY";
  public static final String QUIZ_PROMOTION_DETAILS_SECTION_CODE = "quiz_page_detail";
  public static final String QUIZ_PROMOTION_DETAILS_CM_ASSET_NAME_TYPE = "Name Type";
  public static final String QUIZ_PROMOTION_DETAILS_TEXT = "Quiz Promotion Details";

  // SAVE GOAL QUEST OBEJCTIVE IN CM
  // SAVE GOAL QUEST AND CHALLENGE POINT OBEJCTIVE IN CM
  public static final String GQ_CP_PROMO_OBJECTIVE_ASSET_PREFIX = "goal_quest_cp_promo_objective.";
  public static final String GQ_CP_PROMO_OBJECTIVE_SECTION_CODE = "goal_quest_cp_promo_objective";
  public static final String GQ_CP_PROMO_OBJECTIVE_ASSET_TYPE_NAME = "_GOAL_QUEST_CP_PROMO_OBJECTIVE_DATA";
  public static final String GQ_CP_PROMO_OBJECTIVE_KEY_DESC = "Goal Quest and Challenge Point Promotion Obejctive";
  public static final String GQ_CP_PROMO_OBJECTIVE_KEY_PREFIX = "GOAL_QUEST_CP_PROMO_OBJECTIVE_";

  // SAVE PROMOTION OVERVIEW IN CM
  public static final String PROMO_OVERVIEW_CM_ASSET_PREFIX = "promo_overview.";
  public static final String PROMO_OVERVIEW_CM_ASSET_TYPE_KEY = "PROMO_OVERVIEW";
  public static final String PROMO_OVERVIEW_SECTION_CODE = "promo_overview";
  public static final String PROMO_OVERVIEW_CM_ASSET_TYPE_NAME = "_PROMO_OVERVIEW";
  public static final String PROMO_OVERVIEW_TEXT_KEY_DESC = "Promotion Overview";

  // SAVE GOAL QUEST AND CHALLENGE POINT BASE UNIT IN CM
  public static final String GQ_CP_PROMO_BASE_UNIT_ASSET_PREFIX = "goal_quest_cp_promo_baseunit.";
  public static final String GQ_CP_PROMO_BASE_UNIT_SECTION_CODE = "goal_quest_cp_promo_baseunit";
  public static final String GQ_CP_PROMO_BASE_UNIT_ASSET_TYPE_NAME = "_GOAL_QUEST_CP_PROMO_BASE_UNIT_DATA";
  public static final String GQ_CP_PROMO_BASE_UNIT_KEY_DESC = "Goal Quest and Challenge Point Promotion Base Unit";
  public static final String GQ_CP_PROMO_BASE_UNIT_KEY_PREFIX = "GOAL_QUEST_CP_PROMO_BASE_UNIT_";

  // SAVE THROWDOWN BASE UNIT IN CM
  public static final String TD_PROMO_BASE_UNIT_ASSET_PREFIX = "throwdown_promo_baseunit.";
  public static final String TD_PROMO_BASE_UNIT_SECTION_CODE = "throwdown_promo_baseunit";
  public static final String TD_PROMO_BASE_UNIT_ASSET_TYPE_NAME = "_THROWDOWN_PROMO_BASE_UNIT_DATA";
  public static final String TD_PROMO_BASE_UNIT_KEY_DESC = "Throwdown Promotion Base Unit";
  public static final String TD_PROMO_BASE_UNIT_KEY_PREFIX = "THROWDOWN_PROMO_BASE_UNIT_";

  // SAVE GOAL QUEST AND CHALLENGE POINT GOALS & DESCRIPTION IN CM
  public static final String CM_GOAL_DESCRIPTION_ASSET_PREFIX = "goal_quest_cp.goal_descrpit";
  public static final String CM_GOAL_DESCRIPTION_ASSET_TYPE = "_GQ_CP_GOAL_DESCRIPTION";
  public static final String CM_GOAL_DESCRIPTION_SECTION = "goal_quest_cp";
  public static final String CM_GOALS_KEY = "GOALS";
  public static final String CM_GOALS_KEY_DESC = "Goal";
  public static final String CM_GOAL_DESCRIPTION_KEY = "GOAL_DESCRIPTION";
  public static final String CM_GOAL_DESCRIPTION_KEY_DESC = "Goal Description";

  // SAVE PROMOTION CELEBRATIONS IN CM
  public static final String RECOGNITION_CELEBRATIONS_MESSAGE_CM_ASSET_PREFIX = "promo_celebrations.";
  public static final String RECOGNITION_CELEBRATIONS_MESSAGE_CM_ASSET_TYPE_KEY = "PROMO_CELEBRATIONS";
  public static final String RECOGNITION_CELEBRATIONS_MESSAGE_SECTION_CODE = "promo_celebrations";
  public static final String RECOGNITION_CELEBRATIONS_MESSAGE_CM_ASSET_TYPE_NAME = "_PROMO_CELEBRATIONS";
  public static final String RECOGNITION_CELEBRATIONS_MESSAGE_TEXT_KEY_DESC = "Promotion Celebrations";

  // SAVE PROMOTION LEVEL LABEL NAME IN CM
  public static final String PROMOTION_LEVEL_LABEL_NAME_ASSET_PREFIX = "promotion_level_label_name.";
  public static final String PROMOTION_LEVEL_LABEL_NAME_SECTION_CODE = "promotion_level_label_name";
  public static final String PROMOTION_LEVEL_LABEL_NAME_ASSET_TYPE_NAME = "_PROMOTION_LEVEL_LABEL_NAME_DATA";
  public static final String PROMOTION_LEVEL_LABEL_NAME_KEY_DESC = "Promotion Level Label Name";
  public static final String PROMOTION_LEVEL_LABEL_NAME_KEY_PREFIX = "PROMOTION_LEVEL_LABEL_NAME_";

  // SAVE PROMOTION TIME PERIOD NAME IN CM
  public static final String PROMOTION_TIME_PERIOD_NAME_ASSET_PREFIX = "promotion_time_period_name.";
  public static final String PROMOTION_TIME_PERIOD_NAME_SECTION_CODE = "promotion_time_period_name";
  public static final String PROMOTION_TIME_PERIOD_NAME_ASSET_TYPE_NAME = "_PROMOTION_TIME_PERIOD_NAME_DATA";
  public static final String PROMOTION_TIME_PERIOD_NAME_KEY_DESC = "Promotion Time Period Name";
  public static final String PROMOTION_TIME_PERIOD_NAME_KEY_PREFIX = "PROMOTION_TIME_PERIOD_NAME_";

  // SAVE PROMOTION PAYOUT DESCRIPTION IN CM
  public static final String PAYOUT_DESCRIPTION_ASSET_PREFIX = "payout_description.";
  public static final String PAYOUT_DESCRIPTION_SECTION_CODE = "payout_description";
  public static final String PAYOUT_DESCRIPTION_ASSET_TYPE_NAME = "_PAYOUT_DESCRIPTION_DATA";
  public static final String PAYOUT_DESCRIPTION_KEY_DESC = "Payout Description";
  public static final String PAYOUT_DESCRIPTION_KEY_PREFIX = "PAYOUT_DESCRIPTION_";

  private String promotionName;
  private String promoNameAssetCode;
  private String upperCaseName; // for order by name case insensitive
  private Date submissionStartDate;
  private Date submissionEndDate;
  private Date approvalStartDate;
  private Date approvalEndDate;
  private Integer approvalAutoDelayDays;
  private Integer approvalConditionalClaimCount;
  private Hierarchy approvalHierarchy;
  private NodeType approvalNodeType;
  private Integer approvalNodeLevels;
  private Double approvalConditionalAmount;
  private Date webRulesStartDate;
  private Date webRulesEndDate;
  private String cmAssetCode;
  private String webRulesCmKey;
  private boolean webRulesActive;
  private boolean taxable;
  private boolean deleted;

  private ApprovalConditionalAmmountOperatorType approvalConditionalAmountOperator;
  private ApprovalType approvalType;
  private ApproverType approverType;
  private PromotionType promotionType;
  private PromotionStatusType promotionStatus;
  private PrimaryAudienceType primaryAudienceType; // givers or submitters
  private SecondaryAudienceType secondaryAudienceType; // receivers or team
  private WebRulesAudienceType webRulesAudienceType;
  private PromotionAwardsType awardType;
  private PublicRecognitionAudienceType publicRecognitionAudienceType;
  private ClaimForm claimForm;
  private ClaimFormStepElement approvalConditionalAmountField;
  private Node approverNode;

  private List promotionParticipantApprovers = new ArrayList();
  private List promotionParticipantSubmitters = new ArrayList();
  private List promotionNotifications = new ArrayList();
  private Set<PromotionWizard> promotionWizardOrder = new HashSet<PromotionWizard>();
  // TODO see if we really need a set of journals here
  private Set journals = new LinkedHashSet();
  private Set promotionClaimFormStepElementValidations = new LinkedHashSet();
  private Set promotionApprovalOptions = new LinkedHashSet();
  private Set promotionWebRulesAudiences = new LinkedHashSet();
  private Set promotionPrimaryAudiences = new LinkedHashSet();
  private Set promotionSecondaryAudiences = new LinkedHashSet();
  protected Set promotionSweepstakes = new LinkedHashSet();
  protected List<PromotionBillCode> promotionBillCodes = new ArrayList<PromotionBillCode>();
  protected List<SweepstakesBillCode> sweepstakesBillCodes = new ArrayList<SweepstakesBillCode>();

  private BudgetMaster budgetMaster;
  private Badge badge;

  private Set proxyModulePromotions = new LinkedHashSet();

  private boolean sweepstakesActive;
  private SweepstakesClaimEligibilityType sweepstakesClaimEligibilityType;
  private SweepstakesWinnerEligibilityType sweepstakesWinnerEligibilityType;
  private SweepstakesMultipleAwardsType sweepstakesMultipleAwardType;
  private SweepstakesWinnersType sweepstakesPrimaryBasisType;
  private Integer sweepstakesPrimaryWinners;
  private Long sweepstakesPrimaryAwardLevel;
  private Long sweepstakesPrimaryAwardAmount;
  private SweepstakesWinnersType sweepstakesSecondaryBasisType;
  private Integer sweepstakesSecondaryWinners;
  private Long sweepstakesSecondaryAwardLevel;
  private Long sweepstakesSecondaryAwardAmount;

  private boolean onlineEntry;
  private boolean fileLoadEntry;
  private String purpose;
  private String wellnessUrl;

  private String certificate;

  private boolean allowSelfEnroll;
  private String enrollProgramCode;
  private PartnerAudienceType partnerAudienceType; // partners for GQ Promo
  private Set promotionPartnerAudiences = new LinkedHashSet();

  /**
   * Will hold "Fixed", "range" and "Calculated" values
   */
  private Calculator calculator;

  /**
   * Score By
   */
  private ScoreBy scoreBy;

  private Set promoMerchCountries = new LinkedHashSet();

  private boolean billCodesActive;

  private boolean swpBillCodesActive;

  private Date tileDisplayStartDate;
  private Date tileDisplayEndDate;

  private String overview;

  private BudgetMaster cashBudgetMaster;
  
  private Integer numberOfDays;

  //Client customization start
  private Boolean utilizeParentBudgets;  

  public Boolean isUtilizeParentBudgets()
  {
    return utilizeParentBudgets != null ? utilizeParentBudgets : Boolean.FALSE;
  }

  public void setUtilizeParentBudgets( Boolean utilizeParentBudgets )
  {
    this.utilizeParentBudgets = utilizeParentBudgets;
  }
  //Client customization end

  /* Customization WIP#42198 start */
  private Boolean adihCashOption;
  private Long adihCashMaxAward;

  public Boolean getAdihCashOption()
  {
    return adihCashOption != null ? adihCashOption : Boolean.FALSE;
  }

  public void setAdihCashOption( Boolean adihCashOption )
  {
    this.adihCashOption = adihCashOption;
  }

  public Long getAdihCashMaxAward()
  {
    return adihCashMaxAward;
  }

  public void setAdihCashMaxAward( Long adihCashMaxAward )
  {
    this.adihCashMaxAward = adihCashMaxAward;
  }
  /* Customization WIP#42198 End */
  
  
  public Integer getNumberOfDays()
  {
    return numberOfDays;
  }

  public void setNumberOfDays( Integer numberOfDays )
  {
    this.numberOfDays = numberOfDays;
  }

  public Set<PromoMerchCountry> getPromoMerchCountries()
  {
    return promoMerchCountries;
  }

  public void setPromoMerchCountries( Set promoMerchCountries )
  {
    this.promoMerchCountries = promoMerchCountries;
  }

  public void addPromotionMerchCountry( PromoMerchCountry merchCountry )
  {
    merchCountry.setPromotion( this );
    promoMerchCountries.add( merchCountry );
  }

  /**
   * Get the promoMerchCountry for the specified country code
   * @param countryCode
   * @return
   */
  public PromoMerchCountry getPromoMerchCountryForCountryCode( String countryCode )
  {
    if ( countryCode != null && getPromoMerchCountries() != null )
    {
      for ( Iterator promoMerchCountryIter = getPromoMerchCountries().iterator(); promoMerchCountryIter.hasNext(); )
      {
        PromoMerchCountry promoMerchCountry = (PromoMerchCountry)promoMerchCountryIter.next();
        if ( promoMerchCountry.getCountry().getCountryCode().equals( countryCode ) )
        {
          return promoMerchCountry;
        }
      }
    }
    return null;
  }

  /**
   * Constructor
   */
  public Promotion()
  {
    super();
  }

  /**
   * Get the promotion name
   *
   * @return promotion name
   */
  public String getName()
  {
    return getPromoNameFromCM();
  }

  /**
   * Set the promotion name
   *
   * @param name
   */
  public void setName( String name )
  {
    setPromotionName( name );
  }

  public String getPromotionName()
  {
    return promotionName;
  }

  public void setPromotionName( String promotionName )
  {
    // Bug Bug 55497 - The promotion name is having spaces in the end in db column where as in the
    // CM it is stored without spaces.
    // This is causing equals method returning false as the name has space in the domain and giving
    // NonUniqueObjectException.
    if ( promotionName != null )
    {
      promotionName = promotionName.trim();
    }
    this.promotionName = promotionName;
  }

  public String getPromoNameFromCM()
  {
    String promotionName = null;
    if ( this.promoNameAssetCode != null )
    {
      promotionName = CmsResourceBundle.getCmsBundle().getString( this.promoNameAssetCode, Promotion.PROMOTION_NAME_KEY_PREFIX );
    }
    return StringEscapeUtils.unescapeHtml4( promotionName );
  }

  /**
   * @param promotionClaimFormStepElementValidation
   */
  public void addPromotionClaimFormStepElementValidation( PromotionClaimFormStepElementValidation promotionClaimFormStepElementValidation )
  {
    promotionClaimFormStepElementValidation.setPromotion( this );
    this.promotionClaimFormStepElementValidations.add( promotionClaimFormStepElementValidation );
  }

  /**
   * @return promotionClaimFormStepElementValidations
   */
  public Set getPromotionClaimFormStepElementValidations()
  {
    return this.promotionClaimFormStepElementValidations;
  }

  /**
   * @param promotionClaimFormStepElementValidations
   */
  public void setPromotionClaimFormStepElementValidations( Set promotionClaimFormStepElementValidations )
  {
    this.promotionClaimFormStepElementValidations = promotionClaimFormStepElementValidations;
  }

  /**
   * Get Claim Submission End Date
   *
   * @return Date
   */
  public Date getSubmissionEndDate()
  {
    return submissionEndDate;
  }

  /**
   * Set Claim Submission End Date
   *
   * @param endDate
   */
  public void setSubmissionEndDate( Date endDate )
  {
    this.submissionEndDate = endDate;
  }

  /**
   * Get Claim Submission Start Date
   *
   * @return Date
   */
  public Date getSubmissionStartDate()
  {
    return submissionStartDate;
  }

  /**
   * Set Claim Submission Start Date
   *
   * @param startDate
   */
  public void setSubmissionStartDate( Date startDate )
  {
    this.submissionStartDate = startDate;
  }

  /**
   * Get promotion expired or not
   *
   * @return true if promotion is expired; return false if in any other status
   */
  public boolean isExpired()
  {
    if ( promotionStatus != null )
    {
      return promotionStatus.getCode().equals( PromotionStatusType.EXPIRED );
    }

    return false;
  }

  /**
   * Get promotion live or not
   *
   * @return true if promotion is live; return false if in any other status
   */
  public boolean isLive()
  {
    if ( promotionStatus != null )
    {
      return promotionStatus.getCode().equals( PromotionStatusType.LIVE );
    }

    return false;
  }

  /**
   * Get promotion complete or not
   *
   * @return true if promotion is complete; return false if in any other status
   */
  public boolean isComplete()
  {
    if ( promotionStatus != null )
    {
      return promotionStatus.getCode().equals( PromotionStatusType.COMPLETE );
    }

    return false;
  }

  /**
   * Get promotion under construction or not
   *
   * @return true if promotion is under construction; return false if in any other status
   */
  public boolean isUnderConstruction()
  {
    if ( promotionStatus != null )
    {
      return promotionStatus.getCode().equals( PromotionStatusType.UNDER_CONSTRUCTION );
    }

    return false;
  }

  /**
   * Checks equality of the object parameter to this. Overridden from
   *
   * @param object
   * @return boolean
   * @see com.biperf.core.domain.BaseDomain#equals(Object)
   */
  public boolean equals( Object object )
  {
    if ( this == object )
    {
      return true;
    }

    if ( ! ( object instanceof Promotion ) )
    {
      return false;
    }

    final Promotion promotion = (Promotion)object;

    if ( getPromotionName() != null )
    {
      if ( !getPromotionName().equals( promotion.getPromotionName() ) )
      {
        return false;
      }
    }

    return true;
  }

  /**
   * Define the hashCode from the id. Overridden from
   *
   * @return int
   * @see com.biperf.core.domain.BaseDomain#hashCode()
   */
  public int hashCode()
  {
    return getName() != null ? getName().hashCode() : 0;
  }

  /**
   * @return promotionStatus
   */
  public PromotionStatusType getPromotionStatus()
  {
    return promotionStatus;
  }

  /**
   * @param promotionStatus
   */
  public void setPromotionStatus( PromotionStatusType promotionStatus )
  {
    this.promotionStatus = promotionStatus;
  }

  /**
   * @return promotionType
   */
  public PromotionType getPromotionType()
  {
    return promotionType;
  }

  /**
   * @param promotionType
   */
  public void setPromotionType( PromotionType promotionType )
  {
    this.promotionType = promotionType;
  }

  /**
   * @return approvalAutoDelayDays
   */
  public Integer getApprovalAutoDelayDays()
  {
    return approvalAutoDelayDays;
  }

  /**
   * @param approvalAutoDelayDays
   */
  public void setApprovalAutoDelayDays( Integer approvalAutoDelayDays )
  {
    this.approvalAutoDelayDays = approvalAutoDelayDays;
  }

  /**
   * @return approvalConditionalAmount
   */
  public Double getApprovalConditionalAmount()
  {
    return approvalConditionalAmount;
  }

  /**
   * @param approvalConditionalAmount
   */
  public void setApprovalConditionalAmount( Double approvalConditionalAmount )
  {
    this.approvalConditionalAmount = approvalConditionalAmount;
  }

  /**
   * @return approvalConditionalAmountOperator
   */
  public ApprovalConditionalAmmountOperatorType getApprovalConditionalAmountOperator()
  {
    return approvalConditionalAmountOperator;
  }

  /**
   * @param approvalConditionalAmountOperator
   */
  public void setApprovalConditionalAmountOperator( ApprovalConditionalAmmountOperatorType approvalConditionalAmountOperator )
  {
    this.approvalConditionalAmountOperator = approvalConditionalAmountOperator;
  }

  /**
   * @return approvalConditionalClaimCount
   */
  public Integer getApprovalConditionalClaimCount()
  {
    return approvalConditionalClaimCount;
  }

  /**
   * @param approvalConditionalClaimCount
   */
  public void setApprovalConditionalClaimCount( Integer approvalConditionalClaimCount )
  {
    this.approvalConditionalClaimCount = approvalConditionalClaimCount;
  }

  /**
   * @return approvalType
   */
  public ApprovalType getApprovalType()
  {
    return approvalType;
  }

  /**
   * @param approvalType
   */
  public void setApprovalType( ApprovalType approvalType )
  {
    this.approvalType = approvalType;
  }

  /**
   * @return approvalNodeLevels
   */
  public Integer getApprovalNodeLevels()
  {
    return approvalNodeLevels;
  }

  /**
   * @param approvalNodeLevels
   */
  public void setApprovalNodeLevels( Integer approvalNodeLevels )
  {
    this.approvalNodeLevels = approvalNodeLevels;
  }

  /**
   * @return approverType
   */
  public ApproverType getApproverType()
  {
    return approverType;
  }

  /**
   * @param aproverType
   */
  public void setApproverType( ApproverType aproverType )
  {
    this.approverType = aproverType;
  }

  /**
   * @return claimForm
   */
  public ClaimForm getClaimForm()
  {
    return claimForm;
  }

  /**
   * @param claimForm
   */
  public void setClaimForm( ClaimForm claimForm )
  {
    this.claimForm = claimForm;
  }

  /**
   * @return approvalConditionalAmountField
   */
  public ClaimFormStepElement getApprovalConditionalAmountField()
  {
    return approvalConditionalAmountField;
  }

  /**
   * @param approvalConditionalAmountFieldsId
   */
  public void setApprovalConditionalAmountField( ClaimFormStepElement approvalConditionalAmountFieldsId )
  {
    this.approvalConditionalAmountField = approvalConditionalAmountFieldsId;
  }

  /**
   * @return approverNode
   */
  public Node getApproverNode()
  {
    return approverNode;
  }

  /**
   * @param approverNode
   */
  public void setApproverNode( Node approverNode )
  {
    this.approverNode = approverNode;
  }

  /**
   * @return true if promotion is marked deleted; return false if not
   */
  public boolean isDeleted()
  {
    return deleted;
  }

  /**
   * @param deleted
   */
  public void setDeleted( boolean deleted )
  {
    this.deleted = deleted;
  }

  /**
   * @return lastUpdateDate in String
   */
  public String getDisplayLastUpdatedDate()
  {
    return DateUtils.toDisplayString( this.getAuditUpdateInfo().getDateModified() );
  }

  /**
   * @return submissionStartDate in String
   */
  public String getDisplayLiveDate()
  {
    return DateUtils.toDisplayString( this.getSubmissionStartDate() );
  }

  /**
   * Get webRulesEndDate
   *
   * @return Date
   */
  public Date getWebRulesEndDate()
  {
    return webRulesEndDate;
  }

  /**
   * Get webRulesEndDate
   *
   * @param webRulesEndDate
   */
  public void setWebRulesEndDate( Date webRulesEndDate )
  {
    this.webRulesEndDate = webRulesEndDate;
  }

  public boolean isWebRulesEndDateExpired()
  {
    if ( webRulesEndDate == null )
    {
      return false;
    }
    Calendar endDateTime = Calendar.getInstance();
    endDateTime.setTime( webRulesEndDate );
    endDateTime.set( Calendar.HOUR_OF_DAY, 23 );
    endDateTime.set( Calendar.MINUTE, 59 );
    endDateTime.set( Calendar.SECOND, 59 );
    if ( endDateTime.before( Calendar.getInstance() ) )
    {
      return true;
    }
    return false;

  }

  /**
   * Get webRulesStartDate
   *
   * @return Date
   */
  public Date getWebRulesStartDate()
  {
    return webRulesStartDate;
  }

  /**
   * Set webRulesStartDate
   *
   * @param webRulesStartDate
   */
  public void setWebRulesStartDate( Date webRulesStartDate )
  {
    this.webRulesStartDate = webRulesStartDate;
  }

  /**
   * Get cmAssetCode
   *
   * @return String
   */
  public String getCmAssetCode()
  {
    return cmAssetCode;
  }

  /**
   * Set cmAssetCode
   *
   * @param webRulesText
   */
  public void setCmAssetCode( String webRulesText )
  {
    this.cmAssetCode = webRulesText;
  }

  /**
   * Get webRulesActive
   *
   * @return boolean
   */
  public boolean isWebRulesActive()
  {
    return webRulesActive;
  }

  /**
   * Set webRulesActive
   *
   * @param webRulesActive
   */
  public void setWebRulesActive( boolean webRulesActive )
  {
    this.webRulesActive = webRulesActive;
  }

  /**
   * Get webRulesCmKey
   *
   * @return String
   */
  public String getWebRulesCmKey()
  {
    return webRulesCmKey;
  }

  /**
   * Set webRulesCmKey
   *
   * @param webRulesCmKey
   */
  public void setWebRulesCmKey( String webRulesCmKey )
  {
    this.webRulesCmKey = webRulesCmKey;
  }

  /**
   * Get approvalStartDate
   *
   * @return Date
   */
  public Date getApprovalStartDate()
  {
    return approvalStartDate;
  }

  /**
   * Set approvalStartDate
   *
   * @param approvalStartDate
   */
  public void setApprovalStartDate( Date approvalStartDate )
  {
    this.approvalStartDate = approvalStartDate;
  }

  /**
   * Get approvalEndDate
   *
   * @return Date
   */
  public Date getApprovalEndDate()
  {
    return approvalEndDate;
  }

  /**
   * Set approvalEndDate
   *
   * @param approvalEndDate
   */
  public void setApprovalEndDate( Date approvalEndDate )
  {
    this.approvalEndDate = approvalEndDate;
  }

  /**
   * Is the award taxable
   *
   * @return boolean
   */
  public boolean isTaxable()
  {
    return taxable;
  }

  /**
   * Set taxable
   *
   * @param taxable
   */
  public void setTaxable( boolean taxable )
  {
    this.taxable = taxable;
  }

  public String getCertificate()
  {
    return certificate;
  }

  public void setCertificate( String certificate )
  {
    this.certificate = certificate;
  }

  /**
   * Does a deep copy of the promotion and its children if specified. This is a customized
   * implementation of Every Set and List should be accounted for in the deepCopy method, if the Set
   * or List is not used it still should be set to a new LinkedHashSet or ArrayList in the copied
   * promotion (See journals and sweepstakes).
   *
   * @see java.lang.Object#clone()
   * @param cloneWithChildren
   * @param newPromotionName
   * @param newChildPromotionNameHolders
   * @return Object
   * @throws CloneNotSupportedException
   */
  public Object deepCopy( boolean cloneWithChildren, String newPromotionName, List newChildPromotionNameHolders ) throws CloneNotSupportedException
  {

    Promotion clonedPromotion = (Promotion)super.clone();

    clonedPromotion.resetBaseDomain();

    // Set the Name.
    clonedPromotion.setName( newPromotionName );

    // set the clone's status to UNDER_CONSTRUCTION
    clonedPromotion.setPromotionStatus( PromotionStatusType.lookup( PromotionStatusType.UNDER_CONSTRUCTION ) );

    // copy the promotionParticipantApprovers
    clonedPromotion.setPromotionParticipantApprovers( new ArrayList() );
    for ( Iterator promotionParticipanApproversIter = this.getPromotionParticipantApprovers().iterator(); promotionParticipanApproversIter.hasNext(); )
    {
      PromotionParticipantApprover promotionParticipantApprover = (PromotionParticipantApprover)promotionParticipanApproversIter.next();
      clonedPromotion.addPromotionParticipantApprover( (PromotionParticipantApprover)promotionParticipantApprover.clone() );
    }

    // copy the promotionParticipantSubmitters
    clonedPromotion.setPromotionParticipantSubmitters( new ArrayList() );
    for ( Iterator promotionParticipantSubmitterIter = this.getPromotionParticipantSubmitters().iterator(); promotionParticipantSubmitterIter.hasNext(); )
    {
      PromotionParticipantSubmitter promotionParticipantSubmitterOriginal = (PromotionParticipantSubmitter)promotionParticipantSubmitterIter.next();
      clonedPromotion.addPromotionParticipantSubmitter( (PromotionParticipantSubmitter)promotionParticipantSubmitterOriginal.clone() );
    }

    // copy the promotionClaimFormElementValidations
    clonedPromotion.setPromotionClaimFormStepElementValidations( new LinkedHashSet() );
    for ( Iterator cfseValidationsIter = this.getPromotionClaimFormStepElementValidations().iterator(); cfseValidationsIter.hasNext(); )
    {
      PromotionClaimFormStepElementValidation promotionClaimFormStepElementValidation = (PromotionClaimFormStepElementValidation)cfseValidationsIter.next();
      clonedPromotion.addPromotionClaimFormStepElementValidation( (PromotionClaimFormStepElementValidation)promotionClaimFormStepElementValidation.clone() );
    }

    // clone the wizard order
    clonedPromotion.setPromotionWizardOrder( new HashSet<PromotionWizard>() );
    for ( Iterator<PromotionWizard> wizardOrderIterator = this.getPromotionWizardOrder().iterator(); wizardOrderIterator.hasNext(); )
    {
      PromotionWizard wizardOrder = wizardOrderIterator.next();
      clonedPromotion.addPromotionWizardOrder( wizardOrder.deepCopy() );
    }

    // copy the promotionNotifications
    clonedPromotion.setPromotionNotifications( new ArrayList() );
    for ( Iterator notificationIter = this.getPromotionNotifications().iterator(); notificationIter.hasNext(); )
    {
      PromotionNotification promotionNotificationOriginal = (PromotionNotification)notificationIter.next();
      if ( promotionNotificationOriginal instanceof PromotionNotificationType )
      {
        clonedPromotion.addPromotionNotification( (PromotionNotificationType)promotionNotificationOriginal.clone() );
      }
      else
      {
        clonedPromotion.addPromotionNotification( (ClaimFormNotificationType)promotionNotificationOriginal.clone() );
      }
    }

    // copy the promotionWebRulesAudiences
    clonedPromotion.setPromotionWebRulesAudiences( new LinkedHashSet() );
    for ( Iterator promotionWebRulesAudiencesIter = this.promotionWebRulesAudiences.iterator(); promotionWebRulesAudiencesIter.hasNext(); )
    {
      PromotionWebRulesAudience promotionWebRulesAudience = (PromotionWebRulesAudience)promotionWebRulesAudiencesIter.next();
      clonedPromotion.addPromotionWebRulesAudience( (PromotionWebRulesAudience)promotionWebRulesAudience.clone() );
    }

    // copy the promotionPrimaryAudiences
    clonedPromotion.setPromotionPrimaryAudiences( new LinkedHashSet() );
    for ( Iterator promotionPrimaryAudiencesIter = this.promotionPrimaryAudiences.iterator(); promotionPrimaryAudiencesIter.hasNext(); )
    {
      PromotionPrimaryAudience promotionPrimaryAudience = (PromotionPrimaryAudience)promotionPrimaryAudiencesIter.next();
      clonedPromotion.addPromotionPrimaryAudience( (PromotionPrimaryAudience)promotionPrimaryAudience.clone() );
    }

    // copy the promotionSecondaryAudiences
    clonedPromotion.setPromotionSecondaryAudiences( new LinkedHashSet() );
    if ( !isGoalQuestPromotion() ) // Don't copy secondary audiences for goalquest - self enroll
                                   // audience must be unique
    {
      for ( Iterator promotionSecondaryAudiencesIter = this.promotionSecondaryAudiences.iterator(); promotionSecondaryAudiencesIter.hasNext(); )
      {
        PromotionSecondaryAudience promotionSecondaryAudience = (PromotionSecondaryAudience)promotionSecondaryAudiencesIter.next();
        clonedPromotion.addPromotionSecondaryAudience( (PromotionSecondaryAudience)promotionSecondaryAudience.clone() );
      }
    }
    // copy the promotionPartnerAudiences
    clonedPromotion.setPromotionPartnerAudiences( new LinkedHashSet() );
    for ( Iterator promotionPartnerAudiencesIter = this.promotionPartnerAudiences.iterator(); promotionPartnerAudiencesIter.hasNext(); )
    {
      PromotionPartnerAudience promotionPartnerAudience = (PromotionPartnerAudience)promotionPartnerAudiencesIter.next();
      clonedPromotion.addPromotionPartnerAudience( (PromotionPartnerAudience)promotionPartnerAudience.clone() );
    }

    // copy promotionApprovalOptions
    clonedPromotion.setPromotionApprovalOptions( new LinkedHashSet() );
    for ( Iterator approvalOptionsIter = this.promotionApprovalOptions.iterator(); approvalOptionsIter.hasNext(); )
    {
      PromotionApprovalOption promotionApprovalOption = (PromotionApprovalOption)approvalOptionsIter.next();

      clonedPromotion.addPromotionApprovalOption( (PromotionApprovalOption)promotionApprovalOption.clone() );
    }

    // copy promoMerchCountries
    clonedPromotion.setPromoMerchCountries( new LinkedHashSet() );
    if ( getPromoMerchCountries() != null )
    {
      for ( Iterator iter = this.getPromoMerchCountries().iterator(); iter.hasNext(); )
      {
        PromoMerchCountry country = (PromoMerchCountry)iter.next();
        clonedPromotion.addPromotionMerchCountry( country.deepCopy() );
      }
    }

    // copy the budget master
    clonedPromotion.setBudgetMaster( this.getBudgetMaster() );

    // copy sweepstakes
    clonedPromotion.setSweepstakesActive( this.isSweepstakesActive() );
    clonedPromotion.setSweepstakesClaimEligibilityType( this.getSweepstakesClaimEligibilityType() );
    clonedPromotion.setSweepstakesWinnerEligibilityType( this.getSweepstakesWinnerEligibilityType() );
    clonedPromotion.setSweepstakesMultipleAwardType( this.getSweepstakesMultipleAwardType() );
    clonedPromotion.setSweepstakesPrimaryBasisType( this.getSweepstakesPrimaryBasisType() );
    clonedPromotion.setSweepstakesPrimaryWinners( this.getSweepstakesPrimaryWinners() );
    clonedPromotion.setSweepstakesPrimaryAwardAmount( this.getSweepstakesPrimaryAwardAmount() );
    clonedPromotion.setSweepstakesSecondaryBasisType( this.getSweepstakesSecondaryBasisType() );
    clonedPromotion.setSweepstakesSecondaryWinners( this.getSweepstakesSecondaryWinners() );
    clonedPromotion.setSweepstakesSecondaryAwardAmount( this.getSweepstakesSecondaryAwardAmount() );

    clonedPromotion.setBillCodesActive( this.billCodesActive );

    // don't copy Journals
    clonedPromotion.setJournals( new LinkedHashSet() );

    // don't copy PromotionSweepstakes
    clonedPromotion.setPromotionSweepstakes( new LinkedHashSet() );

    // don't copy proxyModulePromotions
    clonedPromotion.setProxyModulePromotions( new LinkedHashSet() );

    clonedPromotion.setCertificate( this.certificate );

    // copy promoBillcodes
    clonedPromotion.setPromotionBillCodes( new ArrayList<PromotionBillCode>() );
    clonedPromotion.setSweepstakesBillCodes( new ArrayList<SweepstakesBillCode>() );
    if ( getPromotionBillCodes() != null )
    {
      for ( Iterator iter = this.getPromotionBillCodes().iterator(); iter.hasNext(); )
      {
        PromotionBillCode billCode = (PromotionBillCode)iter.next();
        if ( billCode != null )
        {
          clonedPromotion.addPromotionBillCodes( billCode.deepCopy() );
        }
      }
    }

    if ( getSweepstakesBillCodes() != null )
    {
      for ( Iterator iter = this.getSweepstakesBillCodes().iterator(); iter.hasNext(); )
      {
        SweepstakesBillCode billCode = (SweepstakesBillCode)iter.next();
        if ( billCode != null )
        {
          clonedPromotion.addSweepstakesBillCodes( billCode.deepCopy() );
        }
      }
    }

    return clonedPromotion;
  }

  /**
   * Get promotionApprovalOptions
   *
   * @return promotionApprovalOptions
   */
  public Set getPromotionApprovalOptions()
  {
    return promotionApprovalOptions;
  }

  /**
   * Set promotionApprovalOptions to promotionApprovalOptions
   *
   * @param promotionApprovalOptions
   */
  public void setPromotionApprovalOptions( Set promotionApprovalOptions )
  {
    this.promotionApprovalOptions = promotionApprovalOptions;
  }

  /**
   * Add a promotionApprovalOption to promotionApprovalOptions
   *
   * @param promoApprovalOption
   */
  public void addPromotionApprovalOption( PromotionApprovalOption promoApprovalOption )
  {
    promoApprovalOption.setPromotion( this );
    this.promotionApprovalOptions.add( promoApprovalOption );
  }

  /**
   * Add a journal to journals collection
   *
   * @param journal
   */
  public void addJournal( Journal journal )
  {
    journal.setPromotion( this );
    this.journals.add( journal );
  }

  /**
   * Get promotionNotifications
   *
   * @return promotionNotifications
   */
  public List getPromotionNotifications()
  {
    return promotionNotifications;
  }

  /**
   * Set promotionNotifications to promotionNotifications
   *
   * @param promotionNotifications
   */
  public void setPromotionNotifications( List promotionNotifications )
  {
    this.promotionNotifications = promotionNotifications;
  }

  /**
   * Add a promotionNotification to promotionNotifications
   *
   * @param promoNotification
   */
  public void addPromotionNotification( PromotionNotification promoNotification )
  {
    promoNotification.setPromotion( this );
    this.promotionNotifications.add( promoNotification );
  }

  /**
   * Return the list of journals
   *
   * @return List
   */
  public Set getJournals()
  {
    return journals;
  }

  /**
   * Set the list of journals
   *
   * @param journals
   */
  public void setJournals( Set journals )
  {
    this.journals = journals;
  }

  /**
   * Add a promotionWebRulesAudience to promotionWebRulesAudiences
   *
   * @param promotionWebRulesAudience
   */
  public void addPromotionWebRulesAudience( PromotionWebRulesAudience promotionWebRulesAudience )
  {
    promotionWebRulesAudience.setPromotion( this );
    this.promotionWebRulesAudiences.add( promotionWebRulesAudience );
  }

  /**
   * Get promotionWebRulesAudiences
   *
   * @return promotionWebRulesAudiences
   */
  public Set getPromotionWebRulesAudiences()
  {
    return this.promotionWebRulesAudiences;
  }

  /**
   * Set promotionWebRulesAudiences to promotionWebRulesAudiences
   *
   * @param promotionWebRulesAudiences
   */
  public void setPromotionWebRulesAudiences( Set promotionWebRulesAudiences )
  {
    this.promotionWebRulesAudiences = promotionWebRulesAudiences;
  }

  /**
   * Add a promotionParticipantSubmitter to promotionParticipantSubmitters
   *
   * @param promotionParticipantSubmitter
   */
  public void addPromotionParticipantSubmitter( PromotionParticipantSubmitter promotionParticipantSubmitter )
  {
    promotionParticipantSubmitter.setPromotion( this );
    this.promotionParticipantSubmitters.add( promotionParticipantSubmitter );
  }

  /**
   * Add a promotionParticipantApprover to promotionParticipantApprovers
   *
   * @param promotionParticipantApprover
   */
  public void addPromotionParticipantApprover( PromotionParticipantApprover promotionParticipantApprover )
  {
    promotionParticipantApprover.setPromotion( this );
    this.promotionParticipantApprovers.add( promotionParticipantApprover );
  }

  /**
   * Get promotionParticipantApprovers
   *
   * @return promotionParticipantApprovers
   */
  public List getPromotionParticipantApprovers()
  {
    return promotionParticipantApprovers;
  }

  /**
   * Set promotionParticipantApprovers to promotionParticipantApprovers
   *
   * @param promotionParticipantApprovers
   */
  public void setPromotionParticipantApprovers( List promotionParticipantApprovers )
  {
    this.promotionParticipantApprovers = promotionParticipantApprovers;
  }

  /**
   * Get promotionParticipantSubmitters
   *
   * @return promotionParticipantSubmitters
   */
  public List getPromotionParticipantSubmitters()
  {
    return promotionParticipantSubmitters;
  }

  /**
   * Set promotionParticipantSubmitters to promotionParticipantSubmitters
   *
   * @param promotionParticipantSubmitters
   */
  public void setPromotionParticipantSubmitters( List promotionParticipantSubmitters )
  {
    this.promotionParticipantSubmitters = promotionParticipantSubmitters;
  }

  /**
   * Get denied reason code types
   *
   * @return promotionApprovalOptionTypeCode
   */
  public List getDeniedReasonCodeTypes()
  {
    return getReasonCodeTypes( PromotionApprovalOptionType.DENIED );
  }

  /**
   * Get held reason code types
   *
   * @return promotionApprovalOptionTypeCode
   */
  public List getHeldReasonCodeTypes()
  {
    return getReasonCodeTypes( PromotionApprovalOptionType.HELD );
  }

  private List getReasonCodeTypes( String promotionApprovalOptionTypeCode )
  {
    List reasonCodeTypes = new ArrayList();

    for ( Iterator iter = getPromotionApprovalOptions().iterator(); iter.hasNext(); )
    {
      PromotionApprovalOption promotionApprovalOption = (PromotionApprovalOption)iter.next();
      PromotionApprovalOptionType promotionApprovalOptionType = promotionApprovalOption.getPromotionApprovalOptionType();
      if ( promotionApprovalOptionType.getCode().equals( promotionApprovalOptionTypeCode ) )
      {
        for ( Iterator iterator = promotionApprovalOption.getPromotionApprovalOptionReasons().iterator(); iterator.hasNext(); )
        {
          PromotionApprovalOptionReason promotionApprovalOptionReason = (PromotionApprovalOptionReason)iterator.next();
          reasonCodeTypes.add( promotionApprovalOptionReason.getPromotionApprovalOptionReasonType() );
        }
      }
    }
    return reasonCodeTypes;
  }

  /**
   * Get the list of Approval Option types selected for this promotion. This impl is a "get it done
   * quick" hack. The problem is that approval development and promotion setup development each used
   * their own picklist for approval status type, and they use different codes, and the codes are
   * referred to directly in jsps meaning that it's not easy to just change to using only one of the
   * picklist. So, since the only option that the promotino setup provides is to include held or
   * not, this method just looks to see if the promo setup includes the 'held' option and removes it
   * from the returned list.
   *
   * @return List of AprovalStatusType objects.
   */
  public List getApprovalOptionTypes()
  {
    // Get full set of approval options
    List approvalOptionTypes = new ArrayList( ApprovalStatusType.getList( promotionType.getCode(), null ) );

    // Hack since approvals and promos use different pick lists for
    // approval options: only 'held' is variable, so if found then remove from approvalOptionTypes.
    boolean removeHeld = true;
    for ( Iterator iter = getPromotionApprovalOptions().iterator(); iter.hasNext(); )
    {
      PromotionApprovalOption promotionApprovalOption = (PromotionApprovalOption)iter.next();
      if ( promotionApprovalOption.getPromotionApprovalOptionType().getCode().equals( PromotionApprovalOptionType.HELD ) )
      {
        removeHeld = false;
        break;
      }
    }

    if ( removeHeld )
    {
      for ( Iterator iter = approvalOptionTypes.iterator(); iter.hasNext(); )
      {
        ApprovalStatusType approvalStatusType = (ApprovalStatusType)iter.next();
        if ( approvalStatusType.getCode().equals( ApprovalStatusType.HOLD ) )
        {
          iter.remove();
        }
      }
    }

    return approvalOptionTypes;
  }

  public List getApprovalOptionTypesSorted()
  {
    List approvalOptionTypes = getApprovalOptionTypes();
    Collections.sort( approvalOptionTypes, new PickListItemSortOrderComparator() );
    return approvalOptionTypes;
  }

  public boolean isProductClaimPromotion()
  {
    return promotionType.getCode().equals( PromotionType.PRODUCT_CLAIM );
  }

  public boolean isNominationPromotion()
  {
    return promotionType.getCode().equals( PromotionType.NOMINATION );
  }

  public boolean isRecognitionPromotion()
  {
    return promotionType.getCode().equals( PromotionType.RECOGNITION );
  }

  public boolean isAbstractRecognitionPromotion()
  {
    return BaseAssociationRequest.initializeAndUnproxy( this ) instanceof AbstractRecognitionPromotion;
  }

  public boolean isQuizPromotion()
  {
    return promotionType.getCode().equals( PromotionType.QUIZ );
  }

  public boolean isDIYQuizPromotion()
  {
    return promotionType.getCode().equals( PromotionType.DIY_QUIZ );
  }

  public boolean isSurveyPromotion()
  {
    return promotionType.getCode().equals( PromotionType.SURVEY );
  }

  public boolean isGoalQuestPromotion()
  {
    return promotionType.getCode().equals( PromotionType.GOALQUEST );
  }

  public boolean isChallengePointPromotion()
  {
    return promotionType.getCode().equals( PromotionType.CHALLENGE_POINT );
  }

  public boolean isGoalQuestOrChallengePointPromotion()
  {
    return promotionType.getCode().equals( PromotionType.GOALQUEST ) || promotionType.getCode().equals( PromotionType.CHALLENGE_POINT );
  }

  public boolean isWellnessPromotion()
  {
    return promotionType.getCode().equals( PromotionType.WELLNESS );
  }

  public boolean isThrowdownPromotion()
  {
    return promotionType.getCode().equals( PromotionType.THROWDOWN );
  }

  public boolean isBadgePromotion()
  {
    return promotionType.getCode().equals( PromotionType.BADGE );
  }

  public boolean isEngagementPromotion()
  {
    return promotionType.getCode().equals( PromotionType.ENGAGEMENT );
  }

  public boolean isSSIPromotion()
  {
    return promotionType.getCode().equals( PromotionType.SELF_SERV_INCENTIVES );
  }

  public abstract boolean hasParent();

  /**
   * @return Alias for <code>hasParent()</code>.
   */
  public boolean isChild()
  {
    return hasParent();
  }

  /**
   * @return True is promotion is a master promotion (i.e. not a child).
   */
  public boolean isMaster()
  {
    return !isChild();
  }

  /**
   * Get Live Parent and its Live Children promotions
   *
   * @return livePrimaryAndChildPromotions
   */
  public Set getLivePrimaryAndChildPromotions()
  {
    Set livePrimaryAndChildPromotions = new LinkedHashSet();
    // Bug Fix 18859.Approve the Recognitions(manual) Which were in expired status.
    if ( isRecognitionPromotion() && !isLive() )
    {
      livePrimaryAndChildPromotions.add( this );
    }
    else if ( isLive() )
    {
      livePrimaryAndChildPromotions.add( this );
    }

    return livePrimaryAndChildPromotions;
  }

  public PrimaryAudienceType getPrimaryAudienceType()
  {
    return primaryAudienceType;
  }

  public void setPrimaryAudienceType( PrimaryAudienceType primaryAudienceType )
  {
    this.primaryAudienceType = primaryAudienceType;
  }

  public SecondaryAudienceType getSecondaryAudienceType()
  {
    return secondaryAudienceType;
  }

  public void setSecondaryAudienceType( SecondaryAudienceType secondaryAudienceType )
  {
    this.secondaryAudienceType = secondaryAudienceType;
  }

  public WebRulesAudienceType getWebRulesAudienceType()
  {
    return webRulesAudienceType;
  }

  public void setWebRulesAudienceType( WebRulesAudienceType webRulesAudienceType )
  {
    this.webRulesAudienceType = webRulesAudienceType;
  }

  public PromotionAwardsType getAwardType()
  {
    return awardType;
  }

  public void setAwardType( PromotionAwardsType awardType )
  {
    this.awardType = awardType;
  }

  // to get properly translated content for a given code
  public String getAwardTypeNameFromCM()
  {
    if ( awardType == null || StringUtils.isEmpty( awardType.getCode() ) )
    {
      return "";
    }

    return PromotionAwardsType.lookup( awardType.getCode() ).getName();
  }

  /**
   * @return value of promotionPrimaryAudiences property
   */
  public Set getPromotionPrimaryAudiences()
  {
    return promotionPrimaryAudiences;
  }

  /**
   * @param promotionPrimaryAudiences value for promotionPrimaryAudiences property
   */
  public void setPromotionPrimaryAudiences( Set promotionPrimaryAudiences )
  {
    this.promotionPrimaryAudiences = promotionPrimaryAudiences;
  }

  /**
   * Returns the primary audiences associated with this promotion.
   *
   * @return the primary audiences assocated with this promotion, as a <code>Set</code> of
   *         {@link com.biperf.core.domain.participant.Audience} objects.
   */
  public Set getPrimaryAudiences()
  {
    Set primaryAudiences = new HashSet();

    for ( Iterator iter = promotionPrimaryAudiences.iterator(); iter.hasNext(); )
    {
      PromotionPrimaryAudience promotionPrimaryAudience = (PromotionPrimaryAudience)iter.next();
      primaryAudiences.add( promotionPrimaryAudience.getAudience() );
    }

    return primaryAudiences;
  }

  /**
   * Returns the partner audiences associated with this promotion.
   *
   * @return the partner audiences assocated with this promotion, as a <code>Set</code> of
   *         {@link com.biperf.core.domain.participant.Audience} objects.
   */
  public Set<Audience> getPartnerAudiences()
  {
    Set<Audience> partnerAudiences = new HashSet<Audience>();

    for ( Iterator iter = promotionPartnerAudiences.iterator(); iter.hasNext(); )
    {
      PromotionPartnerAudience promotionPartnerAudience = (PromotionPartnerAudience)iter.next();
      partnerAudiences.add( promotionPartnerAudience.getAudience() );
    }

    return partnerAudiences;
  }

  /**
   * Add a promotionPrimaryAudience to promotionPrimaryAudiences
   *
   * @param promotionPrimaryAudience
   */
  public void addPromotionPrimaryAudience( PromotionPrimaryAudience promotionPrimaryAudience )
  {
    promotionPrimaryAudience.setPromotion( this );
    this.promotionPrimaryAudiences.add( promotionPrimaryAudience );
  }

  /**
   * Add a promotionSecondaryAudience to promotionSecondaryAudiences
   *
   * @param promotionSecondaryAudience
   */
  public void addPromotionSecondaryAudience( PromotionSecondaryAudience promotionSecondaryAudience )
  {
    promotionSecondaryAudience.setPromotion( this );
    this.promotionSecondaryAudiences.add( promotionSecondaryAudience );
  }

  /**
   * Add a promotionPartnerAudience to promotionPartnerAudiences
   *
   * @param promotionPartnerAudience
   */
  public void addPromotionPartnerAudience( PromotionPartnerAudience promotionPartnerAudience )
  {
    promotionPartnerAudience.setPromotion( this );
    this.promotionPartnerAudiences.add( promotionPartnerAudience );
  }

  /**
   * Set promotionSecondaryAudiences to promotionSecondaryAudience
   *
   * @param promotionSecondaryAudience
   */
  public void setPromotionSecondaryAudiences( Set promotionSecondaryAudience )
  {
    this.promotionSecondaryAudiences = promotionSecondaryAudience;
  }

  /**
   * Set promotionSecondaryAudiences
   *
   * @return promotionSecondaryAudiences
   */
  public Set getPromotionSecondaryAudiences()
  {
    return this.promotionSecondaryAudiences;
  }

  /**
   * Returns the secondary audiences associated with this promotion.
   *
   * @return the secondary audiences assocated with this promotion, as a <code>Set</code> of
   *         {@link com.biperf.core.domain.participant.Audience} objects.
   */
  public Set getSecondaryAudiences()
  {
    Set secondaryAudiences = new HashSet();

    for ( Iterator iter = promotionSecondaryAudiences.iterator(); iter.hasNext(); )
    {
      PromotionSecondaryAudience promotionSecondaryAudience = (PromotionSecondaryAudience)iter.next();
      secondaryAudiences.add( promotionSecondaryAudience.getAudience() );
    }

    return secondaryAudiences;
  }

  /**
   * Add a promotionSweepstake to the set of sweepstakes.
   *
   * @param promotionSweepstake
   */
  public void addPromotionSweepstake( PromotionSweepstake promotionSweepstake )
  {
    promotionSweepstake.setPromotion( this );
    this.promotionSweepstakes.add( promotionSweepstake );
  }

  /**
   * @return the Audience objects from the Primary PromotionAudience objects
   */
  public Set getPromotionPrimaryAudiencesAsAudiences()
  {
    if ( getPromotionPrimaryAudiences() == null )
    {
      return null;
    }

    Set audiences = new LinkedHashSet( getPromotionPrimaryAudiences().size() );
    for ( Iterator iter = getPromotionPrimaryAudiences().iterator(); iter.hasNext(); )
    {
      PromotionAudience promotionAudience = (PromotionAudience)iter.next();
      audiences.add( promotionAudience.getAudience() );
    }

    return audiences;
  }

  /**
   * @return the Audience objects from the Secondary PromotionAudience objects
   */
  public Set getPromotionSecondaryAudiencesAsAudiences()
  {
    if ( getPromotionSecondaryAudiences() == null )
    {
      return null;
    }

    Set audiences = new LinkedHashSet( getPromotionSecondaryAudiences().size() );
    for ( Iterator iter = getPromotionSecondaryAudiences().iterator(); iter.hasNext(); )
    {
      PromotionAudience promotionAudience = (PromotionAudience)iter.next();
      audiences.add( promotionAudience.getAudience() );
    }

    return audiences;
  }

  public Set getPromotionSweepstakes()
  {
    return this.promotionSweepstakes;
  }

  public void setPromotionSweepstakes( Set promotionSweepstakes )
  {
    this.promotionSweepstakes = promotionSweepstakes;
  }

  /**
   * @return True if this promotion type uses claim forms (some don't - quiz, for example).
   */
  public abstract boolean isClaimFormUsed();

  /**
   * The promotion that needs specific implementaion needs to override in their respective domain
   * classes This function can be useful for sorting the promotion list by name. As we have issue of
   * keeping parent/child together with reverse sorting we might need to find a away to keep parent
   * child together. Overridden from
   *
   * @see com.biperf.core.domain.promotion.Promotion#getSortName()
   * @return String
   */

  public String getSortName()
  {
    return promotionName;
  }

  public boolean isDateValidForSubmission( Date date )
  {
    if ( date == null )
    {
      return false;
    }

    Date effectiveEndDate = getSubmissionEndDate();

    if ( effectiveEndDate == null )
    {
      effectiveEndDate = DateUtils.toEndDate( DateUtils.applyTimeZone( new Date(), UserManager.getTimeZoneID() ) );
    }
    else
    {
      // BUG 12840 -- make sure end date is at 11:59pm, Using DateUtils.toEndDate to set 11:59:59:59
      // on effective date
      effectiveEndDate = DateUtils.toEndDate( effectiveEndDate );
    }

    if ( date.before( submissionStartDate ) || date.after( effectiveEndDate ) )
    {
      return false;
    }

    return true;
  }

  public BudgetMaster getBudgetMaster()
  {
    return budgetMaster;
  }

  public void setBudgetMaster( BudgetMaster budgetMaster )
  {
    this.budgetMaster = budgetMaster;
  }

  public Badge getBadge()
  {
    return badge;
  }

  public void setBadge( Badge badge )
  {
    this.badge = badge;
  }

  public void addProxyModulePromotion( ProxyModulePromotion proxyModulePromotion )
  {
    proxyModulePromotion.setPromotion( this );
    this.proxyModulePromotions.add( proxyModulePromotion );
  }

  public void setProxyModulePromotions( Set proxyModulePromotions )
  {
    this.proxyModulePromotions = proxyModulePromotions;
  }

  public Set getProxyModulePromotions()
  {
    return this.proxyModulePromotions;
  }

  public boolean isSweepstakesActive()
  {
    return sweepstakesActive;
  }

  public void setSweepstakesActive( boolean sweepstakesActive )
  {
    this.sweepstakesActive = sweepstakesActive;
  }

  public SweepstakesClaimEligibilityType getSweepstakesClaimEligibilityType()
  {
    return sweepstakesClaimEligibilityType;
  }

  public void setSweepstakesClaimEligibilityType( SweepstakesClaimEligibilityType sweepstakesClaimEligibilityType )
  {
    this.sweepstakesClaimEligibilityType = sweepstakesClaimEligibilityType;
  }

  public SweepstakesMultipleAwardsType getSweepstakesMultipleAwardType()
  {
    return sweepstakesMultipleAwardType;
  }

  public void setSweepstakesMultipleAwardType( SweepstakesMultipleAwardsType sweepstakesMultipleAwardType )
  {
    this.sweepstakesMultipleAwardType = sweepstakesMultipleAwardType;
  }

  public Long getSweepstakesPrimaryAwardAmount()
  {
    return sweepstakesPrimaryAwardAmount;
  }

  public void setSweepstakesPrimaryAwardAmount( Long sweepstakesPrimaryAwardAmount )
  {
    this.sweepstakesPrimaryAwardAmount = sweepstakesPrimaryAwardAmount;
  }

  public SweepstakesWinnersType getSweepstakesPrimaryBasisType()
  {
    return sweepstakesPrimaryBasisType;
  }

  public void setSweepstakesPrimaryBasisType( SweepstakesWinnersType sweepstakesPrimaryBasisType )
  {
    this.sweepstakesPrimaryBasisType = sweepstakesPrimaryBasisType;
  }

  public Integer getSweepstakesPrimaryWinners()
  {
    return sweepstakesPrimaryWinners;
  }

  public void setSweepstakesPrimaryWinners( Integer sweepstakesPrimaryWinners )
  {
    this.sweepstakesPrimaryWinners = sweepstakesPrimaryWinners;
  }

  public Long getSweepstakesSecondaryAwardAmount()
  {
    return sweepstakesSecondaryAwardAmount;
  }

  public void setSweepstakesSecondaryAwardAmount( Long sweepstakesSecondaryAwardAmount )
  {
    this.sweepstakesSecondaryAwardAmount = sweepstakesSecondaryAwardAmount;
  }

  public SweepstakesWinnersType getSweepstakesSecondaryBasisType()
  {
    return sweepstakesSecondaryBasisType;
  }

  public void setSweepstakesSecondaryBasisType( SweepstakesWinnersType sweepstakesSecondaryBasisType )
  {
    this.sweepstakesSecondaryBasisType = sweepstakesSecondaryBasisType;
  }

  public Integer getSweepstakesSecondaryWinners()
  {
    return sweepstakesSecondaryWinners;
  }

  public void setSweepstakesSecondaryWinners( Integer sweepstakesSecondaryWinners )
  {
    this.sweepstakesSecondaryWinners = sweepstakesSecondaryWinners;
  }

  public SweepstakesWinnerEligibilityType getSweepstakesWinnerEligibilityType()
  {
    return sweepstakesWinnerEligibilityType;
  }

  public void setSweepstakesWinnerEligibilityType( SweepstakesWinnerEligibilityType sweepstakesWinnerEligibilityType )
  {
    this.sweepstakesWinnerEligibilityType = sweepstakesWinnerEligibilityType;
  }

  public Hierarchy getApprovalHierarchy()
  {
    return approvalHierarchy;
  }

  public void setApprovalHierarchy( Hierarchy approvalHierarchy )
  {
    this.approvalHierarchy = approvalHierarchy;
  }

  public NodeType getApprovalNodeType()
  {
    return approvalNodeType;
  }

  public void setApprovalNodeType( NodeType approvalNodeType )
  {
    this.approvalNodeType = approvalNodeType;
  }

  public int getChildrenCount()
  {
    return 0;
  }

  public boolean isFileLoadEntry()
  {
    return fileLoadEntry;
  }

  public void setFileLoadEntry( boolean fileLoadEntry )
  {
    this.fileLoadEntry = fileLoadEntry;
  }

  public boolean isOnlineEntry()
  {
    return onlineEntry;
  }

  public void setOnlineEntry( boolean onlineEntry )
  {
    this.onlineEntry = onlineEntry;
  }

  /**
   * Get has sweepstakes to process Note: Promotion needs to be fully hydrated with sweepstakes
   *
   * @return boolean returns true if there are sweepstakes to process
   */
  public boolean getHasSweepstakesToProcess()
  {
    if ( this.promotionSweepstakes != null )
    {
      Iterator iter = this.promotionSweepstakes.iterator();
      while ( iter.hasNext() )
      {
        PromotionSweepstake sweepstake = (PromotionSweepstake)iter.next();
        if ( !sweepstake.isProcessed() )
        {
          return true;
        }
      }
    }
    return false;
  }

  /**
   * Get has sweepstakes history (sweepstakes that have been processed) Note: Promotion needs to be
   * fully hydrated with sweepstakes
   *
   * @return boolean returns true if there is a sweepstakes history
   */
  public boolean getHasSweepstakesHistory()
  {
    if ( this.promotionSweepstakes != null )
    {
      Iterator iter = this.promotionSweepstakes.iterator();
      while ( iter.hasNext() )
      {
        PromotionSweepstake sweepstake = (PromotionSweepstake)iter.next();
        if ( sweepstake.isProcessed() )
        {
          return true;
        }
      }
    }
    return false;
  }

  public String getWebRulesText()
  {
    String rulesText = "";

    if ( this.cmAssetCode != null && this.webRulesCmKey != null )
    {
      rulesText = CmsResourceBundle.getCmsBundle().getString( this.cmAssetCode, this.webRulesCmKey );
    }

    return rulesText;
  }

  /**
   * Returns true if this promotion has a budget; returns false otherwise.
   *
   * @return true if this promotion has a budget; returns false otherwise.
   */
  public boolean isBudgetUsed()
  {
    return getBudgetMaster() != null;
  }

  public boolean isCashBudgetUsed()
  {
    return getCashBudgetMaster() != null;
  }

  public String getPurpose()
  {
    return purpose;
  }

  public void setPurpose( String purpose )
  {
    this.purpose = purpose;
  }

  public String getUpperCaseName()
  {
    return upperCaseName;
  }

  public void setUpperCaseName( String upperCaseName )
  {
    this.upperCaseName = upperCaseName;
  }

  public Calculator getCalculator()
  {
    return calculator;
  }

  public void setCalculator( Calculator calculator )
  {
    this.calculator = calculator;
  }

  public ScoreBy getScoreBy()
  {
    return scoreBy;
  }

  public void setScoreBy( ScoreBy scoreBy )
  {
    this.scoreBy = scoreBy;
  }

  public boolean isAllowSelfEnroll()
  {
    return allowSelfEnroll;
  }

  public void setAllowSelfEnroll( boolean allowSelfEnroll )
  {
    this.allowSelfEnroll = allowSelfEnroll;
  }

  public String getEnrollProgramCode()
  {
    return enrollProgramCode;
  }

  public void setEnrollProgramCode( String enrollProgramCode )
  {
    this.enrollProgramCode = enrollProgramCode;
  }

  public Long getSweepstakesSecondaryAwardLevel()
  {
    return sweepstakesSecondaryAwardLevel;
  }

  public void setSweepstakesSecondaryAwardLevel( Long sweepstakesSecondaryAwardLevel )
  {
    this.sweepstakesSecondaryAwardLevel = sweepstakesSecondaryAwardLevel;
  }

  public Long getSweepstakesPrimaryAwardLevel()
  {
    return sweepstakesPrimaryAwardLevel;
  }

  public void setSweepstakesPrimaryAwardLevel( Long sweepstakesPrimaryAwardLevel )
  {
    this.sweepstakesPrimaryAwardLevel = sweepstakesPrimaryAwardLevel;
  }

  public PartnerAudienceType getPartnerAudienceType()
  {
    return partnerAudienceType;
  }

  public boolean isPartnersEnabled()
  {
    return null != partnerAudienceType;
  }

  public void setPartnerAudienceType( PartnerAudienceType partnerAudienceType )
  {
    this.partnerAudienceType = partnerAudienceType;
  }

  public Set getPromotionPartnerAudiences()
  {
    return promotionPartnerAudiences;
  }

  public void setPromotionPartnerAudiences( Set promotionPartnerAudiences )
  {
    this.promotionPartnerAudiences = promotionPartnerAudiences;
  }

  /**
   * @return boolean to indicate if promotion name needs to be wrapped in next line in home page
   */
  public boolean isPromotionNameSplit()
  {
    return promotionName != null && promotionName.length() > 22;
  }

  public String getWellnessUrl()
  {
    return wellnessUrl;
  }

  public void setWellnessUrl( String wellnessUrl )
  {
    this.wellnessUrl = wellnessUrl;
  }

  /**
   * @return String[]
   */
  public String[] getSplitPromotionNames()
  {
    if ( promotionName != null && promotionName.length() > 0 )
    {
      int size = promotionName.length() / 22 + 1;
      String[] promotionNames = new String[size];
      int subStringIndex1 = 0;
      int subStringIndex2 = 22;
      for ( int i = 0; i <= size - 1; i++ )
      {
        if ( i == size - 1 )
        {
          promotionNames[i] = promotionName.substring( subStringIndex1 );
        }
        else
        {
          promotionNames[i] = promotionName.substring( subStringIndex1, subStringIndex2 );
        }
        subStringIndex1 = subStringIndex2;
        subStringIndex2 = subStringIndex2 + 22;
      }
      return promotionNames;
    }
    return null;
  }

  public String getPromoNameAssetCode()
  {
    return promoNameAssetCode;
  }

  public void setPromoNameAssetCode( String promoNameAssetCode )
  {
    this.promoNameAssetCode = promoNameAssetCode;
  }

  public PublicRecognitionAudienceType getPublicRecognitionAudienceType()
  {
    return publicRecognitionAudienceType;
  }

  public void setPublicRecognitionAudienceType( PublicRecognitionAudienceType publicRecognitionAudienceType )
  {
    this.publicRecognitionAudienceType = publicRecognitionAudienceType;
  }

  public boolean isBillCodesActive()
  {
    return billCodesActive;
  }

  public void setBillCodesActive( boolean billCodesActive )
  {
    this.billCodesActive = billCodesActive;
  }

  public boolean isSwpBillCodesActive()
  {
    return swpBillCodesActive;
  }

  public void setSwpBillCodesActive( boolean swpBillCodesActive )
  {
    this.swpBillCodesActive = swpBillCodesActive;
  }

  public Date getTileDisplayStartDate()
  {
    return tileDisplayStartDate;
  }

  public void setTileDisplayStartDate( Date tileDisplayStartDate )
  {
    this.tileDisplayStartDate = tileDisplayStartDate;
  }

  public Date getTileDisplayEndDate()
  {
    return tileDisplayEndDate;
  }

  public void setTileDisplayEndDate( Date tileDisplayEndDate )
  {
    this.tileDisplayEndDate = tileDisplayEndDate;
  }

  public String getOverview()
  {
    return overview;
  }

  public void setOverview( String overview )
  {
    this.overview = overview;
  }

  public String getOverviewDetailsText()
  {
    String overviewDetailsText = null;
    if ( this.overview != null )
    {
      overviewDetailsText = CmsResourceBundle.getCmsBundle().getString( this.overview, Promotion.PROMO_OVERVIEW_CM_ASSET_TYPE_KEY );
    }
    return overviewDetailsText;
  }

  public List<PromotionBillCode> getPromotionBillCodes()
  {
    return promotionBillCodes;
  }

  public void setPromotionBillCodes( List<PromotionBillCode> promotionBillCodes )
  {
    this.promotionBillCodes = promotionBillCodes;
  }

  public void addPromotionBillCodes( PromotionBillCode billCode )
  {
    billCode.setPromotion( this );
    this.promotionBillCodes.add( billCode );
  }

  public List<SweepstakesBillCode> getSweepstakesBillCodes()
  {
    return sweepstakesBillCodes;
  }

  public void setSweepstakesBillCodes( List<SweepstakesBillCode> sweepstakesBillCodes )
  {
    this.sweepstakesBillCodes = sweepstakesBillCodes;
  }

  public void addSweepstakesBillCodes( SweepstakesBillCode sweepstakesBillCode )
  {
    sweepstakesBillCode.setPromotion( this );
    this.sweepstakesBillCodes.add( sweepstakesBillCode );
  }

  public Set<PromotionWizard> getPromotionWizardOrder()
  {
    return promotionWizardOrder;
  }

  public void setPromotionWizardOrder( Set<PromotionWizard> promotionWizardOrder )
  {
    this.promotionWizardOrder = promotionWizardOrder;
  }

  public void addPromotionWizardOrder( PromotionWizard promotionWizard )
  {
    promotionWizard.setPromotion( this );
    this.promotionWizardOrder.add( promotionWizard );
  }

  public BudgetMaster getCashBudgetMaster()
  {
    return cashBudgetMaster;
  }

  public void setCashBudgetMaster( BudgetMaster cashBudgetMaster )
  {
    this.cashBudgetMaster = cashBudgetMaster;
  }

}
