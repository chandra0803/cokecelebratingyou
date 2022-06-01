
package com.biperf.core.value.nomination;

public class NominationsInProgressListColumnValueObject
{

  private Integer id;
  private String name;
  private String displayName;
  private boolean sortable;

  public String getName()
  {
    return name;
  }

  public void setName( String name )
  {
    this.name = name;
  }

  public String getDisplayName()
  {
    return displayName;
  }

  public void setDisplayName( String displayName )
  {
    this.displayName = displayName;
  }

  public boolean isSortable()
  {
    return sortable;
  }

  public void setSortable( boolean sortable )
  {
    this.sortable = sortable;
  }

  public Integer getId()
  {
    return id;
  }

  public void setId( Integer id )
  {
    this.id = id;
  }

}
