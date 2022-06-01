/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/service/participant/UserAssociationRequest.java,v $
 */

package com.biperf.core.service.participant;

import com.biperf.core.domain.user.User;
import com.biperf.core.service.BaseAssociationRequest;

/**
 * UserAssociationRequest.
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
 * <td>zahler</td>
 * <td>May 19, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class UserAssociationRequest extends BaseAssociationRequest
{
  private int hydrateLevel = 0;

  public static final int ALL = 1;
  public static final int PHONE = 2;
  public static final int EMAIL = 3;
  public static final int ADDRESS = 4;
  public static final int NODE = 5;
  public static final int ROLE = 6;
  public static final int CHARACTERISTIC = 7;

  public UserAssociationRequest( int hydrateLevel )
  {
    this.hydrateLevel = hydrateLevel;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.AssociationRequest#execute(Object)
   * @param domainObject
   */
  public void execute( Object domainObject )
  {
    User user = (User)domainObject;

    switch ( hydrateLevel )
    {
      case ALL:
      {
        hydrateRoles( user );
        hydrateNodes( user );
        hydratePhones( user );
        hydrateAddresses( user );
        hydrateEmails( user );
        hydrateCharacteristics( user );
        break;
      }
      case PHONE:
      {
        hydratePhones( user );
        break;
      }
      case EMAIL:
      {
        hydrateEmails( user );
        break;
      }
      case ADDRESS:
      {
        hydrateAddresses( user );
        break;
      }
      case ROLE:
      {
        hydrateRoles( user );
        break;
      }
      case NODE:
      {
        hydrateNodes( user );
        break;
      }
      case CHARACTERISTIC:
      {
        hydrateCharacteristics( user );
        break;
      }
      default:
      {
        break;
      }
    } // switch
  }

  /**
   * @param user
   */
  private void hydrateRoles( User user )
  {
    initialize( user.getUserRoles() );
  }

  /**
   * @param user
   */
  private void hydrateNodes( User user )
  {
    initialize( user.getUserNodes() );
  }

  /**
   * @param user
   */
  private void hydratePhones( User user )
  {
    initialize( user.getUserPhones() );
  }

  /**
   * @param user
   */
  private void hydrateEmails( User user )
  {
    initialize( user.getUserEmailAddresses() );
  }

  /**
   * @param user
   */
  private void hydrateAddresses( User user )
  {
    initialize( user.getUserAddresses() );
  }

  /**
   * @param user
   */
  private void hydrateCharacteristics( User user )
  {
    initialize( user.getUserCharacteristics() );
  }

}
