/*
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/service/participant/UserNodeHistoryAssociationRequest.java,v $
 * (c) 2005 BI, Inc.  All rights reserved.
 */

package com.biperf.core.service.participant;

import com.biperf.core.domain.user.UserNodeHistory;
import com.biperf.core.service.BaseAssociationRequest;

/**
 * UserNodeHistoryAssociationRequest.
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
 * <td>attada</td>
 * <td>Dec 17, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class UserNodeHistoryAssociationRequest extends BaseAssociationRequest
{
  private int hydrateLevel = 0;

  public static final int ALL = 1;
  public static final int USER = 2;
  public static final int NODE = 3;
  public static final int HIERARCHY = 4;

  public UserNodeHistoryAssociationRequest( int hydrateLevel )
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
    UserNodeHistory userNodeHistory = (UserNodeHistory)domainObject;
    initialize( userNodeHistory );
    switch ( hydrateLevel )
    {
      case ALL:
      {
        initialize( userNodeHistory.getNode() ); // hydrate the association to Node
        if ( userNodeHistory.getNode().getHierarchy() != null )
        {
          initialize( userNodeHistory.getNode().getHierarchy() ); // hydrate the association to Node
          // hierarchy
        }
        initialize( userNodeHistory.getUser() ); // hydrate the association to User
        break;
      }

      case USER:
      {
        initialize( userNodeHistory.getUser() ); // hydrate the association to User
        break;
      }
      case HIERARCHY:
      {
        initialize( userNodeHistory.getNode() ); // hydrate the association to Node
        if ( userNodeHistory.getNode().getHierarchy() != null )
        {
          initialize( userNodeHistory.getNode().getHierarchy() ); // hydrate the association to Node
          // hierarchy
        }
        break;
      }

      default:
      {
        break;
      }
    } // switch
  }
}
