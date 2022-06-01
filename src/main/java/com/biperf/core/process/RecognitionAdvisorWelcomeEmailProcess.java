
package com.biperf.core.process;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.domain.enums.MailingType;
import com.biperf.core.domain.mailing.Mailing;
import com.biperf.core.domain.mailing.MailingRecipient;
import com.biperf.core.domain.message.Message;
import com.biperf.core.domain.user.User;
import com.biperf.core.service.message.MessageService;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.utils.ClientStateUtils;
import com.biperf.core.utils.GuidUtils;
import com.biperf.core.value.RAValueBean;

/**
 * Used for the RecognitonAdvisor Email TeamMember screen(s).
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
 * <td>Ramesh J</td>
 * <td>Mar 20, 2018</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class RecognitionAdvisorWelcomeEmailProcess extends BaseProcessImpl
{
  private static final Log log = LogFactory.getLog( RecognitionAdvisorWelcomeEmailProcess.class );

  public static final String BEAN_NAME = "recognitionAdvisorWelcomeEmailProcess";
  public static final String imagePath = "/assets/img/recognitionadvisor";

  int successCount = 0;

  public RecognitionAdvisorWelcomeEmailProcess()
  {
    super();
  }

  @Override
  protected void onExecute()
  {
    try
    {
      if ( getSystemVariableService().getPropertyByName( SystemVariableService.RA_ENABLE ).getBooleanVal() )
      {

        List<RAValueBean> raWelcomeEmailList = userService.getAllUsersForRAWelcomeMail();
        if ( raWelcomeEmailList.size() > 0 )
        {
          raWelcomeEmailList.stream().forEach( p ->
          {
            User user = userService.getUserById( p.getUserId() );

            if ( !Objects.isNull( user ) )
            {
              sendSummaryMessage( user );
              updateUser( user );
              success();
            }
          } );

          addComment( "Summary: Success Email Count: " + successCount );
        }
        else
        {
          addComment( "No Users are eligible for welcome Email" );
        }

      }

      else
      {
        addComment( "Recognition Advisor system variable ra.enabled is : " + getSystemVariableService().getPropertyByName( SystemVariableService.RA_ENABLE ).getBooleanVal() + " Please enable" );

      }

    }
    catch( Exception e )
    {
      logErrorMessage( e );
      addComment( "An exception occurred while processing Recognition Advisor Welcome Email Process " );
    }
  }

  public void sendSummaryMessage( User mgr )
  {
    try
    {
      Mailing mailing = new Mailing();
      Message message = messageService.getMessageByCMAssetCode( MessageService.RECOGNITION_ADVISOR_WELCOME_EMAIL_MESSAGE_CM_ASSET_CODE );
      String clientPrefix = getSystemVariableService().getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();

      Map<String, Object> objectMap = new HashMap<String, Object>();
      objectMap.put( "raImagePath", clientPrefix + imagePath );
      objectMap.put( "firstName", mgr.getFirstName() );
      Map<String, Object> parms = new HashMap<String, Object>();

      parms.put( "isRaFlow", true );
      parms.put( "managerId", mgr.getId().toString() );
      objectMap.put( "redirectToDetail",
                     ClientStateUtils.generateEncodedLink( "",
                                                           systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal() + "/raLogin.do?isRaFlow=true",
                                                           parms ) );

      mailing = composeMail( message.getCmAssetCode(), MailingType.PROMOTION );
      // Add the recipient
      MailingRecipient mr = addRecipient( mgr );
      mailing.addMailingRecipient( mr );

      // Send the e-mail message with personalization
      mailingService.submitMailing( mailing, objectMap );
    }
    catch( Exception e )
    {
      log.error( "An exception occurred while sending Ra email received email" + e.getMessage() );
    }
  }

  protected void updateUser( User user )
  {
    try
    {
      user.setRaWelcomeEmailSent( Boolean.TRUE );
      userService.saveUser( user );
    }
    catch( Exception e )
    {
      String message = "An exception occurred while updating the user information while sending RA Welcome Email Email.  " + "(process invocation ID = " + getProcessInvocationId() + ")";

      log.error( message, e );
      addComment( message );
    }
  }

  protected MailingRecipient addRecipient( User recipient )
  {
    MailingRecipient mailingRecipient = new MailingRecipient();
    mailingRecipient.setGuid( GuidUtils.generateGuid() );
    mailingRecipient.setUser( recipient );
    mailingRecipient
        .setLocale( mailingRecipient.getUser().getLanguageType() != null ? mailingRecipient.getUser().getLanguageType().getCode() : systemVariableService.getDefaultLanguage().getStringVal() );

    return mailingRecipient;
  }

  public void setParticipantService( ParticipantService participantService )
  {
    this.participantService = participantService;
  }

  private void success()
  {
    successCount++;

  }

}
