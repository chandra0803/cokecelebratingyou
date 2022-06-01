
package com.biperf.core.ui.diycommunication;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ExecutorService;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.upload.FormFile;

import com.biperf.core.domain.diycommunications.DIYCommunications;
import com.biperf.core.domain.hierarchy.Hierarchy;
import com.biperf.core.domain.participant.Audience;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.claim.ClaimService;
import com.biperf.core.service.cms.CMAssetService;
import com.biperf.core.service.country.CountryService;
import com.biperf.core.service.diycommunications.ParticipantDIYCommunicationsService;
import com.biperf.core.service.hierarchy.HierarchyService;
import com.biperf.core.service.hierarchy.NodeService;
import com.biperf.core.service.participant.AudienceService;
import com.biperf.core.service.participant.ListBuilderService;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.service.purl.PurlService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.strategy.FileUploadStrategy;
import com.biperf.core.strategy.ImageCropStrategy;
import com.biperf.core.strategy.ImageResizeStrategy;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.utils.ValidatorChecks;
import com.biperf.core.utils.AwsUtils;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.ClientStateUtils;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.Environment;
import com.biperf.core.utils.ImageUtils;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.DIYCommunicationsFileUploadValue;
import com.biperf.core.value.FormattedValueBean;
import com.biperf.core.value.diycommunication.AudienceResults;
import com.biperf.core.value.diycommunication.CommunicationsUploadDoc;
import com.biperf.core.value.diycommunication.MessageUploadBean;
import com.biperf.core.value.diycommunication.MessageUploadBean.ImagesUploadBean;
import com.biperf.core.value.diycommunication.ResourceList;
import com.biperf.core.value.diycommunication.SelectAudienceParticipantData;
import com.biperf.util.StringUtils;
import com.objectpartners.cms.util.CmsConfiguration;
import com.objectpartners.cms.util.CmsResourceBundle;

public abstract class BaseDIYCommunicationsAction extends BaseDispatchAction
{
  private static final Log logger = LogFactory.getLog( BaseDIYCommunicationsAction.class );

  public static final int DIMENSION_628 = 628;
  public static final int DIMENSION_308 = 308;
  public static final int DIMENSION_162 = 162;
  public static final int DIMENSION_148 = 148;

  // New dimensions
  public static final int DIMENSION_1410 = 1410;
  public static final int DIMENSION_705 = 705;
  public static final int DIMENSION_2820 = 2820;

  public static final int DIMENSION_600 = 600;
  public static final int DIMENSION_300 = 300;
  public static final int DIMENSION_1200 = 1200;

  protected static final String REQUEST_PARAM_PROMOTION_ID = "promotionId";
  protected static final String REQUEST_PARAM_NODE_ID = "nodeId";
  protected static final String REQUEST_PARAM_RECIPIENT_ID = "recipientId";
  protected static final String REQUEST_PARAM_COMMENT_TEXT = "commentText";
  protected static final String REQUEST_PARAM_RECIPIENT_NODE_ID = "recipientNodeId";

  protected DIYOwnImageUploadBean uploadImage( FormFile formFile, List<String> sizes, String communicationType )
  {
    DIYOwnImageUploadBean status = null;
    String originalFileName = null;
    String orginalfilename = formFile.getFileName();
    String extension = "." + getFileExtension( orginalfilename );
    String filename = orginalfilename.substring( 0, orginalfilename.length() - extension.length() );
    if ( filename != null )
    {
      filename = ValidatorChecks.removesSpecialCharactersAndSpaces( filename );
      filename = ImageUtils.getValidFileName( filename );
    }
    filename = filename + extension;

    try
    {
      if ( DIYCommunications.COMMUNICATION_TYPE_BANNER.equals( communicationType ) )
      {
        originalFileName = "userUploadBanner." + getFileExtension( filename );
      }
      else
      {
        originalFileName = "userUploadNewsImage." + getFileExtension( filename );
      }

      byte[] data = formFile.getFileData();

      if ( sizes.size() > 0 && ( sizes.contains( "default" ) || sizes.contains( "mobile" ) || sizes.contains( "max" ) ) )
      {
        status = uploadImageV1( originalFileName, data, sizes, communicationType );
      }
      else
      {
        // TODO WE NEED TO GET RID OF THE BELOW METHOD ONCE THE BANNERS AND NEWS ARE WORKING MAKING
        // SURE NOT BREAKING ANYTHING
        status = uploadImage( originalFileName, data, sizes, communicationType );
      }
    }
    catch( Exception e )
    {
      logger.error( "\n\n****************\nERROR in " + getClass().getName() + " in method uploadEcard(FormFile)" + " while upload a card:\n****************\n\n", e );

      status = new DIYOwnImageUploadBean( createWebErrorMessage( "An unknown error occurred. Please try again later.", "error" ) );
    }

    return status;
  }

  protected String getFileExtension( String filename )
  {
    return FilenameUtils.getExtension( filename );
  }

  private DIYOwnImageUploadBean uploadImage( String originalFileName, byte[] data, List<String> sizes, String communicationType )
  {
    DIYOwnImageUploadBean status = null;
    MessageUploadBean message = null;

    try
    {
      if ( originalFileName == null || originalFileName.trim().length() == 0 )
      {
        throw new RuntimeException( "invalid file name" );
      }

      if ( data == null || data.length == 0 )
      {
        throw new RuntimeException( "empty image file data" );
      }

      String fileExtension = getFileExtension( originalFileName );
      boolean imageTypeValid = ImageUtils.isImageTypeValid( fileExtension );
      // make sure it's a supported image format
      if ( !imageTypeValid )
      {
        message = createWebErrorMessage( CmsResourceBundle.getCmsBundle().getString( "diyCommunications.errors.INVALID_EXT" ), "error" );
        message.setName( CmsResourceBundle.getCmsBundle().getString( "diyCommunications.errors.IMAGE_UPLOAD_FAILURE" ) );
        return new DIYOwnImageUploadBean( message );
      }

      // check the size of the image.
      BufferedImage img = ImageIO.read( new ByteArrayInputStream( data ) );

      if ( !ImageUtils.isImageSizeValid( data.length ) )
      {
        message = createWebErrorMessage( CmsResourceBundle.getCmsBundle().getString( "diyCommunications.errors.IMAGE_SIZE" ), "error" );
        message.setName( CmsResourceBundle.getCmsBundle().getString( "diyCommunications.errors.IMAGE_UPLOAD_FAILURE" ) );
        return new DIYOwnImageUploadBean( message );
      }

      ImageUtils imgInstance = new ImageUtils();
      BufferedImage inputImage = imgInstance.readImage( data );

      /* Process the image and upload the images to webdav */
      BufferedImage image = inputImage;

      if ( sizes.size() > 1 )
      {
        // check the dimension of the image.
        if ( img.getHeight() < DIMENSION_628 || img.getWidth() < DIMENSION_628 )
        {
          message = createWebErrorMessage( CmsResourceBundle.getCmsBundle().getString( "diyCommunications.errors.IMAGE_DIMENSION" ), "error" );
          message.setName( CmsResourceBundle.getCmsBundle().getString( "diyCommunications.errors.IMAGE_UPLOAD_FAILURE" ) );
          return new DIYOwnImageUploadBean( message );
        }
        else
        {
          message = uploadAllsizeImagesToWebdav( originalFileName, image, fileExtension, communicationType );
        }
      }
      else if ( sizes.get( 0 ) != null && sizes.get( 0 ).equals( ImageUtils.IMAGE_SIZE_4x4 ) )
      {
        // check the dimension of the image.
        if ( img.getHeight() < DIMENSION_628 || img.getWidth() < DIMENSION_628 )
        {
          message = createWebErrorMessage( CmsResourceBundle.getCmsBundle().getString( "diyCommunications.errors.IMAGE_DIMENSION" ), "error" );
          message.setName( CmsResourceBundle.getCmsBundle().getString( "diyCommunications.errors.IMAGE_UPLOAD_FAILURE" ) );
          return new DIYOwnImageUploadBean( message );
        }
        else
        {
          WebDavUploadResult uploadResult628x628 = upload628x628ToWebdav( originalFileName, image, fileExtension, communicationType );
          message = buildMessage( uploadResult628x628, sizes.get( 0 ) );
        }
      }
      else if ( sizes.get( 0 ) != null && sizes.get( 0 ).equals( ImageUtils.IMAGE_SIZE_4x2 ) )
      {
        // check the dimension of the image.
        if ( img.getWidth() < DIMENSION_628 || img.getHeight() < DIMENSION_308 )
        {
          message = createWebErrorMessage( CmsResourceBundle.getCmsBundle().getString( "diyCommunications.errors.IMAGE_DIMENSION_628x308" ), "error" );
          message.setName( CmsResourceBundle.getCmsBundle().getString( "diyCommunications.errors.IMAGE_UPLOAD_FAILURE" ) );
          return new DIYOwnImageUploadBean( message );
        }
        else
        {
          WebDavUploadResult uploadResult628x308 = upload628x308ToWebdav( originalFileName, image, fileExtension, communicationType );
          message = buildMessage( uploadResult628x308, sizes.get( 0 ) );
        }
      }
      else if ( sizes.get( 0 ) != null && sizes.get( 0 ).equals( ImageUtils.IMAGE_SIZE_2x2 ) )
      {
        // check the dimension of the image.
        if ( img.getHeight() < DIMENSION_308 || img.getWidth() < DIMENSION_308 )
        {
          message = createWebErrorMessage( CmsResourceBundle.getCmsBundle().getString( "diyCommunications.errors.IMAGE_DIMENSION_308x308" ), "error" );
          message.setName( CmsResourceBundle.getCmsBundle().getString( "diyCommunications.errors.IMAGE_UPLOAD_FAILURE" ) );
          return new DIYOwnImageUploadBean( message );
        }
        else
        {
          WebDavUploadResult uploadResult308x308 = upload308x308ToWebdav( originalFileName, image, fileExtension, communicationType );
          message = buildMessage( uploadResult308x308, sizes.get( 0 ) );
        }
      }
      else if ( communicationType.equals( DIYCommunications.COMMUNICATION_TYPE_BANNER ) && sizes.get( 0 ) != null && sizes.get( 0 ).equals( ImageUtils.IMAGE_SIZE_2x1 ) )
      {
        // check the dimension of the image.
        if ( img.getWidth() < DIMENSION_308 || img.getHeight() < DIMENSION_148 )
        {
          message = createWebErrorMessage( CmsResourceBundle.getCmsBundle().getString( "diyCommunications.errors.IMAGE_DIMENSION_308x148" ), "error" );
          message.setName( CmsResourceBundle.getCmsBundle().getString( "diyCommunications.errors.IMAGE_UPLOAD_FAILURE" ) );
          return new DIYOwnImageUploadBean( message );
        }
        else
        {
          WebDavUploadResult uploadResult308x148 = upload308x148ToWebdav( originalFileName, image, fileExtension, communicationType );
          message = buildMessage( uploadResult308x148, sizes.get( 0 ) );
        }
      }
      else if ( sizes.get( 0 ) != null && sizes.get( 0 ).equals( ImageUtils.IMAGE_SIZE_1x1 ) )
      {
        // check the dimension of the image.
        if ( img.getHeight() < DIMENSION_162 || img.getWidth() < DIMENSION_162 )
        {
          message = createWebErrorMessage( CmsResourceBundle.getCmsBundle().getString( "diyCommunications.errors.IMAGE_DIMENSION_162x162" ), "error" );
          message.setName( CmsResourceBundle.getCmsBundle().getString( "diyCommunications.errors.IMAGE_UPLOAD_FAILURE" ) );
          return new DIYOwnImageUploadBean( message );
        }
        else
        {
          WebDavUploadResult uploadResult162x162 = upload162x162ToWebdav( originalFileName, image, fileExtension, communicationType );
          message = buildMessage( uploadResult162x162, sizes.get( 0 ) );
        }
      }

      status = new DIYOwnImageUploadBean( message );
    }
    catch( Throwable e )
    {
      logger.error( "\n\n****************\nERROR in " + getClass().getName() + " in method uploadImage(String, byte[])" + " while upload a image:\n****************\n\n", e );

      status = new DIYOwnImageUploadBean( createWebErrorMessage( "An unknown error occurred. Please try again later.", "error" ) );
      if ( CmsResourceBundle.getCmsBundle() != null && message != null )
      {
        message.setName( CmsResourceBundle.getCmsBundle().getString( "diyCommunications.errors.IMAGE_UPLOAD_FAILURE" ) );
      }
    }

    return status;
  }

  /**
   * New method added where we generate different sizes of images when the sizes are empty 
   * @param originalFileName
   * @param data
   * @param sizes
   * @param communicationType
   * @return
   */
  private DIYOwnImageUploadBean uploadImageV1( String originalFileName, byte[] data, List<String> sizes, String communicationType )
  {
    DIYOwnImageUploadBean status = null;
    MessageUploadBean message = null;

    try
    {
      if ( originalFileName == null || originalFileName.trim().length() == 0 )
      {
        throw new RuntimeException( "invalid file name" );
      }

      if ( data == null || data.length == 0 )
      {
        throw new RuntimeException( "empty image file data" );
      }

      String fileExtension = getFileExtension( originalFileName );
      boolean imageTypeValid = ImageUtils.isImageTypeValid( fileExtension );
      // make sure it's a supported image format
      if ( !imageTypeValid )
      {
        message = createWebErrorMessage( CmsResourceBundle.getCmsBundle().getString( "diyCommunications.errors.INVALID_EXT" ), "error" );
        message.setName( CmsResourceBundle.getCmsBundle().getString( "diyCommunications.errors.IMAGE_UPLOAD_FAILURE" ) );
        return new DIYOwnImageUploadBean( message );
      }

      // check the size of the image.
      BufferedImage img = ImageIO.read( new ByteArrayInputStream( data ) );

      if ( !ImageUtils.isImageSizeValid( data.length ) )
      {
        message = createWebErrorMessage( CmsResourceBundle.getCmsBundle().getString( "diyCommunications.errors.IMAGE_SIZE" ), "error" );
        message.setName( CmsResourceBundle.getCmsBundle().getString( "diyCommunications.errors.IMAGE_UPLOAD_FAILURE" ) );
        return new DIYOwnImageUploadBean( message );
      }

      ImageUtils imgInstance = new ImageUtils();
      BufferedImage inputImage = imgInstance.readImage( data );

      /* Process the image and upload the images to webdav */
      BufferedImage image = inputImage;

      //
      if ( sizes.size() > 0 && sizes.contains( "default" ) && sizes.contains( "mobile" ) && sizes.contains( "max" ) )
      {
        message = uploadAllsizeImagesToWebdavV1( originalFileName, image, fileExtension, communicationType );
      }
      else if ( sizes.get( 0 ) != null && sizes.get( 0 ).equals( "mobile" ) )
      {
        WebDavUploadResult uploadResult705x300 = upload705x300ToWebdav( originalFileName, image, fileExtension, communicationType );
        message = buildMessage( uploadResult705x300, sizes.get( 0 ) );
      }
      // TODO NEED TO GET RID OF THE BELOW SIZES LOGIC ONCE NEWS AND BANNERS ARE WORKING AS EXPECTED
      else if ( sizes.get( 0 ) != null && sizes.get( 0 ).equals( ImageUtils.IMAGE_SIZE_4x4 ) )
      {
        // check the dimension of the image.
        if ( img.getHeight() < DIMENSION_628 || img.getWidth() < DIMENSION_628 )
        {
          message = createWebErrorMessage( CmsResourceBundle.getCmsBundle().getString( "diyCommunications.errors.IMAGE_DIMENSION" ), "error" );
          message.setName( CmsResourceBundle.getCmsBundle().getString( "diyCommunications.errors.IMAGE_UPLOAD_FAILURE" ) );
          return new DIYOwnImageUploadBean( message );
        }
        else
        {
          WebDavUploadResult uploadResult628x628 = upload628x628ToWebdav( originalFileName, image, fileExtension, communicationType );
          message = buildMessage( uploadResult628x628, sizes.get( 0 ) );
        }
      }
      else if ( sizes.get( 0 ) != null && sizes.get( 0 ).equals( ImageUtils.IMAGE_SIZE_4x2 ) )
      {
        // check the dimension of the image.
        if ( img.getWidth() < DIMENSION_628 || img.getHeight() < DIMENSION_308 )
        {
          message = createWebErrorMessage( CmsResourceBundle.getCmsBundle().getString( "diyCommunications.errors.IMAGE_DIMENSION_628x308" ), "error" );
          message.setName( CmsResourceBundle.getCmsBundle().getString( "diyCommunications.errors.IMAGE_UPLOAD_FAILURE" ) );
          return new DIYOwnImageUploadBean( message );
        }
        else
        {
          WebDavUploadResult uploadResult628x308 = upload628x308ToWebdav( originalFileName, image, fileExtension, communicationType );
          message = buildMessage( uploadResult628x308, sizes.get( 0 ) );
        }
      }
      else if ( sizes.get( 0 ) != null && sizes.get( 0 ).equals( ImageUtils.IMAGE_SIZE_2x2 ) )
      {
        // check the dimension of the image.
        if ( img.getHeight() < DIMENSION_308 || img.getWidth() < DIMENSION_308 )
        {
          message = createWebErrorMessage( CmsResourceBundle.getCmsBundle().getString( "diyCommunications.errors.IMAGE_DIMENSION_308x308" ), "error" );
          message.setName( CmsResourceBundle.getCmsBundle().getString( "diyCommunications.errors.IMAGE_UPLOAD_FAILURE" ) );
          return new DIYOwnImageUploadBean( message );
        }
        else
        {
          WebDavUploadResult uploadResult308x308 = upload308x308ToWebdav( originalFileName, image, fileExtension, communicationType );
          message = buildMessage( uploadResult308x308, sizes.get( 0 ) );
        }
      }
      else if ( communicationType.equals( DIYCommunications.COMMUNICATION_TYPE_BANNER ) && sizes.get( 0 ) != null && sizes.get( 0 ).equals( ImageUtils.IMAGE_SIZE_2x1 ) )
      {
        // check the dimension of the image.
        if ( img.getWidth() < DIMENSION_308 || img.getHeight() < DIMENSION_148 )
        {
          message = createWebErrorMessage( CmsResourceBundle.getCmsBundle().getString( "diyCommunications.errors.IMAGE_DIMENSION_308x148" ), "error" );
          message.setName( CmsResourceBundle.getCmsBundle().getString( "diyCommunications.errors.IMAGE_UPLOAD_FAILURE" ) );
          return new DIYOwnImageUploadBean( message );
        }
        else
        {
          WebDavUploadResult uploadResult308x148 = upload308x148ToWebdav( originalFileName, image, fileExtension, communicationType );
          message = buildMessage( uploadResult308x148, sizes.get( 0 ) );
        }
      }
      else if ( sizes.get( 0 ) != null && sizes.get( 0 ).equals( ImageUtils.IMAGE_SIZE_1x1 ) )
      {
        // check the dimension of the image.
        if ( img.getHeight() < DIMENSION_162 || img.getWidth() < DIMENSION_162 )
        {
          message = createWebErrorMessage( CmsResourceBundle.getCmsBundle().getString( "diyCommunications.errors.IMAGE_DIMENSION_162x162" ), "error" );
          message.setName( CmsResourceBundle.getCmsBundle().getString( "diyCommunications.errors.IMAGE_UPLOAD_FAILURE" ) );
          return new DIYOwnImageUploadBean( message );
        }
        else
        {
          WebDavUploadResult uploadResult162x162 = upload162x162ToWebdav( originalFileName, image, fileExtension, communicationType );
          message = buildMessage( uploadResult162x162, sizes.get( 0 ) );
        }
      }

      status = new DIYOwnImageUploadBean( message );
    }
    catch( Throwable e )
    {
      logger.error( "\n\n****************\nERROR in " + getClass().getName() + " in method uploadImage(String, byte[])" + " while upload a image:\n****************\n\n", e );

      status = new DIYOwnImageUploadBean( createWebErrorMessage( "An unknown error occurred. Please try again later.", "error" ) );
      if ( CmsResourceBundle.getCmsBundle() != null && message != null )
      {
        message.setName( CmsResourceBundle.getCmsBundle().getString( "diyCommunications.errors.IMAGE_UPLOAD_FAILURE" ) );
      }
    }

    return status;
  }

  private MessageUploadBean buildMessage( WebDavUploadResult uploadResult, String size )
  {
    MessageUploadBean message = null;
    if ( uploadResult.isSuccess() )
    {
      message = createWebErrorMessage( CmsResourceBundle.getCmsBundle().getString( "diyCommunications.errors.IMAGE_UPLOAD_SUCCESS" ), "success" );
      message.setSuccess( true );
      List<ImagesUploadBean> images = new ArrayList<ImagesUploadBean>();

      ImagesUploadBean imagesUploadBean = new ImagesUploadBean();
      imagesUploadBean.setImageSize( size );
      imagesUploadBean.setImageUrl( uploadResult.getWebDavUrl() );
      images.add( imagesUploadBean );
      message.setImages( images );
    }
    else
    {
      message = createWebErrorMessage( CmsResourceBundle.getCmsBundle().getString( "diyCommunications.errors.IMAGE_UPLOAD_FAILURE" ), "error" );
      message.setSuccess( false );
    }

    return message;
  }

  /**
   *  Method to create different sizes for image upload 1410x600, 705x300, 2820x1200
   * @param originalFileName
   * @param image
   * @param fileExtension
   * @param communicationType
   * @return
   * @throws ServiceErrorException
   * @throws IOException
   */
  private MessageUploadBean uploadAllsizeImagesToWebdavV1( String originalFileName, BufferedImage image, String fileExtension, String communicationType ) throws ServiceErrorException, IOException
  {
    MessageUploadBean message = null;

    /* upload image 1410x600 size */
    WebDavUploadResult uploadResult1410x600 = upload1410x600ToWebdav( originalFileName, image, fileExtension, communicationType );

    /* upload image 705x300 size */
    WebDavUploadResult uploadResult705x300 = upload705x300ToWebdav( originalFileName, image, fileExtension, communicationType );

    /* upload image 2820x1200 size */
    WebDavUploadResult uploadResult2820x1200 = upload2820x1200ToWebdav( originalFileName, image, fileExtension, communicationType );

    // /* upload image 308x148 size */
    // WebDavUploadResult uploadResult308x148 = null;
    // if ( communicationType.equals( DIYCommunications.COMMUNICATION_TYPE_BANNER ) )
    // {
    // uploadResult308x148 = upload308x148ToWebdav( originalFileName, image, fileExtension,
    // communicationType );
    // }

    // /* upload image 162x162 size */
    // WebDavUploadResult uploadResult162x162 = null;
    // if ( !communicationType.equals( DIYCommunications.COMMUNICATION_TYPE_BANNER ) )
    // {
    // uploadResult162x162 = upload162x162ToWebdav( originalFileName, image, fileExtension,
    // communicationType );
    // }

    /* build Message */
    if ( !communicationType.equals( DIYCommunications.COMMUNICATION_TYPE_BANNER ) )
    {
      if ( uploadResult1410x600.isSuccess() && uploadResult705x300.isSuccess() && uploadResult2820x1200.isSuccess() )
      {
        message = createWebErrorMessage( CmsResourceBundle.getCmsBundle().getString( "diyCommunications.errors.IMAGE_UPLOAD_SUCCESS" ), "success" );
        message.setSuccess( true );
        List<ImagesUploadBean> images = new ArrayList<ImagesUploadBean>();

        ImagesUploadBean imagesUploadBean1 = new ImagesUploadBean();
        imagesUploadBean1.setImageSize( ImageUtils.IMAGE_SIZE );
        imagesUploadBean1.setImageUrl( uploadResult1410x600.getWebDavUrl() );
        images.add( imagesUploadBean1 );

        ImagesUploadBean imagesUploadBean2 = new ImagesUploadBean();
        imagesUploadBean2.setImageSize( ImageUtils.IMAGE_SIZE_MOBILE );
        imagesUploadBean2.setImageUrl( uploadResult705x300.getWebDavUrl() );
        images.add( imagesUploadBean2 );

        ImagesUploadBean imagesUploadBean3 = new ImagesUploadBean();
        imagesUploadBean3.setImageSize( ImageUtils.IMAGE_SIZE_MAX );
        imagesUploadBean3.setImageUrl( uploadResult2820x1200.getWebDavUrl() );
        images.add( imagesUploadBean3 );

        message.setImages( images );
      }
      else
      {
        message = createWebErrorMessage( CmsResourceBundle.getCmsBundle().getString( "diyCommunications.errors.IMAGE_UPLOAD_FAILURE" ), "error" );
        message.setSuccess( false );
      }
    }
    else
    { // for banners
      if ( uploadResult1410x600.isSuccess() && uploadResult705x300.isSuccess() && uploadResult2820x1200.isSuccess() )
      {
        message = createWebErrorMessage( CmsResourceBundle.getCmsBundle().getString( "diyCommunications.errors.IMAGE_UPLOAD_SUCCESS" ), "success" );
        message.setSuccess( true );
        List<ImagesUploadBean> images = new ArrayList<ImagesUploadBean>();

        ImagesUploadBean imagesUploadBean1 = new ImagesUploadBean();
        imagesUploadBean1.setImageSize( ImageUtils.IMAGE_SIZE );
        imagesUploadBean1.setImageUrl( uploadResult1410x600.getWebDavUrl() );
        images.add( imagesUploadBean1 );

        ImagesUploadBean imagesUploadBean2 = new ImagesUploadBean();
        imagesUploadBean2.setImageSize( ImageUtils.IMAGE_SIZE_MAX );
        imagesUploadBean2.setImageUrl( uploadResult2820x1200.getWebDavUrl() );
        images.add( imagesUploadBean2 );

        ImagesUploadBean imagesUploadBean3 = new ImagesUploadBean();
        imagesUploadBean3.setImageSize( ImageUtils.IMAGE_SIZE_MOBILE );
        imagesUploadBean3.setImageUrl( uploadResult705x300.getWebDavUrl() );
        images.add( imagesUploadBean3 );

        message.setImages( images );
      }
      else
      {
        message = createWebErrorMessage( CmsResourceBundle.getCmsBundle().getString( "diyCommunications.errors.IMAGE_UPLOAD_FAILURE" ), "error" );
        message.setSuccess( false );
      }
    }

    return message;
  }

  private MessageUploadBean uploadAllsizeImagesToWebdav( String originalFileName, BufferedImage image, String fileExtension, String communicationType ) throws ServiceErrorException, IOException
  {
    MessageUploadBean message = null;

    /* upload image 628x628 size */
    WebDavUploadResult uploadResult628x628 = upload628x628ToWebdav( originalFileName, image, fileExtension, communicationType );

    /* upload image 628x308 size */
    WebDavUploadResult uploadResult628x308 = upload628x308ToWebdav( originalFileName, image, fileExtension, communicationType );

    /* upload image 308x308 size */
    WebDavUploadResult uploadResult308x308 = upload308x308ToWebdav( originalFileName, image, fileExtension, communicationType );

    /* upload image 308x148 size */
    WebDavUploadResult uploadResult308x148 = null;
    if ( communicationType.equals( DIYCommunications.COMMUNICATION_TYPE_BANNER ) )
    {
      uploadResult308x148 = upload308x148ToWebdav( originalFileName, image, fileExtension, communicationType );
    }

    /* upload image 162x162 size */
    WebDavUploadResult uploadResult162x162 = null;
    if ( !communicationType.equals( DIYCommunications.COMMUNICATION_TYPE_BANNER ) )
    {
      uploadResult162x162 = upload162x162ToWebdav( originalFileName, image, fileExtension, communicationType );
    }

    /* build Message */
    if ( !communicationType.equals( DIYCommunications.COMMUNICATION_TYPE_BANNER ) )
    {
      if ( uploadResult628x628.isSuccess() && uploadResult628x308.isSuccess() && uploadResult308x308.isSuccess() && uploadResult162x162.isSuccess() )
      {
        message = createWebErrorMessage( CmsResourceBundle.getCmsBundle().getString( "diyCommunications.errors.IMAGE_UPLOAD_SUCCESS" ), "success" );
        message.setSuccess( true );
        List<ImagesUploadBean> images = new ArrayList<ImagesUploadBean>();

        ImagesUploadBean imagesUploadBean1 = new ImagesUploadBean();
        imagesUploadBean1.setImageSize( ImageUtils.IMAGE_SIZE_4x4 );
        imagesUploadBean1.setImageUrl( uploadResult628x628.getWebDavUrl() );
        images.add( imagesUploadBean1 );

        ImagesUploadBean imagesUploadBean2 = new ImagesUploadBean();
        imagesUploadBean2.setImageSize( ImageUtils.IMAGE_SIZE_4x2 );
        imagesUploadBean2.setImageUrl( uploadResult628x308.getWebDavUrl() );
        images.add( imagesUploadBean2 );

        ImagesUploadBean imagesUploadBean3 = new ImagesUploadBean();
        imagesUploadBean3.setImageSize( ImageUtils.IMAGE_SIZE_2x2 );
        imagesUploadBean3.setImageUrl( uploadResult308x308.getWebDavUrl() );
        images.add( imagesUploadBean3 );

        ImagesUploadBean imagesUploadBean4 = new ImagesUploadBean();
        imagesUploadBean4.setImageSize( ImageUtils.IMAGE_SIZE_1x1 );
        imagesUploadBean4.setImageUrl( uploadResult162x162.getWebDavUrl() );
        images.add( imagesUploadBean4 );

        message.setImages( images );
      }
      else
      {
        message = createWebErrorMessage( CmsResourceBundle.getCmsBundle().getString( "diyCommunications.errors.IMAGE_UPLOAD_FAILURE" ), "error" );
        message.setSuccess( false );
      }
    }
    else
    {
      if ( uploadResult628x628.isSuccess() && uploadResult628x308.isSuccess() && uploadResult308x308.isSuccess() && uploadResult308x148.isSuccess() )
      {
        message = createWebErrorMessage( CmsResourceBundle.getCmsBundle().getString( "diyCommunications.errors.IMAGE_UPLOAD_SUCCESS" ), "success" );
        message.setSuccess( true );
        List<ImagesUploadBean> images = new ArrayList<ImagesUploadBean>();

        ImagesUploadBean imagesUploadBean1 = new ImagesUploadBean();
        imagesUploadBean1.setImageSize( ImageUtils.IMAGE_SIZE_4x4 );
        imagesUploadBean1.setImageUrl( uploadResult628x628.getWebDavUrl() );
        images.add( imagesUploadBean1 );

        ImagesUploadBean imagesUploadBean2 = new ImagesUploadBean();
        imagesUploadBean2.setImageSize( ImageUtils.IMAGE_SIZE_4x2 );
        imagesUploadBean2.setImageUrl( uploadResult628x308.getWebDavUrl() );
        images.add( imagesUploadBean2 );

        ImagesUploadBean imagesUploadBean3 = new ImagesUploadBean();
        imagesUploadBean3.setImageSize( ImageUtils.IMAGE_SIZE_2x2 );
        imagesUploadBean3.setImageUrl( uploadResult308x308.getWebDavUrl() );
        images.add( imagesUploadBean3 );

        ImagesUploadBean imagesUploadBean4 = new ImagesUploadBean();
        imagesUploadBean4.setImageSize( ImageUtils.IMAGE_SIZE_2x1 );
        imagesUploadBean4.setImageUrl( uploadResult308x148.getWebDavUrl() );
        images.add( imagesUploadBean4 );

        message.setImages( images );
      }
      else
      {
        message = createWebErrorMessage( CmsResourceBundle.getCmsBundle().getString( "diyCommunications.errors.IMAGE_UPLOAD_FAILURE" ), "error" );
        message.setSuccess( false );
      }
    }

    return message;
  }

  /**
   * This method handles the 1410x600 image size upload 
   * @param originalFileName
   * @param image
   * @param fileExtension
   * @param communicationType
   * @return
   * @throws ServiceErrorException
   * @throws IOException
   */
  private WebDavUploadResult upload1410x600ToWebdav( String originalFileName, BufferedImage image, String fileExtension, String communicationType ) throws ServiceErrorException, IOException
  {
    if ( image.getWidth() != DIMENSION_1410 || image.getHeight() != DIMENSION_600 )
    {
      int targetWidth;
      int targetHeight;
      if ( image.getWidth() < image.getHeight() )
      {
        targetWidth = DIMENSION_1410;
        targetHeight = image.getHeight() * targetWidth / image.getWidth();
      }
      else
      {
        targetHeight = DIMENSION_1410;
        targetWidth = image.getWidth() * targetHeight / image.getHeight();
      }

      image = getImageResizeStrategy().process( image, targetWidth, targetHeight );
      image = getImageCropStrategy().process( image, DIMENSION_1410, DIMENSION_600 );
    }
    ByteArrayOutputStream baos1410x600 = new ByteArrayOutputStream();
    ImageIO.write( image, fileExtension, baos1410x600 );
    baos1410x600.flush();
    byte[] imageInByte1410x600 = baos1410x600.toByteArray();
    WebDavUploadResult uploadResult1410x600 = uploadToWebdav( originalFileName, imageInByte1410x600, communicationType );
    return uploadResult1410x600;
  }

  /**
   * This method crops the image to the 705/300 and uploads the image to webdev
   * @param originalFileName
   * @param image
   * @param fileExtension
   * @param communicationType
   * @return
   * @throws ServiceErrorException
   * @throws IOException
   */
  private WebDavUploadResult upload705x300ToWebdav( String originalFileName, BufferedImage image, String fileExtension, String communicationType ) throws ServiceErrorException, IOException
  {
    if ( image.getWidth() != DIMENSION_705 || image.getHeight() != DIMENSION_300 )
    {
      int targetWidth;
      int targetHeight;
      if ( image.getWidth() < image.getHeight() )
      {
        targetWidth = DIMENSION_705;
        targetHeight = image.getHeight() * targetWidth / image.getWidth();
      }
      else
      {
        targetHeight = DIMENSION_705;
        targetWidth = image.getWidth() * targetHeight / image.getHeight();
      }

      image = getImageResizeStrategy().process( image, targetWidth, targetHeight );
      image = getImageCropStrategy().process( image, DIMENSION_705, DIMENSION_300 );
    }
    ByteArrayOutputStream baos705x300 = new ByteArrayOutputStream();
    ImageIO.write( image, fileExtension, baos705x300 );
    baos705x300.flush();
    byte[] imageInByte705x300 = baos705x300.toByteArray();
    WebDavUploadResult uploadResult705x300 = uploadToWebdav( originalFileName, imageInByte705x300, communicationType );
    return uploadResult705x300;
  }

  private WebDavUploadResult upload2820x1200ToWebdav( String originalFileName, BufferedImage image, String fileExtension, String communicationType ) throws ServiceErrorException, IOException
  {
    if ( image.getWidth() != DIMENSION_2820 || image.getHeight() != DIMENSION_1200 )
    {
      int targetWidth;
      int targetHeight;
      if ( image.getWidth() < image.getHeight() )
      {
        targetWidth = DIMENSION_2820;
        targetHeight = image.getHeight() * targetWidth / image.getWidth();
      }
      else
      {
        targetHeight = DIMENSION_2820;
        targetWidth = image.getWidth() * targetHeight / image.getHeight();
      }

      image = getImageResizeStrategy().process( image, targetWidth, targetHeight );
      image = getImageCropStrategy().process( image, DIMENSION_2820, DIMENSION_1200 );
    }
    ByteArrayOutputStream baos2820x1200 = new ByteArrayOutputStream();
    ImageIO.write( image, fileExtension, baos2820x1200 );
    baos2820x1200.flush();
    byte[] imageInByte2820x1200 = baos2820x1200.toByteArray();
    WebDavUploadResult uploadResult2820x1200 = uploadToWebdav( originalFileName, imageInByte2820x1200, communicationType );
    return uploadResult2820x1200;
  }

  private WebDavUploadResult upload628x628ToWebdav( String originalFileName, BufferedImage image, String fileExtension, String communicationType ) throws ServiceErrorException, IOException
  {
    if ( image.getWidth() != DIMENSION_628 || image.getHeight() != DIMENSION_628 )
    {
      int targetWidth;
      int targetHeight;
      if ( image.getWidth() < image.getHeight() )
      {
        targetWidth = DIMENSION_628;
        targetHeight = image.getHeight() * targetWidth / image.getWidth();
      }
      else
      {
        targetHeight = DIMENSION_628;
        targetWidth = image.getWidth() * targetHeight / image.getHeight();
      }

      image = getImageResizeStrategy().process( image, targetWidth, targetHeight );
      image = getImageCropStrategy().process( image, DIMENSION_628, DIMENSION_628 );
    }
    ByteArrayOutputStream baos628x628 = new ByteArrayOutputStream();
    ImageIO.write( image, fileExtension, baos628x628 );
    baos628x628.flush();
    byte[] imageInByte628x628 = baos628x628.toByteArray();
    WebDavUploadResult uploadResult628x628 = uploadToWebdav( originalFileName, imageInByte628x628, communicationType );
    return uploadResult628x628;
  }

  private WebDavUploadResult upload628x308ToWebdav( String originalFileName, BufferedImage image, String fileExtension, String communicationType ) throws ServiceErrorException, IOException
  {
    if ( image.getWidth() != DIMENSION_628 || image.getHeight() != DIMENSION_308 )
    {
      int targetWidth;
      int targetHeight;
      if ( image.getWidth() < image.getHeight() )
      {
        targetWidth = DIMENSION_628;
        targetHeight = image.getHeight() * targetWidth / image.getWidth();
      }
      else
      {
        targetHeight = DIMENSION_628;
        targetWidth = image.getWidth() * targetHeight / image.getHeight();
      }

      image = getImageResizeStrategy().process( image, targetWidth, targetHeight );
      image = getImageCropStrategy().process( image, DIMENSION_628, DIMENSION_308 );
    }
    ByteArrayOutputStream baos628x308 = new ByteArrayOutputStream();
    ImageIO.write( image, fileExtension, baos628x308 );
    baos628x308.flush();
    byte[] imageInByte628x308 = baos628x308.toByteArray();
    WebDavUploadResult uploadResult628x308 = uploadToWebdav( originalFileName, imageInByte628x308, communicationType );
    return uploadResult628x308;
  }

  private WebDavUploadResult upload308x308ToWebdav( String originalFileName, BufferedImage image, String fileExtension, String communicationType ) throws ServiceErrorException, IOException
  {
    if ( image.getWidth() != DIMENSION_308 || image.getHeight() != DIMENSION_308 )
    {
      int targetWidth;
      int targetHeight;
      if ( image.getWidth() < image.getHeight() )
      {
        targetWidth = DIMENSION_308;
        targetHeight = image.getHeight() * targetWidth / image.getWidth();
      }
      else
      {
        targetHeight = DIMENSION_308;
        targetWidth = image.getWidth() * targetHeight / image.getHeight();
      }

      image = getImageResizeStrategy().process( image, targetWidth, targetHeight );
      image = getImageCropStrategy().process( image, DIMENSION_308, DIMENSION_308 );
    }
    ByteArrayOutputStream baos308x308 = new ByteArrayOutputStream();
    ImageIO.write( image, fileExtension, baos308x308 );
    baos308x308.flush();
    byte[] imageInByte308x308 = baos308x308.toByteArray();
    WebDavUploadResult uploadResult308x308 = uploadToWebdav( originalFileName, imageInByte308x308, communicationType );
    return uploadResult308x308;
  }

  private WebDavUploadResult upload308x148ToWebdav( String originalFileName, BufferedImage image, String fileExtension, String communicationType ) throws ServiceErrorException, IOException
  {
    if ( image.getWidth() != DIMENSION_308 || image.getHeight() != DIMENSION_148 )
    {
      int targetWidth;
      int targetHeight;
      if ( image.getWidth() < image.getHeight() )
      {
        targetWidth = DIMENSION_308;
        targetHeight = image.getHeight() * targetWidth / image.getWidth();
      }
      else
      {
        targetHeight = DIMENSION_308;
        targetWidth = image.getWidth() * targetHeight / image.getHeight();
      }

      image = getImageResizeStrategy().process( image, targetWidth, targetHeight );
      image = getImageCropStrategy().process( image, DIMENSION_308, DIMENSION_148 );
    }
    ByteArrayOutputStream baos308x148 = new ByteArrayOutputStream();
    ImageIO.write( image, fileExtension, baos308x148 );
    baos308x148.flush();
    byte[] imageInByte308x148 = baos308x148.toByteArray();
    WebDavUploadResult uploadResult308x148 = uploadToWebdav( originalFileName, imageInByte308x148, communicationType );
    return uploadResult308x148;
  }

  private WebDavUploadResult upload162x162ToWebdav( String originalFileName, BufferedImage image, String fileExtension, String communicationType ) throws ServiceErrorException, IOException
  {
    if ( image.getWidth() != DIMENSION_162 || image.getHeight() != DIMENSION_162 )
    {
      int targetWidth;
      int targetHeight;
      if ( image.getWidth() < image.getHeight() )
      {
        targetWidth = DIMENSION_162;
        targetHeight = image.getHeight() * targetWidth / image.getWidth();
      }
      else
      {
        targetHeight = DIMENSION_162;
        targetWidth = image.getWidth() * targetHeight / image.getHeight();
      }

      image = getImageResizeStrategy().process( image, targetWidth, targetHeight );
      image = getImageCropStrategy().process( image, DIMENSION_162, DIMENSION_162 );
    }
    ByteArrayOutputStream baos162x162 = new ByteArrayOutputStream();
    ImageIO.write( image, fileExtension, baos162x162 );
    baos162x162.flush();
    byte[] imageInByte162x162 = baos162x162.toByteArray();
    WebDavUploadResult uploadResult162x162 = uploadToWebdav( originalFileName, imageInByte162x162, communicationType );
    return uploadResult162x162;
  }

  public void uploadFile( HttpServletResponse response, String filename, DIYCommunicationsFileUploadValue data, String orginalfilename, String communicationsType )
      throws ServiceErrorException, IOException
  {
    try
    {
      boolean validfileSize = getParticipantDIYCommunicationsService().validFileData( data );
      if ( validfileSize )
      {
        // There won't be an Id when creating a new learning object
        Random r = new Random();
        data.setId( Long.parseLong( String.valueOf( r.nextInt( 10000 ) ) ) );
        data = getParticipantDIYCommunicationsService().uploadFileForCommunications( data, communicationsType );
        getParticipantDIYCommunicationsService().moveFileToWebdav( data.getFull() );
        String siteUrlPrefix = getSystemVariableService().getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();
        if ( !Environment.isCtech() )
        {
          siteUrlPrefix = getSystemVariableService().getPropertyByName( SystemVariableService.SITE_URL_PREFIX + "." + Environment.ENV_QA ).getStringVal();
        }
        if ( AwsUtils.isAws() )
        {
          siteUrlPrefix = getSystemVariableService().getContextName();
        }

        String imageUrlPath = siteUrlPrefix + "-cm/cm3dam" + '/' + data.getFull();

        MessageUploadBean uploadBean = new MessageUploadBean();
        uploadBean.setDocURL( imageUrlPath );
        uploadBean.setSuccess( true );
        uploadBean.setType( "Success" );
        uploadBean.setName( orginalfilename );
        uploadBean.setText( CmsResourceBundle.getCmsBundle().getString( "diyCommunications.errors.FILE_UPLOAD_SUCCESS" ) );
        CommunicationsUploadDoc fileUploadView = new CommunicationsUploadDoc( uploadBean );
        writeUploadJsonToResponse( fileUploadView, response );
      }
      else
      {
        MessageUploadBean uploadBean = new MessageUploadBean();
        uploadBean.setSuccess( false );
        uploadBean.setType( "Success" );
        uploadBean.setName( orginalfilename );
        uploadBean.setText( CmsResourceBundle.getCmsBundle().getString( "diyCommunications.errors.VALID_FILE_SIZE" ) );
        CommunicationsUploadDoc fileUploadView = new CommunicationsUploadDoc( uploadBean );
        writeUploadJsonToResponse( fileUploadView, response );
      }
    }
    catch( Throwable e )
    {
      MessageUploadBean uploadBean = new MessageUploadBean();
      uploadBean.setSuccess( false );
      uploadBean.setType( "Success" );
      uploadBean.setName( orginalfilename );
      uploadBean.setText( CmsResourceBundle.getCmsBundle().getString( "diyCommunications.errors.VALID_FILE" ) );
      CommunicationsUploadDoc fileUploadView = new CommunicationsUploadDoc( uploadBean );
      writeUploadJsonToResponse( fileUploadView, response );
      logger.error( "Error during resource center upload pdf:" + e );
    }
  }

  public void uploadImage( HttpServletResponse response, String filename, DIYCommunicationsFileUploadValue data, String orginalfilename, String communicationsType )
      throws ServiceErrorException, IOException
  {
    try
    {
      data.setType( DIYCommunicationsFileUploadValue.TYPE_PICTURE );
      boolean validImageSize = getParticipantDIYCommunicationsService().validFileData( data );
      if ( validImageSize )
      {
        String siteUrlPrefix = getSystemVariableService().getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();
        if ( !Environment.isCtech() )
        {
          siteUrlPrefix = getSystemVariableService().getPropertyByName( SystemVariableService.SITE_URL_PREFIX + "." + Environment.ENV_QA ).getStringVal();
        }
        if ( AwsUtils.isAws() )
        {
          siteUrlPrefix = getSystemVariableService().getContextName();
        }
        // There won't be an Id when creating a new learning object
        Random r = new Random();
        data.setId( Long.parseLong( String.valueOf( r.nextInt( 10000 ) ) ) );
        data.setFileFullPath( siteUrlPrefix + "-cm/cm3dam" + '/' );
        data = getParticipantDIYCommunicationsService().uploadPhotoForCommunications( data, communicationsType );
        getParticipantDIYCommunicationsService().moveFileToWebdav( data.getFull() );
        getParticipantDIYCommunicationsService().moveFileToWebdav( data.getThumb() );

        String imageUrlPath = siteUrlPrefix + "-cm/cm3dam" + '/' + data.getThumb();

        MessageUploadBean uploadBean = new MessageUploadBean();
        uploadBean.setDocURL( imageUrlPath );
        uploadBean.setSuccess( true );
        uploadBean.setType( "Success" );
        uploadBean.setName( orginalfilename );
        uploadBean.setText( CmsResourceBundle.getCmsBundle().getString( "diyCommunications.errors.IMAGE_UPLOAD_SUCCESS" ) );
        CommunicationsUploadDoc fileUploadView = new CommunicationsUploadDoc( uploadBean );
        writeUploadJsonToResponse( fileUploadView, response );
      }
      else
      {
        MessageUploadBean uploadBean = new MessageUploadBean();
        double lowerSizeLimit = 1024 * 1024 * .001;
        if ( data.getSize() <= lowerSizeLimit )
        {
          String imageSizeLowerLimitMessage = CmsResourceBundle.getCmsBundle().getString( "diyCommunications.errors.VALID_IMAGE_LOWER_LIMIT" );
          uploadBean.setText( imageSizeLowerLimitMessage );
        }
        else
        {
          String imageSizeValidMessage = CmsResourceBundle.getCmsBundle().getString( "diyCommunications.errors.IMAGE_SIZE" );
          uploadBean.setText( imageSizeValidMessage );
        }

        uploadBean.setSuccess( false );
        uploadBean.setType( "Failed" );
        uploadBean.setName( orginalfilename );
        CommunicationsUploadDoc fileUploadView = new CommunicationsUploadDoc( uploadBean );
        writeUploadJsonToResponse( fileUploadView, response );
      }
    }
    catch( Throwable e )
    {
      MessageUploadBean uploadBean = new MessageUploadBean();
      uploadBean.setSuccess( false );
      uploadBean.setType( "Failed" );
      uploadBean.setName( orginalfilename );
      uploadBean.setText( CmsResourceBundle.getCmsBundle().getString( "diyCommunications.errors.IMAGE_UPLOAD_FAILURE" ) );
      CommunicationsUploadDoc fileUploadView = new CommunicationsUploadDoc( uploadBean );
      writeUploadJsonToResponse( fileUploadView, response );
      logger.error( "Error during" + communicationsType + " image upload :" + e );
      e.printStackTrace();
    }
  }

  private WebDavUploadResult uploadToWebdav( String originalFileName, byte[] data, String communicationType )
  {
    String ownCardNameTemp = UserManager.getUserId() + "_" + DateUtils.getCurrentDateAsLong() + "_" + originalFileName;
    String filePath = "/diyCommunications/" + communicationType + '/' + ownCardNameTemp;

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

    String siteUrlPrefix = getSystemVariableService().getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();
    if ( !Environment.isCtech() )
    {
      siteUrlPrefix = getSystemVariableService().getPropertyByName( SystemVariableService.SITE_URL_PREFIX + "." + Environment.ENV_QA ).getStringVal();
    }
    if ( AwsUtils.isAws() )
    {
      siteUrlPrefix = getSystemVariableService().getContextName();
    }

    String ownCardWebDavPath = siteUrlPrefix + "-cm/cm3dam" + filePath;

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

  protected MessageUploadBean createWebErrorMessage( String text, String type )
  {
    MessageUploadBean message = new MessageUploadBean();
    message.setType( type );
    message.setName( CmsResourceBundle.getCmsBundle().getString( "diyCommunications.errors.IMAGE_UPLOAD_SUCCESS" ) );
    message.setText( text );

    return message;
  }

  protected Long buildCommunicationsTypeId( HttpServletRequest request, String communicationsTypeId )
  {
    Long primerId = null;
    try
    {
      String strPrimerId = ClientStateUtils.getParameterValue( request, ClientStateUtils.getClientStateMap( request ), communicationsTypeId );

      if ( !StringUtils.isEmpty( strPrimerId ) )
      {
        primerId = new Long( strPrimerId );
      }

      // check the standard request (for the JSON calls)
      if ( primerId == null )
      {
        primerId = !StringUtils.isEmpty( request.getParameter( communicationsTypeId ) ) ? new Long( request.getParameter( communicationsTypeId ) ) : null;
      }

    }
    catch( NumberFormatException e )
    {
      log.error( " Error occured while getting primer. Primer Name : " + communicationsTypeId );
    }
    return primerId;
  }

  /**
   * We need to set the content type as text/html for upload.
   * Default is text/html
   */
  public void writeUploadJsonToResponse( Object bean, HttpServletResponse response ) throws IOException
  {
    PrintWriter out = response.getWriter();
    out.print( toJson( bean ) );
    out.flush();
    out.close();
  }

  public List<com.objectpartners.cms.domain.Audience> getAudienceNames( DIYCommunications communications )
  {
    List<com.objectpartners.cms.domain.Audience> audienceList = new ArrayList<com.objectpartners.cms.domain.Audience>();

    audienceList = getCMAssetService().getAudienceForAsset( communications.getCmAssetCode() );

    return audienceList;
  }

  public List<FormattedValueBean> populateFormattedValueBean( Set<Audience> audiences )
  {
    Hierarchy primaryHierarchy = getHierarchyService().getPrimaryHierarchy();
    List<FormattedValueBean> newFvbPaxSecondList = new ArrayList<FormattedValueBean>();
    List<FormattedValueBean> fvbPaxSecondList = getListBuilderService().searchParticipants( audiences, primaryHierarchy.getId(), true, null, true );
    for ( Iterator<FormattedValueBean> fvbIter = fvbPaxSecondList.iterator(); fvbIter.hasNext(); )
    {
      FormattedValueBean fmv = fvbIter.next();
      fmv.setFnameLname( getParticipantService().getLNameFNameByPaxId( fmv.getId() ) );
      newFvbPaxSecondList.add( fmv );
    }

    return newFvbPaxSecondList;
  }

  public SelectAudienceParticipantData generateAudience( String communicationId, HttpServletRequest request )
  {
    SelectAudienceParticipantData audienceList = new SelectAudienceParticipantData();
    List<Audience> publicAudienceList = getParticipantDIYCommunicationsService().getPublicAudienceList();
    List<com.objectpartners.cms.domain.Audience> savedAudience = new ArrayList<com.objectpartners.cms.domain.Audience>();
    List<Audience> selectedAudience = new ArrayList<Audience>();
    List<AudienceResults> results = new ArrayList<AudienceResults>();

    @SuppressWarnings( "unchecked" )
    List<AudienceResults> sessionAudienceList = (List<AudienceResults>)request.getSession().getAttribute( "communicationsAudienceList" );

    if ( sessionAudienceList != null )
    {
      for ( Iterator<AudienceResults> audienceIter = sessionAudienceList.iterator(); audienceIter.hasNext(); )
      {
        AudienceResults audienceResult = audienceIter.next();
        Audience audience = getAudienceService().getAudienceById( new Long( audienceResult.getId() ), null );
        selectedAudience.add( audience );
      }
    }
    else
    {
      Long communicationIdLong = (Long)request.getSession().getAttribute( communicationId );

      DIYCommunications communications = communicationIdLong == null ? new DIYCommunications() : getCommunicationById( communicationIdLong );

      if ( communicationIdLong != null )
      {
        savedAudience = getAudienceNames( communications );
      }
    }

    request.getSession().removeAttribute( "audienceResultsList" );
    List<Long> audienceResultsList = new ArrayList<Long>();
    for ( Iterator<Audience> audienceIter = publicAudienceList.iterator(); audienceIter.hasNext(); )
    {
      Audience audience = audienceIter.next();

      Set<Audience> audiences = new LinkedHashSet<Audience>();
      audiences.add( audience );
      List<FormattedValueBean> newFvbPaxSecondList = getParticipantDIYCommunicationsService().getAudienceParticipants( audience.getId() );
      AudienceResults audienceResult = new AudienceResults( audience, newFvbPaxSecondList, savedAudience, selectedAudience );
      Long audienceExportLimit = getSystemVariableService().getPropertyByName( SystemVariableService.DIY_AUDIENCE_EXPORT_LIMIT ).getLongVal();
      if ( audience.getSize() > audienceExportLimit )
      {
        audienceResultsList.add( audience.getId() );
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.put( "audienceId", audience.getId() );
        String audienceExportUrl = ClientStateUtils.generateEncodedLink( "", request.getServletContext().getContextPath() + "/participant/exportDiyAudience.do?method=exportAudience", parameterMap );
        audienceResult.setParticipantExportLink( audienceExportUrl );
      }
      results.add( audienceResult );
      audienceList.getAudienceTable().setResults( results );
    }

    request.getSession().setAttribute( "audienceResultsList", audienceResultsList );

    request.getSession().removeAttribute( "communicationsAudienceList" );

    return audienceList;
  }

  protected boolean addContentInDefaultLocale( List<ResourceList> resourceList )
  {
    Locale locale = getCMSDefaultLocale();
    for ( Iterator<ResourceList> resourceListIter = resourceList.iterator(); resourceListIter.hasNext(); )
    {
      ResourceList resource = resourceListIter.next();
      if ( resource.getLanguage().equals( locale.toString() ) )
      {
        return false;
      }
    }

    return true;
  }

  protected Locale getCMSDefaultLocale()
  {
    CmsConfiguration cmsConfiguration = (CmsConfiguration)BeanLocator.getBean( "cmsConfiguration" );
    // CMS Reader setup
    Locale locale = cmsConfiguration.getDefaultLocale();

    return locale;
  }

  protected boolean insertDefaultLocaleContent( List<ResourceList> diyContentList )
  {
    Locale systemDefaultLanguage = getCMSDefaultLocale();
    boolean insertDefaultLocale = true;
    for ( Iterator<ResourceList> contentIterator = diyContentList.iterator(); contentIterator.hasNext(); )
    {
      ResourceList content = contentIterator.next();
      if ( content.getLanguage().equals( systemDefaultLanguage.toString() ) )
      {
        insertDefaultLocale = false;
        break;
      }
    }
    return insertDefaultLocale;
  }

  protected void removeExistingDefaultContent( List<ResourceList> diyContentList )
  {
    Locale systemDefaultLanguage = getCMSDefaultLocale();
    for ( Iterator<ResourceList> contentIterator = diyContentList.iterator(); contentIterator.hasNext(); )
    {
      ResourceList content = contentIterator.next();
      if ( content.getLanguage().equals( systemDefaultLanguage.toString() ) )
      {
        contentIterator.remove();
        break;
      }
    }
  }

  public DIYCommunications getCommunicationById( Long communicationId )
  {
    return getParticipantDIYCommunicationsService().getDIYCommunicationsById( communicationId );
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

  protected static ParticipantService getParticipantService()
  {
    return (ParticipantService)getService( ParticipantService.BEAN_NAME );
  }

  protected static CountryService getCountryService()
  {
    return (CountryService)getService( CountryService.BEAN_NAME );
  }

  private boolean moveFileToWebdav( String mediaUrl )
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

  public ParticipantDIYCommunicationsService getParticipantDIYCommunicationsService()
  {
    return (ParticipantDIYCommunicationsService)getService( ParticipantDIYCommunicationsService.BEAN_NAME );
  }

  public HierarchyService getHierarchyService()
  {
    return (HierarchyService)getService( HierarchyService.BEAN_NAME );
  }

  public ListBuilderService getListBuilderService()
  {
    return (ListBuilderService)getService( ListBuilderService.BEAN_NAME );
  }

  public SystemVariableService getSystemVariableService()
  {
    return (SystemVariableService)getService( SystemVariableService.BEAN_NAME );
  }

  public CMAssetService getCMAssetService()
  {
    return (CMAssetService)getService( CMAssetService.BEAN_NAME );
  }

  public AudienceService getAudienceService()
  {
    return (AudienceService)getService( AudienceService.BEAN_NAME );
  }

  public ImageResizeStrategy getImageResizeStrategy()
  {
    return (ImageResizeStrategy)BeanLocator.getBean( "imageResizeStrategy" );
  }

}
