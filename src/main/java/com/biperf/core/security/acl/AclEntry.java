/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/security/acl/AclEntry.java,v $
 */

package com.biperf.core.security.acl;

/**
 * AclEntry is the interface that all AclEntry(s) need to implement.
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
 * <td>Sep 29, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public interface AclEntry
{
  public boolean hasPermission( int permissions, Object objectToTest );

  public void setTarget( String target );

  public void setPermissionMask( String permissionString );

}
