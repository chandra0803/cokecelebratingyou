
package com.biperf.core.domain.gamification;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.objectpartners.cms.util.CmsResourceBundle;

@JsonInclude( value = Include.NON_NULL )
public class BadgeDetails implements Serializable
{
  private static final long serialVersionUID = 1L;

  @JsonProperty( "id" )
  private Long uniqueId;

  @JsonIgnore
  private Long badgeId;

  @JsonProperty( "earnCount" )
  private int earnCount = 0;

  @JsonProperty( "type" )
  private String badgeType;

  @JsonProperty( "name" )
  private String badgeName;

  @JsonProperty( "howToEarnText" )
  private String badgeDescription;

  private String imgLarge;
  private String img;
  private Boolean earned;
  private String dateEarned;
  private Long progressNumerator;
  private Long progressDenominator;
  private Boolean progressVisible;
  private String contestName;

  public Long getUniqueId()
  {
    return uniqueId;
  }

  public void setUniqueId( Long uniqueId )
  {
    this.uniqueId = uniqueId;
  }

  public Long getBadgeId()
  {
    return badgeId;
  }

  public void setBadgeId( Long badgeId )
  {
    this.badgeId = badgeId;
  }

  public String getBadgeType()
  {
    return badgeType;
  }

  public void setBadgeType( String badgeType )
  {
    this.badgeType = badgeType;
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

  public String getImgLarge()
  {
    return imgLarge;
  }

  public void setImgLarge( String imgLarge )
  {
    this.imgLarge = imgLarge;
  }

  public String getImg()
  {
    return img;
  }

  public void setImg( String img )
  {
    this.img = img;
  }

  public Boolean getEarned()
  {
    return earned;
  }

  public void setEarned( Boolean earned )
  {
    this.earned = earned;
  }

  public Boolean getProgressVisible()
  {
    return progressVisible;
  }

  public void setProgressVisible( Boolean progressVisible )
  {
    this.progressVisible = progressVisible;
  }

  public String getDateEarned()
  {
    return dateEarned;
  }

  public void setDateEarned( String dateEarned )
  {
    this.dateEarned = dateEarned;
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

  public int getEarnCount()
  {
    return earnCount;
  }

  public void setEarnCount( int earnCount )
  {
    this.earnCount = earnCount;
  }

  @JsonIgnore
  public String getBadgeNameTextFromCM()
  {
    String rulesText = "";
    if ( this.badgeName != null )
    {
      rulesText = CmsResourceBundle.getCmsBundle().getString( this.badgeName, BadgeRule.BADGE_RULES_CMASSET_TYPE_KEY );
    }

    return rulesText.trim();
  }

  @JsonIgnore
  public String getBadgeDescriptionTextFromCM()
  {
    String rulesText = "";
    if ( this.badgeDescription != null )
    {
      rulesText = CmsResourceBundle.getCmsBundle().getString( this.badgeDescription, BadgeRule.BADGE_RULES_CMASSET_TYPE_KEY );
    }

    if ( rulesText.startsWith( "???" ) )
    {
      return "";
    }

    return rulesText.trim();
  }

  public String getContestName()
  {
    return contestName;
  }

  public void setContestName( String contestName )
  {
    this.contestName = contestName;
  }

}
