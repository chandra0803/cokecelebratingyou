
package com.biperf.core.service.welcomemail.impl;

import java.util.Iterator;
import java.util.List;

import com.biperf.core.dao.welcomemail.WelcomeMessageDAO;
import com.biperf.core.domain.welcomemail.WelcomeMessage;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.welcomemail.WelcomeMessageService;

/**
 * 
 * WelcomeMessageServiceImpl.
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
public class WelcomeMessageServiceImpl implements WelcomeMessageService
{

  private WelcomeMessageDAO welcomeMessageDAO;

  /**
   * 
   * Overridden from @see com.biperf.core.service.welcomemail.WelcomeMessageService#saveWelcomeMessage(com.biperf.core.domain.welcomemail.WelcomeMessage)
   * @param welcomeMessage
   * @return welcomeMessage
   * @throws ServiceErrorException
   */
  public WelcomeMessage saveWelcomeMessage( WelcomeMessage welcomeMessage ) throws ServiceErrorException
  {
    this.welcomeMessageDAO.saveWelcomeMessage( welcomeMessage );

    return welcomeMessage;
  }

  /**
   * 
   * Overridden from @see com.biperf.core.service.welcomemail.WelcomeMessageService#getAllMessage()
   * @return list
   * @throws ServiceErrorException
   */
  public List getAllWelcomeMessages() throws ServiceErrorException
  {
    List list = this.welcomeMessageDAO.getAllWelcomeMessages();

    return list;
  }

  /**
   * 
   * Overridden from @see com.biperf.core.service.welcomemail.WelcomeMessageService#getAllMessageByNotificationDate()
   * @return list
   */
  public List getAllMessageByNotificationDate()
  {
    List list = this.welcomeMessageDAO.getAllMessageByNotificationDate();

    return list;
  }

  /**
   * 
   * @param welcomeMessageDAO
   */
  public void setWelcomeMessageDAO( WelcomeMessageDAO welcomeMessageDAO )
  {
    this.welcomeMessageDAO = welcomeMessageDAO;
  }

  /**
   * 
   * Overridden from @see com.biperf.core.service.welcomemail.WelcomeMessageService#deleteWelcomeMessages(java.util.List)
   * @param welcomemessageIds
   * @throws ServiceErrorException
   */
  public void deleteWelcomeMessages( List welcomemessageIds ) throws ServiceErrorException
  {
    Iterator iter = welcomemessageIds.iterator();
    while ( iter.hasNext() )
    {
      Long welcomemessageId = (Long)iter.next();

      this.deleteWelcomeMessagesForm( welcomemessageId );
    }
  }

  /**
   * 
   * @param welcomemessageId
   * @throws ServiceErrorException
   */
  public void deleteWelcomeMessagesForm( Long welcomemessageId ) throws ServiceErrorException
  {
    WelcomeMessage welcomeMessage = this.welcomeMessageDAO.getWelcomeMessageById( welcomemessageId );
    this.welcomeMessageDAO.deleteWelcomeMessagesForm( welcomeMessage );

  }

}
