
package com.biperf.core.process;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.domain.enums.MailingType;
import com.biperf.core.domain.mailing.Mailing;
import com.biperf.core.domain.mailing.MailingRecipient;
import com.biperf.core.domain.message.Message;
import com.biperf.core.domain.ssi.SSIContest;
import com.biperf.core.domain.user.User;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.cms.CMAssetService;
import com.biperf.core.service.message.MessageService;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.service.ssi.SSIContestService;
import com.biperf.core.service.system.SystemVariableService;
import com.objectpartners.cms.util.CmsUtil;

public class SSIContestStackRankUpdateProcess extends BaseProcessImpl
{
  private static final Log log = LogFactory.getLog( SSIContestStackRankUpdateProcess.class );

  public static final String PROCESS_NAME = "SSI Contest StackRank Update Process";
  public static final String BEAN_NAME = "ssiContestStackRankUpdateProcess";

  public static final String BI_ADMIN = "BI-ADMIN";
  private SSIContestService ssiContestService;
  private UserService userService;
  private CMAssetService cmAssetService;

  private Long contestId;

  protected void onExecute()
  {
    try
    {
      ssiContestService.updateContestStackRank( contestId );
    }
    catch( ServiceErrorException e )
    {
      logErrorMessage( e );
      sendFailureEmail();
    }

  }

  private void sendFailureEmail()
  {
    User recipientUser = userService.getUserByUserName( BI_ADMIN );

    // Compose the mailing
    Message m = messageService.getMessageByCMAssetCode( MessageService.SSI_CONTEST_STACKRANK_UPDATE_MESSAGE_CM_ASSETCODE );
    Mailing mailing = composeMail( m.getId(), MailingType.PROCESS_EMAIL );

    // Add the recipient
    MailingRecipient mr = addRecipient( recipientUser );
    mailing.addMailingRecipient( mr );

    SSIContest contest = ssiContestService.getContestById( contestId );
    Map objectMap = new HashMap();
    objectMap.put( "processName", BEAN_NAME );
    objectMap.put( "contestName", getContestName( contest, mr.getLocale() ) );
    objectMap.put( "contestId", contest.getId() );
    objectMap.put( "clientName", systemVariableService.getPropertyByName( SystemVariableService.CLIENT_NAME ).getStringVal() );
    objectMap.put( "clientPrefix", systemVariableService.getPropertyByName( SystemVariableService.CLIENT_PREFIX ).getStringVal() );

    // Send the e-mail message with personalization
    try
    {
      mailingService.submitMailing( mailing, objectMap );

      log.debug( "--------------------------------------------------------------------------------" );
      log.debug( "process: " + BEAN_NAME + " has been sent to:" );
      log.debug( "Run By User: " + recipientUser.getFirstName() + " " + recipientUser.getLastName() );
      log.debug( "--------------------------------------------------------------------------------" );

      addComment( "process: " + BEAN_NAME + " has been sent to the process scheduler " + recipientUser.getFirstName() + " " + recipientUser.getLastName() + ". Total number of promotions swept: 0" );
    }
    catch( Exception e )
    {
      log.error( "An exception occurred while sending " + BEAN_NAME + " (process invocation ID = " + getProcessInvocationId() + ")", e );
      addComment( "An exception occurred while sending " + BEAN_NAME + ". See the log file for additional information.  " + "(process invocation ID = " + getProcessInvocationId() + ")" );
    }
  }

  private String getContestName( SSIContest contest, String mailingRecipientLocale )
  {
    return cmAssetService.getString( contest.getCmAssetCode(), SSIContest.CONTEST_CMASSET_NAME, getLocale( mailingRecipientLocale ), true );
  }

  private Locale getLocale( String locale )
  {
    return CmsUtil.getLocale( locale );
  }

  public void setSsiContestService( SSIContestService ssiContestService )
  {
    this.ssiContestService = ssiContestService;
  }

  public void setUserService( UserService userService )
  {
    this.userService = userService;
  }

  public void setCmAssetService( CMAssetService cmAssetService )
  {
    this.cmAssetService = cmAssetService;
  }

  public Long getContestId()
  {
    return contestId;
  }

  public void setContestId( Long contestId )
  {
    this.contestId = contestId;
  }

}
