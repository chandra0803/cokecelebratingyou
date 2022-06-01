
package com.biperf.core.domain.enums;

import java.util.List;
import java.util.Locale;

/**
 * MonthNameType.
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
 * <td>kandhi</td>
 * <td>Apr 04, 2013</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class MonthNameType extends PickListItem
{

  /**
   * Asset name used in Content Manager
   */
  public static final String PICKLIST_ASSET = "picklist.monthname.type";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected MonthNameType()
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
    return getPickListFactory().getPickList( MonthNameType.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return PickListItem
   */
  public static MonthNameType lookup( String code )
  {
    return (MonthNameType)getPickListFactory().getPickListItem( MonthNameType.class, code );
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
