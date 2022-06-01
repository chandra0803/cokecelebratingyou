
package com.biperf.core.ui.engagement;

/**
 * 
 * EngagementRecognizedParticipantView.
 * 
 * @author kandhi
 * @since Jul 11, 2014
 * @version 1.0
 */
public class EngagementRecognizedParticipantView
{
  private Long id;
  private String name;
  private String avatarUrl;
  private int count;
  private String nodeName;
  private Long nodeId;

  public Long getId()
  {
    return id;
  }

  public void setId( Long id )
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

  public String getAvatarUrl()
  {
    return avatarUrl;
  }

  public void setAvatarUrl( String avatarUrl )
  {
    this.avatarUrl = avatarUrl;
  }

  public int getCount()
  {
    return count;
  }

  public void setCount( int count )
  {
    this.count = count;
  }

  public String getNodeName()
  {
    return nodeName;
  }

  public void setNodeName( String nodeName )
  {
    this.nodeName = nodeName;
  }

  public Long getNodeId()
  {
    return nodeId;
  }

  public void setNodeId( Long nodeId )
  {
    this.nodeId = nodeId;
  }

}
