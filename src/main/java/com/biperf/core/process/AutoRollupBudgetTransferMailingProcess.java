
package com.biperf.core.process;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.domain.enums.LanguageType;
import com.biperf.core.domain.enums.MailingType;
import com.biperf.core.domain.enums.PromotionAwardsType;
import com.biperf.core.domain.mailing.Mailing;
import com.biperf.core.domain.mailing.MailingRecipient;
import com.biperf.core.domain.user.User;
import com.biperf.core.service.budget.BudgetTransferService;
import com.biperf.core.service.message.MessageService;
import com.biperf.core.utils.GuidUtils;
import com.objectpartners.cms.domain.Content;
import com.objectpartners.cms.domain.Translations;
import com.objectpartners.cms.service.ContentReader;
import com.objectpartners.cms.util.CmsUtil;
import com.objectpartners.cms.util.ContentReaderManager;

public class AutoRollupBudgetTransferMailingProcess extends BaseProcessImpl
{

  public static final String BEAN_NAME = "AutoRollupBudgetTransferMailingProcess";
  public static final String MESSAGE_NAME = "AutoRollupBudgetTransferMailingProcess Notification";

  private BudgetTransferService budgetTransferService;

  /**
   * The logger for this class.
   */
  private static final Log logger = LogFactory.getLog( AutoRollupBudgetTransferMailingProcess.class );

  /**
   * Overridden from
   * 
   * @see com.biperf.core.process.BaseProcessImpl#onExecute()
   */
  public void onExecute()
  {

    int giverEmailCount = 0;
    int recEmailCount = 0;

    List giverUserIdList = budgetTransferService.getBudgetGivers();
    List recUserIdList = budgetTransferService.getBudgetReceivers();

    if ( giverUserIdList != null && giverUserIdList.size() > 0 )
    {
      for ( Iterator managerUserIdListIterator = giverUserIdList.iterator(); managerUserIdListIterator.hasNext(); )
      {
        Object[] userListObject = (Object[])managerUserIdListIterator.next();

        Long userId = (Long)userListObject[0];
        Long budgetAmount = (Long)userListObject[1];
        String budgetHolderPaxName = (String)userListObject[2];

        try
        {
          logger.debug( "Start writing to pdf for mgrUserId " + userId + "." );

          User mgrUser = userService.getUserById( userId );
          sendBudgetGiverMessage( mgrUser, userId, budgetAmount, budgetHolderPaxName );

          giverEmailCount++;

        }
        catch( Exception e )
        {
          logger.error( "Exception encountered  in AutoRollupBudgetTransferMailingProcess process " + e.getMessage() );
          e.printStackTrace();
          addComment( "Exception encountered  in AutoRollupBudgetTransferMailingProcess process. error= " + e.getMessage() );
        }

      }
    }

    if ( recUserIdList != null && recUserIdList.size() > 0 )
    {
      for ( Iterator managerUserIdList1Iterator = recUserIdList.iterator(); managerUserIdList1Iterator.hasNext(); )
      {
        Object[] userListObject = (Object[])managerUserIdList1Iterator.next();

        Long giverUserId = (Long)userListObject[0];
        Long recUserId = (Long)userListObject[1];
        Long budgetAmount = (Long)userListObject[2];
        String promotionName = (String)userListObject[3];
        Date dateOfRollup = (Date)userListObject[4];

        try
        {
          logger.debug( "Start writing to pdf for mgrUserId " + recUserId + "." );

          User giverUser = userService.getUserById( giverUserId );
          User recUser = userService.getUserById( recUserId );
          sendBudgetReciverMessage( giverUser, recUser, budgetAmount, promotionName, dateOfRollup );

          recEmailCount++;

        }
        catch( Exception e )
        {
          logger.error( "Exception encountered  in AutoRollupBudgetTransferMailingProcess process " + e.getMessage() );
          e.printStackTrace();
          addComment( "Exception encountered  in AutoRollupBudgetTransferMailingProcess process. error= " + e.getMessage() );
        }

      }
    }

    logger.error( "Total AutoRollupBudgetTransferMailingProcess Givers Emails Sent: " + giverEmailCount + " Recivers sent:" + recEmailCount );
    addComment( "Total AutoRollupBudgetTransferMailingProcess Givers Emails Sent: " + giverEmailCount + " Recivers sent:" + recEmailCount );

    logger.info( "**processName: " + BEAN_NAME + " - Completed AutoRollup Budget Transfer Mailing Process" );
    addComment( "**processName: " + BEAN_NAME + " - Completed AutoRollup Budget Transfer Mailing Process" );

    /*
     * ------------------------------------ SEND PROCESS MESSAGE
     */
    sendSummaryMessage( giverEmailCount, recEmailCount );
  }

  /**
   * sendBudgetGiverMessage
   */

  private void sendBudgetGiverMessage( User mgrUser, Long userId, Long budgetAmount, String budgetHolderPaxName )
  {
    String awardMedia = null;

    ContentReader contentReader = ContentReaderManager.getContentReader();
    List promotionAwardList = new ArrayList();
    if ( contentReader.getContent( "picklist.promotion.awardstype.items", CmsUtil.getLocale( mgrUser.getLanguageType().getCode() ) ) instanceof java.util.List )
    {
      promotionAwardList = (List)contentReader.getContent( "picklist.promotion.awardstype.items", CmsUtil.getLocale( mgrUser.getLanguageType().getCode() ) );
      for ( Object content : promotionAwardList )
      {
        Content contentData = (Content)content;
        Map m = contentData.getContentDataMapList();
        Translations nameObject = (Translations)m.get( "CODE" );

        if ( nameObject.getValue().equalsIgnoreCase( PromotionAwardsType.POINTS ) )
        {
          Translations valueObject = (Translations)m.get( "NAME" );
          awardMedia = valueObject.getValue();
          break;
        }
      }
    }

    // Compose the mailing
    Mailing mailing = composeMail( MessageService.BUDGET_TRANSFER_GIVER_EMAIL_MESSAGE_CM_ASSET_CODE, MailingType.PROCESS_EMAIL );

    Map objectMap = new HashMap();
    objectMap.put( "giverFirstName", mgrUser.getFirstName() );
    objectMap.put( "giverLastName", mgrUser.getLastName() );
    objectMap.put( "changedBudgetAmount", budgetAmount );
    objectMap.put( "awardMedia", awardMedia );
    objectMap.put( "budgetHolderPaxName", budgetHolderPaxName );

    mailing.addMailingRecipient( createMailingRecipient( mgrUser ) );

    try
    {
      logger.debug( "Send mailing for mgrUserId " + mgrUser.getId() );
      // process mailing
      mailingService.submitMailing( mailing, objectMap );

      updateReminderSent( mgrUser.getId() );

    }
    catch( Exception e )
    {
      logger.error( "An exception occurred while sending " + MESSAGE_NAME + " Manager Email.  " + "(process invocation ID = " + getProcessInvocationId() + ")", e );
      addComment( "An exception occurred while sending " + MESSAGE_NAME + " Manager Email.  " + "See the log file for additional information.  " + "(process invocation ID = "
          + getProcessInvocationId() + ")" );
    }
  }

  private void sendBudgetReciverMessage( User giverUser, User recUser, Long budgetAmount, String promotionName, Date dateOfRollup )
  {
    String awardMedia = null;

    ContentReader contentReader = ContentReaderManager.getContentReader();
    List promotionAwardList = new ArrayList();
    if ( contentReader.getContent( "picklist.promotion.awardstype.items", CmsUtil.getLocale( recUser.getLanguageType().getCode() ) ) instanceof java.util.List )
    {
      promotionAwardList = (List)contentReader.getContent( "picklist.promotion.awardstype.items", CmsUtil.getLocale( recUser.getLanguageType().getCode() ) );
      for ( Object content : promotionAwardList )
      {
        Content contentData = (Content)content;
        Map m = contentData.getContentDataMapList();
        Translations nameObject = (Translations)m.get( "CODE" );

        if ( nameObject.getValue().equalsIgnoreCase( PromotionAwardsType.POINTS ) )
        {
          Translations valueObject = (Translations)m.get( "NAME" );
          awardMedia = valueObject.getValue();
          break;
        }
      }
    }

    // Compose the mailing
    Mailing mailing = composeMail( MessageService.BUDGET_TRANSFER_RECIVER_EMAIL_MESSAGE_CM_ASSET_CODE, MailingType.PROCESS_EMAIL );

    Map objectMap = new HashMap();
    objectMap.put( "recipientFirstName", recUser.getFirstName() );
    objectMap.put( "recipientLastName", recUser.getLastName() );
    objectMap.put( "giverFirstName", giverUser.getFirstName() );
    objectMap.put( "giverLastName", giverUser.getLastName() );
    objectMap.put( "awardMedia", awardMedia );
    objectMap.put( "changedBudgetAmount", budgetAmount );
    objectMap.put( "promotionName", promotionName );
    objectMap.put( "dateOfRollup", dateOfRollup );

    mailing.addMailingRecipient( createMailingRecipient( recUser ) );

    try
    {
      logger.debug( "Send mailing for mgrUserId " + recUser.getId() );
      // process mailing
      mailingService.submitMailing( mailing, objectMap );

      updateReminderSent( recUser.getId() );

    }
    catch( Exception e )
    {
      logger.error( "An exception occurred while sending " + MESSAGE_NAME + " Manager Email.  " + "(process invocation ID = " + getProcessInvocationId() + ")", e );
      addComment( "An exception occurred while sending " + MESSAGE_NAME + " Manager Email.  " + "See the log file for additional information.  " + "(process invocation ID = "
          + getProcessInvocationId() + ")" );
    }
  }

  /**
   * sendSummaryMessage
   */
  private void sendSummaryMessage( int giverEmailCount, int recEmailCount )
  {
    User runUser = getRunByUser();

    // Add the summary info to the objectMap
    Map objectMap = new HashMap();
    objectMap.put( "firstName", runUser.getFirstName() );
    objectMap.put( "lastName", runUser.getLastName() );
    objectMap.put( "processName", BEAN_NAME );
    objectMap.put( "giverEmailCount", giverEmailCount );
    objectMap.put( "recEmailCount", recEmailCount );

    // Compose the mailing
    Mailing mailing = composeMail( MessageService.BUDGET_TRANSFER_PROCESS_MESSAGE_CM_ASSET_CODE, MailingType.PROCESS_EMAIL );

    // Add the recipient
    MailingRecipient mr = addRecipient( runUser );
    mailing.addMailingRecipient( mr );

    try
    {
      // Send the e-mail message with personalization
      mailingService.submitMailing( mailing, objectMap );

      logger.debug( "--------------------------------------------------------------------------------" );
      logger.debug( "processName: " + BEAN_NAME + " has been sent to:" );
      logger.debug( "Run By User: " + runUser.getFirstName() + " " + runUser.getLastName() );
      logger.debug( "--------------------------------------------------------------------------------" );

      addComment( "processName: " + BEAN_NAME + " email has been sent to: " + runUser.getFirstName() + " " + runUser.getLastName() );
    }
    catch( Exception e )
    {
      logger.error( "An exception occurred while sending " + MESSAGE_NAME + " Summary Email.  " + "(process invocation ID = " + getProcessInvocationId() + ")", e );
      addComment( "An exception occurred while sending " + MESSAGE_NAME + " Summary Email.  " + "See the log file for additional information.  " + "(process invocation ID = "
          + getProcessInvocationId() + ")" );
    }
  }

  private void updateReminderSent( Long mgrUserId )
  {
    int rowsUpdated = budgetTransferService.updateReminderSent( mgrUserId );

  }

  private MailingRecipient createMailingRecipient( User user )
  {
    MailingRecipient mailingRecipient = new MailingRecipient();
    mailingRecipient.setGuid( GuidUtils.generateGuid() );

    if ( user.getLanguageType() != null )
    {
      mailingRecipient.setLocale( user.getLanguageType().getCode() );
    }
    else
    {
      mailingRecipient.setLocale( LanguageType.ENGLISH );
    }

    mailingRecipient.setUser( user );

    return mailingRecipient;
  }

  public void setbudgetTransferService( BudgetTransferService budgetTransferService )
  {
    this.budgetTransferService = budgetTransferService;
  }

}
