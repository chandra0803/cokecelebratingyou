/*
 * (c) 2019 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.process;

import static com.biperf.core.utils.Environment.getEnvironment;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.UUID;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.biperf.core.domain.promotion.RecognitionPromotion;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.imageservice.v1.ImageUploadRequest;
import com.biperf.core.service.imageservice.v1.ImageUrlResult;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.strategy.AmazonClientFactory;
import com.biperf.core.utils.AwsUtils;
import com.biperf.core.utils.ImageUtils;
import com.biperf.core.utils.MeshServicesUtil;
import com.biperf.core.value.participant.PromoRecPicUpdatedData;
import com.biperf.core.value.participant.PromoRecPictureData;
import com.biperf.core.vo.ids.IDServiceResVO;
import com.objectpartners.cms.domain.Content;
import com.objectpartners.cms.domain.Translations;
import com.objectpartners.cms.service.ContentReader;
import com.objectpartners.cms.util.ContentReaderManager;

public class PromoRecognitionPictureMigrationProcess extends BaseProcessImpl
{
  private static final Log log = LogFactory.getLog( PromoRecognitionPictureMigrationProcess.class );

  public static final String PROCESS_NAME = "Promo Recognition Picture Migration Process";
  public static final String BEAN_NAME = "promoRecognitionPictureMigrationProcess";

  private Properties webDavProperties;
  private @Autowired AmazonClientFactory amazonClientFactory;
  private @Autowired PromotionService promotionService;

  private String companyId = null;
  private String meshSecretKey = null;
  private String meshClientId = null;
  private String companyIdentifier = null;
  private String meshBaseUrl = null;
  private String onPremHostBaseUrl = null;
  private boolean isAwsClient;
  private String awsHostBaseUrl = null;
  private String awsS3BucketName = null;

  private Long initialCount = 0L;
  private Long finalCount = 0L;

  private ContentReader contentReader = ContentReaderManager.getContentReader();

  @Override
  protected void onExecute()
  {
    try
    {
      companyId = MeshServicesUtil.getCompanyId();
    }
    catch( Exception exc )
    {
      addComment( " Company set up is invalid or improperly defined. Consequently, the process could not be completed successfully. Check the Application logs for more details." );
      log.error( "Exception while getting CompanyId : ", exc );
      return;
    }

    meshSecretKey = getSystemVariableService().getPropertyByNameAndEnvironment( SystemVariableService.NACKLE_MESH_SERVICES_SECRET_KEY ).getStringVal();
    meshClientId = getSystemVariableService().getPropertyByNameAndEnvironment( SystemVariableService.NACKLE_MESH_SERVICES_CLIENT_ID ).getStringVal();

    companyIdentifier = getSystemVariableService().getContextName();
    meshBaseUrl = getSystemVariableService().getPropertyByNameAndEnvironment( SystemVariableService.NACKLE_MESH_SERVICES_HOST_BASE_URL ).getStringVal();
    onPremHostBaseUrl = getOnPremHostBaseUrl();

    isAwsClient = AwsUtils.isAws();
    awsHostBaseUrl = getAwsHostBaseUrl();
    awsS3BucketName = getAwsS3BucketName();

    if ( StringUtils.isNotEmpty( companyId ) && ( StringUtils.isNotEmpty( meshSecretKey ) && !meshSecretKey.equals( "CHANGE ME!" ) )
        && ( StringUtils.isNotEmpty( meshClientId ) && !meshClientId.equals( "CHANGE ME!" ) ) )
    {
      try
      {
        List<PromoRecPictureData> promoRecPictureData = promotionService.getNotMigratedPromRecogPictureData();
        initialCount = (long)promoRecPictureData.size();

        if ( !CollectionUtils.isEmpty( promoRecPictureData ) )
        {
          promoRecPictureData.stream().forEach( inputObj ->
          {
            PromoRecPicUpdatedData mgtData = null;
            String urlPath = null;

            try
            {
              urlPath = getfilePathFromCM( inputObj.getContentResource() );

              if ( StringUtils.isNotEmpty( urlPath ) )
              {
                if ( urlPath.contains( "biw.cloud/v1" ) )
                {
                  promotionService.updateContResPic( inputObj.getPromotionId() );
                }
                else
                {
                  inputObj.setPictureUrl( urlPath );
                  mgtData = getMigratedUrlPath( inputObj );

                  if ( Objects.nonNull( mgtData.getPromotionId() ) && StringUtils.isNotEmpty( mgtData.getMigratedUrl() ) )
                  {
                    RecognitionPromotion reconitionPromotion = (RecognitionPromotion)promotionService.getPromotionById( mgtData.getPromotionId() );
                    String mediaHtmlString = getHtmlImageString( mgtData.getMigratedUrl() );
                    try
                    {
                      reconitionPromotion = promotionService.savePurlStandardMessageImage( reconitionPromotion, mediaHtmlString, null, mgtData.getMigratedUrl() );
                      if ( null != reconitionPromotion.getContentResourceCMCode() )
                      {
                        promotionService.updateContResPic( reconitionPromotion.getId() );
                      }

                    }
                    catch( ServiceErrorException e )
                    {
                      log.error( "While updating the image, content resource : " + inputObj.getContentResource() + "  \n" + e.getMessage(), e );
                    }
                  }
                }
              }
            }
            catch( Exception ex )
            {
              log.error( "Content resource : " + inputObj.getContentResource() );
              log.error( "While processing the image : " + ex.getMessage(), ex );
            }
          } );

          finalCount = (long)promotionService.getNotMigratedPromRecogPictureData().size();
          Long totalCount = initialCount - finalCount;

          if ( initialCount == finalCount )
          {
            addComment( " Eligible pictures for Migration : " + initialCount );
            addComment( " Your request failed. Check the Application logs for more details." );
          }
          else if ( finalCount != 0L )
          {
            addComment( " Eligible pictures for Migration : " + initialCount );
            addComment( " Your request was processed. " + totalCount + " Pictures migrated successfully while " + finalCount
                + " Pictures failed migration. Check the Application logs for more details." );
          }
          else
          {
            addComment( " Eligible pictures for Migration : " + initialCount );
            addComment( " Your request was processed successfully. " + initialCount + " Pictures were migrated effectively." );
          }

        }
        else
        {
          addComment( " Your request was processed successfully. However did not find any picture to migrate. Check the IS_CONT_RES_MIGRATED column in PROMO_RECOGNITION table." );
        }

      }
      catch( Exception exc )
      {
        log.error( "Promo Recognition Picture Migration Process : " + exc.getMessage(), exc );
      }

    }
    else
    {
      addComment( " The authorization credentials provided for Kong are invalid. Check the value of the credentials as defined in System Variables(nackle.mesh.services.client.id.<env> & nackle.mesh.services.secret.key.<env>)." );
      return;
    }

  }

  private PromoRecPicUpdatedData getMigratedUrlPath( PromoRecPictureData promoRecPictureData )
  {
    String contentResBase64 = null;
    String migratedUrl = null;
    PromoRecPicUpdatedData opData = new PromoRecPicUpdatedData();

    try
    {
      if ( !promoRecPictureData.getPictureUrl().contains( "biw.cloud/v1" ) )
      {
        if ( isAwsClient )
        {
          contentResBase64 = getAwsFileData( promoRecPictureData.getPictureUrl(), promoRecPictureData.getPromotionId() );
        }
        else
        {
          contentResBase64 = getOnPremImageData( promoRecPictureData.getPictureUrl(), promoRecPictureData.getPromotionId() );
        }
      }

      if ( StringUtils.isNotEmpty( contentResBase64 ) )
      {
        String imgContentType = getContentType( promoRecPictureData.getPictureUrl() );
        String upLoadData = getImageDataInUploadFormat( imgContentType, contentResBase64 );

        String kongToken = getKongToken( promoRecPictureData.getPromotionId() );

        if ( StringUtils.isNotEmpty( kongToken ) )
        {
          migratedUrl = uploadImage( upLoadData, UUID.randomUUID().toString(), kongToken );
        }
      }

      if ( StringUtils.isNotEmpty( migratedUrl ) )
      {
        opData.setPromotionId( promoRecPictureData.getPromotionId() );
        opData.setMigratedUrl( migratedUrl );
        opData.setContentResource( promoRecPictureData.getContentResource() );
      }

    }
    catch( Exception exc )
    {
      log.error( exc );
    }
    return opData;

  }

  private String uploadImage( String imageData, String id, String kongToken )
  {
    ImageUrlResult urlResult = null;

    HttpHeaders headers = new HttpHeaders();
    headers.add( "authorization", "Bearer " + kongToken );

    ImageUploadRequest request = buildImageUploadRequest( imageData, id );

    try
    {
      urlResult = MeshServicesUtil.getRestWebClient().postForObject( meshBaseUrl + "/images", new HttpEntity<ImageUploadRequest>( request, headers ), ImageUrlResult.class );

      return urlResult.getProxyUrl();

    }
    catch( Exception e )
    {
      log.error( "Message from image service", e );
    }

    return null;
  }

  public String getHtmlImageString( String url )
  {
    StringBuilder htmlImageString = new StringBuilder();
    htmlImageString.append( "<p><img src=\"" + url + "\" alt=\"Photo\" class=\"thumb\"/></p>" );

    return htmlImageString.toString();
  }

  private ImageUploadRequest buildImageUploadRequest( String imageData, String id )
  {
    ImageUploadRequest request = new ImageUploadRequest();
    request.setImage( imageData );
    request.setName( "prorecpic" + "_" + System.currentTimeMillis() + "_" + UUID.randomUUID().toString() + "-" + RandomStringUtils.randomAlphanumeric( 3 ) );
    request.setPublic( true );
    return request;
  }

  private String getImageDataInUploadFormat( String contentType, String imageInBytes )
  {
    StringBuilder imageDataSb = new StringBuilder();
    imageDataSb.append( "data:" );
    imageDataSb.append( contentType );
    imageDataSb.append( ";base64," );
    imageDataSb.append( imageInBytes );

    return imageDataSb.toString();

  }

  private String getKongToken( Long promotionId )
  {
    String opKongToken = null;

    MultiValueMap<String, String> parameters = new LinkedMultiValueMap<String, String>();
    parameters.add( "client_id", meshClientId );
    parameters.add( "client_secret", meshSecretKey );
    parameters.add( "company_identifier", companyIdentifier );
    parameters.add( "grant_type", "daymaker" );
    parameters.add( "person_id", UUID.randomUUID().toString() );

    HttpHeaders headers = new HttpHeaders();
    headers.add( "content-type", MediaType.APPLICATION_FORM_URLENCODED_VALUE );

    HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>( parameters, headers );

    ResponseEntity<IDServiceResVO> idServiceResVO = null;

    try
    {
      idServiceResVO = MeshServicesUtil.getRestWebClient().postForEntity( meshBaseUrl + "/id/token", requestEntity, IDServiceResVO.class );

      if ( idServiceResVO.getStatusCode() == HttpStatus.OK && Objects.nonNull( idServiceResVO.getBody().getAccessToken() ) )
      {
        opKongToken = idServiceResVO.getBody().getAccessToken();
      }
      else
      {
        log.error( " :: Oops !!!. ID Service not retuning Kong token to authenticate Image Service. Response Code : " + idServiceResVO.getStatusCode() );
      }
    }
    catch( Exception resException )
    {
      log.error( " :: ID Service not retuning Kong token to authenticate Image Service." );

      try
      {
        log.error( " :: HTTP Response code :: " + idServiceResVO.getStatusCode() + " & body from ID Service : " + idServiceResVO.getBody().toString() );
      }
      catch( Exception ex )
      {
      }

      log.error( " :: Exception while getting token from ID service : ", resException );
    }

    return opKongToken;

  }

  private String getContentType( String imageName )
  {
    String contentType = null;

    if ( StringUtils.isNotEmpty( imageName ) )
    {
      String[] imgName = imageName.split( "\\." );
      contentType = "image/" + imgName[imgName.length - 1];
    }
    return contentType;
  }

  private String getOnPremImageData( String filePath, Long promotionId )
  {
    final String PROTOCOL_HTTP = "http";
    final String PROTOCOL_HTTPS = "https";

    String urlLocation = null;
    DataInputStream reader = null;
    InputStream iStream = null;
    HttpURLConnection conn = null;
    byte[] opData = null;
    String picBase64Value = null;

    try
    {
      urlLocation = onPremHostBaseUrl + ImageUtils.convertToUrlPath( filePath );

      if ( urlLocation.startsWith( PROTOCOL_HTTPS ) )
      {
        urlLocation = urlLocation.replace( PROTOCOL_HTTPS, PROTOCOL_HTTP );
      }

      URL url = new URL( urlLocation );
      conn = (HttpURLConnection)url.openConnection();
      reader = new DataInputStream( conn.getInputStream() );

      opData = IOUtils.toByteArray( reader );

    }
    catch( IOException e )
    {
      log.error( " ====>>>  Picture Is Missing For The Promotion ID   : " + promotionId + " . The Content Resource Picture Url Path Is Exist In DB ( PROMO_RECOGNITION table & CM Assets ) : "
          + filePath + " ,But Not Exist In Webdav : " + urlLocation );

      log.error( "Failed to invoke URL ", e );
    }
    finally
    {
      if ( conn != null )
      {
        conn.disconnect();
      }
      try
      {
        if ( null != iStream )
        {
          iStream.close();
        }
      }
      catch( Throwable t )
      {
      }
      try
      {
        if ( null != reader )
        {
          reader.close();
        }
      }
      catch( Throwable t )
      {
      }
    }

    if ( Objects.nonNull( opData ) )
    {
      picBase64Value = Base64.getEncoder().encodeToString( opData );
    }

    return picBase64Value;

  }

  private String getAwsFileData( String filePath, Long promotionId )
  {
    AmazonS3 s3client = null;
    String urlKey = null;
    byte[] data = null;
    String picBase64Value = null;

    try
    {
      s3client = amazonClientFactory.getClient();
      urlKey = awsHostBaseUrl + ImageUtils.convertToUrlPath( filePath );
      S3Object object = s3client.getObject( new GetObjectRequest( awsS3BucketName, urlKey ) );
      data = new byte[(int)object.getObjectMetadata().getContentLength()];
      DataInputStream dataIs = new DataInputStream( object.getObjectContent() );
      dataIs.readFully( data );
      dataIs.close();
      object.close();

    }
    catch( AmazonServiceException ase )
    {
      if ( ase.getStatusCode() == 404 && ase.getErrorCode().contains( "NoSuchKey" ) )
      {
        log.error( " ====>>>  Picture Is Missing For The Promotion ID   : " + promotionId + " . The Content Resource Picture Url Path Is Exist In DB ( PROMO_RECOGNITION table & CM Assets ) : "
            + filePath + " ,But Not Exist In AWS S3 Bucket (Name) : " + awsS3BucketName + " , The S3 Url Path Is : " + urlKey );
      }

      String msg = buildAmazonLog( ase );
      log.error( msg, ase );
    }
    catch( Throwable t )
    {
      log.error( t.getMessage(), t );
    }
    finally
    {
      if ( s3client != null )
      {
        s3client.shutdown();
      }
    }

    if ( Objects.nonNull( data ) )
    {
      picBase64Value = Base64.getEncoder().encodeToString( data );
    }

    return picBase64Value;
  }

  private String buildAmazonLog( AmazonServiceException ase )
  {
    StringBuilder sb = new StringBuilder();
    sb.append( "Caught an AmazonServiceException, which means your request made it to Amazon S3, but was rejected with an error response for some reason. " + ase.getMessage() );
    sb.append( "\nError Message:    " + ase.getMessage() );
    sb.append( "\nHTTP Status Code: " + ase.getStatusCode() );
    sb.append( "\nAWS Error Code:   " + ase.getErrorCode() );
    sb.append( "\nError Type:       " + ase.getErrorType() );
    sb.append( "\nRequest ID:       " + ase.getRequestId() );
    return sb.toString();
  }

  private String getfilePathFromCM( String code )
  {
    String urlPath = null;

    try
    {
      List contentList = new ArrayList();
      Content content = null;

      if ( contentReader.getContent( code, Locale.ENGLISH ) instanceof java.util.List )
      {
        contentList = (List)contentReader.getContent( code, Locale.ENGLISH );
        content = (Content)contentList.get( 0 );
      }
      else
      {
        content = (Content)contentReader.getContent( code, Locale.ENGLISH );
      }

      Map opMap = content.getContentDataMapList();
      Translations leftObject = (Translations)opMap.get( "LEFT_COLUMN" );
      Translations filePathObject = (Translations)opMap.get( "FILE_PATH" );

      if ( filePathObject != null )
      {
        urlPath = filePathObject.getValue();
      }
      else if ( leftObject != null )
      {
        if ( leftObject.getValue().contains( "<p>" ) )
        {
          String leftColumn = leftObject.getValue();
          String s = "<img src=\"";
          int ix = leftColumn.indexOf( s ) + s.length();
          urlPath = leftColumn.substring( ix, leftColumn.indexOf( "\"", ix + 1 ) );
        }
      }
      else
      {
        log.error( "File Path not exist in CM assets for the asset code :" + code + ". Please check CM assets for more information." );
      }

    }
    catch( Exception exc )
    {
      log.error( " Exception while getting picture path from CM : " + code, exc );
    }

    return urlPath;

  }

  private String getAwsHostBaseUrl()
  {
    return getSystemVariableService().getContextName() + "-cm/cm3dam/";
  }

  private String getAwsS3BucketName()
  {
    return getSystemVariableService().getContextName() + "-" + getEnvironment() + "-user-content";
  }

  private String getOnPremHostBaseUrl()
  {
    return getWebDavProperty( "webDavHost" ) + getWebDavProperty( "collectionName" );
  }

  private String getWebDavProperty( String propertyName )
  {
    return webDavProperties.getProperty( propertyName );
  }

  public void setWebDavProperties( Properties webDavProperties )
  {
    this.webDavProperties = webDavProperties;
  }

}
