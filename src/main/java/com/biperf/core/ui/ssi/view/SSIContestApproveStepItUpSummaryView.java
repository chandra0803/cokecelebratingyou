
package com.biperf.core.ui.ssi.view;

import java.util.ArrayList;
import java.util.List;

import com.biperf.core.domain.enums.SSIIndividualBaselineType;
import com.biperf.core.domain.ssi.SSIContest;
import com.biperf.core.domain.ssi.SSIContestLevel;
import com.biperf.core.utils.SSIContestUtil;
import com.biperf.core.value.ssi.SSIContestValueBean;
import com.objectpartners.cms.util.CmsResourceBundle;

/**
 * @author dudam
 * @since Dec 17, 2014
 * @version 1.0
 */
public class SSIContestApproveStepItUpSummaryView extends SSIContestApproveSummaryView
{

  private String activityMeasuredValue;
  private boolean includeBonus;
  private String individualBonusCap;
  private String bonus;
  private String bonusForEvery;
  private String bonusGoal;
  private String goal;
  private String maximumPayout;
  private String maximumPayoutBonus;
  private String maximumPotential;
  private List<SSIContestLevelView> contestLevels;
  private boolean individualBaseline;
  private static final String WHITESPACE = " ";

  public SSIContestApproveStepItUpSummaryView()
  {

  }

  public SSIContestApproveStepItUpSummaryView( SSIContest contest, List<SSIContestLevel> ssiContestLevels, SSIContestValueBean valueBean, Double totalBaseLineAmount )
  {
    super( contest, valueBean );

    String activityPrefix = SSIContestUtil.getActivityPrefix( valueBean );
    String payoutPrefix = SSIContestUtil.getPayoutPrefix( valueBean );
    String payoutSuffix = SSIContestUtil.getPayoutSuffix( valueBean );
    int precision = SSIContestUtil.getPrecision( valueBean.getActivityMeasureType().getCode() );

    this.goal = activityPrefix + SSIContestUtil.getFormattedValue( contest.getContestGoal(), precision );
    if ( contest.isIncludeBonus() )
    {
      StringBuffer bonusRow = new StringBuffer();
      bonusRow.append( CmsResourceBundle.getCmsBundle().getString( "ssi_contest.payout_stepitup.FOR_EVERY" ) );
      bonusRow.append( WHITESPACE );
      bonusRow.append( activityPrefix + SSIContestUtil.getFormattedValue( contest.getStepItUpBonusIncrement(), precision ) );
      bonusRow.append( WHITESPACE );
      bonusRow.append( CmsResourceBundle.getCmsBundle().getString( "ssi_contest.payout_stepitup.OVER_LEVEL" ) );
      bonusRow.append( WHITESPACE );
      bonusRow.append( ssiContestLevels.size() );
      bonusRow.append( WHITESPACE );
      bonusRow.append( CmsResourceBundle.getCmsBundle().getString( "ssi_contest.payout_stepitup.PAX_EARN" ) );
      bonusRow.append( WHITESPACE );
      bonusRow.append( payoutPrefix + SSIContestUtil.getFormattedValue( contest.getStepItUpBonusPayout(), SSIContestUtil.PAYOUT_DECIMAL_PRECISION ) + payoutSuffix );
      this.includeBonus = contest.isIncludeBonus();
      this.bonus = bonusRow.toString();
      this.individualBonusCap = SSIContestUtil.getFormattedValue( contest.getStepItUpBonusCap(), 0 ) + payoutSuffix;
    }
    Double calculated = new Double( 0 );
    if ( SSIIndividualBaselineType.CURRENCY_OVER_BASELINE_CODE.equals( contest.getIndividualBaselineType().getCode() ) )
    {
      calculated = totalBaseLineAmount + SSIContestUtil.getHighestLevelGoalAmount( ssiContestLevels ) * valueBean.getParticipantCount();
    }
    else if ( SSIIndividualBaselineType.PERCENTAGE_OVER_BASELINE_CODE.equals( contest.getIndividualBaselineType().getCode() ) )
    {
      calculated = totalBaseLineAmount + totalBaseLineAmount / 100 * SSIContestUtil.getHighestLevelGoalAmount( ssiContestLevels );
    }
    else
    {
      calculated = SSIContestUtil.getHighestLevelGoalAmount( ssiContestLevels ) * valueBean.getParticipantCount();
    }
    // maximum potential
    this.maximumPotential = activityPrefix + SSIContestUtil.getFormattedValue( calculated, precision );
    if ( ssiContestLevels != null )
    {
      if ( contestLevels == null )
      {
        contestLevels = new ArrayList<SSIContestLevelView>();
      }
      for ( SSIContestLevel ssiContestLevel : ssiContestLevels )
      {
        this.contestLevels.add( new SSIContestLevelView( ssiContestLevel,
                                                         contest,
                                                         activityPrefix,
                                                         precision,
                                                         payoutPrefix,
                                                         payoutSuffix,
                                                         ssiContestLevel.getSequenceNumber().equals( 1 ),
                                                         ssiContestLevel.getSequenceNumber().equals( ssiContestLevels.size() ) ) );
      }
      // maximumPayout & maximumPayoutBonus
      Long maximumPayoutCalculated = valueBean.getParticipantCount() * SSIContestUtil.getHighestLevelPayoutAmount( ssiContestLevels );
      this.maximumPayout = payoutPrefix + SSIContestUtil.getFormattedValue( maximumPayoutCalculated, 0 ) + payoutSuffix;
      if ( contest.isIncludeBonus() )
      {
        Long maxPayoutWithBonusCalculated = maximumPayoutCalculated + contest.getStepItUpBonusCap() * valueBean.getParticipantCount();
        this.maximumPayoutBonus = SSIContestUtil.getFormattedValue( maxPayoutWithBonusCalculated, 0 ) + payoutSuffix;
      }
    }
    this.individualBaseline = totalBaseLineAmount > 0 ? true : false;
  }

  public String getActivityMeasuredValue()
  {
    return activityMeasuredValue;
  }

  public void setActivityMeasuredValue( String activityMeasuredValue )
  {
    this.activityMeasuredValue = activityMeasuredValue;
  }

  public boolean isIncludeBonus()
  {
    return includeBonus;
  }

  public void setIncludeBonus( boolean includeBonus )
  {
    this.includeBonus = includeBonus;
  }

  public String getIndividualBonusCap()
  {
    return individualBonusCap;
  }

  public void setIndividualBonusCap( String individualBonusCap )
  {
    this.individualBonusCap = individualBonusCap;
  }

  public String getBonus()
  {
    return bonus;
  }

  public void setBonus( String bonus )
  {
    this.bonus = bonus;
  }

  public String getBonusForEvery()
  {
    return bonusForEvery;
  }

  public void setBonusForEvery( String bonusForEvery )
  {
    this.bonusForEvery = bonusForEvery;
  }

  public String getBonusGoal()
  {
    return bonusGoal;
  }

  public void setBonusGoal( String bonusGoal )
  {
    this.bonusGoal = bonusGoal;
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

  public String getMaximumPotential()
  {
    return maximumPotential;
  }

  public void setMaximumPotential( String maximumPotential )
  {
    this.maximumPotential = maximumPotential;
  }

  public List<SSIContestLevelView> getContestLevels()
  {
    return contestLevels;
  }

  public void setContestLevels( List<SSIContestLevelView> contestLevels )
  {
    this.contestLevels = contestLevels;
  }

  public boolean isIndividualBaseline()
  {
    return individualBaseline;
  }

  public void setIndividualBaseline( boolean individualBaseline )
  {
    this.individualBaseline = individualBaseline;
  }

}
