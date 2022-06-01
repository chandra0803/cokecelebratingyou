
package com.biperf.core.ui.ssi.view;

import com.biperf.core.domain.ssi.SSIContest;
import com.biperf.core.utils.SSIContestUtil;
import com.biperf.core.value.ssi.SSIContestPayoutStackRankTotalsValueBean;

/**
 * SSIContestStackRankPreviewView.
 * 
 * @author dudam
 * @since Feb 16, 2015
 * @version 1.0
 */
public class SSIContestStackRankPreviewView extends SSIContestPreviewView
{
  private String activityDescription;
  private String goal;
  private String maximumPayout;
  private Long rankCount;
  private Boolean includeMinimumQualifier;
  private String minimumQualifier;
  private String stackRankingOrder;

  public SSIContestStackRankPreviewView( SSIContestPreviewViewWrapper contestPreviewViewWrapper, SSIContestPayoutStackRankTotalsValueBean valueBean )
  {
    super( contestPreviewViewWrapper );
    SSIContest contest = contestPreviewViewWrapper.getContest();
    int decimalPrecision = SSIContestUtil.getPrecision( contestPreviewViewWrapper.getContest().getActivityMeasureType().getCode() );
    if ( contest.getContestGoal() != null )
    {
      this.goal = SSIContestUtil.getActivityPrefix( contestPreviewViewWrapper.getContestValueBean() ) + SSIContestUtil.getFormattedValue( contest.getContestGoal(), decimalPrecision );
    }
    this.activityDescription = contest.getActivityDescription();
    this.includeMinimumQualifier = contest.getIncludeStackRankQualifier();
    this.minimumQualifier = SSIContestUtil.getActivityPrefix( contestPreviewViewWrapper.getContestValueBean() )
        + SSIContestUtil.getFormattedValue( contest.getStackRankQualifierAmount(), decimalPrecision );
    this.stackRankingOrder = contest.getStackRankOrder();
    this.maximumPayout = SSIContestUtil.getPayoutPrefix( contestPreviewViewWrapper.getContestValueBean() ) + SSIContestUtil.getFormattedValue( valueBean.getTotalPayout(), 0 )
        + SSIContestUtil.getPayoutSuffix( contestPreviewViewWrapper.getContestValueBean() );
    this.rankCount = valueBean.getTotalRanks();
  }

  public String getActivityDescription()
  {
    return activityDescription;
  }

  public void setActivityDescription( String activityDescription )
  {
    this.activityDescription = activityDescription;
  }

  public String getGoal()
  {
    return goal;
  }

  public void setGoal( String goal )
  {
    this.goal = goal;
  }

  public String getMaximumPayout()
  {
    return maximumPayout;
  }

  public void setMaximumPayout( String maximumPayout )
  {
    this.maximumPayout = maximumPayout;
  }

  public Long getRankCount()
  {
    return rankCount;
  }

  public void setRankCount( Long rankCount )
  {
    this.rankCount = rankCount;
  }

  public Boolean getIncludeMinimumQualifier()
  {
    return includeMinimumQualifier;
  }

  public void setIncludeMinimumQualifier( Boolean includeMinimumQualifier )
  {
    this.includeMinimumQualifier = includeMinimumQualifier;
  }

  public String getMinimumQualifier()
  {
    return minimumQualifier;
  }

  public void setMinimumQualifier( String minimumQualifier )
  {
    this.minimumQualifier = minimumQualifier;
  }

  public String getStackRankingOrder()
  {
    return stackRankingOrder;
  }

  public void setStackRankingOrder( String stackRankingOrder )
  {
    this.stackRankingOrder = stackRankingOrder;
  }

}
