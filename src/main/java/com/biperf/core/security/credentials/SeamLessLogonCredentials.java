
package com.biperf.core.security.credentials;

/**
 * Client programs would implement this SeamLessLogonCredentials.
 * SeamLessLogonCredentials.
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
 * <td>mattam</td>
 * <td>Aug 27, 2010</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 */
public interface SeamLessLogonCredentials
{

  /**
   * 
   * @return boolean.
   */
  public boolean isValid();

}
