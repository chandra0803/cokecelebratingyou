
package com.biperf.core.service.underarmour.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.biperf.core.dao.promotion.hibernate.GoalQuestPromotionQueryConstraint;
import com.biperf.core.domain.enums.GoalQuestPaxActivityStatus;
import com.biperf.core.domain.enums.GoalQuestPaxActivityType;
import com.biperf.core.domain.enums.PromotionStatusType;
import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.domain.goalquest.GoalQuestParticipantActivity;
import com.biperf.core.domain.goalquest.PaxGoal;
import com.biperf.core.domain.participant.AudienceParticipant;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.GoalQuestPromotion;
import com.biperf.core.domain.system.PropertySetItem;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.goalquest.GoalQuestPaxActivityService;
import com.biperf.core.service.goalquest.PaxGoalService;
import com.biperf.core.service.goalquest.impl.PaxGoalAssociationRequest;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.service.underarmour.UARepositoryFactory;
import com.biperf.core.service.underarmour.UnderArmourService;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.crypto.SHA256Hash;
import com.biperf.core.value.FormattedValueBean;
import com.biperf.core.value.underArmour.v1.BaseRestResponseObject;
import com.biperf.core.value.underArmour.v1.actigraphy.ActigraphyRequest;
import com.biperf.core.value.underArmour.v1.actigraphy.response.Actigraphy;
import com.biperf.core.value.underArmour.v1.actigraphy.response.ActigraphyResponse;
import com.biperf.core.value.underArmour.v1.actigraphy.response.Aggregate;
import com.biperf.core.value.underArmour.v1.participant.ParticipantRequest;
import com.biperf.core.value.underArmour.v1.participant.PaxAuthStatusResponse;

@Service( "underArmourService" )
public class UnderArmourServiceImpl implements UnderArmourService
{
  private static final Log log = LogFactory.getLog( UnderArmourServiceImpl.class );
  @Autowired
  private SystemVariableService systemVariableService;
  @Autowired
  private PromotionService promotionService;
  @Autowired
  private GoalQuestPaxActivityService goalQuestPaxActivityService;
  @Autowired
  private ParticipantService participantService;
  @Autowired
  private PaxGoalService paxGoalService;

  @Autowired
  private UARepositoryFactory uaRepo;

  @Override
  public boolean authorizeParticipant( long clientPaxId, String authCode ) throws ServiceErrorException
  {
    try
    {

      String appCode = getPropertyStringVal( SystemVariableService.UNDERARMOUR_CODE );
      if ( appCode != null )
      {
        log.debug( "appCode: " + appCode );
        Date authorizedDate = null;
        ParticipantRequest participantRequest = new ParticipantRequest();
        participantRequest.setAppCode( getPropertyStringVal( SystemVariableService.UNDERARMOUR_CODE ) );
        participantRequest.setClientPaxId( clientPaxId );
        participantRequest.setAuthCode( authCode );
        participantRequest.setSecurityToken( getSecurityToken( participantRequest.getHashableAttribute() ) );
        Participant participant = getParticipant( clientPaxId );
        if ( participant.getUaAuthorizedDate() == null )
        {
          authorizedDate = promotionService.getUAGoalQuestPromotionStartDate( clientPaxId );
          participantRequest.setPaxAuthorizedDate( DateUtils.getDateStringyyyy_MM_dd( authorizedDate ) );
        }
        BaseRestResponseObject result = uaRepo.getRepo().authorizeParticipant( participantRequest );
        log.debug( "return code : " + result.getReturnCode() + " retuen message : " + result.getReturnMessage() );
        if ( result != null && result.getReturnCode() == BaseRestResponseObject.SUCCESS )
        {
          if ( participant.getUaAuthorizedDate() == null )
          {
            participant.setUaAuthorizedDate( authorizedDate );
            participantService.updatePaxAuthorizeDate( participant );
          }
          return true;
        }
      }
    }
    catch( Exception e )
    {
      log.error( "Failed to authorizeParticipant [" + clientPaxId + "]", e );
      throw new ServiceErrorException( e.toString(), e );
    }
    return false;
  }

  @Override
  public boolean deAuthorizeParticipant( long clientPaxId ) throws ServiceErrorException
  {
    try
    {
      ParticipantRequest participantRequest = new ParticipantRequest();
      participantRequest.setAppCode( getPropertyStringVal( SystemVariableService.UNDERARMOUR_CODE ) );
      participantRequest.setClientPaxId( clientPaxId );
      participantRequest.setSecurityToken( getSecurityToken( participantRequest.getHashableAttribute() ) );
      BaseRestResponseObject result = uaRepo.getRepo().deAuthorizeParticipant( participantRequest );
      if ( result != null && result.getReturnCode() == BaseRestResponseObject.SUCCESS )
      {
        return true;
      }
      else
      {
        log.error( result.getReturnMessage() );
        throw new ServiceErrorException( result.getReturnMessage() );
      }
    }
    catch( ServiceErrorException e )
    {
      throw e;
    }
    catch( Exception e )
    {
      log.error( "Failed to deAuthorizeParticipant [" + clientPaxId + "]", e );
      throw new ServiceErrorException( e.toString(), e );
    }
  }

  @Override
  public boolean isParticipantAuthorized( long clientPaxId ) throws ServiceErrorException
  {
    try
    {
      if ( uaEnabled() )
      {
        ParticipantRequest participantRequest = new ParticipantRequest();
        participantRequest.setAppCode( getPropertyStringVal( SystemVariableService.UNDERARMOUR_CODE ) );
        participantRequest.setClientPaxId( clientPaxId );
        participantRequest.setSecurityToken( getSecurityToken( participantRequest.getHashableAttribute() ) );
        PaxAuthStatusResponse result = uaRepo.getRepo().isAuthorizeParticipant( participantRequest );
        if ( result != null )
        {
          return result.isAuthorized();
        }
      }
    }
    catch( Exception e )
    {
      log.error( "Failed to isParticipantAuthorized [" + clientPaxId + "]", e );
      throw new ServiceErrorException( e.toString(), e );
    }
    return false;
  }

  @SuppressWarnings( { "rawtypes", "unchecked" } )
  @Override
  public GetActigraphyResult getActigraphyData()
  {
    GetActigraphyResult getActigraphyResult = new GetActigraphyResult();

    try
    {
      List promotionList = getUnderArmourGQPromotions();
      for ( Iterator iter = promotionList.iterator(); iter.hasNext(); )
      {
        GoalQuestPromotion goalQuestPromotion = (GoalQuestPromotion)iter.next();
        Set allUsers = new HashSet();
        List<Participant> filteredUsers = new ArrayList<>();
        allUsers.addAll( participantService.getAllEligibleActivePaxForPromotion( goalQuestPromotion.getId(), true ) );
        for ( Iterator iterUser = allUsers.iterator(); iterUser.hasNext(); )
        {
          Participant pax = getParticipant( iterUser.next() );

          if ( pax != null && pax.isActive().booleanValue() && pax.getUaAuthorizedDate() != null )
          {
            filteredUsers.add( pax );
          }

        }
        for ( Participant pax : filteredUsers )
        {
          PaxGoal paxGoal = isGoalAssigned( goalQuestPromotion.getId(), pax.getId() );
          if ( uaEnabled() && paxGoal != null )
          {
            // derive from date: if user data is fetched earlier, make from date as next day to
            // requested past days last_poll_date should not be before goalquest promotion start
            // date
            Date gqPromoSubmissionStartDate = goalQuestPromotion.getSubmissionStartDate();
            int pastDue = systemVariableService.getPropertyByName( SystemVariableService.UA_PAST_DAYS ).getIntVal();
            Date lastPollDate = paxGoal.getSubmissionDate() != null ? paxGoal.getSubmissionDate() : gqPromoSubmissionStartDate;

            String pastFromDate = paxGoal.getSubmissionDate() != null
                ? DateUtils.getDateStringyyyy_MM_dd( DateUtils.getNextDay( DateUtils.getPastDueDate( lastPollDate, pastDue ) ) )
                : DateUtils.getDateStringyyyy_MM_dd( lastPollDate );

            String fromDate = pastFromDate != null && DateUtils.getDateFromStringyyyy_MM_dd( pastFromDate ).before( gqPromoSubmissionStartDate )
                ? DateUtils.getDateStringyyyy_MM_dd( gqPromoSubmissionStartDate )
                : pastFromDate;
            String toDate = DateUtils.getPreviousDayStringyyyy_MM_dd();
            // if from date is not valid or if from date is after to date, skip the fetch data
            if ( fromDate == null || ( !org.apache.commons.lang.time.DateUtils.isSameDay( DateUtils.toDateFromyyyyMMdd( toDate ), DateUtils.toDateFromyyyyMMdd( fromDate ) )
                && DateUtils.toDateFromyyyyMMdd( toDate ).before( DateUtils.toDateFromyyyyMMdd( fromDate ) ) ) )
            {
              log.debug( "processing for participant " + pax.getId() + " skipped. fromDate : " + fromDate + " , toDate " + toDate );
              continue;
            }
            ActigraphyRequest actigraphyRequest = new ActigraphyRequest();
            actigraphyRequest.setAppCode( getPropertyStringVal( SystemVariableService.UNDERARMOUR_CODE ) );
            actigraphyRequest.setClientPaxId( pax.getId() );
            actigraphyRequest.setFromDate( fromDate );
            actigraphyRequest.setToDate( toDate );
            actigraphyRequest.setSecurityToken( getSecurityToken( actigraphyRequest.getHashableAttribute() ) );
            ActigraphyResponse result = uaRepo.getRepo().getActigraphy( actigraphyRequest );
            if ( result.getReturnCode() == BaseRestResponseObject.SUCCESS && result.getReturnMessage().equals( BaseRestResponseObject.SUCCESS_MESSAGE ) )
            {
              for ( Actigraphy actigraphy : result.getActigraphys() )
              {
                GoalQuestParticipantActivity goalQuestParticipantActivityExist = checkifActigraphyExists( goalQuestPromotion, actigraphy, pax );
                // Set<Aggregate> should contains always single aggregate object. no need to use
                // loop
                Aggregate aggregate = actigraphy.getAggregates().iterator().next();
                if ( goalQuestParticipantActivityExist == null )
                {
                  Double step = aggregate.getSteps();
                  GoalQuestParticipantActivity goalQuestParticipantActivity = new GoalQuestParticipantActivity();
                  goalQuestParticipantActivity.setType( GoalQuestPaxActivityType.lookup( GoalQuestPaxActivityType.INCREMENTAL ) );
                  goalQuestParticipantActivity.setAutomotive( false );
                  goalQuestParticipantActivity.setStatus( GoalQuestPaxActivityStatus.lookup( GoalQuestPaxActivityStatus.PENDING ) );
                  goalQuestParticipantActivity.setGoalQuestPromotion( goalQuestPromotion );
                  goalQuestParticipantActivity.setParticipant( pax );
                  goalQuestParticipantActivity.setSubmissionDate( DateUtils.toDateFromyyyyMMdd( actigraphy.getDate() ) );
                  goalQuestParticipantActivity.setQuantity( new BigDecimal( step ) );
                  goalQuestPaxActivityService.saveGoalQuestPaxActivity( goalQuestParticipantActivity );
                }
                else
                {
                  if ( new BigDecimal( aggregate.getSteps() ).equals( goalQuestParticipantActivityExist.getQuantity() ) )
                  {
                    continue;
                  }
                  else
                  {
                    goalQuestPaxActivityService.updateGoalQuestPaxActivity( goalQuestParticipantActivityExist, aggregate.getSteps() );
                  }
                }
              }
              // Save the ActigraphyLastCollection date. List of Actigraphys was already sorted in
              // UA micro service.
              if ( CollectionUtils.isNotEmpty( result.getActigraphys() ) )
              {
                Actigraphy actigraphyLastCollection = (Actigraphy)result.getActigraphys().get( result.getActigraphys().size() - 1 );
                if ( log.isDebugEnabled() )
                {
                  log.debug( "Promotion id : " + goalQuestPromotion.getId() + "Participant id : " + pax.getId() + " Actigraphy Last Collection Date from UA microservice : "
                      + actigraphyLastCollection.getDate() );
                }
                paxGoal.setSubmissionDate( DateUtils.getDateFromStringyyyy_MM_dd( actigraphyLastCollection.getDate() ) );
                paxGoalService.savePaxGoal( paxGoal );
                getActigraphyResult.setNumberParticipantsUpdated( getActigraphyResult.getNumberParticipantsUpdated() + 1 );
              }
            }
          }
        }
      }
    }
    catch( Exception e )
    {
      log.error( "Error occurred while saving GoalQuest Pax Activity", e );
    }

    return getActigraphyResult;
  }

  @SuppressWarnings( { "rawtypes", "unchecked" } )
  @Override
  public boolean isPaxEligibleForUAPromotion( long clientPaxId )
  {
    List promotionList = getUnderArmourGQPromotions();
    if ( !promotionList.isEmpty() )
    {
      for ( Iterator iter = promotionList.iterator(); iter.hasNext(); )
      {
        GoalQuestPromotion goalQuestPromotion = (GoalQuestPromotion)iter.next();
        Set participantList = new HashSet();
        participantList.addAll( participantService.getAllEligibleActivePaxForPromotion( goalQuestPromotion.getId(), true ) );
        Participant participant = getParticipant( clientPaxId );
        if ( participantList.contains( participant ) )
        {
          if ( DateUtils.getCurrentDateTrimmed().compareTo( goalQuestPromotion.getGoalCollectionEndDate() ) <= 0 )
          {
            return true;
          }
        }
      }
    }
    return false;
  }

  @SuppressWarnings( "unchecked" )
  private PaxGoal isGoalAssigned( Long promotionId, Long paxId )
  {
    AssociationRequestCollection arCollection = new AssociationRequestCollection();
    arCollection.add( new PaxGoalAssociationRequest( PaxGoalAssociationRequest.GOAL_LEVEL ) );
    PaxGoal paxGoal = paxGoalService.getPaxGoalByPromotionIdAndUserId( promotionId, paxId, arCollection );

    return paxGoal;
  }

  @SuppressWarnings( "rawtypes" )
  private List getUnderArmourGQPromotions()
  {
    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    GoalQuestPromotionQueryConstraint queryConstraint = new GoalQuestPromotionQueryConstraint();
    queryConstraint.setPromotionStatusTypesIncluded( new PromotionStatusType[] { PromotionStatusType.lookup( PromotionStatusType.LIVE ), PromotionStatusType.lookup( PromotionStatusType.EXPIRED ) } );
    queryConstraint.setPromotionTypesIncluded( new PromotionType[] { PromotionType.lookup( PromotionType.GOALQUEST ) } );
    queryConstraint.setHasAllowUnderArmour( Boolean.TRUE );
    queryConstraint.setHasIssueAwardsRun( Boolean.FALSE );
    return this.promotionService.getPromotionListWithAssociations( queryConstraint, associationRequestCollection );
  }

  private Participant getParticipant( Object paxTypeUnknown )
  {
    Participant participant = null;
    // paxs might be AudienceParticipant objects or Participants or FormattedValueBeans
    if ( paxTypeUnknown instanceof Participant )
    {
      participant = (Participant)paxTypeUnknown;
    }
    else if ( paxTypeUnknown instanceof AudienceParticipant )
    {
      participant = ( (AudienceParticipant)paxTypeUnknown ).getParticipant();
    }
    else if ( paxTypeUnknown instanceof FormattedValueBean )
    {
      participant = participantService.getParticipantById( ( (FormattedValueBean)paxTypeUnknown ).getId() );
    }

    return participant;
  }

  private String getSecurityToken( String hashableValue )
  {
    String encrytionSalt = getPropertyStringVal( SystemVariableService.UNDERARMOUR_ENCRYPTION_SALT );
    StringBuilder hash = new StringBuilder();
    hash.append( hashableValue );
    hash.append( encrytionSalt );
    String encrypted = new SHA256Hash().encrypt( hash.toString() );
    return encrypted;
  }

  private String getPropertyStringVal( String byName )
  {
    PropertySetItem propertySetItem = getPropertySetItem( byName );
    return propertySetItem != null ? propertySetItem.getStringVal() : null;
  }

  private boolean getPropertyBooleanVal( String byName )
  {
    PropertySetItem propertySetItem = getPropertySetItem( byName );
    return propertySetItem != null ? propertySetItem.getBooleanVal() : false;
  }

  private PropertySetItem getPropertySetItem( String byName )
  {
    return systemVariableService.getPropertyByName( byName );
  }

  private Participant getParticipant( Long paxId )
  {
    return participantService.getParticipantById( paxId );
  }

  @Override
  public boolean uaEnabled()
  {
    return getPropertyBooleanVal( SystemVariableService.UNDERARMOUR_MICROSERVICE_ENABLED );
  }

  public GoalQuestParticipantActivity checkifActigraphyExists( GoalQuestPromotion goalQuestPromotion, Actigraphy actigraphy, Participant participant )
  {
    GoalQuestParticipantActivity goalQuestParticipantActivity = goalQuestPaxActivityService
        .getPaxActivityByPromotionIdAndUserIdAndSubDate( goalQuestPromotion.getId(), participant.getId(), DateUtils.toDateFromyyyyMMdd( actigraphy.getDate() ) );
    if ( goalQuestParticipantActivity != null )
    {
      return goalQuestParticipantActivity;
    }
    return null;

  }
}
