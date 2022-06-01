
package com.biperf.core.value.nomination;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents behavior option during nomination submission
 */
public class NominationSubmitDataBehaviorValueBean
{

  private String id;
  private String name;
  private String image;
  private boolean selected;
  private int position;

  @JsonProperty( "id" )
  public String getId()
  {
    return id;
  }

  public void setId( String id )
  {
    this.id = id;
  }

  @JsonProperty( "name" )
  public String getName()
  {
    return name;
  }

  public void setName( String name )
  {
    this.name = name;
  }

  @JsonProperty( "img" )
  public String getImage()
  {
    return image;
  }

  public void setImage( String image )
  {
    this.image = image;
  }

  @JsonProperty( "selected" )
  public boolean isSelected()
  {
    return selected;
  }

  public void setSelected( boolean selected )
  {
    this.selected = selected;
  }

  public int getPosition()
  {
    return position;
  }

  public void setPosition( int position )
  {
    this.position = position;
  }

}
