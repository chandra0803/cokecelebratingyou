
package com.biperf.core.ui.profile;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;

import com.biperf.core.domain.WebErrorMessage;
import com.biperf.core.domain.enums.AboutMeQuestionType;
import com.biperf.core.domain.participant.AboutMe;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.service.ProjectionAttribute;
import com.biperf.core.service.ProjectionCollection;
import com.biperf.core.service.client.CokeCareerMomentsService;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.participant.ProfileService;
import com.biperf.core.service.profileavatar.ProfileAvatarService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.WebErrorMessageList;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.utils.ValidatorChecks;
import com.biperf.core.utils.ImageUtils;
import com.biperf.core.utils.UserManager;
import com.biperf.core.utils.WebResponseConstants;
import com.biperf.core.value.AboutMeValueBean;
import com.biperf.core.value.PersonalInfoFileUploadValue;
import com.objectpartners.cms.util.CmsResourceBundle;

/**
 * PersonalInfoAction.
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
 * <td>robinrsa</td>
 * <td>Dec. 21, 2009</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class PersonalInfoAction extends BaseDispatchAction
{
  private static final Log logger = LogFactory.getLog( PersonalInfoAction.class );

  private static final String PARTICIPANT_STATE = "SESSION_PARTICIPANT_STATE";

  public ActionForward unspecified( ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    try
    {
      return display( mapping, actionForm, request, response );
    }
    catch( Exception e )
    {
      logger.error( e.getMessage(), e );
    }
    return null;
  }

  private void initParticipantState( HttpServletRequest request, Long participantId )
  {
    request.getSession( false ).setAttribute( PARTICIPANT_STATE, new ParticipantState( participantId, 10 ) );
  }

  private void clearParticipantState( HttpServletRequest request )
  {
    request.getSession( false ).removeAttribute( PARTICIPANT_STATE );
  }

  public ActionForward display( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    PersonalInfoForm personalInfoForm = (PersonalInfoForm)form;
    Long participantId = UserManager.getUserId();

    List allAboutMeByUserId = getProfileService().getAllAboutMeByUserId( participantId );
    Map likesMap = getCokeCareerMomentsService().getAboutMeLikesByUserId( UserManager.getUserId(), allAboutMeByUserId );
    personalInfoForm.load( allAboutMeByUserId, likesMap );
    

    String siteUrlPrefix = getSystemVariableService().getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();
    request.setAttribute( "siteUrlPrefix", siteUrlPrefix );
    clearParticipantState( request );

    initParticipantState( request, participantId );

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
   * 
   * This method will called when user saves about me information from profile page.
   */
  public ActionForward saveAboutMeInfo( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    ActionMessages errors = new ActionMessages();
    if ( !isTokenValid( request, true ) )
    {
      // when the screen is edited and saved for second time , for the same request isTokenValid
      // become false for that issue , we are reseting the token
      resetToken( request );
    }

    PersonalInfoForm personalInfoForm = (PersonalInfoForm)form;
    Long participantId = UserManager.getUserId();
    WebErrorMessageList messages = new WebErrorMessageList();
    WebErrorMessage message = new WebErrorMessage( WebResponseConstants.RESPONSE_COMMAND_MODAL, WebResponseConstants.RESPONSE_TYPE_SERVER_CMD );
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
      setMessage( message, CmsResourceBundle.getCmsBundle().getString( "system.general.SUCCESS" ), CmsResourceBundle.getCmsBundle().getString( "profile.personal.info.ABOUTME_INFO" ) );
      messages.getMessages().add( message );
    }
    catch( Exception e1 )
    {
      logger.error( e1.getMessage(), e1 );
    }
    super.writeAsJsonToResponse( messages, response );
   
    return null;
  }

  /**
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return
   * @throws Exception
   * 
   * This method will called when user uploads any image from profile page.
   */
  public ActionForward uploadAvatar( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    PersonalInfoForm personalInfoForm = (PersonalInfoForm)form;

    // creating json view objects for success or failure message
    WebErrorMessage message = new WebErrorMessage( WebResponseConstants.RESPONSE_COMMAND_MODAL, WebResponseConstants.RESPONSE_TYPE_SERVER_CMD );
    ProfileAvatarImageUploadPropertiesView propertiesView = new ProfileAvatarImageUploadPropertiesView();
    ProfileAvatarImageUploadView profileUploadAvatar = new ProfileAvatarImageUploadView();

    // get pax and old avatar data
    ProjectionCollection projections = new ProjectionCollection();
    projections.add( new ProjectionAttribute( "avatarOriginal" ) );
    projections.add( new ProjectionAttribute( "avatarSmall" ) );
    projections.add( new ProjectionAttribute( "id" ) );
    Participant participant = getParticipantService().getParticipantByIdWithProjections( UserManager.getUserId(), projections );
    String paxAvatarSmall = participant.getAvatarSmall();

    if ( personalInfoForm.getProfileImage() != null )
    {
      if ( !ImageUtils.isImageFormatValid( personalInfoForm.getProfileImage().getContentType() ) )
      {
        setMessage( message, CmsResourceBundle.getCmsBundle().getString( "profile.personal.info.FAILED" ), CmsResourceBundle.getCmsBundle().getString( "profile.personal.info.INVALID_EXT" ) );
        propertiesView.setAvatarUrl( paxAvatarSmall );
      }
      // check for image size limit
      else if ( !ImageUtils.isImageSizeValid( personalInfoForm.getProfileImage().getFileSize() ) )
      {
        setMessage( message,
                    CmsResourceBundle.getCmsBundle().getString( "profile.personal.info.FAILED" ),
                    CmsResourceBundle.getCmsBundle().getString( "profile.personal.info.IMAGE_SIZE" ) + " " + ImageUtils.getImageSize() );
        propertiesView.setAvatarUrl( paxAvatarSmall );
      }
      else
      {
        String orginalfilename = personalInfoForm.getProfileImage().getFileName();
        String extension = "." + getFileExtension( orginalfilename );
        String filename = orginalfilename.substring( 0, orginalfilename.length() - extension.length() );
        if ( filename != null )
        {
          filename = ValidatorChecks.removesSpecialCharactersAndSpaces( filename );
          filename = ImageUtils.getValidFileName( filename );
        }
        filename = filename + extension;
        int filesize = personalInfoForm.getProfileImage().getFileSize();
        byte[] imageInByte = personalInfoForm.getProfileImage().getFileData();

        PersonalInfoFileUploadValue data = new PersonalInfoFileUploadValue();
        data.setId( UserManager.getUserId() );
        data.setData( imageInByte );
        data.setType( PersonalInfoFileUploadValue.TYPE_AVATAR );
        data.setName( filename );
        data.setSize( filesize );
        data.setContentType( personalInfoForm.getProfileImage().getContentType() );
        try
        {
          data = getProfileService().uploadAvatar( data, participant.getId() );
          propertiesView.setAvatarUrl( data.getThumb() );
          setMessage( message, CmsResourceBundle.getCmsBundle().getString( "system.general.SUCCESS" ), CmsResourceBundle.getCmsBundle().getString( "profile.personal.info.UPLOAD_SUCCESS" ) );
        }
        catch( Exception e )
        {
          e.printStackTrace();
          setMessage( message, CmsResourceBundle.getCmsBundle().getString( "profile.personal.info.FAILED" ), CmsResourceBundle.getCmsBundle().getString( "profile.personal.info.PLEASE_TRY_LATER" ) );
          propertiesView.setAvatarUrl( paxAvatarSmall );
        }
      }
    }
    else
    {
      setMessage( message, CmsResourceBundle.getCmsBundle().getString( "profile.personal.info.FAILED" ), CmsResourceBundle.getCmsBundle().getString( "profile.personal.info.PLEASE_TRY_LATER" ) );
    }

    // This should set to true all the time to show the model
    propertiesView.setIsSuccess( true );
    profileUploadAvatar.setProperties( propertiesView );
    profileUploadAvatar.getMessages().add( message );

    writeAsJsonToResponse( profileUploadAvatar, response );
    return null;
  }

  private void setMessage( WebErrorMessage message, String name, String text )
  {
    message.setName( name );
    message.setText( text );
  }

  /**
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return
   */
  public ActionForward cancel( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    return mapping.findForward( ActionConstants.CANCEL_FORWARD );
  }

  // Just for Avatar upload ajax responses, overriding the method in BaseDispatch since the content
  // type application/json is not working
  public void writeAsJsonToResponse( Object bean, HttpServletResponse response ) throws IOException
  {
    PrintWriter out = response.getWriter();
    out.print( toJson( bean ) );
    out.flush();
    out.close();
  }

  private void unSuccessfulAvatarUpLoadErrorMsg( WebErrorMessage message )
  {
    setMessage( message, CmsResourceBundle.getCmsBundle().getString( "profile.personal.info.FAILED" ), CmsResourceBundle.getCmsBundle().getString( "profile.personal.info.PLEASE_TRY_LATER" ) );

  }

  private String getFileExtension( String filename )
  {
    return FilenameUtils.getExtension( filename );
  }

  private ProfileService getProfileService()
  {
    return (ProfileService)getService( ProfileService.BEAN_NAME );
  }

  private ParticipantService getParticipantService()
  {
    return (ParticipantService)getService( ParticipantService.BEAN_NAME );
  }

  private static SystemVariableService getSystemVariableService()
  {
    return (SystemVariableService)getService( SystemVariableService.BEAN_NAME );
  }

  public ProfileAvatarService getProfileAvatarService()
  {
    return (ProfileAvatarService)getService( ProfileAvatarService.BEAN_NAME );
  }
  
  public CokeCareerMomentsService getCokeCareerMomentsService()
  {
    return (CokeCareerMomentsService)getService( CokeCareerMomentsService.BEAN_NAME );
  }

}
