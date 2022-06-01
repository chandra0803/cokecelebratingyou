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
public class ForumTopicValueBean
{
  private Long id;
  private String topicCmAssetCode;
  private String audienceName;
  private Date stickyStartDate;
  private Date stickyEndDate;
  private Date lastActivityDate;
  private Long sortOrder;
  private String status;
  private Long discussionCount;
  private boolean allActivePaxEntry;
  private boolean specifyAudienceEntry;
  private boolean specifyOrder;
  private boolean sticky;
  private String lastActivityDateString;
  private Long repliesCount;
  private String lastPostUserName;
  private Long lastPostUserId;
  private String selectedTopic;
  private Long selectedTopicId;

  public ForumTopicValueBean()
  {
    super();
  }

  public Long getId()
  {
    return id;
  }

  public void setId( Long id )
  {
    this.id = id;
  }

  public String getTopicCmAssetCode()
  {
    return topicCmAssetCode;
  }

  public void setTopicCmAssetCode( String topicCmAssetCode )
  {
    this.topicCmAssetCode = topicCmAssetCode;
  }

  public String getAudienceName()
  {
    return audienceName;
  }

  public void setAudienceName( String audienceName )
  {
    this.audienceName = audienceName;
  }

  public Date getStickyStartDate()
  {
    return stickyStartDate;
  }

  public void setStickyStartDate( Date stickyStartDate )
  {
    this.stickyStartDate = stickyStartDate;
  }

  public Date getStickyEndDate()
  {
    return stickyEndDate;
  }

  public void setStickyEndDate( Date stickyEndDate )
  {
    this.stickyEndDate = stickyEndDate;
  }

  public Date getLastActivityDate()
  {
    return lastActivityDate;
  }

  public void setLastActivityDate( Date lastActivityDate )
  {
    this.lastActivityDate = lastActivityDate;
  }

  public Long getSortOrder()
  {
    return sortOrder;
  }

  public void setSortOrder( Long sortOrder )
  {
    this.sortOrder = sortOrder;
  }

  public String getStatus()
  {
    return status;
  }

  public void setStatus( String status )
  {
    this.status = status;
  }

  public Long getDiscussionCount()
  {
    return discussionCount;
  }

  public void setDiscussionCount( Long discussionCount )
  {
    this.discussionCount = discussionCount;
  }

  public boolean isAllActivePaxEntry()
  {
    return allActivePaxEntry;
  }

  public void setAllActivePaxEntry( boolean allActivePaxEntry )
  {
    this.allActivePaxEntry = allActivePaxEntry;
  }

  public boolean isSpecifyAudienceEntry()
  {
    return specifyAudienceEntry;
  }

  public void setSpecifyAudienceEntry( boolean specifyAudienceEntry )
  {
    this.specifyAudienceEntry = specifyAudienceEntry;
  }

  public boolean isSpecifyOrder()
  {
    return specifyOrder;
  }

  public void setSpecifyOrder( boolean specifyOrder )
  {
    this.specifyOrder = specifyOrder;
  }

  public boolean isSticky()
  {
    return sticky;
  }

  public void setSticky( boolean sticky )
  {
    this.sticky = sticky;
  }

  public String getTopicNameFromCM()
  {
    String topicNameFromCm = null;
    if ( this.topicCmAssetCode != null )
    {
      topicNameFromCm = CmsResourceBundle.getCmsBundle().getString( this.topicCmAssetCode, ForumTopic.FORUMTOPIC_NAME_CMASSET_TYPE_KEY );
    }

    return topicNameFromCm;
  }

  public String getLastActivityDateString()
  {
    return lastActivityDateString;
  }

  public void setLastActivityDateString( String lastActivityDateString )
  {
    this.lastActivityDateString = lastActivityDateString;
  }

  public Long getRepliesCount()
  {
    return repliesCount;
  }

  public void setRepliesCount( Long repliesCount )
  {
    this.repliesCount = repliesCount;
  }

  public String getLastPostUserName()
  {
    return lastPostUserName;
  }

  public void setLastPostUserName( String lastPostUserName )
  {
    this.lastPostUserName = lastPostUserName;
  }

  public Long getLastPostUserId()
  {
    return lastPostUserId;
  }

  public void setLastPostUserId( Long lastPostUserId )
  {
    this.lastPostUserId = lastPostUserId;
  }

  public String getSelectedTopic()
  {
    return selectedTopic;
  }

  public void setSelectedTopic( String selectedTopic )
  {
    this.selectedTopic = selectedTopic;
  }

  public Long getSelectedTopicId()
  {
    return selectedTopicId;
  }

  public void setSelectedTopicId( Long selectedTopicId )
  {
    this.selectedTopicId = selectedTopicId;
  }

}
