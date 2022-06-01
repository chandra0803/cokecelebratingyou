
package com.biperf.core.ui.nomination;

import com.fasterxml.jackson.annotation.JsonProperty;

public class NominationsInProgressListColumnViewObject
{

  private Integer id;
  private String name;
  private String displayName;
  private boolean sortable;

  @JsonProperty( "name" )
  public String getName()
  {
    return name;
  }

  public void setName( String name )
  {
    this.name = name;
  }

  @JsonProperty( "displayName" )
  public String getDisplayName()
  {
    return displayName;
  }

  public void setDisplayName( String displayName )
  {
    this.displayName = displayName;
  }

  @JsonProperty( "sortable" )
  public boolean isSortable()
  {
    return sortable;
  }

  public void setSortable( boolean sortable )
  {
    this.sortable = sortable;
  }

  @JsonProperty( "id" )
  public Integer getId()
  {
    return id;
  }

  public void setId( Integer id )
  {
    this.id = id;
  }

}
