
package com.biperf.core.value.ssi;

import com.biperf.core.domain.enums.SSIActivityMeasureType;
import com.biperf.core.utils.SSIContestUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude( value = Include.NON_NULL )
public class SSIContestStackRankTeamValueBean
{
  private Long participantId;
  private String firstName;
  private String lastName;
  private String avatarUrl;
  private Integer rank;
  private Double score;
  private Long payout;
  private String payoutDescription;
  private String activityMeasureType;

  // below property is used for procedure ssi_contest_progress
  private Long activityId;
  private Boolean teamMember; // Bug #61359

  public SSIContestStackRankTeamValueBean()
  {
  }

  public SSIContestStackRankTeamValueBean( Long participantId, String firstName, String lastName, String avatarUrl, Integer rank, Double score )
  {
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

  @JsonProperty( "score" )
  public String getDisplayScore()
  {
    if ( SSIActivityMeasureType.CURRENCY_CODE.equals( this.activityMeasureType ) )
    {
      return SSIContestUtil.getFormattedValue( this.score, SSIContestUtil.ACTIVITY_CURRENCY_DECIMAL_PRECISION );
    }
    else
    {
      return SSIContestUtil.getFormattedValue( this.score, SSIContestUtil.ACTIVITY_UNITS_DECIMAL_PRECISION );
    }
  }

  @JsonIgnore
  public Double getScore()
  {
    return score;
  }

  public void setScore( Double score )
  {
    this.score = score;
  }

  public Long getActivityId()
  {
    return activityId;
  }

  public void setActivityId( Long activityId )
  {
    this.activityId = activityId;
  }

  public Long getPayout()
  {
    return payout;
  }

  public void setPayout( Long payout )
  {
    this.payout = payout;
  }

  public String getPayoutDescription()
  {
    return payoutDescription;
  }

  public void setPayoutDescription( String payoutDescription )
  {
    this.payoutDescription = payoutDescription;
  }

  @JsonIgnore
  public String getActivityMeasureType()
  {
    return activityMeasureType;
  }

  public void setActivityMeasureType( String activityMeasureType )
  {
    this.activityMeasureType = activityMeasureType;
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

}
