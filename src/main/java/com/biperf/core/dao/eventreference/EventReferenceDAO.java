/*
 * (c) 2017 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.dao.eventreference;

import java.util.List;

import com.biperf.core.dao.DAO;
import com.biperf.core.domain.eventreference.EventReference;

/**
 * 
 * @author palaniss
 * @since Nov 01, 2018
 * 
 */
public interface EventReferenceDAO extends DAO
{
  public static final String BEAN_NAME = "eventReferenceDAO";

  EventReference saveEventReference( EventReference eventReference );

  @SuppressWarnings( { "unchecked", "rawtypes" } )
  List getEvents( String appName, String schemaName, String eventName, String state, Long startRow, Long noOfRows );

  @SuppressWarnings( { "unchecked", "rawtypes" } )
  int getEventsCount( String appName, String schemaName, String eventName, String state );

  @SuppressWarnings( { "unchecked", "rawtypes" } )
  List getEventsSource();

  @SuppressWarnings( { "unchecked", "rawtypes" } )
  List getLastDayEventsByState( String state ) throws Exception;

  @SuppressWarnings( { "unchecked", "rawtypes" } )
  int getCountOfLastDayEventsByState( String state ) throws Exception;

}
