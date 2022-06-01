
package com.biperf.core.ui.promotion;

import java.io.Serializable;

@SuppressWarnings( "serial" )
public class PromotionTranslationDivisionFormBean implements Serializable
{
  private String localeCode;
  private Long divisionId;
  private String name;
  private String nameKey;
  private String divisionNameCmAssetCode;

  public String getName()
  {
    return name;
  }

  public void setName( String name )
  {
    this.name = name;
  }

  public String getNameKey()
  {
    return nameKey;
  }

  public void setNameKey( String nameKey )
  {
    this.nameKey = nameKey;
  }

  public String getLocaleCode()
  {
    return localeCode;
  }

  public void setLocaleCode( String localeCode )
  {
    this.localeCode = localeCode;
  }

  public Long getDivisionId()
  {
    return divisionId;
  }

  public void setDivisionId( Long divisionId )
  {
    this.divisionId = divisionId;
  }

  public String getDivisionNameCmAssetCode()
  {
    return divisionNameCmAssetCode;
  }

  public void setDivisionNameCmAssetCode( String divisionNameCmAssetCode )
  {
    this.divisionNameCmAssetCode = divisionNameCmAssetCode;
  }

}
