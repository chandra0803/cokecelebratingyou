
package com.biperf.core.value.event;

import java.util.ArrayList;
import java.util.List;

public class EventSourceView
{
  private boolean success = true;
  private List<EventSource> eventSource = new ArrayList<EventSource>();

  public boolean isSuccess()
  {
    return success;
  }

  public void setSuccess( boolean success )
  {
    this.success = success;
  }

  public List<EventSource> getEventSource()
  {
    return eventSource;
  }

  public void setEventSource( List<EventSource> eventSource )
  {
    this.eventSource = eventSource;
  }

}
