
package com.biperf.core.value;

public class MatchListColumn
{

  private String id;
  private String name;
  private String description;
  private String type;
  private String alignment;
  private int colSpan;
  private String nameId;
  private boolean sortable;
  private String sortUrl;

  public void setId( String id )
  {
    this.id = id;
  }

  public String getId()
  {
    return id;
  }

  public void setDescription( String description )
  {
    this.description = description;
  }

  public String getDescription()
  {
    return description;
  }

  public void setName( String name )
  {
    this.name = name;
  }

  public String getName()
  {
    return name;
  }

  public void setType( String type )
  {
    this.type = type;
  }

  public String getType()
  {
    return type;
  }

  public void setAlignment( String alignment )
  {
    this.alignment = alignment;
  }

  public String getAlignment()
  {
    return alignment;
  }

  public void setColSpan( int colSpan )
  {
    this.colSpan = colSpan;
  }

  public int getColSpan()
  {
    return colSpan;
  }

  public void setNameId( String nameId )
  {
    this.nameId = nameId;
  }

  public String getNameId()
  {
    return nameId;
  }

  public void setSortable( boolean sortable )
  {
    this.sortable = sortable;
  }

  public boolean isSortable()
  {
    return sortable;
  }

  public void setSortUrl( String sortUrl )
  {
    this.sortUrl = sortUrl;
  }

  public String getSortUrl()
  {
    return sortUrl;
  }

}
