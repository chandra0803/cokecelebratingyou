
package com.biperf.core.ui.engagement;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 
 * EngagementNodeView.
 * 
 * @author kandhi
 * @since May 20, 2014
 * @version 1.0
 */
public class EngagementNodeView
{
  private Long id;
  private String name;
  private Long parentNodeId;
  private boolean selected;

  public EngagementNodeView( Long id, String name, boolean selected )
  {
    super();
    this.id = id;
    this.name = name;
    this.selected = selected;
  }

  public EngagementNodeView( Long id, String name, Long parentNodeId )
  {
    super();
    this.id = id;
    this.name = name;
    this.parentNodeId = parentNodeId;
  }

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

  public boolean isSelected()
  {
    return selected;
  }

  public void setSelected( boolean selected )
  {
    this.selected = selected;
  }

  @JsonIgnore
  public Long getParentNodeId()
  {
    return parentNodeId;
  }

  public void setParentNodeId( Long parentNodeId )
  {
    this.parentNodeId = parentNodeId;
  }

}
