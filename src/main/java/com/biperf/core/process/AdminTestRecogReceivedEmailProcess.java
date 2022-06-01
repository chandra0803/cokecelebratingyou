
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
import com.biperf.core.value.AdminTestProcessParmBean;

public class AdminTestRecogReceivedEmailProcess extends BaseProcessImpl
{

  private static final Log log = LogFactory.getLog( AdminTestRecogReceivedEmailProcess.class );

  public static final String PROCESS_NAME = "Admin Test Recognition Received Email Process";
  public static final String BEAN_NAME = "adminTestRecogReceivedEmailProcess";

  private AdminTestProcessService adminTestProcessService;

  private String recipientUserName;
  private String senderUserName;
  private String displayCard;
  private String displayBehavior;
  private String displayCertificate;
  private Long awardAmount;
  private Long promotionId;
  private String recipientLocale;
  private String submitterComments;
  private String isManager;
  private Long badgePromotionId;
  private String displayProgressBar;

  public AdminTestRecogReceivedEmailProcess()
  {
    super();
  }

  public void onExecute()
  {
    log.debug( "Starting Admin Test Recognition Received Email Process with User Name: " + recipientUserName );
    try
    {
      Participant recipient = participantService.getParticipantByUserName( recipientUserName );
      if ( recipient != null )
      {
        Mailing mailing = new Mailing();
        Message message = messageService.getMessageByCMAssetCode( MessageService.RECOGNITION_RECEIVED_MESSAGE_CM_ASSET_CODE );
        mailing.setGuid( GuidUtils.generateGuid() );
        mailing.setMailingType( MailingType.lookup( MailingType.PROMOTION ) );
        mailing.setMessage( message );
        mailing.setDeliveryDate( new Timestamp( new Date().getTime() ) );
        mailing.setSender( getSystemIncentiveEmailAddress() );

        AdminTestProcessParmBean parmBean = new AdminTestProcessParmBean();
        parmBean.setProcessInvocationId( getProcessInvocationId() );
        parmBean.setRecipientUserName( recipientUserName );
        parmBean.setPromotionId( promotionId );
        parmBean.setRecipientLocale( recipientLocale );
        parmBean.setSenderUserName( senderUserName );
        parmBean.setDisplayCard( displayCard );
        parmBean.setDisplayBehavior( displayBehavior );
        parmBean.setDisplayCertificate( displayCertificate );
        parmBean.setAwardAmount( awardAmount );
        parmBean.setSubmitterComments( submitterComments );
        parmBean.setIsManager( isManager );
        parmBean.setBadgePromotionId( badgePromotionId );
        parmBean.setDisplayProgressBar( displayProgressBar );

        /* normally sent by MailingServiceImpl.buildRecognitionMailingRecipient */
        MailingRecipient mailingRecipient = adminTestProcessService.buildRecognitionMailingRecipient( recipient, parmBean );
        mailing.addMailingRecipient( mailingRecipient );
        mailingService.submitMailing( mailing, null );

        addComment( "Email sent to user name " + recipientUserName );
      }
      else
      {
        addComment( "User name " + recipientUserName + " not available in the system to launch Admin Test Recognition Received Email Process." );
      }
    }
    catch( Exception e )
    {
      logErrorMessage( e );
      addComment( "An exception occurred while processing Admin Test Recognition Received Email Process with Username: " + recipientUserName );
    }
  }

  public void setAdminTestProcessService( AdminTestProcessService adminTestProcessService )
  {
    this.adminTestProcessService = adminTestProcessService;
  }

  public String getRecipientUserName()
  {
    return recipientUserName;
  }

  public void setRecipientUserName( String recipientUserName )
  {
    this.recipientUserName = recipientUserName;
  }

  public String getSenderUserName()
  {
    return senderUserName;
  }

  public void setSenderUserName( String senderUserName )
  {
    this.senderUserName = senderUserName;
  }

  public String getDisplayCard()
  {
    return displayCard;
  }

  public void setDisplayCard( String displayCard )
  {
    this.displayCard = displayCard;
  }

  public String getDisplayBehavior()
  {
    return displayBehavior;
  }

  public void setDisplayBehavior( String displayBehavior )
  {
    this.displayBehavior = displayBehavior;
  }

  public String getDisplayCertificate()
  {
    return displayCertificate;
  }

  public void setDisplayCertificate( String displayCertificate )
  {
    this.displayCertificate = displayCertificate;
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

  public String getSubmitterComments()
  {
    return submitterComments;
  }

  public void setSubmitterComments( String submitterComments )
  {
    this.submitterComments = submitterComments;
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

}
