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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.biperf.core.service.participant.ProfileService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.strategy.AmazonClientFactory;
import com.biperf.core.utils.AwsUtils;
import com.biperf.core.utils.ImageUtils;
import com.biperf.core.utils.MeshServicesUtil;
import com.biperf.core.value.participant.MigratedAvatarData;
import com.biperf.core.value.participant.PaxAvatarData;
import com.biperf.core.value.profile.v1.uploadavatar.AvatarView;
import com.biperf.core.value.profile.v1.uploadavatar.UploadAvatarRequest;
import com.biperf.core.vo.ids.IDServiceResVO;

public class AvatarMigrationProcess extends BaseProcessImpl
{
  private static final Log log = LogFactory.getLog( AvatarMigrationProcess.class );

  public static final String PROCESS_NAME = "Participant Avatar Migration Process";
  public static final String BEAN_NAME = "avatarMigrationProcess";

  private @Autowired ProfileService profileService;
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
        List<Future<MigratedAvatarData>> futureList = new ArrayList<Future<MigratedAvatarData>>();
        List<PaxAvatarData> paxAvatarDataList = profileService.getNotMigratedPaxAvatarData();

        if ( !CollectionUtils.isEmpty( paxAvatarDataList ) )
        {
          ExecutorService executor = Executors.newFixedThreadPool( 70 );

          initialCount = (long)paxAvatarDataList.size();

          paxAvatarDataList.stream().forEach( inputObj ->
          {
            inputObj.setOnPremHostBaseUrl( onPremHostBaseUrl );
            inputObj.setCompanyIdentifier( companyIdentifier );
            inputObj.setMeshBaseUrl( meshBaseUrl );
            inputObj.setMeshSecretKey( meshSecretKey );
            inputObj.setMeshClientId( meshClientId );
            inputObj.setAws( isAwsClient );
            inputObj.setAwsS3BucketName( awsS3BucketName );
            inputObj.setAwsHostBaseUrl( awsHostBaseUrl );

            Callable<MigratedAvatarData> callable = new CallableMigrationTask( inputObj );
            Future<MigratedAvatarData> future = executor.submit( callable );
            futureList.add( future );

          } );

          futureList.stream().forEach( futureObj ->
          {
            try
            {
              if ( futureObj.get() != null )
              {
                MigratedAvatarData migratedAvatarData = (MigratedAvatarData)futureObj.get();

                if ( Objects.nonNull( migratedAvatarData.getUserId() ) && Objects.nonNull( migratedAvatarData.getAvatarOriginalUrl() ) && Objects.nonNull( migratedAvatarData.getAvatarSmallUrl() ) )
                {
                  profileService.updateMigratedPaxAvatarData( migratedAvatarData.getUserId(), migratedAvatarData.getAvatarOriginalUrl(), migratedAvatarData.getAvatarSmallUrl() );

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
          addComment( " Your request was processed successfully. However did not find any Avatar to migrate. Check the avatar URL in PARTICIPANT table and appropriate ROSTER_USER_ID in APPLICATION_USER table." );
          return;
        }

      }
      catch( Exception exc )
      {
        log.error( "Avatar migration failed  : " + exc.getMessage() );
      }

      Long finalCount = (long)profileService.getNotMigratedPaxAvatarData().size();
      Long totalCount = initialCount - finalCount;

      if ( initialCount == finalCount )
      {
        addComment( " Eligible avatars for Migration : " + initialCount );
        addComment( " Your request failed. Check the Application logs for more details." );
      }
      else if ( finalCount != 0L )
      {
        addComment( " Eligible avatars for Migration : " + initialCount );
        addComment( " Your request was processed. " + totalCount + " Avatars migrated successfully while " + finalCount + " avatars failed migration. Check the Application logs for more details." );
      }
      else
      {
        addComment( " Eligible avatars for Migration : " + initialCount );
        addComment( " Your request was processed successfully. " + initialCount + " Avatars were migrated effectively." );
      }

    }
    else
    {
      addComment( " The authorization credentials provided for Kong are invalid. Check the value of the credentials as defined in System Variables(nackle.mesh.services.client.id.<env> & nackle.mesh.services.secret.key.<env>)." );
    }

  }

  class CallableMigrationTask implements Callable<MigratedAvatarData>
  {
    private Long userId;
    private String avatarOriginal;
    private String onPremHostBaseUrl;
    private UUID rosterUserId;
    private String meshBaseUrl;
    private String companyIdentifier;
    private String meshClientId;
    private String meshSecretKey;
    private boolean isAwsClient;
    private String awsHostBaseUrl;
    private String awsS3BucketName;

    public CallableMigrationTask( PaxAvatarData paxAvatarData )
    {
      userId = paxAvatarData.getUserId();
      avatarOriginal = paxAvatarData.getAvatarOriginal();
      onPremHostBaseUrl = paxAvatarData.getOnPremHostBaseUrl();
      rosterUserId = paxAvatarData.getRosterUserId();
      meshBaseUrl = paxAvatarData.getMeshBaseUrl();
      companyIdentifier = paxAvatarData.getCompanyIdentifier();
      meshClientId = paxAvatarData.getMeshClientId();
      meshSecretKey = paxAvatarData.getMeshSecretKey();
      isAwsClient = paxAvatarData.isAws();
      awsHostBaseUrl = paxAvatarData.getAwsHostBaseUrl();
      awsS3BucketName = paxAvatarData.getAwsS3BucketName();
    }

    @Override
    public MigratedAvatarData call() throws Exception
    {
      MigratedAvatarData migratedAvatarData = new MigratedAvatarData();
      String avatarOrgMigratedUrl = null;
      String avatarOrgBase64 = null;
      String kongToken = null;

      try
      {
        if ( !avatarOriginal.contains( "biw.cloud/v1" ) )
        {
          if ( isAwsClient )
          {
            avatarOrgBase64 = getAwsImageData( avatarOriginal, awsHostBaseUrl, awsS3BucketName, userId );
          }
          else
          {
            avatarOrgBase64 = getImageData( avatarOriginal, onPremHostBaseUrl, userId );
          }
        }

        if ( StringUtils.isNotEmpty( avatarOrgBase64 ) )
        {
          String imgContentType = getContentType( avatarOriginal );

          avatarOrgBase64 = getImageDataInUploadFormat( imgContentType, avatarOrgBase64 );

          kongToken = getKongToken( meshClientId, meshSecretKey, companyIdentifier, rosterUserId, meshBaseUrl );

          if ( Objects.nonNull( kongToken ) && Objects.nonNull( avatarOrgBase64 ) )
          {
            avatarOrgMigratedUrl = uploadImage( avatarOrgBase64, rosterUserId, kongToken, meshBaseUrl );

            migratedAvatarData.setUserId( userId );

            if ( Objects.nonNull( avatarOrgMigratedUrl ) && Objects.nonNull( rosterUserId ) )
            {
              String opUrl = MeshServicesUtil.getNackleMeshServicesBaseEndPoint() + "/profile/public/avatars/image/" + MeshServicesUtil.getCompanyId().toString() + "/" + rosterUserId.toString();
              migratedAvatarData.setAvatarOriginalUrl( opUrl );
              migratedAvatarData.setAvatarSmallUrl( opUrl );
            }
          }
        }

      }
      catch( Exception exc )
      {
        log.error( "Exception for the person id :" + this.userId, exc );
      }

      return migratedAvatarData;
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

    private String getImageData( String filePath, String onPremHostBaseUrl, Long userId )
    {
      String imageBase64Value = null;

      try
      {
        byte[] data = getFileDataWebdav( filePath, onPremHostBaseUrl, userId );

        if ( Objects.nonNull( data ) )
        {
          imageBase64Value = Base64.getEncoder().encodeToString( data );
        }

      }
      catch( Exception e1 )
      {
        log.error( " AvatarMigrationProcess : Error getting image data from WebDav, Cause: " + e1.getMessage() );
      }

      return imageBase64Value;

    }

    private String getAwsImageData( String filePath, String awsHostBaseUrl, String awsS3BucketName, Long userId )
    {
      String imageBase64Value = null;

      try
      {
        byte[] data = getAwsFileData( filePath, awsHostBaseUrl, awsS3BucketName, userId );

        if ( Objects.nonNull( data ) )
        {
          imageBase64Value = Base64.getEncoder().encodeToString( data );
        }

      }
      catch( Exception e1 )
      {
        log.error( " AvatarMigrationProcess : Error getting image data from S3, Cause: " + e1.getMessage() );
      }

      return imageBase64Value;
    }

    private String getKongToken( String clientID, String clientSecret, String contextName, UUID rosterUserId, String meshBaseUrl )
    {
      String opKongToken = null;

      MultiValueMap<String, String> parameters = new LinkedMultiValueMap<String, String>();
      parameters.add( "client_id", clientID );
      parameters.add( "client_secret", clientSecret );
      parameters.add( "company_identifier", contextName );
      parameters.add( "grant_type", "daymaker" );
      parameters.add( "person_id", rosterUserId.toString() );

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
          log.error( " :: Oops !!!. ID Service not retuning Kong token to authenticate Profile Service. Response Code : " + idServiceResVO.getStatusCode() );
        }

      }
      catch( Exception resException )
      {
        log.error( " :: ID Service not retuning Kong token to authenticate Profile Service." );

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

    private String uploadImage( String imageData, UUID rosterUserId, String kongToken, String meshBaseUrl )
    {
      String mgtUrl = null;
      ResponseEntity<AvatarView> avatarView = null;

      try
      {
        HttpHeaders headers = MeshServicesUtil.getHeaders();
        headers.add( "authorization", "Bearer " + kongToken );

        UploadAvatarRequest uploadAvatarRequest = new UploadAvatarRequest();
        uploadAvatarRequest.setImageData( imageData );

        Map<String, Object> param = new HashMap<>();
        param.put( "personId", rosterUserId.toString() );

        avatarView = MeshServicesUtil.getRestWebClient().exchange( meshBaseUrl + "/profile/avatars/{personId}",
                                                                   HttpMethod.PUT,
                                                                   new HttpEntity<UploadAvatarRequest>( uploadAvatarRequest, headers ),
                                                                   AvatarView.class,
                                                                   param );

        if ( avatarView.getStatusCode() == HttpStatus.OK )
        {
          mgtUrl = avatarView.getBody().getAvatarUrl();
        }
        else
        {
          log.error( " :: HTTP Response Code from Profile Service : " + avatarView.getStatusCode() );
          log.error( " :: Seems there is a temporary issue at Profile Service end, thus not able to upload avatar to Profile Service." );
        }
      }
      catch( RestClientException ex )
      {
        log.error( " Rest Client Exception while uploading avatar : ", ex );
      }
      catch( Exception exception )
      {
        log.error( "Exception while uploading : ", exception );
      }

      return mgtUrl;
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

    private byte[] getFileDataWebdav( String filePath, String onPremHostBaseUrl, Long userId )
    {
      final String PROTOCOL_HTTP = "http";
      final String PROTOCOL_HTTPS = "https";

      String fullUrl = null;
      DataInputStream reader = null;
      InputStream iStream = null;
      HttpURLConnection conn = null;
      byte[] opData = null;

      try
      {
        fullUrl = onPremHostBaseUrl + ImageUtils.convertToUrlPath( filePath );

        if ( fullUrl.startsWith( PROTOCOL_HTTPS ) )
        {
          fullUrl = fullUrl.replace( PROTOCOL_HTTPS, PROTOCOL_HTTP );
        }

        URL url = new URL( fullUrl );
        conn = (HttpURLConnection)url.openConnection();
        reader = new DataInputStream( conn.getInputStream() );

        opData = IOUtils.toByteArray( reader );

      }
      catch( IOException e )
      {
        log.error( " ====>>>  Avatar Is Missing For The User Id : " + userId + " . Avatar Url Path Exist In DB ( participant table ) : " + filePath + " , But Not Exist In The WebDav : " + fullUrl );
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

    private byte[] getAwsFileData( String filePath, String awsHostBaseUrl, String awsS3BucketName, Long userId )
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
          log.error( " ====>>>  Avatar Is Missing For The User Id : " + userId + " . The Avatar Url Path Is Exist In DB ( participant table ) : " + filePath
              + " ,But Not Exist In AWS S3 Bucket (Name) : " + awsS3BucketName + " , The S3 Url Path Is : " + urlKey );
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
