/**
 * 
 */

package com.biperf.core.domain.purl;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author poddutur
 *
 */
@JsonInclude( value = Include.NON_NULL )
public class PurlCelebrationView
{
  private Long id;
  private String lastName;
  private String firstName;
  private String avatarUrl = null;
  private String contributeUrl;
  private String viewUrl;
  private String promotion;
  private String expirationDate;
  private String anniversary;
  private String timeLeft;
  private String badgeUrl;
  @JsonProperty( "isToday" )
  private boolean isToday = false;
  private String positionType;
  private String primaryColor;
  private String secondaryColor;
  private String celebrationId;
  private String programNameCmxCode;
  private String celebLabelCmxCode;

  public String getProgramNameCmxCode()
  {
    return programNameCmxCode;
  }

  public void setProgramNameCmxCode( String programNameCmxCode )
  {
    this.programNameCmxCode = programNameCmxCode;
  }

  public String getCelebLabelCmxCode()
  {
    return celebLabelCmxCode;
  }

  public void setCelebLabelCmxCode( String celebLabelCmxCode )
  {
    this.celebLabelCmxCode = celebLabelCmxCode;
  }

  public Long getId()
  {
    return id;
  }

  public void setId( Long id )
  {
    this.id = id;
  }

  public String getLastName()
  {
    return lastName;
  }

  public void setLastName( String lastName )
  {
    this.lastName = lastName;
  }

  public String getFirstName()
  {
    return firstName;
  }

  public void setFirstName( String firstName )
  {
    this.firstName = firstName;
  }

  public String getAvatarUrl()
  {
    return avatarUrl;
  }

  public void setAvatarUrl( String avatarUrl )
  {
    this.avatarUrl = avatarUrl;
  }

  public String getContributeUrl()
  {
    return contributeUrl;
  }

  public void setContributeUrl( String contributeUrl )
  {
    this.contributeUrl = contributeUrl;
  }

  public String getViewUrl()
  {
    return viewUrl;
  }

  public void setViewUrl( String viewUrl )
  {
    this.viewUrl = viewUrl;
  }

  public String getPromotion()
  {
    return promotion;
  }

  public void setPromotion( String promotion )
  {
    this.promotion = promotion;
  }

  public String getExpirationDate()
  {
    return expirationDate;
  }

  public void setExpirationDate( String expirationDate )
  {
    this.expirationDate = expirationDate;
  }

  public String getTimeLeft()
  {
    return timeLeft;
  }

  public void setTimeLeft( String timeLeft )
  {
    this.timeLeft = timeLeft;
  }

  public String getAnniversary()
  {
    return anniversary;
  }

  public void setAnniversary( String anniversary )
  {
    this.anniversary = anniversary;
  }

  public boolean isToday()
  {
    return isToday;
  }

  public void setToday( boolean isToday )
  {
    this.isToday = isToday;
  }

  public String getPositionType()
  {
    return positionType;
  }

  public void setPositionType( String positionType )
  {
    this.positionType = positionType;
  }

  public String getPrimaryColor()
  {
    return primaryColor;
  }

  public void setPrimaryColor( String primaryColor )
  {
    this.primaryColor = primaryColor;
  }

  public String getSecondaryColor()
  {
    return secondaryColor;
  }

  public void setSecondaryColor( String secondaryColor )
  {
    this.secondaryColor = secondaryColor;
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
