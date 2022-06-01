
package com.biperf.core.ui.profile;

import com.biperf.core.domain.gamification.GamificationBadgeTileView;
import com.biperf.core.ui.engagement.EngagementView;
import com.biperf.core.ui.workhappier.WhPastResultsViewBean;
import com.biperf.core.value.BudgetMeter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude( Include.NON_NULL )
public class ProfileRPMView
{

  @JsonProperty( "budgets" )
  private BudgetMeter budgetMeter;
  @JsonProperty( "badges" )
  private GamificationBadgeTileView gamificationBadgeTileView;
  @JsonProperty( "dashboard" )
  private EngagementView engagementView;
  @JsonProperty( "workHappier" )
  private WhPastResultsViewBean whPastResultsViewBean;

  public EngagementView getEngagementView()
  {
    return engagementView;
  }

  public void setEngagementView( EngagementView engagementView )
  {
    this.engagementView = engagementView;
  }

  public GamificationBadgeTileView getGamificationBadgeTileView()
  {
    return gamificationBadgeTileView;
  }

  public void setGamificationBadgeTileView( GamificationBadgeTileView gamificationBadgeTileView )
  {
    this.gamificationBadgeTileView = gamificationBadgeTileView;
  }

  public WhPastResultsViewBean getWhPastResultsViewBean()
  {
    return whPastResultsViewBean;
  }

  public void setWhPastResultsViewBean( WhPastResultsViewBean whPastResultsViewBean )
  {
    this.whPastResultsViewBean = whPastResultsViewBean;
  }

  public BudgetMeter getBudgetMeter()
  {
    return budgetMeter;
  }

  public void setBudgetMeter( BudgetMeter budgetMeter )
  {
    this.budgetMeter = budgetMeter;
  }

}
