
package com.biperf.core.value.profile;

public class ProfileRPMDTO
{

  private String mode;
  private String nodeIds;
  private String timeframeType = "month";
  private int timeframeYear;
  private Long userId;
  private String timeframeNavigate;
  private String sortedOn;
  private String sortedBy;
  private String nodeName;
  private boolean _drillDown;
  int page = 1;
  private String type;

  public String getMode()
  {
    return mode;
  }

  public void setMode( String mode )
  {
    this.mode = mode;
  }

  public String getNodeIds()
  {
    return nodeIds;
  }

  public void setNodeIds( String nodeIds )
  {
    this.nodeIds = nodeIds;
  }

  public String getTimeframeType()
  {
    return timeframeType;
  }

  public void setTimeframeType( String timeframeType )
  {
    this.timeframeType = timeframeType;
  }

  public int getTimeframeYear()
  {
    return timeframeYear;
  }

  public void setTimeframeYear( int timeframeYear )
  {
    this.timeframeYear = timeframeYear;
  }

  public Long getUserId()
  {
    return userId;
  }

  public void setUserId( Long userId )
  {
    this.userId = userId;
  }

  public String getTimeframeNavigate()
  {
    return timeframeNavigate;
  }

  public void setTimeframeNavigate( String timeframeNavigate )
  {
    this.timeframeNavigate = timeframeNavigate;
  }

  public String getSortedOn()
  {
    return sortedOn;
  }

  public void setSortedOn( String sortedOn )
  {
    this.sortedOn = sortedOn;
  }

  public String getSortedBy()
  {
    return sortedBy;
  }

  public void setSortedBy( String sortedBy )
  {
    this.sortedBy = sortedBy;
  }

  public String getNodeName()
  {
    return nodeName;
  }

  public void setNodeName( String nodeName )
  {
    this.nodeName = nodeName;
  }

  public boolean is_drillDown()
  {
    return _drillDown;
  }

  public void set_drillDown( boolean _drillDown )
  {
    this._drillDown = _drillDown;
  }

  public int getPage()
  {
    return page;
  }

  public void setPage( int page )
  {
    this.page = page;
  }

  public String getType()
  {
    return type;
  }

  public void setType( String type )
  {
    this.type = type;
  }

}
