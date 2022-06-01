
package com.biperf.core.ui.recognition.purl;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.text.MessageFormat;

import javax.imageio.ImageIO;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.upload.FormFile;
import org.jcodec.api.awt.FrameGrab;
import org.jcodec.common.io.ByteBufferSeekableByteChannel;

import com.biperf.core.domain.WebErrorMessage;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.purl.PurlService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.strategy.FileUploadStrategy;
import com.biperf.core.strategy.ImageCropStrategy;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.recognition.BaseRecognitionAction;
import com.biperf.core.ui.recognition.EcardUploadBean;
import com.biperf.core.ui.recognition.EcardUtil;
import com.biperf.core.ui.recognition.SupportedEcardVideoTypes;
import com.biperf.core.ui.utils.ValidatorChecks;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.ImageUtils;
import com.biperf.core.utils.UserManager;
import com.biperf.core.utils.WebResponseConstants;
import com.biperf.core.value.PurlFileUploadValue;
import com.objectpartners.cms.util.CmsResourceBundle;
import com.objectpartners.cms.util.ContentReaderManager;

public class ContributorMediaUploadAction extends BaseDispatchAction
{

  private static final Log logger = LogFactory.getLog( BaseRecognitionAction.class );

  private enum EcardUploadFileType
  {
    IMAGE, VIDEO
  }

  protected EcardUploadBean uploadEcard( FormFile formFile )
  {
    EcardUploadBean status = null;
    String orginalfilename = formFile.getFileName();
    String extension = "." + getFileExtension( orginalfilename );
    String filename = orginalfilename.substring( 0, orginalfilename.length() - extension.length() );
    if ( filename != null )
    {
      filename = ValidatorChecks.removesSpecialCharactersAndSpaces( filename );

    }
    filename = filename + extension;

    try
    {
      String originalFileName = "userUploadEcard." + getFileExtension( filename );
      byte[] data = formFile.getFileData();

      status = uploadEcard( originalFileName, data );
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

  protected EcardUploadBean uploadEcard( String originalFileName, byte[] data )
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
        throw new RuntimeException( "empty ecard file data" );
      }

      String fileExtension = getFileExtension( originalFileName );

      EcardUploadFileType fileType = null;

      // make sure it's either a supported image format or a supported video format
      if ( !ImageUtils.isImageTypeValid( fileExtension ) && !SupportedEcardVideoTypes.isSupported( fileExtension ) )
      {
        String imageSizeText = ImageUtils.getImageSizeLimit() + "MB";
        String videoSizeText = ImageUtils.getVideoSizeLimit() + "MB";
        String validExtensionMessage = MessageFormat.format( ContentReaderManager.getText( "recognition.submit", "VALID_EXTENSION_MESSAGE" ), new Object[] { imageSizeText, videoSizeText } );
        message = createWebErrorMessage( validExtensionMessage );
        message.setName( CmsResourceBundle.getCmsBundle().getString( "recognition.submit.APOLOGETIC_UPLOAD_FAILURE" ) );
        return new EcardUploadBean( message );
      }

      // special processing for images; check the size and also crop it to be square
      if ( ImageUtils.isImageTypeValid( fileExtension ) )
      {
        fileType = EcardUploadFileType.IMAGE;

        // check the size
        if ( !ImageUtils.isImageSizeValid( data.length ) )
        {
          message = createWebErrorMessage( CmsResourceBundle.getCmsBundle().getString( "recognition.submit.IMAGE_SIZE" ) + " " + ImageUtils.getImageSizeLimit() + "MB" );
          message.setName( CmsResourceBundle.getCmsBundle().getString( "recognition.submit.IMAGE_UPLOAD_FAILURE" ) );
          return new EcardUploadBean( message );
        }

        ImageUtils imgInstance = new ImageUtils();
        // BufferedImage full = imgInstance.readImage( data );

        // crop the image to be square
        /*
         * int targetCropDimension = ( full.getHeight() < full.getWidth() ) ? full.getHeight() :
         * full.getWidth(); data = getImageCropStrategy().process(data, targetCropDimension,
         * targetCropDimension);
         */
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
      PurlFileUploadValue dataFileValue = new PurlFileUploadValue();
      String extension = "." + getFileExtension( originalFileName );
      String filename = originalFileName.substring( 0, originalFileName.length() - extension.length() );
      if ( !EcardUploadFileType.VIDEO.equals( fileType ) )
      {
        dataFileValue.setId( UserManager.getUserId() );
        dataFileValue.setData( data );
        dataFileValue.setType( PurlFileUploadValue.TYPE_PICTURE );
        dataFileValue.setName( originalFileName );
        dataFileValue.setSize( data.length );

        dataFileValue = getPurlService().uploadPhotoForContributor( dataFileValue );
      }
      else
      {
        dataFileValue.setId( UserManager.getUserId() );
        dataFileValue.setData( data );
        dataFileValue.setType( PurlFileUploadValue.TYPE_VIDEO );
        dataFileValue.setName( originalFileName );
        dataFileValue.setSize( data.length );

        dataFileValue = getPurlService().uploadVideoForContributor( dataFileValue );
      }
      // WebDavUploadResult uploadResult = uploadToWebdav( originalFileName, data );

      String messageKey;

      if ( dataFileValue != null )
      {
        messageKey = EcardUploadFileType.IMAGE.equals( fileType ) ? "recognition.submit.IMAGE_UPLOAD_SUCCESS" : "recognition.submit.VIDEO_UPLOAD_SUCCESS";
        String text = CmsResourceBundle.getCmsBundle().getString( messageKey );
        message = createWebErrorMessage( "", text );
      }
      else
      {
        messageKey = EcardUploadFileType.IMAGE.equals( fileType ) ? "recognition.submit.IMAGE_UPLOAD_FAILURE" : "recognition.submit.VIDEO_UPLOAD_FAILURE";
        String text = CmsResourceBundle.getCmsBundle().getString( messageKey );
        message = createWebErrorMessage( "", text );
      }

      // If uploading a video, get preview image
      String thumbnailUrl = null;
      if ( dataFileValue != null && EcardUploadFileType.VIDEO.equals( fileType ) )
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
        success = dataFileValue != null && thumbnailUrl != null;
        imageUrl = thumbnailUrl;
        videoUrl = dataFileValue.getFull();
      }
      else
      {
        success = dataFileValue != null;
        imageUrl = dataFileValue.getFull();
      }

      status = new EcardUploadBean( message, success, filename, imageUrl, videoUrl );
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

  private WebDavUploadResult uploadToWebdav( String originalFileName, byte[] data )
  {
    String ownCardNameTemp = UserManager.getUserId() + "_" + DateUtils.getCurrentDateAsLong() + "_" + originalFileName;
    String filePath = "purl" + '/' + ownCardNameTemp;

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
    String ownCardWebDavPath = EcardUtil.getPurlCardUrl( ownCardNameTemp );

    return new WebDavUploadResult( true, ownCardNameTemp, ownCardWebDavPath );
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

  private ImageCropStrategy getImageCropStrategy()
  {
    return (ImageCropStrategy)BeanLocator.getBean( "imageCropStrategy" );
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

  private static FileUploadStrategy getAppDataFileUploadStrategy()
  {
    return (FileUploadStrategy)BeanLocator.getBean( FileUploadStrategy.APPDATADIR );
  }

  private static FileUploadStrategy getWebDavFileUploadStrategy()
  {
    return (FileUploadStrategy)BeanLocator.getBean( FileUploadStrategy.WEBDAV );
  }

  private SystemVariableService getSystemVariableService()
  {
    return (SystemVariableService)BeanLocator.getBean( SystemVariableService.BEAN_NAME );
  }

  private String getDefaultVideoImageUrl()
  {
    return getSiteUrlPrefix() + "/assets/img/placeHolderVid.jpg";
  }

  private String getSiteUrlPrefix()
  {
    return getSystemVariableService().getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();
  }

  private PurlService getPurlService()
  {
    return (PurlService)getService( PurlService.BEAN_NAME );
  }
}
