
package com.biperf.core.service.profileavatar.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.profileavatar.ProfileAvatarRepository;
import com.biperf.core.utils.MeshServicesUtil;
import com.biperf.core.value.profile.v1.uploadavatar.AvatarView;
import com.biperf.core.value.profile.v1.uploadavatar.UploadAvatarRequest;

@Service( "ProfileAvatarRepositoryImpl" )
public class ProfileAvatarRepositoryImpl implements ProfileAvatarRepository
{

  private static final Log log = LogFactory.getLog( ProfileAvatarRepositoryImpl.class );

  @Override
  public ResponseEntity<AvatarView> uploadAvatar( String personId, UploadAvatarRequest uploadAvatarRequest ) throws ServiceErrorException, Exception
  {
    Map<String, Object> param = new HashMap<>();
    param.put( "personId", personId );

    ResponseEntity<AvatarView> avatarView = null;

    try
    {
      avatarView = MeshServicesUtil.getRestWebClient().exchange( MeshServicesUtil.getNackleMeshServicesBaseEndPoint() + "/profile/avatars/{personId}",
                                                                 HttpMethod.PUT,
                                                                 new HttpEntity<UploadAvatarRequest>( uploadAvatarRequest,
                                                                                                      MeshServicesUtil.getAuthorizationHeadersWithCompanyIDAndJWTToken( personId ) ),
                                                                 AvatarView.class,
                                                                 param );
    }
    catch( RestClientException ex )
    {
      log.error( "Exception while uploading avatar : " + ex );
    }
    catch( Exception exception )
    {
      log.error( "Exception while uploading video : " + exception );
      throw new ServiceErrorException( exception.getMessage() );
    }

    return avatarView;

  }

}
