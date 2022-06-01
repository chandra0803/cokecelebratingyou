
package com.biperf.core.service.eventreference.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.biperf.core.dao.eventreference.EventReferenceDAO;
import com.biperf.core.domain.eventreference.EventReference;
import com.biperf.core.service.eventreference.EventReferenceService;

@Service( "eventReferenceService" )
public class EventReferenceServiceImpl implements EventReferenceService
{

  private static final Log log = LogFactory.getLog( EventReferenceServiceImpl.class );

  @Autowired
  private EventReferenceDAO eventReferenceDAO;

  @Override
  public EventReference saveEventReference( EventReference eventReference ) throws Exception
  {

    try
    {
      eventReference = eventReferenceDAO.saveEventReference( eventReference );
    }
    catch( HibernateException e )
    {
      log.error( "Even record for Program details is not saved successfully" + eventReference.getSchemaName() );
      throw e;
    }
    return eventReference;
  }

  @Override
  @SuppressWarnings( { "unchecked", "rawtypes" } )
  public List getEvents( String appName, String schemaName, String eventName, String state, Long startRow, Long noOfRows ) throws Exception
  {
    List events = null;
    try
    {
      events = eventReferenceDAO.getEvents( appName, schemaName, eventName, state, startRow, noOfRows );
    }
    catch( HibernateException e )
    {
      log.error( "HibernateException when get events", e );
      throw e;
    }

    return events;
  }

  @Override
  @SuppressWarnings( { "unchecked", "rawtypes" } )
  public int getEventsCount( String appName, String schemaName, String eventName, String state )
  {
    int noOfRecords = 0;

    try
    {
      noOfRecords = eventReferenceDAO.getEventsCount( appName, schemaName, eventName, state );
    }
    catch( HibernateException e )
    {
      log.error( "HibernateException when get events count", e );
      throw e;
    }
    return noOfRecords;
  }

  @Override
  @SuppressWarnings( { "unchecked", "rawtypes" } )
  public List getEventsSource() throws Exception
  {
    return eventReferenceDAO.getEventsSource();
  }

  @Override
  @SuppressWarnings( { "unchecked", "rawtypes" } )
  public List getLastDayEventsByState( String state ) throws Exception
  {
    return eventReferenceDAO.getLastDayEventsByState( state );
  }

  @Override
  @SuppressWarnings( { "unchecked", "rawtypes" } )
  public int getCountOfLastDayEventsByState( String state ) throws Exception
  {
    return eventReferenceDAO.getCountOfLastDayEventsByState( state );
  }

}
