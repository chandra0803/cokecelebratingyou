/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/scheduledrecognition/impl/ScheduledRecognitionServiceImpl.java,v $
 */

package com.biperf.core.service.scheduledrecognition.impl;

import static org.quartz.TriggerBuilder.newTrigger;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.TriggerKey;

import com.biperf.core.dao.promotion.ScheduledRecognitionDAO;
import com.biperf.core.domain.claim.CalculatorResponse;
import com.biperf.core.domain.claim.ClaimElement;
import com.biperf.core.domain.claim.RecognitionClaimSource;
import com.biperf.core.domain.enums.ProcessFrequencyType;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.process.Process;
import com.biperf.core.domain.process.ProcessSchedule;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.ScheduledRecognition;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.process.DelaySendRecognitionProcess;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.process.ProcessService;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.service.scheduledrecognition.ScheduledRecognitionService;
import com.biperf.core.utils.UserManager;

public class ScheduledRecognitionServiceImpl implements ScheduledRecognitionService
{
  private static final Log logger = LogFactory.getLog( ScheduledRecognitionServiceImpl.class );

  private Scheduler scheduler;
  private ScheduledRecognitionDAO scheduledRecognitionDAO;
  private ProcessService processService;
  private PromotionService promotionService;
  private ParticipantService participantService;

  @Override
  public void rescheduleRecognition( long scheduledRecognitionId, Date newTimeToRun ) throws ServiceErrorException
  {
    ScheduledRecognition scheduledRecognition = scheduledRecognitionDAO.getScheduledRecognitionById( new Long( scheduledRecognitionId ) );
    validateScheduledRecognition( scheduledRecognition, newTimeToRun );

    // build the key to find the existing Trigger
    TriggerKey existingTriggerKey = buildTriggerKey( scheduledRecognition );

    try
    {
      Trigger existingTrigger = scheduler.getTrigger( existingTriggerKey );
      if ( null == existingTrigger )
      {
        throw new ServiceErrorException( "Could not find the existing Trigger: " + existingTriggerKey );
      }
      scheduler.rescheduleJob( existingTriggerKey, buildTrigger( existingTrigger, newTimeToRun ) );
    }
    catch( SchedulerException e )
    {
      logger.error( e.getMessage(), e );
      throw new ServiceErrorException( e.getMessage(), e );
    }
  }

  @Override
  public void deleteScheduledRecognition( long scheduledRecognitionId ) throws ServiceErrorException
  {
    ScheduledRecognition scheduledRecognition = scheduledRecognitionDAO.getScheduledRecognitionById( new Long( scheduledRecognitionId ) );
    // build the key to find the existing Trigger
    TriggerKey existingTriggerKey = buildTriggerKey( scheduledRecognition );

    try
    {
      Trigger existingTrigger = scheduler.getTrigger( existingTriggerKey );
      JobKey jobKey = existingTrigger.getJobKey();
      scheduler.deleteJob( jobKey );
    }
    catch( SchedulerException e )
    {
      logger.error( e.getMessage(), e );
      throw new ServiceErrorException( e.getMessage(), e );
    }
  }

  private void validateScheduledRecognition( ScheduledRecognition scheduledRecognition, Date newTimeToRun ) throws ServiceErrorException
  {
    // make sure the object even exists
    if ( scheduledRecognition == null )
    {
      throw new ServiceErrorException( "The selected scheduled recognition is invalid/null" );
    }

    Promotion promotion = scheduledRecognition.getPromotion();
    // verify promotion is still active
    if ( !promotion.isLive() )
    {
      throw new ServiceErrorException( "Promotion [" + promotion.getId() + "] is not live" );
    }

    // verify promotion end date is is after the new time
    if ( promotion.getSubmissionEndDate() != null && promotion.getSubmissionEndDate().before( newTimeToRun ) )
    {
      throw new ServiceErrorException( "Cannot schedule the recognition after the promotion's end date" );
    }

    // verify both participants are active?
    if ( !isUserValid( scheduledRecognition.getSender() ) )
    {
      throw new ServiceErrorException( "The Sender is no longer a valid user/active" );
    }

    if ( !isUserValid( scheduledRecognition.getRecipient() ) )
    {
      throw new ServiceErrorException( "The recipient is no longer a valid user/active" );
    }
  }

  private Trigger buildTrigger( Trigger existingTrigger, Date newTimeToRun )
  {
    SimpleTrigger trigger = (SimpleTrigger)newTrigger().withIdentity( existingTrigger.getKey().getName(), existingTrigger.getKey().getGroup() ).startAt( newTimeToRun ) // new
                                                                                                                                                                        // date
        .forJob( existingTrigger.getJobKey().getName(), existingTrigger.getJobKey().getGroup() ) // identify
                                                                                                 // job
                                                                                                 // with
                                                                                                 // name,group
                                                                                                 // strings
        .build();
    return trigger;
  }

  private TriggerKey buildTriggerKey( ScheduledRecognition scheduledRecognition )
  {
    return TriggerKey.triggerKey( scheduledRecognition.getTriggerName(), scheduledRecognition.getTriggerGroup() );
  }

  private boolean isUserValid( Participant participant )
  {
    // just check for active????
    return participant.isActive();
  }

  @Override
  public ScheduledRecognition createScheduledRecognition( ScheduledRecognition scheduledRecognition, LinkedHashMap inputParameters, Date deliveryDate )
  {

    Long promotionId = (Long)inputParameters.get( "promotionId" );
    Long submitterUserId = (Long)inputParameters.get( "submitterUserId" );
    Long submitterNodeId = (Long)inputParameters.get( "submitterNodeId" );
    Long recipientUserId = (Long)inputParameters.get( "recipientUserId" );
    Long recipientNodeId = (Long)inputParameters.get( "recipientNodeId" );
    Long teamId = (Long)inputParameters.get( "teamId" );
    Long proxyUserId = (Long)inputParameters.get( "proxyUserId" );
    RecognitionClaimSource source = (RecognitionClaimSource)inputParameters.get( "source" );
    String proxyId = "";
    if ( proxyUserId != null )
    {
      proxyId = proxyUserId.toString();
    }
    Long budgetId = (Long)inputParameters.get( "budgetId" );
    String budget = "";
    if ( budgetId != null )
    {
      budget = budgetId.toString();
    }

    String submitterComments = (String)inputParameters.get( "submitterComments" );
    String behavior = (String)inputParameters.get( "behavior" );
    if ( behavior == null )
    {
      behavior = "";
    }
    String card = "";
    Long cardId = (Long)inputParameters.get( "cardId" );
    if ( cardId != null )
    {
      card = cardId.toString();
    }
    String certificate = "";
    Long certificateId = (Long)inputParameters.get( "certificateId" );
    if ( certificateId != null )
    {
      certificate = certificateId.toString();
    }
    String awardQuantity = (String)inputParameters.get( "awardQuantity" );
    if ( awardQuantity == null )
    {
      awardQuantity = "";
    }
    String calculatorScore = (String)inputParameters.get( "calculatorScore" );
    if ( calculatorScore == null )
    {
      calculatorScore = "";
    }
    String level = "";
    Long levelId = (Long)inputParameters.get( "levelId" );
    if ( levelId != null )
    {
      level = levelId.toString();
    }
    String productId = (String)inputParameters.get( "productId" );
    if ( productId == null )
    {
      productId = "";
    }
    String recipientCountryCode = (String)inputParameters.get( "recipientCountryCode" );
    if ( recipientCountryCode == null )
    {
      recipientCountryCode = "";
    }
    String ownCardName = (String)inputParameters.get( "ownCardName" );
    if ( ownCardName == null )
    {
      ownCardName = "";
    }
    Boolean sendCopyToMe = (Boolean)inputParameters.get( "sendCopyToMe" );
    Boolean sendCopyToManager = (Boolean)inputParameters.get( "sendCopyToManager" );
    Boolean copyOthers = (Boolean)inputParameters.get( "copyOthers" );
    String sendCopyToOthers = (String)inputParameters.get( "sendCopyToOthers" );
    Boolean hidePublicRecognition = (Boolean)inputParameters.get( "hidePublicRecognition" );

    Integer claimElementSize = (Integer)inputParameters.get( "claimElementSize" );
    String claimElements = "";
    if ( claimElementSize != null && claimElementSize.intValue() > 0 )
    {
      for ( int i = 1; i <= claimElementSize.intValue(); i++ )
      {
        ClaimElement claimElement = (ClaimElement)inputParameters.get( "claimElement" + i );
        claimElements += claimElement.toProcessString() + "|";
      }
    }

    Integer calculatorResponseSize = (Integer)inputParameters.get( "calculatorResponseSize" );
    String calculatorResponses = "";
    if ( calculatorResponseSize != null && calculatorResponseSize.intValue() > 0 )
    {
      for ( int i = 1; i <= calculatorResponseSize.intValue(); i++ )
      {
        CalculatorResponse calculatorResponse = (CalculatorResponse)inputParameters.get( "calculatorResponse" + i );
        calculatorResponses += calculatorResponse.toProcessString() + "|";
      }
    }

    String anniversaryNumberOfDays = (String)inputParameters.get( "anniversaryNumberOfDays" );
    if ( anniversaryNumberOfDays == null )
    {
      anniversaryNumberOfDays = "";
    }
    String anniversaryNumberOfYears = (String)inputParameters.get( "anniversaryNumberOfYears" );
    if ( anniversaryNumberOfYears == null )
    {
      anniversaryNumberOfYears = "";
    }

    String celebrationManagerMessage = "";
    Long celebrationManagerMessageId = (Long)inputParameters.get( "celebrationManagerMessageId" );
    if ( celebrationManagerMessageId != null )
    {
      celebrationManagerMessage = celebrationManagerMessageId.toString();
    }

    Process process = processService.createOrLoadSystemProcess( DelaySendRecognitionProcess.PROCESS_NAME, DelaySendRecognitionProcess.BEAN_NAME );

    scheduledRecognition = processService.populateScheduleRecognitionTriggerName( scheduledRecognition, process );

    scheduledRecognition.setPromotion( promotionService.getPromotionById( promotionId ) );
    scheduledRecognition.setSender( participantService.getParticipantById( submitterUserId ) );
    scheduledRecognition.setRecipient( participantService.getParticipantById( recipientUserId ) );
    scheduledRecognition.setDeliveryDate( deliveryDate );
    scheduledRecognition.setFired( false );

    scheduledRecognition.setSubmitterNodeId( submitterNodeId.toString() );
    scheduledRecognition.setRecipientNodeId( recipientNodeId.toString() );
    scheduledRecognition.setProxyUserId( proxyId );
    scheduledRecognition.setBudgetId( budget );
    scheduledRecognition.setTeamId( teamId.toString() );

    scheduledRecognition.setSubmitterComments( submitterComments );
    scheduledRecognition.setBehavior( behavior );
    scheduledRecognition.setCardId( card );
    scheduledRecognition.setCertificateId( certificate );

    scheduledRecognition.setAwardQuantity( awardQuantity );
    scheduledRecognition.setCalculatorScore( calculatorScore );
    scheduledRecognition.setLevelId( level );
    scheduledRecognition.setProductId( productId );

    scheduledRecognition.setRecipientCountryCode( recipientCountryCode );

    scheduledRecognition.setCopySender( sendCopyToMe.toString() );
    scheduledRecognition.setCopyManager( sendCopyToManager.toString() );
    scheduledRecognition.setCopyOthers( copyOthers.toString() );
    scheduledRecognition.setSendCopyToOthers( sendCopyToOthers );
    scheduledRecognition.setHidePublicRecognition( hidePublicRecognition );

    scheduledRecognition.setClaimElements( claimElements );
    scheduledRecognition.setCalculatorResponses( calculatorResponses );

    scheduledRecognition.setAnniversaryNumberOfDays( anniversaryNumberOfDays );
    scheduledRecognition.setAnniversaryNumberOfYears( anniversaryNumberOfYears );
    scheduledRecognition.setCelebrationManagerMessageId( celebrationManagerMessage );
    scheduledRecognition.setSource( source );
    scheduledRecognition.setOwnCardName( ownCardName );
    scheduledRecognition = scheduledRecognitionDAO.saveScheduledRecognition( scheduledRecognition );

    return scheduledRecognition;
  }

  @Override
  public void scheduleDelaySendRecognitionProcess( ScheduledRecognition scheduledRecognition, Date deliveryDate, Long timeGap )
  {
    // schedule job
    Process process = processService.createOrLoadSystemProcess( DelaySendRecognitionProcess.PROCESS_NAME, DelaySendRecognitionProcess.BEAN_NAME );

    LinkedHashMap parameterValueMap = new LinkedHashMap();

    ProcessSchedule processSchedule = new ProcessSchedule();
    processSchedule.setStartDate( deliveryDate );
    // processSchedule.setStartDate( new Date() );
    processSchedule.setTimeOfDayMillis( new Long( DateUtils.MILLIS_PER_HOUR ) + timeGap );
    // processSchedule.setTimeOfDayMillis( new Long( 0 ) );

    processSchedule.setProcessFrequencyType( ProcessFrequencyType.lookup( ProcessFrequencyType.ONE_TIME_ONLY ) );

    parameterValueMap.put( "scheduledRecogId", new String[] { scheduledRecognition.getId().toString() } );

    scheduledRecognition = processService.scheduleRecognitionProcess( scheduledRecognition, process, processSchedule, parameterValueMap, UserManager.getUserId() );

    scheduledRecognitionDAO.saveScheduledRecognition( scheduledRecognition );
  }

  public List<ScheduledRecognition> getScheduledRecognitionsByDeliveryDate( Date deliveryDate )
  {
    return scheduledRecognitionDAO.getScheduledRecognitionsByDeliveryDate( deliveryDate );
  }

  public void updateScheduleRecognitionWithClaimId( String triggerName, Long claimid ) throws ServiceErrorException
  {
    ScheduledRecognition scheduledRecognition = scheduledRecognitionDAO.getScheduledRecognitionByTriggerName( triggerName );
    if ( scheduledRecognition != null )
    {
      scheduledRecognition.setClaimId( claimid );
      scheduledRecognition.setFired( true );
      scheduledRecognitionDAO.saveScheduledRecognition( scheduledRecognition );
    }
  }

  public ScheduledRecognition getScheduledRecognitionById( Long id )
  {
    return this.scheduledRecognitionDAO.getScheduledRecognitionById( id );
  }

  public List<Long> getScheduledRecognitionIdsForRetryProcess()
  {
    List<Long> scheduleRecognitionIds = null;
    scheduleRecognitionIds = scheduledRecognitionDAO.getScheduledRecognitionIdsForRetryProcess();
    return scheduleRecognitionIds;
  }

  public Scheduler getScheduler()
  {
    return scheduler;
  }

  public void setScheduler( Scheduler scheduler )
  {
    this.scheduler = scheduler;
  }

  public void setScheduledRecognitionDAO( ScheduledRecognitionDAO scheduledRecognitionDAO )
  {
    this.scheduledRecognitionDAO = scheduledRecognitionDAO;
  }

  public void setProcessService( ProcessService processService )
  {
    this.processService = processService;
  }

  public void setPromotionService( PromotionService promotionService )
  {
    this.promotionService = promotionService;
  }

  public void setParticipantService( ParticipantService participantService )
  {
    this.participantService = participantService;
  }

}
