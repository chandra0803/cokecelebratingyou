
package com.biperf.core.ui.engagement;

/**
 * 
 * EngagementRecognitionByBehaviorView.
 * 
 * @author kandhi
 * @since May 20, 2014
 * @version 1.0
 */
public class EngagementRecognitionByBehaviorView
{
  private int id;
  private String name;
  private int count;
  private String iconUrl;

  public EngagementRecognitionByBehaviorView( int id, String name, int count, String iconUrl )
  {
    super();
    this.id = id;
    this.name = name;
    this.count = count;
    this.iconUrl = iconUrl;
  }

  public int getId()
  {
    return id;
  }

  public void setId( int id )
  {
    this.id = id;
  }

  public String getName()
  {
    return name;
  }

  public void setName( String name )
  {
    this.name = name;
  }

  public int getCount()
  {
    return count;
  }

  public void setCount( int count )
  {
    this.count = count;
  }

  public String getIconUrl()
  {
    return iconUrl;
  }

  public void setIconUrl( String iconUrl )
  {
    this.iconUrl = iconUrl;
  }

}
