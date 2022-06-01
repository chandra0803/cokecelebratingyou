/**
 * 
 */

package com.biperf.core.domain.forum;

import java.util.Comparator;

import com.biperf.core.value.forum.ForumTopicValueBean;

/**
 * @author poddutur
 *
 */
@SuppressWarnings( "rawtypes" )
public class TopicNamesSortComparator implements Comparator
{

  @SuppressWarnings( "unchecked" )
  @Override
  public int compare( Object o1, Object o2 )
  {
    if ( ! ( o1 instanceof ForumTopicValueBean ) || ! ( o2 instanceof ForumTopicValueBean ) )
    {
      throw new ClassCastException( "Object is not a Forum Topic domain object!" );
    }
    ForumTopicValueBean topic1 = (ForumTopicValueBean)o1;
    ForumTopicValueBean topic2 = (ForumTopicValueBean)o2;

    return ( (Comparable)topic1.getTopicCmAssetCode().toUpperCase() ).compareTo( topic2.getTopicCmAssetCode().toUpperCase() );
  }

}
