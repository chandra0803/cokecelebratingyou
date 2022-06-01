
package com.biperf.core.ui.participant;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.domain.enums.ThrowdownMatchProgressType;
import com.biperf.core.domain.promotion.MatchTeamProgress;
import com.biperf.core.domain.promotion.ThrowdownPromotion;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.service.throwdown.TeamService;
import com.biperf.core.service.throwdown.ThrowdownService;
import com.biperf.core.ui.BaseController;
import com.biperf.core.utils.ClientStateUtils;
import com.biperf.core.value.PromotionRoundValue;
import com.biperf.core.value.ThrowdownReviewProgress;

public class ParticipantThrowdownProgressController extends BaseController
{

  @Override
  public void onExecute( ComponentContext tileContext, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext ) throws Exception
  {
    ParticipantThrowdownProgressForm participantThrowdownProgressForm = (ParticipantThrowdownProgressForm)request.getAttribute( "participantThrowdownProgressForm" );
    Long promotionId = new Long( ClientStateUtils.getParameterValue( request, ClientStateUtils.getClientStateMap( request ), "promotionId" ) );
    Long userId = null;
    String userIdString = ClientStateUtils.getParameterValue( request, ClientStateUtils.getClientStateMap( request ), "userId" );
    if ( userIdString != null )
    {
      userId = new Long( userIdString );
      request.setAttribute( "userId", userId );
    }

    if ( promotionId != null && userId != null )
    {
      ThrowdownPromotion throwdownPromotion = (ThrowdownPromotion)getPromotionService().getPromotionById( promotionId );
      int precision = throwdownPromotion.getAchievementPrecision().getPrecision();
      int roundingMode = throwdownPromotion.getRoundingMethod().getBigDecimalRoundingMode();
      Set<Integer> roundNumbers = new HashSet<Integer>();
      Set<PromotionRoundValue> values = getThrowdownService().getRoundsForProgressLoad( throwdownPromotion.getId() );
      for ( PromotionRoundValue value : values )
      {
        roundNumbers.add( value.getRoundNumber() );
      }

      List<MatchTeamProgress> progressList = new ArrayList<MatchTeamProgress>();
      if ( participantThrowdownProgressForm != null && participantThrowdownProgressForm.getRoundNumber() > 0 )
      {
        progressList = getTeamService().getProgressListByUser( userId, throwdownPromotion.getId(), participantThrowdownProgressForm.getRoundNumber() );
      }

      List<ThrowdownReviewProgress> cumulativeProgressList = new ArrayList<ThrowdownReviewProgress>();
      BigDecimal cumulativeTotal = new BigDecimal( "0" );
      BigDecimal cumulativeTotalRounded = new BigDecimal( "0" );
      if ( progressList != null && progressList.size() > 0 )
      {
        for ( Iterator<MatchTeamProgress> iterator = progressList.iterator(); iterator.hasNext(); )
        {
          MatchTeamProgress matchTeamProgress = iterator.next();
          ThrowdownReviewProgress reviewProgress = new ThrowdownReviewProgress();
          reviewProgress.setSubmissionDate( matchTeamProgress.getAuditCreateInfo().getDateCreated() );
          reviewProgress.setProgress( matchTeamProgress.getProgress() );
          reviewProgress.setProgressType( matchTeamProgress.getProgressType().getName() );
          if ( matchTeamProgress.getProgress() != null )
          {
            if ( matchTeamProgress.getProgressType().equals( ThrowdownMatchProgressType.lookup( ThrowdownMatchProgressType.INCREMENTAL ) ) )
            {
              reviewProgress.setProgress( matchTeamProgress.getProgress() );
              cumulativeTotal = cumulativeTotal.add( matchTeamProgress.getProgress() );
            }
            else if ( matchTeamProgress.getProgressType().equals( ThrowdownMatchProgressType.lookup( ThrowdownMatchProgressType.REPLACE ) ) )
            {
              reviewProgress.setProgress( matchTeamProgress.getProgress().subtract( cumulativeTotal ) );
              cumulativeTotal = matchTeamProgress.getProgress();
            }
            cumulativeTotalRounded = cumulativeTotal.setScale( precision, roundingMode );
          }
          reviewProgress.setCumulativeTotal( cumulativeTotalRounded );
          cumulativeProgressList.add( reviewProgress );
        }
      }
      request.setAttribute( "cumulativeProgressList", cumulativeProgressList );
      request.setAttribute( "promotionId", promotionId );
      request.setAttribute( "throwdownPromotion", throwdownPromotion );
      request.setAttribute( "roundNumbers", roundNumbers );
    }
    request.setAttribute( "addReplaceList", ThrowdownMatchProgressType.getList() );
  }

  protected PromotionService getPromotionService()
  {
    return (PromotionService)getService( PromotionService.BEAN_NAME );
  }

  private TeamService getTeamService()
  {
    return (TeamService)getService( TeamService.BEAN_NAME );
  }

  private ThrowdownService getThrowdownService()
  {
    return (ThrowdownService)getService( ThrowdownService.BEAN_NAME );
  }

}
