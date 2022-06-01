
package com.biperf.core.event.processor;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Iterator;
import java.util.Objects;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.biperf.core.domain.engageprogram.EngageProgram;
import com.biperf.core.domain.engageprogram.ProgramAwardLevel;
import com.biperf.core.exception.BadRequestException;
import com.biperf.core.service.engageprogram.EngageProgramService;
import com.biperf.core.utils.CommonConstants;
import com.biperf.core.utils.DateUtils;
import com.biw.event.streams.consume.EventProcessor;
import com.biw.event.streams.event.Event;
import com.fasterxml.jackson.databind.JsonNode;

@Component
public class ProgramEventProcessor extends BaseEventProcessor implements EventProcessor
{
  private static Log log = LogFactory.getLog( ProgramEventProcessor.class );

  private @Autowired EngageProgramService engageProgramService;

  private static final String[] ACCEPTED_SCHEMAS = new String[] { "program" };

  private static final String EVENT_CREATE = "create";
  private static final String EVENT_UPDATE = "update";

  /**
   * Validate and Save the Program Event 
   */
  @Override
  protected boolean handleEvent( Event<JsonNode> event ) throws BadRequestException, Exception
  {
    EngageProgram engageProgram = null;

    validateProgramEvent( event );

    if ( EVENT_CREATE.equalsIgnoreCase( event.getEvent() ) )
    {
      engageProgram = prepareSAProgramInfo( event, new EngageProgram() );
    }
    else if ( EVENT_UPDATE.equalsIgnoreCase( event.getEvent() ) )
    {
      // Get the existing program
      engageProgram = getProgram( event );
      if ( engageProgram == null )
      {
        // Create a new Instance
        engageProgram = new EngageProgram();
        // throw new BadRequestException( "There is no existing Program found to update" );
        log.info( "There is no existing program" );
      }

      engageProgram = prepareSAProgramInfo( event, engageProgram );
      engageProgram.getAuditUpdateInfo().setDateModified( new Timestamp( System.currentTimeMillis() ) );
    }
    else
    {
      throw new BadRequestException( "Invalid Event" );
    }

    return engageProgramService.saveEngageProgramDetails( engageProgram );

  }

  /**
   * Return valid schemas to process
   */
  @Override
  protected String[] acceptedSchemas() throws Exception
  {
    return ACCEPTED_SCHEMAS;
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

    return engageProgram;
  }

  /**
   * Validate program event
   * 
   * @param event
   * @return
   * @throws BadRequestException
   */
  private boolean validateProgramEvent( Event<JsonNode> event ) throws BadRequestException
  {
    String[] dataFieldToValidate = new String[] { "programId", "programName/key", "programStartDate", "awardType", "createdAt", "allowContribution", "programState" };

    validateRequiredEventDatas( event, dataFieldToValidate );

    isValidDate( event, "programStartDate" );
    isValidDate( event, "programEndDate" );
    isValidDate( event, "createdAt" );

    if ( !isValidData( event, "allowContribution", new String[] { "true", "false" } ) )
    {
      throw new BadRequestException( "SA Program allowContribution contains invalid data" );
    }

    if ( !isValidData( event, "programState", new String[] { "Active", "Inactive" } ) )
    {
      throw new BadRequestException( "SA Program allowContribution contains invalid data" );
    }

    return true;

  }

  /**
   * Construct the EngageProgram object
   * 
   * @param event
   * @param inEngageProgram
   * @return
   * @throws BadRequestException
   */
  private EngageProgram prepareSAProgramInfo( Event<JsonNode> event, EngageProgram engageProgram ) throws BadRequestException
  {

    engageProgram.setProgramType( CommonConstants.PROGRAM_TYPE_SERVICE_ANIVERSARY );

    if ( event.getCompanyId() != null && !StringUtils.isBlank( event.getCompanyId().toString() ) )
    {
      engageProgram.setCompanyId( event.getCompanyId() );
    }
    if ( Objects.isNull( engageProgram.getExternalProgramId() ) )
    {
      engageProgram.setExternalProgramId( UUID.fromString( getEventData( event, "programId" ) ) );
    }

    engageProgram.setProgramName( getEventData( event, "programName/en-US" ) );

    engageProgram.setProgramNameCMXAssetCode( getEventData( event, "programName/key" ) );

    String awardType = getEventData( event, "awardType" );
    if ( CommonConstants.GIFT_CODE_AWARD_TYPE.equalsIgnoreCase( awardType ) )
    {
      engageProgram.setAwardType( CommonConstants.MERCHANDISE_AWARD_TYPE );
    }
    else
    {
      engageProgram.setAwardType( awardType );
    }

    engageProgram.setProgramHeader( getEventData( event, "welcomeMessage/headerMessage/en-US" ) );

    engageProgram.setProgramHeaderCMXAsstCode( getEventData( event, "welcomeMessage/headerMessage/key" ) );

    String pgmState = getEventData( event, "programState" );
    if ( "Active".equalsIgnoreCase( pgmState ) )
    {
      engageProgram.setProgramStatus( CommonConstants.PROGRAM_STATUS_LIVE );
    }
    else if ( "Inactive".equalsIgnoreCase( pgmState ) )
    {
      engageProgram.setProgramStatus( CommonConstants.PROGRAM_STATUS_EXPIRED );
    }

    if ( !StringUtils.isBlank( getEventData( event, "programStartDate" ) ) )
    {
      Date programStartDate = DateUtils.convert( getEventData( event, "programStartDate" ) );

      engageProgram.setProgramStartDate( programStartDate );
    }

    if ( !StringUtils.isBlank( getEventData( event, "programEndDate" ) ) )
    {
      Date programEndDate = DateUtils.convert( getEventData( event, "programEndDate" ) );

      engageProgram.setProgramEndDate( programEndDate );
    }

    if ( !StringUtils.isBlank( getEventData( event, "createdAt" ) ) )
    {
      Date programCreateDate = DateUtils.convert( getEventData( event, "createdAt" ) );

      engageProgram.setProgramCreateDate( programCreateDate );
    }

    String allowContribution = getEventData( event, "allowContribution" );
    if ( "true".equalsIgnoreCase( allowContribution ) )
    {
      engageProgram.setAllowContribution( true );
    }
    else if ( "false".equalsIgnoreCase( allowContribution ) )
    {
      engageProgram.setAllowContribution( false );
    }

    if ( !StringUtils.isBlank( getEventData( event, "primaryColor" ) ) )
    {
      engageProgram.setPrimaryColor( getEventData( event, "primaryColor" ) );
    }

    if ( !StringUtils.isBlank( getEventData( event, "secondaryColor" ) ) )
    {
      engageProgram.setSecondaryColor( getEventData( event, "secondaryColor" ) );
    }

    engageProgram.getProgramsAwardLevels().clear();
    JsonNode awardLevelsNode = event.getData().at( "/awardLevels" );

    if ( awardLevelsNode != null )
    {
      Iterator<JsonNode> awardLevels = awardLevelsNode.elements();
      while ( awardLevels.hasNext() )
      {
        ProgramAwardLevel programAwardLevel = new ProgramAwardLevel();
        JsonNode awardLevelNode = awardLevels.next();
        programAwardLevel.setAwardLevel( getEventData( awardLevelNode, "awardLevel" ) );
        programAwardLevel.setCountry( getEventData( awardLevelNode, "awardCountry" ) );
        programAwardLevel.setCelebImgUrl( getEventData( awardLevelNode, "celebrationImageUrl" ) );
        programAwardLevel.setCelebImgDescCmxAssetCode( getEventData( awardLevelNode, "celebrationImageDesc/key" ) );
        programAwardLevel.setCelebImgDesc( getEventData( awardLevelNode, "celebrationImageDesc/en-US" ) );
        programAwardLevel.setCelebLabelCmxAssetCode( getEventData( awardLevelNode, "celebrationLabel/key" ) );
        programAwardLevel.setCelebLabel( getEventData( awardLevelNode, "celebrationLabel/en-US" ) );
        programAwardLevel.setCelebMsgCmxAssetCode( getEventData( awardLevelNode, "celebrationDescription/key" ) );
        programAwardLevel.setCelebMsg( getEventData( awardLevelNode, "celebrationDescription/en-US" ) );
        engageProgram.addProgramAwardLevel( programAwardLevel );
      }
    }
    return engageProgram;

  }

}
