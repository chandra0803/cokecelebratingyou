
package com.biperf.core.process;

import static com.biperf.core.utils.Environment.getEnvironment;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
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
import com.biperf.core.service.ecard.EcardService;
import com.biperf.core.service.imageservice.v1.ImageUploadRequest;
import com.biperf.core.service.imageservice.v1.ImageUrlResult;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.strategy.AmazonClientFactory;
import com.biperf.core.utils.AwsUtils;
import com.biperf.core.utils.ImageUtils;
import com.biperf.core.utils.MeshServicesUtil;
import com.biperf.core.value.ecard.MigrateOwnCardInfo;
import com.biperf.core.value.ecard.MigratedCardInfo;
import com.biperf.core.value.ecard.OwnCardImageData;
import com.biperf.core.vo.ids.IDServiceResVO;

public class OwnCardMigrationProcess extends BaseProcessImpl
{
  private static final Log log = LogFactory.getLog( OwnCardMigrationProcess.class );

  public static final String BEAN_NAME = "ownCardMigrationProcess";

  private @Autowired AmazonClientFactory amazonClientFactory;
  private @Autowired EcardService ecardService;

  private Properties webDavProperties;

  boolean isRecAvl = true;
  boolean isNomAvl = true;

  String companyId;
  String meshSecretKey;
  String meshClientId;
  String companyIdentifier;
  String meshBaseUrl;
  String onPremHostBaseUrl;
  boolean isAwsClient;
  String awsHostBaseUrl;
  String awsS3BucketName;

  @Override
  public void onExecute()
  {
    meshSecretKey = getSystemVariableService().getPropertyByNameAndEnvironment( SystemVariableService.NACKLE_MESH_SERVICES_SECRET_KEY ).getStringVal();
    meshClientId = getSystemVariableService().getPropertyByNameAndEnvironment( SystemVariableService.NACKLE_MESH_SERVICES_CLIENT_ID ).getStringVal();

    companyIdentifier = getSystemVariableService().getContextName();
    meshBaseUrl = getSystemVariableService().getPropertyByNameAndEnvironment( SystemVariableService.NACKLE_MESH_SERVICES_HOST_BASE_URL ).getStringVal();
    onPremHostBaseUrl = getOnPremHostBaseUrl();

    isAwsClient = AwsUtils.isAws();
    awsHostBaseUrl = getAwsHostBaseUrl();
    awsS3BucketName = getAwsS3BucketName();

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

    if ( StringUtils.isEmpty( meshSecretKey ) || meshSecretKey.equals( "CHANGE ME!" ) || StringUtils.isEmpty( meshClientId ) || meshClientId.equals( "CHANGE ME!" ) )
    {
      addComment( " The authorization credentials provided for Kong are invalid. Check the value of the credentials as defined in System Variables(nackle.mesh.services.client.id.<env> & nackle.mesh.services.secret.key.<env>)." );
      return;
    }

    Long initialCount = 0L;
    Long initialCountForNom = 0L;
    List<OwnCardImageData> ownCardData = new ArrayList<>();
    List<OwnCardImageData> ownCardDataForNomination = new ArrayList<>();

    try
    {

      List<Future<MigratedCardInfo>> regFutureList = new ArrayList<Future<MigratedCardInfo>>();

      ownCardData = ecardService.getNotMigratedRecogOwnCardData();

      if ( !CollectionUtils.isEmpty( ownCardData ) )
      {
        ExecutorService recogExecutor = Executors.newFixedThreadPool( 70 );

        try
        {
          initialCount = (long)ownCardData.size();

          ownCardData.stream().forEach( inputObj ->
          {
            if ( !inputObj.getOwnCardName().contains( "biw.cloud/v1" ) )
            {
              inputObj.setOnPremHostBaseUrl( onPremHostBaseUrl );
              inputObj.setCompanyIdentifier( companyIdentifier );
              inputObj.setMeshBaseUrl( meshBaseUrl );
              inputObj.setMeshSecretKey( meshSecretKey );
              inputObj.setMeshClientId( meshClientId );
              inputObj.setAws( isAwsClient );
              inputObj.setAwsS3BucketName( awsS3BucketName );
              inputObj.setAwsHostBaseUrl( awsHostBaseUrl );

              Callable<MigratedCardInfo> callable = new CallableCardMigrationTask( inputObj );
              Future<MigratedCardInfo> regFuture = recogExecutor.submit( callable );
              regFutureList.add( regFuture );
            }
          } );

          regFutureList.stream().forEach( opObj ->
          {
            try
            {
              if ( opObj.get() != null )
              {
                MigratedCardInfo migratedCardInfo = (MigratedCardInfo)opObj.get();

                if ( Objects.nonNull( migratedCardInfo.getClaimId() ) && StringUtils.isNotEmpty( migratedCardInfo.getMigratedUrl() ) )
                {
                  ecardService.updateOwnCard( migratedCardInfo.getClaimId(), migratedCardInfo.getMigratedUrl() );
                }

              }
            }
            catch( InterruptedException | ExecutionException e )
            {
              try
              {
                log.error( "Getting Recognition future Object : " + e.getMessage() + "The future obj value : " + opObj.get().toString() );
              }
              catch( Exception e1 )
              {
                log.error( "Exception future object : ", e1 );
              }
            }

          } );

        }
        catch( Exception exc )
        {
          log.error( "Recognition own card migration failed  : " + exc.getMessage() );
        }
        finally
        {
          recogExecutor.shutdown();
        }
      }
      else
      {
        addComment( " Your request for Recogniton own cards was processed successfully. However did not find any Recogniton own card to migrate. Check the own card URL in Recogniton_Claim table." );
        isRecAvl = false;
      }

      Long finalCount = (long)ecardService.getNotMigratedRecogOwnCardData().size();
      Long totalCount = initialCount - finalCount;

      if ( isRecAvl )
      {
        if ( initialCount == finalCount )
        {
          addComment( " Eligible cards for Migration (Recogniton own cards): " + initialCount );
          addComment( " Your request failed for Recogniton own cards. Check the Application logs for more details." );
        }
        else if ( finalCount != 0L )
        {
          addComment( " Eligible cards for Migration (Recogniton own cards): " + initialCount );
          addComment( " Your request for Recogniton own cards was processed. " + totalCount + " Recogniton own cards migrated successfully while " + finalCount
              + " own cards failed migration.Check the Application logs for more details." );
        }
        else
        {
          addComment( " Eligible cards for Migration (Recogniton own cards): " + initialCount );
          addComment( " Your request for Recogniton own cards was processed successfully. " + initialCount + " Recogniton own cards were migrated effectively." );
        }

      }

      List<Future<MigratedCardInfo>> nomFutureList = new ArrayList<Future<MigratedCardInfo>>();

      ownCardDataForNomination = ecardService.getNotMigratedNomOwnCardData();

      if ( !CollectionUtils.isEmpty( ownCardDataForNomination ) )
      {
        ExecutorService nomExecutor = Executors.newFixedThreadPool( 70 );

        try
        {

          initialCountForNom = (long)ownCardDataForNomination.size();

          ownCardDataForNomination.stream().forEach( nomObj ->
          {
            if ( !nomObj.getOwnCardName().contains( "biw.cloud/v1" ) )
            {
              nomObj.setOnPremHostBaseUrl( onPremHostBaseUrl );
              nomObj.setCompanyIdentifier( companyIdentifier );
              nomObj.setMeshBaseUrl( meshBaseUrl );
              nomObj.setMeshSecretKey( meshSecretKey );
              nomObj.setMeshClientId( meshClientId );
              nomObj.setAws( isAwsClient );
              nomObj.setAwsS3BucketName( awsS3BucketName );
              nomObj.setAwsHostBaseUrl( awsHostBaseUrl );

              Callable<MigratedCardInfo> nomCallable = new CallableCardMigrationTask( nomObj );
              Future<MigratedCardInfo> nomFuture = nomExecutor.submit( nomCallable );
              nomFutureList.add( nomFuture );
            }
          } );

          nomFutureList.stream().forEach( nomObj ->
          {
            try
            {
              if ( nomObj.get() != null )
              {
                MigratedCardInfo migratedCardInfo = (MigratedCardInfo)nomObj.get();

                if ( Objects.nonNull( migratedCardInfo.getClaimId() ) && StringUtils.isNotEmpty( migratedCardInfo.getMigratedUrl() ) )
                {
                  ecardService.updateOwnCardForNomination( migratedCardInfo.getClaimId(), migratedCardInfo.getMigratedUrl() );
                }

              }
            }
            catch( InterruptedException | ExecutionException e )
            {
              try
              {
                log.error( "Getting nomination future Object : " + e.getMessage() + "The future obj value : " + nomObj.get().toString() );
              }
              catch( Exception e1 )
              {
                log.error( "Exception future object : ", e1 );
              }
            }

          } );

        }
        catch( Exception exc )
        {
          log.error( "Nomination own card migration failed  : " + exc.getMessage() );
        }
        finally
        {
          nomExecutor.shutdown();
        }
      }
      else
      {
        addComment( " Your request for nomination own card was processed successfully. However did not find any nomination own card to migrate. Check the own card URL in Nomination_Claim table." );
        isNomAvl = false;
      }

      Long finalCountForNom = (long)ecardService.getNotMigratedNomOwnCardData().size();
      Long totalCountForNom = initialCountForNom - finalCountForNom;

      if ( isNomAvl )
      {
        if ( initialCountForNom == finalCountForNom )
        {
          addComment( " Eligible cards for Migration (Nomination own cards): " + initialCountForNom );
          addComment( " Your request for nomination own card failed. Check the Application logs for more details." );
        }
        else if ( finalCountForNom != 0L )
        {
          addComment( " Eligible cards for Migration (Nomination own cards): " + initialCountForNom );
          addComment( " Your request for nomination own card was processed. " + totalCountForNom + " own cards migrated successfully while " + finalCountForNom
              + " cards failed migration. Check the Application logs for more details." );
        }
        else
        {
          addComment( " Eligible cards for Migration (Nomination own cards): " + initialCountForNom );
          addComment( " Your request for Nomination own cards was processed successfully. " + initialCountForNom + " Nomination own cards were migrated effectively." );
        }

      }

    }
    catch( Exception e )
    {
      log.error( e );
    }
  }

  class CallableCardMigrationTask implements Callable<MigratedCardInfo>
  {
    private Long claimId;
    private String randomUUID;
    private String recOwnCardUrl;
    private String onPremHostBaseUrl;
    private String meshBaseUrl;
    private String companyIdentifier;
    private String meshClientId;
    private String meshSecretKey;
    private boolean isAwsClient;
    private String awsHostBaseUrl;
    private String awsS3BucketName;

    public CallableCardMigrationTask( OwnCardImageData ownCardImageData )
    {
      claimId = ownCardImageData.getId();
      recOwnCardUrl = ownCardImageData.getOwnCardName();
      randomUUID = UUID.randomUUID().toString();
      onPremHostBaseUrl = ownCardImageData.getOnPremHostBaseUrl();
      meshBaseUrl = ownCardImageData.getMeshBaseUrl();
      companyIdentifier = ownCardImageData.getCompanyIdentifier();
      meshClientId = ownCardImageData.getMeshClientId();
      meshSecretKey = ownCardImageData.getMeshSecretKey();
      isAwsClient = ownCardImageData.isAws();
      awsHostBaseUrl = ownCardImageData.getAwsHostBaseUrl();
      awsS3BucketName = ownCardImageData.getAwsS3BucketName();
    }

    @Override
    public MigratedCardInfo call() throws Exception
    {
      MigratedCardInfo mgtCardInfo = new MigratedCardInfo();
      String kongToken = null;
      MigrateOwnCardInfo uploadData = null;

      String ownCardBase64 = null;
      String migratedUrl = null;

      try
      {
        if ( Objects.nonNull( claimId ) && StringUtils.isNotEmpty( recOwnCardUrl ) )
        {
          uploadData = getUploadData( claimId, recOwnCardUrl );

          if ( Objects.nonNull( uploadData ) && Objects.nonNull( uploadData.getCardData() ) && Objects.nonNull( uploadData.getClaimId() ) )
          {
            ownCardBase64 = getImageData( ImageUtils.getFileExtension( uploadData.getImageName() ), uploadData.getCardData() );

            if ( StringUtils.isNotEmpty( ownCardBase64 ) )
            {
              kongToken = getKongToken( meshClientId, meshSecretKey, companyIdentifier, randomUUID, meshBaseUrl );

              if ( StringUtils.isNotEmpty( kongToken ) )
              {
                migratedUrl = uploadImage( ownCardBase64, randomUUID, kongToken, meshBaseUrl );
              }

              if ( StringUtils.isNotEmpty( migratedUrl ) )
              {
                mgtCardInfo.setMigratedUrl( migratedUrl );
                mgtCardInfo.setClaimId( claimId );
              }
            }
          }
        }
      }
      catch( Exception exc )
      {
        log.error( "Recognition claim Id : " + claimId + " Url Path : " + recOwnCardUrl, exc );
      }

      return mgtCardInfo;
    }

    private String uploadImage( String imageData, String id, String kongToken, String meshBaseUrl )
    {
      ImageUrlResult urlResult = null;

      HttpHeaders headers = new HttpHeaders();
      headers.add( "authorization", "Bearer " + kongToken );

      ImageUploadRequest request = buildRecogImageUploadRequest( imageData, id );

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

    private ImageUploadRequest buildRecogImageUploadRequest( String imageData, String id )
    {
      ImageUploadRequest request = new ImageUploadRequest();
      request.setImage( imageData );
      request.setName( "owncard" + "_" + System.currentTimeMillis() + "_" + UUID.randomUUID().toString() + "-" + RandomStringUtils.randomAlphanumeric( 3 ) );
      request.setPublic( true );
      return request;
    }

    private String getKongToken( String clientID, String clientSecret, String contextName, String randomUUID, String meshBaseUrl )
    {
      String opKongToken = null;

      MultiValueMap<String, String> parameters = new LinkedMultiValueMap<String, String>();
      parameters.add( "client_id", clientID );
      parameters.add( "client_secret", clientSecret );
      parameters.add( "company_identifier", contextName );
      parameters.add( "grant_type", "daymaker" );
      parameters.add( "person_id", randomUUID );

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

    private MigrateOwnCardInfo getUploadData( Long claimId, String cardPath )
    {
      MigrateOwnCardInfo migrateInfo = new MigrateOwnCardInfo();
      migrateInfo.setClaimId( claimId );
      migrateInfo.setImageName( getImageName( cardPath ) );
      migrateInfo.setCardData( getCardData( cardPath, claimId ) );
      return migrateInfo;
    }

    private String getImageName( String cardPath )
    {
      URL aURL;
      String imageName = null;
      try
      {
        aURL = new URL( cardPath );
        String urlPath = aURL.getPath();
        String[] splitArr = urlPath.split( "/" );
        imageName = splitArr[splitArr.length - 1];
      }
      catch( MalformedURLException e )
      {
        log.error( e );
      }
      return imageName;
    }

    private String getCardData( String cardPath, Long claimId )
    {
      byte[] data = null;
      String imageBase64Value = null;
      String[] pathArr = null;
      AmazonS3 s3client = null;
      String urlKey = null;

      try
      {
        if ( isAwsClient )
        {
          s3client = amazonClientFactory.getClient();
          urlKey = awsHostBaseUrl + getImageName( cardPath );
          S3Object object = s3client.getObject( new GetObjectRequest( awsS3BucketName, urlKey ) );
          data = new byte[(int)object.getObjectMetadata().getContentLength()];
          DataInputStream dataIs = new DataInputStream( object.getObjectContent() );
          dataIs.readFully( data );
          dataIs.close();

        }
        else
        {
          pathArr = getPath( cardPath );
          data = getFileDataWebdav( pathArr[pathArr.length - 2] + "/" + pathArr[pathArr.length - 1], onPremHostBaseUrl, claimId );
        }

        if ( Objects.nonNull( data ) )
        {
          imageBase64Value = Base64.getEncoder().encodeToString( data );
        }

      }
      catch( AmazonServiceException ase )
      {
        if ( ase.getStatusCode() == 404 && ase.getErrorCode().contains( "NoSuchKey" ) )
        {
          log.error( " ====>>>  Recognition/Nomination Claim For The Claim Id : " + claimId + " . Own Card Url Path Is Exist In DB ( recogniton_claim or nomination_claim table table ) : "
              + recOwnCardUrl + " ,But Not Exist In AWS S3 Bucket (Name) : " + awsS3BucketName + " , The S3 Url Path Is : " + urlKey );
        }
      }
      catch( IOException e )
      {
        log.error( " ====>>>  Recognition/Nomination Claim For The Claim Id : " + claimId + " . Own Card Url Path Exist In DB ( recogniton_claim or nomination_claim table table ) : " + recOwnCardUrl
            + " , But Not Exist in S3/Webdav " );
        log.error( e );
      }
      finally
      {
        if ( s3client != null && isAwsClient )
        {
          s3client.shutdown();
        }
      }

      return imageBase64Value;
    }

    private byte[] getFileDataWebdav( String filePath, String onPremHostBaseUrl, Long claimId )
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
        log.error( " ====>>>  Recognition/Nomination Claim For The Claim Id : " + claimId + " . Own Card Url Path Exist In DB ( recogniton_claim or nomination_claim table ) : " + recOwnCardUrl
            + " , But Not Exist In WebDav : " + urlLocation );
        log.error( e );
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

    private String getImageData( String contentType, String imageInBytes )
    {
      StringBuilder imageDataSb = new StringBuilder();
      imageDataSb.append( "data:" );
      imageDataSb.append( contentType );
      imageDataSb.append( ";base64," );
      imageDataSb.append( imageInBytes );

      return imageDataSb.toString();
    }

    private String[] getPath( String cardPath )
    {
      URL aURL = null;
      String urlPath = null;
      String[] splitArr = null;
      try
      {
        aURL = new URL( cardPath );
        urlPath = aURL.getPath();
        splitArr = urlPath.split( "/" );
      }
      catch( MalformedURLException e )
      {
        e.printStackTrace();
      }
      return splitArr;

    }
  }

  private String getAwsHostBaseUrl()
  {
    return getSystemVariableService().getContextName() + "-cm/cm3dam/ecard/";
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
