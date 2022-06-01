/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/utils/ClientStatePasswordManager.java,v $
 */

package com.biperf.core.utils;

/*
 * ClientStatePasswordManager <p> <b>Change History:</b><br> <table border="1"> <tr> <th>Author</th>
 * <th>Date</th> <th>Version</th> <th>Comments</th> </tr> <tr> <td>Thomas Eaton</td> <td>Oct
 * 6, 2005</td> <td>1.0</td> <td>created</td> </tr> </table> </p>
 * 
 *
 */

public class ClientStatePasswordManager
{
  // ---------------------------------------------------------------------------
  // Public Methods
  // ---------------------------------------------------------------------------

  /**
   * Returns the client state password stored in resource-manager scope.
   * 
   * @return the client state password stored in resource-manager scope.
   */
  public static String getPassword()
  {
    return (String)ResourceManager.getResource( ClientStateConstants.CLIENT_STATE_PASSWORD_KEY );
  }

  /**
   * Removes the client state password from resource-manager scope.
   */
  public static void removePassword()
  {
    ResourceManager.removeResource( ClientStateConstants.CLIENT_STATE_PASSWORD_KEY );
  }

  /**
   * Stores the client state password in resource-manager scope.
   * 
   * @param password the client state password to store.
   */
  public static void setPassword( String password )
  {
    ResourceManager.setResource( ClientStateConstants.CLIENT_STATE_PASSWORD_KEY, password );
  }

  /**
   * The global password is used for encrypting that needs to be done accross sessions. eg. Sending
   * encrypted links in an email.
   * 
   * @return Password used when not using session
   */
  public static String getGlobalPassword()
  {
    return ClientStateConstants.CLIENT_STATE_GLOBAL_PASSWORD;
  }
}
