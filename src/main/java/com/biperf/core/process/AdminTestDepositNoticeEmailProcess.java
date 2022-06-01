
package com.biperf.core.process;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.domain.enums.MailingType;
import com.biperf.core.domain.mailing.Mailing;
import com.biperf.core.domain.mailing.MailingRecipient;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.service.message.MessageService;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.utils.GuidUtils;

public class AdminTestDepositNoticeEmailProcess extends BaseProcessImpl
{

  private static final Log log = LogFactory.getLog( AdminTestDepositNoticeEmailProcess.class );

  public static final String PROCESS_NAME = "Admin Test Deposit Notice Email Process";
  public static final String BEAN_NAME = "adminTestDepositNoticeEmailProcess";

  private PromotionService promotionService;

  private String recipientUserName;
  private Long promotionId;
  private String recipientLocale;
  private String awardAmount;

  public AdminTestDepositNoticeEmailProcess()
  {
    super();
  }

  public void onExecute()
  {
    log.debug( "Starting Admin Test Deposit Notice Email Process with User Name: " + recipientUserName );
    try
    {
      Participant participant = participantService.getParticipantByUserName( recipientUserName );
      if ( participant != null )
      {
        /*
         * normally sent by DepositProcess(MailingType.SYSTEM) or selected on file
         * loads(MailingType.PROMOTION)
         */
        Promotion promotion = promotionService.getPromotionById( promotionId );

        // Compose the mailing
        Mailing mailing = composeMail( MessageService.DEPOSIT_NOTICE_MESSAGE_CM_ASSET_CODE, MailingType.PROMOTION );

        MailingRecipient mailingRecipient = new MailingRecipient();
        mailingRecipient.setGuid( GuidUtils.generateGuid() );
        mailingRecipient.setUser( participant );
        mailingRecipient.setLocale( recipientLocale );

        Map objectMap = new HashMap();
        // Add the mailing level data to the objectMap
        objectMap.put( "firstName", participant.getFirstName() );
        objectMap.put( "lastName", participant.getLastName() );
        Long transactionAmountLong = new Long( awardAmount );
        if ( transactionAmountLong.longValue() > 1 )
        {
          objectMap.put( "manyAwardAmount", "TRUE" );
        }
        objectMap.put( "awardAmount", String.valueOf( transactionAmountLong ) );
        objectMap.put( "mediaType", String.valueOf( promotion.getAwardType().getAbbr() ) );
        objectMap.put( "programName", promotion.getName() );
        // Message Library Insert Field is using promotionName for this token
        objectMap.put( "promotionName", promotion.getName() );
        objectMap.put( "websiteUrl",
                       systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal()
                           + systemVariableService.getPropertyByName( SystemVariableService.CLIENT_URL ).getStringVal() );

        mailing.addMailingRecipient( mailingRecipient );
        this.mailingService.submitMailing( mailing, objectMap );

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

  public void setPromotionService( PromotionService promotionService )
  {
    this.promotionService = promotionService;
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

  public String getAwardAmount()
  {
    return awardAmount;
  }

  public void setAwardAmount( String awardAmount )
  {
    this.awardAmount = awardAmount;
  }

}
