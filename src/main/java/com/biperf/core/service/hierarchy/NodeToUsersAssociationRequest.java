/*
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/service/hierarchy/NodeToUsersAssociationRequest.java,v $
 * (c) 2005 BI, Inc.  All rights reserved.
 */

package com.biperf.core.service.hierarchy;

import java.util.Iterator;

import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.domain.user.UserNode;
import com.biperf.core.service.BaseAssociationRequest;

/**
 * NodeToUsersAssociationRequest.
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
 * <td>sharma</td>
 * <td>May 17, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class NodeToUsersAssociationRequest extends BaseAssociationRequest
{

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.AssociationRequest#execute(Object)
   * @param domainObject
   */
  public void execute( Object domainObject )
  {
    Node node = (Node)domainObject;
    initialize( node.getUserNodes() ); // hydrate the association

    // also hydrate the userNode to User association
    for ( Iterator iter = node.getUserNodes().iterator(); iter.hasNext(); )
    {
      UserNode userNode = (UserNode)iter.next();
      initialize( userNode.getUser() );
    }
  }

}
