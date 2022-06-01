/*
 * (c) 2007 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javaui/com/biperf/core/ui/participant/ParticipantGoalQuestProgressAction.java,v $
 */

package com.biperf.core.ui.participant;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.biperf.core.domain.enums.GoalQuestPaxActivityStatus;
import com.biperf.core.domain.enums.GoalQuestPaxActivityType;
import com.biperf.core.domain.goalquest.GoalQuestParticipantActivity;
import com.biperf.core.service.goalquest.GoalQuestPaxActivityService;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.utils.ActionUtils;
import com.biperf.core.utils.ClientStateUtils;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.SelectGoalUtil;

/**
 * ParticipantGoalQuestProgressAction.
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
 * <td>viswanat</td>
 * <td>Feb 21, 2007</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ParticipantGoalQuestProgressAction extends BaseDispatchAction
{
  private static final Log logger = LogFactory.getLog( ParticipantGoalQuestProgressAction.class );

  /**
   * 
   * @param mapping the action mapping used to select this instance.
   * @param form the ActionForm bean for this request.
   * @param request the HTTP request we are processing.
   * @param response the HTTP response we are creating.
   * @return an <code>ActionForward</code> instance describing where and how control sho uld be
   *         forwarded, or null if the response has already been completed.
   */
  public ActionForward savePaxGoal( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    ParticipantGoalQuestProgressForm participantGoalQuestProgressForm = (ParticipantGoalQuestProgressForm)form;
    ActionMessages messages = new ActionMessages();
    try
    {
      getGoalQuestPaxActivityService().saveGoalQuestPaxActivity( buildGoalQuestPaxActivity( participantGoalQuestProgressForm ) );
    }
    catch( Exception e )
    {
      logger.error( "Error occurred while saving GoalQuest Pax Activity", e );
      messages.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_SYSTEM_EXCEPTION ) );
    }
    Map clientStateParameterMap = new HashMap();
    clientStateParameterMap.put( "promotionId", participantGoalQuestProgressForm.getPromotionId() );
    clientStateParameterMap.put( "userId", participantGoalQuestProgressForm.getUserId() );
    String queryString = ClientStateUtils.generateEncodedLink( "", "", clientStateParameterMap );
    if ( !messages.isEmpty() )
    {
      saveErrors( request, messages );
      return mapping.findForward( ActionConstants.FAIL_FORWARD );
    }
    participantGoalQuestProgressForm.clearValues();
    return ActionUtils.forwardWithParameters( mapping, ActionConstants.SUCCESS_FORWARD, new String[] { queryString } );
  }

  /**
   * Get the GoalQuestService from the beanLocator.
   * 
   * @return GoalQuestService
   */
  private GoalQuestPaxActivityService getGoalQuestPaxActivityService()
  {
    return (GoalQuestPaxActivityService)getService( GoalQuestPaxActivityService.BEAN_NAME );
  }

  private GoalQuestParticipantActivity buildGoalQuestPaxActivity( ParticipantGoalQuestProgressForm participantGoalQuestProgressForm )
  {
    GoalQuestParticipantActivity goalQuestParticipantActivity = new GoalQuestParticipantActivity();
    goalQuestParticipantActivity.setType( GoalQuestPaxActivityType.lookup( participantGoalQuestProgressForm.getAddReplaceType() ) );
    goalQuestParticipantActivity.setAutomotive( false );
    goalQuestParticipantActivity.setStatus( GoalQuestPaxActivityStatus.lookup( GoalQuestPaxActivityStatus.PENDING ) );
    goalQuestParticipantActivity.setGoalQuestPromotion( SelectGoalUtil.getPromotion( participantGoalQuestProgressForm.getPromotionId() ) );
    goalQuestParticipantActivity.setParticipant( SelectGoalUtil.getParticipant( participantGoalQuestProgressForm.getUserId() ) );
    goalQuestParticipantActivity.setQuantity( new BigDecimal( participantGoalQuestProgressForm.getNewQuantity() ) );
    goalQuestParticipantActivity.setSubmissionDate( DateUtils.getCurrentDate() );
    return goalQuestParticipantActivity;
  }
}
