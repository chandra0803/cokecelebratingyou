/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/dao/proxy/hibernate/ProxyDAOImpl.java,v $
 */

package com.biperf.core.dao.proxy.hibernate;

import java.util.Iterator;
import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.Query;

import com.biperf.core.dao.BaseDAO;
import com.biperf.core.dao.proxy.ProxyDAO;
import com.biperf.core.domain.proxy.Proxy;
import com.biperf.core.domain.proxy.ProxyModule;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.utils.hibernate.HibernateUtil;

/**
 * ProxyDAOImpl.
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
public class ProxyDAOImpl extends BaseDAO implements ProxyDAO
{

  /**
   * Overridden from
   * 
   * @see com.biperf.core.dao.proxy.ProxyDAO#getProxyById(java.lang.Long)
   * @param id
   * @return Proxy
   */
  public Proxy getProxyById( Long id )
  {
    return getProxyByIdWithAssociations( id, null );
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.dao.proxy.ProxyDAO#getProxyByIdWithAssociations(java.lang.Long,
   *      com.biperf.core.service.AssociationRequestCollection)
   * @param id
   * @param associationRequestCollection
   * @return Proxy
   */
  public Proxy getProxyByIdWithAssociations( Long id, AssociationRequestCollection associationRequestCollection )
  {
    Proxy proxy = (Proxy)getSession().get( Proxy.class, id );

    if ( associationRequestCollection != null )
    {
      associationRequestCollection.process( proxy );
    }

    return proxy;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.dao.proxy.ProxyDAO#getProxyByUserAndProxyUserWithAssociations(java.lang.Long,
   *      java.lang.Long, com.biperf.core.service.AssociationRequestCollection)
   * @param userId
   * @param proxyUserId
   * @param associationRequestCollection
   * @return Proxy
   */
  public Proxy getProxyByUserAndProxyUserWithAssociations( Long userId, Long proxyUserId, AssociationRequestCollection associationRequestCollection )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.proxy.getProxyByUserAndProxyUser" );

    query.setLong( "userId", userId.longValue() );
    query.setLong( "proxyUserId", proxyUserId.longValue() );

    Proxy proxy = (Proxy)query.uniqueResult();

    if ( associationRequestCollection != null )
    {
      associationRequestCollection.process( proxy );
    }

    return proxy;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.dao.proxy.ProxyDAO#getProxiesByUserId(java.lang.Long)
   * @param userId
   * @return List
   */
  public List getProxiesByUserId( Long userId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.proxy.getProxiesByUserId" );

    query.setLong( "userId", userId.longValue() );
    List proxyList = query.list();

    Iterator proxyIter = proxyList.iterator();
    while ( proxyIter.hasNext() )
    {
      Proxy proxy = (Proxy)proxyIter.next();
      Hibernate.initialize( proxy.getProxyModules() );
      Iterator proxyModuleIter = proxy.getProxyModules().iterator();
      while ( proxyModuleIter.hasNext() )
      {
        ProxyModule proxyModule = (ProxyModule)proxyModuleIter.next();
        Hibernate.initialize( proxyModule.getProxyModulePromotions() );
      }
    }

    return proxyList;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.dao.proxy.ProxyDAO#getUsersByProxyUserId(java.lang.Long)
   * @param proxyUserId
   * @return List
   */
  public List getUsersByProxyUserId( Long proxyUserId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.proxy.getUsersByProxyUser" );

    query.setLong( "proxyUserId", proxyUserId.longValue() );

    return query.list();
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.dao.proxy.ProxyDAO#save(com.biperf.core.domain.proxy.Proxy)
   * @param proxy
   * @return Proxy
   */
  public Proxy save( Proxy proxy )
  {
    return (Proxy)HibernateUtil.saveOrUpdateOrShallowMerge( proxy );
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.dao.proxy.ProxyDAO#delete(com.biperf.core.domain.proxy.Proxy)
   * @param proxy
   */
  public void delete( Proxy proxy )
  {
    getSession().delete( proxy );
  }

  /**
   * Overridden from @see com.biperf.core.dao.proxy.ProxyDAO#getDuplicateProxyUserCount(java.lang.Long, java.lang.Long)
   * @param userId
   * @param proxyUserId
   * @return Long
   */
  public Long getDuplicateProxyUserCount( Long userId, Long proxyUserId )
  {
    Query query = getSession().createQuery( "select count(*)from Proxy p  where p.user.id = :userId  and p.proxyUser.id = :proxyUserId " );
    query.setLong( "userId", userId.longValue() );
    query.setLong( "proxyUserId", proxyUserId.longValue() );
    return (Long)query.uniqueResult();

  }
}
