
package com.biperf.core.service.eventreference;

import java.util.List;

import com.biperf.core.domain.eventreference.EventReference;
import com.biperf.core.service.SAO;

public interface EventReferenceService extends SAO
{
  EventReference saveEventReference( EventReference eventReference ) throws Exception;

  @SuppressWarnings( { "unchecked", "rawtypes" } )
  int getEventsCount( String appName, String schemaName, String eventName, String state );

  @SuppressWarnings( { "unchecked", "rawtypes" } )
  List getEvents( String appName, String schemaName, String eventName, String state, Long startRow, Long noOfRows ) throws Exception;

  @SuppressWarnings( { "unchecked", "rawtypes" } )
  List getEventsSource() throws Exception;

  @SuppressWarnings( { "unchecked", "rawtypes" } )
  List getLastDayEventsByState( String state ) throws Exception;

  @SuppressWarnings( { "unchecked", "rawtypes" } )
  int getCountOfLastDayEventsByState( String state ) throws Exception;

}
