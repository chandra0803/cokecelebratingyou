/**
 * 
 */

package com.biperf.core.value.forum;

import java.util.Date;

import com.biperf.core.domain.forum.ForumTopic;
import com.objectpartners.cms.util.CmsResourceBundle;

/**
 * @author poddutur
 *
 */
public class ForumDiscussionValueBean
{
  private Long discussionId;
  private Long topicId;
  private String topicCmAssetCode;
  private String discussionTitle;
  private String discussionBody;
  private Long parentDiscussionId;
  private String status;
  private Date dateCreated;
  private Date dateReplied;
  private String createdBy;
  private Long replies;
  private String repliedBy;
  private String createdDate;
  private String repliedDate;
  private Long createdUserId;
  private Long repliedUserId;

  public Long getDiscussionId()
  {
    return discussionId;
  }

  public void setDiscussionId( Long discussionId )
  {
    this.discussionId = discussionId;
  }

  public String getDiscussionTitle()
  {
    return discussionTitle;
  }

  public void setDiscussionTitle( String discussionTitle )
  {
    this.discussionTitle = discussionTitle;
  }

  public String getDiscussionBody()
  {
    return discussionBody;
  }

  public void setDiscussionBody( String discussionBody )
  {
    this.discussionBody = discussionBody;
  }

  public Long getParentDiscussionId()
  {
    return parentDiscussionId;
  }

  public void setParentDiscussionId( Long parentDiscussionId )
  {
    this.parentDiscussionId = parentDiscussionId;
  }

  public String getStatus()
  {
    return status;
  }

  public void setStatus( String status )
  {
    this.status = status;
  }

  public Long getReplies()
  {
    return replies;
  }

  public void setReplies( Long replies )
  {
    this.replies = replies;
  }

  public Long getTopicId()
  {
    return topicId;
  }

  public void setTopicId( Long topicId )
  {
    this.topicId = topicId;
  }

  public Date getDateCreated()
  {
    return dateCreated;
  }

  public void setDateCreated( Date dateCreated )
  {
    this.dateCreated = dateCreated;
  }

  public Date getDateReplied()
  {
    return dateReplied;
  }

  public void setDateReplied( Date dateReplied )
  {
    this.dateReplied = dateReplied;
  }

  public String getCreatedBy()
  {
    return createdBy;
  }

  public void setCreatedBy( String createdBy )
  {
    this.createdBy = createdBy;
  }

  public String getRepliedBy()
  {
    return repliedBy;
  }

  public void setRepliedBy( String repliedBy )
  {
    this.repliedBy = repliedBy;
  }

  public String getCreatedDate()
  {
    return createdDate;
  }

  public void setCreatedDate( String createdDate )
  {
    this.createdDate = createdDate;
  }

  public String getRepliedDate()
  {
    return repliedDate;
  }

  public void setRepliedDate( String repliedDate )
  {
    this.repliedDate = repliedDate;
  }

  public String getTopicCmAssetCode()
  {
    return topicCmAssetCode;
  }

  public void setTopicCmAssetCode( String topicCmAssetCode )
  {
    this.topicCmAssetCode = topicCmAssetCode;
  }

  public String getTopicNameFromCM()
  {
    String topicName = null;
    if ( this.topicCmAssetCode != null )
    {
      topicName = CmsResourceBundle.getCmsBundle().getString( this.topicCmAssetCode, ForumTopic.FORUMTOPIC_NAME_CMASSET_TYPE_KEY );
    }

    return topicName;
  }

  public Long getCreatedUserId()
  {
    return createdUserId;
  }

  public void setCreatedUserId( Long createdUserId )
  {
    this.createdUserId = createdUserId;
  }

  public Long getRepliedUserId()
  {
    return repliedUserId;
  }

  public void setRepliedUserId( Long repliedUserId )
  {
    this.repliedUserId = repliedUserId;
  }
}
