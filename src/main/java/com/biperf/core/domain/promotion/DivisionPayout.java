
package com.biperf.core.domain.promotion;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.enums.MatchTeamOutcomeType;
import com.fasterxml.jackson.annotation.JsonBackReference;

@SuppressWarnings( "serial" )
public class DivisionPayout extends BaseDomain implements Cloneable
{
  @JsonBackReference
  private Division division;
  private MatchTeamOutcomeType outcome;
  private int points;
  private int pickPoints;

  public MatchTeamOutcomeType getOutcome()
  {
    return outcome;
  }

  public void setOutcome( MatchTeamOutcomeType matchOutcomeType )
  {
    this.outcome = matchOutcomeType;
  }

  public int getPoints()
  {
    return points;
  }

  public void setPoints( int points )
  {
    this.points = points;
  }

  public int getPickPoints()
  {
    return pickPoints;
  }

  public void setPickPoints( int pickPoints )
  {
    this.pickPoints = pickPoints;
  }

  public Division getDivision()
  {
    return division;
  }

  public void setDivision( Division division )
  {
    this.division = division;
  }

  @Override
  public boolean equals( Object object )
  {
    if ( this == object )
    {
      return true;
    }
    if ( ! ( object instanceof DivisionPayout ) )
    {
      return false;
    }

    final DivisionPayout divPayout = (DivisionPayout)object;

    if ( getDivision() != null ? !getDivision().equals( divPayout.getDivision() ) : divPayout.getDivision() != null )
    {
      return false;
    }

    if ( getOutcome() != null ? !getOutcome().equals( divPayout.getOutcome() ) : divPayout.getOutcome() != null )
    {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode()
  {
    int result;
    result = getDivision() != null ? getDivision().hashCode() : 0;
    result = 31 * result + ( getOutcome() != null ? getOutcome().getCode().hashCode() : 0 );

    return result;
  }

  /**
   * Clones this, removes the auditInfo and id. Overridden from
   * 
   * @see java.lang.Object#clone()
   * @return Object
   * @throws CloneNotSupportedException
   */
  public Object clone() throws CloneNotSupportedException
  {

    DivisionPayout clonedPayoutDivision = (DivisionPayout)super.clone();
    clonedPayoutDivision.resetBaseDomain();

    return clonedPayoutDivision;
  }

  public String toString()
  {
    StringBuilder sb = new StringBuilder();
    if ( division != null )
    {
      sb.append( "Division id: " + division.getId() + "\n" );
    }
    sb.append( "outcomeType=" + outcome.getCode() + "\n" );
    sb.append( "points=" + points + "\n" );
    sb.append( "pickPoints=" + pickPoints + "\n" );
    return sb.toString();
  }
}
