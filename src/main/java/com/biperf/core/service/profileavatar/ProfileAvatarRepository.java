
package com.biperf.core.service.profileavatar;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.SAO;
import com.biperf.core.value.profile.v1.uploadavatar.AvatarView;
import com.biperf.core.value.profile.v1.uploadavatar.UploadAvatarRequest;

@Component
public interface ProfileAvatarRepository extends SAO
{
  public static final String BEAN_NAME = "profileAvatarRepository";

  public ResponseEntity<AvatarView> uploadAvatar( String personId, UploadAvatarRequest uploadAvatarRequest ) throws ServiceErrorException, Exception;

}
