
package com.biperf.core.domain.ssi;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.participant.Participant;

public class SSIContestParticipantStackRank extends BaseDomain
{
  private SSIContest contest;
  private Participant participant;
  private SSIContestActivity activity;
  private Long stackRankPosition;

  public SSIContest getContest()
  {
    return contest;
  }

  public void setContest( SSIContest contest )
  {
    this.contest = contest;
  }

  public Participant getParticipant()
  {
    return participant;
  }

  public void setParticipant( Participant participant )
  {
    this.participant = participant;
  }

  public SSIContestActivity getActivity()
  {
    return activity;
  }

  public void setActivity( SSIContestActivity activity )
  {
    this.activity = activity;
  }

  public Long getStackRankPosition()
  {
    return stackRankPosition;
  }

  public void setStackRankPosition( Long stackRankPosition )
  {
    this.stackRankPosition = stackRankPosition;
  }

  @Override
  public boolean equals( Object object )
  {
    return false;
  }

  @Override
  public int hashCode()
  {
    return 0;
  }
}
