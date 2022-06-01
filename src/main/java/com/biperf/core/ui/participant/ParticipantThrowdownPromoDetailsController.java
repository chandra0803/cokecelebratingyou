
package com.biperf.core.ui.participant;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.domain.promotion.Division;
import com.biperf.core.domain.promotion.MatchTeamOutcome;
import com.biperf.core.domain.promotion.Round;
import com.biperf.core.domain.promotion.Team;
import com.biperf.core.domain.promotion.ThrowdownPromotion;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.service.throwdown.TeamService;
import com.biperf.core.service.throwdown.ThrowdownService;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.ui.BaseController;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.ClientStateUtils;
import com.biperf.core.utils.InvalidClientStateException;
import com.biperf.core.value.ThrowdownReviewProgress;

public class ParticipantThrowdownPromoDetailsController extends BaseController
{

  @Override
  @SuppressWarnings( "rawtypes" )
  public void onExecute( ComponentContext tileContext, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext ) throws Exception
  {
    ThrowdownPromotion throwdownPromotion = null;
    ParticipantThrowdownProgressForm participantThrowdownProgressForm = (ParticipantThrowdownProgressForm)request.getAttribute( "participantThrowdownProgressForm" );
    Long promotionId = new Long( ClientStateUtils.getParameterValue( request, ClientStateUtils.getClientStateMap( request ), "promotionId" ) );
    Long userId = null;
    String userIdString = ClientStateUtils.getParameterValue( request, ClientStateUtils.getClientStateMap( request ), "userId" );
    try
    {
      String clientState = RequestUtils.getRequiredParamString( request, "clientState" );
      String cryptoPass = RequestUtils.getOptionalParamString( request, "cryptoPass" );
      String password = ClientStatePasswordManager.getPassword();

      if ( cryptoPass != null && cryptoPass.equals( "1" ) )
      {
        password = ClientStatePasswordManager.getGlobalPassword();
      }
      // Deserialize the client state.
      Map clientStateMap = ClientStateSerializer.deserialize( clientState, password );
      if ( userIdString != null )
      {
        userId = new Long( userIdString );
      }
      try
      {
        promotionId = (Long)clientStateMap.get( "promotionId" );
      }
      catch( ClassCastException cce )
      {
        promotionId = new Long( (String)clientStateMap.get( "promotionId" ) );
      }
      if ( userId != null )
      {
        request.setAttribute( "userId", userId );
      }
      else
      {
        ActionMessages errors = new ActionMessages();
        errors.add( "errorMessage", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, "userId as part of clientState" ) );
      }
      if ( promotionId != null && userId != null )
      {
        Division div = getTeamService().getRandomDivisionForPromotion( promotionId );
        List<Round> rounds = getThrowdownService().getRoundsByDivision( div.getId() );
        Set<Integer> roundNumbers = new HashSet<Integer>();
        for ( Round round : rounds )
        {
          roundNumbers.add( round.getRoundNumber() );
        }

        throwdownPromotion = (ThrowdownPromotion)getPromotionService().getPromotionById( promotionId );
        if ( participantThrowdownProgressForm != null && participantThrowdownProgressForm.getRoundNumber() > 0 )
        {
          Team team = getTeamService().getTeamByUserIdForPromotion( userId, promotionId );
          MatchTeamOutcome matchTeamOutcome = null;
          matchTeamOutcome = getTeamService().getOutcomeForMatch( team.getId(), promotionId, participantThrowdownProgressForm.getRoundNumber() );
          ThrowdownReviewProgress reviewProgress = new ThrowdownReviewProgress();
          if ( matchTeamOutcome != null )
          {
            if ( matchTeamOutcome.getCurrentValue() != null )
            {
              reviewProgress.setCumulativeTotal( matchTeamOutcome.getCurrentValueWithPrecisionAndRounding() );
            }
            reviewProgress.setSubmissionDate( matchTeamOutcome.getAuditUpdateInfo().getDateModified() );
          }
          request.setAttribute( "progressDetails", reviewProgress );
        }
        request.setAttribute( "roundNumbers", roundNumbers );
        request.setAttribute( "promotionId", promotionId );
        request.setAttribute( "throwdownPromotion", throwdownPromotion );
      }

    }
    catch( InvalidClientStateException e )
    {
      throw new IllegalArgumentException( "request parameter clientState was missing" );
    }

  }

  /**
   * Does a Bean lookup for the PromotionService
   * 
   * @return PromotionService
   */
  protected PromotionService getPromotionService()
  {
    return (PromotionService)getService( PromotionService.BEAN_NAME );
  }

  protected TeamService getTeamService()
  {
    return (TeamService)getService( TeamService.BEAN_NAME );
  }

  private ThrowdownService getThrowdownService()
  {
    return (ThrowdownService)getService( ThrowdownService.BEAN_NAME );
  }
}
