
package com.biperf.core.domain.ssi;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.enums.SSIContestType;
import com.biperf.core.domain.participant.Participant;

/**
 * 
 * SSIContestParticipant.
 * 
 * @author kandhi
 * @since Nov 5, 2014
 * @version 1.0
 */
public class SSIContestParticipant extends BaseDomain
{
  private SSIContest contest;
  private Participant participant;
  private String activityDescription;
  private Double objectiveAmount;
  private Long objectivePayout;
  private String objectivePayoutDescription;
  private Double objectiveBonusIncrement;
  private Long objectiveBonusPayout;
  private Long objectiveBonusCap;
  private Double stepItUpBaselineAmount;

  // Applies only to Award them now. For other contest types this will be null
  private Short awardIssuanceNumber;
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

  public Participant getParticipant()
  {
    return participant;
  }

  public void setParticipant( Participant participant )
  {
    this.participant = participant;
  }

  public String getActivityDescription()
  {
    return activityDescription;
  }

  public void setActivityDescription( String activityDescription )
  {
    this.activityDescription = activityDescription;
  }

  public Double getObjectiveAmount()
  {
    return objectiveAmount;
  }

  public void setObjectiveAmount( Double objectiveAmount )
  {
    this.objectiveAmount = objectiveAmount;
  }

  public Long getObjectivePayout()
  {
    return objectivePayout;
  }

  public void setObjectivePayout( Long objectivePayout )
  {
    this.objectivePayout = objectivePayout;
  }

  public String getObjectivePayoutDescription()
  {
    return objectivePayoutDescription;
  }

  public void setObjectivePayoutDescription( String objectivePayoutDescription )
  {
    this.objectivePayoutDescription = objectivePayoutDescription;
  }

  public Double getObjectiveBonusIncrement()
  {
    return objectiveBonusIncrement;
  }

  public void setObjectiveBonusIncrement( Double objectiveBonusIncrement )
  {
    this.objectiveBonusIncrement = objectiveBonusIncrement;
  }

  public Long getObjectiveBonusPayout()
  {
    return objectiveBonusPayout;
  }

  public void setObjectiveBonusPayout( Long objectiveBonusPayout )
  {
    this.objectiveBonusPayout = objectiveBonusPayout;
  }

  public Long getObjectiveBonusCap()
  {
    return objectiveBonusCap;
  }

  public void setObjectiveBonusCap( Long objectiveBonusCap )
  {
    this.objectiveBonusCap = objectiveBonusCap;
  }

  public Double getStepItUpBaselineAmount()
  {
    return stepItUpBaselineAmount;
  }

  public void setStepItUpBaselineAmount( Double stepItUpBaselineAmount )
  {
    this.stepItUpBaselineAmount = stepItUpBaselineAmount;
  }

  public Short getAwardIssuanceNumber()
  {
    return awardIssuanceNumber;
  }

  public void setAwardIssuanceNumber( Short awardIssuanceNumber )
  {
    this.awardIssuanceNumber = awardIssuanceNumber;
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

    SSIContestParticipant other = (SSIContestParticipant)object;
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
    else if ( contest != null && other.contest != null && contest.equals( other.contest ) )
    {
      if ( SSIContestType.AWARD_THEM_NOW.equals( contest.getContestType().getCode() ) )
      {
        if ( awardIssuanceNumber == null || other.awardIssuanceNumber == null )
        {
          return false;
        }
        else if ( !awardIssuanceNumber.equals( other.awardIssuanceNumber ) )
        {
          return false;
        }
      }
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
    result = prime * result + ( awardIssuanceNumber == null ? 0 : awardIssuanceNumber.hashCode() );
    result = prime * result + ( participant == null ? 0 : participant.hashCode() );
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
