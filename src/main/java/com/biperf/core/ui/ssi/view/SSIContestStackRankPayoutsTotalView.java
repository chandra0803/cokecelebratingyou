
package com.biperf.core.ui.ssi.view;

import java.util.List;

import com.biperf.core.domain.ssi.SSIContest;
import com.biperf.core.value.ssi.SSIContestPayoutsValueBean;
import com.biperf.core.value.ssi.SSIContestStackRankPayoutValueBean;

public class SSIContestStackRankPayoutsTotalView extends SSIContestPayoutsTotalView
{
  private String totalActivity;

  // StackRank points
  private String totalPayout;
  // StackRank other
  private String totalPayoutValue;

  // StackRank Tied
  private String payoutCap;
  private boolean hasTie = false;
  private List<SSIContestStackRankPayoutValueBean> payouts;
  private String currencySymbol;

  public SSIContestStackRankPayoutsTotalView( SSIContest contest,
                                              SSIContestPayoutsValueBean ssiContestPayouts,
                                              String contestName,
                                              boolean hasQualifiedPayouts,
                                              boolean includeSubmitClaim,
                                              String activityPrefix,
                                              String payoutPrefix,
                                              String payoutSuffix,
                                              List<SSIContestStackRankPayoutValueBean> payouts )
  {
    super( contest, ssiContestPayouts, contestName, hasQualifiedPayouts, includeSubmitClaim );
    populateContestPayoutsTotal( ssiContestPayouts, activityPrefix, payoutPrefix, payoutSuffix );
    this.payouts = payouts;
  }

  public void populateContestPayoutsTotal( SSIContestPayoutsValueBean ssiContestPayouts, String activityPrefix, String payoutPrefix, String payoutSuffix )
  {
    this.totalActivity = activityPrefix + ssiContestPayouts.getTotalActivity();
    if ( isPayoutInPoints() )
    {
      this.totalPayout = ssiContestPayouts.getTotalPayout() + payoutSuffix;
    }
    else
    {
      if ( !ssiContestPayouts.isHasTie() )
      {
        this.totalPayoutValue = payoutPrefix + ssiContestPayouts.getTotalPayoutValue();
      }
    }
    if ( ssiContestPayouts.isHasTie() )
    {
      this.hasTie = true;
      this.payoutCap = payoutPrefix + ssiContestPayouts.getPayoutCap();
      if ( isPayoutInOther() )
      {
        this.currencySymbol = payoutPrefix;
        this.totalPayoutValue = ssiContestPayouts.getTotalPayoutValue();
      }
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

  public String getPayoutCap()
  {
    return payoutCap;
  }

  public void setPayoutCap( String payoutCap )
  {
    this.payoutCap = payoutCap;
  }

  public boolean isHasTie()
  {
    return hasTie;
  }

  public void setHasTie( boolean hasTie )
  {
    this.hasTie = hasTie;
  }

  public List<SSIContestStackRankPayoutValueBean> getPayouts()
  {
    return payouts;
  }

  public void setPayouts( List<SSIContestStackRankPayoutValueBean> payouts )
  {
    this.payouts = payouts;
  }

  public String getCurrencySymbol()
  {
    return currencySymbol;
  }

  public void setCurrencySymbol( String currencySymbol )
  {
    this.currencySymbol = currencySymbol;
  }

}
