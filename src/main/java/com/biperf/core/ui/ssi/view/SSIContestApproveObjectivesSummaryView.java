
package com.biperf.core.ui.ssi.view;

import java.text.MessageFormat;

import com.biperf.core.domain.enums.SSIPayoutType;
import com.biperf.core.domain.ssi.SSIContest;
import com.biperf.core.utils.SSIContestUtil;
import com.biperf.core.value.ssi.SSIContestPayoutObjectivesTotalsValueBean;
import com.biperf.core.value.ssi.SSIContestUniqueCheckValueBean;
import com.biperf.core.value.ssi.SSIContestValueBean;
import com.objectpartners.cms.util.ContentReaderManager;

/**
 * @author dudam
 * @since Dec 17, 2014
 * @version 1.0
 */
public class SSIContestApproveObjectivesSummaryView extends SSIContestApproveSummaryView
{
  private boolean includeBonus;
  private String bonus;
  private String individualBonusCap;
  private SSIPaxContestBadgeView badge;
  private String goal;
  private String objectiveAmount;
  private String payoutAmount;
  private String maximumPayout;
  private String maximumPayoutBonus;
  private String payoutDescription;
  private String maximumPotential;

  public SSIContestApproveObjectivesSummaryView( SSIContest contest, SSIContestValueBean valueBean )
  {
    super( contest, valueBean );
    int decimalPrecision = SSIContestUtil.getPrecision( valueBean.getActivityMeasureType().getCode() );

    this.includeBonus = contest.isIncludeBonus();
    this.badge = new SSIPaxContestBadgeView( contest.getBadgeRule() );
    if ( contest.getContestGoal() != null )
    {
      this.goal = SSIContestUtil.getActivityPrefix( valueBean ) + SSIContestUtil.getFormattedValue( contest.getContestGoal(), decimalPrecision );
    }
  }

  public void setUniqueValues( SSIContestUniqueCheckValueBean contestUniqueCheckValueBean, SSIContestValueBean valueBean, String variesByPax, SSIContest contest )
  {
    int decimalPrecision = SSIContestUtil.getPrecision( valueBean.getActivityMeasureType().getCode() );
    String activityPrefix = SSIContestUtil.getActivityPrefix( valueBean );
    String payoutPrefix = SSIContestUtil.getPayoutPrefix( valueBean );
    String payoutSuffix = SSIContestUtil.getPayoutSuffix( valueBean );

    // Activity Description
    if ( contest.getSameObjectiveDescription() )
    {
      this.setActivityDescription( contest.getActivityDescription() );
    }
    else
    {
      this.setActivityDescription( contestUniqueCheckValueBean.isActivityDescSame() ? contestUniqueCheckValueBean.getActivityDesc() : variesByPax );
    }

    // Objective Amount
    this.objectiveAmount = contestUniqueCheckValueBean.isAmountSame() ? activityPrefix + SSIContestUtil.getFormattedValue( contestUniqueCheckValueBean.getAmount(), decimalPrecision ) : variesByPax;

    // Bonus cap and bonus amount
    if ( SSIPayoutType.POINTS_CODE.equals( valueBean.getPayoutType().getCode() ) )
    {
      this.individualBonusCap = contestUniqueCheckValueBean.isBonusCapSame() ? SSIContestUtil.getFormattedValue( contestUniqueCheckValueBean.getBonusCap(), 0 ) + payoutSuffix : variesByPax;
      if ( contestUniqueCheckValueBean.isBonusSame() )
      {
        this.bonus = MessageFormat.format( ContentReaderManager.getText( "ssi_contest.approvals.summary", "BONUS_ROW" ),
                                           new Object[] { activityPrefix + contestUniqueCheckValueBean.getBonusIncrement(), contestUniqueCheckValueBean.getBonusPayout() } );
      }
      else
      {
        this.bonus = variesByPax;
      }
    }

    // Pay out Description
    this.payoutDescription = contestUniqueCheckValueBean.isPayoutDescSame() ? contestUniqueCheckValueBean.getPayoutDesc() : variesByPax;

    // Pay out Amount
    this.payoutAmount = contestUniqueCheckValueBean.isPayoutSame()
        ? payoutPrefix + SSIContestUtil.getFormattedValue( contestUniqueCheckValueBean.getPayout(), SSIContestUtil.PAYOUT_DECIMAL_PRECISION ) + payoutSuffix
        : variesByPax;
  }

  public void setContestPayoutTotalsValues( SSIContestPayoutObjectivesTotalsValueBean contestPayoutTotalsValueBean, SSIContest contest, SSIContestValueBean valueBean )
  {
    int decimalPrecision = SSIContestUtil.getPrecision( contest.getActivityMeasureType().getCode() );
    if ( contestPayoutTotalsValueBean.getObjectiveAmountTotal() != null )
    {
      this.objectiveAmount = SSIContestUtil.getActivityPrefix( valueBean ) + SSIContestUtil.getFormattedValue( contestPayoutTotalsValueBean.getObjectiveAmountTotal(), decimalPrecision );
    }
    if ( contestPayoutTotalsValueBean.getMaxPotential() != null )
    {
      this.maximumPotential = SSIContestUtil.getActivityPrefix( valueBean ) + SSIContestUtil.getFormattedValue( contestPayoutTotalsValueBean.getMaxPotential(), decimalPrecision );
    }
    if ( contestPayoutTotalsValueBean.getObjectivePayoutTotal() != null )
    {
      this.payoutAmount = SSIContestUtil.getPayoutPrefix( valueBean ) + SSIContestUtil.getFormattedValue( contestPayoutTotalsValueBean.getObjectivePayoutTotal(), 0 )
          + SSIContestUtil.getPayoutSuffix( valueBean );
    }
    if ( contestPayoutTotalsValueBean.getMaxPayout() != null )
    {
      this.maximumPayout = SSIContestUtil.getPayoutPrefix( valueBean ) + SSIContestUtil.getFormattedValue( contestPayoutTotalsValueBean.getMaxPayout(), 0 )
          + SSIContestUtil.getPayoutSuffix( valueBean );
    }
    if ( contest.isIncludeBonus() && contestPayoutTotalsValueBean.getMaxPayoutWithBonus() != null )
    {
      this.maximumPayoutBonus = SSIContestUtil.getFormattedValue( contestPayoutTotalsValueBean.getMaxPayoutWithBonus(), 0 ) + SSIContestUtil.getPayoutSuffix( valueBean );
    }
  }

  public String getBonus()
  {
    return bonus;
  }

  public void setBonus( String bonus )
  {
    this.bonus = bonus;
  }

  public String getIndividualBonusCap()
  {
    return individualBonusCap;
  }

  public void setIndividualBonusCap( String individualBonusCap )
  {
    this.individualBonusCap = individualBonusCap;
  }

  public SSIPaxContestBadgeView getBadge()
  {
    return badge;
  }

  public void setBadge( SSIPaxContestBadgeView badge )
  {
    this.badge = badge;
  }

  public String getGoal()
  {
    return goal;
  }

  public void setGoal( String goal )
  {
    this.goal = goal;
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

  public String getMaximumPayout()
  {
    return maximumPayout;
  }

  public void setMaximumPayout( String maximumPayout )
  {
    this.maximumPayout = maximumPayout;
  }

  public String getMaximumPayoutBonus()
  {
    return maximumPayoutBonus;
  }

  public void setMaximumPayoutBonus( String maximumPayoutBonus )
  {
    this.maximumPayoutBonus = maximumPayoutBonus;
  }

  public String getPayoutDescription()
  {
    return payoutDescription;
  }

  public void setPayoutDescription( String payoutDescription )
  {
    this.payoutDescription = payoutDescription;
  }

  public String getMaximumPotential()
  {
    return maximumPotential;
  }

  public void setMaximumPotential( String maximumPotential )
  {
    this.maximumPotential = maximumPotential;
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
