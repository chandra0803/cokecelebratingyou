
package com.biperf.core.domain.participant;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude( value = Include.NON_EMPTY )
public class ParticipantAlertView
{
  private Boolean displayAlert;
  private String alertTitle;
  private String alertLink;
  private String alertLinkText;
  private boolean openNewWindow;

  private String instantPollId; // added for instant poll
  private boolean isInstantPoll;
  private boolean isPlateauRedemption;

  // New SA
  private String celebrationId;
  private boolean saGiftCode;

  public boolean isSaGiftCode()
  {
    return saGiftCode;
  }

  public void setSaGiftCode( boolean saGiftCode )
  {
    this.saGiftCode = saGiftCode;
  }

  public String getCelebrationId()
  {
    return celebrationId;
  }

  public void setCelebrationId( String celebrationId )
  {
    this.celebrationId = celebrationId;
  }

  public Boolean getDisplayAlert()
  {
    return displayAlert;
  }

  public void setDisplayAlert( Boolean displayAlert )
  {
    this.displayAlert = displayAlert;
  }

  public String getAlertTitle()
  {
    return alertTitle;
  }

  public void setAlertTitle( String alertTitle )
  {
    this.alertTitle = alertTitle;
  }

  public String getAlertLink()
  {
    return alertLink;
  }

  public void setAlertLink( String alertLink )
  {
    this.alertLink = alertLink;
  }

  public String getAlertLinkText()
  {
    return alertLinkText;
  }

  public void setAlertLinkText( String alertLinkText )
  {
    this.alertLinkText = alertLinkText;
  }

  public boolean isOpenNewWindow()
  {
    return openNewWindow;
  }

  public void setOpenNewWindow( boolean openNewWindow )
  {
    this.openNewWindow = openNewWindow;
  }

  public String getInstantPollId()
  {
    return instantPollId;
  }

  public void setInstantPollId( String instantPollId )
  {
    this.instantPollId = instantPollId;
  }

  public boolean isInstantPoll()
  {
    return isInstantPoll;
  }

  public void setInstantPoll( boolean isInstantPoll )
  {
    this.isInstantPoll = isInstantPoll;
  }

  public void setPlateauRedemption( boolean isPlateauRedemption )
  {
    this.isPlateauRedemption = isPlateauRedemption;
  }

  @JsonProperty( "isPlateauRedemption" )
  public boolean isPlateauRedemption()
  {
    return isPlateauRedemption;
  }
}
