/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/BaseDispatchAction.java,v $
 */

package com.biperf.core.ui;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.actions.DispatchAction;

import com.biperf.core.domain.WebErrorMessage;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.SAO;
import com.biperf.core.service.maincontent.MainContentService;
import com.biperf.core.service.security.AuthorizationService;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.utils.ServiceErrorStrutsUtils;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.PromotionMenuBean;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.jamonapi.Monitor;
import com.jamonapi.MonitorFactory;

/**
 * BaseDispatchAction for retrieving the App Context from the ServletContext.
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
 * <td>Feb 16, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class BaseDispatchAction extends DispatchAction
{
  protected enum ContentType
  {
    JSON( "application/json" ), HTML( "text/html" );

    private final String contentType;

    private ContentType( String contentType )
    {
      this.contentType = contentType;
    }

    public String getContentType()
    {
      return this.contentType;
    }
  }

  /**
   * Not using "log" since DispatchAction (and some of our subclasses - though they probably
   * shouldn't) uses it and we want our our own so we can turn on or off just logging of this class.
   */
  private static final Log localLogger = LogFactory.getLog( BaseDispatchAction.class );

  /*
   * This is a thread-safe attribute - sub-classes can override the getter method to use a custom
   * mapper if necessary.
   */
  protected static ObjectMapper mapper = new ObjectMapper();

  /**
   * Bean location through BeanLocator look-up.
   * 
   * @param beanName
   * @return SAO
   */
  protected static SAO getService( String beanName )
  {
    return (SAO)BeanLocator.getBean( beanName );
  }

  /**
   * Overriden to provide "one-stop" method logging. Overridden from
   * 
   * @see org.apache.struts.actions.DispatchAction#dispatchMethod(org.apache.struts.action.ActionMapping,
   *      org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest,
   *      javax.servlet.http.HttpServletResponse, java.lang.String)
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @param name
   * @return ActionForward
   * @throws Exception
   */
  protected ActionForward dispatchMethod( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, String name ) throws Exception
  {
    StringBuffer methodMessage = null;
    boolean showMethodLog = localLogger.isInfoEnabled();
    Monitor monitor = MonitorFactory.start( this.getClass().getName() + "." + name );
    if ( showMethodLog )
    {
      methodMessage = new StringBuffer();
      methodMessage.append( this.getClass().getName() ).append( "." ).append( name ).append( "()" );
      localLogger.info( new StringBuffer().append( ">>> " ).append( methodMessage ) );
    }

    ActionForward actionForward = null;
    try
    {
      // Do the actual work
      actionForward = super.dispatchMethod( mapping, form, request, response, name );

      if ( showMethodLog )
      {
        localLogger.info( new StringBuffer().append( "<<< " ).append( methodMessage ) );
      }
      return actionForward;
    }
    catch( Exception e )
    {
      // catch all Throwables and display friendly error page.
      localLogger.error( "Caught Exception processing action", e );
      String input = mapping.getInput();
      if ( input != null )
      {
        ActionMessages errors = new ActionMessages();
        errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_SYSTEM_EXCEPTION ) );
        saveErrors( request, errors );
        actionForward = mapping.getInputForward();
        return actionForward;
      }

      throw e;
    }
    finally
    {
      monitor.stop();
    }
  }

  /**
   * Converts and Save the Service Errors to Action Errors
   * @param request
   * @param serviceException
   */
  protected void handleServiceErrorException( HttpServletRequest request, ServiceErrorException serviceException )
  {
    ActionMessages errors = new ActionMessages();
    List serviceErrors = serviceException.getServiceErrors();
    ServiceErrorStrutsUtils.convertServiceErrorsToActionErrors( serviceErrors, errors );
    saveErrors( request, errors );
  }

  /**
   * Get ActionMessages from the session or create new one
   * and add the messages to it.
   */
  public void saveWarningMessages( HttpServletRequest request, ActionMessages messages )
  {
    ActionMessages warnings = (ActionMessages)request.getSession().getAttribute( ActionConstants.SESSION_WARNINGS );
    if ( warnings == null )
    {
      warnings = new ActionMessages();
    }
    warnings.add( messages );
    request.getSession().setAttribute( ActionConstants.SESSION_WARNINGS, warnings );
  }

  /**
   * Add the display messages from the session that are displayed incase of sendredirects
   * and remove it from session.
   */
  public void setWarningMessagesIfAny( HttpServletRequest request )
  {
    if ( request.getSession().getAttribute( ActionConstants.SESSION_WARNINGS ) != null )
    {
      ActionMessages warnings = (ActionMessages)request.getSession().getAttribute( ActionConstants.SESSION_WARNINGS );
      request.getSession().removeAttribute( ActionConstants.SESSION_WARNINGS );
      saveErrors( request, warnings );
    }
  }

  protected List<PromotionMenuBean> getEligiblePromotions( HttpServletRequest request )
  {
    if ( UserManager.getUser().isParticipant() && !UserManager.getUser().isOptOutOfProgram() )
    {
      List<PromotionMenuBean> eligiblePromotions = (List<PromotionMenuBean>)request.getSession().getAttribute( "eligiblePromotions" );
      if ( null != eligiblePromotions )
      {
        return eligiblePromotions;
      }
      else
      {
        eligiblePromotions = getMainContentService().buildEligiblePromoList( UserManager.getUser() );
        request.getSession().setAttribute( "eligiblePromotions", eligiblePromotions );
        return eligiblePromotions;
      }
    }
    return Lists.newArrayList();
  }

  protected void refreshPointBalance( HttpServletRequest request )
  {
    request.getSession().removeAttribute( "pointsView" );
  }

  /*
   * Gets Json String
   */
  protected String toJson( Object bean )
  {
    ObjectMapper mapper = getObjectMapper();
    Writer writer = new StringWriter();

    try
    {
      mapper.writeValue( writer, bean );
    }
    catch( Throwable t )
    {
      localLogger.error( "\n\n\nERROR!!!\n\n\n" + t.getMessage() );
    }

    return writer.toString();
  }

  /*
   * Writes the Json String Object to httpservletresponse
   */
  protected void writeAsJsonToResponse( Object bean, HttpServletResponse response ) throws IOException
  {
    writeAsJsonToResponse( bean, response, ContentType.JSON );
  }

  protected void writeAsJsonToResponse( Object bean, HttpServletResponse response, ContentType contentType ) throws IOException
  {
    PrintWriter out = response.getWriter();
    out.print( toJson( bean ) );
    out.flush();
    out.close();
  }

  public <T> T getRequestContentAsJson( HttpServletRequest req, Class<T> type ) throws JsonParseException, JsonMappingException, IOException
  {
    ObjectMapper mapper = new ObjectMapper();
    T readValue = mapper.readValue( req.getInputStream(), type );
    return readValue;
  }

  protected ObjectMapper getObjectMapper()
  {
    return mapper;
  }

  private MainContentService getMainContentService()
  {
    return (MainContentService)getService( MainContentService.BEAN_NAME );
  }
  
  private AuthorizationService getAuthorizationService()
  {
    return (AuthorizationService)getService( AuthorizationService.BEAN_NAME );
  }

  protected void writeAppErrorAsJsonResponse( HttpServletResponse response, Throwable t )
  {
    WebErrorMessageList messageList = new WebErrorMessageList();
    WebErrorMessage message = new WebErrorMessage();
    message.setText( "Application error occured ..contact Admin " + t.getMessage() );
    WebErrorMessage.addErrorMessage( message );
    messageList.getMessages().add( message );

    try
    {
      writeAsJsonToResponse( messageList, response, ContentType.JSON );
    }
    catch( Exception e )
    {
      e.printStackTrace();
    }
  }
  
  protected boolean isAdmin()
  {
    return getAuthorizationService().isUserInRole( AuthorizationService.ROLE_CODE_BI_ADMIN );
  }
  
  protected boolean isDelegate()
  {
    return UserManager.getUser().isDelegate();
  }
  
}
