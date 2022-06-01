/**
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javaui/com/biperf/core/ui/promotion/PromoMerchProgramLevelFormBean.java,v $
 * (c) 2005 BI, Inc.  All rights reserved.
 */

package com.biperf.core.ui.promotion;

import java.io.Serializable;

/**
 * PromoMerchProgramLevelFormBean.
 * <p>
 * <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>babu</td>
 * <td>Oct 17, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class PromoMerchProgramLevelFormBean implements Serializable
{
  private Long countryId = null;
  private Long promoMerchCountryId = null;
  private String countryAssetKey = null;
  private String programId = null;
  private long ordinalPosition;
  private String cmAssetKey;
  // Values from OM..
  private String levelName;
  private int minValue;
  private int maxValue;
  private Long promoMerchLevelId = null;
  private String omLevelName = null;
  private boolean newCountry = false;

  public Long getPromoMerchLevelId()
  {
    return promoMerchLevelId;
  }

  public void setPromoMerchLevelId( Long promoMerchLevelId )
  {
    this.promoMerchLevelId = promoMerchLevelId;
  }

  public String getLevelName()
  {
    return levelName;
  }

  public void setLevelName( String levelName )
  {
    this.levelName = levelName;
  }

  public Long getCountryId()
  {
    return countryId;
  }

  public void setCountryId( Long countryId )
  {
    this.countryId = countryId;
  }

  public String getOmLevelName()
  {
    return omLevelName;
  }

  public void setOmLevelName( String omLevelName )
  {
    this.omLevelName = omLevelName;
  }

  public String getCmAssetKey()
  {
    return cmAssetKey;
  }

  public void setCmAssetKey( String cmAssetKey )
  {
    this.cmAssetKey = cmAssetKey;
  }

  public int getMaxValue()
  {
    return maxValue;
  }

  public void setMaxValue( int maxValue )
  {
    this.maxValue = maxValue;
  }

  public int getMinValue()
  {
    return minValue;
  }

  public void setMinValue( int minValue )
  {
    this.minValue = minValue;
  }

  public boolean isNewCountry()
  {
    return newCountry;
  }

  public void setNewCountry( boolean newCountry )
  {
    this.newCountry = newCountry;
  }

  public long getOrdinalPosition()
  {
    return ordinalPosition;
  }

  public void setOrdinalPosition( long ordinalPosition )
  {
    this.ordinalPosition = ordinalPosition;
  }

  public String getCountryAssetKey()
  {
    return countryAssetKey;
  }

  public void setCountryAssetKey( String countryAssetKey )
  {
    this.countryAssetKey = countryAssetKey;
  }

  public String getProgramId()
  {
    return programId;
  }

  public void setProgramId( String programId )
  {
    this.programId = programId;
  }

  public Long getPromoMerchCountryId()
  {
    return promoMerchCountryId;
  }

  public void setPromoMerchCountryId( Long promoMerchCountryId )
  {
    this.promoMerchCountryId = promoMerchCountryId;
  }

}
