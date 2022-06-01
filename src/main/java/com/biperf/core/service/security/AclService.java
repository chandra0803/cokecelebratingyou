/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/service/security/AclService.java,v $
 */

package com.biperf.core.service.security;

import java.util.List;

import com.biperf.core.domain.user.Acl;
import com.biperf.core.service.SAO;

/**
 * Service interface for Acl.
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
 * <td>Apr 1, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public interface AclService extends SAO
{
  /**
   * BEAN_NAME is for applicationContext.xml reference
   */
  public static String BEAN_NAME = "aclService";

  /**
   * Get Acl by id.
   * 
   * @param id
   * @return Acl
   */
  public Acl getAclById( Long id );

  /**
   * Save Acl.
   * 
   * @param acl
   */
  public void saveAcl( Acl acl );

  /**
   * Search for a list of Acls with the given criteria.
   * 
   * @param name
   * @param category
   * @param appCode
   * @return List
   */
  public List searchAcl( String name, String category, String appCode );

  /**
   * Get all ACLS in the database.
   * 
   * @return List
   */
  public List getAll();

  /**
   * Get all available acls not in the aclList passed in.
   * 
   * @param aclList
   * @return List
   */
  public List getAvailableAclsNotInList( List aclList );

}
