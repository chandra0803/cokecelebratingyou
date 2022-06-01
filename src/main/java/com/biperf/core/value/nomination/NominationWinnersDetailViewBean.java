
package com.biperf.core.value.nomination;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class NominationWinnersDetailViewBean
{
  private boolean mine;
  private boolean winner;
  private boolean allowTranslate;
  private String winnerName;
  private String awardName;
  private String awardedDate;
  private String award;
  private String currencyLabel;
  private List<NominatorDetailInfoViewBean> nominatorInfo;
  private String contextPath;
  private Long teamId;
  private boolean team;
  private List<TeamListViewBean> teamList = new ArrayList<>();

  @JsonProperty( "isMine" )
  public boolean isMine()
  {
    return mine;
  }

  public void setMine( boolean mine )
  {
    this.mine = mine;
  }

  public String getWinnerName()
  {
    return winnerName;
  }

  public void setWinnerName( String winnerName )
  {
    this.winnerName = winnerName;
  }

  public String getAwardName()
  {
    return awardName;
  }

  public void setAwardName( String awardName )
  {
    this.awardName = awardName;
  }

  public String getAwardedDate()
  {
    return awardedDate;
  }

  public void setAwardedDate( String awardedDate )
  {
    this.awardedDate = awardedDate;
  }

  public String getAward()
  {
    return award;
  }

  public void setAward( String award )
  {
    this.award = award;
  }

  public String getCurrencyLabel()
  {
    return currencyLabel;
  }

  public void setCurrencyLabel( String currencyLabel )
  {
    this.currencyLabel = currencyLabel;
  }

  public List<NominatorDetailInfoViewBean> getNominatorInfo()
  {
    return nominatorInfo;
  }

  public void setNominatorInfo( List<NominatorDetailInfoViewBean> nominatorInfo )
  {
    this.nominatorInfo = nominatorInfo;
  }

  @JsonProperty( "isWinner" )
  public boolean isWinner()
  {
    return winner;
  }

  public void setWinner( boolean winner )
  {
    this.winner = winner;
  }

  public boolean isAllowTranslate()
  {
    return allowTranslate;
  }

  public void setAllowTranslate( boolean allowTranslate )
  {
    this.allowTranslate = allowTranslate;
  }

  public String getContextPath()
  {
    return contextPath;
  }

  public void setContextPath( String contextPath )
  {
    this.contextPath = contextPath;
  }

  public Long getTeamId()
  {
    return teamId;
  }

  public void setTeamId( Long teamId )
  {
    this.teamId = teamId;
  }

  @JsonProperty( "isTeam" )
  public boolean isTeam()
  {
    return team;
  }

  public void setTeam( boolean team )
  {
    this.team = team;
  }

  public List<TeamListViewBean> getTeamList()
  {
    return teamList;
  }

  public void setTeamList( List<TeamListViewBean> teamList )
  {
    this.teamList = teamList;
  }

}
