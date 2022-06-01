
package com.biperf.core.ui.participant;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.domain.enums.AboutMeQuestionType;
import com.biperf.core.domain.participant.AboutMe;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.participant.ProfileService;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.ClientStateUtils;
import com.biperf.core.utils.InvalidClientStateException;
import com.biperf.core.utils.PageConstants;
import com.biperf.core.value.AboutMeValueBean;

public class PersonalInfoAdminMaintainAction extends BaseDispatchAction
{
  private static final Log logger = LogFactory.getLog( PersonalInfoAdminMaintainAction.class );

  public ActionForward display( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {

    PersonalInfoAdminMaintainForm personalInfoForm = (PersonalInfoAdminMaintainForm)form;

    // Put the user id onto the form
    Long userId = getUserId( request );

    List<AboutMe> aboutMeList = getProfileService().getAllAboutMeByUserId( userId );

    personalInfoForm.load( aboutMeList );

    personalInfoForm.setUserId( userId.toString() );

    loadPaxAvatar( personalInfoForm );

    return mapping.findForward( ActionConstants.DISPLAY_FORWARD );
  }

  public ActionForward deleteAvatar( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    PersonalInfoAdminMaintainForm personalInfoForm = (PersonalInfoAdminMaintainForm)form;
    Long userId = getUserId( request );
    try
    {
      getProfileService().deleteAvatar( userId );
    }
    catch( Exception e )
    {
      logger.error( "Error during remove profile image" );
    }
    loadPaxAvatar( personalInfoForm );
    return mapping.findForward( ActionConstants.DISPLAY_FORWARD );
  }

  /**
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public ActionForward save( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    PersonalInfoAdminMaintainForm personalInfoForm = (PersonalInfoAdminMaintainForm)form;
    Long participantId = new Long( personalInfoForm.getUserId() );
    List<AboutMeValueBean> aboutMeValueBeans = personalInfoForm.getAboutMeQuestions();
    try
    {
      for ( Iterator<AboutMeValueBean> iterator = aboutMeValueBeans.iterator(); iterator.hasNext(); )
      {
        AboutMeValueBean aboutMeValueBean = iterator.next();
        AboutMe aboutMe = getProfileService().getAboutMeByUserIdAndCode( participantId, aboutMeValueBean.getAboutmeQuestioncode() );
        if ( aboutMe != null )
        {
          if ( StringUtils.isNotEmpty( aboutMeValueBean.getAboutmeAnswer() ) )
          {
            if ( !aboutMeValueBean.getAboutmeAnswer().equals( aboutMe.getAnswer() ) )
            {
              aboutMe.setAnswer( aboutMeValueBean.getAboutmeAnswer() );
              getProfileService().saveAboutMe( aboutMe ); // update
            }
          }
          else if ( StringUtils.isEmpty( aboutMeValueBean.getAboutmeAnswer() ) )
          {
            getProfileService().deleteAboutMe( aboutMe );
          }
        }
        else if ( StringUtils.isNotEmpty( aboutMeValueBean.getAboutmeAnswer() ) )
        {
          aboutMe = new AboutMe();
          aboutMe.setAnswer( aboutMeValueBean.getAboutmeAnswer() );
          AboutMeQuestionType aboutMeQuestionType = AboutMeQuestionType.lookup( aboutMeValueBean.getAboutmeQuestioncode() );
          aboutMe.setAboutMeQuestionType( aboutMeQuestionType );
          Participant participant = getParticipantService().getParticipantById( participantId );
          aboutMe.setUser( participant );
          getProfileService().saveAboutMe( aboutMe );
        }
      }
    }
    catch( Exception e1 )
    {
      logger.error( e1.getMessage(), e1 );
    }
    loadPaxAvatar( personalInfoForm );
    // append the domain object id to tell the detail page which id we are referring to.
    Map<String, Long> clientStateMap = new HashMap<String, Long>();
    clientStateMap.put( "userId", participantId );
    String redirectUrl = RequestUtils.getBaseURI( request ) + ClientStateUtils.generateEncodedLink( "", PageConstants.PARTICIPANT_OVERVIEW, clientStateMap ) + "&method=display";
    response.sendRedirect( redirectUrl );
    return null;
  }

  private void loadPaxAvatar( PersonalInfoAdminMaintainForm personalInfoForm )
  {
    Participant pax = getParticipantService().getParticipantById( new Long( personalInfoForm.getUserId() ) );

    if ( pax.getAvatarOriginal() != null )
    {
      personalInfoForm.setDefaultImage( false );
    }
    else
    {
      personalInfoForm.setDefaultImage( true );
    }
    personalInfoForm.setAvatarUrl( pax.getAvatarOriginalFullPath() );

  }

  private Long getUserId( HttpServletRequest request )
  {
    Long userId;
    if ( RequestUtils.containsAttribute( request, "userId" ) )
    {
      userId = RequestUtils.getRequiredAttributeLong( request, "userId" );
    }
    else
    {
      try
      {
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
          String userIdString = (String)clientStateMap.get( "userId" );
          userId = new Long( userIdString );
        }
        catch( ClassCastException cce )
        {
          userId = (Long)clientStateMap.get( "userId" );
        }

      }
      catch( InvalidClientStateException e )
      {
        throw new IllegalArgumentException( "request parameter clientState was missing" );
      }
    }
    return userId;
  }

  private ProfileService getProfileService()
  {
    return (ProfileService)getService( ProfileService.BEAN_NAME );
  }

  private ParticipantService getParticipantService()
  {
    return (ParticipantService)getService( ParticipantService.BEAN_NAME );
  }
}
