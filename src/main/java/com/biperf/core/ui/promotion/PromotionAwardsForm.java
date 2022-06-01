/**
 * 
 */

package com.biperf.core.ui.promotion;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections4.Factory;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.hibernate.LazyInitializationException;

import com.biperf.core.dao.calculator.CalculatorQueryConstraint;
import com.biperf.core.domain.budget.BudgetMaster;
import com.biperf.core.domain.budget.BudgetSegment;
import com.biperf.core.domain.calculator.Calculator;
import com.biperf.core.domain.enums.ApprovalType;
import com.biperf.core.domain.enums.BudgetMasterAwardType;
import com.biperf.core.domain.enums.BudgetOverrideableType;
import com.biperf.core.domain.enums.BudgetType;
import com.biperf.core.domain.enums.CalculatorAwardType;
import com.biperf.core.domain.enums.CalculatorStatusType;
import com.biperf.core.domain.enums.CustomApproverType;
import com.biperf.core.domain.enums.NominationAwardSpecifierType;
import com.biperf.core.domain.enums.NominationEvaluationType;
import com.biperf.core.domain.enums.PromotionAwardsType;
import com.biperf.core.domain.enums.PromotionIssuanceType;
import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.domain.enums.ScoreBy;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.AbstractRecognitionPromotion;
import com.biperf.core.domain.promotion.ApproverOption;
import com.biperf.core.domain.promotion.HomePageItem;
import com.biperf.core.domain.promotion.NominationPromotion;
import com.biperf.core.domain.promotion.NominationPromotionLevel;
import com.biperf.core.domain.promotion.NominationPromotionTimePeriod;
import com.biperf.core.domain.promotion.PromoMerchCountry;
import com.biperf.core.domain.promotion.PromoMerchProgramLevel;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.QuizPromotion;
import com.biperf.core.domain.promotion.RecognitionPromotion;
import com.biperf.core.domain.promotion.WellnessPromotion;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.awardbanq.impl.MerchLevelProductValueObject;
import com.biperf.core.service.awardbanq.impl.ProductEntryVO;
import com.biperf.core.service.awardbanq.impl.ProductGroupDescriptionsVO;
import com.biperf.core.service.budget.BudgetMasterService;
import com.biperf.core.service.budget.BudgetMasterToBudgetSegmentsAssociationRequest;
import com.biperf.core.service.calculator.CalculatorAssociationRequest;
import com.biperf.core.service.calculator.CalculatorService;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.promotion.PromotionAssociationRequest;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.ui.BaseActionForm;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.StringUtil;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.BudgetSegmentValueBean;
import com.biperf.core.value.NominationTimePeriodValueBean;
import com.biperf.core.value.NominationTimePeriodValueBeanComparator;
import com.objectpartners.cms.util.CmsResourceBundle;

/**
 * PromotionAwardsForm.
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
 * <td>asondgeroth</td>
 * <td>Oct 6, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class PromotionAwardsForm extends BaseActionForm
{
  public static final String BUDGET_NONE = "none";

  public static final String BUDGET_EXISTING = "existing";

  public static final String BUDGET_SWEEP_EXISTING = "sweepexisting";

  public static final String BUDGET_NEW = "new";

  public static final String FINAL_LEVEL = "finalLevel";

  public static final String EACH_LEVEL = "eachLevel";

  private String method;

  private String returnActionUrl;

  // Promotion display information
  private Long promotionId;

  private String promotionName;

  private String promotionTypeCode;

  private String promotionTypeName;

  private String promotionStatus;

  private boolean expired;

  private boolean live;

  private boolean nominationCumulative;

  private String promotionIssuanceTypeCode; // this is to dispaly budget or

  // not - refer bug 10865

  // Persistence information
  private boolean awardsActive;

  private String awardsType = "points";

  private String awardsTypeDesc;

  private String awardAmountTypeFixed;

  private Long calculatorId;

  private String scoreBy;

  private boolean useRecognitionCalculator;

  private String fixedAmount;

  private String rangeAmountMin = null;

  private String rangeAmountMax = null;

  private String budgetOption = BUDGET_NONE;

  private Long budgetMasterId;

  private String budgetMasterName;

  private String existingBudgetType;
  private String budgetType = BudgetType.PAX_BUDGET_TYPE;

  private String budgetCapType = BudgetOverrideableType.HARD_OVERRIDE;

  private Long hiddenBudgetMasterId; // Used if budget section is disabled

  private boolean fileloadBudgetAmount;

  private Long budgetApproverId;

  private String budgetApproverName = "<none selected>";

  private String finalPayoutRule;

  private String approvalType;

  private String awardStructure;

  private boolean noNotification;

  // private boolean giftWrap;

  private boolean apqConversion;

  private boolean featuredAwardsEnabled;

  // List of formbean of all country/levels
  private List countryList = null;

  private List stdHomePageItemList;
  private HomePageItem homePageItem;
  private String ccProductId;
  private String stdProductId;

  // if all the involved countries have equal number of levels, this flag is
  // set
  // to true
  private boolean numOfLevelsEqual = false;
  // Number of levels will be populated only if the number of levels is equal for all involved
  // countries
  private Long numberOfLevels = null;

  private static String FIXED = "true";
  private static String RANGE = "false";
  private static String CALCULATOR = "cal";

  private boolean budgetSweepEnabled;
  private String promotionStartDate;
  private String promotionEndDate;
  private boolean showInBudgetTracker;
  private boolean includePurl;

  // Budget Segment fields
  private String budgetMasterStartDate = DateUtils.displayDateFormatMask;
  private String budgetMasterEndDate = DateUtils.displayDateFormatMask;
  private List<BudgetSegmentValueBean> budgetSegmentVBList = new ArrayList<BudgetSegmentValueBean>();
  private Long budgetSegmentId;

  // Nomination 'request more budget' section
  private boolean requestMoreBudget;
  private Long nomBudgetApproverId; // Current approver
  private String budgetApproverSearchLastName; // Last-name search field
  private Long selectedBudgetApproverUserId; // Selection from the search results

  private boolean payoutEachLevel;
  private boolean payoutFinalLevel;

  private List<NominationPromotionLevelBean> nominationPayoutEachLevelList = new ArrayList<NominationPromotionLevelBean>();
  private List<NominationPayoutFinalLevelBean> nominationPayoutFinalLevelList = new ArrayList<NominationPayoutFinalLevelBean>();
  private boolean taxable = true;

  private boolean nominatorRecommendedAward;

  private String limitNomineeWinner;
  private Long limitPerPromotion;

  private boolean timePeriodActive;
  private List<NominationTimePeriodValueBean> nominationTimePeriodVBList = new ArrayList<NominationTimePeriodValueBean>();

  private String startDate;
  private String endDate;

  private String cashBudgetOption = BUDGET_NONE;
  private Long cashBudgetMasterId;
  private String cashBudgetMasterName;
  private String cashBudgetType = BudgetType.PAX_BUDGET_TYPE;
  private String cashBudgetCapType = BudgetOverrideableType.HARD_OVERRIDE;
  private Long hiddenCashBudgetMasterId;
  private String cashFinalPayoutRule;

  private String cashBudgetMasterStartDate = DateUtils.displayDateFormatMask;
  private String cashBudgetMasterEndDate = DateUtils.displayDateFormatMask;
  private List<BudgetSegmentValueBean> cashBudgetSegmentVBList = new ArrayList<BudgetSegmentValueBean>();

  private String existingCashBudgetType;
  private Long cashBudgetSegmentId;

  // Transient flag variable fields for enabling / disabling controls
  private boolean oneLevelApproval;
  private boolean cumulativeApproval;
  private boolean firstLevelAward = false;
  private boolean awardSelected = false;
  private boolean approverTypeCustom = false;
  private boolean finalLevelAward = false;
  private Integer numApprovalLevels = 0;
  private boolean hideTpSubmissionLimitColumns = false;
  private boolean otherLevelsAward = false;
  private int customApproverAwardLevel;
  private boolean behaviorActive;

  private Long awardLevelIndex;
  
  /*Client customization start*/
  private boolean utilizeParentBudgets;
  private String nominationAwardSpecifierTypeCode;
  /*Client customization end*/

  public void reset( ActionMapping mapping, HttpServletRequest request )
  {
    super.reset( mapping, request );

    // Blanks out the respective fields if not set
    fixedAmount = null;
    rangeAmountMin = null;
    rangeAmountMax = null;
    calculatorId = null;
    scoreBy = null;
    approvalType = null;
    awardStructure = null;
    countryList = null;
    includePurl = false;

    budgetSegmentVBList = getEmptyValueList( RequestUtils.getOptionalParamInt( request, "budgetSegmentVBListSize" ) );
    cashBudgetSegmentVBList = getEmptyValueList( RequestUtils.getOptionalParamInt( request, "cashBudgetSegmentVBListSize" ) );
    nominationTimePeriodVBList = getEmptyNominationTimePeriodValueList( RequestUtils.getOptionalParamInt( request, "nominationTimePeriodVBListSize" ) );

    nominationPayoutEachLevelList = getEmptyNominationPayoutEachLevelList( RequestUtils
        .getOptionalParamInt( request,
                              "nominationPayoutEachLevelListSize" ) ); /*
                                                                        * RequestUtils.
                                                                        * getOptionalParamInt(
                                                                        * request, "numberOfLevels"
                                                                        * )
                                                                        */
    nominationPayoutFinalLevelList = getEmptyNominationPayoutFinalLevelList( RequestUtils.getOptionalParamInt( request, "nominationPayoutFinalLevelListSize" ) );

    int countryListCount = RequestUtils.getOptionalParamInt( request, "countryListCount" );
    countryList = getEmptyPromoMerchCountryBeanList( countryListCount );
    getStdHomePageItemAsList().clear();
  }

  /**
   * resets the value list with empty PromotionApprovalParticipantBeans
   * 
   * @param listCount
   * @return List
   */
  private List getEmptyPromoMerchCountryBeanList( int listCount )
  {
    List valueList = new ArrayList();

    for ( int i = 0; i < listCount; i++ )
    {
      PromoMerchProgramLevelFormBean promoMerchProgramLevelFormBean = new PromoMerchProgramLevelFormBean();
      valueList.add( promoMerchProgramLevelFormBean );
    }

    return valueList;
  }

  /**
   * Accessor for the number of PromoMerchCountry objects in the list.
   * 
   * @return int
   */
  public int getCountryListCount()
  {
    if ( countryList == null )
    {
      return 0;
    }

    return countryList.size();
  }

  /**
   * Load the form with the domain object value;
   * 
   * @param promotion
   */
  public void load( Promotion promotion )
  {
    // Promotion display information

    this.promotionId = promotion.getId();
    this.promotionName = promotion.getName();
    this.promotionTypeCode = promotion.getPromotionType().getCode();
    this.promotionTypeName = promotion.getPromotionType().getName();
    this.promotionStatus = promotion.getPromotionStatus().getCode();
    if ( promotion.isNominationPromotion() )
    {
      NominationPromotion nomPromotion = (NominationPromotion)promotion;

      this.nominationCumulative = nomPromotion.getEvaluationType().getCode().equals( NominationEvaluationType.CUMULATIVE );
      /*
       * NominationAwardSpecifierType awardSpecifierType = nomPromotion.getAwardSpecifierType();
       * nominationAwardSpecifierTypeCode = awardSpecifierType != null ?
       * awardSpecifierType.getCode() : null;
       */

      // "Request more budget" fields
      this.requestMoreBudget = nomPromotion.isRequestMoreBudget();
      if ( nomPromotion.getBudgetApprover() != null )
      {
        this.nomBudgetApproverId = nomPromotion.getBudgetApprover().getId();
      }

      if ( !nomPromotion.getCustomApproverOptions().isEmpty() )
      {
        this.setApproverTypeCustom( true );

        for ( Iterator<ApproverOption> iter = nomPromotion.getCustomApproverOptions().iterator(); iter.hasNext(); )
        {
          ApproverOption option = iter.next();
          if ( option.getApproverType().getCode().equals( CustomApproverType.AWARD ) )
          {
            this.setAwardSelected( true );

            this.setAwardLevelIndex( option.getApprovalLevel() );
            if ( option.getApprovalLevel().equals( new Long( 1 ) ) )
            {
              this.setFirstLevelAward( true );
              break;
            }
          }
        }

        if ( !nomPromotion.getCustomApproverOptions().isEmpty() )
        {
          int index = 0;
          Iterator<ApproverOption> iterator = nomPromotion.getCustomApproverOptions().iterator();

          while ( iterator.hasNext() )
          {
            index++;
            ApproverOption approverOption = iterator.next();
            if ( approverOption.getApproverType() != null )
            {
              if ( approverOption.getApproverType().getCode().equals( CustomApproverType.AWARD ) )
              {
                this.setOtherLevelsAward( true );
                this.setCustomApproverAwardLevel( approverOption.getApprovalLevel().intValue() );
                Integer lastIndex = new Integer( index );
                if ( lastIndex.equals( nomPromotion.getApprovalNodeLevels() ) )
                {
                  this.setFinalLevelAward( true );
                }
              }
            }
          }

        }
      }

      // hide is payout each level and is payout final level if approval level is only one
      if ( nomPromotion.getApprovalNodeLevels() != null && nomPromotion.getApprovalNodeLevels().intValue() == 1 )
      {
        this.setOneLevelApproval( true );
        this.setPayoutFinalLevel( true );
      }

      // Recommended award set to no when cumulative approval
      if ( nomPromotion.isCumulative() )
      {
        this.setCumulativeApproval( true );
      }

      this.setNumApprovalLevels( nomPromotion.getApprovalNodeLevels() );

      // This flag will make it so columns of the time period table are hidden
      if ( nomPromotion.isTeam() )
      {
        setHideTpSubmissionLimitColumns( true );
      }
    }

    this.live = promotion.isLive();
    this.expired = promotion.isExpired();

    if ( promotion.isAbstractRecognitionPromotion() )

    {
      loadAbstractRecognitionPromotion( (AbstractRecognitionPromotion)promotion );
      if ( promotion.getApprovalType() != null )
      {
        this.approvalType = promotion.getApprovalType().getCode();
      }
    }
    else if ( promotion.isQuizPromotion() )
    {
      loadQuizPromotion( (QuizPromotion)promotion );
    }
    else if ( promotion.isWellnessPromotion() )
    {
      loadWellnessPromotion( (WellnessPromotion)promotion );
    }
  }

  private void loadAbstractRecognitionPromotion( AbstractRecognitionPromotion promotion )
  {

    this.awardsActive = promotion.isAwardActive();

    if ( promotion.getAwardType() != null )
    {
      this.awardsType = promotion.getAwardType().getCode();
      this.awardsTypeDesc = promotion.getAwardType().getName();
    }
    this.awardStructure = promotion.getAwardStructure();
    this.apqConversion = promotion.isApqConversion();
    this.noNotification = promotion.isNoNotification();
    this.featuredAwardsEnabled = promotion.isFeaturedAwardsEnabled();
    if ( promotion.isFileLoadEntry() )
    {
      this.promotionIssuanceTypeCode = PromotionIssuanceType.FILE_LOAD;
    }
    if ( promotion.isOnlineEntry() )
    {
      this.promotionIssuanceTypeCode = PromotionIssuanceType.ONLINE;
    }
    if ( promotion instanceof RecognitionPromotion )
    {
      RecognitionPromotion recogPromo = (RecognitionPromotion)promotion;
      Set items = recogPromo.getHomePageItems();
      if ( null != items && !items.isEmpty() )
      {
        this.stdProductId = ( (HomePageItem)items.iterator().next() ).getProductId();
      }

      this.budgetSweepEnabled = recogPromo.isBudgetSweepEnabled();
      this.promotionStartDate = DateUtils.toDisplayString( recogPromo.getSubmissionStartDate() );
      if ( null != recogPromo.getSubmissionEndDate() )
      {
        this.promotionEndDate = DateUtils.toDisplayString( recogPromo.getSubmissionEndDate() );
      }
      this.showInBudgetTracker = recogPromo.isShowInBudgetTracker();
      this.includePurl = recogPromo.isIncludePurl();
      /* client customization start */
      this.utilizeParentBudgets = promotion.isUtilizeParentBudgets();
      /* client customization end */
    }
    else if ( promotion.isNominationPromotion() )
    {
      NominationPromotion nominationPromotion = (NominationPromotion)promotion;
      this.promotionStartDate = DateUtils.toDisplayString( nominationPromotion.getSubmissionStartDate() );
      this.promotionEndDate = DateUtils.toDisplayString( nominationPromotion.getSubmissionEndDate() );
      this.taxable = promotion.isTaxable();

      if ( nominationPromotion.getPayoutLevel() != null && nominationPromotion.getPayoutLevel().equals( EACH_LEVEL ) )
      {
        this.payoutEachLevel = true;
        this.payoutFinalLevel = false;
      }
      else if ( nominationPromotion.getPayoutLevel() != null && nominationPromotion.getPayoutLevel().equals( FINAL_LEVEL ) )
      {
        this.payoutEachLevel = false;
        this.payoutFinalLevel = true;
      }
      else
      {
        this.payoutEachLevel = false;
        this.payoutFinalLevel = false;
      }

      this.nominatorRecommendedAward = nominationPromotion.isNominatorRecommendedAward();

      if ( !nominationPromotion.getCustomApproverOptions().isEmpty() )
      {
        for ( Iterator<ApproverOption> iter = nominationPromotion.getCustomApproverOptions().iterator(); iter.hasNext(); )
        {
          ApproverOption option = iter.next();
          if ( option.getApproverType().getCode().equals( CustomApproverType.AWARD ) )
          {
            this.setAwardsActive( true );
          }
        }
      }

      if ( nominationPromotion.getPayoutLevel() != null && nominationPromotion.getNominationLevels() != null && nominationPromotion.getNominationLevels().size() > 0 )
      {
        List<NominationPromotionLevel> nomLevels = new ArrayList<NominationPromotionLevel>( nominationPromotion.getNominationLevels() );
        int levelIndex = 1;
        for ( NominationPromotionLevel nominationPromotionLevel : nomLevels )
        {
          NominationPromotionLevelBean nominationPromotionLevelBean = new NominationPromotionLevelBean();
          NominationPayoutFinalLevelBean finalLevelBean = new NominationPayoutFinalLevelBean();

          if ( nominationPromotion.getPayoutLevel() != null && nominationPromotion.getPayoutLevel().equals( EACH_LEVEL ) )
          {
            nominationPromotionLevelBean.setLevelId( nominationPromotionLevel.getId() );
            nominationPromotionLevelBean.setLevelLabel( nominationPromotionLevel.getLevelLabel() );
            nominationPromotionLevelBean.setAwardsType( nominationPromotionLevel.getAwardPayoutType().getCode() );
            nominationPromotionLevelBean.setAwardsTypeList( buildAwardsType( levelIndex ) );
            nominationPromotionLevelBean.setCalculatorList( buildCalculators( awardLevelIndex != null && awardLevelIndex.equals( new Long( levelIndex ) ) ) );
            if ( nominationPromotionLevel.getAwardPayoutType().equals( PromotionAwardsType.lookup( PromotionAwardsType.POINTS ) )
                || nominationPromotionLevel.getAwardPayoutType().equals( PromotionAwardsType.lookup( PromotionAwardsType.CASH ) ) )
            {
              if ( nominationPromotionLevel.getCalculator() != null )
              {
                nominationPromotionLevelBean.setEachLevelCalculatorId( nominationPromotionLevel.getCalculator().getId() );
                nominationPromotionLevelBean.setAwardAmountTypeFixed( CALCULATOR );
              }
              else if ( nominationPromotionLevel.isNominationAwardAmountTypeFixed() )
              {
                nominationPromotionLevelBean.setAwardAmountTypeFixed( FIXED );
                if ( nominationPromotionLevel.getNominationAwardAmountFixed() != null )
                {
                  nominationPromotionLevelBean.setFixedAmount( "" + nominationPromotionLevel.getNominationAwardAmountFixed() );
                }
              }
              else
              {
                // range
                nominationPromotionLevelBean.setAwardAmountTypeFixed( RANGE );
                if ( nominationPromotionLevel.getNominationAwardAmountMin() != null && nominationPromotionLevel.getNominationAwardAmountMax() != null )
                {
                  nominationPromotionLevelBean.setRangeAmountMin( "" + nominationPromotionLevel.getNominationAwardAmountMin() );
                  nominationPromotionLevelBean.setRangeAmountMax( "" + nominationPromotionLevel.getNominationAwardAmountMax() );
                }
              }
            }
            else if ( nominationPromotionLevel.getAwardPayoutType().equals( PromotionAwardsType.lookup( PromotionAwardsType.OTHER ) ) )
            {
              nominationPromotionLevelBean.setPayoutDescription( nominationPromotionLevel.getPayoutDescriptionFromCM() );
              if ( nominationPromotionLevel.getPayoutValue() != null )
              {
                BigDecimal bd = nominationPromotionLevel.getPayoutValue().setScale( 2, BigDecimal.ROUND_DOWN );

                DecimalFormat df = new DecimalFormat();

                df.setMaximumFractionDigits( 2 );

                df.setMinimumFractionDigits( 0 );

                df.setGroupingUsed( false );

                String eachPayoutValueresult = df.format( bd );
                nominationPromotionLevelBean.setPayoutValue( eachPayoutValueresult );
              }
              nominationPromotionLevelBean.setPayoutCurrency( nominationPromotionLevel.getPayoutCurrency() );
              if ( nominationPromotionLevel.getQuantity() != null )
              {
                nominationPromotionLevelBean.setQuantity( nominationPromotionLevel.getQuantity().toString() );
              }
              else
              {
                nominationPromotionLevelBean.setQuantity( null );
              }

            }
            this.nominationPayoutEachLevelList.add( nominationPromotionLevelBean );
          }
          // Else is final level
          else if ( nominationPromotion.getPayoutLevel() != null && nominationPromotion.getPayoutLevel().equals( FINAL_LEVEL ) )
          {
            this.awardsType = nominationPromotionLevel.getAwardPayoutType().getCode();
            finalLevelBean.setLevelId( nominationPromotionLevel.getId() );
            finalLevelBean.setLevelLabel( nominationPromotionLevel.getLevelLabel() );
            finalLevelBean.setAwardsType( nominationPromotionLevel.getAwardPayoutType().getCode() );
            finalLevelBean.setAwardsTypeList( buildAwardsType( levelIndex ) );
            finalLevelBean.setCalculatorList( buildCalculators( awardLevelIndex != null && awardLevelIndex.equals( new Long( levelIndex ) ) ) );
            if ( nominationPromotionLevel.getAwardPayoutType().equals( PromotionAwardsType.lookup( PromotionAwardsType.POINTS ) )
                || nominationPromotionLevel.getAwardPayoutType().equals( PromotionAwardsType.lookup( PromotionAwardsType.CASH ) ) )
            {
              if ( nominationPromotionLevel.getCalculator() != null )
              {
                finalLevelBean.setFinalLevelCalculatorId( nominationPromotionLevel.getCalculator().getId() );
                finalLevelBean.setAwardAmountTypeFixed( CALCULATOR );
              }
              else if ( nominationPromotionLevel.isNominationAwardAmountTypeFixed() )
              {
                finalLevelBean.setAwardAmountTypeFixed( FIXED );
                if ( nominationPromotionLevel.getNominationAwardAmountFixed() != null )
                {
                  finalLevelBean.setFixedAmount( "" + nominationPromotionLevel.getNominationAwardAmountFixed() );
                }
              }
              else
              {
                // range
                finalLevelBean.setAwardAmountTypeFixed( RANGE );
                if ( nominationPromotionLevel.getNominationAwardAmountMin() != null && nominationPromotionLevel.getNominationAwardAmountMax() != null )
                {
                  finalLevelBean.setRangeAmountMin( "" + nominationPromotionLevel.getNominationAwardAmountMin() );
                  finalLevelBean.setRangeAmountMax( "" + nominationPromotionLevel.getNominationAwardAmountMax() );
                }
              }
            }
            else if ( nominationPromotionLevel.getAwardPayoutType().equals( PromotionAwardsType.lookup( PromotionAwardsType.OTHER ) ) )
            {
              finalLevelBean.setPayoutDescription( nominationPromotionLevel.getPayoutDescription() );
              if ( nominationPromotionLevel.getPayoutValue() != null )
              {
                BigDecimal bd = nominationPromotionLevel.getPayoutValue().setScale( 2, BigDecimal.ROUND_DOWN );

                DecimalFormat df = new DecimalFormat();

                df.setMaximumFractionDigits( 2 );

                df.setMinimumFractionDigits( 0 );

                df.setGroupingUsed( false );

                String finalPayoutValueResult = df.format( bd );
                finalLevelBean.setPayoutValue( finalPayoutValueResult );
              }
              finalLevelBean.setPayoutCurrency( nominationPromotionLevel.getPayoutCurrency() );
              // Quantity can be null.
              finalLevelBean.setQuantity( nominationPromotionLevel.getQuantity() != null ? nominationPromotionLevel.getQuantity().toString() : null );
            }
            this.nominationPayoutFinalLevelList.add( finalLevelBean );
          }
          levelIndex++;
        }
        if ( nominationPromotion.getPayoutLevel() != null && nominationPromotion.getPayoutLevel().equals( EACH_LEVEL ) )
        {
          this.nominationPayoutFinalLevelList = getEmptyNominationPayoutFinalLevelList( 1 );
        }
        else if ( nominationPromotion.getPayoutLevel() != null && nominationPromotion.getPayoutLevel().equals( FINAL_LEVEL ) )
        {
          this.nominationPayoutEachLevelList = getEmptyNominationPayoutEachLevelList( nominationPromotion.getApprovalNodeLevels() );
        }
      }
      else
      {
        int numApprovalNodeLevels = nominationPromotion.getApprovalNodeLevels() == null ? 0 : nominationPromotion.getApprovalNodeLevels();

        this.nominationPayoutEachLevelList = getEmptyNominationPayoutEachLevelList( numApprovalNodeLevels );
        this.nominationPayoutFinalLevelList = getEmptyNominationPayoutFinalLevelList( 1 );
      }

      this.timePeriodActive = nominationPromotion.isTimePeriodActive();

      try
      {
        if ( nominationPromotion.getNominationTimePeriods() != null && nominationPromotion.getNominationTimePeriods().size() > 0 )
        {
          for ( NominationPromotionTimePeriod nominationTimePeriod : nominationPromotion.getNominationTimePeriods() )
          {
            if ( nominationTimePeriod != null )
            {
              NominationTimePeriodValueBean timePeriodVB = new NominationTimePeriodValueBean();

              timePeriodVB.setTimePeriodId( nominationTimePeriod.getId() );
              timePeriodVB.setTimePeriodName( nominationTimePeriod.getTimePeriodName() );
              timePeriodVB.setTimePeriodStartDate( DateUtils.toDisplayString( nominationTimePeriod.getTimePeriodStartDate() ) );
              timePeriodVB.setTimePeriodStartDateEditable( false );
              timePeriodVB.setTimePeriodEndDate( DateUtils.toDisplayString( nominationTimePeriod.getTimePeriodEndDate() ) );
              if ( nominationTimePeriod.getTimePeriodEndDate() != null )
              {
                timePeriodVB.setTimePeriodEndDateEditable( isTimePeriodStartDateEditable( DateUtils.toDisplayString( nominationTimePeriod.getTimePeriodEndDate() ) ) );
              }
              if ( nominationTimePeriod.getMaxSubmissionAllowed() != null )
              {
                timePeriodVB.setMaxSubmissionAllowed( nominationTimePeriod.getMaxSubmissionAllowed().toString() );
              }
              if ( nominationTimePeriod.getMaxNominationsAllowed() != null )
              {
                timePeriodVB.setMaxNominationsAllowed( nominationTimePeriod.getMaxNominationsAllowed().toString() );
              }
              if ( nominationTimePeriod.getMaxWinsllowed() != null )
              {
                timePeriodVB.setMaxWinsllowed( nominationTimePeriod.getMaxWinsllowed().toString() );
              }

              nominationTimePeriodVBList.add( timePeriodVB );
            }
          }
          Collections.sort( nominationTimePeriodVBList, new NominationTimePeriodValueBeanComparator() );
        }
      }
      catch( LazyInitializationException e )
      {
        if ( nominationTimePeriodVBList == null || nominationTimePeriodVBList.size() == 0 )
        {
          addFirstNominationTimePeriod();
        }
      }
    }
    // this.promotionIssuanceTypeCode =
    // promotion.getIssuanceType().getCode();

    // Do this for Recognition only

    if ( promotion.isAwardAmountTypeFixed() )
    {
      setAwardAmountTypeFixed( FIXED );
      if ( promotion.getAwardAmountFixed() != null )
      {
        this.fixedAmount = "" + promotion.getAwardAmountFixed();
      }
    }
    else if ( promotion.getCalculator() != null )
    {
      setAwardAmountTypeFixed( CALCULATOR );
      if ( promotion.getScoreBy() != null && promotion.getCalculator() != null )
      {
        this.scoreBy = promotion.getScoreBy().getCode();
        this.calculatorId = promotion.getCalculator().getId();
        this.setUseRecognitionCalculator( true );
      }
    }
    else
    {
      // range
      setAwardAmountTypeFixed( RANGE );
      if ( promotion.getAwardAmountMin() != null && promotion.getAwardAmountMax() != null )
      {
        this.rangeAmountMin = "" + promotion.getAwardAmountMin();
        this.rangeAmountMax = "" + promotion.getAwardAmountMax();
      }
    }

    this.budgetOption = BUDGET_NONE;

    // Set Budget data only if the awardType is points and method of entry is not file load
    if ( awardsType != null && ( awardsType.equals( PromotionAwardsType.POINTS ) || awardsType.equals( PromotionAwardsType.MERCHANDISE ) )
        && !PromotionIssuanceType.FILE_LOAD.equals( promotionIssuanceTypeCode ) )
    {
      BudgetMaster budgetMaster = promotion.getBudgetMaster();
      if ( budgetMaster != null )
      {
        this.budgetMasterId = budgetMaster.getId();
        this.hiddenBudgetMasterId = budgetMaster.getId();
        if ( budgetMasterId != null )
        {
          this.budgetOption = BUDGET_EXISTING;
        }
        else
        {
          this.budgetOption = BUDGET_NEW;

          this.budgetType = budgetMaster.getBudgetType().getCode();
          this.budgetCapType = budgetMaster.getOverrideableType().getCode();
          this.finalPayoutRule = budgetMaster.getFinalPayoutRule().getCode();
        }
      }
      // to add default budget segments.
      defaultEmptyBudgetSegment();
      this.fileloadBudgetAmount = promotion.isFileloadBudgetAmount();
    }

    this.cashBudgetOption = BUDGET_NONE;

    // Set cash Budget data only if nomination promotion (currently only type with a cash budget)
    if ( promotion.isNominationPromotion() )
    {
      BudgetMaster cashBudgetMaster = promotion.getCashBudgetMaster();
      if ( cashBudgetMaster != null )
      {
        this.cashBudgetMasterId = cashBudgetMaster.getId();
        this.hiddenCashBudgetMasterId = cashBudgetMaster.getId();
        if ( cashBudgetMasterId != null )
        {
          this.cashBudgetOption = BUDGET_EXISTING;
        }
        else
        {
          this.cashBudgetOption = BUDGET_NEW;

          this.cashBudgetType = cashBudgetMaster.getBudgetType().getCode();
          this.cashBudgetCapType = cashBudgetMaster.getOverrideableType().getCode();
          this.cashFinalPayoutRule = cashBudgetMaster.getFinalPayoutRule().getCode();
        }
      }
      // to add default budget segments.
      defaultEmptyCashBudgetSegment();
    }
  }

  private void loadWellnessPromotion( WellnessPromotion promotion )
  {
    this.awardsActive = promotion.isAwardActive();
    this.awardsType = promotion.getAwardType().getCode();
    this.awardsTypeDesc = promotion.getAwardType().getName();
    if ( promotion.isFileLoadEntry() )
    {
      this.promotionIssuanceTypeCode = PromotionIssuanceType.FILE_LOAD;
    }
    if ( promotion.isOnlineEntry() )
    {
      this.promotionIssuanceTypeCode = PromotionIssuanceType.ONLINE;
    }
    SimpleDateFormat sdf = new SimpleDateFormat( "MM/dd/yyyy" );
    this.promotionStartDate = sdf.format( promotion.getSubmissionStartDate() );

    if ( null != promotion.getSubmissionEndDate() )
    {
      this.promotionEndDate = sdf.format( promotion.getSubmissionEndDate() );
    }

    if ( promotion.isAwardAmountTypeFixed() )
    {
      setAwardAmountTypeFixed( FIXED );
      if ( promotion.getAwardAmountFixed() != null )
      {
        this.fixedAmount = "" + promotion.getAwardAmountFixed();
      }
    }
    else
    {
      // range
      setAwardAmountTypeFixed( RANGE );
      if ( promotion.getAwardAmountMin() != null && promotion.getAwardAmountMax() != null )
      {
        this.rangeAmountMin = "" + promotion.getAwardAmountMin();
        this.rangeAmountMax = "" + promotion.getAwardAmountMax();
      }
    }
  }

  private void loadQuizPromotion( QuizPromotion promotion )
  {
    this.awardsActive = promotion.isAwardActive();
    this.awardsType = promotion.getAwardType().getCode();
    this.awardsTypeDesc = promotion.getAwardType().getName();
    if ( promotion.getAwardAmount() != null )
    {
      this.fixedAmount = promotion.getAwardAmount().toString();
    }
    this.budgetOption = BUDGET_NONE;

    // Set Budget data only if the awardType is points and method of entry is not file load
    if ( awardsType.equals( PromotionAwardsType.POINTS ) && !PromotionIssuanceType.FILE_LOAD.equals( promotionIssuanceTypeCode ) )
    {
      BudgetMaster budgetMaster = promotion.getBudgetMaster();
      if ( budgetMaster != null )
      {
        this.budgetMasterId = budgetMaster.getId();
        this.hiddenBudgetMasterId = budgetMaster.getId();
        if ( budgetMasterId != null )
        {
          this.budgetOption = BUDGET_EXISTING;
        }
        else
        {
          this.budgetOption = BUDGET_NEW;
          this.finalPayoutRule = budgetMaster.getFinalPayoutRule().getCode();
          this.budgetType = budgetMaster.getBudgetType().getCode();
          this.budgetCapType = budgetMaster.getOverrideableType().getCode();
        }
      }
      // to add default budget segments.
      defaultEmptyBudgetSegment();
    }
  }

  private List generateMerchLevelProducts( java.util.Set products )
  {
    List merchList = new ArrayList();
    Iterator iter = products.iterator();
    while ( iter.hasNext() )
    {
      MerchLevelProductValueObject merchVo = (MerchLevelProductValueObject)iter.next();

      HomePageItem homePageItem = new HomePageItem();
      homePageItem.setCatalogId( merchVo.getCatalogId() );
      ProductGroupDescriptionsVO productDescription = merchVo.getProductGroupDescriptions();
      if ( !productDescription.getEntry().isEmpty() )
      {
        for ( Iterator<ProductEntryVO> prdIter = productDescription.getEntry().iterator(); prdIter.hasNext(); )
        {
          ProductEntryVO peVO = prdIter.next();
          if ( peVO.getValue().getLocale().equals( UserManager.getUserLocale() ) )
          {
            if ( peVO.getValue().getCopy() != null && peVO.getValue().getCopy().length() > 0 )
            {
              homePageItem.setCopy( peVO.getValue().getCopy().substring( 0, peVO.getValue().getCopy().length() > 1024 ? 1024 : peVO.getValue().getCopy().length() ) );
            }
            homePageItem.setDescription( peVO.getValue().getDescription() );
            break;
          }
        }
      }

      homePageItem.setDetailImgUrl( merchVo.getDetailImageURL().toString() );
      homePageItem.setProductId( (String)merchVo.getProductIds().iterator().next() );
      homePageItem.setProductSetId( merchVo.getProductSetId() );
      homePageItem.setTmbImageUrl( merchVo.getThumbnailImageURL().toString() );
      merchList.add( homePageItem );

    }

    return merchList;
  }

  public void loadStdMerchLevelProducts( java.util.Set products )
  {
    getStdHomePageItemAsList().addAll( generateMerchLevelProducts( products ) );
  }

  public void loadCountryList( List domainCountryList )
  {
    if ( domainCountryList == null )
    {
      return;
    }
    countryList = new ArrayList();
    numOfLevelsEqual = true;
    int sizeOfFirstCountry = -1;
    for ( Iterator countryIter = domainCountryList.iterator(); countryIter.hasNext(); )
    {
      PromoMerchCountry promoMerchCountry = (PromoMerchCountry)countryIter.next();
      PromoMerchProgramLevelFormBean promoMerchProgramLevelFormBean = new PromoMerchProgramLevelFormBean();
      promoMerchProgramLevelFormBean.setNewCountry( true );
      if ( promoMerchCountry != null && promoMerchCountry.getLevels() != null )
      {

        // This block happens only for the first country..
        if ( sizeOfFirstCountry < 0 )
        {
          sizeOfFirstCountry = promoMerchCountry.getLevels().size();

        }
        // This check happens for every country. If the size of first
        // country
        // is not equal to any other country flag is initialized to
        // false.
        if ( sizeOfFirstCountry != promoMerchCountry.getLevels().size() )
        {
          numOfLevelsEqual = false;
        }
        Set<PromoMerchProgramLevel> levelsSet = new TreeSet( new ProgramLevelComparator() );
        levelsSet.addAll( promoMerchCountry.getLevels() );
        for ( Iterator levelIter = levelsSet.iterator(); levelIter.hasNext(); )
        {
          PromoMerchProgramLevel promoMerchProgramLevel = (PromoMerchProgramLevel)levelIter.next();
          promoMerchProgramLevelFormBean.setCountryId( promoMerchCountry.getCountry().getId() );
          promoMerchProgramLevelFormBean.setPromoMerchCountryId( promoMerchCountry.getId() );
          promoMerchProgramLevelFormBean.setOrdinalPosition( promoMerchProgramLevel.getOrdinalPosition() );
          promoMerchProgramLevelFormBean.setPromoMerchLevelId( promoMerchProgramLevel.getId() );
          promoMerchProgramLevelFormBean.setOmLevelName( promoMerchProgramLevel.getLevelName() );
          promoMerchProgramLevelFormBean.setMaxValue( promoMerchProgramLevel.getMaxValue() );
          promoMerchProgramLevelFormBean.setMinValue( promoMerchProgramLevel.getMinValue() );
          promoMerchProgramLevelFormBean.setCountryAssetKey( promoMerchCountry.getCountry().getCmAssetCode() );
          promoMerchProgramLevelFormBean.setProgramId( promoMerchCountry.getProgramId() );
          if ( promoMerchProgramLevel.getCmAssetKey() != null && promoMerchProgramLevel.getCmAssetKey().length() > 0 )
          {
            promoMerchProgramLevelFormBean.setLevelName( CmsResourceBundle.getCmsBundle().getString( promoMerchProgramLevel.getCmAssetKey() + ".LEVEL_NAME" ) );
          }
          else
          {
            promoMerchProgramLevelFormBean.setLevelName( "" );
          }
          countryList.add( promoMerchProgramLevelFormBean );
          promoMerchProgramLevelFormBean = new PromoMerchProgramLevelFormBean();
        }
      }
    }
    if ( numOfLevelsEqual && sizeOfFirstCountry >= 0 )
    {
      numberOfLevels = new Long( sizeOfFirstCountry );
    }
  }

  private class ProgramLevelComparator implements Comparator, Serializable
  {
    public int compare( Object arg1, Object arg2 )
    {
      Long maxValue1 = new Long( ( (PromoMerchProgramLevel)arg1 ).getMaxValue() );
      Long maxValue2 = new Long( ( (PromoMerchProgramLevel)arg2 ).getMaxValue() );

      return maxValue1.compareTo( maxValue2 );
    }
  }

  /**
   * Creates a detatched Promotion Domain Object that will later be synchronized with a looked up
   * promotion object in the service
   * 
   * @return RecognitionPromotion
   */
  public Promotion toDomainObject( Promotion promotion )
  {
    if ( promotion.isAbstractRecognitionPromotion() )
    {
      return toAbstractRecognitionDomainObject( (AbstractRecognitionPromotion)promotion );
    }
    else if ( promotion.isWellnessPromotion() )
    {
      return toWellnessDomainObject( (WellnessPromotion)promotion );
    }

    return toQuizDomainObject( (QuizPromotion)promotion );
  }

  /**
   * Copy values from the form to the domain object.
   * 
   * @param promotion
   * @return Promotion
   */
  public AbstractRecognitionPromotion toAbstractRecognitionDomainObject( AbstractRecognitionPromotion promotion )
  {
    // Time periods first, they'll save even if awards not active
    if ( promotion.isNominationPromotion() )
    {
      NominationPromotion nominationPromotion = (NominationPromotion)promotion;
      if ( !nominationPromotion.isTimePeriodActive() )
      {
        nominationPromotion.setTimePeriodActive( this.timePeriodActive );
      }

      List<NominationPromotionTimePeriod> nominationPromotionTimePeriods = new ArrayList<NominationPromotionTimePeriod>();
      if ( this.nominationTimePeriodVBList != null && this.nominationTimePeriodVBList.size() > 0 )
      {
        for ( NominationTimePeriodValueBean nominationTimePeriodVB : this.nominationTimePeriodVBList )
        {
          NominationPromotionTimePeriod nominationTimePeriod = new NominationPromotionTimePeriod();
          if ( nominationTimePeriodVB.getTimePeriodId() != null && nominationTimePeriodVB.getTimePeriodId() != 0 )
          {
            nominationTimePeriod.setId( nominationTimePeriodVB.getTimePeriodId() );
          }
          nominationTimePeriod.setTimePeriodName( nominationTimePeriodVB.getTimePeriodName() );
          nominationTimePeriod.setTimePeriodStartDate( DateUtils.toDate( nominationTimePeriodVB.getTimePeriodStartDate() ) );
          nominationTimePeriod.setTimePeriodEndDate( DateUtils.toDate( nominationTimePeriodVB.getTimePeriodEndDate() ) );
          // Validation should make sure that this is an integer beforehand (safe to convert.)
          // When empty, leave null.
          nominationTimePeriod
              .setMaxSubmissionAllowed( StringUtil.isNullOrEmpty( nominationTimePeriodVB.getMaxSubmissionAllowed() ) ? null : Integer.parseInt( nominationTimePeriodVB.getMaxSubmissionAllowed() ) );
          nominationTimePeriod
              .setMaxNominationsAllowed( StringUtil.isNullOrEmpty( nominationTimePeriodVB.getMaxNominationsAllowed() ) ? null : Integer.parseInt( nominationTimePeriodVB.getMaxNominationsAllowed() ) );
          nominationTimePeriod.setMaxWinsllowed( StringUtil.isNullOrEmpty( nominationTimePeriodVB.getMaxWinsllowed() ) ? null : Integer.parseInt( nominationTimePeriodVB.getMaxWinsllowed() ) );

          nominationPromotionTimePeriods.add( nominationTimePeriod );
        }
        nominationPromotion.setNominationTimePeriods( new HashSet<NominationPromotionTimePeriod>( nominationPromotionTimePeriods ) );
      }
    }

    promotion.setAwardActive( this.awardsActive );
    if ( !this.awardsActive )
    {
      // if awards is not active we can't have a budget
      if ( this.budgetOption.equals( BUDGET_NONE ) )
      {
        promotion.setBudgetMaster( null );
      }
      promotion.setAwardAmountMin( null );
      promotion.setAwardAmountMax( null );
      promotion.setAwardAmountFixed( null );
      promotion.setCalculator( null );
      promotion.setScoreBy( null );
      promotion.setCashBudgetMaster( null );

      if ( promotion.isRecognitionPromotion() )
      {
        RecognitionPromotion promoRecognition = (RecognitionPromotion)promotion;
        promotion.setFeaturedAwardsEnabled( this.featuredAwardsEnabled );

        Set items = promoRecognition.getHomePageItems();
        items.clear();
        HomePageItem item = toDomainHomePageItem();
        if ( null != item )
        {
          items.add( item );
        }
        promoRecognition.setHomePageItems( items );

        promoRecognition.setBudgetSweepEnabled( false );
        if ( this.budgetOption.equals( BUDGET_NONE ) )
        {
          promoRecognition.setShowInBudgetTracker( false );
        }
        else
        {
          promoRecognition.setShowInBudgetTracker( this.showInBudgetTracker );
        }
        
        /* client customization start */
        promoRecognition.setUtilizeParentBudgets( false );
        /* client customization end */

      }
      else if ( promotion.isNominationPromotion() )
      {
        NominationPromotion promoNomination = (NominationPromotion)promotion;
        if ( this.payoutEachLevel )
        {
          promoNomination.setPayoutLevel( EACH_LEVEL );
          int index = 1;
          LinkedHashSet<NominationPromotionLevel> nominationLevels = new LinkedHashSet<NominationPromotionLevel>();
          for ( NominationPromotionLevelBean levelBean : this.nominationPayoutEachLevelList )
          {
            NominationPromotionLevel nomPromoLevel = new NominationPromotionLevel();
            nomPromoLevel.setId( levelBean.getLevelId() );
            nomPromoLevel.setNominationPromotion( promoNomination );

            nomPromoLevel.setAwardPayoutType( PromotionAwardsType.lookup( levelBean.getAwardsType() ) );
            nomPromoLevel.setLevelIndex( Long.valueOf( index ) );
            nomPromoLevel.setLevelLabel( levelBean.getLevelLabel() );
            nominationLevels.add( nomPromoLevel );
            index++;
            // break;
          }
          promoNomination.getNominationLevels().clear();
          promoNomination.getNominationLevels().addAll( nominationLevels );
        }
        else if ( this.payoutFinalLevel )
        {
          promoNomination.setPayoutLevel( FINAL_LEVEL );
          for ( NominationPayoutFinalLevelBean finalLevelBean : this.nominationPayoutFinalLevelList )
          {
            NominationPromotionLevel nomPromoLevel = new NominationPromotionLevel();
            nomPromoLevel.setId( finalLevelBean.getLevelId() );
            nomPromoLevel.setNominationPromotion( promoNomination );

            nomPromoLevel.setAwardPayoutType( PromotionAwardsType.lookup( finalLevelBean.getAwardsType() ) );
            nomPromoLevel.setLevelIndex( Long.valueOf( promoNomination.getApprovalNodeLevels() ) );
            nomPromoLevel.setLevelLabel( finalLevelBean.getLevelLabel() );
            promoNomination.getNominationLevels().clear();
            promoNomination.getNominationLevels().add( nomPromoLevel );
          }
        }
      }
      return promotion;
    }
    else
    {
      if ( promotion.isNominationPromotion() )
      {
        NominationPromotion nominationPromotion = (NominationPromotion)promotion;
        promotion.setAwardType( PromotionAwardsType.lookup( this.awardsType ) );
        promotion.setTaxable( this.taxable );

        // Request more budget fields
        nominationPromotion.setRequestMoreBudget( false );
        nominationPromotion.setBudgetApprover( null );

        nominationPromotion.setNominatorRecommendedAward( this.nominatorRecommendedAward );
      }
      
      if (promotion.isRecognitionPromotion())
      {
        RecognitionPromotion promoRecognition = (RecognitionPromotion)promotion;
        /* client customization start */
        promotion.setUtilizeParentBudgets( this.utilizeParentBudgets );
        /* client customization end */
      }
    }

    if ( awardsType == null || !awardsType.equals( "merchandise" ) )
    {
      if ( FIXED.equals( awardAmountTypeFixed ) )
      {
        promotion.setAwardAmountTypeFixed( true );
        promotion.setAwardAmountFixed( new Long( this.fixedAmount ) );

        promotion.setAwardAmountMin( null );
        promotion.setAwardAmountMax( null );
        promotion.setCalculator( null );
        promotion.setScoreBy( null );

      }
      else
      {
        promotion.setAwardAmountTypeFixed( false );
        if ( RANGE.equals( awardAmountTypeFixed ) )
        {
          promotion.setAwardAmountMin( new Long( this.rangeAmountMin ) );
          promotion.setAwardAmountMax( new Long( this.rangeAmountMax ) );
          promotion.setAwardAmountFixed( null );
          promotion.setCalculator( null );
          promotion.setScoreBy( null );

        }
        else
        {
          if ( promotion.isRecognitionPromotion() )
          {
            promotion.setAwardAmountMin( null );
            promotion.setAwardAmountMax( null );
            promotion.setAwardAmountFixed( null );
            promotion.setScoreBy( ScoreBy.lookup( this.scoreBy ) );
          }
        }
      }
    }

    // Set Budget data
    if ( this.awardsType != null && !this.awardsType.equals( PromotionAwardsType.POINTS ) && !this.awardsType.equals( PromotionAwardsType.MERCHANDISE ) || this.budgetOption.equals( BUDGET_NONE ) )
    {
      promotion.setBudgetMaster( null );
    }
    else
    {
      if ( this.budgetOption.equals( BUDGET_EXISTING ) || this.budgetOption.equals( BUDGET_SWEEP_EXISTING ) )
      {
        BudgetMaster budgetMaster = new BudgetMaster();
        if ( this.budgetMasterId == null )
        {
          this.budgetMasterId = this.hiddenBudgetMasterId;
        }
        budgetMaster.setId( this.budgetMasterId );
        promotion.setBudgetMaster( budgetMaster );
      }
    }

    promotion.setFileloadBudgetAmount( fileloadBudgetAmount );

    // Cash budget
    if ( BUDGET_NONE.equals( cashBudgetOption ) )
    {
      promotion.setCashBudgetMaster( null );
    }
    else
    {
      if ( BUDGET_EXISTING.equals( cashBudgetOption ) )
      {
        BudgetMaster cashBudgetMaster = new BudgetMaster();
        if ( this.cashBudgetMasterId == null )
        {
          this.cashBudgetMasterId = this.hiddenCashBudgetMasterId;
        }
        cashBudgetMaster.setId( this.cashBudgetMasterId );
        promotion.setCashBudgetMaster( cashBudgetMaster );
      }
    }

    if ( promotion.isNominationPromotion() )
    {
      NominationPromotion nomPromotion = (NominationPromotion)promotion;
      if ( !StringUtils.isBlank( nominationAwardSpecifierTypeCode ) )
      {
        ( (NominationPromotion)promotion ).setAwardSpecifierType( NominationAwardSpecifierType
            .lookup( nominationAwardSpecifierTypeCode ) );
      }
      else
      {
        ( (NominationPromotion)promotion ).setAwardSpecifierType( null );
      }
      /*
       * if ( !StringUtils.isBlank( nominationAwardSpecifierTypeCode ) ) {
       * nomPromotion.setAwardSpecifierType( NominationAwardSpecifierType.lookup(
       * nominationAwardSpecifierTypeCode ) ); } else { nomPromotion.setAwardSpecifierType( null );
       * }
       */

      // Request more budget simply based on flag
      nomPromotion.setRequestMoreBudget( requestMoreBudget );

      // Determine the correct budget approver based on existing approver and new selection
      Participant budgetApprover = null;
      if ( requestMoreBudget )
      {
        // New approver selection
        if ( selectedBudgetApproverUserId != null && selectedBudgetApproverUserId != 0 )
        {
          budgetApprover = getParticipantService().getParticipantById( selectedBudgetApproverUserId );
        }
        // Use existing selection
        else
        {
          budgetApprover = getParticipantService().getParticipantById( nomBudgetApproverId );
        }
      }
      nomPromotion.setBudgetApprover( budgetApprover );
    }

    // Merchandise Addition
    promotion.setAwardStructure( this.awardStructure );

    promotion.setApqConversion( this.apqConversion );
    promotion.setNoNotification( this.noNotification );
    promotion.setFeaturedAwardsEnabled( this.featuredAwardsEnabled );

    if ( promotion.isRecognitionPromotion() )
    {
      RecognitionPromotion promoRecognition = (RecognitionPromotion)promotion;
      Set items = promoRecognition.getHomePageItems();
      items.clear();
      HomePageItem item = toDomainHomePageItem();
      if ( null != item )
      {
        items.add( item );
      }
      promoRecognition.setHomePageItems( items );

      if ( this.awardsType.equals( PromotionAwardsType.POINTS ) )
      {
        promoRecognition.setBudgetSweepEnabled( this.budgetSweepEnabled );
      }
      promoRecognition.setShowInBudgetTracker( this.showInBudgetTracker );
    }

    if ( promotion.isNominationPromotion() )
    {
      NominationPromotion promoNomination = (NominationPromotion)promotion;
      /*
       * if ( promoNomination.getApprovalNodeLevels() != null &&
       * promoNomination.getApprovalNodeLevels().intValue() == 1 ) { this.setPayoutFinalLevel( true
       * ); }
       */
      promoNomination.getNominationLevels().clear();

      if ( this.payoutEachLevel )
      {
        promoNomination.setPayoutLevel( EACH_LEVEL );
        int index = 1;
        LinkedHashSet<NominationPromotionLevel> nominationLevels = new LinkedHashSet<NominationPromotionLevel>();
        for ( NominationPromotionLevelBean levelBean : this.nominationPayoutEachLevelList )
        {
          NominationPromotionLevel nomPromoLevel = new NominationPromotionLevel();
          nomPromoLevel.setId( levelBean.getLevelId() );
          nomPromoLevel.setNominationPromotion( promoNomination );
          if ( StringUtils.isNotEmpty( levelBean.getFixedAmount() ) )
          {
            nomPromoLevel.setNominationAwardAmountFixed( StringUtil.convertStringToDecimal( levelBean.getFixedAmount() ) );
          }
          if ( StringUtils.isNotEmpty( levelBean.getRangeAmountMin() ) )
          {
            nomPromoLevel.setNominationAwardAmountMin( StringUtil.convertStringToDecimal( levelBean.getRangeAmountMin() ) );
          }
          if ( StringUtils.isNotEmpty( levelBean.getRangeAmountMax() ) )
          {
            nomPromoLevel.setNominationAwardAmountMax( StringUtil.convertStringToDecimal( levelBean.getRangeAmountMax() ) );
          }

          if ( FIXED.equals( levelBean.getAwardAmountTypeFixed() ) )
          {
            nomPromoLevel.setNominationAwardAmountTypeFixed( true );
          }
          else if ( CALCULATOR.equals( levelBean.getAwardAmountTypeFixed() ) )
          {
            nomPromoLevel.setNominationAwardAmountTypeFixed( false );
          }
          else
          {
            nomPromoLevel.setNominationAwardAmountTypeFixed( false );
          }

          nomPromoLevel.setAwardPayoutType( PromotionAwardsType.lookup( levelBean.getAwardsType() ) );
          nomPromoLevel.setLevelIndex( Long.valueOf( index ) );
          nomPromoLevel.setLevelLabel( levelBean.getLevelLabel() );
          nomPromoLevel.setPayoutCurrency( levelBean.getPayoutCurrency() );
          nomPromoLevel.setPayoutDescription( levelBean.getPayoutDescription() );
          if ( StringUtils.isNotEmpty( levelBean.getPayoutValue() ) )
          {
            nomPromoLevel.setPayoutValue( new BigDecimal( levelBean.getPayoutValue() ) );
          }
          nomPromoLevel.setQuantity( StringUtils.isEmpty( levelBean.getQuantity() ) ? null : Long.valueOf( levelBean.getQuantity() ) );

          if ( levelBean.getEachLevelCalculatorId() != null )
          {
            Calculator calculator = getCalculatorService().getCalculatorById( levelBean.getEachLevelCalculatorId() );
            nomPromoLevel.setCalculator( calculator );
          }
          nominationLevels.add( nomPromoLevel );
          index++;
        }
        promoNomination.getNominationLevels().clear();
        promoNomination.getNominationLevels().addAll( nominationLevels );
      }
      else if ( this.payoutFinalLevel )
      {
        promoNomination.setPayoutLevel( FINAL_LEVEL );
        for ( NominationPayoutFinalLevelBean finalLevelBean : this.nominationPayoutFinalLevelList )
        {
          NominationPromotionLevel nomPromoLevel = new NominationPromotionLevel();
          nomPromoLevel.setId( finalLevelBean.getLevelId() );
          nomPromoLevel.setNominationPromotion( promoNomination );
          if ( StringUtils.isNotEmpty( finalLevelBean.getFixedAmount() ) )
          {
            nomPromoLevel.setNominationAwardAmountFixed( StringUtil.convertStringToDecimal( finalLevelBean.getFixedAmount() ) );
          }
          if ( StringUtils.isNotEmpty( finalLevelBean.getRangeAmountMin() ) )
          {
            nomPromoLevel.setNominationAwardAmountMin( StringUtil.convertStringToDecimal( finalLevelBean.getRangeAmountMin() ) );
          }
          if ( StringUtils.isNotEmpty( finalLevelBean.getRangeAmountMax() ) )
          {
            nomPromoLevel.setNominationAwardAmountMax( StringUtil.convertStringToDecimal( finalLevelBean.getRangeAmountMax() ) );
          }
          if ( FIXED.equals( finalLevelBean.getAwardAmountTypeFixed() ) )
          {
            nomPromoLevel.setNominationAwardAmountTypeFixed( true );
          }
          else if ( CALCULATOR.equals( finalLevelBean.getAwardAmountTypeFixed() ) )
          {
            nomPromoLevel.setNominationAwardAmountTypeFixed( false );
          }
          else
          {
            nomPromoLevel.setNominationAwardAmountTypeFixed( false );
          }

          nomPromoLevel.setAwardPayoutType( PromotionAwardsType.lookup( finalLevelBean.getAwardsType() ) );
          nomPromoLevel.setLevelIndex( Long.valueOf( promoNomination.getApprovalNodeLevels() ) );
          nomPromoLevel.setLevelLabel( finalLevelBean.getLevelLabel() );
          nomPromoLevel.setPayoutCurrency( finalLevelBean.getPayoutCurrency() );
          nomPromoLevel.setPayoutDescription( finalLevelBean.getPayoutDescription() );
          nomPromoLevel.setPayoutValue( StringUtils.isNotEmpty( finalLevelBean.getPayoutValue() ) ? new BigDecimal( finalLevelBean.getPayoutValue() ) : null );
          nomPromoLevel.setQuantity( finalLevelBean.getQuantity() != null && !finalLevelBean.getQuantity().isEmpty() ? Long.parseLong( finalLevelBean.getQuantity() ) : null );
          if ( finalLevelBean.getFinalLevelCalculatorId() != null )
          {
            Calculator calculator = getCalculatorService().getCalculatorById( finalLevelBean.getFinalLevelCalculatorId() );
            nomPromoLevel.setCalculator( calculator );
          }
          promoNomination.getNominationLevels().add( nomPromoLevel );
        }
      }
    }
    else
    {
      if ( promotion.isNominationPromotion() )
      {
        NominationPromotion promoNomination = (NominationPromotion)promotion;
        promoNomination.getNominationLevels().clear();
      }
    }

    return promotion;
  }

  /**
   * Copy values from the form to the domain object.
   * 
   * @param promotion
   * @return Promotion
   */
  public QuizPromotion toQuizDomainObject( QuizPromotion promotion )
  {
    promotion.setAwardActive( this.awardsActive );

    if ( !this.awardsActive )
    {
      return promotion;
    }

    promotion.setAwardAmount( new Long( this.fixedAmount ) );

    // Set Budget data
    if ( !this.awardsType.equals( PromotionAwardsType.POINTS ) || this.budgetOption.equals( BUDGET_NONE ) )
    {
      promotion.setBudgetMaster( null );
    }
    else
    {
      BudgetMaster budgetMaster = new BudgetMaster();
      if ( this.budgetOption.equals( BUDGET_EXISTING ) )
      {
        budgetMaster.setId( this.budgetMasterId );
        promotion.setBudgetMaster( budgetMaster );
      }

    }

    return promotion;
  }

  public WellnessPromotion toWellnessDomainObject( WellnessPromotion promotion )
  {
    promotion.setAwardActive( this.awardsActive );

    if ( !this.awardsActive )
    {
      promotion.setAwardAmountMin( null );
      promotion.setAwardAmountMax( null );
      promotion.setAwardAmountFixed( null );

      return promotion;
    }

    if ( FIXED.equals( awardAmountTypeFixed ) )
    {
      promotion.setAwardAmountTypeFixed( true );
      promotion.setAwardAmountFixed( new Long( this.fixedAmount ) );
      promotion.setAwardAmountMin( null );
      promotion.setAwardAmountMax( null );
    }
    else
    {
      promotion.setAwardAmountTypeFixed( false );
      if ( RANGE.equals( awardAmountTypeFixed ) )
      {
        promotion.setAwardAmountMin( new Long( this.rangeAmountMin ) );
        promotion.setAwardAmountMax( new Long( this.rangeAmountMax ) );
        promotion.setAwardAmountFixed( null );
      }
    }

    return promotion;
  }

  public List toPromoMerchProgramLevelDomainObject( List domainCountryList )
  {

    if ( countryList != null )
    {
      int ordinalPositionCounter = 0;
      for ( Iterator countryIter = countryList.iterator(); countryIter.hasNext(); )
      {
        ordinalPositionCounter++;
        PromoMerchProgramLevelFormBean promoMerchProgramLevelFormBean = (PromoMerchProgramLevelFormBean)countryIter.next();
        PromoMerchProgramLevel promoMerchProgramLevel = new PromoMerchProgramLevel();
        promoMerchProgramLevel.setLevelName( promoMerchProgramLevelFormBean.getOmLevelName() );
        // Iterate to domain country list
        for ( Iterator domainCountryIter = domainCountryList.iterator(); domainCountryIter.hasNext(); )
        {
          PromoMerchCountry promoMerchCountry = (PromoMerchCountry)domainCountryIter.next();
          if ( promoMerchCountry.getId().longValue() == promoMerchProgramLevelFormBean.getPromoMerchCountryId().longValue() )
          {
            boolean found = false;
            Collection levels = promoMerchCountry.getLevels();
            if ( levels != null )
            {
              // Iterate each level in domain Country
              for ( Iterator levelIter = levels.iterator(); levelIter.hasNext(); )
              {
                PromoMerchProgramLevel promoMerchProgramLevelLoop = (PromoMerchProgramLevel)levelIter.next();
                if ( promoMerchProgramLevelLoop != null && promoMerchProgramLevelLoop.getLevelName().equals( promoMerchProgramLevel.getLevelName() ) )
                {
                  promoMerchProgramLevelLoop.setDisplayLevelName( promoMerchProgramLevelFormBean.getLevelName() );
                  found = true;
                  break;
                }
              }
            }
            // If level is not found in this country, then add it to
            // the list..
            if ( !found )
            {
              promoMerchProgramLevel.setDisplayLevelName( promoMerchProgramLevelFormBean.getLevelName() );
              promoMerchProgramLevel.setOrdinalPosition( ordinalPositionCounter );
              promoMerchProgramLevel.setLevelName( promoMerchProgramLevelFormBean.getOmLevelName() );
              promoMerchProgramLevel.setProgramId( promoMerchProgramLevelFormBean.getProgramId() );
              promoMerchProgramLevel.setPromoMerchCountry( promoMerchCountry );
              if ( promoMerchCountry.getLevels() == null )
              {
                promoMerchCountry.setLevels( new LinkedHashSet() );
              }
              promoMerchCountry.getLevels().add( promoMerchProgramLevel );
            }
          } // If country id match
        }
      }
    }
    return domainCountryList;
  }

  /**
   * Validate the properties that have been set from this HTTP request, and return an
   * <code>ActionErrors</code> object that encapsulates any validation errors that have been
   * found. If no errors are found, return <code>null</code> or an <code>ActionErrors</code>
   * object with no recorded error messages.
   * 
   * @param mapping the mapping used to select this instance.
   * @param request the servlet request we are processing.
   * @return <code>ActionErrors</code> object that encapsulates any validation errors.
   */
  public ActionErrors validate( ActionMapping mapping, HttpServletRequest request )
  {

    ActionErrors errors = super.validate( mapping, request );
    if ( errors == null )
    {
      errors = new ActionErrors();
    }

    // Nomination time periods can be changed even when awards not active. Validate it first.
    if ( promotionTypeCode.equals( PromotionType.NOMINATION ) && timePeriodActive )
    {
      validateTimPeriods( errors );
    }

    // new nomination validation
    if ( promotionTypeCode.equals( PromotionType.NOMINATION ) )
    {
      validateNominationAwards( errors );
    }

    if ( promotionTypeCode.equals( PromotionType.RECOGNITION ) )
    {
      if ( awardsType.equalsIgnoreCase( PromotionAwardsType.MERCHANDISE ) )
      {
        if ( countryList != null )
        {
          for ( Iterator countryIter = countryList.iterator(); countryIter.hasNext(); )
          {
            PromoMerchProgramLevelFormBean promoMerchProgramLevelFormBean = (PromoMerchProgramLevelFormBean)countryIter.next();
            if ( promoMerchProgramLevelFormBean.getLevelName() == null || promoMerchProgramLevelFormBean.getLevelName().trim().length() <= 0 )
            {
              errors.add( "levelNames", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "promotion.awards.AWARD_LEVEL_NAMES" ) ) );
              break;
            }
          }
        }
      }
    }

    // Validate the 'request more budget' section for nomination promotion
    // Doing this now because the remainder of validation expects budget to be the last thing
    if ( requestMoreBudget )
    {
      if ( promotionTypeCode.equals( PromotionType.NOMINATION ) )
      {
        // Check if there is a new approver selection
        if ( selectedBudgetApproverUserId != null && selectedBudgetApproverUserId != 0 )
        {
          // Make sure the selection actually leads to a participant
          if ( getParticipantService().getParticipantById( selectedBudgetApproverUserId ) == null )
          {
            errors.add( "budgetApprover", new ActionMessage( "promotion.awards.errors.INVALID_BUDGET_APPROVER" ) );
          }
        }
        // Fall back to existing selection
        else
        {
          // Check that there actually is an existing selection
          if ( nomBudgetApproverId == null || nomBudgetApproverId == 0 )
          {
            errors.add( "budgetApprover", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "promotion.awards.BUDGET_APPROVER" ) ) );
          }
          // Check that the existing selection actually leads to a participant
          else if ( getParticipantService().getParticipantById( nomBudgetApproverId ) == null )
          {
            errors.add( "budgetApprover", new ActionMessage( "promotion.awards.errors.INVALID_BUDGET_APPROVER" ) );
          }
        }
      }
    }

    if ( promotionTypeCode.equals( PromotionType.RECOGNITION ) )
    {
      if ( awardsType != null && awardsType.equalsIgnoreCase( PromotionAwardsType.MERCHANDISE ) )
      {
        validateMerchandiseType( errors );
      }
      else
      {
        // ***** Validate awardAmountTypeFixed *****/
        // Make sure its not empty
        // bug fix for 15926 . no need to to do award amount validation when ticket type is paper
        // ticket.
        if ( awardsActive )
        {
          if ( StringUtils.isBlank( awardAmountTypeFixed ) )
          {
            errors.add( "awardAmountTypeFixed", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "promotion.awards.AMOUNT" ) ) );
          }
          else if ( FIXED.equals( awardAmountTypeFixed ) )
          {
            validateFixedNonMerchandiseAwardType( errors );
          }
          else if ( RANGE.equals( awardAmountTypeFixed ) && promotionIssuanceTypeCode.equals( PromotionIssuanceType.ONLINE ) )
          {
            validateRangeAwardType( errors );
          }
          else if ( RANGE.equals( awardAmountTypeFixed ) && promotionIssuanceTypeCode.equals( PromotionIssuanceType.FILE_LOAD ) )
          {
            validateAllowMinNegativeAmtForRangeAward( errors );
          }
          else if ( CALCULATOR.equals( awardAmountTypeFixed ) )
          {
            validateNonMerchandiseCalculator( errors );
          }

        }
        /*
         * else if ( promotionTypeCode.equals( PromotionType.NOMINATION ) && RANGE.equals(
         * awardAmountTypeFixed ) ) { validateRangeAwardType( errors ); }
         */
      }

      // ***** Validate awardsType *****/
      String awardsType = request.getParameter( "awardsType" );
      // If awardsType is not Points, then we don't need to validate
      // the budget data
      if ( awardsType != null && !awardsType.equals( PromotionAwardsType.POINTS ) && !awardsType.equals( PromotionAwardsType.MERCHANDISE ) && !awardsType.equals( PromotionAwardsType.CASH )
          && !awardsType.equals( PromotionAwardsType.OTHER ) )
      {
        return errors;
      }

      // If the validation captures any promotion related validation errors, then throw the
      // errors before doing budget related validations
      if ( ( this.getPromotionTypeCode().equals( PromotionType.RECOGNITION ) || this.getPromotionTypeCode().equals( PromotionType.NOMINATION ) ) && this.live && !errors.isEmpty() )
      {
        return errors;
      }

      /*
       * if( this.awardsType != null && !this.awardsType.isEmpty()) { if(this.payoutValue != null &&
       * !this.payoutValue.isEmpty()) { if(this.payoutValue.indexOf( "." ) > -1 &&
       * (this.payoutValue.length() - (this.payoutValue.indexOf( "." )+1) > 2)) { errors.add(
       * ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
       * "promotion.basics.errors.PAYOUT_VALUE_NOT_VALID" ) ); } if(this.payoutCurrency == null ||
       * this.payoutCurrency.isEmpty()) { errors.add( ActionMessages.GLOBAL_MESSAGE, new
       * ActionMessage( "promotion.basics.errors.PAYOUT_CURRENCY_REQUIRED" ) ); } } }
       */
    }
    else if ( promotionTypeCode.equals( PromotionType.WELLNESS ) )
    {
      if ( StringUtils.isBlank( awardAmountTypeFixed ) )
      {
        errors.add( "awardAmountTypeFixed", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "promotion.awards.AMOUNT" ) ) );
      }
      else if ( FIXED.equals( awardAmountTypeFixed ) )
      {
        validateFixedNonMerchandiseAwardType( errors );
      }
      else if ( RANGE.equals( awardAmountTypeFixed ) && promotionIssuanceTypeCode.equals( PromotionIssuanceType.ONLINE ) )
      {
        validateRangeAwardType( errors );
      }
      else if ( RANGE.equals( awardAmountTypeFixed ) && promotionIssuanceTypeCode.equals( PromotionIssuanceType.FILE_LOAD ) )
      {
        validateAllowMinNegativeAmtForRangeAward( errors );
      }
    }
    // quiz promotion validation
    else
    {
      if ( !promotionTypeCode.equals( PromotionType.NOMINATION ) )
      {
        if ( awardsActive && ( fixedAmount == null || fixedAmount.length() == 0 ) )
        {
          errors.add( ActionMessages.GLOBAL_MESSAGE, new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "promotion.awards.AMOUNT" ) ) );
        }
        // to catch the numberFormatExpection
        try
        {
          if ( fixedAmount != null && !fixedAmount.equals( "" ) && Long.parseLong( fixedAmount ) < 0 )
          {
            /*
             * errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage(
             * "promotion.notification.errors.SUB_ZERO", CmsResourceBundle
             * .getCmsBundle().getString( "promotion.awards.AMOUNT" ) ) );
             */
            errors.add( ActionMessages.GLOBAL_MESSAGE, new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "promotion.awards.AMOUNT" ) ) );
          }
        }
        catch( NumberFormatException e )
        {
          errors.add( ActionMessages.GLOBAL_MESSAGE, new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_INVALID, CmsResourceBundle.getCmsBundle().getString( "promotion.awards.AMOUNT" ) ) );
        }
      }
    }

    if ( promotionTypeCode.equals( PromotionType.QUIZ ) )
    {
      setPromotionIssuanceTypeCode( PromotionIssuanceType.ONLINE );
    }

    // Cash budget validation
    if ( awardsActive && cashBudgetType != null && !cashBudgetType.isEmpty() )
    {
      validateCashBudget( request, errors );
    }

    // ***** BUDGET VALIDATION *****/
    if ( awardsActive && !promotionTypeCode.equals( PromotionType.WELLNESS ) && !promotionIssuanceTypeCode.equals( PromotionIssuanceType.FILE_LOAD ) )
    {
      // ***** Validate fileloadBudgetAmount *****/
      if ( !BudgetType.CENTRAL_BUDGET_TYPE.equals( budgetType ) )
      {
        String fileloadBudgetAmount = request.getParameter( "fileloadBudgetAmount" );
        // Make sure its not empty
        if ( fileloadBudgetAmount == null || fileloadBudgetAmount.length() == 0 )
        {
          errors.add( "fileloadBudgetAmount", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "promotion.awards.BUDGET_AMOUNTS" ) ) );
        }
      }

      // ***** Validate budgetOption *****/
      String budgetOption = request.getParameter( "budgetOption" );
      // Make sure its not empty
      if ( budgetOption == null || budgetOption.length() == 0 )
      {
        errors.add( "budgetOption", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "promotion.awards.HAS_BUDGET" ) ) );

        // if no budgetOption was selected, then no point validating further
        return errors;
      }

      // If no budget, then we are finished validating
      if ( budgetOption.equals( BUDGET_NONE ) )
      {
        if ( this.budgetSweepEnabled )
        {
          errors.add( "budgetSweepEnabled", new ActionMessage( "promotion.awards.BUDGET_SWEEP_BUDGET_ERROR" ) );
        }

        if ( this.showInBudgetTracker )
        {
          errors.add( "showInBudgetTracker", new ActionMessage( "promotion.awards.BUDGET_TRACKER_BUDGET_ERROR" ) );
        }

        return errors;
      }

      if ( budgetOption.equals( BUDGET_EXISTING ) )
      {
        // ***** Validate budgetMasterId *****/
        String budgetMasterId = request.getParameter( "budgetMasterId" );
        String budgetMasterSweepId = request.getParameter( "budgetMasterSweepId" );
        if ( budgetMasterId == null || budgetMasterId.length() == 0 || budgetMasterId.equals( "0" ) )
        {
          errors.add( "budgetMasterId", new ActionMessage( "promotion.awards.errors.CHOOSE_EXISTING_BUDGET" ) );
        }
        else
        {
          try
          {
            Long.parseLong( budgetMasterId );
          }
          catch( NumberFormatException e )
          {
            errors.add( "budgetMasterId", new ActionMessage( "promotion.awards.errors.INVALID_BUDGET" ) );
          }
          // start
          // validate sweep budget vs non sweep budget
          if ( this.budgetSweepEnabled )
          {
            boolean nonSweepBudget = false;
            AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
            associationRequestCollection.add( new BudgetMasterToBudgetSegmentsAssociationRequest() );
            BudgetMaster budgetMaster = getBudgetMasterService().getBudgetMasterById( new Long( budgetMasterId ), associationRequestCollection );

            for ( BudgetSegment budgetSegment : budgetMaster.getBudgetSegments() )
            {
              if ( budgetSegment.getPromotionBudgetSweeps().isEmpty() )
              {
                nonSweepBudget = true;
                break;
              }
            }

            if ( nonSweepBudget )
            {
              errors.add( "budgetMasterId", new ActionMessage( "promotion.awards.errors.INVALID_BUDGET" ) );
            }
          }
          // end
        }
        return errors;
      }
      if ( budgetOption.equals( BUDGET_NEW ) )
      {
        // ***** Validate budgetMasterName *****/
        String budgetMasterName = request.getParameter( "budgetMasterName" );
        // Make sure its not empty
        if ( budgetMasterName == null || budgetMasterName.length() == 0 )
        {
          errors.add( "budgetMasterName", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "promotion.awards.BUDGET_MASTER_NAME" ) ) );
        }

        // ***** Validate budgetMasterStartDate *****/
        Date startDateLocal = null;
        Date endDateLocal = null;

        // Make sure its not empty
        if ( budgetMasterStartDate == null || budgetMasterStartDate.length() == 0 )
        {
          errors.add( "budgetMasterStartDate",
                      new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "admin.budgetmaster.details.START_DATE" ) ) );
        }
        else
        {
          // The date string must represent a date.
          startDateLocal = DateUtils.toDate( budgetMasterStartDate );
          if ( startDateLocal == null )
          {
            errors.add( "budgetMasterStartDate",
                        new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_DATE, CmsResourceBundle.getCmsBundle().getString( "admin.budgetmaster.details", "START_DATE" ) ) );
          }
        }

        // end date of budget master can be empty, so no validation for null.
        if ( budgetMasterEndDate != null && budgetMasterEndDate.length() > 0 )
        {
          // The date string must represent a date.
          endDateLocal = DateUtils.toDate( budgetMasterEndDate );
          if ( endDateLocal == null )
          {
            errors.add( "budgetMasterEndDate",
                        new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_DATE, CmsResourceBundle.getCmsBundle().getString( "admin.budgetmaster.details", "END_DATE" ) ) );
          }
          else if ( endDateLocal.before( DateUtils.toStartDate( DateUtils.getCurrentDate() ) ) )
          {
            // The date is before current date
            errors.add( "budgetMasterEndDate", new ActionMessage( "promotion.awards.errors.BUDGET_MASTER_END_DATE_INVALID" ) );
          }

          // The start date must be earlier than or equal to the end date.
          if ( startDateLocal != null && endDateLocal != null && endDateLocal.before( startDateLocal ) )
          {
            errors.add( "budgetMasterStartDate", new ActionMessage( "promotion.awards.errors.BUDGET_MASTER_START_DATE_INVALID" ) );
          }
        }

        if ( startDateLocal != null && endDateLocal != null && startDateLocal.compareTo( endDateLocal ) == 0 )
        {
          errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "admin.budgetmaster.details.BUDGET_MASTER_START_END_DATE_EQUAL" ) );
        }

        // ***** Validate budgetType *****/
        String budgetType = request.getParameter( "budgetType" );
        // Make sure its not empty
        if ( budgetType == null || budgetType.length() == 0 )
        {
          errors.add( "budgetType", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "promotion.awards.BUDGET_TYPE" ) ) );
        }

        // ***** Validate budgetCapType *****/
        String budgetCapType = request.getParameter( "budgetCapType" );
        // Make sure its not empty
        if ( budgetCapType == null || budgetCapType.length() == 0 )
        {
          errors.add( "budgetCapType", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "promotion.awards.CAP_TYPE" ) ) );

          // if no budgetCapType was selected, then no point validating
          // further
          return errors;
        }

        // validate for time period start/end dates and segment name.
        errors = validateTimePeriod( errors );
      }

      if ( BudgetType.CENTRAL_BUDGET_TYPE.equals( budgetType ) )
      {
        errors = validateTimePeriodBudgetAmount( errors, budgetSegmentVBList, getBudgetSegmentVBListSize() );
        // validate the finalPayoutRule required for quiz and recognition promotion when new budget
        // is created on fly
        if ( promotionTypeCode.equals( PromotionType.QUIZ ) || promotionTypeCode.equals( PromotionType.RECOGNITION ) )
        {
          if ( awardsType.equals( PromotionAwardsType.POINTS ) )
          {
            if ( this.finalPayoutRule == null )
            {
              errors.add( "finalPayoutRule", new ActionMessage( "system.errors.REQUIRED", CmsResourceBundle.getCmsBundle().getString( "admin.budgetmaster.details", "FINAL_PAYOUT_RULE" ) ) );
            }
          }
        }
      }
    }

    return errors;
  }

  /**
   * Validate the cash budget section. Pulled into its own method since it was copied from point budget, 
   * and point budget relies on being able to return early from validation. This way the logic is the same, 
   * and the method can return early without stopping further validation. 
   */
  private void validateCashBudget( HttpServletRequest request, ActionErrors errors )
  {
    // ***** CASH BUDGET VALIDATION *****/
    if ( promotionTypeCode.equals( PromotionType.NOMINATION ) )
    {
      // Cash budget must be a central budget
      if ( !BudgetType.CENTRAL_BUDGET_TYPE.equals( cashBudgetType ) )
      {
        errors.add( "cashBudgetType", new ActionMessage( "promotion.awards.errors.CASH_CENTRAL_TYPE" ) );
      }

      // ***** Validate budgetOption *****/
      String cashBudgetOption = request.getParameter( "cashBudgetOption" );
      // Make sure its not empty
      if ( cashBudgetOption == null || cashBudgetOption.length() == 0 )
      {
        errors.add( "cashBudgetOption", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "promotion.awards.HAS_BUDGET" ) ) );

        // if no budgetOption was selected, then no point validating further
        return;
      }

      // If no budget, then we are finished validating
      if ( cashBudgetOption.equals( BUDGET_NONE ) )
      {
        return;
      }

      if ( cashBudgetOption.equals( BUDGET_EXISTING ) )
      {
        // ***** Validate budgetMasterId *****/
        String cashBudgetMasterId = request.getParameter( "cashBudgetMasterId" );
        if ( cashBudgetMasterId == null || cashBudgetMasterId.length() == 0 || cashBudgetMasterId.equals( "0" ) )
        {
          errors.add( "cashBudgetMasterId", new ActionMessage( "promotion.awards.errors.CHOOSE_EXISTING_BUDGET" ) );
        }
        else
        {
          try
          {
            Long.parseLong( cashBudgetMasterId );
          }
          catch( NumberFormatException e )
          {
            errors.add( "cashBudgetMasterId", new ActionMessage( "promotion.awards.errors.INVALID_BUDGET" ) );
          }
        }
        return;
      }
      if ( cashBudgetOption.equals( BUDGET_NEW ) )
      {
        // ***** Validate budgetMasterName *****/
        String cashBudgetMasterName = request.getParameter( "cashBudgetMasterName" );
        // Make sure its not empty
        if ( cashBudgetMasterName == null || cashBudgetMasterName.length() == 0 )
        {
          errors.add( "cashBudgetMasterName",
                      new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "promotion.awards.BUDGET_MASTER_NAME" ) ) );
        }

        // ***** Validate budgetMasterStartDate *****/
        Date startDateLocal = null;
        Date endDateLocal = null;

        // Make sure its not empty
        if ( cashBudgetMasterStartDate == null || cashBudgetMasterStartDate.length() == 0 )
        {
          errors.add( "cashBudgetMasterStartDate",
                      new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "admin.budgetmaster.details.START_DATE" ) ) );
        }
        else
        {
          // The date string must represent a date.
          startDateLocal = DateUtils.toDate( cashBudgetMasterStartDate );
          if ( startDateLocal == null )
          {
            errors.add( "cashBudgetMasterStartDate",
                        new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_DATE, CmsResourceBundle.getCmsBundle().getString( "admin.budgetmaster.details", "START_DATE" ) ) );
          }
        }

        // end date of budget master can be empty, so no validation for null.
        if ( cashBudgetMasterEndDate != null && cashBudgetMasterEndDate.length() > 0 )
        {
          // The date string must represent a date.
          endDateLocal = DateUtils.toDate( cashBudgetMasterEndDate );
          if ( endDateLocal == null )
          {
            errors.add( "cashBudgetMasterEndDate",
                        new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_DATE, CmsResourceBundle.getCmsBundle().getString( "admin.budgetmaster.details", "END_DATE" ) ) );
          }
          else if ( endDateLocal.before( DateUtils.toStartDate( DateUtils.getCurrentDate() ) ) )
          {
            // The date is before current date
            errors.add( "cashBudgetMasterEndDate", new ActionMessage( "promotion.awards.errors.BUDGET_MASTER_END_DATE_INVALID" ) );
          }

          // The start date must be earlier than or equal to the end date.
          if ( startDateLocal != null && endDateLocal != null && endDateLocal.before( startDateLocal ) )
          {
            errors.add( "cashBudgetMasterStartDate", new ActionMessage( "promotion.awards.errors.BUDGET_MASTER_START_DATE_INVALID" ) );
          }
        }

        if ( startDateLocal != null && endDateLocal != null && startDateLocal.compareTo( endDateLocal ) == 0 )
        {
          errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "admin.budgetmaster.details.BUDGET_MASTER_START_END_DATE_EQUAL" ) );
        }

        // ***** Validate budgetType *****/
        String cashBudgetType = request.getParameter( "cashBudgetType" );
        // Make sure its not empty
        if ( cashBudgetType == null || cashBudgetType.length() == 0 )
        {
          errors.add( "cashBudgetType", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "promotion.awards.BUDGET_TYPE" ) ) );
        }

        // ***** Validate budgetCapType *****/
        String cashBudgetCapType = request.getParameter( "cashBudgetCapType" );
        // Make sure its not empty
        if ( cashBudgetCapType == null || cashBudgetCapType.length() == 0 )
        {
          errors.add( "cashBudgetCapType", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "promotion.awards.CAP_TYPE" ) ) );

          // if no budgetCapType was selected, then no point validating
          // further
          return;
        }

        // validate for time period start/end dates and segment name.
        errors = validateCashTimePeriod( errors );
      }

      if ( BudgetType.CENTRAL_BUDGET_TYPE.equals( cashBudgetType ) )
      {
        errors = validateTimePeriodBudgetAmount( errors, cashBudgetSegmentVBList, getCashBudgetSegmentVBListSize() );
        // validate the finalPayoutRule required for quiz and recognition promotion when new budget
        // is created on fly
        if ( promotionTypeCode.equals( PromotionType.QUIZ ) || promotionTypeCode.equals( PromotionType.RECOGNITION ) )
        {
          if ( awardsType.equals( PromotionAwardsType.POINTS ) )
          {
            if ( this.cashFinalPayoutRule == null )
            {
              errors.add( "cashFinalPayoutRule", new ActionMessage( "system.errors.REQUIRED", CmsResourceBundle.getCmsBundle().getString( "admin.budgetmaster.details", "FINAL_PAYOUT_RULE" ) ) );
            }
          }
        }
      }
    }
  }

  private void validateNominationAwards( ActionErrors errors )
  {

    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.CUSTOM_APPROVER_OPTIONS ) );
    NominationPromotion promotion = (NominationPromotion)getPromotionService().getPromotionByIdWithAssociations( promotionId, associationRequestCollection );

    if ( promotion.isLive() )
    {
      if ( promotion.getPayoutLevel().equals( EACH_LEVEL ) )
      {
        this.payoutEachLevel = true;
      }
      else
      {
        this.payoutFinalLevel = true;
      }
    }
     
    if ( promotion.isLevelPayoutByApproverAvailable() && !this.payoutEachLevel && !this.payoutFinalLevel )
    {
      errors.add( "payoutEachLevel" + "payoutFinalLevel",
                  new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "promotion.awards.PAYOUT_LEVEL_TYPE" ) ) );
    }
    if ( !promotion.getCustomApproverOptions().isEmpty() )
    {
      Iterator<ApproverOption> iterator = promotion.getCustomApproverOptions().iterator();

      while ( iterator.hasNext() )
      {
        ApproverOption approverOption = iterator.next();
        if ( approverOption.getApproverType() != null )
        {
          if ( approverOption.getApproverType().getCode().equals( CustomApproverType.AWARD ) && !this.awardsActive )
          {
            errors.add( "", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "promotion.awards.SELECT_AWARDS" ) ) );
          }
        }
      }
    }
    if ( this.payoutEachLevel )
    {
      validatePayoutEachLevel( errors );
    }
    else if ( this.payoutFinalLevel )
    {
      validatePayoutFinalLevel( errors );
    }
  }

  private void validatePayoutEachLevel( ActionErrors errors )
  {
    Set<Long> calcIds = new HashSet<Long>();
    Set<String> levelLabels = new HashSet<String>();
    List<String> levelLabelsList = new ArrayList<String>();
    String currentLevelAwardType = null;
    String previousLevelAwardType = null;
    int levelIndex = 0;

    for ( NominationPromotionLevelBean nomPromoLevelBean : getNominationPayoutEachLevelList() )
    {
      levelIndex++;
      if ( StringUtils.isBlank( nomPromoLevelBean.getLevelLabel() ) )
      {
        errors.add( "nomPromoLevelBean.getLevelLabel()",
                    new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "promotion.awards.LEVEL_LABEL" ) ) );
      }
      else
      {
        levelLabels.add( nomPromoLevelBean.getLevelLabel().toLowerCase() );
        levelLabelsList.add( nomPromoLevelBean.getLevelLabel().toLowerCase() );
      }
      if ( StringUtils.isBlank( nomPromoLevelBean.getAwardsType() ) )
      {
        errors.add( "nomPromoLevelBean.getAwardsType()", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "promotion.awards.TYPE" ) ) );
      }
      else if ( nomPromoLevelBean.getAwardsType().equals( PromotionAwardsType.POINTS ) || nomPromoLevelBean.getAwardsType().equals( PromotionAwardsType.CASH ) )
      {
        if ( nomPromoLevelBean.getAwardAmountTypeFixed().equals( FIXED ) )
        {
          if ( nomPromoLevelBean.getFixedAmount() == null || nomPromoLevelBean.getFixedAmount().length() == 0 )
          {
            errors.add( "nomPromoLevelBean.getAwardAmountTypeFixed()",
                        new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "promotion.awards.AMOUNT" ) ) );
          }
          else
          {
            try
            {
              if ( nomPromoLevelBean.getAwardsType().equals( PromotionAwardsType.POINTS ) )
              {
                Pattern pattern = Pattern.compile( "\\." );
                Matcher matcher = pattern.matcher( nomPromoLevelBean.getFixedAmount() );
                if ( matcher.find() )
                {
                  errors.add( "", new ActionMessage( "promotion.awards.DECIMAL_POINTS" ) );
                  break;
                }
              }
              // make sure the data is actually valid
              BigDecimal fAmt = StringUtil.convertStringToDecimal( nomPromoLevelBean.getFixedAmount() );
              if ( fAmt != null )
              {
                if ( fAmt.signum() < 0 )
                {
                  errors.add( "nomPromoLevelBean.getAwardAmountTypeFixed(",
                              new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "promotion.awards.AMOUNT" ) ) );
                }
                else if ( fAmt.signum() == 0 )
                {
                  errors.add( "nomPromoLevelBean.getAwardAmountTypeFixed(",
                              new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_NON_ZERO_POSITIVE_INTEGER, CmsResourceBundle.getCmsBundle().getString( "promotion.awards.AMOUNT" ) ) );
                }
              }
            }
            catch( NumberFormatException e )
            {
              errors.add( "awardAmountTypeFixed", new ActionMessage( "promotion.awards.errors.FIXED_AMOUNT" ) );
            }
          }
        }
        else if ( nomPromoLevelBean.getAwardAmountTypeFixed().equals( RANGE ) )
        {
          try
          {
            // required field validation
            if ( StringUtil.isEmpty( nomPromoLevelBean.getRangeAmountMin() ) || StringUtil.isEmpty( nomPromoLevelBean.getRangeAmountMax() ) )
            {
              errors.add( "nomPromoLevelBean.getAwardAmountTypeFixed()", new ActionMessage( "promotion.awards.errors.AMOUNT_RANGES_REQ" ) );
            }
            else
            {
              if ( nomPromoLevelBean.getAwardsType().equals( PromotionAwardsType.POINTS ) )
              {
                Pattern pattern = Pattern.compile( "\\." );
                Matcher matcher1 = pattern.matcher( nomPromoLevelBean.getRangeAmountMin() );
                Matcher matcher2 = pattern.matcher( nomPromoLevelBean.getRangeAmountMax() );
                if ( matcher1.find() || matcher2.find() )
                {
                  errors.add( "", new ActionMessage( "promotion.awards.DECIMAL_POINTS" ) );
                  break;
                }
              }
              // make sure the data is actually valid
              BigDecimal rangeAmountMinValue = StringUtil.convertStringToDecimal( nomPromoLevelBean.getRangeAmountMin() );
              BigDecimal rangeAmountMaxValue = StringUtil.convertStringToDecimal( nomPromoLevelBean.getRangeAmountMax() );
              // If the min or max value is less than or equal
              // to zero, then add the error
              if ( rangeAmountMinValue.signum() <= 0 || rangeAmountMaxValue.signum() <= 0 )
              {
                errors.add( "nomPromoLevelBean.getAwardAmountTypeFixed()", new ActionMessage( "promotion.awards.errors.AMOUNT_RANGES" ) );
              }

              // If the min value is greater than or equals to
              // the max value, then add the error
              if ( rangeAmountMinValue.compareTo( rangeAmountMaxValue ) > 0 )
              {
                errors.add( "nomPromoLevelBean.getAwardAmountTypeFixed()", new ActionMessage( "promotion.awards.errors.AMOUNT_RANGES" ) );
              }
            }
          }
          catch( NumberFormatException e )
          {
            errors.add( "nomPromoLevelBean.getAwardAmountTypeFixed()", new ActionMessage( "promotion.awards.errors.AMOUNT_RANGES" ) );
          }
        }
        else
        {
          if ( nomPromoLevelBean.getEachLevelCalculatorId() == null || nomPromoLevelBean.getEachLevelCalculatorId().longValue() == 0 )
          {
            errors.add( "nomPromoLevelBean.getAwardAmountTypeFixed()", new ActionMessage( "promotion.awards.errors.CALCULATOR_NOT_SELECTED" ) );
          }
          else
          {
            calcIds.add( nomPromoLevelBean.getEachLevelCalculatorId() );
            if ( calcIds.size() > 1 )
            {
              errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "promotion.awards.errors.CALCULATOR_MUST_SAME" ) );
            }
          }
        }
      }
      else if ( nomPromoLevelBean.getAwardsType().equals( PromotionAwardsType.OTHER ) )
      {
        if ( StringUtils.isBlank( nomPromoLevelBean.getPayoutDescription() ) )
        {
          errors.add( "payoutDescription", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "promotion.basics.PAYOUT_DESCRIPTION" ) ) );
        }
        if ( StringUtils.isBlank( nomPromoLevelBean.getPayoutValue() ) )
        {
          errors.add( "payoutValue", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "promotion.basics.PAYOUT_VALUE" ) ) );
        }
        if ( StringUtils.isBlank( nomPromoLevelBean.getPayoutCurrency() ) )
        {
          errors.add( "payoutCurrency", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "promotion.basics.PAYOUT_CURRENCY" ) ) );
        }
        if ( !StringUtils.isBlank( nomPromoLevelBean.getQuantity() ) && Integer.parseInt( nomPromoLevelBean.getQuantity() ) < 0 )
        {
          errors.add( "nomPromoLevelBean.getAwardAmountTypeFixed(",
                      new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_NON_NEGATIVE_INTEGER, CmsResourceBundle.getCmsBundle().getString( "promotion.basics.QUANTITY" ) ) );
        }
      }

      if ( levelIndex > 1 && this.otherLevelsAward && this.customApproverAwardLevel == levelIndex )
      {
        currentLevelAwardType = nomPromoLevelBean.getAwardsType();
        if ( previousLevelAwardType != null && currentLevelAwardType != null )
        {
          if ( !previousLevelAwardType.equals( currentLevelAwardType ) )
          {
            String message = "Level " + ( this.customApproverAwardLevel - 1 ) + " and Level " + this.customApproverAwardLevel;
            errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "promotion.awards.errors.AWARD_TYPE_NOT_EQUAL", message ) );
          }
        }
      }
      previousLevelAwardType = nomPromoLevelBean.getAwardsType();
    }

    if ( !levelLabels.isEmpty() && levelLabels.size() < levelLabelsList.size() )
    {
      errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "promotion.awards.errors.LABEL_NOT_UNIQUE" ) );
    }
    levelLabels.clear();
    calcIds.clear();

    // Recommended award must be no
    if ( nominatorRecommendedAward )
    {
      errors.add( "nominatorRecommendedAward", new ActionMessage( "promotion.awards.errors.REC_AWARD_EACH_LEVEL" ) );
    }
  }

  private void validatePayoutFinalLevel( ActionErrors errors )
  {
    Set<Long> calcIds = new HashSet<Long>();
    Set<String> levelLabels = new HashSet<String>();
    List<String> levelLabelsList = new ArrayList<String>();
    for ( NominationPayoutFinalLevelBean nomPromoLevelBean : getNominationPayoutFinalLevelList() )
    {
      if ( this.approverTypeCustom && StringUtils.isBlank( nomPromoLevelBean.getLevelLabel() ) )
      {
        errors.add( "nomPromoLevelBean.getLevelLabel()",
                    new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "promotion.awards.LEVEL_LABEL" ) ) );
      }
      else
      {
        if ( !StringUtils.isBlank( nomPromoLevelBean.getLevelLabel() ) )
        {
          levelLabels.add( nomPromoLevelBean.getLevelLabel().toLowerCase() );
          levelLabelsList.add( nomPromoLevelBean.getLevelLabel().toLowerCase() );
        }
      }

      if ( StringUtils.isBlank( nomPromoLevelBean.getAwardsType() ) )
      {
        errors.add( "nomPromoLevelBean.getAwardsType()", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "promotion.awards.TYPE" ) ) );
      }
      else if ( nomPromoLevelBean.getAwardsType().equals( PromotionAwardsType.POINTS ) || nomPromoLevelBean.getAwardsType().equals( PromotionAwardsType.CASH ) )
      {
        if ( nomPromoLevelBean.getAwardAmountTypeFixed().equals( FIXED ) )
        {
          // Cannot choose payout final level, custom approver level 1
          // points/cash, and fixed payout type
          if ( firstLevelAward )
          {
            errors.add( "awardAmountTypeFixed", new ActionMessage( "promotion.awards.errors.PAYOUT_TYPE_NOT_FIXED" ) );
          }

          if ( nomPromoLevelBean.getFixedAmount() == null || nomPromoLevelBean.getFixedAmount().length() == 0 )
          {
            errors.add( "nomPromoLevelBean.getAwardAmountTypeFixed()",
                        new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "promotion.awards.AMOUNT" ) ) );
          }
          else
          {
            try
            {
              // make sure the data is actually valid
              if ( nomPromoLevelBean.getAwardsType().equals( PromotionAwardsType.POINTS ) )
              {
                Pattern pattern = Pattern.compile( "\\." );
                Matcher matcher = pattern.matcher( nomPromoLevelBean.getFixedAmount() );
                if ( matcher.find() )
                {
                  errors.add( "", new ActionMessage( "promotion.awards.DECIMAL_POINTS" ) );
                  break;
                }
              }
              BigDecimal fAmt = StringUtil.convertStringToDecimal( nomPromoLevelBean.getFixedAmount() );
              if ( fAmt != null )
              {

                if ( fAmt.signum() < 0 )
                {
                  errors.add( "nomPromoLevelBean.getAwardAmountTypeFixed(",
                              new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "promotion.awards.AMOUNT" ) ) );
                }
                else if ( fAmt.signum() == 0 )
                {
                  errors.add( "nomPromoLevelBean.getAwardAmountTypeFixed(",
                              new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_NON_ZERO_POSITIVE_INTEGER, CmsResourceBundle.getCmsBundle().getString( "promotion.awards.AMOUNT" ) ) );
                }
              }
            }
            catch( NumberFormatException e )
            {
              errors.add( "awardAmountTypeFixed", new ActionMessage( "promotion.awards.errors.FIXED_AMOUNT" ) );
            }
          }
        }
        else if ( nomPromoLevelBean.getAwardAmountTypeFixed().equals( RANGE ) )
        {
          try
          {
            // required field validation
            if ( StringUtil.isEmpty( nomPromoLevelBean.getRangeAmountMin() ) || StringUtil.isEmpty( nomPromoLevelBean.getRangeAmountMax() ) )
            {
              errors.add( "nomPromoLevelBean.getAwardAmountTypeFixed()", new ActionMessage( "promotion.awards.errors.AMOUNT_RANGES_REQ" ) );
            }
            else
            {
              if ( nomPromoLevelBean.getAwardsType().equals( PromotionAwardsType.POINTS ) )
              {
                Pattern pattern = Pattern.compile( "\\." );
                Matcher matcher1 = pattern.matcher( nomPromoLevelBean.getRangeAmountMin() );
                Matcher matcher2 = pattern.matcher( nomPromoLevelBean.getRangeAmountMax() );
                if ( matcher1.find() || matcher2.find() )
                {
                  errors.add( "", new ActionMessage( "promotion.awards.DECIMAL_POINTS" ) );
                  break;
                }
              }
              // make sure the data is actually valid
              BigDecimal rangeAmountMinValue = StringUtil.convertStringToDecimal( nomPromoLevelBean.getRangeAmountMin() );
              BigDecimal rangeAmountMaxValue = StringUtil.convertStringToDecimal( nomPromoLevelBean.getRangeAmountMax() );

              // If the min or max value is less than or equal
              // to zero, then add the error
              if ( rangeAmountMinValue.signum() <= 0 || rangeAmountMaxValue.signum() <= 0 )
              {
                errors.add( "nomPromoLevelBean.getAwardAmountTypeFixed()", new ActionMessage( "promotion.awards.errors.AMOUNT_RANGES" ) );
              }

              // If the min value is greater than or equals to
              // the max value, then add the error
              if ( rangeAmountMinValue.compareTo( rangeAmountMaxValue ) > 0 )
              {
                errors.add( "nomPromoLevelBean.getAwardAmountTypeFixed()", new ActionMessage( "promotion.awards.errors.AMOUNT_RANGES" ) );
              }
            }
          }
          catch( NumberFormatException e )
          {
            errors.add( "nomPromoLevelBean.getAwardAmountTypeFixed()", new ActionMessage( "promotion.awards.errors.AMOUNT_RANGES" ) );
          }
        }
        else
        {
          if ( nomPromoLevelBean.getFinalLevelCalculatorId() == null || nomPromoLevelBean.getFinalLevelCalculatorId().longValue() == 0 )
          {
            errors.add( "nomPromoLevelBean.getAwardAmountTypeFixed()", new ActionMessage( "promotion.awards.errors.CALCULATOR_NOT_SELECTED" ) );
          }
          else
          {
            calcIds.add( nomPromoLevelBean.getFinalLevelCalculatorId() );
            if ( calcIds.size() > 1 )
            {
              errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "promotion.awards.errors.CALCULATOR_MUST_SAME" ) );
            }
          }
        }
      }
      else if ( nomPromoLevelBean.getAwardsType().equals( PromotionAwardsType.OTHER ) )
      {
        if ( StringUtils.isBlank( nomPromoLevelBean.getPayoutDescription() ) )
        {
          errors.add( "payoutDescription", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "promotion.basics.PAYOUT_DESCRIPTION" ) ) );
        }
        if ( StringUtils.isBlank( nomPromoLevelBean.getPayoutValue() ) )
        {
          errors.add( "payoutValue", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "promotion.basics.PAYOUT_VALUE" ) ) );
        }
        if ( StringUtils.isBlank( nomPromoLevelBean.getPayoutCurrency() ) )
        {
          errors.add( "payoutCurrency", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "promotion.basics.PAYOUT_CURRENCY" ) ) );
        }
        if ( !StringUtils.isBlank( nomPromoLevelBean.getQuantity() ) && Integer.parseInt( nomPromoLevelBean.getQuantity() ) < 0 )
        {
          errors.add( "nomPromoLevelBean.getAwardAmountTypeFixed(",
                      new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_NON_NEGATIVE_INTEGER, CmsResourceBundle.getCmsBundle().getString( "promotion.basics.QUANTITY" ) ) );
        }
        if ( firstLevelAward )
        {
          errors.add( "nomPromoLevelBean.awardsType", new ActionMessage( "promotion.awards.errors.AWARD_TYPE_NOT_OTHER" ) );
        }
      }
    }
    if ( !levelLabels.isEmpty() && levelLabels.size() < levelLabelsList.size() )
    {
      errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "promotion.awards.errors.LABEL_NOT_UNIQUE" ) );
    }
    levelLabels.clear();
    calcIds.clear();

    // If custom approver level 1 is award, recommended award must be true
    if ( firstLevelAward && !nominatorRecommendedAward )
    {
      errors.add( "nominatorRecommendedAward", new ActionMessage( "promotion.awards.errors.RECOMMENDED_AWARD_TRUE" ) );
    }
    if ( approverTypeCustom && !firstLevelAward && !finalLevelAward && nominatorRecommendedAward )
    {
      errors.add( "nominatorRecommendedAward", new ActionMessage( "promotion.awards.errors.RECOMMENDED_AWARD_FALSE" ) );
    }
  }

  private void validateTimPeriods( ActionErrors errors )
  {
    if ( this.nominationTimePeriodVBList != null && this.nominationTimePeriodVBList.size() > 0 )
    {
      Set<String> timePeriodNames = new HashSet<String>();

      for ( NominationTimePeriodValueBean timePeriodVB : this.nominationTimePeriodVBList )
      {
        if ( timePeriodVB != null )
        {
          if ( StringUtils.isBlank( timePeriodVB.getTimePeriodName() ) )
          {
            errors.add( ActionMessages.GLOBAL_MESSAGE, new ActionMessage( "promotion.awards.errors.TIME_PERIOD_NAME_REQUIRED" ) );
          }
          else
          {
            if ( !timePeriodNames.add( timePeriodVB.getTimePeriodName().toLowerCase() ) )
            {
              errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "promotion.awards.errors.TIME_PERIOD_NAME_NOT_UNIQUE" ) );
            }
          }

          Date startDateLocal = null;
          Date endDateLocal = null;
          if ( timePeriodVB.getTimePeriodStartDate() == null || timePeriodVB.getTimePeriodName().isEmpty() )
          {
            errors.add( ActionMessages.GLOBAL_MESSAGE, new ActionMessage( "promotion.awards.errors.TIME_PERIOD_START_DATE_REQUIRED" ) );
          }
          else
          {
            startDateLocal = DateUtils.toDate( timePeriodVB.getTimePeriodStartDate() );
            if ( startDateLocal == null )
            {
              errors.add( ActionMessages.GLOBAL_MESSAGE, new ActionMessage( "promotion.awards.errors.TIME_PERIOD_START_DATE_INVALID" ) );
            }
            else if ( this.promotionStartDate != null && this.promotionStartDate.length() > 0 && this.promotionEndDate != null && this.promotionEndDate.length() > 0
                && ( startDateLocal.before( DateUtils.toDate( this.promotionStartDate ) ) || startDateLocal.after( DateUtils.toDate( this.promotionEndDate ) ) ) )
            {
              errors.add( ActionMessages.GLOBAL_MESSAGE, new ActionMessage( "promotion.awards.errors.TIME_PERIOD_START_DATE_PROMOTION" ) );
            }
            else if ( this.promotionStartDate != null && !this.promotionStartDate.isEmpty() && startDateLocal.before( DateUtils.toDate( this.promotionStartDate ) ) )
            {
              errors.add( ActionMessages.GLOBAL_MESSAGE, new ActionMessage( "promotion.awards.errors.START_DATE_BEFORE_PROMOTION_DATE" ) );
            }
          }

          if ( timePeriodVB.getTimePeriodEndDate() == null || timePeriodVB.getTimePeriodEndDate().isEmpty() )
          {
            timePeriodVB.setTimePeriodEndDateEditable( true );
            errors.add( ActionMessages.GLOBAL_MESSAGE, new ActionMessage( "promotion.awards.errors.TIME_PERIOD_END_DATE_REQUIRED" ) );
          }
          else if ( timePeriodVB.getTimePeriodEndDate() != null || !timePeriodVB.getTimePeriodEndDate().isEmpty() )
          {
            endDateLocal = DateUtils.toDate( timePeriodVB.getTimePeriodEndDate() );
            if ( endDateLocal == null )
            {
              errors.add( ActionMessages.GLOBAL_MESSAGE, new ActionMessage( "promotion.awards.errors.TIME_PERIOD_END_DATE_INVALID" ) );
            }
            else if ( endDateLocal.before( DateUtils.toStartDate( DateUtils.getCurrentDate() ) ) && !live )
            {
              // The date is before current date
              errors.add( ActionMessages.GLOBAL_MESSAGE, new ActionMessage( "promotion.awards.errors.END_DATE_BEFORE_CURRENT_DATE" ) );
            }
            else if ( this.promotionEndDate != null && !this.promotionEndDate.isEmpty() && endDateLocal.after( DateUtils.toDate( this.promotionEndDate ) ) )
            {
              errors.add( ActionMessages.GLOBAL_MESSAGE, new ActionMessage( "promotion.awards.errors.END_DATE_AFTER_PROMOTION_DATE" ) );
            }

            // The start date must be earlier than or equal to the end date.
            if ( startDateLocal != null && endDateLocal != null && endDateLocal.before( startDateLocal ) )
            {
              timePeriodVB.setTimePeriodEndDateEditable( true );
              errors.add( "endDate", new ActionMessage( "system.errors.END_BEFORE_START_DATE" ) );
            }

            if ( this.promotionStartDate != null && this.promotionStartDate.length() > 0 && this.promotionEndDate != null && this.promotionEndDate.length() > 0
                && ( endDateLocal.before( DateUtils.toDate( this.promotionStartDate ) ) || endDateLocal.after( DateUtils.toDate( this.promotionEndDate ) ) ) )
            {
              timePeriodVB.setTimePeriodEndDateEditable( true );
              errors.add( ActionMessages.GLOBAL_MESSAGE, new ActionMessage( "promotion.awards.errors.TIME_PERIOD_END_DATE_PROMOTION" ) );
            }
          }
          if ( startDateLocal != null && endDateLocal != null && startDateLocal.compareTo( endDateLocal ) == 0 )
          {
            errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "promotion.awards.errors.TIME_PERIOD_START_END_DATE_EQUAL" ) );
          }
          if ( timePeriodVB.getMaxNominationsAllowed() != null && !timePeriodVB.getMaxNominationsAllowed().isEmpty() && timePeriodVB.getMaxNominationsAllowed().equals( "0" ) )
          {
            errors.add( ActionMessages.GLOBAL_MESSAGE, new ActionMessage( "promotion.awards.errors.MAX_NOMINATIONS_ZERO" ) );
          }
          if ( timePeriodVB.getMaxSubmissionAllowed() != null && !timePeriodVB.getMaxSubmissionAllowed().isEmpty() && timePeriodVB.getMaxSubmissionAllowed().equals( "0" ) )
          {
            errors.add( ActionMessages.GLOBAL_MESSAGE, new ActionMessage( "promotion.awards.errors.MAX_SUBMISSIONS_ZERO" ) );
          }
          if ( timePeriodVB.getMaxWinsllowed() != null && !timePeriodVB.getMaxWinsllowed().isEmpty() && timePeriodVB.getMaxWinsllowed().equals( "0" ) )
          {
            errors.add( ActionMessages.GLOBAL_MESSAGE, new ActionMessage( "promotion.awards.errors.MAX_WINS_ZERO" ) );
          }
        }
        if ( errors.isEmpty() )
        {
          if ( validateTimePeriodDateOverlap() )
          {
            errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "promotion.awards.errors.TIME_PERIOD_DATE_OVERLAP" ) );
          }
          if ( validateTimePeriodNextDay() )
          {
            errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "promotion.awards.errors.START_DATE_NON_NEXT_DAY" ) );
          }
          if ( sameTimePeriodStartDateEndDate() )
          {
            errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "promotion.awards.errors.TIME_PERIOD_START_END_DATE_EQUAL" ) );
          }
        }
        timePeriodVB.setTimePeriodStartDateEditable( true );
        timePeriodVB.setTimePeriodEndDateEditable( true );
      }
      timePeriodNames.clear();
    }
    else
    {
      errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "promotion.awards.errors.TIME_PERIODS_REQUIRED" ) );
    }
  }

  public ActionErrors validateTimePeriod( ActionErrors errors )
  {
    // Not breaking the loop on first error, as to find all error.
    if ( this.getBudgetSegmentVBListSize() > 0 )
    {
      BudgetSegmentValueBean lastBudgetSegmentValueBean = budgetSegmentVBList.get( budgetSegmentVBList.size() - 1 );
      int index = 0;
      for ( Iterator<BudgetSegmentValueBean> budgetSegmentIter = budgetSegmentVBList.iterator(); budgetSegmentIter.hasNext(); )
      {
        boolean isLastSegmentEndDate = budgetSegmentVBList.size() - 1 == index ? true : false;
        BudgetSegmentValueBean budgetSegmentVB = (BudgetSegmentValueBean)budgetSegmentIter.next();

        errors = validateSegmentName( budgetSegmentVB, errors, index );
        errors = validateSegmentStartDate( budgetSegmentVB, budgetMasterStartDate, errors, index );
        errors = validateSegmentEndDate( budgetSegmentVB, budgetMasterEndDate, errors, index, isLastSegmentEndDate );
        if ( this.isBudgetSweepEnabled() && this.awardsType.equals( PromotionAwardsType.POINTS ) )
        {
          errors = validateSegmentSweepDate( budgetSegmentVB, errors, index );
        }
        if ( budgetSegmentVB.getStartDate() != null && budgetSegmentVB.getEndDate() != null && budgetSegmentVB.getEndDate().before( budgetSegmentVB.getStartDate() ) )
        {
          errors.add( ActionErrors.GLOBAL_MESSAGE,
                      new ActionMessage( "admin.budgetmaster.details.SEGMENT_END_DATE_LESS", budgetSegmentVB.getEndDateStr(), budgetSegmentVB.getStartDateStr(), budgetSegmentVB.getSegmentName() ) );
        }
        index++;
      } // end for
      errors = validateSegmentEndDate( errors, DateUtils.toDate( budgetMasterEndDate ), lastBudgetSegmentValueBean );
      // all required field must be filled in before doing overlap check
      if ( errors.isEmpty() )
      {
        if ( validateDateOverlap( budgetSegmentVBList ) )
        {
          errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "admin.budgetmaster.details.SEGMENT_DATE_OVERLAP" ) );
        }
        if ( validateNextDay( budgetSegmentVBList ) )
        {
          errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "admin.budgetmaster.details.SEGMENT_START_DATE_NON_NEXT_DAY" ) );
        }
        if ( sameStartDateEndDate( budgetSegmentVBList ) )
        {
          errors.add( "budgetSegments", new ActionMessage( "admin.budgetmaster.details.TIME_PERIOD_START_END_DATE_EQUAL" ) );
        }
      }
    }
    return errors;
  }

  public ActionErrors validateCashTimePeriod( ActionErrors errors )
  {
    // Not breaking the loop on first error, as to find all error.
    if ( this.getCashBudgetSegmentVBListSize() > 0 )
    {
      BudgetSegmentValueBean lastBudgetSegmentValueBean = cashBudgetSegmentVBList.get( cashBudgetSegmentVBList.size() - 1 );
      int index = 0;
      for ( Iterator<BudgetSegmentValueBean> cashBudgetSegmentIter = cashBudgetSegmentVBList.iterator(); cashBudgetSegmentIter.hasNext(); )
      {
        boolean isLastSegmentEndDate = cashBudgetSegmentVBList.size() - 1 == index ? true : false;
        BudgetSegmentValueBean budgetSegmentVB = (BudgetSegmentValueBean)cashBudgetSegmentIter.next();

        errors = validateSegmentName( budgetSegmentVB, errors, index );
        errors = validateSegmentStartDate( budgetSegmentVB, cashBudgetMasterStartDate, errors, index );
        errors = validateSegmentEndDate( budgetSegmentVB, cashBudgetMasterEndDate, errors, index, isLastSegmentEndDate );
        if ( budgetSegmentVB.getStartDate() != null && budgetSegmentVB.getEndDate() != null && budgetSegmentVB.getEndDate().before( budgetSegmentVB.getStartDate() ) )
        {
          errors.add( ActionErrors.GLOBAL_MESSAGE,
                      new ActionMessage( "admin.budgetmaster.details.SEGMENT_END_DATE_LESS", budgetSegmentVB.getEndDateStr(), budgetSegmentVB.getStartDateStr(), budgetSegmentVB.getSegmentName() ) );
        }
        index++;
      } // end for
      errors = validateSegmentEndDate( errors, DateUtils.toDate( cashBudgetMasterEndDate ), lastBudgetSegmentValueBean );
      // all required field must be filled in before doing overlap check
      if ( errors.isEmpty() )
      {
        if ( validateDateOverlap( cashBudgetSegmentVBList ) )
        {
          errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "admin.budgetmaster.details.SEGMENT_DATE_OVERLAP" ) );
        }
        if ( validateNextDay( cashBudgetSegmentVBList ) )
        {
          errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "admin.budgetmaster.details.SEGMENT_START_DATE_NON_NEXT_DAY" ) );
        }
        if ( sameStartDateEndDate( cashBudgetSegmentVBList ) )
        {
          errors.add( "budgetSegments", new ActionMessage( "admin.budgetmaster.details.TIME_PERIOD_START_END_DATE_EQUAL" ) );
        }
      }
    }
    return errors;
  }

  private boolean validateTimePeriodDateOverlap()
  {
    int size = nominationTimePeriodVBList.size();

    for ( int i = 0; i < size - 1; i++ )
    {
      NominationTimePeriodValueBean currentTimePeriodVB = nominationTimePeriodVBList.get( i );
      NominationTimePeriodValueBean nextTimePeriodVB = nominationTimePeriodVBList.get( i + 1 );
      boolean overlap = checkOverlappingDates( currentTimePeriodVB.getStartDate(), currentTimePeriodVB.getEndDate(), nextTimePeriodVB.getStartDate(), nextTimePeriodVB.getEndDate() );

      if ( overlap )
      {
        return true;
      }

    } // end for
    return false;
  }

  private boolean validateTimePeriodNextDay()
  {
    int size = nominationTimePeriodVBList.size();

    for ( int i = 0; i < size - 1; i++ )
    {
      NominationTimePeriodValueBean currentTimePeriodVB = nominationTimePeriodVBList.get( i );
      NominationTimePeriodValueBean nextTimePeriodVB = nominationTimePeriodVBList.get( i + 1 );
      Date nextTimePeriodStartDateLocal = null;
      Date currentTimePeriodEndDateLocal = null;

      nextTimePeriodStartDateLocal = DateUtils.toDate( currentTimePeriodVB.getTimePeriodStartDate() );
      currentTimePeriodEndDateLocal = DateUtils.toDate( currentTimePeriodVB.getTimePeriodEndDate() );
      if ( nextTimePeriodStartDateLocal != null && currentTimePeriodEndDateLocal != null )
      {
        if ( !nextTimePeriodVB.getStartDate().equals( DateUtils.getNextDay( currentTimePeriodVB.getEndDate() ) ) )
        {
          return true;
        }
      }
    } // end for
    return false;
  }

  private boolean sameTimePeriodStartDateEndDate()
  {
    int size = nominationTimePeriodVBList.size();

    for ( int i = 0; i < size; i++ )
    {
      NominationTimePeriodValueBean timePeriodVB = nominationTimePeriodVBList.get( i );
      boolean startDateEndDatesAreEqual = checkStartDateEndDatesAreEqual( timePeriodVB.getStartDate(), timePeriodVB.getEndDate() );
      if ( startDateEndDatesAreEqual )
      {
        return true;
      }
    }
    return false;
  }

  private boolean validateDateOverlap( List<BudgetSegmentValueBean> budgetSegmentVBList )
  {
    int size = budgetSegmentVBList.size();

    for ( int i = 0; i < size - 1; i++ )
    {
      BudgetSegmentValueBean currentBudgetSegmentVB = budgetSegmentVBList.get( i );
      BudgetSegmentValueBean nextBudgetSegmentVB = budgetSegmentVBList.get( i + 1 );
      boolean overlap = checkOverlappingDates( currentBudgetSegmentVB.getStartDate(), currentBudgetSegmentVB.getEndDate(), nextBudgetSegmentVB.getStartDate(), nextBudgetSegmentVB.getEndDate() );

      if ( overlap )
      {
        return true;
      }

    } // end for
    return false;
  }

  private boolean validateNextDay( List<BudgetSegmentValueBean> budgetSegmentVBList )
  {
    int size = budgetSegmentVBList.size();

    for ( int i = 0; i < size - 1; i++ )
    {
      BudgetSegmentValueBean currentBudgetSegmentVB = budgetSegmentVBList.get( i );
      BudgetSegmentValueBean nextBudgetSegmentVB = budgetSegmentVBList.get( i + 1 );
      Date nextSegmentStartDateLocal = null;
      Date currentSegmentEndDateLocal = null;

      nextSegmentStartDateLocal = DateUtils.toDate( nextBudgetSegmentVB.getStartDateStr() );
      currentSegmentEndDateLocal = DateUtils.toDate( currentBudgetSegmentVB.getEndDateStr() );
      if ( nextSegmentStartDateLocal != null && currentSegmentEndDateLocal != null )
      {
        if ( !nextBudgetSegmentVB.getStartDate().equals( DateUtils.getNextDay( currentBudgetSegmentVB.getEndDate() ) ) )
        {
          return true;
        }
      }
    } // end for
    return false;
  }

  private boolean sameStartDateEndDate( List<BudgetSegmentValueBean> budgetSegmentVBList )
  {
    int size = budgetSegmentVBList.size();

    for ( int i = 0; i < size; i++ )
    {
      BudgetSegmentValueBean budgetSegmentVB = budgetSegmentVBList.get( i );
      boolean startDateEndDatesAreEqual = checkStartDateEndDatesAreEqual( budgetSegmentVB.getStartDate(), budgetSegmentVB.getEndDate() );
      if ( startDateEndDatesAreEqual )
      {
        return true;
      }
    }
    return false;
  }

  private boolean checkStartDateEndDatesAreEqual( Date startDate, Date endDate )
  {
    // end date must be greater than start date
    if ( startDate != null && endDate != null && startDate.compareTo( endDate ) == 0 )
    {
      return true;
    }

    return false;
  }

  private ActionErrors validateSegmentName( BudgetSegmentValueBean budgetSegmentVB, ActionErrors actionErrors, int index )
  {
    String segmentName = budgetSegmentVB.getSegmentName();
    if ( StringUtils.isEmpty( segmentName ) )
    {
      actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "admin.budgetmaster.errors.SEGMENT_NAME_MISSING", String.valueOf( index + 1 ) ) );
    }
    else
    {
      if ( segmentName != null && segmentName.length() > 50 )
      {
        actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "admin.budgetmaster.details.SEGMENT_NAME_TOO_LONG", segmentName ) );
      }
    }
    return actionErrors;
  }

  private ActionErrors validateSegmentStartDate( BudgetSegmentValueBean budgetSegmentVB, String budgetMasterStartDate, ActionErrors actionErrors, int index )
  {

    Date segmentStartDateLocal = null;
    if ( budgetSegmentVB.getStartDateStr() == null || budgetSegmentVB.getStartDateStr().length() == 0 )
    {
      actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "admin.budgetmaster.errors.SEGMENT_START_DATE_MISSING", String.valueOf( index + 1 ) ) );
    }
    else
    {
      // The date string must represent a date.
      segmentStartDateLocal = DateUtils.toDate( budgetSegmentVB.getStartDateStr() );
      if ( segmentStartDateLocal == null )
      {
        actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "admin.budgetmaster.errors.SEGMENT_START_DATE_MISSING", String.valueOf( index + 1 ) ) );
      }
      else
      {
        if ( budgetMasterStartDate != null && budgetMasterStartDate.length() > 0 && DateUtils.toDate( budgetMasterStartDate ) != null
            && segmentStartDateLocal.before( DateUtils.toDate( budgetMasterStartDate ) ) )
        {
          actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "admin.budgetmaster.details.SEGMENT_START_DATE_LESS", budgetSegmentVB.getStartDateStr(), budgetMasterStartDate ) );
        }
      }
    }
    return actionErrors;
  }

  private ActionErrors validateSegmentEndDate( BudgetSegmentValueBean budgetSegmentVB, String budgetMasterEndDate, ActionErrors actionErrors, int index, boolean isLastSegmentEndDate )
  {
    Date segmentEndDateLocal = null;
    if ( !isLastSegmentEndDate )
    {
      if ( budgetSegmentVB.getEndDateStr() == null || budgetSegmentVB.getEndDateStr().length() == 0 )
      {
        actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "admin.budgetmaster.errors.SEGMENT_END_DATE_MISSING", String.valueOf( index + 1 ) ) );
      }
    }
    // The date string must represent a date.
    if ( budgetSegmentVB.getEndDateStr() != null && budgetSegmentVB.getEndDateStr().length() > 0 )
    {
      segmentEndDateLocal = DateUtils.toDate( budgetSegmentVB.getEndDateStr() );
      if ( segmentEndDateLocal == null )
      {
        actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "admin.budgetmaster.errors.SEGMENT_END_DATE_MISSING", String.valueOf( index + 1 ) ) );
      }
      else
      {
        if ( budgetMasterEndDate != null && budgetMasterEndDate.length() > 0 && DateUtils.toDate( budgetMasterEndDate ) != null
            && segmentEndDateLocal.after( DateUtils.toDate( budgetMasterEndDate ) ) )
        {
          actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "admin.budgetmaster.details.SEGMENT_END_DATE_GREATER", budgetSegmentVB.getEndDateStr(), budgetMasterEndDate ) );
        }
      }
    }
    return actionErrors;
  }

  private ActionErrors validateSegmentSweepDate( BudgetSegmentValueBean budgetSegmentVB, ActionErrors actionErrors, int index )
  {

    Date segmentSweepDateLocal = null;
    if ( budgetSegmentVB.getBudgetSweepDate() == null )
    {
      actionErrors.add( ActionErrors.GLOBAL_MESSAGE,
                        new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "promotion.awards", "SWEEP_BUDGET_DATE" ), index ) );
    }
    if ( budgetSegmentVB.getBudgetSweepDate().length() == 0 )
    {
      actionErrors.add( ActionErrors.GLOBAL_MESSAGE,
                        new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "promotion.awards", "SWEEP_BUDGET_DATE" ), index ) );
    }
    else
    {
      // The date string must represent a date.
      segmentSweepDateLocal = DateUtils.toDate( budgetSegmentVB.getBudgetSweepDate() );
      if ( segmentSweepDateLocal == null )
      {
        actionErrors
            .add( ActionErrors.GLOBAL_MESSAGE,
                  new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_DATE, CmsResourceBundle.getCmsBundle().getString( "promotion.awards", "SWEEP_BUDGET_DATE" ), String.valueOf( index ) ) );
      }
    }
    // sweepDate must be greater than budgetMasterStartDate
    if ( budgetSegmentVB.getBudgetSweepDate() != null && ! ( budgetSegmentVB.getBudgetSweepDate().length() == 0 ) && !budgetMasterStartDate.isEmpty() )
    {
      if ( DateUtils.toDate( budgetSegmentVB.getBudgetSweepDate() ).compareTo( DateUtils.toDate( budgetMasterStartDate ) ) < 0 )
      {
        actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "admin.budgetmaster.details.GREATER_BUDGET_SWEEP_DATE", budgetSegmentVB.getBudgetSweepDate(), budgetMasterStartDate ) );
      }
    }
    return actionErrors;
  }

  private boolean checkOverlappingDates( Date startDate, Date endDate, Date newStartDate, Date newEndDate )
  {
    // new start date must be greater than old end date
    if ( newStartDate != null && endDate != null && newStartDate.compareTo( endDate ) <= 0 )
    {
      return true;
    }
    // new start date must be greater than old start date
    if ( newStartDate != null && startDate != null && newStartDate.compareTo( startDate ) <= 0 )
    {
      return true;
    }
    // new end date must be greater than the old start date
    if ( newEndDate != null && startDate != null && newEndDate.compareTo( startDate ) <= 0 )
    {
      return true;
    }
    // new end date must be greater than the old end date
    if ( newEndDate != null && endDate != null && newEndDate.compareTo( endDate ) <= 0 )
    {
      return true;
    }

    return false;
  }

  public ActionErrors validateTimePeriodBudgetAmount( ActionErrors errors, List<BudgetSegmentValueBean> vbList, int vbListSize )
  {
    int index = 0;
    if ( vbListSize > 0 )
    {
      for ( Iterator<BudgetSegmentValueBean> budgetSegmentVBListIter = vbList.iterator(); budgetSegmentVBListIter.hasNext(); )
      {
        index = index + 1;
        BudgetSegmentValueBean budgetSegmentValueBean = (BudgetSegmentValueBean)budgetSegmentVBListIter.next();
        String budgetOriginalValue = budgetSegmentValueBean.getOriginalValue();
        if ( StringUtils.isBlank( budgetOriginalValue ) )
        {
          errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "admin.budgetmaster.errors.SEGMENT_AMOUNT_MISSING", String.valueOf( index ) ) );
        }
        else
        {
          try
          {
            // make sure the data is actually valid
            int centralBudgetAmountInt = Integer.parseInt( budgetOriginalValue );

            // make sure the amount is greater than zero
            if ( centralBudgetAmountInt <= 0 )
            {
              errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "promotion.awards.errors.CENTRAL_BUDGET_AMOUNT_LESS_THAN_ONE" ) );
            }
          }
          catch( NumberFormatException e )
          {
            errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "promotion.awards.errors.NOT_VALID_NUMBER" ) );
          }
        }

      }
    }
    return errors;
  }

  /**
   * @param request
   * @param errors
   */
  private void validateNonMerchandiseCalculator( ActionErrors errors )
  {
    // Check for approver type...
    if ( calculatorId == null || calculatorId.longValue() == 0 )
    {
      errors.add( "awardAmountTypeFixed", new ActionMessage( "promotion.awards.errors.CALCULATOR_NOT_SELECTED" ) );
    }
    else if ( scoreBy == null || scoreBy.length() == 0 )
    {
      errors.add( "awardAmountTypeFixed", new ActionMessage( "promotion.awards.errors.SCORE_NOT_SELECTED" ) );
    }
    // approver check.
    else
    {
      if ( scoreBy.equals( ScoreBy.APPROVER ) && ( !approvalType.equals( "" ) || approvalType.length() != 0 ) )
      {
        if ( !approvalType.equals( ApprovalType.MANUAL ) )
        {
          errors.add( "awardAmountTypeFixed", new ActionMessage( "promotion.approvals.errors.APPROVALTYPE_NOT_MANUAL" ) );
        }

      }
    }
  }

  /**
   * @param errors
   */
  private void validateRangeAwardType( ActionErrors errors )
  {
    try
    {
      // required field validation
      if ( StringUtil.isEmpty( rangeAmountMin ) || StringUtil.isEmpty( rangeAmountMax ) )
      {
        errors.add( "awardAmountTypeFixed", new ActionMessage( "promotion.awards.errors.AMOUNT_RANGES_REQ" ) );
      }
      else
      {
        // make sure the data is actually valid
        long rangeAmountMinValue = Long.parseLong( rangeAmountMin );
        long rangeAmountMaxValue = Long.parseLong( rangeAmountMax );

        // If the min or max value is less than or equal
        // to zero, then add the error
        if ( !includePurl && rangeAmountMinValue <= 0 || rangeAmountMaxValue <= 0 )
        {
          errors.add( "awardAmountTypeFixed", new ActionMessage( "promotion.awards.errors.AMOUNT_RANGES" ) );
        }

        // If the min value is greater than or equals to
        // the max value, then add the error
        if ( rangeAmountMinValue >= rangeAmountMaxValue )
        {
          errors.add( "awardAmountTypeFixed", new ActionMessage( "promotion.awards.errors.AMOUNT_RANGES" ) );
        }
      }
    }
    catch( NumberFormatException e )
    {
      errors.add( "awardAmountTypeFixed", new ActionMessage( "promotion.awards.errors.AMOUNT_RANGES" ) );
    }
  }

  private void validateAllowMinNegativeAmtForRangeAward( ActionErrors errors )
  {
    try
    {
      // make sure the data is actually valid
      long rangeAmountMinValue = Long.parseLong( rangeAmountMin );
      long rangeAmountMaxValue = Long.parseLong( rangeAmountMax );

      // If the max value is less than or equal
      // to zero, then add the error and allow negative award amount for min amount
      if ( rangeAmountMaxValue <= 0 )
      {
        errors.add( "awardAmountTypeFixed", new ActionMessage( "promotion.awards.errors.AMOUNT_RANGES" ) );
      }

      // If the min value is greater than or equals to
      // the max value, then add the error
      if ( rangeAmountMinValue >= rangeAmountMaxValue )
      {
        errors.add( "awardAmountTypeFixed", new ActionMessage( "promotion.awards.errors.AMOUNT_RANGES" ) );
      }
    }
    catch( NumberFormatException e )
    {
      errors.add( "awardAmountTypeFixed", new ActionMessage( "promotion.awards.errors.AMOUNT_RANGES" ) );
    }
  }

  /**
   * @param request
   * @param errors
   */
  private void validateFixedNonMerchandiseAwardType( ActionErrors errors )
  {
    // ***** Validate fixedAmount *****/
    // Make sure its not empty
    if ( fixedAmount == null || fixedAmount.length() == 0 )
    {
      errors.add( "awardAmountTypeFixed", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "promotion.awards.AMOUNT" ) ) );
    }
    else
    {
      try
      {
        // make sure the data is actually valid
        int fAmt = Integer.parseInt( fixedAmount );

        if ( fAmt < 0 )
        {
          errors.add( "awardAmountTypeFixed", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "promotion.awards.AMOUNT" ) ) );
        }

        // bug fix 64825 start
        // validate fixedAwardAmount must be greater than zero
        else if ( fAmt == 0 )
        {
          errors.add( "awardAmountTypeFixed",
                      new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_NON_ZERO_POSITIVE_INTEGER, CmsResourceBundle.getCmsBundle().getString( "promotion.awards.AMOUNT" ) ) );
        }
        // bug fix 64825 end
      }
      catch( NumberFormatException e )
      {
        errors.add( "awardAmountTypeFixed", new ActionMessage( "promotion.awards.errors.FIXED_AMOUNT" ) );
      }
    }
  }

  /**
   * @param errors
   */
  private void validateMerchandiseType( ActionErrors errors )
  {
    if ( awardStructure == null || awardStructure.trim().length() == 0 )
    {
      errors.add( "awardStructure", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "promotion.awards.AWARD_STRUCTURE" ) ) );
    }
    if ( awardStructure != null && awardStructure.equals( "level" ) && isUseRecognitionCalculator() )
    {
      if ( calculatorId == null || calculatorId.longValue() == 0 )
      {
        errors.add( "merchCalculator", new ActionMessage( "promotion.awards.errors.CALCULATOR_NOT_SELECTED" ) );
      }
      else if ( scoreBy == null || scoreBy.trim().length() == 0 )
      {
        errors.add( "merchCalculator", new ActionMessage( "promotion.awards.errors.SCORE_NOT_SELECTED" ) );
      }
      else
      {
        if ( scoreBy.equals( ScoreBy.APPROVER ) && ( !approvalType.equals( "" ) || approvalType.length() != 0 ) )
        {
          if ( !approvalType.equals( ApprovalType.MANUAL ) )
          {
            errors.add( "merchCalculator", new ActionMessage( "promotion.approvals.errors.APPROVALTYPE_NOT_MANUAL" ) );
          }

        }
      }
    }
  }

  public boolean isAwardsActive()
  {
    return awardsActive;
  }

  public void setAwardsActive( boolean active )
  {
    this.awardsActive = active;
  }

  public String getAwardAmountTypeFixed()
  {
    return awardAmountTypeFixed;
  }

  public void setAwardAmountTypeFixed( String awardAmountTypeFixed )
  {
    this.awardAmountTypeFixed = awardAmountTypeFixed;
  }

  public boolean isExpired()
  {
    return expired;
  }

  public void setExpired( boolean expired )
  {
    this.expired = expired;
  }

  public String getFixedAmount()
  {
    return fixedAmount;
  }

  public void setFixedAmount( String fixedAmount )
  {
    this.fixedAmount = fixedAmount;
  }

  public boolean isLive()
  {
    return live;
  }

  public void setLive( boolean live )
  {
    this.live = live;
  }

  public Long getPromotionId()
  {
    return promotionId;
  }

  public void setPromotionId( Long promotionId )
  {
    this.promotionId = promotionId;
  }

  public String getPromotionStatus()
  {
    return promotionStatus;
  }

  public void setPromotionStatus( String promotionStatus )
  {
    this.promotionStatus = promotionStatus;
  }

  public String getPromotionName()
  {
    return promotionName;
  }

  public void setPromotionName( String promotionName )
  {
    this.promotionName = promotionName;
  }

  public String getPromotionTypeName()
  {
    return promotionTypeName;
  }

  public void setPromotionTypeName( String promotionTypeName )
  {
    this.promotionTypeName = promotionTypeName;
  }

  public String getRangeAmountMax()
  {
    return rangeAmountMax;
  }

  public void setRangeAmountMax( String rangeAmountMax )
  {
    this.rangeAmountMax = rangeAmountMax;
  }

  public String getRangeAmountMin()
  {
    return rangeAmountMin;
  }

  public void setRangeAmountMin( String rangeAmountMin )
  {
    this.rangeAmountMin = rangeAmountMin;
  }

  public String getAwardsType()
  {
    return awardsType;
  }

  public void setAwardsType( String type )
  {
    this.awardsType = type;
  }

  public String getMethod()
  {
    return method;
  }

  public void setMethod( String method )
  {
    this.method = method;
  }

  public String getReturnActionUrl()
  {
    return returnActionUrl;
  }

  public void setReturnActionUrl( String returnActionUrl )
  {
    this.returnActionUrl = returnActionUrl;
  }

  public String getBudgetOption()
  {
    return budgetOption;
  }

  public void setBudgetOption( String budget )
  {
    this.budgetOption = budget;
  }

  public String getBudgetCapType()
  {
    return budgetCapType;
  }

  public void setBudgetCapType( String budgetCapType )
  {
    this.budgetCapType = budgetCapType;
  }

  public String getBudgetMasterName()
  {
    return budgetMasterName;
  }

  public void setBudgetMasterName( String budgetMasterName )
  {
    this.budgetMasterName = budgetMasterName;
  }

  public void setExistingBudgetType( String existingBudgetType )
  {
    this.existingBudgetType = existingBudgetType;
  }

  public String getExistingBudgetType()
  {
    return existingBudgetType;
  }

  public String getBudgetType()
  {
    return budgetType;
  }

  public void setBudgetType( String budgetType )
  {
    this.budgetType = budgetType;
  }

  public boolean isFileloadBudgetAmount()
  {
    return fileloadBudgetAmount;
  }

  public void setFileloadBudgetAmount( boolean fileloadBudgetAmount )
  {
    this.fileloadBudgetAmount = fileloadBudgetAmount;
  }

  public Long getBudgetMasterId()
  {
    return budgetMasterId;
  }

  public void setBudgetMasterId( Long budgetMasterId )
  {
    this.budgetMasterId = budgetMasterId;
  }

  public Long getBudgetApproverId()
  {
    return budgetApproverId;
  }

  public void setBudgetApproverId( Long budgetApproverId )
  {
    this.budgetApproverId = budgetApproverId;
  }

  public String getBudgetApproverName()
  {
    return budgetApproverName;
  }

  public void setBudgetApproverName( String budgetApproverName )
  {
    this.budgetApproverName = budgetApproverName;
  }

  public String getPromotionTypeCode()
  {
    return promotionTypeCode;
  }

  public void setPromotionTypeCode( String promotionTypeCode )
  {
    this.promotionTypeCode = promotionTypeCode;
  }

  public boolean isCreateNewBudgetMaster()
  {
    if ( !this.awardsActive )
    {
      return false;
    }

    return this.budgetOption.equals( BUDGET_NEW );
  }

  public boolean isCreateNewCashBudgetMaster()
  {
    if ( !this.awardsActive )
    {
      return false;
    }

    return this.cashBudgetOption.equals( BUDGET_NEW );
  }

  public boolean isUseExistingBudgetMaster()
  {
    return this.budgetOption.equals( BUDGET_EXISTING ) || this.budgetOption.equals( BUDGET_SWEEP_EXISTING );
  }

  public boolean isUseExistingCashBudgetMaster()
  {
    return this.cashBudgetOption.equals( BUDGET_EXISTING ) || this.cashBudgetOption.equals( BUDGET_SWEEP_EXISTING );
  }

  public BudgetMaster getNewBudgetMaster()
  {
    BudgetMaster budgetMaster = new BudgetMaster();
    budgetMaster.setBudgetName( this.budgetMasterName );
    budgetMaster.setBudgetType( BudgetType.lookup( this.budgetType ) );
    budgetMaster.setAwardType( BudgetMasterAwardType.lookup( BudgetMasterAwardType.POINTS ) );
    budgetMaster.setOverrideableType( BudgetOverrideableType.lookup( this.budgetCapType ) );
    budgetMaster.setActive( true );
    budgetMaster.setStartDate( DateUtils.toDate( this.budgetMasterStartDate ) );
    budgetMaster.setEndDate( DateUtils.toDate( this.budgetMasterEndDate ) );

    return budgetMaster;
  }

  public BudgetMaster getNewCashBudgetMaster()
  {
    BudgetMaster budgetMaster = new BudgetMaster();
    budgetMaster.setBudgetName( this.cashBudgetMasterName );
    budgetMaster.setBudgetType( BudgetType.lookup( this.cashBudgetType ) );
    budgetMaster.setAwardType( BudgetMasterAwardType.lookup( BudgetMasterAwardType.CASH ) );
    budgetMaster.setOverrideableType( BudgetOverrideableType.lookup( this.cashBudgetCapType ) );
    budgetMaster.setActive( true );
    budgetMaster.setStartDate( DateUtils.toDate( this.cashBudgetMasterStartDate ) );
    budgetMaster.setEndDate( DateUtils.toDate( this.cashBudgetMasterEndDate ) );

    return budgetMaster;
  }

  public String getAwardsTypeDesc()
  {
    return awardsTypeDesc;
  }

  public void setAwardsTypeDesc( String awardsTypeDesc )
  {
    this.awardsTypeDesc = awardsTypeDesc;
  }

  public String getPromotionIssuanceTypeCode()
  {
    return promotionIssuanceTypeCode;
  }

  public void setPromotionIssuanceTypeCode( String promotionIssuanceTypeCode )
  {
    this.promotionIssuanceTypeCode = promotionIssuanceTypeCode;
  }

  /**
   * @return value of nominationCumulative property
   */
  public boolean isNominationCumulative()
  {
    return nominationCumulative;
  }

  /**
   * @param nominationCumulative value for nominationCumulative property
   */
  public void setNominationCumulative( boolean nominationCumulative )
  {
    this.nominationCumulative = nominationCumulative;
  }

  public String getScoreBy()
  {
    return scoreBy;
  }

  public void setScoreBy( String scoreBy )
  {
    this.scoreBy = scoreBy;
  }

  public String getFinalPayoutRule()
  {
    return finalPayoutRule;
  }

  public void setFinalPayoutRule( String finalPayoutRule )
  {
    this.finalPayoutRule = finalPayoutRule;
  }

  public Long getCalculatorId()
  {
    return calculatorId;
  }

  public void setCalculatorId( Long calculatorId )
  {
    this.calculatorId = calculatorId;
  }

  public boolean isUseRecognitionCalculator()
  {
    return useRecognitionCalculator;
  }

  public void setUseRecognitionCalculator( boolean useRecognitionCalculator )
  {
    this.useRecognitionCalculator = useRecognitionCalculator;
  }

  public String getApprovalType()
  {
    return approvalType;
  }

  public void setApprovalType( String approvalType )
  {
    this.approvalType = approvalType;
  }

  public boolean isNoNotification()
  {
    return noNotification;
  }

  public void setNoNotification( boolean noNotification )
  {
    this.noNotification = noNotification;
  }

  public String getAwardStructure()
  {
    return awardStructure;
  }

  public void setAwardStructure( String awardStructure )
  {
    this.awardStructure = awardStructure;
  }

  public List getCountryList()
  {
    return countryList;
  }

  /**
   * @param countryList the countryList to set
   */
  public void setCountryList( List countryList )
  {
    this.countryList = countryList;
  }

  /**
   * @return the apqConversion
   */
  public boolean isApqConversion()
  {
    return apqConversion;
  }

  /**
   * @param apqConversion the apqConversion to set
   */
  public void setApqConversion( boolean apqConversion )
  {
    this.apqConversion = apqConversion;
  }

  public boolean isNumOfLevelsEqual()
  {
    return numOfLevelsEqual;
  }

  public void setNumOfLevelsEqual( boolean numOfLevelsEqual )
  {
    this.numOfLevelsEqual = numOfLevelsEqual;
  }

  public Long getNumberOfLevels()
  {
    return numberOfLevels;
  }

  public void setNumberOfLevels( Long numberOfLevels )
  {
    this.numberOfLevels = numberOfLevels;
  }

  public boolean isFeaturedAwardsEnabled()
  {
    return featuredAwardsEnabled;
  }

  public void setFeaturedAwardsEnabled( boolean featuredAwardsEnabled )
  {
    this.featuredAwardsEnabled = featuredAwardsEnabled;
  }

  public List getStdHomePageItemAsList()
  {
    if ( stdHomePageItemList == null )
    {
      Factory factory = new Factory()
      {
        public Object create()
        {
          return new HomePageItem();
        }
      };
      stdHomePageItemList = ListUtils.lazyList( new ArrayList(), factory );
    }
    return stdHomePageItemList;
  }

  public void setStdHomePageItemAsList( List homePageItemList )
  {
    this.stdHomePageItemList = homePageItemList;
  }

  public HomePageItem getStdHomePageItemList( int index )
  {
    return (HomePageItem)getStdHomePageItemAsList().get( index );
  }

  public HomePageItem getHomePageItem()
  {
    return homePageItem;
  }

  public void setHomePageItem( HomePageItem homePageItem )
  {
    this.homePageItem = homePageItem;
  }

  public String getCcProductId()
  {
    return ccProductId;
  }

  public void setCcProductId( String productId )
  {
    this.ccProductId = productId;
  }

  public String getStdProductId()
  {
    return stdProductId;
  }

  public void setStdProductId( String stdProductId )
  {
    this.stdProductId = stdProductId;
  }

  public boolean isBudgetSweepEnabled()
  {
    return budgetSweepEnabled;
  }

  public void setBudgetSweepEnabled( boolean budgetSweepEnabled )
  {
    this.budgetSweepEnabled = budgetSweepEnabled;
  }

  public String getPromotionEndDate()
  {
    return promotionEndDate;
  }

  public void setPromotionEndDate( String promotionEndDate )
  {
    this.promotionEndDate = promotionEndDate;
  }

  public String getPromotionStartDate()
  {
    return promotionStartDate;
  }

  public void setPromotionStartDate( String promotionStartDate )
  {
    this.promotionStartDate = promotionStartDate;
  }

  public boolean isShowInBudgetTracker()
  {
    return showInBudgetTracker;
  }

  public void setShowInBudgetTracker( boolean showInBudgetTracker )
  {
    this.showInBudgetTracker = showInBudgetTracker;
  }

  private HomePageItem toDomainHomePageItem()
  {
    return loadHomePageItem( getStdHomePageItemAsList(), stdProductId );
  }

  private HomePageItem loadHomePageItem( List items, String productId )
  {
    if ( productId == null )
    {
      return null;
    }

    HomePageItem item = null;
    for ( int i = 0; i < items.size(); i++ )
    {
      item = (HomePageItem)items.get( i );

      if ( item.getProductId().equals( productId ) )
      {
        return item;
      }
    }
    return null;
  }

  public BudgetSegment populateBudgetSegment( BudgetSegmentValueBean budgetSegmentVB )
  {
    BudgetSegment budgetSegment = new BudgetSegment();
    budgetSegment.setName( budgetSegmentVB.getSegmentName() );
    budgetSegment.setStartDate( DateUtils.toDate( budgetSegmentVB.getStartDateStr() ) );
    budgetSegment.setEndDate( DateUtils.toDate( budgetSegmentVB.getEndDateStr() ) );
    budgetSegment.setStatus( Boolean.TRUE ); // if delete functionality is added then need to add
                                             // logic
    return budgetSegment;
  }

  private List<BudgetSegmentValueBean> getEmptyValueList( int valueListCount )
  {
    List<BudgetSegmentValueBean> valueList = new ArrayList<BudgetSegmentValueBean>();

    for ( int i = 0; i < valueListCount; i++ )
    {
      BudgetSegmentValueBean budgetSegment = new BudgetSegmentValueBean();
      valueList.add( budgetSegment );
    }

    return valueList;
  }

  public void defaultEmptyBudgetSegment()
  {
    BudgetSegmentValueBean budgetSegment = new BudgetSegmentValueBean();
    budgetSegment.setSegmentName( CmsResourceBundle.getCmsBundle().getString( "promotion.awards.errors.DEFAULT_SEGMENT_NAME" ) );
    this.budgetSegmentVBList.add( budgetSegment );
  }

  public void defaultEmptyCashBudgetSegment()
  {
    BudgetSegmentValueBean budgetSegment = new BudgetSegmentValueBean();
    budgetSegment.setSegmentName( CmsResourceBundle.getCmsBundle().getString( "promotion.awards.errors.DEFAULT_SEGMENT_NAME" ) );
    this.cashBudgetSegmentVBList.add( budgetSegment );
  }

  public void addEmptyBudgetSegment()
  {
    BudgetSegmentValueBean budgetSegment = new BudgetSegmentValueBean();
    this.budgetSegmentVBList.add( budgetSegment );
  }

  public void addEmptyCashBudgetSegment()
  {
    BudgetSegmentValueBean budgetSegment = new BudgetSegmentValueBean();
    this.cashBudgetSegmentVBList.add( budgetSegment );
  }

  public int getBudgetSegmentVBListSize()
  {
    if ( this.budgetSegmentVBList != null )
    {
      return this.budgetSegmentVBList.size();
    }

    return 0;
  }

  public int getCashBudgetSegmentVBListSize()
  {
    if ( this.cashBudgetSegmentVBList != null )
    {
      return this.cashBudgetSegmentVBList.size();
    }

    return 0;
  }

  public int getNominationTimePeriodVBListSize()
  {
    if ( this.nominationTimePeriodVBList != null )
    {
      return this.nominationTimePeriodVBList.size();
    }

    return 0;
  }

  private List<NominationTimePeriodValueBean> getEmptyNominationTimePeriodValueList( int valueListCount )
  {
    List<NominationTimePeriodValueBean> valueList = new ArrayList<NominationTimePeriodValueBean>();

    for ( int i = 0; i < valueListCount; i++ )
    {
      NominationTimePeriodValueBean nominationTimePeriod = new NominationTimePeriodValueBean();
      valueList.add( nominationTimePeriod );
    }

    return valueList;
  }

  public boolean isTimePeriodStartDateEditable( String date )
  {
    boolean editable = false;
    if ( date != null && DateUtils.toDate( date ).after( DateUtils.toStartDate( DateUtils.getCurrentDate() ) ) )
    {
      editable = true;
    }
    return editable;
  }

  public void addFirstNominationTimePeriod()
  {
    NominationTimePeriodValueBean nominationTimePeriod = new NominationTimePeriodValueBean();
    nominationTimePeriod.setTimePeriodStartDateEditable( true );
    nominationTimePeriod.setTimePeriodEndDateEditable( true );
    this.nominationTimePeriodVBList.add( nominationTimePeriod );
  }

  public Long getBudgetSegmentId()
  {
    return budgetSegmentId;
  }

  public void setBudgetSegmentId( Long budgetSegmentId )
  {
    this.budgetSegmentId = budgetSegmentId;
  }

  public Long getHiddenBudgetMasterId()
  {
    return hiddenBudgetMasterId;
  }

  public void setHiddenBudgetMasterId( Long hiddenBudgetMasterId )
  {
    this.hiddenBudgetMasterId = hiddenBudgetMasterId;
  }

  public boolean isIncludePurl()
  {
    return includePurl;
  }

  public void setIncludePurl( boolean includePurl )
  {
    this.includePurl = includePurl;
  }

  public String getBudgetMasterStartDate()
  {
    return budgetMasterStartDate;
  }

  public String getBudgetMasterEndDate()
  {
    return budgetMasterEndDate;
  }

  public void setBudgetMasterStartDate( String budgetMasterStartDate )
  {
    this.budgetMasterStartDate = budgetMasterStartDate;
  }

  public void setBudgetMasterEndDate( String budgetMasterEndDate )
  {
    this.budgetMasterEndDate = budgetMasterEndDate;
  }

  public void setBudgetSegmentVBList( List<BudgetSegmentValueBean> budgetSegmentVBList )
  {
    this.budgetSegmentVBList = budgetSegmentVBList;
  }

  public List<BudgetSegmentValueBean> getBudgetSegmentVBList()
  {
    return budgetSegmentVBList;
  }

  private BudgetMasterService getBudgetMasterService()
  {
    return (BudgetMasterService)BeanLocator.getBean( BudgetMasterService.BEAN_NAME );
  }

  public boolean isPayoutEachLevel()
  {
    return payoutEachLevel;
  }

  public void setPayoutEachLevel( boolean payoutEachLevel )
  {
    this.payoutEachLevel = payoutEachLevel;
  }

  public boolean isPayoutFinalLevel()
  {
    return payoutFinalLevel;
  }

  public void setPayoutFinalLevel( boolean payoutFinalLevel )
  {
    this.payoutFinalLevel = payoutFinalLevel;
  }

  public boolean isTaxable()
  {
    return taxable;
  }

  public void setTaxable( boolean taxable )
  {
    this.taxable = taxable;
  }

  /**
   * This validation will check to make sure the last period end date is equal to the budget master end date in case they are entered.
   */
  private ActionErrors validateSegmentEndDate( ActionErrors actionErrors, Date budgetMasterEndDate, BudgetSegmentValueBean lastBudgetSegmentValueBean )
  {
    if ( budgetMasterEndDate != null && lastBudgetSegmentValueBean.getEndDate() != null && budgetMasterEndDate.compareTo( lastBudgetSegmentValueBean.getEndDate() ) != 0 )
    {
      actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "admin.budgetmaster.details.SEGMENT_END_DATE_NOT_EQUAL" ) );
    }
    return actionErrors;
  }

  public boolean isRequestMoreBudget()
  {
    return requestMoreBudget;
  }

  public void setRequestMoreBudget( boolean requestMoreBudget )
  {
    this.requestMoreBudget = requestMoreBudget;
  }

  public Long getNomBudgetApproverId()
  {
    return nomBudgetApproverId;
  }

  public void setNomBudgetApproverId( Long nomBudgetApproverId )
  {
    this.nomBudgetApproverId = nomBudgetApproverId;
  }

  public String getBudgetApproverSearchLastName()
  {
    return budgetApproverSearchLastName;
  }

  public void setBudgetApproverSearchLastName( String budgetApproverSearchLastName )
  {
    this.budgetApproverSearchLastName = budgetApproverSearchLastName;
  }

  public Long getSelectedBudgetApproverUserId()
  {
    return selectedBudgetApproverUserId;
  }

  public void setSelectedBudgetApproverUserId( Long selectedBudgetApproverUserId )
  {
    this.selectedBudgetApproverUserId = selectedBudgetApproverUserId;
  }

  private ParticipantService getParticipantService()
  {
    return (ParticipantService)BeanLocator.getBean( ParticipantService.BEAN_NAME );
  }

  private PromotionService getPromotionService()
  {
    return (PromotionService)BeanLocator.getBean( PromotionService.BEAN_NAME );
  }

  public boolean isTimePeriodActive()
  {
    return timePeriodActive;
  }

  public void setTimePeriodActive( boolean timePeriodActive )
  {
    this.timePeriodActive = timePeriodActive;
  }

  public List<NominationTimePeriodValueBean> getNominationTimePeriodVBList()
  {
    return nominationTimePeriodVBList;
  }

  public void setNominationTimePeriodVBList( List<NominationTimePeriodValueBean> nominationTimePeriodVBList )
  {
    this.nominationTimePeriodVBList = nominationTimePeriodVBList;
  }

  public String getStartDate()
  {
    return startDate;
  }

  public void setStartDate( String startDate )
  {
    this.startDate = startDate;
  }

  public String getEndDate()
  {
    return endDate;
  }

  public void setEndDate( String endDate )
  {
    this.endDate = endDate;
  }

  public List<NominationPromotionLevelBean> getNominationPayoutEachLevelList()
  {
    return nominationPayoutEachLevelList;
  }

  public void setNominationPayoutEachLevelList( List<NominationPromotionLevelBean> nominationPayoutEachLevelList )
  {
    this.nominationPayoutEachLevelList = nominationPayoutEachLevelList;
  }

  public NominationPromotionLevelBean getNominationPromotionLevelBean( int index )
  {
    while ( nominationPayoutEachLevelList.size() <= index )
    {
      nominationPayoutEachLevelList.add( new NominationPromotionLevelBean() );
    }
    return nominationPayoutEachLevelList.get( index );
  }

  public NominationPayoutFinalLevelBean getNominationPayoutFinalLevelBean( int index )
  {
    while ( nominationPayoutFinalLevelList.size() <= index )
    {
      nominationPayoutFinalLevelList.add( new NominationPayoutFinalLevelBean() );
    }
    return nominationPayoutFinalLevelList.get( index );
  }

  public List<NominationPayoutFinalLevelBean> getNominationPayoutFinalLevelList()
  {
    return nominationPayoutFinalLevelList;
  }

  public void setNominationPayoutFinalLevelList( List<NominationPayoutFinalLevelBean> nominationPayoutFinalLevelList )
  {
    this.nominationPayoutFinalLevelList = nominationPayoutFinalLevelList;
  }

  private List<NominationPromotionLevelBean> getEmptyNominationPayoutEachLevelList( int numberOfLevels )
  {
    List<NominationPromotionLevelBean> levelList = new ArrayList<NominationPromotionLevelBean>();

    for ( int i = 0; i < numberOfLevels; i++ )
    {
      NominationPromotionLevelBean nominationPromoLevel = new NominationPromotionLevelBean();
      nominationPromoLevel.setAwardsTypeList( buildAwardsType( i + 1 ) );
      nominationPromoLevel.setCalculatorList( buildCalculators( awardLevelIndex != null && awardLevelIndex.equals( new Long( i + 1 ) ) ) );
      levelList.add( nominationPromoLevel );
    }

    return levelList;
  }

  private List<PromotionAwardsType> buildAwardsType( int beanIndex )
  {
    List<PromotionAwardsType> awardsTypeList = PromotionAwardsType.getNominationList();
    if ( this.awardLevelIndex != null && new Long( beanIndex ).equals( awardLevelIndex ) )
    {
      for ( Iterator<PromotionAwardsType> awardIter = awardsTypeList.iterator(); awardIter.hasNext(); )
      {
        PromotionAwardsType awardsType = awardIter.next();
        if ( awardsType.getCode().equals( PromotionAwardsType.lookup( PromotionAwardsType.OTHER ).getCode() ) )
        {
          awardIter.remove();
        }

      }
    }

    return awardsTypeList;
  }

  private List<Calculator> buildCalculators( boolean isRangeOnly )
  {
    CalculatorQueryConstraint calcQueryConstraint = new CalculatorQueryConstraint();
    calcQueryConstraint
        .setCalculatorStatusTypeIncluded( new CalculatorStatusType[] { CalculatorStatusType.lookup( CalculatorStatusType.COMPLETED ), CalculatorStatusType.lookup( CalculatorStatusType.ASSIGNED ) } );
    // get the correct calculators for the awards type
    if ( isRangeOnly )
    {
      calcQueryConstraint.setCalculatorAwardTypeIncluded( new CalculatorAwardType[] { CalculatorAwardType.lookup( CalculatorAwardType.RANGE_AWARD ) } );
    }
    else
    {
      calcQueryConstraint
          .setCalculatorAwardTypeIncluded( new CalculatorAwardType[] { CalculatorAwardType.lookup( CalculatorAwardType.FIXED_AWARD ), CalculatorAwardType.lookup( CalculatorAwardType.RANGE_AWARD ) } );
    }

    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( new CalculatorAssociationRequest( CalculatorAssociationRequest.CALCULATOR_PAYOUT ) );

    List<Calculator> calculatorTypeList = getCalculatorService().getCalculatorListWithAssociations( calcQueryConstraint, associationRequestCollection );
    return calculatorTypeList;
  }

  private List<NominationPayoutFinalLevelBean> getEmptyNominationPayoutFinalLevelList( int numberOfLevels )
  {
    List<NominationPayoutFinalLevelBean> levelList = new ArrayList<NominationPayoutFinalLevelBean>();
    for ( int i = 0; i < numberOfLevels; i++ )
    {
      NominationPayoutFinalLevelBean nominationPromoLevel = new NominationPayoutFinalLevelBean();
      nominationPromoLevel.setAwardsTypeList( buildAwardsType( i + 1 ) );
      nominationPromoLevel.setCalculatorList( buildCalculators( awardLevelIndex != null && awardLevelIndex.equals( new Long( i + 1 ) ) ) );
      levelList.add( nominationPromoLevel );
    }
    return levelList;
  }

  public int getNominationPayoutEachLevelListSize()
  {
    if ( this.nominationPayoutEachLevelList != null )
    {
      return this.nominationPayoutEachLevelList.size();
    }

    return 0;
  }

  public int getNominationPayoutFinalLevelListSize()
  {
    if ( this.nominationPayoutFinalLevelList != null )
    {
      return this.nominationPayoutFinalLevelList.size();
    }

    return 0;
  }

  public boolean isNominatorRecommendedAward()
  {
    return nominatorRecommendedAward;
  }

  public void setNominatorRecommendedAward( boolean nominatorRecommendedAward )
  {
    this.nominatorRecommendedAward = nominatorRecommendedAward;
  }

  public String getLimitNomineeWinner()
  {
    return limitNomineeWinner;
  }

  public void setLimitNomineeWinner( String limitNomineeWinner )
  {
    this.limitNomineeWinner = limitNomineeWinner;
  }

  public Long getLimitPerPromotion()
  {
    return limitPerPromotion;
  }

  public void setLimitPerPromotion( Long limitPerPromotion )
  {
    this.limitPerPromotion = limitPerPromotion;
  }

  public boolean isOneLevelApproval()
  {
    return oneLevelApproval;
  }

  public void setOneLevelApproval( boolean oneLevelApproval )
  {
    this.oneLevelApproval = oneLevelApproval;
  }

  public boolean isCumulativeApproval()
  {
    return cumulativeApproval;
  }

  public void setCumulativeApproval( boolean cumulativeApproval )
  {
    this.cumulativeApproval = cumulativeApproval;
  }

  private static CalculatorService getCalculatorService()
  {
    return (CalculatorService)BeanLocator.getBean( CalculatorService.BEAN_NAME );
  }

  public boolean isFirstLevelAward()
  {
    return firstLevelAward;
  }

  public void setFirstLevelAward( boolean firstLevelAward )
  {
    this.firstLevelAward = firstLevelAward;
  }

  public boolean isAwardSelected()
  {
    return awardSelected;
  }

  public void setAwardSelected( boolean awardSelected )
  {
    this.awardSelected = awardSelected;
  }

  public boolean isApproverTypeCustom()
  {
    return approverTypeCustom;
  }

  public void setApproverTypeCustom( boolean approverTypeCustom )
  {
    this.approverTypeCustom = approverTypeCustom;
  }

  public String getCashBudgetOption()
  {
    return cashBudgetOption;
  }

  public void setCashBudgetOption( String cashBudgetOption )
  {
    this.cashBudgetOption = cashBudgetOption;
  }

  public Long getCashBudgetMasterId()
  {
    return cashBudgetMasterId;
  }

  public void setCashBudgetMasterId( Long cashBudgetMasterId )
  {
    this.cashBudgetMasterId = cashBudgetMasterId;
  }

  public String getCashBudgetMasterName()
  {
    return cashBudgetMasterName;
  }

  public void setCashBudgetMasterName( String cashBudgetMasterName )
  {
    this.cashBudgetMasterName = cashBudgetMasterName;
  }

  public String getCashBudgetType()
  {
    return cashBudgetType;
  }

  public void setCashBudgetType( String cashBudgetType )
  {
    this.cashBudgetType = cashBudgetType;
  }

  public String getCashBudgetCapType()
  {
    return cashBudgetCapType;
  }

  public void setCashBudgetCapType( String cashBudgetCapType )
  {
    this.cashBudgetCapType = cashBudgetCapType;
  }

  public Long getHiddenCashBudgetMasterId()
  {
    return hiddenCashBudgetMasterId;
  }

  public void setHiddenCashBudgetMasterId( Long hiddenCashBudgetMasterId )
  {
    this.hiddenCashBudgetMasterId = hiddenCashBudgetMasterId;
  }

  public String getCashFinalPayoutRule()
  {
    return cashFinalPayoutRule;
  }

  public void setCashFinalPayoutRule( String cashFinalPayoutRule )
  {
    this.cashFinalPayoutRule = cashFinalPayoutRule;
  }

  public String getCashBudgetMasterStartDate()
  {
    return cashBudgetMasterStartDate;
  }

  public void setCashBudgetMasterStartDate( String cashBudgetMasterStartDate )
  {
    this.cashBudgetMasterStartDate = cashBudgetMasterStartDate;
  }

  public String getCashBudgetMasterEndDate()
  {
    return cashBudgetMasterEndDate;
  }

  public void setCashBudgetMasterEndDate( String cashBudgetMasterEndDate )
  {
    this.cashBudgetMasterEndDate = cashBudgetMasterEndDate;
  }

  public List<BudgetSegmentValueBean> getCashBudgetSegmentVBList()
  {
    return cashBudgetSegmentVBList;
  }

  public void setCashBudgetSegmentVBList( List<BudgetSegmentValueBean> cashBudgetSegmentVBList )
  {
    this.cashBudgetSegmentVBList = cashBudgetSegmentVBList;
  }

  public Long getAwardLevelIndex()
  {
    return awardLevelIndex;
  }

  public void setAwardLevelIndex( Long awardLevelIndex )
  {
    this.awardLevelIndex = awardLevelIndex;
  }

  public Integer getNumApprovalLevels()
  {
    return numApprovalLevels;
  }

  public void setNumApprovalLevels( Integer numApprovalLevels )
  {
    this.numApprovalLevels = numApprovalLevels;
  }

  public boolean isHideTpSubmissionLimitColumns()
  {
    return hideTpSubmissionLimitColumns;
  }

  public void setHideTpSubmissionLimitColumns( boolean hideTpSubmissionLimitColumns )
  {
    this.hideTpSubmissionLimitColumns = hideTpSubmissionLimitColumns;
  }

  public boolean isOtherLevelsAward()
  {
    return otherLevelsAward;
  }

  public int getCustomApproverAwardLevel()
  {
    return customApproverAwardLevel;
  }

  public boolean isBehaviorActive()
  {
    return behaviorActive;
  }

  public void setBehaviorActive( boolean behaviorActive )
  {
    this.behaviorActive = behaviorActive;
  }

  public void setOtherLevelsAward( boolean otherLevelsAward )
  {
    this.otherLevelsAward = otherLevelsAward;
  }

  public void setCustomApproverAwardLevel( int customApproverAwardLevel )
  {
    this.customApproverAwardLevel = customApproverAwardLevel;
  }

  public boolean isFinalLevelAward()
  {
    return finalLevelAward;
  }

  public void setFinalLevelAward( boolean finalLevelAward )
  {
    this.finalLevelAward = finalLevelAward;
  }
  
  /*Client customization start*/
  public boolean isUtilizeParentBudgets()
  {
    return utilizeParentBudgets;
  }

  public void setUtilizeParentBudgets( boolean utilizeParentBudgets )
  {
    this.utilizeParentBudgets = utilizeParentBudgets;
  }

/**
 * @return the nominationAwardSpecifierTypeCode
 */
public String getNominationAwardSpecifierTypeCode() {
	return nominationAwardSpecifierTypeCode;
}

/**
 * @param nominationAwardSpecifierTypeCode the nominationAwardSpecifierTypeCode to set
 */
public void setNominationAwardSpecifierTypeCode(String nominationAwardSpecifierTypeCode) {
	this.nominationAwardSpecifierTypeCode = nominationAwardSpecifierTypeCode;
}
  
  /*Client customization end*/
}
