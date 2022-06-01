/*
 * (c) 2015 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/budget/Attic/InactiveBudgetsForm.java,v $
 */

package com.biperf.core.ui.budget;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import com.biperf.core.ui.BaseForm;
import com.objectpartners.cms.util.CmsResourceBundle;

/**
 * 
 * @author poddutur
 * @since Oct 28, 2015
 */
public class InactiveBudgetsForm extends BaseForm
{
  private static final long serialVersionUID = -4776760585657222707L;
  public static final String FORM_NAME = "inactiveBudgetsForm";
  private Long budgetMasterId;
  private Long budgetSegmentId;
  private String method;

  public ActionErrors validate( ActionMapping actionMapping, HttpServletRequest request )
  {
    ActionErrors actionErrors = super.validate( actionMapping, request );

    if ( actionErrors == null )
    {
      actionErrors = new ActionErrors();
    }
    if ( this.method != null && this.method.equals( "extract" ) )
    {
      if ( this.budgetMasterId != null && this.budgetMasterId != 0 )
      {
        if ( this.budgetSegmentId == null || this.budgetSegmentId == 0 )
        {
          actionErrors.add( ActionErrors.GLOBAL_MESSAGE,
                            new ActionMessage( "system.errors.REQUIRED", CmsResourceBundle.getCmsBundle().getString( "admin.fileload.budgetdistribution", "BUDGET_TIME_PERIOD" ) ) );
        }
      }
      else
      {
        actionErrors.add( ActionErrors.GLOBAL_MESSAGE,
                          new ActionMessage( "system.errors.REQUIRED", CmsResourceBundle.getCmsBundle().getString( "admin.fileload.budgetdistribution", "BUDGET_MASTER_ID" ) ) );
      }
    }
    return actionErrors;
  }

  public Long getBudgetMasterId()
  {
    return budgetMasterId;
  }

  public void setBudgetMasterId( Long budgetMasterId )
  {
    this.budgetMasterId = budgetMasterId;
  }

  public Long getBudgetSegmentId()
  {
    return budgetSegmentId;
  }

  public void setBudgetSegmentId( Long budgetSegmentId )
  {
    this.budgetSegmentId = budgetSegmentId;
  }

  public String getMethod()
  {
    return method;
  }

  public void setMethod( String method )
  {
    this.method = method;
  }

}
