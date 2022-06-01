
package com.biperf.core.ui.ssi.view;

import java.util.ArrayList;
import java.util.List;

import com.biperf.core.domain.enums.SSIIndividualBaselineType;
import com.biperf.core.domain.ssi.SSIContest;
import com.biperf.core.domain.ssi.SSIContestLevel;
import com.biperf.core.utils.SSIContestUtil;

public class SSIContestStepItUpPreviewView extends SSIContestPreviewView
{
  private String activityDescription;
  private boolean includeStackRanking;
  private boolean includeBonus;
  private String bonus;
  private String individualBonusCap;
  private String maximumPotential;
  private String goal;
  private String maximumPayout;
  private String maximumPayoutBonus;
  private List<SSIContestLevelView> contestLevels;

  public SSIContestStepItUpPreviewView( SSIContestPreviewViewWrapper wrapper, Double totalBaseLineAmount )
  {
    super( wrapper );
    SSIContest contest = wrapper.getContest();

    String activityPrefix = SSIContestUtil.getActivityPrefix( wrapper.getContestValueBean() );
    String payoutPrefix = SSIContestUtil.getPayoutPrefix( wrapper.getContestValueBean() );
    String payoutSuffix = SSIContestUtil.getPayoutSuffix( wrapper.getContestValueBean() );
    int precision = SSIContestUtil.getPrecision( contest.getActivityMeasureType().getCode() );

    this.setIncludeStackRanking( wrapper.getContest().isIncludeStackRank() );
    this.setActivityDescription( contest.getActivityDescription() );
    this.setIncludeStackRanking( contest.isIncludeStackRank() );
    this.setIncludeBonus( contest.isIncludeBonus() );
    this.setBonus( wrapper.getBonusRow() );
    List<SSIContestLevel> levels = wrapper.getSsiContestLevels();

    Double calculated = new Double( 0 );
    if ( SSIIndividualBaselineType.CURRENCY_OVER_BASELINE_CODE.equals( contest.getIndividualBaselineType().getCode() ) )
    {
      calculated = totalBaseLineAmount + SSIContestUtil.getHighestLevelGoalAmount( levels ) * wrapper.getParticipantsCount();
    }
    else if ( SSIIndividualBaselineType.PERCENTAGE_OVER_BASELINE_CODE.equals( contest.getIndividualBaselineType().getCode() ) )
    {
      calculated = totalBaseLineAmount + totalBaseLineAmount / 100 * SSIContestUtil.getHighestLevelGoalAmount( levels );
    }
    else
    {
      calculated = SSIContestUtil.getHighestLevelGoalAmount( levels ) != null ? SSIContestUtil.getHighestLevelGoalAmount( levels ) * wrapper.getParticipantsCount() : 0;
    }
    this.maximumPotential = activityPrefix + SSIContestUtil.getFormattedValue( calculated, precision );
    this.goal = activityPrefix + SSIContestUtil.getFormattedValue( contest.getContestGoal(), precision );
    if ( wrapper.getSsiContestLevels() != null )
    {
      Long maximumPayout = wrapper.getParticipantsCount() * SSIContestUtil.getHighestLevelPayoutAmount( levels );
      this.maximumPayout = payoutPrefix + SSIContestUtil.getFormattedValue( maximumPayout, 0 ) + payoutSuffix;
      if ( contest.isIncludeBonus() )
      {
        Long maxPayoutWithBonus = maximumPayout + contest.getStepItUpBonusCap() * wrapper.getParticipantsCount();
        this.maximumPayoutBonus = SSIContestUtil.getFormattedValue( maxPayoutWithBonus, 0 ) + payoutSuffix;
        this.individualBonusCap = SSIContestUtil.getFormattedValue( contest.getStepItUpBonusCap(), 0 ) + payoutSuffix;
      }
      contestLevels = new ArrayList<SSIContestLevelView>();
      for ( SSIContestLevel ssiContestLevel : wrapper.getSsiContestLevels() )
      {
        this.contestLevels.add( new SSIContestLevelView( ssiContestLevel,
                                                         contest,
                                                         activityPrefix,
                                                         precision,
                                                         payoutPrefix,
                                                         payoutSuffix,
                                                         ssiContestLevel.getSequenceNumber().equals( 1 ),
                                                         ssiContestLevel.getSequenceNumber().equals( wrapper.getSsiContestLevels().size() ) ) );
      }
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

  public boolean isIncludeStackRanking()
  {
    return includeStackRanking;
  }

  public void setIncludeStackRanking( boolean includeStackRanking )
  {
    this.includeStackRanking = includeStackRanking;
  }

  public boolean isIncludeBonus()
  {
    return includeBonus;
  }

  public void setIncludeBonus( boolean includeBonus )
  {
    this.includeBonus = includeBonus;
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

  public List<SSIContestLevelView> getContestLevels()
  {
    return contestLevels;
  }

  public void setContestLevels( List<SSIContestLevelView> contestLevels )
  {
    this.contestLevels = contestLevels;
  }

}
