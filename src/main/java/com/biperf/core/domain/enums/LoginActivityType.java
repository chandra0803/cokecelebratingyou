
package com.biperf.core.domain.enums;

import java.util.List;

public class LoginActivityType extends PickListItem
{

  /**
   * Asset name used in Content Manager
   */
  public static final String PICKLIST_ASSET = "picklist.loginactivity.type";

  public static final String HAVE_LOGGED_IN = "loggedIn";
  public static final String HAVE_NOT_LOGGED_IN = "haveNotLoggedIn";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected LoginActivityType()
  {
    super();
  }

  /**
   * Get the pick list from content manager.
   * 
   * @return List of LoginActivityType
   */
  public static List getList()
  {
    return getPickListFactory().getPickList( LoginActivityType.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return LoginActivityType
   */
  public static LoginActivityType lookup( String code )
  {
    return (LoginActivityType)getPickListFactory().getPickListItem( LoginActivityType.class, code );
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
