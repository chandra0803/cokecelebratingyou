
package com.biperf.core.domain.homepage;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.enums.FilterSetupType;

@SuppressWarnings( "serial" )
public class FilterAppSetup extends BaseDomain
{
  private boolean mobileEnabled;
  private int priority;
  private int orderNumber;
  private ModuleApp moduleApp;
  private FilterSetupType filterSetupType;
  private boolean searchEnabled;

  // non-mapped field
  private String size = null;

  public int getOrderNumber()
  {
    return orderNumber;
  }

  public int getPriority()
  {
    return priority;
  }

  public void setPriority( int priority )
  {
    this.priority = priority;
  }

  public void setOrderNumber( int orderNumber )
  {
    this.orderNumber = orderNumber;
  }

  public ModuleApp getModuleApp()
  {
    return moduleApp;
  }

  public void setModuleApp( ModuleApp moduleApp )
  {
    this.moduleApp = moduleApp;
  }

  public FilterSetupType getFilterSetupType()
  {
    return filterSetupType;
  }

  public void setFilterSetupType( FilterSetupType filterSetupType )
  {
    this.filterSetupType = filterSetupType;
  }

  public boolean equals( Object object )
  {
    if ( this == object )
    {
      return true;
    }

    if ( ! ( object instanceof FilterAppSetup ) )
    {
      return false;
    }

    final FilterAppSetup filterApp = (FilterAppSetup)object;

    if ( null != filterApp.getModuleApp() && null != getModuleApp() )
    {
      if ( !filterApp.getModuleApp().getName().equals( getModuleApp().getName() ) )
      {
        return false;
      }
      if ( filterApp.getModuleApp().getTileMappingType() != getModuleApp().getTileMappingType() )
      {
        return false;
      }
      if ( filterApp.getOrderNumber() != getOrderNumber() )
      {
        return false;
      }
    }
    return true;
  }

  public String getSize()
  {
    return size;
  }

  public void setSize( String size )
  {
    this.size = size;
  }

  /**
   * Define the hashCode from the id. Overridden from
   *
   * @return int
   * @see com.biperf.core.domain.BaseDomain#hashCode()
   */
  public int hashCode()
  {
    String name = getModuleApp().getName() + getModuleApp().getTileMappingType().getCode();
    return name.hashCode();
  }

  public boolean isMobileEnabled()
  {
    return mobileEnabled;
  }

  public void setMobileEnabled( boolean mobileEnabled )
  {
    this.mobileEnabled = mobileEnabled;
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
