
package com.biperf.core.value.event;

import java.util.List;

public class EventsView
{
  private int totalRecords;
  private List<EventReferenceView> events;

  public int getTotalRecords()
  {
    return totalRecords;
  }

  public void setTotalRecords( int totalRecords )
  {
    this.totalRecords = totalRecords;
  }

  public List<EventReferenceView> getEvents()
  {
    return events;
  }

  public void setEvents( List<EventReferenceView> events )
  {
    this.events = events;
  }

}
