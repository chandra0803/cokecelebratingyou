
package com.biperf.core.ui.budget;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import com.biperf.core.domain.budget.Budget;
import com.biperf.core.domain.budget.BudgetMaster;
import com.biperf.core.domain.enums.BudgetOverrideableType;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.participant.ParticipantInfoView;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.ui.BaseActionForm;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.BudgetUtils;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.BudgetReallocationComparator;
import com.biperf.core.value.BudgetReallocationValueBean;
import com.objectpartners.cms.util.CmsResourceBundle;
import com.objectpartners.cms.util.ContentReaderManager;

public class BudgetReallocationForm extends BaseActionForm
{
  private static final long serialVersionUID = 1L;

  private Long budgetMasterId;
  private String budgetMasterName;
  private Long budgetSegmentId;
  private String budgetSegmentName;
  private String method;
  private boolean budgetSelected;
  private boolean budgetSegmentSelected;

  private List<BudgetReallocationValueBean> childReallocationBudgetList = new ArrayList<BudgetReallocationValueBean>();

  private String ownerBudgetCurrentValue;
  private String totalAdjustments;
  private String ownerBudgetAfterAdjustments;
  private String ownerBudgetId;

  // node-based budgets only
  private String ownerBudgetNodeId;

  private ArrayList<ParticipantInfoView> participants = new ArrayList<ParticipantInfoView>();

  private boolean nodeBudget;

  private boolean NA;

  public boolean isNodeBudget()
  {
    return nodeBudget;
  }

  public void setNodeBudget( boolean nodeBudget )
  {
    this.nodeBudget = nodeBudget;
  }

  public ArrayList<ParticipantInfoView> getParticipantsAsList()
  {
    return participants;
  }

  public ParticipantInfoView getParticipants( int index )
  {
    while ( index >= participants.size() )
    {
      participants.add( new ParticipantInfoView() );
    }
    return (ParticipantInfoView)participants.get( index );
  }

  public void setParticipantsAsList( ParticipantInfoView participant )
  {
    participants.add( participant );
  }

  public List<BudgetReallocationValueBean> getChildReallocationBudgetList()
  {
    return childReallocationBudgetList;
  }

  public void setChildReallocationBudgetList( List<BudgetReallocationValueBean> childReallocationBudgetList )
  {
    this.childReallocationBudgetList = childReallocationBudgetList;
  }

  /**
     * Accessor for the value list
     * 
     * @param index
     * @return Single instance of BudgetReallocationValueBean from the value list
     */
  public BudgetReallocationValueBean getChildReallocationBean( int index )
  {
    while ( childReallocationBudgetList.size() <= index )
    {
      childReallocationBudgetList.add( new BudgetReallocationValueBean() );
    }
    return childReallocationBudgetList.get( index );
  }

  /**
     * Reset all properties to their default values.
     * 
     * @param mapping The mapping used to select this instance
     * @param request The servlet request we are processing
     */
  public void reset( ActionMapping mapping, HttpServletRequest request )
  {
    // reset needs to be used to populate an empty list of
    // BudgetReallocationValueBean. If this is not done, the form wont initialize
    // properly.
    childReallocationBudgetList = getEmptyValueList( RequestUtils.getOptionalParamInt( request, "childReallocationBudgetListCount" ) );
  }

  /**
    * resets the childReallocationBudgetList with empty BudgetReallocationForm beans
    * 
    * @param valueListCount
    * @return List
    */
  private List<BudgetReallocationValueBean> getEmptyValueList( int valueListCount )
  {
    List<BudgetReallocationValueBean> valueList = new ArrayList<BudgetReallocationValueBean>();

    for ( int i = 0; i < valueListCount; i++ )
    {
      // create an empty BudgetReallocationValueBean
      BudgetReallocationValueBean giverBean = new BudgetReallocationValueBean();
      valueList.add( giverBean );
    }

    return valueList;
  }

  public void load( List<BudgetReallocationValueBean> childNodeOwners, BudgetMaster budgetMaster, Budget nodeOwnerBudget )
  {
    this.budgetMasterId = budgetMaster.getId();
    this.budgetSegmentId = nodeOwnerBudget.getBudgetSegment().getId();
    this.ownerBudgetNodeId = nodeOwnerBudget.getNode() == null ? null : nodeOwnerBudget.getNode().getId().toString();
    this.ownerBudgetId = nodeOwnerBudget.getId().toString();
    this.budgetMasterName = ContentReaderManager.getText( budgetMaster.getCmAssetCode(), budgetMaster.getNameCmKey() );

    if ( nodeOwnerBudget.getCurrentValueDisplay() < 0 && budgetMaster.getOverrideableType().getCode().equals( BudgetOverrideableType.HARD_OVERRIDE ) )
    {
      this.ownerBudgetCurrentValue = Integer.toString( 0 );
    }
    else
    {
      this.ownerBudgetCurrentValue = "" + (int)Math.floor( nodeOwnerBudget.getCurrentValue().doubleValue() );// Integer.toString(
                                                                                                             // nodeOwnerBudget.getCurrentValueDisplay()
                                                                                                             // );
    }

    if ( childNodeOwners != null && !childNodeOwners.isEmpty() )
    {
      childReallocationBudgetList.addAll( childNodeOwners );

      for ( BudgetReallocationValueBean childNode : childNodeOwners )
      {
        if ( nodeOwnerBudget.getNode() != null )
        {
          if ( nodeOwnerBudget.getNode().getName().equals( childNode.getNodeName() ) )
          {
            childReallocationBudgetList.remove( childNode );
          }
        }
      }
      Collections.sort( childReallocationBudgetList, new BudgetReallocationComparator() );
    }
  }

  public void addExternalParticipant( Budget budget, Participant participant, BigDecimal US_MEDIA_VALUE )
  {
    BudgetReallocationValueBean budgetReallocationValueBean = new BudgetReallocationValueBean();
    budgetReallocationValueBean.setChildBudgetId( null );

    // Check if the searched user is in logged in user org unit or below.
    // If the person is in orgunit and below then do not add because it will already populated.
    // If the person not belongs to orgunit and it is in different org then add the pax and display
    // the values as N/A
    boolean isUserinOrgunit = getUserService().IsUserinOrgUnitorBelow( UserManager.getUserId(), participant.getId() );

    if ( !isUserinOrgunit )
    {
      if ( isNodeBudget() )
      {
        if ( budget != null )
        {
          final BigDecimal OWNER_MEDIA_VALUE = getUserService().getBudgetMediaValueForUser( UserManager.getUserId() );
          BigDecimal convertedCurrentValue = BudgetUtils.applyMediaConversion( budget.getCurrentValue(), US_MEDIA_VALUE, OWNER_MEDIA_VALUE );
          budgetReallocationValueBean.setNodeName( participant.getPrimaryUserNode().getNode().getName() );
          budgetReallocationValueBean.setBudgetSpent( "0" );
          budgetReallocationValueBean.setCurrentBudget( "" + (int)Math.floor( convertedCurrentValue.doubleValue() ) );
          budgetReallocationValueBean.setChildNodeOwnerNodeId( participant.getPrimaryUserNode().getNode().getId().toString() );
          budgetReallocationValueBean.setUserId( "" );
          budgetReallocationValueBean.setAdjustmentAmount( "0" );
          budgetReallocationValueBean.setChildBudgetId( budget.getId() );
          budgetReallocationValueBean.setNA( true );
        }
        else
        {
          budgetReallocationValueBean.setNodeName( participant.getPrimaryUserNode().getNode().getName() );
          budgetReallocationValueBean.setBudgetSpent( "0" );
          budgetReallocationValueBean.setCurrentBudget( "0" );
          budgetReallocationValueBean.setChildNodeOwnerNodeId( participant.getPrimaryUserNode().getNode().getId().toString() );
          budgetReallocationValueBean.setUserId( "" );
          budgetReallocationValueBean.setAdjustmentAmount( "0" );
          budgetReallocationValueBean.setNA( true );
        }
      }
      else
      {
        if ( budget != null )
        {
          final BigDecimal OWNER_MEDIA_VALUE = getUserService().getBudgetMediaValueForUser( budget.getUser().getId() );
          BigDecimal convertedCurrentValue = BudgetUtils.applyMediaConversion( budget.getCurrentValue(), US_MEDIA_VALUE, OWNER_MEDIA_VALUE );
          budgetReallocationValueBean.setNodeName( budget.getUser().getNameFLNoComma() );
          budgetReallocationValueBean.setBudgetSpent( "0" );
          budgetReallocationValueBean.setCurrentBudget( "" + (int)Math.floor( convertedCurrentValue.doubleValue() ) );
          budgetReallocationValueBean.setChildNodeOwnerNodeId( "" );
          budgetReallocationValueBean.setUserId( budget.getUser().getId().toString() );
          budgetReallocationValueBean.setAdjustmentAmount( "0" );
          budgetReallocationValueBean.setChildBudgetId( budget.getId() );
          budgetReallocationValueBean.setNA( true );
        }
        else
        {
          budgetReallocationValueBean.setNodeName( participant.getNameFLNoComma() );
          budgetReallocationValueBean.setBudgetSpent( "0" );
          budgetReallocationValueBean.setCurrentBudget( "0" );
          budgetReallocationValueBean.setChildNodeOwnerNodeId( "" );
          budgetReallocationValueBean.setUserId( participant.getId().toString() );
          budgetReallocationValueBean.setAdjustmentAmount( "0" );
          budgetReallocationValueBean.setNA( true );
        }
      }
      childReallocationBudgetList.add( 0, budgetReallocationValueBean );
      NA = true;
    }
  }

  public ActionErrors validate( ActionMapping mapping, HttpServletRequest request )
  {
    ActionErrors errors = super.validate( mapping, request );
    if ( errors == null )
    {
      errors = new ActionErrors();
    }

    if ( this.getMethod() != null && this.getMethod().equals( "prepareReview" ) )
    {
      boolean hasAdjustmentAmount = false;
      Integer adjustmentAmountIntegerValue;
      int totalAdjustmentAmount = 0;
      boolean hasAmount = false;

      for ( BudgetReallocationValueBean bean : getChildReallocationBudgetList() )
      {
        try
        {
          if ( StringUtils.isNotBlank( bean.getAdjustmentAmount() ) )
          {
            hasAdjustmentAmount = true;
            if ( !bean.getAdjustmentAmount().equals( "0" ) && !bean.getAdjustmentAmount().equals( "0" ) && !hasAmount )
            {
              hasAmount = true;
            }
            // if adjustment amount is not valid integer then add error message
            if ( !validateInteger( bean.getAdjustmentAmount() ) )
            {
              errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "budget.reallocation.ADJUSTMENT_AMOUNT_INTEGER" ) );
              break;
            }
            adjustmentAmountIntegerValue = NumberUtils.createInteger( bean.getAdjustmentAmount() );
            totalAdjustmentAmount += adjustmentAmountIntegerValue.intValue();

            if ( StringUtils.isNotBlank( bean.getCurrentBudget() ) )
            {
              int currentValue = Integer.parseInt( bean.getCurrentBudget() );

              if ( adjustmentAmountIntegerValue <= 0 )
              {
                adjustmentAmountIntegerValue = -adjustmentAmountIntegerValue;
                if ( currentValue < adjustmentAmountIntegerValue )
                {
                  errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "budget.reallocation.SMALL_AMOUNT_REQUIRE", currentValue ) );
                }
              }
            }
          }
        }
        catch( NumberFormatException e )
        {
          errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "budget.reallocation.ADJUSTMENT_AMOUNT_INTEGER" ) );
        }
      }

      // if node owner current budget value is less than adjustment amount then add error message
      if ( hasAdjustmentAmount )
      {
        int ownerCurrentValue = Integer.parseInt( ownerBudgetCurrentValue );

        if ( totalAdjustmentAmount > ownerCurrentValue )
        {
          errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "budget.reallocation.ADJUSTMENT_AMOUNT_EXCEED" ) );
        }
      }

      // At least one record should have adjustment amount otherwise add error message
      if ( !hasAdjustmentAmount || !hasAmount )
      {
        errors.add( ActionConstants.ERROR_MESSAGE_PROPERTY, new ActionMessage( "system.errors.REQUIRED", CmsResourceBundle.getCmsBundle().getString( "budget.reallocation", "ADJUSTMENT_AMOUNT" ) ) );
      }
    }

    return errors;
  }

  private boolean validateInteger( String string )
  {
    Integer numericValue = getIntegerValue( string );
    return numericValue != null && ( numericValue.intValue() >= 0 || numericValue.intValue() <= 0 );
  }

  private Integer getIntegerValue( String string )
  {
    Integer integerValue = null;
    try
    {
      if ( StringUtils.isNotEmpty( string ) )
      {
        integerValue = NumberUtils.createInteger( string );
      }
    }
    catch( NumberFormatException nfe )
    {
    }
    return integerValue;
  }

  public String getOwnerBudgetNodeId()
  {
    return ownerBudgetNodeId;
  }

  public void setOwnerBudgetNodeId( String ownerBudgetNodeId )
  {
    this.ownerBudgetNodeId = ownerBudgetNodeId;
  }

  public String getTotalAdjustments()
  {
    return totalAdjustments;
  }

  public void setTotalAdjustments( String totalAdjustments )
  {
    this.totalAdjustments = totalAdjustments;
  }

  public String getOwnerBudgetAfterAdjustments()
  {
    return ownerBudgetAfterAdjustments;
  }

  public void setOwnerBudgetAfterAdjustments( String ownerBudgetAfterAdjustments )
  {
    this.ownerBudgetAfterAdjustments = ownerBudgetAfterAdjustments;
  }

  public void setMethod( String method )
  {
    this.method = method;
  }

  public String getMethod()
  {
    return method;
  }

  public void setBudgetSelected( boolean budgetSelected )
  {
    this.budgetSelected = budgetSelected;
  }

  public boolean isBudgetSelected()
  {
    return budgetSelected;
  }

  public int getChildReallocationBudgetListCount()
  {
    return childReallocationBudgetList.size();
  }

  public void setOwnerBudgetCurrentValue( String ownerBudgetCurrentValue )
  {
    this.ownerBudgetCurrentValue = ownerBudgetCurrentValue;
  }

  public String getOwnerBudgetCurrentValue()
  {
    return ownerBudgetCurrentValue;
  }

  public void setOwnerBudgetId( String ownerBudgetId )
  {
    this.ownerBudgetId = ownerBudgetId;
  }

  public String getOwnerBudgetId()
  {
    return ownerBudgetId;
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

  public boolean isBudgetSegmentSelected()
  {
    return budgetSegmentSelected;
  }

  public void setBudgetSegmentSelected( boolean budgetSegmentSelected )
  {
    this.budgetSegmentSelected = budgetSegmentSelected;
  }

  public boolean isNA()
  {
    return NA;
  }

  public void setNA( boolean nA )
  {
    NA = nA;
  }

  private UserService getUserService()
  {
    return (UserService)BeanLocator.getBean( UserService.BEAN_NAME );
  }
}
