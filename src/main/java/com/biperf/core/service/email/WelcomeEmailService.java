
package com.biperf.core.service.email;

import java.io.File;
import java.util.List;

import com.biperf.core.domain.message.Message;
import com.biperf.core.domain.user.User;
import com.biperf.core.service.SAO;
import com.biperf.core.value.UserValueBean;

/**
 * 
 * WelcomeEmailService.
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
 * <td>kunaseka</td>
 * <td>Sep 18, 2009</td>
 * <td>1.0</td>
 * <td>modified</td>
 * </tr>
 * </table>
 *
 */

public interface WelcomeEmailService extends SAO
{
  public static final String BEAN_NAME = "welcomeEmailService";

  public void sendWelcomeEmail( UserValueBean userValueBean, String password, User runByUser, boolean notice, File file );

  public void resendWelcomeEmail( List<Long> userIds );

  public void resendWelcomeEmail( Long userId );

  public void sendWelcomeEmailCountToAdmin( int count, File attachment );

  public void sendNewWelcomeEmail( User pax, Message message, String passwordToken, User runByUser, boolean isUniqueEmail );

  public void updateUser( Long userId );

}
