/*
 * (c) 2017 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.dao.eventreference.hibernate;

import java.util.List;

import org.hibernate.Query;

import com.biperf.core.dao.BaseDAO;
import com.biperf.core.dao.BaseResultTransformer;
import com.biperf.core.dao.eventreference.EventReferenceDAO;
import com.biperf.core.domain.eventreference.EventReference;
import com.biperf.core.utils.hibernate.HibernateUtil;

/**
 * 
 * @author palaniss
 * @since Nov 01, 2018
 * 
 */
public class EventReferenceDAOImpl extends BaseDAO implements EventReferenceDAO
{

  @Override
  public EventReference saveEventReference( EventReference eventReference )
  {
    return (EventReference)HibernateUtil.saveOrUpdateOrDeepMerge( eventReference );
  }

  @Override
  @SuppressWarnings( { "unchecked", "rawtypes" } )
  public List getEvents( String appName, String schemaName, String eventName, String state, Long startRow, Long noOfRows )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.eventreference.GetEvents" );
    query.setString( "appName", appName );
    query.setString( "schName", schemaName );
    query.setString( "evtName", eventName );
    query.setString( "state", state );
    query.setLong( "startRow", startRow - 1 );
    query.setLong( "noOfRows", noOfRows );
    List lst = query.list();
    return lst;
  }

  @Override
  @SuppressWarnings( { "unchecked", "rawtypes" } )
  public int getEventsCount( String appName, String schemaName, String eventName, String state )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.eventreference.GetEventsCount" );
    query.setString( "appName", appName );
    query.setString( "schName", schemaName );
    query.setString( "evtName", eventName );
    query.setString( "state", state );

    Integer count = (Integer)query.uniqueResult();
    return count.intValue();
  }

  @Override
  @SuppressWarnings( { "unchecked", "rawtypes" } )
  public List getEventsSource()
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.eventreference.GetEventsSource" );
    query.setResultTransformer( new EventReferenceListValueBeannResultTransformer() );
    return query.list();
  }

  private class EventReferenceListValueBeannResultTransformer extends BaseResultTransformer
  {
    @Override
    public Object transformTuple( Object[] tuple, String[] aliases )
    {
      EventReference eventReference = new EventReference();
      eventReference.setEventName( tuple[0].toString() );
      eventReference.setSchemaName( tuple[1].toString() );
      eventReference.setApplicationName( tuple[2].toString() );
      return eventReference;
    }
  }

  @Override
  @SuppressWarnings( { "unchecked", "rawtypes" } )
  public List getLastDayEventsByState( String state ) throws Exception
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.eventreference.GetLastDayEventsByState" );
    query.setString( "state", state );
    List lst = query.list();
    return lst;
  }

  @Override
  @SuppressWarnings( { "unchecked", "rawtypes" } )
  public int getCountOfLastDayEventsByState( String state ) throws Exception
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.eventreference.GetCountOfLastDayEventsByState" );
    query.setString( "state", state );

    Integer count = (Integer)query.uniqueResult();
    return count.intValue();
  }

}
