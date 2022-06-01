
package com.biperf.core.ui.ssi.view;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.biperf.core.domain.ssi.SSIContestActivity;
import com.biperf.core.utils.SSIContestUtil;

public class SSIContestDoThisGetThatPreviewView extends SSIContestPreviewView
{
  private boolean includeStackRanking;
  private String totalMaxPayout;
  private List<SSIContestActivityView> activities;

  public SSIContestDoThisGetThatPreviewView( SSIContestPreviewViewWrapper ssiContestPreviewViewWrapper, List<SSIContestActivity> contestActivities )
  {
    super( ssiContestPreviewViewWrapper );
    this.includeStackRanking = ssiContestPreviewViewWrapper.getContest().isIncludeStackRank();
    this.totalMaxPayout = SSIContestUtil.getPayoutPrefix( ssiContestPreviewViewWrapper.getContestValueBean() )
        + SSIContestUtil.getFormattedValue( ssiContestPreviewViewWrapper.getTotalMaxPayout() * ssiContestPreviewViewWrapper.getParticipantsCount(), 0 );
    if ( contestActivities != null )
    {
      activities = new ArrayList<SSIContestActivityView>();
      for ( SSIContestActivity ssiContestActivity : contestActivities )
      {
        activities.add( new SSIContestActivityView( ssiContestPreviewViewWrapper.getContest(), ssiContestPreviewViewWrapper.getContestValueBean(), ssiContestActivity ) );
      }
      Collections.sort( activities );
    }
  }

  public boolean isIncludeStackRanking()
  {
    return includeStackRanking;
  }

  public void setIncludeStackRanking( boolean includeStackRanking )
  {
    this.includeStackRanking = includeStackRanking;
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
