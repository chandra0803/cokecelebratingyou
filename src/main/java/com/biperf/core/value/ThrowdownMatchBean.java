
package com.biperf.core.value;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.biperf.core.domain.promotion.Division;
import com.biperf.core.domain.promotion.Match;
import com.biperf.core.domain.promotion.Round;
import com.biperf.core.domain.promotion.SmackTalkComment;
import com.biperf.core.domain.promotion.ThrowdownPromotion;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.HtmlUtils;
import com.biperf.core.utils.UserManager;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude( value = Include.NON_NULL )
public class ThrowdownMatchBean extends BaseJsonView
{
  private static final long serialVersionUID = 1L;

  private ThrowdownPromotion promotion;
  private Division division = null;
  private Round round = null;
  private Match match = null;
  private ThrowdownTeamBean primaryTeam = null;
  private ThrowdownTeamBean secondaryTeam = null;
  private boolean isMine;
  private String matchUrl = null;
  private List<SmackTalkCommentViewBean> smackTalkPosts = new ArrayList<SmackTalkCommentViewBean>();
  private List<SmackTalkComment> userSmackTalkPosts;
  private String asOfDate;
  private boolean progressLoaded;

  private String relativeMatchStartDate;
  private String rulesUrl;

  @JsonProperty( "roundNumber" )
  public int getRoundNumber()
  {
    return round.getRoundNumber();
  }

  @JsonProperty( "numberOfRounds" )
  public int getNumberOfRounds()
  {
    return promotion.getNumberOfRounds();
  }

  @JsonProperty( "displayProgress" )
  public boolean isDisplayProgress()
  {
    return promotion.isDisplayTeamProgress();
  }

  @JsonProperty( "promotionId" )
  public Long getPromotionId()
  {
    return promotion.getId();
  }

  @JsonProperty( "promotionName" )
  public String getPromotionName()
  {
    return promotion.getName();
  }

  @JsonProperty( "promotionStartDate" )
  public String getPromotionStartDate()
  {
    return DateUtils.toDisplayString( promotion.getSubmissionStartDate() );
  }

  @JsonProperty( "promotionEndDate" )
  public String getPromotionEndDate()
  {
    return DateUtils.toDisplayString( promotion.getSubmissionEndDate() );
  }

  @JsonProperty( "roundStartDate" )
  public String getRoundStartDate()
  {
    return DateUtils.toDisplayString( round.getStartDate() );
  }

  @JsonProperty( "roundEndDate" )
  public String getRoundEndDate()
  {
    return DateUtils.toDisplayString( round.getEndDate() );
  }

  @JsonProperty( "matchUrl" )
  public String getMatchUrl()
  {
    return matchUrl;
  }

  public void setMatchUrl( String matchUrl )
  {
    this.matchUrl = matchUrl;
  }

  @JsonProperty( "matchId" )
  public Long getMatchId()
  {
    return match.getId();
  }

  @JsonProperty( "roundYetToStart" )
  public boolean isRoundYetToStart()
  {
    return round.getStartDate().after( UserManager.getCurrentDateWithTimeZoneID() );
  }

  @JsonProperty( "roundCompleted" )
  public boolean isRoundCompleted()
  {
    return round.getEndDate().before( UserManager.getCurrentDateWithTimeZoneID() );
  }

  @JsonProperty( "timeRemaining" )
  public String getTimeRemaining()
  {
    Date roundEndDate = round.getEndDate();
    Date currentDate = UserManager.getCurrentDateWithTimeZoneID();
    // in milliseconds
    long diff = roundEndDate.getTime() - currentDate.getTime();

    long diffSeconds = diff / 1000 % 60;
    long diffMinutes = diff / ( 60 * 1000 ) % 60;
    long diffHours = diff / ( 60 * 60 * 1000 ) % 24;
    long diffDays = diff / ( 24 * 60 * 60 * 1000 );
    return diffDays + ":" + diffHours + ":" + diffMinutes + ":" + diffSeconds;
  }

  @JsonProperty( "promotionOverview" )
  public String getPromotionOverviewFormatted()
  {
    return HtmlUtils.removeFormatting( promotion.getOverviewDetailsText() );
  }

  @JsonProperty( "overview" )
  public String getPromotionOverview()
  {
    return promotion.getOverviewDetailsText();
  }

  @JsonIgnore
  public ThrowdownPromotion getPromotion()
  {
    return promotion;
  }

  @JsonIgnore
  public void setPromotion( ThrowdownPromotion promotion )
  {
    this.promotion = promotion;
  }

  @JsonIgnore
  public Division getDivision()
  {
    return division;
  }

  @JsonIgnore
  public void setDivision( Division division )
  {
    this.division = division;
  }

  @JsonIgnore
  public Round getRound()
  {
    return round;
  }

  @JsonIgnore
  public void setRound( Round round )
  {
    this.round = round;
  }

  @JsonIgnore
  public Match getMatch()
  {
    return match;
  }

  @JsonIgnore
  public void setMatch( Match match )
  {
    this.match = match;
  }

  @JsonProperty( "primaryTeam" )
  public ThrowdownTeamBean getPrimaryTeam()
  {
    return primaryTeam;
  }

  @JsonIgnore
  public void setPrimaryTeam( ThrowdownTeamBean primaryTeam )
  {
    if ( secondaryTeam != null )
    {
      primaryTeam.setOpponentCurrentProgress( secondaryTeam.getProgress() );
      secondaryTeam.setOpponentCurrentProgress( primaryTeam.getProgress() );
    }
    this.primaryTeam = primaryTeam;
  }

  @JsonProperty( "secondaryTeam" )
  public ThrowdownTeamBean getSecondaryTeam()
  {
    return secondaryTeam;
  }

  @JsonIgnore
  public void setSecondaryTeam( ThrowdownTeamBean secondaryTeam )
  {
    if ( primaryTeam != null )
    {
      secondaryTeam.setOpponentCurrentProgress( primaryTeam.getProgress() );
      primaryTeam.setOpponentCurrentProgress( secondaryTeam.getProgress() );
    }
    this.secondaryTeam = secondaryTeam;
  }

  @JsonProperty( "comments" )
  public List<SmackTalkCommentViewBean> getSmackTalkPosts()
  {
    return smackTalkPosts;
  }

  public void setSmackTalkPosts( List<SmackTalkCommentViewBean> smackTalkPosts )
  {
    this.smackTalkPosts = smackTalkPosts;
  }

  @JsonProperty( "isMine" )
  public boolean isMine()
  {
    return isMine;
  }

  public void setMine( boolean isMine )
  {
    this.isMine = isMine;
  }

  public String getRelativeMatchStartDate()
  {
    relativeMatchStartDate = DateUtils.toRelativeTimeLapsed( match.getAuditCreateInfo().getDateCreated() );
    return relativeMatchStartDate;
  }

  public void setUserSmackTalkPosts( List<SmackTalkComment> userSmackTalkPosts )
  {
    this.userSmackTalkPosts = userSmackTalkPosts;
  }

  public List<SmackTalkComment> getUserSmackTalkPosts()
  {
    return userSmackTalkPosts;
  }

  public String getRulesUrl()
  {
    return rulesUrl;
  }

  public void setRulesUrl( String rulesUrl )
  {
    this.rulesUrl = rulesUrl;
  }

  public boolean isOpponentDecided()
  {
    return secondaryTeam != null;
  }

  public String getAvatarUrlForTBDPlayer()
  {
    return null;
  }

  public String getAvatarUrlSmallForTBDPlayer()
  {
    return null;
  }

  @JsonProperty( "endDate" )
  public Date getEndDate()
  {
    return round.getEndDate();
  }

  @JsonProperty( "smackTalkAvailable" )
  public boolean getSmackTalkAvailable()
  {
    return promotion.isSmackTalkAvailable();
  }

  @JsonProperty( "asOfDate" )
  public String getAsOfDate()
  {
    return asOfDate;
  }

  public void setAsOfDate( String asOfDate )
  {
    this.asOfDate = asOfDate;
  }

  @JsonProperty( "isProgressLoaded" )
  public boolean getProgressLoaded()
  {
    return progressLoaded;
  }

  @JsonIgnore
  public void setProgressLoaded( boolean progressLoaded )
  {
    this.progressLoaded = progressLoaded;
  }

}
