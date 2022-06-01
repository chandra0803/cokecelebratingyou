
package com.biperf.core.event.processor;

import java.util.UUID;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.biperf.core.domain.company.Company;
import com.biperf.core.domain.eventreference.EventReference;
import com.biperf.core.exception.BadRequestException;
import com.biperf.core.service.company.CompanyService;
import com.biperf.core.service.eventreference.EventReferenceService;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.utils.DateUtils;
import com.biw.event.streams.event.Event;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public abstract class BaseEventProcessor
{

  private static Log log = LogFactory.getLog( BaseEventProcessor.class );

  protected @Autowired SystemVariableService systemVariableService;
  protected @Autowired EventReferenceService eventReferenceService;
  protected @Autowired UserService userService;
  protected @Autowired CompanyService companyService;

  final String EVENT_STATE_INITIAL = "INITIAL";
  final String EVENT_STATE_COMPLETED = "COMPLETED";
  final String EVENT_STATE_ERROR = "ERROR";

  /**
   * Process events from Kinesis (Invoked by event-stream library)
   *
   * @param event
   */
  public void processEvent( Event<JsonNode> event )
  {
    boolean validCompany = isValidCompany( event );

    if ( !validCompany )
    {
      return;
    }

    EventReference eventReference = null;

    try
    {
      eventReference = saveEventReference( null, event, EVENT_STATE_INITIAL, null, null );
      validate( event );
      // Added this synchronized block to process the events in sequence, It avoid the Error when a
      // Row was
      // updated or deleted by another transaction
      synchronized ( this )
      {
        handleEvent( event );
      }
      eventReference = saveEventReference( eventReference, event, EVENT_STATE_COMPLETED, null, null );

    }
    catch( BadRequestException badRequestException )
    {
      logEventError( "BadRequestException when process event", event );
      saveEventReference( eventReference, event, EVENT_STATE_ERROR, badRequestException.getMessage(), ExceptionUtils.getStackTrace( badRequestException ) );

    }
    catch( Exception exception )
    {
      logEventError( "Exception when process event", event );
      saveEventReference( eventReference, event, EVENT_STATE_ERROR, exception.getMessage(), ExceptionUtils.getStackTrace( exception ) );
    }

  }

  /**
   * Return true if the event is valid (Invoked by event-stream library)
   *
   * @param schemaName
   * @return
   */
  public boolean acceptsEvent( String schemaName )
  {
    try
    {
      String[] acceptedSchemas = acceptedSchemas();
      return ArrayUtils.contains( acceptedSchemas, schemaName );
    }
    catch( Exception exception )
    {
      log.error( "Exception in acceptEvent schema-name:" + schemaName, exception );
    }
    return false;
  }

  /**
   * Validate the company
   *
   * @param event
   * @return
   */
  protected boolean isValidCompany( Event<JsonNode> event )
  {
    if ( event.getCompanyId() == null || StringUtils.isBlank( event.getCompanyId().toString() ) )
    {
      log.warn( "Company ID is missing" );
      return false;
    }

    try
    {
      UUID companyId = event.getCompanyId();

      Company company = companyService.getCompanyDetail();

      if ( companyId.equals( company.getCompanyId() ) )
      {
        return true;
      }

      return false;

    }
    catch( Exception exception )
    {
      log.error( "Exception in validateCompany()", exception );
    }
    return false;
  }

  /**
   * Validate application and event
   *
   * @param event
   * @return
   * @throws BadRequestException
   */
  protected boolean validate( Event<JsonNode> event ) throws BadRequestException
  {
    if ( StringUtils.isBlank( event.getApplication() ) )
    {
      throw new BadRequestException( "Application is missing" );
    }

    if ( StringUtils.isBlank( event.getEvent() ) )
    {
      throw new BadRequestException( "Event name is missing" );
    }
    return true;

  }

  /**
   * Construct the CheckSum
   *
   * @param event
   * @return
   * @throws Exception
   */
  protected String buildEventChecksum( Event<?> event ) throws Exception
  {
    ObjectMapper objectMapper = new ObjectMapper();
    // The salt is to avoid random values.
    // We don't need cryptographic security for our event checksums. Fast is better.
    String jsonString = objectMapper.writeValueAsString( event );
    String md5Checksum = DigestUtils.sha256Hex( jsonString.getBytes() );
    return md5Checksum;
  }

  /**
   * Construct EventReference object
   *
   * @param event
   * @param checksum
   * @param state
   * @return
   * @throws Exception
   */
  private EventReference buildEventReference( Event<JsonNode> event, String checksum, String state ) throws Exception
  {
    EventReference eventReference = new EventReference();
    eventReference.setCompanyId( event.getCompanyId() );
    if ( event.getPersonId() != null && !StringUtils.isBlank( event.getPersonId().toString() ) )
    {
      Long recipientId = userService.getUserIdByRosterUserId( event.getPersonId() );

      eventReference.setRecipientId( recipientId );
    }
    if ( event.getApplication() != null && !StringUtils.isBlank( event.getApplication() ) )
    {
      eventReference.setApplicationName( event.getApplication() );
    }
    if ( event.getSchemaName() != null && !StringUtils.isBlank( event.getSchemaName() ) )
    {
      eventReference.setSchemaName( event.getSchemaName() );
    }
    if ( event.getEvent() != null && !StringUtils.isBlank( event.getEvent() ) )
    {
      eventReference.setEventName( event.getEvent() );
    }

    eventReference.setState( state );
    eventReference.setChecksum( checksum );

    ObjectMapper objectMapper = new ObjectMapper();
    String eventStr = objectMapper.writeValueAsString( event );
    eventReference.setData( eventStr );

    return eventReference;
  }

  /**
   * Used to catch duplicate events.
   * Will compute a checksum from the event, and try to save an EventReference.
   * The save is expected to fail on a unique constraint (the checksum) for duplicate events.
   * @return An EventReference, if successful (this is the first time we're seeing this event)
   * @throws Exception If the checksum could not be computed, or the event is a duplicate
   */
  protected EventReference saveEventReference( EventReference eventReference, Event<JsonNode> event, String state, String msg, String logMsg )

  {
    try
    {
      if ( eventReference == null )
      {
        String checksum = buildEventChecksum( event );
        EventReference insEventReference = buildEventReference( event, checksum, state );
        return eventReferenceService.saveEventReference( insEventReference );
      }
      else
      {
        eventReference.setState( state );
        eventReference.setMessage( msg );
        eventReference.setLog( logMsg );
        return eventReferenceService.saveEventReference( eventReference );
      }
    }
    catch( Exception exception )
    {
      log.error( "Exception when save event reference", exception );
      return null;
    }
  }

  /**
   * Log the event error
   *
   * @param message
   * @param event
   */
  protected void logEventError( String message, Event<?> event )
  {
    StringBuilder sb = new StringBuilder();
    sb.append( message );
    sb.append( System.lineSeparator() );
    sb.append( "Event contents: " );
    sb.append( eventJsonError( event ) );
    log.error( sb.toString() );
  }

  /**
   * Convert Event Object to String
   *
   * @param event
   * @return
   */
  protected String eventJsonError( Event<?> event )
  {
    try
    {
      ObjectMapper objectMapper = new ObjectMapper();
      return objectMapper.writeValueAsString( event );
    }
    catch( Exception e )
    {
      return "Error writing event json: " + e.getMessage();
    }
  }

  /**
   * Validate the data
   *
   * @param event
   * @param fieldName
   * @param datas
   * @return
   */
  protected boolean isValidData( Event<JsonNode> event, String fieldName, String[] datas )
  {
    if ( event.getData().has( fieldName ) && !StringUtils.isBlank( event.getData().get( fieldName ).asText() ) )
    {
      String fieldValue = event.getData().get( fieldName ).asText();
      for ( String data : datas )
      {
        if ( fieldValue.equalsIgnoreCase( data ) )
        {
          return true;
        }
      }
    }
    return false;
  }

  /**
   * Validate Date
   *
   * @param event
   * @param fieldName
   * @param dtFmt
   * @return True if the given date is valid
   * @throws BadRequestException
   */
  protected boolean isValidDate( Event<JsonNode> event, String fieldName ) throws BadRequestException
  {
    if ( event.getData().has( fieldName ) && !StringUtils.isBlank( event.getData().get( fieldName ).asText() ) && !"null".equals( event.getData().get( fieldName ).asText() ) )
    {
      boolean isValid = DateUtils.isValidUTCDate( event.getData().get( fieldName ).asText() );

      if ( !isValid )
      {
        String msg = "A Service award Program has invalid " + fieldName;
        throw new BadRequestException( msg );
      }
      return isValid;
    }
    return false;
  }

  /**
   * Return True if the Event Data required fields are not empty otherwise throw BadRequestException
   * 
   * @param event
   * @param fieldName
   * @return
   * @throws BadRequestException
   */
  protected boolean validateRequiredEventDatas( Event<JsonNode> event, String[] fieldNames ) throws BadRequestException
  {
    JsonNode dataNode = event.getData();
    for ( String fieldName : fieldNames )
    {
      if ( StringUtils.isBlank( dataNode.at( "/" + fieldName ).asText() ) )
      {
        String msg = "A Service award Program has empty " + fieldName;
        throw new BadRequestException( msg );
      }
    }
    return true;
  }

  /**
   * Return a valid String representation value if the node is a value node, other wise return null
   * 
   * @param event
   * @param fieldName
   * @return
   */
  protected String getEventData( Event<JsonNode> event, String fieldName )
  {
    JsonNode dataNode = event.getData();
    return getEventData( dataNode, fieldName );

  }

  /**
   * Return a valid String representation value if the node is a value node, other wise return null
   * 
   * @param dataNode
   * @param fieldName
   * @return
   */
  protected String getEventData( JsonNode dataNode, String fieldName )
  {
    if ( !StringUtils.isBlank( dataNode.at( "/" + fieldName ).asText() ) && !"null".equalsIgnoreCase( dataNode.at( "/" + fieldName ).asText() ) )
    {
      return dataNode.at( "/" + fieldName ).asText();
    }
    return null;

  }

  protected abstract boolean handleEvent( Event<JsonNode> event ) throws Exception;

  protected abstract String[] acceptedSchemas() throws Exception;

}
