
package com.biperf.core.service.throwdown;

import com.biperf.core.domain.promotion.Team;

public class TeamMatching implements java.io.Serializable
{
  private Team competitor;
  private int numberOfMatches = 0;

  public Team getCompetitor()
  {
    return competitor;
  }

  public void setCompetitor( Team competitor )
  {
    this.competitor = competitor;
  }

  public int getNumberOfMatches()
  {
    return numberOfMatches;
  }

  public void setNumberOfMatches( int numberOfMatches )
  {
    this.numberOfMatches = numberOfMatches;
  }

  public String toString()
  {
    StringBuilder sb = new StringBuilder();
    sb.append( "Team ID: " + ( null != competitor ? String.valueOf( competitor.getId() ) : "null" ) + "\n" );
    sb.append( "number of matches: " + numberOfMatches );
    return sb.toString();
  }
}
