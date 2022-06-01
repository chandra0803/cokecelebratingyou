/**
 * 
 */

package com.biperf.core.domain.forum;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.user.User;

/**
 * @author poddutur
 * 
 */
public class ForumDiscussionLike extends BaseDomain
{
  /**
   * 
   */
  private static final long serialVersionUID = -199408746915396832L;
  private ForumDiscussion forumDiscussion;
  private User user;

  public ForumDiscussionLike()
  {
    super();
  }

  public ForumDiscussion getForumDiscussion()
  {
    return forumDiscussion;
  }

  public void setForumDiscussion( ForumDiscussion forumDiscussion )
  {
    this.forumDiscussion = forumDiscussion;
  }

  public User getUser()
  {
    return user;
  }

  public void setUser( User user )
  {
    this.user = user;
  }

  /**
   * Checks equality of the object parameter to this.
   * 
   * @param o
   * @return boolean
   */
  public boolean equals( Object o )
  {
    if ( this == o )
    {
      return true;
    }
    if ( ! ( o instanceof ForumDiscussionLike ) )
    {
      return false;
    }

    final ForumDiscussionLike forumDiscussionLike = (ForumDiscussionLike)o;

    if ( getForumDiscussion() != null ? !getForumDiscussion().equals( forumDiscussionLike.getForumDiscussion() ) : forumDiscussionLike.getForumDiscussion() != null )
    {
      return false;
    }
    if ( getUser() != null ? !getUser().equals( forumDiscussionLike.getUser() ) : forumDiscussionLike.getUser() != null )
    {
      return false;
    }

    return true;
  }

  /**
   * Define the hashCode from the id. Overridden from
   * 
   * @see com.biperf.core.domain.BaseDomain#hashCode()
   * @return int
   */
  public int hashCode()
  {
    int result;
    result = getForumDiscussion() != null ? getForumDiscussion().hashCode() : 0;
    result = 29 * result + ( getUser() != null ? getUser().hashCode() : 0 );

    return result;
  }

}
