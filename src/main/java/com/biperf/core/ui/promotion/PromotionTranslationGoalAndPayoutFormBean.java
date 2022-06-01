
package com.biperf.core.ui.promotion;

import java.io.Serializable;

public class PromotionTranslationGoalAndPayoutFormBean implements Serializable
{
  private int sequenceNumber;
  private String localeCode;
  private Long goalLevelId;
  private String name;
  private String description;
  private String nameKey;
  private String descriptionKey;
  private String goalLevelcmAssetCode;

  public String getName()
  {
    return name;
  }

  public void setName( String name )
  {
    this.name = name;
  }

  public String getDescription()
  {
    return description;
  }

  public void setDescription( String description )
  {
    this.description = description;
  }

  public String getNameKey()
  {
    return nameKey;
  }

  public void setNameKey( String nameKey )
  {
    this.nameKey = nameKey;
  }

  public String getDescriptionKey()
  {
    return descriptionKey;
  }

  public void setDescriptionKey( String descriptionKey )
  {
    this.descriptionKey = descriptionKey;
  }

  public String getGoalLevelcmAssetCode()
  {
    return goalLevelcmAssetCode;
  }

  public void setGoalLevelcmAssetCode( String goalLevelcmAssetCode )
  {
    this.goalLevelcmAssetCode = goalLevelcmAssetCode;
  }

  public Long getGoalLevelId()
  {
    return goalLevelId;
  }

  public void setGoalLevelId( Long goalLevelId )
  {
    this.goalLevelId = goalLevelId;
  }

  public String getLocaleCode()
  {
    return localeCode;
  }

  public void setLocaleCode( String localeCode )
  {
    this.localeCode = localeCode;
  }

  public int getSequenceNumber()
  {
    return sequenceNumber;
  }

  public void setSequenceNumber( int sequenceNumber )
  {
    this.sequenceNumber = sequenceNumber;
  }

}
