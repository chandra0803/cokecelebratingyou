
package com.biperf.core.ui.engagement;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * 
 * EngagementRecognizedRootView.
 * 
 * @author kandhi
 * @since Jun 23, 2014
 * @version 1.0
 */
public class EngagementRecognizedRootView
{
  @JsonInclude( value = Include.NON_NULL )
  private Long userId;
  @JsonInclude( value = Include.NON_NULL )
  private Long nodeId;
  private String name;
  private String avatarUrl;

  public EngagementRecognizedRootView( Long nodeId, Long userId, String name, String avatarUrl )
  {
    super();
    this.nodeId = nodeId;
    this.userId = userId;
    this.name = name;
    this.avatarUrl = avatarUrl;
  }

  public Long getUserId()
  {
    return userId;
  }

  public void setUserId( Long userId )
  {
    this.userId = userId;
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

  public Long getNodeId()
  {
    return nodeId;
  }

  public void setNodeId( Long nodeId )
  {
    this.nodeId = nodeId;
  }

}
