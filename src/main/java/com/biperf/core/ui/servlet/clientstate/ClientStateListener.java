/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/servlet/clientstate/ClientStateListener.java,v $
 */

package com.biperf.core.ui.servlet.clientstate;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import com.biperf.core.ui.servlet.SessionUtils;
import com.biperf.core.utils.ClientStateConstants;
import com.biperf.core.utils.crypto.PasswordGenerator;

/*
 * ClientStateListener <p> <b>Change History:</b><br> <table border="1"> <tr> <th>Author</th>
 * <th>Date</th> <th>Version</th> <th>Comments</th> </tr> <tr> <td>Thomas Eaton</td> <td>Oct
 * 6, 2005</td> <td>1.0</td> <td>created</td> </tr> </table> </p>
 * 
 *
 */

public class ClientStateListener implements HttpSessionListener
{
  /**
   * Creates a client state password and stores it in session scope.
   * 
   * @param httpSessionEvent describes the "HTTP session created" event.
   */
  @Override
  public void sessionCreated( HttpSessionEvent httpSessionEvent )
  {
    HttpSession session = httpSessionEvent.getSession();
    String password = PasswordGenerator.generatePassword();

    session.setAttribute( ClientStateConstants.CLIENT_STATE_PASSWORD_KEY, password );
  }

  /**
   * Removes the client state password from session scope.
   * 
   * @param httpSessionEvent describes the "HTTP session destroyed" event.
   */
  @Override
  public void sessionDestroyed( HttpSessionEvent httpSessionEvent )
  {
    HttpSession session = httpSessionEvent.getSession();
    session.removeAttribute( ClientStateConstants.CLIENT_STATE_PASSWORD_KEY );

    SessionUtils.clearUserSession( httpSessionEvent.getSession() );
  }
}
