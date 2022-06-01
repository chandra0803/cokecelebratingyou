
package com.biperf.core.domain.ssi;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.participant.Participant;

public class SSIContestManager extends BaseDomain
{
  public static final int RECORDS_PER_PAGE = 20;
  public static final String DEFAULT_SORTED_BY = "asc";
  public static final String DEFAULT_SORTED_ON = "lastName";

  public static final Integer FIRST_PAGE_NUMBER = 1;

  private SSIContest contest;
  private Participant manager;
  // To notify whether launch notification is triggered.
  private boolean isLaunchNotificationSent;

  public SSIContest getContest()
  {
    return contest;
  }

  public void setContest( SSIContest contest )
  {
    this.contest = contest;
  }

  public Participant getManager()
  {
    return manager;
  }

  public void setManager( Participant manager )
  {
    this.manager = manager;
  }

  @Override
  public boolean equals( Object object )
  {
    if ( this == object )
    {
      return true;
    }

    if ( object == null )
    {
      return false;
    }

    SSIContestManager other = (SSIContestManager)object;
    if ( contest == null )
    {
      if ( other.contest != null )
      {
        return false;
      }
    }
    else if ( !contest.equals( other.contest ) )
    {
      return false;
    }

    if ( manager == null )
    {
      if ( other.manager != null )
      {
        return false;
      }
    }
    else if ( !manager.equals( other.manager ) )
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
    result = prime * result + ( contest == null ? 0 : contest.hashCode() );
    result = prime * result + ( manager == null ? 0 : manager.hashCode() );
    return result;
  }

  public boolean isLaunchNotificationSent()
  {
    return isLaunchNotificationSent;
  }

  public void setLaunchNotificationSent( boolean isLaunchNotificationSent )
  {
    this.isLaunchNotificationSent = isLaunchNotificationSent;
  }
}
