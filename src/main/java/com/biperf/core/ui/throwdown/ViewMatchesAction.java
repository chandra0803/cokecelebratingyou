
package com.biperf.core.ui.throwdown;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.domain.promotion.Round;
import com.biperf.core.domain.promotion.ThrowdownPromotion;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.AdvancedListPageInfo;
import com.biperf.core.utils.ClientStateUtils;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.BaseJsonView;
import com.biperf.core.value.MatchListColumn;
import com.biperf.core.value.ThrowdownMatchBean;
import com.biperf.core.value.ThrowdownMatchListBean;
import com.biperf.core.value.ThrowdownMatchListMetaView;
import com.biperf.core.value.ThrowdownMatchListTabularData;
import com.objectpartners.cms.util.CmsResourceBundle;

public class ViewMatchesAction extends BaseThrowdownAction
{
  @SuppressWarnings( "unused" )
  private static final Log logger = LogFactory.getLog( ViewMatchesAction.class );

  public ActionForward summary( ActionMapping actionMapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws IOException
  {
    Long promotionId = buildPromotionId( request );
    boolean showSummary = getTeamService().showMatchSummary( promotionId, UserManager.getUserId() );
    if ( showSummary )
    {
      super.writeAsJsonToResponse( BaseJsonView.getHideView(), response );
      return null;
    }
    ThrowdownMatchListBean matchList = new ThrowdownMatchListBean();
    buildMatchListDetail( request, matchList );
    super.writeAsJsonToResponse( matchList, response );
    return null;
  }

  public ActionForward detail( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws IOException, ServletException
  {

    Long promotionId = buildPromotionId( request );
    String matchFilterName = request.getParameter( "matchFilterName" ) != null ? request.getParameter( "matchFilterName" ) : "all";
    ThrowdownPromotion promotion = getThrowdownService().getThrowdownPromotion( promotionId );
    Long longCurrentRound = buildPrimary( request, "currentRound" );
    Integer currentRound = longCurrentRound != null ? new Integer( longCurrentRound.toString() ) : null;
    reorderEligiblePromotions( request, promotionId );
    request.setAttribute( "rulesUrl", generateRulesUrl( promotion.getId(), request ) );
    request.setAttribute( "promotionId", promotion.getId() );
    request.setAttribute( "promotionName", promotion.getName() );
    request.setAttribute( "promotionStartDate", promotion.getSubmissionStartDate() );
    request.setAttribute( "promotionEndDate", promotion.getSubmissionEndDate() );
    request.setAttribute( "promotionOverviewText", promotion.getOverviewDetailsText() );
    request.setAttribute( "matchFilterName", matchFilterName );
    request.setAttribute( "eligibleThrowdownPromotions", getEligibleThrowdownPromotions( request ) );

    return mapping.findForward( ActionConstants.DETAILS_FORWARD );
  }

  public ActionForward detailInfo( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws IOException, ServletException
  {
    ThrowdownMatchListBean matchList = new ThrowdownMatchListBean();
    buildMatchListDetail( request, matchList );
    super.writeAsJsonToResponse( matchList, response );
    return null;
  }

  private void buildMatchListDetail( HttpServletRequest request, ThrowdownMatchListBean view )
  {
    Long promotionId = buildPromotionId( request );
    Long longCurrentRound = buildPrimary( request, "currentRound" );
    Long longTotalRounds = buildPrimary( request, "totalRounds" );
    Integer currentRound = longCurrentRound != null ? new Integer( longCurrentRound.toString() ) : null;
    Integer totalRounds = longTotalRounds != null ? new Integer( longTotalRounds.toString() ) : null;
    String matchFilterName = request.getParameter( "matchFilterName" ) != null ? request.getParameter( "matchFilterName" ) : "all";
    Integer pageNumber = request.getParameter( "currentPage" ) != null ? new Integer( request.getParameter( "currentPage" ) ) : 1;

    if ( currentRound != null )
    {
      String selectRound = request.getParameter( "selectRound" ) != null ? request.getParameter( "selectRound" ) : "";
      if ( selectRound.equals( "prev" ) )
      {
        if ( currentRound != 1 )
        {
          currentRound -= 1;
        }
      }
      if ( selectRound.equals( "next" ) )
      {
        if ( currentRound < totalRounds )
        {
          currentRound += 1;
        }
      }
    }

    ThrowdownPromotion promotion = getThrowdownService().getThrowdownPromotion( promotionId );
    Round round = currentRound != null ? getTeamService().getRound( promotionId, currentRound ) : getTeamService().getAppropriateRound( promotionId );

    AdvancedListPageInfo<ThrowdownMatchBean> pageInfo = RequestUtils.getListPageInfo( request, ThrowdownMatchBean.class );
    pageInfo.setPageNumber( pageNumber );
    pageInfo.setObjectsPerPage( ThrowdownMatchListBean.PAGE_SIZE );
    if ( matchFilterName != null )
    {
      if ( matchFilterName.equals( "mine" ) )
      {
        getTeamService().getMyMatch( promotionId, UserManager.getUserId(), currentRound, pageInfo );
      }
      else if ( matchFilterName.equals( "team" ) )
      {
        getTeamService().getMatchListForMyTeam( promotionId, currentRound, pageInfo );
      }
      else if ( matchFilterName.equals( "all" ) )
      {
        getTeamService().getMatchListForPromotion( promotionId, currentRound, pageInfo );
      }
    }
    Date progressEndDate = getTeamService().getLastFileLoadDateForPromotionAndRound( promotionId, currentRound );
    boolean isRoundCompleted = round.getEndDate().before( UserManager.getCurrentDateWithTimeZoneID() );
    if ( progressEndDate != null && !isRoundCompleted )
    {
      view.setProgressLoaded( true );
    }
    else
    {
      view.setProgressLoaded( false );
    }
    request.setAttribute( "eligibleThrowdownPromotions", getEligibleThrowdownPromotions( request ) );
    request.setAttribute( "promotionId", promotion.getId() );
    request.setAttribute( "promotionName", promotion.getName() );
    request.setAttribute( "promotionStartDate", promotion.getSubmissionStartDate() );
    request.setAttribute( "promotionEndDate", promotion.getSubmissionEndDate() );
    request.setAttribute( "promotionOverviewText", promotion.getOverviewDetailsText() );
    request.setAttribute( "matchFilterName", matchFilterName );
    request.setAttribute( "rulesUrl", generateRulesUrl( promotion.getId(), request ) );
    ThrowdownMatchListTabularData tabularData = new ThrowdownMatchListTabularData();
    ThrowdownMatchListMetaView meta = new ThrowdownMatchListMetaView();
    if ( !promotion.isDisplayTeamProgress() )
    {
      for ( Iterator<MatchListColumn> itr = meta.getColumns().iterator(); itr.hasNext(); )
      {
        MatchListColumn matchColumnList = itr.next();
        if ( CmsResourceBundle.getCmsBundle().getString( "participant.throwdownstats.SCORES" ).equals( matchColumnList.getName() ) )
        {
          matchColumnList.setName( "" );
        }
      }
    }
    meta.setColumns( meta.getColumns() );
    tabularData.setMeta( meta );
    view.setTabularData( tabularData );

    view.setPromotion( promotion );
    view.setRound( round );

    view.setCurrentPage( pageInfo.getPageNumber() );
    if ( matchFilterName != null && matchFilterName.equals( "mine" ) && pageInfo.getList().isEmpty() && pageInfo.getFullListSize() > 0 )
    {
      view.setTotalMatches( 1 );
      view.setDoIHaveMatch( false );
    }
    else
    {
      view.setTotalMatches( pageInfo.getFullListSize() );
    }
    view.setMatches( pageInfo.getList() );
    view.setAllMatchesUrl( getAllMatchesUrl( request, promotion.getId(), round.getRoundNumber() ) );
    view.setProgressEndDate( DateUtils.toDisplayString( progressEndDate ) );

  }

  private String getAllMatchesUrl( HttpServletRequest request, Long promotionId, Integer currentRound )
  {
    Map<String, Object> parameterMap = new HashMap<String, Object>();
    parameterMap.put( "promotionId", promotionId );
    parameterMap.put( "currentRound", new Long( currentRound ) );
    String viewMatchesUrl = ClientStateUtils.generateEncodedLink( "", request.getServletContext().getContextPath() + "/throwdown/viewMatchesDetail.do?method=detail", parameterMap );
    return viewMatchesUrl.concat( "#Matches" );
  }

}
