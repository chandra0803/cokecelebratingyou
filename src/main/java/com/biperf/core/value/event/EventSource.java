
package com.biperf.core.value.event;

import java.util.ArrayList;
import java.util.List;

public class EventSource
{
  private String applicationName;
  private List<EventSchema> eventSchema = new ArrayList<EventSchema>();

  public String getApplicationName()
  {
    return applicationName;
  }

  public void setApplicationName( String applicationName )
  {
    this.applicationName = applicationName;
  }

  public List<EventSchema> getEventSchema()
  {
    return eventSchema;
  }

  public void setEventSchema( List<EventSchema> eventSchema )
  {
    this.eventSchema = eventSchema;
  }

}
