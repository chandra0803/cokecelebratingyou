/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/goalquest/PendingGQAwardSummaryAction.java,v $
 */

package com.biperf.core.ui.goalquest;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.biperf.core.domain.goalquest.PendingGoalQuestAwardSummary;
import com.biperf.core.domain.promotion.GoalQuestPromotion;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.utils.ClientStateUtils;
import com.biperf.core.utils.DateUtils;

/**
 * PendingGQAwardSummaryAction.
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
 * <td>esakkimu</td>
 * <td>Apr 26, 2018</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 */
public class PendingGQAwardSummaryAction extends BaseGoalQuestAction
{
  public ActionForward runCalculation( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    String promotionId = ClientStateUtils.getParameterValue( request, ClientStateUtils.getClientStateMap( request ), "id" );
    PendingGoalQuestAwardSummary pendingGoalQuestAwardSummary = getGoalQuestService().getGoalQuestAwardSummaryByPromotionId( new Long( promotionId ) );

    // Get the promotion
    GoalQuestPromotion promotion = (GoalQuestPromotion)pendingGoalQuestAwardSummary.getPromotion();

    SimpleDateFormat sdf = new SimpleDateFormat( "MM/dd/yyyy" );
    String finalProcesDate = sdf.format( promotion.getFinalProcessDate() );

    // Final processing date plus two days
    Date plusFinalProcesDate = DateUtils.getDateAfterNumberOfDays( promotion.getFinalProcessDate(), 2 );

    if ( promotion.isAllowUnderArmour() && ! ( DateUtils.getCurrentDateTrimmed().after( plusFinalProcesDate ) ) )
    {
      ActionMessages errors = new ActionMessages();
      errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "promotion.goalquest.list.UA_RUN_CALCULATION_ERROR", finalProcesDate ) );
      saveErrors( request, errors );

      return mapping.findForward( ActionConstants.FAIL_FORWARD );
    }
    else
    {
      return mapping.findForward( ActionConstants.SUCCESS_FORWARD );
    }
  }

}
