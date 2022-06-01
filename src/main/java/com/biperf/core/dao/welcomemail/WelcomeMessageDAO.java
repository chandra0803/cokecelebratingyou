
package com.biperf.core.dao.welcomemail;

import java.util.List;
import java.util.Map;

import com.biperf.core.dao.DAO;
import com.biperf.core.domain.welcomemail.WelcomeMessage;

/**
 * 
 * WelcomeMessageDAO.
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
public interface WelcomeMessageDAO extends DAO
{

  public static final String BEAN_NAME = "welcomeMessageDAO";

  /**
   * 
   * @param welcomeMessage
   * @return WelcomeMessage
   */
  public WelcomeMessage saveWelcomeMessage( WelcomeMessage welcomeMessage );

  public List getAllWelcomeMessages();

  public WelcomeMessage getWelcomeMessageById( Long welcomeMessageId );

  public void deleteWelcomeMessagesForm( WelcomeMessage welcomeMessage );

  public List getAllMessageByNotificationDate();

  public void truncateStrongMailUserTable();

  public Map<String, Object> executeWelcomeEmailProcedure();
}
