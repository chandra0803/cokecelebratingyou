
package com.biperf.core.value.ssi;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude( value = Include.NON_NULL )
public class SSIContestStackRankPaxValueBean
{

  private Integer rank;
  private Integer participantsCount;
  private String contestUrl;
  private String thumbnailUrl;

  private Long participantId;
  private String firstName;
  private String lastName;
  private String avatarUrl;
  private Double score;
  private Boolean teamMember;
  private Boolean currentUser;

  public SSIContestStackRankPaxValueBean()
  {

  }

  public SSIContestStackRankPaxValueBean( Integer rank, String firstName, String lastName, String avatarUrl, Long participantId, Double score )
  {
    this.rank = rank;
    this.firstName = firstName;
    this.lastName = lastName;
    this.avatarUrl = avatarUrl;
    this.participantId = participantId;
    this.score = score;
  }

  public SSIContestStackRankPaxValueBean( Integer rank, Integer participantsCount, String thumbnailUrl )
  {
    this.rank = rank;
    this.participantsCount = participantsCount;
    this.thumbnailUrl = thumbnailUrl;
    this.avatarUrl = thumbnailUrl;
  }

  public Integer getRank()
  {
    return rank;
  }

  public void setRank( Integer rank )
  {
    this.rank = rank;
  }

  public Integer getParticipantsCount()
  {
    return participantsCount;
  }

  public void setParticipantsCount( Integer participantsCount )
  {
    this.participantsCount = participantsCount;
  }

  public String getContestUrl()
  {
    return contestUrl;
  }

  public void setContestUrl( String contestUrl )
  {
    this.contestUrl = contestUrl;
  }

  public String getThumbnailUrl()
  {
    return thumbnailUrl;
  }

  public void setThumbnailUrl( String thumbnailUrl )
  {
    this.thumbnailUrl = thumbnailUrl;
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

  public Double getScore()
  {
    return score;
  }

  public void setScore( Double score )
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

  @JsonProperty( "currentUser" )
  public Boolean getCurrentUser()
  {
    return currentUser;
  }

  public void setCurrentUser( Boolean currentUser )
  {
    this.currentUser = currentUser;
  }

}
