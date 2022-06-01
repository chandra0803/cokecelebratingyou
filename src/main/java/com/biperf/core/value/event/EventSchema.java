
package com.biperf.core.value.event;

import java.util.ArrayList;
import java.util.List;

public class EventSchema
{

  private String schemaName;
  private List<String> events = new ArrayList<String>();

  public String getSchemaName()
  {
    return schemaName;
  }

  public void setSchemaName( String schemaName )
  {
    this.schemaName = schemaName;
  }

  public List<String> getEvents()
  {
    return events;
  }

  public void setEvents( List<String> events )
  {
    this.events = events;
  }

}
