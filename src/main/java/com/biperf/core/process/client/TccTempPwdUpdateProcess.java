package com.biperf.core.process.client;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.domain.enums.MailingType;
import com.biperf.core.domain.mailing.Mailing;
import com.biperf.core.domain.mailing.MailingRecipient;
import com.biperf.core.domain.user.User;
import com.biperf.core.process.BaseProcessImpl;
import com.biperf.core.service.client.TccTempPwdExpiredProcessService;
import com.biperf.core.service.message.MessageService;
import com.biperf.core.service.system.SystemVariableService;
/**
 * TODO Javadoc for TccTempPwdUpdateProcess.
 * 
 * @author Ramesh Jaligama
 * @since 05-Sept-2016
 * @version 1.0
 */


public class TccTempPwdUpdateProcess extends BaseProcessImpl
{

	  public static final String BEAN_NAME = "TccTempPwdUpdateProcess";

	  public static final String MESSAGE_NAME = "TCC ADIH Password EXPIRE Process Notification";

	  private TccTempPwdExpiredProcessService tccTempPwdExpiredProcessService;
	/**
	   * The logger for this class.
	   */
		private static final Log logger = LogFactory.getLog( TccTempPwdUpdateProcess.class );

	  // properties set from jobDataMap
	  

	  /**
	   * Overridden from
	   * 
	   * @see com.biperf.core.process.BaseProcessImpl#onExecute()
	   */
	  public void onExecute()
	  {
	    logger.info( "processName: " + BEAN_NAME + " - Calling Tcc Temp password Expired Procedure." );
	    //addComment( "processName: " + BEAN_NAME + " - Calling UPS Register Calc Procedure." );

	    Map resultMap = tccTempPwdExpiredProcessService.callTccTempPasswordExpireCalcSP();

	    long resultCode = ( (BigDecimal)resultMap.get( "p_out_return_code" ) ).longValue();
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

	    logger.info( "processName: " + BEAN_NAME + " -Calling Tcc Temp password Expired Procedure." );

	    // Notify the administrator.
	    sendSummaryMessage( success );
	  }

	  /**
	   * Composes and sends a summary e-mail to the "run by" user.
	   * 
	   * @param toDate
	   */
	  private void sendSummaryMessage( boolean success )
	  {
	    User recipientUser = getRunByUser();

	    // Add the summary info to the objectMap
	    Map<String, Object> objectMap = new HashMap<String, Object>();
	    objectMap.put( "firstName", recipientUser.getFirstName() );
	    objectMap.put( "lastName", recipientUser.getLastName() );
	    objectMap.put( "processName", BEAN_NAME );
	    objectMap.put( "clientName", systemVariableService.getPropertyByName( SystemVariableService.CLIENT_NAME ).getStringVal() );
	    objectMap.put( "clientPrefix", systemVariableService.getPropertyByName( SystemVariableService.CLIENT_PREFIX ).getStringVal() );

	    objectMap.put( "success", "Completed. " );

	    if ( success )
	    {
	      objectMap.put( "success", "Completed without errors. " );
	    }
	    else
	    {
	      objectMap.put( "success", "Completed with errors.  Please see execution log for errors." );
	    }

	    // Compose the mailing
	    Mailing mailing = composeMail( MessageService.TCC_ADIH_EXPIRE_TEMP_PWD, MailingType.PROCESS_EMAIL );

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
	  
	  
		public void setTccTempPwdExpiredProcessService(
				TccTempPwdExpiredProcessService tccTempPwdExpiredProcessService) {
			this.tccTempPwdExpiredProcessService = tccTempPwdExpiredProcessService;
		}

		

		public TccTempPwdExpiredProcessService getTccTempPwdExpiredProcessService() {
			return tccTempPwdExpiredProcessService;
		}
	  
	}

