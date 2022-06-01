/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/utils/hibernate/HibernateChainedInterceptor.java,v $
 *
 */

package com.biperf.core.utils.hibernate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.CallbackException;
import org.hibernate.EntityMode;
import org.hibernate.Interceptor;
import org.hibernate.Transaction;
import org.hibernate.type.Type;

/**
 * HibernateChainedInterceptor <p/> <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>dunne</td>
 * <td>Jul 13, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 * @author dunne
 *
 */
public class HibernateChainedInterceptor implements Interceptor
{
  // Interceptors to be chained
  private Interceptor[] interceptors;

  /**
   * Constructor
   */
  public HibernateChainedInterceptor()
  {
    super();
  }

  /**
   * Constructor
   * 
   * @param interceptors An array of interceptors
   */
  public HibernateChainedInterceptor( Interceptor[] interceptors )
  {
    super();
    this.interceptors = interceptors;
  }

  /**
   * @see org.hibernate.Interceptor#onLoad(java.lang.Object, java.io.Serializable,
   *      java.lang.Object[], java.lang.String[], org.hibernate.type.Type[])
   */
  public boolean onLoad( Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types ) throws CallbackException
  {
    boolean result = false;
    for ( int i = 0; i < interceptors.length; i++ )
    {
      if ( interceptors[i].onLoad( entity, id, state, propertyNames, types ) )
      {
        /*
         * Returns true if one interceptor in the chain has modified the object state
         */
        result = true;
      }
    }
    return result;
  }

  /**
   * @see org.hibernate.Interceptor#onFlushDirty(java.lang.Object, java.io.Serializable,
   *      java.lang.Object[], java.lang.Object[], java.lang.String[], org.hibernate.type.Type[])
   */
  public boolean onFlushDirty( Object entity, Serializable id, Object[] currentState, Object[] previousState, String[] propertyNames, Type[] types ) throws CallbackException
  {
    boolean result = false;
    for ( int i = 0; i < interceptors.length; i++ )
    {
      if ( interceptors[i].onFlushDirty( entity, id, currentState, previousState, propertyNames, types ) )
      {
        /*
         * Returns true if one interceptor in the chain has modified the object current state
         */
        result = true;
      }
    }
    return result;
  }

  /**
   * @see org.hibernate.Interceptor#onSave(java.lang.Object, java.io.Serializable,
   *      java.lang.Object[], java.lang.String[], org.hibernate.type.Type[])
   */
  public boolean onSave( Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types ) throws CallbackException
  {
    boolean result = false;
    for ( int i = 0; i < interceptors.length; i++ )
    {
      if ( interceptors[i].onSave( entity, id, state, propertyNames, types ) )
      {
        /*
         * Returns true if one interceptor in the chain has modified the object state
         */
        result = true;
      }
    }
    return result;
  }

  /**
   * @see org.hibernate.Interceptor#onDelete(java.lang.Object, java.io.Serializable,
   *      java.lang.Object[], java.lang.String[], org.hibernate.type.Type[])
   */
  public void onDelete( Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types ) throws CallbackException
  {
    for ( int i = 0; i < interceptors.length; i++ )
    {
      interceptors[i].onDelete( entity, id, state, propertyNames, types );
    }
  }

  public void onCollectionRecreate( Object collection, Serializable key ) throws CallbackException
  {
    // empty
  }

  public void onCollectionRemove( Object collection, Serializable key ) throws CallbackException
  {
    // empty
  }

  public void onCollectionUpdate( Object collection, Serializable key ) throws CallbackException
  {
    // empty
  }

  /**
   * @see Interceptor#postFlush(Iterator)
   */
  public void postFlush( Iterator entities ) throws CallbackException
  {
    List entityList = createList( entities );
    for ( int i = 0; i < interceptors.length; i++ )
    {
      interceptors[i].postFlush( entityList.iterator() );
    }
  }

  /**
   * Called to distinguish between transient and detached entities. The return value determines the
   * state of the entity with respect to the current session.
   * <ul>
   * <li><tt>Boolean.TRUE</tt> - the entity is transient
   * <li><tt>Boolean.FALSE</tt> - the entity is detached
   * <li><tt>null</tt> - Hibernate uses the <tt>unsaved-value</tt> mapping and other heuristics
   * to determine if the object is unsaved
   * </ul>
   * 
   * @param entity a transient or detached entity
   * @return Boolean or <tt>null</tt> to choose default behaviour
   */
  public Boolean isTransient( Object entity )
  {
    for ( int i = 0; i < interceptors.length; i++ )
    {
      Boolean result = interceptors[i].isTransient( entity );
      if ( result != null )
      {
        /*
         * If any interceptor has returned something not null, stop the chain
         */
        return result;
      }
    }
    return null;
  }

  /**
   * @see Interceptor#preFlush(Iterator)
   */
  public void preFlush( Iterator entities ) throws CallbackException
  {
    List entityList = createList( entities );
    for ( int i = 0; i < interceptors.length; i++ )
    {
      interceptors[i].preFlush( entityList.iterator() );
    }
  }

  /**
   * Creates and returns a new <code>List</code> containing all the elements returned from the
   * <code>Iterator</code>.
   * 
   * @param iterator The iterator.
   * @return A <code>List</code> of the iterator's elements.
   */
  private List createList( Iterator iterator )
  {
    List list = new ArrayList();
    while ( iterator.hasNext() )
    {
      list.add( iterator.next() );
    }
    return list;
  }

  /**
   * @see org.hibernate.Interceptor#findDirty(java.lang.Object, java.io.Serializable,
   *      java.lang.Object[], java.lang.Object[], java.lang.String[], org.hibernate.type.Type[])
   */
  public int[] findDirty( Object entity, Serializable id, Object[] currentState, Object[] previousState, String[] propertyNames, Type[] types )
  {
    int[] result = null;
    for ( int i = 0; i < interceptors.length; i++ )
    {
      result = interceptors[i].findDirty( entity, id, currentState, previousState, propertyNames, types );
      if ( result != null )
      {
        /*
         * If any interceptor has returned something not null, stop the chain
         */
        break;
      }
    }
    return result;
  }

  /**
   * Instantiate the entity class. Return <tt>null</tt> to indicate that Hibernate should use the
   * default constructor of the class. The identifier property of the returned instance should be
   * initialized with the given identifier.
   * 
   * @param entityName the name of the entity
   * @param entityMode The type of entity instance to be returned.
   * @param id the identifier of the new instance
   * @return an instance of the class, or <tt>null</tt> to choose default behaviour
   * @throws CallbackException
   */
  public Object instantiate( String entityName, EntityMode entityMode, Serializable id ) throws CallbackException
  {
    return null;
  }

  /**
   * Get the entity name for a persistent or transient instance
   * 
   * @param object an entity instance
   * @return the name of the entity
   * @throws CallbackException
   */
  public String getEntityName( Object object ) throws CallbackException
  {
    return null;
  }

  /**
   * Get a fully loaded entity instance that is cached externally
   * 
   * @param entityName the name of the entity
   * @param id the instance identifier
   * @return a fully initialized entity
   * @throws org.hibernate.CallbackException
   */
  public Object getEntity( String entityName, Serializable id ) throws CallbackException
  {
    return null;
  }

  /**
   * Called when a Hibernate transaction is begun via the Hibernate <tt>Transaction</tt> API. Will
   * not be called if transactions are being controlled via some other mechanism (CMT, for example).
   * Overridden from
   * 
   * @see org.hibernate.Interceptor#afterTransactionBegin(org.hibernate.Transaction)
   * @param tx
   */
  public void afterTransactionBegin( Transaction tx )
  {
    for ( int i = 0; i < interceptors.length; i++ )
    {
      interceptors[i].afterTransactionBegin( tx );
    }
  }

  /**
   * Called before a transaction is committed (but not before rollback). Overridden from
   * 
   * @see org.hibernate.Interceptor#beforeTransactionCompletion(org.hibernate.Transaction)
   * @param tx
   */
  public void beforeTransactionCompletion( Transaction tx )
  {
    for ( int i = 0; i < interceptors.length; i++ )
    {
      interceptors[i].beforeTransactionCompletion( tx );
    }
  }

  /**
   * Called after a transaction is committed or rolled back. Overridden from
   * 
   * @see org.hibernate.Interceptor#afterTransactionCompletion(org.hibernate.Transaction)
   * @param tx
   */
  public void afterTransactionCompletion( Transaction tx )
  {
    for ( int i = 0; i < interceptors.length; i++ )
    {
      interceptors[i].afterTransactionCompletion( tx );
    }
  }

  public String onPrepareStatement( String sql )
  {
    return sql;
  }

  /**
   * Returns an array containing the instances of the <code>Interceptor</code> interface that are
   * chained within this interceptor.
   * 
   * @return An array of interceptor
   */
  public Interceptor[] getInterceptors()
  {
    return interceptors;
  }

  /**
   * Sets the instances of the <code>Interceptor</code> interface that are chained within this
   * interceptor.
   * 
   * @param interceptors
   */
  public void setInterceptors( Interceptor[] interceptors )
  {
    this.interceptors = interceptors;
  }
}
