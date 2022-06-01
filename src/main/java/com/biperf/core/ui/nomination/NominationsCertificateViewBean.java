
package com.biperf.core.ui.nomination;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude( value = Include.NON_NULL )
public class NominationsCertificateViewBean
{
  @JsonProperty( "nominees" )
  private List<NomineeViewBean> nominees = new ArrayList<NomineeViewBean>();
  @JsonProperty( "nominator" )
  private String nominator;
  @JsonProperty( "date_submitted" )
  private String dateSubmitted;
  @JsonProperty( "team_name" )
  private String teamName;
  @JsonProperty( "level_name" )
  private String levelName;
  @JsonProperty( "time_period_name" )
  private String timePeriodName;
  @JsonProperty( "promotion_name" )
  private String promotionName;
  @JsonProperty( "reason" )
  private String reason;
  @JsonProperty( "cert_type" )
  private String certType;
  @JsonProperty( "cert_background" )
  private String certBckGround;
  @JsonProperty( "position" )
  private String position;
  @JsonProperty( "presented_to" )
  private String presentedToText;
  @JsonProperty( "nominated_by" )
  private String nominatedByText;
  @JsonProperty( "on" )
  private String onText;
  @JsonProperty( "text_color" )
  private String textColor;

  public List<NomineeViewBean> getNominees()
  {
    return nominees;
  }

  public void setNominees( List<NomineeViewBean> nominees )
  {
    this.nominees = nominees;
  }

  public String getNominator()
  {
    return nominator;
  }

  public void setNominator( String nominator )
  {
    this.nominator = nominator;
  }

  public String getDateSubmitted()
  {
    return dateSubmitted;
  }

  public void setDateSubmitted( String dateSubmitted )
  {
    this.dateSubmitted = dateSubmitted;
  }

  public String getTeamName()
  {
    return teamName;
  }

  public void setTeamName( String teamName )
  {
    this.teamName = teamName;
  }

  public String getLevelName()
  {
    return levelName;
  }

  public void setLevelName( String levelName )
  {
    this.levelName = levelName;
  }

  public String getTimePeriodName()
  {
    return timePeriodName;
  }

  public void setTimePeriodName( String timePeriodName )
  {
    this.timePeriodName = timePeriodName;
  }

  public String getPromotionName()
  {
    return promotionName;
  }

  public void setPromotionName( String promotionName )
  {
    this.promotionName = promotionName;
  }

  public String getReason()
  {
    return reason;
  }

  public void setReason( String reason )
  {
    this.reason = reason;
  }

  public String getCertType()
  {
    return certType;
  }

  public void setCertType( String certType )
  {
    this.certType = certType;
  }

  public String getCertBckGround()
  {
    return certBckGround;
  }

  public void setCertBckGround( String certBckGround )
  {
    this.certBckGround = certBckGround;
  }

  public String getPosition()
  {
    return position;
  }

  public void setPosition( String position )
  {
    this.position = position;
  }

  public String getPresentedToText()
  {
    return presentedToText;
  }

  public void setPresentedToText( String presentedToText )
  {
    this.presentedToText = presentedToText;
  }

  public String getNominatedByText()
  {
    return nominatedByText;
  }

  public void setNominatedByText( String nominatedByText )
  {
    this.nominatedByText = nominatedByText;
  }

  public String getOnText()
  {
    return onText;
  }

  public void setOnText( String onText )
  {
    this.onText = onText;
  }

  public String getTextColor()
  {
    return textColor;
  }

  public void setTextColor( String textColor )
  {
    this.textColor = textColor;
  }

}
