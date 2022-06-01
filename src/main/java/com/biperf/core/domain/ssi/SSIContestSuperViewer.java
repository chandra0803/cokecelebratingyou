
package com.biperf.core.domain.ssi;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.participant.Participant;

public class SSIContestSuperViewer extends BaseDomain
{
  public static final int RECORDS_PER_PAGE = 20;
  public static final String DEFAULT_SORTED_BY = "asc";
  public static final String DEFAULT_SORTED_ON = "lastName";

  public static final Integer FIRST_PAGE_NUMBER = 1;

  private SSIContest contest;
  private Participant superViewer;

  public SSIContest getContest()
  {
    return contest;
  }

  public void setContest( SSIContest contest )
  {
    this.contest = contest;
  }

  public Participant getSuperViewer()
  {
    return superViewer;
  }

  public void setSuperViewer( Participant superViewer )
  {
    this.superViewer = superViewer;
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

    SSIContestSuperViewer other = (SSIContestSuperViewer)object;
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

    if ( superViewer == null )
    {
      if ( other.superViewer != null )
      {
        return false;
      }
    }
    else if ( !superViewer.equals( other.superViewer ) )
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
    result = prime * result + ( superViewer == null ? 0 : superViewer.hashCode() );
    return result;
  }
}
