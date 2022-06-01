
package com.biperf.core.utils;

import java.util.Objects;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.biperf.core.value.profile.v1.uploadavatar.AvatarView;

public class ProfileAvatarUploadUtil
{

  public static String getImageData( String contentType, String imageInBytes )
  {
    StringBuilder imageDataSb = new StringBuilder();
    imageDataSb.append( "data:" );
    imageDataSb.append( contentType );
    imageDataSb.append( ";base64," );
    imageDataSb.append( imageInBytes );

    return imageDataSb.toString();

  }

  public static boolean isValidResponse( ResponseEntity<AvatarView> avatarView )
  {

    return Objects.nonNull( avatarView ) && avatarView.getStatusCode() == HttpStatus.OK && Objects.nonNull( avatarView.getBody().getAvatarUrl() );

  }

  public static String getContentType( String imageName )
  {
    return "image/" + ImageUtils.getFileExtension( imageName );
  }
}
