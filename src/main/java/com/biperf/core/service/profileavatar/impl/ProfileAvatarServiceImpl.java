
package com.biperf.core.service.profileavatar.impl;

import java.util.Objects;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;

import com.biperf.core.dao.participant.ParticipantDAO;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.imageservice.ImageServiceRepositoryFactory;
import com.biperf.core.service.profileavatar.ProfileAvatarRepositoryFactory;
import com.biperf.core.service.profileavatar.ProfileAvatarService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.utils.MeshServicesUtil;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.profile.v1.uploadavatar.AvatarView;
import com.biperf.core.value.profile.v1.uploadavatar.UploadAvatarRequest;

@Service( "profileAvatarService" )
public class ProfileAvatarServiceImpl implements ProfileAvatarService
{

  private static final Log logger = LogFactory.getLog( ProfileAvatarServiceImpl.class );

  @Autowired
  private ProfileAvatarRepositoryFactory profileAvatarRepositoryFactory;

  @Autowired
  private SystemVariableService systemVariableService;

  @Autowired
  private ParticipantDAO participantDAO;

  private @Autowired ImageServiceRepositoryFactory imageServiceRepositoryFactory;

  @Override
  public ResponseEntity<AvatarView> uploadAvatar( String personId, UploadAvatarRequest uploadAvatarRequest ) throws ServiceErrorException, JSONException, Exception
  {
    ResponseEntity<AvatarView> avatarView = null;

    Participant participant = getParticipantDAO().getParticipantById( UserManager.getUserId() );

    try
    {
      avatarView = profileAvatarRepositoryFactory.getRepo().uploadAvatar( personId, uploadAvatarRequest );

      String avatarURL = avatarView.getBody().getAvatarUrl();

      if ( avatarView.getStatusCode() == HttpStatus.OK && Objects.nonNull( avatarURL ) )
      {
        String opUrl = MeshServicesUtil.getNackleMeshServicesBaseEndPoint() + "/profile/public/avatars/image/" + MeshServicesUtil.getCompanyId().toString() + "/"
            + UserManager.getRosterUserId().toString();

        participant.setAvatarOriginal( opUrl );
        participant.setAvatarSmall( opUrl );
        getParticipantDAO().saveParticipant( participant );
      }

    }
    catch( HttpStatusCodeException httpException )
    {
      logger.error( "Exception while uploading avatar : " + httpException.getMessage() );
      throw new ServiceErrorException( httpException.getResponseBodyAsString() );
    }
    catch( Exception exception )
    {
      logger.error( "Exception while uploading avatar : " + exception.getMessage() );
      throw new ServiceErrorException( exception.getMessage() );
    }

    return avatarView;
  }

  @Override
  public String uploadDefaultPurlAvatar( String id, String imgData ) throws ServiceErrorException, JSONException, Exception
  {
    String imageServiceUrl = null;

    try
    {
      imageServiceUrl = imageServiceRepositoryFactory.getRepo().uploadImage( imgData, id, "purl_avatar" );
    }
    catch( HttpStatusCodeException httpException )
    {
      logger.error( "Exception while uploading image : " + httpException.getMessage() );
      throw new ServiceErrorException( httpException.getResponseBodyAsString() );
    }
    catch( Exception exception )
    {
      logger.error( "Exception while uploading image : " + exception.getMessage() );
      throw new ServiceErrorException( exception.getMessage() );
    }

    return imageServiceUrl;
  }

  @Override
  public String uploadDefaultPurlPicture( String id, String imgData ) throws ServiceErrorException, JSONException, Exception
  {
    String imageServiceUrl = null;

    try
    {
      imageServiceUrl = imageServiceRepositoryFactory.getRepo().uploadImage( imgData, id, "purl_picture" );
    }
    catch( HttpStatusCodeException httpException )
    {
      logger.error( "Exception while uploading image : " + httpException.getMessage() );
      throw new ServiceErrorException( httpException.getResponseBodyAsString() );
    }
    catch( Exception exception )
    {
      logger.error( "Exception while uploading image : " + exception.getMessage() );
      throw new ServiceErrorException( exception.getMessage() );
    }

    return imageServiceUrl;
  }

  public ProfileAvatarRepositoryFactory getProfileAvatarRepositoryFactory()
  {
    return profileAvatarRepositoryFactory;
  }

  public void setProfileAvatarRepositoryFactory( ProfileAvatarRepositoryFactory profileAvatarRepositoryFactory )
  {
    this.profileAvatarRepositoryFactory = profileAvatarRepositoryFactory;
  }

  public SystemVariableService getSystemVariableService()
  {
    return systemVariableService;
  }

  public void setSystemVariableService( SystemVariableService systemVariableService )
  {
    this.systemVariableService = systemVariableService;
  }

  /**
   * used for dependency injection
   * 
   * @return ParticipantDAO
   */
  public ParticipantDAO getParticipantDAO()
  {
    return participantDAO;
  }

  /**
   * used for dependency injection
   * 
   * @param participantDAO
   */

}
