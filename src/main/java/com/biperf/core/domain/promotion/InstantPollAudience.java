/**
 * 
 */

package com.biperf.core.domain.promotion;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.participant.Audience;

/**
 * @author poddutur
 *
 */
public class InstantPollAudience extends BaseDomain
{
  /**
   * 
   */
  private static final long serialVersionUID = 2047958135956860283L;

  private InstantPoll instantPoll;
  private Audience audience;

  public Audience getAudience()
  {
    return audience;
  }

  public void setAudience( Audience audience )
  {
    this.audience = audience;
  }

  public InstantPoll getInstantPoll()
  {
    return instantPoll;
  }

  public void setInstantPoll( InstantPoll instantPoll )
  {
    this.instantPoll = instantPoll;
  }

  @Override
  public boolean equals( Object o )
  {
    if ( this == o )
    {
      return true;
    }
    if ( ! ( o instanceof InstantPollAudience ) )
    {
      return false;
    }

    final InstantPollAudience instantPollAudience = (InstantPollAudience)o;

    if ( getInstantPoll() != null ? !getInstantPoll().equals( instantPollAudience.getInstantPoll() ) : instantPollAudience.getInstantPoll() != null )
    {
      return false;
    }
    if ( getAudience() != null ? !getAudience().equals( instantPollAudience.getAudience() ) : instantPollAudience.getAudience() != null )
    {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode()
  {
    int result;
    result = getInstantPoll() != null ? getInstantPoll().hashCode() : 0;
    result = 29 * result + ( getAudience() != null ? getAudience().hashCode() : 0 );

    return result;
  }

}
