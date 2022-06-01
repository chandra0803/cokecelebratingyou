/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/domain/enums/LanguageType.java,v $
 */

package com.biperf.core.domain.enums;

import java.util.List;
import java.util.Locale;

/**
 * AdminLanguageType.
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
public class AdminLanguageType extends PickListItem
{

  /**
   * Asset name used in Content Manager
   */
  public static final String PICKLIST_ASSET = "picklist.language";

  public static final String ENGLISH = "en_US";
  public static final String FRENCH = "fr_FR";
  public static final String SPANISH = "es_MX";
  public static final String GERMAN = "de_DE";
  public static final String CHINESE = "zh_CN";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected AdminLanguageType()
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

  /**
   * Get the pick list from content manager.
   * 
   * @return List
   */
  public static List getList()
  {
    return getPickListFactory().getPickList( AdminLanguageType.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return PickListItem
   */
  public static AdminLanguageType lookup( String code )
  {
    return (AdminLanguageType)getPickListFactory().getPickListItem( AdminLanguageType.class, code );
  }

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
