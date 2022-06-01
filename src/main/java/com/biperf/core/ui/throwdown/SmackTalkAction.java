
package com.biperf.core.ui.throwdown;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.domain.WebErrorMessage;
import com.biperf.core.domain.enums.SmackTalkTabType;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.Division;
import com.biperf.core.domain.promotion.Match;
import com.biperf.core.domain.promotion.Round;
import com.biperf.core.domain.promotion.SmackTalkComment;
import com.biperf.core.domain.promotion.SmackTalkLike;
import com.biperf.core.domain.promotion.SmackTalkSet;
import com.biperf.core.domain.promotion.ThrowdownPromotion;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.service.throwdown.SmackTalkService;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.ClientStateUtils;
import com.biperf.core.utils.StringUtil;
import com.biperf.core.utils.UserManager;
import com.biperf.core.utils.WebResponseConstants;
import com.biperf.core.value.PromotionMenuBean;
import com.biperf.core.value.SmackTalkCommentView;
import com.biperf.core.value.SmackTalkCommentViewBean;
import com.biperf.core.value.SmackTalkLikeView;
import com.biperf.core.value.SmackTalkMainView;
import com.biperf.core.value.SmackTalkParticipantView;
import com.biperf.core.value.SmackTalkView;
import com.biperf.core.value.ThrowdownMatchBean;
import com.objectpartners.cms.util.ContentReaderManager;

public class SmackTalkAction extends BaseThrowdownAction
{
  private static final String key = "USER_FRIENDLY_SYSTEM_ERROR_MESSAGE";
  private static final String asset = "system.generalerror";
  public static final Integer PAGE_SIZE = 10;

  public ActionForward summary( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws IOException, ServletException
  {
    Long promotionId = buildPromotionId( request );
    String tabType = request.getParameter( "smackTalkSetNameId" );
    String participantId = request.getParameter( "participantId" );
    SmackTalkMainView view = new SmackTalkMainView();

    // make sure this promotion has smack talk, otherwise bypass
    if ( !getThrowdownPromotion( promotionId, request ).isSmackTalkAvailable() )
    {
      view.setVisible( false );
      super.writeAsJsonToResponse( view, response );
      return null;
    }

    Long userId = UserManager.getUserId();
    Round round = getTeamService().getAppropriateRound( promotionId );
    Division division = getTeamService().getDivisionForUser( promotionId, userId, round.getRoundNumber() );
    Long divisionId = division != null ? division.getId() : getTeamService().getRandomDivisionForPromotion( promotionId ).getId();
    round = getTeamService().getRound( promotionId, divisionId, round.getRoundNumber() );
    Long roundId = round != null ? round.getId() : null;
    Integer roundNumber = round != null ? round.getRoundNumber() : null;

    if ( StringUtil.isEmpty( participantId ) )
    {
      if ( tabType == null )
      {
        tabType = getSystemVariableService().getPropertyByName( SystemVariableService.SMACK_TALK_DEFAULT_TAB_NAME ).getStringVal();
        if ( tabType == null )
        {
          tabType = SmackTalkTabType.lookup( SmackTalkTabType.GLOBAL_TAB ).getCode();
        }
      }
      if ( tabType.equals( SmackTalkTabType.lookup( SmackTalkTabType.GLOBAL_TAB ).getCode() ) )
      {
        view = getTeamService().getSmackTalkDetailsForPromotion( promotionId, divisionId, roundId, roundNumber, null, PAGE_SIZE, getPageNumber( request ) );
      }
      else if ( tabType.equals( SmackTalkTabType.lookup( SmackTalkTabType.TEAM_TAB ).getCode() ) )
      {
        view = getTeamService().getSmackTalkDetailsForTeam( promotionId, divisionId, roundId, roundNumber, null, PAGE_SIZE, getPageNumber( request ) );
      }
      else
      {
        view = getTeamService().getSmackTalkDetailsForMyMatchAndMyPosts( promotionId, divisionId, roundId, roundNumber, null, PAGE_SIZE, getPageNumber( request ) );
      }
    }
    else
    {
      view = getTeamService().getSmackTalkDetailsForMyMatchAndMyPosts( promotionId, divisionId, roundId, roundNumber, Long.parseLong( participantId ), PAGE_SIZE, getPageNumber( request ) );

      // taking mine tab content in to local variable beans
      List<SmackTalkCommentViewBean> beans = null;
      for ( SmackTalkSet set : view.smackTalkSets )
      {
        if ( set.getNameId().equals( SmackTalkTabType.ME_TAB ) )
        {
          beans = new ArrayList<SmackTalkCommentViewBean>();
          beans = set.getCommentBeans();
        }
      }

      // moving local variable beans content i.e (mine tab content into global tab) and making mine
      // tab content to empty
      for ( SmackTalkSet set : view.smackTalkSets )
      {
        for ( SmackTalkCommentViewBean commentBean : set.getCommentBeans() )
        {
          if ( userId.equals( commentBean.getMatchBean().getPrimaryTeam().getTeam().getParticipant().getId() )
              || userId.equals( commentBean.getMatchBean().getSecondaryTeam().getTeam().getParticipant().getId() ) )
          {
            commentBean.getMatchBean().setMine( true );
          }
          else
          {
            commentBean.getMatchBean().setMine( false );
          }
        }
        if ( set.getNameId().equals( SmackTalkTabType.GLOBAL_TAB ) )
        {
          set.setCommentBeans( beans );
        }
        if ( set.getNameId().equals( SmackTalkTabType.ME_TAB ) )
        {
          set.setCommentBeans( new ArrayList<SmackTalkCommentViewBean>() );
        }
      }
    }
    view.setSmackTalkUrl( buildSmackTalkUrl( request, promotionId ) );
    super.writeAsJsonToResponse( view, response );
    return null;
  }

  public ActionForward postComment( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws IOException, ServletException
  {
    Long userId = UserManager.getUserId();
    // Get the form
    SmackTalkCommentForm commentForm = (SmackTalkCommentForm)form;

    String comment = commentForm.getComment();
    String matchId = commentForm.getMatchId();
    String smackTalkId = commentForm.getSmackTalkId();

    if ( matchId == null || matchId.equals( "" ) )
    {
      matchId = request.getParameter( "matchId" );
    }
    if ( smackTalkId == null || smackTalkId.equals( "" ) )
    {
      smackTalkId = request.getParameter( "smackTalkId" );
    }
    if ( comment == null || comment.equals( "" ) )
    {
      comment = request.getParameter( "comment" );
    }
    if ( comment == null || comment.equals( "" ) )
    {
      comment = request.getParameter( "smackTalkComment" );
    }

    // json view object
    SmackTalkCommentView view = new SmackTalkCommentView();

    Match match = getTeamService().getMatchById( Long.valueOf( matchId ) );
    Participant participant = getParticipantService().getParticipantById( UserManager.getUserId() );

    SmackTalkComment commentToSave = new SmackTalkComment();
    commentToSave.setComments( comment );
    commentToSave.setMatch( match );
    commentToSave.setUser( participant );

    if ( smackTalkId != null && !smackTalkId.equals( "" ) )
    {

      commentToSave.setParent( getSmackTalkService().getSmackTalkComment( Long.valueOf( smackTalkId ) ) );

      // Save domain objects and write response

      SmackTalkComment savedComment = getSmackTalkService().saveSmackTalkComment( commentToSave );

      try
      {
        // if success build success view object
        SmackTalkCommentViewBean commentBean = new SmackTalkCommentViewBean();
        commentBean.setId( savedComment.getId() );
        commentBean.setMatchId( Long.valueOf( matchId ) );
        commentBean.setComment( comment );
        if ( userId.equals( savedComment.getUser().getId() ) )
        {
          commentBean.setMine( true );
        }
        SmackTalkParticipantView commenter = new SmackTalkParticipantView( participant );
        commentBean.setCommenter( commenter );
        view.setComment( commentBean );
      }
      catch( Exception e )
      {
        // if failure build failure view object
        WebErrorMessage message = new WebErrorMessage();
        message.setSuccess( false );
        message.setType( WebResponseConstants.RESPONSE_TYPE_SERVER_ERROR );
        message.setText( ContentReaderManager.getText( asset, key ) );
        view.getMessages().add( message );
      }
    }
    super.writeAsJsonToResponse( view, response );
    return null;
  }

  public ActionForward saveSmackTalkPost( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws IOException, ServletException
  {
    Long userId = UserManager.getUserId();
    // Get the form
    SmackTalkCommentForm commentForm = (SmackTalkCommentForm)form;

    String comment = commentForm.getComment();
    String matchId = commentForm.getMatchId();

    if ( matchId == null )
    {
      matchId = request.getParameter( "matchId" );
    }
    if ( comment == null )
    {
      comment = request.getParameter( "comment" );
    }
    if ( comment == null )
    {
      comment = request.getParameter( "smackTalkComment" );
    }
    SmackTalkView postView = new SmackTalkView();
    Match match = getTeamService().getMatchById( Long.valueOf( matchId ) );
    ThrowdownMatchBean matchBean = getTeamService().getMatchDetails( Long.valueOf( matchId ) );
    Participant participant = getParticipantService().getParticipantById( UserManager.getUserId() );

    SmackTalkComment commentToSave = new SmackTalkComment();
    commentToSave.setComments( comment );
    commentToSave.setMatch( match );
    commentToSave.setUser( participant );

    commentToSave.setParent( null );

    // Save domain objects and write response
    try
    {
      SmackTalkComment savedComment = getSmackTalkService().saveSmackTalkComment( commentToSave );

      // if success build success view object

      postView.setId( savedComment.getId() );
      postView.setDetail( true );

      if ( !matchBean.getPrimaryTeam().getTeam().isShadowPlayer() && matchBean.getPrimaryTeam().getTeam().getParticipant().getId().equals( userId )
          || !matchBean.getSecondaryTeam().getTeam().isShadowPlayer() && matchBean.getSecondaryTeam().getTeam().getParticipant().getId().equals( userId ) )
      {
        postView.setIsMyMatch( true );
      }
      else
      {
        postView.setIsMyMatch( false );
      }

      postView.setCommenterMain( new SmackTalkParticipantView( savedComment.getUser().getId(),
                                                               savedComment.getUser().getFirstName(),
                                                               savedComment.getUser().getLastName(),
                                                               savedComment.getUser().getAvatarSmallFullPath(),
                                                               savedComment.getUser().getPositionType(),
                                                               null,
                                                               null ) );
      postView.setMine( true );
      postView.setComment( savedComment.getComments() );
      postView.setNumLikers( savedComment.getNumLikers() );
      postView.setLiked( savedComment.isLiked() );
      postView.setHidden( savedComment.getIsHidden() );
      postView.setTime( savedComment.getRelativePostCreatedDate() );
      postView.setMatchId( matchBean.getMatchId() );

    }
    catch( Exception e )
    {
      // if failure build failure view object
      WebErrorMessage message = new WebErrorMessage();
      message.setSuccess( false );
      message.setType( WebResponseConstants.RESPONSE_TYPE_SERVER_ERROR );
      message.setText( ContentReaderManager.getText( asset, key ) );
      postView.getMessages().add( message );
    }
    super.writeAsJsonToResponse( postView, response );
    return null;
  }

  public ActionForward like( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws IOException, ServletException
  {
    // view object
    SmackTalkLikeView view = new SmackTalkLikeView();
    SmackTalkComment smacktalkcomment;
    String commentId = request.getParameter( "commentId" );
    String smackTalkId = request.getParameter( "smackTalkId" );

    // to avoid null pointer if bi-admin likes any match
    if ( UserManager.getUser().isParticipant() )
    {
      try
      {
        Participant user = getParticipantService().getParticipantById( UserManager.getUserId() );
        if ( commentId == null || commentId.equals( "" ) )
        {
          smacktalkcomment = getSmackTalkService().getSmackTalkComment( Long.valueOf( smackTalkId ) );
        }
        else
        {
          smacktalkcomment = getSmackTalkService().getSmackTalkComment( Long.valueOf( commentId ) );
        }
        SmackTalkLike smackTalkLike = new SmackTalkLike();
        smackTalkLike.setSmackTalkComment( smacktalkcomment );
        smackTalkLike.setUser( user );
        getSmackTalkService().saveSmackTalkLike( smackTalkLike );
        view.setNumberOfLikers( getSmackTalkService().getLikeCountBySmackTalk( Long.valueOf( smacktalkcomment.getId() ) ) );
      }
      catch( Exception e )
      {
        // if failure build failure view object
        WebErrorMessage message = new WebErrorMessage();
        message.setSuccess( false );
        message.setType( WebResponseConstants.RESPONSE_TYPE_SERVER_ERROR );
        message.setText( ContentReaderManager.getText( asset, key ) );
        view.getMessages().add( message );
      }
    }
    super.writeAsJsonToResponse( view, response );
    return null;
  }

  @SuppressWarnings( "rawtypes" )
  public ActionForward detail( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws IOException, ServletException
  {
    try
    {
      String clientState = RequestUtils.getRequiredParamString( request, "clientState" );
      String cryptoPass = RequestUtils.getOptionalParamString( request, "cryptoPass" );
      String password = ClientStatePasswordManager.getPassword();
      if ( cryptoPass != null && cryptoPass.equals( "1" ) )
      {
        password = ClientStatePasswordManager.getGlobalPassword();
      }
      Map clientStateMap = ClientStateSerializer.deserialize( clientState, password );
      Long promotionId = (Long)clientStateMap.get( "promotionId" );
      request.setAttribute( "promotionId", promotionId );

    }
    catch( Exception e )
    {
      e.printStackTrace();
      return mapping.findForward( ActionConstants.DETAILS_FORWARD );
    }
    return mapping.findForward( ActionConstants.DETAILS_FORWARD );
  }

  /**
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return
   * @throws IOException
   * @throws ServletException
   * 
   *When user clicks on hide button this method will invoke.
   */
  public ActionForward hide( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws IOException, ServletException
  {

    String smackTalkId = request.getParameter( "smackTalkId" );
    String commentId = request.getParameter( "commentId" );

    if ( commentId != null && !commentId.equals( "" ) )
    {
      getSmackTalkService().hideSmackTalkComment( Long.valueOf( commentId ) );

    }
    else if ( smackTalkId != null && !smackTalkId.equals( "" ) )
    {
      getSmackTalkService().hideSmackTalkComment( Long.valueOf( smackTalkId ) );

    }

    super.writeAsJsonToResponse( new SmackTalkHideValueBean(), response );
    return null;
  }

  /**
   * 
   * @param request
   * @return pageNumber
   */
  private int getPageNumber( HttpServletRequest request )
  {
    String pageNumber = request.getParameter( "pageNumber" );
    if ( !StringUtil.isEmpty( pageNumber ) )
    {
      return Integer.parseInt( pageNumber );
    }
    return 1;
  }

  private ThrowdownPromotion getThrowdownPromotion( Long promotionId, HttpServletRequest request )
  {
    for ( PromotionMenuBean promoBean : getEligibleThrowdownPromotions( request ) )
    {
      if ( promoBean.getPromotion().getId().equals( promotionId ) )
      {
        return (ThrowdownPromotion)promoBean.getPromotion();
      }
    }
    return null;
  }

  private ParticipantService getParticipantService()
  {
    return (ParticipantService)getService( ParticipantService.BEAN_NAME );
  }

  private SmackTalkService getSmackTalkService()
  {
    return (SmackTalkService)getService( SmackTalkService.BEAN_NAME );
  }

  private SystemVariableService getSystemVariableService()
  {
    return (SystemVariableService)getService( SystemVariableService.BEAN_NAME );
  }

  private String buildSmackTalkUrl( HttpServletRequest request, Long promotionId )
  {
    Map<String, Object> parameterMap = new HashMap<String, Object>();
    parameterMap.put( "promotionId", promotionId );
    return ClientStateUtils.generateEncodedLink( "", request.getServletContext().getContextPath() + "/throwdown/smackTalkDetail.do?method=detail", parameterMap );
  }
}
