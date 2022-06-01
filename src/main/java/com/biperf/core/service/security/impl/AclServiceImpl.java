/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/service/security/impl/AclServiceImpl.java,v $
 */

package com.biperf.core.service.security.impl;

import java.util.List;

import com.biperf.core.dao.security.AclDAO;
import com.biperf.core.domain.user.Acl;
import com.biperf.core.service.security.AclService;

/**
 * Service to manage UserAcl interaction with DAO and business logic.
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
public class AclServiceImpl implements AclService
{

  /** AclDAO */
  AclDAO aclDAO;

  /**
   * Set the aclDAO through IoC.
   * 
   * @param aclDAO
   */
  public void setAclDAO( AclDAO aclDAO )
  {
    this.aclDAO = aclDAO;
  }

  /**
   * Gets the Acl by id. Overridden from
   * 
   * @see com.biperf.core.service.security.AclService#getAclById(java.lang.Long)
   * @param id
   * @return UserAcl
   */
  public Acl getAclById( Long id )
  {
    Acl acl = this.aclDAO.getAclById( id );

    return acl;
  }

  /**
   * Save or update the Acl parameter. Overridden from
   * 
   * @see com.biperf.core.service.security.AclService#saveAcl(com.biperf.core.domain.user.Acl)
   * @param acl
   */
  public void saveAcl( Acl acl )
  {
    this.aclDAO.saveAcl( acl );
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.security.AclService#searchAcl(java.lang.String, java.lang.String,
   *      java.lang.String)
   * @param name
   * @param category
   * @param appCode
   * @return List
   */
  public List searchAcl( String name, String category, String appCode )
  {
    return this.aclDAO.searchAcl( name, category, appCode );
  }

  /**
   * Get a list of All acls in the database. Overridden from
   * 
   * @see com.biperf.core.service.security.AclService#getAll()
   * @return List
   */
  public List getAll()
  {
    return this.aclDAO.getAll();
  }

  /**
   * Gets the available list of acls which aren't in the list passed in. Overridden from
   * 
   * @see com.biperf.core.service.security.AclService#getAvailableAclsNotInList(java.util.List)
   * @param aclList
   * @return List
   */
  public List getAvailableAclsNotInList( List aclList )
  {
    List availableAcls = getAll();

    availableAcls.removeAll( aclList );

    return availableAcls;
  }

}
