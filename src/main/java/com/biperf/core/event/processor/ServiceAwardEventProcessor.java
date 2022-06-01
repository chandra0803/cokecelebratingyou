
package com.biperf.core.event.processor;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.biperf.core.domain.engageprogram.EngageProgram;
import com.biperf.core.domain.serviceanniversary.SACelebrationInfo;
import com.biperf.core.exception.BadRequestException;
import com.biperf.core.service.claim.ClaimService;
import com.biperf.core.service.engageprogram.EngageProgramService;
import com.biperf.core.service.serviceanniversary.ServiceAnniversaryService;
import com.biperf.core.utils.CommonConstants;
import com.biperf.core.utils.DateUtils;
import com.biw.event.streams.consume.EventProcessor;
import com.biw.event.streams.event.Event;
import com.fasterxml.jackson.databind.JsonNode;

@Component
public class ServiceAwardEventProcessor extends BaseEventProcessor implements EventProcessor
{
  private static Log log = LogFactory.getLog( ServiceAwardEventProcessor.class );

  private @Autowired EngageProgramService engageProgramService;
  private @Autowired ServiceAnniversaryService serviceAnniversaryService;
  private @Autowired ClaimService claimService;

  private static final String[] ACCEPTED_SCHEMAS = new String[] { "service-awards-award" };

  private static final String EVENT_CREATE = "create";
  private static final String EVENT_UPDATE = "update";

  /**
   * Validate and Save Service award events 
   */
  @Override
  protected boolean handleEvent( Event<JsonNode> event ) throws BadRequestException, Exception
  {
    SACelebrationInfo saCelebrationInfo = null;
    EngageProgram engageProgram = null;

    validateServiceAwardEvent( event );

    engageProgram = getProgram( event );

    if ( EVENT_CREATE.equalsIgnoreCase( event.getEvent() ) )
    {
      // Receiving create event for migration data, to avoid duplicate records in
      // sa_celebration_info get the existing record and update the event data
      SACelebrationInfo extSACelebrationInfo = getSACelebration( event, engageProgram );
      saCelebrationInfo = prepareSACelebrationInfo( event, extSACelebrationInfo );
      if ( extSACelebrationInfo != null )
      {
        saCelebrationInfo.getAuditUpdateInfo().setDateModified( new Timestamp( System.currentTimeMillis() ) );
      }
    }
    else if ( EVENT_UPDATE.equalsIgnoreCase( event.getEvent() ) )
    {

      SACelebrationInfo extSACelebrationInfo = getSACelebration( event, engageProgram );
      if ( extSACelebrationInfo != null )
      {
        log.info( "There is no existing service award" );
      }
      saCelebrationInfo = prepareSACelebrationInfo( event, extSACelebrationInfo );
      saCelebrationInfo.getAuditUpdateInfo().setDateModified( new Timestamp( System.currentTimeMillis() ) );
    }
    else
    {
      throw new BadRequestException( "Invalid Event" );
    }
    saCelebrationInfo.setProgramId( engageProgram.getProgramId() );

    return serviceAnniversaryService.saveSACelebrationInfo( saCelebrationInfo );

  }

  /**
   * Return the valid service award schemas
   * 
   */
  @Override
  protected String[] acceptedSchemas() throws Exception
  {
    return ACCEPTED_SCHEMAS;
  }

  /**
   * Validate Service award event data
   * 
   * @param event
   * @return
   * @throws BadRequestException
   */
  private boolean validateServiceAwardEvent( Event<JsonNode> event ) throws BadRequestException
  {

    if ( event.getPersonId() == null || StringUtils.isBlank( event.getPersonId().toString() ) )
    {
      throw new BadRequestException( "A Service award Program has empty person id" );
    }

    String[] dataFieldToValidate = new String[] { "programId", "celebrationSiteId", "awardDate" };

    validateRequiredEventDatas( event, dataFieldToValidate );

    isValidDate( event, "awardDate" );

    return true;

  }

  /**
   * Return SACelebrationInfo
   * 
   * @param event
   * @param engageProgram
   * @return
   * @throws Exception
   */
  private SACelebrationInfo getSACelebration( Event<JsonNode> event, EngageProgram engageProgram ) throws Exception
  {
    UUID celebrationSiteId = Objects.nonNull( event.getData().get( "celebrationSiteId" ).asText() ) ? UUID.fromString( event.getData().get( "celebrationSiteId" ).asText() ) : null;

    SACelebrationInfo saCelebrationInfo = serviceAnniversaryService.getSACelebrationInfo( engageProgram.getProgramId(), celebrationSiteId, event.getCompanyId() );
    return saCelebrationInfo;
  }

  /**
   * Return EngageProgram
   * 
   * @param event
   * @return
   * @throws BadRequestException
   */
  private EngageProgram getProgram( Event<JsonNode> event ) throws BadRequestException
  {

    UUID extProgramId = Objects.nonNull( event.getData().get( "programId" ).asText() ) ? UUID.fromString( event.getData().get( "programId" ).asText() ) : null;

    EngageProgram engageProgram = engageProgramService.getEngageProgramByExternalProgramIdandType( extProgramId, CommonConstants.PROGRAM_TYPE_SERVICE_ANIVERSARY );

    if ( engageProgram == null )
    {
      throw new BadRequestException( "There is no configured program" );
    }
    return engageProgram;
  }

  /**
   * Construct SACelebrationInfo object
   * 
   * @param event
   * @return
   * @throws BadRequestException
   */
  private SACelebrationInfo prepareSACelebrationInfo( Event<JsonNode> event, SACelebrationInfo inSACelebrationInfo ) throws BadRequestException
  {
    SACelebrationInfo saCelebrationInfo = null;

    if ( inSACelebrationInfo != null )
    {
      saCelebrationInfo = inSACelebrationInfo;
    }
    else
    {
      saCelebrationInfo = new SACelebrationInfo();
    }

    if ( event.getCompanyId() != null && !StringUtils.isBlank( event.getCompanyId().toString() ) )
    {
      saCelebrationInfo.setCompanyId( event.getCompanyId() );
    }

    if ( event.getPersonId() != null && !StringUtils.isBlank( event.getPersonId().toString() ) )
    {
      saCelebrationInfo.setRecipientId( userService.getUserIdByRosterUserId( event.getPersonId() ) );

    }
    if ( inSACelebrationInfo == null )
    {
      saCelebrationInfo.setCelebrationId( UUID.fromString( getEventData( event, "celebrationSiteId" ) ) );
    }
    saCelebrationInfo.setAwardLevel( getEventData( event, "awardLevel" ) );

    Date awardDate = DateUtils.convert( getEventData( event, "awardDate" ) );
    saCelebrationInfo.setAwardDate( awardDate );

    String hasGiftCode = getEventData( event, "hasGiftCode" );
    if ( "true".equalsIgnoreCase( hasGiftCode ) )
    {
      saCelebrationInfo.setGiftCode( true );
    }
    else
    {
      saCelebrationInfo.setGiftCode( false );
    }

    String hasPonits = getEventData( event, "hasPoints" );
    if ( "true".equalsIgnoreCase( hasPonits ) )
    {
      saCelebrationInfo.setPoints( true );
    }
    else
    {
      saCelebrationInfo.setPoints( false );
    }

    String celebrationSite = getEventData( event, "celebrationSite" );
    if ( "true".equalsIgnoreCase( celebrationSite ) )
    {
      saCelebrationInfo.setCelebrationSite( true );
    }
    else
    {
      saCelebrationInfo.setCelebrationSite( false );
    }

    String points = getEventData( event, "points" );
    if ( points != null && !StringUtils.isBlank( points ) )
    {
      saCelebrationInfo.setAwardPoints( Double.parseDouble( points ) );
    }

    String hasTaxable = getEventData( event, "hasTaxable" );
    if ( "true".equalsIgnoreCase( hasTaxable ) )
    {
      saCelebrationInfo.setTaxable( true );
    }
    else
    {
      saCelebrationInfo.setTaxable( false );
    }

    saCelebrationInfo.setGiftCodeStatus( getEventData( event, "giftCodeStatus" ) );
    saCelebrationInfo.setPointsStatus( getEventData( event, "pointsStatus" ) );
    saCelebrationInfo.setAwardStatus( getEventData( event, "awardStatus" ) );
    saCelebrationInfo.setCountry( getEventData( event, "awardCountry" ) );

    Long teamId = claimService.getNextTeamId();
    saCelebrationInfo.setTeamId( teamId );

    return saCelebrationInfo;

  }

}
