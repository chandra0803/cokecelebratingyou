/**
 * 
 */

package com.biperf.core.domain.forum;

import java.util.HashSet;
import java.util.Set;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.user.User;

/**
 * @author poddutur
 * 
 */
public class ForumDiscussion extends BaseDomain
{
  /**
   * 
   */
  private static final long serialVersionUID = 1465487472577331064L;
  private ForumTopic forumTopic;
  private String discussionTitle;
  private String discussionBody;
  private String status;
  private String createdDate;
  private String modifiedDate;
  private Set<ForumDiscussionReply> discussionReply = new HashSet<ForumDiscussionReply>( 0 );
  private Set<ForumDiscussionLike> discussionLike = new HashSet<ForumDiscussionLike>( 0 );
  private User user;

  public ForumDiscussion()
  {
    super();
  }

  public ForumTopic getForumTopic()
  {
    return forumTopic;
  }

  public void setForumTopic( ForumTopic forumTopic )
  {
    this.forumTopic = forumTopic;
  }

  public String getStatus()
  {
    return status;
  }

  public void setStatus( String status )
  {
    this.status = status;
  }

  public Set<ForumDiscussionReply> getDiscussionReply()
  {
    return discussionReply;
  }

  public void setDiscussionReply( Set<ForumDiscussionReply> discussionReply )
  {
    this.discussionReply = discussionReply;
  }

  public Set<ForumDiscussionLike> getDiscussionLike()
  {
    return discussionLike;
  }

  public void setDiscussionLike( Set<ForumDiscussionLike> discussionLike )
  {
    this.discussionLike = discussionLike;
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

  public User getUser()
  {
    return user;
  }

  public void setUser( User user )
  {
    this.user = user;
  }

  public String getCreatedDate()
  {
    return createdDate;
  }

  public void setCreatedDate( String createdDate )
  {
    this.createdDate = createdDate;
  }

  public String getModifiedDate()
  {
    return modifiedDate;
  }

  public void setModifiedDate( String modifiedDate )
  {
    this.modifiedDate = modifiedDate;
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
    ForumDiscussion other = (ForumDiscussion)obj;
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
    final int prime = 31;
    int result = 1;
    result = prime * result + ( getId() == null ? 0 : getId().hashCode() );
    return result;
  }

}
