
package com.biperf.core.ui.ssi.view;

import java.util.ArrayList;
import java.util.List;

import com.biperf.core.domain.ssi.SSIContest;
import com.biperf.core.domain.ssi.SSIContestActivity;
import com.biperf.core.utils.SSIContestUtil;
import com.biperf.core.value.ssi.SSIContestValueBean;

/**
 * @author dudam
 * @since Dec 17, 2014
 * @version 1.0
 */
public class SSIContestApproveDoThisGetThatSummaryView extends SSIContestApproveSummaryView
{
  private SSIPaxContestBadgeView badge;
  private String totalMaxPayout;
  private List<SSIContestActivityView> activities;

  public SSIContestApproveDoThisGetThatSummaryView()
  {

  }

  public SSIContestApproveDoThisGetThatSummaryView( SSIContest ssiContest, List<SSIContestActivity> contestActivities, SSIContestValueBean valueBean )
  {
    super( ssiContest, valueBean );
    this.badge = new SSIPaxContestBadgeView( ssiContest.getBadgeRule() );
    this.totalMaxPayout = SSIContestUtil.getPayoutPrefix( valueBean )
        + SSIContestUtil.getFormattedValue( valueBean.getPayoutSumDtgt() * valueBean.getParticipantCount(), SSIContestUtil.PAYOUT_DECIMAL_PRECISION );
    if ( contestActivities != null )
    {
      if ( activities == null )
      {
        activities = new ArrayList<SSIContestActivityView>();
      }
      for ( SSIContestActivity contestActivity : contestActivities )
      {
        this.activities.add( new SSIContestActivityView( ssiContest, valueBean, contestActivity ) );
      }
    }
  }

  public SSIPaxContestBadgeView getBadge()
  {
    return badge;
  }

  public void setBadge( SSIPaxContestBadgeView badge )
  {
    this.badge = badge;
  }

  public String getTotalMaxPayout()
  {
    return totalMaxPayout;
  }

  public void setTotalMaxPayout( String totalMaxPayout )
  {
    this.totalMaxPayout = totalMaxPayout;
  }

  public List<SSIContestActivityView> getActivities()
  {
    return activities;
  }

  public void setActivities( List<SSIContestActivityView> activities )
  {
    this.activities = activities;
  }

}
