/**
 * 
 */

package com.biperf.core.domain.forum;

import java.util.Comparator;

import com.biperf.core.utils.DateUtils;
import com.biperf.core.value.forum.ForumTopicValueBean;

/**
 * @author poddutur
 *
 */
public class TopicSortComparator implements Comparator
{
  @Override
  public int compare( Object o1, Object o2 )
  {
    if ( ! ( o1 instanceof ForumTopicValueBean ) || ! ( o2 instanceof ForumTopicValueBean ) )
    {
      throw new ClassCastException( "Object is not a Forum Topic domain object!" );
    }
    ForumTopicValueBean topic1 = (ForumTopicValueBean)o1;
    ForumTopicValueBean topic2 = (ForumTopicValueBean)o2;

    if ( topic1 != null && topic2 != null )
    {
      if ( topic1.getStickyEndDate() != null && topic2.getStickyEndDate() != null )
      {
        if ( topic1.getStickyEndDate().compareTo( DateUtils.getCurrentDateTrimmed() ) >= 0 && topic2.getStickyEndDate().compareTo( DateUtils.getCurrentDateTrimmed() ) >= 0 )
        {
          int priority = CallSortOrderMethod( topic1, topic2 );
          return priority;
        }
        else if ( topic1.getStickyEndDate().compareTo( DateUtils.getCurrentDateTrimmed() ) >= 0 && topic2.getStickyEndDate().compareTo( DateUtils.getCurrentDateTrimmed() ) < 0 )
        {
          return -1;
        }
        else if ( topic2.getStickyEndDate().compareTo( DateUtils.getCurrentDateTrimmed() ) >= 0 && topic1.getStickyEndDate().compareTo( DateUtils.getCurrentDateTrimmed() ) < 0 )
        {
          return 1;
        }
        else if ( topic1.getStickyEndDate().compareTo( DateUtils.getCurrentDateTrimmed() ) < 0 && topic2.getStickyEndDate().compareTo( DateUtils.getCurrentDateTrimmed() ) < 0 )
        {
          int priority = CallSortOrderMethod( topic1, topic2 );
          return priority;
        }
      }
      else if ( topic1.getStickyEndDate() != null && topic2.getStickyEndDate() == null )
      {
        if ( topic1.getStickyEndDate().compareTo( DateUtils.getCurrentDateTrimmed() ) >= 0 )
        {
          return -1;
        }
        else if ( topic1.getStickyEndDate().compareTo( DateUtils.getCurrentDateTrimmed() ) < 0 )
        {
          int priority = CallSortOrderMethod( topic1, topic2 );
          return priority;
        }
      }
      else if ( topic2.getStickyEndDate() != null && topic1.getStickyEndDate() == null )
      {
        if ( topic2.getStickyEndDate().compareTo( DateUtils.getCurrentDateTrimmed() ) >= 0 )
        {
          return 1;
        }
        else if ( topic2.getStickyEndDate().compareTo( DateUtils.getCurrentDateTrimmed() ) < 0 )
        {
          int priority = CallSortOrderMethod( topic1, topic2 );
          return priority;
        }
      }
      else if ( topic1.getStickyEndDate() == null && topic2.getStickyEndDate() == null )
      {
        int priority = CallSortOrderMethod( topic1, topic2 );
        return priority;
      }
    }

    return 0;
  }

  public int CallSortOrderMethod( ForumTopicValueBean topic1, ForumTopicValueBean topic2 )
  {
    if ( topic1.getSortOrder() != null && topic2.getSortOrder() != null )
    {
      if ( topic1.getSortOrder().compareTo( topic2.getSortOrder() ) == 0 )
      {
        int priorityActivityDate = CallLastActivityDateMethod( topic1, topic2 );
        return priorityActivityDate;
      }
      else
      {
        return topic1.getSortOrder().compareTo( topic2.getSortOrder() );
      }
    }
    else if ( topic1.getSortOrder() != null && topic2.getSortOrder() == null )
    {
      return -1;
    }
    else if ( topic1.getSortOrder() == null && topic2.getSortOrder() != null )
    {
      return 1;
    }
    else
    {
      int priorityActivityDate = CallLastActivityDateMethod( topic1, topic2 );
      return priorityActivityDate;
    }
  }

  public int CallLastActivityDateMethod( ForumTopicValueBean topic1, ForumTopicValueBean topic2 )
  {
    if ( topic1.getLastActivityDate() != null && topic2.getLastActivityDate() != null )
    {
      return topic1.getLastActivityDate().compareTo( topic2.getLastActivityDate() );
    }
    else if ( topic1.getLastActivityDate() != null && topic2.getLastActivityDate() == null )
    {
      return -1;
    }
    else if ( topic1.getLastActivityDate() == null && topic2.getLastActivityDate() != null )
    {
      return 1;
    }
    else
    {
      return 0;
    }
  }
}
