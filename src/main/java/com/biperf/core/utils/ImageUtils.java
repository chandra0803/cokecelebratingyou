
package com.biperf.core.utils;

import java.awt.color.ColorSpace;
import java.awt.color.ICC_ColorSpace;
import java.awt.color.ICC_Profile;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;

import javax.imageio.IIOException;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.sanselan.ImageReadException;
import org.apache.sanselan.Sanselan;
import org.apache.sanselan.common.IImageMetadata;
import org.apache.sanselan.common.byteSources.ByteSource;
import org.apache.sanselan.common.byteSources.ByteSourceArray;
import org.apache.sanselan.formats.jpeg.JpegImageMetadata;
import org.apache.sanselan.formats.jpeg.JpegImageParser;
import org.apache.sanselan.formats.jpeg.segments.UnknownSegment;
import org.apache.sanselan.formats.tiff.TiffField;
import org.apache.sanselan.formats.tiff.TiffImageMetadata;
import org.apache.sanselan.formats.tiff.constants.ExifTagConstants;

import com.biperf.core.domain.enums.EnvironmentTypeEnum;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.SAO;
import com.biperf.core.service.maincontent.DesignThemeService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.strategy.ImageCropStrategy;

public class ImageUtils
{
  private static final String UnixFileSeparator = "/";
  private static final String WindowsFileSeparator = "\\";
  private static final String FileExtensionDelimiter = ".";
  private static final String DetailPrefix = "-_dtl_-";
  private static final String ThumbPrefix = "-_tn_-";
  private static final String PurlPrefix = "purl";
  private static final String ProfilePrefix = "profile";//client customization
  private static final String QuizPrefix = "quiz";
  private static final String CelebrationPrefix = "celebration";
  private static final String PersonalInfoPerfix = "personal";
  private static final String DIYCommunicationsPrefix = "diyCommunications";
  private static final String SSIContestInfoPerfix = "ssi_contest";
  private static final String SSIContestGuidePerfix = "ssi_contestGuide";
  private static final String SSIContestClaimsPerfix = "ssi_contest_claims";
  private static final String SSIContestProgressPerfix = "ssi_contestProgress";
  private static final String SSIContestPAXUploadPerfix = "ssi_contestPAXUpload";
  private static final String SSIContestProgressADCPerfix = "SSIProgress";
  private static final String SSIContesPAXUploadADCPerfix = "SSIPAXUpload";
  private static final String ADC_INCOMING_FOLDER = "incoming";
  private static final String ADC_VALID_FOLDER = "valid";
  public static final int COLOR_TYPE_RGB = 1;
  public static final int COLOR_TYPE_CMYK = 2;
  public static final int COLOR_TYPE_YCCK = 3;
  public static final int MEGABYTES_TO_BYTES_MULTIPLIER = 1024 * 1024;

  private static int colorType = COLOR_TYPE_RGB;
  private static boolean hasAdobeMarker = false;

  private static final Log logger = LogFactory.getLog( ImageUtils.class );

  public static final String AVATAR_IMAGE_TINY = "avatar-blank-48.png";
  public static final String AVATAR_IMAGE_SMALL = "avatar-blank-72.png";
  public static final String AVATAR_IMAGE_LARGE = "avatar-blank-288.png";
  public static final String AVATAR_MULTI_IMAGE_SMALL = "avatar-multi-blank-72.png";

  public static final String SHADOW_AVATAR_IMAGE_TINY = "avatar-shadow-48.png";
  public static final String SHADOW_AVATAR_IMAGE_SMALL = "avatar-shadow-72.png";
  // public static final String SHADOW_AVATAR_IMAGE_SMALL = "avatar-shadow-144.png";
  public static final String SHADOW_AVATAR_IMAGE_LARGE = "avatar-shadow-288.png";
  public static final String IMAGE_SIZE_4x4 = "4x4";
  public static final String IMAGE_SIZE_4x2 = "4x2";
  public static final String IMAGE_SIZE_2x2 = "2x2";
  public static final String IMAGE_SIZE_2x1 = "2x1";
  public static final String IMAGE_SIZE_1x1 = "page";

  public static final String IMAGE_SIZE_MAX = "max";
  public static final String IMAGE_SIZE = "default";
  public static final String IMAGE_SIZE_MOBILE = "mobile";

  public static String getPath( String filePath )
  {
    if ( null != filePath )
    {
      int pathIndex = filePath.lastIndexOf( UnixFileSeparator );
      if ( pathIndex == -1 )
      {
        pathIndex = filePath.lastIndexOf( WindowsFileSeparator );
      }

      if ( pathIndex >= 0 )
      {
        return filePath.substring( 0, pathIndex + 1 );
      }
    }
    return null;
  }

  public static String getFilename( String filePath )
  {
    if ( null != filePath )
    {
      int pathIndex = filePath.lastIndexOf( UnixFileSeparator );
      if ( pathIndex == -1 )
      {
        pathIndex = filePath.lastIndexOf( WindowsFileSeparator );
      }

      if ( pathIndex >= 0 )
      {
        return filePath.substring( pathIndex + 1 );
      }
    }
    return null;
  }

  public static String getValidFileName( String fileName )
  {
    if ( fileName != null )
    {
      if ( fileName.length() > 25 )
      {
        fileName = fileName.substring( 0, 25 );
      }
    }
    return fileName;
  }

  public static String getFileExtension( String filePath )
  {
    if ( null != filePath )
    {
      int extIndex = filePath.lastIndexOf( FileExtensionDelimiter );
      if ( extIndex >= 0 )
      {
        return filePath.substring( extIndex + 1 );
      }
    }
    return null;
  }

  public static String convertPath( String filePath )
  {
    String replacedFilePath = null;
    if ( File.separator.equals( WindowsFileSeparator ) )
    {
      replacedFilePath = filePath.replace( '/', '\\' );
    }
    if ( File.separator.equals( UnixFileSeparator ) )
    {
      replacedFilePath = filePath.replace( '\\', '/' );
    }
    return replacedFilePath;
  }

  public static String convertToUrlPath( String filePath )
  {
    return filePath.replace( '\\', '/' );
  }

  public static String getQuizDetailPath( Long id, String formName, String filename )
  {
    return getQuizPath( id, formName, filename, DetailPrefix );
  }

  public static String getQuizThumbPath( Long id, String formName, String filename )
  {
    return getQuizPath( id, formName, filename, ThumbPrefix );
  }

  private static String getQuizPath( Long id, String formName, String filename, String prefix )
  {
    String formatFileName = filename.replace( " ", "_" );
    String formatFormame = formName.replace( " ", "_" );
    return QuizPrefix + UnixFileSeparator + formatFormame + UnixFileSeparator + id + "_" + formatFileName;
  }

  public static String getCelebrationThumbPath( String type, Long id, String filename )
  {
    return getCelebrationPath( type, id, filename, ThumbPrefix );
  }

  private static String getCelebrationPath( String type, Long id, String filename, String prefix )
  {
    String formatFileName = filename.replaceAll( "[^A-Za-z0-9.]", "_" );
    return CelebrationPrefix + UnixFileSeparator + type + UnixFileSeparator + id + '-' + new Date().getTime() + prefix + formatFileName;
  }

  public static String getCommunicationsResourceCenterDetailPath( Long id, String filename, String communicationsType )
  {
    return getcommunicationsPath( id, filename, DetailPrefix, communicationsType );
  }

  public static String getCommunicationsThumbPath( Long id, String filename, String communicationsType )
  {
    return getcommunicationsPath( id, filename, ThumbPrefix, communicationsType );
  }

  private static String getcommunicationsPath( Long id, String filename, String prefix, String communicationsType )
  {
    String formatFileName = filename.replace( " ", "_" );
    return DIYCommunicationsPrefix + UnixFileSeparator + communicationsType + UnixFileSeparator + id + "_" + formatFileName;
  }

  public static String getPurlDetailPath( String type, Long id, String filename )
  {
    return getPurlPath( type, id, filename, DetailPrefix );
  }
  
  //Client customization start
  public static String getProfileDetailPath( String type, Long id, String filename )
  {
    return getProfilePath( type, id, filename, DetailPrefix );
  }
  
  private static String getProfilePath( String type, Long id, String filename, String prefix )
  {
    // Fix for bug# 40885
    String formatFileName = filename.replaceAll( "[^A-Za-z0-9.]", "_" );
    return ProfilePrefix + UnixFileSeparator + type + UnixFileSeparator + id + '-' + new Date().getTime() + prefix + formatFileName;
  }
  //client customization ends

  public static String getSSIContestGuidePath( String type, Long id, String filename )
  {
    String formatFileName = filename.replaceAll( "[^A-Za-z0-9.]", "_" );
    return SSIContestGuidePerfix + UnixFileSeparator + type + UnixFileSeparator + id + '-' + new Date().getTime() + formatFileName;
  }

  public static String getSSIContestClaimsPath( String type, Long id, String filename )
  {
    String formatFileName = filename.replaceAll( "[^A-Za-z0-9.]", "_" );
    return SSIContestClaimsPerfix + UnixFileSeparator + id + '-' + new Date().getTime() + formatFileName;
  }

  public static String getSSIContestProgressFilePath( String type, Long id, String filename )
  {
    String formatFileName = filename.replaceAll( "[^A-Za-z0-9.]", "_" );
    return SSIContestProgressPerfix + UnixFileSeparator + type + UnixFileSeparator + id + '-' + new Date().getTime() + formatFileName;
  }

  public static String getSSIContestPAXUploadFilePath( String type, Long id, String filename )
  {
    String formatFileName = filename.replaceAll( "[^A-Za-z0-9.]", "_" );
    return SSIContestPAXUploadPerfix + UnixFileSeparator + type + UnixFileSeparator + id + '-' + new Date().getTime() + formatFileName;
  }

  public static String getADCIncomingFolderName( String subfolder )
  {
    String incomingFolder = UnixFileSeparator + subfolder + UnixFileSeparator;

    if ( !AwsUtils.isAws() ) // AWS "ADC" file structure is different...
    {
      incomingFolder = incomingFolder + ADC_INCOMING_FOLDER + UnixFileSeparator;
    }

    return incomingFolder;
  }

  public static String getSSIContestProgressADCFilePath( Long contestId, String extension )
  {
    String date = DateUtils.toDisplayTimeUString( new Date(), Locale.US );
    String formattedDate = date.replaceAll( "/", "" );
    // return SSIContestProgressADCPerfix + "_" + contestId + "_" + formattedDate + extension; //use
    // it after ADC setup done
    if ( AwsUtils.isAws() )
    {
      return "ssiprog_" + SSIContestProgressADCPerfix + "_" + formattedDate + extension;
    }
    else
    {
      return SSIContestProgressADCPerfix + "_" + formattedDate + extension;
    }
  }

  public static String getSSIContestPAXUploadADCFilePath( Long contestId, String extension )
  {
    String date = DateUtils.toDisplayTimeUString( new Date(), Locale.US );
    String formattedDate = date.replaceAll( "/", "" );
    return SSIContesPAXUploadADCPerfix + "_" + formattedDate + extension;
  }

  public static String getPurlThumbPath( String type, Long id, String filename )
  {
    return getPurlPath( type, id, filename, ThumbPrefix );
  }

  private static String getPurlPath( String type, Long id, String filename, String prefix )
  {
    // Fix for bug# 40885
    String formatFileName = filename.replaceAll( "[^A-Za-z0-9.]", "_" );
    return PurlPrefix + UnixFileSeparator + type + UnixFileSeparator + id + '-' + new Date().getTime() + prefix + formatFileName;
  }

  public static byte[] convertToByteArray( BufferedImage image ) throws IOException
  {
    return convertToByteArray( image, "JPG" );
  }

  public static byte[] convertToByteArray( BufferedImage image, String imageType ) throws IOException
  {
    ByteArrayOutputStream oStream = new ByteArrayOutputStream();
    ImageIO.write( image, imageType, oStream );
    return oStream.toByteArray();
  }

  public static BufferedImage convertToBufferedImage( byte[] data ) throws IOException
  {
    return ImageIO.read( new ByteArrayInputStream( data ) );
  }

  public static String getPersonalInfoThumbPath( String type, Long id, String filename )
  {
    return getPersonalInfoPath( type, id, filename, ThumbPrefix );
  }

  public static String getPersonalInfoDetailPath( String type, Long id, String filename )
  {
    return getPersonalInfoPath( type, id, filename, DetailPrefix );
  }

  private static String getPersonalInfoPath( String type, Long id, String filename, String prefix )
  {
    String formatFileName = filename.replace( " ", "_" );
    return PersonalInfoPerfix + UnixFileSeparator + type + UnixFileSeparator + id + '-' + new Date().getTime() + prefix + formatFileName;
  }

  public static String getSSIContestInfoDetailPath( Long id, String filename )
  {
    return getSSIContestInfoPath( id, filename, DetailPrefix );
  }

  private static String getSSIContestInfoPath( Long id, String filename, String prefix )
  {
    String formatFileName = filename.replace( " ", "_" );
    return SSIContestInfoPerfix + UnixFileSeparator + id + '-' + new Date().getTime() + formatFileName;
  }

  public static File getFile( byte[] byteFile, String fileName ) throws Exception
  {
    FileOutputStream fileOuputStream = new FileOutputStream( fileName );
    try
    {
      // convert array of bytes into file
      fileOuputStream.write( byteFile );
      fileOuputStream.close();
    }
    catch( Exception e )
    {
      e.printStackTrace();
    }
    File file = new File( fileName );
    return file;
  }

  public BufferedImage readImage( byte[] data ) throws IOException, ImageReadException
  {
    colorType = COLOR_TYPE_RGB;
    hasAdobeMarker = false;
    int orientation = 0;

    IImageMetadata metadata = Sanselan.getMetadata( data );
    if ( metadata instanceof JpegImageMetadata )
    {
      JpegImageMetadata jpegMetadata = (JpegImageMetadata)metadata;
      TiffImageMetadata newData = null;
      if ( jpegMetadata != null )
      {
        newData = jpegMetadata.getExif();
        if ( newData != null )
        {
          TiffField tf = newData.findField( ExifTagConstants.EXIF_TAG_ORIENTATION );
          if ( tf != null )
          {
            orientation = tf.getIntValue();
          }
        }
      }
    }

    ImageInputStream stream = ImageIO.createImageInputStream( new ByteArrayInputStream( data ) );
    Iterator<ImageReader> iter = ImageIO.getImageReaders( stream );
    if ( iter.hasNext() )
    {
      ImageReader reader = iter.next();
      reader.setInput( stream );

      BufferedImage image;
      ICC_Profile profile = null;
      try
      {
        image = reader.read( 0 );
        if ( orientation > 1 )
        {
          AffineTransform tx = new AffineTransform();
          tx = getExifTransformation( orientation, image );
          // tx.rotate(Math.toRadians(degreeRotate), image.getWidth()/2, image.getHeight()/2);
          // tx.rotate(Math.toRadians(degreeRotate), image.getWidth()/2.5, image.getHeight()/2.5);
          AffineTransformOp op = new AffineTransformOp( tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR );
          image = op.filter( image, null );
        }

      }
      catch( IIOException e )
      {
        colorType = COLOR_TYPE_CMYK;
        checkAdobeMarker( data );
        profile = Sanselan.getICCProfile( data );
        WritableRaster raster = (WritableRaster)reader.readRaster( 0, null );
        if ( colorType == COLOR_TYPE_YCCK )
        {
          convertYcckToCmyk( raster );
        }
        if ( hasAdobeMarker )
        {
          convertInvertedColors( raster );
        }
        image = convertCmykToRgb( raster, profile );
      }

      return image;
    }

    return null;
  }

  public static boolean isGif( byte[] imageBytes )
  {
    String format = ImageUtils.getImageFormat( imageBytes );
    if ( format != null )
    {
      if ( format.toLowerCase().contains( "gif" ) )
      {
        return true;
      }
    }
    return false;
  }

  public static String getImageFormat( byte[] imageBytes )
  {
    try
    {
      InputStream is = new ByteArrayInputStream( imageBytes );

      // create an image input stream on the image
      ImageInputStream iis = ImageIO.createImageInputStream( is );

      // find all image readers that recognize the image format
      Iterator<ImageReader> iter = ImageIO.getImageReaders( iis );
      if ( !iter.hasNext() )
      {
        // no readers found
        return null;
      }

      // use the first reader
      ImageReader reader = (ImageReader)iter.next();

      // close stream
      iis.close();

      // return the format name
      return reader.getFormatName();
    }
    catch( Throwable t )
    {
      // eh, do nothing, will return null
    }

    return null;
  }

  public static byte[] cropToSquare( byte[] data, ImageCropStrategy imageCropStrategy ) throws IOException, ServiceErrorException
  {
    BufferedImage bi = convertToBufferedImage( data );
    int targetCropDimension = bi.getHeight() < bi.getWidth() ? bi.getHeight() : bi.getWidth();
    bi = imageCropStrategy.process( bi, targetCropDimension, targetCropDimension );
    return ImageUtils.convertToByteArray( bi );
  }

  public static void checkAdobeMarker( byte[] imageData ) throws IOException, ImageReadException
  {
    JpegImageParser parser = new JpegImageParser();
    ByteSource byteSource = new ByteSourceArray( imageData );
    @SuppressWarnings( "rawtypes" )
    ArrayList segments = parser.readSegments( byteSource, new int[] { 0xffee }, true );
    if ( segments != null && segments.size() >= 1 )
    {
      UnknownSegment app14Segment = (UnknownSegment)segments.get( 0 );
      byte[] data = app14Segment.bytes;
      if ( data.length >= 12 && data[0] == 'A' && data[1] == 'd' && data[2] == 'o' && data[3] == 'b' && data[4] == 'e' )
      {
        hasAdobeMarker = true;
        int transform = app14Segment.bytes[11] & 0xff;
        if ( transform == 2 )
        {
          colorType = COLOR_TYPE_YCCK;
        }
      }
    }
  }

  public static void convertYcckToCmyk( WritableRaster raster )
  {
    int height = raster.getHeight();
    int width = raster.getWidth();
    int stride = width * 4;
    int[] pixelRow = new int[stride];
    for ( int h = 0; h < height; h++ )
    {
      raster.getPixels( 0, h, width, 1, pixelRow );

      for ( int x = 0; x < stride; x += 4 )
      {
        int y = pixelRow[x];
        int cb = pixelRow[x + 1];
        int cr = pixelRow[x + 2];

        int c = (int) ( y + 1.402 * cr - 178.956 );
        int m = (int) ( y - 0.34414 * cb - 0.71414 * cr + 135.95984 );
        y = (int) ( y + 1.772 * cb - 226.316 );

        if ( c < 0 )
        {
          c = 0;
        }
        else if ( c > 255 )
        {
          c = 255;
        }
        if ( m < 0 )
        {
          m = 0;
        }
        else if ( m > 255 )
        {
          m = 255;
        }
        if ( y < 0 )
        {
          y = 0;
        }
        else if ( y > 255 )
        {
          y = 255;
        }

        pixelRow[x] = 255 - c;
        pixelRow[x + 1] = 255 - m;
        pixelRow[x + 2] = 255 - y;
      }

      raster.setPixels( 0, h, width, 1, pixelRow );
    }
  }

  public static void convertInvertedColors( WritableRaster raster )
  {
    int height = raster.getHeight();
    int width = raster.getWidth();
    int stride = width * 4;
    int[] pixelRow = new int[stride];
    for ( int h = 0; h < height; h++ )
    {
      raster.getPixels( 0, h, width, 1, pixelRow );
      for ( int x = 0; x < stride; x++ )
      {
        pixelRow[x] = 255 - pixelRow[x];
      }
      raster.setPixels( 0, h, width, 1, pixelRow );
    }
  }

  public static String getFullImageUrlPath( String avatarUrl )
  {
    // Leveraging the profile service instead of cm3dam.
    if ( !StringUtil.isEmpty( avatarUrl ) && avatarUrl.contains( "biw.cloud/v1/images" ) )
    {
      return avatarUrl;
    }
    String fullImageUrlPath;
    String siteUrlPrefix = getSystemVariableService().getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();
    if ( !Environment.isCtech() )
    {
      siteUrlPrefix = getSystemVariableService().getPropertyByName( SystemVariableService.SITE_URL_PREFIX + "." + Environment.ENV_QA ).getStringVal();
    }
    if ( !StringUtil.isEmpty( avatarUrl ) )
    {
      if ( !getDefaultAvatarSmall().equalsIgnoreCase( avatarUrl ) )
      {
        if ( EnvironmentTypeEnum.isAws() )
        {
          siteUrlPrefix = buildAwsImagePrefix();
        }
        fullImageUrlPath = siteUrlPrefix + "-cm/cm3dam" + '/' + avatarUrl;
      }
      else
      {
        fullImageUrlPath = avatarUrl;
      }
    }
    else
    {
      fullImageUrlPath = null;
    }
    return fullImageUrlPath;
  }

  public static String buildAwsImagePrefix()
  {
    return getSystemVariableService().getContextName();
  }

  public static String getLargeAvatarIMageUrlPath( String avatarUrl )
  {
    return getFullImageUrlPath( avatarUrl, null );
  }

  public static String getFullImageUrlPath( String avatarUrl, String defaultAvatar )
  {
    String fullImageUrlPath;
    String siteUrlPrefix = getSystemVariableService().getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();
    if ( !Environment.isCtech() )
    {
      siteUrlPrefix = getSystemVariableService().getPropertyByName( SystemVariableService.SITE_URL_PREFIX + "." + Environment.ENV_QA ).getStringVal();
    }
    if ( !StringUtil.isEmpty( avatarUrl ) )
    {
      if ( EnvironmentTypeEnum.isAws() )
      {
        siteUrlPrefix = buildAwsImagePrefix();
      }
      fullImageUrlPath = siteUrlPrefix + "-cm/cm3dam" + '/' + avatarUrl;
    }
    else
    {
      fullImageUrlPath = defaultAvatar;
    }
    return fullImageUrlPath;
  }

  public static String getImageUploadPath()
  {
    String imageUrlPath;
    String siteUrlPrefix = getSystemVariableService().getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();
    if ( !Environment.isCtech() )
    {
      siteUrlPrefix = getSystemVariableService().getPropertyByName( SystemVariableService.SITE_URL_PREFIX + "." + Environment.ENV_QA ).getStringVal();
    }
    if ( EnvironmentTypeEnum.isAws() )
    {
      siteUrlPrefix = buildAwsImagePrefix();
    }
    imageUrlPath = siteUrlPrefix + "-cm/cm3dam" + '/';

    return imageUrlPath;
  }

  public static boolean isImageFormatValid( String contentType )
  {
    String imageExt = contentType;
    String[] imgNext = imageExt.split( "/" );
    boolean valid = false;
    if ( imgNext[1].equals( "gif" ) || imgNext[1].equals( "jpg" ) || imgNext[1].equals( "jpeg" ) || imgNext[1].equals( "x-png" ) || imgNext[1].equals( "png" ) || imgNext[1].equals( "pjpeg" ) )
    {
      valid = true;
    }

    return valid;
  }

  public static boolean isImageTypeValid( String extension )
  {
    if ( extension == null || extension.trim().length() == 0 )
    {
      return false;
    }
    boolean valid = false;
    if ( extension.equalsIgnoreCase( "gif" ) || extension.equalsIgnoreCase( "jpg" ) || extension.equalsIgnoreCase( "jpeg" ) || extension.equalsIgnoreCase( "x-png" )
        || extension.equalsIgnoreCase( "png" ) || extension.equalsIgnoreCase( "pjpeg" ) )
    {
      valid = true;
    }
    return valid;
  }

  /** Comma separated list of supported image file types */
  public static String getValidImageTypes()
  {
    return "jpg, gif, png";
  }

  public static boolean isImageSizeValid( int imageSize )
  {
    return isMediaFileSizeValid( SystemVariableService.SYSTEM_IMG_SIZE_LIMIT, imageSize );
  }

  public static boolean isVideoSizeValid( int size )
  {
    return isMediaFileSizeValid( SystemVariableService.SYSTEM_VIDEO_SIZE_LIMIT, size );
  }

  private static boolean isMediaFileSizeValid( String systemVariableKey, int fileSize )
  {
    boolean valid = false;
    int systemImageSizeLimitInMB = getSystemVariableService().getPropertyByName( systemVariableKey ).getIntVal();
    int systemImageSizeLimitInBytes = MEGABYTES_TO_BYTES_MULTIPLIER * systemImageSizeLimitInMB;

    if ( fileSize < systemImageSizeLimitInBytes )
    {
      valid = true;
    }

    return valid;
  }

  public static boolean isImageSize4x4( int imageSize, int height, int width )
  {
    boolean valid = false;

    if ( isImageSizeValid( imageSize ) && height == 628 && width == 628 )
    {
      valid = true;
    }

    return valid;
  }

  public static boolean isImageSize4x2( int imageSize, int height, int width )
  {
    boolean valid = false;

    if ( isImageSizeValid( imageSize ) && height == 308 && width == 628 )
    {
      valid = true;
    }

    return valid;
  }

  public static boolean isImageSize2x2( int imageSize, int height, int width )
  {
    boolean valid = false;

    if ( isImageSizeValid( imageSize ) && height == 308 && width == 308 )
    {
      valid = true;
    }

    return valid;
  }

  public static boolean isImageSize2x1( int imageSize, int height, int width )
  {
    boolean valid = false;

    if ( isImageSizeValid( imageSize ) && height == 148 && width == 308 )
    {
      valid = true;
    }

    return valid;
  }

  public static boolean isImageSize1x1( int imageSize, int height, int width )
  {
    boolean valid = false;

    if ( isImageSizeValid( imageSize ) && height == 162 && width == 162 )
    {
      valid = true;
    }

    return valid;
  }

  public static int getImageSizeLimit()
  {
    return getSystemVariableService().getPropertyByName( SystemVariableService.SYSTEM_IMG_SIZE_LIMIT ).getIntVal();
  }

  public static String getImageSize()
  {
    return getSystemVariableService().getPropertyByName( SystemVariableService.SYSTEM_IMG_SIZE_LIMIT ).getIntVal() + "MB.";
  }

  public static int getVideoSizeLimit()
  {
    return getSystemVariableService().getPropertyByName( SystemVariableService.SYSTEM_VIDEO_SIZE_LIMIT ).getIntVal();
  }

  public static String getVideoSize()
  {
    return getSystemVariableService().getPropertyByName( SystemVariableService.SYSTEM_VIDEO_SIZE_LIMIT ).getIntVal() + "MB.";
  }

  public static String getDefaultAvatarPath()
  {
    return getSystemVariableService().getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal() + "/assets/skins/" + getDesignTheme() + "/img/";
  }

  public static String getAvatarInitials( Participant user )
  {
    String fc = user.getFirstName() != null ? user.getFirstName().substring( 0, 1 ) : "";
    String lc = user.getLastName() != null ? user.getLastName().substring( 0, 1 ) : "";

    return fc.concat( lc );
  }

  public static String getDefaultAvatarTiny()
  {
    return getDefaultAvatarPath() + AVATAR_IMAGE_TINY;
  }

  public static String getDefaultAvatarSmall()
  {
    return getDefaultAvatarPath() + AVATAR_IMAGE_SMALL;
  }

  public static String getDefaultAvatarLarge()
  {
    return getDefaultAvatarPath() + AVATAR_IMAGE_LARGE;
  }

  public static String getDefaultMultiAvatarSmall()
  {
    return getDefaultAvatarPath() + AVATAR_MULTI_IMAGE_SMALL;
  }

  public static String getDefaultShadowAvatarTiny()
  {
    return getDefaultAvatarPath() + "throwdown/" + SHADOW_AVATAR_IMAGE_TINY;
  }

  public static String getDefaultShadowAvatarSmall()
  {
    return getDefaultAvatarPath() + "throwdown/" + SHADOW_AVATAR_IMAGE_SMALL;
  }

  public static String getDefaultShadowAvatarLarge()
  {
    return getDefaultAvatarPath() + "throwdown/" + SHADOW_AVATAR_IMAGE_LARGE;
  }

  public BufferedImage convertCmykToRgb( Raster cmykRaster, ICC_Profile cmykProfile ) throws IOException
  {
    BufferedImage rgbImage = null;
    if ( cmykProfile == null )
    {
      InputStream inputStream = null;
      try
      {
        // ClassLoader loader = Thread.currentThread().getContextClassLoader();
        // URL url = loader.getResource("USWebCoatedSWOP.pf" );

        // InputStream is = url.openStream();
        /*
         * String iccprofilePath=System.getProperty("java.iccprofile.path"); File afile =new
         * File(iccprofilePath+"/USWebCoatedSWOP.pf"); String
         * appdataDir=System.getProperty("appdatadir"); System.setProperty(
         * "java.iccprofile.path",appdataDir );
         * iccprofilePath=System.getProperty("java.iccprofile.path"); afile.renameTo( new
         * File(iccprofilePath+"/USWebCoatedSWOP.pf") );
         */
        // InputStream inputStream =new
        // BufferedInputStream(Thread.currentThread().getContextClassLoader().getResourceAsStream("USWebCoatedSWOP.pf"));
        inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream( "USWebCoatedSWOP.icc" );
        cmykProfile = ICC_Profile.getInstance( inputStream );
        // cmykProfile = ICC_Profile.getInstance("USWebCoatedSWOP.pf");
        // cmykProfile =
        // ICC_Profile.getInstance(ImageUtils.class.getResourceAsStream("/USWebCoatedSWOP.icc"));
        // InputStream propStream =
        // ClassLoader.getSystemClassLoader().getResourceAsStream("USWebCoatedSWOP.properties");
        // InputStream propStream =getClass().getResourceAsStream("USWebCoatedSWOP.properties");
        // InputStream propStream
        // =Thread.currentThread().getContextClassLoader().getResourceAsStream("USWebCoatedSWOP.icc");
        // cmykProfile = ICC_Profile.getInstance(propStream);
        // cmykProfile =
        // ICC_Profile.getInstance(ImageUtils.class.getResourceAsStream("/USWebCoatedSWOP.properties"));

        // cmykProfile =
        // ICC_Profile.getInstance(ClassLoader.getSystemResourceAsStream("USWebCoatedSWOP.properties"));
        // Resource resource = new ClassPathResource("/USWebCoatedSWOP.icc");
        // cmykProfile = ICC_Profile.getInstance(iccResource.getInputStream());

        // cmykProfile =
        // ICC_Profile.getInstance(getClass().getClassLoader().getResourceAsStream("USWebCoatedSWOP.icc"));
      }
      catch( Exception e )
      {
        logger.error( e );
      }
      finally
      {
        try
        {
          if ( inputStream != null )
          {
            inputStream.close();
          }
        }
        catch( IOException e )
        {
          logger.error( e.getMessage(), e );
        }

      }
    }
    if ( cmykProfile != null )
    {
      if ( cmykProfile.getProfileClass() != ICC_Profile.CLASS_DISPLAY )
      {
        byte[] profileData = cmykProfile.getData();

        if ( profileData[ICC_Profile.icHdrRenderingIntent] == ICC_Profile.icPerceptual )
        {
          intToBigEndian( ICC_Profile.icSigDisplayClass, profileData, ICC_Profile.icHdrDeviceClass ); // Header
                                                                                                      // is
                                                                                                      // first

          cmykProfile = ICC_Profile.getInstance( profileData );
        }
      }

      ICC_ColorSpace cmykCS = new ICC_ColorSpace( cmykProfile );
      rgbImage = new BufferedImage( cmykRaster.getWidth(), cmykRaster.getHeight(), BufferedImage.TYPE_INT_RGB );
      WritableRaster rgbRaster = rgbImage.getRaster();
      ColorSpace rgbCS = rgbImage.getColorModel().getColorSpace();
      ColorConvertOp cmykToRgb = new ColorConvertOp( cmykCS, rgbCS, null );
      cmykToRgb.filter( cmykRaster, rgbRaster );
      return rgbImage;
    }
    return null;
  }

  static void intToBigEndian( int value, byte[] array, int index )
  {
    array[index] = (byte) ( value >> 24 );
    array[index + 1] = (byte) ( value >> 16 );
    array[index + 2] = (byte) ( value >> 8 );
    array[index + 3] = (byte)value;
  }

  public static AffineTransform getExifTransformation( int orientation, BufferedImage info )
  {

    AffineTransform t = new AffineTransform();

    switch ( orientation )
    {
      case 1:
        break;
      case 2: // Flip X
        t.scale( -1.0, 1.0 );
        t.translate( -info.getWidth(), 0 );
        break;
      case 3: // PI rotation
        t.translate( info.getWidth(), info.getHeight() );
        t.rotate( Math.PI );
        break;
      case 4: // Flip Y
        t.scale( 1.0, -1.0 );
        t.translate( 0, -info.getHeight() );
        break;
      case 5: // - PI/2 and Flip X
        t.rotate( -Math.PI / 2 );
        t.scale( -1.0, 1.0 );
        break;
      case 6: // -PI/2 and -width
        t.translate( info.getHeight(), 0 );
        t.rotate( Math.PI / 2 );
        break;
      case 7: // PI/2 and Flip
        t.scale( -1.0, 1.0 );
        t.translate( -info.getHeight(), 0 );
        t.translate( 0, info.getWidth() );
        t.rotate( 3 * Math.PI / 2 );
        break;
      case 8: // PI / 2
        t.translate( 0, info.getWidth() );
        t.rotate( 3 * Math.PI / 2 );
        break;
      default:
        break;
    }

    return t;
  }

  private static SystemVariableService getSystemVariableService()
  {
    return (SystemVariableService)getService( SystemVariableService.BEAN_NAME );
  }

  protected static SAO getService( String beanName )
  {
    return (SAO)BeanLocator.getBean( beanName );
  }

  protected static String getDesignTheme()
  {
    return getDesignThemeService().getDesignTheme( UserManager.getUser() );
  }

  protected static DesignThemeService getDesignThemeService()
  {
    return (DesignThemeService)getService( DesignThemeService.BEAN_NAME );
  }

}
