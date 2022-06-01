
package com.biperf.core.domain.purl;

import com.biperf.core.domain.participant.ParticipantInfoView;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude( value = Include.NON_NULL )
public class PurlCelebration
{

  private long id;
  private String milestone;
  private String contributeBy;
  private ParticipantInfoView participant;
  // For New SA
  private String celebrationId;

  public PurlCelebration()
  {

  }

  public long getId()
  {
    return id;
  }

  public void setId( long id )
  {
    this.id = id;
  }

  public String getMilestone()
  {
    return milestone;
  }

  public void setMilestone( String milestone )
  {
    this.milestone = milestone;
  }

  public String getContributeBy()
  {
    return contributeBy;
  }

  public void setContributeBy( String contributeBy )
  {
    this.contributeBy = contributeBy;
  }

  public ParticipantInfoView getParticipant()
  {
    return participant;
  }

  public void setParticipant( ParticipantInfoView participant )
  {
    this.participant = participant;
  }

  public String getCelebrationId()
  {
    return celebrationId;
  }

  public void setCelebrationId( String celebrationId )
  {
    this.celebrationId = celebrationId;
  }

}
