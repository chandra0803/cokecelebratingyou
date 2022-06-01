
package com.biperf.core.ui.promotion;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.biperf.core.domain.budget.BudgetMaster;
import com.biperf.core.domain.budget.BudgetSegment;
import com.biperf.core.domain.enums.ApprovalType;
import com.biperf.core.domain.enums.BudgetMasterAwardType;
import com.biperf.core.domain.enums.BudgetOverrideableType;
import com.biperf.core.domain.enums.BudgetType;
import com.biperf.core.domain.enums.PromotionAwardsType;
import com.biperf.core.domain.enums.PromotionIssuanceType;
import com.biperf.core.domain.enums.PromotionStatusType;
import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.domain.enums.PublicRecognitionAudienceType;
import com.biperf.core.domain.promotion.NominationPromotion;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.RecognitionPromotion;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.ui.BaseForm;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.value.BudgetSegmentValueBean;
import com.objectpartners.cms.util.CmsResourceBundle;

public class PromotionPublicRecAddOnForm extends BaseForm
{
  public static final String SESSION_KEY = "promotionPublicRecAddOnForm";
  private static final long serialVersionUID = 1L;

  public static final String BUDGET_NONE = "none";
  public static final String BUDGET_EXISTING = "existing";
  public static final String BUDGET_NEW = "new";

  private static String RANGE = "false";
  private static String FIXED = "true";

  private boolean allowPublicRecognitionPoints;
  private String method;
  private String awardsType;
  private String rangeAmountMin = null;
  private String rangeAmountMax = null;
  private String fixedAmount;
  private String awardAmountTypeFixed;

  private Long budgetMasterId;
  private String budgetMasterName;
  private String budgetType = BudgetType.PAX_BUDGET_TYPE;
  private String budgetCapType = BudgetOverrideableType.HARD_OVERRIDE;
  private Long hiddenBudgetMasterId; // Used if budget section is disabled
  private Long budgetApproverId;
  private String budgetApproverName = "<none selected>";
  private String finalPayoutRule;
  private String budgetOption = BUDGET_NONE;
  private String centralBudgetAmount;

  private boolean live;
  private Long promotionId;
  private String promotionName;
  private String promotionType;
  private String promotionTypeCode;
  private String promotionTypeName;
  private String promotionStatus;
  private String promotionIssuanceTypeCode;
  private boolean hasParent;
  private boolean hasChildren;
  private Long version;

  private String audience;
  private String audienceId;
  private boolean canRemoveAudience;
  private String[] deletePublicRecognitionAudience;
  private boolean allowPublicRecognition = false;

  // Budget Segment fields
  private String budgetMasterStartDate = DateUtils.displayDateFormatMask;
  private String budgetMasterEndDate = DateUtils.displayDateFormatMask;
  private List<BudgetSegmentValueBean> budgetSegmentVBList = new ArrayList<BudgetSegmentValueBean>();
  private Long budgetSegmentId;

  public void load( Promotion promotion )
  {
    // Promotion display information
    this.promotionId = promotion.getId();
    this.promotionType = promotion.getPromotionType().toString();
    this.budgetOption = BUDGET_NONE;
    this.hasParent = promotion.hasParent();
    this.promotionStatus = promotion.getPromotionStatus().getCode();
    this.promotionTypeName = promotion.getPromotionType().getName();
    this.version = promotion.getVersion();
    this.promotionName = promotion.getName();
    this.live = promotion.isLive();

    // For a nomination promotion, award type might be null
    if ( promotion.getAwardType() != null )
    {
      this.awardsType = promotion.getAwardType().getCode();
    }

    ApprovalType approvalType = promotion.getApprovalType();
    PromotionStatusType promotionStatus = promotion.getPromotionStatus();

    canRemoveAudience = promotionStatus.isUnderConstruction() || promotionStatus.isComplete()
        || promotionStatus.isLive() && !promotion.isGoalQuestPromotion() && ( approvalType == null || approvalType.isAutomaticImmediate() );

    // Break into specific load methods based on type of promotion
    if ( promotion.isRecognitionPromotion() )
    {
      load( (RecognitionPromotion)promotion );
    }
    else if ( promotion.isNominationPromotion() )
    {
      load( (NominationPromotion)promotion );
    }
  }

  private void load( RecognitionPromotion recPromo )
  {
    // Bug 54911 - Do not allow public recognition when the award type is plateau
    if ( recPromo.isAllowPublicRecognition() && !PromotionAwardsType.MERCHANDISE.equalsIgnoreCase( awardsType ) && !PromotionAwardsType.TRAVEL_AWARD.equalsIgnoreCase( awardsType ) )
    {
      this.allowPublicRecognition = true;
    }

    if ( recPromo.getPublicRecognitionAudienceType() != null )
    {
      setAudience( recPromo.getPublicRecognitionAudienceType().getCode() );
    }
    else
    {
      setAudience( PublicRecognitionAudienceType.ALL_ACTIVE_PAX_CODE );
    }
    this.promotionTypeCode = recPromo.getPromotionType().getCode();
    // Set Budget data only if the awardType is points and method of entry is not file load
    if ( ( awardsType.equals( PromotionAwardsType.POINTS ) || awardsType.equals( PromotionAwardsType.MERCHANDISE ) ) && !PromotionIssuanceType.FILE_LOAD.equals( promotionIssuanceTypeCode ) )
    {
      BudgetMaster budgetMaster = recPromo.getPublicRecogBudgetMaster();
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
        }
      }
      // to add default budget segments.
      defaultEmptyBudgetSegment();
    }

    this.allowPublicRecognitionPoints = recPromo.isAllowPublicRecognitionPoints();
    if ( recPromo.isPublicRecogAwardAmountTypeFixed() )
    {
      setAwardAmountTypeFixed( FIXED );
      if ( recPromo.getPublicRecogAwardAmountFixed() != null )
      {
        this.fixedAmount = "" + recPromo.getPublicRecogAwardAmountFixed().intValue();
      }
    }

    else
    {
      // range
      setAwardAmountTypeFixed( RANGE );
      if ( recPromo.getPublicRecogAwardAmountMin() != null && recPromo.getPublicRecogAwardAmountMax() != null )
      {
        this.rangeAmountMin = "" + recPromo.getPublicRecogAwardAmountMin().intValue();
        this.rangeAmountMax = "" + recPromo.getPublicRecogAwardAmountMax().intValue();
      }
    }
  }

  private void load( NominationPromotion nomPromo )
  {
    // Bug 54911 - Do not allow public recognition when the award type is plateau
    if ( nomPromo.isAllowPublicRecognition() && !PromotionAwardsType.MERCHANDISE.equalsIgnoreCase( awardsType ) && !PromotionAwardsType.TRAVEL_AWARD.equalsIgnoreCase( awardsType ) )
    {
      this.allowPublicRecognition = true;
    }

    if ( nomPromo.getPublicRecognitionAudienceType() != null )
    {
      setAudience( nomPromo.getPublicRecognitionAudienceType().getCode() );
    }
    else
    {
      setAudience( PublicRecognitionAudienceType.ALL_ACTIVE_PAX_CODE );
    }
    this.promotionTypeCode = nomPromo.getPromotionType().getCode();
    // Set Budget data only if the awardType is points and method of entry is not file load
    if ( ( PromotionAwardsType.POINTS.equals( awardsType ) || PromotionAwardsType.MERCHANDISE.equals( awardsType ) ) && !PromotionIssuanceType.FILE_LOAD.equals( promotionIssuanceTypeCode ) )
    {
      BudgetMaster budgetMaster = nomPromo.getPublicRecogBudgetMaster();
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
        }
      }
      // to add default budget segments.
      defaultEmptyBudgetSegment();
    }

    this.allowPublicRecognitionPoints = nomPromo.isAllowPublicRecognitionPoints();
    if ( nomPromo.isPublicRecogAwardAmountTypeFixed() )
    {
      setAwardAmountTypeFixed( FIXED );
      if ( nomPromo.getPublicRecogAwardAmountFixed() != null )
      {
        this.fixedAmount = "" + nomPromo.getPublicRecogAwardAmountFixed().intValue();
      }
    }

    else
    {
      // range
      setAwardAmountTypeFixed( RANGE );
      if ( nomPromo.getPublicRecogAwardAmountMin() != null && nomPromo.getPublicRecogAwardAmountMax() != null )
      {
        this.rangeAmountMin = "" + nomPromo.getPublicRecogAwardAmountMin().intValue();
        this.rangeAmountMax = "" + nomPromo.getPublicRecogAwardAmountMax().intValue();
      }
    }
  }

  public void reset( ActionMapping mapping, HttpServletRequest request )
  {
    budgetSegmentVBList = getEmptyBudgetSegmentValueList( RequestUtils.getOptionalParamInt( request, "budgetSegmentVBListSize" ) );
  }

  public void buildPublicRecogAudience( Promotion promotion, ActionMessages errors )
  {
    if ( this.allowPublicRecognitionPoints )
    {
      if ( promotion.isRecognitionPromotion() )
      {
        ( (RecognitionPromotion)promotion ).setPublicRecognitionAudienceType( PublicRecognitionAudienceType.lookup( this.audience ) );
      }
      else if ( promotion.isNominationPromotion() )
      {
        ( (NominationPromotion)promotion ).setPublicRecognitionAudienceType( PublicRecognitionAudienceType.lookup( this.audience ) );
      }
    }
  }

  public ActionErrors validate( ActionMapping mapping, HttpServletRequest request )
  {
    ActionErrors errors = super.validate( mapping, request );
    if ( errors == null )
    {
      errors = new ActionErrors();
    }

    Date dateStart = null;
    Date dateEnd = null;

    if ( this.allowPublicRecognitionPoints )
    {
      try
      {
        if ( FIXED.equals( awardAmountTypeFixed ) )
        {
          if ( this.fixedAmount == null || this.fixedAmount.length() == 0 )
          {
            errors.add( "awardAmountTypeFixed",
                        new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "promotion.public.recognition.AMOUNT" ) ) );
          }
          else
          {
            try
            {
              int fAmt = Integer.parseInt( fixedAmount );

              if ( fAmt < 0 )
              {
                errors.add( "awardAmountTypeFixed",
                            new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "promotion.public.recognition.AMOUNT" ) ) );
              }
            }
            catch( NumberFormatException e )
            {
              errors.add( "awardAmountTypeFixed", new ActionMessage( "promotion.public.recognition.errors.FIXED_AMOUNT" ) );
            }
          }
        }
        else
        {
          if ( this.rangeAmountMin != null && !this.rangeAmountMin.equals( "" ) && this.rangeAmountMax != null && !this.rangeAmountMax.equals( "" ) )
          {
            long rangeAmountMinValue = Long.parseLong( rangeAmountMin );
            long rangeAmountMaxValue = Long.parseLong( rangeAmountMax );

            // If the min or max value is less than or equal to zero, then add the error
            if ( rangeAmountMinValue <= 0 || rangeAmountMaxValue <= 0 )
            {
              errors.add( "awardAmountTypeFixed", new ActionMessage( "promotion.public.recognition.errors.AMOUNT_RANGES" ) );
            }

            // If the min value is greater than or equals to the max value, then add the error
            if ( rangeAmountMinValue >= rangeAmountMaxValue )
            {
              errors.add( "awardAmountTypeFixed", new ActionMessage( "promotion.public.recognition.errors.AMOUNT_RANGES" ) );
            }
          }
          else
          {
            errors.add( "awardAmountTypeFixed", new ActionMessage( "promotion.public.recognition.errors.AMOUNT_RANGES" ) );
          }
        }
      }
      catch( NumberFormatException e )
      {
        errors.add( "awardAmountTypeFixed", new ActionMessage( "promotion.public.recognition.errors.AMOUNT_RANGES" ) );
      }

      String budgetOption = request.getParameter( "budgetOption" );
      // Make sure its not empty
      if ( budgetOption == null || budgetOption.length() == 0 )
      {
        errors.add( "budgetOption", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "promotion.public.recognition.HAS_BUDGET" ) ) );
        // if no budgetOption was selected, then no point validating further
        return errors;
      }

      if ( budgetOption.equals( BUDGET_EXISTING ) )
      {
        // ***** Validate budgetMasterId *****/
        String budgetMasterId = request.getParameter( "budgetMasterId" );
        // Make sure its not empty
        if ( budgetMasterId == null || budgetMasterId.length() == 0 )
        {
          errors.add( "budgetMasterId", new ActionMessage( "promotion.public.recognition.errors.CHOOSE_EXISTING_BUDGET" ) );
        }
        else
        {
          try
          {
            Long.parseLong( budgetMasterId );
          }
          catch( NumberFormatException e )
          {
            errors.add( "budgetMasterId", new ActionMessage( "promotion.public.recognition.errors.INVALID_BUDGET" ) );
          }
        }
        return errors;
      }
      if ( budgetOption.equals( BUDGET_NEW ) )
      {
        // ***** Validate budgetMasterName *****/
        Date startDateLocal = null;
        Date endDateLocal = null;

        // ***** Validate budgetMasterName *****/
        String budgetMasterName = request.getParameter( "budgetMasterName" );
        // Make sure its not empty
        if ( budgetMasterName == null || budgetMasterName.length() == 0 )
        {
          errors.add( "budgetMasterName", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "promotion.awards.BUDGET_MASTER_NAME" ) ) );
        }

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

        if ( startDateLocal != null && endDateLocal != null && endDateLocal.compareTo( startDateLocal ) == 0 )
        {
          errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "admin.budgetmaster.details.BUDGET_MASTER_START_END_DATE_EQUAL" ) );
        }
        // ***** Validate budgetType *****/
        String budgetType = request.getParameter( "budgetType" );
        // Make sure its not empty
        if ( budgetType == null || budgetType.length() == 0 )
        {
          errors.add( "budgetType", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "promotion.public.recognition.BUDGET_TYPE" ) ) );
        }

        // ***** Validate budgetCapType *****/
        String budgetCapType = request.getParameter( "budgetCapType" );
        // Make sure its not empty
        if ( budgetCapType == null || budgetCapType.length() == 0 )
        {
          errors.add( "budgetCapType", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "promotion.public.recognition.CAP_TYPE" ) ) );
          // if no budgetCapType was selected, then no point validating further
          return errors;
        }
        // validate for time period start/end dates and segment name.
        errors = validateTimePeriod( errors );
      }

      if ( BudgetType.CENTRAL_BUDGET_TYPE.equals( budgetType ) )
      {
        errors = validateTimePeriodBudgetAmount( errors );
        if ( this.finalPayoutRule == null )
        {
          errors.add( "finalPayoutRule", new ActionMessage( "system.errors.REQUIRED", CmsResourceBundle.getCmsBundle().getString( "admin.budgetmaster.details", "FINAL_PAYOUT_RULE" ) ) );
        }
      }
    }

    return errors;
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
        if ( sameStartDateEndDate() )
        {
          errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "admin.budgetmaster.details.TIME_PERIOD_START_END_DATE_EQUAL" ) );
        }
      }
    }
    return errors;
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
    return false;
  }

  public ActionErrors validateTimePeriodBudgetAmount( ActionErrors errors )
  {
    int index = 0;
    if ( this.getBudgetSegmentVBListSize() > 0 )
    {
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

  private boolean sameStartDateEndDate()
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

  public boolean isCreateNewBudgetMaster()
  {
    if ( !this.allowPublicRecognitionPoints )
    {
      return false;
    }

    return this.budgetOption.equals( BUDGET_NEW );
  }

  public boolean isUseExistingBudgetMaster()
  {
    if ( !this.allowPublicRecognitionPoints )
    {
      return false;
    }

    return this.budgetOption.equals( BUDGET_EXISTING );
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

  public Promotion toDomainObject()
  {
    Promotion promotion = null;

    // Create a new Promotion since one was not passed in
    if ( promotionTypeCode.equals( PromotionType.RECOGNITION ) )
    {
      promotion = new RecognitionPromotion();
      promotion.setPromotionType( PromotionType.lookup( PromotionType.RECOGNITION ) );
    }
    if ( promotion != null )
    {
      return toDomainObject( promotion );
    }
    return promotion;
  }

  /**
   * Creates a detatched Promotion Domain Object that will later be synchronized with a looked up
   * promotion object in the service
   * 
   * @return RecognitionPromotion
   */
  public Promotion toDomainObject( Promotion promotion )
  {
    promotion.setId( new Long( this.getPromotionId() ) );
    promotion.setName( this.getPromotionName() );
    promotion.setVersion( new Long( this.getVersion() ) );

    if ( promotion.isRecognitionPromotion() )
    {
      return toRecognitionPromotionDomainObject( (RecognitionPromotion)promotion );
    }
    else if ( promotion.isNominationPromotion() )
    {
      return toNominationPromotionDomainObject( (NominationPromotion)promotion );
    }
    else
    {
      // Situation where we don't know how to create the domain object
      throw new RuntimeException( "PromotionPublicRecAddOnForm cannot create a domain object from the given promotion" );
    }
  }

  public RecognitionPromotion toRecognitionPromotionDomainObject( RecognitionPromotion promotion )
  {
    promotion.setAllowPublicRecognitionPoints( this.allowPublicRecognitionPoints );
    if ( !this.allowPublicRecognitionPoints )
    {
      // if public recognition is not active we can't have a budget
      promotion.setPublicRecogBudgetMaster( null );
      promotion.setPublicRecogAwardAmountMin( null );
      promotion.setPublicRecogAwardAmountMax( null );
      promotion.setPublicRecogAwardAmountFixed( null );

      return promotion;
    }

    if ( this.budgetOption.equals( BUDGET_NONE ) )
    {
      promotion.setPublicRecogBudgetMaster( null );
    }

    if ( this.budgetOption.equals( BUDGET_EXISTING ) )
    {
      BudgetMaster budgetMaster = new BudgetMaster();
      if ( this.budgetMasterId == null )
      {
        this.budgetMasterId = this.hiddenBudgetMasterId;
      }
      budgetMaster.setId( this.budgetMasterId );
      promotion.setPublicRecogBudgetMaster( budgetMaster );
    }

    if ( FIXED.equals( awardAmountTypeFixed ) )
    {
      promotion.setPublicRecogAwardAmountTypeFixed( true );
      promotion.setPublicRecogAwardAmountFixed( new Long( this.fixedAmount ) );
      promotion.setPublicRecogAwardAmountMin( null );
      promotion.setPublicRecogAwardAmountMax( null );
    }
    else
    {
      promotion.setPublicRecogAwardAmountTypeFixed( false );
      if ( RANGE.equals( awardAmountTypeFixed ) )
      {
        promotion.setPublicRecogAwardAmountMin( new Long( this.rangeAmountMin ) );
        promotion.setPublicRecogAwardAmountMax( new Long( this.rangeAmountMax ) );
        promotion.setPublicRecogAwardAmountFixed( null );
      }
    }

    return promotion;
  }

  public NominationPromotion toNominationPromotionDomainObject( NominationPromotion promotion )
  {
    promotion.setAllowPublicRecognitionPoints( this.allowPublicRecognitionPoints );
    if ( !this.allowPublicRecognitionPoints )
    {
      // if public recognition is not active we can't have a budget
      promotion.setPublicRecogBudgetMaster( null );
      promotion.setPublicRecogAwardAmountMin( null );
      promotion.setPublicRecogAwardAmountMax( null );
      promotion.setPublicRecogAwardAmountFixed( null );

      return promotion;
    }

    if ( this.budgetOption.equals( BUDGET_NONE ) )
    {
      promotion.setPublicRecogBudgetMaster( null );
    }

    if ( this.budgetOption.equals( BUDGET_EXISTING ) )
    {
      BudgetMaster budgetMaster = new BudgetMaster();
      if ( this.budgetMasterId == null )
      {
        this.budgetMasterId = this.hiddenBudgetMasterId;
      }
      budgetMaster.setId( this.budgetMasterId );
      promotion.setPublicRecogBudgetMaster( budgetMaster );
    }

    if ( FIXED.equals( awardAmountTypeFixed ) )
    {
      promotion.setPublicRecogAwardAmountTypeFixed( true );
      promotion.setPublicRecogAwardAmountFixed( new Long( this.fixedAmount ) );
      promotion.setPublicRecogAwardAmountMin( null );
      promotion.setPublicRecogAwardAmountMax( null );
    }
    else
    {
      promotion.setPublicRecogAwardAmountTypeFixed( false );
      if ( RANGE.equals( awardAmountTypeFixed ) )
      {
        promotion.setPublicRecogAwardAmountMin( new Long( this.rangeAmountMin ) );
        promotion.setPublicRecogAwardAmountMax( new Long( this.rangeAmountMax ) );
        promotion.setPublicRecogAwardAmountFixed( null );
      }
    }

    return promotion;
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

  public boolean isAllowPublicRecognitionPoints()
  {
    return allowPublicRecognitionPoints;
  }

  public void setAllowPublicRecognitionPoints( boolean allowPublicRecognitionPoints )
  {
    this.allowPublicRecognitionPoints = allowPublicRecognitionPoints;
  }

  public String getMethod()
  {
    return method;
  }

  public void setMethod( String method )
  {
    this.method = method;
  }

  public String getAwardsType()
  {
    return awardsType;
  }

  public void setAwardsType( String awardsType )
  {
    this.awardsType = awardsType;
  }

  public String getAwardAmountTypeFixed()
  {
    return awardAmountTypeFixed;
  }

  public void setAwardAmountTypeFixed( String awardAmountTypeFixed )
  {
    this.awardAmountTypeFixed = awardAmountTypeFixed;
  }

  public String getRangeAmountMin()
  {
    return rangeAmountMin;
  }

  public void setRangeAmountMin( String rangeAmountMin )
  {
    this.rangeAmountMin = rangeAmountMin;
  }

  public String getRangeAmountMax()
  {
    return rangeAmountMax;
  }

  public void setRangeAmountMax( String rangeAmountMax )
  {
    this.rangeAmountMax = rangeAmountMax;
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

  public String getBudgetType()
  {
    return budgetType;
  }

  public void setBudgetType( String budgetType )
  {
    this.budgetType = budgetType;
  }

  public String getBudgetCapType()
  {
    return budgetCapType;
  }

  public void setBudgetCapType( String budgetCapType )
  {
    this.budgetCapType = budgetCapType;
  }

  public Long getHiddenBudgetMasterId()
  {
    return hiddenBudgetMasterId;
  }

  public void setHiddenBudgetMasterId( Long hiddenBudgetMasterId )
  {
    this.hiddenBudgetMasterId = hiddenBudgetMasterId;
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

  public String getFinalPayoutRule()
  {
    return finalPayoutRule;
  }

  public void setFinalPayoutRule( String finalPayoutRule )
  {
    this.finalPayoutRule = finalPayoutRule;
  }

  public String getBudgetOption()
  {
    return budgetOption;
  }

  public void setBudgetOption( String budgetOption )
  {
    this.budgetOption = budgetOption;
  }

  public String getCentralBudgetAmount()
  {
    return centralBudgetAmount;
  }

  public void setCentralBudgetAmount( String centralBudgetAmount )
  {
    this.centralBudgetAmount = centralBudgetAmount;
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

  public String getPromotionType()
  {
    return promotionType;
  }

  public void setPromotionType( String promotionType )
  {
    this.promotionType = promotionType;
  }

  public String getPromotionTypeCode()
  {
    return promotionTypeCode;
  }

  public void setPromotionTypeCode( String promotionTypeCode )
  {
    this.promotionTypeCode = promotionTypeCode;
  }

  public String getPromotionTypeName()
  {
    return promotionTypeName;
  }

  public void setPromotionTypeName( String promotionTypeName )
  {
    this.promotionTypeName = promotionTypeName;
  }

  public String getPromotionIssuanceTypeCode()
  {
    return promotionIssuanceTypeCode;
  }

  public void setPromotionIssuanceTypeCode( String promotionIssuanceTypeCode )
  {
    this.promotionIssuanceTypeCode = promotionIssuanceTypeCode;
  }

  public boolean isHasParent()
  {
    return hasParent;
  }

  public void setHasParent( boolean hasParent )
  {
    this.hasParent = hasParent;
  }

  public boolean isHasChildren()
  {
    return hasChildren;
  }

  public void setHasChildren( boolean hasChildren )
  {
    this.hasChildren = hasChildren;
  }

  public Long getVersion()
  {
    return version;
  }

  public void setVersion( Long version )
  {
    this.version = version;
  }

  public String getAudience()
  {
    return audience;
  }

  public void setAudience( String audience )
  {
    this.audience = audience;
  }

  public String getAudienceId()
  {
    return audienceId;
  }

  public void setAudienceId( String audienceId )
  {
    this.audienceId = audienceId;
  }

  public String getPromotionStatus()
  {
    return promotionStatus;
  }

  public void setPromotionStatus( String promotionStatus )
  {
    this.promotionStatus = promotionStatus;
  }

  public boolean isCanRemoveAudience()
  {
    return canRemoveAudience;
  }

  public void setCanRemoveAudience( boolean canRemoveAudience )
  {
    this.canRemoveAudience = canRemoveAudience;
  }

  public String[] getDeletePublicRecognitionAudience()
  {
    return this.deletePublicRecognitionAudience;
  }

  public void setDeletePublicRecognitionAudience( String[] deletePublicRecognitionAudience )
  {
    this.deletePublicRecognitionAudience = deletePublicRecognitionAudience;
  }

  public boolean isLive()
  {
    return live;
  }

  public void setLive( boolean live )
  {
    this.live = live;
  }

  public void setAllowPublicRecognition( boolean allowPublicRecognition )
  {
    this.allowPublicRecognition = allowPublicRecognition;
  }

  public boolean isAllowPublicRecognition()
  {
    return allowPublicRecognition;
  }

  public String getFixedAmount()
  {
    return fixedAmount;
  }

  public void setFixedAmount( String fixedAmount )
  {
    this.fixedAmount = fixedAmount;
  }

  public String getBudgetMasterStartDate()
  {
    return budgetMasterStartDate;
  }

  public String getBudgetMasterEndDate()
  {
    return budgetMasterEndDate;
  }

  public List<BudgetSegmentValueBean> getBudgetSegmentVBList()
  {
    return budgetSegmentVBList;
  }

  public Long getBudgetSegmentId()
  {
    return budgetSegmentId;
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

  public void setBudgetSegmentId( Long budgetSegmentId )
  {
    this.budgetSegmentId = budgetSegmentId;
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

}
