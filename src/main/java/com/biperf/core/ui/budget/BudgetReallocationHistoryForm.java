
package com.biperf.core.ui.budget;

import java.text.ParseException;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import com.biperf.core.ui.BaseForm;
import com.biperf.core.utils.DateUtils;

public class BudgetReallocationHistoryForm extends BaseForm
{
  private static final long serialVersionUID = 1L;

  /**
  * The key to the HTTP request attribute that refers to this form.
  */
  public static final String FORM_NAME = "budgetReallocationHistoryForm";

  private String startDate;
  private String endDate;
  private String budgetMasterId;
  private String budgetSegmentId;
  private String ownerBudgetId;
  private Boolean isOrderByDateAscending = Boolean.FALSE;

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

  public void setOwnerBudgetId( String ownerBudgetId )
  {
    this.ownerBudgetId = ownerBudgetId;
  }

  public String getOwnerBudgetId()
  {
    return ownerBudgetId;
  }

  public void setIsOrderByDateAscending( Boolean isOrderByDateAscending )
  {
    this.isOrderByDateAscending = isOrderByDateAscending;
  }

  public Boolean getIsOrderByDateAscending()
  {
    return isOrderByDateAscending;
  }

  public ActionErrors validate( ActionMapping mapping, HttpServletRequest request )
  {
    ActionErrors errors = super.validate( mapping, request );
    if ( errors == null )
    {
      errors = new ActionErrors();
    }

    if ( this.startDate != null && this.endDate != null )
    {
      try
      {
        if ( DateUtils.toStartDate( this.startDate ).after( DateUtils.toStartDate( this.endDate ) ) )
        {
          errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "budget.reallocation.history.PAGE_ERROR" ) );
        }
      }
      catch( ParseException e )
      {
        e.printStackTrace();
      }
    }

    if ( !errors.isEmpty() )
    {
      request.setAttribute( "serverReturnedErrored", true );
    }
    return errors;
  }

  public String getBudgetSegmentId()
  {
    return budgetSegmentId;
  }

  public void setBudgetSegmentId( String budgetSegmentId )
  {
    this.budgetSegmentId = budgetSegmentId;
  }

  public String getBudgetMasterId()
  {
    return budgetMasterId;
  }

  public void setBudgetMasterId( String budgetMasterId )
  {
    this.budgetMasterId = budgetMasterId;
  }

}
