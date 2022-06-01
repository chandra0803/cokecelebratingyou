
package com.biperf.core.process;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.domain.enums.MailingType;
import com.biperf.core.domain.mailing.Mailing;
import com.biperf.core.domain.user.User;
import com.biperf.core.service.goalquest.GoalQuestDataRepositoryService;
import com.biperf.core.service.message.MessageService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.utils.UserManager;

public class GoalQuestDataRepositoryProcess extends BaseProcessImpl
{
  private static final Log log = LogFactory.getLog( GoalQuestDataRepositoryProcess.class );

  public static final String BEAN_NAME = "goalQuestDataRepositoryProcess";

  public static final String MESSAGE_NAME = "GoalQuest Data Repository Process";

  private GoalQuestDataRepositoryService goalQuestDataRepositoryService;

  /**
   * Return code returned from the stored procedure
   */
  public static final String OUTPUT_RETURN_CODE = "p_out_return_code";
  public static final String OUTPUT_REPORT_PATH = "p_out_report_path";

  /**
   * Result set returned from the stored procedure
   */
  // public static final String OUTPUT_RESULT_SET = "p_out_result_set";

  /**
   * Stored proc returns this code when the stored procedure executed without errors
   */
  public static final int GOOD = 0;
  public static final int MAX_LIMIT_EXCEEDED = 97;
  public static final int NO_SURVEY = 98;

  private String promotionId; // will terminate process if null
  private String locale;

  public GoalQuestDataRepositoryProcess()
  {
    super();
  }

  public void onExecute()
  {

    if ( this.isRequiredProcessParametersMissing() )
    {
      String msg = new String( "Required Parameters missing while executing " + MESSAGE_NAME + " ,promotionId is null." + "(process invocation ID = " + getProcessInvocationId() + ")" );
      log.warn( msg );
      addComment( msg );
    }
    else
    {

      try
      {

        Map output = goalQuestDataRepositoryService.goalQuestDataRepositoryExtract( getAsLong( promotionId ), locale );
        if ( GOOD == Integer.parseInt( output.get( OUTPUT_RETURN_CODE ).toString() ) )
        {
          addComment( "Process,processName: " + BEAN_NAME + " ,has been completed successfully." );

          sendMessage( String.valueOf( output.get( OUTPUT_REPORT_PATH ).toString() ) );
        }
        else if ( MAX_LIMIT_EXCEEDED == Integer.parseInt( output.get( OUTPUT_RETURN_CODE ).toString() ) )
        {
          addComment( "Exceeded Response's Maximum limit." );
        }
        else if ( NO_SURVEY == Integer.parseInt( output.get( OUTPUT_RETURN_CODE ).toString() ) )
        {
          addComment( "Survey has no response yet." );
        }
        else
        {
          StringBuffer msg = new StringBuffer( "Process Failed: Uncaught Exception running Process " + "with process bean name: (" );
          msg.append( BEAN_NAME ).append( "). The error caused by: " + " return code = " + output.get( OUTPUT_RETURN_CODE ) );

          log.warn( msg );
          addComment( msg.toString() );
        }
      }
      catch( Exception e )
      {
        StringBuffer msg = new StringBuffer( "GoalQuestDataRepositoryProcess Terminated : GoalQuestDataRepositoryService thrown above exception" );
        log.warn( msg );
        addComment( msg.toString() );
        logErrorMessage( e );
      }
    }
  }

  private void sendMessage( String outputReportPath )
  {
    // Set up mailing-level personalization data.
    User runByUser = getRunByUser();

    // Add the summary info to the objectMap
    Map objectMap = new HashMap();

    objectMap.put( "clientName", systemVariableService.getPropertyByName( SystemVariableService.CLIENT_NAME ).getStringVal() );
    objectMap.put( "clientPrefix", systemVariableService.getPropertyByName( SystemVariableService.CLIENT_PREFIX ).getStringVal() );
    objectMap.put( "reportPath", outputReportPath );

    // Compose the e-mail message.
    Mailing mailing = composeMail( MessageService.GOALQUEST_DATA_REPOSITORY_PROCESS_MESSAGE_CM_ASSET_CODE, MailingType.PROCESS_EMAIL );

    mailing.addMailingRecipient( addRecipient( runByUser ) );

    try
    {
      // Send the e-mail message with personalization
      mailingService.submitMailing( mailing, objectMap );

      log.debug( "--------------------------------------------------------------------------------" );
      log.debug( "processName: " + BEAN_NAME + " has been sent to:" );
      log.debug( "Run By User: " + runByUser.getFirstName() + " " + runByUser.getLastName() );
      log.debug( "--------------------------------------------------------------------------------" );

      addComment( "processName: " + BEAN_NAME + " has been sent to: " + runByUser.getFirstName() + " " + runByUser.getLastName() );
    }
    catch( Exception e )
    {
      log.error( "An exception occurred while sending " + MESSAGE_NAME + " Summary Email.  " + "(process invocation ID = " + getProcessInvocationId() + ")", e );
      addComment( "An exception occurred while sending " + MESSAGE_NAME + " Summary Email.  " + "See the log file for additional information.  " + "(process invocation ID = "
          + getProcessInvocationId() + ")" );
    }
  }

  public GoalQuestDataRepositoryService getGoalQuestDataRepositoryService()
  {
    return goalQuestDataRepositoryService;
  }

  private boolean isRequiredProcessParametersMissing()
  {
    // If required parameters are missing, write invocation comments and stop
    if ( promotionId == null || "".equals( promotionId.trim() ) )
    {
      return true;
    }
    if ( this.getLocale() == null || "".equals( this.getLocale().trim() ) )
    {
      return true;
    }
    return false;
  }

  public void setGoalQuestDataRepositoryService( GoalQuestDataRepositoryService goalQuestDataRepositoryService )
  {
    this.goalQuestDataRepositoryService = goalQuestDataRepositoryService;
  }

  private String getReportParms()
  {
    StringBuffer sb = new StringBuffer();
    sb.append( " promotionId=" + this.promotionId );
    sb.append( " locale=" + this.getLocale() );
    return sb.toString();
  }

  private Long getAsLong( String stringValue )
  {
    if ( stringValue == null || stringValue.equals( "" ) )
    {
      return null;
    }

    return new Long( stringValue );

  }

  public String getPromotionId()
  {
    return promotionId;
  }

  public void setPromotionId( String promotionId )
  {
    this.promotionId = promotionId;
  }

  public String getLocale()
  {
    if ( locale == null )
    {
      Locale userLocale = UserManager.getLocale();
      this.locale = userLocale.toString();
    }
    return locale;
  }

  public void setLocale( String locale )
  {
    this.locale = locale;
  }

}
