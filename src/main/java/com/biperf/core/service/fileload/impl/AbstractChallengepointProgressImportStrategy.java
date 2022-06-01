/*
 * (c) 2007 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/fileload/impl/AbstractChallengepointProgressImportStrategy.java,v $
 */

package com.biperf.core.service.fileload.impl;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.biperf.core.domain.challengepoint.ChallengepointProgress;
import com.biperf.core.domain.enums.MailingType;
import com.biperf.core.domain.enums.PromotionAwardsType;
import com.biperf.core.domain.enums.PromotionEmailNotificationType;
import com.biperf.core.domain.fileload.ImportFile;
import com.biperf.core.domain.goalquest.PaxGoal;
import com.biperf.core.domain.mailing.Mailing;
import com.biperf.core.domain.mailing.MailingBatch;
import com.biperf.core.domain.mailing.MailingRecipient;
import com.biperf.core.domain.message.Message;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.participant.ParticipantPartner;
import com.biperf.core.domain.promotion.ChallengePointPromotion;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.challengepoint.ChallengePointService;
import com.biperf.core.service.challengepoint.ChallengepointProgressService;
import com.biperf.core.service.email.MailingService;
import com.biperf.core.service.fileload.ImportStrategy;
import com.biperf.core.service.message.MessageService;
import com.biperf.core.service.participant.AudienceService;
import com.biperf.core.service.participant.ParticipantAssociationRequest;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.service.promotion.engine.ChallengePointAchievementStrategyFactory;
import com.biperf.core.service.promotion.engine.ChallengePointCalculationResult;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.GuidUtils;
import com.biperf.core.value.ChallengepointPaxValueBean;
import com.biperf.core.value.MailingBatchHolder;

/**
 * AbstractChallengepointProgressImportStrategy.
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
public abstract class AbstractChallengepointProgressImportStrategy extends ImportStrategy
{
  /** PaxGoalService **/
  private ChallengePointService challengePointService;
  private MessageService messageService;
  private MailingService mailingService;
  private PromotionService promotionService;
  private AudienceService audienceService;
  private ChallengepointProgressService challengepointProgressService;
  private static ChallengePointAchievementStrategyFactory challengePointAchievementStrategyFactory;
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
   * @param challengepointProgress
   * @param importFile 
   */
  public void sendEmailForPaxProgress( ChallengepointProgress challengepointProgress, ImportFile importFile, MailingBatchHolder mailingBatchHolder ) throws ServiceErrorException
  {
    ChallengePointPromotion promotion = challengepointProgress.getChallengePointPromotion();
    Participant participant = challengepointProgress.getParticipant();
    if ( promotion != null && participant != null && promotion.isNotificationRequired( PromotionEmailNotificationType.CHALLENGEPOINT_PROGRESS_UPDATED ) )
    {
      PaxGoal paxGoal = challengePointService.getPaxChallengePoint( promotion.getId(), participant.getId() );
      if ( paxGoal != null && !paxGoal.getParticipant().isOwner() )
      {
        ChallengePointCalculationResult challengePointCalculationResult = getChallengePointCalculationResult( promotion, paxGoal );

        // Bug Fix 19164. Avoid notifying Inactive Pax.
        if ( participant.isActive().booleanValue() )
        {
          Message message = messageService.getMessageByCMAssetCode( MessageService.CHALLENGEPOINT_PROGRESSUPDATED_MESSAGE_CM_ASSET_CODE );
          ChallengepointPaxValueBean challengepointPaxValueBean = getChallengepointProgressService().getChallengepointProgressSummary( paxGoal.getGoalQuestPromotion().getId(), participant.getId() );

          MailingRecipient mailingRecipient = mailingService.buildMailingRecipientForChallengepointEmail( promotion, participant, challengepointPaxValueBean, importFile, null );

          // Create mailing object
          Mailing mailing = new Mailing();
          mailing.setMailingBatch( mailingBatchHolder.getPaxProgressMailingBatch() );
          mailing.setMessage( message );
          mailing.addMailingRecipient( mailingRecipient );
          mailing.setDeliveryDate( new Timestamp( DateUtils.getCurrentDateAsLong() ) );
          mailing.setSender( "Challengepoint Promotion" );
          mailing.setMailingType( MailingType.lookup( MailingType.PROMOTION ) );
          mailing.setGuid( GuidUtils.generateGuid() );
          mailingService.submitMailing( mailing, null );

          // Send email to partner
          if ( promotion.isNotificationRequired( PromotionEmailNotificationType.CP_PARTNER_PROGRESS_UPDATED ) && promotion.getPartnerAudienceType() != null
              && promotion.getAwardType().getCode().equals( PromotionAwardsType.POINTS ) )
          {
            sendPartnerProgressUpdatedEmailNotification( participant, promotion, challengePointCalculationResult, importFile.getProgressEndDate(), mailingBatchHolder );
          }

        }
      }
    }
  }

  private void sendPartnerProgressUpdatedEmailNotification( Participant participant,
                                                            ChallengePointPromotion promotion,
                                                            ChallengePointCalculationResult goalCalculationResult,
                                                            Date progressEndDate,
                                                            MailingBatchHolder mailingBatchHolder )
  {
    // Get the Partner List
    ParticipantAssociationRequest paxAscReq = new ParticipantAssociationRequest( ParticipantAssociationRequest.PARTICIPANT );
    List partnerList = promotionService.getPartnersByPromotionAndParticipantWithAssociations( promotion.getId(), participant.getId(), paxAscReq );

    // Send message to partners if any picked by participant
    if ( partnerList.size() > 0 )
    {
      Message message = getMessageService().getMessageByCMAssetCode( MessageService.CHALLENGEPOINT_PARTNER_PROGRESSUPDATED_MESSAGE_CM_ASSET_CODE );
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
          HashMap dataMap = getMailingService().buildMailingRecipientForPartnerCPEmail( promotion, participant, goalCalculationResult, partner );
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
      mailing.setSender( "Challengepoint Promotion" );
      mailing.setMailingType( MailingType.lookup( MailingType.PROMOTION ) );
      mailing.setGuid( GuidUtils.generateGuid() );
      getMailingService().submitMailing( mailing, null );
    }
  }

  private ChallengePointCalculationResult getChallengePointCalculationResult( ChallengePointPromotion promotion, PaxGoal paxGoal )
  {
    ChallengePointCalculationResult challengePointCalculationResult = null;
    challengePointCalculationResult = getChallengePointAchievementStrategyFactory().getChallengePointAchievementStrategy( promotion.getAchievementRule().getCode() ).processChallengePoint( paxGoal );
    return challengePointCalculationResult;
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

  public ChallengePointService getChallengePointService()
  {
    return challengePointService;
  }

  public void setChallengePointService( ChallengePointService challengepointService )
  {
    this.challengePointService = challengepointService;
  }

  public static ChallengePointAchievementStrategyFactory getChallengePointAchievementStrategyFactory()
  {
    if ( challengePointAchievementStrategyFactory != null )
    {
      return challengePointAchievementStrategyFactory;
    }
    challengePointAchievementStrategyFactory = (ChallengePointAchievementStrategyFactory)BeanLocator.getBean( ChallengePointAchievementStrategyFactory.BEAN_NAME );
    return challengePointAchievementStrategyFactory;
  }

  public AudienceService getAudienceService()
  {
    return audienceService;
  }

  public void setAudienceService( AudienceService audienceService )
  {
    this.audienceService = audienceService;
  }

  public ChallengepointProgressService getChallengepointProgressService()
  {
    return challengepointProgressService;
  }

  public void setChallengepointProgressService( ChallengepointProgressService challengepointProgressService )
  {
    this.challengepointProgressService = challengepointProgressService;
  }

  public void setPromotionService( PromotionService promotionService )
  {
    this.promotionService = promotionService;
  }

}
