
package com.biperf.core.ui.user;

public class ActivationField
{
  private Long participantIdentifierId;
  private String label;
  private String description;
  private String value;

  public Long getParticipantIdentifierId()
  {
    return participantIdentifierId;
  }

  public void setParticipantIdentifierId( Long participantIdentifierId )
  {
    this.participantIdentifierId = participantIdentifierId;
  }

  public String getLabel()
  {
    return label;
  }

  public void setLabel( String label )
  {
    this.label = label;
  }

  public String getDescription()
  {
    return description;
  }

  public void setDescription( String description )
  {
    this.description = description;
  }

  public String getValue()
  {
    return value;
  }

  public void setValue( String value )
  {
    this.value = value;
  }

}
