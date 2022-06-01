
package com.biperf.core.service.goalquest.impl;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.dao.goalquest.PaxGoalDAO;
import com.biperf.core.dao.participant.ParticipantPartnerDAO;
import com.biperf.core.domain.enums.MailingType;
import com.biperf.core.domain.enums.PromotionAwardsType;
import com.biperf.core.domain.enums.PromotionEmailNotificationType;
import com.biperf.core.domain.goalquest.PaxGoal;
import com.biperf.core.domain.mailing.Mailing;
import com.biperf.core.domain.mailing.MailingRecipient;
import com.biperf.core.domain.message.Message;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.participant.ParticipantPartner;
import com.biperf.core.domain.promotion.ChallengePointPromotion;
import com.biperf.core.domain.promotion.GoalLevel;
import com.biperf.core.domain.promotion.GoalQuestPromotion;
import com.biperf.core.domain.promotion.PromotionNotification;
import com.biperf.core.domain.promotion.PromotionNotificationType;
import com.biperf.core.domain.user.User;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.challengepoint.ChallengePointService;
import com.biperf.core.service.email.MailingService;
import com.biperf.core.service.goalquest.PaxGoalService;
import com.biperf.core.service.message.MessageService;
import com.biperf.core.service.participant.ParticipantAssociationRequest;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.service.promotion.PromotionAssociationRequest;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.service.promotion.engine.ChallengePointAchievementStrategyFactory;
import com.biperf.core.service.promotion.engine.ChallengePointCalculationResult;
import com.biperf.core.service.promotion.engine.GoalCalculationResult;
import com.biperf.core.service.promotion.engine.GoalPayoutStrategyFactory;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.GuidUtils;
import com.biperf.core.value.ChallengepointPaxValueBean;

public class PaxGoalServiceImpl implements PaxGoalService
{
  private static final Log log = LogFactory.getLog( PaxGoalServiceImpl.class );

  private PaxGoalDAO paxGoalDAO;
  private ParticipantPartnerDAO participantPartnerDAO;
  private MessageService messageService = null;
  private MailingService mailingService = null;
  private PromotionService promotionService = null;
  private SystemVariableService systemVariableService = null;
  private UserService userService = null;
  private ChallengePointService challengePointService = null;

  /**
   * Set the paxGoalDAO through IoC.
   * 
   * @param paxGoalDAO
   */
  public void setPaxGoalDAO( PaxGoalDAO paxGoalDAO )
  {
    this.paxGoalDAO = paxGoalDAO;
  }

  /**
   * Get the paxGoal by the id param. Overridden from
   * 
   * @see com.biperf.core.service.goalquest.PaxGoalService#getPaxGoalById(java.lang.Long)
   * @param id
   * @return PaxGoal
   */
  public PaxGoal getPaxGoalById( Long id )
  {
    return this.paxGoalDAO.getPaxGoalById( id );
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.goalquest.PaxGoalService#getPaxGoalByIdWithAssociations(java.lang.Long,
   *      com.biperf.core.service.AssociationRequestCollection)
   * @param id
   * @param associationRequestCollection
   * @return PaxGoal
   */
  public PaxGoal getPaxGoalByIdWithAssociations( Long id, AssociationRequestCollection associationRequestCollection )
  {
    return this.paxGoalDAO.getPaxGoalByIdWithAssociations( id, associationRequestCollection );
  }

  /**
   * Gets a PaxGoal object by promotionId and userId.
   * 
   * @param promotionId 
   * @param userId a participant's userID.
   * @return the specified PaxGoal object.
   */
  public PaxGoal getPaxGoalByPromotionIdAndUserId( Long promotionId, Long userId )
  {
    return this.paxGoalDAO.getPaxGoalByPromotionIdAndUserId( promotionId, userId );
  }

  /**
   * Gets a PaxGoal object by promotionId and userId.
   * 
   * @param promotionId 
   * @param userId a participant's userID.
   * @param associationRequestCollection
   * @return the specified PaxGoal object.
   */
  public PaxGoal getPaxGoalByPromotionIdAndUserId( Long promotionId, Long userId, AssociationRequestCollection associationRequestCollection )
  {
    PaxGoal paxGoal = this.paxGoalDAO.getPaxGoalByPromotionIdAndUserId( promotionId, userId );
    if ( associationRequestCollection != null )
    {
      associationRequestCollection.process( paxGoal );
    }
    return paxGoal;
  }

  /**
   * Get the paxGoal of a participant by promotion id.
   * 
   * @param promotionId
   * @param associationRequestCollection
   * @return paxGoal
   */
  public List<PaxGoal> getPaxGoalByPromotionId( Long promotionId, AssociationRequestCollection associationRequestCollection )
  {
    return this.paxGoalDAO.getPaxGoalByPromotionId( promotionId, associationRequestCollection );
  }

  public PaxGoal savePaxGoalWithPartners( Long promotionId, PaxGoal paxGoal, List<ParticipantPartner> partners )
  {
    paxGoal = savePaxGoal( paxGoal );
    participantPartnerDAO.clearPartnersForParticipantAndPromotion( promotionId, paxGoal.getParticipant().getId() );
    for ( ParticipantPartner partner : partners )
    {
      participantPartnerDAO.saveParticipantPartnerAssoc( partner );
    }

    GoalQuestPromotion goalQuestPromotion = null;

    AssociationRequestCollection promoAssociationRequestCollection = new AssociationRequestCollection();
    promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.NOTIFICATIONS ) );
    promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.PROMOTION_PARTICIPANT_PARTNERS ) );

    goalQuestPromotion = (GoalQuestPromotion)promotionService.getPromotionByIdWithAssociations( promotionId, promoAssociationRequestCollection );

    /*
     * if( paxGoal.getGoalLevel() != null && goalQuestPromotion.getPartnerAudienceType() != null &&
     * goalQuestPromotion.getAwardType().getCode().equals( PromotionAwardsType.POINTS ) ) { List
     * paxPartners = getPaxPartners(goalQuestPromotion,paxGoal); if(
     * goalQuestPromotion.isNotificationRequired( PromotionEmailNotificationType.PARTNER_SELECTED )
     * && paxGoal.getParticipant().isActive().booleanValue() && paxPartners != null &&
     * paxPartners.size() > 0 ) { if ( goalQuestPromotion.isNotificationRequired(
     * PromotionEmailNotificationType.GOAL_SELECTED ) &&
     * paxGoal.getParticipant().isActive().booleanValue() ) {
     * sendGoalSelectedEmailNotification(paxGoal , paxPartners); }
     * sendPartnerGoalSelectedEmailNotification( paxGoal , paxPartners); }else if (
     * goalQuestPromotion.isNotificationRequired( PromotionEmailNotificationType.GOAL_SELECTED ) &&
     * paxGoal.getParticipant().isActive().booleanValue() ) {
     * sendGoalSelectedEmailNotification(paxGoal , paxPartners); } }else
     */
    List paxPartners = null;
    if ( paxGoal.getGoalLevel() != null && goalQuestPromotion.getPartnerAudienceType() != null && goalQuestPromotion.getAwardType().getCode().equals( PromotionAwardsType.POINTS ) )
    {
      paxPartners = getPaxPartners( goalQuestPromotion, paxGoal );
    }

    if ( goalQuestPromotion.isGoalQuestPromotion() && goalQuestPromotion.isNotificationRequired( PromotionEmailNotificationType.GOAL_SELECTED ) )
    {
      sendGoalSelectedEmailNotification( paxGoal, paxPartners );
    }

    else if ( goalQuestPromotion.isChallengePointPromotion() && goalQuestPromotion.isNotificationRequired( PromotionEmailNotificationType.CHALLENGEPOINT_SELECTED ) )
    {
      try
      {
        sendChallengepointSelectedEmailNotification( paxGoal, paxPartners );
      }
      catch( ServiceErrorException e )
      {
        log.error( e.getMessage(), e );
      }
    }

    return paxGoal;
  }

  /**
   * Get the paxGoal of a participant by promotion id.
   * 
   * @param promotionId
   * @return paxGoal
   */
  public List<PaxGoal> getPaxGoalByPromotionId( Long promotionId )
  {
    return this.paxGoalDAO.getPaxGoalByPromotionId( promotionId );
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.goalquest.PaxGoalService#savePaxGoal(com.biperf.core.domain.goalquest.PaxGoal)
   * @param paxGoal
   * @return PaxGoal
   */
  public PaxGoal savePaxGoal( PaxGoal paxGoal )
  {
    return this.paxGoalDAO.savePaxGoal( paxGoal );
  }

  public ParticipantPartnerDAO getParticipantPartnerDAO()
  {
    return participantPartnerDAO;
  }

  public void setParticipantPartnerDAO( ParticipantPartnerDAO participantPartnerDAO )
  {
    this.participantPartnerDAO = participantPartnerDAO;
  }

  private void sendChallengepointSelectedEmailNotification( PaxGoal paxGoal, List paxPartners ) throws ServiceErrorException
  {
    ChallengePointPromotion challengepointPromotion = null;
    Long cpPromotionId = paxGoal.getGoalQuestPromotion().getId();

    AssociationRequestCollection promoAssociationRequestCollection = new AssociationRequestCollection();
    promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.NOTIFICATIONS ) );
    challengepointPromotion = (ChallengePointPromotion)getPromotionService().getPromotionByIdWithAssociations( cpPromotionId, promoAssociationRequestCollection );

    ChallengepointPaxValueBean challengepointPaxValueBean = challengePointService
        .populateChallengepointPaxValueBean( paxGoal, challengepointPromotion, paxGoal.getGoalLevel() != null ? (GoalLevel)paxGoal.getGoalLevel() : null, false );

    Message message = null;
    if ( challengepointPromotion.getPromotionNotifications().size() > 0 )
    {
      Iterator<PromotionNotification> notificationsIter = challengepointPromotion.getPromotionNotifications().iterator();
      while ( notificationsIter.hasNext() )
      {
        PromotionNotification notification = notificationsIter.next();
        if ( notification.isPromotionNotificationType() )
        {
          PromotionNotificationType promotionNotificationType = (PromotionNotificationType)notification;
          long messageId = promotionNotificationType.getNotificationMessageId();

          String notificationTypeCode = promotionNotificationType.getPromotionEmailNotificationType().getCode();

          if ( messageId > 0 && notificationTypeCode.equals( PromotionEmailNotificationType.CHALLENGEPOINT_SELECTED ) )
          {
            message = messageService.getMessageById( messageId );
            break;
          }
        }
      }
    }
    MailingRecipient mailingRecipient = getMailingService().buildMailingRecipientForChallengepointEmail( challengepointPromotion,
                                                                                                         paxGoal.getParticipant(),
                                                                                                         challengepointPaxValueBean,
                                                                                                         null,
                                                                                                         paxPartners );
    // Create mailing object
    Mailing mailing = new Mailing();
    mailing.setMessage( message );
    mailing.addMailingRecipient( mailingRecipient );
    mailing.setDeliveryDate( new Timestamp( DateUtils.getCurrentDateAsLong() ) );
    mailing.setSender( "Challengepoint Promotion" );
    mailing.setMailingType( MailingType.lookup( MailingType.PROMOTION ) );
    mailing.setGuid( GuidUtils.generateGuid() );
    getMailingService().submitMailing( mailing, null );
  }

  public void sendCPPartnerGoalSelectedEmailNotification( PaxGoal paxGoal, Participant partner )
  {
    ChallengePointPromotion challengepointPromotion = null;
    Long cpPromotionId = paxGoal.getGoalQuestPromotion().getId();

    AssociationRequestCollection promoAssociationRequestCollection = new AssociationRequestCollection();
    promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.NOTIFICATIONS ) );
    challengepointPromotion = (ChallengePointPromotion)getPromotionService().getPromotionByIdWithAssociations( cpPromotionId, promoAssociationRequestCollection );

    Message message = null;
    if ( challengepointPromotion.getPromotionNotifications().size() > 0 )
    {
      Iterator<PromotionNotification> notificationsIter = challengepointPromotion.getPromotionNotifications().iterator();
      while ( notificationsIter.hasNext() )
      {
        PromotionNotification notification = notificationsIter.next();
        if ( notification.isPromotionNotificationType() )
        {
          PromotionNotificationType promotionNotificationType = (PromotionNotificationType)notification;
          long messageId = promotionNotificationType.getNotificationMessageId();

          String notificationTypeCode = promotionNotificationType.getPromotionEmailNotificationType().getCode();

          if ( messageId > 0 && notificationTypeCode.equals( PromotionEmailNotificationType.CP_PARTNER_GOAL_SELECTED ) )
          {
            message = messageService.getMessageById( messageId );
            break;

          }
        }
      }
    }

    Mailing mailing = new Mailing();
    mailing.setMessage( message );

    HashMap dataMap = null;
    ChallengePointCalculationResult challengePointCalculationResult = null;

    ChallengePointAchievementStrategyFactory goalPayoutStrategyFactory = (ChallengePointAchievementStrategyFactory)BeanLocator.getBean( ChallengePointAchievementStrategyFactory.BEAN_NAME );
    challengePointCalculationResult = goalPayoutStrategyFactory.getChallengePointAchievementStrategy( ( (ChallengePointPromotion)paxGoal.getGoalQuestPromotion() ).getAchievementRule().getCode() )
        .processChallengePoint( paxGoal );
    dataMap = mailingService.buildMailingRecipientForPartnerCPEmail( challengepointPromotion, paxGoal.getParticipant(), challengePointCalculationResult, partner );
    if ( null != partner && !partner.isWelcomeEmailSent().booleanValue() )
    {
      // BugFix 20237.
      dataMap.put( "welcomeEmailSend", "true" );
      partner.setPassword( buildPassword() );
      updateUser( partner );
      if ( dataMap.get( "password" ) != null )
      {
        dataMap.remove( "password" );
        dataMap.put( "password", partner.getPassword() );
      }
      else
      {
        dataMap.remove( "password" );
        dataMap.put( "password", partner.getPassword() );
      }
    }
    else
    {
      if ( dataMap.get( "password" ) != null )
      {
        dataMap.remove( "password" );
      }
      if ( dataMap.get( "user" ) != null )
      {
        dataMap.remove( "user" );
      }
    }

    MailingRecipient mailingRecipient = new MailingRecipient();
    mailingRecipient.setGuid( GuidUtils.generateGuid() );
    mailingRecipient.setUser( partner );
    if ( partner != null && partner.getLanguageType() != null )
    {
      mailingRecipient.setLocale( partner.getLanguageType().getCode() );
    }
    else
    {
      mailingRecipient.setLocale( systemVariableService.getDefaultLanguage().getStringVal() );
    }
    mailingRecipient.addMailingRecipientDataFromMap( dataMap );
    mailing.addMailingRecipient( mailingRecipient );

    if ( mailing.getMailingRecipients() != null && mailing.getMailingRecipients().size() > 0 )
    {
      mailing.setDeliveryDate( new Timestamp( DateUtils.getCurrentDateAsLong() ) );
      mailing.setSender( "Challengepoint Promotion" );
      mailing.setMailingType( MailingType.lookup( MailingType.PROMOTION ) );
      mailing.setGuid( GuidUtils.generateGuid() );
      mailingService.submitMailing( mailing, null );

    }
  }

  public void sendGoalSelectedEmailNotification( PaxGoal paxGoal, List paxPartners )
  {
    GoalQuestPromotion goalQuestPromotion = null;
    Long goalQuestPromotionId = paxGoal.getGoalQuestPromotion().getId();

    AssociationRequestCollection promoAssociationRequestCollection = new AssociationRequestCollection();
    promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.NOTIFICATIONS ) );
    goalQuestPromotion = (GoalQuestPromotion)getPromotionService().getPromotionByIdWithAssociations( goalQuestPromotionId, promoAssociationRequestCollection );

    GoalCalculationResult goalCalculationResult = null;

    GoalPayoutStrategyFactory goalPayoutStrategyFactory = (GoalPayoutStrategyFactory)BeanLocator.getBean( GoalPayoutStrategyFactory.BEAN_NAME );

    /*
     * if(!paxGoal.getParticipant().isOwner())//if participant {
     */
    if ( goalQuestPromotion.getAwardType().getCode().equals( PromotionAwardsType.POINTS ) )
    {
      goalCalculationResult = goalPayoutStrategyFactory.getGoalPayoutStrategy( goalQuestPromotion.getPayoutStructure().getCode() ).processGoal( paxGoal );
    }
    else
    // Merch
    {
      goalCalculationResult = goalPayoutStrategyFactory.getGoalPayoutStrategy().processGoal( paxGoal );
    }
    // }
    /*
     * TODO : Will look into this code later else{ //if manager goalCalculationResult = new
     * GoalCalculationResult(); goalCalculationResult.setGoalLevel( paxGoal.getGoalLevel() );
     * goalCalculationResult.setManager( true ); if(paxGoal.getGoalLevel() != null) {
     * goalCalculationResult.setAmountToAchieve(
     * ((ManagerOverrideGoalLevel)paxGoal.getGoalLevel()).getTeamAchievementPercent() ); } }
     */
    Message message = null;
    if ( goalQuestPromotion.getPromotionNotifications().size() > 0 )
    {
      Iterator<PromotionNotification> notificationsIter = goalQuestPromotion.getPromotionNotifications().iterator();
      while ( notificationsIter.hasNext() )
      {
        PromotionNotification notification = notificationsIter.next();
        if ( notification.isPromotionNotificationType() )
        {
          PromotionNotificationType promotionNotificationType = (PromotionNotificationType)notification;
          long messageId = promotionNotificationType.getNotificationMessageId();

          String notificationTypeCode = promotionNotificationType.getPromotionEmailNotificationType().getCode();

          if ( messageId > 0 && notificationTypeCode.equals( PromotionEmailNotificationType.GOAL_SELECTED ) )
          {
            message = messageService.getMessageById( messageId );
            break;
          }
        }
      }
    }

    MailingRecipient mailingRecipient = getMailingService().buildMailingRecipientForGoalQuestEmail( goalQuestPromotion, paxGoal.getParticipant(), goalCalculationResult, paxPartners, null );
    // Create mailing object
    Mailing mailing = new Mailing();
    mailing.setMessage( message );
    mailing.addMailingRecipient( mailingRecipient );
    mailing.setDeliveryDate( new Timestamp( DateUtils.getCurrentDateAsLong() ) );
    mailing.setSender( "GoalQuest Promotion" );
    mailing.setMailingType( MailingType.lookup( MailingType.PROMOTION ) );
    mailing.setGuid( GuidUtils.generateGuid() );
    mailingService.submitMailing( mailing, null );
  }

  public void sendPartnerGoalSelectedEmailNotification( PaxGoal paxGoal, Participant partner )
  {
    GoalQuestPromotion goalQuestPromotion = null;
    Long goalQuestPromotionId = paxGoal.getGoalQuestPromotion().getId();

    AssociationRequestCollection promoAssociationRequestCollection = new AssociationRequestCollection();
    promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.NOTIFICATIONS ) );
    goalQuestPromotion = (GoalQuestPromotion)getPromotionService().getPromotionByIdWithAssociations( goalQuestPromotionId, promoAssociationRequestCollection );

    GoalCalculationResult goalCalculationResult = null;

    GoalPayoutStrategyFactory goalPayoutStrategyFactory = (GoalPayoutStrategyFactory)BeanLocator.getBean( GoalPayoutStrategyFactory.BEAN_NAME );
    goalCalculationResult = goalPayoutStrategyFactory.getGoalPayoutStrategy( goalQuestPromotion.getPayoutStructure().getCode() ).processGoal( paxGoal );
    Message message = null;
    if ( goalQuestPromotion.getPromotionNotifications().size() > 0 )
    {
      Iterator<PromotionNotification> notificationsIter = goalQuestPromotion.getPromotionNotifications().iterator();
      while ( notificationsIter.hasNext() )
      {
        PromotionNotification notification = notificationsIter.next();
        if ( notification.isPromotionNotificationType() )
        {
          PromotionNotificationType promotionNotificationType = (PromotionNotificationType)notification;
          long messageId = promotionNotificationType.getNotificationMessageId();

          String notificationTypeCode = promotionNotificationType.getPromotionEmailNotificationType().getCode();

          if ( messageId > 0 && notificationTypeCode.equals( PromotionEmailNotificationType.PARTNER_SELECTED ) )
          {
            message = messageService.getMessageById( messageId );
            break;
          }
        }
      }
    }

    Mailing mailing = new Mailing();
    mailing.setMessage( message );

    HashMap dataMap = null;
    dataMap = mailingService.buildMailingRecipientForPartnerGoalQuestEmail( goalQuestPromotion, paxGoal.getParticipant(), goalCalculationResult, partner );
    if ( null != partner && !partner.isWelcomeEmailSent().booleanValue() )
    {
      // BugFix 20237.
      dataMap.put( "welcomeEmailSend", "true" );
      partner.setPassword( buildPassword() );
      updateUser( partner );
      if ( dataMap.get( "password" ) != null )
      {
        dataMap.remove( "password" );
        dataMap.put( "password", partner.getPassword() );
      }
      else
      {
        dataMap.remove( "password" );
        dataMap.put( "password", partner.getPassword() );
      }
    }
    else
    {
      if ( dataMap.get( "password" ) != null )
      {
        dataMap.remove( "password" );
      }
      if ( dataMap.get( "user" ) != null )
      {
        dataMap.remove( "user" );
      }
    }

    MailingRecipient mailingRecipient = new MailingRecipient();
    mailingRecipient.setGuid( GuidUtils.generateGuid() );
    mailingRecipient.setUser( partner );
    if ( partner != null && partner.getLanguageType() != null )
    {
      mailingRecipient.setLocale( partner.getLanguageType().getCode() );
    }
    else
    {
      mailingRecipient.setLocale( systemVariableService.getDefaultLanguage().getStringVal() );
    }
    mailingRecipient.addMailingRecipientDataFromMap( dataMap );
    mailing.addMailingRecipient( mailingRecipient );

    if ( mailing.getMailingRecipients() != null && mailing.getMailingRecipients().size() > 0 )
    {
      mailing.setDeliveryDate( new Timestamp( DateUtils.getCurrentDateAsLong() ) );
      mailing.setSender( "GoalQuest Promotion" );
      mailing.setMailingType( MailingType.lookup( MailingType.PROMOTION ) );
      mailing.setGuid( GuidUtils.generateGuid() );
      mailingService.submitMailing( mailing, null );
    }
  }

  private String buildPassword()
  {
    return userService.generatePassword();
  }

  private void updateUser( Participant participant )
  {
    try
    {
      User user = userService.getUserById( participant.getId() );
      user.setWelcomeEmailSent( Boolean.TRUE );

      userService.saveUser( user );
    }
    catch( Exception e )
    {
      String message = "An exception occurred while updating the user information while sending GoalQuest Goal Selected  Email.  ";
      log.error( message );
    }
  }

  public List getPaxPartners( GoalQuestPromotion gqPromo, PaxGoal paxGoal )
  {
    ParticipantAssociationRequest paxAscReq = new ParticipantAssociationRequest( ParticipantAssociationRequest.PARTICIPANT );
    List partnersList = promotionService.getPartnersByPromotionAndParticipantWithAssociations( gqPromo.getId(), paxGoal.getParticipant().getId(), paxAscReq );
    return partnersList;
  }

  public MessageService getMessageService()
  {
    return messageService;
  }

  public void setMessageService( MessageService messageService )
  {
    this.messageService = messageService;
  }

  public MailingService getMailingService()
  {
    return mailingService;
  }

  public void setMailingService( MailingService mailingService )
  {
    this.mailingService = mailingService;
  }

  public PromotionService getPromotionService()
  {
    return promotionService;
  }

  public void setPromotionService( PromotionService promotionService )
  {
    this.promotionService = promotionService;
  }

  public SystemVariableService getSystemVariableService()
  {
    return systemVariableService;
  }

  public void setSystemVariableService( SystemVariableService systemVariableService )
  {
    this.systemVariableService = systemVariableService;
  }

  public UserService getUserService()
  {
    return userService;
  }

  public void setUserService( UserService userService )
  {
    this.userService = userService;
  }

  public ChallengePointService getChallengePointService()
  {
    return challengePointService;
  }

  public void setChallengePointService( ChallengePointService challengePointService )
  {
    this.challengePointService = challengePointService;
  }

}
