
package com.biperf.core.ui.engagement;

/**
 * 
 * EngagementTeamsView.
 * 
 * @author kandhi
 * @since Jul 31, 2014
 * @version 1.0
 */
public class EngagementTeamsView
{
  private EngagementTeamTeamsView teams;

  public EngagementTeamsView( EngagementTeamTeamsView teams )
  {
    super();
    this.teams = teams;
  }

  public EngagementTeamTeamsView getTeams()
  {
    return teams;
  }

  public void setTeams( EngagementTeamTeamsView teams )
  {
    this.teams = teams;
  }

}
