
package com.biperf.core.domain.promotion;

import java.util.Date;
import java.util.Set;

import com.biperf.core.domain.BaseDomain;

@SuppressWarnings( "serial" )
public class Round extends BaseDomain implements Cloneable
{
  private Division division;
  private Date startDate;
  private Date endDate;
  private int roundNumber;
  private boolean payoutsIssued = false;
  private Set<Match> matches;

  public Division getDivision()
  {
    return division;
  }

  public void setDivision( Division division )
  {
    this.division = division;
  }

  public Date getStartDate()
  {
    return startDate;
  }

  public void setStartDate( Date startDate )
  {
    this.startDate = startDate;
  }

  public Date getEndDate()
  {
    return endDate;
  }

  public void setEndDate( Date endDate )
  {
    this.endDate = endDate;
  }

  public Set<Match> getMatches()
  {
    return matches;
  }

  public void setMatches( Set<Match> matches )
  {
    this.matches = matches;
  }

  public int getRoundNumber()
  {
    return roundNumber;
  }

  public void setRoundNumber( int roundNumber )
  {
    this.roundNumber = roundNumber;
  }

  public boolean isPayoutsIssued()
  {
    return payoutsIssued;
  }

  public void setPayoutsIssued( boolean payoutsIssued )
  {
    this.payoutsIssued = payoutsIssued;
  }

  @Override
  public boolean equals( Object object )
  {
    if ( this == object )
    {
      return true;
    }
    if ( ! ( object instanceof Round ) )
    {
      return false;
    }

    final Round round = (Round)object;

    if ( getDivision() != null ? !getDivision().equals( round.getDivision() ) : round.getDivision() != null )
    {
      return false;
    }

    if ( getRoundNumber() != round.getRoundNumber() )
    {
      return false;
    }

    if ( getId() != null ? !getId().equals( round.getId() ) : round.getId() != null )
    {
      return false;
    }

    return false;
  }

  @Override
  public int hashCode()
  {
    int result;
    result = getDivision() != null ? getDivision().hashCode() : 0;
    result = 31 * result + getRoundNumber();

    return result;
  }
}
