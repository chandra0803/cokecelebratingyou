
package com.biperf.core.ui.ssi.view;

import java.text.MessageFormat;

import com.biperf.core.domain.ssi.SSIContest;
import com.biperf.core.domain.ssi.SSIContestLevel;
import com.biperf.core.utils.SSIContestUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.objectpartners.cms.util.CmsResourceBundle;

@JsonInclude( value = Include.NON_NULL )
public class SSIContestLevelView
{
  private Long id;
  private String name;
  private String payout;
  private String activityLabel;
  private String payoutDescription;
  private SSIPaxContestBadgeView badge = new SSIPaxContestBadgeView();
  private Integer sequenceNumber;
  private String amount;
  private Long badgeId;
  private Boolean firstLevel;
  private Boolean lastLevel;

  private Boolean currentLevel;
  private Boolean completed;
  private String goalPercent;
  private String goalAmount;
  private String goal;
  private String progress;
  private String remaining;
  private Long index; // sequence number from contestLevel
  private String progressFormatted;
  private Long remainingProgress;
  private Long levelMin;
  private Long levelMax;
  private String baseline;
  private Long bonusEarned;

  // VALUES FOR PAX STACK RANK
  private int participantsCount;
  private int stackRank;
  private String avatarUrl;

  // Badge Info
  private Long badgeRuleId;
  private String badgeName;
  private String badgeUrl;

  public SSIContestLevelView()
  {

  }

  public SSIContestLevelView( SSIContestLevel ssiContestLevel,
                              SSIContest contest,
                              String activityPrefix,
                              int precision,
                              String payoutPrefix,
                              String payoutSuffix,
                              Boolean firstLevel,
                              Boolean lastLevel )
  {
    this.id = ssiContestLevel.getId();
    this.name = MessageFormat.format( CmsResourceBundle.getCmsBundle().getString( "ssi_contest.approvals.detail.LEVEL" ), new Object[] { ssiContestLevel.getSequenceNumber() } )
        + ( firstLevel ? CmsResourceBundle.getCmsBundle().getString( "ssi_contest.approvals.detail.LOWEST_LEVEL" ) : "" )
        + ( lastLevel ? CmsResourceBundle.getCmsBundle().getString( "ssi_contest.approvals.detail.HIGHEST_LEVEL" ) : "" );
    this.payout = payoutPrefix + SSIContestUtil.getFormattedValue( ssiContestLevel.getPayoutAmount(), SSIContestUtil.PAYOUT_DECIMAL_PRECISION )
        + ( contest.getPayoutType().isPoints() ? payoutSuffix : "" );
    this.activityLabel = SSIContestUtil.getBaselineTypeLabel( contest );
    this.payoutDescription = ssiContestLevel.getPayoutDesc();
    this.sequenceNumber = ssiContestLevel.getSequenceNumber();
    if ( activityPrefix != null )
    {
      if ( contest.getIndividualBaselineType().isPercentageOverBaseline() )
      {
        this.amount = SSIContestUtil.getFormattedValue( ssiContestLevel.getGoalAmount(), SSIContestUtil.PAYOUT_DECIMAL_PRECISION );
      }
      else
      {
        this.amount = activityPrefix + SSIContestUtil.getFormattedValue( ssiContestLevel.getGoalAmount(), precision );
      }
    }
    else
    {
      this.amount = String.valueOf( ssiContestLevel.getGoalAmount() );
    }
    this.badgeId = ssiContestLevel.getBadgeRule() != null ? ssiContestLevel.getBadgeRule().getId() : null;
    this.badge = new SSIPaxContestBadgeView( ssiContestLevel.getBadgeRule() );
    this.firstLevel = firstLevel;
    this.lastLevel = lastLevel;
  }

  public Long getId()
  {
    return id;
  }

  public void setId( Long id )
  {
    this.id = id;
  }

  public String getName()
  {
    return name;
  }

  public void setName( String name )
  {
    this.name = name;
  }

  public String getActivityLabel()
  {
    return activityLabel;
  }

  public void setActivityLabel( String activityLabel )
  {
    this.activityLabel = activityLabel;
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

  public Integer getSequenceNumber()
  {
    return sequenceNumber;
  }

  public void setSequenceNumber( Integer sequenceNumber )
  {
    this.sequenceNumber = sequenceNumber;
  }

  public String getPayout()
  {
    return payout;
  }

  public void setPayout( String payout )
  {
    this.payout = payout;
  }

  public String getAmount()
  {
    return amount;
  }

  public void setAmount( String amount )
  {
    this.amount = amount;
  }

  public Long getBadgeId()
  {
    return badgeId;
  }

  public void setBadgeId( Long badgeId )
  {
    this.badgeId = badgeId;
  }

  @JsonProperty( "isFirstLevel" )
  public Boolean getFirstLevel()
  {
    return firstLevel;
  }

  public void setFirstLevel( Boolean firstLevel )
  {
    this.firstLevel = firstLevel;
  }

  @JsonProperty( "isLastLevel" )
  public Boolean getLastLevel()
  {
    return lastLevel;
  }

  public void setLastLevel( Boolean lastLevel )
  {
    this.lastLevel = lastLevel;
  }

  @JsonProperty( "isCurrentLevel" )
  public Boolean getCurrentLevel()
  {
    return currentLevel;
  }

  public void setCurrentLevel( Boolean currentLevel )
  {
    this.currentLevel = currentLevel;
  }

  @JsonProperty( "isCompleted" )
  public Boolean getCompleted()
  {
    return completed;
  }

  public void setCompleted( Boolean completed )
  {
    this.completed = completed;
  }

  public String getGoalPercent()
  {
    return goalPercent;
  }

  public void setGoalPercent( String goalPercent )
  {
    this.goalPercent = goalPercent;
  }

  public String getProgress()
  {
    if ( progress == null || progress.equals( "" ) )
    {
      return "0";
    }
    return progress;
  }

  public void setProgress( String progress )
  {
    this.progress = progress;
  }

  public String getRemaining()
  {
    return remaining;
  }

  public void setRemaining( String remaining )
  {
    this.remaining = remaining;
  }

  public Long getIndex()
  {
    return index;
  }

  public void setIndex( Long index )
  {
    this.index = index;
  }

  public String getProgressFormatted()
  {
    return progressFormatted;
  }

  public void setProgressFormatted( String progressFormatted )
  {
    this.progressFormatted = progressFormatted;
  }

  public Long getRemainingProgress()
  {
    return remainingProgress;
  }

  public void setRemainingProgress( Long remainingProgress )
  {
    this.remainingProgress = remainingProgress;
  }

  public Long getLevelMin()
  {
    return levelMin;
  }

  public void setLevelMin( Long levelMin )
  {
    this.levelMin = levelMin;
  }

  public Long getLevelMax()
  {
    return levelMax;
  }

  public void setLevelMax( Long levelMax )
  {
    this.levelMax = levelMax;
  }

  public String getGoalAmount()
  {
    return goalAmount;
  }

  public void setGoalAmount( String goalAmount )
  {
    this.goalAmount = goalAmount;
  }

  public String getGoal()
  {
    return goal;
  }

  public void setGoal( String goal )
  {
    this.goal = goal;
  }

  public String getBaseline()
  {
    return baseline;
  }

  public void setBaseline( String baseline )
  {
    this.baseline = baseline;
  }

  public Long getBonusEarned()
  {
    return bonusEarned;
  }

  public void setBonusEarned( Long bonusEarned )
  {
    this.bonusEarned = bonusEarned;
  }

  public int getParticipantsCount()
  {
    return participantsCount;
  }

  public void setParticipantsCount( int participantsCount )
  {
    this.participantsCount = participantsCount;
  }

  public int getStackRank()
  {
    return stackRank;
  }

  public void setStackRank( int stackRank )
  {
    this.stackRank = stackRank;
  }

  public String getAvatarUrl()
  {
    return avatarUrl;
  }

  public void setAvatarUrl( String avatarUrl )
  {
    this.avatarUrl = avatarUrl;
  }

  public Long getBadgeRuleId()
  {
    return badgeRuleId;
  }

  public void setBadgeRuleId( Long badgeRuleId )
  {
    this.badgeRuleId = badgeRuleId;
  }

  public String getBadgeName()
  {
    return badgeName;
  }

  public void setBadgeName( String badgeName )
  {
    this.badgeName = badgeName;
  }

  public String getBadgeUrl()
  {
    return badgeUrl;
  }

  public void setBadgeUrl( String badgeUrl )
  {
    this.badgeUrl = badgeUrl;
  }

}
