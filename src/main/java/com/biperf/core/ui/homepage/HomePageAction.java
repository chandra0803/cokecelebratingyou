/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/homepage/HomePageAction.java,v $
 */

package com.biperf.core.ui.homepage;

import static com.biperf.core.utils.Environment.ENV_DEV;
import static com.biperf.core.utils.Environment.getEnvironment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.biperf.core.domain.enums.FilterSetupType;
import com.biperf.core.domain.enums.LanguageType;
import com.biperf.core.domain.enums.PickListItemSortOrderComparator;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.system.PropertySetItem;
import com.biperf.core.service.home.FilterAppSetupService;
import com.biperf.core.service.home.HomePageFilterHolder;
import com.biperf.core.service.home.ModuleAppFilterMap;
import com.biperf.core.service.ids.IDService;
import com.biperf.core.service.maincontent.DesignThemeService;
import com.biperf.core.service.participant.AudienceService;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.service.rewardoffering.RewardOfferingsService;
import com.biperf.core.service.security.AuthorizationService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.help.ContactUsAction;
import com.biperf.core.ui.managertoolkit.ManagerAlertAction;
import com.biperf.core.ui.managertoolkit.PlateauAwardsReminderAction;
import com.biperf.core.ui.productclaim.ClaimSubmittedBean;
import com.biperf.core.ui.recognition.RecognitionSentBean;
import com.biperf.core.ui.recognitionadvisor.RARecognitionFlowBean;
import com.biperf.core.ui.survey.ParticipantTakeSurveyAction;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.InvalidClientStateException;
import com.biperf.core.utils.MailNavigationUtil;
import com.biperf.core.utils.MeshServicesUtil;
import com.biperf.core.utils.NewServiceAnniversaryUtil;
import com.biperf.core.utils.RecognitionAdvisorUtil;
import com.biperf.core.utils.StringUtil;
import com.biperf.core.utils.UserManager;
import com.biperf.core.utils.UserSessionAttributes;
import com.biperf.core.utils.fedresources.FEDResourceLocatorFactory;
import com.biperf.core.utils.fedresources.UserFEDResources;
import com.biperf.core.value.PurlContributorInviteValue;
import com.objectpartners.cms.util.ContentReaderManager;

/**
 * HomePageAction.
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
 * <td>crosenquest</td>
 * <td>Mar 31, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class HomePageAction extends BaseDispatchAction
{
  private static final Log logger = LogFactory.getLog( HomePageAction.class );

  // ---------------------------------------------------------------------------
  // Fields
  // ---------------------------------------------------------------------------

  // ---------------------------------------------------------------------------
  // Public Methods
  // ---------------------------------------------------------------------------

  /**
   * Dispatcher.  Default to home page display.  Too much work to append 'method=display'
   * to all the paths that lead to the home page.  
   */
  public ActionForward execute( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {

    if ( mapping.getParameter() != null )
    {
      return super.execute( mapping, form, request, response );
    }
    else
    {
      return this.display( mapping, form, request, response );
    }
  }

  /**
   * Display home page.
   */
  public ActionForward display( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    // SSO Cookie url for DMSA
    if ( NewServiceAnniversaryUtil.isNewServiceAnniversaryEnabled() && !ENV_DEV.equalsIgnoreCase( getEnvironment() ) )
    {
      storeSSOEndPoint( request );
    }

    String forward = "homePageDefault";
    String switchProfile = request.getParameter( "switchProfile" );
    request.setAttribute( "invites", request.getSession().getAttribute( "invites" ) );
    request.getSession().removeAttribute( "invites" );
    // Removing session variable
    request.getSession().removeAttribute( "ssoLogin" );
    request.getSession().setAttribute( "loggedInUserDate", UserManager.getUserDatePattern() );
    String languageCode;
    if ( !StringUtil.isEmpty( request.getParameter( "cmsLocaleCode" ) ) )
    {
      languageCode = request.getParameter( "cmsLocaleCode" );
    }
    else
    {
      languageCode = UserManager.getUserLocale().toString().replace( "_", "-" );
    }
    request.getSession().setAttribute( "feLocaleCode", languageCode );

    checkForClientStateParams( request );

    // Save Login Info In DB. CR -- 21595
    request.getSession().removeAttribute( "paxIds" );
    if ( request.getSession().getAttribute( "loginInfoSaved" ) == null )
    {
      getUserService().saveLoginInfo( UserManager.getUserId() );
      // through below Session variable we can achieve the below constraint
      // **** Do NOT insert a record when an administrator launches the site as a participant. *****
      request.getSession().setAttribute( "loginInfoSaved", "yes" );
    }
    /*
     * If standalone, forward it to different home page.
     */
    if ( UserManager.getUser().isPaxInactive() && !UserManager.getUser().isLaunched() )
    {
      forward = "login";
    }
    else if ( UserManager.getUser().isUser() )
    {
      forward = "homePageAdmin";
    }
    else if ( UserManager.getUser().isDelegate() && StringUtils.isEmpty( switchProfile ) )
    {
      forward = "homePageDelegate";
    }
    else
    {
      getEligiblePromotions( request );// prime the promotion list..
      buildFilteredModuleList( request );
      request.setAttribute( "filterPage", Boolean.TRUE );
    }
    buildUserFEDResources( request );
    request.setAttribute( "sliderRefreshRate", new Integer( getRefreshInterval() ) );

    if ( logger.isDebugEnabled() )
    { // added for auto builds test
      logger.debug( "forward:" + forward );
    }

    if ( Objects.isNull( request.getSession().getAttribute( UserSessionAttributes.PARTICIPANT_FOLLOWERS ) ) )
    {
      request.getSession().setAttribute( UserSessionAttributes.PARTICIPANT_FOLLOWERS, getParticipantService().getFollowersByUserId( UserManager.getUserId() ) );
    }

    request.getSession().setAttribute( UserSessionAttributes.ELIGIBLE_PROMOTIONS_EXIST, CollectionUtils.isNotEmpty( (List)request.getSession().getAttribute( "eligiblePromotions" ) ) );

    // RA recognition sent confirmation modal
    if ( getSystemVariableService().getPropertyByName( SystemVariableService.RA_ENABLE ).getBooleanVal() && !Objects.isNull( request.getSession().getAttribute( RARecognitionFlowBean.KEY ) ) )
    {
      RARecognitionFlowBean.moveToRequest( request );
    }

    // recognition sent confirmation modal
    RecognitionSentBean.moveToRequest( request );

    // contact us sent confirmation modal
    ContactUsAction.moveToRequest( request );

    // alert sent confirmation modal
    ManagerAlertAction.moveToRequest( request );

    // plateau award reminders modal
    PlateauAwardsReminderAction.moveToRequest( request );

    // claim submitted confirmation modal
    ClaimSubmittedBean.moveToRequest( request );

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

    ParticipantTakeSurveyAction.moveToRequest( request );
    String ssiAdnminurl = (String)request.getSession().getAttribute( "ssiAdnminurl" );
    if ( UserManager.getUser().isSSIAdmin() && !StringUtil.isNullOrEmpty( ssiAdnminurl ) )
    {
      request.getSession().removeAttribute( "ssiAdnminurl" );
      response.sendRedirect( response.encodeRedirectURL( ssiAdnminurl ) );
      return null;
    }
    else if ( RecognitionAdvisorUtil.isValidFlow( request, false ) )
    {
      if ( !RecognitionAdvisorUtil.isLoginParamsHasErrorsPostLogin( request ) )
      {
        String reporteeIdEncrypted = (String)request.getSession().getAttribute( "reporteeId" );
        RecognitionAdvisorUtil.clearLoginParams( request );
        response.sendRedirect( RecognitionAdvisorUtil.getRedirectUrl( reporteeIdEncrypted ) );
        return null;
      }
      return mapping.findForward( forward );
    }

    else if ( !NewServiceAnniversaryUtil.isNewServiceAnniversaryEnabled() && MailNavigationUtil.isValidFlow( request ) )
    {
      try
      {
        if ( !MailNavigationUtil.isLoginParamsHasErrorsPostLogin( request ) )
        {
          String reDirectUrl = MailNavigationUtil.getRedirectUrl( request );
          MailNavigationUtil.clearCelebrationParams( request );
          response.sendRedirect( reDirectUrl );
          return null;
        }
      }
      catch( Exception e )
      {
        logger.error( "Celebration email navigation " + e );
        MailNavigationUtil.clearCelebrationParams( request );
      }
      return mapping.findForward( forward );

    }
    else
    {
      return mapping.findForward( forward );
    }
  }

  private int getRefreshInterval()
  {
    PropertySetItem prop = getSystemVariableService().getPropertyByName( SystemVariableService.TIP_DAY_ROTATE_SECONDS );
    // apply default 10 seconds if there is no value. JS takes milli-seconds
    return null != prop ? prop.getIntVal() * 1000 : 10000;
  }

  private void buildUserFEDResources( HttpServletRequest request )
  {
    HttpSession session = request.getSession();
    if ( null == session.getAttribute( UserSessionAttributes.USER_FED_RESOURCES ) )
    {
      if ( UserManager.getUser().isUser() )
      {
        session.setAttribute( UserSessionAttributes.USER_FED_RESOURCES, UserFEDResources.enableAllModules() );
      }
      else
      {
        session.setAttribute( UserSessionAttributes.USER_FED_RESOURCES, getFEDResourceLocatorFactory().getUserFEDResources( request ) );
      }
    }
  }

  private void buildFilteredModuleList( HttpServletRequest request )
  {
    if ( null == request.getSession().getAttribute( "filteredModuleList" ) )
    {
      Participant participant = getParticipantService().getParticipantById( UserManager.getUserId() );
      boolean isLoginAs = getAuthorizationService().isUserInRole( AuthorizationService.ROLE_CODE_LOGIN_AS );

      HomePageFilterHolder filterHolder = getFilterAppSetupService().getHomePageModulesForUser( participant, buildSessionMap( request ) );
      List<ModuleAppFilterMap> filteredModuleList = filterHolder.getModuleAppFilterMap();

      List<ModuleAppFilterMap> finalFilteredModuleList = new ArrayList<ModuleAppFilterMap>();
      for ( Iterator<ModuleAppFilterMap> iter = filteredModuleList.iterator(); iter.hasNext(); )
      {
        ModuleAppFilterMap filterMap = iter.next();

        // New Service Anniversary Celebration Module Tile Enabling.
        if ( NewServiceAnniversaryUtil.isNewServiceAnniversaryEnabled() && filterMap.getModule().getTileMappingType().getCode().equals( "purlCelebrateModule" ) )
        {
          filterMap.getModule().getTileMappingType().setCode( "newServiceAnniversaryModule" );
          filterMap.getModule().setAppName( "newServiceAnniversary" );
        }

        if ( filterMap.getModule().getTileMappingType().isShopTile() )
        {
          if ( !isLoginAs )
          {
            finalFilteredModuleList.add( filterMap );
          }
        }
        else
        {
          finalFilteredModuleList.add( filterMap );
        }

      }
      request.getSession().setAttribute( "filteredModuleList", finalFilteredModuleList );

      // Get valid Filter List, remove any filters that do not have eligible modules
      List<FilterSetupType> filterSetupList = filterHolder.getActiveFilters();
      filterSetupList.add( FilterSetupType.lookup( FilterSetupType.REPORTS ) );
      Collections.sort( filterSetupList, new PickListItemSortOrderComparator() );
      request.getSession().setAttribute( "filterList", filterSetupList );
      request.getSession().setAttribute( "priorityOneFilter", filterSetupList.get( 0 ).getCode() );
    }
  }

  private SystemVariableService getSystemVariableService()
  {
    return (SystemVariableService)getService( SystemVariableService.BEAN_NAME );
  }

  private UserService getUserService()
  {
    return (UserService)getService( UserService.BEAN_NAME );
  }

  private ParticipantService getParticipantService()
  {
    return (ParticipantService)getService( ParticipantService.BEAN_NAME );
  }

  private FilterAppSetupService getFilterAppSetupService()
  {
    return (FilterAppSetupService)getService( FilterAppSetupService.BEAN_NAME );
  }

  /**
   * Clears FilterAppSetup cache
   * 
   * @param mapping the action mapping used to select this instance.
   * @param form the ActionForm bean for this request.
   * @param request the HTTP request we are processing.
   * @param response the HTTP response we are creating.
   * @return an <code>ActionForward</code> instance describing where and how control should be
   *         forwarded, or null if the response has already been completed.
   */
  public ActionForward clearFilterAppSetupCache( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    FilterAppSetupService filterAppSetupService = (FilterAppSetupService)getService( FilterAppSetupService.BEAN_NAME );
    filterAppSetupService.clearFilterAppSetupCache();
    return mapping.findForward( ActionConstants.SUCCESS_DETAILS );
  }

  /**
   * Clears RewardOfferings cache
   * 
   * @param mapping the action mapping used to select this instance.
   * @param form the ActionForm bean for this request.
   * @param request the HTTP request we are processing.
   * @param response the HTTP response we are creating.
   * @return an <code>ActionForward</code> instance describing where and how control should be
   *         forwarded, or null if the response has already been completed.
   */
  public ActionForward clearRewardOfferingsCache( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    RewardOfferingsService rewardOfferingsService = (RewardOfferingsService)getService( RewardOfferingsService.BEAN_NAME );
    rewardOfferingsService.destroy();
    return mapping.findForward( ActionConstants.SUCCESS_FORWARD );
  }

  /**
   * Clears the skin cache for the specified user.
   * 
   * @param mapping the action mapping used to select this instance.
   * @param form the ActionForm bean for this request.
   * @param request the HTTP request we are processing.
   * @param response the HTTP response we are creating.
   * @return an <code>ActionForward</code> instance describing where and how control should be
   *         forwarded, or null if the response has already been completed.
   */
  public ActionForward clearSkinCache( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    ActionForward forward = mapping.findForward( ActionConstants.SUCCESS_FORWARD );
    ActionMessages errors = new ActionMessages();

    try
    {
      DesignThemeService designThemeService = (DesignThemeService)getService( DesignThemeService.BEAN_NAME );
      designThemeService.clearSkinCache( UserManager.getUser() );
    }
    catch( IllegalArgumentException e )
    {
      errors.add( "errorMessage", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, "userId" ) );
    }

    if ( errors.size() != 0 )
    {
      saveErrors( request, errors );
      forward = mapping.findForward( ActionConstants.FAIL_ERRORS_ONLY );
    }

    return forward;
  }

  /**
   * Clears criteria audience cache
   * 
   * @param mapping the action mapping used to select this instance.
   * @param form the ActionForm bean for this request.
   * @param request the HTTP request we are processing.
   * @param response the HTTP response we are creating.
   * @return an <code>ActionForward</code> instance describing where and how control should be
   *         forwarded, or null if the response has already been completed.
   */
  public ActionForward clearCriteriaAudienceCache( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    AudienceService audienceService = (AudienceService)getService( AudienceService.BEAN_NAME );
    audienceService.clearCriteriaAudienceCache();
    return mapping.findForward( ActionConstants.SUCCESS_FORWARD );
  }

  public ActionForward clearPromoEligibilityCache( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    AudienceService audienceService = (AudienceService)getService( AudienceService.BEAN_NAME );
    audienceService.clearPromoEligibilityCache();
    return mapping.findForward( ActionConstants.SUCCESS_FORWARD );
  }

  /**
   * Bug 2800
   * Update the user session with the new language. This will not update database.
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public ActionForward updateLanguagePreference( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    ActionMessages errors = new ActionMessages();
    if ( !StringUtil.isEmpty( request.getParameter( "cmsLocaleCode" ) ) )
    {
      String languageCode = request.getParameter( "cmsLocaleCode" );
      request.setAttribute( "userLanguage", LanguageType.lookup( languageCode ) );
      request.getSession().setAttribute( "cmsLocaleCode", languageCode );
      request.getSession().removeAttribute( "filteredModuleList" );

      String feLocaleCode = languageCode.replaceAll( "_", "-" );
      request.getSession().setAttribute( "feLocaleCode", feLocaleCode );

      int index = languageCode.indexOf( '_' );
      if ( index == -1 )
      {
        UserManager.getUser().setLocale( new Locale( languageCode ) );
      }
      else
      {
        UserManager.getUser().setLocale( new Locale( languageCode.substring( 0, index ), languageCode.substring( index + 1 ) ) );
      }

      // As per the functionality, whenever pax change their language it should not update their
      // user preferences langua.
      /*
       * try { Long userId = UserManager.getUserId(); Participant participant =
       * getParticipantService().getParticipantById(userId);
       * getParticipantService().updateParticipantPreferences( userId, null, null, null,
       * languageCode, null, null, participant.isAllowPublicRecognition(),
       * participant.isAllowPublicInformation(), participant.isAllowPublicBirthDate(),
       * participant.isAllowPublicHireDate()); } catch( Exception e ) { log.debug( e ); saveErrors(
       * request, errors ); }
       */
    }
    return mapping.findForward( ActionConstants.SUCCESS_FORWARD );
  }

  @SuppressWarnings( { "rawtypes" } )
  private void checkForClientStateParams( HttpServletRequest request ) throws InvalidClientStateException
  {
    String clientState = RequestUtils.getOptionalParamString( request, "clientState" );
    if ( StringUtils.isNotBlank( clientState ) )
    {
      String cryptoPass = RequestUtils.getOptionalParamString( request, "cryptoPass" );
      String password = ClientStatePasswordManager.getPassword();

      if ( cryptoPass != null && cryptoPass.equals( "1" ) )
      {
        password = ClientStatePasswordManager.getGlobalPassword();
      }
      Map clientStateMap = ClientStateSerializer.deserialize( clientState, password );
      // to prevent buffer overflow exception in the request header if invites is too long
      // request.setAttribute( "purlInvites", clientStateMap.get( "invites" ) );
      request.setAttribute( "purlListUrl", clientStateMap.get( "purlListUrl" ) );
      request.setAttribute( "budgetConfirmation", clientStateMap.get( "budgetConfirmation" ) );
      if ( (List<PurlContributorInviteValue>)request.getSession().getAttribute( "invites" ) != null )
      {
        request.setAttribute( "purlInvites", request.getSession().getAttribute( "invites" ) );
        request.getSession().removeAttribute( "invites" );
      }
    }
  }

  @SuppressWarnings( "rawtypes" )
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

  private void storeSSOEndPoint( HttpServletRequest request )
  {
    try
    {
      String ssoUrl = getIDService().getIDSSsoUrl();

      if ( ssoUrl.contains( "https" ) )
      {
        request.setAttribute( "idsSSOEndPoint", ssoUrl );
      }
      else
      {
        request.setAttribute( "idsSSOEndPoint", MeshServicesUtil.getNackleMeshServicesBaseEndPoint() + "/id" + ssoUrl );
      }

    }
    catch( Exception e )
    {
      logger.error( "IDService SSO End point : " + e );
    }
  }

  private AuthorizationService getAuthorizationService()
  {
    return (AuthorizationService)getService( AuthorizationService.BEAN_NAME );
  }

  private FEDResourceLocatorFactory getFEDResourceLocatorFactory()
  {
    return (FEDResourceLocatorFactory)getService( FEDResourceLocatorFactory.BEAN_NAME );
  }

  private IDService getIDService()
  {
    return (IDService)getService( IDService.BEAN_NAME );
  }

}
