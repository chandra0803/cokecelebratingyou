/**
 * 
 */

package com.biperf.core.service.profileavatar;

import org.json.JSONException;
import org.springframework.http.ResponseEntity;

import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.SAO;
import com.biperf.core.value.profile.v1.uploadavatar.AvatarView;
import com.biperf.core.value.profile.v1.uploadavatar.UploadAvatarRequest;

/**
 * ProfileAvatarService.
 * <p>
 * <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>esakkimu</td>
 * <td>Nov 5, 2018</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public interface ProfileAvatarService extends SAO
{
  public static final String BEAN_NAME = "profileAvatarService";

  public ResponseEntity<AvatarView> uploadAvatar( String personId, UploadAvatarRequest uploadAvatarRequest ) throws ServiceErrorException, JSONException, Exception;

  public String uploadDefaultPurlAvatar( String personId, String imgData ) throws ServiceErrorException, JSONException, Exception;
  
  public String uploadDefaultPurlPicture( String personId, String imgData ) throws ServiceErrorException, JSONException, Exception;
}
