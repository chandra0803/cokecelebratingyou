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
import java.util.Objects;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

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
import com.biperf.core.service.imageservice.v1.ImageUploadRequest;
import com.biperf.core.service.imageservice.v1.ImageUrlResult;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.strategy.AmazonClientFactory;
import com.biperf.core.ui.utils.RosterUtil;
import com.biperf.core.utils.AwsUtils;
import com.biperf.core.utils.ImageUtils;
import com.biperf.core.utils.MeshServicesUtil;
import com.biperf.core.value.participant.PromRecImgSavedData;
import com.biperf.core.value.participant.PromoRecImageData;
import com.biperf.core.vo.ids.IDServiceResVO;

public class PromoRecognitionAvatarMigrationProcess extends BaseProcessImpl
{

  private static final Log log = LogFactory.getLog( PromoRecognitionAvatarMigrationProcess.class );

  public static final String PROCESS_NAME = "Promo Recognition Avatar Migration Process";
  public static final String BEAN_NAME = "promoRecognitionAvatarMigrationProcess";

  private @Autowired PromotionService promotionService;
  private @Autowired AmazonClientFactory amazonClientFactory;

  private Properties webDavProperties;

  @Override
  protected void onExecute()
  {
    String companyId = null;

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

    String meshSecretKey = getSystemVariableService().getPropertyByNameAndEnvironment( SystemVariableService.NACKLE_MESH_SERVICES_SECRET_KEY ).getStringVal();
    String meshClientId = getSystemVariableService().getPropertyByNameAndEnvironment( SystemVariableService.NACKLE_MESH_SERVICES_CLIENT_ID ).getStringVal();

    String companyIdentifier = getSystemVariableService().getContextName();
    String meshBaseUrl = getSystemVariableService().getPropertyByNameAndEnvironment( SystemVariableService.NACKLE_MESH_SERVICES_HOST_BASE_URL ).getStringVal();
    String onPremHostBaseUrl = getOnPremHostBaseUrl();

    boolean isAwsClient = AwsUtils.isAws();
    String awsHostBaseUrl = getAwsHostBaseUrl();
    String awsS3BucketName = getAwsS3BucketName();

    if ( StringUtils.isNotEmpty( companyId ) && ( StringUtils.isNotEmpty( meshSecretKey ) && !meshSecretKey.equals( "CHANGE ME!" ) )
        && ( StringUtils.isNotEmpty( meshClientId ) && !meshClientId.equals( "CHANGE ME!" ) ) )
    {
      Long initialCount = 0L;

      try
      {
        List<Future<PromRecImgSavedData>> futureList = new ArrayList<Future<PromRecImgSavedData>>();
        List<PromoRecImageData> promoRecImgList = promotionService.getNotMigratedPromRecogAvatarData();

        if ( !CollectionUtils.isEmpty( promoRecImgList ) )
        {
          ExecutorService executor = Executors.newFixedThreadPool( 70 );

          initialCount = (long)promoRecImgList.size();

          promoRecImgList.stream().forEach( inputObj ->
          {
            inputObj.setOnPremHostBaseUrl( onPremHostBaseUrl );
            /*
             * The below random UUID used only to get participant Kong token from ID service in
             * order to get migrate the recognition avatar to image service.
             */
            inputObj.setRosterId( RosterUtil.getRandomUUId().toString() );
            inputObj.setCompanyIdentifier( companyIdentifier );
            inputObj.setMeshBaseUrl( meshBaseUrl );
            inputObj.setMeshSecretKey( meshSecretKey );
            inputObj.setMeshClientId( meshClientId );
            inputObj.setAws( isAwsClient );
            inputObj.setAwsS3BucketName( awsS3BucketName );
            inputObj.setAwsHostBaseUrl( awsHostBaseUrl );

            Callable<PromRecImgSavedData> callable = new CallableMigrationTask( inputObj );
            Future<PromRecImgSavedData> future = executor.submit( callable );
            futureList.add( future );

          } );

          futureList.stream().forEach( futureObj ->
          {
            try
            {
              if ( futureObj.get() != null )
              {
                PromRecImgSavedData promRecImgSavedData = (PromRecImgSavedData)futureObj.get();

                if ( Objects.nonNull( promRecImgSavedData.getPromotionId() ) && StringUtils.isNotEmpty( promRecImgSavedData.getDefCeleAvatarUrl() )
                    && StringUtils.isNotEmpty( promRecImgSavedData.getDefContAvatarUrl() ) )
                {
                  promotionService.updatePromRecAvatar( promRecImgSavedData.getPromotionId(), promRecImgSavedData.getDefCeleAvatarUrl(), promRecImgSavedData.getDefContAvatarUrl() );
                }
                else if ( Objects.nonNull( promRecImgSavedData.getPromotionId() ) && StringUtils.isNotEmpty( promRecImgSavedData.getDefCeleAvatarUrl() ) )
                {
                  promotionService.updatePromRecCeleAvatar( promRecImgSavedData.getPromotionId(), promRecImgSavedData.getDefCeleAvatarUrl() );
                }
                else if ( Objects.nonNull( promRecImgSavedData.getPromotionId() ) && StringUtils.isNotEmpty( promRecImgSavedData.getDefContAvatarUrl() ) )
                {
                  promotionService.updatePromRecContrAvatar( promRecImgSavedData.getPromotionId(), promRecImgSavedData.getDefContAvatarUrl() );
                }
              }
            }
            catch( InterruptedException | ExecutionException e )
            {
              try
              {
                log.error( "Getting future Object : " + e.getMessage() + "The future obj value : " + futureObj.get().toString() );
              }
              catch( Exception e1 )
              {
                log.error( "Exception future object : ", e1 );
              }

            }
          } );

          executor.shutdown();

        }
        else
        {
          addComment( " Your request was processed successfully. However did not find any Avatar to migrate. Check the avatar URL in PROMO_RECOGNITION( DEFAULT_CONTRIBUTOR_AVATAR, DEFAULT_CELEBRATION_AVATAR ) table." );
          return;
        }
      }
      catch( Exception exc )
      {
        log.error( "Promo Recognition Avatar Migration Process : " + exc.getMessage(), exc );
      }

      Long finalCount = (long)promotionService.getNotMigratedPromRecogAvatarData().size();
      Long totalCount = initialCount - finalCount;

      if ( initialCount == finalCount )
      {
        addComment( " Eligible avatars for Migration : " + initialCount );
        addComment( " Your request failed. Check the Application logs for more details." );
      }
      else if ( finalCount != 0L )
      {
        addComment( " Eligible avatars for Migration : " + initialCount );
        addComment( " Your request was processed. " + totalCount + " Recognition Avatars migrated successfully while " + finalCount
            + " avatars failed migration. Check the Application logs for more details." );
      }
      else
      {
        addComment( " Eligible avatars for Migration : " + initialCount );
        addComment( " Your request was processed successfully. " + initialCount + " Recognition Avatars were migrated effectively." );
      }
    }
    else
    {
      addComment( " The authorization credentials provided for Kong are invalid. Check the value of the credentials as defined in System Variables(nackle.mesh.services.client.id.<env> & nackle.mesh.services.secret.key.<env>)." );
    }

  }

  class CallableMigrationTask implements Callable<PromRecImgSavedData>
  {
    private Long promotionID;
    private String defaultContributorAvatar;
    private String defaultCelebrationAvatar;
    private String onPremHostBaseUrl;
    private String rosterId;
    private String meshBaseUrl;
    private String companyIdentifier;
    private String meshClientId;
    private String meshSecretKey;
    private boolean isAwsClient;
    private String awsHostBaseUrl;
    private String awsS3BucketName;

    public CallableMigrationTask( PromoRecImageData promoRecImageData )
    {
      promotionID = promoRecImageData.getPromotionId();
      defaultContributorAvatar = promoRecImageData.getDefaultContributorAvatar();
      defaultCelebrationAvatar = promoRecImageData.getDefaultCelebrationAvatar();
      onPremHostBaseUrl = promoRecImageData.getOnPremHostBaseUrl();
      rosterId = promoRecImageData.getRosterId();
      meshBaseUrl = promoRecImageData.getMeshBaseUrl();
      companyIdentifier = promoRecImageData.getCompanyIdentifier();
      meshClientId = promoRecImageData.getMeshClientId();
      meshSecretKey = promoRecImageData.getMeshSecretKey();
      isAwsClient = promoRecImageData.isAws();
      awsHostBaseUrl = promoRecImageData.getAwsHostBaseUrl();
      awsS3BucketName = promoRecImageData.getAwsS3BucketName();
    }

    @Override
    public PromRecImgSavedData call() throws Exception
    {
      PromRecImgSavedData returnObj = new PromRecImgSavedData();
      returnObj.setPromotionId( promotionID );

      String defContAvatar64 = null;
      String defCeleAvatar64 = null;

      try
      {
        if ( isAwsClient )
        {
          if ( StringUtils.isNotEmpty( defaultContributorAvatar ) && !defaultContributorAvatar.contains( "biw.cloud/v1" ) )
          {
            defContAvatar64 = getAwsImageData( defaultContributorAvatar, awsHostBaseUrl, awsS3BucketName, promotionID );
          }

          if ( StringUtils.isNotEmpty( defaultCelebrationAvatar ) && !defaultCelebrationAvatar.contains( "biw.cloud/v1" ) )
          {
            defCeleAvatar64 = getAwsImageData( defaultCelebrationAvatar, awsHostBaseUrl, awsS3BucketName, promotionID );
          }
        }
        else
        {
          if ( StringUtils.isNotEmpty( defaultContributorAvatar ) && !defaultContributorAvatar.contains( "biw.cloud/v1" ) )
          {
            defContAvatar64 = getImageData( defaultContributorAvatar, onPremHostBaseUrl, promotionID );
          }

          if ( StringUtils.isNotEmpty( defaultCelebrationAvatar ) && !defaultCelebrationAvatar.contains( "biw.cloud/v1" ) )
          {
            defCeleAvatar64 = getImageData( defaultCelebrationAvatar, onPremHostBaseUrl, promotionID );
          }
        }

        if ( StringUtils.isNotEmpty( defContAvatar64 ) )
        {
          String imgContentType = getContentType( defaultContributorAvatar );

          defContAvatar64 = getImageDataInUploadFormat( imgContentType, defContAvatar64 );

          String kongToken = getKongToken( meshClientId, meshSecretKey, companyIdentifier, rosterId, meshBaseUrl );

          if ( StringUtils.isNotEmpty( defContAvatar64 ) && StringUtils.isNotEmpty( kongToken ) )
          {
            String defContAvatarUrl = uploadImage( defContAvatar64, kongToken, meshBaseUrl );
            returnObj.setDefContAvatarUrl( defContAvatarUrl );
          }

        }

        if ( StringUtils.isNotEmpty( defCeleAvatar64 ) )
        {
          String imgContentType = getContentType( defaultCelebrationAvatar );

          defCeleAvatar64 = getImageDataInUploadFormat( imgContentType, defCeleAvatar64 );

          String kongToken = getKongToken( meshClientId, meshSecretKey, companyIdentifier, rosterId, meshBaseUrl );

          if ( StringUtils.isNotEmpty( defCeleAvatar64 ) && StringUtils.isNotEmpty( kongToken ) )
          {
            String defCeleAvatarUrl = uploadImage( defCeleAvatar64, kongToken, meshBaseUrl );
            returnObj.setDefCeleAvatarUrl( defCeleAvatarUrl );
          }
        }

      }
      catch( Exception exc )
      {
        log.error( "Exception for the promotion ID :" + this.promotionID, exc );
      }
      return returnObj;
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

    private String getImageData( String filePath, String urlLocationPrefix, Long promotionId )
    {
      String imageBase64Value = null;

      try
      {
        byte[] data = getFileDataWebdav( filePath, urlLocationPrefix, promotionId );

        if ( Objects.nonNull( data ) )
        {
          imageBase64Value = Base64.getEncoder().encodeToString( data );
        }

      }
      catch( Exception e1 )
      {
        log.error( " PurlContributorAvatarMigrationProcess : Error getting image data from WebDav, Cause: " + e1.getMessage(), e1 );
      }

      return imageBase64Value;

    }

    private String getAwsImageData( String filePath, String awsHostBaseUrl, String awsS3BucketName, Long promotionId )
    {
      String imageBase64Value = null;

      try
      {
        byte[] data = getAwsFileData( filePath, awsHostBaseUrl, awsS3BucketName, promotionId );

        if ( Objects.nonNull( data ) )
        {
          imageBase64Value = Base64.getEncoder().encodeToString( data );
        }

      }
      catch( Exception e1 )
      {
        log.error( " PurlContributorAvatarMigrationProcess : Error getting image data from S3, Cause: " + e1.getMessage(), e1 );
      }

      return imageBase64Value;
    }

    private String getKongToken( String clientID, String clientSecret, String contextName, String rosterId, String meshBaseUrl )
    {
      String opKongToken = null;

      MultiValueMap<String, String> parameters = new LinkedMultiValueMap<String, String>();
      parameters.add( "client_id", clientID );
      parameters.add( "client_secret", clientSecret );
      parameters.add( "company_identifier", contextName );
      parameters.add( "grant_type", "daymaker" );
      parameters.add( "person_id", rosterId );

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

    private String uploadImage( String imageData, String kongToken, String meshBaseUrl )
    {
      ImageUrlResult urlResult = null;

      HttpHeaders headers = new HttpHeaders();
      headers.add( "authorization", "Bearer " + kongToken );

      ImageUploadRequest request = buildImageUploadRequest( imageData );

      try
      {
        urlResult = MeshServicesUtil.getRestWebClient().postForObject( meshBaseUrl + "/images", new HttpEntity<ImageUploadRequest>( request, headers ), ImageUrlResult.class );

        return urlResult.getProxyUrl();

      }
      catch( Exception e )
      {
        log.error( "Message from image service : ", e );
      }

      return null;
    }

    private ImageUploadRequest buildImageUploadRequest( String imageData )
    {
      ImageUploadRequest request = new ImageUploadRequest();
      request.setImage( imageData );
      request.setName( "promorecog" + "_" + System.currentTimeMillis() + "_" + UUID.randomUUID().toString() + "-" + RandomStringUtils.randomAlphanumeric( 3 ) );
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

    private byte[] getFileDataWebdav( String filePath, String urlLocationPrefix, Long promotionId )
    {
      final String PROTOCOL_HTTP = "http";
      final String PROTOCOL_HTTPS = "https";

      String urlLocation = null;
      DataInputStream reader = null;
      InputStream iStream = null;
      HttpURLConnection conn = null;
      byte[] opData = null;

      try
      {
        urlLocation = urlLocationPrefix + ImageUtils.convertToUrlPath( filePath );

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
        log.error( " ====>>>  Avatar/Image Is Missing For The Promotion ID   : " + promotionId + " . Celebration/Contributor Avatar/Image Url Path Is Exist In DB ( promo_recognition table ) : "
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

      return opData;

    }

    private byte[] getAwsFileData( String filePath, String awsHostBaseUrl, String awsS3BucketName, Long promotionId )
    {
      AmazonS3 s3client = null;
      String urlKey = null;
      byte[] data = null;

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
          log.error( " ====>>>  Avatar/Image Is Missing For The Promotion ID   : " + promotionId + " . The Celebration/Contributor Avatar/Image Url Path Is Exist In DB ( promo_recognition table ) : "
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

      return data;
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
