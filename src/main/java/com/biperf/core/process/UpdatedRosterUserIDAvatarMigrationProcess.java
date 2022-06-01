/*
 * (c) 2019 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.process;

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
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Stream;

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

import com.biperf.core.service.participant.ProfileService;
import com.biperf.core.service.purl.PurlService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.utils.BICollectionUtils;
import com.biperf.core.utils.Environment;
import com.biperf.core.utils.MeshServicesUtil;
import com.biperf.core.value.participant.MigratedAvatarData;
import com.biperf.core.value.participant.PaxAvatarData;
import com.biperf.core.value.participant.PurlContributorAvatarData;
import com.biperf.core.value.profile.v1.uploadavatar.AvatarView;
import com.biperf.core.value.profile.v1.uploadavatar.UploadAvatarRequest;
import com.biperf.core.vo.ids.IDServiceResVO;

public class UpdatedRosterUserIDAvatarMigrationProcess extends BaseProcessImpl
{
  private static final Log log = LogFactory.getLog( UpdatedRosterUserIDAvatarMigrationProcess.class );

  public static final String PROCESS_NAME = "Updated Roster UserID Avatar Migration Process";
  public static final String BEAN_NAME = "updatedRosterUserIDAvatarMigrationProcess";

  private @Autowired ProfileService profileService;
  private @Autowired PurlService purlService;

  private String companyId = null;
  private List<Long> purlPaxs = null;
  private List<PaxAvatarData> paxAvatarDataList = null;
  private List<PurlContributorAvatarData> purlContributorAvatarDataList = null;
  private boolean showCopyMsg = true;

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

    String meshSecretKey = getSystemVariableService().getPropertyByNameAndEnvironment( SystemVariableService.NACKLE_MESH_SERVICES_SECRET_KEY ).getStringVal();
    String meshClientId = getSystemVariableService().getPropertyByNameAndEnvironment( SystemVariableService.NACKLE_MESH_SERVICES_CLIENT_ID ).getStringVal();

    String companyIdentifier = getSystemVariableService().getContextName();
    String meshBaseUrl = getSystemVariableService().getPropertyByNameAndEnvironment( SystemVariableService.NACKLE_MESH_SERVICES_HOST_BASE_URL ).getStringVal();

    if ( StringUtils.isNotEmpty( companyId ) && ( StringUtils.isNotEmpty( meshSecretKey ) && !meshSecretKey.equals( "CHANGE ME!" ) )
        && ( StringUtils.isNotEmpty( meshClientId ) && !meshClientId.equals( "CHANGE ME!" ) ) )
    {
      Long initialCount = 0L;

      try
      {
        List<Future<MigratedAvatarData>> futureList = new ArrayList<Future<MigratedAvatarData>>();

        paxAvatarDataList = profileService.getUpdatedRosterUserIdPaxAvatarData();

        initialCount = (long)paxAvatarDataList.size();

        purlPaxs = purlService.getAllPurlContrbUsersToCopyTheUrl();

        if ( !CollectionUtils.isEmpty( paxAvatarDataList ) )
        {
          ExecutorService executor = Executors.newFixedThreadPool( 70 );

          paxAvatarDataList.stream().forEach( inputObj ->
          {
            inputObj.setCompanyIdentifier( companyIdentifier );
            inputObj.setMeshBaseUrl( meshBaseUrl );
            inputObj.setMeshSecretKey( meshSecretKey );
            inputObj.setMeshClientId( meshClientId );

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
        else if ( !CollectionUtils.isEmpty( purlPaxs ) )
        {
          log.info( " Initiated eligible avatars synchronization." );
        }
        else
        {
          addComment( " Your request was processed successfully. However did not find any Avatar to Sync Up. Check the avatar URL in PARTICIPANT table and appropriate ROSTER_USER_ID in APPLICATION_USER table." );
          return;
        }

      }
      catch( Exception exc )
      {
        log.error( "ReUpdatedRosterUserIDAvatarMigrationProcess migration failed  : " + exc.getMessage() );
      }

      try
      {
        if ( !CollectionUtils.isEmpty( purlPaxs ) )
        {
          Stream<List<Long>> batches = BICollectionUtils.batches( purlPaxs, 500 );

          batches.forEach( inputList ->
          {
            if ( !CollectionUtils.isEmpty( inputList ) )
            {
              purlContributorAvatarDataList = purlService.getNotSyncPurlContrbUserAvatarData( inputList );

              if ( !CollectionUtils.isEmpty( purlContributorAvatarDataList ) )
              {
                purlContributorAvatarDataList.stream().forEach( avtObj ->
                {
                  purlService.updateMigratedPurlContrPaxAvatar( avtObj.getUserId(), avtObj.getAvatarUrl() );
                } );
              }
            }
          } );

          if ( CollectionUtils.isEmpty( paxAvatarDataList ) && !CollectionUtils.isEmpty( purlContributorAvatarDataList ) )
          {
            showCopyMsg = false;
            addComment( "Your request was processed successfully. Eligible avatars were synchronized effectively." );
          }
        }
      }
      catch( Exception exc1 )
      {
        if ( CollectionUtils.isEmpty( paxAvatarDataList ) )
        {
          showCopyMsg = false;
          addComment( " Eligible avatars are Not synchronized, from participant table to purl_contributor table. Check the Application logs for more details." );
          log.error( "ReUpdatedRosterUserIDAvatarMigrationProcess, copying the default contributors avatar   : " + exc1.getMessage() );
        }
      }

      if ( !CollectionUtils.isEmpty( paxAvatarDataList ) )
      {
        Long finalCount = (long)profileService.getUpdatedRosterUserIdPaxAvatarData().size();
        Long totalCount = initialCount - finalCount;

        if ( initialCount == finalCount )
        {
          addComment( " Eligible avatars for Unique Roster User ID Sync Up : " + initialCount );
          addComment( " Your request failed. Check the Application logs for more details." );
        }
        else if ( finalCount != 0L )
        {
          addComment( " Eligible avatars for Unique Roster User ID Sync Up : " + initialCount );
          addComment( " Your request was processed. " + totalCount + " Avatars synchronized successfully while " + finalCount
              + " avatars failed synchronization. Check the Application logs for more details." );
        }
        else
        {
          addComment( " Eligible avatars for Unique Roster User ID Sync Up : " + initialCount );
          addComment( " Your request was processed successfully. " + initialCount + " Avatars were synchronized effectively." );
        }
      }
      else if ( showCopyMsg )
      {
        addComment( "Your request was processed successfully." );
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
    private UUID rosterUserId;
    private String meshBaseUrl;
    private String companyIdentifier;
    private String meshClientId;
    private String meshSecretKey;

    public CallableMigrationTask( PaxAvatarData paxAvatarImgInfo )
    {
      userId = paxAvatarImgInfo.getUserId();
      avatarOriginal = paxAvatarImgInfo.getAvatarOriginal();
      rosterUserId = paxAvatarImgInfo.getRosterUserId();
      meshBaseUrl = paxAvatarImgInfo.getMeshBaseUrl();
      companyIdentifier = paxAvatarImgInfo.getCompanyIdentifier();
      meshClientId = paxAvatarImgInfo.getMeshClientId();
      meshSecretKey = paxAvatarImgInfo.getMeshSecretKey();
    }

    @Override
    public MigratedAvatarData call() throws Exception
    {
      MigratedAvatarData migratedAvatarInfo = new MigratedAvatarData();
      String avatarOrgMigratedUrl = null;
      String avatarOrgBase64 = null;
      String kongToken = null;

      try
      {
        if ( avatarOriginal.contains( "biw.cloud/v1" ) )
        {
          avatarOrgBase64 = getImageData( avatarOriginal, userId, rosterUserId );
        }

        if ( StringUtils.isNotEmpty( avatarOrgBase64 ) )
        {
          String imgContentType = getContentType();

          avatarOrgBase64 = getImageDataInUploadFormat( imgContentType, avatarOrgBase64 );

          kongToken = getKongToken( meshClientId, meshSecretKey, companyIdentifier, rosterUserId, meshBaseUrl );

          if ( Objects.nonNull( kongToken ) && Objects.nonNull( avatarOrgBase64 ) )
          {
            avatarOrgMigratedUrl = uploadImage( avatarOrgBase64, rosterUserId, kongToken, meshBaseUrl );

            migratedAvatarInfo.setUserId( userId );

            if ( Objects.nonNull( avatarOrgMigratedUrl ) && Objects.nonNull( rosterUserId ) )
            {
              String opUrl = MeshServicesUtil.getNackleMeshServicesBaseEndPoint() + "/profile/public/avatars/image/" + MeshServicesUtil.getCompanyId().toString() + "/" + rosterUserId.toString();
              migratedAvatarInfo.setAvatarOriginalUrl( opUrl );
              migratedAvatarInfo.setAvatarSmallUrl( opUrl );
            }
          }
        }

      }
      catch( Exception exc )
      {
        log.error( "Exception for the person id :" + this.userId, exc );
      }

      return migratedAvatarInfo;
    }

    private String getContentType()
    {
      return "image/jpg";
    }

    private String getImageData( String filePath, Long userId, UUID rosterUserId )
    {
      String imageBase64Value = null;

      try
      {
        byte[] data = getFileDataFromProfileService( filePath, userId, rosterUserId );

        if ( Objects.nonNull( data ) )
        {
          imageBase64Value = Base64.getEncoder().encodeToString( data );
        }

      }
      catch( Exception e1 )
      {
        log.error( " ReUpdatedRosterUserIDAvatarMigrationProcess : Error getting image data from Profile Service, Cause: " + e1.getMessage() );
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

    private byte[] getFileDataFromProfileService( String filePath, Long userId, UUID rosterUserId )
    {
      DataInputStream reader = null;
      InputStream iStream = null;
      HttpURLConnection conn = null;
      byte[] opData = null;

      try
      {

        URL url = new URL( filePath );
        conn = (HttpURLConnection)url.openConnection( Environment.buildProxy() );
        conn.setConnectTimeout( 180000 );
        conn.setReadTimeout( 180000 );
        reader = new DataInputStream( conn.getInputStream() );

        opData = IOUtils.toByteArray( reader );

      }
      catch( IOException e )
      {
        log.error( " ====>>>  Avatar Is Missing For The User Id : " + userId + " , appropriate Roster user Id : " + rosterUserId + " . Avatar Url Path Exist In DB ( participant table ) : " + filePath
            + " , But Not Exist In The Profile service " );
        log.error( "Failed to invoke URL ", e );

      }
      catch( Exception exc )
      {
        log.error( "Failed to fetch the data from URL ", exc );
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

  }

}
