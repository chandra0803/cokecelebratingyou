/*
 * (c) 2007 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/domain/promotion/PromoMerchProgramLevel.java,v $
 */

package com.biperf.core.domain.promotion;

import com.biperf.core.domain.BaseDomain;
import com.objectpartners.cms.util.CmsResourceBundle;

/**
 * PromoMerchProgramLevel.
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
 * <td>Babu</td>
 * <td>July 19, 2007</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 *          babu Exp $
 */
public class PromoMerchProgramLevel extends BaseDomain
{

  public static final String SPOTLIGHT_LEVEL_NAME_KEY = "LEVEL_NAME";
  public static final String SPOTLIGHT_LEVEL_SECTION_CODE = "spotlight_levels_data";// Section Code
  public static final String SPOTLIGHT_LEVEL_CMASSET_TYPE_NAME = "_SPOTLIGHT_LEVELS_DATA"; // Type
                                                                                           // Name
  public static final String SPOTLIGHT_LEVEL_CMASSET_NAME = "_SPOTLIGHT_LEVELS_DATA"; // CM ASSET
                                                                                      // NAME
                                                                                      // //String
  // Key
  public static final String SPOTLIGHT_LEVEL_CMASSET_PREFIX = "spotlight_levels_data."; // Code

  private PromoMerchCountry promoMerchCountry;

  private String levelName;
  private String displayLevelName;
  private String cmAssetKey;
  private int minValue;
  private int maxValue;
  private long ordinalPosition;
  private String programId;

  /**
   * Default Constructor
   */
  public PromoMerchProgramLevel()
  {
    super();
  }

  /**
   * Construct this with a user and a node.
   * 
   * @param user
   * @param node
   */
  public PromoMerchProgramLevel( String levelName, String cmAssetKey )
  {
    super();
    this.levelName = levelName;
    this.cmAssetKey = cmAssetKey;
  }

  public String getCmAssetKey()
  {
    return cmAssetKey;
  }

  public void setCmAssetKey( String cmAssetKey )
  {
    this.cmAssetKey = cmAssetKey;
  }

  public String getLevelName()
  {
    return levelName;
  }

  public void setLevelName( String levelName )
  {
    this.levelName = levelName;
  }

  public PromoMerchCountry getPromoMerchCountry()
  {
    return promoMerchCountry;
  }

  public void setPromoMerchCountry( PromoMerchCountry promoMerchCountry )
  {
    this.promoMerchCountry = promoMerchCountry;
  }

  public int hashCode()
  {
    final int PRIME = 31;
    int result = 1;
    result = PRIME * result + ( cmAssetKey == null ? 0 : cmAssetKey.hashCode() );
    result = PRIME * result + ( levelName == null ? 0 : levelName.hashCode() );
    result = PRIME * result + ( promoMerchCountry == null ? 0 : promoMerchCountry.hashCode() );
    return result;
  }

  public boolean equals( Object obj )
  {
    if ( this == obj )
    {
      return true;
    }
    if ( obj == null )
    {
      return false;
    }
    if ( getClass() != obj.getClass() )
    {
      return false;
    }
    final PromoMerchProgramLevel other = (PromoMerchProgramLevel)obj;
    if ( cmAssetKey == null )
    {
      if ( other.cmAssetKey != null )
      {
        return false;
      }
    }
    else if ( !cmAssetKey.equals( other.cmAssetKey ) )
    {
      return false;
    }
    if ( levelName == null )
    {
      if ( other.levelName != null )
      {
        return false;
      }
    }
    else if ( !levelName.equals( other.levelName ) )
    {
      return false;
    }
    if ( promoMerchCountry == null )
    {
      if ( other.promoMerchCountry != null )
      {
        return false;
      }
    }
    else if ( !promoMerchCountry.equals( other.promoMerchCountry ) )
    {
      return false;
    }
    return true;
  }

  public PromoMerchProgramLevel deepCopy()
  {
    PromoMerchProgramLevel clone = new PromoMerchProgramLevel();
    clone.setLevelName( levelName );
    clone.setDisplayLevelName( getDisplayLevelName() );
    clone.setCmAssetKey( null );
    clone.setMinValue( minValue );
    clone.setMaxValue( maxValue );
    clone.setOrdinalPosition( ordinalPosition );
    clone.setProgramId( programId );
    return clone;
  }

  public String getDisplayLevelName()
  {
    if ( displayLevelName == null || displayLevelName.trim().length() == 0 )
    {
      displayLevelName = CmsResourceBundle.getCmsBundle().getString( getCmAssetKey(), SPOTLIGHT_LEVEL_NAME_KEY );
    }
    return displayLevelName;
  }

  public void setDisplayLevelName( String displayLevelName )
  {
    this.displayLevelName = displayLevelName;
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

  public long getOrdinalPosition()
  {
    return ordinalPosition;
  }

  public void setOrdinalPosition( long ordinalPosition )
  {
    this.ordinalPosition = ordinalPosition;
  }

  public String getProgramId()
  {
    return programId;
  }

  public void setProgramId( String programId )
  {
    this.programId = programId;
  }

}
