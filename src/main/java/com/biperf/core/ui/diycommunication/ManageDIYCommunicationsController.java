
package com.biperf.core.ui.diycommunication;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.domain.diycommunications.DIYCommunications;
import com.biperf.core.domain.enums.LanguageType;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.security.AuthenticatedUser;
import com.biperf.core.service.participant.AudienceService;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.ui.BaseController;
import com.biperf.core.utils.UserManager;

public class ManageDIYCommunicationsController extends BaseController
{
  public void onExecute( ComponentContext tileContext, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext ) throws Exception
  {

    Long userId = UserManager.getUserId();
    boolean isUserInDIYBanner = false;
    boolean isUserInDIYNews = false;
    boolean isUserInDIYTips = false;
    boolean isUserInDIYResourceCenter = false;
    boolean showManagerAlert = false;

    Boolean bannerServerError = request.getAttribute( "serverReturnedErrorForBanner" ) != null ? request.getAttribute( "serverReturnedErrorForBanner" ) == Boolean.TRUE : false;
    Boolean newsServerError = request.getAttribute( "serverReturnedErrorForNews" ) != null ? request.getAttribute( "serverReturnedErrorForNews" ) == Boolean.TRUE : false;
    Boolean resourceCenterServerError = request.getAttribute( "serverReturnedErrorForResourceCenter" ) != null ? request.getAttribute( "serverReturnedErrorForResourceCenter" ) == Boolean.TRUE : false;
    Boolean tipsServerError = request.getAttribute( "serverReturnedErrorForTips" ) != null ? request.getAttribute( "serverReturnedErrorForTips" ) == Boolean.TRUE : false;

    if ( bannerServerError )
    {
      DIYCommunicationsBannerForm form = (DIYCommunicationsBannerForm)request.getAttribute( DIYCommunications.SESSION_BANNER_FORM );
      request.getSession().setAttribute( DIYCommunications.SESSION_BANNER_FORM, form );
    }
    else if ( newsServerError )
    {
      DIYCommunicationsNewsForm form = (DIYCommunicationsNewsForm)request.getAttribute( DIYCommunications.SESSION_NEWS_FORM );
      request.getSession().setAttribute( DIYCommunications.SESSION_NEWS_FORM, form );
    }
    else if ( resourceCenterServerError )
    {
      DIYCommunicationsResourceCenterForm form = (DIYCommunicationsResourceCenterForm)request.getAttribute( DIYCommunications.SESSION_RESOURCE_CENTER_FORM );
      request.getSession().setAttribute( DIYCommunications.SESSION_RESOURCE_CENTER_FORM, form );
    }
    else if ( tipsServerError )
    {
      DIYCommunicationsTipsForm form = (DIYCommunicationsTipsForm)request.getAttribute( DIYCommunications.SESSION_TIPS_FORM );
      request.getSession().setAttribute( DIYCommunications.SESSION_TIPS_FORM, form );
    }
    AuthenticatedUser user = UserManager.getUser();
    if ( !user.isDelegate() )
    {
      isUserInDIYBanner = getAudienceService().isUserInDIYCommAudience( userId, "DIY_BANNER_ADMIN" );
      request.setAttribute( "userInBannerAudience", isUserInDIYBanner );

      isUserInDIYNews = getAudienceService().isUserInDIYCommAudience( userId, "DIY_NEWS_ADMIN" );
      request.setAttribute( "userInNewsAudience", isUserInDIYNews );

      isUserInDIYTips = getAudienceService().isUserInDIYCommAudience( userId, "DIY_TIPS_ADMIN" );
      request.setAttribute( "userInTipsAudience", isUserInDIYTips );

      isUserInDIYResourceCenter = getAudienceService().isUserInDIYCommAudience( userId, "DIY_RESOURCE_ADMIN" );
      request.setAttribute( "userInResourceCenterAudience", isUserInDIYResourceCenter );
    }

    showManagerAlert = getSystemVariableService().getPropertyByName( SystemVariableService.MANAGER_SEND_ALERT ).getBooleanVal();

    Participant participant = getParticipantService().getParticipantById( UserManager.getUserId() );

    request.setAttribute( "languageList", LanguageType.getList() );
    request.setAttribute( "showManagerAlert", showManagerAlert && ( participant.isManager() || participant.isOwner() ) );

    request.setAttribute( "pdfSixeLimit", getSystemVariableService().getPropertyByName( SystemVariableService.SYSTEM_PDF_SIZE_LIMIT ).getIntVal() );
    request.setAttribute( "imgSixeLimit", getSystemVariableService().getPropertyByName( SystemVariableService.SYSTEM_IMG_SIZE_LIMIT ).getIntVal() );

  }

  private AudienceService getAudienceService()
  {
    return (AudienceService)getService( AudienceService.BEAN_NAME );
  }

  private SystemVariableService getSystemVariableService()
  {
    return (SystemVariableService)getService( SystemVariableService.BEAN_NAME );
  }

  public static ParticipantService getParticipantService()
  {
    return (ParticipantService)getService( ParticipantService.BEAN_NAME );
  }

}
