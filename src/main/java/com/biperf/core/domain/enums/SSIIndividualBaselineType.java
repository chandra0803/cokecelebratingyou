
package com.biperf.core.domain.enums;

import java.util.List;

/**
 * @author patelp
 * @since Jan 8, 2015
 * @version 1.0
 */
public class SSIIndividualBaselineType extends PickListItem
{

  private static final long serialVersionUID = 1L;
  /**
   * Asset name used in Content Manager
   */
  private static final String PICKLIST_ASSET = "picklist.ssi.individualbaselinetype";
  public static final String NO_BASELINE_CODE = "no";
  public static final String PERCENTAGE_OVER_BASELINE_CODE = "percent";
  public static final String CURRENCY_OVER_BASELINE_CODE = "currency";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected SSIIndividualBaselineType()
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
    return getPickListFactory().getPickList( SSIIndividualBaselineType.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return PickListItem
   */
  public static SSIIndividualBaselineType lookup( String code )
  {
    return (SSIIndividualBaselineType)getPickListFactory().getPickListItem( SSIIndividualBaselineType.class, code );
  }

  /**
   * 
   * @return true if this object represents the "No"; false
   *         otherwise.
   */
  public boolean isNo()
  {
    return getCode().equalsIgnoreCase( NO_BASELINE_CODE );
  }

  /**
   * 
   * @return true if this object represents the "Percentage over Baseline"; false
   *         otherwise.
   */
  public boolean isPercentageOverBaseline()
  {
    return getCode().equalsIgnoreCase( PERCENTAGE_OVER_BASELINE_CODE );
  }

  /**
    * 
    * @return true if this object represents the "Currency Over Baseline"; false
    *         otherwise.
    */
  public boolean isCurrencyOverBaseline()
  {
    return getCode().equalsIgnoreCase( CURRENCY_OVER_BASELINE_CODE );
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
