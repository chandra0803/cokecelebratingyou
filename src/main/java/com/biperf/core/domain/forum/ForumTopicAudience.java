/**
 * 
 */

package com.biperf.core.domain.forum;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.participant.Audience;

/**
 * @author poddutur
 * 
 */
public class ForumTopicAudience extends BaseDomain
{
  /**
   * 
   */
  private static final long serialVersionUID = -425109340468900251L;
  private Audience audience;
  private ForumTopic forumTopic;

  public ForumTopicAudience()
  {
    super();
  }

  public Audience getAudience()
  {
    return audience;
  }

  public void setAudience( Audience audience )
  {
    this.audience = audience;
  }

  public ForumTopic getForumTopic()
  {
    return forumTopic;
  }

  public void setForumTopic( ForumTopic forumTopic )
  {
    this.forumTopic = forumTopic;
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
    if ( ! ( o instanceof ForumTopicAudience ) )
    {
      return false;
    }

    final ForumTopicAudience forumTopicAudience = (ForumTopicAudience)o;

    if ( getForumTopic() != null ? !getForumTopic().equals( forumTopicAudience.getForumTopic() ) : forumTopicAudience.getForumTopic() != null )
    {
      return false;
    }
    if ( getAudience() != null ? !getAudience().equals( forumTopicAudience.getAudience() ) : forumTopicAudience.getAudience() != null )
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
    result = getForumTopic() != null ? getForumTopic().hashCode() : 0;
    result = 29 * result + ( getAudience() != null ? getAudience().hashCode() : 0 );

    return result;
  }

}
