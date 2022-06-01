
package com.biperf.core.value.nomination;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class NominationApprovalsViewBean
{
  private Long activityId;
  private List<WinnersInfoViewBean> winnersInfo = new ArrayList<>();
  private List<TeamListViewBean> teamList = new ArrayList<>();
  private List<NominatorInfoViewBean> nominatorInfo = new ArrayList<>();
  private String commentTitle;
  private String commentText;
  private String detailURL;

  public Long getActivityId()
  {
    return activityId;
  }

  public void setActivityId( Long activityId )
  {
    this.activityId = activityId;
  }

  public List<WinnersInfoViewBean> getWinnersInfo()
  {
    return winnersInfo;
  }

  public void setWinnersInfo( List<WinnersInfoViewBean> winnersInfo )
  {
    this.winnersInfo = winnersInfo;
  }

  public List<TeamListViewBean> getTeamList()
  {
    return teamList;
  }

  public void setTeamList( List<TeamListViewBean> teamList )
  {
    this.teamList = teamList;
  }

  public List<NominatorInfoViewBean> getNominatorInfo()
  {
    return nominatorInfo;
  }

  public void setNominatorInfo( List<NominatorInfoViewBean> nominatorInfo )
  {
    this.nominatorInfo = nominatorInfo;
  }

  public String getCommentTitle()
  {
    return commentTitle;
  }

  public void setCommentTitle( String commentTitle )
  {
    this.commentTitle = commentTitle;
  }

  public String getCommentText()
  {
    return commentText;
  }

  public void setCommentText( String commentText )
  {
    this.commentText = commentText;
  }

  @JsonProperty( "detailUrl" )
  public String getDetailURL()
  {
    return detailURL;
  }

  public void setDetailURL( String detailURL )
  {
    this.detailURL = detailURL;
  }
}
