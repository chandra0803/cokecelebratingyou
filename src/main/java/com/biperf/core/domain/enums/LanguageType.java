/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/domain/enums/LanguageType.java,v $
 */

package com.biperf.core.domain.enums;

import java.util.List;
import java.util.Locale;

/**
 * LanguageType.
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
 * <td>dunne</td>
 * <td>Apr 14, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class LanguageType extends PickListItem
{

  /**
   * Asset name used in Content Manager
   */
  public static final String PICKLIST_ASSET = "default.locale";

  public static final String ENGLISH = "en_US";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected LanguageType()
  {
    super();
  }

  /**
   * Get the self name from the corresponding Locale object.
   * 
   * @return String
   */
  public String getSelfName()
  {
    String selfname = "";
    Locale[] locales = Locale.getAvailableLocales();
    for ( int i = 0; i < locales.length; i++ )
    {
      Locale locale = locales[i];
      if ( locale.toString().equals( getCode() ) )
      {
        selfname = locale.getDisplayName( locale );
        break;
      }
    }
    return selfname;
  }

  public String getLanguageCode()
  {
    String localeCode = this.getCode();
    return getLanguageFrom( localeCode );
  }

  public static String getLanguageFrom( String localeCode )
  {
    if ( localeCode == null || localeCode.trim().length() == 0 )
    {
      return "";
    }

    if ( localeCode.indexOf( "_" ) < 0 )
    {
      return localeCode;
    }

    return localeCode.substring( 0, localeCode.indexOf( "_" ) );
  }

  /**
   * Get the pick list from content manager.
   * 
   * @return List
   */
  public static List getList()
  {
    return getPickListFactory().getPickList( LanguageType.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return PickListItem
   */
  public static LanguageType lookup( String code )
  {
    return (LanguageType)getPickListFactory().getPickListItem( LanguageType.class, code );
  }

  /**
   * Returns null if the defaultItem is not defined - or invalid
   * 
   * @return default PickListItem
   */
  // public static LanguageType getDefaultItem()
  // {
  // return (LanguageType)getPickListFactory().getDefaultPickListItem( LanguageType.class );
  // }
  /**
   * Overridden from
   * 
   * @see com.biperf.core.domain.enums.PickListItem#getPickListAssetCode()
   * @return PICKLIST_ASSET
   */
  public String getPickListAssetCode()
  {
    return PICKLIST_ASSET;
  }

}
