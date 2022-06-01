
package com.biperf.core.ui.participant;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.biperf.core.domain.enums.MatchStatusType;
import com.biperf.core.domain.enums.ThrowdownMatchProgressType;
import com.biperf.core.domain.promotion.MatchTeamOutcome;
import com.biperf.core.domain.promotion.MatchTeamProgress;
import com.biperf.core.domain.promotion.Team;
import com.biperf.core.service.throwdown.TeamService;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.utils.ActionUtils;
import com.biperf.core.utils.ClientStateUtils;

public class ParticipantThrowdownProgressAction extends BaseDispatchAction
{
  public ActionForward getProgress( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    ParticipantThrowdownProgressForm participantThrowdownProgressForm = (ParticipantThrowdownProgressForm)form;
    Map<String, Object> clientStateParameterMap = new HashMap<String, Object>();
    clientStateParameterMap.put( "promotionId", participantThrowdownProgressForm.getPromotionId() );
    clientStateParameterMap.put( "userId", participantThrowdownProgressForm.getUserId() );

    String queryString = ClientStateUtils.generateEncodedLink( "", "", clientStateParameterMap );
    return ActionUtils.forwardWithParameters( mapping, ActionConstants.SUCCESS_FORWARD, new String[] { queryString } );
  }

  /**
   * 
   * @param mapping the action mapping used to select this instance.
   * @param form the ActionForm bean for this request.
   * @param request the HTTP request we are processing.
   * @param response the HTTP response we are creating.
   * @return an <code>ActionForward</code> instance describing where and how control sho uld be
   *         forwarded, or null if the response has already been completed.
   */
  public ActionForward savePaxProgress( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    ParticipantThrowdownProgressForm participantThrowdownProgressForm = (ParticipantThrowdownProgressForm)form;

    ActionMessages errors = new ActionMessages();
    Map<String, Object> clientStateParameterMap = new HashMap<String, Object>();
    clientStateParameterMap.put( "promotionId", participantThrowdownProgressForm.getPromotionId() );
    clientStateParameterMap.put( "userId", participantThrowdownProgressForm.getUserId() );

    Team team = getTeamService().getTeamByUserIdForPromotion( participantThrowdownProgressForm.getUserId(), participantThrowdownProgressForm.getPromotionId() );
    MatchTeamOutcome teamOutcome = getTeamService().getOutcomeForMatch( team.getId(), participantThrowdownProgressForm.getPromotionId(), participantThrowdownProgressForm.getRoundNumber() );
    if ( teamOutcome == null )
    {
      errors.add( ActionMessages.GLOBAL_MESSAGE, new ActionMessage( "participant.throwdown.promo.detail.NO_MATCH_FOUND" ) );
      saveErrors( request, errors );
      return mapping.findForward( ActionConstants.FAIL_FORWARD );
    }
    else if ( teamOutcome.getMatch().getStatus().equals( MatchStatusType.lookup( MatchStatusType.PLAYED ) ) )
    {
      errors.add( ActionMessages.GLOBAL_MESSAGE, new ActionMessage( "participant.throwdown.promo.detail.THROWDOWN_MATCH_PAYOUT_COMPLETE" ) );
      saveErrors( request, errors );
      return mapping.findForward( ActionConstants.FAIL_FORWARD );
    }
    getTeamService().saveProgress( buildProgress( teamOutcome, participantThrowdownProgressForm ) );

    String queryString = ClientStateUtils.generateEncodedLink( "", "", clientStateParameterMap );
    if ( !errors.isEmpty() )
    {
      saveErrors( request, errors );
      return mapping.findForward( ActionConstants.FAIL_FORWARD );
    }
    participantThrowdownProgressForm.clearValues();
    return ActionUtils.forwardWithParameters( mapping, ActionConstants.SUCCESS_FORWARD, new String[] { queryString } );
  }

  private MatchTeamProgress buildProgress( MatchTeamOutcome teamOutcome, ParticipantThrowdownProgressForm participantThrowdownProgressForm )
  {

    MatchTeamProgress matchTeamProgress = new MatchTeamProgress();
    matchTeamProgress.setProgressType( ThrowdownMatchProgressType.lookup( participantThrowdownProgressForm.getAddReplaceType() ) );
    matchTeamProgress.setProgress( new BigDecimal( participantThrowdownProgressForm.getNewQuantity() ) );
    matchTeamProgress.setTeamOutcome( teamOutcome );
    return matchTeamProgress;
  }

  private TeamService getTeamService()
  {
    return (TeamService)getService( TeamService.BEAN_NAME );
  }
}
