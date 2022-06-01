
package com.biperf.core.ui.throwdown;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.utils.ClientStateUtils;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.BaseJsonView;
import com.biperf.core.value.SmackTalkCommentViewBean;
import com.biperf.core.value.SmackTalkParticipantView;
import com.biperf.core.value.SmackTalkView;
import com.biperf.core.value.ThrowdownMatchBean;

public class MatchSummaryAction extends BaseThrowdownAction
{

  public static final int SUMMARY_BADGE_SIZE = 3;
  public static final int DETAIL_BADGE_SIZE = 5;

  public ActionForward summary( ActionMapping actionMapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws IOException
  {
    BaseJsonView matchBean = buildMatchBeanSummary( request );
    reorderEligiblePromotions( request, null );
    super.writeAsJsonToResponse( matchBean, response );
    return null;
  }

  public ActionForward detail( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws IOException, ServletException
  {
    ArrayList<SmackTalkView> smackTalks = new ArrayList<SmackTalkView>();
    request.setAttribute( "eligibleThrowdownPromotions", getEligibleThrowdownPromotions( request ) );
    ThrowdownMatchBean matchBean = buildMatchBeanDetail( request );
    reorderEligiblePromotions( request, matchBean.getPromotion().getId() );
    if ( matchBean.getPromotion().isSmackTalkAvailable() )
    {
      matchBean = getTeamService().getSmackTalkDetailsForMatch( matchBean );
      List<SmackTalkCommentViewBean> smackTalkPosts = matchBean.getSmackTalkPosts();

      if ( smackTalkPosts != null && !smackTalkPosts.isEmpty() )
      {
        for ( SmackTalkCommentViewBean smackTalkPost : smackTalkPosts )
        {
          SmackTalkView smackView = new SmackTalkView();
          smackView.setDetail( true );
          smackView.setPromotionName( matchBean.getPromotion().getName() );
          smackView.setPromotionType( PromotionType.THROWDOWN );
          smackView.setCommenterMain( smackTalkPost.getCommenter() );
          smackView.setComments( smackTalkPost.getCommentsPerPost() );
          smackView.setComment( smackTalkPost.getComment() );
          smackView.setId( smackTalkPost.getId() );
          smackView.setLiked( smackTalkPost.getLiked() );
          smackView.setNumLikers( smackTalkPost.getNumLikers() );
          smackView.setMatchId( matchBean.getMatchId() );
          smackView.setIsMyMatch( matchBean.isMine() );
          smackView.setTime( smackTalkPost.getRelativePostCreatedDate() );
          smackView.setHidden( smackTalkPost.getHidden() );
          if ( smackTalkPost.getCommenter().getId().equals( UserManager.getUserId() ) )
          {
            smackView.setMine( true );
          }
          if ( !matchBean.getPrimaryTeam().getTeam().isShadowPlayer() )
          {
            smackView.setPlayer1( new SmackTalkParticipantView( matchBean.getPrimaryTeam().getTeam().getParticipant().getId(),
                                                                matchBean.getPrimaryTeam().getTeam().getParticipant().getFirstName(),
                                                                matchBean.getPrimaryTeam().getTeam().getParticipant().getLastName(),
                                                                matchBean.getPrimaryTeam().getTeam().getParticipant().getAvatarSmallFullPath(),
                                                                null,
                                                                null,
                                                                null ) );
          }
          else
          {
            smackView.setPlayer1( new SmackTalkParticipantView( null, matchBean.getPromotion().getTeamUnavailableResolverType().getName(), "", null, null, null, null ) );

          }
          if ( !matchBean.getSecondaryTeam().getTeam().isShadowPlayer() )
          {
            smackView.setPlayer2( new SmackTalkParticipantView( matchBean.getSecondaryTeam().getTeam().getParticipant().getId(),
                                                                matchBean.getSecondaryTeam().getTeam().getParticipant().getFirstName(),
                                                                matchBean.getSecondaryTeam().getTeam().getParticipant().getLastName(),
                                                                matchBean.getSecondaryTeam().getTeam().getParticipant().getAvatarSmallFullPath(),
                                                                null,
                                                                null,
                                                                null ) );
          }
          else
          {
            smackView.setPlayer2( new SmackTalkParticipantView( null, matchBean.getPromotion().getTeamUnavailableResolverType().getName(), "", null, null, null, null ) );
          }
          smackTalks.add( smackView );
        }
      }

    }
    request.setAttribute( "matchDetailsJson", toJson( matchBean ) );
    request.setAttribute( "smackTalkJson", toJson( smackTalks ) );
    request.setAttribute( "matchBean", matchBean );
    request.setAttribute( "matchDetail", "Y" );
    return mapping.findForward( ActionConstants.DETAILS_FORWARD );
  }

  public ActionForward detailAjax( ActionMapping actionMapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws IOException
  {
    ThrowdownMatchBean matchBean = buildMatchBeanDetail( request );
    List<ThrowdownMatchBean> matchBeanViewList = new ArrayList<ThrowdownMatchBean>();
    matchBeanViewList.add( matchBean );
    super.writeAsJsonToResponse( matchBeanViewList, response );
    return null;
  }

  private ThrowdownMatchBean buildMatchBeanDetail( HttpServletRequest request )
  {
    ThrowdownMatchBean matchBean = new ThrowdownMatchBean();
    String value = ClientStateUtils.getParameterValue( request, ClientStateUtils.getClientStateMap( request ), "matchId" );

    if ( value == null || value.equals( "" ) )
    {
      value = ClientStateUtils.getParameterValue( request, ClientStateUtils.getClientStateMap( request ), "smackTalkId" );
    }
    Long matchId = !StringUtils.isEmpty( value ) ? new Long( value ) : null;
    if ( matchId != null )
    {
      matchBean = getTeamService().getMatchDetails( matchId, DETAIL_BADGE_SIZE );
    }
    else
    {
      matchBean = getTeamService().getAppropriateMatchDetails( buildPromotionId( request ), DETAIL_BADGE_SIZE );
    }
    Map<String, Object> parameterMap = new HashMap<String, Object>();
    parameterMap.put( "matchId", matchBean.getMatchId() );
    parameterMap.put( "roundNumber", new Long( matchBean.getRound().getRoundNumber() ) );
    parameterMap.put( "promotionId", matchBean.getPromotion().getId() );
    String viewMatchesUrl = ClientStateUtils.generateEncodedLink( "", request.getServletContext().getContextPath() + "/throwdown/viewMatchesDetail.do?method=detail", parameterMap );
    matchBean.setMatchUrl( viewMatchesUrl.concat( "#Matches" ) );
    matchBean.setRulesUrl( generateRulesUrl( matchBean.getPromotion().getId(), request ) );
    Date progressEndDate = null;
    if ( matchBean != null )
    {
      progressEndDate = getTeamService().getLastFileLoadDateForPromotionAndRound( matchBean.getPromotion().getId(), matchBean.getRound().getRoundNumber() );
    }
    if ( progressEndDate != null && !matchBean.isRoundCompleted() )
    {
      matchBean.setProgressLoaded( true );
    }
    else
    {
      matchBean.setProgressLoaded( false );
    }
    matchBean.setAsOfDate( DateUtils.toDisplayString( progressEndDate ) );
    return matchBean;
  }

  private BaseJsonView buildMatchBeanSummary( HttpServletRequest request )
  {
    ThrowdownMatchBean matchBean = new ThrowdownMatchBean();
    Long matchId = request.getParameter( "matchId" ) != null ? new Long( request.getParameter( "matchId" ) ) : null;
    if ( matchId != null )
    {
      matchBean = getTeamService().getMatchSummary( matchId, SUMMARY_BADGE_SIZE );
    }
    else
    {
      Long promotionId = buildPromotionId( request );
      if ( promotionId != null )
      {
        boolean showSummary = getTeamService().showMatchSummary( promotionId, UserManager.getUserId() );
        if ( !showSummary )
        {
          return BaseJsonView.getHideView();
        }
        else
        {
          matchBean = getTeamService().getAppropriateMatchSummary( promotionId, SUMMARY_BADGE_SIZE );
          Map<String, Object> parameterMap = new HashMap<String, Object>();
          parameterMap.put( "matchId", matchBean.getMatchId() );
          matchBean.setMatchUrl( ClientStateUtils.generateEncodedLink( "", request.getServletContext().getContextPath() + "/throwdown/matchDetail.do?method=detail", parameterMap ) );
          Date progressEndDate = null;
          if ( matchBean != null )
          {
            progressEndDate = getTeamService().getLastFileLoadDateForPromotionAndRound( matchBean.getPromotion().getId(), matchBean.getRound().getRoundNumber() );
          }
          if ( progressEndDate != null && !matchBean.isRoundCompleted() )
          {
            matchBean.setProgressLoaded( true );
          }
          else
          {
            matchBean.setProgressLoaded( false );
          }
          matchBean.setAsOfDate( DateUtils.toDisplayString( progressEndDate ) );
        }
      }
    }
    return matchBean;
  }

}
