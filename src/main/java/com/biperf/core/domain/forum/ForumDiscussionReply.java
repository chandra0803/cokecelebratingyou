/**
 * 
 */

package com.biperf.core.domain.forum;

/**
 * @author poddutur
 *
 */
public class ForumDiscussionReply extends ForumDiscussion
{
  /**
   * 
   */
  private static final long serialVersionUID = 7512762147930925126L;

  private Long parentDiscussionId;

  public ForumDiscussionReply()
  {
    // empty constructor
  }

  public Long getParentDiscussionId()
  {
    return parentDiscussionId;
  }

  public void setParentDiscussionId( Long parentDiscussionId )
  {
    this.parentDiscussionId = parentDiscussionId;
  }
}
