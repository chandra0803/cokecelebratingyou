
package com.biperf.core.process;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.domain.enums.MailingType;
import com.biperf.core.domain.mailing.Mailing;
import com.biperf.core.domain.mailing.MailingRecipient;
import com.biperf.core.domain.mailing.MailingRecipientData;
import com.biperf.core.domain.message.Message;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.service.cms.CMAssetService;
import com.biperf.core.service.message.MessageService;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.utils.GuidUtils;
import com.objectpartners.cms.util.CmsUtil;

public class AdminTestRecogPlateauNonRedemptionReminderEmailProcess extends BaseProcessImpl
{

  private static final Log log = LogFactory.getLog( AdminTestRecogPlateauNonRedemptionReminderEmailProcess.class );

  public static final String PROCESS_NAME = "Admin Test Recognition Plateau Non-Redemption Reminder Email Process";
  public static final String BEAN_NAME = "adminTestRecogPlateauNonRedemptionReminderEmailProcess";

  private PromotionService promotionService;
  private CMAssetService cmAssetService;

  private String recipientUserName;
  private Long promotionId;
  private String recipientLocale;
  private String awardLevel;

  public AdminTestRecogPlateauNonRedemptionReminderEmailProcess()
  {
    super();
  }

  public void onExecute()
  {
    log.debug( "Starting Admin Test Recognition Plateau Non-Redemption Reminder Email Process with User Name: " + recipientUserName );
    try
    {
      Participant participant = participantService.getParticipantByUserName( recipientUserName );
      if ( participant != null )
      {
        Message plateauReminderMessage = messageService.getMessageByCMAssetCode( MessageService.RECOGNITION_PLATEAU_AWARDS_REMINDER_MESSAGE_CM_ASSET_CODE );

        /* normally sent by ProactiveEmailProcess */
        MailingRecipient mailingRecipient = sendRedemptionReminderMessage( participant, plateauReminderMessage.getId() );
        Mailing mailing = new Mailing();
        mailing.setGuid( GuidUtils.generateGuid() );
        mailing.setMailingType( MailingType.lookup( MailingType.PROMOTION ) );
        mailing.setMessage( plateauReminderMessage );
        mailing.setDeliveryDate( new Timestamp( new Date().getTime() ) );
        mailing.setSender( systemVariableService.getPropertyByName( SystemVariableService.SYSTEM_EMAIL_ADDRESS ).getStringVal() );
        mailing.addMailingRecipient( mailingRecipient );
        this.mailingService.submitMailing( mailing, null );

        addComment( "Email sent to user name " + recipientUserName );
      }
      else
      {
        addComment( "User name " + recipientUserName + " not available in the system." );
      }
    }
    catch( Exception e )
    {
      logErrorMessage( e );
      addComment( "An exception occurred while processing with user name: " + recipientUserName );
    }
  }

  public MailingRecipient sendRedemptionReminderMessage( Participant participant, Long messageId )
  {
    final String refNumber = "123456789";
    final String fullGiftCode = "1234567890123456";
    Promotion promotion = promotionService.getPromotionById( promotionId );

    MailingRecipient mailingRecipient = new MailingRecipient();
    mailingRecipient.setGuid( GuidUtils.generateGuid() );
    mailingRecipient.setUser( participant );
    mailingRecipient.setLocale( recipientLocale );

    MailingRecipientData firstName = new MailingRecipientData();
    firstName.setKey( "firstName" );
    firstName.setValue( mailingRecipient.getUser().getFirstName() );
    firstName.setMailingRecipient( mailingRecipient );

    MailingRecipientData lastName = new MailingRecipientData();
    lastName.setKey( "lastName" );
    lastName.setValue( mailingRecipient.getUser().getLastName() );
    lastName.setMailingRecipient( mailingRecipient );

    MailingRecipientData merchlinqLink = new MailingRecipientData();
    merchlinqLink.setKey( "levelLink" );
    merchlinqLink.setValue( "#" );

    merchlinqLink.setMailingRecipient( mailingRecipient );

    MailingRecipientData level = new MailingRecipientData();
    level.setKey( "level" );
    level.setValue( awardLevel );
    level.setMailingRecipient( mailingRecipient );

    MailingRecipientData referenceNumber = new MailingRecipientData();
    referenceNumber.setKey( "referenceNumber" );
    referenceNumber.setValue( refNumber );
    referenceNumber.setMailingRecipient( mailingRecipient );

    MailingRecipientData giftCode = new MailingRecipientData();
    giftCode.setKey( "giftCode" );
    giftCode.setValue( fullGiftCode );
    giftCode.setMailingRecipient( mailingRecipient );

    MailingRecipientData phoneNumber = new MailingRecipientData();
    phoneNumber.setKey( "phoneNumber" );
    phoneNumber.setValue( systemVariableService.getPropertyByName( SystemVariableService.MERCHLINQ_ORDER_PHONE ).getStringVal() );
    phoneNumber.setMailingRecipient( mailingRecipient );

    mailingRecipient.addMailingRecipientData( firstName );
    mailingRecipient.addMailingRecipientData( lastName );
    mailingRecipient.addMailingRecipientData( merchlinqLink );
    mailingRecipient.addMailingRecipientData( referenceNumber );
    mailingRecipient.addMailingRecipientData( giftCode );
    mailingRecipient.addMailingRecipientData( phoneNumber );
    mailingRecipient.addMailingRecipientData( level );

    if ( promotion.getPromoNameAssetCode() != null )
    {
      MailingRecipientData promotionName = new MailingRecipientData();
      promotionName.setKey( "promotionName" );
      promotionName.setValue( getPromotionName( promotion.getPromoNameAssetCode(), mailingRecipient.getLocale() ) );
      mailingRecipient.addMailingRecipientData( promotionName );
    }

    return mailingRecipient;
  }

  private String getPromotionName( String promoNameAssetCode, String userLocale )
  {
    Locale locale = CmsUtil.getLocale( userLocale );
    return cmAssetService.getString( promoNameAssetCode, Promotion.PROMOTION_NAME_KEY_PREFIX, locale, true );
  }

  public void setPromotionService( PromotionService promotionService )
  {
    this.promotionService = promotionService;
  }

  public void setCmAssetService( CMAssetService cmAssetService )
  {
    this.cmAssetService = cmAssetService;
  }

  public String getRecipientUserName()
  {
    return recipientUserName;
  }

  public void setRecipientUserName( String recipientUserName )
  {
    this.recipientUserName = recipientUserName;
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

  public String getAwardLevel()
  {
    return awardLevel;
  }

  public void setAwardLevel( String awardLevel )
  {
    this.awardLevel = awardLevel;
  }

}
