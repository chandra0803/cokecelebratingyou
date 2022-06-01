
package com.biperf.core.value;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.biperf.core.dao.throwdown.TeamStats;
import com.biperf.core.domain.enums.BaseUnitPosition;
import com.biperf.core.domain.enums.MatchTeamOutcomeType;
import com.biperf.core.domain.gamification.BadgeInfo;
import com.biperf.core.domain.promotion.Team;
import com.biperf.core.utils.NumberFormatUtil;
import com.biperf.core.utils.UserManager;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.objectpartners.cms.util.CmsResourceBundle;

@JsonInclude( value = Include.NON_NULL )
public class ThrowdownTeamBean implements Serializable
{
  private static final long serialVersionUID = 1L;

  private Team team = null;
  private TeamStats stats = null;
  private Integer rank = new Integer( -1 );
  private BigDecimal currentProgress = null;
  private BigDecimal opponentCurrentProgress = null;
  private String baseUnit;
  private String avatarUrl = null;
  private String avatarUrlSmall = null;
  private BaseUnitPosition baseUnitPosition;
  private List<BadgeInfo> badges = new ArrayList<BadgeInfo>();
  private String teamStatsUrl = null;
  private boolean displayProgress;
  private MatchTeamOutcomeType outcome;
  private String profileUrl;
  private String rankUrl;
  private int precision;

  @JsonProperty( "teamStatsUrl" )
  public String getTeamStatsUrl()
  {
    return teamStatsUrl;
  }

  @JsonIgnore
  public void setTeamStatsUrl( String teamStatsUrl )
  {
    this.teamStatsUrl = teamStatsUrl;
  }

  @JsonProperty( "avatarUrl" )
  public String getAvatarUrl()
  {
    return avatarUrl;
  }

  @JsonIgnore
  public void setAvatarUrl( String avatarUrl )
  {
    this.avatarUrl = avatarUrl;
  }

  @JsonIgnore
  public Team getTeam()
  {
    return team;
  }

  @JsonProperty( "badgeInfos" )
  public List<BadgeInfo> getBadges()
  {
    return badges;
  }

  @JsonIgnore
  public void setBadges( List<BadgeInfo> badges )
  {
    this.badges = badges;
  }

  @JsonIgnore
  public void setTeam( Team team )
  {
    this.team = team;
  }

  @JsonProperty( "isMyTeam" )
  public boolean isMyTeam()
  {
    if ( team.isShadowPlayer() )
    {
      return false;
    }

    return UserManager.getUserId().equals( team.getParticipant().getId() );
  }

  @JsonProperty( "name" )
  public String getName()
  {
    if ( !team.isShadowPlayer() )
    {
      return team.getParticipant().getNameLFMWithComma();
    }
    else
    {
      return team.getDivision().getPromotion().getTeamUnavailableResolverType().getName();
    }
  }

  @JsonProperty( "stats" )
  public TeamStats getStats()
  {
    return stats;
  }

  @JsonIgnore
  public void setStats( TeamStats stats )
  {
    this.stats = stats;
  }

  @JsonProperty( "rankInt" )
  public Integer getRank()
  {
    return rank;
  }

  @JsonProperty( "rank" )
  public String getDisplayRank()
  {
    return rank != null && rank.intValue() > 0 ? rank.toString() : "";
  }

  @JsonIgnore
  public void setRank( Integer rank )
  {
    this.rank = rank;
  }

  @JsonIgnore
  public void setCurrentProgress( BigDecimal currentProgress )
  {
    this.currentProgress = currentProgress;
  }

  public BigDecimal getProgress()
  {
    return currentProgress;
  }

  @JsonProperty( "currentProgress" )
  public String getCurrentProgress()
  {
    return NumberFormatUtil.getLocaleBasedBigDecimalFormat( this.currentProgress, precision, UserManager.getLocale() );
  }

  @JsonProperty( "currentProgressForDisplay" )
  public String getCurrentProgressForDisplay()
  {
    StringBuilder sb = new StringBuilder();
    BigDecimal progress = null != currentProgress ? currentProgress : new BigDecimal( 0 );

    if ( baseUnit != null )
    {
      if ( baseUnitPosition != null && baseUnitPosition.getCode().equals( BaseUnitPosition.UNIT_BEFORE ) )
      {
        sb.append( baseUnit + " " + NumberFormatUtil.getLocaleBasedBigDecimalFormat( progress, precision, UserManager.getLocale() ) );
      }
      else
      {
        sb.append( NumberFormatUtil.getLocaleBasedBigDecimalFormat( progress, precision, UserManager.getLocale() ) + " " + baseUnit );
      }
    }
    else
    {
      sb.append( NumberFormatUtil.getLocaleBasedBigDecimalFormat( progress, precision, UserManager.getLocale() ) );
    }
    return sb.toString();
  }

  @JsonProperty( "currentProgressForDisplayWithIndicator" )
  public String getCurrentProgressForDisplayWithIndicator()
  {
    if ( isMyTeam() || isDisplayProgress() )
    {
      StringBuilder sb = new StringBuilder();
      sb.append( getCurrentProgressForDisplay() );

      if ( !isDisplayProgress() && ( outcome == null || outcome.equals( MatchTeamOutcomeType.lookup( MatchTeamOutcomeType.NONE ) ) ) )
      {
        sb.append( " " + getProgressVerbiage() );
      }
      return sb.toString();
    }
    else
    {
      if ( outcome == null || outcome.equals( MatchTeamOutcomeType.lookup( MatchTeamOutcomeType.NONE ) ) )
      {
        return getProgressVerbiage();
      }
    }
    return "";
  }

  @JsonProperty( "progressVerbiage" )
  public String getProgressVerbiage()
  {
    BigDecimal myProgress = null != currentProgress ? currentProgress : new BigDecimal( 0 );
    BigDecimal opponentProgress = null != opponentCurrentProgress ? opponentCurrentProgress : new BigDecimal( 0 );

    if ( myProgress.compareTo( opponentProgress ) > 0 )
    {
      return CmsResourceBundle.getCmsBundle().getString( "participant.throwdownstats.CURRENTLY_AHEAD" );
    }
    else if ( myProgress.compareTo( opponentProgress ) == 0 )
    {
      return CmsResourceBundle.getCmsBundle().getString( "participant.throwdownstats.CURRENTLY_TIED" );
    }
    else
    {
      return "";
    }
  }

  @JsonProperty( "outcomeForDisplayShortForm" )
  public String getOutcomeForDisplayShortForm()
  {
    if ( isWin() )
    {
      return CmsResourceBundle.getCmsBundle().getString( "participant.throwdownstats.WIN_SHORT_FORM" );
    }
    else if ( isTie() )
    {
      return CmsResourceBundle.getCmsBundle().getString( "participant.throwdownstats.TIE_SHORT_FORM" );
    }
    return "";
  }

  @JsonProperty( "outcomeForDisplayFullForm" )
  public String getOutcomeForDisplayFullForm()
  {
    if ( isWin() )
    {
      return CmsResourceBundle.getCmsBundle().getString( "participant.throwdownstats.WINNER" );
    }
    else if ( isTie() )
    {
      return CmsResourceBundle.getCmsBundle().getString( "participant.throwdownstats.TIE" );
    }
    return "";
  }

  @JsonProperty( "isShadowPlayer" )
  public boolean isShadowPlayer()
  {
    return team.isShadowPlayer();
  }

  @JsonIgnore
  public String getBaseUnit()
  {
    return baseUnit;
  }

  @JsonIgnore
  public void setBaseUnit( String baseUnit )
  {
    this.baseUnit = baseUnit;
  }

  @JsonIgnore
  public BaseUnitPosition getBaseUnitPosition()
  {
    return baseUnitPosition;
  }

  @JsonIgnore
  public void setBaseUnitPosition( BaseUnitPosition baseUnitPosition )
  {
    this.baseUnitPosition = baseUnitPosition;
  }

  @JsonProperty( "matchResult" )
  public String getMatchResult()
  {
    return outcome != null ? outcome.getName() : null;
  }

  @JsonProperty( "avatarUrlSmall" )
  public String getAvatarUrlSmall()
  {
    return avatarUrlSmall;
  }

  @JsonIgnore
  public void setAvatarUrlSmall( String avatarUrlSmall )
  {
    this.avatarUrlSmall = avatarUrlSmall;
  }

  @JsonProperty( "displayProgress" )
  public boolean isDisplayProgress()
  {
    return displayProgress;
  }

  @JsonIgnore
  public void setDisplayProgress( boolean displayProgress )
  {
    this.displayProgress = displayProgress;
  }

  @JsonIgnore
  public MatchTeamOutcomeType getOutcome()
  {
    return outcome;
  }

  @JsonIgnore
  public void setOutcome( MatchTeamOutcomeType outcome )
  {
    this.outcome = outcome;
  }

  public boolean isLoss()
  {
    return outcome != null ? outcome.equals( MatchTeamOutcomeType.lookup( MatchTeamOutcomeType.LOSS ) ) : false;
  }

  public boolean isWin()
  {
    return outcome != null ? outcome.equals( MatchTeamOutcomeType.lookup( MatchTeamOutcomeType.WIN ) ) : false;
  }

  public boolean isTie()
  {
    return outcome != null ? outcome.equals( MatchTeamOutcomeType.lookup( MatchTeamOutcomeType.TIE ) ) : false;
  }

  @JsonProperty( "id" )
  public Long getId()
  {
    return !team.isShadowPlayer() ? team.getParticipant().getId() : null;
  }

  @JsonIgnore
  public BigDecimal getOpponentCurrentProgress()
  {
    return opponentCurrentProgress;
  }

  @JsonIgnore
  public void setOpponentCurrentProgress( BigDecimal opponentCurrentProgress )
  {
    this.opponentCurrentProgress = opponentCurrentProgress;
  }

  @JsonProperty( "profileUrl" )
  public String getProfileUrl()
  {
    return profileUrl;
  }

  @JsonIgnore
  public void setProfileUrl( String profileUrl )
  {
    this.profileUrl = profileUrl;
  }

  @JsonProperty( "rankUrl" )
  public String getRankUrl()
  {
    return rankUrl;
  }

  @JsonIgnore
  public void setRankUrl( String rankUrl )
  {
    this.rankUrl = rankUrl;
  }

  public void setPrecision( int precision )
  {
    this.precision = precision;
  }

  public int getPrecision()
  {
    return precision;
  }

}
