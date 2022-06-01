
package com.biperf.core.ui.purl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import com.biperf.core.domain.enums.LanguageType;
import com.biperf.core.domain.enums.PurlContributorState;
import com.biperf.core.domain.purl.PurlContributor;
import com.biperf.core.domain.user.User;
import com.biperf.core.exception.DataException;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.participant.UserAssociationRequest;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.service.serviceanniversary.ServiceAnniversaryService;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.utils.ActionUtils;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.ui.utils.RosterUtil;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.ClientStateUtils;
import com.biperf.core.utils.NewServiceAnniversaryUtil;
import com.biperf.core.utils.StringUtil;
import com.biperf.core.utils.UserManager;

import botdetect.web.Captcha;

public class PurlTNCAction extends BasePurlContributionAction
{
  private static final Log logger = LogFactory.getLog( PurlTNCAction.class );

  // private ClientServiceImpl clientServiceImpl = new ClientServiceImpl();

  public ActionForward display( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    Long purlContributorId = null;
    Long purlRecipientId = null;
    Long userId = null;
    Long promotionId = null;
    PurlContributor contributor = null;

    String clientState = RequestUtils.getRequiredParamString( request, "clientState" );
    String cryptoPass = RequestUtils.getOptionalParamString( request, "cryptoPass" );
    String password = ClientStatePasswordManager.getPassword();

    if ( cryptoPass != null && cryptoPass.equals( "1" ) )
    {
      password = ClientStatePasswordManager.getGlobalPassword();
    }
    // Deserialize the client state.
    Map clientStateMap = ClientStateSerializer.deserialize( clientState, password );
    try
    {
      purlContributorId = (Long)clientStateMap.get( "purlContributorId" );
      purlRecipientId = (Long)clientStateMap.get( "purlRecipientId" );
      userId = (Long)clientStateMap.get( "userId" );
      promotionId = (Long)clientStateMap.get( "promotionId" );
    }
    catch( ClassCastException cce )
    {
      purlContributorId = new Long( (String)clientStateMap.get( "purlContributorId" ) );
    }

    // For New SA Redirect to new SA Url from old mail link
    if ( NewServiceAnniversaryUtil.isNewServiceAnniversaryEnabled() && null != purlContributorId )
    {
      try
      {
        String celebrationId = getServiceAnniversaryService().getCelebrationId( purlContributorId );

        if ( !StringUtil.isEmpty( celebrationId ) )
        {
          String url = getServiceAnniversaryService().getContributePageUrl( celebrationId, RosterUtil.getRandomUUId().toString() );
          if ( !StringUtil.isEmpty( url ) )
          {
            response.sendRedirect( url );
            return null;
          }
        }
      }
      catch( DataException e )
      {
        logger.error( "Error while getting page url contributor Id : " + purlContributorId + " " + e.getMessage() );
      }
      catch( Exception e )
      {
        logger.error( "Error while getting page url contributor Id : " + purlContributorId + " " + e.getMessage() );
      }
      return mapping.findForward( "invalid" );
    }
    // End New SA

    List languages = LanguageType.getList();
    if ( languages.size() == 1 )
    {
      // only display language selection if there is more than one active language.
      languages = new ArrayList();
    }
    request.setAttribute( "languageList", languages );

    if ( logger.isDebugEnabled() )
    {
      logger.debug( "PurlContributorId : " + purlContributorId );
    }

    if ( !UserManager.isUserLoggedIn() )
    {
      // Bug 50810 - Reload the same page when change language is clicked and user is not logged in
      Map<String, Object> purlContributionParamMap = new HashMap<String, Object>();
      purlContributionParamMap.put( "purlContributorId", purlContributorId );
      request.setAttribute( "changeLanguageForwardUrl", ClientStateUtils.generateEncodedLink( "", "/purl/purlTNC.do?method=display", purlContributionParamMap ) );
    }

    if ( purlContributorId != null && !getPurlService().isValidForContribution( purlContributorId ) )
    {
      return mapping.findForward( "invalid" );
    }
    else
    {
      if ( purlContributorId == null )
      {
        contributor = getPurlService().getPurlContributorByPromotionIdandUserId( userId, purlRecipientId, promotionId );
        purlContributorId = contributor != null ? contributor.getId() : null;
      }

      if ( purlContributorId != null )
      {
        contributor = getPurlService().getPurlContributorById( purlContributorId );
        // Change the state to contribution after the first time TNC are accepted
        if ( UserManager.isUserLoggedIn() && PurlContributorState.INVITATION.equals( contributor.getState().getCode() ) )
        {
          contributor.setState( PurlContributorState.lookup( PurlContributorState.CONTRIBUTION ) );
          contributor = getPurlService().savePurlContributor( contributor );
        }
      }

      // Create Back Button URL
      PurlTNCForm purlTNCForm = (PurlTNCForm)form;
      if ( contributor == null )
      {
        contributor = new PurlContributor();
        request.setAttribute( "contributorBackURL", null );
      }
      else
      {
        purlTNCForm.setPurlContributor( contributor );
        request.getSession().setAttribute( "purlContributor", contributor );
        request.setAttribute( "contributorBackURL", buildContributorBackUrl( contributor ) );
      }
    }

    return mapping.findForward( ActionConstants.DISPLAY_FORWARD );
  }

  public ActionForward submit( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    Long purlContributorId = null;

    PurlContributor pc = (PurlContributor)request.getSession().getAttribute( "purlContributor" );

    if ( pc != null )
    {
      purlContributorId = pc.getId();
    }

    PurlTNCForm purlTNCForm = (PurlTNCForm)form;

    PurlContributor contributor = null;
    if ( ( purlTNCForm.getUserId() != null && purlTNCForm.getUserId() != 0 ) || ( UserManager.isUserLoggedIn() && UserManager.getUserId() != 0 ) )
    {

      try
      {
        if ( purlTNCForm.getAcceptTNC().booleanValue() )
        {
          if ( purlContributorId == null )
          {
            contributor = new PurlContributor();
            User user = null;
            AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
            associationRequestCollection.add( new UserAssociationRequest( UserAssociationRequest.EMAIL ) );
            user = getUserService().getUserByIdWithAssociations( purlTNCForm.getUserId(), associationRequestCollection );

            // check if the record for logged in user and purl_recipient is already available in
            // purlcontributor table and then SAVE
            PurlContributor purlContributor = getPurlService().getPurlContributorByUserId( purlTNCForm.getUserId(), purlTNCForm.getPurlRecipientId() );
            if ( purlContributor == null )
            {
              contributor.setPurlRecipient( getPurlService().getPurlRecipientById( purlTNCForm.getPurlRecipientId() ) );
              contributor.setUser( user );
              contributor.setFirstName( user.getFirstName() );
              contributor.setLastName( user.getLastName() );
              contributor.setEmailAddr( user.getPrimaryEmailAddress().getEmailAddr() );
              contributor.setAvatarUrl( getParticipantService().getParticipantById( user.getId() ).getAvatarOriginal() );
              contributor.setState( PurlContributorState.lookup( PurlContributorState.INVITATION ) );
              contributor.setDefaultInvitee( false );
              contributor = getPurlService().savePurlContributor( contributor );
              purlContributorId = contributor.getId();
            }
            if ( purlContributor != null && UserManager.isUserLoggedIn() )
            {
              purlContributorId = purlContributor.getId();
              // Change the state to contribution
              if ( PurlContributorState.INVITATION.equals( purlContributor.getState().getCode() ) )
              {
                contributor.setState( PurlContributorState.lookup( PurlContributorState.CONTRIBUTION ) );
                contributor = getPurlService().savePurlContributor( purlContributor );
              }
            }
          }
          else if ( purlContributorId != null )
          {
            // Change the state to contribution

            PurlContributor existingPurlContributor = getPurlService().getPurlContributorById( purlContributorId );
            if ( PurlContributorState.INVITATION.equals( existingPurlContributor.getState().getCode() ) )
            {
              contributor.setState( PurlContributorState.lookup( PurlContributorState.CONTRIBUTION ) );
              contributor = getPurlService().savePurlContributor( existingPurlContributor );
            }
          }
        }
        request.getSession().removeAttribute( "purlContributor" );
      }

      catch( ClassCastException cce )
      {
        logger.error( cce.getMessage() );
      }
    }

    ActionMessages errors = new ActionErrors();

    if ( logger.isDebugEnabled() )
    {
      logger.debug( "Terms and Conditions accepted? " + purlTNCForm.getAcceptTNC() );
    }

    if ( purlTNCForm.getAcceptTNC().booleanValue() )
    {
      boolean isUserLoggedIn = UserManager.isUserLoggedIn();
      boolean verifyCaptcha = false;
      if ( logger.isDebugEnabled() )
      {
        logger.debug( "Is USER logged into the system? " + isUserLoggedIn );
      }

      if ( !isUserLoggedIn )
      {
        verifyCaptcha = verifyCaptcha( request );
        if ( logger.isDebugEnabled() )
        {
          logger.debug( "Verify Captcha " + ( verifyCaptcha ? "SUCCESS" : "FAILURE" ) );
        }

        if ( !verifyCaptcha )
        {
          errors.add( "captcha", new ActionMessage( "purl.tnc.error.params.CAPTCHA_FAILED" ) );
          saveErrors( request, errors );
          return mapping.findForward( ActionConstants.DISPLAY_FORWARD );
        }
      }
      Map clientStateParameterMap = new HashMap();
      if ( purlTNCForm.getPurlContributor().getId() != null && purlTNCForm.getPurlContributor().getId() != 0 )
      {
        Long contributorId = purlTNCForm.getPurlContributor().getId();

        clientStateParameterMap.put( "purlContributorId", contributorId );
        clientStateParameterMap.put( "verifyCaptcha", verifyCaptcha );
      }
      else
      {
        clientStateParameterMap.put( "purlContributorId", purlContributorId );
        clientStateParameterMap.put( "verifyCaptcha", verifyCaptcha );
      }

      if ( logger.isDebugEnabled() )
      {
        logger.debug( "Purl Contributor ID : " + purlContributorId );
      }

      return ActionUtils.forwardWithParameters( mapping, ActionConstants.SUCCESS_FORWARD, new String[] { ClientStateUtils.generateEncodedLink( "", "", clientStateParameterMap ), "method=display" } );
    }
    return mapping.findForward( ActionConstants.CANCEL_TO_HOMEPAGE );
  }

  private boolean verifyCaptcha( HttpServletRequest request )
  {
    String captchaId = request.getSession().getId();
    String response = request.getParameter( "captchaResponse" );
    Captcha captcha = Captcha.load( request, "loginFormCaptcha" );
    boolean isCaptchaOk = false;
    try
    {
      isCaptchaOk = captcha.validate( request, response );
      return isCaptchaOk;
    }
    catch( Exception e )
    {
      // Unable to verify
    }

    return false;
  }

  private UserService getUserService()
  {
    return (UserService)getService( UserService.BEAN_NAME );
  }

  private ParticipantService getParticipantService()
  {
    return (ParticipantService)getService( ParticipantService.BEAN_NAME );
  }

  private ServiceAnniversaryService getServiceAnniversaryService()
  {
    return (ServiceAnniversaryService)getService( ServiceAnniversaryService.BEAN_NAME );
  }

}
