
package com.biperf.core.value.nomination;

import java.io.Serializable;

public class NominationsInProgressListValueObject implements Serializable
{

  private static final long serialVersionUID = 1L;
  public static final int PAGE_SIZE = 100;

  private int totalInProgressCount;
  private int sortedOnIndex;
  private int startIndex;
  private int endIndex;
  private int currentPage;
  private String sortBy;

  public Integer nominationPerPage;

  private NominationsInProgressListTabularDataValueObject tabularData;

  public int getTotalInProgressCount()
  {
    return totalInProgressCount;
  }

  public void setTotalInProgressCount( int totalInProgressCount )
  {
    this.totalInProgressCount = totalInProgressCount;
  }

  public int getSortedOnIndex()
  {
    return sortedOnIndex;
  }

  public void setSortedOnIndex( int sortedOn )
  {
    this.sortedOnIndex = sortedOn;
  }

  public int getStartIndex()
  {
    return startIndex;
  }

  public void setStartIndex( int startIndex )
  {
    this.startIndex = startIndex;
  }

  public int getEndIndex()
  {
    return endIndex;
  }

  public void setEndIndex( int endIndex )
  {
    this.endIndex = endIndex;
  }

  public int getCurrentPage()
  {
    return currentPage;
  }

  public void setCurrentPage( int currentPage )
  {
    this.currentPage = currentPage;
  }

  public String getSortBy()
  {
    return sortBy;
  }

  public void setSortBy( String sortedBy )
  {
    this.sortBy = sortedBy;
  }

  public NominationsInProgressListTabularDataValueObject getTabularData()
  {
    return tabularData;
  }

  public void setTabularData( NominationsInProgressListTabularDataValueObject tabularData )
  {
    this.tabularData = tabularData;
  }

  public Integer getNominationPerPage()
  {
    return nominationPerPage;
  }

  public void setNominationPerPage( Integer nominationPerPage )
  {
    this.nominationPerPage = nominationPerPage;
  }
}
