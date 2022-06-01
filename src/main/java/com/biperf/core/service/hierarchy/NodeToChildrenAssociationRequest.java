/*
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/service/hierarchy/NodeToChildrenAssociationRequest.java,v $
 * (c) 2005 BI, Inc.  All rights reserved.
 */

package com.biperf.core.service.hierarchy;

import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.service.BaseAssociationRequest;

/**
 * NodeToChildrenAssociationRequest <p/> <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>kumars</td>
 * <td>May 17, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class NodeToChildrenAssociationRequest extends BaseAssociationRequest
{
  public void execute( Object domainObject )
  {
    Node node = (Node)domainObject;

    initialize( node.getChildNodes() );
  }

}
