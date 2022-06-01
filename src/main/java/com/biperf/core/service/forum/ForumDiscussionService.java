/**
 * 
 */

package com.biperf.core.service.forum;

import java.util.List;

import com.biperf.core.domain.forum.ForumDiscussion;
import com.biperf.core.domain.forum.ForumDiscussionLike;
import com.biperf.core.domain.forum.ForumDiscussionReply;
import com.biperf.core.domain.forum.ForumTopic;
import com.biperf.core.domain.user.User;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.SAO;
import com.biperf.core.value.forum.ForumDiscussionDetailValueBean;
import com.biperf.core.value.forum.ForumDiscussionValueBean;

/**
 * @author poddutur
 *
 */
public interface ForumDiscussionService extends SAO
{
  /**
     * BEAN_NAME is for applicationContext.xml reference
     */
  public static String BEAN_NAME = "forumDiscussionService";

  /**
   * Get all forum discussions from the database.
   * 
   * @return List
   */
  public List getAll();

  /**
   * Save or update the forum discussion to the database.
   * 
   * @param ForumDiscussion
   * @return ForumDiscussion
   */
  public ForumDiscussion save( ForumDiscussion forumDiscussion );

  /**
   * create a new discussion
   * 
   * @param ForumDiscussion, User, ForumTopic
   * @return ForumDiscussion
   */
  public ForumDiscussion save( User user, ForumTopic forumTopic, ForumDiscussion forumDiscussion );

  /**
   * Gets the forum discussion from the database by the discussionId param.
   * 
   * @param discussionId
   * @return ForumDiscussion
   */
  public ForumDiscussion getDiscussionById( Long discussionId );

  /**
   * Deletes a list of forum discussions.
   * 
   * @param forumDiscussionIdList - List of forumDiscussion.id
   */
  public void deleteDiscussions( List forumDiscussionIdList ) throws ServiceErrorException;

  /**
   * Delete a forum discussion.
   * 
   * @param discussionId
   * @throws ServiceErrorException
   */
  public void deleteDiscussion( Long discussionId ) throws ServiceErrorException;

  /**
   * gets the sorted list of discussion.
   * 
   * @param topicId
   * @return ForumDiscussion
   */
  public List<ForumDiscussionValueBean> getSortedDiscussionList( Long topicId );

  public List<ForumDiscussionDetailValueBean> getDiscussionDetailItems( Long discussionId, Long userId );

  public List<ForumDiscussionDetailValueBean> getDiscussionReplies( int rowNumStart, int rowNumEnd, Long discussionId, Long userId );

  public ForumDiscussionLike saveForumDiscussionLike( ForumDiscussion forumDiscussion, ForumDiscussionLike forumDiscussionLike );

  public Long getDiscussionLikeCountForDiscussion( Long discussionId );

  public ForumDiscussionLike saveForumDiscussionCommentLike( ForumDiscussion forumDiscussion, ForumDiscussionLike forumDiscussionLike );

  public Long getCommentLikeCountForDiscussion( Long commentId, Long discussionId );

  public List getDiscussionLikedUsersList( Long discussionId );

  public List getCommentsLikedUsersList( Long commentId );

  public int getDiscussionRepliesCount( Long discussionId );

  public boolean isDiscussionNameExists( String discussionName, Long topicId );

  public ForumDiscussionReply saveReply( User user, ForumTopic forumTopic, ForumDiscussionReply forumDiscussionReply );

  public void deleteDiscussionReply( Long commentId ) throws ServiceErrorException;

}
