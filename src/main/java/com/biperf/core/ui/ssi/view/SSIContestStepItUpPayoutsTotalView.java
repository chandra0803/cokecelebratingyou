
package com.biperf.core.ui.ssi.view;

import com.biperf.core.domain.ssi.SSIContest;
import com.biperf.core.value.ssi.SSIContestPayoutsValueBean;

public class SSIContestStepItUpPayoutsTotalView extends SSIContestPayoutsTotalView
{
  private String totalActivity;

  // Step It up points
  private String totalLevelPayout;
  private String totalBonusPayout;
  private String totalPayout;

  // Step It up other
  private String totalPayoutValue;

  private boolean includeBonus = false;

  public SSIContestStepItUpPayoutsTotalView( SSIContest contest,
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

    this.totalActivity = activityPrefix + ssiContestPayouts.getTotalActivity();
    this.includeBonus = ssiContestPayouts.isIncludeBonus();
    if ( isPayoutInPoints() )
    {
      this.totalLevelPayout = payoutPrefix + ssiContestPayouts.getTotalLevelPayout() + payoutSuffix;
      this.totalBonusPayout = payoutPrefix + ssiContestPayouts.getTotalBonusPayout() + payoutSuffix;
      this.totalPayout = payoutPrefix + ssiContestPayouts.getTotalPayout() + payoutSuffix;
    }
    else
    {
      this.totalPayoutValue = payoutPrefix + ssiContestPayouts.getTotalPayoutValue() + payoutSuffix;
    }
  }

  public String getTotalActivity()
  {
    return totalActivity;
  }

  public void setTotalActivity( String totalActivity )
  {
    this.totalActivity = totalActivity;
  }

  public String getTotalLevelPayout()
  {
    return totalLevelPayout;
  }

  public void setTotalLevelPayout( String totalLevelPayout )
  {
    this.totalLevelPayout = totalLevelPayout;
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
