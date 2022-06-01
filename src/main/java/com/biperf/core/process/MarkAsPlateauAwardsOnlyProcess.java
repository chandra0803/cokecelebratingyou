
package com.biperf.core.process;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.service.participant.ParticipantService;

/**
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
 * <td>Bala</td>
 * <td>Dec 06, 2011</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */

public class MarkAsPlateauAwardsOnlyProcess extends BaseProcessImpl
{
  private static final Log log = LogFactory.getLog( MarkAsPlateauAwardsOnlyProcess.class );

  public static final String BEAN_NAME = "markAsPlateauAwardsOnlyProcess";

  /**
   * Return code returned from the stored procedure
   */
  public static final String OUTPUT_RETURN_CODE = "p_out_return_code";

  /**
    * Stored proc returns this code when the stored procedure executed without errors
    */
  public static final String GOOD = "00";

  private ParticipantService participantService;

  public MarkAsPlateauAwardsOnlyProcess()
  {
    super();
  }

  public void onExecute()
  {
    Map output = participantService.markAsPlateauAwardsOnly();

    if ( GOOD.equals( output.get( OUTPUT_RETURN_CODE ) ) )
    {
      addComment( "Process,processName: " + BEAN_NAME + " ,has been completed successfully." );
    }
    else
    {
      StringBuffer msg = new StringBuffer( "Process Failed: Uncaught Exception running Process " + "with process bean name: (" );
      msg.append( BEAN_NAME ).append( "). The error caused by: " + " return code = " + output.get( OUTPUT_RETURN_CODE ) );

      log.warn( msg );
      addComment( msg.toString() );
    }
  }

  public void setParticipantService( ParticipantService participantService )
  {
    this.participantService = participantService;
  }
}
