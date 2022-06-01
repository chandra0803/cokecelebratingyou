
package com.biperf.core.value.recognitionadvisor;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude( value = Include.NON_NULL )
public class RecognitionAdvisorBadgesValueBean implements Serializable
{
  private static final long serialVersionUID = 1L;

  @JsonProperty( "earned" )
  private Boolean earned;

  @JsonProperty( "badgeName" )
  private String badgeName;

  @JsonProperty( "badgeDescription" )
  private String badgeDescription;

  @JsonProperty( "progressNumerator" )
  private Long progressNumerator;

  @JsonProperty( "progressDenominator" )
  private Long progressDenominator;

  @JsonProperty( "img" )
  private String img;

  @JsonProperty( "dateEarned" )
  private String dateEarned;

  @JsonProperty( "badgeType" )
  private String badgeType;

  public Boolean getEarned()
  {
    return earned;
  }

  public void setEarned( Boolean earned )
  {
    this.earned = earned;
  }

  public String getBadgeName()
  {
    return badgeName;
  }

  public void setBadgeName( String badgeName )
  {
    this.badgeName = badgeName;
  }

  public String getBadgeDescription()
  {
    return badgeDescription;
  }

  public void setBadgeDescription( String badgeDescription )
  {
    this.badgeDescription = badgeDescription;
  }

  public Long getProgressNumerator()
  {
    return progressNumerator;
  }

  public void setProgressNumerator( Long progressNumerator )
  {
    this.progressNumerator = progressNumerator;
  }

  public Long getProgressDenominator()
  {
    return progressDenominator;
  }

  public void setProgressDenominator( Long progressDenominator )
  {
    this.progressDenominator = progressDenominator;
  }

  public String getImg()
  {
    return img;
  }

  public void setImg( String img )
  {
    this.img = img;
  }

  public String getDateEarned()
  {
    return dateEarned;
  }

  public void setDateEarned( String dateEarned )
  {
    this.dateEarned = dateEarned;
  }

  public String getBadgeType()
  {
    return badgeType;
  }

  public void setBadgeType( String badgeType )
  {
    this.badgeType = badgeType;
  }

}
