
package com.biperf.core.domain.ssi;

import java.util.Date;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.participant.Participant;

/**
 * 
 * @author patelp
 * @since Nov 24, 2014
 * @version 1.0
 */
public class SSIContestParticipantProgress extends BaseDomain
{
  private static final long serialVersionUID = 1L;

  public static final int PAGE_SIZE = 10;
  public static final String DEFAULT_SORTED_BY = "desc";
  public static final String DEFAULT_SORTED_ON = "Date";

  private SSIContest contest;
  private Participant participant;
  private Double activityAmount;
  private Date activityDate;
  private SSIContestActivity contestActivity;

  public SSIContest getContest()
  {
    return contest;
  }

  public void setContest( SSIContest contest )
  {
    this.contest = contest;
  }

  public SSIContestActivity getContestActivity()
  {
    return contestActivity;
  }

  public void setContestActivity( SSIContestActivity contestActivity )
  {
    this.contestActivity = contestActivity;
  }

  public Double getActivityAmount()
  {
    return activityAmount;
  }

  public Participant getParticipant()
  {
    return participant;
  }

  public void setParticipant( Participant participant )
  {
    this.participant = participant;
  }

  public void setActivityAmount( Double activityAmount )
  {
    this.activityAmount = activityAmount;
  }

  public Date getActivityDate()
  {
    return activityDate;
  }

  public void setActivityDate( Date activityDate )
  {
    this.activityDate = activityDate;
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

    SSIContestParticipantProgress other = (SSIContestParticipantProgress)object;
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

  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + ( contest == null ? 0 : contest.hashCode() );
    result = prime * result + ( participant == null ? 0 : participant.hashCode() );
    return result;
  }

}
