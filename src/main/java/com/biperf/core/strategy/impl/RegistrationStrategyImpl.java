
package com.biperf.core.strategy.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.domain.participant.Audience;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.GoalQuestPromotion;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.RegistrationCode;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.hierarchy.NodeService;
import com.biperf.core.service.participant.AudienceService;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.service.util.ServiceError;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.strategy.BaseStrategy;
import com.biperf.core.strategy.RegistrationStrategy;
import com.biperf.core.utils.DateUtils;

public class RegistrationStrategyImpl extends BaseStrategy implements RegistrationStrategy
{
  private static final Log log = LogFactory.getLog( RegistrationStrategyImpl.class );

  AudienceService audienceService;
  NodeService nodeService;
  PromotionService promotionService;
  ParticipantService participantService;

  /**
   * This method will return a map of promotion id, audience id and node id for the specified registration code.
   * @param code
   * @return Map
   */
  public Map getRegistrationInfoByRegistrationCode( String code )
  {
    HashMap registrationMap = new HashMap();

    RegistrationCode registrationCode = convertRegistrationCode( code );

    if ( registrationCode != null )
    {
      // promotion id
      List promotionList = promotionService.getPromotionByEnrollProgramCode( registrationCode.getEnrollProgramCode() );
      if ( promotionList.size() == 1 )
      {
        Promotion promotion = (Promotion)promotionList.iterator().next();
        if ( promotion.isLive() )
        {
          registrationMap.put( "promotionId", promotion.getId() );
        }
      }

      // audience id
      Audience audience = audienceService.getAudienceByName( registrationCode.getEnrollProgramCode() );
      if ( audience != null )
      {
        registrationMap.put( "audienceId", audience.getId() );
      }

      // node id
      Node node = nodeService.getNodeByRegistrationLocCode( registrationCode.getLocationCode() );
      if ( node != null )
      {
        registrationMap.put( "nodeId", node.getId() );
      }
    }
    return registrationMap;
  }

  /**
   * This method will check the given registration code to see if it fits the validation rules. If not, it
   * will return a List of validationErrors.
   * 
   * Overridden from @see com.biperf.core.strategy.RegistrationStrategy#getRegistrationCodeValidationErrors(java.lang.String)
   * 
   * @param regCode
   * @return the list of errors, or an empty list if no errors
   */
  public List getRegistrationCodeValidationErrors( String regCode )
  {
    List results = new ArrayList();

    // Validate format is correct.
    RegistrationCode registrationCode = convertRegistrationCode( regCode );

    if ( registrationCode == null )
    {
      log.error( "Registration Code validation error: Code=null" );
      results.add( new ServiceError( ServiceErrorMessageKeys.REGISTRATION_CODE_INVALID_FORMAT ) );
    }
    else
    {
      boolean invalidErrorFound = false;

      // Validate audience exists.
      Audience audience = audienceService.getAudienceByName( registrationCode.getEnrollProgramCode() );
      if ( audience != null )
      {
        // Validate promotion is live and goal selection period has not ended.
        List promotionList = promotionService.getPromotionByEnrollProgramCode( registrationCode.getEnrollProgramCode() );
        if ( promotionList.size() != 1 )
        {
          log.error( "Registration Code validation error: Program code does not match an unique promotion. " + registrationCode.getEnrollProgramCode() );
          invalidErrorFound = true;
        }
        else
        {
          Promotion promotion = (Promotion)promotionList.iterator().next();
          if ( !promotion.isLive() )
          {
            log.error( "Registration Code validation error: Program code does not match a live promotion. " + registrationCode.getEnrollProgramCode() );
            invalidErrorFound = true;
          }
          if ( promotion.isGoalQuestPromotion() )
          {
            Date now = new Date();
            GoalQuestPromotion goalQuestPromotion = (GoalQuestPromotion)promotion;
            if ( goalQuestPromotion.getGoalCollectionEndDate() != null && DateUtils.toEndDate( goalQuestPromotion.getGoalCollectionEndDate() ).before( now ) )
            {
              log.error( "Registration Code validation error: Program code is for a Goalquest promotion that is past the goal selection period. " + registrationCode.getEnrollProgramCode() );
              invalidErrorFound = true;
            }
          }
        }
      }
      else
      {
        log.error( "Registration Code validation error: Program code does not match a promotion audience. " + registrationCode.getEnrollProgramCode() );
        invalidErrorFound = true;
      }

      // Validate node exists.
      Node node = nodeService.getNodeByRegistrationLocCode( registrationCode.getLocationCode() );
      if ( node == null )
      {
        log.error( "Registration Code validation error: Location code does not match an active node. " + registrationCode.getLocationCode() );
        invalidErrorFound = true;
      }

      // Only want the error message once.
      if ( invalidErrorFound )
      {
        results.add( new ServiceError( ServiceErrorMessageKeys.REGISTRATION_CODE_INVALID_ERROR ) );
      }
    }

    return results;
  }

  /**
   * Returns a registration code, which, for this implementation, is consist of: [enrollProgramCode]-[locationCode]
   * 
   * enrollProgramCode: is the code entered on the audience page of the goalquest promotion wizard
   * locationCode: is the location code assigned to a specific node, generated when the view registration code button is pressed on the promotion overiview page
   * 
   * @param code
   * @return registrationCode
   */
  private RegistrationCode convertRegistrationCode( String code )
  {
    RegistrationCode registrationCode = null;

    if ( !StringUtils.isBlank( code.trim() ) && code.trim().indexOf( RegistrationCode.DELIMITER ) != -1 )
    {
      registrationCode = new RegistrationCode();
      registrationCode.setEnrollProgramCode( code.substring( 0, code.indexOf( RegistrationCode.DELIMITER ) ) );
      registrationCode.setLocationCode( code.substring( code.indexOf( RegistrationCode.DELIMITER ) + RegistrationCode.DELIMITER.length(), code.length() ) );
    }
    return registrationCode;
  }

  /**
   * This method will insert the participant and its relevant information to the database.
   * 
   * @param participant
   * @param promotionId
   * @param audienceId
   * @return the list of errors, or an empty list if no errors
   */
  public List enrollParticipant( Participant participant )
  {

    List errors = new ArrayList();
    Participant paxWithSameSSN = null;
    try
    {
      // Duplicate Login ID (username)?
      Participant paxWithSameUserName = participantService.getParticipantByUserName( participant.getUserName() );
      if ( paxWithSameUserName != null && paxWithSameUserName.isActive().booleanValue() )
      {
        errors.add( new ServiceError( ServiceErrorMessageKeys.PARTICIPANT_DUPLICATE_USER_NAME, participant.getUserName() ) );
      }

      // Duplicate SSN?
      // BugFix 20898,return null Pax if the ssn is Blank.
      if ( !com.biperf.util.StringUtils.isEmpty( participant.getSsn() ) )
      {
        paxWithSameSSN = participantService.getParticipantBySSN( participant.getSsn() );
      }
      if ( paxWithSameSSN != null && paxWithSameSSN.isActive().booleanValue() )
      {
        errors.add( new ServiceError( ServiceErrorMessageKeys.PARTICIPANT_DUPLICATE_SSN, participant.getUserName() ) );
      }

      if ( errors.isEmpty() )
      {
        // Persists Participant.
        participantService.createFullParticipant( participant );
      }
    }
    catch( ServiceErrorException e )
    {
      errors.add( e.getServiceErrors() );
    }

    return errors;
  }

  public void setAudienceService( AudienceService audienceService )
  {
    this.audienceService = audienceService;
  }

  public void setNodeService( NodeService nodeService )
  {
    this.nodeService = nodeService;
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
