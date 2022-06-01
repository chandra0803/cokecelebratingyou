/**
 * 
 */

package com.biperf.core.ui.forum;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.domain.forum.CommenterInfo;
import com.biperf.core.domain.forum.ForumCommentsTileView;
import com.biperf.core.domain.forum.ForumCommentsTileView.CommentsView;
import com.biperf.core.domain.forum.ForumDiscussion;
import com.biperf.core.domain.forum.ForumDiscussionLike;
import com.biperf.core.domain.forum.ForumLikeCountView;
import com.biperf.core.domain.forum.ForumTileView;
import com.biperf.core.domain.forum.ForumTileView.LastRepliesView;
import com.biperf.core.domain.forum.ForumTileView.ThreadAuthorView;
import com.biperf.core.domain.forum.ForumTileView.ThreadView;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.user.User;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.forum.ForumDiscussionService;
import com.biperf.core.service.participant.ParticipantAssociationRequest;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.forum.ForumDiscussionDetailValueBean;

/**
 * @author poddutur
 *
 */
public class ForumDiscussionDetailMaintainAction extends BaseDispatchAction
{
  @SuppressWarnings( "unchecked" )
  public ActionForward displayDetailDiscussion( ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    Long topicId = null;
    String topicName = null;
    Long discussionId = null;
    String discussionName = null;
    HttpSession session = request.getSession();
    String submitReply = (String)session.getAttribute( "submitReply" );

    String clientState = RequestUtils.getOptionalParamString( request, "clientState" );
    if ( StringUtils.isNotBlank( clientState ) )
    {
      String cryptoPass = RequestUtils.getOptionalParamString( request, "cryptoPass" );
      String password = ClientStatePasswordManager.getPassword();

      if ( cryptoPass != null && cryptoPass.equals( "1" ) )
      {
        password = ClientStatePasswordManager.getGlobalPassword();
      }
      Map<String, Object> clientStateMap = ClientStateSerializer.deserialize( clientState, password );
      topicId = (Long)clientStateMap.get( "topicId" );
      topicName = (String)clientStateMap.get( "topicName" );
      discussionId = (Long)clientStateMap.get( "discussionId" );
      discussionName = (String)clientStateMap.get( "discussionTitle" );
    }
    else
    {
      topicId = (Long)session.getAttribute( "topicId" );
      topicName = (String)session.getAttribute( "topicName" );
      discussionId = (Long)session.getAttribute( "discussionId" );
      discussionName = (String)session.getAttribute( "discussionName" );
    }

    AssociationRequestCollection requestCollection = new AssociationRequestCollection();
    requestCollection.add( new ParticipantAssociationRequest( ParticipantAssociationRequest.EMAILS ) );
    if ( UserManager.getUser().isParticipant() )
    {
      Participant sessionUser = getParticipantService().getParticipantByIdWithAssociations( UserManager.getUserId(), requestCollection );

      request.setAttribute( "imageUrl", sessionUser.getAvatarSmallFullPath() );
    }

    if ( submitReply != null )
    {
      request.setAttribute( "submitReply", submitReply );
    }
    else
    {
      request.setAttribute( "submitReply", "false" );
    }
    session.removeAttribute( "submitReply" );
    request.setAttribute( "topicId", topicId );
    request.setAttribute( "topicName", topicName );
    request.setAttribute( "discussionId", discussionId );
    request.setAttribute( "discussionName", discussionName );
    return actionMapping.findForward( "display_detail_discussion" );
  }

  public ActionForward displayDetailDiscussionFromTile( ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    Long topicId;
    Long discussionId;
    String topicName;
    String discussionName;
    HttpSession session = request.getSession();
    String submitReply = (String)session.getAttribute( "submitReply" );

    discussionId = Long.parseLong( request.getParameter( "discussionId" ) );
    topicId = Long.parseLong( request.getParameter( "topicId" ) );
    topicName = request.getParameter( "topicName" );
    discussionName = request.getParameter( "discussionName" );

    AssociationRequestCollection requestCollection = new AssociationRequestCollection();
    requestCollection.add( new ParticipantAssociationRequest( ParticipantAssociationRequest.EMAILS ) );
    if ( UserManager.getUser().isParticipant() )
    {
      Participant sessionUser = getParticipantService().getParticipantByIdWithAssociations( UserManager.getUserId(), requestCollection );

      request.setAttribute( "imageUrl", sessionUser.getAvatarSmallFullPath() );
    }

    if ( submitReply != null )
    {
      request.setAttribute( "submitReply", submitReply );
    }
    else
    {
      request.setAttribute( "submitReply", "false" );
    }

    session.removeAttribute( "submitReply" );
    request.setAttribute( "topicId", topicId );
    request.setAttribute( "topicName", topicName );
    request.setAttribute( "discussionId", discussionId );
    request.setAttribute( "discussionName", discussionName );
    return actionMapping.findForward( "display_detail_discussion" );
  }

  public ActionForward fetchDiscussionDetailItems( ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    HttpSession session = request.getSession();
    Long topicId = null;
    String topicName = null;
    Long discussionId = null;
    String discussionName = null;

    int pageNumber = 1;
    int repliesPerPage = 15;
    int rowNumStart = ( pageNumber - 1 ) * repliesPerPage + 1;
    int rowNumEnd = pageNumber * repliesPerPage;

    String clientState = RequestUtils.getOptionalParamString( request, "clientState" );
    if ( StringUtils.isNotBlank( clientState ) )
    {
      String cryptoPass = RequestUtils.getOptionalParamString( request, "cryptoPass" );
      String password = ClientStatePasswordManager.getPassword();

      if ( cryptoPass != null && cryptoPass.equals( "1" ) )
      {
        password = ClientStatePasswordManager.getGlobalPassword();
      }
      @SuppressWarnings( "unchecked" )
      Map<String, Object> clientStateMap = ClientStateSerializer.deserialize( clientState, password );
      topicId = (Long)clientStateMap.get( "topicId" );
      topicName = (String)clientStateMap.get( "topicName" );
      discussionId = (Long)clientStateMap.get( "discussionId" );
      discussionName = (String)clientStateMap.get( "discussionName" );
    }
    else
    {
      topicId = (Long)session.getAttribute( "topicId" );
      topicName = (String)session.getAttribute( "topicName" );
      discussionId = (Long)session.getAttribute( "discussionId" );
      discussionName = (String)session.getAttribute( "discussionName" );
    }

    List<ForumDiscussionDetailValueBean> dicussionDetailItemsList = new ArrayList<ForumDiscussionDetailValueBean>();
    List<ForumDiscussionDetailValueBean> discussionRepliesList = new ArrayList<ForumDiscussionDetailValueBean>();
    @SuppressWarnings( "rawtypes" )
    List discussionLikedUsersList;
    @SuppressWarnings( "rawtypes" )
    List commentsLikedUsersList;

    dicussionDetailItemsList = getForumDiscussionService().getDiscussionDetailItems( discussionId, UserManager.getUser().getUserId() );

    discussionLikedUsersList = getForumDiscussionService().getDiscussionLikedUsersList( discussionId );

    discussionRepliesList = getForumDiscussionService().getDiscussionReplies( rowNumStart, rowNumEnd, discussionId, UserManager.getUser().getUserId() );

    ForumTileView forumTileView = new ForumTileView();
    ThreadView threadView = new ThreadView();
    ThreadAuthorView threadAuthorView = new ThreadAuthorView();

    List<LastRepliesView> lastRepliesViewList = new ArrayList<LastRepliesView>();

    for ( ForumDiscussionDetailValueBean item : discussionRepliesList )
    {
      LastRepliesView lastRepliesView = new LastRepliesView();
      CommenterInfo commenterInfo = new CommenterInfo();

      commenterInfo.setId( item.getCommenterId() );
      commenterInfo.setFirstName( item.getCommenterFirstName() );
      commenterInfo.setLastName( item.getCommenterLastName() );
      commenterInfo.setAvatarUrl( item.getCommenterAvatarUrl() );

      lastRepliesView.setId( item.getCommentId() );
      lastRepliesView.setLikedIds( item.getCommenterLikedIds() );
      lastRepliesView.setComment( item.getCommentBody() );
      lastRepliesView.setCommenterInfo( commenterInfo );
      lastRepliesView.setNumberOfLikes( item.getCommentNumOfLikers() );

      commentsLikedUsersList = getForumDiscussionService().getCommentsLikedUsersList( item.getCommentId() );
      if ( commentsLikedUsersList.contains( UserManager.getUserId() ) && item.getCommentNumOfLikers() >= 1 )
      {
        lastRepliesView.setIsLiked( true );
      }
      else
      {
        lastRepliesView.setIsLiked( item.getCommentIsLiked() );
      }
      lastRepliesView.setDate( item.getCommentedDate() );
      lastRepliesView.setTime( item.getCommentedTime() );
      lastRepliesViewList.add( lastRepliesView );
    }

    for ( ForumDiscussionDetailValueBean item : dicussionDetailItemsList )
    {
      threadAuthorView.setId( item.getUserId() );
      threadAuthorView.setFirstName( item.getFirstName() );
      threadAuthorView.setLastName( item.getLastName() );
      threadAuthorView.setAvatarUrl( item.getAvatarUrl() );

      threadView.setTopicId( item.getTopicId() );
      if ( item.getTopicName() != null )
      {
        threadView.setTopicName( item.getTopicName() );
      }
      else
      {
        threadView.setTopicName( item.getTopicNameDefault() );
      }
      threadView.setDiscussionName( item.getDiscussionName() );
      threadView.setDiscussionId( item.getDiscussionId() );
      threadView.setDiscussionBody( item.getDiscussionBody() );
      threadView.setNumberOfLikes( item.getNumberOfLikes() );
      threadView.setTotalNumberOfReplies( item.getNumberOfReplies() );

      if ( discussionLikedUsersList.contains( UserManager.getUserId() ) && item.getNumberOfLikes() >= 1 )
      {
        threadView.setIsLiked( true );
      }
      else
      {
        threadView.setIsLiked( item.getDiscussionIsLiked() );
      }
      threadView.setLikedIds( item.getCommenterLikedIds() );
      threadView.setRepliesPerPage( repliesPerPage );
      threadView.setPage( pageNumber );
      threadView.setDateCreated( item.getCreatedDate() );
      threadView.setTimeCreated( item.getCreatedTime() );
    }
    threadView.setLastRepliesView( lastRepliesViewList );
    threadView.setThreadAuthorView( threadAuthorView );
    forumTileView.setThreadView( threadView );

    session.setAttribute( "topicId", topicId );
    session.setAttribute( "topicName", topicName );
    session.setAttribute( "discussionId", discussionId );
    session.setAttribute( "discussionName", discussionName );

    super.writeAsJsonToResponse( forumTileView, response );

    return null;
  }

  public ActionForward fetchMoreDiscussionReplies( ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    HttpSession session = request.getSession();
    Long discussionId = (Long)session.getAttribute( "discussionId" );
    int pageNumber;
    if ( request.getParameter( "page" ) != null )
    {
      pageNumber = Integer.parseInt( request.getParameter( "page" ) );
    }
    else
    {
      pageNumber = 1;
    }
    int repliesPerPage = 15;
    int rowNumStart = ( pageNumber - 1 ) * repliesPerPage + 1;
    int rowNumEnd = pageNumber * repliesPerPage;
    int totalDiscussionRepliesCount;

    List<ForumDiscussionDetailValueBean> discussionRepliesList = new ArrayList<ForumDiscussionDetailValueBean>();
    @SuppressWarnings( "rawtypes" )
    List commentsLikedUsersList;

    discussionRepliesList = getForumDiscussionService().getDiscussionReplies( rowNumStart, rowNumEnd, discussionId, UserManager.getUser().getUserId() );

    totalDiscussionRepliesCount = getForumDiscussionService().getDiscussionRepliesCount( discussionId );

    ForumCommentsTileView forumCommentsTileView = new ForumCommentsTileView();

    List<CommentsView> commentsViewList = new ArrayList<CommentsView>();

    for ( ForumDiscussionDetailValueBean item : discussionRepliesList )
    {
      CommentsView commentsView = new CommentsView();
      CommenterInfo commenterInfo = new CommenterInfo();

      commenterInfo.setId( item.getCommenterId() );
      commenterInfo.setFirstName( item.getCommenterFirstName() );
      commenterInfo.setLastName( item.getCommenterLastName() );
      commenterInfo.setAvatarUrl( item.getCommenterAvatarUrl() );

      commentsView.setId( item.getCommentId() );
      commentsView.setComment( item.getCommentBody() );
      commentsView.setCommenterInfo( commenterInfo );
      commentsView.setNumberOfLikes( item.getCommentNumOfLikers() );

      commentsLikedUsersList = getForumDiscussionService().getCommentsLikedUsersList( item.getCommentId() );

      if ( commentsLikedUsersList.contains( UserManager.getUserId() ) && item.getCommentNumOfLikers() >= 1 )
      {
        commentsView.setIsLiked( true );
      }
      else
      {
        commentsView.setIsLiked( item.getCommentIsLiked() );
      }
      commentsView.setLikedIds( item.getCommenterLikedIds() );
      commentsView.setDatePosted( item.getCommentedDate() );
      commentsView.setTimePosted( item.getCommentedTime() );

      commentsViewList.add( commentsView );
    }
    forumCommentsTileView.setTotalNumberOfReplies( totalDiscussionRepliesCount );
    forumCommentsTileView.setRepliesPerPage( repliesPerPage );
    forumCommentsTileView.setPage( pageNumber );

    forumCommentsTileView.setCommentsView( commentsViewList );

    super.writeAsJsonToResponse( forumCommentsTileView, response );

    return null;
  }

  public ActionForward fetchDiscussionDetailItemsAfterSubmitReply( ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    HttpSession session = request.getSession();
    Long topicId = null;
    String topicName = null;
    Long discussionId = null;
    String discussionName = null;

    String clientState = RequestUtils.getOptionalParamString( request, "clientState" );
    if ( StringUtils.isNotBlank( clientState ) )
    {
      String cryptoPass = RequestUtils.getOptionalParamString( request, "cryptoPass" );
      String password = ClientStatePasswordManager.getPassword();

      if ( cryptoPass != null && cryptoPass.equals( "1" ) )
      {
        password = ClientStatePasswordManager.getGlobalPassword();
      }
      @SuppressWarnings( "unchecked" )
      Map<String, Object> clientStateMap = ClientStateSerializer.deserialize( clientState, password );
      topicId = (Long)clientStateMap.get( "topicId" );
      topicName = (String)clientStateMap.get( "topicName" );
      discussionId = (Long)clientStateMap.get( "discussionId" );
      discussionName = (String)clientStateMap.get( "discussionName" );
    }
    else
    {
      topicId = (Long)session.getAttribute( "topicId" );
      topicName = (String)session.getAttribute( "topicName" );
      discussionId = (Long)session.getAttribute( "discussionId" );
      discussionName = (String)session.getAttribute( "discussionName" );
    }

    double totalNumberOfRepliesCount = getForumDiscussionService().getDiscussionRepliesCount( discussionId );
    double repliesPerPage = 15;
    int pageNumber = (int)Math.ceil( totalNumberOfRepliesCount / repliesPerPage );
    int rowNumStart = (int) ( ( pageNumber - 1 ) * repliesPerPage + 1 );
    int rowNumEnd = (int) ( pageNumber * repliesPerPage );

    List<ForumDiscussionDetailValueBean> dicussionDetailItemsList = new ArrayList<ForumDiscussionDetailValueBean>();
    List<ForumDiscussionDetailValueBean> discussionRepliesList = new ArrayList<ForumDiscussionDetailValueBean>();
    @SuppressWarnings( "rawtypes" )
    List discussionLikedUsersList;
    @SuppressWarnings( "rawtypes" )
    List commentsLikedUsersList;

    dicussionDetailItemsList = getForumDiscussionService().getDiscussionDetailItems( discussionId, UserManager.getUser().getUserId() );

    discussionLikedUsersList = getForumDiscussionService().getDiscussionLikedUsersList( discussionId );

    discussionRepliesList = getForumDiscussionService().getDiscussionReplies( rowNumStart, rowNumEnd, discussionId, UserManager.getUser().getUserId() );

    ForumTileView forumTileView = new ForumTileView();
    ThreadView threadView = new ThreadView();
    ThreadAuthorView threadAuthorView = new ThreadAuthorView();

    List<LastRepliesView> lastRepliesViewList = new ArrayList<LastRepliesView>();

    for ( ForumDiscussionDetailValueBean item : discussionRepliesList )
    {
      LastRepliesView lastRepliesView = new LastRepliesView();
      CommenterInfo commenterInfo = new CommenterInfo();

      commenterInfo.setId( item.getCommenterId() );
      commenterInfo.setFirstName( item.getCommenterFirstName() );
      commenterInfo.setLastName( item.getCommenterLastName() );
      commenterInfo.setAvatarUrl( item.getCommenterAvatarUrl() );

      lastRepliesView.setId( item.getCommentId() );
      lastRepliesView.setLikedIds( item.getCommenterLikedIds() );
      lastRepliesView.setComment( item.getCommentBody() );
      lastRepliesView.setCommenterInfo( commenterInfo );
      lastRepliesView.setNumberOfLikes( item.getCommentNumOfLikers() );

      commentsLikedUsersList = getForumDiscussionService().getCommentsLikedUsersList( item.getCommentId() );
      if ( commentsLikedUsersList.contains( UserManager.getUserId() ) && item.getCommentNumOfLikers() >= 1 )
      {
        lastRepliesView.setIsLiked( true );
      }
      else
      {
        lastRepliesView.setIsLiked( item.getCommentIsLiked() );
      }
      lastRepliesView.setDate( item.getCommentedDate() );
      lastRepliesView.setTime( item.getCommentedTime() );
      lastRepliesViewList.add( lastRepliesView );
    }

    for ( ForumDiscussionDetailValueBean item : dicussionDetailItemsList )
    {
      threadAuthorView.setId( item.getUserId() );
      threadAuthorView.setFirstName( item.getFirstName() );
      threadAuthorView.setLastName( item.getLastName() );
      threadAuthorView.setAvatarUrl( item.getAvatarUrl() );

      threadView.setTopicId( item.getTopicId() );
      threadView.setTopicName( item.getTopicName() );
      threadView.setDiscussionName( item.getDiscussionName() );
      threadView.setDiscussionId( item.getDiscussionId() );
      threadView.setDiscussionBody( item.getDiscussionBody() );
      threadView.setNumberOfLikes( item.getNumberOfLikes() );
      threadView.setTotalNumberOfReplies( item.getNumberOfReplies() );

      if ( discussionLikedUsersList.contains( UserManager.getUserId() ) && item.getNumberOfLikes() >= 1 )
      {
        threadView.setIsLiked( true );
      }
      else
      {
        threadView.setIsLiked( item.getDiscussionIsLiked() );
      }
      threadView.setLikedIds( item.getCommenterLikedIds() );
      threadView.setRepliesPerPage( (int)repliesPerPage );
      threadView.setPage( pageNumber );
      threadView.setDateCreated( item.getCreatedDate() );
      threadView.setTimeCreated( item.getCreatedTime() );
      threadView.setShowLatest( true );
    }
    threadView.setLastRepliesView( lastRepliesViewList );
    threadView.setThreadAuthorView( threadAuthorView );
    forumTileView.setThreadView( threadView );

    session.setAttribute( "topicId", topicId );
    session.setAttribute( "topicName", topicName );
    session.setAttribute( "discussionId", discussionId );
    session.setAttribute( "discussionName", discussionName );

    super.writeAsJsonToResponse( forumTileView, response );

    return null;
  }

  public ActionForward fetchForumDiscussionSaveLike( ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    Long discussionId = Long.parseLong( request.getParameter( "discussionId" ) );
    Long userId = UserManager.getUserId();
    Long numberOfLikes;
    ForumDiscussion forumDiscussion = new ForumDiscussion();
    ForumDiscussionLike forumDiscussionLike = new ForumDiscussionLike();
    ForumLikeCountView forumLikeCountView = new ForumLikeCountView();
    User user = new User();
    user = getUserService().getUserById( userId );
    forumDiscussion.setId( discussionId );
    forumDiscussionLike.setUser( user );
    try
    {
      getForumDiscussionService().saveForumDiscussionLike( forumDiscussion, forumDiscussionLike );
      numberOfLikes = getForumDiscussionService().getDiscussionLikeCountForDiscussion( discussionId );
      forumLikeCountView.setNumberOfLikes( numberOfLikes );
    }
    catch( Exception e )
    {
      return actionMapping.findForward( ActionConstants.FAIL_FORWARD );
    }
    super.writeAsJsonToResponse( forumLikeCountView, response );
    return null;
  }

  public ActionForward fetchForumCommentSaveLike( ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    Long commentId = Long.parseLong( request.getParameter( "commentId" ) );
    Long discussionId = Long.parseLong( request.getParameter( "discussionId" ) );
    Long userId = UserManager.getUserId();
    Long numberOfLikes;
    ForumDiscussion forumDiscussion = new ForumDiscussion();
    ForumDiscussionLike forumDiscussionLike = new ForumDiscussionLike();
    ForumLikeCountView forumLikeCountView = new ForumLikeCountView();
    User user = new User();
    user = getUserService().getUserById( userId );
    forumDiscussion.setId( commentId );
    forumDiscussionLike.setUser( user );
    try
    {
      getForumDiscussionService().saveForumDiscussionCommentLike( forumDiscussion, forumDiscussionLike );
      numberOfLikes = getForumDiscussionService().getCommentLikeCountForDiscussion( commentId, discussionId );
      forumLikeCountView.setNumberOfLikes( numberOfLikes );
    }
    catch( Exception e )
    {
      return actionMapping.findForward( ActionConstants.FAIL_FORWARD );
    }
    super.writeAsJsonToResponse( forumLikeCountView, response );
    return null;
  }

  private ForumDiscussionService getForumDiscussionService()
  {
    return (ForumDiscussionService)getService( ForumDiscussionService.BEAN_NAME );
  }

  private UserService getUserService()
  {
    return (UserService)getService( UserService.BEAN_NAME );
  }

  private ParticipantService getParticipantService()
  {
    return (ParticipantService)getService( ParticipantService.BEAN_NAME );
  }

}
