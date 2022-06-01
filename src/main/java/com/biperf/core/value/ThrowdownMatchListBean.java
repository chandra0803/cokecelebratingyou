/*
 * (c) 2013 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/value/ThrowdownMatchListBean.java,v $
 */

package com.biperf.core.value;

import java.util.Date;
import java.util.List;

import com.biperf.core.domain.promotion.Round;
import com.biperf.core.domain.promotion.ThrowdownPromotion;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.HtmlUtils;
import com.biperf.core.utils.UserManager;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 
 * @author kothanda
 * @since Nov 4, 2013
 * @version 1.0
 */
public class ThrowdownMatchListBean extends BaseJsonView
{

  private static final long serialVersionUID = 1L;

  private ThrowdownPromotion promotion;
  private Round round = null;
  private List<ThrowdownMatchBean> matches;
  private int totalMatches;
  private int currentPage;
  private ThrowdownMatchListTabularData tabularData;
  private String allMatchesUrl;
  private boolean doIHaveMatch = true;
  private String progressEndDate;
  private boolean progressLoaded;
  public static final int PAGE_SIZE = 25;

  @JsonProperty( "totalRounds" )
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

  @JsonProperty( "promotionName" )
  public String getPromotionName()
  {
    return promotion.getName();
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

  @JsonProperty( "currentRound" )
  public int getRoundNumber()
  {
    return round.getRoundNumber();
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

  @JsonProperty( "teams" )
  public List<ThrowdownMatchBean> getMatches()
  {
    return matches;
  }

  @JsonIgnore
  public void setMatches( List<ThrowdownMatchBean> matches )
  {
    this.matches = matches;
  }

  @JsonProperty( "totalMatches" )
  public int getTotalMatches()
  {
    return totalMatches;
  }

  @JsonIgnore
  public void setTotalMatches( int totalMatches )
  {
    this.totalMatches = totalMatches;
  }

  @JsonProperty( "matchesPerPage" )
  public int getMatchesPerPage()
  {
    return PAGE_SIZE;
  }

  @JsonProperty( "currentPage" )
  public int getCurrentPage()
  {
    return currentPage;
  }

  @JsonIgnore
  public void setCurrentPage( int currentPage )
  {
    this.currentPage = currentPage;
  }

  public void setTabularData( ThrowdownMatchListTabularData tabularData )
  {
    this.tabularData = tabularData;
  }

  @JsonProperty( "tabularData" )
  public ThrowdownMatchListTabularData getTabularData()
  {
    return tabularData;
  }

  public void setAllMatchesUrl( String allMatchesUrl )
  {
    this.allMatchesUrl = allMatchesUrl;
  }

  @JsonProperty( "allMatchesUrl" )
  public String getAllMatchesUrl()
  {
    return allMatchesUrl;
  }

  @JsonProperty( "roundScheduled" )
  public boolean getRoundScheduled()
  {
    return totalMatches > 0;
  }

  @JsonProperty( "doIHaveMatch" )
  public boolean isDoIHaveMatch()
  {
    return doIHaveMatch;
  }

  @JsonIgnore
  public void setDoIHaveMatch( boolean doIHaveMatch )
  {
    this.doIHaveMatch = doIHaveMatch;
  }

  @JsonProperty( "smackTalkAvailable" )
  public boolean getSmackTalkAvailable()
  {
    return promotion.isSmackTalkAvailable();
  }

  @JsonProperty( "progressEndDate" )
  public String getProgressEndDate()
  {
    return progressEndDate;
  }

  public void setProgressEndDate( String progressEndDate )
  {
    this.progressEndDate = progressEndDate;
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
