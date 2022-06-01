
package com.biperf.core.service.home;

public class ModuleDisplayMapping implements java.io.Serializable
{
  private String filterName;
  private String size;
  private String order;
  private boolean isActiveForUser = false;
  private boolean searchEnabled;

  public String getFilterName()
  {
    return filterName;
  }

  public String getOrder()
  {
    return order;
  }

  public void setSize( String size )
  {
    this.size = size;
  }

  public String getSize()
  {
    return size;
  }

  public void setFilterName( String filterName )
  {
    this.filterName = filterName;
  }

  public void setOrder( String order )
  {
    this.order = order;
  }

  public boolean isActiveForUser()
  {
    return isActiveForUser;
  }

  public void setActiveForUser( boolean isActiveForUser )
  {
    this.isActiveForUser = isActiveForUser;
  }

  public boolean isSearchEnabled()
  {
    return searchEnabled;
  }

  public void setSearchEnabled( boolean searchEnabled )
  {
    this.searchEnabled = searchEnabled;
  }
}
