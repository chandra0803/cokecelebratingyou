/**
 * 
 */

package com.biperf.core.dao.forum;

import java.util.List;

import com.biperf.core.dao.DAO;
import com.biperf.core.domain.forum.ForumDiscussion;
import com.biperf.core.domain.forum.ForumDiscussionLike;
import com.biperf.core.domain.forum.ForumDiscussionReply;
import com.biperf.core.domain.forum.ForumTopic;
import com.biperf.core.domain.user.User;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.value.forum.ForumDiscussionDetailValueBean;
import com.biperf.core.value.forum.ForumDiscussionValueBean;

/**
 * @author poddutur
 *
 */
public interface ForumDiscussionDAO extends DAO
{
  /** ApplicationContext beanName */
  public static final String BEAN_NAME = "forumDiscussionDAO";

  /**
     * Get all forum discussions from the database.
     * 
     * @return List
     */
  List getAll();

  /**
     * create a forum discussion.
     * 
     * @param ForumDiscussion, User, ForumTopic
     * @return ForumDiscussion
     */
  ForumDiscussion save( User user, ForumTopic forumtopic, ForumDiscussion forumDiscussion );

  /**
     * Gets the forum discussion from the database by the discussionId param.
     * 
     * @param discussionId
     * @return ForumDiscussion
     */
  ForumDiscussion getDiscussionById( Long discussionId );

  /**
     * Delete a forum discussion.
     * 
     * @param discussionToDelete
   * @param repliesToDeleteForDiscussion 
     * @throws ServiceErrorException
     */
  void deleteDiscussion( ForumDiscussion discussionToDelete, List<ForumDiscussionReply> repliesToDeleteForDiscussion );

  /**
    * gets the sorted list of discussion.
    * 
    * @param topicId
    * @return ForumDiscussion
    */
  List<ForumDiscussionValueBean> getSortedDiscussionList( Long topicId );

  List<ForumDiscussionDetailValueBean> getDiscussionDetailItems( Long discussionId, Long userId );

  ForumDiscussion save( ForumDiscussion forumDiscussion );

  List<ForumDiscussionDetailValueBean> getDiscussionReplies( int rowNumStart, int rowNumEnd, Long discussionId, Long userId );

  ForumDiscussionLike saveForumDiscussionLike( ForumDiscussion forumDiscussion, ForumDiscussionLike forumDiscussionLike );

  Long getDiscussionLikeCountForDiscussion( Long discussionId );

  ForumDiscussionLike saveForumDiscussionCommentLike( ForumDiscussion forumDiscussion, ForumDiscussionLike forumDiscussionLike );

  Long getCommentLikeCountForDiscussion( Long commentId, Long discussionId );

  List getDiscussionLikedUsersList( Long discussionId );

  List getCommentsLikedUsersList( Long commentId );

  int getDiscussionRepliesCount( Long discussionId );

  int getDiscussionCountByName( String discussionName, Long topicId );

  ForumDiscussionReply saveReply( User user, ForumTopic forumTopic, ForumDiscussionReply forumDiscussionReply );

  ForumDiscussionReply getDiscussionReplyById( Long commentId );

  void deleteDiscussionReply( ForumDiscussionReply repliesToDelete );

  List<ForumDiscussionReply> getRepliesByParentDiscussionId( Long discussionId );

}
