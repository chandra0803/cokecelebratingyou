
package com.biperf.core.ui.profile;

import java.io.Serializable;

public class ProfilePointsView implements Serializable
{

  private String type;
  private String name;
  private Points data = new Points();

  public ProfilePointsView( Long points )
  {
    data.setPoints( points );
  }

  public Points getData()
  {
    return data;
  }

  public void setData( Points data )
  {
    this.data = data;
  }

  public String getType()
  {
    return type;
  }

  public void setType( String type )
  {
    this.type = type;
  }

  public String getName()
  {
    return name;
  }

  public void setName( String name )
  {
    this.name = name;
  }

  public static final class Points implements Serializable
  {
    private boolean showPoints = true;
    private Long points;

    public Long getPoints()
    {
      return points;
    }

    public void setPoints( Long points )
    {
      this.points = points;
    }

    public boolean isShowPoints()
    {
      return showPoints;
    }

    public void setShowPoints( boolean showPoints )
    {
      this.showPoints = showPoints;
    }
  }

}
