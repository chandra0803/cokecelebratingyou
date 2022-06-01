
package com.biperf.core.domain.enums;

import java.util.List;

/**
 * @author dudam
 * @since Nov 4, 2014
 * @version 1.0
 */
public class SSIActivityMeasureType extends PickListItem
{

  private static final long serialVersionUID = 1L;

  /**
   * Asset name used in Content Manager
   */
  private static final String PICKLIST_ASSET = "picklist.ssi.activitymeasure";

  public static final String CURRENCY_CODE = "currency";
  public static final String UNIT_CODE = "units";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected SSIActivityMeasureType()
  {
    super();
  }

  /**
   * Get the pick list from content manager.
   * 
   * @return List
   */
  public static List getList()
  {
    return getPickListFactory().getPickList( SSIActivityMeasureType.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return PickListItem
   */
  public static SSIActivityMeasureType lookup( String code )
  {
    return (SSIActivityMeasureType)getPickListFactory().getPickListItem( SSIActivityMeasureType.class, code );
  }

  /**
   * 
   * @return true if this object represents the "currency"; false
   *         otherwise.
   */
  public boolean isCurrency()
  {
    return getCode().equalsIgnoreCase( CURRENCY_CODE );
  }

  /**
   * 
   * @return true if this object represents the "unit"; false
   *         otherwise.
   */
  public boolean isUnit()
  {
    return getCode().equalsIgnoreCase( UNIT_CODE );
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
