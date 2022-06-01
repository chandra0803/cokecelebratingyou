/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/dao/security/AclDAO.java,v $
 */

package com.biperf.core.dao.security;

import java.util.List;

import com.biperf.core.dao.DAO;
import com.biperf.core.domain.user.Acl;

/**
 * AclDAO is a DAO to manage User ACL.
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
 * <td>crosenquest</td>
 * <td>Mar 31, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 */
public interface AclDAO extends DAO
{
  /**
   * Abstract method to return Acl by the id
   * 
   * @param id
   * @return Acl
   */
  public abstract Acl getAclById( Long id );

  /**
   * @param userAcl
   * @return Acl
   */
  public abstract Acl saveAcl( Acl userAcl );

  /**
   * @param name
   * @param category
   * @param appCode
   * @return List of Acls
   */
  public abstract List searchAcl( String name, String category, String appCode );

  /**
   * Get a list of all Acls in the database.
   * 
   * @return List
   */
  public abstract List getAll();

}
