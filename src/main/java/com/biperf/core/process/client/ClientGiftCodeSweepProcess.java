
package com.biperf.core.process.client;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.domain.enums.MailingType;
import com.biperf.core.domain.mailing.Mailing;
import com.biperf.core.domain.mailing.MailingRecipient;
import com.biperf.core.domain.merchandise.MerchOrder;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.user.User;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.process.BaseProcessImpl;
import com.biperf.core.service.awardbanq.AwardBanQServiceFactory;
import com.biperf.core.service.awardbanq.GiftcodeStatusResponseValueObject;
import com.biperf.core.service.merchlevel.MerchLevelService;
import com.biperf.core.service.merchorder.MerchOrderService;
import com.biperf.core.service.message.MessageService;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.participant.UserCharacteristicService;
import com.biperf.core.service.system.SystemVariableService;

/**
 * ClientGiftCodeSweepProcess.
 * 
 * @author Dudam
 * @since Nov 1, 2016
 * @version 1.0
 */
public class ClientGiftCodeSweepProcess extends BaseProcessImpl
{

  public static final String BEAN_NAME = "clientGiftCodeSweepProcess";
  public static final String MESSAGE_NAME = "Client Gift Code Sweep Process Notification";

  private MerchOrderService merchOrderService;
  private MerchLevelService merchLevelService;
  private ParticipantService participantService;
  private AwardBanQServiceFactory awardBanQServiceFactory;
  private UserCharacteristicService userCharacteristicService;

  /**
   * The log for this class.
   */
  private static final Log log = LogFactory.getLog( ClientGiftCodeSweepProcess.class );

  // properties set from jobDataMap
  private String sweepPromoId;
  private String sweepMonthYear; // mmyyyy

  /**
   * Overridden from
   * 
   * @see com.biperf.core.process.BaseProcessImpl#onExecute()
   */
  public void onExecute()
  {
    log.info( "processName: " + BEAN_NAME + " - Calling Gift Code Sweep Process with Month/Year: " + sweepMonthYear + " promotionId: " + sweepPromoId );
    addComment( "processName: " + BEAN_NAME + " - Calling Gift Code Sweep Process with Month/Year: " + sweepMonthYear + " promotionId: " + sweepPromoId );

    String resultMsg = "";
    try
    {
      // get all unredeemed merch orders for the promotion
      List merchOrders = merchOrderService.getAllUnredeemedOrdersByPromotion( new Long( sweepPromoId ), sweepMonthYear );
      addComment( "**** merchOrders read : " + merchOrders.size() );

      int redeemedCount = 0;
      int processedCount = 0;
      String transactionDescription = systemVariableService.getPropertyByName( SystemVariableService.COKE_GIFT_CODE_SWEEP_TRANS_DESC ).getStringVal();

      for ( int i = 0; i < merchOrders.size(); i++ )
      {
        String merchOrderId = (String)merchOrders.get( i );
        MerchOrder order = merchOrderService.getMerchOrderById( new Long( merchOrderId ) );

        // since the status may have changed in OM, check each one before processing
        // MerchlinqGiftCodeStatusData status = merchLevelService.getGiftCodeStatus(
        // order.getFullGiftCode() );
        String programId = null;
        if ( order != null && order.getPromoMerchProgramLevel() != null )
        {
          programId = order.getPromoMerchProgramLevel().getProgramId();
        }
        GiftcodeStatusResponseValueObject status = merchLevelService.getGiftCodeStatus( order.getFullGiftCode(), programId, order.getOrderNumber(), order.getReferenceNumber() );
        if ( status.getBalanceAvailable() > 0 )
        {
          try
          {
            processedCount++;
            Participant participant = participantService.getParticipantById( order.getParticipant().getId() );
            String nodeCharBillingCode = "";
            String billingCode = "";
            
            /* WIP 37311 customization starts */
            int j = 0;
            
            String[] characteristicValue = new String[2];
            
            String charaterticsIds = systemVariableService.getPropertyByName( SystemVariableService.COKE_GIFTCODE_SWEEP_BILLCODE_LIST ).getStringVal();
            
            if(charaterticsIds != null && !charaterticsIds.isEmpty())
            {
             String[] charaterticsId = charaterticsIds.split( "," );
             for ( String charId : charaterticsId )
             {
            	characteristicValue[j] = userCharacteristicService.getCharacteristicValueByUserAndCharacterisiticId(participant.getId(), Long.parseLong(charId));
            	j++;
             }
            }
            /* WIP 37311 customization ends */
            Long pointsDeposited = getAwardBanQServiceFactory().getAwardBanQService().clientConvertGiftCode( participant.getId(),
                                                                                                             order.getFullGiftCode(),
                                                                                                             new Long( sweepPromoId ),
                                                                                                             nodeCharBillingCode,
                                                                                                             transactionDescription, characteristicValue);
            if ( pointsDeposited != null && pointsDeposited > 0 )
            {
              redeemedCount++;
            }
            else
            {
              addComment( "Giftcode : " + order.getFullGiftCode() + " was converted but no points were found on the gift code to convert." );
            }
            merchOrderService.updateOrderBillingCodeAndRedeem( order.getId(), billingCode );
          }
          catch( ServiceErrorException e )
          {
            log.error( "Exception while converting Giftcode: " + order.getFullGiftCode(), e );
            addComment( "Exception while converting Giftcode: " + order.getFullGiftCode() + " " + e.getMessage() );
            continue;
          }
        }
        else
        {
          addComment( "Giftcode: " + order.getFullGiftCode() + " was not converted because status is already redeemed." );
          merchOrderService.updateOrderStatus( order );
        }
      }
      resultMsg = resultMsg + " Process finished. Total merchOrders processed = " + processedCount + " Total converted to perqs = " + redeemedCount;
      addComment( "Process finished. Total merchOrders processed = " + processedCount + " Total converted to perqs = " + redeemedCount );
    }
    catch( Exception e )
    {
      logErrorMessage( e );
      resultMsg = "Process did not complete successfully. Error Message = " + e.getMessage();
      addComment( "Process did not complete successfully. Error Message = " + e.getMessage() );
    }

    log.info( "processName: " + BEAN_NAME + " - Completed Gift Code Sweep Process with month/year: " + sweepMonthYear + " promotionId: " + sweepPromoId );
    addComment( "processName: " + BEAN_NAME + " - Completed Gift Code Sweep Process with month/year: " + sweepMonthYear + " promotionId: " + sweepPromoId );

    // Notify the administrator.
    sendSummaryMessage( sweepMonthYear, sweepPromoId, resultMsg );
  }

  /**
   * Composes and sends a summary e-mail to the "run by" user the number of deposits attempted, and
   * of that how many were success and failures. Also email the total number of points deposited
   */
  private void sendSummaryMessage( String sweepMonthYear, String promotionId, String resultMsg )
  {
    User recipientUser = getRunByUser();

    // Add the summary info to the objectMap
    Map objectMap = new HashMap();
    objectMap.put( "firstName", recipientUser.getFirstName() );
    objectMap.put( "lastName", recipientUser.getLastName() );
    objectMap.put( "processName", BEAN_NAME );
    objectMap.put( "priorToDate", sweepMonthYear );
    objectMap.put( "promotionId", promotionId );

    objectMap.put( "message", resultMsg );

    // Compose the mailing
    Mailing mailing = composeMail( MessageService.CLIENT_GIFT_CODE_SWEEP_PROCESS_MESSAGE_CM_ASSET_CODE, MailingType.PROCESS_EMAIL );

    // Add the recipient
    MailingRecipient mr = addRecipient( recipientUser );
    mailing.addMailingRecipient( mr );

    try
    {
      // Send the e-mail message with personalization
      mailingService.submitMailing( mailing, objectMap );

      log.debug( "--------------------------------------------------------------------------------" );
      log.debug( "processName: " + BEAN_NAME + " has been sent to:" );
      log.debug( "Run By User: " + recipientUser.getFirstName() + " " + recipientUser.getLastName() );
      log.debug( "--------------------------------------------------------------------------------" );

      addComment( "processName: " + BEAN_NAME + " email has been sent to: " + recipientUser.getFirstName() + " " + recipientUser.getLastName() );
    }
    catch( Exception e )
    {
      log.error( "An exception occurred while sending " + MESSAGE_NAME + " Summary Email.  " + "(process invocation ID = " + getProcessInvocationId() + ")", e );
      addComment( "An exception occurred while sending " + MESSAGE_NAME + " Summary Email.  " + "See the log file for additional information.  " + "(process invocation ID = "
          + getProcessInvocationId() + ")" );
    }
  }

  public void setMerchOrderService( MerchOrderService merchOrderService )
  {
    this.merchOrderService = merchOrderService;
  }

  public MerchOrderService getMerchOrderService()
  {
    return this.merchOrderService;
  }

  public MerchLevelService getMerchLevelService()
  {
    return merchLevelService;
  }

  public void setMerchLevelService( MerchLevelService merchLevelService )
  {
    this.merchLevelService = merchLevelService;
  }

  public ParticipantService getParticipantService()
  {
    return participantService;
  }

  public void setParticipantService( ParticipantService participantService )
  {
    this.participantService = participantService;
  }

  public AwardBanQServiceFactory getAwardBanQServiceFactory()
  {
    return awardBanQServiceFactory;
  }

  public void setAwardBanQServiceFactory( AwardBanQServiceFactory awardBanQServiceFactory )
  {
    this.awardBanQServiceFactory = awardBanQServiceFactory;
  }

  public String getSweepPromoId()
  {
    return sweepPromoId;
  }

  public void setSweepPromoId( String sweepPromoId )
  {
    this.sweepPromoId = sweepPromoId;
  }

  public String getSweepMonthYear()
  {
    return sweepMonthYear;
  }

  public void setSweepMonthYear( String sweepMonthYear )
  {
    this.sweepMonthYear = sweepMonthYear;
  }

  public UserCharacteristicService getUserCharacteristicService() 
  {
	return userCharacteristicService;
  }

  public void setUserCharacteristicService(UserCharacteristicService userCharacteristicService) 
  {
	this.userCharacteristicService = userCharacteristicService;
  }

}
