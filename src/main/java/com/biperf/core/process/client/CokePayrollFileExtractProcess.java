
package com.biperf.core.process.client;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.domain.enums.MailingType;
import com.biperf.core.domain.mailing.Mailing;
import com.biperf.core.domain.mailing.MailingRecipient;
import com.biperf.core.domain.user.User;
import com.biperf.core.process.BaseProcessImpl;
import com.biperf.core.service.client.CokeClientService;
import com.biperf.core.service.cms.CMAssetService;
import com.biperf.core.service.message.MessageService;
import com.biperf.core.service.reports.WorkHappierReportsService;
import com.biperf.core.utils.UserManager;

public class CokePayrollFileExtractProcess  extends BaseProcessImpl
{
  public static final String BEAN_NAME = "cokePayrollFileExtractProcess";
  public static final String MESSAGE_NAME = "Coke Payroll File Extract Process";

  private static final Log logger = LogFactory.getLog( CokePayrollFileExtractProcess.class );

  private CokeClientService cokeClientService;
 
  public CokePayrollFileExtractProcess()
  {
    super();
  }

  @Override
  protected void onExecute()
  {
	  	logger.info( "processName: " + BEAN_NAME + " - Calling Coke Payroll File Extract Procedure" );
	    addComment( "processName: " + BEAN_NAME + " - Calling Coke Payroll File Extract Procedure" );
	    Map resultMap = getCokeClientService().launchAndProcessCokePayrollFileExtract();

    int resultCode = ( (Integer)resultMap.get( "p_out_return_code" ) ).intValue();
    boolean success;
    if ( resultCode == 0 )
    {
      // success
      success = true;
    }
    else
    {
      // fail
      success = false;
    }

    logger.info( "processName: " + BEAN_NAME + " - Completed Coke Payroll File Extract Procedure" );
    addComment( "processName: " + BEAN_NAME + " - Completed Coke Payroll File Extract Procedure" );

    // Send an email to the user who schedules/launches this process
    sendSummaryMessage( success );	 
  }

  private void sendSummaryMessage( boolean success )
  {
    User recipientUser = getRunByUser();

    // Add the summary info to the objectMap
    Map<String, Object> objectMap = new HashMap<String, Object>();
    objectMap.put( "firstName", recipientUser.getFirstName() );
    objectMap.put( "lastName", recipientUser.getLastName() );
    objectMap.put( "processName", BEAN_NAME );

    if ( success )
    {
      objectMap.put( "success", " is Successfull." );
    }
    else
    {
	  objectMap.put( "success",
                    "either did not complete or completed with errors.  Please see the execution log." );
    } 

    // Compose the mailing
    Mailing mailing = composeMail( MessageService.COKE_PAYROLL_FILE_EXTRACT_PROCESS_MESSAGE_CM_ASSET_CODE, MailingType.PROCESS_EMAIL );

    // Add the recipient
    MailingRecipient mr = addRecipient( recipientUser );
    mailing.addMailingRecipient( mr );

    try
    {
      // Send the e-mail message with personalization
      mailingService.submitMailing( mailing, objectMap );

      logger.debug( "--------------------------------------------------------------------------------" );
      logger.debug( "processName: " + BEAN_NAME + " has been sent to:" );
      logger.debug( "Run By User: " + recipientUser.getFirstName() + " " + recipientUser.getLastName() );
      logger.debug( "--------------------------------------------------------------------------------" );

      addComment( "processName: " + BEAN_NAME + " email has been sent to: " + recipientUser.getFirstName() + " " + recipientUser.getLastName() );
    }
    catch( Exception e )
    {
      logger.error( "An exception occurred while sending " + MESSAGE_NAME + " Summary Email.  " + "(process invocation ID = " + getProcessInvocationId() + ")", e );
      addComment( "An exception occurred while sending " + MESSAGE_NAME + " Summary Email.  " + "See the log file for additional information.  " + "(process invocation ID = "
          + getProcessInvocationId() + ")" );
    }
  }
  
  public CokeClientService getCokeClientService()
  {
    return cokeClientService;
  }

  public void setCokeClientService( CokeClientService cokeClientService )
  {
    this.cokeClientService = cokeClientService;
  }

  
}
