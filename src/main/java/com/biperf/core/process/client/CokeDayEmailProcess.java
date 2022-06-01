
package com.biperf.core.process.client;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.domain.enums.MailingType;
import com.biperf.core.domain.mailing.Mailing;
import com.biperf.core.domain.mailing.MailingRecipient;
import com.biperf.core.domain.merchandise.MerchOrder;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.user.User;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.process.BaseProcessImpl;
import com.biperf.core.service.awardbanq.AwardBanQServiceFactory;
import com.biperf.core.service.awardbanq.GiftcodeStatusResponseValueObject;
import com.biperf.core.service.client.CokeProcessService;
import com.biperf.core.service.merchlevel.MerchLevelService;
import com.biperf.core.service.merchorder.MerchOrderService;
import com.biperf.core.service.message.MessageService;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.participant.UserCharacteristicService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.value.client.CokeDayPaxValueBean;
import com.biperf.core.value.client.ParticipantEmployerValueBean;

/**
 * ClientGiftCodeSweepProcess.
 * 
 * @author Dudam
 * @since Nov 1, 2016
 * @version 1.0
 */
public class CokeDayEmailProcess extends BaseProcessImpl
{

  public static final String BEAN_NAME = "cokeDayEmailProcess";
  public static final String MESSAGE_NAME = "Coke Day Email Process Notification";
  
  private CokeProcessService cokeProcessService;

 private ParticipantService participantService;


  /**
   * The log for this class.
   */
  private static final Log log = LogFactory.getLog( CokeDayEmailProcess.class );

  /**
   * Overridden from
   * 
   * @see com.biperf.core.process.BaseProcessImpl#onExecute()
   */
  public void onExecute()
  {
    log.info( "processName: " + BEAN_NAME + " - Calling Coke Day Email Process ");
    addComment( "processName: " + BEAN_NAME + " - Calling Coke Day Email Process " );
    

    List<CokeDayPaxValueBean> cokeDayPaxValueBeanList = cokeProcessService.getCokeDayServicePax();
    	
    	int totalAnniversaryEmployees = ( cokeDayPaxValueBeanList != null ? cokeDayPaxValueBeanList.size() : 0 );
    	
    	if(totalAnniversaryEmployees!=0)
		{
    		try
    		{
    			for ( CokeDayPaxValueBean employeeValueBean : cokeDayPaxValueBeanList )
    			{
    				Participant participant = participantService.getParticipantById( employeeValueBean.getUserId() );
    				if (participant!=null )
    				{
    					sendCokeDayEmail( employeeValueBean, participant );
    				}
    			}
    			
    			log.info( "processName: " + BEAN_NAME + " - Completed Coke Day Email Process, Total Anniversary Employees: " + totalAnniversaryEmployees);
    			 addComment( "processName: " + BEAN_NAME + " - Completed Coke Day Email Process, Total Anniversary Employees: " + totalAnniversaryEmployees );
    			          
    		}
    		catch( Exception e )
    	    {
    	      logErrorMessage( e );
    	      log.info("Process did not complete successfully. Error Message = " + e.getMessage() );
    	      addComment( "Process did not complete successfully. Error Message = " + e.getMessage() );
    	    }
        }
    	else
        {
          log.info( "processName: " + BEAN_NAME + " - Completed Coke Day Email Process with ZERO anniversary employees." );
          addComment( "processName: " + BEAN_NAME + " - Completed Coke Day Email Process with ZERO anniversary employees." );
        }

    
  }

  
  private void sendCokeDayEmail( CokeDayPaxValueBean employeeValueBean, Participant participant )
  {
	  User recipientUser = userService.getUserById(employeeValueBean.getUserId());
	  
	  // Compose the mailing
		  Mailing mailing = composeMail( MessageService.COKE_DAY_EMAIL_MESSAGE_CM_ASSET_CODE, MailingType.PROCESS_EMAIL );
		  mailing.addMailingRecipient( addRecipient( participant ) );
		  
		  Map objectMap = new HashMap();
		  objectMap.put( "first_Name", employeeValueBean.getFirstName() );
		  objectMap.put( "Anniversary_Date", employeeValueBean.getHireDate() );
    
		  try
		  {
			  // Send the e-mail message with personalization
			  mailingService.submitMailing( mailing, objectMap );

			  log.debug( "--------------------------------------------------------------------------------" );
			  log.debug( "processName: " + BEAN_NAME + " has been sent to:" );
			  log.debug(  recipientUser.getFirstName() + " " + recipientUser.getLastName() );
			  log.debug( "--------------------------------------------------------------------------------" );

		  }
		  catch( Exception e )
		  {
			  log.error( "An exception occurred while sending " + MESSAGE_NAME + " Summary Email.  " + "(process invocation ID = " + getProcessInvocationId() + ")", e );
			  addComment( "An exception occurred while sending " + MESSAGE_NAME + " Summary Email.  " + "See the log file for additional information.  " + "(process invocation ID = "
          + getProcessInvocationId() + ")" );
		  }
  }

 
  public ParticipantService getParticipantService()
  {
    return participantService;
  }

  public void setParticipantService( ParticipantService participantService )
  {
    this.participantService = participantService;
  }

  public CokeProcessService getCokeProcessService()
  {
    return cokeProcessService;
  }

  public void setCokeProcessService( CokeProcessService cokeProcessService )
  {
    this.cokeProcessService = cokeProcessService;
  }

}
