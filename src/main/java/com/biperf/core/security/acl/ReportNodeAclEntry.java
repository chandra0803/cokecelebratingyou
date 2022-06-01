/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/security/acl/ReportNodeAclEntry.java,v $
 */

package com.biperf.core.security.acl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;

import com.biperf.core.domain.hierarchy.Hierarchy;
import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.service.hierarchy.HierarchyService;
import com.biperf.core.service.hierarchy.NodeService;
import com.biperf.core.utils.ApplicationContextFactory;

/**
 * ReportNodeAclEntry.
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
 * <td>Brian Repko</td>
 * <td>Dec 2, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ReportNodeAclEntry implements AclEntry
{
  public static final String ACL_CODE = "RPT_NODE";

  public static final int PERM_ALLOWS = 1;

  private static final Log log = LogFactory.getLog( ReportNodeAclEntry.class );

  // TODO move most of this to an AbstractAclEntry class
  private String target;

  /**
   * Overridden from
   * 
   * @see com.biperf.core.security.acl.AclEntry#hasPermission(int, java.lang.Object)
   * @param permissions
   * @param objectToTest
   * @return boolean if objectToTest is a subnode of target (as a Node)
   */
  public boolean hasPermission( int permissions, Object objectToTest )
  {
    // object must be a Node that is under the target Node
    // ignore permissions
    boolean result = false;
    if ( objectToTest instanceof Node )
    {
      Node subNode = (Node)objectToTest;
      Node targetNode = getTargetNode();
      String subNodePath = subNode.getPath();
      String targetNodePath = targetNode.getPath();
      result = subNodePath.startsWith( targetNodePath );
    }
    return result;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.security.acl.AclEntry#setTarget(java.lang.String)
   * @param target
   */
  public void setTarget( String target )
  {
    this.target = target;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.security.acl.AclEntry#setPermissionMask(java.lang.String)
   * @param permissionString
   */
  public void setPermissionMask( String permissionString )
  {
    // ignore the permission
  }

  public Node getTargetNode()
  {
    Node result = null;
    try
    {
      ApplicationContext ac = ApplicationContextFactory.getApplicationContext();
      NodeService nodeService = (NodeService)ac.getBean( NodeService.BEAN_NAME );
      HierarchyService hierarchyService = (HierarchyService)ac.getBean( HierarchyService.BEAN_NAME );
      Hierarchy primaryHierarchy = hierarchyService.getPrimaryHierarchy();
      result = nodeService.getNodeByNameAndHierarchy( target, primaryHierarchy );
    }
    catch( Exception e )
    {
      log.error( "Unable to get target Node", e );
      result = null;
    }
    return result;
  }

}
