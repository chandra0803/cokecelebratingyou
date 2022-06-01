
package com.biperf.core.ui.budget;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.domain.budget.Budget;
import com.biperf.core.domain.budget.BudgetMaster;
import com.biperf.core.domain.budget.BudgetReallocationHistoryBean;
import com.biperf.core.domain.budget.BudgetSegment;
import com.biperf.core.domain.country.Country;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.budget.BudgetMasterService;
import com.biperf.core.service.budget.BudgetToNodeOwnersAddressAssociationRequest;
import com.biperf.core.service.country.CountryService;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.utils.PresentationUtils;
import com.biperf.core.utils.BudgetUtils;
import com.biperf.core.utils.ClientStateUtils;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.BudgetAdjustmentValueBean;
import com.objectpartners.cms.util.CmsResourceBundle;
import com.objectpartners.cms.util.ContentReaderManager;

public class BudgetReallocationHistoryAction extends BaseDispatchAction
{

  public ActionForward execute( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    // Populate form parameters
    Long budgetMasterId = null;
    Long budgetSegmentId = null;
    Long ownerBudgetId = null;
    String startDate = null;
    String endDate = null;
    Boolean isOrderByDateAscending = null;

    BudgetReallocationHistoryForm budgetReallocationHistoryForm = (BudgetReallocationHistoryForm)form;

    if ( budgetReallocationHistoryForm.getBudgetMasterId() != null )
    {

      budgetMasterId = new Long( budgetReallocationHistoryForm.getBudgetMasterId() );
    }
    else
    {
      budgetMasterId = new Long( ClientStateUtils.getParameterValue( request, ClientStateUtils.getClientStateMap( request ), "budgetMasterId" ) );
    }

    if ( budgetReallocationHistoryForm.getBudgetSegmentId() != null )
    {

      budgetSegmentId = new Long( budgetReallocationHistoryForm.getBudgetSegmentId() );
    }
    else
    {
      budgetSegmentId = new Long( ClientStateUtils.getParameterValue( request, ClientStateUtils.getClientStateMap( request ), "budgetSegmentId" ) );
    }

    if ( budgetReallocationHistoryForm.getOwnerBudgetId() != null )
    {
      ownerBudgetId = new Long( budgetReallocationHistoryForm.getOwnerBudgetId() );
    }
    else
    {
      ownerBudgetId = new Long( ClientStateUtils.getParameterValue( request, ClientStateUtils.getClientStateMap( request ), "ownerBudgetId" ) );
    }

    if ( budgetReallocationHistoryForm.getStartDate() != null )
    {
      startDate = budgetReallocationHistoryForm.getStartDate();
    }
    else
    {
      startDate = DateUtils.toDisplayString( getDefaultStartDate() );
    }

    if ( budgetReallocationHistoryForm.getEndDate() != null )
    {
      endDate = budgetReallocationHistoryForm.getEndDate();
    }
    else
    {
      endDate = DateUtils.toDisplayString( getDefaultEndDate() );
    }

    if ( budgetReallocationHistoryForm.getIsOrderByDateAscending() != null )
    {
      isOrderByDateAscending = budgetReallocationHistoryForm.getIsOrderByDateAscending();
    }
    else
    {
      isOrderByDateAscending = true;
    }

    budgetReallocationHistoryForm.setStartDate( startDate );
    budgetReallocationHistoryForm.setEndDate( endDate );
    budgetReallocationHistoryForm.setBudgetMasterId( budgetMasterId.toString() );
    budgetReallocationHistoryForm.setBudgetSegmentId( budgetSegmentId.toString() );
    budgetReallocationHistoryForm.setOwnerBudgetId( ownerBudgetId.toString() );
    budgetReallocationHistoryForm.setIsOrderByDateAscending( isOrderByDateAscending );

    // Fetch budget reallocation list
    BudgetMaster budgetMaster = getBudgetMasterService().getBudgetMasterById( budgetMasterId, null );
    BudgetSegment budgetSegment = getBudgetMasterService().getBudgetSegmentById( budgetSegmentId, null );
    List<BudgetAdjustmentValueBean> budgetAdjustmentList = getBudgetReallocationHistory( budgetSegmentId, ownerBudgetId, startDate, endDate, isOrderByDateAscending );

    request.setAttribute( "budgetAdjustmentList", budgetAdjustmentList );
    request.setAttribute( "pageSize", new Integer( PresentationUtils.getDisplayTablePageSize( budgetAdjustmentList.size() ) ) );
    request.setAttribute( "budgetMasterName", ContentReaderManager.getText( budgetMaster.getCmAssetCode(), budgetMaster.getNameCmKey() ) );
    request.setAttribute( "budgetSegmentName", budgetSegment.getPaxDisplaySegmentName() );

    return mapping.findForward( ActionConstants.SUCCESS_FORWARD );
  }

  @SuppressWarnings( "unchecked" )
  private List<BudgetAdjustmentValueBean> getBudgetReallocationHistory( Long budgetSegmentId, Long ownerBudgetId, String startDate, String endDate, boolean isOrderByDateAscending )
  {
    final BigDecimal US_MEDIA_VALUE = getCountryService().getBudgetMediaValueByCountryCode( Country.UNITED_STATES );

    AssociationRequestCollection budgetAssociationRequestCollection = new AssociationRequestCollection();
    budgetAssociationRequestCollection.add( new BudgetToNodeOwnersAddressAssociationRequest() );
    Budget ownerBudget = getBudgetMasterService().getBudgetbyId( ownerBudgetId, budgetAssociationRequestCollection );
    BudgetMaster ownerBudgetMaster = ownerBudget.getBudgetSegment().getBudgetMaster();
    BigDecimal OWNER_MEDIA_VALUE = null;

    OWNER_MEDIA_VALUE = getUserService().getBudgetMediaValueForUser( UserManager.getUserId() );

    List<BudgetReallocationHistoryBean> budgetHistoryList = new ArrayList<BudgetReallocationHistoryBean>();
    if ( ownerBudgetMaster.isNodeBudget() )
    {
      budgetHistoryList = getBudgetMasterService().getBudgetAllocationHistoryForNode( ownerBudget.getNode().getId(), budgetSegmentId, startDate, endDate, isOrderByDateAscending );
    }
    else if ( ownerBudgetMaster.isParticipantBudget() )
    {
      budgetHistoryList = getBudgetMasterService().getBudgetAllocationHistoryForPax( ownerBudget.getUser().getId(), budgetSegmentId, startDate, endDate, isOrderByDateAscending );
    }

    List<BudgetAdjustmentValueBean> budgetAdjustmentList = new ArrayList<BudgetAdjustmentValueBean>();
    for ( BudgetReallocationHistoryBean budgetHistoryBean : budgetHistoryList )
    {
      if ( !budgetHistoryBean.getBudgetId().equals( ownerBudget.getId() ) )
      {
        BudgetAdjustmentValueBean bean = new BudgetAdjustmentValueBean();
        BigDecimal adjustedAmount = BudgetUtils.applyMediaConversion( budgetHistoryBean.getAmount(), US_MEDIA_VALUE, OWNER_MEDIA_VALUE );
        Budget childOwnerBudget = getBudgetMasterService().getBudget( budgetSegmentId, budgetHistoryBean.getBudgetId() );
        BudgetMaster childOwnerBudgetMaster = ownerBudget.getBudgetSegment().getBudgetMaster();

        bean.setBudgetHistoryDate( DateUtils.toDisplayString( budgetHistoryBean.getDateCreated() ) );

        if ( budgetHistoryBean.getSource().equals( "admin" ) )
        {
          bean.setBudgetName( ( childOwnerBudgetMaster.isNodeBudget() ? childOwnerBudget.getNode().getName() : childOwnerBudget.getUser().getNameFLNoComma() ) + " "
              + CmsResourceBundle.getCmsBundle().getString( "budget.reallocation.ADMIN_TRANSFER" ) );
        }
        else
        {
          bean.setBudgetName( childOwnerBudgetMaster.isNodeBudget() ? childOwnerBudget.getNode().getName() : childOwnerBudget.getUser().getNameFLNoComma() );
        }

        bean.setAdjustedAmount( String.valueOf( BudgetUtils.getBudgetDisplayValue( adjustedAmount ) ) );
        budgetAdjustmentList.add( bean );
      }
    }

    return budgetAdjustmentList;
  }

  private BigDecimal getBudgetMediaValueForBudget( Budget budget )
  {
    Long budgetOwnerId = null;
    BudgetMaster budgetMaster = budget.getBudgetSegment().getBudgetMaster();
    if ( budgetMaster.isNodeBudget() )
    {
      budgetOwnerId = budget.getNode().getNodeOwner().getId();
    }
    else if ( budgetMaster.isParticipantBudget() )
    {
      budgetOwnerId = budget.getUser().getId();
    }
    return budgetOwnerId == null ? null : getUserService().getBudgetMediaValueForUser( budgetOwnerId );
  }

  private static Date getDefaultStartDate()
  {
    Date launchDate = getSystemVariableService().getPropertyByName( SystemVariableService.CLIENT_LAUNCH_DATE ).getDateVal();
    Calendar calendar = GregorianCalendar.getInstance();
    calendar.add( GregorianCalendar.MONTH, -1 );
    Date todayMinusMonth = DateUtils.toStartDate( calendar.getTime() );
    return todayMinusMonth.after( launchDate ) ? todayMinusMonth : launchDate;
  }

  private static Date getDefaultEndDate()
  {
    return DateUtils.toEndDate( DateUtils.getCurrentDate() );
  }

  private static CountryService getCountryService()
  {
    return (CountryService)getService( CountryService.BEAN_NAME );
  }

  private static BudgetMasterService getBudgetMasterService()
  {
    return (BudgetMasterService)getService( BudgetMasterService.BEAN_NAME );
  }

  private static SystemVariableService getSystemVariableService()
  {
    return (SystemVariableService)getService( SystemVariableService.BEAN_NAME );
  }

  private static UserService getUserService()
  {
    return (UserService)getService( UserService.BEAN_NAME );
  }

}
