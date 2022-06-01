
package com.biperf.core.ui.ssi.view;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude( value = Include.NON_NULL )
public class SSIStackRankParticipantViewBean
{
  private Long participantId;
  private String firstName;
  private String lastName;
  private String avatarUrl;
  private String thumbnailUrl;
  private Integer rank;
  private String score;
  private Boolean teamMember;
  private String participantDetailPageUrl;
  private Integer participantsCount;
  private String activityMeasureType;
  private Long activityId;
  private String contestUrl;
  private Boolean currentUser;
  SSIContestStackRankPayoutView payout = new SSIContestStackRankPayoutView();

  public SSIStackRankParticipantViewBean()
  {

  }

  public SSIStackRankParticipantViewBean( Integer rank, Integer participantsCount, String thumbnailUrl )
  {
    this.rank = rank;
    this.participantsCount = participantsCount;
    this.thumbnailUrl = thumbnailUrl;
    this.avatarUrl = thumbnailUrl;
  }

  public SSIStackRankParticipantViewBean( Long participantId, String firstName, String lastName, String avatarUrl, Integer rank, String score )
  {
    super();
    this.participantId = participantId;
    this.firstName = firstName;
    this.lastName = lastName;
    this.avatarUrl = avatarUrl;
    this.rank = rank;
    this.score = score;
  }

  @JsonProperty( "id" )
  public Long getId()
  {
    return participantId;
  }

  public Long getParticipantId()
  {
    return participantId;
  }

  public void setParticipantId( Long participantId )
  {
    this.participantId = participantId;
  }

  public String getFirstName()
  {
    return firstName;
  }

  public void setFirstName( String firstName )
  {
    this.firstName = firstName;
  }

  public String getLastName()
  {
    return lastName;
  }

  public void setLastName( String lastName )
  {
    this.lastName = lastName;
  }

  public String getAvatarUrl()
  {
    return avatarUrl;
  }

  public void setAvatarUrl( String avatarUrl )
  {
    this.avatarUrl = avatarUrl;
  }

  public Integer getRank()
  {
    return rank;
  }

  public void setRank( Integer rank )
  {
    this.rank = rank;
  }

  public String getScore()
  {
    if ( score == null || score.equals( "" ) )
    {
      return "0";
    }
    return score;
  }

  public void setScore( String score )
  {
    this.score = score;
  }

  @JsonProperty( "isTeamMember" )
  public Boolean getTeamMember()
  {
    return teamMember;
  }

  public void setTeamMember( Boolean teamMember )
  {
    this.teamMember = teamMember;
  }

  public String getParticipantDetailPageUrl()
  {
    return participantDetailPageUrl;
  }

  public void setParticipantDetailPageUrl( String participantDetailPageUrl )
  {
    this.participantDetailPageUrl = participantDetailPageUrl;
  }

  public Integer getParticipantsCount()
  {
    return participantsCount;
  }

  public void setParticipantsCount( Integer participantsCount )
  {
    this.participantsCount = participantsCount;
  }

  public String getThumbnailUrl()
  {
    return thumbnailUrl;
  }

  public void setThumbnailUrl( String thumbnailUrl )
  {
    this.thumbnailUrl = thumbnailUrl;
  }

  public String getActivityMeasureType()
  {
    return activityMeasureType;
  }

  public void setActivityMeasureType( String activityMeasureType )
  {
    this.activityMeasureType = activityMeasureType;
  }

  public Long getActivityId()
  {
    return activityId;
  }

  public void setActivityId( Long activityId )
  {
    this.activityId = activityId;
  }

  public String getContestUrl()
  {
    return contestUrl;
  }

  public void setContestUrl( String contestUrl )
  {
    this.contestUrl = contestUrl;
  }

  public Boolean getCurrentUser()
  {
    return currentUser;
  }

  public void setCurrentUser( Boolean currentUser )
  {
    this.currentUser = currentUser;
  }

  public SSIContestStackRankPayoutView getPayout()
  {
    return payout;
  }

  public void setPayout( SSIContestStackRankPayoutView payout )
  {
    this.payout = payout;
  }

}
