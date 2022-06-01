/*
 * (c) 2017 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.process;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.biperf.core.domain.enums.MailingType;
import com.biperf.core.domain.enums.PickListItem;
import com.biperf.core.domain.mailing.Mailing;
import com.biperf.core.domain.mailing.MailingRecipient;
import com.biperf.core.domain.message.Message;
import com.biperf.core.domain.user.User;
import com.biperf.core.domain.user.UserNode;
import com.biperf.core.mobileapp.recognition.service.MobileNotificationService;
import com.biperf.core.service.maincontent.DesignThemeService;
import com.biperf.core.service.message.MessageService;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.recognitionadvisor.RecognitionAdvisorService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.ClientStateUtils;
import com.biperf.core.utils.GuidUtils;
import com.biperf.core.utils.LocaleUtils;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.recognitionadvisor.RecognitionAdvisorValueBean;
import com.objectpartners.cms.domain.Content;
import com.objectpartners.cms.service.ContentReader;
import com.objectpartners.cms.util.CmsResourceBundle;
import com.objectpartners.cms.util.ContentReaderManager;

/**
 * 
 * @author rajadura
 * @since Dec 27, 2017
 * 
 */
public class RecognitionAdvisorNewHireEmailProcess extends BaseProcessImpl
{

  private static final Log log = LogFactory.getLog( RecognitionAdvisorNewHireEmailProcess.class );

  public static final String BEAN_NAME = "recognitionAdvisorNewHireEmailProcess";

  private static final String CM_KEY_PREFIX = "recognition.content.model.info.";

  @Autowired
  private RecognitionAdvisorService recognitionAdvisorService;
  @Autowired
  private ParticipantService participantService;
  @Autowired
  private MobileNotificationService mobileNotificationService;
  public static final String imagePath = "/assets/img/recognitionadvisor";
  int successCount = 0;
  int failureCount = 0;
  int count = 0;

  @Override
  protected void onExecute()
  {
    log.debug( "Starting Recognition Advisor NewHire SendEmail Process:" );
    try
    {
      boolean raEnabled = getSystemVariableService().getPropertyByName( SystemVariableService.RA_ENABLE ).getBooleanVal();

      boolean raNewHireEmailEnabled = getSystemVariableService().getPropertyByName( SystemVariableService.RA_SEND_REMINDER_TO_RECOGNIZE_NEW_HIRE ).getBooleanVal();
      if ( raEnabled && raNewHireEmailEnabled )
      {
        List<UserNode> userNodeLst = participantService.getAllManagerAndOwner();
        userNodeLst.stream().forEach( p ->
        {
          List<RecognitionAdvisorValueBean> raNewHireList = recognitionAdvisorService.showRAReminderNewHireEmailPaxData( p.getUser().getId() );
          if ( !Objects.isNull( raNewHireList ) && raNewHireList.size() > 0 && p.getUser().isActive() && !Objects.isNull( raNewHireList.get( 0 ).getFirstName() ) )
          {
            sendSummaryMessage( raNewHireList, p.getUser() );
            success();
            RecognitionAdvisorValueBean advisorValueBean = null;
            if ( raNewHireList.size() == 1 )
            {
              advisorValueBean = raNewHireList.get( 0 );
            }
            mobileNotificationService.raNewHireNotification( p.getUser().getId(), advisorValueBean );
          }
          else
          {

            if ( p.getUser().isActive() )
            {
              failure();
              log.error( "No new hire employees for this Manager : " + p.getUser().getFirstName() + " " + p.getUser().getLastName() );

            }

          }
        } );

        addComment( "Summary: Success Email Count: " + successCount + ".Failure Count : " + failureCount );
      }
      else
      {

        if ( !raEnabled )
        {
          addComment( "Recognition Advisor system variable ra.enabled is : " + raEnabled + " Please enable" );
        }
        else if ( !raNewHireEmailEnabled )
        {
          addComment( "Recognition Advisor NewHire email system variable ra.sendreminder.torecognize.newhire is : " + raNewHireEmailEnabled + " Please enable" );
        }

      }
    }
    catch( Exception e )
    {
      logErrorMessage( e );
      addComment( "An exception occurred while processing Recognition Advisor New Hire Email Process " );
    }
  }

  private void failure()
  {
    failureCount++;

  }

  private void success()
  {
    successCount++;

  }

  public void sendSummaryMessage( List<RecognitionAdvisorValueBean> raNewHireList, User recipientUser )
  {

    Mailing mailing = new Mailing();
    Message message = messageService.getMessageByCMAssetCode( MessageService.RECOGNITION_ADVISOR_NEW_HIRE_EMAIL_MESSAGE_CM_ASSET_CODE );
    Integer days = getSystemVariableService().getPropertyByName( SystemVariableService.RA_NUMBER_OF_DAYS_NEW_HIRE_TO_REGULAR_EMPLOYEE ).getIntVal();
    String clientPrefix = getSystemVariableService().getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();
    String localeCode = recipientUser.getLanguageType() != null ? recipientUser.getLanguageType().getCode() : systemVariableService.getDefaultLanguage().getStringVal();
    Map<String, Object> objectMap = new HashMap<String, Object>();
    objectMap.put( "raImagePath", clientPrefix + imagePath );
    objectMap.put( "mgrfirstName", recipientUser.getFirstName() );
    objectMap.put( "days", days );
    objectMap.put( "recognition", buildCMSMessage( CM_KEY_PREFIX + "RA_RECOGNIZE" ) );
    objectMap.put( "newEmployee", buildCMSMessage( CM_KEY_PREFIX + "RA_NEW_EMPLOYEE" ) );
    objectMap.put( "raImagePathSkin", clientPrefix + "/assets/skins/" + getDesignThemeService().getDesignTheme( UserManager.getUser() ) + "/img/recognitionadvisor" );
    Map<String, Object> parms = new HashMap<String, Object>();
    parms.put( "isRaFlow", true );
    parms.put( "managerId", recipientUser.getId().toString() );
    objectMap.put( "redirectToDetail",
                   ClientStateUtils
                       .generateEncodedLink( "", systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal() + "/raLogin.do?isRaFlow=true", parms ) );

    raNewHireList.forEach( raUser ->
    {

      parms.put( "reporteeId", raUser.getId().toString() );

      String clientStatePassword = ClientStatePasswordManager.getGlobalPassword();
      String clientState = ClientStateSerializer.serialize( parms, clientStatePassword );

      try
      {
        clientState = URLEncoder.encode( clientState, "UTF-8" );
      }
      catch( UnsupportedEncodingException e )
      {
        log.error( "Error in creating the clientstate", e );
      }

      if ( raUser.getFirstName().length() <= 17 )
      {
        objectMap.put( "firstName" + count, raUser.getFirstName() );
      }
      else
      {
        String fname = raUser.getFirstName();
        fname = fname.substring( 0, 14 ) + "...";
        objectMap.put( "firstName" + count, fname );
      }
      if ( ( raUser.getFirstName() + " " + raUser.getLastName() ).length() <= 17 )
      {
        objectMap.put( "userName" + count, raUser.getFirstName() + " " + raUser.getLastName() );
      }
      else
      {
        String uname = raUser.getFirstName() + " " + raUser.getLastName();
        uname = uname.substring( 0, 14 ) + "...";
        objectMap.put( "userName" + count, uname );
      }

      objectMap.put( "isUserExist" + count, Boolean.TRUE );
      String recUrl = "recognitionUrl" + count;

      objectMap.put( recUrl,
                     ClientStateUtils.generateEncodedLink( "",
                                                           systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal() + "/raLogin.do?isRaFlow=true",
                                                           parms ) );

      if ( raUser.getPositionType() != null )
      {
        objectMap.put( "isPositionExist" + count, Boolean.TRUE );
        ContentReader contentReader = ContentReaderManager.getContentReader();
        List contentList = (List)contentReader.getContent( "picklist.positiontype.items", LocaleUtils.getLocale( localeCode ) );
        for ( Iterator itr = contentList.iterator(); itr.hasNext(); )
        {
          Content content = (Content)itr.next();
          String code = (String)content.getContentDataMap().get( PickListItem.ITEM_CODE_KEY );
          if ( raUser.getPositionType().equalsIgnoreCase( code ) )
          {
            String positionType = (String)content.getContentDataMap().get( PickListItem.ITEM_NAME_KEY );
            objectMap.put( "positionType" + count, StringEscapeUtils.unescapeHtml4( positionType ) );
            break;
          }
        }

      }

      if ( raUser.getAvatarUrl() != null && !raUser.getAvatarUrl().isEmpty() )
      {
        objectMap.put( "avatarImg" + count, raUser.getAvatarUrl() );
        objectMap.put( "isAvatarFlag" + count, Boolean.TRUE );
      }
      else
      {
        objectMap.put( "NoAvatarImg" + count, raUser.getFirstName().charAt( 0 ) + "" + raUser.getLastName().charAt( 0 ) );
        objectMap.put( "noAvatarFlag" + count, Boolean.TRUE );
      }
      count++;
    } );

    mailing = composeMail( message.getCmAssetCode(), MailingType.PROMOTION );
    // Add the recipient
    MailingRecipient mr = addRecipient( recipientUser );
    mailing.addMailingRecipient( mr );
    try
    {
      // Send the e-mail message with personalization
      mailingService.submitMailing( mailing, objectMap );
      count = 0;
    }
    catch( Exception e )
    {
      log.error( "An exception occurred while sending New Hire Remainder email" );
    }
  }

  public static String buildCMSMessage( String key )
  {
    return CmsResourceBundle.getCmsBundle().getString( key );
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

  private DesignThemeService getDesignThemeService()
  {
    return (DesignThemeService)BeanLocator.getBean( DesignThemeService.BEAN_NAME );
  }

  public void setMobileNotificationService( MobileNotificationService mobileNotificationService )
  {
    this.mobileNotificationService = mobileNotificationService;
  }

}
