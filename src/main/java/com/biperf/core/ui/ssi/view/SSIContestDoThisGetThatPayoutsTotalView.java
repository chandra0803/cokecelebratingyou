
package com.biperf.core.ui.ssi.view;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.biperf.core.domain.ssi.SSIContest;
import com.biperf.core.utils.SSIContestUtil;
import com.biperf.core.value.ssi.SSIContestDoThisGetThatActivityPayoutsTotalValueBean;
import com.biperf.core.value.ssi.SSIContestPayoutsValueBean;

public class SSIContestDoThisGetThatPayoutsTotalView extends SSIContestPayoutsTotalView
{
  private String updatedOnDate;
  private List<SSIContestDoThisGetThatActivityPayoutsTotalValueBean> activities = new ArrayList<SSIContestDoThisGetThatActivityPayoutsTotalValueBean>();

  public SSIContestDoThisGetThatPayoutsTotalView( SSIContest contest,
                                                  SSIContestPayoutsValueBean ssiContestPayouts,
                                                  String contestName,
                                                  boolean hasQualifiedPayouts,
                                                  boolean includeSubmitClaim,
                                                  String activityPrefix,
                                                  String payoutPrefix,
                                                  String payoutSuffix )
  {
    super( contest, ssiContestPayouts, contestName, hasQualifiedPayouts, includeSubmitClaim );
    this.updatedOnDate = SSIContestUtil.getContestProgressUpdateDate( contest );
    populateContestPayoutsTotal( ssiContestPayouts, activityPrefix, payoutPrefix, payoutSuffix );
  }

  public String getUpdatedOnDate()
  {
    return updatedOnDate;
  }

  public void setUpdatedOnDate( String updatedOnDate )
  {
    this.updatedOnDate = updatedOnDate;
  }

  public List<SSIContestDoThisGetThatActivityPayoutsTotalValueBean> getActivities()
  {
    return activities;
  }

  public void setActivities( List<SSIContestDoThisGetThatActivityPayoutsTotalValueBean> activities )
  {
    this.activities = activities;
  }

  public void populateContestPayoutsTotal( SSIContestPayoutsValueBean ssiContestPayouts )
  {
  }

  public void populateContestPayoutsTotal( SSIContestPayoutsValueBean ssiContestPayouts, String activityPrefix, String payoutPrefix, String payoutSuffix )
  {
    List<SSIContestDoThisGetThatActivityPayoutsTotalValueBean> dtgtActivityPayoutTotals = ssiContestPayouts.getDtgtActivityPayoutTotals();
    for ( Iterator iter = dtgtActivityPayoutTotals.iterator(); iter.hasNext(); )
    {
      SSIContestDoThisGetThatActivityPayoutsTotalValueBean payoutValueBean = (SSIContestDoThisGetThatActivityPayoutsTotalValueBean)iter.next();
      if ( payoutValueBean != null )
      {
        payoutValueBean.setForEvery( activityPrefix + payoutValueBean.getForEvery() );
        payoutValueBean.setWillEarn( payoutPrefix + payoutValueBean.getWillEarn() );
        payoutValueBean.setMinQualifier( activityPrefix + payoutValueBean.getMinQualifier() );
      }
      payoutValueBean.setTotalActivity( activityPrefix + payoutValueBean.getTotalActivity() );
      payoutValueBean.setQualifiedActivity( activityPrefix + payoutValueBean.getQualifiedActivity() );
      // added code to fix bug num 62261
      payoutValueBean.setTotalPayout( payoutValueBean.getTotalPayout() != null ? payoutPrefix + payoutValueBean.getTotalPayout() : null );
      payoutValueBean.setTotalPayoutValue( payoutValueBean.getTotalPayoutValue() != null ? payoutPrefix + payoutValueBean.getTotalPayoutValue() : null );
    }
    activities = dtgtActivityPayoutTotals;
  }

}
