
package com.biperf.core.process.client;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.domain.enums.MailingType;
import com.biperf.core.domain.mailing.Mailing;
import com.biperf.core.domain.mailing.MailingRecipient;
import com.biperf.core.domain.user.User;
import com.biperf.core.process.BaseProcessImpl;
import com.biperf.core.service.client.GenerateBudgetFileProcessService;
import com.biperf.core.service.message.MessageService;
import com.biperf.core.service.system.SystemVariableService;

public class GenerateBudgetFileProcess extends BaseProcessImpl
{

	public static final String BEAN_NAME = "generateBudgetFileProcess";

	public static final String MESSAGE_NAME = "Generate Budget File Process";

	private GenerateBudgetFileProcessService generateBudgetFileProcessService;
	  
	private String orgUnits;
	private String level;	  

	/**
	   * The logger for this class.
	   */
	private static final Log logger = LogFactory.getLog( GenerateBudgetFileProcess.class );

	  // properties set from jobDataMap
	  

	  /**
	   * Overridden from
	   * 
	   * @see com.biperf.core.process.BaseProcessImpl#onExecute()
	   */
	@SuppressWarnings({ "rawtypes", "unused" })
	protected void onExecute() 
	{	      
		logger.info( "processName: " + BEAN_NAME + " - Calling Generate Budget File Procedure" );
	    addComment( "processName: " + BEAN_NAME + " - Calling Generate Budget File  Procedure" );

	    // Call the stored procedure
  	    Map resultMap;	    
        if ( "Count only with in the org unit".equalsIgnoreCase( level ) )
        {
    	     resultMap = generateBudgetFileProcessService.callGenerateBudgetFileProc(orgUnits, 0 );
        }
        else
        {
    	     resultMap = generateBudgetFileProcessService.callGenerateBudgetFileProc(orgUnits, 1 );	
        }	    	    
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

	    logger.info( "processName: " + BEAN_NAME + " - Completed Generate Budget File Procedure" );
	    addComment( "processName: " + BEAN_NAME + " - Completed Generate Budget File Procedure" );

	    // Send an email to the user who schedules/launches this process
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
	    Mailing mailing = composeMail( MessageService.GENERATE_BUDGET_FILE_PROCESS_MESSAGE_CM_ASSET_CODE, MailingType.PROCESS_EMAIL );

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


	public String getOrgUnits() 
	{
		return orgUnits;
	}
	
	public void setOrgUnits(String orgUnits) 
	{
		this.orgUnits = orgUnits;
	}

	public String getLevel() 
	{
		return level;
	}
	public void setLevel(String level) 
	{
		this.level = level;
	}	
	  
	public GenerateBudgetFileProcessService getGenerateBudgetFileProcessService() 
	{
		return generateBudgetFileProcessService;
	}

	public void setGenerateBudgetFileProcessService(
			GenerateBudgetFileProcessService generateBudgetFileProcessService) 
	{
		this.generateBudgetFileProcessService = generateBudgetFileProcessService;
	}
}
