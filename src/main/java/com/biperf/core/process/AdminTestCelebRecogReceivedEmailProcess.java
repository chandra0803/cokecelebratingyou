
package com.biperf.core.process;

import java.sql.Timestamp;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.domain.enums.MailingType;
import com.biperf.core.domain.mailing.Mailing;
import com.biperf.core.domain.mailing.MailingRecipient;
import com.biperf.core.domain.message.Message;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.service.message.MessageService;
import com.biperf.core.service.process.AdminTestProcessService;
import com.biperf.core.utils.GuidUtils;
import com.biperf.core.utils.NewServiceAnniversaryUtil;
import com.biperf.core.value.AdminTestProcessParmBean;

public class AdminTestCelebRecogReceivedEmailProcess extends BaseProcessImpl
{

  private static final Log log = LogFactory.getLog( AdminTestCelebRecogReceivedEmailProcess.class );

  public static final String PROCESS_NAME = "Admin Test Celebration Recognition Received Email Process";
  public static final String BEAN_NAME = "adminTestCelebRecogReceivedEmailProcess";

  private AdminTestProcessService adminTestProcessService;

  private String senderUserName;
  private String recipientUserName;
  private Long awardAmount;
  private Long promotionId;
  private String recipientLocale;
  private String isManager;
  private Long badgePromotionId;
  private String displayProgressBar = "no"; // not used in 10021843 message
  private String awardDate;
  private String customFormField1; // not used in 10021843 message
  private String customFormField2; // not used in 10021843 message
  private String customFormField3; // not used in 10021843 message
  private String managerUserName;
  private String managerAboveUserName;
  private String celebManagerMessage;
  private String celebManagerAboveMessage;
  private String anniversaryNumberOfYears;
  private String anniversaryNumberOfDays;

  public AdminTestCelebRecogReceivedEmailProcess()
  {
    super();
  }

  public void onExecute()
  {

    // Disabling The Process As New Service Anniversary & Celebration Module Enabled From Nackle
    // Eco-System.
    if ( NewServiceAnniversaryUtil.isNewServiceAnniversaryEnabled() )
    {
      log.info( " The Process 'AdminTestCelebRecogReceivedEmailProcess' Has Been Restricted !!!!, Since The New Service Anniversary Has Been Enabled IN DM From Nackle Eco-System." );
      addComment( " The Process 'AdminTestCelebRecogReceivedEmailProcess' Has Been Restricted !!!!, Since The New Service Anniversary Has Been Enabled IN DM From Nackle Eco-System." );

    }
    else
    {
      log.debug( "Starting Admin Test Celebration Recognition Received Email Process with User Name: " + recipientUserName );
      try
      {
        Participant recipient = participantService.getParticipantByUserName( recipientUserName );
        if ( recipient != null )
        {
          Mailing mailing = new Mailing();
          Message message = messageService.getMessageByCMAssetCode( MessageService.CELEBRATION_RECOGNITION_RECEIVED_MESSAGE_CM_ASSET_CODE );
          mailing.setGuid( GuidUtils.generateGuid() );
          mailing.setMailingType( MailingType.lookup( MailingType.PROMOTION ) );
          mailing.setMessage( message );
          mailing.setDeliveryDate( new Timestamp( new Date().getTime() ) );
          mailing.setSender( getSystemIncentiveEmailAddress() );

          AdminTestProcessParmBean parmBean = new AdminTestProcessParmBean();
          parmBean.setProcessInvocationId( getProcessInvocationId() );
          parmBean.setSenderUserName( senderUserName );
          parmBean.setRecipientUserName( recipientUserName );
          parmBean.setPromotionId( promotionId );
          parmBean.setRecipientLocale( recipientLocale );
          parmBean.setAwardAmount( awardAmount );
          parmBean.setIsManager( isManager );
          parmBean.setBadgePromotionId( badgePromotionId );
          parmBean.setDisplayProgressBar( displayProgressBar );
          parmBean.setAwardDate( awardDate );
          parmBean.setCustomFormField1( customFormField1 );
          parmBean.setCustomFormField2( customFormField2 );
          parmBean.setCustomFormField3( customFormField3 );
          parmBean.setManagerUserName( managerUserName );
          parmBean.setManagerAboveUserName( managerAboveUserName );
          parmBean.setCelebManagerMessage( celebManagerMessage );
          parmBean.setCelebManagerAboveMessage( celebManagerAboveMessage );
          parmBean.setAnniversaryNumberOfYears( anniversaryNumberOfYears );
          parmBean.setAnniversaryNumberOfDays( anniversaryNumberOfDays );

          /* normally sent by MailingServiceImpl.buildRecognitionMailingRecipient */
          MailingRecipient mailingRecipient = adminTestProcessService.buildRecognitionMailingRecipient( recipient, parmBean );
          mailing.addMailingRecipient( mailingRecipient );
          mailingService.submitMailing( mailing, null );

          addComment( "Email sent to user name " + recipientUserName );
        }
        else
        {
          addComment( "User name " + recipientUserName + " not available in the system to launch Admin Test Celebration Recognition Received Email Process." );
        }
      }
      catch( Exception e )
      {
        logErrorMessage( e );
        addComment( "An exception occurred while processing Admin Test Celebration Recognition Received Email Process with Username: " + recipientUserName );
      }

    }

  }

  public void setAdminTestProcessService( AdminTestProcessService adminTestProcessService )
  {
    this.adminTestProcessService = adminTestProcessService;
  }

  public String getSenderUserName()
  {
    return senderUserName;
  }

  public void setSenderUserName( String senderUserName )
  {
    this.senderUserName = senderUserName;
  }

  public String getRecipientUserName()
  {
    return recipientUserName;
  }

  public void setRecipientUserName( String recipientUserName )
  {
    this.recipientUserName = recipientUserName;
  }

  public Long getAwardAmount()
  {
    return awardAmount;
  }

  public void setAwardAmount( Long awardAmount )
  {
    this.awardAmount = awardAmount;
  }

  public Long getPromotionId()
  {
    return promotionId;
  }

  public void setPromotionId( Long promotionId )
  {
    this.promotionId = promotionId;
  }

  public String getRecipientLocale()
  {
    return recipientLocale;
  }

  public void setRecipientLocale( String recipientLocale )
  {
    this.recipientLocale = recipientLocale;
  }

  public String getIsManager()
  {
    return isManager;
  }

  public void setIsManager( String isManager )
  {
    this.isManager = isManager;
  }

  public Long getBadgePromotionId()
  {
    return badgePromotionId;
  }

  public void setBadgePromotionId( Long badgePromotionId )
  {
    this.badgePromotionId = badgePromotionId;
  }

  public String getDisplayProgressBar()
  {
    return displayProgressBar;
  }

  public void setDisplayProgressBar( String displayProgressBar )
  {
    this.displayProgressBar = displayProgressBar;
  }

  public String getAwardDate()
  {
    return awardDate;
  }

  public void setAwardDate( String awardDate )
  {
    this.awardDate = awardDate;
  }

  public String getCustomFormField1()
  {
    return customFormField1;
  }

  public void setCustomFormField1( String customFormField1 )
  {
    this.customFormField1 = customFormField1;
  }

  public String getCustomFormField2()
  {
    return customFormField2;
  }

  public void setCustomFormField2( String customFormField2 )
  {
    this.customFormField2 = customFormField2;
  }

  public String getCustomFormField3()
  {
    return customFormField3;
  }

  public void setCustomFormField3( String customFormField3 )
  {
    this.customFormField3 = customFormField3;
  }

  public String getManagerUserName()
  {
    return managerUserName;
  }

  public void setManagerUserName( String managerUserName )
  {
    this.managerUserName = managerUserName;
  }

  public String getManagerAboveUserName()
  {
    return managerAboveUserName;
  }

  public void setManagerAboveUserName( String managerAboveUserName )
  {
    this.managerAboveUserName = managerAboveUserName;
  }

  public String getCelebManagerMessage()
  {
    return celebManagerMessage;
  }

  public void setCelebManagerMessage( String celebManagerMessage )
  {
    this.celebManagerMessage = celebManagerMessage;
  }

  public String getCelebManagerAboveMessage()
  {
    return celebManagerAboveMessage;
  }

  public void setCelebManagerAboveMessage( String celebManagerAboveMessage )
  {
    this.celebManagerAboveMessage = celebManagerAboveMessage;
  }

  public String getAnniversaryNumberOfYears()
  {
    return anniversaryNumberOfYears;
  }

  public void setAnniversaryNumberOfYears( String anniversaryNumberOfYears )
  {
    this.anniversaryNumberOfYears = anniversaryNumberOfYears;
  }

  public String getAnniversaryNumberOfDays()
  {
    return anniversaryNumberOfDays;
  }

  public void setAnniversaryNumberOfDays( String anniversaryNumberOfDays )
  {
    this.anniversaryNumberOfDays = anniversaryNumberOfDays;
  }

}
