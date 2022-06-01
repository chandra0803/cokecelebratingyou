/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javaui/com/biperf/core/ui/promotion/PromotionWizardViolationAction.java,v $
 */

package com.biperf.core.ui.promotion;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.biperf.core.ui.BaseAction;
import com.livinglogic.struts.workflow.WorkflowUtils;

/**
 * PromotionWizardViolationAction <p/> <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>crosenquest</td>
 * <td>Aug 10, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class PromotionWizardViolationAction extends BaseAction
{

  public ActionForward execute( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws IOException, ServletException
  {
    ActionMessages errors = new ActionMessages();

    if ( null != WorkflowUtils.getNextStateViolationAction( request ) )
    {
      errors.add( "violation", new ActionMessage( "error.wizard.workflow.violation" ) );
      saveErrors( request, errors );
    }

    return mapping.findForward( "success" );

  }

}
