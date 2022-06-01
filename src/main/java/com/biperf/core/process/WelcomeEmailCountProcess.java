/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/process/WelcomeEmailCountProcess.java,v $
 */

package com.biperf.core.process;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.domain.welcomemail.WelcomeMessage;

/**
 * WelcomeEmailCountProcess. this is the main process that gives the count of
 * the number of participants supposed to receive the Welcome Emails when the
 * Welcome Email process is run.
 * 
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
 * <td>Nov 20, 2007</td>
 * <td>1.0</td>
 * <td>modified</td>
 * </tr>
 * </table>
 * 
 */
public class WelcomeEmailCountProcess extends WelcomeEmailProcess
{
  private static final Log log = LogFactory.getLog( WelcomeEmailCountProcess.class );

  public static final String BEAN_NAME = "welcomeEmailCountProcess";

  /**
   * 
   */
  public WelcomeEmailCountProcess()
  {
    super();
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.process.BaseProcessImpl#onExecute()
   */
  public void onExecute()
  {
    // creates mailing process if not created already
    createOrLoadMailingProcess();
    if ( !isBounceBackVerified() )
    {
      notifyBounceBackVerifyError();
    }
    else
    {
      List welccomeMessageList = welcomeMessageService.getAllMessageByNotificationDate();
      List allUsers = new ArrayList();
      List successUsers = new ArrayList();
      List failedUserNames = new ArrayList();
      List usersWithoutEmail = new ArrayList();
      String message = null;
      if ( welccomeMessageList != null && welccomeMessageList.size() > 0 )
      {
        for ( Iterator welccomeMessageIter = welccomeMessageList.iterator(); welccomeMessageIter.hasNext(); )
        {
          WelcomeMessage welcomeMessage = (WelcomeMessage)welccomeMessageIter.next();

          findUsersForWelcomeEmail( welcomeMessage, allUsers, successUsers, failedUserNames, usersWithoutEmail );

          // add process log comment
        }
        if ( allUsers.size() == 0 )
        {
          message = "No users found for a welcome email.";
          addComment( message );
        }

        if ( allUsers.size() > 0 && successUsers.size() > 0 )
        {
          message = "Number of users who would successfully receive Welcome Email : " + successUsers.size();
          log.debug( message );
          addComment( message );
        }
      }
      else
      {
        sendMailDefaultAudience( allUsers, successUsers, failedUserNames, usersWithoutEmail );
      }
    }
  }

  protected void sendMailDefaultAudience( List allUsers, List successUsers, List failedUserNames, List usersWithoutEmail )
  {
    findUsersFromDefaultAudience( allUsers, successUsers, failedUserNames, usersWithoutEmail );
    String message = null;
    if ( allUsers.isEmpty() )
    {
      message = "No users found for a welcome email.";
    }
    else
    {
      /*
       * Bug#40711 - do not send email since it resets the password boolean separate =
       * isSeparateEmail(); // fetch user for sample email UserValueBean userValueBean =
       * (UserValueBean)allUsers.get( 0 ); // extract file with all users in it File file =
       * extractFile( allUsers, BEAN_NAME ); // send email with attachment sendWelcomeEmail(
       * userValueBean, failedUserNames, separate, true, file );
       */
      message = "Number of users who would successfully receive Welcome Email : " + successUsers.size();
    }
    // add process log comment
    log.debug( message );
    addComment( message );
  }

}
