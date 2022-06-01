
package com.biperf.core.domain.promotion;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.enums.ThrowdownMatchProgressType;
import com.biperf.core.domain.participant.Participant;

@SuppressWarnings( "serial" )
public class MatchTeamProgress extends BaseDomain implements Cloneable
{
  private MatchTeamOutcome teamOutcome;
  private ThrowdownMatchProgressType progressType;
  private BigDecimal progress;

  public MatchTeamOutcome getTeamOutcome()
  {
    return teamOutcome;
  }

  public void setTeamOutcome( MatchTeamOutcome teamOutcome )
  {
    this.teamOutcome = teamOutcome;
  }

  public BigDecimal getProgress()
  {
    return progress;
  }

  public void setProgress( BigDecimal progress )
  {
    this.progress = progress;
  }

  public ThrowdownMatchProgressType getProgressType()
  {
    return progressType;
  }

  public void setProgressType( ThrowdownMatchProgressType progressType )
  {
    this.progressType = progressType;
  }

  @Override
  public boolean equals( Object object )
  {
    if ( this == object )
    {
      return true;
    }
    if ( ! ( object instanceof MatchTeamProgress ) )
    {
      return false;
    }

    final MatchTeamProgress matchTeamProgress = (MatchTeamProgress)object;
    MatchTeamOutcome thisOutcome = getTeamOutcome();
    MatchTeamOutcome otherOutcome = matchTeamProgress.getTeamOutcome();

    if ( thisOutcome != null ? !thisOutcome.equals( otherOutcome ) : otherOutcome != null )
    {
      return false;
    }

    ThrowdownPromotion thisPromotion = thisOutcome != null ? thisOutcome.getPromotion() : null;
    ThrowdownPromotion otherPromotion = otherOutcome != null ? otherOutcome.getPromotion() : null;
    if ( thisPromotion != null ? !thisPromotion.equals( otherPromotion ) : otherPromotion != null )
    {
      return false;
    }

    Participant thisPax = thisOutcome != null && thisOutcome.getTeam() != null ? thisOutcome.getTeam().getParticipant() : null;
    Participant otherPax = otherOutcome != null && otherOutcome.getTeam() != null ? otherOutcome.getTeam().getParticipant() : null;
    if ( thisPax != null ? !thisPax.equals( otherPax ) : otherPax != null )
    {
      return false;
    }

    Timestamp thisDate = getAuditCreateInfo() != null ? getAuditCreateInfo().getDateCreated() : null;
    Timestamp otherDate = matchTeamProgress.getAuditCreateInfo() != null ? matchTeamProgress.getAuditCreateInfo().getDateCreated() : null;
    if ( thisDate != null ? !thisDate.equals( otherDate ) : otherDate != null )
    {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode()
  {
    int result;

    MatchTeamOutcome thisOutcome = getTeamOutcome();
    ThrowdownPromotion thisPromotion = thisOutcome != null ? thisOutcome.getPromotion() : null;
    Participant thisPax = thisOutcome != null && thisOutcome.getTeam() != null ? thisOutcome.getTeam().getParticipant() : null;
    Timestamp thisDate = getAuditCreateInfo() != null ? getAuditCreateInfo().getDateCreated() : null;

    result = thisOutcome != null ? thisOutcome.hashCode() : 0;
    result = thisPromotion != null ? thisPromotion.hashCode() : 0;
    result = 29 * result + ( thisPax != null ? thisPax.hashCode() : 0 );
    result = 29 * result + ( thisDate != null ? thisDate.hashCode() : 0 );

    return result;
  }
}
