
package com.biperf.core.domain.enums;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import com.biperf.core.domain.country.Country;
import com.biperf.util.StringUtils;

public class ShippingMethodType extends PickListItem
{

  /**
   * Asset name used in Content Manager
   */
  private static final String PICKLIST_ASSET = "picklist.shippingmethodtype";

  /** Shipping method is standard ground shipment */
  public static final String GROUND = "G";
  /** Shipping method is next day delivery */
  public static final String NEXTDAY = "N";
  /** Shipping method is second day delivery */
  public static final String SECONDDAY = "S";
  /** Shipping method is all types */
  public static final String ALL = "A";

  public static HashSet restrictedUSStates = new HashSet();
  static
  {
    restrictedUSStates.add( "us_aa" );
    restrictedUSStates.add( "us_ae" );
    restrictedUSStates.add( "us_ap" );
    restrictedUSStates.add( "us_pr" );
  }

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected ShippingMethodType()
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
    return getPickListFactory().getPickList( ShippingMethodType.class );
  }

  /**
   * Get the pick list for available shipping methods.
   * 
   * @return List
   */
  public static List getAvailableShippingMethods( String countryCode )
  {
    return getAvailableShippingMethods( countryCode, "" );
  }

  /**
   * Get the pick list for available shipping methods.
   * 
   * @return List
   */
  public static List getAvailableShippingMethods( String countryCode, String stateCode )
  {
    if ( countryCode.equalsIgnoreCase( Country.UNITED_STATES ) && ( StringUtils.isEmpty( stateCode ) || !restrictedUSStates.contains( stateCode ) ) )
    {
      return getPickListFactory().getPickList( ShippingMethodType.class );
    }

    List list = new ArrayList();
    list.add( ShippingMethodType.lookup( ShippingMethodType.GROUND ) );
    return list;
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return PickListItem
   */
  public static ShippingMethodType lookup( String code )
  {
    return (ShippingMethodType)getPickListFactory().getPickListItem( ShippingMethodType.class, code );
  }

  /**
   * Returns null if the defaultItem is not defined - or invalid
   * 
   * @return default PickListItem
   */
  public static ShippingMethodType getDefaultItem()
  {
    return (ShippingMethodType)getPickListFactory().getDefaultPickListItem( ShippingMethodType.class );
  }

  /**
   * Extract amount from the text information
   * 
   * @return amount
   */
  public float getShippingCost()
  {
    float amount = 0;
    if ( this.getName() != null && this.getName().length() > 0 )
    {
      int pos = this.getName().indexOf( '$' );
      int pos2 = this.getName().indexOf( ')' );
      if ( pos > 0 )
      {
        try
        {
          amount = Float.parseFloat( this.getName().substring( pos + 1, pos2 ) );
        }
        catch( NumberFormatException e )
        {
        }
      }
    }
    return amount;
  }

  public static boolean isValidShippingMethod( String s )
  {
    if ( StringUtils.isEmpty( s ) )
    {
      return false;
    }
    if ( lookup( s ) == null )
    {
      return false;
    }
    return true;
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
