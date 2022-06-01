/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/process/RecognitionBudgetReminderProcess.java,v $
 */

package com.biperf.core.process;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.domain.budget.Budget;
import com.biperf.core.domain.budget.BudgetMaster;
import com.biperf.core.domain.budget.BudgetSegment;
import com.biperf.core.domain.country.Country;
import com.biperf.core.domain.enums.BudgetStatusType;
import com.biperf.core.domain.enums.MailingType;
import com.biperf.core.domain.enums.PromotionAwardsType;
import com.biperf.core.domain.enums.PromotionEmailNotificationType;
import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.domain.mailing.Mailing;
import com.biperf.core.domain.mailing.MailingRecipient;
import com.biperf.core.domain.mailing.MailingRecipientData;
import com.biperf.core.domain.message.Message;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.PromotionNotificationType;
import com.biperf.core.domain.promotion.RecognitionPromotion;
import com.biperf.core.domain.user.User;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.budget.BudgetMasterService;
import com.biperf.core.service.budget.BudgetMasterToBudgetSegmentsAssociationRequest;
import com.biperf.core.service.claim.ClaimService;
import com.biperf.core.service.cms.CMAssetService;
import com.biperf.core.service.hierarchy.NodeService;
import com.biperf.core.service.hierarchy.NodeToUsersAssociationRequest;
import com.biperf.core.service.message.MessageService;
import com.biperf.core.service.promotion.PromotionNotificationService;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.utils.BudgetUtils;
import com.biperf.core.utils.GuidUtils;
import com.biperf.core.utils.LocaleUtils;
import com.biperf.core.utils.NumberFormatUtil;
import com.objectpartners.cms.domain.Content;
import com.objectpartners.cms.domain.Translations;
import com.objectpartners.cms.service.ContentReader;
import com.objectpartners.cms.util.CmsUtil;
import com.objectpartners.cms.util.ContentReaderManager;

//

public class RecognitionBudgetReminderProcess extends BaseProcessImpl
{
  // ---------------------------------------------------------------------------
  // Constants
  // ---------------------------------------------------------------------------

  /**
   * The name of the Spring bean for this class.
   */
  public static final String BEAN_NAME = "recognitionBudgetReminderProcess";
  public static final String DEPOSIT_MESSAGE_NAME = "Budget Reminder";
  public static final String BUDGET_END_MESSAGE = "Recognition Budget Reminder Process";
  public static final String ADMIN_MESSAGE_NAME = "Recognition Budget Reminder Process";

  // ---------------------------------------------------------------------------
  // Fields
  // ---------------------------------------------------------------------------

  /**
   * The logger for this class.
   */
  private static final Log log = LogFactory.getLog( RecognitionBudgetReminderProcess.class );

  /**
   * The ID of the promotion 
   */
  private String promotionId;

  // Services injected
  protected PromotionService promotionService;
  protected PromotionNotificationService promotionNotificationService;
  protected BudgetMasterService budgetMasterService;
  protected NodeService nodeService;
  private CMAssetService cmAssetService;
  private ClaimService claimService;

  // ---------------------------------------------------------------------------
  // Public Methods
  // ---------------------------------------------------------------------------

  /**
   * Posts the approved journals of type "AwardPerqs" of the given promotion.
   */
  public void onExecute()
  {
    if ( isValid() )
    {
      Date now = new Date();
      int totalBudgetEndPaxCount = 0;
      Promotion promotion = promotionService.getPromotionById( new Long( promotionId ) );

      // Get Promotion Notifications
      @SuppressWarnings( "rawtypes" )
      ArrayList notifications = (ArrayList)promotionNotificationService.getPromotionTypeNotificationsByPromotionId( promotion.getId() );

      // For each Promotion Notification
      for ( Iterator notificationsIter = notifications.iterator(); notificationsIter.hasNext(); )
      {
        PromotionNotificationType promotionNotificationType = (PromotionNotificationType)notificationsIter.next();

        long messageId = promotionNotificationType.getNotificationMessageId();

        // Process only when a notification has been set up on the promotion
        if ( messageId > 0 )
        {
          String notificationTypeCode = promotionNotificationType.getPromotionEmailNotificationType().getCode();
          log.debug( "--------------------------------------------------------------------------------" );
          log.debug( "process :" + BEAN_NAME );
          log.debug( "promotion id:" + promotion.getId() );
          log.debug( "promotion status:" + promotion.isComplete() );
          log.debug( "notification type:" + notificationTypeCode );

          if ( notificationTypeCode.equals( PromotionEmailNotificationType.BUDGET_REMINDER ) && promotion.isLive() && promotion.isRecognitionPromotion()
              && ( promotion.getSubmissionEndDate() == null || promotion.getSubmissionEndDate() != null && now.before( promotion.getSubmissionEndDate() ) ) )
          {
            int budgetEndPaxCount = 0;
            RecognitionPromotion promoRecognition = (RecognitionPromotion)promotion;

            if ( promoRecognition.getBudgetMaster() != null && promoRecognition.isBudgetUsed() )
            {
              @SuppressWarnings( "rawtypes" )
              Set allElibigiblePaxs = new HashSet();
              allElibigiblePaxs.addAll( participantService.getAllEligiblePaxForPromotion( promotion.getId(), true ) );
              Long budgetMasterId = promoRecognition.getBudgetMaster().getId();
              AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
              associationRequestCollection.add( new BudgetMasterToBudgetSegmentsAssociationRequest() );
              BudgetMaster budgetMaster = budgetMasterService.getBudgetMasterById( budgetMasterId, associationRequestCollection );
              Map budgetMap = new HashMap();
              Set mailingRecipients = new HashSet();
              Mailing mailing = composeMail( new Long( messageId ), MailingType.PROMOTION );
              if ( budgetMaster.isActive() )
              {
                // Business Requirement to use system time zone instead of pax time zone for
                // improved performance and since Budget Reminder
                // should be scheduled to send few days before the budget end date
                String systemTimeZoneID = claimService.getDBTimeZone();
                BudgetSegment currentBudgetSegment = budgetMaster.getCurrentBudgetSegment( systemTimeZoneID );
                List<Budget> budgets = null;
                if ( currentBudgetSegment != null )
                {
                  budgets = budgetMasterService.getBudgetsByBudgetSegmentId( currentBudgetSegment.getId() );
                }
                if ( budgets != null && budgets.size() > 0 )
                {
                  Iterator iter = budgets.iterator();
                  while ( iter.hasNext() )
                  {
                    Budget tmpBudget = (Budget)iter.next();
                    // if ( tmpBudget.getEndDate() != null && now.before(tmpBudget.getEndDate()))
                    // {
                    // Budget End Notification date : X days prior to the Budget End
                    // Date budgetEndNotificationDate = new Date( tmpBudget.getEndDate().getTime()
                    // -k
                    // ( promotionNotificationType.getNumberOfDays().longValue() *
                    // DateUtils.MILLIS_PER_DAY ) );
                    // Current Date should be same as Budget End Date
                    // if ( DateUtils.isSameDay( now, budgetEndNotificationDate ) )
                    // {
                    if ( budgetMaster.isParticipantBudget() )
                    {
                      if ( null != tmpBudget.getUser() && BudgetStatusType.ACTIVE.equals( tmpBudget.getStatus().getCode() ) && tmpBudget.getOriginalValue().intValue() > 0
                          && tmpBudget.getCurrentValue().intValue() > 0 )
                      {
                        if ( ( promoRecognition.getAwardAmountFixed() == null && promoRecognition.getAwardAmountMin() == null && promoRecognition.getAwardAmountMax() == null )
                            || ( promoRecognition.getAwardAmountFixed() != null && tmpBudget.getCurrentValue().intValue() >= promoRecognition.getAwardAmountFixed()
                                || promoRecognition.getAwardAmountMin() != null && tmpBudget.getCurrentValue().intValue() >= promoRecognition.getAwardAmountMin() ) )
                        {
                          for ( Iterator paxIter = allElibigiblePaxs.iterator(); paxIter.hasNext(); )
                          {
                            Participant participant = (Participant)paxIter.next();
                            if ( tmpBudget.getUser().getId().equals( participant.getId() ) )
                            {
                              mailing.addMailingRecipient( createBudgetEndMessage( createMailingRecipient( participant ), promoRecognition, currentBudgetSegment, tmpBudget, budgetMaster ) );
                            }
                          }
                        }
                      }
                    }
                    else if ( budgetMaster.isNodeBudget() )
                    {
                      if ( null != tmpBudget.getNode() && BudgetStatusType.ACTIVE.equals( tmpBudget.getStatus().getCode() ) && tmpBudget.getOriginalValue().intValue() > 0
                          && tmpBudget.getCurrentValue().intValue() > 0 )
                      {
                        if ( ( promoRecognition.getAwardAmountFixed() == null && promoRecognition.getAwardAmountMin() == null && promoRecognition.getAwardAmountMax() == null )
                            || ( promoRecognition.getAwardAmountFixed() != null && tmpBudget.getCurrentValue().intValue() >= promoRecognition.getAwardAmountFixed()
                                || promoRecognition.getAwardAmountMin() != null && tmpBudget.getCurrentValue().intValue() >= promoRecognition.getAwardAmountMin() ) )
                        {
                          AssociationRequestCollection nodeAssociationRequestCollection = new AssociationRequestCollection();
                          nodeAssociationRequestCollection.add( new NodeToUsersAssociationRequest() );
                          Node node = nodeService.getNodeWithAssociationsById( tmpBudget.getNode().getId(), nodeAssociationRequestCollection );
                          List usersInNode = node.getActiveUserList();
                          for ( Iterator userIter = usersInNode.iterator(); userIter.hasNext(); )
                          {
                            User user = (User)userIter.next();
                            for ( Iterator paxIter = allElibigiblePaxs.iterator(); paxIter.hasNext(); )
                            {
                              Participant participant = (Participant)paxIter.next();
                              if ( user.getId().equals( participant.getId() ) )
                              {
                                if ( node.getId().equals( tmpBudget.getNode().getId() ) )
                                {
                                  mailing.addMailingRecipient( createBudgetEndMessage( createMailingRecipient( participant ), promoRecognition, currentBudgetSegment, tmpBudget, budgetMaster ) );
                                }
                              }
                            }
                          }
                        }
                      }
                    }
                    // }
                    // }
                  }
                  // Set mailingRecipients = createMailingRecipients( allElibigiblePaxs );

                  // Send Budget End Email to paxs
                  budgetEndPaxCount = sendBudgetEndMessage( mailing, promoRecognition );

                  // Add Comment to process invocation
                  addComment( "Promotion: " + promotion.getName() + ". Number of recipients attempted for Budget End Alert: " + budgetEndPaxCount );

                  log.debug( "Number of recipients attempted for Budget End Alert: " + budgetEndPaxCount );

                }
              }
            }
            totalBudgetEndPaxCount += budgetEndPaxCount;

            // Notify the administrator.
            sendSummaryMessage( totalBudgetEndPaxCount, BUDGET_END_MESSAGE );
          }
        }
      }
    }
  }

  /**
   * @param paxs
   * @return Set containing MailingRecipient objects for mailing
   */
  protected MailingRecipient createMailingRecipient( Participant pax )
  {
    MailingRecipient mr = new MailingRecipient();
    mr.setId( pax.getId() );
    mr.setGuid( GuidUtils.generateGuid() );

    if ( pax.getLanguageType() != null )
    {
      mr.setLocale( pax.getLanguageType().getCode() );
    }
    else
    {
      mr.setLocale( systemVariableService.getDefaultLanguage().getStringVal() );
    }

    mr.setUser( pax );
    // BugFix 21288..populate place holder values for site links
    Map dataMap = new HashMap();
    String siteLink = systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();
    dataMap.put( "siteLink", siteLink );
    dataMap.put( "siteURL", siteLink );
    dataMap.put( "webSite", siteLink );
    dataMap.put( "contactUsUrl", new StringBuffer( siteLink ).append( systemVariableService.getPropertyByName( SystemVariableService.CLIENT_CONTACT_URL ).getStringVal() ).toString() );
    mr.addMailingRecipientDataFromMap( dataMap );

    return mr;

  }

  /**
   * Compose and send the Budget End alert emails
   * 
   * @param recipients
   * @param messageId
   * @param promo
   */
  protected int sendBudgetEndMessage( Mailing mailing, RecognitionPromotion promoRecognition )
  {
    int recipientCount = 0;

    // Add the personalization data to the objectMap
    Map objectMap = new HashMap();
    objectMap.put( "promotionName", promoRecognition.getName() );
    objectMap.put( "siteUrl", systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal() );
    objectMap.put( "companyName", systemVariableService.getPropertyByName( SystemVariableService.CLIENT_NAME ).getStringVal() );

    if ( mailing != null && mailing.getMailingRecipients() != null )
    {
      recipientCount = mailing.getMailingRecipients().size();
      if ( recipientCount > 0 )
      {
        // Send the e-mail message with personalization
        try
        {
          mailingService.submitMailing( mailing, objectMap );

          log.debug( "Number of recipients attempted for Budget Reminder/End Email: " + recipientCount );
          addComment( recipientCount + " users need Budget End email for promotion: " + promoRecognition.getName() );
        }
        catch( Exception e )
        {
          log.error( "An exception occurred while sending Budget Reminder/End Email for promotion: " + promoRecognition.getName() + " (process invocation ID = " + getProcessInvocationId() + ")", e );
          addComment( "An exception occurred while sending Budget Reminder/End Email for promotion: " + promoRecognition.getName() + ". See the log file for additional information.  "
              + "(process invocation ID = " + getProcessInvocationId() + ")" );
        }
      }
    }

    return recipientCount;
  }

  /**
   * Compose and send the Budget End alert emails
  //   * 
   * @param recipients
   * @param messageId
   * @param promo
   */
  protected MailingRecipient createBudgetEndMessage( MailingRecipient mr, RecognitionPromotion promoRecognition, BudgetSegment currentBudgetSegment, Budget budgetData, BudgetMaster budgetMaster )
  {
    // Set recipient specific data on the MailingRecipientData
    MailingRecipientData mrd1 = new MailingRecipientData();
    mrd1.setKey( "firstName" );
    mrd1.setValue( mr.getUser().getFirstName() );
    mr.addMailingRecipientData( mrd1 );

    MailingRecipientData mrd2 = new MailingRecipientData();
    mrd2.setKey( "lastName" );
    mrd2.setValue( mr.getUser().getLastName() );
    mr.addMailingRecipientData( mrd2 );

    MailingRecipientData mrd3 = new MailingRecipientData();
    mrd3.setKey( "budgetRemaining" );
    BigDecimal targetMediaValue = userService.getBudgetMediaValueForUser( mr.getId() );
    BigDecimal currentMediaValue = countryService.getBudgetMediaValueByCountryCode( Country.UNITED_STATES );
    BigDecimal convertedBudgetAmount = BudgetUtils.applyMediaConversion( budgetData.getCurrentValue(), currentMediaValue, targetMediaValue );
    String adjstmtValue = NumberFormatUtil.getUserLocaleBasedNumberFormat( (long)Math.floor( convertedBudgetAmount.doubleValue() ), LocaleUtils.getLocale( mr.getLocale() ) );
    mrd3.setValue( adjstmtValue );
    mr.addMailingRecipientData( mrd3 );

    MailingRecipientData mrd4 = new MailingRecipientData();
    mrd4.setKey( "budgetName" );
    mrd4.setValue( budgetMaster.getBudgetName() );
    mr.addMailingRecipientData( mrd4 );

    MailingRecipientData mrd5 = new MailingRecipientData();
    if ( promoRecognition.getSubmissionEndDate() != null && currentBudgetSegment.getEndDate() != null )
    {
      if ( promoRecognition.getSubmissionEndDate().before( currentBudgetSegment.getEndDate() ) )
      {
        mrd5.setKey( "endDate" );
        mrd5.setValue( com.biperf.core.utils.DateUtils.toDisplayString( promoRecognition.getSubmissionEndDate(), LocaleUtils.getLocale( mr.getLocale() ) ) );
      }
      else
      {
        mrd5.setKey( "endDate" );
        mrd5.setValue( com.biperf.core.utils.DateUtils.toDisplayString( currentBudgetSegment.getEndDate(), LocaleUtils.getLocale( mr.getLocale() ) ) );
      }
    }
    else if ( promoRecognition.getSubmissionEndDate() != null )
    {
      mrd5.setKey( "endDate" );
      mrd5.setValue( com.biperf.core.utils.DateUtils.toDisplayString( promoRecognition.getSubmissionEndDate(), LocaleUtils.getLocale( mr.getLocale() ) ) );
    }

    mr.addMailingRecipientData( mrd5 );

    if ( promoRecognition.getAwardType() != null )
    {
      MailingRecipientData awardMedia = new MailingRecipientData();
      awardMedia.setKey( "awardMedia" );
      // awardMedia.setValue( cmAssetService.getString( "picklist.promotion.awardstype.items",
      // "NAME", CmsUtil.getLocale( mr.getLocale() ), true ) );
      awardMedia.setValue( PromotionAwardsType.lookup( promoRecognition.getAwardType().getCode() ).getName() ); // Code
                                                                                                                // chnage
                                                                                                                // for
                                                                                                                // 66716
      ContentReader contentReader = ContentReaderManager.getContentReader();
      List promotionAwardList = new ArrayList();
      if ( contentReader.getContent( "picklist.promotion.awardstype.items", CmsUtil.getLocale( mr.getLocale() ) ) instanceof java.util.List )
      {
        promotionAwardList = (List)contentReader.getContent( "picklist.promotion.awardstype.items", CmsUtil.getLocale( mr.getLocale() ) );
        for ( Object content : promotionAwardList )
        {
          Content contentData = (Content)content;
          Map m = contentData.getContentDataMapList();
          Translations nameObject = (Translations)m.get( "CODE" );

          if ( nameObject.getValue().equalsIgnoreCase( PromotionAwardsType.POINTS ) )
          {
            Translations valueObject = (Translations)m.get( "NAME" );
            awardMedia.setValue( valueObject.getValue() );
            mr.addMailingRecipientData( awardMedia );
            break;
          }
        }
      }

    }

    return mr;
  }

  /**
   * Sets the ID of the promotion whose approved journals this process will post.
   * 
   * @param promotionId the ID of the promotion whose approved journals this process will post.
   */
  public void setPromotionId( String promotionId )
  {
    this.promotionId = promotionId;
  }

  // ---------------------------------------------------------------------------
  // Private Methods
  // ---------------------------------------------------------------------------

  /**
   * Composes and sends the summary e-mail to the "run by" user.
   * 
   * @param recipientCount
   * @param processType
   */
  protected void sendSummaryMessage( long recipientCount, String processType )
  {
    User recipientUser = getRunByUser();

    // Add the summary info to the objectMap
    Map objectMap = new HashMap();
    objectMap.put( "firstName", recipientUser.getFirstName() );
    objectMap.put( "lastName", recipientUser.getLastName() );
    objectMap.put( "processName", BEAN_NAME );
    objectMap.put( "processType", processType );
    objectMap.put( "count", new Long( recipientCount ) );
    objectMap.put( "clientName", systemVariableService.getPropertyByName( SystemVariableService.CLIENT_NAME ).getStringVal() );
    objectMap.put( "clientPrefix", systemVariableService.getPropertyByName( SystemVariableService.CLIENT_PREFIX ).getStringVal() );

    // Compose the mailing
    Message m = messageService.getMessageByCMAssetCode( MessageService.RECOGNITION_BUDGET_REMINDER_SUMMARY_CM_ASSET_CODE );
    Mailing mailing = composeMail( m.getId(), MailingType.PROMOTION );

    // Add the recipient
    MailingRecipient mr = addRecipient( recipientUser );
    mailing.addMailingRecipient( mr );

    // Send the e-mail message with personalization
    try
    {
      mailingService.submitMailing( mailing, objectMap );

      log.debug( "--------------------------------------------------------------------------------" );
      log.debug( "process: " + processType + " has been sent to:" );
      log.debug( "Run By User: " + recipientUser.getFirstName() + " " + recipientUser.getLastName() );
      log.debug( "number of recipients: " + recipientCount );
      log.debug( "--------------------------------------------------------------------------------" );

      addComment( "process: " + processType + " has been sent to the process scheduler " + recipientUser.getFirstName() + " " + recipientUser.getLastName() + ". Total number of pax recipients: "
          + recipientCount );
    }
    catch( Exception e )
    {
      log.error( "An exception occurred while sending " + processType + " (process invocation ID = " + getProcessInvocationId() + ")", e );
      addComment( "An exception occurred while sending " + processType + ". See the log file for additional information.  " + "(process invocation ID = " + getProcessInvocationId() + ")" );
    }
  }

  /**
   * Returns true if this <code>DepositProcess</code> object is in a valid state; returns false
   * otherwise.
   * 
   * @return true if this <code>DepositProcess</code> object is in a valid state; returns false
   *         otherwise.
   */
  private boolean isValid()
  {
    boolean isValid = true;

    if ( promotionId == null )
    {
      isValid = false;
      log.error( "Invalid state: Field \"promotionId\" is null." );
    }

    try
    {
      new Long( promotionId );
    }
    catch( NumberFormatException e )
    {
      isValid = false;
      log.error( "Invalid state: Field \"promotionId\" does not represent an integer." );
    }

    return isValid;
  }

  public void setPromotionService( PromotionService promotionService )
  {
    this.promotionService = promotionService;
  }

  public void setPromotionNotificationService( PromotionNotificationService promotionNotificationService )
  {
    this.promotionNotificationService = promotionNotificationService;
  }

  public void setNodeService( NodeService nodeService )
  {
    this.nodeService = nodeService;
  }

  public void setBudgetMasterService( BudgetMasterService budgetMasterService )
  {
    this.budgetMasterService = budgetMasterService;
  }

  public void setCmAssetService( CMAssetService cmAssetService )
  {
    this.cmAssetService = cmAssetService;
  }

  public void setClaimService( ClaimService claimService )
  {
    this.claimService = claimService;
  }

}
