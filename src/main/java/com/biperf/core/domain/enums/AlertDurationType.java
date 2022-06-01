
package com.biperf.core.domain.enums;

import java.util.List;

public class AlertDurationType extends PickListItem
{

  /**
   * Asset name used in Content Manager
   */
  private static final String PICKLIST_ASSET = "picklist.duration.type";

  public static final String TWENTY_FOUR_HRS = "twentyfourhrs";
  public static final String FOURTY_EIGHT_HRS = "fourtyeighthrs";
  public static final String SEVEN_DAYS = "sevendays";

  protected AlertDurationType()
  {
    super();
  }

  public static List getList()
  {
    return getPickListFactory().getPickList( AlertDurationType.class );
  }

  public static AlertDurationType lookup( String code )
  {
    return (AlertDurationType)getPickListFactory().getPickListItem( AlertDurationType.class, code );
  }

  public String getPickListAssetCode()
  {
    return PICKLIST_ASSET;
  }
}
