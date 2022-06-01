
package com.biperf.core.ui.profile;

import java.util.List;

import com.biperf.core.domain.participant.ParticipantAlertView;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Alerts
{
  private Integer numberOfMessages;
  private List<ParticipantAlertView> alerts;

  @JsonProperty( "numMessages" )
  public Integer getNumberOfMessages()
  {
    return numberOfMessages;
  }

  public void setNumberOfMessages( Integer numberOfMessages )
  {
    this.numberOfMessages = numberOfMessages;
  }

  public List<ParticipantAlertView> getAlerts()
  {
    return alerts;
  }

  public void setAlerts( List<ParticipantAlertView> alert )
  {
    this.alerts = alert;
  }

}
