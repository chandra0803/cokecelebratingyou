
package com.biperf.core.ui.search;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.biperf.core.domain.eventreference.EventReference;
import com.biperf.core.event.KinesisEventScheduler;
import com.biperf.core.service.eventreference.EventReferenceService;
import com.biperf.core.utils.NewServiceAnniversaryUtil;
import com.biperf.core.value.event.EventReferenceView;
import com.biperf.core.value.event.EventSchema;
import com.biperf.core.value.event.EventSource;
import com.biperf.core.value.event.EventSourceView;
import com.biperf.core.value.event.EventsView;
import com.biperf.core.value.event.KinesisSchedulerStatusView;

@Controller
@RequestMapping( "/admin/event" )
public class EventAdminController
{

  private static final Log LOGGER = LogFactory.getLog( EventAdminController.class );

  @Autowired
  KinesisEventScheduler kinesisEventScheduler;

  @Autowired
  EventReferenceService eventReferenceService;

  @RequestMapping( value = "/status.action", method = RequestMethod.GET )
  public @ResponseBody KinesisSchedulerStatusView kinesisSchedulerStatus() throws Exception
  {
    KinesisSchedulerStatusView kinesisSchedulerStatusView = new KinesisSchedulerStatusView();
    if ( kinesisEventScheduler.kinesisSchedulerStatus() )
    {
      kinesisSchedulerStatusView.setStatus( "up" );
    }
    else
    {
      kinesisSchedulerStatusView.setStatus( "down" );
    }

    return kinesisSchedulerStatusView;
  }

  @RequestMapping( value = "/scheduler/start.action", method = RequestMethod.POST )
  public @ResponseBody KinesisSchedulerStatusView startKinesisScheduler() throws Exception
  {
    KinesisSchedulerStatusView kinesisSchedulerStatusView = new KinesisSchedulerStatusView();

    if ( !NewServiceAnniversaryUtil.isNewServiceAnniversaryEnabled() )
    {
      kinesisSchedulerStatusView.setStatus( "down" );
      kinesisSchedulerStatusView.setMessage( "New version of Service Anniversary is NOT enabled" );
      return kinesisSchedulerStatusView;
    }

    if ( !kinesisEventScheduler.kinesisSchedulerStatus() )
    {
      kinesisEventScheduler.startKinesisConsumer();
    }
    
    try
    {
      Thread.sleep( 10000 );
    }
    catch( Exception e )
    {

    }

    if ( kinesisEventScheduler.kinesisSchedulerStatus() )
    {
      kinesisSchedulerStatusView.setStatus( "up" );
    }
    else
    {
      kinesisSchedulerStatusView.setStatus( "down" );
    }

    return kinesisSchedulerStatusView;
  }

  @RequestMapping( value = "/list.action", method = RequestMethod.GET )
  public @ResponseBody EventsView getEvents( HttpServletRequest httpRequest ) throws Exception
  {
    String appName = null;
    String schemaName = null;
    String eventName = null;
    String state = null;
    Long startRow = 0L;
    Long endRow = 0L;

    if ( httpRequest.getParameter( "appName" ) != null && httpRequest.getParameter( "appName" ).length() > 0 )
    {
      appName = httpRequest.getParameter( "appName" );
    }

    if ( httpRequest.getParameter( "schemaName" ) != null && httpRequest.getParameter( "schemaName" ).length() > 0 )
    {
      schemaName = httpRequest.getParameter( "schemaName" );
    }

    if ( httpRequest.getParameter( "eventName" ) != null && httpRequest.getParameter( "eventName" ).length() > 0 )
    {
      eventName = httpRequest.getParameter( "eventName" );
    }

    if ( httpRequest.getParameter( "state" ) != null && httpRequest.getParameter( "state" ).length() > 0 )
    {
      state = httpRequest.getParameter( "state" );
    }

    if ( httpRequest.getParameter( "startRow" ) != null && httpRequest.getParameter( "startRow" ).length() > 0 )
    {

      startRow = new Long( httpRequest.getParameter( "startRow" ) );
    }
    int totalRecords = eventReferenceService.getEventsCount( appName, schemaName, eventName, state );

    List<EventReference> evts = (List<EventReference>)eventReferenceService.getEvents( appName, schemaName, eventName, state, startRow, 10L );

    List<EventReferenceView> eventViews = new ArrayList<EventReferenceView>();
    for ( EventReference eventReference : evts )
    {
      EventReferenceView eventReferenceView = new EventReferenceView( eventReference );
      eventViews.add( eventReferenceView );
    }
    EventsView eventsView = new EventsView();
    eventsView.setEvents( eventViews );
    eventsView.setTotalRecords( totalRecords );

    return eventsView;
  }
  
  @RequestMapping( value = "/source.action", method = RequestMethod.GET )
  public @ResponseBody EventSourceView getEvensSource( HttpServletRequest httpRequest ) throws Exception
  {
    EventSourceView eventSourceView = new EventSourceView();

    try
    {
      String applnName = null;
      String schName = null;
      List<EventSource> eventSource = new ArrayList<EventSource>();
      List<EventSchema> eventSchemas = null;
      List<String> events = null;
      List<EventReference> evts = (List<EventReference>)eventReferenceService.getEventsSource();
      eventSourceView.setEventSource( eventSource );
      eventSourceView.setSuccess( true );

      EventSchema evtSchema = null;
      EventSource evtSrc = null;

      for ( EventReference evtRef : evts )
      {
        String tApplnName = evtRef.getApplicationName();
        String tSchName = evtRef.getSchemaName();
        String tEvtName = evtRef.getEventName();

        if ( tApplnName != null && !tApplnName.equals( applnName ) )
        {
          applnName = tApplnName;
          evtSrc = new EventSource();
          evtSrc.setApplicationName( applnName );
          eventSchemas = new ArrayList<EventSchema>();
          evtSrc.setEventSchema( eventSchemas );
          eventSource.add( evtSrc );
        }
        
        if ( tSchName != null && !tSchName.equals( schName ) )
        {
          schName = tSchName;
          evtSchema = new EventSchema();
          evtSchema.setSchemaName( schName );
          events = new ArrayList<String>();
          evtSchema.setEvents( events );
          eventSchemas.add( evtSchema );
        }
        
        events.add( tEvtName );

      }
    }
    catch( Exception e )
    {
      LOGGER.error( e );
    }

    return eventSourceView;
  }
}
