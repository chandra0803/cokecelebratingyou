
package com.biperf.core.domain.enums;

import java.util.List;

public class CareerMomentsType extends PickListItem
{
  private static final long serialVersionUID = 1L;

  private static final String PICKLIST_ASSET = "picklist.careermoments";

  public static final String NEW_HIRE = "newhire";
  public static final String JOB_CHANGE = "jobchange";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected CareerMomentsType()
  {
    super();
  }

  /**
   * Get the pick list from content manager.
   *
   * @return List of AddressType
   */
  public static List getList()
  {
    return getPickListFactory().getPickList( CareerMomentsType.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   *
   * @param code
   * @return AddressType
   */
  public static CareerMomentsType lookup( String code )
  {
    return (CareerMomentsType)getPickListFactory().getPickListItem( CareerMomentsType.class, code );
  }

  /**
   * Overridden from
   *
   * @see com.biperf.core.domain.enums.PickListItem#getPickListAssetCode()
   * @return PICKLIST_ASSET
   */
  @Override
  public String getPickListAssetCode()
  {
    return PICKLIST_ASSET;
  }

}
