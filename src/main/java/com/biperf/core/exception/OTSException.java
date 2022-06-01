
package com.biperf.core.exception;

/**
 * OTSException.
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
 * <td>Saravanan Sivanandam</td>
 * <td>Nov 21, 2017</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */

@SuppressWarnings( "serial" )
public class OTSException extends Exception
{
  private String responseMessage;

  public OTSException( String responseMessage )
  {
    super( responseMessage );
    this.responseMessage = responseMessage;
  }

  public String getResponseMessage()
  {
    return responseMessage;
  }
}
