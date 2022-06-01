
package com.biperf.core.ui.engagement;

/**
 * 
 * EngagementIndividualsView.
 * 
 * @author kandhi
 * @since Jul 31, 2014
 * @version 1.0
 */
public class EngagementIndividualsView
{
  private EngagementTeamIndividualsView individuals;

  public EngagementIndividualsView( EngagementTeamIndividualsView individuals )
  {
    super();
    this.individuals = individuals;
  }

  public EngagementTeamIndividualsView getIndividuals()
  {
    return individuals;
  }

  public void setIndividuals( EngagementTeamIndividualsView individuals )
  {
    this.individuals = individuals;
  }

}
