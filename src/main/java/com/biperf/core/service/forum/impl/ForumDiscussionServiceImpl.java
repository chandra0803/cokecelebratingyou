/**
 * 
 */

package com.biperf.core.service.forum.impl;

import java.util.Iterator;
import java.util.List;

import com.biperf.core.dao.forum.ForumDiscussionDAO;
import com.biperf.core.dao.forum.ForumTopicDAO;
import com.biperf.core.domain.forum.ForumDiscussion;
import com.biperf.core.domain.forum.ForumDiscussionLike;
import com.biperf.core.domain.forum.ForumDiscussionReply;
import com.biperf.core.domain.forum.ForumTopic;
import com.biperf.core.domain.user.User;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.forum.ForumDiscussionService;
import com.biperf.core.value.forum.ForumDiscussionDetailValueBean;
import com.biperf.core.value.forum.ForumDiscussionValueBean;

/**
 * @author poddutur
 *
 */
public class ForumDiscussionServiceImpl implements ForumDiscussionService
{
  private ForumDiscussionDAO forumDiscussionDAO;

  private ForumTopicDAO forumTopicDAO;

  @Override
  public List getAll()
  {
    return forumDiscussionDAO.getAll();
  }

  @Override
  public ForumDiscussion save( ForumDiscussion forumDiscussion )
  {
    return forumDiscussionDAO.save( forumDiscussion );
  }

  @Override
  public ForumDiscussion save( User user, ForumTopic forumTopic, ForumDiscussion forumDiscussion )
  {
    return forumDiscussionDAO.save( user, forumTopic, forumDiscussion );
  }

  @Override
  public ForumDiscussionReply saveReply( User user, ForumTopic forumTopic, ForumDiscussionReply forumDiscussionReply )
  {
    return forumDiscussionDAO.saveReply( user, forumTopic, forumDiscussionReply );

  }

  @Override
  public ForumDiscussion getDiscussionById( Long discussionId )
  {
    return forumDiscussionDAO.getDiscussionById( discussionId );
  }

  @Override
  public void deleteDiscussions( List forumDiscussionIdList ) throws ServiceErrorException
  {
    Iterator idIter = forumDiscussionIdList.iterator();

    while ( idIter.hasNext() )
    {
      this.deleteDiscussion( (Long)idIter.next() );
    }
  }

  @Override
  public void deleteDiscussion( Long discussionId ) throws ServiceErrorException
  {
    ForumDiscussion discussionToDelete = forumDiscussionDAO.getDiscussionById( discussionId );
    List<ForumDiscussionReply> repliesToDeleteForDiscussion = forumDiscussionDAO.getRepliesByParentDiscussionId( discussionId );
    if ( discussionToDelete != null )
    {
      forumDiscussionDAO.deleteDiscussion( discussionToDelete, repliesToDeleteForDiscussion );
    }
  }

  @Override
  public void deleteDiscussionReply( Long commentId ) throws ServiceErrorException
  {
    ForumDiscussionReply repliesToDelete = forumDiscussionDAO.getDiscussionReplyById( commentId );

    if ( repliesToDelete != null )
    {
      forumDiscussionDAO.deleteDiscussionReply( repliesToDelete );
    }
  }

  @Override
  public List<ForumDiscussionValueBean> getSortedDiscussionList( Long topicId )
  {
    List<ForumDiscussionValueBean> discussionList = forumDiscussionDAO.getSortedDiscussionList( topicId );
    return discussionList;
  }

  @Override
  public List<ForumDiscussionDetailValueBean> getDiscussionDetailItems( Long discussionId, Long userId )
  {
    List<ForumDiscussionDetailValueBean> discussionList = forumDiscussionDAO.getDiscussionDetailItems( discussionId, userId );
    return discussionList;
  }

  @Override
  public List<ForumDiscussionDetailValueBean> getDiscussionReplies( int rowNumStart, int rowNumEnd, Long discussionId, Long userId )
  {
    List<ForumDiscussionDetailValueBean> discussionRepliesList = forumDiscussionDAO.getDiscussionReplies( rowNumStart, rowNumEnd, discussionId, userId );
    return discussionRepliesList;
  }

  @Override
  public int getDiscussionRepliesCount( Long discussionId )
  {
    return forumDiscussionDAO.getDiscussionRepliesCount( discussionId );
  }

  @Override
  public ForumDiscussionLike saveForumDiscussionLike( ForumDiscussion forumDiscussion, ForumDiscussionLike forumDiscussionLike )
  {
    return forumDiscussionDAO.saveForumDiscussionLike( forumDiscussion, forumDiscussionLike );
  }

  @Override
  public Long getDiscussionLikeCountForDiscussion( Long discussionId )
  {
    return forumDiscussionDAO.getDiscussionLikeCountForDiscussion( discussionId );
  }

  @Override
  public ForumDiscussionLike saveForumDiscussionCommentLike( ForumDiscussion forumDiscussion, ForumDiscussionLike forumDiscussionLike )
  {
    return forumDiscussionDAO.saveForumDiscussionCommentLike( forumDiscussion, forumDiscussionLike );
  }

  @Override
  public Long getCommentLikeCountForDiscussion( Long commentId, Long discussionId )
  {
    return forumDiscussionDAO.getCommentLikeCountForDiscussion( commentId, discussionId );
  }

  @Override
  public List getDiscussionLikedUsersList( Long discussionId )
  {
    return forumDiscussionDAO.getDiscussionLikedUsersList( discussionId );
  }

  @Override
  public List getCommentsLikedUsersList( Long commentId )
  {
    return forumDiscussionDAO.getCommentsLikedUsersList( commentId );
  }

  @Override
  public boolean isDiscussionNameExists( String discussionName, Long topicId )
  {
    int discussionCnt = this.forumDiscussionDAO.getDiscussionCountByName( discussionName, topicId );

    boolean isDiscussionNameExists = discussionCnt > 0 ? true : false;

    return isDiscussionNameExists;
  }

  public void setForumDiscussionDAO( ForumDiscussionDAO forumDiscussionDAO )
  {
    this.forumDiscussionDAO = forumDiscussionDAO;
  }

  public void setForumTopicDAO( ForumTopicDAO forumTopicDAO )
  {
    this.forumTopicDAO = forumTopicDAO;
  }

}
