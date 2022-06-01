
package com.biperf.core.utils.hibernate;

import java.io.Serializable;
import java.sql.Connection;

import org.hibernate.CacheMode;
import org.hibernate.Criteria;
import org.hibernate.EntityMode;
import org.hibernate.Filter;
import org.hibernate.FlushMode;
import org.hibernate.HibernateException;
import org.hibernate.LobHelper;
import org.hibernate.LockMode;
import org.hibernate.LockOptions;
import org.hibernate.Query;
import org.hibernate.ReplicationMode;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.TypeHelper;
import org.hibernate.UnknownProfileException;
import org.hibernate.engine.PersistenceContext;
import org.hibernate.impl.SessionImpl;
import org.hibernate.jdbc.Work;
import org.hibernate.persister.entity.EntityPersister;
import org.hibernate.stat.SessionStatistics;

/**
 *  Hibernate Session Wrapper that is in charge with flushing after saves. 
 *  The save is triggered by a load operation if prior to it there was a saveOrUpdate, delete or update.
 */
public class SessionWrapper implements Session
{
  private Session session;

  // to track if there is pending data that needs to be flushed prior to any subsequent query
  private boolean requiresFlush = false;

  //
  // keep track of flushes for performance profiling
  //
  // to track flushes for application lifecycle
  private static int globalCount = 0;

  // to track flushes per session
  private int count = 0;

  public SessionWrapper( Session session )
  {
    this.session = session;
    requiresFlush = false;
  }

  public Transaction beginTransaction() throws HibernateException
  {
    requiresFlush = false;
    return session.beginTransaction();
  }

  public void cancelQuery() throws HibernateException
  {
    session.cancelQuery();
  }

  public void clear()
  {
    session.clear();
    requiresFlush = false;
  }

  public Connection close() throws HibernateException
  {
    return session.close();
  }

  public Connection connection() throws HibernateException
  {
    return session.connection();
  }

  public boolean contains( Object object )
  {
    return session.contains( object );
  }

  public Criteria createCriteria( Class persistentClass, String alias )
  {
    flushIfRequired();
    return session.createCriteria( persistentClass, alias );
  }

  public Criteria createCriteria( Class persistentClass )
  {
    flushIfRequired();
    return session.createCriteria( persistentClass );
  }

  public Criteria createCriteria( String entityName, String alias )
  {
    flushIfRequired();
    return session.createCriteria( entityName, alias );
  }

  public Criteria createCriteria( String entityName )
  {
    flushIfRequired();
    return session.createCriteria( entityName );
  }

  public Query createFilter( Object collection, String queryString ) throws HibernateException
  {
    flushIfRequired();
    return session.createFilter( collection, queryString );
  }

  public Query createQuery( String queryString ) throws HibernateException
  {
    flushIfRequired();
    return session.createQuery( queryString );
  }

  public SQLQuery createSQLQuery( String queryString ) throws HibernateException
  {
    flushIfRequired();
    return session.createSQLQuery( queryString );
  }

  public void delete( Object object ) throws HibernateException
  {
    session.delete( object );
    requiresFlush = true;
  }

  public void delete( String entityName, Object object ) throws HibernateException
  {
    session.delete( entityName, object );
    requiresFlush = true;
  }

  public void disableFilter( String filterName )
  {
    session.disableFilter( filterName );
  }

  public Connection disconnect() throws HibernateException
  {
    return session.disconnect();
  }

  public Filter enableFilter( String filterName )
  {
    return session.enableFilter( filterName );
  }

  public void evict( Object object ) throws HibernateException
  {
    session.evict( object );
  }

  /**
   * Force this session to flush. Must be called at the end of a unit of work,
   * before commiting the transaction and closing the session (depending on flush-mode,
   * Transaction.commit() calls this method).
   *
   * @see org.hibernate.Session.flush()
   *
   * @throws HibernateException
   */
  public void flush() throws HibernateException
  {
    count++;
    globalCount++;
    session.flush();

    // clear the flush flag
    requiresFlush = false;
  }

  public Object get( Class clazz, Serializable id, LockMode lockMode ) throws HibernateException
  {
    flushIfRequired();
    return session.get( clazz, id, lockMode );
  }

  public Object get( Class clazz, Serializable id ) throws HibernateException
  {
    flushIfRequired();
    return session.get( clazz, id );
  }

  public Object get( String entityName, Serializable id, LockMode lockMode ) throws HibernateException
  {
    flushIfRequired();
    return session.get( entityName, id, lockMode );
  }

  public Object get( String entityName, Serializable id ) throws HibernateException
  {
    flushIfRequired();
    return session.get( entityName, id );
  }

  public CacheMode getCacheMode()
  {
    return session.getCacheMode();
  }

  public LockMode getCurrentLockMode( Object object ) throws HibernateException
  {
    return session.getCurrentLockMode( object );
  }

  public Filter getEnabledFilter( String filterName )
  {
    return session.getEnabledFilter( filterName );
  }

  public EntityMode getEntityMode()
  {
    return session.getEntityMode();
  }

  public String getEntityName( Object object ) throws HibernateException
  {
    return session.getEntityName( object );
  }

  public FlushMode getFlushMode()
  {
    return session.getFlushMode();
  }

  public Serializable getIdentifier( Object object ) throws HibernateException
  {
    return session.getIdentifier( object );
  }

  public Query getNamedQuery( String queryName ) throws HibernateException
  {
    return session.getNamedQuery( queryName );
  }

  public Session getSession( EntityMode entityMode )
  {
    return session.getSession( entityMode );
  }

  public SessionFactory getSessionFactory()
  {
    return session.getSessionFactory();
  }

  public SessionStatistics getStatistics()
  {
    return session.getStatistics();
  }

  public Transaction getTransaction()
  {
    return session.getTransaction();
  }

  public boolean isConnected()
  {
    return session.isConnected();
  }

  public boolean isDirty() throws HibernateException
  {
    return requiresFlush || session.isDirty();
  }

  public boolean isOpen()
  {
    return session.isOpen();
  }

  public Object load( Class theClass, Serializable id, LockMode lockMode ) throws HibernateException
  {
    flushIfRequired();
    return session.load( theClass, id, lockMode );
  }

  public Object load( Class theClass, Serializable id ) throws HibernateException
  {
    flushIfRequired();
    return session.load( theClass, id );
  }

  public void load( Object object, Serializable id ) throws HibernateException
  {
    flushIfRequired();
    session.load( object, id );
  }

  public Object load( String entityName, Serializable id, LockMode lockMode ) throws HibernateException
  {
    flushIfRequired();
    return session.load( entityName, id, lockMode );
  }

  public Object load( String entityName, Serializable id ) throws HibernateException
  {
    flushIfRequired();
    return session.load( entityName, id );
  }

  public void lock( Object object, LockMode lockMode ) throws HibernateException
  {
    flushIfRequired();
    session.lock( object, lockMode );
  }

  public void lock( String entityName, Object object, LockMode lockMode ) throws HibernateException
  {
    session.lock( entityName, object, lockMode );
  }

  public Object merge( Object object ) throws HibernateException
  {
    Object obj = session.merge( object );
    return obj;
  }

  public Object merge( String entityName, Object object ) throws HibernateException
  {
    Object obj = session.merge( entityName, object );
    return obj;
  }

  public void persist( Object object ) throws HibernateException
  {
    session.persist( object );
  }

  public void persist( String entityName, Object object ) throws HibernateException
  {
    session.persist( entityName, object );
  }

  /**
   * @deprecated
   * @throws HibernateException
   */
  @Deprecated
  public void reconnect() throws HibernateException
  {
    session.reconnect();
  }

  public void reconnect( Connection connection ) throws HibernateException
  {
    session.reconnect( connection );
  }

  public void refresh( Object object, LockMode lockMode ) throws HibernateException
  {
    session.refresh( object, lockMode );
  }

  public void refresh( Object object ) throws HibernateException
  {
    session.refresh( object );
  }

  public void replicate( Object object, ReplicationMode replicationMode ) throws HibernateException
  {
    session.replicate( object, replicationMode );
  }

  public void replicate( String entityName, Object object, ReplicationMode replicationMode ) throws HibernateException
  {
    session.replicate( entityName, object, replicationMode );
  }

  public Serializable save( Object object ) throws HibernateException
  {
    Serializable obj = session.save( object );
    return obj;
  }

  public Serializable save( String entityName, Object object ) throws HibernateException
  {
    Serializable obj = session.save( entityName, object );
    return obj;
  }

  public void saveOrUpdate( Object object ) throws HibernateException
  {
    session.saveOrUpdate( object );
    requiresFlush = true;
    return;

  }

  public void saveOrUpdate( String entityName, Object object ) throws HibernateException
  {
    session.saveOrUpdate( entityName, object );
    requiresFlush = true;
  }

  public void setCacheMode( CacheMode cacheMode )
  {
    session.setCacheMode( cacheMode );
  }

  public void setFlushMode( FlushMode flushMode )
  {
    session.setFlushMode( flushMode );
  }

  public void setReadOnly( Object entity, boolean readOnly )
  {
    session.setReadOnly( entity, readOnly );
  }

  public void update( Object object ) throws HibernateException
  {
    session.update( object );
    requiresFlush = true;
  }

  public void update( String entityName, Object object ) throws HibernateException
  {
    session.update( entityName, object );
    requiresFlush = true;
  }

  public SessionFactory getFactory()
  {
    return ( (SessionImpl)session ).getFactory();
  }

  /**
   * 
   * @param session
   * @param domainObject
   * @return
   */
  public EntityPersister getEntityPersister( Object domainObject )
  {
    SessionImpl sessionImpl = (SessionImpl)session;
    return sessionImpl.getFactory().getEntityPersister( sessionImpl.bestGuessEntityName( domainObject ) );
  }

  /**
   * 
   * @return
   */
  public PersistenceContext getPersistenceContext()
  {
    SessionImpl sessionImpl = (SessionImpl)session;
    return sessionImpl.getPersistenceContext();
  }

  private void flushIfRequired()
  {
    if ( this.requiresFlush )
    {
      flush();
    }
  }

  public LockRequest buildLockRequest( LockOptions arg0 )
  {
    return session.buildLockRequest( arg0 );
  }

  public void disableFetchProfile( String arg0 ) throws UnknownProfileException
  {
    session.disableFetchProfile( arg0 );
  }

  public void doWork( Work arg0 ) throws HibernateException
  {
    session.doWork( arg0 );
  }

  public void enableFetchProfile( String arg0 ) throws UnknownProfileException
  {
    session.enableFetchProfile( arg0 );
  }

  public Object get( Class arg0, Serializable arg1, LockOptions arg2 ) throws HibernateException
  {
    return session.get( arg0, arg1, arg2 );
  }

  public Object get( String arg0, Serializable arg1, LockOptions arg2 ) throws HibernateException
  {
    return session.get( arg0, arg1, arg2 );
  }

  public LobHelper getLobHelper()
  {
    return session.getLobHelper();
  }

  public TypeHelper getTypeHelper()
  {
    return session.getTypeHelper();
  }

  public boolean isDefaultReadOnly()
  {
    return session.isDefaultReadOnly();
  }

  public boolean isFetchProfileEnabled( String arg0 ) throws UnknownProfileException
  {
    return session.isFetchProfileEnabled( arg0 );
  }

  public boolean isReadOnly( Object arg0 )
  {
    return session.isReadOnly( arg0 );
  }

  public Object load( Class arg0, Serializable arg1, LockOptions arg2 ) throws HibernateException
  {
    return session.load( arg0, arg1, arg2 );
  }

  public Object load( String arg0, Serializable arg1, LockOptions arg2 ) throws HibernateException
  {
    return session.load( arg0, arg1, arg2 );
  }

  public void refresh( Object arg0, LockOptions arg1 ) throws HibernateException
  {
    session.refresh( arg0, arg1 );
  }

  public void setDefaultReadOnly( boolean arg0 )
  {
    session.setDefaultReadOnly( arg0 );
  }

}
