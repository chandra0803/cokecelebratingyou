
package com.biperf.core.ui.engagement;

/**
 * 
 * EngagementDetailView.
 * 
 * @author kandhi
 * @since May 20, 2014
 * @version 1.0
 */
public class EngagementDetailView
{
  private String type;
  private EngagementDetailDataView data;

  public EngagementDetailView( String type, EngagementDetailDataView data )
  {
    this.type = type;
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

  public EngagementDetailDataView getData()
  {
    return data;
  }

  public void setData( EngagementDetailDataView data )
  {
    this.data = data;
  }

}
