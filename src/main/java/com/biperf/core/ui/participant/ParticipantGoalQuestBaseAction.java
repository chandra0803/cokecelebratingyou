
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

import com.biperf.core.domain.goalquest.PaxGoal;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.GoalQuestPromotion;
import com.biperf.core.service.goalquest.PaxGoalService;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.utils.SelectGoalUtil;
import com.biperf.util.StringUtils;

public class ParticipantGoalQuestBaseAction extends BaseDispatchAction
{

  private static final Log logger = LogFactory.getLog( ParticipantGoalQuestBaseAction.class );

  /**
   * 
   * @param mapping the action mapping used to select this instance.
   * @param form the ActionForm bean for this request.
   * @param request the HTTP request we are processing.
   * @param response the HTTP response we are creating.
   * @return an <code>ActionForward</code> instance describing where and how control sho uld be
   *         forwarded, or null if the response has already been completed.
   */
  public ActionForward saveBaseGoal( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    ParticipantGoalQuestBaseForm participantGoalQuestBaseForm = (ParticipantGoalQuestBaseForm)form;

    Map clientStateParameterMap = new HashMap();
    clientStateParameterMap.put( "promotionId", participantGoalQuestBaseForm.getPromotionBaseId() );
    clientStateParameterMap.put( "userId", participantGoalQuestBaseForm.getUserId() );
    if ( clientStateParameterMap != null )
    {
      request.setAttribute( "clientStateParameterMap", clientStateParameterMap );
    }

    ActionMessages messages = new ActionMessages();
    if ( StringUtils.isEmpty( participantGoalQuestBaseForm.getNewQuantity() ) )
    {
      messages.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "participant.goalquest.promo.detail.BASE_REQUIRED" ) );
    }
    else if ( !StringUtils.isEmpty( participantGoalQuestBaseForm.getNewQuantity() ) && Double.parseDouble( participantGoalQuestBaseForm.getNewQuantity() ) == 0 )
    {
      messages.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "participant.goalquest.promo.detail.BASE_INTEGER" ) );
    }
    else
    {
      try
      {
        PaxGoal paxGoal = buildPaxGoal( participantGoalQuestBaseForm );
        getPaxGoalService().savePaxGoal( paxGoal );
      }
      catch( Exception e )
      {
        logger.error( "Error occurred while saving GoalQuest Pax Activity", e );
        messages.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_SYSTEM_EXCEPTION ) );
      }
    }
    if ( !messages.isEmpty() )
    {
      saveErrors( request, messages );
      return mapping.findForward( ActionConstants.FAIL_FORWARD );
    }
    return mapping.findForward( ActionConstants.SUCCESS_FORWARD );
  }

  private PaxGoal buildPaxGoal( ParticipantGoalQuestBaseForm participantGoalQuestBaseForm )
  {
    Participant pax = SelectGoalUtil.getParticipant( participantGoalQuestBaseForm.getUserId() );
    GoalQuestPromotion promotion = (GoalQuestPromotion)SelectGoalUtil.getPromotion( participantGoalQuestBaseForm.getPromotionBaseId() );
    PaxGoal paxGoal = getPaxGoalService().getPaxGoalByPromotionIdAndUserId( promotion.getId(), pax.getId() );

    // Issue fixed for 4477 - to set the achievement precision to the base quantity.
    int precision = promotion.getAchievementPrecision().getPrecision();
    int roundingMode = promotion.getRoundingMethod().getBigDecimalRoundingMode();
    BigDecimal baseQty = new BigDecimal( Double.parseDouble( participantGoalQuestBaseForm.getNewQuantity() ) ).setScale( precision, roundingMode );

    // This could happen - they may not have a goal selected when base objective is loaded
    if ( paxGoal == null )
    {
      paxGoal = new PaxGoal();
      paxGoal.setGoalQuestPromotion( promotion );
      paxGoal.setParticipant( pax );
    }
    // This load will always overwrite just the base objective amount
    paxGoal.setBaseQuantity( baseQty );

    return paxGoal;
  }

  /**
   * Get the GoalQuestService from the beanLocator.
   * 
   * @return GoalQuestService
   */
  private PaxGoalService getPaxGoalService()
  {
    return (PaxGoalService)getService( PaxGoalService.BEAN_NAME );
  }

}
