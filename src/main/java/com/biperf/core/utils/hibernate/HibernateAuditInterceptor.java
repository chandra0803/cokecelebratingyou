/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/utils/hibernate/HibernateAuditInterceptor.java,v $
 */

package com.biperf.core.utils.hibernate;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Iterator;

import org.hibernate.CallbackException;
import org.hibernate.EntityMode;
import org.hibernate.Interceptor;
import org.hibernate.Transaction;
import org.hibernate.type.Type;

import com.biperf.core.domain.AuditCreateInfo;
import com.biperf.core.domain.AuditCreateInterface;
import com.biperf.core.domain.AuditUpdateInfo;
import com.biperf.core.domain.AuditUpdateInterface;
import com.biperf.core.utils.UserManager;

/**
 * The HibernateAuditInterceptor sets the audit trail info on all domain objects that implement the
 * AuditIterface. This interceptor is on the Hibernate SessionFactory. The reason that this is a
 * Hibernate interceptor as opposed to a Spring interceptor is the Parent/Child problem. When
 * updating a domain object, spring only knows about parent domain objects. We could look into
 * domain object to find sets of other domain objects - but then how far deep do you go? The
 * solution is to implement this intereceptor on the SessionFactory.
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
 * <td>jdunne</td>
 * <td>Apr 5, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class HibernateAuditInterceptor implements Interceptor
{

  /**
   * We do not do anything with the onLoad event. Return false.
   * 
   * @param arg0
   * @param arg1
   * @param arg2
   * @param arg3
   * @param arg4
   * @return false
   * @throws CallbackException
   */
  public boolean onLoad( Object arg0, Serializable arg1, Object[] arg2, String[] arg3, Type[] arg4 ) throws CallbackException
  {
    return false;
  }

  /**
   * The onFlushDirty event is called when the Hibernate Session is updating a record in the
   * database. We set the dateModified and modifiedBy properties of the AuditInterface here.
   * 
   * @param entity
   * @param id
   * @param currentState
   * @param previousState
   * @param propertyNames
   * @param types
   * @return true iff we have modified the entity.
   * @throws CallbackException
   */
  public boolean onFlushDirty( Object entity, Serializable id, Object[] currentState, Object[] previousState, String[] propertyNames, Type[] types ) throws CallbackException
  {

    /*
     * if the user is not logged in, we don't have a username available for audit information so
     * just return if ( !UserManager.isUserLoggedIn() ) { return false; }
     */

    boolean stateModified = false;
    if ( entity instanceof AuditUpdateInterface )
    {
      AuditUpdateInfo ai = ( (AuditUpdateInterface)entity ).getAuditUpdateInfo();
      ai.setDateModified( new Timestamp( System.currentTimeMillis() ) );
      /*
       * The record modified Id 0, indicating the record was modified/updated by roster/nackle
       * system.
       */
      ai.setModifiedBy( UserManager.isUserLoggedIn() ? UserManager.getUserId() : new Long( 0 ) );
      stateModified = true;
    }
    return stateModified;
  }

  /**
   * The onSave event is called when the Hibernate Session is inserting a record in the database. We
   * set the dateCreated and createddBy properties of the AuditInterface here.
   * 
   * @param entity
   * @param id
   * @param currentState
   * @param propertyNames
   * @param types
   * @return true iff we have modified the entity.
   * @throws CallbackException
   */
  public boolean onSave( Object entity, Serializable id, Object[] currentState, String[] propertyNames, Type[] types ) throws CallbackException
  {

    boolean stateModified = false;
    if ( entity instanceof AuditCreateInterface )
    {
      AuditCreateInfo ai = ( (AuditCreateInterface)entity ).getAuditCreateInfo();
      ai.setDateCreated( new Timestamp( System.currentTimeMillis() ) );
      // make sure we don't send NULL to the database
      // (userId is null during login processing)
      Long userId = UserManager.getUserId();
      ai.setCreatedBy( userId != null ? userId : new Long( 0 ) );
      stateModified = true;
    }
    return stateModified;
  }

  /**
   * The onDelete event. Empty method.
   * 
   * @param arg0
   * @param arg1
   * @param arg2
   * @param arg3
   * @param arg4
   * @throws CallbackException
   */
  public void onDelete( Object arg0, Serializable arg1, Object[] arg2, String[] arg3, Type[] arg4 ) throws CallbackException
  {
    // empty
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
   * The preFlush event. Empty method.
   * 
   * @param arg0
   * @throws CallbackException
   */
  public void preFlush( Iterator arg0 ) throws CallbackException
  {
    // empty
  }

  /**
   * The postFlush event. Empty method.
   * 
   * @param arg0
   * @throws CallbackException
   */
  public void postFlush( Iterator arg0 ) throws CallbackException
  {
    // empty
  }

  /**
   * The isTransient event. Empty method.
   * 
   * @param arg0
   * @return Boolean
   */
  public Boolean isTransient( Object arg0 )
  {
    return null;
  }

  /**
   * The findDirty event. Empty method.
   * 
   * @see org.hibernate.Interceptor#findDirty(java.lang.Object, java.io.Serializable,
   *      java.lang.Object[], java.lang.Object[], java.lang.String[], org.hibernate.type.Type[])
   * @param entity
   * @param id
   * @param currentState
   * @param previousState
   * @param propertyNames
   * @param types
   * @return int[]
   */
  public int[] findDirty( Object entity, Serializable id, Object[] currentState, Object[] previousState, String[] propertyNames, Type[] types )
  {
    return null;
  }

  /**
   * The instantiate event. Empty method.
   * 
   * @see org.hibernate.Interceptor
   * @param arg0
   * @param arg1
   * @return Object
   * @throws CallbackException
   */
  public Object instantiate( String arg0, Serializable arg1 ) throws CallbackException
  {
    return null;
  }

  /**
   * The getEntityName event. Empty method.
   * 
   * @see org.hibernate.Interceptor#getEntityName(java.lang.Object)
   * @param arg0
   * @return String
   * @throws CallbackException
   */
  public String getEntityName( Object arg0 ) throws CallbackException
  {
    return null;
  }

  /**
   * The getEntity event. Empty method.
   * 
   * @see org.hibernate.Interceptor#getEntity(java.lang.String, java.io.Serializable)
   * @param arg0
   * @param arg1
   * @return Object
   * @throws CallbackException
   */
  public Object getEntity( String arg0, Serializable arg1 ) throws CallbackException
  {
    return null;
  }

  /**
   * The afterTransactionBegin event. Empty method.
   * 
   * @see org.hibernate.Interceptor#afterTransactionBegin(org.hibernate.Transaction)
   * @param arg0
   */
  public void afterTransactionBegin( Transaction arg0 )
  {
    // empty
  }

  /**
   * The beforeTransactionCompletion event. Empty method.
   * 
   * @see org.hibernate.Interceptor#beforeTransactionCompletion(org.hibernate.Transaction)
   * @param arg0
   */
  public void beforeTransactionCompletion( Transaction arg0 )
  {
    // empty
  }

  /**
   * The afterTransactionCompletion event. Empty method.
   * 
   * @see org.hibernate.Interceptor#afterTransactionCompletion(org.hibernate.Transaction)
   * @param arg0
   */
  public void afterTransactionCompletion( Transaction arg0 )
  {
    // empty
  }

  public String onPrepareStatement( String sql )
  {
    return sql;
  }

  /**
   * The instantiate event. Empty method.
   * 
   * @see org.hibernate.Interceptor#instantiate(java.lang.String, org.hibernate.EntityMode,
   *      java.io.Serializable)
   * @param entityName
   * @param entityMode
   * @param id
   * @return Object
   * @throws CallbackException
   */
  public Object instantiate( String entityName, EntityMode entityMode, Serializable id ) throws CallbackException
  {
    return null;
  }
}
