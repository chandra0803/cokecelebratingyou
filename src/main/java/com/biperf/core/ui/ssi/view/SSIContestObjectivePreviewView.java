
package com.biperf.core.ui.ssi.view;

import java.text.MessageFormat;

import com.biperf.core.domain.ssi.SSIContest;
import com.biperf.core.utils.SSIContestUtil;
import com.biperf.core.value.ssi.SSIContestUniqueCheckValueBean;
import com.objectpartners.cms.util.CmsResourceBundle;
import com.objectpartners.cms.util.ContentReaderManager;

/**
 * 
 * SSIContestPayoutObjectivePreviewView.
 * 
 * @author patelp
 * @since Dec 30, 2014
 * @version 1.0
 */

public class SSIContestObjectivePreviewView extends SSIContestPreviewView
{

  private String activityDescription;
  private String goal;
  private boolean includeBonus;
  private String bonus;
  private boolean optionShowBadges;
  private String individualBonusCap;
  private String maximumPayoutBonus;
  private String objectiveAmount;
  private String payoutAmount;
  private String maximumPayout;
  private String payoutDescription;
  private SSIPaxContestBadgeView badge;
  private String maximumPotential;
  private boolean includeStackRanking;

  public SSIContestObjectivePreviewView( SSIContestPreviewViewWrapper wrapper )
  {
    super( wrapper );
    int decimalPrecision = SSIContestUtil.getPrecision( wrapper.getContest().getActivityMeasureType().getCode() );
    String activityPrefix = SSIContestUtil.getActivityPrefix( wrapper.getContestValueBean() );
    String payoutPrefix = SSIContestUtil.getPayoutPrefix( wrapper.getContestValueBean() );
    String payoutSuffix = SSIContestUtil.getPayoutSuffix( wrapper.getContestValueBean() );

    this.includeStackRanking = wrapper.getContest().isIncludeStackRank();
    /*
     * if ( wrapper.getContestPayoutTotalsValueBean().getObjectiveAmountTotal() != null ) {
     * this.objectiveAmount = SSIContestUtil.getActivityPrefix( wrapper.getContestValueBean() ) +
     * SSIContestUtil.getFormattedValue(
     * wrapper.getContestPayoutTotalsValueBean().getObjectiveAmountTotal(), decimalPrecision ); }
     */
    if ( wrapper.getContestPayoutTotalsValueBean().getMaxPotential() != null )
    {
      this.maximumPotential = activityPrefix + SSIContestUtil.getFormattedValue( wrapper.getContestPayoutTotalsValueBean().getMaxPotential(), decimalPrecision );
    }
    if ( wrapper.getContestPayoutTotalsValueBean().getObjectivePayoutTotal() != null )
    {
      this.payoutAmount = SSIContestUtil.getPayoutPrefix( wrapper.getContestValueBean() ) + SSIContestUtil.getFormattedValue( wrapper.getContestPayoutTotalsValueBean().getObjectivePayoutTotal(), 0 )
          + payoutSuffix;
    }
    if ( wrapper.getContestPayoutTotalsValueBean().getMaxPayout() != null )
    {
      this.maximumPayout = SSIContestUtil.getPayoutPrefix( wrapper.getContestValueBean() ) + SSIContestUtil.getFormattedValue( wrapper.getContestPayoutTotalsValueBean().getMaxPayout(), 0 )
          + payoutSuffix;
    }
    if ( wrapper.getContest().isIncludeBonus() && wrapper.getContestPayoutTotalsValueBean().getMaxPayoutWithBonus() != null )
    {
      this.maximumPayoutBonus = SSIContestUtil.getPayoutPrefix( wrapper.getContestValueBean() )
          + SSIContestUtil.getFormattedValue( wrapper.getContestPayoutTotalsValueBean().getMaxPayoutWithBonus(), 0 ) + payoutSuffix;
    }
    this.setActivityMeasuredIn( wrapper.getContest().getActivityMeasureType() != null ? wrapper.getContest().getActivityMeasureType().getCode() : null );
    this.setActivityMeasuredName( wrapper.getContest().getActivityMeasureType() != null ? wrapper.getContest().getActivityMeasureType().getName() : null );
    this.goal = activityPrefix + SSIContestUtil.getFormattedValue( wrapper.getContest().getContestGoal(), decimalPrecision );
    this.setIncludeBonus( wrapper.getContest().isIncludeBonus() );
    this.setPayoutType( wrapper.getContest().getPayoutType() != null ? wrapper.getContest().getPayoutType().getCode() : null );
    this.setPayoutTypeName( wrapper.getContest().getPayoutType() != null ? wrapper.getContest().getPayoutType().getName() : null );
    this.setOptionShowBadges( wrapper.getContest().getPromotion().getBadge() != null ? true : false );
    this.setBadge( new SSIPaxContestBadgeView( wrapper.getContest().getBadgeRule() ) );
    // populate unique payout unique values
    setUniqueValues( wrapper, decimalPrecision, activityPrefix, payoutPrefix );
  }

  public void setUniqueValues( SSIContestPreviewViewWrapper wrapper, int decimalPrecision, String activityPrefix, String payoutPrefix )
  {
    SSIContestUniqueCheckValueBean contestUniqueCheckValueBean = wrapper.getContestUniqueCheckValueBean();
    SSIContest contest = wrapper.getContest();
    String variesByPax = CmsResourceBundle.getCmsBundle().getString( "ssi_contest.approvals.summary.VARIES_BY_PARTICIPANT" );

    // Activity Description
    if ( contest.getSameObjectiveDescription() )
    {
      this.setActivityDescription( contest.getActivityDescription() );
    }
    else if ( contestUniqueCheckValueBean.isActivityDescSame() )
    {
      this.setActivityDescription( contestUniqueCheckValueBean.getActivityDesc() );
    }
    else
    {
      this.setActivityDescription( variesByPax );
    }

    // Objective Amount
    this.objectiveAmount = contestUniqueCheckValueBean.isAmountSame()
        ? SSIContestUtil.getActivityPrefix( wrapper.getContestValueBean() ) + SSIContestUtil.getFormattedValue( contestUniqueCheckValueBean.getAmount(), decimalPrecision )
        : variesByPax;

    // Bonus Cap & Bonus Amount
    if ( contest.getPayoutType().isPoints() )
    {
      if ( contestUniqueCheckValueBean.isBonusCapSame() )
      {
        if ( contestUniqueCheckValueBean.getBonusCap() != null )
        {
          this.setIndividualBonusCap( SSIContestUtil.getFormattedValue( contestUniqueCheckValueBean.getBonusCap(), 0 ) + SSIContestUtil.getPayoutSuffix( wrapper.getContestValueBean() ) );
        }
      }
      else
      {
        this.setIndividualBonusCap( variesByPax );
      }
      if ( contestUniqueCheckValueBean.isBonusSame() )
      {
        this.bonus = MessageFormat.format( ContentReaderManager.getText( "ssi_contest.approvals.summary", "BONUS_ROW" ),
                                           new Object[] { activityPrefix + SSIContestUtil.getFormattedValue( contestUniqueCheckValueBean.getBonusIncrement(), 0 ),
                                                          SSIContestUtil.getFormattedValue( contestUniqueCheckValueBean.getBonusPayout(), 0 ) } );
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
        ? payoutPrefix + SSIContestUtil.getFormattedValue( contestUniqueCheckValueBean.getPayout(), SSIContestUtil.PAYOUT_DECIMAL_PRECISION )
            + SSIContestUtil.getPayoutSuffix( wrapper.getContestValueBean() )
        : variesByPax;
  }

  public String getActivityDescription()
  {
    return activityDescription;
  }

  public void setActivityDescription( String activityDescription )
  {
    this.activityDescription = activityDescription;
  }

  public boolean isOptionShowBadges()
  {
    return optionShowBadges;
  }

  public void setOptionShowBadges( boolean optionShowBadges )
  {
    this.optionShowBadges = optionShowBadges;
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

  public String getMaximumPotential()
  {
    return maximumPotential;
  }

  public void setMaximumPotential( String maximumPotential )
  {
    this.maximumPotential = maximumPotential;
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

  public String getMaximumPayoutBonus()
  {
    return maximumPayoutBonus;
  }

  public void setMaximumPayoutBonus( String maximumPayoutBonus )
  {
    this.maximumPayoutBonus = maximumPayoutBonus;
  }

  public boolean isIncludeBonus()
  {
    return includeBonus;
  }

  public void setIncludeBonus( boolean includeBonus )
  {
    this.includeBonus = includeBonus;
  }

  public boolean isIncludeStackRanking()
  {
    return includeStackRanking;
  }

  public void setIncludeStackRanking( boolean includeStackRanking )
  {
    this.includeStackRanking = includeStackRanking;
  }

}
