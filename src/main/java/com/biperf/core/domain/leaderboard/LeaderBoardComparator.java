
package com.biperf.core.domain.leaderboard;

import java.util.Comparator;

public class LeaderBoardComparator implements Comparator
{
  public int compare( Object o1, Object o2 )
  {
    if ( ! ( o1 instanceof LeaderBoard ) || ! ( o2 instanceof LeaderBoard ) )
    {
      throw new ClassCastException( "Object is not a accountTransaction domain object!" );
    }
    LeaderBoard event1 = (LeaderBoard)o1;
    LeaderBoard event2 = (LeaderBoard)o2;

    if ( event1 != null && event2 != null )
    {
      return event1.getName().toUpperCase().compareTo( event2.getName().toUpperCase() );
    }
    return 0;
  }
}
