/*
 * (c) 2005 BI, Inc. All rights reserved. $Source:
 * /usr/local/ndscvsroot/products/beacon/src/javaui/com/biperf/core/ui/promotion/PromotionPayoutForm.java,v $
 */

package com.biperf.core.ui.promotion;

import java.text.SimpleDateFormat;
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
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.springframework.beans.support.MutableSortDefinition;
import org.springframework.beans.support.PropertyComparator;

import com.biperf.core.domain.budget.BudgetMaster;
import com.biperf.core.domain.budget.BudgetSegment;
import com.biperf.core.domain.claim.ClaimFormStepElement;
import com.biperf.core.domain.enums.BudgetFinalPayoutRule;
import com.biperf.core.domain.enums.BudgetMasterAwardType;
import com.biperf.core.domain.enums.BudgetOverrideableType;
import com.biperf.core.domain.enums.BudgetType;
import com.biperf.core.domain.enums.PromoMgrPayoutFreqType;
import com.biperf.core.domain.enums.PromotionAwardsType;
import com.biperf.core.domain.enums.PromotionIssuanceType;
import com.biperf.core.domain.enums.PromotionPayoutType;
import com.biperf.core.domain.enums.PromotionStatusType;
import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.domain.enums.StackRankApprovalType;
import com.biperf.core.domain.enums.StackRankFactorType;
import com.biperf.core.domain.enums.SubmittersToRankType;
import com.biperf.core.domain.hierarchy.NodeType;
import com.biperf.core.domain.product.Product;
import com.biperf.core.domain.product.ProductCategory;
import com.biperf.core.domain.promotion.ProductClaimPromotion;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.PromotionPayout;
import com.biperf.core.domain.promotion.PromotionPayoutGroup;
import com.biperf.core.domain.promotion.StackRankPayout;
import com.biperf.core.domain.promotion.StackRankPayoutGroup;
import com.biperf.core.service.SAO;
import com.biperf.core.service.hierarchy.NodeTypeService;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.ui.BaseForm;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.DateFormatterUtil;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.BudgetSegmentValueBean;
import com.objectpartners.cms.util.CmsResourceBundle;

/**
 * Promotion Payout ActionForm transfer object <p/> <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>sedey</td>
 * <td>June 29, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class PromotionPayoutForm extends BaseForm
{
  public static final String ONE_TO_ONE = "one_to_one";
  public static final String TIERED = "tiered";
  public static final String CROSS_SELL = "cross_sell";
  public static final String STACK_RANK = "stack_rank";

  // Break the Bank Budget Info
  public static final String BUDGET_NONE = "none";
  public static final String BUDGET_EXISTING = "existing";
  public static final String BUDGET_NEW = "new";
  public static final String BUDGET_SAME_AS_PARENT = "sameAsParent";

  private String promotionIssuanceTypeCode; // this is to display budget or not
  private String budgetOption = BUDGET_NONE;
  private Long budgetMasterId;
  private String budgetMasterName;
  private String budgetType = BudgetType.CENTRAL_BUDGET_TYPE;
  private String budgetCapType = BudgetOverrideableType.HARD_OVERRIDE;
  private String finalPayoutRule;
  private String centralBudgetAmount;
  // END Break the Bank Budget Info

  private String method = "";
  private Long promotionId;
  private String promotionName;
  private String promotionTypeName;
  private String promotionStatus;
  private String alternateReturnUrl;
  private String payoutType = "";
  private String parentPayoutType = "";
  private boolean managerOverride = false;
  private String managerOverridePercent;
  private String managerOverrideFrequency;
  private boolean carryoverAllowed = false;
  private boolean teamUsed;
  private Long version;
  private String groupEditId;

  private String prevPayoutType;

  private List promoPayoutGroupValueList;
  private String promotionTypeCode;

  private boolean hasParent;
  private String awardType; // this is to display budget or not
  private String awardTypeName;
  private String promotionSubmitStartDate;
  private String promotionSubmitEndDate;

  private boolean allowDefaultQuantity;
  private int defaultQuantity;

  // stackrank
  private boolean stackRankApprovalType = false;
  private String stackRankFactorType = "";
  private boolean displayStackRankFactor = false;
  private boolean displayFullListLinkToParticipants = false;
  private String stackRankGroupEditId;
  private List promoStackRankPayoutGroupValueList;
  private boolean disableProductSection = false;
  private boolean hasPendingStackRank = false;

  private Map groupDependentMap = new HashMap();

  private String playingMethod;
  private String expirationDate = DateUtils.displayDateFormatMask;

  private Long lowPaymentThreshold;

  // only for validation.
  private Integer payoutCalculation;
  public static final int PAYOUT_TIER_COUNT = 5;

  // Budget Segment fields
  private String budgetMasterStartDate = DateUtils.displayDateFormatMask;
  private String budgetMasterEndDate = DateUtils.displayDateFormatMask;
  private List<BudgetSegmentValueBean> budgetSegmentVBList = new ArrayList<BudgetSegmentValueBean>();
  private Long budgetSegmentId;

  /**
   * Reset all properties to their default values.
   * 
   * @param mapping The mapping used to select this instance
   * @param request The servlet request we are processing
   */
  public void reset( ActionMapping mapping, HttpServletRequest request )
  {
    // reset needs to be used to populate an empty list of
    // PromotionPayoutFormBeans. If this is not done, the form wont initialize
    // properly.

    int groupCount = RequestUtils.getOptionalParamInt( request, "promoPayoutGroupValueListCount" );
    String payoutTypeValue = "";

    // if groupCount is 0, try looking in the session for a form
    // because this might be coming from the category or product search page

    if ( groupCount == 0 )
    {
      PromotionPayoutForm sessionPromoPayoutForm = (PromotionPayoutForm)request.getSession().getAttribute( PromotionPayoutAction.SESSION_PROMO_PAYOUT_FORM );
      if ( sessionPromoPayoutForm != null )
      {
        groupCount = sessionPromoPayoutForm.getPromoPayoutGroupValueListCount();
        payoutTypeValue = sessionPromoPayoutForm.getPayoutType();
      }
    }
    else
    {
      // If there is data, then we need to temporarily keep the data structure intact.
      // Otherwise struts pukes when it tries to populate the form.
      payoutTypeValue = RequestUtils.getOptionalParamString( request, "oldPayoutType" );
    }

    promoPayoutGroupValueList = getEmptyValueList( groupCount );
    if ( groupCount > 0 )
    {
      if ( StringUtils.isEmpty( payoutTypeValue ) )
      {
        payoutTypeValue = RequestUtils.getOptionalParamString( request, "payoutType" );
      }

      if ( payoutTypeValue.equals( ONE_TO_ONE ) || payoutTypeValue.equals( STACK_RANK ) )
      {
        for ( Iterator iter = promoPayoutGroupValueList.iterator(); iter.hasNext(); )
        {
          PromotionPayoutGroupFormBean group = (PromotionPayoutGroupFormBean)iter.next();
          group.setPromoPayoutValueList( getEmptyPayoutValueList( 1 ) );
        }
      }
      else if ( payoutTypeValue.equals( CROSS_SELL ) || payoutTypeValue.equals( TIERED ) )
      {
        // Look for groupId's
        for ( int i = 0; i < groupCount; i++ )
        {
          String key = "promoPayoutGroupValueList[" + i + "].guid";
          String guid = RequestUtils.getOptionalParamString( request, key );
          if ( guid != null && guid.length() > 0 )
          {
            PromotionPayoutGroupFormBean group = (PromotionPayoutGroupFormBean)promoPayoutGroupValueList.get( i );
            group.setGuid( guid );
            String payoutSizeId = "groupPayoutSize" + guid;
            int payoutSize = RequestUtils.getOptionalParamInt( request, payoutSizeId );
            group.setPromoPayoutValueList( getEmptyPayoutValueList( payoutSize ) );
          }
        }
      }
    }
    if ( StringUtils.isEmpty( payoutTypeValue ) )
    {
      payoutTypeValue = RequestUtils.getOptionalParamString( request, "payoutType" );
    }
    // stack rank
    // Bug # 35386
    String oldPayoutTypeValue = RequestUtils.getOptionalParamString( request, "oldPayoutType" );
    if ( payoutTypeValue.equals( STACK_RANK ) || oldPayoutTypeValue.equals( STACK_RANK ) )
    {
      // reset needs to be used to populate an empty list of
      // PromotionPayoutFormBeans. If this is not done, the form wont initialize
      // properly.

      int groupCountStackRank = RequestUtils.getOptionalParamInt( request, "promoStackRankPayoutGroupValueListCount" );
      // String payoutTypeValueStackRank = "";

      // if groupCount is 0, try looking in the session for a form
      // because this might be coming from the category or product search page

      if ( groupCountStackRank == 0 )
      {
        PromotionPayoutForm sessionPromoPayoutForm = (PromotionPayoutForm)request.getSession().getAttribute( PromotionPayoutAction.SESSION_PROMO_PAYOUT_FORM );
        if ( sessionPromoPayoutForm != null )
        {
          groupCountStackRank = sessionPromoPayoutForm.getPromoStackRankPayoutGroupValueListCount();
        }
      }

      promoStackRankPayoutGroupValueList = getStackRankEmptyValueList( groupCountStackRank );
      if ( groupCountStackRank > 0 )
      {
        // Look for groupId's
        for ( int i = 0; i < groupCountStackRank; i++ )
        {
          String key = "promoStackRankPayoutGroupValueList[" + i + "].guid";
          String guid = RequestUtils.getOptionalParamString( request, key );
          if ( guid != null && guid.length() > 0 )
          {
            PromotionStackRankPayoutGroupFormBean groupStackRank = (PromotionStackRankPayoutGroupFormBean)promoStackRankPayoutGroupValueList.get( i );
            groupStackRank.setGuid( guid );
            String payoutSizeId = "groupStackRankPayoutSize" + guid;
            int payoutSize = RequestUtils.getOptionalParamInt( request, payoutSizeId );
            groupStackRank.setPromoStackRankPayoutValueList( getStackRankEmptyPayoutValueList( payoutSize ) );
          }
        }
      }
    }

    budgetSegmentVBList = getEmptyBudgetSegmentValueList( RequestUtils.getOptionalParamInt( request, "budgetSegmentVBListSize" ) );

  }

  /**
   * Validate the properties that have been set from this HTTP request, and return an
   * <code>ActionErrors</code> object that encapsulates any validation errors that have been
   * found. If no errors are found, return <code>null</code> or an <code>ActionErrors</code>
   * object with no recorded error messages.
   * 
   * @param actionMapping the ActionMapping used to select this instance
   * @param request the HTTP request that is being processed
   * @return org.apache.struts.action.ActionErrors - collection of errors found on validation of
   *         data in form
   */
  public ActionErrors validate( ActionMapping actionMapping, HttpServletRequest request )
  {
    ActionErrors actionErrors = super.validate( actionMapping, request );

    if ( actionErrors == null )
    {
      actionErrors = new ActionErrors();
    }

    // Validate Budget data only if the awardType is points and method of entry is not file load
    if ( awardType.equals( PromotionAwardsType.lookup( PromotionAwardsType.POINTS ).getCode() ) && !PromotionIssuanceType.FILE_LOAD.equals( promotionIssuanceTypeCode ) )
    {
      validateBudgetInfo( actionErrors );
    }

    if ( this.allowDefaultQuantity && this.defaultQuantity < 1 )
    {
      actionErrors.add( "defaultQuantity", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "promotion.payout.DEFAULT_QUANTITY" ) ) );
    }

    PromotionPayoutGroupFormBean promoPayoutGroupFormBean = null;

    // If the payoutType has been selected, then validate that there are payouts
    if ( !payoutType.equals( "" ) && getPromoPayoutGroupValueListCount() == 0 )
    {
      actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "promotion.payout.errors.NO_PAYOUTS" ) );
    }

    for ( int i = 0; i < getPromoPayoutGroupValueListCount(); i++ )
    {
      promoPayoutGroupFormBean = this.getPromoPayoutGroupValueListElement( i );

      if ( !hasParent )
      {
        // If the payout type isn't cross sell then quantity is stored at the group level
        if ( payoutType.equals( PromotionPayoutType.ONE_TO_ONE ) || payoutType.equals( PromotionPayoutType.TIERED ) )
        {
          if ( !validatePositiveNumericMoreThanZero( promoPayoutGroupFormBean.getParentQuantity() ) )
          {
            actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "promotion.payout.errors.QUANTITY" ) );
            break;
          }
        }
        else
        {
          if ( promotionStatus.equals( PromotionStatusType.COMPLETE ) )
          {
            if ( promoPayoutGroupFormBean.getPromoPayoutValueListCount() < 2 )
            {
              actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "promotion.payout.errors.CROSS_SELL_PAYOUT" ) );
            }
          }
        }
        // only need to validate the minimumQualifier and retroPayout if the payoutType
        // is oneToOne or Tiered
        if ( payoutType.equals( PromotionPayoutType.ONE_TO_ONE ) || payoutType.equals( PromotionPayoutType.TIERED ) )
        {
          if ( !validatePositiveNumericMoreThanZero( promoPayoutGroupFormBean.getMinimumQualifier() ) )
          {
            actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "promotion.payout.errors.MINIMUM_QUALIFIER_ERROR" ) );
            break;
          }
          /*
           * if ( Integer.parseInt(promoPayoutGroupFormBean.getMinimumQualifier()) > 1 ) { if (
           * promoPayoutGroupFormBean.getRetroPayout().equals("") ) { actionErrors.add(
           * ActionErrors.GLOBAL_MESSAGE, new ActionMessage(
           * "promotion.payout.errors.RETRO_PAYOUT_ERROR" ) ); break; } }
           */
        }

        if ( payoutType.equals( PromotionPayoutType.TIERED ) )
        {
          if ( Integer.parseInt( promoPayoutGroupFormBean.getMinimumQualifier() ) % Integer.parseInt( promoPayoutGroupFormBean.getParentQuantity() ) != 0 )
          {
            actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "promotion.payout.errors.MIN_QUALIFIER_VALUE_ERROR" ) );
            break;
          }
        }

        if ( !payoutType.equals( PromotionPayoutType.STACK_RANK ) )
        {
          // Check the parent SubmitterPayout
          if ( !validatePositiveNumeric( promoPayoutGroupFormBean.getSubmitterParentPayout() ) )
          {
            Iterator it = actionErrors.get( ActionErrors.GLOBAL_MESSAGE );
            int numberOfMessages = actionErrors.size( ActionErrors.GLOBAL_MESSAGE );
            ActionMessage error = new ActionMessage( "promotion.payout.errors.SUBMITTER_PAYOUT" );
            if ( numberOfMessages == 0 )
            {
              actionErrors.add( ActionErrors.GLOBAL_MESSAGE, error );
            }
            else
            {
              int count = 0;
              while ( it.hasNext() )
              {
                ActionMessage message = (ActionMessage)it.next();
                if ( message.toString().equals( error.toString() ) )
                {
                  count = count + 1;
                }
              }
              if ( count == 0 )
              {
                actionErrors.add( ActionErrors.GLOBAL_MESSAGE, error );
              }
            }
          }

        }

        // If the payoutType is not Tiered and stack rankand if team is used check the team member
        // payout
        if ( !payoutType.equals( PromotionPayoutType.TIERED ) && !payoutType.equals( PromotionPayoutType.STACK_RANK ) )
        {
          if ( teamUsed )
          {
            if ( !validatePositiveNumeric( promoPayoutGroupFormBean.getTeamMemberParentPayout() ) )
            {
              Iterator it = actionErrors.get( ActionErrors.GLOBAL_MESSAGE );
              int numberOfMessages = actionErrors.size( ActionErrors.GLOBAL_MESSAGE );
              ActionMessage error = new ActionMessage( "promotion.payout.errors.TEAM_MEMBER_PAYOUT" );
              if ( numberOfMessages == 0 )
              {
                actionErrors.add( ActionErrors.GLOBAL_MESSAGE, error );
              }
              else
              {
                int count = 0;
                while ( it.hasNext() )
                {
                  ActionMessage message = (ActionMessage)it.next();
                  if ( message.toString().equals( error.toString() ) )
                  {
                    count = count + 1;
                  }
                }
                if ( count == 0 )
                {
                  actionErrors.add( ActionErrors.GLOBAL_MESSAGE, error );
                }
              }
            }
          }
        }

        Iterator payoutIter = promoPayoutGroupFormBean.getPromoPayoutValueList().iterator();
        while ( payoutIter.hasNext() )
        {
          PromotionPayoutFormBean payoutBean = (PromotionPayoutFormBean)payoutIter.next();

          // Cross Sell payout store the quantity at the payout level not the group
          if ( payoutType.equals( PromotionPayoutType.CROSS_SELL ) )
          {
            if ( !validatePositiveNumericMoreThanZero( payoutBean.getParentQuantity() ) )
            {
              actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "promotion.payout.errors.QUANTITY" ) );
            }
          }
          // only the date of parent payouts can be modified so only check them
          // if this is a parent promo and not stack rank

          if ( payoutBean.getProductOrCategoryStartDate().equals( "" ) )
          {
            actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "promotion.payout.errors.START_DATE_EMPTY" ) );
            break;
          }
          else if ( DateUtils.toDate( payoutBean.getProductOrCategoryStartDate() ) == null )
          {
            actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "promotion.payout.errors.DATE_RANGE" ) );
            break;
          }

          if ( DateUtils.toDate( payoutBean.getProductOrCategoryStartDate() ).before( DateUtils.toDate( promotionSubmitStartDate ) ) )
          {
            actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "promotion.payout.errors.START_DATE_RANGE", promotionSubmitStartDate ) );
            break;
          }

          if ( !payoutBean.getProductOrCategoryEndDate().equals( "" ) )
          {
            // if null gets returned by DateUtils.toDate, then date was invalid.
            if ( DateUtils.toDate( payoutBean.getProductOrCategoryEndDate() ) == null )
            {
              actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "promotion.payout.errors.DATE_RANGE" ) );
              break;
            }

            if ( !promotionSubmitEndDate.equals( "" ) )
            {
              if ( DateUtils.toDate( promotionSubmitEndDate ).before( DateUtils.toDate( payoutBean.getProductOrCategoryEndDate() ) ) )
              {
                actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "promotion.payout.errors.END_DATE_RANGE", promotionSubmitEndDate ) );
                break;
              }
            }

            if ( DateUtils.toDate( payoutBean.getProductOrCategoryEndDate() ).before( DateUtils.toDate( payoutBean.getProductOrCategoryStartDate() ) ) )
            {
              actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "promotion.payout.errors.DATE_RANGE" ) );
              break;
            }
          }
        }

      }
      else
      // validate for child payouts
      {
        // All validations for a child payout need to happen only if the payout is included
        Iterator childPayouts = promoPayoutGroupFormBean.getPromoPayoutValueList().iterator();
        int includedPayouts = 0;
        while ( childPayouts.hasNext() )
        {
          PromotionPayoutFormBean childPayout = (PromotionPayoutFormBean)childPayouts.next();
          if ( childPayout.isIncludePayout() )
          {
            includedPayouts++;

            // Cross Sell payout type has quantity stored at the payout level (not the group)
            if ( payoutType.equals( PromotionPayoutType.CROSS_SELL ) )
            {
              if ( !validatePositiveNumericMoreThanZero( childPayout.getChildQuantity() ) )
              {
                actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "promotion.payout.errors.QUANTITY" ) );
              }
            }
            else
            {
              if ( !payoutType.equals( PromotionPayoutType.STACK_RANK ) )
              {
                if ( !validatePositiveNumericMoreThanZero( promoPayoutGroupFormBean.getChildQuantity() ) )
                {
                  actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "promotion.payout.errors.QUANTITY" ) );
                }
              }
            }
            // only need to validate the minimumQualifier and retroPayout if the payoutType
            // is oneToOne or Tiered
            if ( payoutType.equals( PromotionPayoutType.ONE_TO_ONE ) || payoutType.equals( PromotionPayoutType.TIERED ) )
            {
              if ( !validatePositiveNumericMoreThanZero( promoPayoutGroupFormBean.getMinimumQualifier() ) )
              {
                actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "promotion.payout.errors.MINIMUM_QUALIFIER_ERROR" ) );
                break;
              }
            }
            // Check child submitter payout
            if ( !payoutType.equals( PromotionPayoutType.STACK_RANK ) )
            {
              if ( !validatePositiveNumeric( promoPayoutGroupFormBean.getSubmitterChildPayout() ) )
              {
                actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "promotion.payout.errors.SUBMITTER_PAYOUT" ) );
              }
              // Only check team member payout if team is used
              if ( teamUsed )
              {
                if ( !validatePositiveNumeric( promoPayoutGroupFormBean.getTeamMemberChildPayout() ) )
                {
                  actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "promotion.payout.errors.TEAM_MEMBER_PAYOUT" ) );
                }
              }
            }
          }
        }

        if ( promotionStatus.equals( PromotionStatusType.COMPLETE ) )
        {
          if ( includedPayouts < 1 )
          {
            actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "promotion.payout.errors.NO_PAYOUTS" ) );
          }
          if ( payoutType.equals( CROSS_SELL ) )
          {
            if ( includedPayouts < 2 )
            {
              actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "promotion.payout.errors.CROSS_SELL_PAYOUT" ) );
            }
          }
        }
      }
    }

    if ( PromotionPayoutType.STACK_RANK.equals( payoutType ) )
    {
      actionErrors = validateStackRankPayoutRanks( actionMapping, request, actionErrors );
      actionErrors = validateStackRankPayouts( actionMapping, request, actionErrors );
      if ( isStackRankNodeTypeAlreadySelected() )
      {
        actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "promotion.payout.errors.NODE_TYPE_UNIQUE_ERROR" ) );
      }
    }

    if ( isManagerOverride() )
    {
      if ( !validatePositiveNumeric( getManagerOverridePercent() ) )
      {
        actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "promotion.payout.errors.MANAGER_PERCENT" ) );
      }
    }

    Date dateStart = null;
    Date dateEnd = null;
    SimpleDateFormat sdf = new SimpleDateFormat( DateFormatterUtil.getDatePattern( UserManager.getLocale() ) );

    // ***** Validate budgetMasterStartDate *****/
    // Make sure its not empty
    if ( isCreateNewBudgetMaster() )
    {
      // ***** Validate budgetMasterStartDate *****/
      Date startDateLocal = null;
      Date endDateLocal = null;

      // Make sure its not empty
      if ( budgetMasterStartDate == null || budgetMasterStartDate.length() == 0 )
      {
        actionErrors.add( "budgetMasterStartDate",
                          new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "admin.budgetmaster.details.START_DATE" ) ) );
      }
      else
      {
        // The date string must represent a date.
        startDateLocal = DateUtils.toDate( budgetMasterStartDate );
        if ( startDateLocal == null )
        {
          actionErrors.add( "budgetMasterStartDate",
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
          actionErrors.add( "budgetMasterEndDate",
                            new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_DATE, CmsResourceBundle.getCmsBundle().getString( "admin.budgetmaster.details", "END_DATE" ) ) );
        }
        else if ( endDateLocal.before( DateUtils.toStartDate( DateUtils.getCurrentDate() ) ) )
        {
          // The date is before current date
          actionErrors.add( "budgetMasterEndDate", new ActionMessage( "promotion.awards.errors.BUDGET_MASTER_END_DATE_INVALID" ) );
        }

        // The start date must be earlier than or equal to the end date.
        if ( startDateLocal != null && endDateLocal != null && endDateLocal.before( startDateLocal ) )
        {
          actionErrors.add( "budgetMasterStartDate", new ActionMessage( "promotion.awards.errors.BUDGET_MASTER_START_DATE_INVALID" ) );
        }
      }
    }
    return actionErrors;
  }

  /**
   * Validate Budget Information for Break the Bank
   * 
   * @param errors
   * @return ActionErrors
   */
  private ActionErrors validateBudgetInfo( ActionErrors errors )
  {
    // ***** Validate budgetOption *****/
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
      return errors;
    }

    if ( budgetOption.equals( BUDGET_EXISTING ) )
    {
      // ***** Validate budgetMasterId *****/
      // Make sure its not empty
      if ( budgetMasterId == null || budgetMasterId.equals( new Long( 0 ) ) )
      {
        errors.add( "budgetMasterId", new ActionMessage( "promotion.awards.errors.CHOOSE_EXISTING_BUDGET" ) );
        return errors;
      }
    }

    if ( budgetOption.equals( BUDGET_NEW ) )
    {
      // ***** Validate budgetMasterName *****/
      // Make sure its not empty
      if ( budgetMasterName == null || budgetMasterName.length() == 0 )
      {
        errors.add( "budgetMasterName", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "promotion.awards.BUDGET_MASTER_NAME" ) ) );
      }

      // ***** Validate budgetType *****/
      // Make sure its not empty
      if ( budgetType == null || budgetType.length() == 0 )
      {
        errors.add( "budgetType", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "promotion.awards.BUDGET_TYPE" ) ) );
      }

      // ***** Validate budgetCapType *****/
      // Make sure its not empty
      if ( budgetCapType == null || budgetCapType.length() == 0 )
      {
        errors.add( "budgetCapType", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "promotion.awards.CAP_TYPE" ) ) );

        // if no budgetCapType was selected, then no point validating further
        return errors;
      }

      if ( BudgetType.CENTRAL_BUDGET_TYPE.equals( budgetType ) )
      {
        errors = validateTimePeriodBudgetAmount( errors );
        // The final payout rule is required for Central budget
        if ( this.finalPayoutRule == null )
        {
          errors.add( "finalPayoutRule", new ActionMessage( "system.errors.REQUIRED", CmsResourceBundle.getCmsBundle().getString( "admin.budgetmaster.details", "FINAL_PAYOUT_RULE" ) ) );
        }

      }

      // validate for time period start and end dates, segment name.
      errors = validateTimePeriod( errors );
    }
    return errors;
  }

  /**
   * validates String is positive numeric, not null, not blank
   * 
   * @param string String that is going to be validated
   * @return boolean
   */
  private boolean validatePositiveNumeric( String string )
  {
    return string != null && !string.equals( "" ) && string.matches( "\\d*" );
  }

  /**
   * validates String is positive numeric, not null, not blank and > 0
   * 
   * @param string String that is going to be valdidated
   * @return boolean
   */
  private boolean validatePositiveNumericMoreThanZero( String string )
  {
    boolean valid = validatePositiveNumeric( string );
    if ( valid )
    {
      return Integer.parseInt( string ) > 0;
    }
    return false;
  }

  /**
   * Validate the properties that have been set from this HTTP request, and return an
   * <code>ActionErrors</code> object that encapsulates any validation errors that have been
   * found. If no errors are found, return <code>null</code> or an <code>ActionErrors</code>
   * object with no recorded error messages.
   * 
   * @param actionMapping the ActionMapping used to select this instance
   * @param request the HTTP request that is being processed
   * @param errors
   * @return org.apache.struts.action.ActionErrors - collection of errors found on validation of
   *         data in form
   */
  public ActionErrors validateStackRankPayouts( ActionMapping actionMapping, HttpServletRequest request, ActionErrors errors )
  {
    boolean validPayout = true;
    // even if there is one error in validating payouts there is no point in checking
    if ( this.getPromoStackRankPayoutGroupValueListCount() > 0 )
    {
      for ( Iterator promoStackRankPayoutGroupValueListIter = this.getPromoStackRankPayoutGroupValueList().iterator(); promoStackRankPayoutGroupValueListIter.hasNext(); )
      {
        PromotionStackRankPayoutGroupFormBean promotionStackRankPayoutGroupFormBean = (PromotionStackRankPayoutGroupFormBean)promoStackRankPayoutGroupValueListIter.next();
        for ( int i = 0; i < promotionStackRankPayoutGroupFormBean.getPromoStackRankPayoutValueListCount(); i++ )
        {
          PromotionStackRankPayoutFormBean promoStackRankPayoutValueBean = promotionStackRankPayoutGroupFormBean.getPromoPayoutValueList( i );

          validPayout = validatePositiveNumeric( promoStackRankPayoutValueBean.getPayoutAmount() );
          if ( !validPayout )
          {
            // Invalid payout type non numeric - ERROR
            errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "promotion.payout.errors.NO_STACKRANK_PAYOUT_ERROR" ) );
            break;
          }
        }
        if ( !validPayout )
        {
          break;
        }
      }
    }

    return errors;
  }

  /**
   * Validate the properties that have been set from this HTTP request, and return an
   * <code>ActionErrors</code> object that encapsulates any validation errors that have been
   * found. If no errors are found, return <code>null</code> or an <code>ActionErrors</code>
   * object with no recorded error messages.
   * 
   * @param actionMapping the ActionMapping used to select this instance
   * @param request the HTTP request that is being processed
   * @param errors
   * @return org.apache.struts.action.ActionErrors - collection of errors found on validation of
   *         data in form
   */
  public ActionErrors validateStackRankPayoutRanks( ActionMapping actionMapping, HttpServletRequest request, ActionErrors errors )
  {
    // even if there is one error in validating from ot rank there is no point in checking up
    // overlap
    boolean checkRankOverlap = true;
    // adding this to make sure we have atlease one payout and atleast one per payout group
    int totalPayoutCount = 0;
    if ( this.getPromoStackRankPayoutGroupValueListCount() > 0 )
    {
      for ( Iterator promoStackRankPayoutGroupValueListIter = this.getPromoStackRankPayoutGroupValueList().iterator(); promoStackRankPayoutGroupValueListIter.hasNext(); )
      {
        PromotionStackRankPayoutGroupFormBean promotionStackRankPayoutGroupFormBean = (PromotionStackRankPayoutGroupFormBean)promoStackRankPayoutGroupValueListIter.next();
        // adding this to make sure we have atlease one payout and atleast one per payout group
        int groupPayoutCount = 0;
        for ( int i = 0; i < promotionStackRankPayoutGroupFormBean.getPromoStackRankPayoutValueListCount(); i++ )
        {
          totalPayoutCount++;
          groupPayoutCount++;
          PromotionStackRankPayoutFormBean promoStackRankPayoutValueBean = promotionStackRankPayoutGroupFormBean.getPromoPayoutValueList( i );

          boolean validFromRank = validatePositiveNumericMoreThanZero( promoStackRankPayoutValueBean.getFromRank() );
          boolean validToRank = validatePositiveNumericMoreThanZero( promoStackRankPayoutValueBean.getToRank() );

          if ( validFromRank && validToRank )
          {
            if ( Integer.parseInt( promoStackRankPayoutValueBean.getFromRank() ) > Integer.parseInt( promoStackRankPayoutValueBean.getToRank() ) )
            {
              // from reank cant be more than torank - ERROR
              errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "promotion.payout.errors.FROM_RANK_LESS_TO_RANK" ) );
              checkRankOverlap = false;
              break;
            }
          }
          else
          {
            checkRankOverlap = false;
            // one of them is or both are non numeric - ERROR
            errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "promotion.payout.errors.RANK_NUMERIC_ERROR" ) );
            break;
          }
          if ( groupPayoutCount <= 0 )
          {
            // group must have atlease one payout - error
            errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "promotion.payout.errors.MIN_ONE_PAYOUT_PER_GROUP_ERROR" ) );
          }
        }

        if ( totalPayoutCount <= 0 )
        {
          // we must have atlease one payout - error
          errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "promotion.payout.errors.NO_STACKRANK_PAYOUT_ERROR" ) );
        }

        if ( !checkRankOverlap )
        {
          break;
        }
      }

      if ( checkRankOverlap )
      {
        validateStackrankOverlap( errors );
      }
    }
    else
    {
      // need atleast on payout
      errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "promotion.payout.errors.NO_STACKRANK_PAYOUT_ERROR" ) );
    }

    return errors;
  }

  private ActionErrors validateStackrankOverlap( ActionErrors errors )
  {
    boolean isOverlap = false;

    if ( this.getPromoStackRankPayoutGroupValueListCount() > 0 )
    {
      for ( Iterator promoStackRankPayoutGroupValueListIter = this.getPromoStackRankPayoutGroupValueList().iterator(); promoStackRankPayoutGroupValueListIter.hasNext(); )
      {
        PromotionStackRankPayoutGroupFormBean promotionStackRankPayoutGroupFormBean = (PromotionStackRankPayoutGroupFormBean)promoStackRankPayoutGroupValueListIter.next();
        int currFromRank = 0;
        int currToRank = 0;

        if ( promotionStackRankPayoutGroupFormBean.getPromoStackRankPayoutValueList() != null && promotionStackRankPayoutGroupFormBean.getPromoStackRankPayoutValueList().size() > 0 )
        {
          PropertyComparator.sort( promotionStackRankPayoutGroupFormBean.getPromoStackRankPayoutValueList(), new MutableSortDefinition( "fromRank", true, true ) );
          PromotionStackRankPayoutFormBean promoStackRankPayoutValueBean = promotionStackRankPayoutGroupFormBean.getPromoPayoutValueList( 0 );

          currFromRank = Integer.parseInt( promoStackRankPayoutValueBean.getFromRank() );
          currToRank = Integer.parseInt( promoStackRankPayoutValueBean.getToRank() );

          if ( currFromRank <= currToRank )
          {
            for ( int i = 1; i < promotionStackRankPayoutGroupFormBean.getPromoStackRankPayoutValueListCount(); i++ )
            {
              PromotionStackRankPayoutFormBean promoStackRankPayoutValueBeanNext = promotionStackRankPayoutGroupFormBean.getPromoPayoutValueList( i );
              int nextFromRank = Integer.parseInt( promoStackRankPayoutValueBeanNext.getFromRank() );
              int nextToRank = Integer.parseInt( promoStackRankPayoutValueBeanNext.getToRank() );
              if ( nextFromRank <= nextToRank )
              {
                if ( nextFromRank > currToRank && nextToRank > currFromRank && nextToRank > currToRank )
                {
                  currFromRank = nextFromRank;
                  currToRank = nextToRank;
                }
                else
                {
                  // ERROR
                  errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "promotion.payout.errors.RANK_OVERLAP_ERROR" ) );
                  isOverlap = true;
                  break;
                }
              }
              else
              {
                // ERROR
                errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "promotion.payout.errors.FROM_RANK_LESS_TO_RANK" ) );
                isOverlap = true;
                break;
              }
              // even if you find one error break out of this validation
              if ( isOverlap )
              {
                break;
              }
            } // end of payout for
          } // end of if
          else
          {
            // Error
            errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "promotion.payout.errors.FROM_RANK_LESS_TO_RANK" ) );
            isOverlap = true;
            break;
          }
        }

        if ( isOverlap )
        {
          break;
        }
      } // end of bean for
    }

    return errors;
  }

  private boolean isStackRankNodeTypeAlreadySelected()
  {
    boolean isNodeAlreadySelected = false;

    if ( this.getPromoStackRankPayoutGroupValueListCount() > 0 )
    {
      PromotionStackRankPayoutGroupFormBean currPromotionStackRankPayoutGroupFormBean = (PromotionStackRankPayoutGroupFormBean)this.getPromoStackRankPayoutGroupValueList().get( 0 );
      String currNodeTypeId = currPromotionStackRankPayoutGroupFormBean.getNodeTypeId();
      if ( currNodeTypeId != null )
      {
        for ( int i = 1; i < this.getPromoStackRankPayoutGroupValueListCount(); i++ )
        {
          PromotionStackRankPayoutGroupFormBean nextPromotionStackRankPayoutGroupFormBean = (PromotionStackRankPayoutGroupFormBean)this.getPromoStackRankPayoutGroupValueList().get( i );
          String nextNodeTypeId = nextPromotionStackRankPayoutGroupFormBean.getNodeTypeId();
          if ( currNodeTypeId.equals( nextNodeTypeId ) )
          {
            // error and break
            isNodeAlreadySelected = true;
            break;
          }
          currNodeTypeId = nextNodeTypeId;
        }
      }
    }

    return isNodeAlreadySelected;
  }

  /**
   * resets the value list with empty PromotionPayoutFormBeans
   * 
   * @param valueListCount
   * @return List
   */
  private List getEmptyValueList( int valueListCount )
  {
    List valueList = new ArrayList();

    for ( int i = 0; i < valueListCount; i++ )
    {
      valueList.add( new PromotionPayoutGroupFormBean() );
    }

    return valueList;
  }

  /**
   * resets the value list with empty PromotionPayoutFormBeans
   * 
   * @param valueListCount
   * @return List
   */
  private List getEmptyPayoutValueList( int valueListCount )
  {
    List valueList = new ArrayList();

    for ( int i = 0; i < valueListCount; i++ )
    {
      valueList.add( new PromotionPayoutFormBean() );
    }

    return valueList;
  }

  /**
   * resets the value list with empty PromotionStackRankPayoutFormBeans
   * 
   * @param valueListCount
   * @return List
   */
  private List getStackRankEmptyValueList( int valueListCount )
  {
    List valueList = new ArrayList();

    for ( int i = 0; i < valueListCount; i++ )
    {
      valueList.add( new PromotionStackRankPayoutGroupFormBean() );
    }

    return valueList;
  }

  /**
   * resets the value list with empty PromotionStackRankPayoutFormBeans
   * 
   * @param valueListCount
   * @return List
   */
  private List getStackRankEmptyPayoutValueList( int valueListCount )
  {
    List valueList = new ArrayList();

    for ( int i = 0; i < valueListCount; i++ )
    {
      valueList.add( new PromotionStackRankPayoutFormBean() );
    }

    return valueList;
  }

  private List<BudgetSegmentValueBean> getEmptyBudgetSegmentValueList( int valueListCount )
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

  public void addEmptyBudgetSegment()
  {
    BudgetSegmentValueBean budgetSegment = new BudgetSegmentValueBean();
    this.budgetSegmentVBList.add( budgetSegment );
  }

  public int getBudgetSegmentVBListSize()
  {
    if ( this.budgetSegmentVBList != null )
    {
      return this.budgetSegmentVBList.size();
    }

    return 0;
  }

  /**
   * adding a payout level for existing node
   */
  public void addPayoutLevelForThisNode()
  {
    PromotionStackRankPayoutFormBean promoPayoutBean = new PromotionStackRankPayoutFormBean();
    PromotionStackRankPayoutGroupFormBean promotionStackRankPayoutGroupFormBean = null; // new
    // PromotionStackRankPayoutGroupFormBean();

    if ( stackRankGroupEditId != null && stackRankGroupEditId.length() > 0 )
    {
      for ( Iterator iter = promoStackRankPayoutGroupValueList.iterator(); iter.hasNext(); )
      {
        promotionStackRankPayoutGroupFormBean = (PromotionStackRankPayoutGroupFormBean)iter.next();
        // if a groupEditId has been set, then that is where the payout belongs
        if ( promotionStackRankPayoutGroupFormBean.getGuid().equals( stackRankGroupEditId ) && !promotionStackRankPayoutGroupFormBean.getPromoStackRankPayoutValueList().contains( promoPayoutBean ) )
        {
          promotionStackRankPayoutGroupFormBean.getPromoStackRankPayoutValueList().add( promoPayoutBean );
        }
      }
    }
    else
    {
      // TODO: verify this will ever happen
      // Create a new group
      promotionStackRankPayoutGroupFormBean = new PromotionStackRankPayoutGroupFormBean();
      promotionStackRankPayoutGroupFormBean.getPromoStackRankPayoutValueList().add( promoPayoutBean );
      this.getPromoStackRankPayoutGroupValueList().add( promotionStackRankPayoutGroupFormBean );
    }
  }

  /**
   * adds a category to the payout list
   * 
   * @param productCategory
   */
  public void addCategory( ProductCategory productCategory )
  {
    PromotionPayoutFormBean promoPayoutBean = new PromotionPayoutFormBean();

    String minQualifier = "";
    boolean retroPayout = true;

    // if there is not a parent category for this category then there is no
    // subcategory so set the subcategory name to "<all>" and the id to -1,
    // also since we are adding a category, set the product name to "<all>"
    // and its id to -1. The id's are set to -1 because that is the value
    // used by the picklists to represent all
    if ( productCategory.getParentProductCategory() == null )
    {
      promoPayoutBean.setCategoryId( productCategory.getId() );
      promoPayoutBean.setCategoryName( productCategory.getName() );
      promoPayoutBean.setSubcategoryId( new Long( -1 ) );
      promoPayoutBean.setSubcategoryName( "<all>" );
      promoPayoutBean.setProductId( new Long( -1 ) );
      promoPayoutBean.setProductName( "<all>" );
    }
    // if there is a parentProductCategory, then this is a subcategory, so set the
    // values for subcategory and category, but since we are still just adding a
    // category set the product values to "<all>" and -1
    else
    {
      promoPayoutBean.setCategoryId( productCategory.getParentProductCategory().getId() );
      promoPayoutBean.setCategoryName( productCategory.getParentProductCategory().getName() );
      promoPayoutBean.setSubcategoryId( productCategory.getId() );
      promoPayoutBean.setSubcategoryName( productCategory.getName() );
      promoPayoutBean.setProductId( new Long( -1 ) );
      promoPayoutBean.setProductName( "<all>" );
    }

    promoPayoutBean.setProductOrCategoryStartDate( this.promotionSubmitStartDate );
    PromotionPayoutGroupFormBean promotionPayoutGroupFormBean = null;

    // this will vary based on payout type.
    // only add the category if it doesn't already exist in the list
    if ( this.payoutType.equals( ONE_TO_ONE ) || this.payoutType.equals( STACK_RANK ) )
    {
      boolean payoutPresent = false;

      Iterator groupIter = this.getPromoPayoutGroupValueList().iterator();
      while ( groupIter.hasNext() )
      {
        promotionPayoutGroupFormBean = (PromotionPayoutGroupFormBean)groupIter.next();
        if ( promotionPayoutGroupFormBean.getPromoPayoutValueList().contains( promoPayoutBean ) )
        {
          payoutPresent = true;
        }
      }

      if ( !payoutPresent )
      {
        promotionPayoutGroupFormBean = new PromotionPayoutGroupFormBean();
        promotionPayoutGroupFormBean.getPromoPayoutValueList().add( promoPayoutBean );
        this.getPromoPayoutGroupValueList().add( promotionPayoutGroupFormBean );
      }
    }
    else if ( this.payoutType.equals( CROSS_SELL ) || this.payoutType.equals( TIERED ) )
    {
      if ( groupEditId != null && groupEditId.length() > 0 )
      {
        for ( Iterator iter = promoPayoutGroupValueList.iterator(); iter.hasNext(); )
        {
          promotionPayoutGroupFormBean = (PromotionPayoutGroupFormBean)iter.next();
          if ( promotionPayoutGroupFormBean.getPromoPayoutValueList().contains( promoPayoutBean ) )
          {
            if ( this.payoutType.equals( TIERED ) )
            {
              minQualifier = promotionPayoutGroupFormBean.getMinimumQualifier();
              retroPayout = promotionPayoutGroupFormBean.getRetroPayout();
            }
            for ( Iterator payoutIter = promotionPayoutGroupFormBean.getPromoPayoutValueList().iterator(); payoutIter.hasNext(); )
            {
              PromotionPayoutFormBean curPromoPayoutBean = (PromotionPayoutFormBean)payoutIter.next();
              if ( curPromoPayoutBean.equals( promoPayoutBean ) )
              {
                promoPayoutBean.setProductOrCategoryStartDate( curPromoPayoutBean.getProductOrCategoryStartDate() );
                promoPayoutBean.setProductOrCategoryEndDate( curPromoPayoutBean.getProductOrCategoryEndDate() );
              }
            }
          }
          // if a groupEditId has been set, then that is where the payout belongs
          if ( promotionPayoutGroupFormBean.getGuid().equals( groupEditId ) && !promotionPayoutGroupFormBean.getPromoPayoutValueList().contains( promoPayoutBean ) )
          {
            // Bug fix # 21362
            if ( this.payoutType.equals( TIERED ) )
            {
              minQualifier = promotionPayoutGroupFormBean.getMinimumQualifier();
              retroPayout = promotionPayoutGroupFormBean.getRetroPayout();
            }
            promotionPayoutGroupFormBean.setMinimumQualifier( minQualifier );
            promotionPayoutGroupFormBean.setRetroPayout( retroPayout );
            promotionPayoutGroupFormBean.getPromoPayoutValueList().add( promoPayoutBean );
          }
        }
      }
      else
      {
        // Create a new group
        promotionPayoutGroupFormBean = new PromotionPayoutGroupFormBean();
        promotionPayoutGroupFormBean.getPromoPayoutValueList().add( promoPayoutBean );
        this.getPromoPayoutGroupValueList().add( promotionPayoutGroupFormBean );
      }
    }
    buildJavascriptArray();
  }

  /**
   * adds products to the payout list
   * 
   * @param productList
   */
  public void addProducts( List productList )
  {
    if ( productList == null )
    {
      return;
    }

    String minQualifier = "";
    boolean retroPayout = true;

    for ( int i = 0; i < productList.size(); i++ )
    {
      Product product = (Product)productList.get( i );
      PromotionPayoutFormBean promoPayoutBean = new PromotionPayoutFormBean();
      // if there is not a parentProductCategory only set the category name and id
      if ( product.getProductCategory().getParentProductCategory() == null )
      {
        promoPayoutBean.setCategoryId( product.getProductCategory().getId() );
        promoPayoutBean.setCategoryName( product.getProductCategory().getName() );
        promoPayoutBean.setSubcategoryId( new Long( 0 ) );
        promoPayoutBean.setSubcategoryName( "" );
      }
      // if there is a parentProductCategory, set both the subcategory and category
      // name and id
      else
      {
        promoPayoutBean.setCategoryId( product.getProductCategory().getParentProductCategory().getId() );
        promoPayoutBean.setCategoryName( product.getProductCategory().getParentProductCategory().getName() );
        promoPayoutBean.setSubcategoryId( product.getProductCategory().getId() );
        promoPayoutBean.setSubcategoryName( product.getProductCategory().getName() );
      }

      promoPayoutBean.setProductId( product.getId() );
      promoPayoutBean.setProductName( product.getName() );
      promoPayoutBean.setProductOrCategoryStartDate( this.promotionSubmitStartDate );

      PromotionPayoutGroupFormBean promotionPayoutGroupFormBean = null;
      // this will vary based on payout type.
      // only add the category if it doesn't already exist in the list
      if ( this.payoutType.equals( ONE_TO_ONE ) || this.payoutType.equals( STACK_RANK ) )
      {
        boolean payoutPresent = false;

        Iterator groupIter = this.getPromoPayoutGroupValueList().iterator();
        while ( groupIter.hasNext() )
        {
          promotionPayoutGroupFormBean = (PromotionPayoutGroupFormBean)groupIter.next();
          if ( promotionPayoutGroupFormBean.getPromoPayoutValueList().contains( promoPayoutBean ) )
          {
            payoutPresent = true;
          }
        }

        if ( !payoutPresent )
        {
          // TODO: add logic to check if it is already in the list
          promotionPayoutGroupFormBean = new PromotionPayoutGroupFormBean();
          promotionPayoutGroupFormBean.getPromoPayoutValueList().add( promoPayoutBean );
          this.getPromoPayoutGroupValueList().add( promotionPayoutGroupFormBean );
        }
      }
      else if ( this.payoutType.equals( CROSS_SELL ) || this.payoutType.equals( TIERED ) )
      {
        if ( groupEditId != null && groupEditId.length() > 0 )
        {
          for ( Iterator iter = promoPayoutGroupValueList.iterator(); iter.hasNext(); )
          {
            promotionPayoutGroupFormBean = (PromotionPayoutGroupFormBean)iter.next();
            if ( promotionPayoutGroupFormBean.getPromoPayoutValueList().contains( promoPayoutBean ) )
            {
              if ( this.payoutType.equals( TIERED ) )
              {
                minQualifier = promotionPayoutGroupFormBean.getMinimumQualifier();
                retroPayout = promotionPayoutGroupFormBean.getRetroPayout();
              }
              for ( Iterator payoutIter = promotionPayoutGroupFormBean.getPromoPayoutValueList().iterator(); payoutIter.hasNext(); )
              {
                PromotionPayoutFormBean curPromoPayoutBean = (PromotionPayoutFormBean)payoutIter.next();
                if ( curPromoPayoutBean.equals( promoPayoutBean ) )
                {
                  promoPayoutBean.setProductOrCategoryStartDate( curPromoPayoutBean.getProductOrCategoryStartDate() );
                  promoPayoutBean.setProductOrCategoryEndDate( curPromoPayoutBean.getProductOrCategoryEndDate() );
                }
              }
            }
            // if a groupEditId has been set, then that is where the payout belongs

            if ( promotionPayoutGroupFormBean.getGuid().equals( groupEditId ) && !promotionPayoutGroupFormBean.getPromoPayoutValueList().contains( promoPayoutBean ) )
            {
              // Bug fix # 21362
              if ( this.payoutType.equals( TIERED ) )
              {
                minQualifier = promotionPayoutGroupFormBean.getMinimumQualifier();
                retroPayout = promotionPayoutGroupFormBean.getRetroPayout();
              }
              promotionPayoutGroupFormBean.setMinimumQualifier( minQualifier );
              promotionPayoutGroupFormBean.setRetroPayout( retroPayout );
              promotionPayoutGroupFormBean.getPromoPayoutValueList().add( promoPayoutBean );
            }
          }
        } // end if(groupEditId)
      }
    }
    buildJavascriptArray();
  }

  /**
   * Loads the promotion and any payout/budget records
   * 
   * @param promo
   */
  public void loadPromotion( Promotion promo )
  {
    ProductClaimPromotion promotion = (ProductClaimPromotion)promo;

    this.promotionId = promotion.getId();
    this.promotionName = promotion.getName();
    this.promotionTypeName = promotion.getPromotionType().getName();
    this.promotionTypeCode = promotion.getPromotionType().getCode();
    this.promotionStatus = promotion.getPromotionStatus().getCode();
    this.version = promotion.getVersion();
    this.teamUsed = promotion.isTeamUsed();

    if ( promotion.getAwardType() != null )
    {
      this.awardTypeName = promotion.getAwardType().getName();
      this.awardType = promotion.getAwardType().getCode();
    }
    if ( promotion.getPayoutType() != null )
    {
      this.payoutType = promotion.getPayoutType().getCode();
      this.prevPayoutType = this.payoutType;
    }

    if ( promotion.getDefaultQuantity() == null || promotion.getDefaultQuantity().intValue() < 1 )
    {
      this.allowDefaultQuantity = false;
    }
    else
    {
      this.allowDefaultQuantity = true;
      this.defaultQuantity = promotion.getDefaultQuantity();
    }

    // Stack Rank Payout Type does not use Break the Bank Budget
    if ( promotion.getPayoutType() != null && !PromotionPayoutType.STACK_RANK.equals( promotion.getPayoutType().getCode() ) )
    {
      // Break the bank Budget
      if ( promotion.isFileLoadEntry() )
      {
        this.promotionIssuanceTypeCode = PromotionIssuanceType.FILE_LOAD;
      }
      if ( promotion.isOnlineEntry() )
      {
        this.promotionIssuanceTypeCode = PromotionIssuanceType.ONLINE;
      }

      this.budgetOption = BUDGET_NONE;

      // Set Budget data only if the awardType is points and method of entry is not file load
      // changed PromotionAwardsType lookup to getCode instead of getName
      if ( awardType.equals( PromotionAwardsType.lookup( PromotionAwardsType.POINTS ).getCode() ) && !PromotionIssuanceType.FILE_LOAD.equals( promotionIssuanceTypeCode ) )
      {
        BudgetMaster budgetMaster = promotion.getBudgetMaster();
        if ( budgetMaster != null )
        {
          this.budgetMasterId = budgetMaster.getId();
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
            // Break the Bank Budget is always Hard Cap, so there are no Soft Cap Approvers
          }
        }

        defaultEmptyBudgetSegment();
      }
      // END Break the bank Budget
    }

    this.promotionSubmitStartDate = DateUtils.toDisplayString( promotion.getSubmissionStartDate() );
    this.promotionSubmitEndDate = DateUtils.toDisplayString( promotion.getSubmissionEndDate() );
    if ( promotion.getParentPromotion() != null )
    {
      this.hasParent = true;
      this.parentPayoutType = promotion.getParentPromotion().getPayoutType().getCode();
      this.promotionSubmitStartDate = DateUtils.toDisplayString( promotion.getParentPromotion().getSubmissionStartDate() );
    }

    this.managerOverride = promotion.isPayoutManager();
    if ( promotion.isPayoutManager() )
    {
      if ( promotion.getPayoutManagerPercent() != null )
      {
        this.managerOverridePercent = promotion.getPayoutManagerPercent().toString();
      }
      if ( promotion.getPayoutManagerPeriod() != null )
      {
        this.managerOverrideFrequency = promotion.getPayoutManagerPeriod().getCode();
      }
    }

    if ( promotion.getPayoutType() != null && promotion.getPayoutType().getCode().equals( "tiered" ) )
    {
      // must update promotion.payoutCarryOver, methods do not currently exist
      this.carryoverAllowed = promotion.isPayoutCarryOver();
    }
    // satckrank payout type fields
    if ( promotion.getPayoutType() != null && PromotionPayoutType.STACK_RANK.equals( promotion.getPayoutType().getCode() ) )
    {
      this.displayStackRankFactor = promotion.isDisplayStackRankFactor();
      this.displayFullListLinkToParticipants = promotion.isDisplayFullListLinkToParticipants();
      if ( promotion.getStackRankApprovalType() != null && StackRankApprovalType.MANUAL_APPROVAL.equals( promotion.getStackRankApprovalType().getCode() ) )
      {
        this.stackRankApprovalType = true;
      }
      else
      {
        this.stackRankApprovalType = false;
      }

      if ( promotion.getStackRankFactorType() != null && StackRankFactorType.NUMERIC_CLAIM_ELEMENT.equals( promotion.getStackRankFactorType().getCode() ) )
      {
        ClaimFormStepElement claimFormStepElement = promotion.getStackRankClaimFormStepElement();
        if ( claimFormStepElement != null && claimFormStepElement.getId() != null )
        {
          this.stackRankFactorType = String.valueOf( claimFormStepElement.getId().longValue() );
        }
        else
        {
          this.stackRankFactorType = StackRankFactorType.QUANTITY_SOLD;
        }

      }
      else
      {
        // this should be the default value
        this.stackRankFactorType = StackRankFactorType.QUANTITY_SOLD;
      }
    }

    if ( !this.getMethod().equals( "payoutTypeChange" ) || promotion.getPayoutType() != null && this.getPayoutType().equals( promotion.getPayoutType().getCode() ) )
    {
      if ( !hasParent )
      {
        buildParentGroups( promotion );
        if ( promotion.getPayoutType() != null && PromotionPayoutType.STACK_RANK.equals( promotion.getPayoutType().getCode() ) )
        {
          buildStackRankParentGroups( promotion );
        }
      }
    }
    if ( hasParent )
    {
      setChildGroupPayouts( promotion );
      if ( promotion.getPayoutType() != null && PromotionPayoutType.STACK_RANK.equals( promotion.getPayoutType().getCode() ) )
      {
        buildStackRankParentGroups( promotion );
      }
    }
    buildJavascriptArray();
  }

  public void buildParentGroups( ProductClaimPromotion promotion )
  {
    List promotionGroups = null;

    promotionGroups = promotion.getPromotionPayoutGroups();

    if ( promotionGroups != null && promotionGroups.size() != 0 )
    {
      if ( promoPayoutGroupValueList == null )
      {
        promoPayoutGroupValueList = new ArrayList();
      }
      else
      {
        promoPayoutGroupValueList.clear();
      }

      Iterator promotionGroupsIter = promotionGroups.iterator();
      while ( promotionGroupsIter.hasNext() )
      {
        PromotionPayoutGroup promotionPayoutGroup = (PromotionPayoutGroup)promotionGroupsIter.next();
        PromotionPayoutGroupFormBean promotionPayoutGroupFormBean = new PromotionPayoutGroupFormBean();
        promotionPayoutGroupFormBean.setPromoPayoutGroupId( promotionPayoutGroup.getId() );
        promotionPayoutGroupFormBean.setGuid( promotionPayoutGroup.getGuid() );
        promotionPayoutGroupFormBean.setParentQuantity( String.valueOf( promotionPayoutGroup.getQuantity() ) );
        if ( payoutType.equals( PromotionPayoutType.ONE_TO_ONE ) || payoutType.equals( PromotionPayoutType.TIERED ) )
        {
          promotionPayoutGroupFormBean.setMinimumQualifier( promotionPayoutGroup.getMinimumQualifier().toString() );
          promotionPayoutGroupFormBean.setRetroPayout( promotionPayoutGroup.getRetroPayout().booleanValue() );

        }
        promotionPayoutGroupFormBean.setSubmitterParentPayout( String.valueOf( promotionPayoutGroup.getSubmitterPayout() ) );
        Integer teamMemberPayout = promotionPayoutGroup.getTeamMemberPayout();
        promotionPayoutGroupFormBean.setTeamMemberParentPayout( teamMemberPayout != null ? teamMemberPayout.toString() : "" );

        if ( promotionPayoutGroup.getPromotionPayouts() != null && promotionPayoutGroup.getPromotionPayouts().size() > 0 )
        {
          ArrayList promotionPayoutsFormBeans = new ArrayList();
          Iterator promotionPayoutsIter = promotionPayoutGroup.getPromotionPayouts().iterator();

          while ( promotionPayoutsIter.hasNext() )
          {
            PromotionPayout promoPayout = (PromotionPayout)promotionPayoutsIter.next();
            PromotionPayoutFormBean promoPayoutFormBean = buildPayoutBean( promoPayout );

            promoPayoutFormBean.setPromoPayoutId( promoPayout.getId() );
            promoPayoutFormBean.setVersion( promoPayout.getVersion() );

            promotionPayoutsFormBeans.add( promoPayoutFormBean );
          }

          promotionPayoutGroupFormBean.setPromoPayoutValueList( promotionPayoutsFormBeans );
          this.promoPayoutGroupValueList.add( promotionPayoutGroupFormBean );
        }
      } // end for(...promotionGroups.iterator()...)
    }
  }

  /**
   * Builds a map of group dependencies used in javascript so that if a payout exists for the same
   * product or category in multiple groups for a tiered payout type the minimum qualifier and retro
   * payout for both groups are forced to be the same.
   */
  public void buildJavascriptArray()
  {
    Map rewrittenMap = new HashMap();
    if ( this.promoPayoutGroupValueList != null && this.promoPayoutGroupValueList.size() > 0 )
    {
      for ( int i = 0; i < promoPayoutGroupValueList.size(); i++ )
      {
        PromotionPayoutGroupFormBean promoGroupBean = (PromotionPayoutGroupFormBean)this.promoPayoutGroupValueList.get( i );
        rewrittenMap.put( promoGroupBean.getGuid(), new Integer( i ) );
        groupDependentMap.put( new Integer( i ), new ArrayList() );
      }
    }

    if ( this.promoPayoutGroupValueList != null && this.promoPayoutGroupValueList.size() > 0 )
    {
      List copyOfGroupList = new ArrayList( this.promoPayoutGroupValueList );
      Iterator groupIter = copyOfGroupList.iterator();
      while ( groupIter.hasNext() )
      {
        PromotionPayoutGroupFormBean promoGroupBean = (PromotionPayoutGroupFormBean)groupIter.next();
        if ( promoGroupBean.getPromoPayoutValueList() != null && promoGroupBean.getPromoPayoutValueList().size() > 0 )
        {
          Iterator productIter = promoGroupBean.getPromoPayoutValueList().iterator();
          while ( productIter.hasNext() )
          {
            PromotionPayoutFormBean promoPayoutBean = (PromotionPayoutFormBean)productIter.next();
            Iterator dependentGroupIter = copyOfGroupList.iterator();
            while ( dependentGroupIter.hasNext() )
            {
              PromotionPayoutGroupFormBean dependentPromoGroupBean = (PromotionPayoutGroupFormBean)dependentGroupIter.next();
              if ( promoGroupBean != dependentPromoGroupBean )
              {
                if ( dependentPromoGroupBean.getPromoPayoutValueList().contains( promoPayoutBean ) )
                {
                  ( (ArrayList)groupDependentMap.get( rewrittenMap.get( promoGroupBean.getGuid() ) ) ).add( rewrittenMap.get( dependentPromoGroupBean.getGuid() ) );
                  ( (ArrayList)groupDependentMap.get( rewrittenMap.get( dependentPromoGroupBean.getGuid() ) ) ).add( rewrittenMap.get( promoGroupBean.getGuid() ) );
                }
              }
            }
          }
        }
        groupIter.remove();
      }
    }
  }

  public void buildStackRankParentGroups( ProductClaimPromotion promotion )
  {
    Set promotionGroups = null;

    promotionGroups = promotion.getStackRankPayoutGroups();

    if ( promotionGroups != null && promotionGroups.size() != 0 )
    {
      if ( promoStackRankPayoutGroupValueList == null )
      {
        promoStackRankPayoutGroupValueList = new ArrayList();
      }
      else
      {
        promoStackRankPayoutGroupValueList.clear();
      }

      Iterator promotionGroupsIter = promotionGroups.iterator();
      while ( promotionGroupsIter.hasNext() )
      {
        StackRankPayoutGroup promotionStackRankPayoutGroup = (StackRankPayoutGroup)promotionGroupsIter.next();
        PromotionStackRankPayoutGroupFormBean promotionStackRankPayoutGroupFormBean = new PromotionStackRankPayoutGroupFormBean();
        promotionStackRankPayoutGroupFormBean.setPromoPayoutGroupId( promotionStackRankPayoutGroup.getId() );
        promotionStackRankPayoutGroupFormBean.setNodeTypeName( promotionStackRankPayoutGroup.getNodeType().getI18nName() );
        promotionStackRankPayoutGroupFormBean.setNodeTypeId( String.valueOf( promotionStackRankPayoutGroup.getNodeType().getId() ) );
        promotionStackRankPayoutGroupFormBean.setSubmittersRankName( promotionStackRankPayoutGroup.getSubmittersToRankType().getName() );
        promotionStackRankPayoutGroupFormBean.setSubmittersRankNameId( promotionStackRankPayoutGroup.getSubmittersToRankType().getCode() );

        // promotionPayoutGroupFormBean.setGuid( promotionPayoutGroup.getGuid() );

        if ( promotionStackRankPayoutGroup.getPromotionStackRankPayout() != null && promotionStackRankPayoutGroup.getPromotionStackRankPayout().size() > 0 )
        {
          ArrayList promotionPayoutsFormBeans = new ArrayList();
          Iterator promotionPayoutsIter = promotionStackRankPayoutGroup.getPromotionStackRankPayout().iterator();

          while ( promotionPayoutsIter.hasNext() )
          {
            StackRankPayout promoPayout = (StackRankPayout)promotionPayoutsIter.next();
            PromotionStackRankPayoutFormBean promoPayoutFormBean = buildStackRankPayoutBean( promoPayout );

            promoPayoutFormBean.setPromoPayoutId( promoPayout.getId() );
            promoPayoutFormBean.setVersion( promoPayout.getVersion() );

            promotionPayoutsFormBeans.add( promoPayoutFormBean );
          }

          promotionStackRankPayoutGroupFormBean.setPromoStackRankPayoutValueList( promotionPayoutsFormBeans );
          this.promoStackRankPayoutGroupValueList.add( promotionStackRankPayoutGroupFormBean );
        }
      } // end for(...promotionGroups.iterator()...)
    }
  }

  private PromotionPayoutFormBean buildPayoutBean( PromotionPayout promoPayout )
  {
    PromotionPayoutFormBean promoFormBean = new PromotionPayoutFormBean();

    // if the payout has a category check to see if it has a parentProductCategory
    // and set the appropriate information
    if ( promoPayout.getProductCategory() != null )
    {
      if ( promoPayout.getProductCategory().getParentProductCategory() != null )
      {
        promoFormBean.setSubcategoryId( promoPayout.getProductCategory().getId() );
        promoFormBean.setSubcategoryName( promoPayout.getProductCategory().getName() );
        promoFormBean.setCategoryId( promoPayout.getProductCategory().getParentProductCategory().getId() );
        promoFormBean.setCategoryName( promoPayout.getProductCategory().getParentProductCategory().getName() );
        promoFormBean.setProductId( new Long( -1 ) );
        promoFormBean.setProductName( "<all>" );
      }
      else
      {
        promoFormBean.setCategoryId( promoPayout.getProductCategory().getId() );
        promoFormBean.setCategoryName( promoPayout.getProductCategory().getName() );
        promoFormBean.setSubcategoryId( new Long( -1 ) );
        promoFormBean.setSubcategoryName( "<all>" );
        promoFormBean.setProductId( new Long( -1 ) );
        promoFormBean.setProductName( "<all>" );
      }
    }
    // if the payout has a product, set the category information based on
    // the product
    if ( promoPayout.getProduct() != null )
    {
      promoFormBean.setProductId( promoPayout.getProduct().getId() );
      promoFormBean.setProductName( promoPayout.getProduct().getName() );

      if ( promoPayout.getProduct().getProductCategory().getParentProductCategory() != null )
      {
        promoFormBean.setSubcategoryId( promoPayout.getProduct().getProductCategory().getId() );
        promoFormBean.setSubcategoryName( promoPayout.getProduct().getProductCategory().getName() );
        promoFormBean.setCategoryId( promoPayout.getProduct().getProductCategory().getParentProductCategory().getId() );
        promoFormBean.setCategoryName( promoPayout.getProduct().getProductCategory().getParentProductCategory().getName() );
      }
      else
      {
        promoFormBean.setCategoryId( promoPayout.getProduct().getProductCategory().getId() );
        promoFormBean.setCategoryName( promoPayout.getProduct().getProductCategory().getName() );
      }
    }

    promoFormBean.setProductOrCategoryStartDate( DateUtils.toDisplayString( promoPayout.getProductOrCategoryStartDate() ) );
    promoFormBean.setProductOrCategoryEndDate( DateUtils.toDisplayString( promoPayout.getProductOrCategoryEndDate() ) );

    if ( getPayoutType().equals( CROSS_SELL ) )
    {
      promoFormBean.setParentQuantity( String.valueOf( promoPayout.getQuantity() ) );
    }
    return promoFormBean;
  }

  private PromotionStackRankPayoutFormBean buildStackRankPayoutBean( StackRankPayout promoPayout )
  {
    PromotionStackRankPayoutFormBean promoFormBean = new PromotionStackRankPayoutFormBean();
    promoFormBean.setFromRank( String.valueOf( promoPayout.getStartRank() ) );
    promoFormBean.setToRank( String.valueOf( promoPayout.getEndRank() ) );
    promoFormBean.setPayoutAmount( String.valueOf( promoPayout.getPayout() ) );
    return promoFormBean;
  }

  /**
   * Builds a set of payout objects based on the parent promotion, this set will be the "pool" of
   * payouts the child can select from.
   * 
   * @param promotion
   */
  private Set buildChildPayoutPool( ProductClaimPromotion promotion )
  {
    Set availablePayoutSet = new LinkedHashSet();
    Iterator parentPromoGroupIter = promotion.getParentPromotion().getPromotionPayoutGroups().iterator();
    while ( parentPromoGroupIter.hasNext() )
    {
      PromotionPayoutGroup promoPayoutGroup = (PromotionPayoutGroup)parentPromoGroupIter.next();
      List promoPayoutList = promoPayoutGroup.getPromotionPayouts();
      Iterator parentPayoutIter = promoPayoutList.iterator();
      while ( parentPayoutIter.hasNext() )
      {
        PromotionPayout promoPayout = (PromotionPayout)parentPayoutIter.next();
        PromotionPayoutFormBean promoPayoutFormBean = buildPayoutBean( promoPayout );

        // always use child promotion claim submission dates for product dates
        promoPayoutFormBean.setProductOrCategoryStartDate( DateUtils.toDisplayString( promotion.getSubmissionStartDate() ) );
        promoPayoutFormBean.setProductOrCategoryEndDate( DateUtils.toDisplayString( promotion.getSubmissionEndDate() ) );

        availablePayoutSet.add( promoPayoutFormBean );
      }
    }

    return availablePayoutSet;
  }

  /**
   * Builds a list of promoPayoutGroupValue objects for the child payouts
   * 
   * @param promotion
   * @param numberOfGroups
   */
  private void buildChildGroups( ProductClaimPromotion promotion, int numberOfGroups )
  {
    if ( promoPayoutGroupValueList == null )
    {
      promoPayoutGroupValueList = new ArrayList();
    }
    else
    {
      promoPayoutGroupValueList.clear();
    }

    if ( numberOfGroups != 0 )
    {
      for ( int i = 0; i < numberOfGroups; i++ )
      {
        PromotionPayoutGroupFormBean promoPayoutGroupFormBean = new PromotionPayoutGroupFormBean();
        buildChildGroup( promotion, promoPayoutGroupFormBean );
        promoPayoutGroupValueList.add( promoPayoutGroupFormBean );
      }
    }
    else
    {
      Iterator payoutIter = buildChildPayoutPool( promotion ).iterator();
      while ( payoutIter.hasNext() )
      {
        PromotionPayoutGroupFormBean promoPayoutGroupFormBean = new PromotionPayoutGroupFormBean();
        PromotionPayoutFormBean payoutBean = (PromotionPayoutFormBean)payoutIter.next();
        promoPayoutGroupFormBean.getPromoPayoutValueList().add( payoutBean );
        promoPayoutGroupValueList.add( promoPayoutGroupFormBean );
      }
    }
  }

  private PromotionPayoutGroupFormBean buildChildGroup( ProductClaimPromotion promotion, PromotionPayoutGroupFormBean groupBean )
  {
    Set availablePayouts = buildChildPayoutPool( promotion );

    Iterator payoutIter = availablePayouts.iterator();
    while ( payoutIter.hasNext() )
    {
      PromotionPayoutFormBean payoutBean = new PromotionPayoutFormBean();
      PromotionPayoutFormBean payoutBeanFromPool = (PromotionPayoutFormBean)payoutIter.next();
      payoutBean.setCategoryId( payoutBeanFromPool.getCategoryId() );
      payoutBean.setCategoryName( payoutBeanFromPool.getCategoryName() );
      payoutBean.setSubcategoryId( payoutBeanFromPool.getSubcategoryId() );
      payoutBean.setSubcategoryName( payoutBeanFromPool.getSubcategoryName() );
      payoutBean.setProductId( payoutBeanFromPool.getProductId() );
      payoutBean.setProductName( payoutBeanFromPool.getProductName() );
      payoutBean.setProductOrCategoryStartDate( payoutBeanFromPool.getProductOrCategoryStartDate() );
      payoutBean.setProductOrCategoryEndDate( payoutBeanFromPool.getProductOrCategoryEndDate() );

      groupBean.getPromoPayoutValueList().add( payoutBean );
    }

    return groupBean;
  }

  /**
   * Loads the child payout records.
   * 
   * @param promotion
   */
  public void setChildGroupPayouts( ProductClaimPromotion promotion )
  {
    // Get the list of Child promotion payout groups
    List childPromotionGroupList = promotion.getPromotionPayoutGroups();

    if ( getPayoutType().equals( ONE_TO_ONE ) || getPayoutType().equals( STACK_RANK ) )
    {
      buildChildGroups( promotion, 0 );
      if ( childPromotionGroupList != null && childPromotionGroupList.size() > 0 )
      {
        Iterator childPromoGroupIter = childPromotionGroupList.iterator();
        while ( childPromoGroupIter.hasNext() )
        {
          PromotionPayoutGroup childPromoPayoutGroup = (PromotionPayoutGroup)childPromoGroupIter.next();
          Iterator parentPromoGroupIter = promoPayoutGroupValueList.iterator();
          while ( parentPromoGroupIter.hasNext() )
          {
            PromotionPayoutGroupFormBean promoGroupFormBean = (PromotionPayoutGroupFormBean)parentPromoGroupIter.next();
            List childPayoutList = childPromoPayoutGroup.getPromotionPayouts();
            if ( childPayoutList != null && childPayoutList.size() > 0 )
            {
              Iterator childPayoutIter = childPayoutList.iterator();
              while ( childPayoutIter.hasNext() )
              {
                PromotionPayout childPayout = (PromotionPayout)childPayoutIter.next();

                Iterator parentPayoutIter = promoGroupFormBean.getPromoPayoutValueList().iterator();

                while ( parentPayoutIter.hasNext() )
                {
                  setIfEqual( (PromotionPayoutFormBean)parentPayoutIter.next(), childPayout, promoGroupFormBean, childPromoPayoutGroup );
                }
              }
            }
          }
        }
      }
    }
    else
    {
      if ( promotion.getPromotionPayoutGroups().size() > 1 )
      {
        buildChildGroups( promotion, promotion.getPromotionPayoutGroups().size() );
      }
      else
      {
        buildChildGroups( promotion, 1 );
      }
      if ( childPromotionGroupList != null && childPromotionGroupList.size() > 0 )
      {
        for ( int i = 0; i < childPromotionGroupList.size(); i++ )
        {
          PromotionPayoutGroup childPromoPayoutGroup = (PromotionPayoutGroup)childPromotionGroupList.get( i );
          PromotionPayoutGroupFormBean promoGroupFormBean = (PromotionPayoutGroupFormBean)promoPayoutGroupValueList.get( i );
          List childPayoutList = childPromoPayoutGroup.getPromotionPayouts();
          if ( childPayoutList != null && childPayoutList.size() > 0 )
          {
            Iterator childPayoutIter = childPayoutList.iterator();
            while ( childPayoutIter.hasNext() )
            {
              PromotionPayout childPayout = (PromotionPayout)childPayoutIter.next();

              Iterator parentPayoutIter = promoGroupFormBean.getPromoPayoutValueList().iterator();

              while ( parentPayoutIter.hasNext() )
              {
                setIfEqual( (PromotionPayoutFormBean)parentPayoutIter.next(), childPayout, promoGroupFormBean, childPromoPayoutGroup );
              }
            }
          }
        }
      }
    }
  }

  private void setIfEqual( PromotionPayoutFormBean parentPayout, PromotionPayout childPayout, PromotionPayoutGroupFormBean promoGroupFormBean, PromotionPayoutGroup childPromoPayoutGroup )
  {
    boolean areEqual = false;
    if ( childPayout.getProductCategory() != null && parentPayout.getCategoryId().longValue() != -1 )
    {
      if ( childPayout.getProductCategory().getParentProductCategory() != null && parentPayout.getSubcategoryId().longValue() != -1 )
      {
        if ( childPayout.getProductCategory().getId().equals( parentPayout.getSubcategoryId() ) )
        {
          areEqual = true;
        }
      }
      else
      {
        if ( childPayout.getProductCategory().getId().equals( parentPayout.getCategoryId() ) && parentPayout.getProductId().longValue() == -1 )
        {
          areEqual = true;
        }
      }
    }
    if ( childPayout.getProduct() != null && parentPayout.getProductId().longValue() != -1 )
    {
      if ( childPayout.getProduct().getId().equals( parentPayout.getProductId() ) )
      {
        areEqual = true;
      }
    }
    if ( areEqual )
    {
      if ( payoutType.equals( PromotionPayoutType.ONE_TO_ONE ) || payoutType.equals( PromotionPayoutType.TIERED ) )
      {
        promoGroupFormBean.setMinimumQualifier( String.valueOf( childPromoPayoutGroup.getMinimumQualifier() ) );
        promoGroupFormBean.setRetroPayout( childPromoPayoutGroup.getRetroPayout().booleanValue() );
      }
      promoGroupFormBean.setSubmitterChildPayout( String.valueOf( childPromoPayoutGroup.getSubmitterPayout() ) );
      promoGroupFormBean.setTeamMemberChildPayout( String.valueOf( childPromoPayoutGroup.getTeamMemberPayout() ) );
      promoGroupFormBean.setPromoPayoutGroupId( childPromoPayoutGroup.getId() );
      promoGroupFormBean.setGuid( childPromoPayoutGroup.getGuid() );
      promoGroupFormBean.setPromoPayoutGroupId( childPromoPayoutGroup.getId() );
      parentPayout.setPromoPayoutId( childPayout.getId() );
      parentPayout.setVersion( childPayout.getVersion() );
      parentPayout.setIncludePayout( true );
      if ( getPayoutType().equals( CROSS_SELL ) )
      {
        parentPayout.setChildQuantity( String.valueOf( childPayout.getQuantity() ) );
      }
      else
      {
        promoGroupFormBean.setChildQuantity( String.valueOf( childPromoPayoutGroup.getQuantity() ) );
      }
    }
  }

  public boolean isCreateNewBudgetMaster()
  {
    return this.budgetOption.equals( BUDGET_NEW );
  }

  public boolean isChildsBudgetSameAsParents()
  {
    return this.budgetOption.equals( BUDGET_SAME_AS_PARENT );
  }

  /**
   * Creates a detatched Promotion Domain Object that will later be synchronized with a looked up
   * promotion object in the service
   * 
   * @return Promotion
   */
  public Promotion toDomainObject()
  {
    // Create a new Promotion since one was not passed in
    return toDomainObject( new ProductClaimPromotion() );
  }

  /**
   * Creates a detatched Promotion Domain Object that will later be synchronized with a looked up
   * promotion object in the service
   * 
   * @param promotion
   * @return Promotion
   */
  public Promotion toDomainObject( ProductClaimPromotion promotion )
  {
    promotion.setId( getPromotionId() );
    promotion.setPromotionType( PromotionType.lookup( getPromotionTypeCode() ) );
    promotion.setPayoutType( PromotionPayoutType.lookup( getPayoutType() ) );
    promotion.setPromotionStatus( PromotionStatusType.lookup( getPromotionStatus() ) );
    promotion.setVersion( getVersion() );
    promotion.setPayoutCarryOver( this.carryoverAllowed );

    if ( this.allowDefaultQuantity )
    {
      promotion.setDefaultQuantity( this.defaultQuantity );
    }
    else
    {
      promotion.setDefaultQuantity( 0 );
    }

    // Stack Rank Payout does not ues Break the Bank Budget
    if ( !getPayoutType().equals( STACK_RANK ) )
    {
      // Set Break the Bank Budget
      // changed PromotionAwardsType lookup to getCode instead of getName
      if ( !this.awardType.equals( PromotionAwardsType.lookup( PromotionAwardsType.POINTS ).getCode() ) || this.budgetOption.equals( BUDGET_NONE ) )
      {
        promotion.setBudgetMaster( null );
      }
      else if ( this.budgetOption.equals( BUDGET_EXISTING ) )
      {
        BudgetMaster budgetMaster = new BudgetMaster();
        budgetMaster.setId( this.budgetMasterId );
        promotion.setBudgetMaster( budgetMaster );
      }

      // to set up a default empty budget segment, adding if to avoid duplication
      if ( budgetSegmentVBList.size() == 0 )
      {
        defaultEmptyBudgetSegment();
      }
      // END Break the Bank Budget
    }

    promotion.setPayoutManager( isManagerOverride() );
    if ( isManagerOverride() )
    {
      promotion.setPayoutManagerPercent( new Integer( getManagerOverridePercent() ) );
      promotion.setPayoutManagerPeriod( PromoMgrPayoutFreqType.lookup( getManagerOverrideFrequency() ) );
    }
    // stackrank
    // we want default to be quantity sold
    if ( StringUtils.isNotEmpty( getStackRankFactorType() ) && !StackRankFactorType.QUANTITY_SOLD.equals( getStackRankFactorType() ) )
    {
      promotion.setStackRankFactorType( StackRankFactorType.lookup( StackRankFactorType.NUMERIC_CLAIM_ELEMENT ) );
      ClaimFormStepElement claimFormStepElement = new ClaimFormStepElement( Long.valueOf( getStackRankFactorType() ) );
      promotion.setStackRankClaimFormStepElement( claimFormStepElement );
      // promotion.setStackRankClaimFormStepElement(getClaimFormDefinitionService().getClaimFormStepElementById(Long.valueOf(getStackRankFactorType())));
    }
    else
    {
      promotion.setStackRankFactorType( StackRankFactorType.lookup( StackRankFactorType.QUANTITY_SOLD ) );
    }

    promotion.setDisplayStackRankFactor( isDisplayStackRankFactor() );
    if ( isStackRankApprovalType() )
    {
      promotion.setStackRankApprovalType( StackRankApprovalType.lookup( StackRankApprovalType.MANUAL_APPROVAL ) );
    }
    else
    {
      promotion.setStackRankApprovalType( StackRankApprovalType.lookup( StackRankApprovalType.AUTOMATIC_APPROVAL ) );
    }

    promotion.setDisplayFullListLinkToParticipants( this.isDisplayFullListLinkToParticipants() );

    boolean canAddPayout = true;

    if ( this.getPromoPayoutGroupValueListCount() > 0 )
    {
      for ( Iterator promoPayoutGroupValueListIter = this.getPromoPayoutGroupValueList().iterator(); promoPayoutGroupValueListIter.hasNext(); )
      {
        PromotionPayoutGroupFormBean promotionPayoutGroupFormBean = (PromotionPayoutGroupFormBean)promoPayoutGroupValueListIter.next();

        PromotionPayoutGroup promotionPayoutGroup = new PromotionPayoutGroup();
        promotionPayoutGroup.setGuid( promotionPayoutGroupFormBean.getGuid() );
        promotionPayoutGroup.setId( promotionPayoutGroupFormBean.getPromoPayoutGroupId() );

        if ( payoutType.equals( PromotionPayoutType.TIERED ) || payoutType.equals( PromotionPayoutType.ONE_TO_ONE ) || payoutType.equals( PromotionPayoutType.STACK_RANK ) )
        {
          if ( !promotionPayoutGroupFormBean.getMinimumQualifier().equals( "" ) )
          {
            promotionPayoutGroup.setMinimumQualifier( new Integer( promotionPayoutGroupFormBean.getMinimumQualifier() ) );
          }
          promotionPayoutGroup.setRetroPayout( new Boolean( promotionPayoutGroupFormBean.getRetroPayout() ) );
        }

        if ( hasParent )
        {
          // If this is a child payout group, create a new parentPromoPayoutGroup with just
          // the id and set it on the promotionPayoutGroup object
          PromotionPayoutGroup parentPromoPayoutGroup = new PromotionPayoutGroup();
          parentPromoPayoutGroup.setId( promotionPayoutGroupFormBean.getParentPromoPayoutGroupId() );
          promotionPayoutGroup.setParentPromotionPayoutGroup( parentPromoPayoutGroup );
          if ( !promotionPayoutGroupFormBean.getChildQuantity().equals( "" ) )
          {
            promotionPayoutGroup.setQuantity( Integer.parseInt( promotionPayoutGroupFormBean.getChildQuantity() ) );
          }
          if ( !promotionPayoutGroupFormBean.getSubmitterChildPayout().equals( "" ) )
          {
            promotionPayoutGroup.setSubmitterPayout( Integer.parseInt( promotionPayoutGroupFormBean.getSubmitterChildPayout() ) );
          }
        }
        else
        {
          promotionPayoutGroup.setQuantity( Integer.parseInt( promotionPayoutGroupFormBean.getParentQuantity() ) );
          String submitterParentPayout = promotionPayoutGroupFormBean.getSubmitterParentPayout();
          if ( submitterParentPayout != null && submitterParentPayout.length() > 0 )
          {
            promotionPayoutGroup.setSubmitterPayout( Integer.parseInt( promotionPayoutGroupFormBean.getSubmitterParentPayout() ) );
          }

        }
        // Tiered does not track team member payout
        if ( !getPayoutType().equals( TIERED ) && !getPayoutType().equals( STACK_RANK ) )
        {
          if ( teamUsed )
          {
            if ( hasParent )
            {
              if ( !promotionPayoutGroupFormBean.getTeamMemberChildPayout().equals( "" ) )
              {
                promotionPayoutGroup.setTeamMemberPayout( Integer.valueOf( promotionPayoutGroupFormBean.getTeamMemberChildPayout() ) );
              }
            }
            else
            {
              if ( !promotionPayoutGroupFormBean.getTeamMemberParentPayout().equals( "" ) )
              {
                promotionPayoutGroup.setTeamMemberPayout( Integer.valueOf( promotionPayoutGroupFormBean.getTeamMemberParentPayout() ) );
              }
            }
          }
        }

        for ( int i = 0; i < promotionPayoutGroupFormBean.getPromoPayoutValueListCount(); i++ )
        {

          PromotionPayoutFormBean promoPayoutValueBean = promotionPayoutGroupFormBean.getPromoPayoutValueList( i );

          PromotionPayout promoPayout = new PromotionPayout();

          // if the id is null or 0, set it to null
          if ( promoPayoutValueBean.getPromoPayoutId() == null || promoPayoutValueBean.getPromoPayoutId().longValue() == 0 )
          {
            promoPayout.setId( null );
          }
          else
          {
            promoPayout.setId( promoPayoutValueBean.getPromoPayoutId() );
          }

          promoPayout.setProductOrCategoryStartDate( DateUtils.toDate( promoPayoutValueBean.getProductOrCategoryStartDate() ) );
          promoPayout.setProductOrCategoryEndDate( DateUtils.toDate( promoPayoutValueBean.getProductOrCategoryEndDate() ) );

          if ( getPayoutType().equals( CROSS_SELL ) )
          {
            if ( hasParent )
            {
              promoPayout.setQuantity( Integer.parseInt( promoPayoutValueBean.getChildQuantity() ) );
            }
            else
            {
              promoPayout.setQuantity( Integer.parseInt( promoPayoutValueBean.getParentQuantity() ) );
            }
          }

          // if there is a productId for this payout, create a new Product object
          // with just productId and productName (the actual object will be looked
          // up later if needed during synchronization)
          if ( promoPayoutValueBean.getProductId() != null && promoPayoutValueBean.getProductId().longValue() > 0 )
          {
            Product product = new Product();
            product.setId( promoPayoutValueBean.getProductId() );
            product.setName( promoPayoutValueBean.getProductName() );
            promoPayout.setProduct( product );
          }
          // create a ProductCategory object with just id and name, (the actual object
          // can be looked up later if needed during synchronization)
          else
          {
            ProductCategory productCategory = new ProductCategory();

            if ( promoPayoutValueBean.getSubcategoryId() != null && promoPayoutValueBean.getSubcategoryId().longValue() > 0 )
            {
              ProductCategory subCategory = new ProductCategory();
              subCategory.setId( promoPayoutValueBean.getSubcategoryId() );
              subCategory.setName( promoPayoutValueBean.getSubcategoryName() );

              productCategory.setId( promoPayoutValueBean.getCategoryId() );
              productCategory.setName( promoPayoutValueBean.getCategoryName() );
              productCategory.addSubcategory( subCategory );
            }
            else if ( promoPayoutValueBean.getCategoryId() != null && promoPayoutValueBean.getCategoryId().longValue() > 0 )
            {
              productCategory.setId( promoPayoutValueBean.getCategoryId() );
              productCategory.setName( promoPayoutValueBean.getCategoryName() );
            }
            promoPayout.setProductCategory( productCategory );
          }
          if ( canAddPayout )
          {
            promotionPayoutGroup.addPromotionPayout( promoPayout );
          }
          else
          {
            canAddPayout = true;
          }
        }
        if ( promotionPayoutGroup.getPromotionPayouts() != null && promotionPayoutGroup.getPromotionPayouts().size() > 0 )
        {
          promotion.addPromotionPayoutGroup( promotionPayoutGroup );
        }
      }
    }
    if ( getPayoutType().equals( STACK_RANK ) )
    {
      promotion = (ProductClaimPromotion)toStackRankDomainObject( promotion );
    }
    return promotion;
  }

  /**
   * Creates a detatched Promotion Domain Object that will later be synchronized with a looked up
   * promotion object in the service
   * 
   * @param promotion
   * @return Promotion
   */
  private Promotion toStackRankDomainObject( ProductClaimPromotion promotion )
  {
    if ( this.getPromoStackRankPayoutGroupValueListCount() > 0 )
    {
      for ( Iterator promoStackRankPayoutGroupValueListIter = this.getPromoStackRankPayoutGroupValueList().iterator(); promoStackRankPayoutGroupValueListIter.hasNext(); )
      {
        PromotionStackRankPayoutGroupFormBean promotionStackRankPayoutGroupFormBean = (PromotionStackRankPayoutGroupFormBean)promoStackRankPayoutGroupValueListIter.next();

        // promotionPayoutGroup.setGuid( promotionPayoutGroupFormBean.getGuid() );
        // promotionPayoutGroup.setId( promotionPayoutGroupFormBean.getPromoPayoutGroupId() );
        StackRankPayoutGroup stackRankPayoutGroup = new StackRankPayoutGroup();
        stackRankPayoutGroup.setId( promotionStackRankPayoutGroupFormBean.getPromoPayoutGroupId() );

        NodeType nodeType = null;

        // we are aware of the use of service against our normal standards
        // here that is the best way to get nodetype instead of having tons of hidden variables
        // here we need most of the information of node type

        if ( !StringUtils.isEmpty( promotionStackRankPayoutGroupFormBean.getNodeTypeId() ) )
        {
          nodeType = getNodeTypeService().getNodeTypeById( Long.valueOf( promotionStackRankPayoutGroupFormBean.getNodeTypeId() ) );
          stackRankPayoutGroup.setNodeType( nodeType );
        }
        else
        {
          nodeType = new NodeType();
          nodeType.setName( promotionStackRankPayoutGroupFormBean.getNodeTypeName() );
        }
        stackRankPayoutGroup.setSubmittersToRankType( SubmittersToRankType.lookup( promotionStackRankPayoutGroupFormBean.getSubmittersRankNameId() ) );

        for ( int i = 0; i < promotionStackRankPayoutGroupFormBean.getPromoStackRankPayoutValueListCount(); i++ )
        {
          PromotionStackRankPayoutFormBean promoStackRankPayoutValueBean = promotionStackRankPayoutGroupFormBean.getPromoPayoutValueList( i );
          StackRankPayout stackRankPayout = new StackRankPayout();

          // if the id is null or 0, set it to null
          if ( promoStackRankPayoutValueBean.getPromoPayoutId() == null || promoStackRankPayoutValueBean.getPromoPayoutId().longValue() == 0 )
          {
            stackRankPayout.setId( null );
          }
          else
          {
            stackRankPayout.setId( promoStackRankPayoutValueBean.getPromoPayoutId() );
          }

          if ( !StringUtils.isEmpty( promoStackRankPayoutValueBean.getFromRank() ) )
          {
            stackRankPayout.setStartRank( Integer.parseInt( promoStackRankPayoutValueBean.getFromRank() ) );
          }
          if ( !StringUtils.isEmpty( promoStackRankPayoutValueBean.getToRank() ) )
          {
            stackRankPayout.setEndRank( Integer.parseInt( promoStackRankPayoutValueBean.getToRank() ) );
          }
          if ( !StringUtils.isEmpty( promoStackRankPayoutValueBean.getPayoutAmount() ) )
          {
            stackRankPayout.setPayout( Integer.parseInt( promoStackRankPayoutValueBean.getPayoutAmount() ) );
          }

          stackRankPayoutGroup.addStackRankPayout( stackRankPayout );
        }
        if ( stackRankPayoutGroup.getPromotionStackRankPayout() != null && stackRankPayoutGroup.getPromotionStackRankPayout().size() > 0 )
        {
          promotion.addStackRankPayoutGroup( stackRankPayoutGroup );
        }
      }
    }

    return promotion;
  }

  public BudgetMaster getNewBudgetMaster()
  {
    BudgetMaster budgetMaster = new BudgetMaster();
    budgetMaster.setBudgetName( this.budgetMasterName );
    budgetMaster.setBudgetType( BudgetType.lookup( this.budgetType ) );
    budgetMaster.setAwardType( BudgetMasterAwardType.lookup( BudgetMasterAwardType.POINTS ) );
    budgetMaster.setOverrideableType( BudgetOverrideableType.lookup( this.budgetCapType ) );
    budgetMaster.setActive( true );
    budgetMaster.setFinalPayoutRule( BudgetFinalPayoutRule.lookup( this.finalPayoutRule ) );
    budgetMaster.setStartDate( DateUtils.toDate( this.budgetMasterStartDate ) );
    budgetMaster.setEndDate( DateUtils.toDate( this.budgetMasterEndDate ) );

    return budgetMaster;
  }

  public Long getPromotionId()
  {
    return promotionId;
  }

  public void setPromotionId( Long promotionId )
  {
    this.promotionId = promotionId;
  }

  public String getPromotionName()
  {
    return promotionName;
  }

  public void setPromotionName( String promotionName )
  {
    this.promotionName = promotionName;
  }

  public boolean isManagerOverride()
  {
    return managerOverride;
  }

  public void setManagerOverride( boolean managerOverride )
  {
    this.managerOverride = managerOverride;
  }

  public String getManagerOverrideFrequency()
  {
    return managerOverrideFrequency;
  }

  public void setManagerOverrideFrequency( String managerOverrideFrequency )
  {
    this.managerOverrideFrequency = managerOverrideFrequency;
  }

  public String getManagerOverridePercent()
  {
    return managerOverridePercent;
  }

  public void setManagerOverridePercent( String managerOverridePercent )
  {
    this.managerOverridePercent = managerOverridePercent;
  }

  public String getMethod()
  {
    return method;
  }

  public void setMethod( String method )
  {
    this.method = method;
  }

  public String getPayoutType()
  {
    return payoutType;
  }

  public void setPayoutType( String payoutType )
  {
    this.payoutType = payoutType;
  }

  public String getPromotionStatus()
  {
    return promotionStatus;
  }

  public void setPromotionStatus( String promotionStatus )
  {
    this.promotionStatus = promotionStatus;
  }

  public Long getVersion()
  {
    return version;
  }

  public void setVersion( Long version )
  {
    this.version = version;
  }

  /**
   * @return List of PromotionPayoutFormBean objects
   */
  public List getPromoPayoutGroupValueList()
  {
    return promoPayoutGroupValueList;
  }

  public void setPromoGroupPayoutValueList( List promoPayoutGroupValueList )
  {
    this.promoPayoutGroupValueList = promoPayoutGroupValueList;
  }

  /**
   * Accessor for the number of PromotionPayoutFormBean objects in the list.
   * 
   * @return int
   */
  public int getPromoPayoutGroupValueListCount()
  {
    if ( promoPayoutGroupValueList == null )
    {
      return 0;
    }

    return promoPayoutGroupValueList.size();
  }

  /**
   * Accessor for the number of PromotionPayoutFormBean objects in the list.
   * 
   * @return int
   */
  public int getPromoPayoutValueListCount()
  {
    if ( promoPayoutGroupValueList == null || promoPayoutGroupValueList.size() == 0 )
    {
      return 0;
    }

    // Get the size of the child collection
    return getPromoPayoutGroupValueListElement( 0 ).getPromoPayoutValueListCount();
  }

  /**
   * Accessor for the value list
   * 
   * @param index
   * @return Single instance of PromotionPayoutFormBean from the value list
   */
  public PromotionPayoutGroupFormBean getPromoPayoutGroupValueListElement( int index )
  {
    try
    {
      return (PromotionPayoutGroupFormBean)promoPayoutGroupValueList.get( index );
    }
    catch( Exception anException )
    {
      return null;
    }
  }

  /**
   * Adds a blank group to the payout list
   * 
   * @param promotion
   */
  public void addPromoPayoutGroup( ProductClaimPromotion promotion )
  {
    if ( promoPayoutGroupValueList != null )
    {
      PromotionPayoutGroupFormBean group = new PromotionPayoutGroupFormBean();
      if ( hasParent )
      {
        group = buildChildGroup( promotion, group );
      }
      promoPayoutGroupValueList.add( group );
    }
    else
    {
      promoPayoutGroupValueList = getEmptyValueList( 1 );
      PromotionPayoutGroupFormBean group = (PromotionPayoutGroupFormBean)promoPayoutGroupValueList.get( 0 );
      group.setPromoPayoutValueList( getEmptyPayoutValueList( 1 ) );
    }
  }

  public String getGroupEditId()
  {
    return groupEditId;
  }

  public void setGroupEditId( String groupEditId )
  {
    this.groupEditId = groupEditId;
  }

  public void resetPromoPayoutGroupValueList( ProductClaimPromotion promotion )
  {
    promoPayoutGroupValueList = getEmptyValueList( 0 );
    if ( hasParent )
    {
      if ( payoutType.equals( PromotionPayoutType.ONE_TO_ONE ) || payoutType.equals( PromotionPayoutType.STACK_RANK ) )
      {
        buildChildGroups( promotion, 0 );
        if ( payoutType.equals( PromotionPayoutType.STACK_RANK ) )
        {
          addPromoStackRankPayoutGroup( promotion );
        }
      }
      /*
       * else if ( payoutType.equals(PromotionPayoutType.STACK_RANK ) ) { buildStackRankChildGroups(
       * promotion, 0 ); }
       */
      else
      {
        buildChildGroups( promotion, 1 );
      }
    }
    else
    {
      if ( payoutType.equals( PromotionPayoutType.CROSS_SELL ) || payoutType.equals( PromotionPayoutType.TIERED ) )
      {
        addPromoPayoutGroup( promotion );
      }

      if ( payoutType.equals( PromotionPayoutType.STACK_RANK ) )
      {
        addPromoStackRankPayoutGroup( promotion );
      }

    }
  }

  public boolean isCarryoverAllowed()
  {
    return carryoverAllowed;
  }

  public void setCarryoverAllowed( boolean carryoverAllowed )
  {
    this.carryoverAllowed = carryoverAllowed;
  }

  public String getPrevPayoutType()
  {
    return prevPayoutType;
  }

  public void setPrevPayoutType( String prevPayoutType )
  {
    this.prevPayoutType = prevPayoutType;
  }

  public String getPromotionTypeName()
  {
    return promotionTypeName;
  }

  public void setPromotionTypeName( String promotionTypeName )
  {
    this.promotionTypeName = promotionTypeName;
  }

  public String getPromotionTypeCode()
  {
    return promotionTypeCode;
  }

  public boolean isHasParent()
  {
    return hasParent;
  }

  public void setHasParent( boolean hasParent )
  {
    this.hasParent = hasParent;
  }

  public boolean isTeamUsed()
  {
    return teamUsed;
  }

  public void setTeamUsed( boolean teamUsed )
  {
    this.teamUsed = teamUsed;
  }

  public void setPromotionTypeCode( String promotionTypeCode )
  {
    this.promotionTypeCode = promotionTypeCode;
  }

  public String getAwardType()
  {
    return awardType;
  }

  public void setAwardType( String awardType )
  {
    this.awardType = awardType;
  }

  public String getParentPayoutType()
  {
    return parentPayoutType;
  }

  public void setParentPayoutType( String parentPayoutType )
  {
    this.parentPayoutType = parentPayoutType;
  }

  public String getPromotionSubmitStartDate()
  {
    return promotionSubmitStartDate;
  }

  public void setPromotionSubmitStartDate( String promotionSubmitStartDate )
  {
    this.promotionSubmitStartDate = promotionSubmitStartDate;
  }

  public String getPromotionSubmitEndDate()
  {
    return promotionSubmitEndDate;
  }

  public void setPromotionSubmitEndDate( String promotionSubmitEndDate )
  {
    this.promotionSubmitEndDate = promotionSubmitEndDate;
  }

  public boolean isDisplayFullListLinkToParticipants()
  {
    return displayFullListLinkToParticipants;
  }

  public void setDisplayFullListLinkToParticipants( boolean displayFullListLinkToParticipants )
  {
    this.displayFullListLinkToParticipants = displayFullListLinkToParticipants;
  }

  public boolean isDisplayStackRankFactor()
  {
    return displayStackRankFactor;
  }

  public void setDisplayStackRankFactor( boolean displayStackRankFactor )
  {
    this.displayStackRankFactor = displayStackRankFactor;
  }

  public boolean isStackRankApprovalType()
  {
    return stackRankApprovalType;
  }

  public void setStackRankApprovalType( boolean stackRankApprovalType )
  {
    this.stackRankApprovalType = stackRankApprovalType;
  }

  public String getStackRankFactorType()
  {
    return stackRankFactorType;
  }

  public void setStackRankFactorType( String stackRankFactorType )
  {
    this.stackRankFactorType = stackRankFactorType;
  }

  public List getPromoStackRankPayoutGroupValueList()
  {
    return promoStackRankPayoutGroupValueList;
  }

  /**
   * Accessor for the number of PromotionPayoutFormBean objects in the list.
   * 
   * @return int
   */
  public int getPromoStackRankPayoutGroupValueListCount()
  {
    if ( promoStackRankPayoutGroupValueList == null )
    {
      return 0;
    }

    return promoStackRankPayoutGroupValueList.size();
  }

  /**
   * Accessor for the number of PromotionPayoutFormBean objects in the list.
   * 
   * @return int
   */
  public int getPromoStackRankPayoutValueListCount()
  {
    if ( promoStackRankPayoutGroupValueList == null || promoStackRankPayoutGroupValueList.size() == 0 )
    {
      return 0;
    }

    // Get the size of the child collection
    return getPromoStackRankPayoutGroupValueListElement( 0 ).getPromoStackRankPayoutValueListCount();
  }

  /**
   * Accessor for the value list
   * 
   * @param index
   * @return Single instance of PromotionPayoutFormBean from the value list
   */
  public PromotionStackRankPayoutGroupFormBean getPromoStackRankPayoutGroupValueListElement( int index )
  {
    try
    {
      return (PromotionStackRankPayoutGroupFormBean)promoStackRankPayoutGroupValueList.get( index );
    }
    catch( Exception anException )
    {
      return null;
    }
  }

  public void setPromoStackRankPayoutGroupValueList( List promoStackRankPayoutGroupValueList )
  {
    this.promoStackRankPayoutGroupValueList = promoStackRankPayoutGroupValueList;
  }

  /**
   * Adds a blank group to the payout list
   * 
   * @param promotion
   */
  public void addPromoStackRankPayoutGroup( ProductClaimPromotion promotion )
  {
    if ( promoStackRankPayoutGroupValueList != null )
    {
      PromotionStackRankPayoutGroupFormBean group = new PromotionStackRankPayoutGroupFormBean();
      // we dont need to check parent as parent and child payouts section for stack rank should be
      // same
      /*
       * if ( hasParent ) { group = buildStackRankChildGroup( promotion, group ); }
       */
      promoStackRankPayoutGroupValueList.add( group );
    }
    else
    {
      promoStackRankPayoutGroupValueList = getStackRankEmptyValueList( 1 );
      PromotionStackRankPayoutGroupFormBean group = (PromotionStackRankPayoutGroupFormBean)promoStackRankPayoutGroupValueList.get( 0 );
      group.setPromoStackRankPayoutValueList( getStackRankEmptyPayoutValueList( 1 ) );
    }
  }

  public String getStackRankGroupEditId()
  {
    return stackRankGroupEditId;
  }

  public void setStackRankGroupEditId( String stackRankGroupEditId )
  {
    this.stackRankGroupEditId = stackRankGroupEditId;
  }

  // we are aware of the use of service against our normal standards
  // here that is the best way to get nodetype instead of having tons of hidden variables
  // here we need most of the information of node type so we are getting it using the following
  // services

  private NodeTypeService getNodeTypeService()
  {
    return (NodeTypeService)getService( NodeTypeService.BEAN_NAME );
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
   * @return value of alternateReturnUrl property
   */
  public String getAlternateReturnUrl()
  {
    return alternateReturnUrl;
  }

  /**
   * @param alternateReturnUrl value for alternateReturnUrl property
   */
  public void setAlternateReturnUrl( String alternateReturnUrl )
  {
    this.alternateReturnUrl = alternateReturnUrl;
  }

  public boolean isDisableProductSection()
  {
    return disableProductSection;
  }

  public void setDisableProductSection( boolean disableProductSection )
  {
    this.disableProductSection = disableProductSection;
  }

  public boolean isHasPendingStackRank()
  {
    return hasPendingStackRank;
  }

  public void setHasPendingStackRank( boolean hasPendingStackRank )
  {
    this.hasPendingStackRank = hasPendingStackRank;
  }

  public Map getGroupDependentMap()
  {
    return groupDependentMap;
  }

  public void setGroupDependentMap( Map groupDependentMap )
  {
    this.groupDependentMap = groupDependentMap;
  }

  public String getExpirationDate()
  {
    return expirationDate;
  }

  public void setExpirationDate( String expirationDate )
  {
    this.expirationDate = expirationDate;
  }

  public String getPlayingMethod()
  {
    return playingMethod;
  }

  public void setPlayingMethod( String playingMethod )
  {
    this.playingMethod = playingMethod;
  }

  public String getBudgetCapType()
  {
    return budgetCapType;
  }

  public void setBudgetCapType( String budgetCapType )
  {
    this.budgetCapType = budgetCapType;
  }

  public Long getBudgetMasterId()
  {
    return budgetMasterId;
  }

  public void setBudgetMasterId( Long budgetMasterId )
  {
    this.budgetMasterId = budgetMasterId;
  }

  public String getBudgetMasterName()
  {
    return budgetMasterName;
  }

  public void setBudgetMasterName( String budgetMasterName )
  {
    this.budgetMasterName = budgetMasterName;
  }

  public String getBudgetOption()
  {
    return budgetOption;
  }

  public void setBudgetOption( String budgetOption )
  {
    this.budgetOption = budgetOption;
  }

  public String getBudgetType()
  {
    return budgetType;
  }

  public void setBudgetType( String budgetType )
  {
    this.budgetType = budgetType;
  }

  public String getCentralBudgetAmount()
  {
    return centralBudgetAmount;
  }

  public void setCentralBudgetAmount( String centralBudgetAmount )
  {
    this.centralBudgetAmount = centralBudgetAmount;
  }

  public String getPromotionIssuanceTypeCode()
  {
    return promotionIssuanceTypeCode;
  }

  public void setPromotionIssuanceTypeCode( String promotionIssuanceTypeCode )
  {
    this.promotionIssuanceTypeCode = promotionIssuanceTypeCode;
  }

  public String getFinalPayoutRule()
  {
    return finalPayoutRule;
  }

  public void setFinalPayoutRule( String finalPayoutRule )
  {
    this.finalPayoutRule = finalPayoutRule;
  }

  public String getAwardTypeName()
  {
    return awardTypeName;
  }

  public void setAwardTypeName( String awardTypeName )
  {
    this.awardTypeName = awardTypeName;
  }

  public Long getLowPaymentThreshold()
  {
    return lowPaymentThreshold;
  }

  public void setLowPaymentThreshold( Long lowPaymentThreshold )
  {
    this.lowPaymentThreshold = lowPaymentThreshold;
  }

  public Integer getPayoutCalculation()
  {
    return payoutCalculation;
  }

  public void setPayoutCalculation( Integer payoutCalculation )
  {
    this.payoutCalculation = payoutCalculation;
  }

  public boolean isAllowDefaultQuantity()
  {
    return allowDefaultQuantity;
  }

  public void setAllowDefaultQuantity( boolean allowDefaultQuantity )
  {
    this.allowDefaultQuantity = allowDefaultQuantity;
  }

  public int getDefaultQuantity()
  {
    return defaultQuantity;
  }

  public void setDefaultQuantity( int defaultQuantity )
  {
    this.defaultQuantity = defaultQuantity;
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

  public void setBudgetSegmentId( Long budgetSegmentId )
  {
    this.budgetSegmentId = budgetSegmentId;
  }

  public Long getBudgetSegmentId()
  {
    return budgetSegmentId;
  }

  public void validatePayouts( ActionErrors actionErrors )
  {
    float payoutCnt = 0;
    int cnt = 0;
    boolean errorFlag = false;
    int payoutCalculation = 0;

    try
    {
      payoutCalculation = Integer.parseInt( String.valueOf( this.payoutCalculation ) );
      if ( payoutCalculation < 0 )
      {
        actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "promotion.gameattempts.errors.NEGATIVE_PAYOUT_CALCULATION" ) );
        errorFlag = true;
      }
    }
    catch( NumberFormatException ex )
    {
      actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "promotion.gameattempts.errors.INVALID_PAYOUT_CALCULATION" ) );
      errorFlag = true;
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
        errors = validateSegmentStartDate( budgetSegmentVB, errors, index );
        errors = validateSegmentEndDate( budgetSegmentVB, errors, index, isLastSegmentEndDate );
        index++;
      } // end for
      errors = validateSegmentEndDate( errors, DateUtils.toDate( budgetMasterEndDate ), lastBudgetSegmentValueBean );
      // all required field must be filled in before doing overlap check
      if ( errors.isEmpty() )
      {
        if ( validateDateOverlap() )
        {
          errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "admin.budgetmaster.details.SEGMENT_DATE_OVERLAP" ) );
        }
        if ( validateNextDay() )
        {
          errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "admin.budgetmaster.details.SEGMENT_START_DATE_NON_NEXT_DAY" ) );
        }
      }
    }
    return errors;
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

  private boolean validateDateOverlap()
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

  private boolean validateNextDay()
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

  private ActionErrors validateSegmentStartDate( BudgetSegmentValueBean budgetSegmentVB, ActionErrors actionErrors, int index )
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

  private ActionErrors validateSegmentEndDate( BudgetSegmentValueBean budgetSegmentVB, ActionErrors actionErrors, int index, boolean isLastSegmentEndDate )
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
    // new end date must be greater than the new start date
    if ( newStartDate != null && newEndDate != null && newEndDate.compareTo( newStartDate ) <= 0 )
    {
      return true;
    }
    return false;
  }

  public ActionErrors validateTimePeriodBudgetAmount( ActionErrors errors )
  {
    if ( this.getBudgetSegmentVBListSize() > 0 )
    {
      int index = 0;
      for ( Iterator<BudgetSegmentValueBean> budgetSegmentVBListIter = this.getBudgetSegmentVBList().iterator(); budgetSegmentVBListIter.hasNext(); )
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

}
