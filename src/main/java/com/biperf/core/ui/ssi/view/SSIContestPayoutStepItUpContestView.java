
package com.biperf.core.ui.ssi.view;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import com.biperf.core.domain.currency.Currency;
import com.biperf.core.domain.ssi.SSIContest;
import com.biperf.core.domain.ssi.SSIContestLevel;
import com.biperf.core.ui.ssi.SSIContestLevelForm;
import com.biperf.core.utils.PageConstants;

/**
 * 
 * SSIContestPayoutStepItUpContestView.
 * 
 * @author kandhi
 * @since Jan 21, 2015
 * @version 1.0
 */
public class SSIContestPayoutStepItUpContestView extends SSIContestPayoutContestView
{

  private String activityDescription;
  private String measureOverBaseline;
  private boolean includeStackRanking;
  private boolean includeBonus;
  private String bonusForEvery;
  private String bonusPayout;
  private String bonusPayoutCap;
  private Double contestGoal;
  private int participantCount;
  private List<SSIContestLevelForm> levels;

  public SSIContestPayoutStepItUpContestView( SSIContest contest, String ssiContestClientState, List<Currency> currencies, String sysUrl, List<SSIContestBillCodeView> billCodes )
  {
    super( contest, ssiContestClientState, currencies, billCodes );
    setNextUrl( sysUrl + PageConstants.SSI_MANAGE_STEP_IT_UP );
    this.activityDescription = contest.getActivityDescription();
    this.measureOverBaseline = contest.getIndividualBaselineType() != null ? contest.getIndividualBaselineType().getCode() : null;
    this.includeStackRanking = contest.isIncludeStackRank();
    this.includeBonus = contest.isIncludeBonus();
    this.bonusForEvery = contest.getStepItUpBonusIncrement() != null ? String.valueOf( contest.getStepItUpBonusIncrement() ) : null;
    this.bonusPayout = contest.getStepItUpBonusPayout() != null ? String.valueOf( contest.getStepItUpBonusPayout() ) : null;
    this.bonusPayoutCap = contest.getStepItUpBonusCap() != null ? String.valueOf( contest.getStepItUpBonusCap() ) : null;
    this.levels = populateContestLevels( contest );
    this.contestGoal = contest.getContestGoal();
  }

  private List<SSIContestLevelForm> populateContestLevels( SSIContest contest )
  {
    levels = new ArrayList<SSIContestLevelForm>();
    Set<SSIContestLevel> contestLevels = contest.getContestLevels();
    for ( SSIContestLevel contestLevel : contestLevels )
    {
      levels.add( new SSIContestLevelForm( contestLevel, contest ) );
    }
    Collections.sort( levels );
    return levels;
  }

  public int getParticipantCount()
  {
    return participantCount;
  }

  public void setParticipantCount( int participantCount )
  {
    this.participantCount = participantCount;
  }

  public String getActivityDescription()
  {
    return activityDescription;
  }

  public void setActivityDescription( String activityDescription )
  {
    this.activityDescription = activityDescription;
  }

  public String getMeasureOverBaseline()
  {
    return measureOverBaseline;
  }

  public void setMeasureOverBaseline( String measureOverBaseline )
  {
    this.measureOverBaseline = measureOverBaseline;
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

  public String getBonusForEvery()
  {
    return bonusForEvery;
  }

  public void setBonusForEvery( String bonusForEvery )
  {
    this.bonusForEvery = bonusForEvery;
  }

  public String getBonusPayout()
  {
    return bonusPayout;
  }

  public void setBonusPayout( String bonusPayout )
  {
    this.bonusPayout = bonusPayout;
  }

  public String getBonusPayoutCap()
  {
    return bonusPayoutCap;
  }

  public void setBonusPayoutCap( String bonusPayoutCap )
  {
    this.bonusPayoutCap = bonusPayoutCap;
  }

  public Double getContestGoal()
  {
    return contestGoal;
  }

  public void setContestGoal( Double contestGoal )
  {
    this.contestGoal = contestGoal;
  }

  public List<SSIContestLevelForm> getLevels()
  {
    return levels;
  }

  public void setLevels( List<SSIContestLevelForm> levels )
  {
    this.levels = levels;
  }

}
