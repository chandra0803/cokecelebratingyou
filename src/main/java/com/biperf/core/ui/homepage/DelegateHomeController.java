
package com.biperf.core.ui.homepage;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.tiles.ComponentContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.biperf.acegi.authentication.dao.UserCredential;
import com.biperf.core.domain.homepage.ModuleApp;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.proxy.Proxy;
import com.biperf.core.domain.user.UserCookiesAcceptance;
import com.biperf.core.security.AuthenticatedUser;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.home.FilterAppSetupService;
import com.biperf.core.service.maincontent.MainContentService;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.participant.UserCookiesAcceptanceService;
import com.biperf.core.service.proxy.ProxyAssociationRequest;
import com.biperf.core.service.proxy.ProxyService;
import com.biperf.core.service.security.AuthenticationService;
import com.biperf.core.service.ssi.SSIContestService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.ui.BaseController;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.PromotionMenuBean;
import com.biperf.util.StringUtils;
import com.objectpartners.cms.util.ContentReaderManager;

public class DelegateHomeController extends BaseController
{
  /**
   * Overridden from @see com.biperf.core.ui.BaseController#onExecute(org.apache.struts.tiles.ComponentContext, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, javax.servlet.ServletContext)
   * @param tileContext
   * @param request
   * @param response
   * @param servletContext
   * @throws Exception
   */
  public void onExecute( ComponentContext tileContext, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext ) throws Exception
  {
    String clientState = RequestUtils.getOptionalParamString( request, "clientState" );
    String cryptoPass = RequestUtils.getOptionalParamString( request, "cryptoPass" );
    String password = ClientStatePasswordManager.getPassword();
    String delegateId = "";
    if ( cryptoPass != null && cryptoPass.equals( "1" ) )
    {
      password = ClientStatePasswordManager.getGlobalPassword();
    }
    if ( !StringUtils.isEmpty( clientState ) )
    {
      Map clientStateMap = ClientStateSerializer.deserialize( clientState, password );
      delegateId = (String)clientStateMap.get( "id" );
      request.getSession().removeAttribute( "eligiblePromotions" );
      request.getSession().removeAttribute( "pointsView" );
    }
    else
    {
      delegateId = UserManager.getUserId() + "";
    }

    if ( !StringUtils.isEmpty( delegateId ) )
    {
      AuthenticatedUser originalUser = null;
      if ( UserManager.getUser().isDelegate() )
      {
        originalUser = UserManager.getUser().getOriginalAuthenticatedUser();
      }
      else
      {
        originalUser = UserManager.getUser();
      }
      Participant delegatedUser = getParticipantService().getParticipantById( Long.valueOf( delegateId ) );
      UserCredential userCredentials = new UserCredential( delegatedUser.getId().longValue(), delegatedUser.getUserName(), delegatedUser.getPassword() );
      AuthenticatedUser newAuthenticatedUser = getAuthenticationService().buildAuthenticatedUser( delegatedUser, userCredentials );
      // Bug 3417 - Set logged in user locale when acting as delegate
      newAuthenticatedUser.setLocale( originalUser.getLocale() );
      newAuthenticatedUser.setDelegate( true );
      newAuthenticatedUser.setOriginalAuthenticatedUser( originalUser );

      Object principal = newAuthenticatedUser;
      UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken( principal, userCredentials, newAuthenticatedUser.getAuthorities() );

      authentication.setDetails( newAuthenticatedUser );
      // With new ACEGI upgrade, the constructor already sets authenticated to true..
      // authentication.setAuthenticated( true );

      SecurityContext sc = SecurityContextHolder.getContext();
      sc.setAuthentication( authentication );

      if ( newAuthenticatedUser != null && newAuthenticatedUser.getUserId() > 0 )
      {
        UserManager.removeUser();
        UserManager.setUser( newAuthenticatedUser );
      }
    }
    List<ModuleApp> moduleApps = new ArrayList<ModuleApp>();
    getEligiblePromotions( request );// prime the promotion list..
    moduleApps = buildFilteredModuleList( request );
    request.setAttribute( "moduleList", moduleApps );

    // GDPR Compliance start
    request.setAttribute( "cookiesAccepted", true );
    UserCookiesAcceptance userAccpObj = getUserCookiesAcceptanceService().getUserCookiesAcceptanceDetailsByPaxID( UserManager.getUserId() );
    if ( Objects.isNull( userAccpObj ) )
    {
      String envUrl = getSystemVariableService().getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();
      String policyUrl = MessageFormat.format( ContentReaderManager.getText( "recognition.submit", "COOKIES_BODY" ), new Object[] { envUrl } );
      request.setAttribute( "policyUrl", policyUrl );
      request.setAttribute( "cookiesAccepted", false );
    }
    // GDPR Compliance end

    // billboard image
    String imageUrl = ContentReaderManager.getText( "participant.search.view", "BILL_BOARD_IMAGE" );
    String IMAGE_PATTERN = "([^\\s]+(\\.(?i)(jpeg|jpg|png|gif|JPEG|JPG|PNG|GIF))$)";
    Pattern pattern = Pattern.compile( IMAGE_PATTERN );
    Matcher matcher = pattern.matcher( imageUrl );
    request.setAttribute( "billBoardImageUrl", false );
    if ( matcher.matches() )
    {
      request.setAttribute( "billBoardImageUrl", true );
    }

    // Hide settings tab for delegators (They have nothing to see there). Participant themselves and
    // admin can see it.
    if ( UserManager.getUser().isDelegate() )
    {
      request.setAttribute( "hideSettingsTab", true );
    }

    isMyGroupEnabled( request, UserManager.getUser() );

  }

  private List<ModuleApp> buildFilteredModuleList( HttpServletRequest request )
  {
    Participant participant = getParticipantService().getParticipantById( UserManager.getUserId() );
    List<ModuleApp> moduleApps = getFilterAppSetupService().getHomePageModulesForDelegate( participant, buildSessionMap( request ) );
    return moduleApps;
  }

  protected List<PromotionMenuBean> getEligiblePromotions( HttpServletRequest request )
  {
    if ( UserManager.getUser().isParticipant() )
    {
      List<PromotionMenuBean> eligiblePromotions = (List<PromotionMenuBean>)request.getSession().getAttribute( "eligiblePromotions" );
      if ( null != eligiblePromotions )
      {
        return eligiblePromotions;
      }
      else
      {
        eligiblePromotions = getMainContentService().buildEligiblePromoList( UserManager.getUser() );
        Proxy proxy = null;
        AssociationRequestCollection proxyAssociationRequestCollection = new AssociationRequestCollection();
        proxyAssociationRequestCollection.add( new ProxyAssociationRequest( ProxyAssociationRequest.ALL ) );
        proxy = getProxyService().getProxyByUserAndProxyUserWithAssociations( UserManager.getUserId(),
                                                                              UserManager.getUser().getOriginalAuthenticatedUser().getUserId(),
                                                                              proxyAssociationRequestCollection );
        eligiblePromotions = getProxyService().getPromotionsAllowedForDelegate( eligiblePromotions, proxy );
        request.getSession().setAttribute( "eligiblePromotions", eligiblePromotions );
        return eligiblePromotions;
      }
    }
    return null;
  }

  private Map<String, Object> buildSessionMap( HttpServletRequest request )
  {
    Map<String, Object> sessionMap = new HashMap<String, Object>();
    HttpSession session = request.getSession();
    Enumeration keys = session.getAttributeNames();
    while ( keys.hasMoreElements() )
    {
      String key = (String)keys.nextElement();
      sessionMap.put( key, session.getAttribute( key ) );
    }
    return sessionMap;
  }

  private void isMyGroupEnabled( HttpServletRequest request, AuthenticatedUser authUser )
  {
    boolean isGiver = false;
    boolean enableMyGroups = true;
    List<PromotionMenuBean> eligiblePromotions = getEligiblePromotions( request );
    if ( !Objects.isNull( eligiblePromotions ) )
    {
      isGiver = eligiblePromotions.stream().filter( p -> p.isCanSubmit() ).findFirst().isPresent();
      if ( !isGiver )
      {
        if ( !getSSIContestService().isContestCreator( authUser.getUserId() ) )
        {
          enableMyGroups = false;
        }
      }
    }
    else
    {
      enableMyGroups = false;
    }
    request.getSession().setAttribute( "enableMyGroups", enableMyGroups );
  }

  private SSIContestService getSSIContestService()
  {
    return (SSIContestService)getService( SSIContestService.BEAN_NAME );
  }

  public static ParticipantService getParticipantService()
  {
    return (ParticipantService)getService( ParticipantService.BEAN_NAME );
  }

  private SystemVariableService getSystemVariableService()
  {
    return (SystemVariableService)getService( SystemVariableService.BEAN_NAME );
  }

  private FilterAppSetupService getFilterAppSetupService()
  {
    return (FilterAppSetupService)getService( FilterAppSetupService.BEAN_NAME );
  }

  private MainContentService getMainContentService()
  {
    return (MainContentService)getService( MainContentService.BEAN_NAME );
  }

  private AuthenticationService getAuthenticationService()
  {
    return (AuthenticationService)getService( AuthenticationService.BEAN_NAME );
  }

  private ProxyService getProxyService()
  {
    return (ProxyService)getService( ProxyService.BEAN_NAME );
  }

  private static UserCookiesAcceptanceService getUserCookiesAcceptanceService()
  {
    return (UserCookiesAcceptanceService)getService( UserCookiesAcceptanceService.BEAN_NAME );
  }

}
