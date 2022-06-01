
package com.biperf.core.ui.engagement;

/**
 * 
 * EngagementTeamView.
 * 
 * @author kandhi
 * @since May 20, 2014
 * @version 1.0
 */
public class EngagementTeamView
{
  private EngagementTeamModelView model;
  private EngagementTeamIndividualsView individuals;
  private EngagementTeamTeamsView teams;

  public EngagementTeamView( EngagementTeamModelView model, EngagementTeamIndividualsView individuals, EngagementTeamTeamsView teams )
  {
    super();
    this.model = model;
    this.individuals = individuals;
    this.teams = teams;
  }

  public EngagementTeamModelView getModel()
  {
    return model;
  }

  public void setModel( EngagementTeamModelView model )
  {
    this.model = model;
  }

  public EngagementTeamIndividualsView getIndividuals()
  {
    return individuals;
  }

  public void setIndividuals( EngagementTeamIndividualsView individuals )
  {
    this.individuals = individuals;
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
