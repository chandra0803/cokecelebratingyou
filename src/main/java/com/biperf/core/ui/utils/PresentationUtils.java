/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/utils/PresentationUtils.java,v $
 */

package com.biperf.core.ui.utils;

import java.util.ArrayList;
import java.util.List;

import com.biperf.core.domain.enums.PickListItem;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.system.PropertySetItem;
import com.biperf.core.domain.user.User;
import com.biperf.core.security.acl.PaxSSNAclEntry;
import com.biperf.core.service.security.AuthorizationService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.utils.ServiceLocator;
import com.biperf.core.utils.UserManager;

/**
 * PresentationUtils.java <p/> <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>tennant</td>
 * <td>May 5, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class PresentationUtils
{
  /**
   * Converts an array of Strings into a List of Longs Will return an empty List if null is passed
   * in
   * 
   * @param ids
   * @return List
   */
  public static List convertStringArrayToListOfLongs( String[] ids )
  {
    List result = new ArrayList();
    if ( ids == null )
    {
      return result;
    }

    for ( int i = 0; i < ids.length; i++ )
    {
      String id = ids[i];
      result.add( new Long( id ) );
    }

    return result;
  }

  /**
   * Does a null safe get of the PickListItem display name
   * 
   * @param pickListItem
   * @return String
   */
  public static String getPickListTypeDescription( PickListItem pickListItem )
  {
    if ( pickListItem != null )
    {
      return pickListItem.getName();
    }
    return "";
  }

  /**
   * Does a null safe get of the PickListItem code
   * 
   * @param pickListItem
   * @return String
   */
  public static String getPickListTypeCode( PickListItem pickListItem )
  {
    if ( pickListItem != null )
    {
      return pickListItem.getCode();
    }
    return "";
  }

  /**
   * Gets the masked SSN for the user
   * 
   * @param user
   * @return String masked SSN
   */
  public static String getMaskedSSN( User user )
  {
    if ( user instanceof Participant )
    {
      AuthorizationService azn = (AuthorizationService)ServiceLocator.getService( AuthorizationService.BEAN_NAME );
      if ( azn.hasPermission( PaxSSNAclEntry.ACL_CODE, PaxSSNAclEntry.PERM_VIEW_FULL, user ) )
      {
        return user.getSsn();
      }
      if ( azn.hasPermission( PaxSSNAclEntry.ACL_CODE, PaxSSNAclEntry.PERM_VIEW_MASK, user ) )
      {
        return maskSSN( user.getSsn() );
      }
      if ( azn.hasPermission( PaxSSNAclEntry.ACL_CODE, PaxSSNAclEntry.PERM_VIEW_NONE, user ) )
      {
        return "XXX-XX-XXXX";
      }

      //
      // No ACL - use the system default
      //
      PropertySetItem item = getSystemVariableService().getPropertyByName( SystemVariableService.SSN_DEFAULT_VIEW );
      if ( item != null )
      {
        String value = item.getStringVal();
        if ( value.equalsIgnoreCase( "view_full" ) )
        {
          return user.getSsn();
        }
        if ( value.equalsIgnoreCase( "view_mask" ) )
        {
          return maskSSN( user.getSsn() );
        }
      }
      return "XXX-XX-XXXX";
    }
    return "XXX-XX-XXXX";
  }

  private static String maskSSN( String input )
  {
    if ( input != null && input.length() == 9 )
    {
      return "XXX-XX-" + input.substring( 5 );
    }
    return input;
  }

  public static String trimLength( String value, int size, String suffix )
  {
    return value != null ? value.length() > size ? suffix != null ? value.substring( 0, size - suffix.length() - 1 ) + suffix : value.substring( 0, size - 1 ) : value : "";
  }

  public static boolean hasSSNEditPermission( User user )
  {
    if ( UserManager.getUser().isParticipant() )
    {
      AuthorizationService azn = (AuthorizationService)ServiceLocator.getService( AuthorizationService.BEAN_NAME );
      if ( azn.hasPermission( PaxSSNAclEntry.ACL_CODE, PaxSSNAclEntry.PERM_EDIT, user ) )
      {
        return true;
      }
      return false;
    }
    return true;
  }

  public static int getDisplayTablePageSize( int recordCount )
  {
    int maxSinglePageSize = getSystemVariableService().getPropertyByName( SystemVariableService.DISPLAY_TABLE_MAX_PER_SINGLE_PAGE ).getIntVal();

    if ( recordCount <= maxSinglePageSize )
    {
      return maxSinglePageSize;
    }

    return getSystemVariableService().getPropertyByName( SystemVariableService.DISPLAY_TABLE_MAX_PER_MULTI_PAGE ).getIntVal();
  }

  private static SystemVariableService getSystemVariableService()
  {
    return (SystemVariableService)ServiceLocator.getService( SystemVariableService.BEAN_NAME );
  }
}
