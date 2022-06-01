/**
 * 
 */

package com.biperf.core.ui.forum;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.domain.forum.CommenterInfo;
import com.biperf.core.domain.forum.ForumTopicsTileView;
import com.biperf.core.domain.forum.ForumTopicsTileView.DiscussionsTileView;
import com.biperf.core.domain.forum.ForumTopicsTileView.DiscussionsTileView.AuthorTileView;
import com.biperf.core.domain.forum.ForumTopicsTileView.DiscussionsTileView.CommentsView;
import com.biperf.core.service.forum.ForumDiscussionService;
import com.biperf.core.service.forum.ForumTopicService;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.forum.ForumDiscussionDetailValueBean;

/**
 * @author poddutur
 *
 */
public class ForumTopicDisplayListAction extends BaseDispatchAction
{
  /**
   * Method to get the participant forum topics tile Json 
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward(response put to stream for the ajax call)
   */
  public ActionForward fetchForumTopicsForTile( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    List<ForumDiscussionDetailValueBean> forumTopicsTileList = new ArrayList<ForumDiscussionDetailValueBean>();

    Long userId = UserManager.getUser().getUserId();

    forumTopicsTileList = getForumTopicService().getForumTopicsForTile( userId );
    ForumTopicsTileView forumTopicsTileView = new ForumTopicsTileView();
    List<DiscussionsTileView> discussionsTileViewList = new ArrayList<DiscussionsTileView>();
    List commentsLikedUsersList;

    for ( ForumDiscussionDetailValueBean tileValue : forumTopicsTileList )
    {
      DiscussionsTileView discussionsTileView = new DiscussionsTileView();
      AuthorTileView authorTileView = new AuthorTileView();
      CommentsView commentsView = new CommentsView();
      CommenterInfo commenterInfo = new CommenterInfo();
      List<CommentsView> commentsViewList = new ArrayList<CommentsView>();

      commenterInfo.setId( tileValue.getCommenterId() );
      commenterInfo.setFirstName( tileValue.getCommenterFirstName() );
      commenterInfo.setLastName( tileValue.getCommenterLastName() );
      commenterInfo.setAvatarUrl( tileValue.getCommenterAvatarUrl() );

      commentsView.setId( tileValue.getCommentId() );
      commentsView.setLikedIds( tileValue.getCommenterLikedIds() );
      commentsView.setCommenterInfo( commenterInfo );
      commentsView.setNumberOfLikes( tileValue.getCommentNumOfLikers() );

      if ( tileValue.getCommentId() != null )
      {
        commentsLikedUsersList = getForumDiscussionService().getCommentsLikedUsersList( tileValue.getCommentId() );
        if ( commentsLikedUsersList.contains( UserManager.getUserId() ) && tileValue.getCommentNumOfLikers() >= 1 )
        {
          commentsView.setIsLiked( true );
        }
      }
      else
      {
        commentsView.setIsLiked( tileValue.getCommentIsLiked() );
      }
      commentsView.setComment( tileValue.getCommentBody() );
      commentsView.setDatePosted( tileValue.getCommentedDate() );
      commentsView.setTimePosted( tileValue.getCommentedTime() );

      commentsViewList.add( commentsView );

      authorTileView.setId( tileValue.getUserId() );
      authorTileView.setFirstName( tileValue.getFirstName() );
      authorTileView.setLastName( tileValue.getLastName() );
      authorTileView.setAvatarUrl( tileValue.getAvatarUrl() );

      discussionsTileView.setCommentsViewList( commentsViewList );
      discussionsTileView.setAuthorTileView( authorTileView );
      discussionsTileView.setTopicId( tileValue.getTopicId() );
      discussionsTileView.setTopicName( tileValue.getTopicName() );
      discussionsTileView.setDiscussionName( tileValue.getDiscussionName() );
      discussionsTileView.setDiscussionId( tileValue.getDiscussionId() );
      discussionsTileView.setDiscussionBody( tileValue.getDiscussionBody() );
      discussionsTileView.setNumberOfLikes( tileValue.getNumberOfLikes() );
      discussionsTileView.setNumberOfReplies( tileValue.getNumberOfReplies() );
      discussionsTileView.setIsLiked( tileValue.getDiscussionIsLiked() );
      discussionsTileView.setDateCreated( tileValue.getCreatedDate() );
      discussionsTileView.setTimeCreated( tileValue.getCreatedTime() );
      discussionsTileView.setMultipleComments( tileValue.getNumberOfReplies() > 1 );

      discussionsTileViewList.add( discussionsTileView );
    }

    forumTopicsTileView.setDiscussionsTileView( discussionsTileViewList );
    super.writeAsJsonToResponse( forumTopicsTileView, response );
    return null;
  }

  /**
   * Get the ForumTopicService from the beanLocator.
   * 
   * @return ForumTopicService
   */
  private ForumTopicService getForumTopicService()
  {
    return (ForumTopicService)getService( ForumTopicService.BEAN_NAME );
  }

  private ForumDiscussionService getForumDiscussionService()
  {
    return (ForumDiscussionService)getService( ForumDiscussionService.BEAN_NAME );
  }

}
