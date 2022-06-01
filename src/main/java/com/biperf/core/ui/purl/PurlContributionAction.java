
package com.biperf.core.ui.purl;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.biperf.core.domain.enums.PurlContributorState;
import com.biperf.core.domain.enums.PurlContributorVideoType;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.purl.PurlContributor;
import com.biperf.core.domain.purl.PurlContributorComment;
import com.biperf.core.domain.purl.PurlRecipient;
import com.biperf.core.domain.user.UserEmailAddress;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.mtc.MTCService;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.service.purl.PurlContributorCommentValidationException;
import com.biperf.core.service.purl.impl.PurlContributorAssociationRequest;
import com.biperf.core.service.purl.impl.PurlRecipientAssociationRequest;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.recognition.SupportedEcardVideoTypes;
import com.biperf.core.ui.recognition.state.PurlContributorBean;
import com.biperf.core.ui.utils.JsonResponse;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.ui.utils.ValidatorChecks;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.ClientStateUtils;
import com.biperf.core.utils.Environment;
import com.biperf.core.utils.ImageUtils;
import com.biperf.core.utils.InvalidClientStateException;
import com.biperf.core.utils.StringUtil;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.PurlContributorInviteValue;
import com.biperf.core.value.PurlFileUploadValue;
import com.biperf.core.value.PurlMediaUploadValue;
import com.biperf.core.value.mtc.v1.upload.UploadResponse;

import botdetect.web.Captcha;

public class PurlContributionAction extends BasePurlContributionAction
{
  private static final Log logger = LogFactory.getLog( PurlContributionAction.class );

  private static final int PHOTO_UPLOAD_LIMIT = 10;
  private static final String CONTRIBUTION_STATE = "SESSION_CONTRIBUTION_STATE";

  private void initContributorState( HttpServletRequest request, Long purlContributorId )
  {
    request.getSession( false ).setAttribute( CONTRIBUTION_STATE, new ContributionState( purlContributorId, PHOTO_UPLOAD_LIMIT ) );
  }

  private void clearContributorState( HttpServletRequest request )
  {
    request.getSession( false ).removeAttribute( CONTRIBUTION_STATE );
  }

  private ContributionState getContributorState( HttpServletRequest request )
  {
    return (ContributionState)request.getSession( false ).getAttribute( CONTRIBUTION_STATE );
  }

  @SuppressWarnings( "unchecked" )
  public ActionForward display( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    Long purlContributorId = getPurlContributorId( request );
    Boolean verifyCaptcha = getVerifyCaptchaStatus( request );

    if ( logger.isDebugEnabled() )
    {
      logger.debug( "PurlContributorId : " + purlContributorId );
    }

    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( new PurlContributorAssociationRequest( PurlContributorAssociationRequest.PURL_CONTRIBUTOR_USER_EMAIL ) );
    PurlContributor contributor = getPurlService().getPurlContributorById( purlContributorId, associationRequestCollection );
    String purlContributorEmail = null;

    if ( UserManager.isUserLoggedIn() && contributor.getUser() != null && contributor.getUser().getId().equals( UserManager.getUserId() ) )
    {
      contributor.setFirstName( contributor.getUser().getFirstName() );
      contributor.setLastName( contributor.getUser().getLastName() );
      Set<UserEmailAddress> emailAddresses = contributor.getUser().getUserEmailAddresses();
      for ( UserEmailAddress emailAddress : emailAddresses )
      {
        if ( emailAddress.isPrimary() )
        {
          purlContributorEmail = emailAddress.getEmailAddr();
          break;
        }
      }

      Participant participant = getParticipantService().getParticipantById( UserManager.getUserId() );

      if ( participant.getAvatarSmall() != null )
      {
        contributor.setAvatarUrl( participant.getAvatarSmall() );
      }
      else
      {
        contributor.setAvatarUrl( contributor.getAvatarUrl() );
      }

      request.setAttribute( "displayContributorSwitchModal", false );

      List<PurlContributorBean> contributorInvitees = new ArrayList<PurlContributorBean>();
      if ( contributor != null )
      {
        contributorInvitees = getExistingPurlInvitees( contributor );
      }
      request.setAttribute( "invitees", contributorInvitees );
    }
    else if ( !UserManager.isUserLoggedIn() && contributor.getUser() != null && !verifyCaptcha )
    {
      request.setAttribute( "displayContributorSwitchModal", false );
      return mapping.findForward( ActionConstants.LOGIN_FORWARD );
    }
    else
    {
      if ( !Boolean.FALSE.equals( request.getSession() != null ? request.getSession().getAttribute( "displayContributorSwitchModal" ) : null ) )
      {
        request.setAttribute( "displayContributorSwitchModal", true );
      }
    }

    PurlContributionForm purlContributorForm = (PurlContributionForm)form;
    purlContributorForm.setPurlContributor( contributor );

    // don't display original contributor's info in the back end when user is not logged in
    // and filling out information in the modal window.
    if ( request.getAttribute( "displayContributorSwitchModal" ) != null && request.getAttribute( "displayContributorSwitchModal" ).equals( true ) )
    {
      contributor.setFirstName( "" );
      contributor.setLastName( "" );
      contributor.setAvatarUrl( "" );
      request.setAttribute( "purlContributor", contributor );
    }
    else
    {
      request.setAttribute( "purlContributor", contributor );
    }
    request.setAttribute( "purlContributorEmail", purlContributorEmail );

    // Create Back Button URL
    request.setAttribute( "contributorBackURL", buildContributorBackUrl( contributor ) );
    
    // Client customizations for WIP #26532 starts
    boolean isAllowPurlOutsideInvites = getParticipantService().isAllowePurlOutsideInvites( contributor.getPurlRecipient().getUser().getId() );
    if ( isAllowPurlOutsideInvites )
    {
      request.setAttribute( "allowedDomains", "" );
    }
    else
    {
      // set the system default allowed domains if the user doesn't want to invite to outsiders
      String allowedDomains = getSystemVariableService().getPropertyByName( SystemVariableService.COKE_ACCEPTABLE_DOMAINS ).getStringVal();
      String output = "\"" + StringUtils.join( StringUtils.split( allowedDomains, "," ), "\",\"" ) + "\"";
      request.setAttribute( "allowedDomains", output );
    }
    // Client customizations for WIP #26532 ends
    // Clear the previous contribution state, if any
    clearContributorState( request );

    // Initialize a new contribution state
    initContributorState( request, purlContributorId );

    // Load image upload prefixes
    request.setAttribute( "finalPrefixURL", getFinalImagePrefixUrl() );
    request.setAttribute( "stagerPrefixURL", getStageImagePrefixUrl() );
    request.setAttribute( "siteUrl", getSiteUrl() );

    if ( !UserManager.isUserLoggedIn() )
    {
      // Bug 50810 - Reload the same page when change language is clicked and user is not logged in
      Map<String, Object> purlContributionParamMap = new HashMap<String, Object>();
      purlContributionParamMap.put( "purlContributorId", purlContributorId );
      request.setAttribute( "changeLanguageForwardUrl", ClientStateUtils.generateEncodedLink( "", "/purl/purlContribution.do?method=display", purlContributionParamMap ) );
    }

    // Removing displayContributorSwitchModal attribute from session for precaution purpose
    if ( request.getSession() != null )
    {
      request.getSession().removeAttribute( "displayContributorSwitchModal" );
    }

    return mapping.findForward( ActionConstants.DISPLAY_FORWARD );
  }

  public ActionForward updateName( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    String status = JsonResponse.STATUS_UNKNOWN;
    try
    {
      PurlContributionForm purlContributorForm = (PurlContributionForm)form;
      PurlContributor contributor = purlContributorForm.getPurlContributor();

      contributor = getPurlService().updatePurlContributorName( contributor );
      purlContributorForm.setPurlContributor( contributor );

      status = JsonResponse.STATUS_SUCCESS;
    }
    catch( Exception e )
    {
      status = JsonResponse.STATUS_FAILED;
      logger.error( "Error during conributor name update" );
    }

    request.setAttribute( "status", status );

    return mapping.findForward( "update_name_ajax_response" );
  }

  public ActionForward updateContributorPersonalInfo( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    String status = JsonResponse.STATUS_UNKNOWN;
    try
    {
      PurlContributionForm purlContributionForm = (PurlContributionForm)form;

      JSONObject jsonObject = new JSONObject( purlContributionForm.getData() );
      PurlContributorInviteValue newContributorData = createPurlContributorInviteValueFromJson( jsonObject );

      PurlContributor newContributor = createPurlContributorSelf( newContributorData, request );

      request.setAttribute( "firstName", newContributor.getFirstName() );
      request.setAttribute( "lastName", newContributor.getLastName() );
      request.setAttribute( "emailAddress", newContributorData.getEmailAddr() );
      request.setAttribute( "avatarUrl", newContributorData.getDisplayAvatarUrl() );
      request.setAttribute( "contributorID", newContributor.getId() );
      request.setAttribute( "finalPrefixURL", getFinalImagePrefixUrl() );
      request.setAttribute( "stagerPrefixURL", getStageImagePrefixUrl() );
      if ( newContributor.getPurlRecipient() != null )
      {
        request.setAttribute( "recipientId", newContributor.getPurlRecipient().getId() );
      }

      if ( StringUtils.isBlank( newContributorData.getAvatarUrl() ) )
      {
        getPurlService().deleteAvatarForContributor( newContributor.getId() );
      }
      else
      {
        getPurlService().postAvatarForContributor( newContributor.getId(), newContributorData.getAvatarUrl() );
      }

      status = JsonResponse.STATUS_SUCCESS;
    }
    catch( JSONException e )
    {
      status = JsonResponse.STATUS_FAILED;
      logger.error( "Error during json comment population" );
    }

    request.setAttribute( "status", status );
    return mapping.findForward( "update_contributor_info_ajax_response" );
  }

  public ActionForward processAvatar( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    String status = JsonResponse.STATUS_UNKNOWN;

    try
    {
      PurlContributionForm purlContributorForm = (PurlContributionForm)form;
      ContributionState state = getContributorState( request );
      Long purlContributorId = state.getContributorId();

      String orginalfilename = purlContributorForm.getFileAsset().getFileName();
      String extension = "." + getFileExtension( orginalfilename );
      String filename = orginalfilename.substring( 0, orginalfilename.length() - extension.length() );
      if ( filename != null )
      {
        filename = ValidatorChecks.removesSpecialCharactersAndSpaces( filename );
        filename = ImageUtils.getValidFileName( filename );
      }
      filename = filename + extension;
      int filesize = purlContributorForm.getFileAsset().getFileSize();
      byte[] imageInByte = purlContributorForm.getFileAsset().getFileData();

      PurlFileUploadValue data = new PurlFileUploadValue();
      data.setId( purlContributorId );
      data.setData( imageInByte );
      data.setType( PurlFileUploadValue.TYPE_AVATAR );
      data.setName( filename );
      data.setSize( filesize );
      data.setContentType( purlContributorForm.getFileAsset().getContentType() );

      data = getPurlService().uploadAvatarForContributor( data, false );

      if ( state.isAvatarUploaded() )
      {
        PurlContributor purlContributor = getPurlService().getPurlContributorById( purlContributorId );
        purlContributor.setAvatarUrl( state.getAvatarUrl() );
      }

      request.setAttribute( "finalPrefixURL", getFinalImagePrefixUrl() );
      request.setAttribute( "stagerPrefixURL", getStageImagePrefixUrl() );

      request.setAttribute( "avatarImgUrl", data.getThumb() );
      status = JsonResponse.STATUS_SUCCESS;
    }
    catch( Throwable e )
    {
      status = JsonResponse.STATUS_FAILED;
      logger.error( "Error during contributor upload avatar" );
    }

    request.setAttribute( "status", status );
    return mapping.findForward( "upload_avatar_ajax_response" );
  }

  public ActionForward deleteAvatar( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    String status = JsonResponse.STATUS_UNKNOWN;
    try
    {
      ContributionState state = getContributorState( request );

      getPurlService().deleteAvatarForContributor( state.getContributorId() );

      status = JsonResponse.STATUS_SUCCESS;
    }
    catch( Exception e )
    {
      status = JsonResponse.STATUS_FAILED;
      logger.error( "Error during remove profile image" );
    }

    request.setAttribute( "status", status );

    return mapping.findForward( "delete_avatar_ajax_response" );
  }

  public ActionForward previewComment( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    return mapping.findForward( "preview_comment" );
  }

  public ActionForward postComment( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    String status = JsonResponse.STATUS_UNKNOWN;
    String errorText = null;
    try
    {
      PurlContributionForm purlContributorForm = (PurlContributionForm)form;

      JSONArray jsonArray = new JSONArray( purlContributorForm.getData() );
      PurlContributorComment commentObject = createPurlContributorCommentFromJson( jsonArray.getJSONObject( 0 ) );

      boolean isVideo = !StringUtil.isEmpty( commentObject.getImageUrl() ) && SupportedEcardVideoTypes.isSupported( FilenameUtils.getExtension( commentObject.getImageUrl() ) );

      if ( isVideo )
      {
        String videoUrl = commentObject.getImageUrl();
        String videoUrlExtension = FilenameUtils.getExtension( videoUrl );
        String videoUrlThumb = commentObject.getImageUrlThumb();
        commentObject.setVideoType( PurlContributorVideoType.lookup( PurlContributorVideoType.DIRECT ) );
        commentObject.setImageUrl( null );
        commentObject.setImageUrlThumb( null );

        videoUrl = videoUrl.substring( "purl/".length() );
        videoUrl = getPurlService().getPurlVideoUrl( videoUrl );

        videoUrlThumb = videoUrlThumb.substring( "purl/".length() );
        UploadResponse uploadResponse = null;

        uploadResponse = getMTCService().uploadVideo( new URL( videoUrl ) );

        // We're hacking this thing together. URL we get back is truncated. Throw the full URL back
        // together.

        // to basename

        logger.error( "getRequestId" + uploadResponse.getRequestId() );
        videoUrlThumb = getPurlService().getPurlVideoUrl( videoUrlThumb );
        commentObject.setVideoUrl( videoUrl + ActionConstants.REQUEST_ID + uploadResponse.getRequestId() );
        commentObject.setVideoUrlExtension( videoUrlExtension );
        commentObject.setVideoUrlThumb( videoUrlThumb );
      }

      logger.error( commentObject );

      Long purlContributorId = commentObject.getPurlContributor().getId();
      PurlContributorComment purlComment = getPurlService().postCommentForContributor( purlContributorId, commentObject );

      String finalImagePrefixUrl;
      String stagerImagePrefixUrl;
      if ( isVideo )
      {
        finalImagePrefixUrl = "";
        stagerImagePrefixUrl = "";
      }
      else
      {
        finalImagePrefixUrl = getFinalImagePrefixUrl();
        stagerImagePrefixUrl = getStageImagePrefixUrl();
        request.setAttribute( "imageServiceUrlThumb", purlComment.getImageUrlThumb() );
        request.setAttribute( "imageServiceUrlSrc", purlComment.getImageUrl() );
      }
      request.setAttribute( "finalPrefixURL", finalImagePrefixUrl );
      request.setAttribute( "stagerPrefixURL", stagerImagePrefixUrl );

      request.setAttribute( "purlComment", purlComment );
      status = JsonResponse.STATUS_SUCCESS;
    }
    catch( JSONException e )
    {
      status = JsonResponse.STATUS_FAILED;
      logger.error( "Error during json comment population" );
    }
    catch( PurlContributorCommentValidationException e )
    {
      status = JsonResponse.STATUS_FAILED;
      errorText = (String)e.getMessage();
    }
    catch( ServiceErrorException e )
    {
      status = JsonResponse.STATUS_FAILED;
      errorText = (String)e.getServiceErrorsCMText().get( 0 );
      logger.error( "Error during conributor upload avatar" );
    }
    catch( MalformedURLException e )
    {

      status = JsonResponse.STATUS_FAILED;
      logger.error( "Error during conributor upload avatar" );
    }
    request.setAttribute( "status", status );
    request.setAttribute( "errorText", errorText );

    return mapping.findForward( "post_comment_ajax_response" );
  }

  public ActionForward newInvite( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    String globalUniqueId = String.valueOf( new Date().getTime() );

    request.setAttribute( "globalUniqueId", globalUniqueId );

    return mapping.findForward( "new_invite_ajax_response" );
  }

  public ActionForward deleteInvite( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    return mapping.findForward( "delete_invite_ajax_response" );
  }

  public ActionForward deleteInviteConfirm( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    String status = JsonResponse.STATUS_UNKNOWN;
    try
    {
      PurlContributionForm purlContributorForm = (PurlContributionForm)form;

      request.setAttribute( "globalUniqueId", purlContributorForm.getGlobalUniqueId() );

      status = JsonResponse.STATUS_SUCCESS;
    }
    catch( Exception e )
    {
      status = JsonResponse.STATUS_FAILED;
      logger.error( "Error during conributor confirm delete invite" );
    }

    request.setAttribute( "status", status );

    return mapping.findForward( "delete_invite_confirm_ajax_response" );
  }

  public ActionForward previewInvite( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    return mapping.findForward( "preview_invite" );
  }

  public ActionForward sendInvite( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    String status = JsonResponse.STATUS_UNKNOWN;
    try
    {
      PurlContributionForm purlContributorForm = (PurlContributionForm)form;
      ContributionState state = getContributorState( request );
      Long purlContributorId = state.getContributorId();

      JSONObject jsonObject = new JSONObject( purlContributorForm.getData() );
      List<PurlContributorInviteValue> contributorInvites = createPurlContributorInviteListFromJson( jsonObject );

      getPurlService().sendContributorInvitationByContributor( contributorInvites, purlContributorId );

      request.setAttribute( "invites", contributorInvites );
      status = JsonResponse.STATUS_SUCCESS;
    }
    catch( JSONException e )
    {
      status = JsonResponse.STATUS_FAILED;
      logger.error( "Error during json comment population" );
    }

    request.setAttribute( "status", status );
    return mapping.findForward( "confirm_invite_ajax_response" );
  }

  /**
   * Called by the main website when uploading something to a purl. Accepts pictures and videos.
   * Yes, it's named processPhoto. For 5.6.3, video upload needs to go in but without the full proper cloud/service solution.
   */
  public ActionForward processPhoto( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    String status = JsonResponse.STATUS_UNKNOWN;
    ContributionState state = getContributorState( request );

    // Setting these values here, they are needed by the failure message - which could happen at any
    // time
    request.setAttribute( "maxImageSize", ImageUtils.getImageSizeLimit() + "MB" );
    request.setAttribute( "maxVideoSize", ImageUtils.getVideoSizeLimit() + "MB" );

    if ( state.isPhotoUploadMaxLimitReached() )
    {
      status = JsonResponse.STATUS_FAILED;
    }
    else
    {
      try
      {
        PurlContributionForm purlContributorForm = (PurlContributionForm)form;

        String orginalfilename = purlContributorForm.getFileAsset().getFileName();
        String extension = getFileExtension( orginalfilename );
        String extensionWithPeriod = "." + extension;
        String filename = orginalfilename.substring( 0, orginalfilename.length() - extensionWithPeriod.length() );
        if ( filename != null )
        {
          filename = ValidatorChecks.removesSpecialCharactersAndSpaces( filename );
          filename = ImageUtils.getValidFileName( filename );
        }
        filename = filename + extensionWithPeriod;
        int filesize = purlContributorForm.getFileAsset().getFileSize();
        byte[] imageInByte = purlContributorForm.getFileAsset().getFileData();

        // validating the file format and size of the file
        boolean isVideo = SupportedEcardVideoTypes.isSupported( getFileExtension( orginalfilename ) );
        boolean isEligibleForUpload = true;
        if ( isVideo )
        {
          if ( !SupportedEcardVideoTypes.isSupported( extension ) || !ImageUtils.isVideoSizeValid( filesize ) )
          {
            isEligibleForUpload = false;
            status = JsonResponse.INVALID_FILE_TYPE_OR_SIZE;
          }
        }
        else
        {
          if ( !ImageUtils.isImageTypeValid( extension ) || !ImageUtils.isImageSizeValid( filesize ) )
          {
            isEligibleForUpload = false;
            status = JsonResponse.INVALID_FILE_TYPE_OR_SIZE;
          }
        }
        // Not getting the parameter through AJAX file upload, so get it from the session
        Long purlContributorId = state.getContributorId();
        String globalUniqueId = String.valueOf( new Date().getTime() );

        // Image or video? Video will just differ slightly in how it is uploaded and the full file
        // will be a video.
        // Video will still have a thumbnail image.
        PurlFileUploadValue data = new PurlFileUploadValue();
        if ( isEligibleForUpload )
        {
          if ( isVideo )
          {
            data.setId( purlContributorId );
            data.setData( imageInByte );
            data.setType( PurlFileUploadValue.TYPE_VIDEO );
            data.setName( filename );
            data.setSize( filesize );

            data = getPurlService().uploadVideoForContributor( data );

            state.addVideo( globalUniqueId, data.getFull(), data.getThumb() );
          }
          else
          {
            data.setId( purlContributorId );
            data.setData( imageInByte );
            data.setType( PurlFileUploadValue.TYPE_PICTURE );
            data.setName( filename );
            data.setSize( filesize );

            data = getPurlService().uploadPhotoForContributor( data );

            state.addPhoto( globalUniqueId, data.getFull(), data.getThumb() );
            // addPhoto will throw an illegal state exception when the photo upload limit is reached
          }
        }

        request.setAttribute( "type", data.getType() );
        request.setAttribute( "extension", extension );
        request.setAttribute( "globalUniqueId", globalUniqueId );
        request.setAttribute( "mediaFullUrl", data.getFull() );
        request.setAttribute( "mediaThumbUrl", data.getThumb() );
        request.setAttribute( "mediaFilename", filename );

        String finalImagePrefixUrl;
        String stagerImagePrefixUrl;
        if ( isVideo )
        {
          finalImagePrefixUrl = "";
          stagerImagePrefixUrl = "";
        }
        else
        {
          finalImagePrefixUrl = getFinalImagePrefixUrl();
          stagerImagePrefixUrl = getStageImagePrefixUrl();
        }
        request.setAttribute( "finalPrefixURL", finalImagePrefixUrl );
        request.setAttribute( "stagerPrefixURL", stagerImagePrefixUrl );

        if ( !JsonResponse.INVALID_FILE_TYPE_OR_SIZE.equals( status ) )
        {
          status = JsonResponse.STATUS_SUCCESS;
        }
      }
      catch( Throwable e )
      {
        status = JsonResponse.STATUS_FAILED;
        logger.error( "Error during conributor process photo", e );
      }
    }

    request.setAttribute( "status", status );
    return mapping.findForward( "upload_photo" );
  }

  public ActionForward processPhotoRedirect( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    // Setting these values here, they are needed by the failure message - make sure at least they
    // are set
    request.setAttribute( "maxImageSize", ImageUtils.getImageSizeLimit() + "MB" );
    request.setAttribute( "maxVideoSize", ImageUtils.getVideoSizeLimit() + "MB" );

    String clientState = RequestUtils.getRequiredParamString( request, "clientState" );
    String cryptoPass = RequestUtils.getOptionalParamString( request, "cryptoPass" );
    String password = ClientStatePasswordManager.getPassword();

    if ( cryptoPass != null && cryptoPass.equals( "1" ) )
    {
      password = ClientStatePasswordManager.getGlobalPassword();
    }
    // Deserialize the client state.
    Map clientStateMap = ClientStateSerializer.deserialize( clientState, password );

    String status = (String)clientStateMap.get( "status" );
    String globalUniqueId = (String)clientStateMap.get( "globalUniqueId" );
    String mediaFullUrl = (String)clientStateMap.get( "mediaFullUrl" );
    String mediaThumbUrl = (String)clientStateMap.get( "mediaThumbUrl" );
    String mediaFilename = (String)clientStateMap.get( "mediaFilename" );

    request.setAttribute( "status", status );
    request.setAttribute( "globalUniqueId", globalUniqueId );
    request.setAttribute( "mediaFullUrl", mediaFullUrl );
    request.setAttribute( "mediaThumbUrl", mediaThumbUrl );
    request.setAttribute( "mediaFilename", mediaFilename );

    return mapping.findForward( "process_photo_ajax_response" );
  }

  public ActionForward newPhoto( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    return mapping.findForward( "new_photo_ajax_response" );
  }

  public ActionForward deletePhoto( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    return mapping.findForward( "delete_photo_ajax_response" );
  }

  public ActionForward deletePhotoConfirm( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    String status = JsonResponse.STATUS_UNKNOWN;
    try
    {
      PurlContributionForm purlContributorForm = (PurlContributionForm)form;

      String globalUniqueId = purlContributorForm.getGlobalUniqueId();

      ContributionState state = getContributorState( request );
      getPurlService().deleteMediaFromAppDataDir( state.getDetailPhotoUrl( globalUniqueId ) );
      getPurlService().deleteMediaFromAppDataDir( state.getThumbPhotoUrl( globalUniqueId ) );
      state.removePhoto( globalUniqueId );

      request.setAttribute( "globalUniqueId", globalUniqueId );

      status = JsonResponse.STATUS_SUCCESS;
    }
    catch( Exception e )
    {
      status = JsonResponse.STATUS_FAILED;
      logger.error( "Error during conributor confirm photo delete" );
    }

    request.setAttribute( "status", status );

    return mapping.findForward( "delete_photo_confirm_ajax_response" );
  }

  public ActionForward previewPhoto( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    return mapping.findForward( "preview_photo" );
  }

  public ActionForward postPhoto( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    PurlContributionForm purlContributorForm = (PurlContributionForm)form;

    List<PurlMediaUploadValue> mediaUploads = purlContributorForm.getMediaUploads();
    Iterator<PurlMediaUploadValue> iMedia = mediaUploads.iterator();
    while ( iMedia.hasNext() )
    {
      PurlMediaUploadValue media = iMedia.next();
      if ( StringUtils.isEmpty( media.getId() ) )
      {
        iMedia.remove();
      }
    }

    getPurlService().postMediaForContributor( purlContributorForm.getPurlContributor().getId(), mediaUploads );

    request.setAttribute( "mediaList", mediaUploads );
    request.setAttribute( "mediaListSize", mediaUploads.size() );

    return mapping.findForward( "post_photo_ajax_response" );
  }

  public ActionForward supportedVideo( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    return mapping.findForward( "supported_video" );
  }

  public ActionForward processVideo( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    String status = JsonResponse.STATUS_UNKNOWN;
    try
    {
      PurlContributionForm purlContributorForm = (PurlContributionForm)form;

      String globalUniqueId = String.valueOf( new Date().getTime() );

      request.setAttribute( "globalUniqueId", globalUniqueId );
      request.setAttribute( "mediaUrl", purlContributorForm.getMediaUrl() );

      status = JsonResponse.STATUS_SUCCESS;
    }
    catch( Exception e )
    {
      status = JsonResponse.STATUS_FAILED;
      logger.error( "Error during conributor process video" );
    }

    request.setAttribute( "status", status );

    return mapping.findForward( "process_video_ajax_response" );
  }

  public ActionForward newVideo( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    return mapping.findForward( "new_video_ajax_response" );
  }

  public ActionForward deleteVideo( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    return mapping.findForward( "delete_video_ajax_response" );
  }

  public ActionForward deleteVideoConfirm( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    String status = JsonResponse.STATUS_UNKNOWN;
    try
    {
      PurlContributionForm purlContributorForm = (PurlContributionForm)form;

      request.setAttribute( "globalUniqueId", purlContributorForm.getGlobalUniqueId() );

      status = JsonResponse.STATUS_SUCCESS;
    }
    catch( Exception e )
    {
      status = JsonResponse.STATUS_FAILED;
      logger.error( "Error during conributor confirm video delete" );
    }

    request.setAttribute( "status", status );

    return mapping.findForward( "delete_video_confirm_ajax_response" );
  }

  public ActionForward previewVideo( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    return mapping.findForward( "preview_video" );
  }

  public ActionForward postVideo( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    PurlContributionForm purlContributorForm = (PurlContributionForm)form;

    List<PurlMediaUploadValue> mediaUploads = purlContributorForm.getMediaUploads();
    Iterator<PurlMediaUploadValue> iMedia = mediaUploads.iterator();
    while ( iMedia.hasNext() )
    {
      PurlMediaUploadValue media = iMedia.next();
      if ( StringUtils.isEmpty( media.getId() ) )
      {
        iMedia.remove();
      }
    }

    getPurlService().postMediaForContributor( purlContributorForm.getPurlContributor().getId(), mediaUploads );

    request.setAttribute( "mediaList", mediaUploads );
    request.setAttribute( "mediaListSize", mediaUploads.size() );

    return mapping.findForward( "post_video_ajax_response" );
  }

  public ActionForward uploadMediaToWebdav( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    String mediaFilePath = request.getParameter( "mediaFilePath" );

    if ( logger.isDebugEnabled() )
    {
      logger.debug( "mediaFilePath : " + mediaFilePath );
    }

    boolean success = getPurlService().moveFileToWebdav( mediaFilePath );
    if ( logger.isDebugEnabled() )
    {
      logger.debug( "uploadMediaToWebdav status : " + success );
    }

    // This is invoked by process, no view to return
    return null;
  }

  private Long getPurlContributorId( HttpServletRequest request ) throws InvalidClientStateException
  {
    Long purlContributorId = null;

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
    }
    catch( ClassCastException cce )
    {
      purlContributorId = new Long( (String)clientStateMap.get( "purlContributorId" ) );
    }
    return purlContributorId;
  }

  private Boolean getVerifyCaptchaStatus( HttpServletRequest request ) throws InvalidClientStateException
  {
    Boolean verifyCaptcha = false;

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
      verifyCaptcha = (Boolean)clientStateMap.get( "verifyCaptcha" );
    }
    catch( ClassCastException cce )
    {
      verifyCaptcha = new Boolean( (String)clientStateMap.get( "verifyCaptcha" ) );
    }
    return verifyCaptcha;
  }

  private PurlContributor createPurlContributorSelf( PurlContributorInviteValue value, HttpServletRequest request )
  {
    Long purlRecipientId = value.getPurlRecipientId();

    // if more than one contributor has same email address then create a new contributor otherwise
    // update the single contributor
    // Check if Contributor already invited
    PurlContributor purlContributor = getPurlService().getPurlContributorByEmailAddr( value.getEmailAddr(), purlRecipientId );
    if ( null != purlContributor )
    {
      // Update existing contributor First Name and Last Name
      purlContributor.setFirstName( value.getFirstName() );
      purlContributor.setLastName( value.getLastName() );
      // Mark status as contribution since we are giving direct access to contribution
      purlContributor.setState( PurlContributorState.lookup( PurlContributorState.CONTRIBUTION ) );

      // If contributor is marked as Send Later, change status to invited, since we are giving him
      // access to contribute
      if ( purlContributor.isSendLater() )
      {
        purlContributor.setSendLater( false );
      }
    }
    else
    {
      purlContributor = new PurlContributor();
      purlContributor.setFirstName( value.getFirstName() );
      purlContributor.setLastName( value.getLastName() );
      purlContributor.setEmailAddr( value.getEmailAddr() );

      // Mark status as contribution since we are giving direct access to contribution
      purlContributor.setState( PurlContributorState.lookup( PurlContributorState.CONTRIBUTION ) );
      {
        PurlRecipient purlRecipient = getPurlService().getPurlRecipientById( value.getPurlRecipientId() );
        purlContributor.setPurlRecipient( purlRecipient );
      }
    }

    if ( UserManager.isUserLoggedIn() )
    {
      Participant participant = getParticipantService().getParticipantById( UserManager.getUserId() );
      if ( purlContributor.getAvatarUrl() == null )
      {
        if ( participant.getAvatarSmall() != null )
        {
          purlContributor.setAvatarUrl( participant.getAvatarSmall() );
        }
      }
      else
      {
        purlContributor.setAvatarUrl( purlContributor.getAvatarUrl() );
      }
    }
    else
    {
      if ( purlContributor.getAvatarUrl() != null )
      {
        purlContributor.setAvatarUrl( purlContributor.getAvatarUrl() );
      }
    }

    return getPurlService().savePurlContributor( purlContributor );
  }

  private PurlContributorComment createPurlContributorCommentFromJson( JSONObject commentJson ) throws JSONException
  {
    JSONObject userInfoJson = commentJson.getJSONArray( "userInfo" ).getJSONObject( 0 );
    PurlContributor purlContributor = new PurlContributor();
    purlContributor.setId( userInfoJson.getLong( "contributorID" ) );

    if ( UserManager.isUserLoggedIn() )
    {
      Participant participant = getParticipantService().getParticipantById( UserManager.getUserId() );
      if ( purlContributor.getAvatarUrl() == null )
      {
        if ( participant.getAvatarSmall() != null )
        {
          purlContributor.setAvatarUrl( participant.getAvatarSmall() );
        }
      }
      else
      {
        purlContributor.setAvatarUrl( purlContributor.getAvatarUrl() );
      }
    }
    else
    {
      if ( purlContributor.getAvatarUrl() != null )
      {
        purlContributor.setAvatarUrl( purlContributor.getAvatarUrl() );
      }
    }

    PurlContributorComment purlComment = new PurlContributorComment();
    purlComment.setPurlContributor( purlContributor );
    purlComment.setComments( commentJson.getString( "commentText" ) );

    String videoWebLink = commentJson.getString( "videoWebLink" );
    Object mediaObject = commentJson.get( "media" );

    if ( StringUtils.isNotBlank( videoWebLink ) )
    {
      purlComment.setVideoUrl( videoWebLink );
      purlComment.setVideoType( PurlContributorVideoType.lookup( PurlContributorVideoType.WEB ) );
    }
    else if ( mediaObject instanceof JSONArray )
    {
      JSONObject mediaJson = ( (JSONArray)mediaObject ).getJSONObject( 0 );
      String videojson = mediaJson.getString( "video" );
      String photojson = mediaJson.getString( "photo" );
      Object videoObject = null;
      Object photoObject = null;
      if ( videojson != null && !videojson.contains( "null" ) )
      {
        videoObject = mediaJson.get( "video" );
      }
      if ( photojson != null && !photojson.contains( "null" ) )
      {
        photoObject = mediaJson.get( "photo" );
      }
      if ( videoObject != null && videoObject instanceof JSONArray && ( (JSONArray)videoObject ).length() > 0 )
      {
        JSONObject videoJson = ( (JSONArray)videoObject ).getJSONObject( 0 );
        purlComment.setVideoUrl( videoJson.getString( "src" ) );
        purlComment.setVideoType( PurlContributorVideoType.lookup( PurlContributorVideoType.DIRECT ) );
      }
      if ( photoObject != null && photoObject instanceof JSONArray && ( (JSONArray)photoObject ).length() > 0 )
      {
        JSONObject photoJson = ( (JSONArray)photoObject ).getJSONObject( 0 );
        purlComment.setImageUrl( photoJson.getString( "src" ) );
        purlComment.setImageUrlThumb( photoJson.getString( "thumbSrc" ) );
      }
    }

    return purlComment;
  }

  private PurlContributorInviteValue createPurlContributorInviteValueFromJson( JSONObject jsonObject ) throws JSONException
  {
    PurlContributorInviteValue newContributor = new PurlContributorInviteValue();

    JSONObject contributorJson = jsonObject.getJSONArray( "messages" ).getJSONObject( 0 );

    String firstName = contributorJson.getString( "firstName" );
    String lastName = contributorJson.getString( "lastName" );
    newContributor.setFirstName( !StringUtils.isEmpty( firstName ) ? firstName : "" );
    newContributor.setLastName( !StringUtils.isEmpty( lastName ) ? lastName : "" );
    newContributor.setEmailAddr( contributorJson.getString( "emailAddress" ) );
    String avatarUrl = contributorJson.optString( "picURL" );
    if ( avatarUrl != null && !avatarUrl.contains( "avatar-blank-72.png" ) )
    {
      newContributor.setAvatarUrl( avatarUrl );
    }
    newContributor.setPurlRecipientId( Long.parseLong( contributorJson.getString( "recipientId" ) ) );

    return newContributor;
  }

  private List<PurlContributorInviteValue> createPurlContributorInviteListFromJson( JSONObject jsonObject ) throws JSONException
  {
    List<PurlContributorInviteValue> contributorInvites = new ArrayList<PurlContributorInviteValue>();
    JSONArray invitesJsonArray = jsonObject.getJSONArray( "invites" );

    for ( int i = 0; i < invitesJsonArray.length(); i++ )
    {
      JSONObject inviteJsonObject = invitesJsonArray.getJSONObject( i );

      PurlContributorInviteValue newContributor = new PurlContributorInviteValue();
      String firstName = inviteJsonObject.getString( "firstName" );
      String lastName = inviteJsonObject.getString( "lastName" );
      newContributor.setFirstName( StringUtils.isNotEmpty( firstName ) ? firstName : "" );
      newContributor.setLastName( StringUtils.isNotEmpty( lastName ) ? lastName : "" );
      newContributor.setEmailAddr( inviteJsonObject.getString( "emailAddress" ) );

      contributorInvites.add( newContributor );
    }

    return contributorInvites;
  }

  private List<PurlContributorBean> getExistingPurlInvitees( PurlContributor contributor )
  {
    AssociationRequestCollection arc = new AssociationRequestCollection();
    arc.add( new PurlRecipientAssociationRequest( PurlRecipientAssociationRequest.PURL_CONTRIBUTOR ) );
    PurlRecipient purlRecipient = getPurlService().getPurlRecipientById( contributor.getPurlRecipient().getId(), arc );
    List<PurlContributorBean> purlContributorBeans = new ArrayList<PurlContributorBean>();
    for ( PurlContributor existingContributor : purlRecipient.getContributors() )
    {
      if ( existingContributor.getEmailAddr() != null && !existingContributor.getEmailAddr().isEmpty() )
      {
        PurlContributorBean contributorBean = new PurlContributorBean();
        contributorBean.setFirstName( StringUtil.escapeXml( existingContributor.getFirstName() ) );
        contributorBean.setLastName( StringUtil.escapeXml( existingContributor.getLastName() ) );
        contributorBean.setEmail( existingContributor.getEmailAddr() );
        contributorBean.setId( existingContributor.getEmailAddr() );
        purlContributorBeans.add( contributorBean );
      }
    }
    return purlContributorBeans;
  }
/*Customization for WIP 32479 starts here*/
  
  public ActionForward unsubscribeDisplay( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    return mapping.findForward( ActionConstants.DISPLAY_FORWARD );
  }
  
  public ActionForward unsubscribe( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    PurlContributionForm purlContributionForm = (PurlContributionForm) form;
    ActionMessages errors = new ActionMessages();
    verifyCaptcha( request, errors );
    
    if( getUserService().isUserEmailAddressExists( purlContributionForm.getEmailAddress() ) )
    {
      errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "purl.contributor.USER_EXISTS" ) ); 
      saveErrors( request, errors );
    }
    else
    {
      getPurlService().unsubscribeExternalUser( purlContributionForm.getEmailAddress() ); 
    }
    
    if ( !errors.isEmpty() )
    {
      saveErrors( request, errors );
      return mapping.findForward( ActionConstants.FAIL_FORWARD ); 
    }
    else
    {
      return mapping.findForward( ActionConstants.SUCCESS_FORWARD );
    }
  }
  
  private void verifyCaptcha( HttpServletRequest request, ActionMessages errors )
  {
    boolean isCaptchaOk = false;
    String response = request.getParameter( "captchaResponse" );
    Captcha captcha = Captcha.load(request, "loginFormCaptcha");
    try
    {
      isCaptchaOk = captcha.validate( request, response);
    }
    catch( Exception e )
    {
      // Unable to verify
    }

    if ( logger.isDebugEnabled() )
    {
      logger.debug( "Verify Captcha " + ( isCaptchaOk ? "SUCCESS" : "FAILURE" ) );
    }

    if ( !isCaptchaOk )
    {
      errors.add( "captchaResponse", new ActionMessage( "purl.tnc.error.params.CAPTCHA_FAILED" ) );
    }
  }
  
  /*Customization for WIP 32479 ends here*/
  private String getFinalImagePrefixUrl()
  {
    String siteUrlPrefix = getSystemVariableService().getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();
    if ( !Environment.isCtech() )
    {
      siteUrlPrefix = getSystemVariableService().getPropertyByName( SystemVariableService.SITE_URL_PREFIX + "." + Environment.ENV_QA ).getStringVal();
    }
    return siteUrlPrefix + "-cm/cm3dam" + '/';
  }

  private String getStageImagePrefixUrl()
  {
    return "purlTempImage.do?imageUrl=";

  }

  private UserService getUserService()
  {
    return (UserService)getService( UserService.BEAN_NAME );
  }

  private ParticipantService getParticipantService()
  {
    return (ParticipantService)getService( ParticipantService.BEAN_NAME );
  }

  private String getFileExtension( String filename )
  {
    return FilenameUtils.getExtension( filename );
  }

  public MTCService getMTCService()
  {
    return (MTCService)getService( MTCService.BEAN_NAME );
  }

}
