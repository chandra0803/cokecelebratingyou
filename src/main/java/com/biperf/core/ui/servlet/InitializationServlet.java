/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/servlet/InitializationServlet.java,v $
 */

package com.biperf.core.ui.servlet;

import java.beans.Introspector;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.SchedulerException;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.biperf.core.dao.security.hibernate.RoleQueryConstraint;
import com.biperf.core.domain.enums.PickListItem;
import com.biperf.core.domain.enums.UserType;
import com.biperf.core.domain.user.Role;
import com.biperf.core.security.voter.BIRoleIPVoter;
import com.biperf.core.service.cache.CacheManagementService;
import com.biperf.core.service.security.RoleService;
import com.biperf.core.utils.ApplicationContextFactory;
import com.biperf.core.utils.InitializationAwareSchedulerFactoryBean;
import com.biperf.core.utils.ResourceManager;
import com.jamonapi.MonitorFactory;
import com.objectpartners.cms.util.BeanLocator;

/**
 * InitializationServlet.
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
 * <td>kumars</td>
 * <td>Mar 31, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class InitializationServlet extends CMSAwareBaseServlet
{
  private static Log LOG = null;

  /**
   * Beacon system startup
   * 
   * @throws javax.servlet.ServletException
   * @see javax.servlet.GenericServlet#init()
   */
  @Override
  public void init() throws ServletException
  {
    // TODO create a List of InitTask implementations and loop through them calling init
    System.out.println( "InitializationServlet>>> init" );
    LOG = LogFactory.getLog( InitializationServlet.class );
    ResourceManager.initResources();
    PickListItem.initFactory();
    initApplicationContextFactory();
    initWebApplicationContext();
    createSessionFactory();
    setContentManagerServletContext();
    setAdminSecurityRoles();
    disableJamon();
  }

  /*
   * We want to IP Restrict URLs that contain admin type roles
   */
  private void setAdminSecurityRoles()
  {
    RoleQueryConstraint constraint = new RoleQueryConstraint();
    constraint.setActive( true );
    constraint.setUserTypesIncluded( new String[] { UserType.BI
        .toString()/* , UserType.CLIENT.toString() */ } );// allow client admin access
    List<Role> roles = ( (RoleService)com.biperf.core.utils.BeanLocator.getBean( RoleService.class ) ).getRoleList( constraint );
    ( (BIRoleIPVoter)com.biperf.core.utils.BeanLocator.getBean( BIRoleIPVoter.class ) ).setRoles( roles );
  }

  /**
   * Beacon system shutdown
   * 
   * @see javax.servlet.Servlet#destroy()
   */
  @Override
  public void destroy()
  {
    // TODO use a List of InitTask implementations and loop through them (backwards) calling destroy
    System.out.println( "InitializationServlet>>> destroy" );
    shutdownEHCache();
    shutdownQuartz();
    ResourceManager.destroyResources();
    PickListItem.clearFactory();
    LogFactory.releaseAll();
    clearContentManagerApplicationContext();
    destroyWebApplicationContext();
    destroyApplicationContextFactory();
    Introspector.flushCaches();
    System.out.println( "InitializationServlet<<< destroy" );
  }

  /**
   * Initialize the ApplicationContextFactory The ApplicationContextFactory is a non-Spring holder
   * of the ApplicationContext
   * 
   * @throws ServletException
   */
  private void initApplicationContextFactory() throws ServletException
  {
    ApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext( getServletContext() );
    ApplicationContextFactory.setApplicationContext( applicationContext );
  }

  /**
   * Destroy the ApplicationContextFactory
   */
  private void destroyApplicationContextFactory()
  {
    // TODO move this to an EAR listener
    LOG.info( "InitializationServlet.destroyApplicationContextFactory: starting" );
    ApplicationContextFactory.destroy();
    LOG.info( "InitializationServlet.destroyApplicationContextFactory: done" );
  }

  /**
   * Initialize the WebApplicationContext. The Beacon WAC is used for ACEGI security system purposes
   * only
   */
  private void initWebApplicationContext()
  {
    WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext( getServletContext() );
    getServletContext().setAttribute( WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, webApplicationContext );
  }

  /**
   * Destroys the Quartz threads
   */
  private void shutdownQuartz()
  {
    LOG.info( "InitializationServlet.shutdownQuartz: starting" );
    try
    {
      ( (InitializationAwareSchedulerFactoryBean) com.biperf.core.utils.BeanLocator.getBean( InitializationAwareSchedulerFactoryBean.class ) ).destroy();
    }
    catch( SchedulerException e )
    {
      LOG.error( e.getMessage(), e );
    }
    try
    {
      ( (com.objectpartners.cms.util.InitializationAwareSchedulerFactoryBean) BeanLocator.getBean( com.objectpartners.cms.util.InitializationAwareSchedulerFactoryBean.class ) ).destroy();
    }
    catch( SchedulerException e )
    {
      LOG.error( e.getMessage(), e );
    }
    LOG.info( "InitializationServlet.shutdownQuartz: done" );
  }
  
  /**
   * Destroys the EHCache JSMReplicator Threads
   */
  private void shutdownEHCache()
  {
    LOG.info( "InitializationServlet.shutdownEHCache: starting" );
    ( (CacheManagementService) com.biperf.core.utils.BeanLocator.getBean( CacheManagementService.class ) ).shutdown();
    LOG.info( "InitializationServlet.shutdownEHCache: done" );
  }
  
  /**
   * Destroys the WebApplicationContext
   */
  private void destroyWebApplicationContext()
  {
    LOG.info( "InitializationServlet.destroyWebApplicationContext: starting" );
    Object wac = getServletContext().getAttribute( WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE );
    getServletContext().removeAttribute( WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE );
    LOG.info( "InitializationServlet.destroyWebApplicationContext: done" );
  }

  /**
   * Create the session factory (a lazy bean). This is needed for the Hibernate Caching
   * infrastructure which requires the ApplicationContext.
   */
  protected void createSessionFactory()
  {
    LOG.info( "InitializationServlet.createSessionFactory: starting" );
    ApplicationContext ctx = ApplicationContextFactory.getApplicationContext();
    ctx.getBean( "sessionFactory" );
    LOG.info( "InitializationServlet.createSessionFactory: done" );
  }

  /**
   * Get a reference to the Content Manager WAC and save it
   */
  private void setContentManagerServletContext()
  {
    LOG.info( "InitializationServlet.setContentManagerApplicationContext: starting" );
    ServletContext context = getServletContext();
    ApplicationContextFactory.setContentManagerServletContext( context );
    ApplicationContext cmContext = ApplicationContextFactory.getContentManagerApplicationContext();
    BeanLocator.setApplicationContext( cmContext );
    LOG.info( "InitializationServlet.setContentManagerApplicationContext: done" );
  }

  /**
   * Nullify the reference to the Content Manager WAC
   */
  private void clearContentManagerApplicationContext()
  {
    LOG.info( "InitializationServlet.clearContentManagerApplicationContext: starting" );
    ApplicationContextFactory.setContentManagerApplicationContext( null );
    // BeanLocator.releaseResources();
    LOG.info( "InitializationServlet.clearContentManagerApplicationContext: done" );
  }

  /**
   * Disable JAMon
   */
  private void disableJamon()
  {
    LOG.info( "InitializationServlet.disableJAMon: starting" );
    MonitorFactory.setEnabled( false );
    LOG.info( "InitializationServlet.disableJAMon: done" );
  }

}
