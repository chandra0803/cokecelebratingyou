
package com.biperf.core.ui.nomination;

import java.io.Serializable;

import com.biperf.core.domain.enums.nomination.NominationClaimsInProgressSortColumn;
import com.fasterxml.jackson.annotation.JsonProperty;

public class NominationsInProgressListViewObject implements Serializable
{

  private static final long serialVersionUID = 1L;
  public static final int PAGE_SIZE = 100;

  private int total;
  private int currentPage;
  private int nominationPerPage;

  private int startIndex;
  private int endIndex;
  private int sortedOnIndex;
  private String sortBy;

  private NominationsInProgressListTabularDataViewObject tabularData;

  public NominationsInProgressListViewObject()
  {
    super();
  }

  public NominationsInProgressListViewObject( int total, int currentPage, int nominationPerPage, NominationsInProgressListTabularDataViewObject tabularData )
  {
    super();
    this.total = total;
    this.currentPage = currentPage;
    this.nominationPerPage = nominationPerPage;
    this.tabularData = tabularData;
  }

  @JsonProperty( "total" )
  public int getTotal()
  {
    return total;
  }

  public void setTotal( int total )
  {
    this.total = total;
  }

  @JsonProperty( "currentPage" )
  public int getCurrentPage()
  {
    return currentPage;
  }

  public void setCurrentPage( int currentPage )
  {
    this.currentPage = currentPage;
  }

  @JsonProperty( "tabularData" )
  public NominationsInProgressListTabularDataViewObject getTabularData()
  {
    return tabularData;
  }

  public void setTabularData( NominationsInProgressListTabularDataViewObject tabularData )
  {
    this.tabularData = tabularData;
  }

  @JsonProperty( "nominationPerPage" )
  public Integer getNominationPerPage()
  {
    return nominationPerPage;
  }

  public void setNominationPerPage( Integer nominationPerPage )
  {
    this.nominationPerPage = nominationPerPage;
  }

  @JsonProperty( "startIndex" )
  public int getStartIndex()
  {
    return startIndex;
  }

  public void setStartIndex( int startIndex )
  {
    this.startIndex = startIndex;
  }

  @JsonProperty( "endIndex" )
  public int getEndIndex()
  {
    return endIndex;
  }

  public void setEndIndex( int endIndex )
  {
    this.endIndex = endIndex;
  }

  @JsonProperty( "sortedBy" )
  public String getSortBy()
  {
    return sortBy;
  }

  public void setSortBy( String sortedBy )
  {
    this.sortBy = sortedBy;
  }

  @JsonProperty( "sortedOn" )
  public String getSortedOnName()
  {
    return NominationClaimsInProgressSortColumn.getByIndexIfNotExistReturnDefaultAsDateStarted( sortedOnIndex ).getDbColumnName();
  }

  @JsonProperty( "sortedOnColumnIndex" )
  public int getSortedOnIndex()
  {
    return sortedOnIndex;
  }

  public void setSortedOnIndex( int sortedOn )
  {
    this.sortedOnIndex = sortedOn;
  }
}
