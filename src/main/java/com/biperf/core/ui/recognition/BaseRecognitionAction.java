
package com.biperf.core.ui.recognition;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.util.Base64;
import java.util.concurrent.ExecutorService;

import javax.imageio.ImageIO;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.upload.FormFile;
import org.jcodec.api.awt.FrameGrab;
import org.jcodec.common.io.ByteBufferSeekableByteChannel;

import com.biperf.core.domain.WebErrorMessage;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.claim.ClaimService;
import com.biperf.core.service.claim.NominationClaimService;
import com.biperf.core.service.country.CountryService;
import com.biperf.core.service.ecard.EcardService;
import com.biperf.core.service.hierarchy.NodeService;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.service.promotion.NominationPromotionService;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.service.purl.PurlService;
import com.biperf.core.service.security.EncryptionService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.strategy.FileUploadStrategy;
import com.biperf.core.strategy.ImageCropStrategy;
import com.biperf.core.strategy.ImageResizeStrategy;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.nomination.NominationSubmitForm;
import com.biperf.core.ui.utils.ValidatorChecks;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.ImageUtils;
import com.biperf.core.utils.UserManager;
import com.biperf.core.utils.WebResponseConstants;
import com.biperf.core.value.ecard.UploadEcardRequest;
import com.biperf.core.value.nomination.NominationSubmitDataPromotionValueBean;
import com.objectpartners.cms.util.CmsResourceBundle;

public abstract class BaseRecognitionAction extends BaseDispatchAction
{
  private static final Log logger = LogFactory.getLog( BaseRecognitionAction.class );

  protected static final String REQUEST_PARAM_PROMOTION_ID = "promotionId";
  protected static final String REQUEST_PARAM_NODE_ID = "nodeId";
  protected static final String REQUEST_PARAM_RECIPIENT_ID = "recipientId";
  protected static final String REQUEST_PARAM_COMMENT_TEXT = "commentText";
  protected static final String REQUEST_PARAM_RECIPIENT_NODE_ID = "recipientNodeId";
  protected static final int FULL_DIMENSION = 432;
  
  // Client customization for WIP #39189 starts
  protected static final String CLAIM_FILES = "CLAIM_FILES";
  // Client customization for WIP #39189 ends

  protected enum EcardUploadFileType
  {
    IMAGE, VIDEO
  }

  protected EcardUploadBean uploadEcard( FormFile formFile, Long promotionId )
  {
    EcardUploadBean status = null;

    String orginalfilename = formFile.getFileName();
    String fileNameExtn = getFileExtension( orginalfilename );

    String extension = "." + fileNameExtn;
    int targetCropDimension = 0;
    String filename = orginalfilename.substring( 0, orginalfilename.length() - extension.length() );
    if ( filename != null )
    {
      filename = ValidatorChecks.removesSpecialCharactersAndSpaces( filename );

    }
    filename = filename + extension;

    try
    {
      String originalFileName = "userUploadEcard." + fileNameExtn;
      byte[] data = formFile.getFileData();

      if ( ImageUtils.isImageTypeValid( fileNameExtn ) )
      {

        ImageUtils imgInstance = new ImageUtils();
        BufferedImage inputImage = imgInstance.readImage( data );
        BufferedImage full = inputImage;

        targetCropDimension = full.getHeight() < full.getWidth() ? full.getHeight() : full.getWidth();
        full = getImageCropStrategy().process( full, targetCropDimension, targetCropDimension );
        full = getImageResizeStrategy().process( full, FULL_DIMENSION );
        String cardData = Base64.getEncoder().encodeToString( ImageUtils.convertToByteArray( full, fileNameExtn ) );

        status = uploadEcard( originalFileName, cardData, promotionId );
      }
      else
      {
        status = uploadVideo( originalFileName, data );
      }

    }
    catch( Exception e )
    {
      logger.error( "\n\n****************\nERROR in " + getClass().getName() + " in method uploadEcard(FormFile)" + " while upload a card:\n****************\n\n", e );

      status = new EcardUploadBean( createWebErrorMessage( "An unknown error occurred. Please try again later." ) );
    }

    return status;
  }

  protected String getFileExtension( String filename )
  {
    return FilenameUtils.getExtension( filename );
  }

  protected EcardUploadBean uploadEcard( String originalFileName, String cardData, Long cardId )
  {
    EcardUploadBean status = null;
    WebErrorMessage message = null;

    try
    {
      if ( originalFileName == null || originalFileName.trim().length() == 0 )
      {
        throw new RuntimeException( "invalid file name" );
      }

      if ( cardData == null || cardData.length() == 0 )
      {
        throw new RuntimeException( "empty ecard file data" );
      }

      String fileExtension = getFileExtension( originalFileName );

      EcardUploadFileType fileType = null;

      // make sure it's either a supported image format or a supported video format
      if ( !ImageUtils.isImageTypeValid( fileExtension ) && !SupportedEcardVideoTypes.isSupported( fileExtension ) )
      {
        return new EcardUploadBean( createUploadModalWebErrorMessage() );
      }

      // special processing for images; check the size and also crop it to be square
      if ( ImageUtils.isImageTypeValid( fileExtension ) )
      {
        fileType = EcardUploadFileType.IMAGE;

        // check the size
        if ( !ImageUtils.isImageSizeValid( cardData.length() ) )
        {
          message = createWebErrorMessage( CmsResourceBundle.getCmsBundle().getString( "recognition.submit.IMAGE_SIZE" ) + " " + ImageUtils.getImageSizeLimit() + "MB" );
          message.setName( CmsResourceBundle.getCmsBundle().getString( "recognition.submit.IMAGE_UPLOAD_FAILURE" ) );
          return new EcardUploadBean( message );
        }
      }

      WebDavUploadResult uploadResult = null;
      if ( !cardData.contains( ";base64" ) )
      {
        StringBuilder imageDataSb = new StringBuilder();
        imageDataSb.append( "data:" );
        imageDataSb.append( getFileExtension( originalFileName ) );
        imageDataSb.append( ";base64," );
        imageDataSb.append( cardData );
        uploadResult = uploadToS3Bucket( originalFileName, imageDataSb.toString(), cardId );
      }
      else
      {
        uploadResult = uploadToS3Bucket( originalFileName, cardData, cardId );
      }

      if ( uploadResult.isSuccess() )
      {
        message = createSuccessMessage();
      }
      else
      {
        String textKey = EcardUploadFileType.IMAGE.equals( fileType ) ? "recognition.submit.IMAGE_UPLOAD_FAILURE" : "recognition.submit.VIDEO_UPLOAD_FAILURE";
        String text = CmsResourceBundle.getCmsBundle().getString( textKey );
        message = createWebErrorMessage( "", text );
      }

      status = new EcardUploadBean( message, uploadResult.isSuccess(), uploadResult.getUploadedCardFileName(), uploadResult.getWebDavUrl(), null );
    }
    catch( Throwable e )
    {
      logger.error( "\n\n****************\nERROR in " + getClass().getName() + " in method uploadEcard(String, byte[])" + " while upload a card:\n****************\n\n", e );

      status = new EcardUploadBean( createWebErrorMessage( CmsResourceBundle.getCmsBundle().getString( "recognition.submit.GENERIC_UPLOAD_FAILURE_TITLE" ) ) );
      if ( CmsResourceBundle.getCmsBundle() != null && message != null )
      {
        message.setName( CmsResourceBundle.getCmsBundle().getString( "recognition.submit.GENERIC_UPLOAD_FAILURE" ) );
      }
    }

    return status;
  }

  protected EcardUploadBean uploadEcardDrawingData( String originalFileName, String drawingData, Long promotionId )
  {
    EcardUploadBean status = null;
    WebErrorMessage message = null;

    try
    {
      if ( originalFileName == null || originalFileName.trim().length() == 0 )
      {
        throw new RuntimeException( "Invalid file name" );
      }

      if ( drawingData == null || drawingData.length() == 0 )
      {
        throw new RuntimeException( "No drawing data. Is the upload actually a drawing?" );
      }

      // Validate the size of the drawing data layer
      if ( !ImageUtils.isImageSizeValid( drawingData.length() ) )
      {
        message = createWebErrorMessage( CmsResourceBundle.getCmsBundle().getString( "recognition.submit.IMAGE_SIZE" ) + " " + ImageUtils.getImageSize() );
        message.setName( CmsResourceBundle.getCmsBundle().getString( "recognition.submit.IMAGE_UPLOAD_FAILURE" ) );
        return new EcardUploadBean( message );
      }

      // Upload drawing data layer to file storage
      WebDavUploadResult uploadResult = uploadToS3Bucket( originalFileName, drawingData, promotionId );

      boolean success = uploadResult.isSuccess();
      String drawingDataUrl = uploadResult.getWebDavUrl();
      status = new EcardUploadBean( message, success, uploadResult.getUploadedCardFileName(), drawingDataUrl, null );
    }
    catch( Throwable e )
    {
      logger.error( "\n\n****************\nERROR in " + getClass().getName() + " in method uploadEcard(String, byte[])" + " while upload a card:\n****************\n\n", e );

      status = new EcardUploadBean( createWebErrorMessage( CmsResourceBundle.getCmsBundle().getString( "recognition.submit.GENERIC_UPLOAD_FAILURE_TITLE" ) ) );
      if ( CmsResourceBundle.getCmsBundle() != null && message != null )
      {
        message.setName( CmsResourceBundle.getCmsBundle().getString( "recognition.submit.GENERIC_UPLOAD_FAILURE" ) );
      }
    }

    return status;
  }

  private EcardUploadBean uploadVideo( String originalFileName, byte[] data )
  {
    EcardUploadBean status = null;
    WebErrorMessage message = null;

    try
    {
      if ( originalFileName == null || originalFileName.trim().length() == 0 )
      {
        throw new RuntimeException( "invalid file name" );
      }

      if ( data == null || data.length == 0 )
      {
        throw new RuntimeException( "empty video file data" );
      }

      String fileExtension = getFileExtension( originalFileName );

      EcardUploadFileType fileType = null;

      // make sure it's either a supported image format or a supported video format
      if ( !ImageUtils.isImageTypeValid( fileExtension ) && !SupportedEcardVideoTypes.isSupported( fileExtension ) )
      {
        return new EcardUploadBean( createUploadModalWebErrorMessage() );
      }

      // make sure videos are less than 30 MB (approx) in size
      if ( SupportedEcardVideoTypes.isSupported( fileExtension ) )
      {
        fileType = EcardUploadFileType.VIDEO;

        if ( !ImageUtils.isVideoSizeValid( data.length ) )
        {
          message = createWebErrorMessage( CmsResourceBundle.getCmsBundle().getString( "recognition.submit.VIDEO_SIZE" ) + " " + ImageUtils.getVideoSizeLimit() + "MB" );
          message.setName( CmsResourceBundle.getCmsBundle().getString( "recognition.submit.VIDEO_UPLOAD_FAILURE" ) );
          return new EcardUploadBean( message );
        }
      }

      // upload the ecard to webdav!
      WebDavUploadResult uploadResult = uploadToWebdav( originalFileName, data );

      if ( uploadResult.isSuccess() )
      {
        message = createSuccessMessage();
      }
      else
      {
        String textKey = EcardUploadFileType.IMAGE.equals( fileType ) ? "recognition.submit.IMAGE_UPLOAD_FAILURE" : "recognition.submit.VIDEO_UPLOAD_FAILURE";
        String text = CmsResourceBundle.getCmsBundle().getString( textKey );
        message = createWebErrorMessage( "", text );
      }

      // If uploading a video, get preview image
      String thumbnailUrl = null;
      if ( uploadResult.isSuccess() && EcardUploadFileType.VIDEO.equals( fileType ) )
      {
        // FrameGrab does not work with webm files. Use a default thumbnail.
        if ( SupportedEcardVideoTypes.WEBM.getInformalName().equals( fileExtension ) )
        {
          thumbnailUrl = getDefaultVideoImageUrl();
        }
        else
        {
          // Preview image is first frame. Sending that to front end, but leaving original video
          // filename 'encoded'
          // so that we can grab it back out when FE sends the still image URL back to us
          String thumbnailFilename = originalFileName + ".video.jpeg";
          ByteBuffer videoDataBuffer = ByteBuffer.wrap( data );
          ByteBufferSeekableByteChannel videoDataByteChannel = new ByteBufferSeekableByteChannel( videoDataBuffer );
          BufferedImage thumbnailImage = FrameGrab.getFrame( videoDataByteChannel, 1 );
          ByteArrayOutputStream thumbnailOutputStream = new ByteArrayOutputStream( 256 * 1024 );
          ImageIO.write( thumbnailImage, "jpeg", thumbnailOutputStream );
          byte[] thumbnailData = thumbnailOutputStream.toByteArray();

          WebDavUploadResult thumbnailUploadResult = uploadToWebdav( thumbnailFilename, thumbnailData );

          if ( !thumbnailUploadResult.isSuccess() )
          {
            String text = CmsResourceBundle.getCmsBundle().getString( "recognition.submit.VIDEO_UPLOAD_FAILURE" );
            message = createWebErrorMessage( "", text );
          }
          else
          {
            thumbnailUrl = thumbnailUploadResult.getWebDavUrl();
          }
        }
      }

      boolean success;
      String imageUrl;
      String videoUrl = null;
      if ( EcardUploadFileType.VIDEO.equals( fileType ) )
      {
        success = uploadResult.isSuccess() && thumbnailUrl != null;
        imageUrl = thumbnailUrl;
        videoUrl = uploadResult.getWebDavUrl();
      }
      else
      {
        success = uploadResult.isSuccess();
        imageUrl = uploadResult.getWebDavUrl();
      }
      status = new EcardUploadBean( message, success, uploadResult.getUploadedCardFileName(), imageUrl, videoUrl );
    }
    catch( Throwable e )
    {
      logger.error( "\n\n****************\nERROR in " + getClass().getName() + " in method uploadEcard(String, byte[])" + " while upload a card:\n****************\n\n", e );

      status = new EcardUploadBean( createWebErrorMessage( CmsResourceBundle.getCmsBundle().getString( "recognition.submit.GENERIC_UPLOAD_FAILURE_TITLE" ) ) );
      if ( CmsResourceBundle.getCmsBundle() != null && message != null )
      {
        message.setName( CmsResourceBundle.getCmsBundle().getString( "recognition.submit.GENERIC_UPLOAD_FAILURE" ) );
      }
    }

    return status;
  }

  private static final class WebDavUploadResult
  {
    private final boolean success;
    private final String uploadedCardFileName;
    private final String webDavUrl;

    public WebDavUploadResult()
    {
      success = false;
      uploadedCardFileName = "";
      webDavUrl = "";
    }

    public WebDavUploadResult( boolean success, String uploadedCardFileName, String webDavUrl )
    {
      this.success = success;
      this.uploadedCardFileName = uploadedCardFileName;
      this.webDavUrl = webDavUrl;
    }

    public boolean isSuccess()
    {
      return success;
    }

    public String getUploadedCardFileName()
    {
      return uploadedCardFileName;
    }

    public String getWebDavUrl()
    {
      return webDavUrl;
    }
  }

  /** WebErrorMessage which will cause a styled modal to open */
  protected WebErrorMessage createUploadModalWebErrorMessage()
  {
    WebErrorMessage message = new WebErrorMessage();
    message.setCommand( WebResponseConstants.RESPONSE_COMMAND_MODAL );
    message.setType( WebResponseConstants.RESPONSE_TYPE_SERVER_ERROR );
    message.setName( "" );
    message.setText( "" );

    return message;
  }

  protected WebErrorMessage createWebErrorMessage( String text )
  {
    return createWebErrorMessage( text, "" );
  }

  protected WebErrorMessage createWebErrorMessage( String text, String name )
  {
    WebErrorMessage message = new WebErrorMessage();
    message.setCommand( WebResponseConstants.RESPONSE_COMMAND_MODAL );
    message.setType( WebResponseConstants.RESPONSE_TYPE_SERVER_CMD );
    message.setName( name );
    message.setText( text );

    return message;
  }

  protected WebErrorMessage createSuccessMessage()
  {
    WebErrorMessage message = new WebErrorMessage();
    message.setCommand( WebResponseConstants.RESPONSE_COMMAND_MODAL );
    message.setType( WebResponseConstants.RESPONSE_TYPE_SUCCESS );
    message.setSuccess( Boolean.TRUE );
    message.setName( "" );
    message.setText( "" );

    return message;
  }

  protected ExecutorService getExecutorService()
  {
    return (ExecutorService)BeanLocator.getBean( "executorService" );
  }

  protected static UserService getUserService()
  {
    return (UserService)getService( UserService.BEAN_NAME );
  }

  protected PurlService getPurlService()
  {
    return (PurlService)getService( PurlService.BEAN_NAME );
  }

  protected static PromotionService getPromotionService()
  {
    return (PromotionService)getService( PromotionService.BEAN_NAME );
  }

  protected ClaimService getClaimService()
  {
    return (ClaimService)getService( ClaimService.BEAN_NAME );
  }

  protected static NodeService getNodeService()
  {
    return (NodeService)getService( NodeService.BEAN_NAME );
  }

  private ImageCropStrategy getImageCropStrategy()
  {
    return (ImageCropStrategy)BeanLocator.getBean( "imageCropStrategy" );
  }

  private ImageResizeStrategy getImageResizeStrategy()
  {
    return (ImageResizeStrategy)BeanLocator.getBean( "imageResizeStrategy" );
  }

  protected static ParticipantService getParticipantService()
  {
    return (ParticipantService)getService( ParticipantService.BEAN_NAME );
  }

  protected static CountryService getCountryService()
  {
    return (CountryService)getService( CountryService.BEAN_NAME );
  }

  protected static EncryptionService getEncryptionService()
  {
    return (EncryptionService)getService( EncryptionService.BEAN_NAME );
  }

  public boolean moveFileToWebdav( String mediaUrl )
  {
    try
    {
      byte[] media = getAppDataFileUploadStrategy().getFileData( mediaUrl );
      getWebDavFileUploadStrategy().uploadFileData( mediaUrl, media );
      getAppDataFileUploadStrategy().delete( mediaUrl );
      return true;
    }
    catch( Throwable e )
    {
      // Must not have the file in AppDataDir of server executing this process
      logger.error( "\n\n*********************\nERROR in " + getClass().getName() + "#moveFileToWebdav: \n*********************\n\n", e );
    }
    return false;
  }

  protected void storeUserEditedCard( SendRecognitionForm form )
  {

    if ( !StringUtils.isEmpty( form.getCardData() ) )
    {
      // upload it!
      String originalFileName = "userEcard.png";
      EcardUploadBean uploadBean = this.uploadEcard( originalFileName, form.getCardData(), form.getPromotionId() );
      if ( uploadBean.isSuccess() )
      {
        form.setOwnCardName( uploadBean.getOwnCardName() );
        form.setCardUrl( uploadBean.getImageUrl() );
        form.setCardType( SendRecognitionForm.CARD_TYPE_DRAWING );
      }
    }
    else if ( SendRecognitionForm.CARD_TYPE_UPLOAD.equals( form.getCardType() ) )
    {
      form.setOwnCardName( ImageUtils.getFilename( form.getCardUrl() ) );

      // Handle video and image different. Video will two URLs coming back to us, and we save the
      // image URL in a different column
      boolean isVideoUpload = StringUtils.isNotBlank( form.getVideoUrl() );
      if ( isVideoUpload )
      {
        form.setVideoImageUrl( form.getCardUrl() );
      }
      else
      {
        form.setOwnCardName( ImageUtils.getFilename( form.getCardUrl() ) );
      }
    }
  }

  protected void storeNominationUserEditedCard( NominationSubmitForm form )
  {
    NominationSubmitDataPromotionValueBean nominationsPromotion = form.getPromotion();
    if ( !StringUtils.isEmpty( nominationsPromotion.getCardData() ) && !StringUtils.isEmpty( nominationsPromotion.getDrawingData() ) )
    {
      // upload it!
      String originalFileName = "userEcard.png";
      EcardUploadBean drawingUploadBean = this.uploadEcard( originalFileName, nominationsPromotion.getCardData(), nominationsPromotion.getId() );

      // Also upload the drawing data layer so it can be sent back in case of edit
      String originalDrawingDataFilename = "userEcard.txt";
      EcardUploadBean drawingDataUploadBean = this.uploadEcardDrawingData( originalDrawingDataFilename, nominationsPromotion.getDrawingData(), form.getPromotionId() );

      if ( drawingUploadBean.isSuccess() && drawingDataUploadBean.isSuccess() )
      {
        nominationsPromotion.setOwnCardName( drawingUploadBean.getOwnCardName() );
        nominationsPromotion.setCardUrl( drawingUploadBean.getImageUrl() );
        nominationsPromotion.setCardType( SendRecognitionForm.CARD_TYPE_DRAWING );
        nominationsPromotion.setDrawingDataUrl( drawingDataUploadBean.getImageUrl() );
      }
    }
    else if ( NominationSubmitForm.CARD_TYPE_UPLOAD.equals( nominationsPromotion.getCardType() ) )
    {
      // Handle video and image different. Video will two URLs coming back to us, and we save the
      // image URL in a different column
      boolean isVideoUpload = StringUtils.isNotBlank( nominationsPromotion.getVideoUrl() );
      if ( isVideoUpload )
      {
        nominationsPromotion.setVideoImageUrl( nominationsPromotion.getCardUrl() );
      }
      else
      {
        nominationsPromotion.setOwnCardName( ImageUtils.getFilename( nominationsPromotion.getCardUrl() ) );
      }
    }
  }

  private WebDavUploadResult uploadToS3Bucket( String originalFileName, String cardData, Long claimId )
  {
    UploadEcardRequest request = new UploadEcardRequest();
    request.setName( originalFileName );
    request.setFile( cardData );
    request.setClaimId( claimId );
    try
    {
      String url = getEcardService().uploadEcard( request );
      if ( StringUtils.isNotEmpty( url ) )
      {
        return new WebDavUploadResult( true, originalFileName, url );
      }
    }
    catch( Exception e )
    {
      logger.error( e );
    }
    return new WebDavUploadResult();
  }

  private WebDavUploadResult uploadToWebdav( String originalFileName, byte[] data )
  {
    String ownCardNameTemp = UserManager.getUserId() + "_" + DateUtils.getCurrentDateAsLong() + "_" + originalFileName;
    String filePath = "ecard" + '/' + ownCardNameTemp;

    try
    {
      boolean success = getAppDataFileUploadStrategy().uploadFileData( filePath, data );
      if ( !success )
      {
        return new WebDavUploadResult();
      }
    }
    catch( ServiceErrorException t )
    {
      return new WebDavUploadResult();
    }

    try
    {
      boolean ismovedtowebdav = moveFileToWebdav( filePath );
      if ( !ismovedtowebdav )
      {
        return new WebDavUploadResult();
      }
    }
    catch( Throwable t )
    {
      return new WebDavUploadResult();
    }

    // we made it this far so determine the full webDav URL to the media
    // we just uploaded, and return
    String ownCardWebDavPath = EcardUtil.getOwnCardUrl( ownCardNameTemp );

    return new WebDavUploadResult( true, ownCardNameTemp, ownCardWebDavPath );
  }

  private String getDefaultVideoImageUrl()
  {
    return getSiteUrlPrefix() + "/assets/img/placeHolderVid.jpg";
  }

  private String getSiteUrlPrefix()
  {
    return getSystemVariableService().getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();
  }

  private static FileUploadStrategy getAppDataFileUploadStrategy()
  {
    return (FileUploadStrategy)BeanLocator.getBean( FileUploadStrategy.APPDATADIR );
  }

  private static FileUploadStrategy getWebDavFileUploadStrategy()
  {
    return (FileUploadStrategy)BeanLocator.getBean( FileUploadStrategy.WEBDAV );
  }

  protected static NominationPromotionService getNominationPromotionService()
  {
    return (NominationPromotionService)getService( NominationPromotionService.BEAN_NAME );
  }

  protected static NominationClaimService getNominationClaimService()
  {
    return (NominationClaimService)getService( NominationClaimService.BEAN_NAME );
  }

  protected static SystemVariableService getSystemVariableService()
  {
    return (SystemVariableService)getService( SystemVariableService.BEAN_NAME );
  }

  private EcardService getEcardService()
  {
    return (EcardService)getService( EcardService.BEAN_NAME );
  }

}
