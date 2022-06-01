
package com.biperf.core.process;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.biperf.core.domain.enums.BudgetType;
import com.biperf.core.domain.enums.MailingType;
import com.biperf.core.domain.enums.PickListItem;
import com.biperf.core.domain.mailing.Mailing;
import com.biperf.core.domain.mailing.MailingRecipient;
import com.biperf.core.domain.message.Message;
import com.biperf.core.domain.user.User;
import com.biperf.core.domain.user.UserNode;
import com.biperf.core.mobileapp.recognition.service.MobileNotificationService;
import com.biperf.core.service.cms.CMAssetService;
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
import com.biperf.core.value.recognitionadvisor.RecognitionAdvisorUnusedBudgetBean;
import com.biperf.core.value.recognitionadvisor.RecognitionAdvisorValueBean;
import com.objectpartners.cms.domain.Content;
import com.objectpartners.cms.service.ContentReader;
import com.objectpartners.cms.util.ContentReaderManager;

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
 * <td>Dec 26, 2017</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class RecognitionAdvisorTeamMemberEmailProcess extends BaseProcessImpl
{
  private static final Log log = LogFactory.getLog( RecognitionAdvisorTeamMemberEmailProcess.class );

  public static final String BEAN_NAME = "recognitionAdvisorTeamMemberEmailProcess";
  private @Autowired RecognitionAdvisorService recognitionAdvisorService;
  private @Autowired ParticipantService participantService;
  private @Autowired MobileNotificationService mobileNotificationService;

  // images
  public static final String imagePath = "/assets/img/recognitionadvisor";
  private static final String CM_KEY_PREFIX = "recognition.content.model.info";
  int count = 0;
  int budgetCount = 0;
  int failureCount = 0;
  int successCount = 0;
  String data = "";

  public RecognitionAdvisorTeamMemberEmailProcess()
  {
    super();
  }

  @Override
  protected void onExecute()
  {
    try
    {
      boolean raEnabled = getSystemVariableService().getPropertyByName( SystemVariableService.RA_ENABLE ).getBooleanVal();
      boolean raOverDueTeamMemberEmailEnabled = getSystemVariableService().getPropertyByName( SystemVariableService.RA_SEND_REMINDER_TO_RECOGNIZE_TEAM_MEMBER ).getBooleanVal();

      if ( raEnabled && raOverDueTeamMemberEmailEnabled )
      {
        List<UserNode> userNodeLst = participantService.getAllManagerAndOwner();
        userNodeLst.stream().forEach( p ->
        {

          List<RecognitionAdvisorValueBean> raTeamMemberList = recognitionAdvisorService.getRATeamMemberReminderData( p.getUser().getId() );

          if ( !Objects.isNull( raTeamMemberList ) && raTeamMemberList.size() > 0 && p.getUser().isActive() && !Objects.isNull( raTeamMemberList.get( 0 ).getFirstName() ) )
          {
            boolean budgetNotUsedNotificationEnabled = getSystemVariableService().getPropertyByName( SystemVariableService.BUDGET_NOTUSED_NOTIFICATION_ENABLD ).getBooleanVal();
            List<RecognitionAdvisorUnusedBudgetBean> raUnUsedBudgetList = new LinkedList<RecognitionAdvisorUnusedBudgetBean>();
            if ( budgetNotUsedNotificationEnabled )
            {
              raUnUsedBudgetList = recognitionAdvisorService.getRAUnusedBudgetReminderData( p.getUser().getId() );
            }
            sendSummaryMessage( raTeamMemberList, raUnUsedBudgetList, p.getUser() );
            success();
            RecognitionAdvisorValueBean advisorValueBean = null;
            if ( raTeamMemberList.size() == 1 )
            {
              advisorValueBean = raTeamMemberList.get( 0 );
            }
            mobileNotificationService.raTeamOverDueNotification( p.getUser().getId(), advisorValueBean );
          }
          else
          {
            if ( p.getUser().isActive() )
            {
              failure();
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
        else if ( !raOverDueTeamMemberEmailEnabled )
        {
          addComment( "Recognition Advisor Teammember email system variable ra.sendreminder.torecognize.teammember is : " + raOverDueTeamMemberEmailEnabled + " Please enable" );
        }

      }

    }
    catch( Exception e )
    {
      logErrorMessage( e );
      addComment( "An exception occurred while processing Recognition Advisor Team Member Email Process " );
    }
  }

  public void sendSummaryMessage( List<RecognitionAdvisorValueBean> raTeamMemberList, List<RecognitionAdvisorUnusedBudgetBean> raUnUsedBudgetList, User mgr )
  {
    try
    {
      Mailing mailing = new Mailing();
      Message message = messageService.getMessageByCMAssetCode( MessageService.RECOGNITION_ADVISOR_TEAM_MEMBER_EMAIL_MESSAGE_CM_ASSET_CODE );
      Integer days = getSystemVariableService().getPropertyByName( SystemVariableService.RA_NUMBER_OF_DAYS_EMPLOYEE_REMINDER ).getIntVal();
      String clientPrefix = getSystemVariableService().getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();

      String localeCode = mgr.getLanguageType() != null ? mgr.getLanguageType().getCode() : systemVariableService.getDefaultLanguage().getStringVal();
      Map<String, Object> objectMap = new HashMap<String, Object>();
      objectMap.put( "raContent", MessageFormat.format( buildCMSMessage( "RA_EMAIL_CONTENT", localeCode ), new Object[] { days } ) );
      objectMap.put( "mgrfirstName", mgr.getFirstName() );
      objectMap.put( "days", days );
      objectMap.put( "recognition", buildCMSMessage( "RA_RECOGNIZE", localeCode ) );
      objectMap.put( "raEmailHeader", buildCMSMessage( "RA_EMAIL_HEADER", localeCode ) );

      objectMap.put( "raTeamcontent", buildCMSMessage( "RA_SHOW_TEAM_ADDS_UP", localeCode ) );
      objectMap.put( "raImagePath", clientPrefix + imagePath );
      objectMap.put( "raImagePathSkin", clientPrefix + "/assets/skins/" + getDesignThemeService().getDesignTheme( UserManager.getUser() ) + "/img/recognitionadvisor" );
      Map<String, Object> parms = new HashMap<String, Object>();
      parms.put( "isRaFlow", true );
      parms.put( "managerId", mgr.getId().toString() );
      objectMap.put( "redirectToDetail",
                     ClientStateUtils.generateEncodedLink( "",
                                                           systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal() + "/raLogin.do?isRaFlow=true",
                                                           parms ) );

      raTeamMemberList.forEach( raUser ->
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

        if ( raTeamMemberList.size() >= 3 )
        {
          objectMap.put( "isThree", Boolean.TRUE );
          objectMap.put( "isTwo", Boolean.FALSE );
          objectMap.put( "isOne", Boolean.FALSE );

        }
        else if ( raTeamMemberList.size() == 2 )
        {

          objectMap.put( "isTwo", Boolean.TRUE );
          objectMap.put( "isThree", Boolean.FALSE );
          objectMap.put( "isOne", Boolean.FALSE );
        }
        else if ( raTeamMemberList.size() == 1 )
        {
          objectMap.put( "isOne", Boolean.TRUE );
          objectMap.put( "isTwo", Boolean.FALSE );
          objectMap.put( "isThree", Boolean.FALSE );

        }

        objectMap.put( "isUserExist" + count, Boolean.TRUE );
        String recUrl = "recognitionUrl" + count;

        objectMap.put( recUrl,
                       ClientStateUtils
                           .generateEncodedLink( "",
                                                 systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal() + "/raLogin.do?isRaFlow=true",
                                                 parms ) );

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
        if ( raUser.getNoOfDaysPastDueDate() != null )
        {
          objectMap.put( "noOfDaysFlag" + count, Boolean.TRUE );
          objectMap.put( "noOfDays" + count, raUser.getNoOfDaysPastDueDate() + " " + buildCMSMessage( "RA_DAYS_OVER_DUE", localeCode ) );
        }

        if ( raUser.getAvatarUrl() != null && !raUser.getAvatarUrl().isEmpty() )
        {
          objectMap.put( "isAvatarFlag" + count, Boolean.TRUE );
          objectMap.put( "avatarImg" + count, raUser.getAvatarUrl() );
        }
        else
        {
          objectMap.put( "noAvatarFlag" + count, Boolean.TRUE );
          objectMap.put( "NoAvatarImg" + count, raUser.getFirstName().charAt( 0 ) + "" + raUser.getLastName().charAt( 0 ) );
        }
        count++;
      } );

      objectMap.put( "unUsedBudgetNotification", Boolean.FALSE );
      budgetCount = 0;
      data = "";
      raUnUsedBudgetList.forEach( budgetData ->
      {
        Boolean isBudgetShared = Boolean.FALSE;
        String promo_Expire = "";
        String expRemaining = "";
        if ( !budgetData.getBudgetType().getCode().equals( BudgetType.PAX_BUDGET_TYPE ) )
        {
          isBudgetShared = Boolean.TRUE;
        }
        if ( null == budgetData.getDaysToPromoExp() )
        {
          promo_Expire = buildCMSMessage( "RA_PROMO_REMAINING", localeCode );
          expRemaining = buildCMSMessage( "RA_PROMO_EXP_THERE", localeCode );
        }
        else if ( budgetData.getDaysToPromoExp() == 0 )
        {
          promo_Expire = buildCMSMessage( "RA_PROMO_TODAY", localeCode ); // RA_PROMO_TODAY
          expRemaining = buildCMSMessage( "RA_PROMO_EXP_THERE", localeCode );
        }
        else
        {
          expRemaining = buildCMSMessage( "RA_PROMO_EXP_REMAINING", localeCode );
          Boolean gray_BU = ( null != budgetData.getDaystopromoexpcolor() && budgetData.getDaystopromoexpcolor().equalsIgnoreCase( "Gray" ) ) ? Boolean.TRUE : Boolean.FALSE;
          Boolean red_BU = ( null != budgetData.getDaystopromoexpcolor() && budgetData.getDaystopromoexpcolor().equalsIgnoreCase( "Red" ) ) ? Boolean.TRUE : Boolean.FALSE;
          Boolean orange_BU = ( null != budgetData.getDaystopromoexpcolor() && budgetData.getDaystopromoexpcolor().equalsIgnoreCase( "Orange" ) ) ? Boolean.TRUE : Boolean.FALSE;
          promo_Expire = MessageFormat.format( buildCMSMessage( "RA_PROMO_EXPIRE", localeCode ), new Object[] { red_BU, orange_BU, gray_BU, budgetData.getDaysToPromoExp() } );
        }
        if ( budgetCount == 0 )
        {
          String budget_Used = "";
          if ( null == budgetData.getDaysSinceLastRec() || budgetData.getDaysSinceLastRec() == 0 )
          {
            budget_Used = buildCMSMessage( "RA_BUDGET_NEVER_USED", localeCode );
          }
          else
          {
            Boolean gray = ( null != budgetData.getDayssincelastcolor() && budgetData.getDayssincelastcolor().equalsIgnoreCase( "Gray" ) ) ? Boolean.TRUE : Boolean.FALSE;
            Boolean red = ( null != budgetData.getDayssincelastcolor() && budgetData.getDayssincelastcolor().equalsIgnoreCase( "Red" ) ) ? Boolean.TRUE : Boolean.FALSE;
            Boolean orange = ( null != budgetData.getDayssincelastcolor() && budgetData.getDayssincelastcolor().equalsIgnoreCase( "Orange" ) ) ? Boolean.TRUE : Boolean.FALSE;
            budget_Used = MessageFormat.format( buildCMSMessage( "RA_BUDGET_USED", localeCode ), new Object[] { red, orange, gray, budgetData.getDaysSinceLastRec() } );
          }
          objectMap.put( "unUsedBudgetNotification", Boolean.TRUE );
          objectMap.put( "daysSince", budget_Used );
          objectMap.put( "programBudgetName", budgetData.getPromotionName() );
          objectMap.put( "pointsBalance", budgetData.getPoints() );
          objectMap.put( "daysExpiry", promo_Expire );
          objectMap.put( "expRemaining", expRemaining );
          objectMap.put( "isBudgetShared", isBudgetShared );
          objectMap.put( "isMultipleBudgetPrograms", Boolean.FALSE );
          objectMap.put( "isMultipleProgramAvailable", Boolean.FALSE );
          objectMap.put( "isMultipleProgramNotAvailable", Boolean.FALSE );
        }
        else if ( budgetCount >= 1 )
        {
          String budgetMessage = "";
          String budget_Used = "";
          if ( null == budgetData.getDaysSinceLastRec() || budgetData.getDaysSinceLastRec() == 0 )
          {
            budget_Used = buildCMSMessage( "RA_BUDGET_DATA_NEVER_USED", localeCode );

          }
          else
          {
            Boolean gray_BU = ( null != budgetData.getDayssincelastcolor() && budgetData.getDayssincelastcolor().equalsIgnoreCase( "Gray" ) ) ? Boolean.TRUE : Boolean.FALSE;
            Boolean red_BU = ( null != budgetData.getDayssincelastcolor() && budgetData.getDayssincelastcolor().equalsIgnoreCase( "Red" ) ) ? Boolean.TRUE : Boolean.FALSE;
            Boolean orange_BU = ( null != budgetData.getDayssincelastcolor() && budgetData.getDayssincelastcolor().equalsIgnoreCase( "Orange" ) ) ? Boolean.TRUE : Boolean.FALSE;
            budget_Used = MessageFormat.format( buildCMSMessage( "RA_BUDGET_DATA_USED", localeCode ), new Object[] { red_BU, orange_BU, gray_BU, budgetData.getDaysSinceLastRec() } );
          }
          budgetMessage = MessageFormat.format( buildCMSMessage( "RA_BUDGET_DATA", localeCode ),
                                                new Object[] { budgetData.getPromotionName(), budget_Used, budgetData.getPoints(), isBudgetShared, promo_Expire } );

          data += budgetMessage;
          objectMap.put( "isMultipleBudgetPrograms", Boolean.TRUE );
          objectMap.put( "unUsedBudgetNotification", Boolean.TRUE );
          if ( budgetCount > 1 )
          {
            objectMap.put( "isMultipleProgramAvailable", Boolean.TRUE );
            objectMap.put( "isMultipleProgramNotAvailable", Boolean.FALSE );
          }
          else
          {
            objectMap.put( "isMultipleProgramAvailable", Boolean.FALSE );
            objectMap.put( "isMultipleProgramNotAvailable", Boolean.TRUE );
          }
        }
        budgetCount++;
      } );
      objectMap.put( "repeatPrograms", data );
      mailing = composeMail( message.getCmAssetCode(), MailingType.PROMOTION );
      // Add the recipient
      MailingRecipient mr = addRecipient( mgr );
      mailing.addMailingRecipient( mr );

      // Send the e-mail message with personalization
      mailingService.submitMailing( mailing, objectMap );
      count = 0;// reset the count once once over the each loop execution
    }
    catch( Exception e )
    {
      log.error( "An exception occurred while sending Ra email received email" + e );
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

  public void setRecognitionAdvisorService( RecognitionAdvisorService recognitionAdvisorService )
  {
    this.recognitionAdvisorService = recognitionAdvisorService;
  }

  public void setMobileNotificationService( MobileNotificationService mobileNotificationService )
  {
    this.mobileNotificationService = mobileNotificationService;
  }

  public String buildCMSMessage( String key, String localeCode )
  {
    return getCMAssetService().getString( CM_KEY_PREFIX, key, LocaleUtils.getLocale( localeCode ), true );
  }

  private DesignThemeService getDesignThemeService()
  {
    return (DesignThemeService)BeanLocator.getBean( DesignThemeService.BEAN_NAME );
  }

  private void failure()
  {
    failureCount++;

  }

  private void success()
  {
    successCount++;

  }

  private CMAssetService getCMAssetService()
  {
    return (CMAssetService)BeanLocator.getBean( CMAssetService.BEAN_NAME );
  }

}
