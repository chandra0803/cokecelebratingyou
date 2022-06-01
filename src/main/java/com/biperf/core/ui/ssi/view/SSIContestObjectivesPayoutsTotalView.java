
package com.biperf.core.ui.ssi.view;

import com.biperf.core.domain.ssi.SSIContest;
import com.biperf.core.value.ssi.SSIContestPayoutsValueBean;

public class SSIContestObjectivesPayoutsTotalView extends SSIContestPayoutsTotalView
{

  private String totalGoal;
  private String totalProgress;

  private boolean includeBonus = false;

  // Objectives points
  private String totalObjectivePayout;
  private String totalBonusPayout;
  private String totalPayout;

  // Objectives other
  private String totalPayoutValue;

  public SSIContestObjectivesPayoutsTotalView( SSIContest contest,
                                               SSIContestPayoutsValueBean ssiContestPayouts,
                                               String contestName,
                                               boolean hasQualifiedPayouts,
                                               boolean includeSubmitClaim,
                                               String activityPrefix,
                                               String payoutPrefix,
                                               String payoutSuffix )
  {
    super( contest, ssiContestPayouts, contestName, hasQualifiedPayouts, includeSubmitClaim );
    populateContestPayoutsTotal( ssiContestPayouts, activityPrefix, payoutPrefix, payoutSuffix );
  }

  public void populateContestPayoutsTotal( SSIContestPayoutsValueBean ssiContestPayouts, String activityPrefix, String payoutPrefix, String payoutSuffix )
  {
    this.totalGoal = activityPrefix + ssiContestPayouts.getTotalGoal();
    this.totalProgress = activityPrefix + ssiContestPayouts.getTotalProgress();
    this.includeBonus = ssiContestPayouts.isIncludeBonus();
    if ( isPayoutInPoints() )
    {
      this.totalObjectivePayout = payoutPrefix + ssiContestPayouts.getTotalObjectivePayout() + payoutSuffix;
      this.totalBonusPayout = payoutPrefix + ssiContestPayouts.getTotalBonusPayout() + payoutSuffix;
      this.totalPayout = payoutPrefix + ssiContestPayouts.getTotalPayout() + payoutSuffix;
    }
    else
    {
      this.totalPayoutValue = payoutPrefix + ssiContestPayouts.getTotalPayoutValue();
    }
  }

  public String getTotalGoal()
  {
    return totalGoal;
  }

  public void setTotalGoal( String totalGoal )
  {
    this.totalGoal = totalGoal;
  }

  public String getTotalProgress()
  {
    return totalProgress;
  }

  public void setTotalProgress( String totalProgress )
  {
    this.totalProgress = totalProgress;
  }

  public String getTotalObjectivePayout()
  {
    return totalObjectivePayout;
  }

  public void setTotalObjectivePayout( String totalObjectivePayout )
  {
    this.totalObjectivePayout = totalObjectivePayout;
  }

  public String getTotalBonusPayout()
  {
    return totalBonusPayout;
  }

  public void setTotalBonusPayout( String totalBonusPayout )
  {
    this.totalBonusPayout = totalBonusPayout;
  }

  public String getTotalPayout()
  {
    return totalPayout;
  }

  public void setTotalPayout( String totalPayout )
  {
    this.totalPayout = totalPayout;
  }

  public String getTotalPayoutValue()
  {
    return totalPayoutValue;
  }

  public void setTotalPayoutValue( String totalPayoutValue )
  {
    this.totalPayoutValue = totalPayoutValue;
  }

  public boolean isIncludeBonus()
  {
    return includeBonus;
  }

  public void setIncludeBonus( boolean includeBonus )
  {
    this.includeBonus = includeBonus;
  }

}
