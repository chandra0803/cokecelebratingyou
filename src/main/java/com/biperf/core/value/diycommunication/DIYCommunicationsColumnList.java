
package com.biperf.core.value.diycommunication;

public class DIYCommunicationsColumnList
{
  private String id;
  private String name;
  private String type;
  private boolean sortable;
  private String sortUrl;

  public String getId()
  {
    return id;
  }

  public void setId( String id )
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

  public String getType()
  {
    return type;
  }

  public void setType( String type )
  {
    this.type = type;
  }

  public boolean getSortable()
  {
    return sortable;
  }

  public void setSortable( boolean sortable )
  {
    this.sortable = sortable;
  }

  public String getSortUrl()
  {
    return sortUrl;
  }

  public void setSortUrl( String sortUrl )
  {
    this.sortUrl = sortUrl;
  }

}
