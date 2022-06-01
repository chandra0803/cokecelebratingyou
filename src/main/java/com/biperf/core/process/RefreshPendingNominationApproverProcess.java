
package com.biperf.core.process;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.dao.nomination.NominationDAO;
import com.biperf.core.domain.enums.MailingType;
import com.biperf.core.domain.mailing.Mailing;
import com.biperf.core.domain.mailing.MailingRecipient;
import com.biperf.core.domain.user.User;
import com.biperf.core.service.message.MessageService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.utils.UserManager;

public class RefreshPendingNominationApproverProcess extends BaseProcessImpl
{
  public static final String BEAN_NAME = "refreshPendingNominationApproverProcess";

  private static final Log log = LogFactory.getLog( RefreshPendingNominationApproverProcess.class );

  private NominationDAO nominationDAO;

  public void onExecute()
  {
    try
    {
      Map<String, Object> parameters = new HashMap<String, Object>();
      parameters.put( "userId", UserManager.getUserId() );
      Map<String, Object> prcResult = nominationDAO.refreshPendingNominationApprover( parameters );
      sendSummaryMessage( prcResult );
    }
    catch( Exception e )
    {
      log.error( "An exception occurred while invoking prc_refresh_pend_nom_approver." + "(process invocation ID = " + getProcessInvocationId() + ")", e );
      addComment( "An exception occurred while invoking prc_refresh_pend_nom_approver.  " + "See the log file for additional information.  " + "(process invocation ID = " + getProcessInvocationId()
          + ")" );
    }
  }

  private void sendSummaryMessage( Map<String, Object> prcResult )
  {
    User recipientUser = getRunByUser();

    Map<String, String> objectMap = new HashMap<String, String>();
    objectMap.put( "clientPrefix", systemVariableService.getPropertyByName( SystemVariableService.CLIENT_PREFIX ).getStringVal() );
    objectMap.put( "status", "Success" );
    int returnCode = ( (BigDecimal)prcResult.get( "p_out_returncode" ) ).intValue();

    if ( returnCode == 99 )
    {
      objectMap.put( "status", "Failed" );
    }

    objectMap.put( "processName", BEAN_NAME );

    Mailing mailing = composeMail( MessageService.REFRESH_PENDING_NOMINATION_APPROVER_SUMMARY_MESSAGE_CM_ASSET_CODE, MailingType.PROCESS_EMAIL );

    MailingRecipient mr = addRecipient( recipientUser );
    mailing.addMailingRecipient( mr );

    try
    {

      mailingService.submitMailing( mailing, objectMap );

      log.debug( "--------------------------------------------------------------------------------" );
      log.debug( "processName: " + BEAN_NAME + " has been sent to:" );
      log.debug( "Run By User: " + recipientUser.getFirstName() + " " + recipientUser.getLastName() );
      log.debug( "--------------------------------------------------------------------------------" );

      addComment( "processName: " + BEAN_NAME + " -Refresh Pending Nomination Approver  Summary email has been sent to: " + recipientUser.getFirstName() + " " + recipientUser.getLastName() );
    }
    catch( Exception e )
    {
      log.error( "An exception occurred while Refresh Pending Nomination Approver  " + "(process invocation ID = " + getProcessInvocationId() + ")", e );
      addComment( "An exception occurred while Refresh Pending Nomination Approver .  " + "See the log file for additional information.  " + "(process invocation ID = " + getProcessInvocationId()
          + ")" );
    }
  }

  public void setNominationDAO( NominationDAO nominationDAO )
  {
    this.nominationDAO = nominationDAO;
  }

}
