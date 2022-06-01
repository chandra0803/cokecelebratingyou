
package com.biperf.core.domain.gamification;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class BadgeLibrary
{
  // ---------------------------------------------------------------------------
  // Fields
  // ---------------------------------------------------------------------------
  @JsonProperty( "id" )
  private String badgeLibraryId;
  @JsonProperty( "name" )
  private String libraryname;
  @JsonIgnore
  private String earnedImageName;
  @JsonIgnore
  private String notEarnedImageName;
  @JsonIgnore
  private Integer isCustom;
  @JsonIgnore
  private Integer canEarnOnce;
  @JsonIgnore
  private String earnedImageLarge;
  @JsonIgnore
  private String earnedImageMedium;
  @JsonIgnore
  private String earnedImageSmall;

  // ---------------------------------------------------------------------------
  // Getter and Setter Methods
  // ---------------------------------------------------------------------------

  public String getLibraryname()
  {
    return libraryname;
  }

  public void setLibraryname( String libraryname )
  {
    this.libraryname = libraryname;
  }

  public String getEarnedImageName()
  {
    return earnedImageName;
  }

  public void setEarnedImageName( String earnedImageName )
  {
    this.earnedImageName = earnedImageName;
  }

  public String getNotEarnedImageName()
  {
    return notEarnedImageName;
  }

  public void setNotEarnedImageName( String notEarnedImageName )
  {
    this.notEarnedImageName = notEarnedImageName;
  }

  public Integer getIsCustom()
  {
    return isCustom;
  }

  public void setIsCustom( Integer isCustom )
  {
    this.isCustom = isCustom;
  }

  public Integer getCanEarnOnce()
  {
    return canEarnOnce;
  }

  public void setCanEarnOnce( Integer canEarnOnce )
  {
    this.canEarnOnce = canEarnOnce;
  }

  // ---------------------------------------------------------------------------
  // Equals, Hashcode, and To String Methods
  // ---------------------------------------------------------------------------

  public String getBadgeLibraryId()
  {
    return badgeLibraryId;
  }

  public void setBadgeLibraryId( String badgeLibraryId )
  {
    this.badgeLibraryId = badgeLibraryId;
  }

  public String getEarnedImageLarge()
  {
    return earnedImageLarge;
  }

  public void setEarnedImageLarge( String earnedImageLarge )
  {
    this.earnedImageLarge = earnedImageLarge;
  }

  public String getEarnedImageMedium()
  {
    return earnedImageMedium;
  }

  public void setEarnedImageMedium( String earnedImageMedium )
  {
    this.earnedImageMedium = earnedImageMedium;
  }

  public String getEarnedImageSmall()
  {
    return earnedImageSmall;
  }

  public void setEarnedImageSmall( String earnedImageSmall )
  {
    this.earnedImageSmall = earnedImageSmall;
  }

  public String toString()
  {
    return ToStringBuilder.reflectionToString( BadgeLibrary.class );
  }

}
