
package com.biperf.core.ui.search;

public class SearchFilterTypeCountView
{
  private String paxSearchFilterType;
  private int count = 0;

  public SearchFilterTypeCountView()
  {
  }

  public SearchFilterTypeCountView( String type, int count )
  {
    this.paxSearchFilterType = type;
    this.count = count;
  }

  public String getPaxSearchFilterType()
  {
    return paxSearchFilterType;
  }

  public void setPaxSearchFilterType( String paxSearchFilterType )
  {
    this.paxSearchFilterType = paxSearchFilterType;
  }

  public int getCount()
  {
    return count;
  }

  public void setCount( int count )
  {
    this.count = count;
  }

}
