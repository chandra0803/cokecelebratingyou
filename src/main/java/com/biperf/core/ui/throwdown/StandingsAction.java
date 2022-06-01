
package com.biperf.core.ui.throwdown;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.domain.enums.SortByType;
import com.biperf.core.domain.enums.SortOnType;
import com.biperf.core.domain.promotion.Team;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.utils.ClientStateUtils;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.StandingsUrlBean;
import com.biperf.core.value.ThrowdownStandingBean;

public class StandingsAction extends BaseThrowdownAction
{
  public ActionForward summary( ActionMapping actionMapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws IOException
  {
    ThrowdownStandingBean standingBean = new ThrowdownStandingBean();
    Long promotionId = buildPromotionId( request );
    if ( promotionId != null )
    {
      standingBean = getTeamService().getStandingsForPromotion( promotionId, ThrowdownStandingBean.PAGE_SIZE, 0, SortByType.ASC, SortOnType.WINS );
    }
    super.writeAsJsonToResponse( standingBean, response );
    return null;
  }

  public ActionForward detail( ActionMapping actionMapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws IOException
  {
    ThrowdownStandingBean standingBean = new ThrowdownStandingBean();
    Long promotionId = buildPromotionId( request );
    String matchFilterName = request.getParameter( "matchFilterName" ) != null ? request.getParameter( "matchFilterName" ) : "all";
    standingBean.setPromotion( getThrowdownService().getThrowdownPromotion( promotionId ) );
    standingBean.setRulesUrl( generateRulesUrl( promotionId, request ) );
    reorderEligiblePromotions( request, promotionId );
    request.setAttribute( "standingBean", standingBean );
    request.setAttribute( "promotionId", standingBean.getPromotion().getId() );
    request.setAttribute( "promotionName", standingBean.getPromotion().getName() );
    request.setAttribute( "promotionStartDate", standingBean.getPromotion().getSubmissionStartDate() );
    request.setAttribute( "promotionEndDate", standingBean.getPromotion().getSubmissionEndDate() );
    request.setAttribute( "promotionOverviewText", standingBean.getPromotion().getOverviewDetailsText() );
    request.setAttribute( "eligibleThrowdownPromotions", getEligibleThrowdownPromotions( request ) );
    request.setAttribute( "matchFilterName", matchFilterName );
    request.setAttribute( "rulesUrl", generateRulesUrl( standingBean.getPromotion().getId(), request ) );

    return actionMapping.findForward( ActionConstants.DETAILS_FORWARD );
  }

  public ActionForward detailInfo( ActionMapping actionMapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws IOException
  {
    ThrowdownStandingBean standingBean = new ThrowdownStandingBean();
    Long promotionId = buildPromotionId( request );
    String matchFilterName = request.getParameter( "matchFilterName" ) != null ? request.getParameter( "matchFilterName" ) : "all";
    Long userId = UserManager.getUserId();
    Team team = getTeamService().getTeamByUserIdForPromotion( userId, promotionId );
    Long teamId = team != null ? team.getId() : null;
    Integer pageNumber = request.getParameter( "currentPage" ) != null ? new Integer( request.getParameter( "currentPage" ) ) : 0;
    Integer sortedOn = request.getParameter( "sortedOn" ) != null ? new Integer( request.getParameter( "sortedOn" ) ) : 2;
    String sortedBy = request.getParameter( "sortedBy" ) != null ? request.getParameter( "sortedBy" ) : "desc";

    SortByType sortByType = SortByType.valueOf( sortedBy );
    SortOnType sortOnType = SortOnType.WINS;
    for ( SortOnType sortType : SortOnType.values() )
    {
      if ( sortType.getKey().equals( sortedOn.toString() ) )
      {
        sortOnType = sortType;
      }
    }

    if ( matchFilterName != null )
    {
      if ( matchFilterName.equals( "all" ) )
      {
        standingBean = getTeamService().getStandingsForPromotion( promotionId, ThrowdownStandingBean.PAGE_SIZE, pageNumber, sortByType, sortOnType );
      }
      else if ( matchFilterName.equals( "mine" ) )
      {
        standingBean = getTeamService().getMyStandings( promotionId, teamId, ThrowdownStandingBean.PAGE_SIZE, pageNumber, sortByType, sortOnType );
      }
    }
    Date progressEndDate = getTeamService().getLastFileLoadDateForPromotion( promotionId );
    if ( progressEndDate != null )
    {
      standingBean.setProgressLoaded( true );
    }
    else
    {
      standingBean.setProgressLoaded( false );
    }
    standingBean.setRulesUrl( generateRulesUrl( promotionId, request ) );
    standingBean.setSortedOn( sortedOn );
    standingBean.setSortedBy( sortedBy );
    request.setAttribute( "standingBean", standingBean );
    request.setAttribute( "promotionId", standingBean.getPromotion().getId() );
    request.setAttribute( "promotionName", standingBean.getPromotion().getName() );
    request.setAttribute( "promotionStartDate", standingBean.getPromotion().getSubmissionStartDate() );
    request.setAttribute( "promotionEndDate", standingBean.getPromotion().getSubmissionEndDate() );
    request.setAttribute( "promotionOverviewText", standingBean.getPromotion().getOverviewDetailsText() );
    request.setAttribute( "eligibleThrowdownPromotions", getEligibleThrowdownPromotions( request ) );
    request.setAttribute( "matchFilterName", matchFilterName );
    request.setAttribute( "rulesUrl", generateRulesUrl( standingBean.getPromotion().getId(), request ) );

    standingBean.setProgressEndDate( DateUtils.toDisplayString( progressEndDate ) );
    super.writeAsJsonToResponse( standingBean, response );
    return null;
  }

  public ActionForward link( ActionMapping actionMapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws IOException
  {
    StandingsUrlBean standingsUrlBean = new StandingsUrlBean();
    Map<String, Object> parameterMap = new HashMap<String, Object>();
    parameterMap.put( "promotionId", buildPromotionId( request ) );
    String standingsUrl = ClientStateUtils.generateEncodedLink( "", request.getServletContext().getContextPath() + "/throwdown/standingsDetail.do?method=detail", parameterMap );
    standingsUrlBean.setStandingsUrl( standingsUrl.concat( "#Standings" ) );
    super.writeAsJsonToResponse( standingsUrlBean, response );
    return null;
  }
}
