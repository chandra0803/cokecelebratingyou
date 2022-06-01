
package com.biperf.core.orm.ehcache.jms;

import static net.sf.ehcache.distribution.jms.JMSUtil.ACKNOWLEDGEMENT_MODE;
import static net.sf.ehcache.distribution.jms.JMSUtil.GET_QUEUE_BINDING_NAME;
import static net.sf.ehcache.distribution.jms.JMSUtil.GET_QUEUE_CONNECTION_FACTORY_BINDING_NAME;
import static net.sf.ehcache.distribution.jms.JMSUtil.INITIAL_CONTEXT_FACTORY_NAME;
import static net.sf.ehcache.distribution.jms.JMSUtil.LISTEN_TO_TOPIC;
import static net.sf.ehcache.distribution.jms.JMSUtil.PASSWORD;
import static net.sf.ehcache.distribution.jms.JMSUtil.REPLICATION_TOPIC_BINDING_NAME;
import static net.sf.ehcache.distribution.jms.JMSUtil.SECURITY_CREDENTIALS;
import static net.sf.ehcache.distribution.jms.JMSUtil.SECURITY_PRINCIPAL_NAME;
import static net.sf.ehcache.distribution.jms.JMSUtil.TOPIC_CONNECTION_FACTORY_BINDING_NAME;
import static net.sf.ehcache.distribution.jms.JMSUtil.URL_PKG_PREFIXES;
import static net.sf.ehcache.distribution.jms.JMSUtil.USERNAME;
import static net.sf.ehcache.distribution.jms.JMSUtil.closeContext;

import java.util.Properties;

import javax.jms.Queue;
import javax.jms.QueueConnectionFactory;
import javax.jms.Topic;
import javax.jms.TopicConnectionFactory;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import net.sf.ehcache.CacheException;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.distribution.CacheManagerPeerProvider;
import net.sf.ehcache.distribution.CacheManagerPeerProviderFactory;
import net.sf.ehcache.distribution.jms.AcknowledgementMode;
import net.sf.ehcache.util.PropertyUtil;

public class BIJMSCacheManagerPeerProviderFactory extends CacheManagerPeerProviderFactory
{
  private static final Logger log = Logger.getLogger( BIJMSCacheManagerPeerProviderFactory.class );

  /**
   * @param cacheManager the CacheManager instance connected to this peer provider
   * @param properties   implementation specific properties. These are configured as comma
   *                     separated name value pairs in ehcache.xml
   * @return a provider, already connected to the message queue
   */
  @Override
  public CacheManagerPeerProvider createCachePeerProvider( CacheManager cacheManager, Properties properties )
  {
    String securityPrincipalName = PropertyUtil.extractAndLogProperty( SECURITY_PRINCIPAL_NAME, properties );
    String securityCredentials = PropertyUtil.extractAndLogProperty( SECURITY_CREDENTIALS, properties );
    String initialContextFactoryName = PropertyUtil.extractAndLogProperty( INITIAL_CONTEXT_FACTORY_NAME, properties );
    String urlPkgPrefixes = PropertyUtil.extractAndLogProperty( URL_PKG_PREFIXES, properties );
    // String providerURL = PropertyUtil.extractAndLogProperty( PROVIDER_URL, properties );

    // String providerURL = getProviderUrl() ;
    properties.list( System.out );
    String replicationTopicConnectionFactoryBindingName = PropertyUtil.extractAndLogProperty( TOPIC_CONNECTION_FACTORY_BINDING_NAME, properties );
    String replicationTopicBindingName = PropertyUtil.extractAndLogProperty( REPLICATION_TOPIC_BINDING_NAME, properties );
    String getQueueBindingName = PropertyUtil.extractAndLogProperty( GET_QUEUE_BINDING_NAME, properties );
    String getQueueConnectionFactoryBindingName = PropertyUtil.extractAndLogProperty( GET_QUEUE_CONNECTION_FACTORY_BINDING_NAME, properties );
    String userName = PropertyUtil.extractAndLogProperty( USERNAME, properties );
    String password = PropertyUtil.extractAndLogProperty( PASSWORD, properties );

    String acknowledgementMode = PropertyUtil.extractAndLogProperty( ACKNOWLEDGEMENT_MODE, properties );
    AcknowledgementMode effectiveAcknowledgementMode = AcknowledgementMode.forString( acknowledgementMode );

    if ( log.isDebugEnabled() )
    {
      log.debug( "Creating TopicSession in " + effectiveAcknowledgementMode.name() + " mode." );
    }

    // create the credentials object
    SecurityCredentials credentials = new SecurityCredentials();
    credentials.setPassword( password );
    credentials.setUserName( userName );
    credentials.setSecurityPrincipalName( securityPrincipalName );
    credentials.setSecurityCredentialsPassword( securityCredentials );

    String listenToTopicString = PropertyUtil.extractAndLogProperty( LISTEN_TO_TOPIC, properties );
    boolean listenToTopic;
    listenToTopic = listenToTopicString == null || PropertyUtil.parseBoolean( listenToTopicString );

    validateJMSCacheLoaderConfiguration( getQueueBindingName, getQueueConnectionFactoryBindingName );

    Context context = null;

    TopicConnectionFactory topicConnectionFactory;
    Topic replicationTopic;

    QueueConnectionFactory queueConnectionFactory = null;
    Queue getQueue = null;

    try
    {
      /*
       * This is no longer used - since resources are deployed on the servers, a local lookup is
       * required/desired. This is left in for posterity
       *//*
         * context = createInitialContext( securityPrincipalName, securityCredentials,
         * initialContextFactoryName, urlPkgPrefixes, providerURL, replicationTopicBindingName,
         * replicationTopicConnectionFactoryBindingName, getQueueBindingName,
         * getQueueConnectionFactoryBindingName );
         */
      context = new InitialContext();
      System.out.println( "%%%%%%replicationTopicConnectionFactoryBindingName=" + replicationTopicConnectionFactoryBindingName );
      topicConnectionFactory = (TopicConnectionFactory)context.lookup( replicationTopicConnectionFactoryBindingName );
      // Connection topicConnection = topicConnectionFactory.createConnection();
      // Session jmsTopicSession = topicConnection.createSession(false, Session.AUTO_ACKNOWLEDGE);
      replicationTopic = (Topic)context.lookup( replicationTopicBindingName );
      queueConnectionFactory = (QueueConnectionFactory)context.lookup( getQueueConnectionFactoryBindingName );
      // Connection queueConnection = queueConnectionFactory.createConnection();
      // Session jmsQueueSession = queueConnection.createSession(false, Session.AUTO_ACKNOWLEDGE);
      getQueue = (Queue)context.lookup( getQueueBindingName );
      closeContext( context );
    }
    catch( NamingException ne )
    {
      log.error( ne );
      throw new CacheException( "NamingException " + ne.getMessage(), ne );
    }
    /*
     * catch( JMSException je ) { log.error(je); throw new CacheException( "NamingException " +
     * je.getMessage(), je ); }
     */
    return new BIJMSCacheManagerPeerProvider( cacheManager, credentials, topicConnectionFactory, replicationTopic, queueConnectionFactory, getQueue, effectiveAcknowledgementMode, listenToTopic );
  }

  private void validateJMSCacheLoaderConfiguration( String getQueueBindingName, String getQueueConnectionFactoryBindingName )
  {
    if ( getQueueConnectionFactoryBindingName != null && getQueueBindingName == null )
    {
      throw new CacheException( "The 'getQueueBindingName is null'. Please configure." );
    }
    if ( getQueueConnectionFactoryBindingName == null && getQueueBindingName != null )
    {
      throw new CacheException( "The 'getQueueConnectionFactoryBindingName' is null. Please configure." );
    }
  }
}
