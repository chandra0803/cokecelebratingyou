
package com.biperf.core.service.plateauawards.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.biperf.core.dao.merchandise.MerchOrderDAO;
import com.biperf.core.domain.goalquest.PaxGoal;
import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.domain.merchandise.MerchOrder;
import com.biperf.core.domain.message.Message;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.process.Process;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.RecognitionPromotion;
import com.biperf.core.domain.user.User;
import com.biperf.core.process.PlateauAwardsReminderSubmitProcess;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.goalquest.PaxGoalService;
import com.biperf.core.service.message.MessageService;
import com.biperf.core.service.participant.ParticipantAssociationRequest;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.plateauawards.PlateauAwardReminderBean;
import com.biperf.core.service.plateauawards.PlateauAwardsService;
import com.biperf.core.service.plateauawards.PreviewMessage;
import com.biperf.core.service.process.ProcessService;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.utils.NewServiceAnniversaryUtil;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.MerchAwardReminderBean;
import com.objectpartners.cms.util.ContentReaderManager;

public class PlateauAwardsServiceImpl implements PlateauAwardsService
{
  private ParticipantService participantService;
  private SystemVariableService systemVariableService;
  private MessageService messageService;
  private MerchOrderDAO merchOrderDao;
  private ProcessService processService;
  private PromotionService promotionService;
  private PaxGoalService paxGoalService;

  public List<PlateauAwardReminderBean> findPlateauAwardRemindersFor( Long participantId )
  {
    List<MerchAwardReminderBean> merchAwardReminderBeans = this.getUnclaimedMerchOrdersForPaxUnderManager( participantId );

    // create objects from the reminder beans
    List<PlateauAwardReminderBean> plateauAwardReminderBeans = new ArrayList<PlateauAwardReminderBean>( merchAwardReminderBeans.size() );
    AssociationRequestCollection arc = new AssociationRequestCollection();
    ParticipantAssociationRequest par = new ParticipantAssociationRequest( ParticipantAssociationRequest.EMAILS );
    arc.add( par );
    for ( MerchAwardReminderBean marb : merchAwardReminderBeans )
    {
      PlateauAwardReminderBean parb = new PlateauAwardReminderBean();
      if ( !marb.getParticipantId().equals( participantId ) )
      {
        Promotion promotion = promotionService.getPromotionById( marb.getPromotionId() );
        Participant pax = participantService.getParticipantByIdWithAssociations( marb.getParticipantId(), arc );
        parb.setMerchOrderId( marb.getMerchOrderId() );
        parb.setParticipant( pax );
        parb.setPromotionName( marb.getPromotionName() );
        if ( promotion.isGoalQuestOrChallengePointPromotion() )
        {
          PaxGoal paxGoal = paxGoalService.getPaxGoalByPromotionIdAndUserId( promotion.getId(), pax.getId() );
          parb.setAwardLevel( paxGoal.getGoalLevel().getGoalLevelName() );
        }
        else
        {
          parb.setAwardLevel( marb.getDisplayLevelName() );
        }
        parb.setDateIssued( marb.getSubmittedDate() );
        parb.setDateReminded( marb.getLastRemindedDate() );

        plateauAwardReminderBeans.add( parb );
      }
    }

    return plateauAwardReminderBeans;
  }

  public void sendPlateauAwardReminders( List<Long> merchOrderIds, Long managerId, String comments )
  {
    // update the last reminded date on the merchOrder objects separately from sending
    // the reminder emails, because sending a large number of emails takes a long time
    // and may cause transaction timeouts when trying to update these objects
    Date dateReminded = new Date();
    for ( Long merchOrderId : merchOrderIds )
    {
      MerchOrder merchOrder = merchOrderDao.getMerchOrderById( merchOrderId );
      merchOrder.setDateLastReminded( dateReminded );
    }

    // send the reminder emails via a separate process
    LinkedHashMap<String, Object> parameterValueMap = new LinkedHashMap<String, Object>();
    parameterValueMap.put( "merchOrderIds", merchOrderIds );
    parameterValueMap.put( "managerId", managerId );
    parameterValueMap.put( "comments", comments );

    Process process = processService.createOrLoadSystemProcess( PlateauAwardsReminderSubmitProcess.PROCESS_NAME, PlateauAwardsReminderSubmitProcess.BEAN_NAME );
    processService.launchProcess( process, parameterValueMap, UserManager.getUserId() );
  }

  public PreviewMessage getPreviewMessage( Long participantId )
  {
    Message message = messageService.getMessageByCMAssetCode( MessageService.PLATEAU_AWARDS_REMINDER_MESSAGE_CM_ASSET_CODE );

    String subject = message.getI18nSubject();
    String promotionNameReplacement = ContentReaderManager.getText( "manager.plateauawardsreminder", "MESSAGE_PREVIEW_PROMO_NAME" );
    subject = subject.replaceAll( Pattern.quote( "${promotionName}" ), Matcher.quoteReplacement( promotionNameReplacement ) );

    String messageText = message.getI18nHtmlBody();
    String awardLevelReplacement = ContentReaderManager.getText( "manager.plateauawardsreminder", "MESSAGE_PREVIEW_AWARD_LEVEL" );
    String recipientNameReplacement = ContentReaderManager.getText( "manager.plateauawardsreminder", "MESSAGE_PREVIEW_RECIPIENT_NAME" );
    messageText = messageText.replaceAll( Pattern.quote( "${firstName} ${lastName}" ), recipientNameReplacement );
    messageText = messageText.replaceAll( Pattern.quote( "${promotionName}" ), Matcher.quoteReplacement( promotionNameReplacement ) );
    messageText = messageText.replaceAll( Pattern.quote( "${awardLevel}" ), awardLevelReplacement );

    messageText = messageText.replaceAll( Pattern.quote( "${siteUrl}" ), "#" );

    Participant manager = participantService.getParticipantById( participantId );
    messageText = messageText.replaceAll( Pattern.quote( "${managerFirstName}" ), manager.getFirstName() );
    messageText = messageText.replaceAll( Pattern.quote( "${managerLastName}" ), manager.getLastName() );

    String commentsReplacement = ContentReaderManager.getText( "manager.plateauawardsreminder", "MESSAGE_PREVIEW_COMMENTS" );
    messageText = messageText.replaceAll( Pattern.quote( "${managerComments}" ), commentsReplacement );

    String clientName = systemVariableService.getPropertyByName( SystemVariableService.CLIENT_NAME ).getStringVal();
    messageText = messageText.replaceAll( Pattern.quote( "${clientName}" ), clientName );

    return new PreviewMessage( subject, messageText );
  }

  public int getNumberOfPaxWithUnclaimedAwardsFor( Long managerId )
  {
    // get the unclaimed merch awards for pax under this manager
    List<MerchAwardReminderBean> merchAwards = getUnclaimedMerchOrdersForPaxUnderManager( managerId );

    // we just want the unique participants
    Set<Long> paxIds = new HashSet<Long>();

    for ( MerchAwardReminderBean bean : merchAwards )
    {
      if ( !bean.getParticipantId().equals( managerId ) )
      {
        paxIds.add( bean.getParticipantId() );
      }
    }

    return paxIds.size();
  }

  List<MerchAwardReminderBean> getUnclaimedMerchOrdersForPaxUnderManager( Long managerId )
  {
    // get the current user
    User user = getUser( managerId );

    // get the nodes for which the user is an owner or manager
    List<Node> ownerManagerNodes = user.getActiveManagerNodes();

    // get the IDs of those nodes
    List<Long> nodeIds = new ArrayList<Long>();
    for ( Node node : ownerManagerNodes )
    {
      nodeIds.add( node.getId() );
    }

    List<MerchAwardReminderBean> merchAwardReminderBeans = new ArrayList<MerchAwardReminderBean>();

    if ( nodeIds.isEmpty() )
    {
      return merchAwardReminderBeans;
    }

    // calculate the date at which the awards are considered "past due."
    // look for the awards that were submitted on or before the current date
    // minus the number of days set as the system variable.
    int reminderDays = systemVariableService.getPropertyByName( SystemVariableService.PLATEAU_AWARDS_REMINDER_DAYS ).getIntVal();
    Date pastDueDate = org.apache.commons.lang3.time.DateUtils.addDays( new Date(), reminderDays * -1 );

    // use the DAO to get the plateau merch orders in need of reminding

    merchAwardReminderBeans = merchOrderDao.getPlateauAwardRemindersForNodes( nodeIds, pastDueDate );

    // Remove PURL Promotion for New SA
    if ( NewServiceAnniversaryUtil.isNewServiceAnniversaryEnabled() )
    {
      removePurlEnabledPromotion( merchAwardReminderBeans );
    }

    return merchAwardReminderBeans;
  }

  private void removePurlEnabledPromotion( List<MerchAwardReminderBean> promoAwardReminderList )
  {
    if ( promoAwardReminderList != null && promoAwardReminderList.size() > 0 )
    {
      Iterator<MerchAwardReminderBean> promoAwardReminderListIter = promoAwardReminderList.iterator();
      while ( promoAwardReminderListIter.hasNext() )
      {
        MerchAwardReminderBean merchAwardReminder = (MerchAwardReminderBean)promoAwardReminderListIter.next();
        Promotion promotion = promotionService.getPromotionById( merchAwardReminder.getPromotionId() );
        if ( promotion.isRecognitionPromotion() )
        {
          RecognitionPromotion recognitionPromotion = (RecognitionPromotion)promotion;
          if ( recognitionPromotion.isIncludePurl() || recognitionPromotion.isIncludeCelebrations() )
          {
            promoAwardReminderListIter.remove();
          }
        }
      }
    }
  }

  private User getUser( Long userId )
  {
    return participantService.getParticipantById( userId );
  }

  public void setParticipantService( ParticipantService participantService )
  {
    this.participantService = participantService;
  }

  public void setSystemVariableService( SystemVariableService systemVariableService )
  {
    this.systemVariableService = systemVariableService;
  }

  public void setMessageService( MessageService messageService )
  {
    this.messageService = messageService;
  }

  public void setMerchOrderDao( MerchOrderDAO merchOrderDao )
  {
    this.merchOrderDao = merchOrderDao;
  }

  public void setProcessService( ProcessService processService )
  {
    this.processService = processService;
  }

  public void setPromotionService( PromotionService promotionService )
  {
    this.promotionService = promotionService;
  }

  public void setPaxGoalService( PaxGoalService paxGoalService )
  {
    this.paxGoalService = paxGoalService;
  }
}
