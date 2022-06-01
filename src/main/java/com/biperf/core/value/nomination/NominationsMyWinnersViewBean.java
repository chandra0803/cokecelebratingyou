
package com.biperf.core.value.nomination;

public class NominationsMyWinnersViewBean
{
  private MyWinnersTabularDataViewBean tabularData = new MyWinnersTabularDataViewBean();

  private int total;

  private int perPage;

  private int currentPage;

  private String sortedOn;

  private String sortedBy;

  public MyWinnersTabularDataViewBean getTabularData()
  {
    return tabularData;
  }

  public void setTabularData( MyWinnersTabularDataViewBean tabularData )
  {
    this.tabularData = tabularData;
  }

  public int getTotal()
  {
    return total;
  }

  public void setTotal( int total )
  {
    this.total = total;
  }

  public int getPerPage()
  {
    return perPage;
  }

  public void setPerPage( int perPage )
  {
    this.perPage = perPage;
  }

  public int getCurrentPage()
  {
    return currentPage;
  }

  public void setCurrentPage( int currentPage )
  {
    this.currentPage = currentPage;
  }

  public String getSortedOn()
  {
    return sortedOn;
  }

  public void setSortedOn( String sortedOn )
  {
    this.sortedOn = sortedOn;
  }

  public String getSortedBy()
  {
    return sortedBy;
  }

  public void setSortedBy( String sortedBy )
  {
    this.sortedBy = sortedBy;
  }
}
