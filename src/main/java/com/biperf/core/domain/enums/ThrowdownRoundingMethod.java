
package com.biperf.core.domain.enums;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@SuppressWarnings( "serial" )
public class ThrowdownRoundingMethod extends PickListItem
{

  /**
   * Asset name used in Content Manager
   */
  private static final String PICKLIST_ASSET = "picklist.throwdown.roundingmethod";

  public static final String STANDARD = "standard";
  public static final String UP = "up";
  public static final String DOWN = "down";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected ThrowdownRoundingMethod()
  {
    super();
  }

  /**
   * Get the pick list from content manager.
   * 
   * @return List
   */
  @SuppressWarnings( "rawtypes" )
  public static List getList()
  {
    return getPickListFactory().getPickList( ThrowdownRoundingMethod.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return PickListItem
   */
  public static ThrowdownRoundingMethod lookup( String code )
  {
    return (ThrowdownRoundingMethod)getPickListFactory().getPickListItem( ThrowdownRoundingMethod.class, code );
  }

  /**
   * Returns null if the defaultItem is not defined - or invalid
   * 
   * @return PayoutStructure
   */
  public static ThrowdownRoundingMethod getDefaultItem()
  {
    return (ThrowdownRoundingMethod)getPickListFactory().getDefaultPickListItem( ThrowdownRoundingMethod.class );
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

  public int getBigDecimalRoundingMode()
  {
    if ( getCode() != null && getCode().equals( STANDARD ) )
    {
      return BigDecimal.ROUND_HALF_UP;
    }
    if ( getCode() != null && getCode().equals( UP ) )
    {
      return BigDecimal.ROUND_UP;
    }
    return BigDecimal.ROUND_DOWN;
  }

  public RoundingMode getRoundingMode()
  {
    if ( getCode() != null && getCode().equals( STANDARD ) )
    {
      return RoundingMode.HALF_UP;
    }
    if ( getCode() != null && getCode().equals( UP ) )
    {
      return RoundingMode.UP;
    }
    return RoundingMode.DOWN;
  }

}
