
package com.biperf.core.security.credentials;

import java.io.Serializable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * ClientSeamlessLogonCredentials.
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
 * <td>robinsra</td>
 * <td>March 29, 2011</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 */
public class ClientSeamlessLogonCredentials implements Serializable, SeamLessLogonCredentials
{
  private static final Log logger = LogFactory.getLog( ClientSeamlessLogonCredentials.class );

  /**
   * Constructor
   *
   */
  public ClientSeamlessLogonCredentials()
  {
  }

  /**
   * This is just a secondary check, as the first check would be whether this user(Id)
   * exist in db or not, which was done by AuthProcessingFilter.authenticate method.
   *  
   * Validate the credentials as per  business rules.
   * Overridden from @see com.biperf.core.security.credentials.SeamLessLogonCredentials#isValid()
   * @return boolean
   */
  public boolean isValid()
  {
    // placeholder for client implementation
    return true;
  }

  public String decrypt( String encryptedVal )
  {
    String decryptedVal = null;
    // decrypt logic
    return decryptedVal;
  }

  public String encrypt( String unencryptedVal )
  {
    String encryptedVal = null;
    // encrypt logic
    return encryptedVal;
  }
}
