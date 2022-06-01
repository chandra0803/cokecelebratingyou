
package com.biperf.core.ui.throwdown;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.domain.hierarchy.NodeType;
import com.biperf.core.domain.promotion.StackStanding;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.utils.ClientStateUtils;
import com.biperf.core.utils.PageConstants;
import com.biperf.core.value.PromotionMenuBean;
import com.biperf.core.value.ThrowdownStackRanking;
import com.biperf.core.value.ThrowdownStackRankingView;

public class RankingsAction extends BaseThrowdownAction
{
  public ActionForward fetchRankings( ActionMapping actionMapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws IOException
  {
    ThrowdownStackRankingView rankingView = new ThrowdownStackRankingView();
    Long promotionId = buildPromotionId( request );
    if ( promotionId != null )
    {
      rankingView = getTeamService().getRankingSummary( promotionId, ThrowdownStackRanking.TILE_PAGE_SIZE, 0 );
    }
    generateRankingUrl( promotionId, rankingView, request );
    rankingView.setRulesUrl( generateRulesUrl( promotionId, request ) );
    super.writeAsJsonToResponse( rankingView, response );
    return null;
  }

  public ActionForward detail( ActionMapping actionMapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    Long promotionId = buildPromotionId( request );
    Long nodeTypeId = buildPrimary( request, "nodeTypeId" );
    Long nodeId = buildPrimary( request, "nodeId" );

    List<PromotionMenuBean> eligibleThrowDownPromotions = getEligibleThrowdownPromotions( request );
    if ( eligibleThrowDownPromotions != null && eligibleThrowDownPromotions.size() > 0 )
    {
      for ( Iterator<PromotionMenuBean> iter = eligibleThrowDownPromotions.iterator(); iter.hasNext(); )
      {
        PromotionMenuBean promoBean = (PromotionMenuBean)iter.next();
        StackStanding stackStanding = getTeamService().getRankingForPromotion( promoBean.getPromotion().getId() );
        if ( stackStanding == null )
        {
          iter.remove();
        }
      }
    }

    request.setAttribute( "eligibleThrowdownPromotions", eligibleThrowDownPromotions );
    List<NodeType> nodeTypes = getTeamService().getNodeTypesForRanking( promotionId );
    request.setAttribute( "nodeTypes", nodeTypes );
    if ( nodeTypeId == null && nodeId == null )
    {
      // promotion filter changed
      nodeTypeId = -1L;
      nodeId = -1L;
    }
    request.setAttribute( "nodeTypeId", nodeTypeId );
    request.setAttribute( "nodeId", nodeId );
    request.setAttribute( "promotionId", promotionId );
    request.setAttribute( "promotionName", getThrowdownService().getThrowdownPromotion( promotionId ).getName() );
    reorderEligiblePromotions( request, promotionId );
    request.setAttribute( "rulesUrl", generateRulesUrl( promotionId, request ) );
    return actionMapping.findForward( ActionConstants.DETAILS_FORWARD );
  }

  public ActionForward detailPage( ActionMapping actionMapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws IOException
  {
    ThrowdownStackRankingView rankingView = new ThrowdownStackRankingView();
    Long promotionId = buildPromotionId( request );
    Long nodeTypeId = buildPrimary( request, "nodeTypeId" );
    Long nodeId = buildPrimary( request, "nodeId" );
    Integer pageNumber = request.getParameter( "page" ) != null ? new Integer( request.getParameter( "page" ) ) : 0;

    if ( promotionId != null )
    {
      if ( nodeTypeId != null )
      {
        rankingView = getTeamService().getRankingDetails( promotionId, nodeTypeId, nodeId, ThrowdownStackRanking.DETAIL_PAGE_SIZE, pageNumber );
      }
      else
      {
        rankingView = getTeamService().getRankingDetails( promotionId, ThrowdownStackRanking.DETAIL_PAGE_SIZE, pageNumber );
      }
    }
    rankingView.setRulesUrl( generateRulesUrl( promotionId, request ) );
    super.writeAsJsonToResponse( rankingView, response );
    return null;
  }

  private void generateRankingUrl( Long promotionId, ThrowdownStackRankingView rankingView, HttpServletRequest request )
  {
    Map<String, Object> parameterMap = new HashMap<String, Object>();
    parameterMap.put( "promotionId", promotionId );
    parameterMap.put( "nodeTypeId", rankingView.getNodeTypeId() );
    parameterMap.put( "nodeId", rankingView.getNodeId() );
    rankingView.setRankingsUrl( ClientStateUtils.generateEncodedLink( "", request.getServletContext().getContextPath() + PageConstants.THROWDOWN_RANKING_DETAIL, parameterMap ) );
  }

  protected String generateRulesUrl( Long promotionId, HttpServletRequest request )
  {
    Map<String, Object> parameterMap = new HashMap<String, Object>();
    parameterMap.put( "promotionId", promotionId );
    String rulesUrl = ClientStateUtils.generateEncodedLink( "", request.getServletContext().getContextPath() + "/throwdown/viewRules.do?method=detail", parameterMap );
    return rulesUrl;
  }

}
