/*
 * (c) 2007 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/fileload/impl/AbstractGoalQuestProgressImportStrategy.java,v $
 */

package com.biperf.core.service.fileload.impl;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.biperf.core.domain.enums.ImportFileStatusType;
import com.biperf.core.domain.enums.ImportFileTypeType;
import com.biperf.core.domain.enums.MailingType;
import com.biperf.core.domain.enums.PromotionAwardsType;
import com.biperf.core.domain.enums.PromotionEmailNotificationType;
import com.biperf.core.domain.fileload.ImportFile;
import com.biperf.core.domain.goalquest.GoalQuestParticipantActivity;
import com.biperf.core.domain.goalquest.PaxGoal;
import com.biperf.core.domain.mailing.Mailing;
import com.biperf.core.domain.mailing.MailingBatch;
import com.biperf.core.domain.mailing.MailingRecipient;
import com.biperf.core.domain.message.Message;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.participant.ParticipantPartner;
import com.biperf.core.domain.promotion.GoalQuestPromotion;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.PromotionNotification;
import com.biperf.core.domain.promotion.PromotionNotificationType;
import com.biperf.core.service.email.MailingService;
import com.biperf.core.service.fileload.ImportService;
import com.biperf.core.service.fileload.ImportStrategy;
import com.biperf.core.service.goalquest.PaxGoalService;
import com.biperf.core.service.message.MessageService;
import com.biperf.core.service.participant.AudienceService;
import com.biperf.core.service.participant.ParticipantAssociationRequest;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.service.promotion.engine.GoalCalculationResult;
import com.biperf.core.service.promotion.engine.GoalPayoutStrategyFactory;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.GuidUtils;
import com.biperf.core.value.MailingBatchHolder;

/**
 * AbstractGoalQuestProgressImportStrategy.
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
 * <td>viswanat</td>
 * <td>Mar 23, 2007</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public abstract class AbstractGoalQuestProgressImportStrategy extends ImportStrategy
{
  /** PaxGoalService **/
  private PaxGoalService paxGoalService;
  private MessageService messageService;
  private MailingService mailingService;
  private AudienceService audienceService;
  private PromotionService promotionService;
  private GoalPayoutStrategyFactory goalPayoutStrategyFactory;
  private ImportService importService;
  private SystemVariableService systemVariableService;

  public SystemVariableService getSystemVariableService()
  {
    return systemVariableService;
  }

  public void setSystemVariableService( SystemVariableService systemVariableService )
  {
    this.systemVariableService = systemVariableService;
  }

  /**
   * @param goalQuestParticipantActivity
   * @param importFile 
   */
  public void sendEmailForPaxProgress( GoalQuestParticipantActivity goalQuestParticipantActivity, ImportFile importFile, MailingBatchHolder mailingBatchHolder )
  {
    GoalQuestPromotion promotion = goalQuestParticipantActivity.getGoalQuestPromotion();
    Participant participant = goalQuestParticipantActivity.getParticipant();
    if ( promotion != null && participant != null && promotion.isNotificationRequired( PromotionEmailNotificationType.PROGRESS_UPDATED ) )
    {
      PaxGoal paxGoal = paxGoalService.getPaxGoalByPromotionIdAndUserId( promotion.getId(), participant.getId() );
      if ( paxGoal != null )
      {
        GoalCalculationResult goalCalculationResult = getGoalCalculationResult( promotion, paxGoal );
        Message message = null;
        // Bug Fix 19164. Avoid notifying Inactive Pax.
        if ( participant.isActive().booleanValue() )
        {
          for ( Iterator<PromotionNotification> notificationsIter = promotion.getPromotionNotifications().iterator(); notificationsIter.hasNext(); )
          {
            PromotionNotificationType promotionNotificationType = (PromotionNotificationType)notificationsIter.next();
            // if notification is set up for a particular Email Notification Type then return true
            if ( promotionNotificationType.getPromotionEmailNotificationType().getCode().equals( PromotionEmailNotificationType.PROGRESS_UPDATED ) )
            {
              message = messageService.getMessageById( promotionNotificationType.getNotificationMessageId() );
              break;
            }
          }
          MailingRecipient mailingRecipient = mailingService.buildMailingRecipientForGoalQuestEmail( promotion, participant, goalCalculationResult, null, importFile );

          // Create mailing object
          Mailing mailing = new Mailing();
          mailing.setMailingBatch( mailingBatchHolder.getPaxProgressMailingBatch() );
          mailing.setMessage( message );
          mailing.addMailingRecipient( mailingRecipient );
          mailing.setDeliveryDate( new Timestamp( DateUtils.getCurrentDateAsLong() ) );
          mailing.setSender( "GoalQuest Promotion" );
          mailing.setMailingType( MailingType.lookup( MailingType.PROMOTION ) );
          mailing.setGuid( GuidUtils.generateGuid() );
          mailingService.submitMailing( mailing, null );

          // Send email to partner
          if ( promotion.isNotificationRequired( PromotionEmailNotificationType.PARTNER_PROGRESS_UPDATED ) && promotion.getPartnerAudienceType() != null
              && promotion.getAwardType().getCode().equals( PromotionAwardsType.POINTS ) )
          {
            sendPartnerProgressUpdatedEmailNotification( participant, promotion, goalCalculationResult, importFile.getProgressEndDate(), mailingBatchHolder );
          }
        }
      }
    }
  }

  private GoalCalculationResult getGoalCalculationResult( GoalQuestPromotion promotion, PaxGoal paxGoal )
  {
    GoalCalculationResult goalCalculationResult = null;
    if ( promotion.getAwardType().getCode().equals( PromotionAwardsType.POINTS ) )
    {
      goalCalculationResult = getGoalPayoutStrategyFactory().getGoalPayoutStrategy( promotion.getPayoutStructure().getCode() ).processGoal( paxGoal );
    }
    else
    // Merch or Travel
    {
      goalCalculationResult = getGoalPayoutStrategyFactory().getGoalPayoutStrategy().processGoal( paxGoal );
    }
    return goalCalculationResult;
  }

  /**
   * @param promotion
   * @param pax
   * @return boolean true if pax exists in primary or seconday audience else false
   */
  protected boolean isPaxInAudience( Promotion promotion, Participant pax )
  {
    return getAudienceService().isParticipantInPrimaryAudience( promotion, pax ) || getAudienceService().isParticipantInSecondaryAudience( promotion, pax, null );
  }

  private void sendPartnerProgressUpdatedEmailNotification( Participant participant,
                                                            GoalQuestPromotion promotion,
                                                            GoalCalculationResult goalCalculationResult,
                                                            Date progressEndDate,
                                                            MailingBatchHolder mailingBatchHolder )
  {
    // Get the Partner List
    ParticipantAssociationRequest paxAscReq = new ParticipantAssociationRequest( ParticipantAssociationRequest.PARTICIPANT );
    List partnerList = promotionService.getPartnersByPromotionAndParticipantWithAssociations( promotion.getId(), participant.getId(), paxAscReq );

    // Send message to partners if any picked by participant
    if ( partnerList.size() > 0 )
    {
      Message message = null;
      if ( promotion.getPromotionNotifications().size() > 0 )
      {
        Iterator<PromotionNotification> notificationsIter = promotion.getPromotionNotifications().iterator();
        while ( notificationsIter.hasNext() )
        {
          PromotionNotification notification = notificationsIter.next();
          if ( notification.isPromotionNotificationType() )
          {
            PromotionNotificationType promotionNotificationType = (PromotionNotificationType)notification;
            long messageId = promotionNotificationType.getNotificationMessageId();

            String notificationTypeCode = promotionNotificationType.getPromotionEmailNotificationType().getCode();

            if ( messageId > 0 && notificationTypeCode.equals( PromotionEmailNotificationType.PARTNER_PROGRESS_UPDATED ) )
            {
              message = messageService.getMessageById( messageId );
              break;
            }
          }
        }
      }

      Mailing mailing = new Mailing();

      if ( mailingBatchHolder != null && mailingBatchHolder.getPartnerProgressMailingBatch() == null ) // generate
                                                                                                       // Partner
                                                                                                       // Achieved
                                                                                                       // Batch
      {
        // create partner progress batch, and set in holder
        MailingBatch partnerProgressMailingBatch = mailingService.applyBatch( "<i>Partner Progress</i> " + promotion.getName() );
        mailingBatchHolder.setPartnerProgressMailingBatch( partnerProgressMailingBatch );
      }

      if ( mailingBatchHolder != null )
      {
        mailing.setMailingBatch( mailingBatchHolder.getPartnerProgressMailingBatch() );
      }
      mailing.setMessage( message );
      for ( Iterator iter = partnerList.iterator(); iter.hasNext(); )
      {
        Participant partner = ( (ParticipantPartner)iter.next() ).getPartner();
        if ( partner.isActive().booleanValue() )
        {
          HashMap dataMap = getMailingService().buildMailingRecipientForPartnerGoalQuestEmail( promotion, participant, goalCalculationResult, partner );
          if ( dataMap != null )
          {
            if ( dataMap.get( "date" ) != null )
            {
              if ( progressEndDate != null )
              {
                dataMap.remove( "date" );
                dataMap.put( "date", DateUtils.toDisplayString( progressEndDate ) );
              }
            }
          }
          MailingRecipient mailingRecipient = new MailingRecipient();
          mailingRecipient.setGuid( GuidUtils.generateGuid() );
          mailingRecipient.setUser( partner );
          mailingRecipient.setLocale( partner.getLanguageType() != null ? partner.getLanguageType().getCode() : systemVariableService.getDefaultLanguage().getStringVal() );
          if ( dataMap != null )
          {
            mailingRecipient.addMailingRecipientDataFromMap( dataMap );
          }
          mailing.addMailingRecipient( mailingRecipient );
        }
      }

      mailing.setDeliveryDate( new Timestamp( DateUtils.getCurrentDateAsLong() ) );
      mailing.setSender( "GoalQuest Promotion" );
      mailing.setMailingType( MailingType.lookup( MailingType.PROMOTION ) );
      mailing.setGuid( GuidUtils.generateGuid() );
      getMailingService().submitMailing( mailing, null );
    }
  }

  protected boolean isValidProgressSubmissionDate( Long promotionId, Date submissionDate, ImportFileTypeType importFileType )
  {
    ImportFileStatusType importFileStatusType = ImportFileStatusType.lookup( ImportFileStatusType.IMPORTED );
    Iterator iter = getImportService().getImportFiles( null, importFileType, importFileStatusType, null, null ).iterator();

    while ( iter.hasNext() )
    {
      ImportFile importFile = (ImportFile)iter.next();
      if ( promotionId.longValue() == importFile.getPromotion().getId().longValue() && submissionDate.before( importFile.getProgressEndDate() ) )
      {
        return false;
      }
    }

    return true;
  }

  public MailingService getMailingService()
  {
    return mailingService;
  }

  public void setMailingService( MailingService mailingService )
  {
    this.mailingService = mailingService;
  }

  public MessageService getMessageService()
  {
    return messageService;
  }

  public void setMessageService( MessageService messageService )
  {
    this.messageService = messageService;
  }

  public PaxGoalService getPaxGoalService()
  {
    return paxGoalService;
  }

  public void setPaxGoalService( PaxGoalService paxGoalService )
  {
    this.paxGoalService = paxGoalService;
  }

  public GoalPayoutStrategyFactory getGoalPayoutStrategyFactory()
  {
    if ( goalPayoutStrategyFactory == null )
    {
      this.setGoalPayoutStrategyFactory( (GoalPayoutStrategyFactory)BeanLocator.getBean( GoalPayoutStrategyFactory.BEAN_NAME ) );
    }
    return goalPayoutStrategyFactory;
  }

  public void setGoalPayoutStrategyFactory( GoalPayoutStrategyFactory goalPayoutStrategyFactory )
  {
    this.goalPayoutStrategyFactory = goalPayoutStrategyFactory;
  }

  public AudienceService getAudienceService()
  {
    return audienceService;
  }

  public void setAudienceService( AudienceService audienceService )
  {
    this.audienceService = audienceService;
  }

  public void setPromotionService( PromotionService promotionService )
  {
    this.promotionService = promotionService;
  }

  public ImportService getImportService()
  {
    if ( null == importService )
    {
      this.setImportService( (ImportService)BeanLocator.getBean( ImportService.BEAN_NAME ) );
    }
    return importService;
  }

  public void setImportService( ImportService importService )
  {
    this.importService = importService;
  }

}
