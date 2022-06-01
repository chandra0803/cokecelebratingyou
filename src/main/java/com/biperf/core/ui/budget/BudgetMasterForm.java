/*
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/budget/BudgetMasterForm.java,v $
 * (c) 2005 BI, Inc.  All rights reserved.
 */

package com.biperf.core.ui.budget;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.cxf.common.util.StringUtils;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import com.biperf.core.domain.budget.Budget;
import com.biperf.core.domain.budget.BudgetMaster;
import com.biperf.core.domain.budget.BudgetSegment;
import com.biperf.core.domain.budget.BudgetSegmentValueBeanComparator;
import com.biperf.core.domain.budget.PromotionBudgetSweep;
import com.biperf.core.domain.enums.BudgetActionType;
import com.biperf.core.domain.enums.BudgetFinalPayoutRule;
import com.biperf.core.domain.enums.BudgetMasterAwardType;
import com.biperf.core.domain.enums.BudgetOverrideableType;
import com.biperf.core.domain.enums.BudgetReallocationEligType;
import com.biperf.core.domain.enums.BudgetStatusType;
import com.biperf.core.domain.enums.BudgetType;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.budget.BudgetMasterService;
import com.biperf.core.service.budget.BudgetSegmentToBudgetsAssociationRequest;
import com.biperf.core.ui.BaseActionForm;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.StringUtil;
import com.biperf.core.value.BudgetSegmentValueBean;
import com.objectpartners.cms.util.CmsResourceBundle;
import com.objectpartners.cms.util.ContentReaderManager;

/**
 * BudgetMasterForm.
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
 * <td>sharma</td>
 * <td>May 23, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class BudgetMasterForm extends BaseActionForm
{
  // ---------------------------------------------------------------------------
  // Constants
  // ---------------------------------------------------------------------------
  public static final String FORM_NAME = "budgetMasterForm";
  public static final String ALL_BUDGETS = "allBudgets";
  public static final String SPECIFIC_BUDGET = "specificBudget";

  public static final String DEFAULT_SEGMENT_NAME = "Default";

  // ---------------------------------------------------------------------------
  // Fields
  // ---------------------------------------------------------------------------
  private Long id;
  private String budgetName;
  private String nameCmKey;
  private String cmAssetCode;
  private String budgetType = BudgetType.PAX_BUDGET_TYPE;
  private String budgetTypeName;
  private boolean multiPromotion;
  private String awardType;

  private String overrideableType = BudgetOverrideableType.HARD_OVERRIDE;
  private String finalPayoutRule = null;
  private boolean active;
  private boolean allowAdditionalTransferrees;
  private boolean hasBudget;

  private String budgetId;
  private String budgetMasterId;

  private Long[] deleteBudgetMasterIds;
  private Long[] deleteBudgetIds;

  private Long selectedParticipantId;
  private Long selectedNodeId;
  private String budgetsToShow;
  private String searchQuery;
  private Long deleteSegmentId;

  private List<BudgetSegmentValueBean> budgetSegmentVBList = new ArrayList<BudgetSegmentValueBean>();

  /**
   * The date on which the budget starts. Used only when the budget type of the budget master is
   * "central."
   */
  private String startDate;

  /**
   * The date on which the budget ends. Used only when the budget type of the budget master is
   * "central."
   */
  private String endDate;
  private String budgetSweepDate;

  private Long budgetSegmentId;
  private String budgetSegmentName;
  private boolean hasBudgetSweepDate;

  // ---------------------------------------------------------------------------
  // Getter and Setter Methods
  // ---------------------------------------------------------------------------
  public Long getId()
  {
    return id;
  }

  public String getBudgetName()
  {
    return budgetName;
  }

  public String getBudgetTypeName()
  {
    return budgetTypeName;
  }

  public String getNameCmKey()
  {
    return nameCmKey;
  }

  public String getCmAssetCode()
  {
    return cmAssetCode;
  }

  public String getBudgetType()
  {
    return budgetType;
  }

  public boolean isMultiPromotion()
  {
    return multiPromotion;
  }

  public String getAwardType()
  {
    return awardType;
  }

  public String getOverrideableType()
  {
    return overrideableType;
  }

  public boolean isActive()
  {
    return active;
  }

  public boolean isHasBudget()
  {
    return hasBudget;
  }

  public String getBudgetId()
  {
    return budgetId;
  }

  public String getBudgetMasterId()
  {
    return budgetMasterId;
  }

  public Long[] getDeleteBudgetMasterIds()
  {
    return deleteBudgetMasterIds;
  }

  public Long[] getDeleteBudgetIds()
  {
    return deleteBudgetIds;
  }

  public String getStartDate()
  {
    return startDate;
  }

  public String getEndDate()
  {
    return endDate;
  }

  public void setId( Long id )
  {
    this.id = id;
  }

  public void setBudgetName( String budgetName )
  {
    this.budgetName = budgetName;
  }

  public void setBudgetTypeName( String budgetTypeName )
  {
    this.budgetTypeName = budgetTypeName;
  }

  public void setNameCmKey( String nameCmKey )
  {
    this.nameCmKey = nameCmKey;
  }

  public void setCmAssetCode( String cmAssetCode )
  {
    this.cmAssetCode = cmAssetCode;
  }

  public void setBudgetType( String budgetType )
  {
    this.budgetType = budgetType;
  }

  public void setMultiPromotion( boolean multiPromotion )
  {
    this.multiPromotion = multiPromotion;
  }

  public void setAwardType( String awardType )
  {
    this.awardType = awardType;
  }

  public void setOverrideableType( String overrideableType )
  {
    this.overrideableType = overrideableType;
  }

  public void setActive( boolean active )
  {
    this.active = active;
  }

  public void setHasBudget( boolean hasBudget )
  {
    this.hasBudget = hasBudget;
  }

  public void setBudgetId( String budgetId )
  {
    this.budgetId = budgetId;
  }

  public void setBudgetMasterId( String budgetMasterId )
  {
    this.budgetMasterId = budgetMasterId;
  }

  public void setDeleteBudgetMasterIds( Long[] deleteBudgetMasterIds )
  {
    this.deleteBudgetMasterIds = deleteBudgetMasterIds;
  }

  public void setDeleteBudgetIds( Long[] deleteBudgetIds )
  {
    this.deleteBudgetIds = deleteBudgetIds;
  }

  public void setStartDate( String startDate )
  {
    this.startDate = startDate;
  }

  public void setEndDate( String endDate )
  {
    this.endDate = endDate;
  }

  public String getFinalPayoutRule()
  {
    return finalPayoutRule;
  }

  public void setFinalPayoutRule( String finalPayoutRule )
  {
    this.finalPayoutRule = finalPayoutRule;
  }

  public String getBudgetSweepDate()
  {
    return budgetSweepDate;
  }

  public void setBudgetSweepDate( String budgetSweepDate )
  {
    this.budgetSweepDate = budgetSweepDate;
  }

  // ---------------------------------------------------------------------------
  // Load and To Domain Methods
  // ---------------------------------------------------------------------------
  public void loadDomainObject( BudgetMaster budgetMaster )
  {
    setId( budgetMaster.getId() );
    // todo jjd add flag to disable editing for non-default locales
    if ( budgetMaster.getId() != null )
    {
      setBudgetName( ContentReaderManager.getText( budgetMaster.getCmAssetCode(), budgetMaster.getNameCmKey() ) );
    }
    setCmAssetCode( budgetMaster.getCmAssetCode() );
    setNameCmKey( budgetMaster.getNameCmKey() );
    setStartDate( DateUtils.toDisplayString( budgetMaster.getStartDate() ) );
    setEndDate( DateUtils.toDisplayString( budgetMaster.getEndDate() ) );

    if ( budgetMaster.getId() == null )
    {
      addFirstBudgetSegment();
    }

    BudgetType budgetType = budgetMaster.getBudgetType();
    if ( budgetType != null )
    {
      setBudgetType( budgetType.getCode() );
      setBudgetTypeName( budgetType.getName() );
    }

    Set budgetSegments = budgetMaster.getBudgetSegments();
    BudgetSegmentValueBean budgetSegmentVB = null;

    for ( Iterator budgetSegmentIter = budgetSegments.iterator(); budgetSegmentIter.hasNext(); )
    {
      BudgetSegment budgetSegment = (BudgetSegment)budgetSegmentIter.next();

      AssociationRequestCollection segmentAssociationRequestCollection = new AssociationRequestCollection();
      segmentAssociationRequestCollection.add( new BudgetSegmentToBudgetsAssociationRequest() );
      budgetSegment = getBudgetMasterService().getBudgetSegmentById( budgetSegment.getId(), segmentAssociationRequestCollection );
      budgetSegmentVB = loadBudgetSegment( budgetSegment, budgetType );
      budgetSegmentVBList.add( budgetSegmentVB );

    }
    Collections.sort( budgetSegmentVBList, new BudgetSegmentValueBeanComparator() );

    setMultiPromotion( budgetMaster.isMultiPromotion() );

    BudgetOverrideableType overrideType = budgetMaster.getOverrideableType();
    if ( overrideType != null )
    {
      setOverrideableType( overrideType.getCode() );
    }

    BudgetFinalPayoutRule finalPayoutRule = budgetMaster.getFinalPayoutRule();
    if ( finalPayoutRule != null )
    {
      setFinalPayoutRule( finalPayoutRule.getCode() );
    }

    setActive( budgetMaster.isActive() );

    if ( budgetMaster.getAwardType() != null )
    {
      setAwardType( budgetMaster.getAwardType().getCode() );
    }

    setAllowAdditionalTransferrees( budgetMaster.isAllowAdditionalTransferrees() );
  }

  public BudgetMaster toDomainObject()
  {
    BudgetMaster budgetMaster = new BudgetMaster();
    populateDomainObject( budgetMaster );
    return budgetMaster;
  }

  public void populateDomainObject( BudgetMaster budgetMaster )
  {
    budgetMaster.setId( isNewBudgetMaster() ? null : id );
    budgetMaster.setBudgetName( budgetName );
    budgetMaster.setCmAssetCode( cmAssetCode );
    budgetMaster.setNameCmKey( nameCmKey );
    budgetMaster.setBudgetType( BudgetType.lookup( budgetType ) );
    budgetMaster.setAwardType( BudgetMasterAwardType.lookup( awardType ) );
    budgetMaster.setMultiPromotion( multiPromotion );
    budgetMaster.setOverrideableType( BudgetOverrideableType.lookup( overrideableType ) );
    budgetMaster.setFinalPayoutRule( BudgetFinalPayoutRule.lookup( finalPayoutRule ) );
    budgetMaster.setActive( active );

    budgetMaster.setAllowAdditionalTransferrees( allowAdditionalTransferrees );

    budgetMaster.setStartDate( DateUtils.toDate( startDate ) );
    budgetMaster.setEndDate( DateUtils.toDate( endDate ) );

    // budgetSegments
    budgetMaster.getBudgetSegments().clear();
    for ( Iterator<BudgetSegmentValueBean> iter = budgetSegmentVBList.iterator(); iter.hasNext(); )
    {
      BudgetSegmentValueBean budgetSegmentVB = (BudgetSegmentValueBean)iter.next();

      // build budget segment obj
      BudgetSegment budgetSegment = null;
      if ( budgetSegmentVB.getId() != null )
      {
        AssociationRequestCollection segmentAssociationRequestCollection = new AssociationRequestCollection();
        segmentAssociationRequestCollection.add( new BudgetSegmentToBudgetsAssociationRequest() );
        budgetSegment = getBudgetMasterService().getBudgetSegmentById( budgetSegmentVB.getId(), segmentAssociationRequestCollection );
      }
      budgetSegment = populateBudgetSegment( budgetSegment, budgetSegmentVB );

      if ( budgetType.equals( BudgetType.CENTRAL_BUDGET_TYPE ) )
      {
        Budget budget = getCentralBudget( budgetSegment );

        budget.setStatus( budgetMaster.isActive() ? BudgetStatusType.lookup( BudgetStatusType.ACTIVE ) : BudgetStatusType.lookup( BudgetStatusType.SUSPENDED ) );
        budget.setActionType( BudgetActionType.lookup( BudgetActionType.DEPOSIT ) );

        if ( isNewBudgetSegment( budgetSegment ) )
        {
          if ( !budgetSegmentVB.getOriginalValue().isEmpty() )
          {
            BigDecimal originalValueLocal = new BigDecimal( budgetSegmentVB.getOriginalValue() );
            budget.setOriginalValue( originalValueLocal );
            budget.setCurrentValue( originalValueLocal );
          }
          else
          {
            if ( budgetSegmentVB.getAddOnValue() != null )
            {
              BigDecimal originalValueLocal = new BigDecimal( budgetSegmentVB.getAddOnValue() );
              budget.setOriginalValue( originalValueLocal );
              budget.setCurrentValue( originalValueLocal );
            }
          }
        }
        else
        {
          if ( !budgetSegmentVB.getAddOnValue().isEmpty() )
          {
            BigDecimal budgetAddOnValueLocal = new BigDecimal( budgetSegmentVB.getAddOnValue() );
            if ( budget.getOriginalValue() != null )
            {
              budget.setOriginalValue( budget.getOriginalValue().add( budgetAddOnValueLocal ) );
            }
            else
            {
              budget.setOriginalValue( budgetAddOnValueLocal );
            }
            if ( budget.getCurrentValue() != null )
            {
              budget.setCurrentValue( budget.getCurrentValue().add( budgetAddOnValueLocal ) );
            }
            else
            {
              budget.setCurrentValue( budgetAddOnValueLocal );
            }
          }
        }

        budgetSegment.getBudgets().clear();
        budgetSegment.addBudget( budget );
      }
      budgetMaster.addBudgetSegment( budgetSegment );
    }
  }

  private Budget getCentralBudget( BudgetSegment budgetSegment )
  {
    Budget budget = null;

    if ( isNewBudgetSegment( budgetSegment ) )
    {
      budget = new Budget();
    }
    else
    {
      Set budgets = budgetSegment.getBudgets();
      if ( budgets != null && budgets.size() > 0 )
      {
        Iterator iter = budgets.iterator();
        if ( iter.hasNext() )
        {
          budget = (Budget)iter.next();
        }
      }
    }
    return budget;
  }

  public BudgetSegmentValueBean loadBudgetSegment( BudgetSegment budgetSegment, BudgetType budgetType )
  {
    BudgetSegmentValueBean budgetSegmentVB = new BudgetSegmentValueBean();
    budgetSegmentVB.setId( budgetSegment.getId() );
    budgetSegmentVB.setSegmentName( budgetSegment.getName() );
    budgetSegmentVB.setStartDateStr( DateUtils.toDisplayString( budgetSegment.getStartDate() ) );
    budgetSegmentVB.setEndDateStr( DateUtils.toDisplayString( budgetSegment.getEndDate() ) );
    budgetSegmentVB.setAllowBudgetReallocation( budgetSegment.isAllowBudgetReallocation() );

    if ( budgetSegment.getPromotionBudgetSweeps() != null && !budgetSegment.getPromotionBudgetSweeps().isEmpty() )
    {
      for ( PromotionBudgetSweep promotionBudgetSweep : budgetSegment.getPromotionBudgetSweeps() )
      {
        budgetSegmentVB.setBudgetSweepDate( DateUtils.toDisplayString( promotionBudgetSweep.getBudgetSweepDate() ) );
      }
      this.setHasBudgetSweepDate( Boolean.TRUE );
    }

    if ( budgetType.isPaxBudgetType() )
    {
      if ( budgetSegment.getBudgetReallocationEligType() != null )
      {
        budgetSegmentVB.setBudgetReallocationEligTypeCode( budgetSegment.getBudgetReallocationEligType().getCode() );
      }
    }

    // load budget for central
    if ( budgetType.isCentralBudgetType() )
    {
      Set budgets = budgetSegment.getBudgets();
      if ( budgets != null )
      {
        Iterator iter = budgets.iterator();
        if ( iter.hasNext() )
        {
          Budget budget = (Budget)iter.next();
          budgetSegmentVB.setOriginalValue( new Integer( budget.getOriginalValueDisplay() ).toString() );
          budgetSegmentVB.setCurrentValue( new Integer( budget.getCurrentValueDisplay() ).toString() );
          budgetSegmentVB.setBudgetStatus( budget.getStatus() );
          budgetSegmentVB.setAddOnValue( "0" );
        }
      }
    }
    return budgetSegmentVB;
  }

  public BudgetSegment populateBudgetSegment( BudgetSegment budgetSegment, BudgetSegmentValueBean budgetSegmentVB )
  {
    if ( budgetSegment == null )
    {
      budgetSegment = new BudgetSegment();
    }
    budgetSegment.setName( budgetSegmentVB.getSegmentName() );
    budgetSegment.setStartDate( DateUtils.toDate( budgetSegmentVB.getStartDateStr() ) );
    budgetSegment.setEndDate( DateUtils.toDate( budgetSegmentVB.getEndDateStr() ) );
    budgetSegment.setStatus( Boolean.TRUE ); // if delete functionality is added then need to add
                                             // logic
    if ( budgetSegmentVB.getBudgetSweepDate() != null )
    {
      Set<PromotionBudgetSweep> promotionBudgetSweeps = new LinkedHashSet<PromotionBudgetSweep>();
      if ( budgetSegment.getPromotionBudgetSweeps() == null )
      {
        PromotionBudgetSweep promotionBudgetSweep = new PromotionBudgetSweep();
        promotionBudgetSweep.setBudgetSweepDate( DateUtils.toDate( budgetSegmentVB.getBudgetSweepDate() ) );
        promotionBudgetSweeps.add( promotionBudgetSweep );
        budgetSegment.setPromotionBudgetSweeps( promotionBudgetSweeps );
      }
      else
      {
        for ( PromotionBudgetSweep promotionBudgetSweep : budgetSegment.getPromotionBudgetSweeps() )
        {
          promotionBudgetSweep.setBudgetSweepDate( DateUtils.toDate( budgetSegmentVB.getBudgetSweepDate() ) );
          promotionBudgetSweeps.add( promotionBudgetSweep );
          budgetSegment.setPromotionBudgetSweeps( promotionBudgetSweeps );
        }
      }
    }
    budgetSegment.setAllowBudgetReallocation( budgetSegmentVB.isAllowBudgetReallocation() );

    if ( BudgetType.PAX_BUDGET_TYPE.equals( BudgetType.lookup( budgetType ).getCode() ) )
    {
      if ( budgetSegmentVB.isAllowBudgetReallocation() )
      {
        budgetSegment.setBudgetReallocationEligType( BudgetReallocationEligType.lookup( budgetSegmentVB.getBudgetReallocationEligTypeCode() ) );
      }
    }
    else if ( BudgetType.NODE_BUDGET_TYPE.equals( BudgetType.lookup( budgetType ).getCode() ) )
    {
      if ( budgetSegmentVB.isAllowBudgetReallocation() )
      {
        budgetSegment.setBudgetReallocationEligType( BudgetReallocationEligType.lookup( BudgetReallocationEligType.ORG_UNIT_ONLY ) );
      }
    }
    return budgetSegment;
  }

  /**
   * Returns true if the user is creating a new budget master; returns false if the user is editing
   * an existing budget master.
   * 
   * @return true if the user is creating a new budget master; return false if the user is editing
   *         an existing budget master.
   */
  private boolean isNewBudgetMaster()
  {
    return id == null || id.longValue() == 0;
  }

  private boolean isNewBudgetSegment( BudgetSegment budgetSegment )
  {
    return budgetSegment.getId() == null || budgetSegment.getId().longValue() == 0;
  }

  // ---------------------------------------------------------------------------
  // Validation Methods
  // ---------------------------------------------------------------------------
  /**
   * Validates the data in this form.
   * 
   * @param mapping The mapping used to select this instance
   * @param request The servlet request we are processing
   * @return <code>ActionErrors</code> object that encapsulates any validation errors
   */
  public ActionErrors validate( ActionMapping mapping, HttpServletRequest request )
  {
    ActionErrors actionErrors = super.validate( mapping, request );

    if ( actionErrors == null )
    {
      actionErrors = new ActionErrors();
    }

    // TODO: Factor out the following start/end date validation code.
    Date startDateLocal = null;
    Date endDateLocal = null;

    // If this is a central budget, then the budget start date is required.
    if ( startDate == null || startDate.length() == 0 )
    {
      actionErrors.add( "startDate", new ActionMessage( "system.errors.REQUIRED", CmsResourceBundle.getCmsBundle().getString( "admin.budgetmaster.details", "START_DATE" ) ) );
    }
    else
    {
      // The date string must represent a date.
      startDateLocal = DateUtils.toDate( startDate );
      if ( startDateLocal == null )
      {
        actionErrors.add( "startDate", new ActionMessage( "system.errors.DATE", CmsResourceBundle.getCmsBundle().getString( "admin.budgetmaster.details", "START_DATE" ) ) );
      }
    }

    // If this is a central budget, then the budget end date is not required.
    if ( endDate != null && endDate.length() > 0 )
    {
      // The date string must represent a date.
      endDateLocal = DateUtils.toDate( endDate );
      if ( endDateLocal == null )
      {
        actionErrors.add( "endDate", new ActionMessage( "system.errors.DATE", CmsResourceBundle.getCmsBundle().getString( "admin.budgetmaster.details", "END_DATE" ) ) );
      }
    }

    // The start date must be earlier than or equal to the end date.
    if ( startDateLocal != null && endDateLocal != null )
    {
      if ( endDateLocal.before( startDateLocal ) )
      {
        actionErrors.add( "endDate", new ActionMessage( "system.errors.END_BEFORE_START_DATE" ) );
      }

      if ( endDateLocal.compareTo( startDateLocal ) == 0 )
      {
        actionErrors.add( "startDate", new ActionMessage( "admin.budgetmaster.details.BUDGET_MASTER_START_END_DATE_EQUAL" ) );
      }
    }
    if ( getBudgetType().equals( BudgetType.CENTRAL_BUDGET_TYPE ) )
    {
      // The final payout rule is required for Central budget
      if ( this.finalPayoutRule == null )
      {
        actionErrors.add( "finalPayoutRule", new ActionMessage( "system.errors.REQUIRED", CmsResourceBundle.getCmsBundle().getString( "admin.budgetmaster.details", "FINAL_PAYOUT_RULE" ) ) );
      }
    }

    // budget segment validations
    if ( budgetSegmentVBList == null || budgetSegmentVBList.size() == 0 )
    {
      actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "admin.budgetmaster.details.ATLEAST_ONE_SEGMENT_REQ" ) );
    }
    else if ( budgetSegmentVBList != null && budgetSegmentVBList.size() > 0 )
    {
      BudgetSegmentValueBean lastBudgetSegmentValueBean = budgetSegmentVBList.get( budgetSegmentVBList.size() - 1 );
      int index = 0;
      for ( Iterator budgetSegmentIter = budgetSegmentVBList.iterator(); budgetSegmentIter.hasNext(); )
      {
        index = 1;
        BudgetSegmentValueBean budgetSegmentVB = (BudgetSegmentValueBean)budgetSegmentIter.next();

        actionErrors = validateSegmentName( budgetSegmentVB, actionErrors, index );
        actionErrors = validateSegmentStartDate( budgetSegmentVB, actionErrors, index );
        actionErrors = validateSegmentEndDate( budgetSegmentVB, actionErrors, index, lastBudgetSegmentValueBean );
        actionErrors = validateOriginalValue( budgetSegmentVB, actionErrors, index, lastBudgetSegmentValueBean );
        actionErrors = validateAddOnValue( budgetSegmentVB, actionErrors, index, lastBudgetSegmentValueBean );
        index++;
      } // end for

      actionErrors = validateSegmentEndDate( actionErrors, endDateLocal, lastBudgetSegmentValueBean );

      if ( budgetSegmentVBList.size() == 1 )
      {
        BudgetSegmentValueBean budgetSegmentVB = budgetSegmentVBList.get( 0 );
        // The start date must be earlier than or equal to the end date.
        if ( budgetSegmentVB.getStartDate() != null && budgetSegmentVB.getEndDate() != null )
        {
          if ( budgetSegmentVB.getEndDate().before( budgetSegmentVB.getStartDate() ) )
          {
            actionErrors.add( "budgetSegments", new ActionMessage( "system.errors.END_BEFORE_START_DATE" ) );
          }
          if ( budgetSegmentVB.getEndDate().compareTo( budgetSegmentVB.getStartDate() ) == 0 )
          {
            actionErrors.add( "budgetSegments", new ActionMessage( "admin.budgetmaster.details.TIME_PERIOD_START_END_DATE_EQUAL" ) );
          }
        }
      }

      // all required field must be filled in before doing overlap check
      if ( actionErrors.isEmpty() )
      {
        if ( validateDateOverlap() )
        {
          actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "admin.budgetmaster.details.SEGMENT_DATE_OVERLAP" ) );
        }
        if ( validateNextDay() )
        {
          actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "admin.budgetmaster.details.SEGMENT_START_DATE_NON_NEXT_DAY" ) );
        }
        if ( sameStartDateEndDate() )
        {
          actionErrors.add( "budgetSegments", new ActionMessage( "admin.budgetmaster.details.TIME_PERIOD_START_END_DATE_EQUAL" ) );
        }
      }
    } // end if

    // A cash budget master must be a central budget master
    if ( BudgetMasterAwardType.CASH.equals( awardType ) && !BudgetType.CENTRAL_BUDGET_TYPE.equals( budgetType ) )
    {
      actionErrors.add( "budgetType", new ActionMessage( "admin.budgetmaster.details.CASH_CENTRAL_TYPES" ) );
    }

    return actionErrors;
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
      if ( !nextBudgetSegmentVB.getStartDate().equals( DateUtils.getNextDay( currentBudgetSegmentVB.getEndDate() ) ) )
      {
        return true;
      }
    } // end for
    return false;
  }

  private ActionErrors validateSegmentName( BudgetSegmentValueBean budgetSegmentVB, ActionErrors actionErrors, int index )
  {
    String segmentName = budgetSegmentVB.getSegmentName();
    Long budgetSegmentId = budgetSegmentVB.getId();
    if ( StringUtils.isEmpty( segmentName ) )
    {
      actionErrors.add( ActionErrors.GLOBAL_MESSAGE,
                        new ActionMessage( "system.errors.REQUIRED", CmsResourceBundle.getCmsBundle().getString( "admin.budgetmaster.details", "BUDGET_SEGMENT_NAME" ), index ) );
    }
    else
    {
      if ( segmentName != null && segmentName.length() > 50 )
      {
        actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "admin.budgetmaster.details.SEGMENT_NAME_TOO_LONG", segmentName ) );
      }
      if ( !getBudgetMasterService().isBudgetSegmentNameUnique( id, segmentName, budgetSegmentId ) )
      {
        actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "admin.budgetmaster.details.SEGMENT_NAME_NOT_UNIQUE", segmentName ) );
      }
    }
    return actionErrors;
  }

  private ActionErrors validateSegmentStartDate( BudgetSegmentValueBean budgetSegmentVB, ActionErrors actionErrors, int index )
  {

    Date segmentStartDateLocal = null;
    if ( budgetSegmentVB.getStartDateStr() == null || budgetSegmentVB.getStartDateStr().length() == 0 )
    {
      actionErrors.add( ActionErrors.GLOBAL_MESSAGE,
                        new ActionMessage( "system.errors.REQUIRED", CmsResourceBundle.getCmsBundle().getString( "admin.budgetmaster.details", "SEGMENT_START_DATE" ), index ) );
    }
    else
    {
      // The date string must represent a date.
      segmentStartDateLocal = DateUtils.toDate( budgetSegmentVB.getStartDateStr() );
      if ( segmentStartDateLocal == null )
      {
        actionErrors.add( ActionErrors.GLOBAL_MESSAGE,
                          new ActionMessage( "system.errors.DATE", CmsResourceBundle.getCmsBundle().getString( "admin.budgetmaster.details", "SEGMENT_START_DATE" ), index ) );
      }
    }
    return actionErrors;
  }

  private ActionErrors validateSegmentEndDate( BudgetSegmentValueBean budgetSegmentVB, ActionErrors actionErrors, int index, BudgetSegmentValueBean lastBudgetSegmentValueBean )
  {
    if ( endDate != null && endDate.length() > 0 )
    {
      Date segmentEndDateLocal = null;
      // only the last element's end date can be empty
      if ( budgetSegmentVB.getEndDateStr() == null || budgetSegmentVB.getEndDateStr().isEmpty()
          || budgetSegmentVB.getEndDateStr().length() == 0 && !budgetSegmentVB.getId().equals( lastBudgetSegmentValueBean.getId() ) )
      {
        actionErrors.add( ActionErrors.GLOBAL_MESSAGE,
                          new ActionMessage( "system.errors.REQUIRED", CmsResourceBundle.getCmsBundle().getString( "admin.budgetmaster.details", "SEGMENT_END_DATE" ), index ) );
      }
      else
      {
        // The date string must represent a date.
        segmentEndDateLocal = DateUtils.toDate( budgetSegmentVB.getEndDateStr() );
        if ( segmentEndDateLocal == null )
        {
          actionErrors.add( ActionErrors.GLOBAL_MESSAGE,
                            new ActionMessage( "system.errors.DATE", CmsResourceBundle.getCmsBundle().getString( "admin.budgetmaster.details", "SEGMENT_END_DATE" ), index ) );
        }
        else
        {
          if ( endDate != null && endDate.length() > 0 && DateUtils.toDate( endDate ) != null && segmentEndDateLocal.after( DateUtils.toDate( endDate ) ) )
          {
            actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "admin.budgetmaster.details.SEGMENT_END_DATE_GREATER", budgetSegmentVB.getEndDateStr(), endDate ) );
          }
        }
      }
    }
    return actionErrors;
  }

  private ActionErrors validateAddOnValue( BudgetSegmentValueBean budgetSegmentVB, ActionErrors actionErrors, int index, BudgetSegmentValueBean lastBudgetSegmentValueBean )
  {
    if ( !StringUtil.isEmpty( budgetSegmentVB.getAddOnValue() ) )
    {
      try
      {
        Long.parseLong( budgetSegmentVB.getAddOnValue() );
      }
      catch( NumberFormatException nfe )
      {
        // add the error
        actionErrors.add( ActionErrors.GLOBAL_MESSAGE,
                          new ActionMessage( "system.errors.INVALID", CmsResourceBundle.getCmsBundle().getString( "claims.product.approval.details", "QUANTITY" ), index ) );
      }
    }
    return actionErrors;
  }

  private ActionErrors validateOriginalValue( BudgetSegmentValueBean budgetSegmentVB, ActionErrors actionErrors, int index, BudgetSegmentValueBean lastBudgetSegmentValueBean )
  {
    if ( !StringUtil.isEmpty( budgetSegmentVB.getOriginalValue() ) )
    {
      try
      {
        Long.parseLong( budgetSegmentVB.getOriginalValue() );
      }
      catch( NumberFormatException nfe )
      {
        // add the error
        actionErrors.add( ActionErrors.GLOBAL_MESSAGE,
                          new ActionMessage( "system.errors.INVALID", CmsResourceBundle.getCmsBundle().getString( "admin.budgetmaster.details", "BUDGET_AMOUNT" ), index ) );
      }
    }
    return actionErrors;
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

  public Long getSelectedParticipantId()
  {
    return selectedParticipantId;
  }

  public void setSelectedParticipantId( Long selectedParticipantId )
  {
    this.selectedParticipantId = selectedParticipantId;
  }

  public Long getSelectedNodeId()
  {
    return selectedNodeId;
  }

  public void setSelectedNodeId( Long selectedNodeId )
  {
    this.selectedNodeId = selectedNodeId;
  }

  public String getBudgetsToShow()
  {
    return budgetsToShow;
  }

  public void setBudgetsToShow( String budgetsToShow )
  {
    this.budgetsToShow = budgetsToShow;
  }

  public String getSearchQuery()
  {
    return searchQuery;
  }

  public void setSearchQuery( String searchQuery )
  {
    this.searchQuery = searchQuery;
  }

  public List<BudgetSegmentValueBean> getBudgetSegmentVBList()
  {
    if ( budgetSegmentVBList == null )
    {
      budgetSegmentVBList = new ArrayList<BudgetSegmentValueBean>();
    }
    return budgetSegmentVBList;
  }

  public void setBudgetSegmentVBList( List<BudgetSegmentValueBean> budgetSegmentVBList )
  {
    this.budgetSegmentVBList = budgetSegmentVBList;
  }

  public BudgetSegmentValueBean getBudgetSegmentList( int index )
  {
    try
    {
      return (BudgetSegmentValueBean)budgetSegmentVBList.get( index );
    }
    catch( Exception anException )
    {
      return null;
    }
  }

  public void reset( ActionMapping mapping, HttpServletRequest request )
  {
    // reset needs to be used to populate an empty list of
    // BudgetSegment. If this is not done, the form wont initialize
    // properly.
    budgetSegmentVBList = getEmptyValueList( RequestUtils.getOptionalParamInt( request, "budgetSegmentVBListSize" ) );

    // Insurance policy to make sure points is the default award type selection
    awardType = BudgetMasterAwardType.POINTS;
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

  public void addFirstBudgetSegment()
  {
    BudgetSegmentValueBean budgetSegment = new BudgetSegmentValueBean();
    budgetSegment.setSegmentName( DEFAULT_SEGMENT_NAME );
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

  public Long getBudgetSegmentId()
  {
    return budgetSegmentId;
  }

  public void setBudgetSegmentId( Long budgetSegmentId )
  {
    this.budgetSegmentId = budgetSegmentId;
  }

  public String getBudgetSegmentName()
  {
    return budgetSegmentName;
  }

  public void setBudgetSegmentName( String budgetSegmentName )
  {
    this.budgetSegmentName = budgetSegmentName;
  }

  private BudgetMasterService getBudgetMasterService()
  {
    return (BudgetMasterService)BeanLocator.getBean( BudgetMasterService.BEAN_NAME );
  }

  public boolean isBudgetMasterStartDateEditable()
  {
    boolean editable = false;
    if ( id == null || id == 0L || startDate != null && DateUtils.toDate( startDate ).after( DateUtils.getCurrentDate() ) )
    {
      editable = true;
    }
    return editable;
  }

  public boolean isHasBudgetSweepDate()
  {
    return hasBudgetSweepDate;
  }

  public void setHasBudgetSweepDate( boolean hasBudgetSweepDate )
  {
    this.hasBudgetSweepDate = hasBudgetSweepDate;
  }

  public Long getDeleteSegmentId()
  {
    return deleteSegmentId;
  }

  public void setDeleteSegmentId( Long deleteSegmentId )
  {
    this.deleteSegmentId = deleteSegmentId;
  }

  public boolean isAllowAdditionalTransferrees()
  {
    return allowAdditionalTransferrees;
  }

  public void setAllowAdditionalTransferrees( boolean allowAdditionalTransferrees )
  {
    this.allowAdditionalTransferrees = allowAdditionalTransferrees;
  }
}
