
package com.biperf.core.value.nomination;

public class MyWinnersTabularDataColumnsViewBean
{
  private int id;
  private String name;
  private String displayName;
  private boolean sortable;

  public MyWinnersTabularDataColumnsViewBean()
  {

  }

  public MyWinnersTabularDataColumnsViewBean( int id, String name, String displayName, boolean sortable )
  {
    this.id = id;
    this.name = name;
    this.displayName = displayName;
    this.sortable = sortable;
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
}
