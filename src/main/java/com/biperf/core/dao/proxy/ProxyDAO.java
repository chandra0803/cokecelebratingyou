/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/dao/proxy/ProxyDAO.java,v $
 */

package com.biperf.core.dao.proxy;

import java.util.List;

import com.biperf.core.dao.DAO;
import com.biperf.core.domain.proxy.Proxy;
import com.biperf.core.service.AssociationRequestCollection;

/**
 * ProxyDAO.
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
 * <td>sedey</td>
 * <td>Nov 17, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 */
public interface ProxyDAO extends DAO
{
  /**
   * BEAN_NAME
   */
  public static final String BEAN_NAME = "proxyDAO";

  /**
   * Get the Proxy from the database by the id.
   * 
   * @param id
   * @return Proxy
   */
  public Proxy getProxyById( Long id );

  /**
   * Get the Proxy with associations from the database by the id.
   * 
   * @param id
   * @param associationRequestCollection
   * @return Proxy
   */
  public Proxy getProxyByIdWithAssociations( Long id, AssociationRequestCollection associationRequestCollection );

  /**
   * Get the Proxy with associations from the database By User and Proxy User.
   * 
   * @param userId
   * @param proxyUserId
   * @param associationRequestCollection
   * @return Proxy
   */
  public Proxy getProxyByUserAndProxyUserWithAssociations( Long userId, Long proxyUserId, AssociationRequestCollection associationRequestCollection );

  /**
   * Get a list of proxies by userId
   * 
   * @param userId
   * @return List
   */
  public List getProxiesByUserId( Long userId );

  /**
   * Get the List of Users from the database by the proxy user.
   * 
   * @param proxyUserId
   * @return List
   */
  public List getUsersByProxyUserId( Long proxyUserId );

  /**
   * Saves the proxy to the database.
   * 
   * @param proxy
   * @return Proxy
   */
  public Proxy save( Proxy proxy );

  /**
   * Deletes the Proxy from the database.
   * 
   * @param proxy
   */
  public void delete( Proxy proxy );

  /**
   * @param userId
   * @param proxyUserId
   * @return Long
   */
  public Long getDuplicateProxyUserCount( Long userId, Long proxyUserId );
}
