/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/profile/TermsAndConditionsAction.java,v $
 */

package com.biperf.core.ui.profile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;

import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.security.AuthenticatedUser;
import com.biperf.core.service.participant.AudienceService;
import com.biperf.core.service.participant.ProfileService;
import com.biperf.core.service.security.AuthenticationService;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.constants.ViewAttributeNames;
import com.biperf.core.utils.UserManager;

/**
 * TermsAndConditionsAction
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
 * <td>Tammy Cheng</td>
 * <td>Mar 21, 2006</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class TermsAndConditionsAction extends BaseDispatchAction
{
  /**
   * The participant enters the T&Cs page. Forward to termsAndConditionsView.do
   * 
   * @param actionMapping
   * @param actionForm
   * @param httpServletRequest
   * @param httpServledtResponse
   * @return ActionForward
   */
  public ActionForward view( ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest httpServletRequest, HttpServletResponse httpServledtResponse )
  {
    String forward = "";
    forward = ActionConstants.FULL_VIEW;

    // If coming here from Registration, no need to save authentication because there is none
    if ( httpServletRequest.getSession().getAttribute( ViewAttributeNames.REGISTRATION_CODE ) == null )
    {
      // Save authentication on the form - this is because we have to first log the
      // user out when they get here, and then business requirements
      // wants the Pax to be able to go directly to the home page if they
      // accept the T&Cs, therefore we have to save the authentication info at this entry point.
      SecurityContext acegiContext = (SecurityContext)httpServletRequest.getSession().getAttribute( AuthenticationService.SPRING_SECURITY_CONTEXT );
      Authentication currentAuthentication = acegiContext.getAuthentication();
      if ( currentAuthentication.getPrincipal() instanceof AuthenticatedUser )
      {
        AuthenticatedUser authUser = (AuthenticatedUser)currentAuthentication.getPrincipal();

        httpServletRequest.getSession().setAttribute( "authUser", authUser );
      }
      // logout Pax to prevent hacking into the site
      logoutPax( httpServletRequest );
    }

    return actionMapping.findForward( forward );
  }

  /**
   * The participant accepts the Terms & Conditions. Forward to the homePage.do if successful,
   * otherwise, forward to logout.do.
   * 
   * @param actionMapping
   * @param actionForm
   * @param httpServletRequest
   * @param httpServletResponse
   * @return ActionForward
   */
  public ActionForward accept( ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse )
  {
    ActionForward actionForward = actionMapping.findForward( ActionConstants.SUCCESS_FORWARD );

    AuthenticatedUser authenticatedUser = (AuthenticatedUser)httpServletRequest.getSession().getAttribute( "authUser" );

    // If coming here from Registration, no need to save T&C because there is no participant yet.
    if ( httpServletRequest.getSession().getAttribute( ViewAttributeNames.REGISTRATION_CODE ) == null )
    {
      try
      {
        ProfileService profileService = getProfileService();
        profileService.setTermsAndConditions( authenticatedUser.getUserId(), true );

        // Pax was previously logged out by the system, so log 'em in now T&Cs accepted
        loginPax( httpServletRequest, authenticatedUser );
      }
      catch( ServiceErrorException e )
      {
        actionForward = actionMapping.findForward( ActionConstants.LEAVE_SITE );
      }

      // refresh criteria audience for this pax
      try
      {
        getAudienceService().rematchParticipantForAllCriteriaAudiences( authenticatedUser.getUserId() );
      }
      catch( ServiceErrorException e )
      {
        log.debug( "Error refreshing Criteria Audience for user:" + authenticatedUser.getUserId() + "]" + e );
      }

      // forward to either the home page or the change password page
      if ( !authenticatedUser.isCredentialsNonExpired() )
      {
        actionForward = actionMapping.findForward( ActionConstants.FORWARD_TO_CHANGE_PASSWORD );
      }

    }
    else
    {
      // forward to self Enroll Registration page.
      actionForward = actionMapping.findForward( ActionConstants.VALIDATED_FORWARD );
    }

    return actionForward;
  }

  /**
   * The participant declines the Terms & Conditions. Forward to Logout.do
   * 
   * @param actionMapping
   * @param actionForm
   * @param httpServletRequest
   * @param httpServletResponse
   * @return ActionForward
   */
  public ActionForward decline( ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse )
  {
    ActionForward actionForward = actionMapping.findForward( ActionConstants.LEAVE_SITE );

    AuthenticatedUser authenticatedUser = (AuthenticatedUser)httpServletRequest.getSession().getAttribute( "authUser" );

    // If coming here from Registration, no need to save T&C because there is no participant yet.
    if ( httpServletRequest.getSession().getAttribute( ViewAttributeNames.REGISTRATION_CODE ) == null )
    {
      try
      {
        ProfileService profileService = getProfileService();
        profileService.setTermsAndConditions( authenticatedUser.getUserId(), false );
      }
      catch( ServiceErrorException e )
      {
        // nothing to do, going to logout.do already
      }
    }
    return actionForward;
  }

  /**
   * The participant cancels after clicking the Decline button. Forward to termsAndConditionsView.do
   * 
   * @param actionMapping
   * @param actionForm
   * @param httpServletRequest
   * @param httpServledtResponse
   * @return ActionForward
   */
  public ActionForward cancel( ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest httpServletRequest, HttpServletResponse httpServledtResponse )
  {
    String forward = "";
    forward = ActionConstants.CANCEL_TO_TC_VIEW;

    // logout Pax to prevent hacking into the site
    logoutPax( httpServletRequest );

    return actionMapping.findForward( forward );
  }

  /**
   * The participant clicks back button after viewing the T&Cs page. Forward to Logout.do
   * 
   * @param actionMapping
   * @param actionForm
   * @param httpServletRequest
   * @param httpServletResponse
   * @return actionForward
   */
  public ActionForward back( ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse )
  {
    ActionForward actionForward = actionMapping.findForward( ActionConstants.LEAVE_SITE );

    return actionForward;
  }

  /**
   * The participant views the Terms and Conditions page after logging in.
   * 
   * @param actionMapping
   * @param actionForm
   * @param httpServletRequest
   * @param httpServledtResponse
   * @return ActionForward
   */
  public ActionForward review( ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest httpServletRequest, HttpServletResponse httpServledtResponse )
  {
    httpServletRequest.getSession().setAttribute( "reviewing", Boolean.TRUE );
    if ( isFullPage( httpServletRequest ) )
    {
      return actionMapping.findForward( ActionConstants.FULL_VIEW );
    }
    else
    {
      return actionMapping.findForward( ActionConstants.SHEET_VIEW );
    }
  }

  private boolean isFullPage( HttpServletRequest request )
  {
    boolean isFullPage = false;
    String fullPageView = request.getParameter( "isFullPage" );
    if ( !StringUtils.isEmpty( fullPageView ) )
    {
      isFullPage = Boolean.valueOf( fullPageView );
    }
    return isFullPage;
  }

  /**
   * Logs the user out. Clears from security and user manager.
   * 
   * @param httpServletRequest
   */
  private void logoutPax( HttpServletRequest httpServletRequest )
  {
    SecurityContext acegiContext = (SecurityContext)httpServletRequest.getSession().getAttribute( AuthenticationService.SPRING_SECURITY_CONTEXT );

    acegiContext.setAuthentication( null );

    UserManager.removeUser();
  }

  /**
   * Logs the user in with the saved authentication.
   * 
   * @param httpServletRequest
   * @param authenticatedUser
   */
  private void loginPax( HttpServletRequest httpServletRequest, AuthenticatedUser authenticatedUser )
  {
    SecurityContext acegiContext = (SecurityContext)httpServletRequest.getSession().getAttribute( AuthenticationService.SPRING_SECURITY_CONTEXT );

    Authentication authentication = new UsernamePasswordAuthenticationToken( authenticatedUser, authenticatedUser.getPassword(), authenticatedUser.getAuthorities() );
    acegiContext.setAuthentication( authentication );

    httpServletRequest.getSession().setAttribute( AuthenticationService.SPRING_SECURITY_CONTEXT, acegiContext );

  }

  private ProfileService getProfileService()
  {
    return (ProfileService)getService( ProfileService.BEAN_NAME );
  }

  private AudienceService getAudienceService()
  {
    return (AudienceService)getService( AudienceService.BEAN_NAME );
  }

}
