
package com.biperf.core.orm.ehcache.jms;

import static net.sf.ehcache.distribution.jms.JMSUtil.CACHE_MANAGER_UID;
import static net.sf.ehcache.distribution.jms.JMSUtil.localCacheManagerUid;

import java.util.ArrayList;
import java.util.List;

import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueReceiver;
import javax.jms.QueueSession;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;
import javax.jms.TopicSubscriber;

import org.apache.log4j.Logger;

import net.sf.ehcache.CacheException;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.distribution.CacheManagerPeerProvider;
import net.sf.ehcache.distribution.CachePeer;
import net.sf.ehcache.distribution.jms.AcknowledgementMode;
import net.sf.ehcache.distribution.jms.JMSCachePeer;

/**
 * Creates a single instance of JMSCachePeer which does not publishing and subscribing to a single topic
 * for the CacheManager
 *
 */
public class BIJMSCacheManagerPeerProvider implements CacheManagerPeerProvider
{
  private static final Logger log = Logger.getLogger( BIJMSCacheManagerPeerProvider.class );

  protected CacheManager cacheManager;
  protected SecurityCredentials credentials;
  protected List<CachePeer> remoteCachePeers = new ArrayList<CachePeer>();

  protected TopicConnectionFactory topicConnectionFactory;
  protected QueueConnectionFactory queueConnectionFactory;

  protected Topic replicationTopic;

  protected QueueConnection getQueueConnection;
  protected Queue getQueue;

  protected AcknowledgementMode acknowledgementMode;

  protected QueueReceiver getQueueRequestReceiver;

  protected TopicSession topicPublisherSession;
  protected TopicPublisher topicPublisher;
  protected TopicSubscriber topicSubscriber;
  protected QueueSession getQueueSession;
  protected JMSCachePeer cachePeer;

  protected boolean listenToTopic;

  /**
   * Constructor
   *
   * @param cacheManager
   * @param replicationTopicConnection
   * @param replicationTopic
   * @param getQueueConnection
   * @param getQueue
   * @param acknowledgementMode
   * @param listenToTopic              whether this provider should listen to events made to the JMS topic
   */
  public BIJMSCacheManagerPeerProvider( CacheManager cacheManager,
                                        SecurityCredentials credentials,
                                        TopicConnectionFactory topicConnectionFactory,
                                        Topic replicationTopic,
                                        QueueConnectionFactory queueConnectionFactory,
                                        Queue getQueue,
                                        AcknowledgementMode acknowledgementMode,
                                        boolean listenToTopic )
  {
    this.cacheManager = cacheManager;
    this.credentials = credentials;
    this.topicConnectionFactory = topicConnectionFactory;
    this.replicationTopic = replicationTopic;
    this.queueConnectionFactory = queueConnectionFactory;
    this.getQueue = getQueue;
    this.acknowledgementMode = acknowledgementMode;
    this.listenToTopic = listenToTopic;
  }

  /**
   * Time for a cluster to form. This varies considerably, depending on the implementation.
   *
   * @return the time in ms, for a cluster to form
   */
  public long getTimeForClusterToForm()
  {
    if ( log.isTraceEnabled() )
    {
      log.trace( "getTimeForClusterToForm ( ) called " );
    }
    return 0;
  }

  /**
   * The replication scheme. Each peer provider has a scheme name, which can be used by caches to specify
   * for replication and bootstrap purposes.
   *
   * @return the well-known scheme name, which is determined by the replication provider author.
   */
  public String getScheme()
  {
    return "JMS";
  }

  /**
   * Notifies providers to initialise themselves.  This is the biggest change from the EHCache supported version.
   * In that version, they are passing the connection and creating sessions.  But apparently you can ONLY create 
   * 1 session from a connection as per the JMS specification.  Without these changes, the following exception was 
   * being thrown:
   * 
   *    MQRA:CA:createTopicSession failed-Only one JMS Session allowed when managed connection is involved in a transaction
   *    
   * TODO: determine if there are side effects from not closing explicitly the TopicConnection - see dispose method    
   * <p/>
   *
   * @throws CacheException
   */
  public void init()
  {
    try
    {
      String username = credentials.getUserName();
      String password = credentials.getPassword();
      topicPublisherSession = createTopicConnection( username, password, topicConnectionFactory ).createTopicSession( false, acknowledgementMode.toInt() );
      topicPublisher = topicPublisherSession.createPublisher( replicationTopic );

      if ( listenToTopic )
      {
        TopicConnection topicListenerConnection = createTopicConnection( username, password, topicConnectionFactory );
        TopicSession topicSubscriberSession = topicListenerConnection.createTopicSession( false, acknowledgementMode.toInt() );
        topicSubscriber = topicSubscriberSession.createSubscriber( replicationTopic, null, true );
        topicListenerConnection.start();
      }
      // noLocal is only supported in the JMS spec for topics. We need to use a message selector
      // on the queue to achieve the same effect.
      getQueueConnection = createQueueConnection( username, password, queueConnectionFactory );
      getQueueSession = getQueueConnection.createQueueSession( false, acknowledgementMode.toInt() );
      String messageSelector = CACHE_MANAGER_UID + " <> " + localCacheManagerUid( cacheManager );
      getQueueRequestReceiver = getQueueSession.createReceiver( getQueue, messageSelector );

      getQueueConnection.start();
    }
    catch( JMSException e )
    {
      log.error( e.getMessage(), e );
      throw new CacheException( "Exception while creating JMS connections: " + e.getMessage(), e );
    }

    cachePeer = new JMSCachePeer( cacheManager, topicPublisher, topicPublisherSession, getQueueSession );
    remoteCachePeers.add( cachePeer );

    try
    {
      if ( listenToTopic )
      {
        topicSubscriber.setMessageListener( cachePeer );
      }
      getQueueRequestReceiver.setMessageListener( cachePeer );
    }
    catch( JMSException e )
    {
      log.error( "Cannot register " + cachePeer + " as messageListener", e );
      throw new CacheException( "Cannot register " + cachePeer + " as messageListener", e );
    }
  }

  /**
   * Providers may be doing all sorts of exotic things and need to be able to clean up on dispose.
   *
   * @throws CacheException
   */
  public void dispose() throws CacheException
  {
    if ( log.isInfoEnabled() )
    {
      log.info( "JMSCacheManagerPeerProvider for CacheManager " + cacheManager.getName() + " being disposed." );
    }

    try
    {
      cachePeer.dispose();
      topicPublisher.close();
      if ( listenToTopic )
      {
        topicSubscriber.close();
        // replicationTopicConnection.stop();
        log.info( "Topic Not explicitly stopped" );
      }
      topicPublisherSession.close();
      // replicationTopicConnection.close();
      log.info( "Topic Not explicitly closed" );
      getQueueRequestReceiver.close();
      getQueueSession.close();
      getQueueConnection.close();

    }
    catch( JMSException e )
    {
      log.error( e.getMessage(), e );
      throw new CacheException( e.getMessage(), e );
    }

  }

  private TopicConnection createTopicConnection( String userName, String password, TopicConnectionFactory topicConnectionFactory ) throws JMSException
  {
    TopicConnection topicConnection;
    if ( userName != null )
    {
      topicConnection = topicConnectionFactory.createTopicConnection( userName, password );
    }
    else
    {
      topicConnection = topicConnectionFactory.createTopicConnection();
    }
    return topicConnection;
  }

  private QueueConnection createQueueConnection( String userName, String password, QueueConnectionFactory queueConnectionFactory ) throws JMSException
  {
    QueueConnection queueConnection;
    if ( userName != null )
    {
      queueConnection = queueConnectionFactory.createQueueConnection( userName, password );
    }
    else
    {
      queueConnection = queueConnectionFactory.createQueueConnection();
    }
    return queueConnection;
  }

  /**
   * @return a list of {@link CachePeer} peers for the given cache, excluding the local peer.
   */
  public List<CachePeer> listRemoteCachePeers( Ehcache cache ) throws CacheException
  {
    return remoteCachePeers;
  }

  /**
   * Register a new peer.
   *
   * @param rmiUrl
   */
  public void registerPeer( String rmiUrl )
  {
    throw new CacheException( "Not implemented for JMS" );
  }

  /**
   * Unregisters a peer.
   *
   * @param rmiUrl
   */
  public void unregisterPeer( String rmiUrl )
  {
    throw new CacheException( "Not implemented for JMS" );
  }

  public SecurityCredentials getCredentials()
  {
    return credentials;
  }

  public void setCredentials( SecurityCredentials credentials )
  {
    this.credentials = credentials;
  }
}
