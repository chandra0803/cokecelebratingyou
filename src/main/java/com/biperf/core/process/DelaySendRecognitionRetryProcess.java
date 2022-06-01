/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/process/DelaySendRecognitionRetryProcess.java,v $
 */

package com.biperf.core.process;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.common.util.StringUtils;

import com.biperf.core.domain.promotion.ScheduledRecognition;
import com.biperf.core.domain.system.PropertySetItem;
import com.biperf.core.domain.user.User;
import com.biperf.core.exception.BeaconRuntimeException;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.email.EmailHeader;
import com.biperf.core.service.email.EmailService;
import com.biperf.core.service.email.TextEmailBody;
import com.biperf.core.service.scheduledrecognition.ScheduledRecognitionService;
import com.biperf.core.service.system.SystemVariableService;

/**
 * 
 * DelaySendRecognitionRetryProcess.
 * 
 * @author bethke
 * @since Dec 5, 2014
 * @version 1.0
 */
public class DelaySendRecognitionRetryProcess extends BaseProcessImpl
{
  public static final String BEAN_NAME = "delaySendRecognitionRetryProcess";
  public static final String MESSAGE_NAME = "Delay Send Recognition Retry Process";

  private ScheduledRecognitionService scheduledRecognitionService;
  private EmailService emailService;

  /**
   * The logger for this class.
   */
  private static final Log log = LogFactory.getLog( DelaySendRecognitionRetryProcess.class );

  /**
   * Overridden from
   * 
   * @see com.biperf.core.process.BaseProcessImpl#onExecute()
   */
  public void onExecute()
  {
    int totalRescheduled = 0;
    String processComment = "";

    List<Long> delayRetryIds = scheduledRecognitionService.getScheduledRecognitionIdsForRetryProcess();

    if ( delayRetryIds != null && !delayRetryIds.isEmpty() )
    {
      log.error( MESSAGE_NAME + " : " + delayRetryIds.size() + " scheduled_recog_id's to be processed." );

      try
      {

        for ( Long scheduledRecogId : delayRetryIds )
        {
          ScheduledRecognition scheduledRecognition = scheduledRecognitionService.getScheduledRecognitionById( scheduledRecogId );

          scheduledRecognitionService.scheduleDelaySendRecognitionProcess( scheduledRecognition, scheduledRecognition.getDeliveryDate(), new Long( 0 ) );
          totalRescheduled++;
        }
      }
      catch( Exception e )
      {
        processComment = "The process did not complete successfully.";
        logErrorMessage( e );
      }
    }

    // Notify the administrator.
    if ( StringUtils.isEmpty( processComment ) )
    {
      processComment = "Total recognition re-scheduled = " + totalRescheduled + " The scheduled_recog_id's re-scheduled are " + delayRetryIds;
    }
    sendSummaryMessage( processComment );
    addComment( processComment );
  }

  protected void sendSummaryMessage( String comment )
  {
    User runByUser = getRunByUser();

    String subject = MESSAGE_NAME;
    EmailHeader header = new EmailHeader();

    PropertySetItem sysProperty = systemVariableService.getPropertyByName( SystemVariableService.SYSTEM_EMAIL_ADDRESS );
    header.setSender( sysProperty.getStringVal() );

    String runByUserEmailAddr = userService.getPrimaryUserEmailAddress( runByUser.getId() ).getEmailAddr();
    header.setRecipients( new String[] { runByUserEmailAddr } );

    subject = "[ " + systemVariableService.getPropertyByName( SystemVariableService.CLIENT_NAME ).getStringVal() + " ] " + subject;
    header.setSubject( subject );

    String name = "";
    StringBuffer bodyHeader = null;

    bodyHeader = new StringBuffer();
    bodyHeader.append( comment );
    bodyHeader.append( "\n" );

    name = bodyHeader.toString();

    TextEmailBody body = new TextEmailBody( name );
    try
    {
      emailService.sendMessage( header, body );
    }
    catch( ServiceErrorException e )
    {
      throw new BeaconRuntimeException( "Error sending message", e );
    }
  }

  public void setScheduledRecognitionService( ScheduledRecognitionService scheduledRecognitionService )
  {
    this.scheduledRecognitionService = scheduledRecognitionService;
  }

  public void setEmailService( EmailService emailService )
  {
    this.emailService = emailService;
  }

}
