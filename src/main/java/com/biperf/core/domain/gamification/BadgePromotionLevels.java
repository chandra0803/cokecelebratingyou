
package com.biperf.core.domain.gamification;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class BadgePromotionLevels
{

  private static final long serialVersionUID = 1L;
  // ---------------------------------------------------------------------------
  // Fields
  // ---------------------------------------------------------------------------
  @JsonIgnore
  private long minRange;
  @JsonIgnore
  private long maxRange;
  @JsonProperty( "id" )
  private long levelId;
  private String levelName;
  private long countryId;
  private String countryCode;

  @JsonIgnore
  private String badgeLibraryId;
  @JsonIgnore
  private String badgeName;
  @JsonIgnore
  private String badgeDescription;
  @JsonIgnore
  private String programId;

  // ---------------------------------------------------------------------------
  // Getter and Setter Methods
  // ---------------------------------------------------------------------------

  public long getMinRange()
  {
    return minRange;
  }

  public void setMinRange( long minRange )
  {
    this.minRange = minRange;
  }

  public long getMaxRange()
  {
    return maxRange;
  }

  public void setMaxRange( long maxRange )
  {
    this.maxRange = maxRange;
  }

  public String getBadgeLibraryId()
  {
    return badgeLibraryId;
  }

  public void setBadgeLibraryId( String badgeLibraryId )
  {
    this.badgeLibraryId = badgeLibraryId;
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

  public String getLevelName()
  {
    return levelName;
  }

  public void setLevelName( String levelName )
  {
    this.levelName = levelName;
  }

  public long getLevelId()
  {
    return levelId;
  }

  public void setLevelId( long levelId )
  {
    this.levelId = levelId;
  }

  public long getCountryId()
  {
    return countryId;
  }

  public void setCountryId( long countryId )
  {
    this.countryId = countryId;
  }

  public String getProgramId()
  {
    return programId;
  }

  public void setProgramId( String programId )
  {
    this.programId = programId;
  }

  public String getCountryCode()
  {
    return countryCode;
  }

  public void setCountryCode( String countryCode )
  {
    this.countryCode = countryCode;
  }

}
