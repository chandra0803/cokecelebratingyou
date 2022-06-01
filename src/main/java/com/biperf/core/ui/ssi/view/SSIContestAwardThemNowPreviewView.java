
package com.biperf.core.ui.ssi.view;

import com.biperf.core.domain.ssi.SSIContest;
import com.biperf.core.utils.SSIContestUtil;
import com.biperf.core.value.ssi.SSIContestUniqueCheckValueBean;
import com.objectpartners.cms.util.CmsResourceBundle;

/**
 * 
 * SSIContestPayoutObjectivePreviewView.
 * 
 * @author patelp
 * @since Feb 11, 2015
 * @version 1.0
 */

public class SSIContestAwardThemNowPreviewView extends SSIContestPreviewView
{
  private String message;
  private String activityDescription;
  private String objectiveAmount;
  private String payoutAmount;
  private String payoutDescription;
  private SSIPaxContestBadgeView badge;

  public SSIContestAwardThemNowPreviewView( SSIContestPreviewViewWrapper contestPreviewViewWrapper )
  {
    super( contestPreviewViewWrapper );
    this.message = contestPreviewViewWrapper.getMessage();
    this.setBadge( new SSIPaxContestBadgeView( contestPreviewViewWrapper.getContest().getBadgeRule() ) );
    int decimalPrecision = SSIContestUtil.getPrecision( contestPreviewViewWrapper.getContestValueBean().getActivityMeasureType().getCode() );
    if ( contestPreviewViewWrapper.getContestPayoutTotalsValueBean().getObjectiveAmountTotal() != null )
    {
      this.objectiveAmount = SSIContestUtil.getActivityPrefix( contestPreviewViewWrapper.getContestValueBean() )
          + SSIContestUtil.getFormattedValue( contestPreviewViewWrapper.getContestPayoutTotalsValueBean().getObjectiveAmountTotal(), decimalPrecision );

    }
    if ( contestPreviewViewWrapper.getContestPayoutTotalsValueBean().getObjectivePayoutTotal() != null )
    {

      this.payoutAmount = SSIContestUtil.getPayoutPrefix( contestPreviewViewWrapper.getContestValueBean() )
          + SSIContestUtil.getFormattedValue( contestPreviewViewWrapper.getContestPayoutTotalsValueBean().getObjectivePayoutTotal(), 0 )
          + SSIContestUtil.getPayoutSuffix( contestPreviewViewWrapper.getContestValueBean() );
    }

    setUniqueValues( contestPreviewViewWrapper.getContestUniqueCheckValueBean(), contestPreviewViewWrapper.getContest() );

  }

  public void setUniqueValues( SSIContestUniqueCheckValueBean contestUniqueCheckValueBean, SSIContest contest )
  {
    String variesByPax = CmsResourceBundle.getCmsBundle().getString( "ssi_contest.approvals.summary.VARIES_BY_PARTICIPANT" );
    // Activity Description
    if ( contestUniqueCheckValueBean.isActivityDescSame() )
    {
      this.setActivityDescription( contestUniqueCheckValueBean.getActivityDesc() );
    }
    else
    {
      this.setActivityDescription( variesByPax );
    }

    // Activity Description
    if ( contestUniqueCheckValueBean.isPayoutDescSame() )
    {
      this.setPayoutDescription( contestUniqueCheckValueBean.getPayoutDesc() );
    }
    else
    {
      this.setPayoutDescription( variesByPax );
    }

  }

  public String getActivityDescription()
  {
    return activityDescription;
  }

  public void setActivityDescription( String activityDescription )
  {
    this.activityDescription = activityDescription;
  }

  public String getObjectiveAmount()
  {
    return objectiveAmount;
  }

  public void setObjectiveAmount( String objectiveAmount )
  {
    this.objectiveAmount = objectiveAmount;
  }

  public String getPayoutAmount()
  {
    return payoutAmount;
  }

  public void setPayoutAmount( String payoutAmount )
  {
    this.payoutAmount = payoutAmount;
  }

  public String getPayoutDescription()
  {
    return payoutDescription;
  }

  public void setPayoutDescription( String payoutDescription )
  {
    this.payoutDescription = payoutDescription;
  }

  public SSIPaxContestBadgeView getBadge()
  {
    return badge;
  }

  public void setBadge( SSIPaxContestBadgeView badge )
  {
    this.badge = badge;
  }

  public String getMessage()
  {
    return message;
  }

  public void setMessage( String message )
  {
    this.message = message;
  }

}
