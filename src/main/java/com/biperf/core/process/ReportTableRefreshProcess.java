/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/process/ReportTableRefreshProcess.java,v $
 */

package com.biperf.core.process;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.domain.enums.MailingType;
import com.biperf.core.domain.mailing.Mailing;
import com.biperf.core.domain.mailing.MailingRecipient;
import com.biperf.core.domain.user.User;
import com.biperf.core.service.message.MessageService;
import com.biperf.core.service.reporttablerefresh.ReportTableRefreshService;
import com.biperf.core.utils.UserManager;

/**
 * This process is to send monthly electronic statements to all participants.
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
 * <td>sathish</td>
 * <td>Nov 30, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ReportTableRefreshProcess extends BaseProcessImpl
{
  public static final String BEAN_NAME = "reportTableRefreshProcess";
  public static final String EMAIL_MESSAGE_NAME = "Report Refresh Process";

  private static final Log log = LogFactory.getLog( ReportTableRefreshProcess.class );

  private ReportTableRefreshService reportTableRefreshService;

  @SuppressWarnings( "rawtypes" )
  public void onExecute()
  {
    if ( log.isDebugEnabled() )
    {
      log.debug( "Starting the report table refresh service" );
    }
    Map returnMap = this.getReportTableRefreshService().reportTableCoreRefresh( UserManager.getUserId() );
    if ( log.isDebugEnabled() )
    {
      log.debug( "Finished report refresh - " + returnMap );
    }
    sendMessage( returnMap );
  }

  @SuppressWarnings( "rawtypes" )
  private void sendMessage( Map returnMap )
  {
    User runByUser = getRunByUser();

    // Compose the mailing.
    Mailing mailing = composeMail( MessageService.REPORT_TABLE_REFRESH_PROCESS_MESSAGE_CM_ASSET_CODE, MailingType.PROCESS_EMAIL );

    // Collect e-mail message parameters.
    Map<String, Object> objectMap = new HashMap<String, Object>();
    objectMap.put( "firstName", runByUser.getFirstName() );
    objectMap.put( "lastName", runByUser.getLastName() );
    objectMap.put( "resultMessage", (String)returnMap.get( "po_error_message" ) );

    // Add the recipient.
    MailingRecipient mailingRecipient = addRecipient( runByUser );
    mailing.addMailingRecipient( mailingRecipient );

    // Send the e-mail message.
    try
    {
      mailingService.submitMailing( mailing, objectMap );

      log.debug( "------------------------------------------------------------------------------" );
      log.debug( "Process " + BEAN_NAME + " sent an email message to " + runByUser.getFirstName() + " " + runByUser.getLastName() + "." );
      log.debug( "Process " + BEAN_NAME + " was run by user " + runByUser.getFirstName() + " " + runByUser.getLastName() + "." );
      log.debug( "------------------------------------------------------------------------------" );

      addComment( (String)returnMap.get( "po_error_message" ) );
    }
    catch( Exception e )
    {
      log.error( "An exception occurred while sending a " + EMAIL_MESSAGE_NAME + " email message. " + "(process invocation ID = " + getProcessInvocationId() + ")", e );

      addComment( "An exception occurred while sending a " + EMAIL_MESSAGE_NAME + " email message. " + "See the log file for additional information.  " + "(process invocation ID = "
          + getProcessInvocationId() + ")" );
    }
  }

  private ReportTableRefreshService getReportTableRefreshService()
  {
    return this.reportTableRefreshService;
  }

  public void setReportTableRefreshService( ReportTableRefreshService reportTableRefreshService )
  {
    this.reportTableRefreshService = reportTableRefreshService;
  }

}
