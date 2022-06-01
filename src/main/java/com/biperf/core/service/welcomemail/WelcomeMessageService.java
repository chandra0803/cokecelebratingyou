
package com.biperf.core.service.welcomemail;

import java.util.List;

import com.biperf.core.domain.welcomemail.WelcomeMessage;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.SAO;

/**
 * 
 * WelcomeMessageService.
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
 * <td>Ramesh Kunasekaran</td>
 * <td>Sep 18, 2009</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 *
 */
public interface WelcomeMessageService extends SAO
{

  public final String BEAN_NAME = "welcomeMessageService";

  public WelcomeMessage saveWelcomeMessage( WelcomeMessage welcomeMessage ) throws ServiceErrorException;

  public List getAllWelcomeMessages() throws ServiceErrorException;

  public void deleteWelcomeMessages( List list ) throws ServiceErrorException;

  public List getAllMessageByNotificationDate();
}
