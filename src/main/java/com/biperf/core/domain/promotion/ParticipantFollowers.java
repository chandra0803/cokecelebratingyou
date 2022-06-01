
package com.biperf.core.domain.promotion;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.user.User;

/**
 * Product.
 * <p>
 * <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <td>veeramas</td>
 * <td>July 23, 2012</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 *
 *
 */
public class ParticipantFollowers extends BaseDomain
{

  private User participant;

  private User follower;

  public User getParticipant()
  {
    return participant;
  }

  public void setParticipant( User participant )
  {
    this.participant = participant;
  }

  public User getFollower()
  {
    return follower;
  }

  public void setFollower( User follower )
  {
    this.follower = follower;
  }

  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + ( follower == null ? 0 : follower.hashCode() );
    result = prime * result + ( participant == null ? 0 : participant.hashCode() );
    return result;
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
    ParticipantFollowers other = (ParticipantFollowers)obj;
    if ( follower == null )
    {
      if ( other.follower != null )
      {
        return false;
      }
    }
    else if ( !follower.equals( other.follower ) )
    {
      return false;
    }
    if ( participant == null )
    {
      if ( other.participant != null )
      {
        return false;
      }
    }
    else if ( !participant.equals( other.participant ) )
    {
      return false;
    }
    return true;
  }
}
