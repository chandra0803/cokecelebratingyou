
package com.biperf.core.ui.client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.biperf.core.domain.WebErrorMessage;
import com.biperf.core.domain.client.CareerMomentsModuleSet;
import com.biperf.core.domain.client.CareerMomentsModuleSets;
import com.biperf.core.domain.client.CareerMomentsView;
import com.biperf.core.domain.client.ClientProfileComment;
import com.biperf.core.domain.client.ClientProfileLike;
import com.biperf.core.domain.enums.CokeCareerMomentsType;
import com.biperf.core.domain.enums.PurlCelebrationTabType;
import com.biperf.core.domain.purl.PurlCelebrationSet;
import com.biperf.core.domain.purl.PurlCelebrationTableColumnsView;
import com.biperf.core.service.client.CokeCareerMomentsService;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.participant.UserCharacteristicService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.recognition.SupportedEcardVideoTypes;
import com.biperf.core.ui.utils.JsonResponse;
import com.biperf.core.ui.utils.ValidatorChecks;
import com.biperf.core.utils.ClientStateUtils;
import com.biperf.core.utils.Environment;
import com.biperf.core.utils.ImageUtils;
import com.biperf.core.utils.InvalidClientStateException;
import com.biperf.core.utils.NewServiceAnniversaryUtil;
import com.biperf.core.utils.PageConstants;
import com.biperf.core.utils.StringUtil;
import com.biperf.core.utils.UserManager;
import com.biperf.core.utils.WebResponseConstants;
import com.biperf.core.value.PurlFileUploadValue;
import com.biperf.core.value.client.ClientCareerMomentsDropdownValueBean;
import com.biperf.core.value.client.ClientCommentsValueBean;
import com.biperf.core.value.client.ClientSubCommentValueBean;
import com.biperf.core.value.client.CokeLikeUnlikeValueBean;
import com.biperf.core.value.client.Comment;
import com.biperf.core.value.client.Commenter;
import com.biperf.core.value.client.TableColumn;
import com.objectpartners.cms.util.CmsResourceBundle;

public class CokeCareerMomentsAction extends BaseDispatchAction
{
  private static final Log log = LogFactory.getLog( CokeCareerMomentsAction.class );
  public static final String UPCOMING = CokeCareerMomentsType.UPCOMING.getCode();

  public ActionForward unspecified( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    return mapping.findForward( "success" );
  }

  public ActionForward fetchCareerMomentsData( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws IOException,
      ServletException,
      InvalidClientStateException
  {
    CareerMomentsModuleSets view = new CareerMomentsModuleSets();
    List<CareerMomentsModuleSet> careerMomentsSets = new ArrayList<CareerMomentsModuleSet>();

    try
    {
      CareerMomentsModuleSet careerMomentsSet = getCokeCareerMomentsService().getCareerMomentsModuleData();
      careerMomentsSets.add( careerMomentsSet );
      view.setCareerMomentsSets( careerMomentsSets );
    }
    catch( Exception e )
    {
      e.printStackTrace();
      WebErrorMessage message = new WebErrorMessage();
      message.setSuccess( false );
      message.setType( WebResponseConstants.RESPONSE_TYPE_SERVER_ERROR );
      message.setText( e.getMessage() );
      view.getMessages().add( message );
    }
    super.writeAsJsonToResponse( view, response );
    return null;
  }

  public ActionForward fetchDataForDetail( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws IOException,
      ServletException,
      InvalidClientStateException
  {
    CokeCareerMomentsForm cokeCareerMomentsForm = (CokeCareerMomentsForm)form;
    String currentPage = cokeCareerMomentsForm.getCmPastPresentSelect();
    String username = cokeCareerMomentsForm.getName();
    String siteUrl = getSystemVariableService().getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();
    
    if ( !StringUtils.isEmpty( currentPage )&& "past".equals( currentPage ))
    {
      currentPage = "0";
    }
    else
    {
      currentPage = "1";
    }
    
    if(PurlCelebrationTabType.lookup( PurlCelebrationTabType.DIVISION_TAB ).getCode().equals( cokeCareerMomentsForm.getCmId()) )
    {
      cokeCareerMomentsForm.setListValue( getUserCharacteristicService().getCharacteristicValueByDivisionKeyAndUserId(UserManager.getUserId() ) );
    }


    List<CareerMomentsView> dataView = getCokeCareerMomentsService().getCareerMomentsDataForDetail( cokeCareerMomentsForm
        .getCmId(), Integer.valueOf( currentPage ), cokeCareerMomentsForm.getCmType(), cokeCareerMomentsForm.getListValue(), UserManager.getUserLocale(), username );
    
    if(Objects.nonNull( dataView )&& dataView.size()>0)
    {
      for(CareerMomentsView view: dataView)
      {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put( "paxId", Long.valueOf(view.getId()) );
        paramMap.put( "isFullPage", "true" );
        view.setContributeUrl( ClientStateUtils.generateEncodedLink( siteUrl, PageConstants.PUBLIC_PROFILE_VIEW, paramMap ) );
      }
    }
    cokeCareerMomentsForm.setDataView( dataView );
    request.setAttribute( "dataView", dataView );
    request.setAttribute( "totalRecords", dataView.size() );
    if(cokeCareerMomentsForm.getCmType()!=null && cokeCareerMomentsForm.getCmType().toLowerCase().equals( "newhire" ))
    {
      request.setAttribute( "titleName", "newhire" );
    }
    return mapping.findForward( ActionConstants.SUCCESS_DETAILS );
  }
  

  public ActionForward likeAboutMe( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws IOException, ServletException, InvalidClientStateException
  {
    String likeId = request.getParameter( "iamId" );
    Long aboutMeId = Long.valueOf( likeId );
    Long profileUserId = Long.valueOf( request.getParameter( "profileUserId" ) );
    CokeLikeUnlikeValueBean view = new CokeLikeUnlikeValueBean();

    ClientProfileLike profileLike = new ClientProfileLike();
    profileLike.setPaxAboutMeId( aboutMeId );
    profileLike.setLikedUser( getParticipantService().getParticipantById( UserManager.getUserId() ) );
    profileLike.setProfileUserId( profileUserId );

    try
    {
      getCokeCareerMomentsService().saveLike( profileLike );
      view.setNumLikes( Integer.toUnsignedLong( getCokeCareerMomentsService().getLikesCountByAboutMeId( profileLike.getPaxAboutMeId() ) ) );
    }
    catch( Exception e )
    {
      e.printStackTrace();
      WebErrorMessage message = new WebErrorMessage();
      message.setSuccess( false );
      message.setType( WebResponseConstants.RESPONSE_TYPE_SERVER_ERROR );
      message.setText( e.getMessage() );
      view.getMessages().add( message );
    }
    super.writeAsJsonToResponse( view, response );
    return null;
  }

  public ActionForward unlikeAboutMe( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws IOException,
      ServletException,
      InvalidClientStateException
  {
    String likeId = request.getParameter( "iamId" );
    Long aboutMeId = Long.valueOf( likeId );
    Long profileUserId = Long.valueOf( request.getParameter( "profileUserId" ) );
    CokeLikeUnlikeValueBean view = new CokeLikeUnlikeValueBean();

    try
    {
      getCokeCareerMomentsService().removeAboutMeLike( UserManager.getUserId(), profileUserId, aboutMeId );;
      view.setNumLikes( Integer.toUnsignedLong( getCokeCareerMomentsService().getLikesCountByAboutMeId( aboutMeId ) ) );
    }
    catch( Exception e )
    {
      e.printStackTrace();
      WebErrorMessage message = new WebErrorMessage();
      message.setSuccess( false );
      message.setType( WebResponseConstants.RESPONSE_TYPE_SERVER_ERROR );
      message.setText( e.getMessage() );
      view.getMessages().add( message );
    }
    super.writeAsJsonToResponse( view, response );
    return null;
  }

  public ActionForward likeComment( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws IOException, ServletException, InvalidClientStateException
  {
    Long commentId = Long.valueOf( request.getParameter( "commentId" ) );
    Long profileUserId = Long.valueOf( request.getParameter( "profileUserId" ) );
    CokeLikeUnlikeValueBean view = new CokeLikeUnlikeValueBean();

    ClientProfileLike profileLike = new ClientProfileLike();
    profileLike.setProfileCommentId( commentId );
    profileLike.setLikedUser( getParticipantService().getParticipantById( UserManager.getUserId() ) );
    profileLike.setProfileUserId( profileUserId );

    try
    {
      getCokeCareerMomentsService().saveLike( profileLike );
      view.setNumLikes( Integer.toUnsignedLong( getCokeCareerMomentsService().getLikesCountByCommentId( commentId ) ) );
    }
    catch( Exception e )
    {
      e.printStackTrace();
      WebErrorMessage message = new WebErrorMessage();
      message.setSuccess( false );
      message.setType( WebResponseConstants.RESPONSE_TYPE_SERVER_ERROR );
      message.setText( e.getMessage() );
      view.getMessages().add( message );
    }
    super.writeAsJsonToResponse( view, response );
    return null;
  }

  public ActionForward unlikeComment( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws IOException,
      ServletException,
      InvalidClientStateException
  {
    Long commentId = Long.valueOf( request.getParameter( "commentId" ) );
    Long profileUserId = Long.valueOf( request.getParameter( "profileUserId" ) );
    CokeLikeUnlikeValueBean view = new CokeLikeUnlikeValueBean();

    try
    {
      getCokeCareerMomentsService().removeCommentLike( UserManager.getUserId(), profileUserId, commentId );;
      view.setNumLikes( Integer.toUnsignedLong( getCokeCareerMomentsService().getLikesCountByCommentId( commentId ) ) );
    }
    catch( Exception e )
    {
      e.printStackTrace();
      WebErrorMessage message = new WebErrorMessage();
      message.setSuccess( false );
      message.setType( WebResponseConstants.RESPONSE_TYPE_SERVER_ERROR );
      message.setText( e.getMessage() );
      view.getMessages().add( message );
    }
    super.writeAsJsonToResponse( view, response );
    return null;
  }

  public ActionForward fetchComments( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws IOException,
      ServletException,
      InvalidClientStateException,
      JSONException
  {
    ClientCommentsValueBean view = new ClientCommentsValueBean();
    String jsonString = request.getParameter( "data" );
    JSONObject jsonObject = new JSONObject( jsonString );
    Long profileUserId = 0L;
    
    try
    {
      profileUserId = Long.valueOf( jsonObject.getString( "recipientId" ) );
    }
    catch( NumberFormatException e1 )
    {
      // TODO Auto-generated catch block
      e1.printStackTrace();
    }
    catch( JSONException e1 )
    {
      profileUserId = UserManager.getUserId();
    }

    try
    {
      view = getCokeCareerMomentsService().getProfileCommentsByUserId( profileUserId );
    }
    catch( Exception e )
    {
      e.printStackTrace();
      WebErrorMessage message = new WebErrorMessage();
      message.setSuccess( false );
      message.setType( WebResponseConstants.RESPONSE_TYPE_SERVER_ERROR );
      message.setText( e.getMessage() );
      view.getMessages().add( message );
    }
    super.writeAsJsonToResponse( view, response );
    return null;
  }

  public ActionForward saveComments( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws IOException,
      ServletException,
      InvalidClientStateException,
      JSONException
  {
    String[] jsonString = request.getParameterValues( "data" );
    JSONArray jsonArray = new JSONArray( jsonString[0] );
    JSONObject commentJson = jsonArray.getJSONObject( 0 );
    Long profileUserId = Long.valueOf( commentJson.getString( "profileUserId" ) );
    String status = "";
    String errorText = "";

    ClientProfileComment profileComment = new ClientProfileComment();
    profileComment.setCommenterUserId( UserManager.getUserId() );
    profileComment.setCommentLanguageId( UserManager.getLocale().getDisplayLanguage() );
    profileComment.setComments( commentJson.getString( "commentText" ) );
    profileComment.setProfileUserId( profileUserId );
    profileComment.setSequenceNum( 1 );

    String videoWebLink = commentJson.getString( "videoWebLink" );
    Object mediaObject = commentJson.get( "media" );

    if ( StringUtils.isNotBlank( videoWebLink ) )
    {
      profileComment.setVideoUrl( videoWebLink );
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
        profileComment.setVideoUrl( videoJson.getString( "src" ) );
      }
      if ( photoObject != null && photoObject instanceof JSONObject )
      {
        String src = ( (JSONObject)photoObject ).getString( "src" );
        String thumbSrc = ( (JSONObject)photoObject ).getString( "thumbSrc" );
        profileComment.setImageUrl( src.substring( src.indexOf( "=" ) + 1 ) );
        profileComment.setImageUrlThumb( thumbSrc.substring( thumbSrc.indexOf( "=" ) + 1 ) );
      }
    }

    JSONObject resultJson = new JSONObject();
    JSONArray array = new JSONArray();
    JSONObject item = new JSONObject();

    boolean isVideo = !StringUtil.isEmpty( profileComment.getImageUrl() ) && SupportedEcardVideoTypes.isSupported( FilenameUtils.getExtension( profileComment.getImageUrl() ) );

    try
    {
      getCokeCareerMomentsService().saveComment( profileComment );

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
        request.setAttribute( "imageServiceUrlThumb", profileComment.getImageUrlThumb() );
        request.setAttribute( "imageServiceUrlSrc", profileComment.getImageUrl() );
      }
      request.setAttribute( "finalPrefixURL", finalImagePrefixUrl );
      request.setAttribute( "stagerPrefixURL", stagerImagePrefixUrl );

      request.setAttribute( "purlComment", profileComment );
      status = JsonResponse.STATUS_SUCCESS;
      item.put( "isSuccess", true );
    }
    catch( Exception e )
    {
      item.put( "isSuccess", false );
      status = JsonResponse.STATUS_FAILED;
      errorText = e.getMessage();
      e.printStackTrace();
    }

    request.setAttribute( "status", status );
    request.setAttribute( "errorText", errorText );

    item.put( "text", profileComment.getComments() );
    array.put( item );
    resultJson.put( "messages", array );
    request.setAttribute( "status", status );
    request.setAttribute( "errorText", errorText );
    return mapping.findForward( "post_comment_ajax_response" );
  }

  public ActionForward saveSubComment( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws IOException,
      ServletException,
      InvalidClientStateException,
      JSONException
  {

    Long mainCommentId = Long.valueOf( request.getParameter( "commentId" ) );
    String commentString = request.getParameter( "comment" );
    Long profileUserId = Long.valueOf( request.getParameter( "profileUserId" ) );

    ClientProfileComment profileComment = new ClientProfileComment();
    profileComment.setCommenterUserId( UserManager.getUserId() );
    profileComment.setCommentLanguageId( UserManager.getLocale().getDisplayLanguage() );
    profileComment.setComments( commentString );
    profileComment.setProfileUserId( profileUserId );
    profileComment.setSequenceNum( 1 );
    profileComment.setProfileSubCommentId( mainCommentId );

    ClientSubCommentValueBean view = new ClientSubCommentValueBean();
    Comment comment = new Comment();
    comment.setComment( commentString );
    comment.setCommentId( mainCommentId );
    comment.setIsLiked( false );
    comment.setIsMine( true );
    comment.setNumLikers( 0L );

    Commenter commenter = new Commenter();
    commenter.setId( UserManager.getUserId() );
    commenter.setFirstName( UserManager.getUser().getFirstName() );
    commenter.setLastName( UserManager.getUser().getLastName() );
    commenter.setAvatarUrl( "" );

    try
    {
      profileComment = getCokeCareerMomentsService().saveComment( profileComment );
      comment.setLevelOneId( profileComment.getId() );
      comment.setCommenter( commenter );
      view.setComment( comment );
    }
    catch( Exception e )
    {
      e.printStackTrace();
      WebErrorMessage message = new WebErrorMessage();
      message.setSuccess( false );
      message.setType( WebResponseConstants.RESPONSE_TYPE_SERVER_ERROR );
      message.setText( e.getMessage() );
      view.getMessages().add( message );
    }

    super.writeAsJsonToResponse( view, response );
    return null;
  }

  public ActionForward processPhoto( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    String status = JsonResponse.STATUS_UNKNOWN;

    // Setting these values here, they are needed by the failure message - which could happen at any
    // time
    request.setAttribute( "maxImageSize", ImageUtils.getImageSizeLimit() + "MB" );
    request.setAttribute( "maxVideoSize", ImageUtils.getVideoSizeLimit() + "MB" );

    try
    {
      CokeCareerMomentsForm cokeCareerMomentsForm = (CokeCareerMomentsForm)form;

      String orginalfilename = cokeCareerMomentsForm.getFileAsset().getFileName();
      String extension = getFileExtension( orginalfilename );
      String extensionWithPeriod = "." + extension;
      String filename = orginalfilename.substring( 0, orginalfilename.length() - extensionWithPeriod.length() );
      if ( filename != null )
      {
        filename = ValidatorChecks.removesSpecialCharactersAndSpaces( filename );
        filename = ImageUtils.getValidFileName( filename );
      }
      filename = filename + extensionWithPeriod;
      int filesize = cokeCareerMomentsForm.getFileAsset().getFileSize();
      byte[] imageInByte = cokeCareerMomentsForm.getFileAsset().getFileData();

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
      Long commenterUserId = UserManager.getUserId();
      String globalUniqueId = String.valueOf( new Date().getTime() );

      // Image or video? Video will just differ slightly in how it is uploaded and the full file
      // will be a video.
      // Video will still have a thumbnail image.
      PurlFileUploadValue data = new PurlFileUploadValue();
      if ( isEligibleForUpload )
      {
        if ( isVideo )
        {
          data.setId( commenterUserId );
          data.setData( imageInByte );
          data.setType( PurlFileUploadValue.TYPE_VIDEO );
          data.setName( filename );
          data.setSize( filesize );

          data = getCokeCareerMomentsService().uploadVideoForContributor( data );

        }
        else
        {
          data.setId( commenterUserId );
          data.setData( imageInByte );
          data.setType( PurlFileUploadValue.TYPE_PICTURE );
          data.setName( filename );
          data.setSize( filesize );

          data = getCokeCareerMomentsService().uploadPhotoForContributor( data );

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
      log.error( "Error during conributor process photo", e );
    }

    request.setAttribute( "status", status );
    return mapping.findForward( "upload_photo" );
  }

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
    return "profileTempImage.do?imageUrl=";

  }

  private String getFileExtension( String filename )
  {
    return FilenameUtils.getExtension( filename );
  }

  private CokeCareerMomentsService getCokeCareerMomentsService()
  {
    return (CokeCareerMomentsService)getService( CokeCareerMomentsService.BEAN_NAME );
  }

  private SystemVariableService getSystemVariableService()
  {
    return (SystemVariableService)getService( SystemVariableService.BEAN_NAME );
  }

  private ParticipantService getParticipantService()
  {
    return (ParticipantService)getService( ParticipantService.BEAN_NAME );
  }
  
  private UserCharacteristicService getUserCharacteristicService()
  {
    return (UserCharacteristicService)getService( UserCharacteristicService.BEAN_NAME );
  }
  

  /**
   *
   * @param request
   * @return pageNumber
   */
  private int getPageNumber( HttpServletRequest request )
  {
    String pageNumber = request.getParameter( "currentPage" );
    if ( !StringUtil.isEmpty( pageNumber ) )
    {
      return Integer.parseInt( pageNumber );
    }
    return 1;
  }

}
