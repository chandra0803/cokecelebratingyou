/*
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/BaseAssociationRequest.java,v $
 * (c) 2005 BI, Inc.  All rights reserved.
 */

package com.biperf.core.service;

import java.util.List;
import java.util.Set;

import org.hibernate.Hibernate;
import org.hibernate.proxy.HibernateProxy;

import com.biperf.core.domain.AuditCreateInterface;
import com.biperf.core.domain.BaseDomain;

/**
 * BaseAssociationRequest.
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
public abstract class BaseAssociationRequest implements AssociationRequest
{

  protected void initialize( BaseDomain domainObject )
  {
    Hibernate.initialize( domainObject );
  }

  protected void initialize( AuditCreateInterface domainObject )
  {
    Hibernate.initialize( domainObject );
  }

  protected void initialize( List listAssociation )
  {
    Hibernate.initialize( listAssociation );
  }

  protected void initialize( Set setAssociation )
  {
    Hibernate.initialize( setAssociation );
  }

  public static <T> T initializeAndUnproxy( T entity )
  {
    if ( entity == null )
    {
      throw new NullPointerException( "Entity passed for initialization is null" );
    }

    Hibernate.initialize( entity );
    if ( entity instanceof HibernateProxy )
    {
      entity = (T) ( (HibernateProxy)entity ).getHibernateLazyInitializer().getImplementation();
    }
    return entity;

  }

}
