/**
 * 
 */

package com.biperf.core.domain.forum;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.biperf.core.domain.BaseDomain;
import com.objectpartners.cms.util.CmsResourceBundle;

/**
 * @author poddutur
 * 
 */
public class ForumTopic extends BaseDomain
{
  /**
   * 
   */
  private static final long serialVersionUID = 4566691980268824550L;
  private String topicCmAssetCode;
  private String audienceType;
  private Date stickyStartDate;
  private Date stickyEndDate;
  private Date lastActivityDate;
  private Long sortOrder;
  private String status;
  private boolean allActivePaxEntry;
  private boolean specifyAudienceEntry;
  private boolean specifyOrder;
  private boolean sticky;
  private Set<ForumTopicAudience> audience = new HashSet<ForumTopicAudience>( 0 );
  private Set<ForumDiscussion> discussion = new HashSet<ForumDiscussion>( 0 );

  private Long discussionCount;
  private String topicscmAsset;

  public static final String FORUMTOPIC_NAME_SECTION_CODE = "forum";
  public static final String FORUMTOPIC_NAME_CMASSET_TYPE_NAME = "Name Type";
  public static final String FORUMTOPIC_NAME_CMASSET_TYPE_KEY = "HTML_KEY";
  public static final String FORUMTOPIC_NAME_CMASSET_NAME = "ForumTopic Name Text";
  public static final String FORUMTOPIC_NAME_CMASSET_PREFIX = "forum.topicname.";

  public ForumTopic()
  {
    super();
  }

  public String getTopicCmAssetCode()
  {
    return topicCmAssetCode;
  }

  public void setTopicCmAssetCode( String topicCmAssetCode )
  {
    this.topicCmAssetCode = topicCmAssetCode;
  }

  public String getStatus()
  {
    return status;
  }

  public void setStatus( String status )
  {
    this.status = status;
  }

  public String getAudienceType()
  {
    return audienceType;
  }

  public void setAudienceType( String audienceType )
  {
    this.audienceType = audienceType;
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

  public Set<ForumTopicAudience> getAudience()
  {
    return audience;
  }

  public void setAudience( Set<ForumTopicAudience> audience )
  {
    this.audience = audience;
  }

  public Set<ForumDiscussion> getDiscussion()
  {
    return discussion;
  }

  public void setDiscussion( Set<ForumDiscussion> discussion )
  {
    this.discussion = discussion;
  }

  public boolean getSpecifyOrder()
  {
    return specifyOrder;
  }

  public void setSpecifyOrder( boolean specifyOrder )
  {
    this.specifyOrder = specifyOrder;
  }

  public boolean getSticky()
  {
    return sticky;
  }

  public void setSticky( boolean sticky )
  {
    this.sticky = sticky;
  }

  public Long getDiscussionCount()
  {
    return discussionCount;
  }

  public void setDiscussionCount( Long discussionCount )
  {
    this.discussionCount = discussionCount;
  }

  public String getTopicscmAsset()
  {
    return topicscmAsset;
  }

  public void setTopicscmAsset( String topicscmAsset )
  {
    this.topicscmAsset = topicscmAsset;
  }

  public String getTopicNameFromCM()
  {
    String topicName = null;
    if ( this.topicscmAsset != null )
    {
      topicName = CmsResourceBundle.getCmsBundle().getString( this.topicscmAsset, ForumTopic.FORUMTOPIC_NAME_CMASSET_TYPE_KEY );
    }

    return topicName;
  }

  @Override
  public boolean equals( Object obj )
  {
    if ( this == obj )
    {
      return true;
    }
    if ( obj == null )
    {
      return false;
    }
    if ( getClass() != obj.getClass() )
    {
      return false;
    }
    ForumTopic other = (ForumTopic)obj;
    if ( getId() == null )
    {
      if ( other.getId() != null )
      {
        return false;
      }
    }
    else if ( !getId().equals( other.getId() ) )
    {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode()
  {
    int result = 0;

    result += this.getId() != null ? this.getId().hashCode() : 0;

    return result;
  }

}
