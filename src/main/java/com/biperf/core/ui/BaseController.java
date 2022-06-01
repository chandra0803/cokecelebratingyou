/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/BaseController.java,v $
 */

package com.biperf.core.ui;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.tiles.ComponentContext;
import org.apache.struts.tiles.Controller;

import com.biperf.core.exception.BeaconRuntimeException;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.SAO;
import com.biperf.core.service.maincontent.MainContentService;
import com.biperf.core.service.security.AuthorizationService;
import com.biperf.core.ui.utils.ServiceErrorStrutsUtils;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.PromotionMenuBean;
import com.jamonapi.Monitor;
import com.jamonapi.MonitorFactory;

/**
 * BaseController.
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
 * <td>waldal</td>
 * <td>Apr 6, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public abstract class BaseController implements Controller
{
  /**
   * Logger for this class
   */
  private static final Logger log = Logger.getLogger( BaseController.class );

  /**
   * Bean location through BeanLocator look-up returns the service interface.
   * 
   * @param beanName
   * @return SAO
   */
  protected static SAO getService( String beanName )
  {
    return (SAO)BeanLocator.getBean( beanName );
  }

  /**
   * Overridden from
   * 
   * @see org.apache.struts.tiles.Controller#perform(org.apache.struts.tiles.ComponentContext,
   *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
   *      javax.servlet.ServletContext)
   * @param tileContext
   * @param request
   * @param response
   * @param servletContext
   * @throws ServletException
   * @throws IOException
   * @deprecated
   */
  @Override
  @Deprecated
  public void perform( ComponentContext tileContext, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext ) throws ServletException, IOException
  {
    // nop in order to remove from the interface
  }

  /**
   * Made final, so that all subclassed controllers can take advantage of common functionallity such
   * as proper exception reporting. <br/> All subclassers must implement onExecute() where they
   * would normally implement execute().
   * 
   * @see org.apache.struts.tiles.Controller#execute(org.apache.struts.tiles.ComponentContext,
   *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
   *      javax.servlet.ServletContext) <br/>
   * @param tileContext
   * @param request
   * @param response
   * @param servletContext
   * @throws Exception
   */
  @Override
  public final void execute( ComponentContext tileContext, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext ) throws Exception
  {
    Monitor monitor = null;
    try
    {
      monitor = MonitorFactory.start( this.getClass().getName() + ".execute" );
      if ( log.isDebugEnabled() )
      {
        log.debug( ">>> " + this.getClass().getName() );
      }
      // Perform the actual "execute".
      onExecute( tileContext, request, response, servletContext );

      if ( log.isDebugEnabled() )
      {
        log.debug( "<<< " + this.getClass().getName() );
      }
    }
    catch( Exception e )
    {
      // Logging rather than letting caller log since InsertTag swallows the stack trace.
      log.error( "Controller Root Exception", e );
      // if service error exception convert the service error to action errors
      if ( e instanceof ServiceErrorException || e instanceof BeaconRuntimeException && ( (BeaconRuntimeException)e ).getCause() instanceof ServiceErrorException )
      {
        handleServiceErrorException( request, e );
        return;
      }
      throw e;
    }
    finally
    {
      if ( monitor != null )
      {
        monitor.stop();
      }
    }
  }

  /**
   * Converts and Save the Service Errors to Action Errors
   * @param request
   * @param exception
   */
  private void handleServiceErrorException( HttpServletRequest request, Exception exception )
  {
    ServiceErrorException serviceException = null;
    if ( exception instanceof ServiceErrorException )
    {
      serviceException = (ServiceErrorException)exception;
    }
    if ( exception instanceof BeaconRuntimeException && ( (BeaconRuntimeException)exception ).getCause() instanceof ServiceErrorException )
    {
      serviceException = (ServiceErrorException) ( (BeaconRuntimeException)exception ).getCause();
    }
    ActionMessages errors = new ActionMessages();
    List serviceErrors = serviceException.getServiceErrors();
    ServiceErrorStrutsUtils.convertServiceErrorsToActionErrors( serviceErrors, errors );
    // Save the error messages we need
    request.setAttribute( Globals.ERROR_KEY, errors );
  }

  protected List<PromotionMenuBean> getEligiblePromotions( HttpServletRequest request )
  {
    if ( UserManager.getUser() != null && UserManager.getUser().isParticipant() )
    {
      List<PromotionMenuBean> eligiblePromotions = (List<PromotionMenuBean>)request.getSession().getAttribute( "eligiblePromotions" );
      if ( null != eligiblePromotions )
      {
        return eligiblePromotions;
      }
      else
      {
        eligiblePromotions = ( (MainContentService)getService( MainContentService.BEAN_NAME ) ).buildEligiblePromoList( UserManager.getUser() );
        request.getSession().setAttribute( "eligiblePromotions", eligiblePromotions );
        return eligiblePromotions;
      }
    }
    return null;
  }

  protected boolean isAdmin()
  {
    return getAuthorizationService().isUserInRole( AuthorizationService.ROLE_CODE_BI_ADMIN );
  }

  protected boolean isDelegate()
  {
    if ( UserManager.getUser() == null )
    {
      return false;
    }

    return UserManager.getUser().isDelegate();
  }

  /**
   * Method associated to a tile and called immediately before the tile is included. Use onExecute
   * like you would normally use execute().
   * 
   * @param tileContext Current tile context.
   * @param request Current request
   * @param response Current response
   * @param servletContext Current servlet context
   * @throws Exception
   */
  public abstract void onExecute( ComponentContext tileContext, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext ) throws Exception;

  private static AuthorizationService getAuthorizationService()
  {
    return (AuthorizationService)BeanLocator.getBean( AuthorizationService.BEAN_NAME );
  }

}
