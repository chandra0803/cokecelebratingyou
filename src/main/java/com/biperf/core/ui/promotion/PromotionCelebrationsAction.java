
package com.biperf.core.ui.promotion;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.biperf.core.domain.promotion.AdminCelebrationAvatarObjectView;
import com.biperf.core.domain.promotion.AdminPurlAvatarObjectView;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.RecognitionPromotion;
import com.biperf.core.exception.BeaconRuntimeException;
import com.biperf.core.service.celebration.CelebrationService;
import com.biperf.core.service.profileavatar.ProfileAvatarService;
import com.biperf.core.service.promotion.CelebrationsUpdateAssociation;
import com.biperf.core.service.quiz.QuizService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.constants.ViewAttributeNames;
import com.biperf.core.ui.utils.JsonResponse;
import com.biperf.core.ui.utils.RosterUtil;
import com.biperf.core.ui.utils.ValidatorChecks;
import com.biperf.core.utils.ProfileAvatarUploadUtil;
import com.biperf.core.utils.StringUtil;
import com.biperf.core.value.CelebrationAvatarUploadValue;
import com.biperf.core.value.QuizFileUploadValue;
import com.objectpartners.cms.util.CmsResourceBundle;

public class PromotionCelebrationsAction extends PromotionBaseDispatchAction
{
  private static final Log logger = LogFactory.getLog( PromotionCelebrationsAction.class );

  public ActionForward unspecified( ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    return display( mapping, actionForm, request, response );
  }

  public ActionForward display( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    String forwardTo = ActionConstants.SUCCESS_FORWARD;

    PromotionCelebrationsForm celebrationsForm = (PromotionCelebrationsForm)form;
    Promotion promotion = null;

    // WIZARD MODE
    if ( request.getSession().getAttribute( ViewAttributeNames.PAGE_MODE ).equals( ViewAttributeNames.WIZARD_MODE ) )
    {
      promotion = getWizardPromotion( request );
    }
    // NORMAL MODE
    else
    {
      String promotionId = celebrationsForm.getPromotionId().toString();
      if ( promotionId != null && promotionId.length() > 0 )
      {
        promotion = getPromotionService().getPromotionById( new Long( promotionId ) );
      }
    }

    Promotion attachedPromotion = getPromotionService().getPromotionById( promotion != null ? promotion.getId() : null );
    RecognitionPromotion recPromotion = (RecognitionPromotion)attachedPromotion;
    if ( recPromotion.getDefaultCelebrationAvatar() != null )
    {
      request.getSession().setAttribute( "imageUrlPath", recPromotion.getDefaultCelebrationAvatar() );
    }
    celebrationsForm.load( attachedPromotion );

    return mapping.findForward( forwardTo );
  }

  /**
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward save( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    ActionForward forward = mapping.findForward( ActionConstants.SUCCESS_FORWARD );

    PromotionCelebrationsForm celebrationsForm = (PromotionCelebrationsForm)form;
    ActionMessages errors = new ActionMessages();

    Promotion promotion = null;

    if ( isCancelled( request ) )
    {
      if ( isWizardMode( request ) )
      {
        forward = super.cancelPromotion( request, mapping, errors );
      }
      else
      {
        forward = getCancelForward( mapping, request );
      }

      return forward;
    }

    // WIZARD MODE
    if ( request.getSession().getAttribute( ViewAttributeNames.PAGE_MODE ).equals( ViewAttributeNames.WIZARD_MODE ) )
    {
      promotion = getWizardPromotion( request );
    }
    else
    {
      promotion = getPromotionService().getPromotionById( new Long( celebrationsForm.getPromotionId() ) );

    }
    try
    {
      promotion = celebrationsForm.toDomainObject( promotion );
      String imageUrlFullPath = (String)request.getSession().getAttribute( "imageUrlPath" );

      if ( celebrationsForm.isAllowDefaultMessage() )
      {
        if ( imageUrlFullPath != null && !imageUrlFullPath.isEmpty() )
        {
          ( (RecognitionPromotion)promotion ).setDefaultCelebrationAvatar( imageUrlFullPath );
          celebrationsForm.setImageUrl( imageUrlFullPath );
        }
        if ( !StringUtils.isEmpty( celebrationsForm.getDefaultMessage() ) )
        {
          promotion = getPromotionService().savePromotionCelebrationCmText( promotion, celebrationsForm.getDefaultMessage() );
        }
        String withoutHtml = (String)StringUtil.escapeHTML( celebrationsForm.getImageUrl() );
        if ( imageUrlFullPath == null && ( celebrationsForm.getImageUrl() == null || celebrationsForm.getImageUrl().isEmpty() ) || withoutHtml.contains( "undefined" ) )
        {
          errors.add( "defaultCelebrationAvatar", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "promotion.basics.AVATAR" ) ) );
          saveErrors( request, errors );
          forward = mapping.findForward( ActionConstants.FAIL_FORWARD );
          return forward;
        }
      }

      CelebrationsUpdateAssociation celebrationsUpdateAssociation = new CelebrationsUpdateAssociation( promotion );

      List updateAssociations = new ArrayList();

      updateAssociations.add( celebrationsUpdateAssociation );

      promotion = getPromotionService().savePromotion( new Long( celebrationsForm.getPromotionId() ), updateAssociations );

    }

    catch( Exception e )
    {
      throw new BeaconRuntimeException( "This call shouldn't change any unique fields, so must be software bug.", e );
    }

    if ( !errors.isEmpty() )
    {
      saveErrors( request, errors );
      forward = mapping.findForward( ActionConstants.FAIL_FORWARD );
    }
    else
    {
      if ( isWizardMode( request ) )
      {
        setPromotionInWizardManager( request, promotion );

        forward = getWizardNextPage( mapping, request, promotion );
      }
      else
      {
        forward = saveAndExit( mapping, request, promotion );
      }
    }

    return forward;
  }

  private String getFileExtension( String filename )
  {
    return FilenameUtils.getExtension( filename );
  }

  public ActionForward processPhoto( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    String status = JsonResponse.STATUS_UNKNOWN;
    String globalUniqueId = "";
    boolean detailImgmovetoWebDav = false;
    boolean thumbImgmovetoWebDav = false;

    String imageSizeValidMessage = CmsResourceBundle.getCmsBundle().getString( "quiz.learningForm.VALID_IMAGE_SIZE" );
    String imageSizeLowerLimitMessage = CmsResourceBundle.getCmsBundle().getString( "quiz.learningForm.VALID_IMAGE_LOWER_LIMIT" );
    try
    {
      PromotionCelebrationsForm promotionCelebrationsForm = (PromotionCelebrationsForm)form;
      String promotionName = promotionCelebrationsForm.getPromotionName() != null ? promotionCelebrationsForm.getPromotionName() : "";
      String orginalfilename = promotionCelebrationsForm.getFileAsset().getFileName();
      String extension = "." + getFileExtension( orginalfilename );
      String filename = orginalfilename.substring( 0, orginalfilename.length() - extension.length() );
      if ( filename != null )
      {
        filename = ValidatorChecks.removesSpecialCharactersAndSpaces( filename );
      }
      filename = filename + extension;
      int filesize = promotionCelebrationsForm.getFileAsset().getFileSize();
      byte[] imageInByte = promotionCelebrationsForm.getFileAsset().getFileData();

      // Not getting the parameter through AJAX file upload, so get it from the session
      globalUniqueId = String.valueOf( new Date().getTime() );

      CelebrationAvatarUploadValue data = new CelebrationAvatarUploadValue();
      // data.setId( quizLearningForm.getQuizFormId() );
      data.setName( promotionCelebrationsForm.getPromotionName() );
      data.setData( imageInByte );
      data.setType( QuizFileUploadValue.TYPE_PICTURE );
      data.setName( filename );
      data.setSize( filesize );
      boolean validImageSize = getCelebrationService().validFileData( data );
      if ( validImageSize )
      {
        String imageInBytes = Base64.getEncoder().encodeToString( imageInByte );
        String contentType = promotionCelebrationsForm.getFileAsset().getContentType();

        String avatarUrl = null;

        avatarUrl = getProfileAvatarService().uploadDefaultPurlAvatar( RosterUtil.getRandomUUId().toString(), ProfileAvatarUploadUtil.getImageData( contentType, imageInBytes ) );

        if ( Objects.nonNull( avatarUrl ) )
        {
          status = JsonResponse.STATUS_SUCCESS;
          String imageUrlPath = avatarUrl;
          request.getSession().setAttribute( "imageUrlPath", imageUrlPath );

          AdminCelebrationAvatarObjectView celebrationAvatarView = new AdminCelebrationAvatarObjectView();
          celebrationAvatarView.setFull( imageUrlPath );
          celebrationAvatarView.setFilename( data.getName() );
          celebrationAvatarView.setPromotionName( promotionName );
          celebrationAvatarView.setId( globalUniqueId );
          celebrationAvatarView.setImageurl( imageUrlPath );
          celebrationAvatarView.setMedia( "avatar" );
          celebrationAvatarView.setStatus( status );
          celebrationAvatarView.setThumb( imageUrlPath );
          writeUploadJsonToResponse( celebrationAvatarView, response );
        }
        else
        {
          log.error( "Uploading avatar to profile service status : " + avatarUrl );
          status = JsonResponse.STATUS_FAILED;
          AdminCelebrationAvatarObjectView celebrationAvatarView = new AdminCelebrationAvatarObjectView();
          celebrationAvatarView.setStatus( status );
          celebrationAvatarView.setId( globalUniqueId );
          celebrationAvatarView.setFail( CmsResourceBundle.getCmsBundle().getString( "profile.personal.info.PLEASE_TRY_LATER" ) );
        }

      }
      else
      {
        status = JsonResponse.STATUS_FAILED;
        AdminCelebrationAvatarObjectView celebrationAvatarView = new AdminCelebrationAvatarObjectView();
        double lowerSizeLimit = 1024 * 1024 * .001;
        if ( data.getSize() <= lowerSizeLimit )
        {
          celebrationAvatarView.setFail( imageSizeLowerLimitMessage );
        }
        else
        {
          celebrationAvatarView.setFail( imageSizeValidMessage );
        }
        celebrationAvatarView.setStatus( status );
        writeUploadJsonToResponse( celebrationAvatarView, response );
      }
    }
    catch( Throwable e )
    {
      status = JsonResponse.STATUS_FAILED;
      AdminPurlAvatarObjectView purlAvatarView = new AdminPurlAvatarObjectView();
      purlAvatarView.setStatus( status );
      purlAvatarView.setId( globalUniqueId );
      purlAvatarView.setFail( imageSizeValidMessage );
      logger.error( "Error during recognition process photo:" + e );
      e.printStackTrace();
    }
    return null;
  }

  /**
   * We need to set the content type as text/html for upload.
   * Default is text/html
   */
  private void writeUploadJsonToResponse( Object bean, HttpServletResponse response ) throws IOException
  {
    PrintWriter out = response.getWriter();
    out.print( toJson( bean ) );
    out.flush();
    out.close();
  }

  private SystemVariableService getSystemVariableService()
  {
    return (SystemVariableService)getService( SystemVariableService.BEAN_NAME );
  }

  private QuizService getQuizService()
  {
    return (QuizService)getService( QuizService.BEAN_NAME );
  }

  private CelebrationService getCelebrationService()
  {
    return (CelebrationService)getService( CelebrationService.BEAN_NAME );
  }

  private ProfileAvatarService getProfileAvatarService()
  {
    return (ProfileAvatarService)getService( ProfileAvatarService.BEAN_NAME );
  }

}
