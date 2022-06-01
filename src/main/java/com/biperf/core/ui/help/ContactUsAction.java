/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/help/ContactUsAction.java,v $
 */

package com.biperf.core.ui.help;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.domain.WebErrorMessage;
import com.biperf.core.service.help.ContactUsService;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.WebErrorMessageList;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.servlet.PurlRecipientAccessFilter;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.PageConstants;
import com.biperf.core.utils.UserManager;
import com.biperf.core.utils.WebResponseConstants;
import com.objectpartners.cms.util.CmsResourceBundle;

/**
 * This action is used for the ContactUs page.
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
 * <td>tennant</td>
 * <td>Apr 6, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ContactUsAction extends BaseDispatchAction
{
  /**
   * called to save & email the form from the contactUs page
   * 
   * @param actionMapping
   * @param actionForm
   * @param httpServletRequest
   * @param httpServletResponse
   * @return ActionForward
   * @throws IOException 
   */
  public ActionForward saveContactUs( ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse ) throws IOException
  {
    ContactUsForm form = (ContactUsForm)actionForm;

    WebErrorMessageList messages = new WebErrorMessageList();
    WebErrorMessage message = new WebErrorMessage();

    message.setType( WebResponseConstants.RESPONSE_TYPE_SERVER_CMD );
    if ( UserManager.isUserLoggedIn() && isFullPageView( httpServletRequest ) )
    {
      message.setCommand( WebResponseConstants.RESPONSE_COMMAND_REDIRECT );
      message.setUrl( RequestUtils.getBaseURI( httpServletRequest ) + PageConstants.HOME_PAGE_G5_REDIRECT_URL );
    }
    else if ( !isFullPageView( httpServletRequest ) )
    {
      message.setCommand( WebResponseConstants.RESPONSE_COMMAND_MODAL );
      message.setUrl( "" );
    }
    else
    {
      message.setCommand( WebResponseConstants.RESPONSE_COMMAND_REDIRECT );
      message.setUrl( RequestUtils.getBaseURI( httpServletRequest ) + PageConstants.LOGIN_PAGE_URL );
    }
    try
    {
      getContactUsService().submitComments( form.getEmailAddress(), form.getSubject(), form.getComments(), form.getFirstName(), form.getLastName(), form.getCountryId() );
      if ( message.getCommand().equals( WebResponseConstants.RESPONSE_COMMAND_REDIRECT ) )
      {
        httpServletRequest.getSession().setAttribute( "contactUsEmailConfirmation", true );
      }
      else
      {
        httpServletRequest.setAttribute( "contactUsEmailConfirmation", true );
      }
      message.setName( CmsResourceBundle.getCmsBundle().getString( "participant.email.confirm", "SUCCESS_MESSAGE" ) );
      message.setText( CmsResourceBundle.getCmsBundle().getString( "participant.email.confirm", "RESPONSE" ) );
    }
    catch( Exception e )
    {
      log.error( e );
    }

    // If Accessed via PURL URL, redirect back to it
    String purlAccessUrl = PurlRecipientAccessFilter.getPurlAccessUrlFromSession( (HttpServletRequest)httpServletRequest );
    if ( null != purlAccessUrl )
    {
      httpServletRequest.setAttribute( "purlAccessUrl", httpServletRequest.getContextPath() + purlAccessUrl );
    }
    // 36494 - Selecting cancel from contact us returns to purl tnc or contributor page
    if ( StringUtils.isNotEmpty( form.getReturnUrl() ) )
    {
      httpServletRequest.setAttribute( "returnUrl", form.getReturnUrl() );
    }
    messages.getMessages().add( message );
    super.writeAsJsonToResponse( messages, httpServletResponse );
    return null;
  }

  public static void moveToRequest( HttpServletRequest request )
  {
    // get it from session....
    Boolean modelAvailable = (Boolean)request.getSession().getAttribute( "contactUsEmailConfirmation" );
    if ( modelAvailable != null )
    {
      // add it to the request object...
      request.setAttribute( "contactUsEmailConfirmation", true );

      // remove it from session....
      request.getSession().removeAttribute( "contactUsEmailConfirmation" );
    }
    else
    {
      // add it to the request object...
      request.setAttribute( "contactUsEmailConfirmation", false );
    }

  }

  /**
   * @param actionMapping
   * @param actionForm
   * @param httpServletRequest
   * @param httpServletResponse
   * @return ActionForward
   */
  public ActionForward cancelContactUs( ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse )
  {

    // If Accessed via PURL URL, redirect back to it
    String purlAccessUrl = PurlRecipientAccessFilter.getPurlAccessUrlFromSession( (HttpServletRequest)httpServletRequest );
    if ( null != purlAccessUrl )
    {
      try
      {
        httpServletResponse.sendRedirect( httpServletRequest.getContextPath() + purlAccessUrl );
      }
      catch( Throwable e )
      {
        // Do nothing
      }
      return null;
    }
    // 36494 - Selecting cancel from contact us
    ContactUsForm form = (ContactUsForm)actionForm;
    String returnUrl = "";
    if ( StringUtils.isNotEmpty( form.getReturnUrl() ) )
    {
      returnUrl = form.getReturnUrl();
      try
      {
        httpServletResponse.sendRedirect( returnUrl );
      }
      catch( Throwable e )
      {
        // Do nothing
      }
      return null;
    }
    // Otherwise, continue with the earlier flow
    else
    {
      ActionForward forward = null;
      if ( UserManager.isUserLoggedIn() )
      {
        forward = actionMapping.findForward( ActionConstants.CANCEL_TO_HOMEPAGE );
      }
      else
      {
        forward = actionMapping.findForward( ActionConstants.CANCEL_TO_LOGIN );
      }
      return forward;
    }
  }

  /**
   * @param actionMapping
   * @param actionForm
   * @param httpServletRequest
   * @param httpServletResponse
   * @return ActionForward
   */
  public ActionForward contactUsForgotID( ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse )
  {
    log.debug( ">> Entering contactUsForgotID" );
    ActionForward forward = actionMapping.findForward( ActionConstants.SUCCESS_FORWARD );
    ContactUsForm contactUsForm = (ContactUsForm)actionForm;
    contactUsForm.setShowMsg( "Y" );
    log.debug( "<< Exiting contactUsForgotID. forward=" + forward );
    return forward;

  } // end contactUsForgotID

  /**
   * Dispatch action to go to ContactUs
   * 
   * @param actionMapping
   * @param actionForm
   * @param httpServletRequest
   * @param httpServletResponse
   * @return forward
   */
  public ActionForward view( ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse )
  {
    String returnUrlParam = RequestUtils.getOptionalParamString( httpServletRequest, "returnUrl" );
    if ( StringUtils.isNotEmpty( returnUrlParam ) )
    {
      String queryString = RequestUtils.getOriginalRequestURIWithQueryString( httpServletRequest );
      if ( StringUtils.isNotEmpty( queryString ) )
      {
        String returnUrl = "";
        if ( queryString.indexOf( "returnUrl=" ) != -1 )
        {
          returnUrl = queryString.substring( queryString.indexOf( "returnUrl=" ) + 10 );
        }
        else
        {
          returnUrl = returnUrlParam;
        }
        httpServletRequest.setAttribute( "returnUrl", returnUrl );
      }
    }
    if ( isFullPageView( httpServletRequest ) )
    {
      httpServletRequest.setAttribute( "isFullPage", Boolean.TRUE );
      return actionMapping.findForward( ActionConstants.FULL_VIEW );
    }
    else
    {
      return actionMapping.findForward( ActionConstants.SHEET_VIEW );
    }
  }

  private boolean isFullPageView( HttpServletRequest request )
  {
    boolean isFullPage = false;
    String fullPageView = request.getParameter( "isFullPage" );
    if ( !StringUtils.isEmpty( fullPageView ) )
    {
      isFullPage = Boolean.valueOf( fullPageView );
    }
    return isFullPage;
  }

  private ContactUsService getContactUsService()
  {
    return (ContactUsService)getService( ContactUsService.BEAN_NAME );
  }

}
