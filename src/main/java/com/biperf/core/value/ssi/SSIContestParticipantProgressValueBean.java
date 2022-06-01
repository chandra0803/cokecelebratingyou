
package com.biperf.core.value.ssi;

public class SSIContestParticipantProgressValueBean
{
  private Long activityId; // only for DTGT
  private String participantName;
  private String activityName;
  private Double activityAmount;
  private Long paxId;

  public Long getPaxId()
  {
    return paxId;
  }

  public void setPaxId( Long paxId )
  {
    this.paxId = paxId;
  }

  public Long getActivityId()
  {
    return activityId;
  }

  public void setActivityId( Long activityId )
  {
    this.activityId = activityId;
  }

  public String getParticipantName()
  {
    return participantName;
  }

  public void setParticipantName( String participantName )
  {
    this.participantName = participantName;
  }

  public String getActivityName()
  {
    return activityName;
  }

  public void setActivityName( String activityName )
  {
    this.activityName = activityName;
  }

  public Double getActivityAmount()
  {
    return activityAmount;
  }

  public void setActivityAmount( Double activityAmount )
  {
    this.activityAmount = activityAmount;
  }

}
