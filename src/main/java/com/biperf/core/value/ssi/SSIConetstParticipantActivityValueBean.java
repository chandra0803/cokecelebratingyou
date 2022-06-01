
package com.biperf.core.value.ssi;

import java.util.ArrayList;
import java.util.List;

public class SSIConetstParticipantActivityValueBean
{

  private Long id; // pax id
  private List<Activity> activity = new ArrayList<Activity>(); // activities
  private String totalActivity; // non DTGT contest activity amount

  public String getTotalActivity()
  {
    return totalActivity;
  }

  public void setTotalActivity( String totalActivity )
  {
    this.totalActivity = totalActivity;
    // non DTGT contest activity amount
    activity.add( new Activity( null, totalActivity ) );
  }

  public Activity getActivity( int index )
  {
    while ( index >= activity.size() )
    {
      activity.add( new Activity() );
    }
    return activity.get( index );
  }

  public List<Activity> getActivityAsList()
  {
    return activity;
  }

  public void setActivityAsList( Activity activity )
  {
    this.activity.add( activity );
  }

  public int getActivitySize()
  {
    if ( this.activity != null )
    {
      return this.activity.size();
    }
    return 0;
  }

  public Long getId()
  {
    return id;
  }

  public void setId( Long id )
  {
    this.id = id;
  }

  public class Activity
  {
    private Long id;
    private String totalActivity;

    public Activity()
    {
    }

    public Activity( Long id, String totalActivity )
    {
      super();
      this.id = id;
      this.totalActivity = totalActivity;
    }

    public Long getId()
    {
      return id;
    }

    public void setId( Long id )
    {
      this.id = id;
    }

    public String getTotalActivity()
    {
      return totalActivity;
    }

    public void setTotalActivity( String totalActivity )
    {
      this.totalActivity = totalActivity;
    }
  }

}
