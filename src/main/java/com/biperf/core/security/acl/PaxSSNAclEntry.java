/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/security/acl/PaxSSNAclEntry.java,v $
 */

package com.biperf.core.security.acl;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;

/**
 * PaxSSNAclEntry is the ACL that determines access to the pax Social Security Number for a given
 * user. Target is ignored and permissions are defined below.
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
public class PaxSSNAclEntry implements AclEntry, Serializable
{
  public static final String ACL_CODE = "PAX_SSN";

  public static final int PERM_VIEW_NONE = 1;
  public static final int PERM_VIEW_MASK = 2;
  public static final int PERM_VIEW_FULL = 4;
  public static final int PERM_EDIT = 8;

  // TODO move most of this to an AbstractAclEntry class
  // target is not used by this AclEntry
  private int mask;
  private String target;

  /**
   * Overridden from
   * 
   * @see com.biperf.core.security.acl.AclEntry#hasPermission(int, java.lang.Object)
   * @param permissionMask
   * @param objectToTest
   * @return boolean determination
   */
  public boolean hasPermission( int permissionMask, Object objectToTest )
  {
    return ( this.mask & permissionMask ) != 0;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.security.acl.AclEntry#setPermissionMask(java.lang.String)
   * @param permissionString
   */
  public void setPermissionMask( String permissionString )
  {
    int mask = 0;
    String[] permissions = StringUtils.split( permissionString );
    if ( permissions != null && permissions.length > 0 )
    {
      for ( int i = 0; i < permissions.length; i++ )
      {
        mask += parsePermission( permissions[i] );
      }
    }
    this.mask = mask;
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
   * @return String
   */
  public String getTarget()
  {
    return this.target;
  }

  protected int parsePermission( String input )
  {
    if ( input.equalsIgnoreCase( "view_none" ) )
    {
      return PERM_VIEW_NONE;
    }
    if ( input.equalsIgnoreCase( "view_mask" ) )
    {
      return PERM_VIEW_MASK;
    }
    if ( input.equalsIgnoreCase( "view_full" ) )
    {
      return PERM_VIEW_FULL;
    }
    if ( input.equalsIgnoreCase( "edit" ) )
    {
      return PERM_EDIT;
    }
    return 0;
  }
}
